package com.example.home_module;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.annotation.JRouter;

@JRouter(path = "/home/HomeActivity")
public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }
}
