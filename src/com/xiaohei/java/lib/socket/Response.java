package com.xiaohei.java.lib.socket;

import java.io.InputStream;
import java.io.OutputStream;

public class Response {
    public int code;
    protected String error;
    public InputStream inputStream;
    public Header responseHeader;
    public int len;
}
