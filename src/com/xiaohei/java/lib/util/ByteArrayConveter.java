package com.xiaohei.java.lib.util;

public class ByteArrayConveter {

    public static byte[] char2byteArrayBigEndian(char c){
        byte[] buf=new byte[2];
        buf[0]=(byte)((c>>8)&0xff);
        buf[1]=(byte)(c&0xff);
        return buf;
    }

    public static byte[] char2byteArrayLittleEndian(char c){
        byte[] buf=new byte[2];
        buf[0]=(byte)(c&0xff);
        buf[1]=(byte)((c>>8)&0xff);
        return buf;
    }

    public static char byteArray2charBigEndian(byte[] buf){
        return (char)((buf[0]&0xff)<<8|(buf[1]&0xff));
    }

    public static char byteArray2charLittleEndian(byte[] buf){
        return (char)((buf[1]&0xff)<<8|(buf[0]&0xff));
    }

    public static byte[] short2byteArrayBigEndian(short c){
        return char2byteArrayBigEndian((char)c);
    }

    public static byte[] short2byteArrayLittleEndian(short c){
        return char2byteArrayLittleEndian((char)c);
    }

    public static short byteArray2shortBigEndian(byte[] buf){
        return (short)byteArray2charBigEndian(buf);
    }

    public static short byteArray2shortLittleEndian(byte[] buf){
        return (short)byteArray2charLittleEndian(buf);
    }

    public static byte[] int2byteArrayBigEndian(int c){
        byte[] buf=new byte[4];
        buf[0]=(byte)((c>>24)&0xff);
        buf[1]=(byte)((c>>16)&0xff);
        buf[2]=(byte)((c>>8)&0xff);
        buf[3]=(byte)(c&0xff);
        return buf;
    }

    public static byte[] int2byteArrayLittleEndian(int c){
        byte[] buf=new byte[4];
        buf[0]=(byte)(c&0xff);
        buf[1]=(byte)((c>>8)&0xff);
        buf[2]=(byte)((c>>16)&0xff);
        buf[3]=(byte)((c>>24)&0xff);
        return buf;
    }

    public static int byteArray2intBigEndian(byte[] buf){
        return (buf[0]&0xff)<<24|(buf[1]&0xff)<<16|(buf[2]&0xff)<<8|(buf[3]&0xff);
    }

    public static int byteArray2intLittleEndian(byte[] buf){
        return (buf[3]&0xff)<<24|(buf[2]&0xff)<<16|(buf[1]&0xff)<<8|(buf[0]&0xff);
    }

    public static byte[] long2byteArrayBigEndian(long c){
        byte[] buf=new byte[8];
        buf[0]=(byte)((c>>56)&0xff);
        buf[1]=(byte)((c>>48)&0xff);
        buf[2]=(byte)((c>>40)&0xff);
        buf[3]=(byte)((c>>32)&0xff);
        buf[4]=(byte)((c>>24)&0xff);
        buf[5]=(byte)((c>>16)&0xff);
        buf[6]=(byte)((c>>8)&0xff);
        buf[7]=(byte)(c&0xff);
        return buf;
    }

    public static byte[] long2byteArrayLittleEndian(long c){
        byte[] buf=new byte[8];
        buf[0]=(byte)(c&0xff);
        buf[1]=(byte)((c>>8)&0xff);
        buf[2]=(byte)((c>>16)&0xff);
        buf[3]=(byte)((c>>24)&0xff);
        buf[4]=(byte)((c>>32)&0xff);
        buf[5]=(byte)((c>>40)&0xff);
        buf[6]=(byte)((c>>48)&0xff);
        buf[7]=(byte)((c>>56)&0xff);
        return buf;
    }

    public static long byteArray2longBigEndian(byte[] buf){
        return (long) (buf[0]&0xff)<<56|(long) (buf[1]&0xff)<<48|(long) (buf[2]&0xff)<<40|(long) (buf[3]&0xff)<<32|
                (long) (buf[4]&0xff)<<24|(buf[5]&0xff)<<16|(buf[6]&0xff)<<8|(buf[7]&0xff);
    }

    public static long byteArray2longLittleEndian(byte[] buf){
        return (long) (buf[7]&0xff)<<56|(long) (buf[6]&0xff)<<48|(long) (buf[5]&0xff)<<40|(buf[4]&0xff)<<32|
                (long) (buf[3]&0xff)<<24|(buf[2]&0xff)<<16|(buf[1]&0xff)<<8|(buf[0]&0xff);
    }

    public static byte[] float2byteArrayBigEndian(float c){
        return int2byteArrayBigEndian(Float.floatToIntBits(c));
    }

    public static byte[] float2byteArrayLittleEndian(float c){
        return int2byteArrayLittleEndian(Float.floatToIntBits(c));
    }

    public static float byteArray2floatBigEndian(byte[] buf){
        return Float.intBitsToFloat(byteArray2intBigEndian(buf));
    }

    public static float byteArray2floatLittleEndian(byte[] buf){
        return Float.intBitsToFloat(byteArray2intLittleEndian(buf));
    }

    public static byte[] double2byteArrayBigEndian(double c){
        return long2byteArrayBigEndian(Double.doubleToLongBits(c));
    }

    public static byte[] double2byteArrayLittleEndian(double c){
        return long2byteArrayLittleEndian(Double.doubleToLongBits(c));
    }

    public static double byteArray2doubleBigEndian(byte[] buf){
        return Double.longBitsToDouble(byteArray2longBigEndian(buf));
    }

    public static double byteArray2doubleLittleEndian(byte[] buf){
        return Double.longBitsToDouble(byteArray2longLittleEndian(buf));
    }
    public static void main(String[] args){
        double a = Double.MAX_VALUE;
        System.out.println(byteArray2doubleBigEndian(double2byteArrayBigEndian(a)));
        System.out.println(byteArray2doubleLittleEndian(double2byteArrayLittleEndian(a)));
    }
}
