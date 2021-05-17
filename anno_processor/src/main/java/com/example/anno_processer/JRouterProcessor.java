package com.example.anno_processer;

import com.example.annotation.JRouter;
import com.google.auto.service.AutoService;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

/**
 * @Author jacky.peng
 * @Date 2021/5/17 8:52 PM
 * @Version 1.0
 */
//注册JRouterProcessor这个注解处理器
@AutoService(Process.class)
public class JRouterProcessor extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        if (set != null && !set.isEmpty()) {
            Set<? extends Element> elementsAnnotatedWith = roundEnvironment.getElementsAnnotatedWith(JRouter.class);
            parseRoutes(elementsAnnotatedWith);
        }
        return false;
    }

    /**
     * 在生成辅助类之前先写出生成类的结构
     * 然后按照倒叙的方法生成一个类：
     * 1.方法
     * 2.类
     * 3.包
     *
     * @param elementsAnnotatedWith 注解的path结构为    /group名字/类名字     每一个group由模块名决定，全项目唯一
     *                              通过注解处理器，可以拿到被注解的类的所有信息，最后我们希望得到的是
     *                              <p>
     *                              /group/page  ---->XXX.class
     *                              最后用户通过路径跳转到对应的Activity,同时需要注意一个group下面对应着多个页面的映射关系
     *                              Map<String,Class>  routeMap
     *                              Map<GroupName,Map<String,Class>>  groupMap
     *                              <p>
     *                              这里必须定义一个接口，并定义添加路由的方法，否则我们只能以Object来定义辅助类
     */
    private void parseRoutes(Set<? extends Element> elementsAnnotatedWith) {
        if (elementsAnnotatedWith == null || elementsAnnotatedWith.isEmpty()) return;


    }

//  辅助类结构
// public class


    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new HashSet<>();
        types.add(JRouter.class.getCanonicalName());
        return types;
    }
}
