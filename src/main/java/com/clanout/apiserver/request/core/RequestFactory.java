package com.clanout.apiserver.request.core;

import com.clanout.apiserver._core.Constants;
import com.clanout.apiserver._core.ServerContext;
import com.clanout.apiserver.error.ClanoutException;
import com.clanout.apiserver.error.Error;
import com.clanout.apiserver.request.impl.GsonRequestImpl;
import com.clanout.apiserver.request.impl.GsonSessionRequestImpl;
import com.clanout.application.core.Module;
import com.clanout.application.module.auth.context.AuthContext;
import com.clanout.application.module.auth.domain.use_case.VerifySession;
import com.google.gson.JsonParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.core.HttpHeaders;

public class RequestFactory
{
    private static Logger LOG = LogManager.getRootLogger();

    public static SessionRequest createSessionRequest(HttpHeaders httpHeaders, String json)
    {
        String accessToken = null;

        try
        {
            accessToken = httpHeaders.getRequestHeader(Constants.HEADER_ACCESS_TOKEN).get(0);
            if (accessToken == null || accessToken.isEmpty())
            {
                throw new NullPointerException();
            }
        }
        catch (Exception e)
        {
            throw new ClanoutException(Error.INVALID_ACCESS_TOKEN);
        }

        String sessionUser = null;
        AuthContext authContext = (AuthContext) ServerContext.getInstance().getApplicationContext().getContext(Module.AUTH);
        VerifySession verifySession = authContext.verifySession();

        VerifySession.Request request = new VerifySession.Request();
        request.accessToken = accessToken;
        VerifySession.Response response = verifySession.execute(request);

        sessionUser = response.userId;
        if (sessionUser == null)
        {
            throw new ClanoutException(Error.INVALID_ACCESS_TOKEN);
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
            LOG.error("Malformed input json [" + e + "]");
            throw new ClanoutException(Error.MALFORMED_JSON);
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
            LOG.error("Malformed input json [" + e + "]");
            throw new ClanoutException(Error.MALFORMED_JSON);
        }
    }
}
