package com.example.home_module.callback;

public interface DataProgressCallback<T> {

    void onProgress(float total, float cur);

    void onSuccess(T data);

    void onFailed(String msg);
}
