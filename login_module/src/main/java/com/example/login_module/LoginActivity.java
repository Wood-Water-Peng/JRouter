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
        String uid = Repository.getInstance().getString("uid");
        String token = Repository.getInstance().getString("token");
        if (uid.equals("admin") && token.equals("admin")) {
            JRouter.path("/login_module/UserInfoActivity").navigate(this);
            finish();
        }
    }

    public void login(View view) {
        if ("admin".equals(etUserName.getText().toString()) && "admin".equals(etUserPsw.getText().toString())) {
            //登录成功
            Bundle bundle = new Bundle();
            bundle.putString("uid", "admin");
            bundle.putString("token", "admin");
            Repository.getInstance().putString("uid", "admin");
            Repository.getInstance().putString("token", "admin");
            JRouter.path("/login_module/UserInfoActivity").withParam(bundle).navigate(this);
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "登录失败", Toast.LENGTH_SHORT).show();
        }
    }
}
