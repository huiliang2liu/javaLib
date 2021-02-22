/*
 * Copyright (C) 2006 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xiaohei.java.lib.thread.os;



public final class Looper {
    /*
     * API Implementation Note:
     *
     * This class contains the code required to set up and manage an event loop
     * based on MessageQueue.  APIs that affect the state of the queue should be
     * defined on MessageQueue or Handler rather than on Looper itself.  For example,
     * idle handlers and sync barriers are defined on the queue whereas preparing the
     * thread, looping, and quitting are defined on the looper.
     */

    private static final String TAG = "Looper";


    static final ThreadLocal<Looper> sThreadLocal = new ThreadLocal<Looper>();
    private static Looper sMainLooper;  // guarded by Looper.class
    private static Observer sObserver;

    final MessageQueue mQueue;
    final Thread mThread;

    private long mTraceTag;

    /**
     * If set, the looper will show a warning log if a message dispatch takes longer than this.
     */
    private long mSlowDispatchThresholdMs;

    /**
     * If set, the looper will show a warning log if a message delivery (actual delivery time -
     * post time) takes longer than this.
     */
    private long mSlowDeliveryThresholdMs;

    /** Initialize the current thread as a looper.
     * This gives you a chance to create handlers that then reference
     * this looper, before actually starting the loop. Be sure to call
     * {@link #loop()} after calling this method, and end it by calling
     * {@link #quit()}.
     */
    public static void prepare() {
        prepare(true);
    }

    private static void prepare(boolean quitAllowed) {
        if (sThreadLocal.get() != null) {
            throw new RuntimeException("Only one Looper may be created per thread");
        }
        sThreadLocal.set(new Looper(quitAllowed));
    }

    /**
     * Initialize the current thread as a looper, marking it as an
     * application's main looper. The main looper for your application
     * is created by the Android environment, so you should never need
     * to call this function yourself.  See also: {@link #prepare()}
     */
    public static void prepareMainLooper() {
        prepare(false);
        synchronized (Looper.class) {
            if (sMainLooper != null) {
                throw new IllegalStateException("The main Looper has already been prepared.");
            }
            sMainLooper = myLooper();
        }
    }

    /**
     * Returns the application's main looper, which lives in the main thread of the application.
     */
    public static Looper getMainLooper() {
        synchronized (Looper.class) {
            return sMainLooper;
        }
    }

    /**
     * Set the transaction observer for all Loopers in this process.
     *
     * @hide
     */
    public static void setObserver(Observer observer) {
        sObserver = observer;
    }

    /**
     * Run the message queue in this thread. Be sure to call
     * {@link #quit()} to end the loop.
     */
    public static void loop() {
        final Looper me = myLooper();
        if (me == null) {
            throw new RuntimeException("No Looper; Looper.prepare() wasn't called on this thread.");
        }
        final MessageQueue queue = me.mQueue;
        final int thresholdOverride =0;

        boolean slowDeliveryDetected = false;

        for (;;) {
            Message msg = queue.next(); // might block
            if (msg == null) {
                // No message indicates that the message queue is quitting.
                return;
            }

            // Make sure the observer won't change while processing a transaction.
            final Observer observer = sObserver;

            final long traceTag = me.mTraceTag;
            long slowDispatchThresholdMs = me.mSlowDispatchThresholdMs;
            long slowDeliveryThresholdMs = me.mSlowDeliveryThresholdMs;
            if (thresholdOverride > 0) {
                slowDispatchThresholdMs = thresholdOverride;
                slowDeliveryThresholdMs = thresholdOverride;
            }
            final boolean logSlowDelivery = (slowDeliveryThresholdMs > 0) && (msg.when > 0);
            final boolean logSlowDispatch = (slowDispatchThresholdMs > 0);

            final boolean needStartTime = logSlowDelivery || logSlowDispatch;
            final boolean needEndTime = logSlowDispatch;


            final long dispatchStart = needStartTime ? System.currentTimeMillis() : 0;
            final long dispatchEnd;
            Object token = null;
            if (observer != null) {
                token = observer.messageDispatchStarting();
            }
            try {
                msg.target.dispatchMessage(msg);
                if (observer != null) {
                    observer.messageDispatched(token, msg);
                }
                dispatchEnd = needEndTime ? System.currentTimeMillis() : 0;
            } catch (Exception exception) {
                if (observer != null) {
                    observer.dispatchingThrewException(token, msg, exception);
                }
                throw exception;
            } finally {
            }
            if (logSlowDelivery) {
                if (slowDeliveryDetected) {
                    if ((dispatchStart - msg.when) <= 10) {
                        slowDeliveryDetected = false;
                    }
                } else {
                    if (showSlowLog(slowDeliveryThresholdMs, msg.when, dispatchStart, "delivery",
                            msg)) {
                        // Once we write a slow delivery log, suppress until the queue drains.
                        slowDeliveryDetected = true;
                    }
                }
            }
            if (logSlowDispatch) {
                showSlowLog(slowDispatchThresholdMs, dispatchStart, dispatchEnd, "dispatch", msg);
            }
            msg.recycleUnchecked();
        }
    }

    private static boolean showSlowLog(long threshold, long measureStart, long measureEnd,
                                       String what, Message msg) {
        final long actualTime = measureEnd - measureStart;
        if (actualTime < threshold) {
            return false;
        }
        return true;
    }

    /**
     * Return the Looper object associated with the current thread.  Returns
     * null if the calling thread is not associated with a Looper.
     */
    public static  Looper myLooper() {
        return sThreadLocal.get();
    }

    /**
     * Return the {@link MessageQueue} object associated with the current
     * thread.  This must be called from a thread running a Looper, or a
     * NullPointerException will be thrown.
     */
    public static  MessageQueue myQueue() {
        return myLooper().mQueue;
    }

    private Looper(boolean quitAllowed) {
        mQueue = new MessageQueue(quitAllowed);
        mThread = Thread.currentThread();
    }

    /**
     * Returns true if the current thread is this looper's thread.
     */
    public boolean isCurrentThread() {
        return Thread.currentThread() == mThread;
    }




    public void setTraceTag(long traceTag) {
        mTraceTag = traceTag;
    }

    /**
     * Set a thresholds for slow dispatch/delivery log.
     * {@hide}
     */
    public void setSlowLogThresholdMs(long slowDispatchThresholdMs, long slowDeliveryThresholdMs) {
        mSlowDispatchThresholdMs = slowDispatchThresholdMs;
        mSlowDeliveryThresholdMs = slowDeliveryThresholdMs;
    }

    /**
     * Quits the looper.
     * <p>
     * Causes the {@link #loop} method to terminate without processing any
     * more messages in the message queue.
     * </p><p>
     * Any attempt to post messages to the queue after the looper is asked to quit will fail.
     * For example, the {@link Handler#sendMessage(Message)} method will return false.
     * </p><p class="note">
     * Using this method may be unsafe because some messages may not be delivered
     * before the looper terminates.  Consider using {@link #quitSafely} instead to ensure
     * that all pending work is completed in an orderly manner.
     * </p>
     *
     * @see #quitSafely
     */
    public void quit() {
        mQueue.quit(false);
    }

    /**
     * Quits the looper safely.
     * <p>
     * Causes the {@link #loop} method to terminate as soon as all remaining messages
     * in the message queue that are already due to be delivered have been handled.
     * However pending delayed messages with due times in the future will not be
     * delivered before the loop terminates.
     * </p><p>
     * Any attempt to post messages to the queue after the looper is asked to quit will fail.
     * For example, the {@link Handler#sendMessage(Message)} method will return false.
     * </p>
     */
    public void quitSafely() {
        mQueue.quit(true);
    }


    public  Thread getThread() {
        return mThread;
    }


    public  MessageQueue getQueue() {
        return mQueue;
    }


    public void dump(String prefix) {
        mQueue.dump(prefix + "  ", null);
    }


    public void dump(String prefix, Handler handler) {
        mQueue.dump(prefix + "  ", handler);
    }



    @Override
    public String toString() {
        return "Looper (" + mThread.getName() + ", tid " + mThread.getId()
                + ") {" + Integer.toHexString(System.identityHashCode(this)) + "}";
    }

    /** {@hide} */
    public interface Observer {
        /**
         * Called right before a message is dispatched.
         *
         * <p> The token type is not specified to allow the implementation to specify its own type.
         *
         * @return a token used for collecting telemetry when dispatching a single message.
         *         The token token must be passed back exactly once to either
         *         {@link Observer#messageDispatched} or {@link Observer#dispatchingThrewException}
         *         and must not be reused again.
         *
         */
        Object messageDispatchStarting();

        /**
         * Called when a message was processed by a Handler.
         *
         * @param token Token obtained by previously calling
         *              {@link Observer#messageDispatchStarting} on the same Observer instance.
         * @param msg The message that was dispatched.
         */
        void messageDispatched(Object token, Message msg);

        /**
         * Called when an exception was thrown while processing a message.
         *
         * @param token Token obtained by previously calling
         *              {@link Observer#messageDispatchStarting} on the same Observer instance.
         * @param msg The message that was dispatched and caused an exception.
         * @param exception The exception that was thrown.
         */
        void dispatchingThrewException(Object token, Message msg, Exception exception);
    }
}
