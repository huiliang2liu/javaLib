package com.xiaohei.java.lib.thread;


public abstract class Runnable implements java.lang.Runnable {

    public void single() {
        PoolManager.single(this);
    }

    public void singleRemove() {
        PoolManager.singleRemove(this);
    }


    public void shortTime() {
        PoolManager.shortTime(this);
    }

    public void shortTimeRemove() {
        PoolManager.shortTimeRemove(this);
    }

    public void longTime() {
        PoolManager.longTime(this);
    }

    public void longTimeRemove() {
        PoolManager.longTimeRemove(this);
    }

    public void scheduled(long delay, long period) {
        PoolManager.scheduled(this, delay, period);
    }

    public void scheduledRemove() {
        PoolManager.scheduledRemove(this);
    }

    public void scheduled(long delay) {
        scheduled(delay, 0);
    }

    public void scheduledEnd(long delay, long period) {
        PoolManager.scheduledEnd(this, delay, period);
    }

    @Override
    public final void run() {
        try {
            task();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    protected abstract void task();
}
