package com.example.jrouter;

import com.example.base_lib.JLogUtil;
import com.example.perttask.Dispatcher;
import com.example.perttask.Task;

import java.util.Random;

public class DefaultTaskCreator implements ITaskCreator{
    @Override
    public Task createTask(String name) {
        return new Task(name) {
            @Override
            public void call() {
                if (Dispatcher.getInstance().isInUIThread()) {
                    JLogUtil.log(getName() + " run in UI thread");
                } else {
                    try {
                        Thread.sleep(new Random(System.currentTimeMillis()).nextInt(2000)+1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    JLogUtil.log(getName() + " run in thread ->" + Thread.currentThread().getName());
                }
            }
        };
    }
}
