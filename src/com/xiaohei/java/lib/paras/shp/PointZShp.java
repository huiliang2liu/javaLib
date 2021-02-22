package com.xiaohei.java.lib.paras.shp;

import com.xiaohei.java.lib.util.ByteArrayConveter;

import java.io.InputStream;

public class PointZShp extends PointShp{
    double z;
    double m;
    @Override
    public void init(InputStream is) {
        super.init(is);
        byte[] buf=new byte[8];
        read(is,buf);
        z= ByteArrayConveter.byteArray2doubleLittleEndian(buf);
        read(is,buf);
        m= ByteArrayConveter.byteArray2doubleLittleEndian(buf);
    }
}
