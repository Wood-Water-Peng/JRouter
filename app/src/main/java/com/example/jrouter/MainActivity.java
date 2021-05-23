package com.example.jrouter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.home_module.HomeActivity;
import com.example.jrouterapi.JPostcard;
import com.example.jrouterapi.core.JRouter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }
    public void jump(View v){
        JPostcard postcard = JRouter.path("/home_module/HomeActivity");
        postcard.navigate(this);
    }
}
