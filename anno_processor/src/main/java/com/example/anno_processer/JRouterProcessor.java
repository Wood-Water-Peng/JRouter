package com.example.anno_processer;

import com.example.annotation.JRouter;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

import static com.example.anno_processer.Constants.CLASS_NAME_ROUTER_MAP;
import static com.example.anno_processer.Constants.CLASS_NAME_ROUTER_MODULE;
import static com.example.anno_processer.Constants.CLASS_ROUTE_MAP_NAME_PREFIX;
import static com.example.anno_processer.Constants.CLASS_ROUTE_MODULE_NAME_PREFIX;
import static com.example.anno_processer.Constants.PACKAGE_NAME_PREFIX;
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
public class JRouterProcessor extends BaseProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        if (set != null && !set.isEmpty()) {
            logger.info("process begin...");
            System.out.println("process begin...");
            Set<? extends Element> elementsAnnotatedWith = roundEnvironment.getElementsAnnotatedWith(JRouter.class);
            try {
                String routeMapClassName = parseRoutes(elementsAnnotatedWith);

                parseRouteModule(routeMapClassName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        logger.info("process finished...");
        return false;
    }


    //  模块辅助类结构
// public class  JRouter$$RouterModule$$home_module implements IRouteModule{
//      @override
//      public void loadRouteModule(Map<String, IRouteMap> routeModule){
//          map.put("home",new JRouter$$RouterMap$$home_module());
//
//      }
// }

    //生成路由模块辅助类
    private void parseRouteModule(String moduleClassName) throws IOException {
        //方法
        //生成一个泛型类型
        TypeMirror typeIRouteMap = elementUtils.getTypeElement(Constants.IRouteMapInterface).asType();
        ParameterizedTypeName inputMapTypeOfRoute = ParameterizedTypeName.get(
                ClassName.get(Map.class),
                ClassName.get(String.class),
                ClassName.get(typeIRouteMap));

        ParameterSpec mapParamSpec = ParameterSpec.builder(inputMapTypeOfRoute, "routeModule").build();
        // 生成 loadModule 方法
        MethodSpec.Builder loadIntoMethodBuilder = MethodSpec.methodBuilder("loadRouteModule")
                .addAnnotation(Override.class)
                .addModifiers(PUBLIC)
                .addParameter(mapParamSpec);
        loadIntoMethodBuilder.addStatement("routeModule.put($S, new $L())", moduleName, moduleClassName);
        //模块辅助类
        String routeMapFileName = CLASS_ROUTE_MODULE_NAME_PREFIX + moduleName;
        //包名
        String packageName = PACKAGE_NAME_PREFIX + "." + moduleName;
        JavaFile.builder(packageName,
                TypeSpec.classBuilder(routeMapFileName)
                        .addSuperinterface(ClassName.get(elementUtils.getTypeElement(CLASS_NAME_ROUTER_MODULE)))
                        .addModifiers(PUBLIC)
                        .addMethod(loadIntoMethodBuilder.build())
                        .build()).build().writeTo(mFiler);
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
     *                              1.这里必须定义一个接口，并定义添加路由的方法，否则我们只能以Object来定义辅助类
     *                              2.辅助类只在编译时存在，所以他的数据必须被导出来
     */
    //  路由辅助类结构
// public class  JRouter$$RouterMap$$home_module implements IRouteMap{
//      @override
//      public void loadInto(Map<String, JPostcard> map){
//          map.put("/home/homeActivity",new JPostcard("home","/home/homeActivity",HomeActivity.class););
//
//      }
// }
    private String parseRoutes(Set<? extends Element> elementsAnnotatedWith) throws IOException {
        TypeMirror typeJPostCard = elementUtils.getTypeElement(Constants.JPostcard).asType();
        //方法
        //生成一个泛型类型
        ParameterizedTypeName inputMapTypeOfRoute = ParameterizedTypeName.get(
                ClassName.get(Map.class),
                ClassName.get(String.class),
                ClassName.get(typeJPostCard));
        //生成方法参数map，注意这里不能用字符串，因为他是一个入参
        ParameterSpec mapParamSpec = ParameterSpec.builder(inputMapTypeOfRoute, "map").build();

        // 生成 loadInto 方法
        MethodSpec.Builder loadIntoMethodBuilder = MethodSpec.methodBuilder("loadInto")
                .addAnnotation(Override.class)
                .addModifiers(PUBLIC)
                .addParameter(mapParamSpec);
        for (Element element : elementsAnnotatedWith) {
            //填充方法内容
            JRouter annotation = element.getAnnotation(JRouter.class);
            logger.info("parsing target " + TypeName.get(element.asType()));
            loadIntoMethodBuilder.addStatement("map.put($S, new $L($S, $S, $T.class))", annotation.path(), TypeName.get(typeJPostCard), Util.parseGroup(annotation.path()), annotation.path(), TypeName.get(element.asType()));
        }
        //路由辅助类
        String routeMapFileName = CLASS_ROUTE_MAP_NAME_PREFIX + moduleName;
        //包名
        String packageName = PACKAGE_NAME_PREFIX + "." + moduleName;
        JavaFile.builder(packageName,
                TypeSpec.classBuilder(routeMapFileName)
                        .addSuperinterface(ClassName.get(elementUtils.getTypeElement(CLASS_NAME_ROUTER_MAP)))
                        .addModifiers(PUBLIC)
                        .addMethod(loadIntoMethodBuilder.build())
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
