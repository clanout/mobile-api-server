package com.clanout.apiserver.endpoints;

import com.clanout.apiserver.endpoints._core.AbstractEndpoint;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import java.util.HashMap;
import java.util.Map;

@Path("health-check")
public class HealthEndpoint extends AbstractEndpoint
{
    @GET
    public void healthCheck(@Suspended AsyncResponse asyncResponse)
    {
        workerPool.execute(() -> {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Hello, World!");
            asyncResponse.resume(buildSuccessJsonResponse(response));
        });
    }
}
