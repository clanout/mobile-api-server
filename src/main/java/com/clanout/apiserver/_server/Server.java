package com.clanout.apiserver._server;

import com.clanout.apiserver.core.ClanoutApiServer;
import com.clanout.apiserver.core.Constants;
import org.glassfish.grizzly.CompletionHandler;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.NetworkListener;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.net.URI;

public class Server
{
    public static final String HTTP_SCHEME = "http";
    public static final String HOST = "0.0.0.0";
    private static final int PORT = 8080;

    public static final URI BASE_URI = UriBuilder
            .fromPath(Constants.BASE_URI)
            .scheme(HTTP_SCHEME)
            .host(HOST)
            .port(PORT)
            .build();

    public static HttpServer startServer()
    {
        final ClanoutApiServer clanoutApiServer = new ClanoutApiServer();
        final ResourceConfig resourceConfig = ResourceConfig.forApplication(clanoutApiServer);
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, resourceConfig);
    }

    public static void main(String[] args) throws IOException
    {
        final HttpServer server = startServer();

        System.out.println("ClanOut API Server started [" + BASE_URI.toString() + "]");

        System.in.read();
        server.shutdown().addCompletionHandler(
                new CompletionHandler<HttpServer>()
                {
                    @Override
                    public void cancelled()
                    {
                    }

                    @Override
                    public void failed(Throwable throwable)
                    {
                    }

                    @Override
                    public void completed(HttpServer httpServer)
                    {
                        System.out.println("Clanout API Server stopped");
                    }

                    @Override
                    public void updated(HttpServer httpServer)
                    {
                    }
                });
    }
}

