package com.example.perttask;

import java.util.HashMap;
import java.util.Map;

public class TaskFactory {
    private final ITaskCreator taskCreator;
    private final Map<String, Task> tasks = new HashMap<>();

    public TaskFactory(ITaskCreator taskCreator) {
        this.taskCreator = taskCreator;
    }

    public Task getTask(String name) {
        Task task = tasks.get(name);
        if (task == null) {
            task = taskCreator.createTask(name);
            if (task == null) {
                throw new IllegalArgumentException("Create task fail, there is no task corresponding to the task name. Make sure you have create a task instance in TaskCreator.");
            }
            tasks.put(name, task);
        }
        return task;
    }
}
