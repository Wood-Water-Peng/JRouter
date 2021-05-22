package com.example.login_module;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.annotation.JRouter;

@JRouter(path = "/login/userInfoActivity")
public class UserInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
    }
}
