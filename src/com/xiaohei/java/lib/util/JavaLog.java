package com.xiaohei.java.lib.util;

public class JavaLog {
    private static boolean isDebug = true;
    private static final String LOG_FORMAT="tag:%s,msg:%s";

    public static void setDebug(boolean isDebug) {
        JavaLog.isDebug = isDebug;
    }

    public static void e(String tag, Object msg) {
        e(tag, String.valueOf(msg));
    }

    public static void e(String tag, String msg) {
        if (isDebug)
            System.err.println(String.format(LOG_FORMAT, tag, msg));
    }

    public static void d(String tag, Object msg) {
        d(tag, String.valueOf(msg));
    }

    public static void d(String tag, String msg) {
        if (isDebug)
            System.out.println(String.format(LOG_FORMAT, tag, msg));
    }
}