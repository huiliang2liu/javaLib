package com.xiaohei.java.lib.http.down;

import java.io.File;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public interface DownFile {

    public Down down(String url, int threads, File file);

    public Down down(String url, int threads, File file, DownListener listener);

    public Down down(String url, File file);

    public Down down(String url, File file, DownListener listener);


    class Build {

        public DownFile build() {
            return (DownFile) Proxy.newProxyInstance(DownFile.class.getClassLoader(), new Class[]{DownFile.class}, new InvocationHandler() {
                DownFile downFile = new DownFileImpl();

                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    return method.invoke(downFile, args);
                }
            });
        }
    }
}
