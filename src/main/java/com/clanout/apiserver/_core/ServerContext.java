package com.clanout.apiserver._core;

import java.util.concurrent.ExecutorService;

public class ServerContext
{
    public static ServerContext instance;

    public static void init(ExecutorService workerExecutorService)
    {
        instance = new ServerContext(workerExecutorService);
        System.out.println("ServerContext initialized");
    }

    public static void destroy()
    {
        getInstance().workerExecutorService.shutdownNow();
        System.out.println("ServerContext destroyed");
    }

    public static ServerContext getInstance()
    {
        if (instance == null)
        {
            System.err.println("ServerContext not initialized");
            throw new IllegalStateException("ServerContext not initialized");
        }
        return instance;
    }

    private ExecutorService workerExecutorService;

    private ServerContext(ExecutorService workerExecutorService)
    {
        this.workerExecutorService = workerExecutorService;
    }

    public ExecutorService getWorkerExecutorService()
    {
        return workerExecutorService;
    }
}
