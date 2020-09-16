package com.xiaohei.java.lib.thread.pool;

import com.xiaohei.java.lib.util.JavaLog;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

public class ExecutionHandler implements RejectedExecutionHandler {
    private final static String TAG = "ExecutionHandler";
    List<Runnable> runnables = new ArrayList<>();

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        synchronized (this) {
            JavaLog.e(TAG, "任务过多溢出，保存在队列中，用于下次使用");
            runnables.add(r);
        }
    }

    protected List<Runnable> copy(int size) {
        if (size <= 0 || size > runnables.size())
            size = runnables.size();
        if (size <= 0)
            return null;
        JavaLog.e(TAG, "获取溢出的任务再次执行");
        List<Runnable> copys = new ArrayList<>(size);
        synchronized (this) {
            copys.addAll(runnables);
            runnables.removeAll(copys);
        }
        return copys;
    }

    protected void remove(Runnable runnable) {
        synchronized (this) {
            runnables.remove(runnable);
        }
    }
}