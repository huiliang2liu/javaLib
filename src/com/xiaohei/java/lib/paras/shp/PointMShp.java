package com.xiaohei.java.lib.paras.shp;

import com.xiaohei.java.lib.util.ByteArrayConveter;

import java.io.InputStream;

public class PointMShp extends PointShp{
    double m;
    @Override
    public void init(InputStream is) {
        super.init(is);
        byte[] buf=new byte[8];
        read(is,buf);
        m= ByteArrayConveter.byteArray2charLittleEndian(buf);
    }
}
