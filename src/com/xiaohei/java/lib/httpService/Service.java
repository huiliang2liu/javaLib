package com.xiaohei.java.lib.httpService;


import com.xiaohei.java.lib.util.JavaLog;

import java.io.IOException;

public class Service extends HttpService {
    private static final String TAG = "Service";
    private HttpServiceResponse response;
    private Object context;

    public Service(Object context, HttpServiceResponse response, String host, int port) {
        super(host, port);
        JavaLog.d(TAG, "开启服务");
        this.context = context;
        this.response = response;
        try {
            start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Response serve(IHTTPSession session) {
        if (response != null) {
            String uri = session.getUri();
            String method = session.getMethod().toString();
            JavaLog.d(TAG, String.format("接收到请求：uri=%s,method=%s", uri, method));
            if ("GET".equals(method))
                return response.get(context, uri, session.getHeaders(), session.getParms(), session);
            if ("POST".equals(method))
                return response.post(context, uri, session.getHeaders(), session.getParms(), session);
            return response.other(context, method, uri, session.getHeaders(), session.getParms(), session);
        }
        return super.serve(session);
    }

    @Override
    public void stop() {
        super.stop();
        JavaLog.d(TAG, "停止服务");
    }

}
