package com.xiaohei.java.lib.paras.shp;

import com.xiaohei.java.lib.util.ByteArrayConveter;

import java.io.InputStream;

public class LineShp extends Abs {
    BoxShp box;
    int partLen;
    int pointLen;
    int[] numParts;
    PointShp[] numPoints;

    @Override
    public void init(InputStream is) {
        box = new BoxShp();
        box.init(is);
        byte[] buf = new byte[4];
        read(is, buf);
        partLen = ByteArrayConveter.byteArray2intLittleEndian(buf);
        read(is, buf);
        pointLen = ByteArrayConveter.byteArray2intLittleEndian(buf);
        if (DEBUG)
            System.out.println("partLen:" + partLen + ",pointLen:" + pointLen);
        numParts = new int[partLen];
        for (int i = 0; i < partLen; i++) {
            read(is, buf);
            numParts[i] = ByteArrayConveter.byteArray2intLittleEndian(buf);
            if (DEBUG)
                System.out.println(numParts[i]);
        }
        numPoints = new PointShp[pointLen];
        for (int i = 0; i < pointLen; i++) {
            PointShp p = new PointShp();
            p.init(is);
            numPoints[i] = p;
        }
    }
}
