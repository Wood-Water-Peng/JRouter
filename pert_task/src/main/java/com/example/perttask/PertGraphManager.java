package com.example.perttask;


import androidx.annotation.NonNull;

import com.example.base_lib.JLogUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * 负责控制PertGraph的启动
 */
public class PertGraphManager {
    private static PertGraphManager instance = null;
    private List<PertGraph> graphTasks = new ArrayList<>();
    CountDownLatch graphTaskLatch;
    //所有的PertGraph是否完成
    private static final byte[] waitFinishLock = new byte[0];

    private PertGraphManager() {
    }

    public static synchronized PertGraphManager getInstance() {
        if (instance == null) {
            instance = new PertGraphManager();
        }

        return instance;
    }

    public PertGraphManager addGraphTask(@NonNull PertGraph graphTask) {
        graphTasks.add(graphTask);
        graphTask.addGraphLifecycleListener(new TaskLifeCycleListener() {
            @Override
            public void onTaskStarted(Task task) {

            }

            @Override
            public void onTaskFinished(Task task) {
                graphTasks.remove(task);
                graphTaskLatch.countDown();
                if (graphTaskLatch.getCount() == 0) {
                    //任务图都已经执行完
                    synchronized (waitFinishLock) {
                        waitFinishLock.notifyAll();
                    }
                }
            }
        });
        return this;
    }

    public PertGraphManager start() {
        if (graphTasks.isEmpty()) throw new IllegalStateException("graphTask is null !!!");
        graphTaskLatch = new CountDownLatch(graphTasks.size());
        for (PertGraph graph :
                graphTasks) {
            graph.start();
        }
        return this;
    }

    public void waitUntilFinish() {
        synchronized (waitFinishLock) {
            if (graphTaskLatch.getCount() == 0) {
            } else {
                try {
                    JLogUtil.log(Thread.currentThread().getName() + " 阻塞...");
                    waitFinishLock.wait();
                    JLogUtil.log(Thread.currentThread().getName() + " 继续执行...");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
