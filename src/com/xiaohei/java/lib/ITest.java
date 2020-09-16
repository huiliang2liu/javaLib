package com.xiaohei.java.lib;

public interface ITest {
    String value(@TestAnnotation(value = "test")String test);
}
