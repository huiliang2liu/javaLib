package com.xiaohei.java.lib.httpService;


import java.util.Map;

public interface HttpServiceResponse {
    /**
     * get请求
     * @param url
     * @param headers
     * @param params
     * @param session
     * @return
     */
    Response get(Object context, String url, Map<String, String> headers, Map<String, String> params, HttpService.IHTTPSession session);

    /**
     * post请求
     * @param url
     * @param headers
     * @param params
     * @param session
     * @return
     */
    Response post(Object context, String url, Map<String, String> headers, Map<String, String> params, HttpService.IHTTPSession session);

    /**
     * 其他请求
     * @param method
     * @param url
     * @param headers
     * @param params
     * @param session
     * @return
     */
    Response other(Object context, String method, String url, Map<String, String> headers, Map<String, String> params, HttpService.IHTTPSession session);
}
