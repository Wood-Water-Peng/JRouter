package com.example.perttask;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

//用于分发任务
public class Dispatcher {
    private static final Dispatcher dispatcher = new Dispatcher();
    private static final ThreadPoolExecutor taskExecutor;

    static {
        int processors = Runtime.getRuntime().availableProcessors();
        taskExecutor = new ThreadPoolExecutor(processors, processors,
                60L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(),
                new ThreadFactory() {
                    private final AtomicInteger mCount = new AtomicInteger(1);

                    public Thread newThread(Runnable r) {
                        return new Thread(r, "GraphTask Thread #" + mCount.getAndIncrement());
                    }
                });
    }

    public static Dispatcher getInstance() {
        return dispatcher;
    }

    public void dispatch(Task task) {
        if (task.runInUIThread()) {

        } else {
            taskExecutor.submit(task);
        }
    }

    Handler mainHandler = new Handler(Looper.getMainLooper());

    public void runOnUIThread(Task task) {
        mainHandler.post(task);
    }

    public boolean isInUIThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

}
