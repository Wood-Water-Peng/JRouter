package com.example.login_module;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.annotation.JRouterAnno;
import com.example.jrouterapi.core.JRouter;

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
        String uid = Repository.getInstance().getUid();
        String token = Repository.getInstance().getToken();
        if (uid.equals("admin") && token.equals("admin")) {
            tvUserName.setText(uid);
            tvToken.setText(token);
        } else {
            JRouter.path("/login_module/LoginActivity").navigate(this);
            finish();
        }
    }

    public void logout(View view) {
        Repository.getInstance().putUid("uid");
        Repository.getInstance().putToken("token");
        JRouter.path("/login_module/LoginActivity").navigate(this);
        finish();
    }
}
