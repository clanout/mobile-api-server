package com.clanout.apiserver.logging;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import java.io.IOException;

public class ResponseLogger implements ContainerResponseFilter
{
    private static Logger LOG = LogManager.getRootLogger();

    @Override
    public void filter(ContainerRequestContext containerRequestContext, ContainerResponseContext containerResponseContext)
            throws IOException
    {
        String requestId = containerRequestContext.getProperty("request_id").toString();
        long requestStartTime = Long.parseLong(containerRequestContext.getProperty("request_start_time").toString());
        long timeTaken = System.currentTimeMillis() - requestStartTime;

        int httpStatus = containerResponseContext.getStatus();
        try
        {
            containerRequestContext.hashCode();
            String entity = (String) containerResponseContext.getEntity();
            if (entity == null || entity.isEmpty())
            {
                throw new NullPointerException();
            }

            LOG.info("[RESPONSE:" + httpStatus + ":" + requestId + ":" + timeTaken + "ms] " + containerResponseContext.getEntity());
        }
        catch (Exception e)
        {
            LOG.info("[RESPONSE:" + httpStatus + ":" + requestId + ":" + timeTaken + "ms]");
        }
    }
}
