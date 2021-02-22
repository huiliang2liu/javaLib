package com.xiaohei.java.lib.paras.shp;

import com.xiaohei.java.lib.util.ByteArrayConveter;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Shp extends Abs {
    private int code;//文件编号
    private int len;//文件长度，包括文件头
    private int ver;
    private int type;//图形类型
    private BoxShp box;
    private double minZ;
    private double maxZ;
    private double minM;
    private double maxM;
    private List<Abs> shps = new ArrayList<>();

    @Override
    public void init(InputStream is) {
        byte buf[] = new byte[4];
        read(is, buf);
        code = ByteArrayConveter.byteArray2intBigEndian(buf);
        //五个没有被使用的32位整数
        for (int i = 0; i < 5; i++) {
            read(is, buf);
        }
        read(is, buf);
        len = ByteArrayConveter.byteArray2intBigEndian(buf);
        read(is, buf);
        ver = ByteArrayConveter.byteArray2intLittleEndian(buf);
        read(is, buf);
        type = ByteArrayConveter.byteArray2intLittleEndian(buf);//小端序
        box = new BoxShp();
        box.init(is);
        buf = new byte[8];
        read(is, buf);
        minZ = ByteArrayConveter.byteArray2doubleLittleEndian(buf);
        read(is, buf);
        maxZ = ByteArrayConveter.byteArray2doubleLittleEndian(buf);
        read(is, buf);
        minM = ByteArrayConveter.byteArray2doubleLittleEndian(buf);
        read(is, buf);
        maxM = ByteArrayConveter.byteArray2doubleLittleEndian(buf);
        if (DEBUG)
            System.out.println("code:" + code + ",len:" + len + ",ver:" + ver + ",type:" + type + ",minZ:" + minZ + ",maxZ:" + maxZ + ",minM:" + minM + ",maxM:" + maxM);
        while (true) {
            buf = new byte[4];
            if (!read(is, buf))
                break;
            if (DEBUG)
                System.out.println(ByteArrayConveter.byteArray2intBigEndian(buf));
            read(is, buf);
            int l = ByteArrayConveter.byteArray2intBigEndian(buf);
            if (DEBUG)
                System.out.println(l);
            read(is, buf);
            int type = ByteArrayConveter.byteArray2intLittleEndian(buf);
            if (DEBUG)
                System.out.println(type);
            if (type == 5) {
                PolygonShp lineShp = new PolygonShp();
                lineShp.init(is);
                shps.add(lineShp);
            } else if (type == 3) {
                LineShp lineShp = new LineShp();
                lineShp.init(is);
                shps.add(lineShp);

            } else if (type == 1) {
                PointShp pointShp = new PointShp();
                pointShp.init(is);
                shps.add(pointShp);
            } else
                break;
        }
        System.out.println(shps.size());
    }

    public Shp(InputStream is) {
        if (is == null)
            throw new NullPointerException("InputStream is null");
        DataInputStream dis = new DataInputStream(is);
        init(dis);
    }


    public static void main(String[] args) {
        try {
            Shp shpRead = new Shp(new FileInputStream("/Users/liuhuiliang/Downloads/CHN_adm/CHN_adm0.shp"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
