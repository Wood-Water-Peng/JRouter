package com.example.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author jacky.peng
 * @Date 2021/5/24 7:53 PM
 * @Version 1.0
 */

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.SOURCE)
//在模块被加载的时候,收集模块中的模块类，加入到核心库的模块仓库中
public @interface JModuleAnno {
}
