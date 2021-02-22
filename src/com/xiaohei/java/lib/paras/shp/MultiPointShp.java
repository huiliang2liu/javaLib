package com.xiaohei.java.lib.paras.shp;

import com.xiaohei.java.lib.util.ByteArrayConveter;

import java.io.InputStream;

public class MultiPointShp extends Abs {
    BoxShp boxShp;
    int pointLen;
    PointShp[] points;
    @Override
    public void init(InputStream is) {
        boxShp=new BoxShp();
        boxShp.init(is);
        byte[] buf=new byte[4];
        read(is,buf);
        pointLen= ByteArrayConveter.byteArray2intLittleEndian(buf);
        points=new PointShp[pointLen];
        for (int i=0;i<pointLen;i++){
            PointShp pointShp=new PointShp();
            pointShp.init(is);
            points[i]=pointShp;
        }
    }
}
