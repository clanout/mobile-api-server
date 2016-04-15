package com.clanout.apiserver._server;

import com.clanout.apiserver._core.ClanoutApiServer;
import com.clanout.apiserver._core.Constants;
import com.clanout.apiserver._core.ServerContext;
import com.clanout.application.core.ApplicationContext;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.grizzly.CompletionHandler;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.nio.transport.TCPNIOTransport;
import org.glassfish.grizzly.strategies.SameThreadIOStrategy;
import org.glassfish.grizzly.strategies.WorkerThreadIOStrategy;
import org.glassfish.grizzly.threadpool.ThreadPoolConfig;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.net.URI;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

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

    public static void main(String[] args) throws IOException
    {

        final HttpServer server = startHttpServer();

        TCPNIOTransport transport = server.getListeners().iterator().next().getTransport();
        transport.setIOStrategy(WorkerThreadIOStrategy.getInstance());

        ThreadPoolConfig ioThreadPoolConfig = transport.getWorkerThreadPoolConfig();
        processHttpIoThreadPoolConfig(ioThreadPoolConfig);

        LOG.debug("[Server ThreadPool Config: name=" + ioThreadPoolConfig.getPoolName() +
                          "; core_size=" + ioThreadPoolConfig.getCorePoolSize() +
                          "; max_size=" + ioThreadPoolConfig.getMaxPoolSize() + "]");

        server.start();

        LOG.debug("[ClanOut API Server started at " + BASE_URI.toString() + "]");

        Runtime.getRuntime().addShutdownHook(new Thread()
        {
            @Override
            public void run()
            {
                shutdown(server);
            }
        });
    }

    public static HttpServer startHttpServer()
    {
        initServerContext();
        final ClanoutApiServer clanoutApiServer = new ClanoutApiServer();
        final ResourceConfig resourceConfig = ResourceConfig.forApplication(clanoutApiServer);
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, resourceConfig, false);
    }

    public static void shutdown(HttpServer server)
    {
        LOG.debug("[Clanout API Server stopped]");
        ServerContext.destroy();
        server.shutdownNow();
    }

    private static void initServerContext()
    {
        ExecutorService workerPool = getWorkerThreadPool();
        ExecutorService backgroundPool = getBackgroundThreadPool();

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

    private static void processHttpIoThreadPoolConfig(ThreadPoolConfig threadPoolConfig)
    {
        String THREAD_NAME_FORMAT = "http-io-%d";
        int IO_THREAD_POOL_SIZE = 10;
        int IO_THREAD_POOL_MAX_SIZE = 25;

        ThreadFactory httpThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat(THREAD_NAME_FORMAT)
                .build();

        threadPoolConfig.setPoolName("http-io");
        threadPoolConfig.setThreadFactory(httpThreadFactory);
        threadPoolConfig.setCorePoolSize(IO_THREAD_POOL_SIZE);
        threadPoolConfig.setMaxPoolSize(IO_THREAD_POOL_MAX_SIZE);
    }

    private static ExecutorService getWorkerThreadPool()
    {
        String THREAD_NAME_FORMAT = "worker-thread-%d";
        int WORKER_THREAD_POOL_SIZE = 50;

        ThreadFactory workerThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat(THREAD_NAME_FORMAT)
                .build();
        return Executors.newFixedThreadPool(WORKER_THREAD_POOL_SIZE, workerThreadFactory);
    }

    private static ExecutorService getBackgroundThreadPool()
    {
        String THREAD_NAME_FORMAT = "background-thread-%d";
        int BACKGROUND_THREAD_POOL_SIZE = 20;

        ThreadFactory backgroundThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat(THREAD_NAME_FORMAT)
                .build();
        return Executors.newFixedThreadPool(BACKGROUND_THREAD_POOL_SIZE, backgroundThreadFactory);
    }
}

