package com.example.login_module;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

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

    }

    public void login(View view) {
        if("admin".equals(userName)&&"admin".equals(psw)){
            //登录成功
            Bundle bundle = new Bundle();
            bundle.putString("uid","admin");
            bundle.putString("token","admin");
            JRouter.path("/login_module/UserInfoActivity").withParam(bundle).navigate(this);
        }else {

        }
    }
}
