package com.xiaohei.java.lib.httpService;


import java.util.Map;

public class DefaultResponse implements HttpServiceResponse {
    @Override
    public Response get(Object context, String url, Map<String, String> headers, Map<String, String> params, HttpService.IHTTPSession session) {
        String msg = "<html><body><h1>Hello server</h1>\n";
        msg += "<p>We serve " + url + " !</p>";
        return HttpService.newFixedLengthResponse(msg + "</body></html>\n");
    }

    @Override
    public Response post(Object context, String url, Map<String, String> headers, Map<String, String> params, HttpService.IHTTPSession session) {
        return null;
    }

    @Override
    public Response other(Object context, String method, String url, Map<String, String> headers, Map<String, String> params, HttpService.IHTTPSession session) {
        return null;
    }
}
