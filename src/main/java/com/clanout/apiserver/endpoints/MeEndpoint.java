package com.clanout.apiserver.endpoints;

import com.clanout.apiserver.endpoints._core.AbstractEndpoint;
import com.clanout.apiserver.error.ClanoutException;
import com.clanout.apiserver.error.Error;
import com.clanout.apiserver.request.core.SessionRequest;
import com.clanout.application.core.Module;
import com.clanout.application.framework.module.InvalidFieldException;
import com.clanout.application.module.user.context.UserContext;
import com.clanout.application.module.user.domain.use_case.*;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("me")
public class MeEndpoint extends AbstractEndpoint
{
    private UserContext userContext;

    public MeEndpoint()
    {
        userContext = (UserContext) applicationContext.getContext(Module.USER);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void getUser(SessionRequest apiRequest, @Suspended AsyncResponse asyncResponse)
    {
        workerPool.execute(() -> {

            try
            {
                FetchUser fetchUser = userContext.fetchUser();

                FetchUser.Request request = new FetchUser.Request();
                request.userId = apiRequest.getSessionUser();
                FetchUser.Response response = fetchUser.execute(request);

                asyncResponse.resume(buildSuccessJsonResponse(response));
            }
            catch (InvalidFieldException e)
            {
                asyncResponse.resume(new ClanoutException(Error.of(e)));
            }
            catch (Exception e)
            {
                asyncResponse.resume(new ClanoutException(e));
            }

        });
    }

    @Path("/location")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void setLocation(SessionRequest apiRequest, @Suspended AsyncResponse asyncResponse)
    {
        workerPool.execute(() -> {

            double latitude = Double.NaN;
            double longitude = Double.NaN;

            try
            {
                latitude = Double.parseDouble(apiRequest.get("latitude"));
                longitude = Double.parseDouble(apiRequest.get("longitude"));
            }
            catch (Exception e)
            {
                asyncResponse.resume(new ClanoutException(Error.of(new InvalidFieldException("latitude/longitude"))));
            }

            try
            {
                UpdateLocation updateLocation = userContext.updateLocation();

                UpdateLocation.Request request = new UpdateLocation.Request();
                request.userId = apiRequest.getSessionUser();
                request.latitude = latitude;
                request.longitude = longitude;
                UpdateLocation.Response response = updateLocation.execute(request);

                asyncResponse.resume(buildSuccessJsonResponse(response));
            }
            catch (Exception e)
            {
                asyncResponse.resume(new ClanoutException(e));
            }

        });
    }

    @Path("/mobile")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void setMobileNumber(SessionRequest apiRequest, @Suspended AsyncResponse asyncResponse)
    {
        workerPool.execute(() -> {

            try
            {
                UpdateMobile updateMobile = userContext.updateMobile();

                UpdateMobile.Request request = new UpdateMobile.Request();
                request.userId = apiRequest.getSessionUser();
                request.mobileNumber = apiRequest.get("mobile_number");
                updateMobile.execute(request);

                asyncResponse.resume(buildEmptySuccessResponse());
            }
            catch (InvalidFieldException e)
            {
                asyncResponse.resume(new ClanoutException(Error.of(e)));
            }
            catch (Exception e)
            {
                asyncResponse.resume(new ClanoutException(e));
            }

        });
    }

    @Path("/friends")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void getFriends(SessionRequest apiRequest, @Suspended AsyncResponse asyncResponse)
    {
        workerPool.execute(() -> {

            try
            {
                FetchFriends fetchFriends = userContext.fetchFriends();

                FetchFriends.Request request = new FetchFriends.Request();
                request.userId = apiRequest.getSessionUser();
                request.locationZone = apiRequest.get("location_zone");
                FetchFriends.Response response = fetchFriends.execute(request);

                asyncResponse.resume(buildSuccessJsonResponse(response));
            }
            catch (InvalidFieldException e)
            {
                asyncResponse.resume(new ClanoutException(Error.of(e)));
            }
            catch (Exception e)
            {
                asyncResponse.resume(new ClanoutException(e));
            }

        });
    }

    @Path("/block")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void blockFriends(SessionRequest apiRequest, @Suspended AsyncResponse asyncResponse)
    {
        workerPool.execute(() -> {

            try
            {
                List<String> toBlock = apiRequest.getList("to_block");
                List<String> toUnblock = apiRequest.getList("to_unblock");

                BlockFriends blockFriends = userContext.blockFriends();

                BlockFriends.Request request = new BlockFriends.Request();
                request.userId = apiRequest.getSessionUser();
                request.toBlock = toBlock;
                request.toUnblock = toUnblock;
                blockFriends.execute(request);

                asyncResponse.resume(buildEmptySuccessResponse());
            }
            catch (Exception e)
            {
                asyncResponse.resume(new ClanoutException(e));
            }

        });
    }

    @Path("/registered-contacts")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void getRegisteredContacts(SessionRequest apiRequest, @Suspended AsyncResponse asyncResponse)
    {
        workerPool.execute(() -> {

            try
            {
                FetchRegisteredContacts fetchRegisteredContacts = userContext.fetchRegisteredContacts();

                FetchRegisteredContacts.Request request = new FetchRegisteredContacts.Request();
                request.userId = apiRequest.getSessionUser();
                request.mobileHash = apiRequest.getList("mobile_numbers");
                request.locationZone = apiRequest.get("location_zone");
                FetchRegisteredContacts.Response response = fetchRegisteredContacts.execute(request);

                asyncResponse.resume(buildSuccessJsonResponse(response));
            }
            catch (Exception e)
            {
                asyncResponse.resume(new ClanoutException(e));
            }

        });
    }
}
