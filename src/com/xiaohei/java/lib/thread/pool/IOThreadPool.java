package com.xiaohei.java.lib.thread.pool;

import com.xiaohei.java.lib.thread.ThreadConstant;

import java.util.Collection;
import java.util.concurrent.*;

public class IOThreadPool extends ThreadPoolExecutor {
    private final static int QUEUE_SIZE = 128;
    private static final int MAX_THREAD=ThreadConstant.MAXINUM_POOL_XIZE+1;

    public IOThreadPool() {
        this(MAX_THREAD);
    }

    public IOThreadPool(int  maxThread) {
        this(maxThread,QUEUE_SIZE);
    }
    private IOThreadPool(int threadNum,int queueSize){
        super(threadNum, threadNum, 0L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(queueSize));
    }


    @Override
    protected synchronized void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);
    }
}
