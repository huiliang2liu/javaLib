package com.xiaohei.java.lib.http.task;


import com.xiaohei.java.lib.http.response.Response;
import com.xiaohei.java.lib.http.util.Method;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;


public class InputStreamTask extends ATask {
    private final static String TAG = "InputStreamTask";

    @Override
    public Response connection() {
        // TODO Auto-generated method stub
        Response response = new Response();
        if (mRequest == null) {
            response.setCode(500);
            response.setError("request is null");
        }
        response.setRequest(mRequest);
        try {
            Method method = mRequest.getMethod();
            String path = mRequest.getPath();
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
            connection.setRequestMethod(method.getMethod());
            connection.setUseCaches(mRequest.isCaches());
            connection.setDoInput(true);
            if (method == Method.GET)
                connection.setDoOutput(false);
            else {
                connection.setDoOutput(true);
            }
            Map<String, String> head = mRequest.getHead();
            if (head != null && head.size() > 0) {
                Iterator<Entry<String, String>> iterator = head.entrySet()
                        .iterator();
                while (iterator.hasNext()) {
                    Entry<String, String> entry = iterator.next();
                    connection.setRequestProperty(entry.getKey(),
                            entry.getValue());
                }
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
//            if (Build.VERSION.SDK_INT >= 24)
//                response.setLen(connection.getContentLengthLong());
//            else
            response.setLen(connection.getContentLength());
            int code = connection.getResponseCode();
            response.setCode(code);
            response.setResponseHead(connection.getHeaderFields());
            if (code < 400) {
//                if (Build.VERSION.SDK_INT >= 24)
//                    response.setLen(connection.getContentLengthLong());
//                else
                response.setLen(connection.getContentLength());
                response.setInputStream(connection.getInputStream());
            } else {
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
                        if (baos != null)
                            baos.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        if (is != null)
                            is.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            response.setCode(500);
            response.setError(e == null ? "network request failed" : e
                    .getMessage());
            e.printStackTrace();
        }
        return response;
    }

    protected void putHead() {

    }
}