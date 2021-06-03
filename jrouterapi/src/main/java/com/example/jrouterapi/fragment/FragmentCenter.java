package com.example.jrouterapi.fragment;

import androidx.fragment.app.Fragment;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class FragmentCenter {
    final static Map<String, Class<? extends Fragment>> fragmentMap = new HashMap<>();

    public static void addModuleFragment(IModuleFragment moduleFragment) {
        Iterator<Map.Entry<String, Class<? extends Fragment>>> iterator = moduleFragment.fragments().entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Class<? extends Fragment>> entry = iterator.next();
            fragmentMap.put(entry.getKey(), entry.getValue());
        }
    }

    public Map<String, Class<? extends Fragment>> getFragmentMap() {
        return fragmentMap;
    }

    public static void put(String key, Class<? extends Fragment> value) {
        fragmentMap.put(key, value);
    }
}
