package ru.jengine.taskscheduler;

import java.util.ArrayDeque;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

public class TaskSchedulerImpl implements TaskScheduler {
    private final Map<String, Queue<Task>> taskQueues = new ConcurrentHashMap<>();

    @Override
    public void addTask(String queueCode, Task task) {
        synchronized (taskQueues) {
            Queue<Task> taskQueue = taskQueues.computeIfAbsent(queueCode, c -> createTaskQueue());
            taskQueue.add(task);
        }
    }

    @Override
    public void executeTaskQueue(String queueCode) {
        Queue<Task> taskQueue = taskQueues.remove(queueCode);
        Queue<Task> newTaskQueue = createTaskQueue();

        if (taskQueue != null && !taskQueue.isEmpty()) {
            do {
                Task task = taskQueue.poll();
                task.execute();

                if (task.isReusable()) {
                    newTaskQueue.add(task);
                }
            } while (!taskQueue.isEmpty());

            if (!newTaskQueue.isEmpty()) {
                synchronized (taskQueues) {
                    if (!taskQueues.containsKey(queueCode)) {
                        taskQueues.put(queueCode, newTaskQueue);
                    } else {
                        taskQueues.get(queueCode).addAll(newTaskQueue);
                    }
                }
            }
        }
    }

    private static Queue<Task> createTaskQueue() {
        return new ArrayDeque<>();
    }
}
