package com.example.jrouter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.DialogTitle;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.home_module.HomeActivity;
import com.example.jrouterapi.JPostcard;
import com.example.jrouterapi.core.JRouter;
import com.example.jrouterapi.interceptor.IRouteInterceptor;
import com.example.jrouterapi.service.ServiceCenter;
import com.example.login_module_export.IUserService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void jump(View v) {
        JPostcard postcard = JRouter.path("/home_module/HomeActivity");
        Bundle bundle = new Bundle();
        bundle.putString("from", getClass().getCanonicalName());
        postcard.withParam(bundle).navigate(this);
    }

    public void jump2UserInfo(View view) {
        JPostcard postcard = JRouter.path("/login_module/UserInfoActivity");
        Bundle bundle = new Bundle();
        bundle.putString("from", getClass().getCanonicalName());
        postcard.withParam(bundle).navigate(this, new IRouteInterceptor.Callback() {
            @Override
            public void onSuccess(@NonNull JPostcard jPostcard) {

            }

            @Override
            public void onFail(@NonNull Throwable exception) {
                Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
                //跳转到登录页面
                JRouter.path("/login_module/LoginActivity").navigate(MainActivity.this);
            }
        });
    }

    //弹窗显示用户信息
    public void showUserInfo(View view) {
        Object obj = ServiceCenter.getService(IUserService.name);
        if (obj == null) {
            Toast.makeText(getApplicationContext(), "can not find service:" + IUserService.name, Toast.LENGTH_SHORT).show();
        }
        if (!(obj instanceof IUserService)) {
            throw new IllegalStateException(obj.toString() + "can not be converted to " + IUserService.class.getCanonicalName());
        }
        IUserService userService = (IUserService) obj;
        if (userService.getUser() == null) {
            Toast.makeText(getApplicationContext(), "用户未登录", Toast.LENGTH_SHORT).show();
            return;
        }

        new AlertDialog.Builder(this).setTitle(
                "uid:" + userService.getUser().getName() + "\n"
                        + "token:" + userService.getUser().getName() + "\n"
        ).create().show();
    }
}
