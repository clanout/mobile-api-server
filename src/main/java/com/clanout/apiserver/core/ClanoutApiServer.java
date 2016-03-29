package com.clanout.apiserver.core;


import com.clanout.apiserver.endpoints.HealthEndpoint;

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

        /* Endpoints */
        resources.add(HealthEndpoint.class);

        return resources;
    }
}
