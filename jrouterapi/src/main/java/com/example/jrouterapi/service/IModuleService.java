package com.example.jrouterapi.service;

import java.util.Map;

/**
 * @Author jacky.peng
 * @Date 2021/5/27 8:55 AM
 * @Version 1.0
 */
public interface IModuleService {
    //返回模块中的所有的服务
    Map<String, Object> services();
}
