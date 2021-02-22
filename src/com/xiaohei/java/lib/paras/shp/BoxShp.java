package com.xiaohei.java.lib.paras.shp;

import com.xiaohei.java.lib.util.ByteArrayConveter;

import java.io.InputStream;

public class BoxShp extends Abs {
    private double left;
    private double top;
    private double right;
    private double bottom;
    @Override
    public void init(InputStream is) {
        byte[] buf=new byte[8];
        read(is,buf);
        left= ByteArrayConveter.byteArray2longLittleEndian(buf);
        read(is,buf);
        top= ByteArrayConveter.byteArray2longLittleEndian(buf);
        read(is,buf);
        right= ByteArrayConveter.byteArray2longLittleEndian(buf);
        read(is,buf);
        bottom= ByteArrayConveter.byteArray2longLittleEndian(buf);
        if(DEBUG)
            System.out.println("left:"+left+",top:"+top+",right:"+right+",bottom:"+bottom);
    }
}
