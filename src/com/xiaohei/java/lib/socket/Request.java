package com.xiaohei.java.lib.socket;

import java.io.InputStream;

public class Request {
    public static enum Method {
        GET("GET"), POST("POST");
        private String value;

        private Method(String value) {
            this.value = value;
        }
    }

    protected String method = "GET";

    protected String path;
    protected Header header;
    protected int connectTimeout = 5000;
    protected int readTimeout = 5000;
    protected InputStream inputStream;


    public Request(String url) {
        if (url == null || url.isEmpty())
            throw new RuntimeException("you url is empty");
        this.path = url;
        header = new Header();
        header.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        header.addHeader("Cache-Control", "max-age=0");
        header.addHeader("User-Agent", System.getProperty("http.agent", "okhttp/3.0"));
        header.addHeader("Connection", "keep-alive");
        header.addHeader("Accept-Language", "zh-CN,zh;q=0.9");
        header.addHeader("Upgrade-Insecure-Requests", "1");
    }

    public Request setMethod(Method method) {
        this.method = method.value;
        return this;
    }

    public Request post(InputStream is,int contentLength,String contentType){
        setMethod(Method.POST);
        header.addHeader("Content-Length",String.valueOf(contentLength));
        header.addHeader("Content-Type",contentType);
        this.inputStream=is;
        return this;
    }


    public Request addHeader(String key, String value) {
        header.addHeader(key, value);
        return this;
    }

    public Request setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }

    public Request setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
        return this;
    }
}
