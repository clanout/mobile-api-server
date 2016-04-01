package com.clanout.apiserver.request.core;

import com.clanout.apiserver._core.Constants;
import com.clanout.apiserver.request.impl.GsonRequestImpl;
import com.clanout.apiserver.request.impl.GsonSessionRequestImpl;
import com.google.gson.JsonParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.core.HttpHeaders;

public class RequestFactory
{
    private static Logger LOG = LogManager.getRootLogger();

    public static SessionRequest createSessionRequest(HttpHeaders httpHeaders, String json)
    {
        String sessionUser;
        try
        {
            sessionUser = httpHeaders.getRequestHeader(Constants.HEADER_SESSION_USER).get(0);
            if (sessionUser == null || sessionUser.isEmpty())
            {
                throw new NullPointerException();
            }
        }
        catch (NullPointerException e)
        {
            throw new NotAuthorizedException("Session Header missing");
        }

        try
        {
            if (json == null || json.isEmpty())
            {
                return new GsonSessionRequestImpl(new JsonParser().parse("{}").getAsJsonObject(), sessionUser);
            }
            else
            {
                return new GsonSessionRequestImpl(new JsonParser().parse(json).getAsJsonObject(), sessionUser);
            }
        }
        catch (Exception e)
        {
            LOG.error("Malformed input json", e);
            throw new BadRequestException("Malformed input json");
        }
    }

    public static Request createRequest(String json)
    {
        try
        {
            if (json == null || json.isEmpty())
            {
                return new GsonRequestImpl(new JsonParser().parse("{}").getAsJsonObject());
            }
            else
            {
                return new GsonRequestImpl(new JsonParser().parse(json).getAsJsonObject());
            }
        }
        catch (Exception e)
        {
            LOG.error("Malformed input json", e);
            throw new BadRequestException("Malformed input json");
        }
    }
}
