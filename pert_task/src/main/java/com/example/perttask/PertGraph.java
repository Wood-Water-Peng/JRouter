package com.example.perttask;

import androidx.annotation.NonNull;

import com.example.base_lib.JLogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 这是一个比较特殊的task，他是整个task图的入口，可以单独执行，也可以作为一个子task嵌入到其他的task图中
 * <p>
 * PertTask1->PertTask2
 */
public class PertGraph extends Task {
    //该任务图的观察者
    List<TaskLifeCycleListener> lifeCycleListenerList = new ArrayList<>();
    //起点
    private AnchorTask startTask;
    //终点
    private AnchorTask endTask;
    TaskFactory factory;

    public PertGraph(String name) {
        super(name);
        factory = new TaskFactory(new DefaultTaskCreator());
        startTask = new AnchorTask("PertGraph-StartTask");
        startTask.addTaskLifecycleListener(new TaskLifeCycleListener() {

            @Override
            public void onTaskStarted(Task task) {
//                JLogUtil.log(task.getName() + " finish");
                onStarted();
            }

            @Override
            public void onTaskFinished(Task task) {

            }
        });
        endTask = new AnchorTask("PertGraph-EndTask");
        endTask.addTaskLifecycleListener(new TaskLifeCycleListener() {

            @Override
            public void onTaskStarted(Task task) {

            }

            @Override
            public void onTaskFinished(Task task) {
//                JLogUtil.log(task.getName() + " finish");
                onFinished();
            }
        });
    }

    @Override
    protected void onStarted() {
        JLogUtil.log(getName() + " onStarted on thread->" + Thread.currentThread().getName());
        for (int i = 0; i < lifeCycleListenerList.size(); i++) {
            lifeCycleListenerList.get(i).onTaskStarted(this);
        }
    }

    @Override
    protected void onFinished() {
        JLogUtil.log(getName() + " onFinished on thread->" + Thread.currentThread().getName() + "\n\n\n\n\n");
        for (int i = 0; i < lifeCycleListenerList.size(); i++) {
            lifeCycleListenerList.get(i).onTaskFinished(this);
        }
    }

    @Override
    public void call() {

    }

    public void addGraphLifecycleListener(TaskLifeCycleListener listener) {
        lifeCycleListenerList.add(listener);
    }

    public void start() {
        startTask.start();
    }

    /**
     * 在这张图中添加一个任务，并确定这个任务在图中的位置
     * <p>
     * 默认pre是startAnchor,successor是endAnchor
     *
     * @param task
     * @return
     */
    public PertGraph addTask(@NonNull Task task) {
        startTask.addSuccessor(task);
        task.addSuccessor(endTask);
        return this;
    }

    private Task targetTask;

    public PertGraph dependOn(@NonNull String... dependTasks) {
        if (targetTask == null) {
            throw new IllegalStateException("targetTask is null");
        }
        Task[] tasks = new Task[dependTasks.length];
        for (int i = 0; i < tasks.length; i++) {
            tasks[i] = factory.getTask(dependTasks[i]);
            tasks[i].addSuccessor(targetTask);
            //此时item的successor不可能是endTask，需要移除
            endTask.removePredecessor(tasks[i]);
        }
        return this;
    }

    public PertGraph addTask(@NonNull String taskName) {
        Task task = factory.getTask(taskName);
        targetTask = task;
        //默认实现
        startTask.addSuccessor(task);
        task.addSuccessor(endTask);
        return this;
    }

    private static class AnchorTask extends Task {

        public AnchorTask(String name) {
            super(name);
        }

        @Override
        public void call() {
            //默认啥也不做
        }
    }
}
