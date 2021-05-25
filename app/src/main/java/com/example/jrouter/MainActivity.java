package com.example.jrouter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.home_module.HomeActivity;
import com.example.jrouterapi.JPostcard;
import com.example.jrouterapi.core.JRouter;
import com.example.jrouterapi.interceptor.IRouteInterceptor;

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
            }
        });
    }
}
