package com.example.anno_processer;

import com.example.annotation.Interceptor;
import com.example.annotation.JRouter;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
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
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

import static com.example.anno_processer.Constants.CLASS_INTERCEPTOR_NAME_PREFIX;
import static com.example.anno_processer.Constants.JRouterInterceptor;
import static com.example.anno_processer.Constants.JRouterModuleInterceptor;
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
public class JInterceptorProcessor extends BaseProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        if (set != null && !set.isEmpty()) {
            Set<? extends Element> elementsAnnotatedWith = roundEnvironment.getElementsAnnotatedWith(Interceptor.class);
            try {
                String routeMapClassName = parseRoutes(elementsAnnotatedWith);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }


    //  模块辅助类结构
// public class  JRouter$$InterceptorModule$$home_module implements IModuleInterceptor{
//       @override
//        Map<String, IRouteInterceptor> interceptors(){
//        Map<String,IRouteInterceptor> map=new HashMap();
//        map.put("/home/homeActivity",new HomeInterceptor());
    //   }
    //
    //  @override
    //  List<IRouteInterceptor> moduleGlobalInterceptors(){
    //       List<IRouteInterceptor> list=new ArrayList();
    //       list.add(new HomeGlobalInterceptor());
    //       return list;
    //  }
//  }


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
     *                              1.这里必须定义一个接口，并定义添加路由的方法，否则我们只能以Object来定义辅助类
     *                              2.辅助类只在编译时存在，所以他的数据必须被导出来
     */

    private String parseRoutes(Set<? extends Element> elementsAnnotatedWith) throws IOException {
        TypeMirror typeRouterInterceptor = elementUtils.getTypeElement(JRouterInterceptor).asType();
        //interceptors方法
        //生成一个泛型类型
        ParameterizedTypeName inputMapTypeOfRoute = ParameterizedTypeName.get(
                ClassName.get(Map.class),
                ClassName.get(String.class),
                ClassName.get(typeRouterInterceptor));
        // 生成 interceptors 方法
        MethodSpec.Builder loadIntoMethodBuilder = MethodSpec.methodBuilder("interceptors")
                .addAnnotation(Override.class)
                .addModifiers(PUBLIC)
                .returns(inputMapTypeOfRoute);

        loadIntoMethodBuilder.addStatement("$T<$T, $T> map = new $T()",
                Map.class,
                String.class,
                typeRouterInterceptor,
                HashMap.class);

        for (Element element : elementsAnnotatedWith) {
            //填充方法内容
            Interceptor annotation = element.getAnnotation(Interceptor.class);
            if (annotation.path() != null && !annotation.path().equals("")) {
                loadIntoMethodBuilder.addStatement("map.put($S, new $T())", annotation.path(), TypeName.get(element.asType()));
            }
        }
        loadIntoMethodBuilder.addStatement("return map");


        //moduleGlobalInterceptors方法
        //生成一个泛型类型
        ParameterizedTypeName listTypeOfInterceptor = ParameterizedTypeName.get(
                ClassName.get(List.class),
                ClassName.get(typeRouterInterceptor));


        MethodSpec.Builder moduleGlobalInterceptorsBuilder = MethodSpec.methodBuilder("moduleGlobalInterceptors")
                .addAnnotation(Override.class)
                .addModifiers(PUBLIC)
                .returns(listTypeOfInterceptor);
        moduleGlobalInterceptorsBuilder.addStatement("$T<$T> list = new $T()",
                List.class,
                typeRouterInterceptor,
                ArrayList.class);

        for (Element element : elementsAnnotatedWith) {
            //填充方法内容
            Interceptor annotation = element.getAnnotation(Interceptor.class);
            if (annotation.path() == null || annotation.path().equals("")) {
                moduleGlobalInterceptorsBuilder.addStatement("list.add(new $T())", TypeName.get(element.asType()));
            }
        }
        moduleGlobalInterceptorsBuilder.addStatement("return list");

        //拦截器辅助类
        String routeMapFileName = CLASS_INTERCEPTOR_NAME_PREFIX + moduleName;
        //包名
        String packageName = PACKAGE_NAME_INTERCEPTOR_MODULE_PREFIX;
        JavaFile.builder(packageName,
                TypeSpec.classBuilder(routeMapFileName)
                        .addSuperinterface(ClassName.get(elementUtils.getTypeElement(JRouterModuleInterceptor)))
                        .addModifiers(PUBLIC)
                        .addMethod(loadIntoMethodBuilder.build())
                        .addMethod(moduleGlobalInterceptorsBuilder.build())
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
