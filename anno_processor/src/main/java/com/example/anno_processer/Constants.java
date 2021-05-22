package com.example.anno_processer;

/**
 * @Author jacky.peng
 * @Date 2021/5/17 9:00 PM
 * @Version 1.0
 */
public class Constants {
    public static final String PROJECT_NAME = "JRouter";
    public static final String CLASS_NAME_DIVIDER = "$$";

    //对应着各module在build.gradle中生成的注解处理器相关字段
    public static final String KEY_MODULE_NAME = "JROUTER_MODULE_NAME";


    public static final String JPostcard = "com.example.jrouterapi.JPostcard";
    public static final String IRouteMapInterface = "com.example.jrouterapi.IRouteMap";

    //JRouter$$RouteMap$$
    public static final String CLASS_ROUTE_MAP_NAME_PREFIX = PROJECT_NAME + CLASS_NAME_DIVIDER + "RouterMap" + CLASS_NAME_DIVIDER;
    //生成的辅助类的包名
    public static final String PACKAGE_NAME_PREFIX = "com.example.jrouter";
    //IRouterMap接口名称
    public static final String CLASS_NAME_ROUTER_MAP = "com.example.jrouterapi.IRouteMap";
    //IRouterModule接口名称
    public static final String CLASS_NAME_ROUTER_MODULE = "com.example.jrouterapi.IRouteModule";

    //模块辅助类
    public static final String CLASS_ROUTE_MODULE_NAME_PREFIX = PROJECT_NAME + CLASS_NAME_DIVIDER + "RouterModule" + CLASS_NAME_DIVIDER;

    //logger

    // Log
    static final String PREFIX_OF_LOGGER = PROJECT_NAME + "::Compiler ";
    public static final String NO_MODULE_NAME_TIPS = "These no module name, at 'build.gradle', like :\n" +
            "android {\n" +
            "    defaultConfig {\n" +
            "        ...\n" +
            "        javaCompileOptions {\n" +
            "            annotationProcessorOptions {\n" +
            "                arguments = [JROUTER_MODULE_NAME: project.getName()]\n" +
            "            }\n" +
            "        }\n" +
            "    }\n" +
            "}\n";

}
