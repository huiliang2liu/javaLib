package com.xiaohei.java.lib.thread.os;

public class LooperThread extends Thread {
    Looper looper;
    private Handler handler;
   public LooperThread(){
       super("LooperThread"+System.currentTimeMillis());
   }
    @Override
    public void run() {
        Looper.prepare();
        synchronized (this) {
            looper = Looper.myLooper();
            notifyAll();
        }
        Looper.loop();
    }

    public Looper getLooper() {
        if (!isAlive()) {
            return null;
        }
        // If the thread has been started, wait until the looper has been created.
        synchronized (this) {
            while (isAlive() && looper == null) {
                try {
                    wait();
                } catch (InterruptedException e) {
                }
            }
        }
        return looper;
    }

    public Handler getThreadHandler() {
        if (handler == null) {
            handler = new Handler(getLooper());
        }
        return handler;
    }
    public boolean quit() {
        Looper looper = getLooper();
        if (looper != null) {
            looper.quit();
            return true;
        }
        return false;
    }
    public boolean quitSafely() {
        Looper looper = getLooper();
        if (looper != null) {
            looper.quitSafely();
            return true;
        }
        return false;
    }


}
