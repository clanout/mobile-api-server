package com.clanout.apiserver.endpoints._core;

import com.clanout.apiserver._core.ServerContext;

import java.util.concurrent.ExecutorService;

public class AbstractEndpoint
{
    protected ExecutorService workers;

    public AbstractEndpoint()
    {
        workers = ServerContext.getInstance().getWorkerExecutorService();
    }
}
