package com.example.jrouterapi;

import java.util.Map;

/**
 * @Author jacky.peng
 * @Date 2021/5/18 10:46 AM
 * @Version 1.0
 * <p>
 * /home/homeActivity  --> JPostcard
 */
public interface IRouteMap {
    //这里的map是由仓库传进来，辅助类负责将路由信息写入到map中
    void loadInto(Map<String, JPostcard> map);
}
