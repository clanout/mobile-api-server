package com.clanout.apiserver.endpoints;

import com.clanout.apiserver.endpoints._core.AbstractEndpoint;
import com.clanout.apiserver.error.ClanoutException;
import com.clanout.apiserver.error.Error;
import com.clanout.apiserver.request.core.SessionRequest;
import com.clanout.application.core.Module;
import com.clanout.application.framework.module.InvalidFieldException;
import com.clanout.application.module.notification.context.NotificationContext;
import com.clanout.application.module.notification.domain.use_case.RegisterGcmToken;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;

@Path("notification")
public class NotificationEndpoint extends AbstractEndpoint
{
    private NotificationContext notificationContext;

    public NotificationEndpoint()
    {
        notificationContext = (NotificationContext) applicationContext.getContext(Module.NOTIFICATION);
    }

    @Path("/register-gcm")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void registerGcm(SessionRequest apiRequest, @Suspended AsyncResponse asyncResponse)
    {
        workerPool.execute(() -> {

            try
            {
                RegisterGcmToken registerGcmToken = notificationContext.registerGcmToken();

                RegisterGcmToken.Request request = new RegisterGcmToken.Request();
                request.userId = apiRequest.getSessionUser();
                request.gcmToken = apiRequest.get("gcm_token");
                registerGcmToken.execute(request);

                asyncResponse.resume(buildEmptySuccessResponse());
            }
            catch (InvalidFieldException e)
            {
                asyncResponse.resume(new ClanoutException(com.clanout.apiserver.error.Error.INVALID_INPUT_FIELDS));
            }
            catch (Exception e)
            {
                asyncResponse.resume(new ClanoutException(Error.INTERNAL_SERVER_ERROR));
            }

        });
    }
}
