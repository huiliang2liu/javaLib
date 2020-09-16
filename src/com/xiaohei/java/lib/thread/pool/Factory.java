package com.xiaohei.java.lib.thread.pool;

import java.util.concurrent.ThreadFactory;

public class Factory implements ThreadFactory {
    @Override
    public Thread newThread( Runnable r) {
        Thread thread = new Thread(r,"Factory"+System.currentTimeMillis());
        thread.setPriority(Thread.MAX_PRIORITY);
        return thread;
    }
}