package com.example.anno_processer;

/**
 * @Author jacky.peng
 * @Date 2021/5/17 9:00 PM
 * @Version 1.0
 */
public class Constants {
    public static final String PROJECT_NAME = "JRouter";
    public static final String CLASS_NAME_DIVIDER = "$$";
    public static final String PACKAGE_NAME = "com.example.jrouter";
    public static final String CONTEXT = "android.content.Context";

    //对应着各module在build.gradle中生成的注解处理器相关字段
    public static final String KEY_MODULE_NAME = "JROUTER_MODULE_NAME";


    public static final String JPostcard = "com.example.jrouterapi.JPostcard";
    public static final String JRouterModuleInterceptor = "com.example.jrouterapi.interceptor.IModuleInterceptor";
    public static final String JRouterInterceptor = "com.example.jrouterapi.interceptor.IRouteInterceptor";
    public static final String IRouteMapInterface = "com.example.jrouterapi.IRouteMap";
    public static final String IModuleServiceInterface = "com.example.jrouterapi.service.IModuleService";

    /**
     * 模块相关
     */
    //IRouterMap接口名称
    public static final String PACKAGE_NAME_MODULE = PACKAGE_NAME + ".module";
    public static final String CLASS_NAME_MODULE_PREFIX = PROJECT_NAME + CLASS_NAME_DIVIDER + "Module" + CLASS_NAME_DIVIDER;
    public static final String MODULE_ASSIST_CLASS_FULL_PTAH = "com.example.jrouterapi.module.IModuleInterface";

    /**
     * 路由相关
     */
    //JRouter$$RouteMap$$
    public static final String CLASS_ROUTE_MAP_NAME_PREFIX = PROJECT_NAME + CLASS_NAME_DIVIDER + "RouterMap" + CLASS_NAME_DIVIDER;
    //路由模块辅助类的包名
    public static final String PACKAGE_NAME_ROUTE_MODULE_PREFIX = PACKAGE_NAME;
    //IRouterMap接口名称
    public static final String CLASS_NAME_ROUTER_MAP = "com.example.jrouterapi.IRouteMap";
    //IRouterModule接口名称
    public static final String CLASS_NAME_ROUTER_MODULE = "com.example.jrouterapi.IRouteModule";

    //模块辅助类
    public static final String CLASS_ROUTE_MODULE_NAME_PREFIX = PROJECT_NAME + CLASS_NAME_DIVIDER + "RouterModule" + CLASS_NAME_DIVIDER;


    /**
     * 拦截器
     */
    //帮助类
    public static final String INTERCEPTOR_HELPER = "com.example.jrouterapi.interceptor.JInterceptorHelper";
    //拦截器辅助类包名
    public static final String PACKAGE_NAME_INTERCEPTOR_MODULE_PREFIX = PACKAGE_NAME + ".interceptor_modules";

    //拦截器辅助类类名
    public static final String CLASS_INTERCEPTOR_NAME_PREFIX = PROJECT_NAME + CLASS_NAME_DIVIDER + "Interceptor" + CLASS_NAME_DIVIDER;


    /**
     * 服务
     */

    public static final String MODULE_SERVICE_HELPER = "com.example.jrouterapi.service.ServiceHelper";

    //服务辅助类包名
    public static final String PACKAGE_NAME_SERVICE_MODULE_PREFIX = PACKAGE_NAME + ".service_modules";

    //服务辅助类类名
    public static final String CLASS_SERVICE_NAME_PREFIX = PROJECT_NAME + CLASS_NAME_DIVIDER + "Service" + CLASS_NAME_DIVIDER;

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
