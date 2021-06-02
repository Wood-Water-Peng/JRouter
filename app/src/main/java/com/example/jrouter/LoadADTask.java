package com.example.jrouter;

import com.example.base_lib.JLogUtil;
import com.example.perttask.Dispatcher;
import com.example.perttask.Task;

public class LoadADTask extends Task {
    public LoadADTask(String name) {
        super(name);
    }

    @Override
    public void call() {
        if (Dispatcher.getInstance().isInUIThread()) {
            JLogUtil.log(getName() + " run in UI thread");
        } else {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            JLogUtil.log(getName() + " run in thread ->" + Thread.currentThread().getName());
        }
    }
}
