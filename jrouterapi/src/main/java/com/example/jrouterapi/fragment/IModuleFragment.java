package com.example.jrouterapi.fragment;

import androidx.fragment.app.Fragment;

import java.util.Map;

public interface IModuleFragment {
    //返回每个模块中的所有fragment
    Map<String, Class<? extends Fragment>> fragments();
}
