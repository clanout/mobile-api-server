package com.clanout;

import com.clanout.apiserver._server.Server;
import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import static org.junit.Assert.assertEquals;

public class HealthEndpointTest
{
    private HttpServer server;
    private WebTarget target;

    @Before
    public void setUp() throws Exception
    {
        // start the server
        server = Server.startHttpServer();
        // create the client
        Client c = ClientBuilder.newClient();

        target = c.target(Server.BASE_URI);
    }

    @After
    public void tearDown() throws Exception
    {
        server.shutdownNow();
    }

    @Test
    public void testHealthCheck()
    {
        String responseMsg = target.path("health_check").request().get(String.class);
        assertEquals("Hello, World!", responseMsg);
    }
}
