package com.example.jrouterapi.service;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author jacky.peng
 * @Date 2021/5/26 5:11 PM
 * @Version 1.0
 */
public class ServiceCenter {
    //所有模块中的服务，服务名称全项目唯一
    static Map<String, Object> services = new HashMap<>();

    public static void addService(String name, Object service) {
        services.put(name, service);
    }

    public static Object getService(String serviceName){
        return services.get(serviceName);
    }
}
