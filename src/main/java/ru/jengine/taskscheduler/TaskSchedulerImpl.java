package ru.jengine.taskscheduler;

import java.util.ArrayDeque;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TaskSchedulerImpl implements TaskScheduler {
    private static final Logger LOG = LoggerFactory.getLogger(TaskSchedulerImpl.class);
    private final Map<String, Queue<Task>> taskQueues = new ConcurrentHashMap<>();

    @Override
    public void addTask(String queueCode, Task task) {
        synchronized (taskQueues) {
            Queue<Task> taskQueue = taskQueues.computeIfAbsent(queueCode, c -> new ArrayDeque<>());
            taskQueue.add(task);
        }
    }

    @Override
    public void executeTaskQueue(String queueCode) {
        Queue<Task> taskQueue = taskQueues.remove(queueCode);
        if (taskQueue != null) {
            taskQueue.removeIf(task -> {
                try {
                    task.execute();
                } catch (Exception e) {
                    LOG.error("Error in task", e);
                }

                return !task.isReusable();
            });

            if (!taskQueue.isEmpty()) {
                taskQueues.merge(queueCode, taskQueue, (oldQueue, newQueue) -> {
                    oldQueue.addAll(newQueue);
                    return oldQueue;
                });
            }
        }
    }
}
