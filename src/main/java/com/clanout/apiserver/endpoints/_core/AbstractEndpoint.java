package com.clanout.apiserver.endpoints._core;

import com.clanout.apiserver._core.ServerContext;
import com.clanout.apiserver.util.GsonProvider;
import com.clanout.application.core.ApplicationContext;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Objects;
import java.util.concurrent.ExecutorService;

public class AbstractEndpoint
{
    protected ExecutorService workers;
    protected ApplicationContext applicationContext;

    public AbstractEndpoint()
    {
        workers = ServerContext.getInstance().getWorkerExecutorService();
        applicationContext = ServerContext.getInstance().getApplicationContext();
    }

    protected Response buildSuccessJsonResponse(Object o)
    {
        return Response.ok(GsonProvider.get().toJson(o), MediaType.APPLICATION_JSON_TYPE).build();
    }
}
