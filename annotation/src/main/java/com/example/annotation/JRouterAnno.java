package com.example.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author jacky.peng
 * @Date 2021/5/17 8:37 PM
 * @Version 1.0
 * <p>
 * 1.该注解不需要被写入Class文件，只用于在编译时标记类，避免用户瞎搞
 * 2.该注解只能被作用于类
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.SOURCE)
public @interface JRouterAnno {
    String path();
}
