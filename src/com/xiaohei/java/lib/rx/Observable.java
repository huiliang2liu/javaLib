package com.xiaohei.java.lib.rx;

public class Observable<T> {
    private static final boolean DEBUG = true;
    OnSubscribe<T> onSubscribe;

    Observable(OnSubscribe<T> onSubscribe) {
        this.onSubscribe = onSubscribe;
    }

    public static <R> Observable<R> from(R... r) {
        return new Observable<>(new OnSubscribe<>(r));
    }

    public <R> Observable<R> map(final MapFunction<T, R> function) {
        return new Observable<R>(new OnSubscribe<R>(null) {
            @Override
            public void subscribe(SubscribeFunction<R> function1) {
//                function1.call(function.call(onSubscribe.r));
            }
        });
    }

//    public Observable<T> filter(final FilterFunction<T> function) {
//        return new Observable<T>(new OnSubscribe<T>(null) {
//            @Override
//            public void subscribe(SubscribeFunction<T> function1) {
//                if (function.call(onSubscribe.r))
//                    super.subscribe(function1);
//                else {
//                    if (DEBUG)
//                        System.out.println("过滤" + onSubscribe.r);
//                }
//
//            }
//        });
//    }

    public void subscribe(SubscribeFunction<T> function) {
        onSubscribe.subscribe(function);
    }


    private static class OnSubscribe<R> {
        R[] rs;

        OnSubscribe(R... r) {
            this.rs = r;
        }

        public void subscribe(SubscribeFunction<R> function) {
            if (rs != null && rs.length > 0)
                for (R r : rs)
                    function.call(r);
        }
    }
}
