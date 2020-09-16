package com.xiaohei.java.lib.thread;

public class ThreadConstant {
    public static final int CPU_COUNT = Runtime.getRuntime()
            .availableProcessors();
    public static final int CPRE_POOL_SIZE = CPU_COUNT;
    public static final int MAXINUM_POOL_XIZE = (CPU_COUNT << 1);
    public static final long SECOND = 1000;
    public static final long MINUTE = 60 * SECOND;
    public static final long HOUR = 60 * MINUTE;
}
