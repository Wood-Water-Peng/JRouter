package com.example.login_module;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.annotation.JRouterAnno;
import com.example.jrouterapi.core.JRouter;
import com.example.jrouterapi.service.ServiceCenter;
import com.example.login_module_export.IUserService;
import com.example.login_module_export.User;

@JRouterAnno(path = "/login_module/UserInfoActivity")
public class UserInfoActivity extends AppCompatActivity {

    TextView tvUserName;
    TextView tvToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        tvUserName = findViewById(R.id.userName);
        tvToken = findViewById(R.id.token);
        IUserService userService = (IUserService) ServiceCenter.getService(IUserService.name);
        if (userService != null) {
            User user = userService.getUser();
            if (user != null) {
                tvUserName.setText(user.getName());
            } else {
                JRouter.path("/login_module/LoginActivity").navigate(this);
                finish();
            }
        }

    }

    public void logout(View view) {
        Repository.getInstance().logout();
        JRouter.path("/login_module/LoginActivity").navigate(this);
        finish();
    }
}
