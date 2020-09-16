package com.xiaohei.java.lib.http.response;


import com.xiaohei.java.lib.http.request.Request;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Response {
    private int code;
    private String error;
    protected Request request;
    private long len;
    protected Map<String, List<String>> responseHead;
    private InputStream is;
    private String response;
    private String charset;

    public InputStream getInputStream() {
        return is;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getCharset() {
        return charset;
    }

    public String getString() {
        if (response == null || response.isEmpty()) {
            response = stream2string(is, null);
            is = null;
        }
        return response;
    }

    public String getString(String charset) {
        return stream2string(is, charset);
    }

    private String stream2string(InputStream is, String charset) {
        if (is == null)
            return "";
        if (charset == null || charset.isEmpty())
            charset = "utf-8";
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buff = new byte[1024 * 1024];
        byte[] arr = null;
        try {
            int len = is.read(buff);
            while (len > 0) {
                baos.write(buff, 0, len);
                len = is.read(buff);
            }
            arr = baos.toByteArray();
            baos.flush();
            return new String(arr, charset);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                baos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                is.close();
            } catch (Exception e) {
            }
        }
        return "";
    }

    public void setInputStream(InputStream is) {
        this.is = is;
    }

    public void setResponseHead(Map<String, List<String>> responseHead) {
        this.responseHead = responseHead;
    }

    public Map<String, String> heard() {
        if (responseHead == null || responseHead.size() <= 0)
            return Collections.emptyMap();
        Map<String, String> heard = new HashMap<>();
        for (Map.Entry<String, List<String>> entry : responseHead.entrySet()) {
            String key = entry.getKey();
            List<String> list = entry.getValue();
            if (list == null || list.size() <= 0)
                continue;
            heard.put(entry.getKey(), list.get(0));
        }
        return heard;
    }

    public Map<String, List<String>> getResponseHead() {
        return responseHead;
    }

    public long getLen() {
        return len;
    }

    public void setLen(long len) {
        this.len = len;
    }

    public void setRequest(Request request) {
        this.request = request;
        if(request!=null)
            charset=request.getCharset();
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

}
