package com.xiaohei.java.lib.paras.shp;


import com.xiaohei.java.lib.util.ByteArrayConveter;

import java.io.InputStream;

public class PointShp extends Abs {
    double x;
    double y;

    @Override
    public void init(InputStream is) {
        byte[] buf = new byte[8];
        read(is, buf);
        x = ByteArrayConveter.byteArray2doubleLittleEndian(buf);
        read(is, buf);
        y = ByteArrayConveter.byteArray2doubleLittleEndian(buf);
        if (DEBUG)
            System.out.println("x:" + x + ",y:" + y);
    }
}
