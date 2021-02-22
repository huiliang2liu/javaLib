package com.xiaohei.java.lib.thread.os;

import java.lang.reflect.Modifier;

public class Handler {
    /*
     * Set this flag to true to detect anonymous, local or member classes
     * that extend this Handler class and that are not static. These kind
     * of classes can potentially create leaks.
     */
    private static final boolean FIND_POTENTIAL_LEAKS = false;
    private static final String TAG = "Handler";
    private static Handler MAIN_THREAD_HANDLER = null;

    /**
     * Callback interface you can use when instantiating a Handler to avoid
     * having to implement your own subclass of Handler.
     */
    public interface Callback {

        boolean handleMessage( Message msg);
    }

    /**
     * Subclasses must implement this to receive messages.
     */
    public void handleMessage(Message msg) {
    }

    /**
     * Handle system messages here.
     */
    public void dispatchMessage(Message msg) {
        if (msg.callback != null) {
            handleCallback(msg);
        } else {
            if (mCallback != null) {
                if (mCallback.handleMessage(msg)) {
                    return;
                }
            }
            handleMessage(msg);
        }
    }

    public Handler() {
        this(null, false);
    }


    public Handler( Callback callback) {
        this(callback, false);
    }

    public Handler(Looper looper) {
        this(looper, null, false);
    }

    public Handler( Looper looper, Callback callback) {
        this(looper, callback, false);
    }

    public Handler(boolean async) {
        this(null, async);
    }

    public Handler( Callback callback, boolean async) {
        if (FIND_POTENTIAL_LEAKS) {
            final Class<? extends Handler> klass = getClass();
            if ((klass.isAnonymousClass() || klass.isMemberClass() || klass.isLocalClass()) &&
                    (klass.getModifiers() & Modifier.STATIC) == 0) {
                System.out.println("The following Handler class should be static or leaks might occur: " +
                        klass.getCanonicalName());
            }
        }

        mLooper = Looper.myLooper();
        if (mLooper == null) {
            throw new RuntimeException(
                    "Can't create handler inside thread " + Thread.currentThread()
                            + " that has not called Looper.prepare()");
        }
        mQueue = mLooper.mQueue;
        mCallback = callback;
        mAsynchronous = async;
    }


    public Handler( Looper looper, Callback callback, boolean async) {
        mLooper = looper;
        mQueue = looper.mQueue;
        mCallback = callback;
        mAsynchronous = async;
    }


    public static Handler createAsync(Looper looper) {
        if (looper == null) throw new NullPointerException("looper must not be null");
        return new Handler(looper, null, true);
    }


    public static Handler createAsync(Looper looper,  Callback callback) {
        if (looper == null) throw new NullPointerException("looper must not be null");
        if (callback == null) throw new NullPointerException("callback must not be null");
        return new Handler(looper, callback, true);
    }


    public static Handler getMain() {
        if (MAIN_THREAD_HANDLER == null) {
            MAIN_THREAD_HANDLER = new Handler(Looper.getMainLooper());
        }
        return MAIN_THREAD_HANDLER;
    }


    public static Handler mainIfNull( Handler handler) {
        return handler == null ? getMain() : handler;
    }


    public String getTraceName(Message message) {
        final StringBuilder sb = new StringBuilder();
        sb.append(getClass().getName()).append(": ");
        if (message.callback != null) {
            sb.append(message.callback.getClass().getName());
        } else {
            sb.append("#").append(message.what);
        }
        return sb.toString();
    }

    public String getMessageName(Message message) {
        if (message.callback != null) {
            return message.callback.getClass().getName();
        }
        return "0x" + Integer.toHexString(message.what);
    }


    public final Message obtainMessage()
    {
        return Message.obtain(this);
    }


    public final Message obtainMessage(int what)
    {
        return Message.obtain(this, what);
    }


    public final Message obtainMessage(int what, Object obj) {
        return Message.obtain(this, what, obj);
    }


    public final Message obtainMessage(int what, int arg1, int arg2)
    {
        return Message.obtain(this, what, arg1, arg2);
    }


    public final Message obtainMessage(int what, int arg1, int arg2, Object obj) {
        return Message.obtain(this, what, arg1, arg2, obj);
    }


    public final boolean post( Runnable r) {
        return  sendMessageDelayed(getPostMessage(r), 0);
    }


    public final boolean postAtTime( Runnable r, long uptimeMillis) {
        return sendMessageAtTime(getPostMessage(r), uptimeMillis);
    }


    public final boolean postAtTime(
            Runnable r, Object token, long uptimeMillis) {
        return sendMessageAtTime(getPostMessage(r, token), uptimeMillis);
    }


    public final boolean postDelayed(Runnable r, long delayMillis) {
        return sendMessageDelayed(getPostMessage(r), delayMillis);
    }

    /** @hide */
    public final boolean postDelayed(Runnable r, int what, long delayMillis) {
        return sendMessageDelayed(getPostMessage(r).setWhat(what), delayMillis);
    }


    public final boolean postDelayed(
             Runnable r,  Object token, long delayMillis) {
        return sendMessageDelayed(getPostMessage(r, token), delayMillis);
    }


    public final boolean postAtFrontOfQueue( Runnable r) {
        return sendMessageAtFrontOfQueue(getPostMessage(r));
    }


    public final boolean runWithScissors( Runnable r, long timeout) {
        if (r == null) {
            throw new IllegalArgumentException("runnable must not be null");
        }
        if (timeout < 0) {
            throw new IllegalArgumentException("timeout must be non-negative");
        }

        if (Looper.myLooper() == mLooper) {
            r.run();
            return true;
        }

        BlockingRunnable br = new BlockingRunnable(r);
        return br.postAndWait(this, timeout);
    }


    public final void removeCallbacks( Runnable r) {
        mQueue.removeMessages(this, r, null);
    }


    public final void removeCallbacks( Runnable r,  Object token) {
        mQueue.removeMessages(this, r, token);
    }


    public final boolean sendMessage( Message msg) {
        return sendMessageDelayed(msg, 0);
    }


    public final boolean sendEmptyMessage(int what)
    {
        return sendEmptyMessageDelayed(what, 0);
    }


    public final boolean sendEmptyMessageDelayed(int what, long delayMillis) {
        Message msg = Message.obtain();
        msg.what = what;
        return sendMessageDelayed(msg, delayMillis);
    }



    public final boolean sendEmptyMessageAtTime(int what, long uptimeMillis) {
        Message msg = Message.obtain();
        msg.what = what;
        return sendMessageAtTime(msg, uptimeMillis);
    }


    public final boolean sendMessageDelayed( Message msg, long delayMillis) {
        if (delayMillis < 0) {
            delayMillis = 0;
        }
        return sendMessageAtTime(msg, System.currentTimeMillis() + delayMillis);
    }


    public boolean sendMessageAtTime( Message msg, long uptimeMillis) {
        MessageQueue queue = mQueue;
        if (queue == null) {
            RuntimeException e = new RuntimeException(
                    this + " sendMessageAtTime() called with no mQueue");
            System.out.println( e.getMessage());
            return false;
        }
        return enqueueMessage(queue, msg, uptimeMillis);
    }


    public final boolean sendMessageAtFrontOfQueue( Message msg) {
        MessageQueue queue = mQueue;
        if (queue == null) {
            RuntimeException e = new RuntimeException(
                    this + " sendMessageAtTime() called with no mQueue");
            e.printStackTrace();
            return false;
        }
        return enqueueMessage(queue, msg, 0);
    }


    public final boolean executeOrSendMessage( Message msg) {
        if (mLooper == Looper.myLooper()) {
            dispatchMessage(msg);
            return true;
        }
        return sendMessage(msg);
    }

    private boolean enqueueMessage( MessageQueue queue,  Message msg,
                                   long uptimeMillis) {
        msg.target = this;

        if (mAsynchronous) {
            msg.setAsynchronous(true);
        }
        return queue.enqueueMessage(msg, uptimeMillis);
    }


    public final void removeMessages(int what) {
        mQueue.removeMessages(this, what, null);
    }


    public final void removeMessages(int what,  Object object) {
        mQueue.removeMessages(this, what, object);
    }


    public final void removeCallbacksAndMessages( Object token) {
        mQueue.removeCallbacksAndMessages(this, token);
    }


    public final boolean hasMessages(int what) {
        return mQueue.hasMessages(this, what, null);
    }


    public final boolean hasMessagesOrCallbacks() {
        return mQueue.hasMessages(this);
    }


    public final boolean hasMessages(int what,  Object object) {
        return mQueue.hasMessages(this, what, object);
    }


    public final boolean hasCallbacks( Runnable r) {
        return mQueue.hasMessages(this, r, null);
    }


    public final Looper getLooper() {
        return mLooper;
    }

    public final void dump(String prefix) {
        if (mLooper == null) {
            System.out.println(prefix + "looper uninitialized");
        } else {
            mLooper.dump( prefix + "  ");
        }
    }

    /**
     * @hide
     */
    public final void dumpMine( String prefix) {
        System.out.println(prefix + this + " @ " + System.currentTimeMillis());
        if (mLooper == null) {
            System.out.println(prefix + "looper uninitialized");
        } else {
            mLooper.dump( prefix + "  ", this);
        }
    }

    @Override
    public String toString() {
        return "Handler (" + getClass().getName() + ") {"
                + Integer.toHexString(System.identityHashCode(this))
                + "}";
    }





    private static Message getPostMessage(Runnable r) {
        Message m = Message.obtain();
        m.callback = r;
        return m;
    }

    private static Message getPostMessage(Runnable r, Object token) {
        Message m = Message.obtain();
        m.obj = token;
        m.callback = r;
        return m;
    }

    private static void handleCallback(Message message) {
        message.callback.run();
    }

    final Looper mLooper;
    final MessageQueue mQueue;
    final Callback mCallback;
    final boolean mAsynchronous;

    private static final class BlockingRunnable implements Runnable {
        private final Runnable mTask;
        private boolean mDone;

        public BlockingRunnable(Runnable task) {
            mTask = task;
        }

        @Override
        public void run() {
            try {
                mTask.run();
            } finally {
                synchronized (this) {
                    mDone = true;
                    notifyAll();
                }
            }
        }

        public boolean postAndWait(Handler handler, long timeout) {
            if (!handler.post(this)) {
                return false;
            }

            synchronized (this) {
                if (timeout > 0) {
                    final long expirationTime = System.currentTimeMillis() + timeout;
                    while (!mDone) {
                        long delay = expirationTime - System.currentTimeMillis();
                        if (delay <= 0) {
                            return false; // timeout
                        }
                        try {
                            wait(delay);
                        } catch (InterruptedException ex) {
                        }
                    }
                } else {
                    while (!mDone) {
                        try {
                            wait();
                        } catch (InterruptedException ex) {
                        }
                    }
                }
            }
            return true;
        }
    }
}

