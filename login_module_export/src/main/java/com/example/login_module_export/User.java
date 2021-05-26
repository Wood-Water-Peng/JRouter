package com.example.login_module_export;

/**
 * @Author jacky.peng
 * @Date 2021/5/26 3:45 PM
 * @Version 1.0
 * <p>
 * 暴露给其他模块的用户类
 */
public class User {
    String name;
    String token;
    boolean isLogin;

    public User(String name, String token, boolean isLogin) {
        this.name = name;
        this.token = token;
        this.isLogin = isLogin;
    }

    public User(String uid, String token) {
        this.name = name;
        this.token = token;
        isLogin = true;
    }

    public String getName() {
        return name;
    }

    public String getToken() {
        return token;
    }

    public boolean isLogin() {
        return isLogin;
    }
}
