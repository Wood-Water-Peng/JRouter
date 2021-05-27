package com.example.jrouterapi.service;

import java.util.Map;
import java.util.Set;

/**
 * @Author jacky.peng
 * @Date 2021/5/26 5:11 PM
 * @Version 1.0
 */
public class ServiceHelper {
    public static void registerServices(String moduleName) {
        //找到模块中对应的辅助类
        IModuleService moduleService = findModuleByName(moduleName);
        if (moduleService == null) {
            return;
        }

        Set<Map.Entry<String, Object>> entries = moduleService.services().entrySet();
        for (Map.Entry<String, Object> entry :
                entries) {
            ServiceCenter.addService(entry.getKey(), entry.getValue());
        }
    }

    private static IModuleService findModuleByName(String moduleName) {
        try {
            Class<IModuleService> aClass = (Class<IModuleService>) Class.forName("com.example.jrouter.service_modules.JRouter$$Service$$" + moduleName);
            return aClass.getConstructor().newInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {

        }
        return null;
    }
}
