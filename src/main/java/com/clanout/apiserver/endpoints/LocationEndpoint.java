package com.clanout.apiserver.endpoints;

import com.clanout.apiserver.endpoints._core.AbstractEndpoint;
import com.clanout.apiserver.error.*;
import com.clanout.apiserver.error.Error;
import com.clanout.application.core.Module;
import com.clanout.application.framework.module.InvalidFieldException;
import com.clanout.application.module.location.context.LocationContext;
import com.clanout.application.module.location.domain.use_case.GetZone;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;

@Path("location")
public class LocationEndpoint extends AbstractEndpoint
{
    private LocationContext locationContext;

    public LocationEndpoint()
    {
        locationContext = (LocationContext) applicationContext.getContext(Module.LOCATION);
    }

    @GET
    public void getZone(@QueryParam("latitude") String latitudeStr, @QueryParam("longitude") String longitudeStr,
                        @Suspended AsyncResponse asyncResponse)
    {
        workerPool.execute(() -> {

            try
            {
                double latitude = 0.0;
                double longitude = 0.0;

                try
                {
                    latitude = Double.parseDouble(latitudeStr);
                    longitude = Double.parseDouble(longitudeStr);
                }
                catch (Exception e)
                {
                    asyncResponse.resume(new ClanoutException(Error.of(new InvalidFieldException("latitude/longitude"))));
                }

                GetZone getZone = locationContext.getZone();

                GetZone.Request request = new GetZone.Request();
                request.latitude = latitude;
                request.longitude = longitude;

                GetZone.Response response = getZone.execute(request);
                asyncResponse.resume(buildSuccessJsonResponse(response));
            }
            catch (Exception e)
            {
                asyncResponse.resume(new ClanoutException(e));
            }

        });
    }
}
