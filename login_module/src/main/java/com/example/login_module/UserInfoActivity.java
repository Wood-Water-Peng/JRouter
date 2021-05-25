package com.example.login_module;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.annotation.JRouterAnno;

@JRouterAnno(path = "/login_module/UserInfoActivity")
public class UserInfoActivity extends AppCompatActivity {

    TextView tvUserName;
    TextView tvToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        tvUserName=findViewById(R.id.userName);
        tvToken=findViewById(R.id.token);
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            tvUserName.setText(bundle.getString("uid"));
            tvToken.setText(bundle.getString("token"));
        }
    }
}
