package com.clanout.apiserver._core;

import com.clanout.application.core.ApplicationContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ExecutorService;

public class ServerContext
{
    private static Logger LOG = LogManager.getRootLogger();

    public static ServerContext instance;

    public static void init(ApplicationContext applicationContext, ExecutorService workerExecutorService)
    {
        instance = new ServerContext(applicationContext, workerExecutorService);
        LOG.debug("[ServerContext initialized]");
    }

    public static void destroy()
    {
        getInstance().applicationContext.destroy();
        getInstance().workerExecutorService.shutdownNow();

        LOG.debug("[ServerContext destroyed]");
    }

    public static ServerContext getInstance()
    {
        if (instance == null)
        {
            LOG.fatal("[ServerContext not initialized]");
            throw new IllegalStateException("ServerContext not initialized");
        }
        return instance;
    }

    private ExecutorService workerExecutorService;
    private ApplicationContext applicationContext;

    private ServerContext(ApplicationContext applicationContext, ExecutorService workerExecutorService)
    {
        this.applicationContext = applicationContext;
        this.workerExecutorService = workerExecutorService;
    }

    public ExecutorService getWorkerExecutorService()
    {
        return workerExecutorService;
    }

    public ApplicationContext getApplicationContext()
    {
        return applicationContext;
    }
}
