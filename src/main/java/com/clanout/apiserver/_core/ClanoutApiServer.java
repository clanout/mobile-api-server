package com.clanout.apiserver._core;


import com.clanout.apiserver.endpoints.*;
import com.clanout.apiserver.error.handlers.ClanoutExceptionHandler;
import com.clanout.apiserver.error.handlers.DefaultExceptionHandler;
import com.clanout.apiserver.logging.RequestLogger;
import com.clanout.apiserver.logging.ResponseLogger;
import com.clanout.apiserver.request.RequestReader;
import com.clanout.apiserver.request.SessionRequestReader;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath(Constants.BASE_URI)
public class ClanoutApiServer extends Application
{
    public ClanoutApiServer()
    {
    }

    @Override
    public Set<Class<?>> getClasses()
    {
        Set<Class<?>> resources = new HashSet<>();

        /* Logger */
        resources.add(RequestLogger.class);
        resources.add(ResponseLogger.class);

        /* Request Parsers */
        resources.add(RequestReader.class);
        resources.add(SessionRequestReader.class);

        /* Error Mappers */
        resources.add(ClanoutExceptionHandler.class);
        resources.add(DefaultExceptionHandler.class);

        /* Endpoints */
        resources.add(HealthEndpoint.class);
        resources.add(LocationEndpoint.class);
        resources.add(AuthEndpoint.class);
        resources.add(MeEndpoint.class);
        resources.add(ImageEndpoint.class);

        return resources;
    }
}
