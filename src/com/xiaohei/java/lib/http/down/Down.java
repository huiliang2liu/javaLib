package com.xiaohei.java.lib.http.down;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public interface Down {
    public void down();

    public void pause();

    public boolean isPause();

    public boolean isDown();

    public boolean isEnd();

    public float progress();

}
