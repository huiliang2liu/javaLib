package com.xiaohei.java.lib.paras.shp;

import com.xiaohei.java.lib.util.ByteArrayConveter;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Dbf extends Abs {
    int ver;//版本
    String data;//更新日期
    int num;//记录数量
    short heardLen;//文件头中的字节数。
    short numLen;//一条记录中的字节长度。
    int ecnryptionFlag;//dBASE IV 编密码标记。
    private DbfData heard;
    private List<DbfData> dataList;

    public Dbf() {

    }

    @Override
    public void init(InputStream is) {
        DataInputStream dis = new DataInputStream(is);
        ver = read(dis);
        StringBuilder sb = new StringBuilder();
        sb.append(read(dis) + 1900);
        sb.append("年");
        sb.append(read(dis) - 1);
        sb.append("月");
        sb.append(read(dis));
        sb.append("日");
        data = sb.toString();
        byte[] buf = new byte[4];
        read(dis, buf);
        num = ByteArrayConveter.byteArray2intLittleEndian(buf);//RecordNum
        buf = new byte[2];
        read(dis, buf);
        heardLen = ByteArrayConveter.byteArray2shortLittleEndian(buf);//HeaderByteNum
        read(dis, buf);
        numLen = ByteArrayConveter.byteArray2shortLittleEndian(buf);//RecordByteNum
        read(dis, buf);//保留字节，用于以后添加新的说明性信息时使用，这里用 0 来填写。
//        System.out.println(ByteArrayConveter.byteArray2shortLittleEndian(buf));
        read(dis);//表示未完成的操作
        ecnryptionFlag = read(dis);//表示未完成的操作
        buf = new byte[12];
        read(dis, buf);//保留字节，用于多用户处理时使用。
        read(dis);//DBF 文件的 MDX 标识。在创建一个 DBF  表时，如果使用了 MDX  格式的索引文件，那么  DBF  表的表头中的这个字节就自动被设置了一个标志，当你下次试图重新打开这个 DBF 表的时候，数据引擎会自动识别这个标志，如果此标志为真，则数据引擎将试图打开相应的 MDX  文件。
        read(dis);//Language driver ID
        buf = new byte[2];
        read(dis, buf);//保留字节，用于以后添加新的说明性信息时使用，这里用 0 来填写
        heard=new DbfData();
        heard.setLen(heardLen);
        heard.init(dis);
//        buf = new byte[heardLen];
//        read(dis, buf);
        System.out.println(heard);
        read(dis);//作为记录项终止标识
        dataList = new ArrayList<>(num);
        for (int i = 0; i < num; i++){
            DbfData dbfData=new DbfData();
            dbfData.setLen(numLen);
            dbfData.init(dis);
            dataList.add(dbfData);
            System.out.println(dbfData);
        }
        System.out.println(ver);
        System.out.println(data);
        System.out.println(num);
        System.out.println(heardLen);
        System.out.println(numLen);
        System.out.println(ecnryptionFlag);
    }


    public static void main(String[] args) {
        try {
            Dbf dbf = new Dbf();
            dbf.init(new FileInputStream("/Users/liuhuiliang/Downloads/CHN_adm/CHN_adm0.dbf"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
