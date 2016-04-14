package com.clanout.apiserver.logging;

import com.clanout.apiserver._core.Constants;
import com.clanout.apiserver._core.ServerContext;
import com.clanout.apiserver.error.*;
import com.clanout.application.core.Module;
import com.clanout.application.module.auth.context.AuthContext;
import com.clanout.application.module.auth.domain.use_case.VerifySession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.HttpHeaders;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.UUID;

@PreMatching
public class RequestLogger implements ContainerRequestFilter
{
    private static Logger LOG = LogManager.getRootLogger();

    private static final String REQUEST_LOG_FORMAT = "[REQUEST] %s %s %s <<<< %s";
    private static final String REQUEST_LOG_FORMAT_NO_BODY = "[REQUEST] %s %s %s";

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException
    {
        String requestId = getRequestId(containerRequestContext.getHeaderString(Constants.HEADER_ACCESS_TOKEN));
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

            LOG.info(formatLog(requestId, "POST", uri, request));

            containerRequestContext.setEntityStream(new ByteArrayInputStream(request.getBytes()));
        }
        else
        {
            if (!uri.equals("health-check"))
            {
                LOG.info(formatLog(requestId, "GET", uri, null));
            }
        }
    }

    private String getRequestId(String accessToken)
    {
        String sessionUser = null;
        if (accessToken != null && !accessToken.isEmpty())
        {
            AuthContext authContext = (AuthContext) ServerContext.getInstance().getApplicationContext().getContext(Module.AUTH);
            VerifySession verifySession = authContext.verifySession();

            VerifySession.Request request = new VerifySession.Request();
            request.accessToken = accessToken;
            VerifySession.Response response = verifySession.execute(request);

            sessionUser = response.userId;
        }
        else
        {
            sessionUser = "unidentified_user_" + ((int) (Math.random() * 100000));
        }

        return sessionUser + "_" + System.nanoTime();
    }

    private String formatLog(String requestId, String httpMethod, String uri, String body)
    {
        if (body == null || body.isEmpty())
        {
            return String.format(REQUEST_LOG_FORMAT_NO_BODY, requestId, httpMethod, uri);
        }
        else
        {
            return String.format(REQUEST_LOG_FORMAT, requestId, httpMethod, uri, body);
        }
    }
}
