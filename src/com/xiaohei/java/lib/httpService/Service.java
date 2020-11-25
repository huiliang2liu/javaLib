package com.xiaohei.java.lib.httpService;


import com.xiaohei.java.lib.util.JavaLog;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;

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

    public static int port() {
        try {
            ServerSocket serverSocket = new ServerSocket(0);
            int port = serverSocket.getLocalPort();
            serverSocket.close();
            return port;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static boolean port(String hostName, int port) {
        try {
            ServerSocket serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress(hostName, port));
            serverSocket.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static boolean port(int port) {
        try {
            new ServerSocket(port).close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取可用的tcp端口号
     *
     * @return
     */
    public static int getAvailableTcpPort() {
        // 指定范围10000到65535
        for (int i = 10000; i <= 65535; i++) {
            try {
                new ServerSocket(i).close();
                return i;
            } catch (IOException e) { // 抛出异常表示不可以，则进行下一个
                e.printStackTrace();
            }
        }
        return -1;
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
