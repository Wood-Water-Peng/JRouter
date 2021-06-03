package com.example.anno_processer;

import com.example.annotation.JModuleAnno;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

import static com.example.anno_processer.Constants.CLASS_NAME_MODULE_PREFIX;
import static com.example.anno_processer.Constants.MODULE_ASSIST_CLASS_FULL_PTAH;
import static com.example.anno_processer.Constants.PACKAGE_NAME_MODULE;
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
            Set<? extends Element> elementsAnnotatedWith = roundEnvironment.getElementsAnnotatedWith(JModuleAnno.class);
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
    //       void onCreated(Context context){
    //           module.onCreated(context);
    //           //加载模块中的服务
    //           JInterceptorHelper.addInterceptorModule("home_module");
    //           JFragmentHelper.registerServices("home_module");
    //           JFragmentHelper.registerFragmentModule("home_module");
    //       }
    //
    //      @override
    //      void onDestroyed(){
    //           //卸载模块中的服务
    //
    //
    //      }
    //  }

    private MethodSpec generateOnDestroyMethod(){
        MethodSpec.Builder destroyMethodBuilder = MethodSpec.methodBuilder("onDestroy")
                .addAnnotation(Override.class)
                .addModifiers(PUBLIC);
        destroyMethodBuilder.addStatement("$T.removeModuleInterceptor($S)", elementUtils.getTypeElement(Constants.INTERCEPTOR_HELPER), moduleName);
        destroyMethodBuilder.addStatement("this.module.onDestroy()");
        return destroyMethodBuilder.build();
    }

    private String parseModule(Element element) throws IOException {
        //成员变量

        //构造方法
        MethodSpec.Builder builder = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addStatement("this.module = new $T()", element.asType());
        MethodSpec constructMethod = builder.build();

        //onCreated(Application context)方法

        ParameterSpec parameterSpec = ParameterSpec.builder(ClassName.get(elementUtils.getTypeElement(Constants.CONTEXT)), "context")
                .build();

        MethodSpec.Builder onCreatedMethodBuilder = MethodSpec.methodBuilder("onCreated")
                .addAnnotation(Override.class)
                .addParameter(parameterSpec)
                .addModifiers(PUBLIC);

        onCreatedMethodBuilder.addStatement("this.module.onCreated(context)");
        onCreatedMethodBuilder.addStatement("$T.addModuleInterceptor($S)", elementUtils.getTypeElement(Constants.INTERCEPTOR_HELPER), moduleName);
        onCreatedMethodBuilder.addStatement("$T.registerServices($S)", elementUtils.getTypeElement(Constants.MODULE_SERVICE_HELPER), moduleName);
        onCreatedMethodBuilder.addStatement("$T.registerFragmentModule($S)", elementUtils.getTypeElement(Constants.MODULE_FRAGMENT_HELPER), moduleName);

        //拦截器辅助类
        String routeMapFileName = CLASS_NAME_MODULE_PREFIX + moduleName;
        //包名
        String packageName = PACKAGE_NAME_MODULE;
        JavaFile.builder(packageName,
                TypeSpec.classBuilder(routeMapFileName)
                        .addSuperinterface(ClassName.get(elementUtils.getTypeElement(MODULE_ASSIST_CLASS_FULL_PTAH)))
                        .addModifiers(PUBLIC)
                        .addMethod(onCreatedMethodBuilder.build())
                        .addMethod(generateOnDestroyMethod())
                        .addMethod(constructMethod)
                        .addField(FieldSpec.builder(ClassName.get(elementUtils.getTypeElement(MODULE_ASSIST_CLASS_FULL_PTAH)), "module", Modifier.PRIVATE).build())
                        .build()).build().writeTo(mFiler);
        return routeMapFileName;
    }


    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new HashSet<>();
        types.add(JModuleAnno.class.getCanonicalName());
        return types;
    }
}
