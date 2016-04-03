package com.clanout.apiserver.endpoints;

import com.clanout.apiserver.endpoints._core.AbstractEndpoint;
import com.clanout.apiserver.error.*;
import com.clanout.apiserver.error.Error;
import com.clanout.application.core.Module;
import com.clanout.application.module.image.context.ImageContext;
import com.clanout.application.module.image.domain.exception.NoProfileImageException;
import com.clanout.application.module.image.domain.use_case.FetchProfileImage;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

@Path("images")
public class ImageEndpoint extends AbstractEndpoint
{
    ImageContext imageContext;

    public ImageEndpoint()
    {
        imageContext = (ImageContext) applicationContext.getContext(Module.IMAGE);
    }

    @GET
    @Path("/profile_pic/{user_id}")
    public void getProfilePic(@PathParam("user_id") String userId, @Context UriInfo uriInfo, @Suspended AsyncResponse asyncResponse)
    {
        workerPool.execute(() -> {

            try
            {
                FetchProfileImage fetchProfileImage = imageContext.fetchProfileImage();

                FetchProfileImage.Request request = new FetchProfileImage.Request();
                request.userId = userId;
                FetchProfileImage.Response response = fetchProfileImage.execute(request);

                String profileImageUrl = response.profileImageUrl + "?" + uriInfo.getRequestUri().getRawQuery();
                asyncResponse.resume(redirect(profileImageUrl));
            }
            catch (NoProfileImageException e)
            {
                asyncResponse.resume(new ClanoutException(Error.NO_PROFILE_IMAGE));
            }
            catch (Exception e)
            {
                asyncResponse.resume(new ClanoutException(Error.INTERNAL_SERVER_ERROR));
            }

        });
    }
}
