package com.clanout.apiserver._core;

import com.clanout.application.core.ApplicationContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ExecutorService;

public class ServerContext
{
    private static Logger LOG = LogManager.getRootLogger();

    public static ServerContext instance;

    public static void init(ApplicationContext applicationContext, ExecutorService workerPool)
    {
        instance = new ServerContext(applicationContext, workerPool);
        LOG.debug("[ServerContext initialized]");
    }

    public static void destroy()
    {
        getInstance().applicationContext.destroy();
        getInstance().workerPool.shutdownNow();

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

    private ExecutorService workerPool;
    private ApplicationContext applicationContext;

    private ServerContext(ApplicationContext applicationContext, ExecutorService workerPool)
    {
        this.applicationContext = applicationContext;
        this.workerPool = workerPool;
    }

    public ExecutorService getWorkerPool()
    {
        return workerPool;
    }

    public ApplicationContext getApplicationContext()
    {
        return applicationContext;
    }
}
