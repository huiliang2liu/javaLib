package com.xiaohei.java.lib.thread.pool;

import com.xiaohei.java.lib.thread.ThreadConstant;

import java.util.Collection;
import java.util.concurrent.*;

public class IOThreadPool extends ThreadPoolExecutor {
    private final static String TAG = "CPUThreadPool";
    private ExecutionHandler handler;
    private final static int QUEUE_SIZE = 128;
    private static final float USE_RATIO=0.5f;

    public IOThreadPool() {
        this(USE_RATIO);
    }

    public IOThreadPool(float useRatio) {
        this((int) (ThreadConstant.CPRE_POOL_SIZE/useRatio),0);
    }
    private IOThreadPool(int threadNum,int a){
        super(threadNum, threadNum<<1, 0L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), new Factory());
        handler = new ExecutionHandler();
        setRejectedExecutionHandler(handler);
    }


    @Override
    protected synchronized void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);
        BlockingQueue<Runnable> queue = getQueue();
        int size = QUEUE_SIZE - queue.size();
        if (size > 0 && size < QUEUE_SIZE) {
            submit(handler.copy(size));
        }
    }


    private void submit(Collection<Runnable> collection) {
        if (collection == null || collection.size() <= 0)
            return;
        for (Runnable runnable : collection) {
            submit(runnable);
        }
    }
}
