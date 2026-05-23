package com.dfs.client.threading;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TaskManager {

    private static final ExecutorService executor =
            Executors.newFixedThreadPool(2);

    public static void runTask(
            Runnable task) {

        executor.submit(task);
    }
}