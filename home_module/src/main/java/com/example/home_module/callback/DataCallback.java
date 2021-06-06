package com.example.home_module.callback;

public interface DataCallback<T> {
    void onSuccess(T data);

    void onFailed(String msg);
}
