package com.xiaohei.java.lib.paras.shp;

import com.xiaohei.java.lib.util.ByteArrayConveter;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStream;

public class Shx extends Abs {
    private int code;//文件编号
    private int len;//文件长度，包括文件头
    private int ver;
    private int type;//图形类型
    private BoxShp box;
    private double minZ;
    private double maxZ;
    private double minM;
    private double maxM;

    public Shx(InputStream is) {
        if (is == null)
            throw new NullPointerException("InputStream is null");
        DataInputStream dis = new DataInputStream(is);
        init(dis);
    }

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
        int index=0;
        while (true) {
            buf = new byte[4];
            if (!read(is, buf))
                break;
            index++;
            int Offset = ByteArrayConveter.byteArray2intBigEndian(buf);//表示坐标文件中的对应记录的起始位置相对于坐标文件起始位置的位移量
            buf = new byte[4];
            read(is, buf);
            int ContentLength = ByteArrayConveter.byteArray2intBigEndian(buf);//表示坐标文件中的对应记录的长度
            System.out.println("Offset:"+Offset+",ContentLength:"+ContentLength);
        }
        System.out.println(index);
    }

    public static void main(String[] args) {
        try {
            Shx shpRead = new Shx(new FileInputStream("/Users/liuhuiliang/Downloads/CHN_adm/CHN_adm1.shx"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
