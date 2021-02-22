package com.xiaohei.java.lib.paras.shp;

import java.io.IOException;
import java.io.InputStream;

public abstract class Abs {
    protected final static boolean DEBUG=false;

    public abstract void init(InputStream is);

    protected final boolean read(InputStream is, byte buf[]) {
        int len = 0;
        try {
            len = is.read(buf);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (len != buf.length){
//            System.out.println(len+" "+buf.length);
            return false;
        }
        return true;
    }

    protected final int read(InputStream is) {
        try {
            return is.read();
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }
}
