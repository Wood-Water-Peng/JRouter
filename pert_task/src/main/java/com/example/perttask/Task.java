package com.example.perttask;

import java.util.ArrayList;
import java.util.List;

public abstract class Task implements Runnable {

    List<TaskLifeCycleListener> lifeCycleListenerList = new ArrayList<>();
    //前任
    private List<Task> predecessorList = new ArrayList<>();
    //下一任
    private List<Task> successorList = new ArrayList<>();
    private boolean runInUIThread = false;
    private String name;

    public Task(boolean runInUIThread, String name) {
        this.runInUIThread = runInUIThread;
        this.name = name;
    }

    public Task(String name) {
        this.name = name;
    }
    public void addTaskLifecycleListener(TaskLifeCycleListener listener) {
        if (!lifeCycleListenerList.contains(listener)) {
            lifeCycleListenerList.add(listener);
        }
    }
    @Override
    public void run() {
        onStarted();
        call();
        onFinished();
    }

    public String getName() {
        return name;
    }

    protected void onFinished() {
        for (int i = 0; i < lifeCycleListenerList.size(); i++) {
            lifeCycleListenerList.get(i).onTaskFinished(this);
        }

        //通知所有的successor
        for (int i = 0; i < successorList.size(); i++) {
            successorList.get(i).onPredecessorFinished(this);
        }
    }

    protected void onStarted() {
        for (int i = 0; i < lifeCycleListenerList.size(); i++) {
            lifeCycleListenerList.get(i).onTaskStarted(this);
        }
    }

    //方便子类复写
    public abstract void call();


    //前任执行完，判断自己的所有前任是否都执行完
    public void onPredecessorFinished(Task predecessor) {
        predecessorList.remove(predecessor);
        if (predecessorList.isEmpty()) {
            //自己开始执行
            start();
        }
    }


    //添加下一任
    public void addSuccessor(Task successor) {
        if (!successorList.contains(successor)) {
            successorList.add(successor);
            //successor的前任就是自己
            successor.predecessorList.add(this);
        }
    }


    //移除前任
    public void removePredecessor(Task predecessor) {
        predecessorList.remove(predecessor);
        predecessor.successorList.remove(this);
    }

    protected void start() {
        //把自己交给线程分发器去分发吧，可以在子线程，也可以在UI线程
        Dispatcher.getInstance().dispatch(this);
    }

    public List<Task> getPredecessorList() {
        return predecessorList;
    }


    public List<Task> getSuccessorList() {
        return successorList;
    }


    public boolean runInUIThread() {
        return runInUIThread;
    }


}
