package com.clanout.apiserver.endpoints;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("health_check")
public class HealthEndpoint
{
    @GET
    public Response healthCheck()
    {
        return Response.ok("Hello, World!").build();
    }
}
