package com.clanout.apiserver.endpoints;

import com.clanout.apiserver.endpoints._core.AbstractEndpoint;

import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;

@Path("health_check")
public class HealthEndpoint extends AbstractEndpoint
{
    @GET
    public void healthCheck(@Suspended AsyncResponse response)
    {
        workers.execute(() -> response.resume("Hello, World!"));
    }
}
