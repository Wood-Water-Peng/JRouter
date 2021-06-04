package com.example.jrouter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.Observer;
import androidx.viewpager.widget.ViewPager;

import com.example.base_lib.JLogUtil;
import com.example.jrouterapi.JPostcard;
import com.example.jrouterapi.core.JRouter;
import com.example.jrouterapi.fragment.FragmentCenter;
import com.example.jrouterapi.fragment.FragmentHelper;
import com.example.jrouterapi.interceptor.IRouteInterceptor;
import com.example.jrouterapi.service.ServiceCenter;
import com.example.login_module_export.IUserService;
import com.example.login_module_export.User;
import com.example.perttask.PertGraph;
import com.example.perttask.PertGraphManager;
import com.google.android.material.tabs.TabLayout;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    private static List<TabWrapper> tabWrappers;
    private TabAdapter tabAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewpager);

        IUserService userService = (IUserService) ServiceCenter.getService(IUserService.name);
        if (userService != null) {
            tabWrappers = generateFragmentTitles(userService.getUser());
        }

        userService.getUserLiveData().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user == null) {
                    //退出登录
                    tabWrappers.get(tabAdapter.getCount() - 1).title = "用户(未登录)";
                    tabAdapter.notifyDataSetChanged();
                } else {
                    //更新UI
                    if (user.getLevel() == 0) {
                        tabWrappers.get(tabAdapter.getCount() - 1).title = "普通用户";
                    } else if (user.getLevel() == 1) {
                        tabWrappers.get(tabAdapter.getCount() - 1).title = "VIP用户";
                    } else if (user.getLevel() == 2) {
                        tabWrappers.get(tabAdapter.getCount() - 1).title = "老板";
                    }
                    tabAdapter.notifyDataSetChanged();
                }
            }
        });
        tabAdapter = new TabAdapter(getSupportFragmentManager(), 0);
        viewPager.setAdapter(tabAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    public void test() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                JLogUtil.log(Thread.currentThread().getName() + " started");
                PertGraph splashActivityGraph3 = new PertGraph("MainActivityGraph3");
                splashActivityGraph3
                        .addTask(new LoadADTask("loadADTask3"))
                        .addTask(new LoadConfigTask("loadConfigTask3"))
                        .addTask("taskA3").dependOn("taskB3")
                        .addTask("taskB3").dependOn("taskC3")
                        .addTask("taskC3")
                        .addTask("taskE3");

                PertGraph splashActivityGraph4 = new PertGraph("MainActivityGraph4");
                splashActivityGraph4
                        .addTask(new LoadADTask("loadADTask4"))
                        .addTask(new LoadConfigTask("loadConfigTask4"))
                        .addTask("taskA4")
                        .addTask("taskE4");

                PertGraphManager.getInstance()
                        .addGraphTask(splashActivityGraph3)
                        .addGraphTask(splashActivityGraph4)
                        .start()
                        .waitUntilFinish();
                JLogUtil.log(Thread.currentThread().getName() + " finished");
            }
        }).start();
    }

    public void jump(View v) {
        JPostcard postcard = JRouter.path("/home_module/HomeActivity");
        Bundle bundle = new Bundle();
        bundle.putString("from", getClass().getCanonicalName());
        postcard.withParam(bundle).navigate(this);
    }

    public void jump2UserInfo(View view) {
        JPostcard postcard = JRouter.path("/login_module/UserInfoActivity");
        Bundle bundle = new Bundle();
        bundle.putString("from", getClass().getCanonicalName());
        postcard.withParam(bundle).navigate(this, new IRouteInterceptor.Callback() {
            @Override
            public void onSuccess(@NonNull JPostcard jPostcard) {

            }

            @Override
            public void onFail(@NonNull Throwable exception) {
                Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
                //跳转到登录页面
                JRouter.path("/login_module/LoginActivity").navigate(MainActivity.this);
            }
        });
    }

    //弹窗显示用户信息
    public void showUserInfo(View view) {
        Object obj = ServiceCenter.getService(IUserService.name);
        if (obj == null) {
            Toast.makeText(getApplicationContext(), "can not find service:" + IUserService.name, Toast.LENGTH_SHORT).show();
        }
        if (!(obj instanceof IUserService)) {
            throw new IllegalStateException(obj.toString() + "can not be converted to " + IUserService.class.getCanonicalName());
        }
        IUserService userService = (IUserService) obj;
        if (userService.getUser() == null) {
            Toast.makeText(getApplicationContext(), "用户未登录", Toast.LENGTH_SHORT).show();
            return;
        }

        new AlertDialog.Builder(this).setTitle(
                "uid:" + userService.getUser().getName() + "\n"
                        + "token:" + userService.getUser().getName() + "\n"
        ).create().show();
    }

    static class TabAdapter extends FragmentStatePagerAdapter {

        public TabAdapter(@NonNull @NotNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @NonNull
        @NotNull
        @Override
        public Fragment getItem(int position) {
            return tabWrappers.get(position).fragment;
        }

        @Nullable
        @org.jetbrains.annotations.Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return tabWrappers.get(position).title;
        }

        @Override
        public int getCount() {
            return tabWrappers.size();
        }
    }

    //根据用户的登录状态或者身份动态显示tab
    private List<TabWrapper> generateFragmentTitles(User user) {

        List<TabWrapper> list = new ArrayList<>();
        if (user == null) {
            //未登录
            list.add(new TabWrapper("首页", FragmentHelper.getFragment("/home_module/HomeFragment", null)));
            list.add(new TabWrapper("钱包", new EmptyFragment()));
            list.add(new TabWrapper("未登录", FragmentHelper.getFragment("/login_module/UserCenterFragment", null)));
        } else if (user.getLevel() == 0) {
            //普通用户
            list.add(new TabWrapper("首页", FragmentHelper.getFragment("/home_module/HomeFragment", null)));
            list.add(new TabWrapper("钱包", new EmptyFragment()));
            Bundle bundle = new Bundle();
            bundle.putParcelable("user", user);
            list.add(new TabWrapper("普通用户", FragmentHelper.getFragment("/login_module/UserCenterFragment", bundle)));
        } else if (user.getLevel() == 1) {
            //VIP
            list.add(new TabWrapper("首页", FragmentHelper.getFragment("/home_module/HomeFragment", null)));
            list.add(new TabWrapper("钱包", new EmptyFragment()));
            Bundle bundle = new Bundle();
            bundle.putString("uid", "uid");
            bundle.putString("token", "token");
            list.add(new TabWrapper("VIP用户", FragmentHelper.getFragment("/login_module/UserCenterFragment", bundle)));
        }
        return list;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    static class TabWrapper {
        String title;
        Fragment fragment;

        public TabWrapper(String title, Fragment fragment) {
            this.title = title;
            this.fragment = fragment;
        }
    }


}
