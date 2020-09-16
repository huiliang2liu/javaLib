package com.xiaohei.java.lib.http.cache;

import com.xiaohei.java.lib.http.response.Response;

import java.util.HashMap;
import java.util.Map;

public class SoftCache implements Cache {
    private static volatile Map<String, Response> cache = new HashMap<>();

    @Override
    public Response getCache(String url) {
        String key = bytes2hex(url.getBytes());
        if (cache.containsKey(key))
            return cache.get(key);
        return null;
    }

    @Override
    public void cache(Response response, String url) {
        String key = bytes2hex(url.getBytes());
        cache.put(key, response);
    }

    private static final char[] hexArray = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f'};

    private static String bytes2hex(byte[] bytes) {
        int len = bytes.length;
        char[] hexChars = new char[len << 1];
        for (int i = 0; i < len; i++) {
            int index = i << 1;
            int v = bytes[i] & 0xff;
            hexChars[index] = hexArray[v >>> 4];
            hexChars[index + 1] = hexArray[v & 0xf];
        }
        return new String(hexChars);
    }
}
