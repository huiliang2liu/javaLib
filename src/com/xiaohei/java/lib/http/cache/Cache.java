package com.xiaohei.java.lib.http.cache;

import com.xiaohei.java.lib.http.response.Response;

public interface Cache {
    Response getCache(String url);

    void cache(Response response, String url);
}
