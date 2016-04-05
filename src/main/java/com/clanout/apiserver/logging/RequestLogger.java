package com.clanout.apiserver.logging;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.UUID;

@PreMatching
public class RequestLogger implements ContainerRequestFilter
{
    private static Logger LOG = LogManager.getRootLogger();

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException
    {
        String requestId = UUID.randomUUID().toString();
        long startTime = System.currentTimeMillis();
        containerRequestContext.setProperty("request_id", requestId);
        containerRequestContext.setProperty("request_start_time", String.valueOf(startTime));

        String uri = containerRequestContext.getUriInfo().getPath();
        String method = containerRequestContext.getMethod();

        if (method.equalsIgnoreCase("POST"))
        {
            BufferedReader br = new BufferedReader(new InputStreamReader(containerRequestContext.getEntityStream()));
            StringBuilder requestBuilder = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null)
            {
                requestBuilder.append(line.trim());
            }
            String request = requestBuilder.toString();

            if (request.isEmpty())
            {
                LOG.info("[REQUEST:POST:" + requestId + "] " + uri);
            }
            else
            {
                LOG.info("[REQUEST:POST:" + requestId + "] " + uri + "\n" + request);
            }

            containerRequestContext.setEntityStream(new ByteArrayInputStream(request.getBytes()));
        }
        else
        {
            LOG.info("[REQUEST:GET:" + requestId + "] " + uri);
        }
    }
}
