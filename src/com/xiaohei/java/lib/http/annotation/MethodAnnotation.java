package com.xiaohei.java.lib.http.annotation;

import com.xiaohei.java.lib.http.util.Method;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MethodAnnotation{
    Method method() default Method.GET;
    String path();
}
