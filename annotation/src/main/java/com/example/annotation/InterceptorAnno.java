package com.example.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author jacky.peng
 * @Date 2021/5/24 8:58 AM
 * @Version 1.0
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.SOURCE)
//因为该注解只是用于编译器收集注解信息，不需要生成字节码加载到JVM中
public @interface InterceptorAnno {
    //拦截器对应的路径，如果没有设置则认为是全局拦截器
    String path();
}
