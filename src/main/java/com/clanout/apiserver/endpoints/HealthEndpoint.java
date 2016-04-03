package com.clanout.apiserver.endpoints;

import com.clanout.apiserver.endpoints._core.AbstractEndpoint;
import com.clanout.apiserver.request.core.SessionRequest;

import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;

@Path("health_check")
public class HealthEndpoint extends AbstractEndpoint
{
    @GET
    public void healthCheck(@Suspended AsyncResponse asyncResponse)
    {
        workerPool.execute(() -> asyncResponse.resume("Hello, World!"));
    }

    @GET
    @Path("/hello")
    public String sayHello(SessionRequest sessionRequest)
    {
        return "Hello " + sessionRequest.getSessionUser();
    }
}
