package com.clanout.apiserver._server;

import com.clanout.apiserver._core.ClanoutApiServer;
import com.clanout.apiserver._core.Constants;
import com.clanout.apiserver._core.ServerContext;
import com.clanout.application.core.ApplicationContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.grizzly.CompletionHandler;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.net.URI;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server
{
    private static Logger LOG = LogManager.getRootLogger();

    public static final String HTTP_SCHEME = "http";
    public static final String HOST = "0.0.0.0";
    private static final int PORT = 8080;

    public static final URI BASE_URI = UriBuilder
            .fromPath(Constants.BASE_URI)
            .scheme(HTTP_SCHEME)
            .host(HOST)
            .port(PORT)
            .build();

    private static final int WORKER_THREAD_POOL_SIZE = 10;
    private static final int BACKGROUND_THREAD_POOL_SIZE = 10;

    public static void main(String[] args) throws IOException
    {
        final HttpServer server = startHttpServer();
        LOG.debug("[ClanOut API Server started at " + BASE_URI.toString() + "]");

        System.in.read();
        shutdown(server);
    }

    public static HttpServer startHttpServer()
    {
        initServerContext();
        final ClanoutApiServer clanoutApiServer = new ClanoutApiServer();
        final ResourceConfig resourceConfig = ResourceConfig.forApplication(clanoutApiServer);
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, resourceConfig);
    }

    public static void shutdown(HttpServer server)
    {
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
                        ServerContext.destroy();
                        LOG.debug("[Clanout API Server stopped]");
                    }

                    @Override
                    public void updated(HttpServer httpServer)
                    {
                    }
                });
    }

    private static void initServerContext()
    {
        ExecutorService workerPool = Executors.newFixedThreadPool(WORKER_THREAD_POOL_SIZE);
        ExecutorService backgroundPool = Executors.newFixedThreadPool(BACKGROUND_THREAD_POOL_SIZE);

        ApplicationContext applicationContext = null;
        try
        {
            applicationContext = new ApplicationContext(backgroundPool);
        }
        catch (Exception e)
        {
            LOG.fatal("[ApplicationContext initialization failed]", e);
        }
        ServerContext.init(applicationContext, workerPool);
    }
}

