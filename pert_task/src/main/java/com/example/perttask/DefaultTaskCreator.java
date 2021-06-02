package com.example.perttask;

import com.example.base_lib.JLogUtil;

import java.util.List;
import java.util.Random;

public class DefaultTaskCreator implements ITaskCreator {
    @Override
    public Task createTask(String name) {
        return new Task(name) {
            @Override
            public void call() {
                if (Dispatcher.getInstance().isInUIThread()) {
                    JLogUtil.log(getName() + " run in UI thread");
                } else {
                    try {
                        Thread.sleep(new Random(System.currentTimeMillis()).nextInt(2000) + 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    List<Task> successorList = getSuccessorList();
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("[");
                    for (int i = 0; i < successorList.size(); i++) {
                        stringBuilder.append(successorList.get(i).getName());
                        stringBuilder.append(",");
                    }
                    stringBuilder.append("]");
                    JLogUtil.log(getName() + " run in thread ->" + Thread.currentThread().getName() + stringBuilder.toString());
                }
            }
        };
    }
}
