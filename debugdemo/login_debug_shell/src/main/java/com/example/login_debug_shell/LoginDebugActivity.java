package com.example.login_debug_shell;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.jrouterapi.JPostcard;
import com.example.jrouterapi.core.JRouter;
import com.example.jrouterapi.interceptor.IRouteInterceptor;
import com.example.login_module_export.User;

public class LoginDebugActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_debug);
    }

    public void jump2Login(View view) {
        JRouter.path("/login_module/LoginActivity").navigate(this);
    }

    public void jump2UserCenter(View view) {
        JRouter.path("/login_module/UserInfoActivity").navigate(this, new IRouteInterceptor.Callback() {
            @Override
            public void onSuccess(@NonNull @org.jetbrains.annotations.NotNull JPostcard jPostcard) {

            }

            @Override
            public void onFail(@NonNull @org.jetbrains.annotations.NotNull Throwable exception) {
//                Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
                JRouter.path("/login_module/LoginActivity").navigate(getApplicationContext());
            }
        });
    }
}