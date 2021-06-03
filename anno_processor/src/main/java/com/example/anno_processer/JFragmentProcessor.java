package com.example.anno_processer;

import com.example.annotation.JFragAnno;
import com.example.annotation.JServiceAnno;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.WildcardTypeName;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import static com.example.anno_processer.Constants.CLASS_FRAGMENT_NAME_PREFIX;
import static com.example.anno_processer.Constants.CLASS_SERVICE_NAME_PREFIX;
import static com.example.anno_processer.Constants.IModuleFragmentInterface;
import static com.example.anno_processer.Constants.IModuleServiceInterface;
import static com.example.anno_processer.Constants.PACKAGE_NAME_FRAGMENT_MODULE_PREFIX;
import static com.example.anno_processer.Constants.PACKAGE_NAME_SERVICE_MODULE_PREFIX;
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
public class JFragmentProcessor extends BaseProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        if (set != null && !set.isEmpty()) {
            Set<? extends Element> elementsAnnotatedWith = roundEnvironment.getElementsAnnotatedWith(JFragAnno.class);
            try {
                buildFragments(elementsAnnotatedWith);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }


    //  模块辅助类结构
// public class  JRouter$$Fragment$$home_module implements IFragmentModule{
//       @override
//        Map<String, Class<? extends Fragment>> fragments(){
//        Map<String,Class<? extends Fragment>> map=new HashMap();
//        map.put("/home/homeFragment",new HomeServiceImpl());
//        return map;
//       }
//  }


    /**
     * 在生成辅助类之前先写出生成类的结构
     * 然后按照倒叙的方法生成一个类：
     * 1.方法
     * 2.类
     * 3.包
     **/

    private String buildFragments(Set<? extends Element> elementsAnnotatedWith) throws IOException {
        //fragments方法

        ParameterizedTypeName fragmentClass = ParameterizedTypeName.get(
                ClassName.get(Class.class),
                WildcardTypeName.subtypeOf(ClassName.get(elementUtils.getTypeElement(Constants.FRAGMENT))));

        //生成一个泛型类型
        ParameterizedTypeName inputMapTypeOfService = ParameterizedTypeName.get(
                ClassName.get(Map.class),
                ClassName.get(String.class),
                fragmentClass);
        // 生成 fragments方法 方法
        MethodSpec.Builder fragmentMethodBuilder = MethodSpec.methodBuilder("fragments")
                .addAnnotation(Override.class)
                .addModifiers(PUBLIC)
                .returns(inputMapTypeOfService);

        fragmentMethodBuilder.addStatement("$T<$T, $T> map = new $T()",
                Map.class,
                String.class,
                fragmentClass,
                HashMap.class);

        for (Element element : elementsAnnotatedWith) {
            //填充方法内容
            JFragAnno annotation = element.getAnnotation(JFragAnno.class);
            fragmentMethodBuilder.addStatement("map.put($S, $T.class)", annotation.path(), TypeName.get(element.asType()));
        }
        fragmentMethodBuilder.addStatement("return map");

        //服务辅助类
        String routeMapFileName = CLASS_FRAGMENT_NAME_PREFIX + moduleName;
        //包名
        String packageName = PACKAGE_NAME_FRAGMENT_MODULE_PREFIX;
        JavaFile.builder(packageName,
                TypeSpec.classBuilder(routeMapFileName)
                        .addSuperinterface(ClassName.get(elementUtils.getTypeElement(IModuleFragmentInterface)))
                        .addModifiers(PUBLIC)
                        .addMethod(fragmentMethodBuilder.build())
                        .build()).build().writeTo(mFiler);
        return routeMapFileName;
    }


    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new HashSet<>();
        types.add(JFragAnno.class.getCanonicalName());
        return types;
    }
}
