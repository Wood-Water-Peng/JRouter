package com.example.perttask;

public interface TaskLifeCycleListener {
    void onTaskStarted(Task task);
    void onTaskFinished(Task task);
}
