package com.example.anno_processer;

import com.example.annotation.Interceptor;
import com.example.annotation.JModule;
import com.example.annotation.JRouter;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

import static com.example.anno_processer.Constants.CLASS_INTERCEPTOR_NAME_PREFIX;
import static com.example.anno_processer.Constants.JRouterInterceptor;
import static com.example.anno_processer.Constants.JRouterModuleInterceptor;
import static com.example.anno_processer.Constants.MODULE_ASSIST_CLASS_FULL_PTAH;
import static com.example.anno_processer.Constants.PACKAGE_NAME_INTERCEPTOR_MODULE_PREFIX;
import static javax.lang.model.element.Modifier.PUBLIC;

/**
 * @Author jacky.peng
 * @Date 2021/5/17 8:52 PM
 * @Version 1.0
 */
//注册JRouterProcessor这个注解处理器
//1.生成一个模块辅助类，提供给RouterWarehouse使用
//2.生成一个路由辅助类，用于加载模块内部的所有路由信息
@AutoService(Processor.class)
public class JModuleProcessor extends BaseProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        if (set != null && !set.isEmpty()) {
            Set<? extends Element> elementsAnnotatedWith = roundEnvironment.getElementsAnnotatedWith(JModule.class);
            try {
                if (elementsAnnotatedWith.size() > 1) {
                    throw new IllegalArgumentException("Module annotation can only have one!");
                }
                parseModule(elementsAnnotatedWith.iterator().next());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }


    //  模块辅助类结构
// public class  JRouter$$Module$$home_module implements IModuleInterface{
    //      IModuleInterface module;
    //      public void JRouter$$Module$$home_module(){
    //          this.module=new HomeModule();
    //      }
    //
    //       @override
    //       void onCreated(Application context){
    //           module.onCreated(context);
    //           //加载模块中的服务
    //           JInterceptorHelper.addInterceptorModule("home_module");
    //       }
    //
    //      @override
    //      void onDestroyed(){
    //           //卸载模块中的服务
    //
    //
    //      }
    //  }


    private String parseModule(Element element) throws IOException {
        TypeMirror typeRouterInterceptor = elementUtils.getTypeElement(MODULE_ASSIST_CLASS_FULL_PTAH).asType();

        //成员变量

        //构造方法
        MethodSpec.Builder builder = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addStatement("this.module = new $T()", element.asType());
        MethodSpec constructMethod = builder.build();

        //onCreated(Application context)方法

        ParameterSpec parameterSpec = ParameterSpec.builder(ClassName.get(elementUtils.getTypeElement(Constants.APPLICATION)), "application")
                .build();

        MethodSpec.Builder onCreatedMethodBuilder = MethodSpec.methodBuilder("onCreated")
                .addAnnotation(Override.class)
                .addParameter(parameterSpec)
                .addModifiers(PUBLIC);

        onCreatedMethodBuilder.addStatement("$T<$T, $T> map = new $T()",
                Map.class,
                String.class,
                typeRouterInterceptor,
                HashMap.class);


        //拦截器辅助类
        String routeMapFileName = CLASS_INTERCEPTOR_NAME_PREFIX + moduleName;
        //包名
        String packageName = PACKAGE_NAME_INTERCEPTOR_MODULE_PREFIX;
        JavaFile.builder(packageName,
                TypeSpec.classBuilder(routeMapFileName)
                        .addSuperinterface(ClassName.get(elementUtils.getTypeElement(JRouterModuleInterceptor)))
                        .addModifiers(PUBLIC)
                        .addMethod(onCreatedMethodBuilder.build())
                        .addMethod(constructMethod)
                        .addField(FieldSpec.builder(ClassName.get(elementUtils.getTypeElement(MODULE_ASSIST_CLASS_FULL_PTAH)), "module", Modifier.PRIVATE).build())
                        .build()).build().writeTo(mFiler);
        return routeMapFileName;
    }


    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new HashSet<>();
        types.add(JRouter.class.getCanonicalName());
        return types;
    }
}
