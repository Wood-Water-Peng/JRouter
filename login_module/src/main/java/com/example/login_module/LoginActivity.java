package com.example.login_module;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.annotation.JRouterAnno;
import com.example.jrouterapi.core.JRouter;

@JRouterAnno(path = "/login_module/LoginActivity")
public class LoginActivity extends AppCompatActivity {

    String userName;
    String psw;
    EditText etUserName;
    EditText etUserPsw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etUserName = findViewById(R.id.etUserName);
        etUserPsw = findViewById(R.id.etPsw);

        //判断用户是否已经登录
        String uid = Repository.getInstance().getUid();
        String token = Repository.getInstance().getToken();
        if (uid.equals("admin") && token.equals("admin")) {
            JRouter.path("/login_module/UserInfoActivity").navigate(this);
            finish();
        }
    }

    public void login(View view) {
        if ("admin".equals(etUserName.getText().toString().trim()) && "admin".equals(etUserPsw.getText().toString().trim())) {
            //登录成功
            Repository.getInstance().putUid("admin");
            Repository.getInstance().putToken("admin");
            JRouter.path("/login_module/UserInfoActivity").navigate(this);
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "登录失败", Toast.LENGTH_SHORT).show();
        }
    }
}
