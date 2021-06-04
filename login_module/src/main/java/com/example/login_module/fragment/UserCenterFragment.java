package com.example.login_module.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.annotation.JFragAnno;
import com.example.jrouterapi.core.JRouter;
import com.example.jrouterapi.service.ServiceCenter;
import com.example.login_module.R;
import com.example.login_module_export.IUserService;
import com.example.login_module_export.User;

import org.jetbrains.annotations.NotNull;

import static android.app.Activity.RESULT_OK;

/**
 *
 */
@JFragAnno(path = "/login_module/UserCenterFragment")
public class UserCenterFragment extends Fragment {

    View btnGoLogin;
    TextView tvUid;
    TextView tvToken;
    View rootView;
    private int REQUEST_CODE_LOGIN = 100;
    private View btnLogout;
    private IUserService userService;

    public UserCenterFragment() {
        // Required empty public constructor
    }


    User user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = getArguments().getParcelable("user");
        }
        userService = (IUserService) ServiceCenter.getService(IUserService.name);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rootView = view;
        tvUid = view.findViewById(R.id.user_info_uid);
        tvToken = view.findViewById(R.id.user_info_token);
        btnGoLogin = view.findViewById(R.id.user_info_btn_go_login);
        btnLogout = view.findViewById(R.id.user_info_btn_logout);
        updateUI(user);
        userService.getUserLiveData().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                updateUI(user);
            }
        });
    }

    public void updateUI(User user) {
        if (user == null) {
            rootView.findViewById(R.id.user_info_not_login).setVisibility(View.VISIBLE);
            rootView.findViewById(R.id.user_info_login).setVisibility(View.GONE);
            btnGoLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    JRouter.path("/login_module/LoginActivity").withRequestCode(REQUEST_CODE_LOGIN).navigate(getContext());
                }
            });
        } else {
            rootView.findViewById(R.id.user_info_login).setVisibility(View.VISIBLE);
            rootView.findViewById(R.id.user_info_not_login).setVisibility(View.GONE);
            btnLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    userService.logout();
                }
            });
            tvUid.setText(user.getName());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_center, container, false);
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
//        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_LOGIN) {
//            if (user == null) {
//                user = data.getParcelableExtra("user");
//                updateUI(user);
//            }
//        }
//    }
}