package com.xiaohei.java.lib.paras.shp;

import java.io.InputStream;

public class DbfData extends Abs {
//    private String name;
//    private Type type;
    private byte[] buf;


    DbfData() {

    }

    public final void setLen(int len){
        buf = new byte[len];
    }

    @Override
    public void init(InputStream is) {
        read(is,buf);
    }

    @Override
    public String toString() {
        return new String(buf);
    }
    //    private enum Type {
//        B("二进制型,各种字符"), C("字符型,各种字符"), D("日期型,用于区分年、月、日的数字和一个字符，内部存储按照 YYYYMMDD 格式"),
//        G("(General or OLE),各种字符"), N("数值型,- . 0 1 2 3 4 5 6 7 8 9"), L("逻辑型,? Y y N n T t F f (?  表示没有初始化 ) "),
//        M("(Memo),各种字符");
//        private String name;
//
//        private Type(String name) {
//            this.name = name;
//        }
//
//        private static Type int2type(int t) {
//            if (t == 'B')
//                return B;
//            if (t == 'C')
//                return C;
//            if (t == 'D')
//                return D;
//            if (t == 'G')
//                return G;
//            if (t == 'N')
//                return N;
//            if (t == 'L')
//                return L;
//            if (t == 'M')
//                return M;
//            return C;
//        }
//
//        @Override
//        public String toString() {
//            return name;
//        }
//    }
}
