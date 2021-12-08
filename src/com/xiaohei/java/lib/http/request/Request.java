package com.xiaohei.java.lib.http.request;


import com.xiaohei.java.lib.http.cache.Cache;
import com.xiaohei.java.lib.http.util.Method;
import com.xiaohei.java.lib.util.MachinUtil;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;


public class Request {
    protected Method method;
    protected Map<String, String> head;
    private int connectTime = 5 * 1000;// 超时设置
    private int readTimeOut = 5 * 1000;// 读取超时
    protected Map<String, String> params;
    private String path;
    private boolean isRedirect = false;
    private boolean isCaches = false;
    private String charset = "UTF-8";// 设置编码
    private SSLSocketFactory factory;
    private HostnameVerifier verifier;
    private boolean down = false;
    private boolean getHead = false;
    private boolean useCahce = true;
    private Cache cache;

    public Cache getCache() {
        return cache;
    }

    public void setCache(Cache cache) {
        this.cache = cache;
    }

    public boolean isGetHead() {
        return getHead;
    }

    public void setGetHead(boolean getHead) {
        this.getHead = getHead;
    }

    public boolean isDown() {
        return down;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    static {
        defaultHostnameVerifier();
    }

    {
        params = new HashMap<String, String>();
        head = new HashMap<String, String>();
        head.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        head.put("Accept-Charset", "utf-8");
        head.put("Accetpt-Encoding", "deflate");
        head.put("Accept-Language", "zh-cn");
        head.put("Cache-Control", "no-cache");
        head.put("Connection", "keep-Alive");
        head.put(
                "User-Agent",
                MachinUtil.getUserAgent());
        head.put("Content-Type", "application/x-www-form-urlencoded");
        head.put("Charset", "utf-8");
        method = Method.GET;
    }


    /**
     * lhl 2017-11-25 下午3:38:48 说明：设置默认是否忽略证书
     * <p>
     * void
     */
    private static void defaultHostnameVerifier() {
        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {

            @Override
            public boolean verify(String arg0, SSLSession arg1) {
                // TODO Auto-generated method stub
                return true;
            }
        });
    }

    public Method getMethod() {
        return method;
    }

    public Request setMethod(Method method) {
        this.method = method;
        return this;
    }

    public Map<String, String> getHead() {
        return head;
    }

    public Request addHead(Map<String, String> head) {
        if (head != null && head.size() > 0)
            this.head.putAll(head);
        return this;
    }

    public boolean isUseCahce() {
        return useCahce;
    }

    public Request addHead(String key, String value) {
        head.put(key, value);
        return this;
    }

    public Request noCache() {
        useCahce = false;
        return this;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public Request addParams(Map<String, String> params) {
        if (params != null && params.size() > 0)
            this.params.putAll(params);
        return this;
    }

    public Request addParams(String key, String value) {
        params.put(key, value);
        return this;
    }

    public String getPath() {
        if (method == Method.GET) {
            String params = params2string();
            if (params != null) {
                StringBuffer stringBuffer = new StringBuffer(path);
                if (path.indexOf("?") > 1)
                    stringBuffer.append("&");
                else
                    stringBuffer.append("?");
                stringBuffer.append(params);
                return stringBuffer.toString();
            }
        }
        return path;
    }

    public String params2string() {
        if (params != null && params.size() > 0) {
            StringBuffer stringBuffer = new StringBuffer();
            Iterator<Entry<String, String>> iterator = params.entrySet()
                    .iterator();
            while (iterator.hasNext()) {
                Entry<String, String> entry = iterator.next();
                Object object = entry.getValue();
                String value = object == null ? "" : object.toString();
                stringBuffer.append(entry.getKey()).append("=");
                try {
                    stringBuffer.append(URLEncoder.encode(value, charset));
                } catch (UnsupportedEncodingException e) {
                    // TODO Auto-generated catch block
                    stringBuffer.append(value);
                }
                stringBuffer.append("&");
            }
            return stringBuffer.substring(0, stringBuffer.length() - 1);
        }
        return null;
    }

    public SSLSocketFactory getFactory() {
        return factory;
    }

    public void setFactory(SSLSocketFactory factory) {
        this.factory = factory;
    }

    public HostnameVerifier getVerifier() {
        return verifier;
    }

    public void setVerifier(HostnameVerifier verifier) {
        this.verifier = verifier;
    }

    public Request setPath(String path) {
        this.path = path;
        return this;
    }

    public int getConnectTime() {
        return connectTime;
    }

    public Request setConnectTime(int connectTime) {
        this.connectTime = connectTime;
        return this;
    }

    public int getReadTimeOut() {
        return readTimeOut;
    }

    public Request setReadTimeOut(int readTimeOut) {
        this.readTimeOut = readTimeOut;
        return this;
    }

    public boolean isRedirect() {
        return isRedirect;
    }

    public Request setRedirect(boolean isRedirect) {
        this.isRedirect = isRedirect;
        return this;
    }

    public String getCharset() {
        return charset;
    }

    public Request setCharset(String charset) {
        this.charset = charset;
        head.put("Charset", charset);
        return this;
    }

    public Request setAccept(String accept) {
        head.put("Accept", accept);
        return this;
    }

    public Request setAcceptCharset(String charset) {
        head.put("Accept-Charset", charset);
        return this;
    }

    public Request setAcceptEncod(String encod) {
        head.put("Accetpt-Encoding", encod);
        return this;
    }

    public Request setAcceptLanguage(String language) {
        head.put("Accept-Language", language);
        return this;
    }

    public Request setCacheControl(String cacheControl) {
        head.put("Cache-Control", cacheControl);
        return this;
    }

    public Request setConnection(String connection) {
        head.put("Connection", connection);
        return this;
    }

    public Request setUserAgent(String userAgent) {
        head.put("User-Agent", userAgent);
        return this;
    }

    public Request setContentType(String type) {
        head.put("Content-Type", type);
        return this;
    }

    public Request setCookie(String cookie) {
        head.put("Cookie", cookie);
        return this;
    }

    public Request setContentLength(String contentLength) {
        head.put("Content-Length", contentLength);
        return this;
    }

    public Request setReferer(String referer) {
        head.put("Referer", referer);
        return this;
    }

    public Request setRange(String range) {
        head.put("Range", range);
        return this;
    }

    public Request putHead(String key, String value) {
        head.put(key, value);
        return this;
    }

    public boolean isCaches() {
        return isCaches;
    }

    public void setCaches(boolean isCaches) {
        this.isCaches = isCaches;
    }

    public void report(OutputStream os) {
        String params = params2string();
//        System.out.println(params);
        if (params == null)
            return;
        try {
            os.write(params.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
