package com.xiaohei.java.lib.http;


import com.xiaohei.java.lib.http.cache.Cache;
import com.xiaohei.java.lib.http.listen.ProgressListen;
import com.xiaohei.java.lib.http.request.Request;
import com.xiaohei.java.lib.http.response.Response;
import com.xiaohei.java.lib.http.task.ATask;
import com.xiaohei.java.lib.http.task.InputStreamTask;

import java.io.File;


public class HttpManage {
    private Cache cache;

    public HttpManage() {

    }

    public HttpManage setCache(Cache cache) {
        this.cache = cache;
        return this;
    }

    public Response response(Request request) {
        Cache requestCache = request.getCache();
        return response(request, requestCache == null ? cache : requestCache);
    }

    public Response response(Request request, Cache cache) {
        request.setCache(cache);
        return response(new InputStreamTask(), request);
    }


    public Down down(String path, int threadSize, File saveFile,
                     ProgressListen listen) {
        Down down = new Down(path, threadSize, saveFile);
        down.setListen(listen);
        down.connection();
        return down;
    }

    private static Response response(ATask task, Request request) {
        task.setRequest(request);
        return task.connection();
    }


}
