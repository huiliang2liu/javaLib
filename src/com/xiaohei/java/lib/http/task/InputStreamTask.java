package com.xiaohei.java.lib.http.task;


import com.xiaohei.java.lib.http.cache.Cache;
import com.xiaohei.java.lib.http.response.Response;
import com.xiaohei.java.lib.http.util.Method;
import com.xiaohei.java.lib.util.JavaLog;
import com.xiaohei.java.lib.util.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;


public class InputStreamTask extends ATask {
    private final static String TAG = "InputStreamTask";

    @Override
    public Response connection() {
        JavaLog.d(TAG, "开始网络请求");
        // TODO Auto-generated method stub
        Response response = new Response();
        if (mRequest == null) {
            response.setCode(500);
            response.setError("request is null");
        }
        if (StringUtils.isEmpty(mRequest.getPath())) {
            response.setCode(500);
            response.setError("request path is empty");
        }
        Method method = mRequest.getMethod();
        String path = mRequest.getPath();
        response.setRequest(mRequest);
        synchronized (path.hashCode() + "InputStreamTask") {
            Cache cache = null;
            Response cacheResponse = null;
            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(path)
                        .openConnection();
                if (path.startsWith("https")) {
                    HttpsURLConnection https = (HttpsURLConnection) connection;
                    HostnameVerifier verifier = mRequest.getVerifier();
                    SSLSocketFactory factory = mRequest.getFactory();
                    if (verifier != null)
                        https.setHostnameVerifier(verifier);
                    if (factory != null)
                        https.setSSLSocketFactory(factory);
                }
                connection.setConnectTimeout(mRequest.getConnectTime());
                connection.setReadTimeout(mRequest.getReadTimeOut());
                connection.setInstanceFollowRedirects(mRequest.isRedirect());
                connection.setInstanceFollowRedirects(true);
                connection.setRequestMethod(method.getMethod());
                connection.setUseCaches(mRequest.isCaches());
                connection.setDoInput(true);
                Map<String, String> head = mRequest.getHead();
                if (method == Method.GET) {
                    connection.setDoOutput(false);
                    if (mRequest.isUseCahce()) {
                        cache = mRequest.getCache();
                        if (cache != null)
                            cacheResponse = cache.getCache(mRequest.getPath());
                    }
                } else {
                    connection.setDoOutput(true);
                }
                if (head != null && head.size() > 0) {
                    Iterator<Entry<String, String>> iterator = head.entrySet()
                            .iterator();
                    while (iterator.hasNext()) {
                        Entry<String, String> entry = iterator.next();
                        connection.setRequestProperty(entry.getKey(),
                                entry.getValue());
                    }
                }
                if (cacheResponse != null) {
                    Map<String, List<String>> heads = cacheResponse.getResponseHead();
                    connection.setRequestProperty("If-Modified-Since", list2string(heads.get("Last-Modified")));
                    connection.setRequestProperty("If-None-Match",
                            list2string(heads.get("ETag")));
                }
                connection.connect();
                if (method == Method.POST) {
                    OutputStream os = connection.getOutputStream();
                    if (os != null) {
                        mRequest.report(os);
                        os.flush();
                        try {
                            os.close();
                        } catch (Exception e) {
                            // TODO: handle exception
                        }
                    }
                }
                int code = connection.getResponseCode();
                response.setCharset(connection.getContentEncoding());
                response.setCode(code);
                response.setResponseHead(connection.getHeaderFields());
                if (code >= 400) {
                    if (cacheResponse != null)
                        return cacheResponse;
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    InputStream is = connection.getErrorStream();
                    byte[] buff = new byte[1024 * 1024];
                    byte[] arr = null;
                    try {
                        int len = is.read(buff);
                        while (len > 0) {
                            baos.write(buff, 0, len);
                            len = is.read(buff);
                        }
                        arr = baos.toByteArray();
                        response.setError(new String(arr, mRequest.getCharset()));
                        baos.flush();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } finally {
                        try {
                            baos.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            is.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    return response;
                }
                if (code >= 300) {
                    JavaLog.d(TAG, String.valueOf(code));
                    if (code == 301 || code == 302 || code == 303 || code == 307) {
                        mRequest.setPath(response.heard().get("Location"));
                        return connection();
                    }
                    if (code == 304) {//获取本地信息
                        return cacheResponse;
                    }

                }

                response.setLen(connection.getContentLength());
                if (!mRequest.isGetHead()) {
                    response.setInputStream(connection.getInputStream());
                    if (!mRequest.isDown()) {
                        if (cache != null) {
                            String cc = response.heard().get("Cache-Control");
                            String p = response.heard().get("Pragma");
                            if ("no-cache".equalsIgnoreCase(cc) || "no-cache".equalsIgnoreCase(p)) {
                                System.out.println("no-cache");
                            } else
                                cache.cache(response, path);
                        }
                    }
                } else
                    connection.getInputStream().close();

            } catch (Exception e) {
                // TODO Auto-generated catch block
                if (cacheResponse != null)
                    response = cacheResponse;
                else {
                    response.setCode(500);
                    response.setError(e == null ? "network request failed" : e
                            .getMessage());
                }
            }
        }
        return response;
    }

    private String list2string(List<String> list) {
        if (list == null || list.size() <= 0)
            return "";
        StringBuilder sb = new StringBuilder();
        for (String s : list)
            sb.append(s).append(",");
        return sb.substring(0, sb.length() - 1);
    }

    protected void putHead() {

    }
}
