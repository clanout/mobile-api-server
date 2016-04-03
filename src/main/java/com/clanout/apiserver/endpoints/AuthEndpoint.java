package com.clanout.apiserver.endpoints;

import com.clanout.apiserver._core.Constants;
import com.clanout.apiserver.endpoints._core.AbstractEndpoint;
import com.clanout.apiserver.error.ClanoutException;
import com.clanout.apiserver.error.Error;
import com.clanout.application.core.Module;
import com.clanout.application.module.auth.context.AuthContext;
import com.clanout.application.module.auth.domain.exception.*;
import com.clanout.application.module.auth.domain.use_case.CreateSession;
import com.clanout.application.module.auth.domain.use_case.RefreshSession;
import com.clanout.application.module.user.domain.exception.CreateUserException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;

@Path("auth")
public class AuthEndpoint extends AbstractEndpoint
{
    private AuthContext authContext;

    public AuthEndpoint()
    {
        authContext = (AuthContext) applicationContext.getContext(Module.AUTH);
    }

    @GET
    @Path("/token")
    public void createToken(@Context HttpHeaders httpHeaders, @Suspended AsyncResponse asyncResponse)
    {
        workerPool.execute(() -> {

            try
            {
                String authMethod = httpHeaders.getHeaderString(Constants.HEADER_AUTH_METHOD);
                String authToken = httpHeaders.getHeaderString(Constants.HEADER_AUTH_TOKEN);

                CreateSession createSession = authContext.createSession();

                CreateSession.Request request = new CreateSession.Request();
                request.authMethod = authMethod;
                request.authToken = authToken;

                CreateSession.Response response = createSession.execute(request);
                asyncResponse.resume(buildSuccessJsonResponse(response));
            }
            catch (InvalidAuthTokenException e)
            {
                asyncResponse.resume(new ClanoutException(Error.INVALID_AUTH_TOKEN));
            }
            catch (InvalidAuthMethodException e)
            {
                asyncResponse.resume(new ClanoutException(Error.INVALID_AUTH_METHOD));
            }
            catch (CreateUserException | CreateSessionException e)
            {
                asyncResponse.resume(new ClanoutException(Error.SESSION_CREATION_FAILED));
            }
            catch (Exception e)
            {
                asyncResponse.resume(new ClanoutException(Error.INTERNAL_SERVER_ERROR));
            }
        });
    }

    @GET
    @Path("/refresh")
    public void refreshToken(@Context HttpHeaders httpHeaders, @Suspended AsyncResponse asyncResponse)
    {
        workerPool.execute(() -> {

            try
            {
                String refreshToken = httpHeaders.getHeaderString(Constants.HEADER_REFRESH_TOKEN);

                RefreshSession refreshSession = authContext.refreshSession();

                RefreshSession.Request request = new RefreshSession.Request();
                request.refreshToken = refreshToken;

                RefreshSession.Response response = refreshSession.execute(request);
                asyncResponse.resume(buildSuccessJsonResponse(response));
            }
            catch (InvalidRefreshTokenException e)
            {
                asyncResponse.resume(new ClanoutException(Error.INVALID_REFRESH_TOKEN));
            }
            catch (RefreshSessionException e)
            {
                asyncResponse.resume(new ClanoutException(Error.SESSION_REFRESH_FAILED));
            }
            catch (Exception e)
            {
                asyncResponse.resume(new ClanoutException(Error.INTERNAL_SERVER_ERROR));
            }
        });
    }
}
