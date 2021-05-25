package com.example.jrouterapi.module;

import android.text.TextUtils;

import com.example.jrouterapi.Utils;
import com.example.jrouterapi.core.JRouter;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author jacky.peng
 * @Date 2021/5/25 9:38 AM
 * @Version 1.0
 */
public class ModuleHelper {
    private static final Map<String, IModuleInterface> modules = new HashMap<>(16);

    public static void registerModule(String moduleName) {
        if (TextUtils.isEmpty(moduleName)) {
            throw new IllegalArgumentException("invalidate moduleName");
        }
        if (modules.get(moduleName) != null) {
            throw new IllegalArgumentException("module " + moduleName + " has registered");
        }

        //找到模块辅助类
        IModuleInterface module = findModuleByName(moduleName);
        if (module == null) {
            throw new IllegalStateException("find not find module " + moduleName);
        }
        modules.put(moduleName, module);
        module.onCreated(JRouter.context());
    }

    public static void unregisterModule(String moduleName) {
        if (TextUtils.isEmpty(moduleName)) {
            throw new IllegalArgumentException("invalidate moduleName");
        }
        if (modules.get(moduleName) == null) {
            throw new IllegalArgumentException("can find " + moduleName);
        }
        IModuleInterface module = modules.remove(moduleName);
        module.onDestroy();
    }

    private static IModuleInterface findModuleByName(String moduleName) {
        try {
            Class<IModuleInterface> aClass = (Class<IModuleInterface>) Class.forName("com.example.jrouter.module.JRouter$$Module$$" + moduleName);
            return  aClass.getConstructor().newInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {

        }
        return null;
    }
}
