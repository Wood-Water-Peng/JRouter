package com.example.jrouterapi.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.example.jrouterapi.service.IModuleService;

public class FragmentHelper {
    public static void registerFragmentModule(String moduleName) {
        IModuleFragment moduleFragment = findFragmentModuleByName(moduleName);
        if (moduleFragment != null) {
            FragmentCenter.addModuleFragment(moduleFragment);
        }
    }

    private static IModuleFragment findFragmentModuleByName(String moduleName) {
        try {
            Class<IModuleFragment> aClass = (Class<IModuleFragment>) Class.forName("com.example.jrouter.fragment.JRouter$$Fragment$$" + moduleName);
            return aClass.getConstructor().newInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {

        }
        return null;
    }

    public static Fragment getFragment(String path, Bundle bundle) {
        Class<? extends Fragment> aClass = FragmentCenter.fragmentMap.get(path);
        if (aClass == null) {
            throw new IllegalStateException("can not find fragment with path->" + path);
        }
        try {
            Fragment fragment = aClass.newInstance();
            if (bundle != null) {
                fragment.setArguments(bundle);
            }
            return fragment;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }
}
