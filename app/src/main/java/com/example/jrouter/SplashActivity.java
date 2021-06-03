package com.example.jrouter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.base_lib.JLogUtil;
import com.example.jrouterapi.core.JRouter;
import com.example.perttask.Dispatcher;
import com.example.perttask.PertGraph;
import com.example.perttask.PertGraphManager;
import com.example.perttask.Task;
import com.example.perttask.TaskLifeCycleListener;

public class SplashActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //1.加载广告信息

        //2.获取系统权限并提示用户

        //3.获取配置信息
        PertGraph splashActivityGraph = new PertGraph("splashActivityGraph");
        splashActivityGraph
//                .addTask(new LoadADTask("loadADTask"))
//                .addTask(new LoadConfigTask("loadConfigTask"))
                .addTask("taskA").dependOn("taskB","taskC","taskE")
                .addTask("taskB").dependOn("taskC")
                .addTask("taskC")
                .addTask("taskE");

        PertGraph splashActivityGraph2 = new PertGraph("splashActivityGraph2");
        splashActivityGraph2
//                .addTask(new LoadADTask("loadADTask2"))
//                .addTask(new LoadConfigTask("loadConfigTask2"))
                .addTask("taskA2")
                .addTask("taskE2");

        splashActivityGraph.addGraphLifecycleListener(new TaskLifeCycleListener() {
            @Override
            public void onTaskStarted(Task task) {

            }

            @Override
            public void onTaskFinished(Task task) {
                //整个任务图已经执行完毕
//                SplashActivity.this.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
//                    }
//                });

            }
        });

//        PertGraphManager.getInstance()
//                .addGraphTask(splashActivityGraph)
//                .addGraphTask(splashActivityGraph2)
//                .start()
//                .waitUntilFinish();
        SplashActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        });
    }
}