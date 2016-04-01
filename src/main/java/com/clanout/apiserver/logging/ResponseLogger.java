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
        try
        {
            containerRequestContext.hashCode();
            String entity = (String) containerResponseContext.getEntity();
            if (entity == null || entity.isEmpty())
            {
                throw new NullPointerException();
            }

            LOG.info("[RESPONSE:" + requestId + "] " + containerResponseContext.getEntity());
        }
        catch (Exception e)
        {
            int status = containerResponseContext.getStatus();
            String body = "HTTP Status = " + status;

            switch (status)
            {
                case 200:
                    body = "SUCCESS";
                    break;
                case 400:
                    body = "BAD REQUEST";
                    break;
                case 401:
                    body = "UNAUTHORIZED";
                    break;
                case 403:
                    body = "FORBIDDEN";
                    break;
                case 404:
                    body = "NOT FOUND";
                    break;
            }
            LOG.info("[RESPONSE:" + requestId + "] " + body);
        }
    }
}
