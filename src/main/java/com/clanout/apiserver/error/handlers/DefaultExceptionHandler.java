package com.clanout.apiserver.error.handlers;

import com.clanout.apiserver.error.*;
import com.clanout.apiserver.error.Error;
import com.clanout.apiserver.util.GsonProvider;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class DefaultExceptionHandler implements ExceptionMapper<Throwable>
{
    @Override
    public Response toResponse(Throwable throwable)
    {
        throwable.printStackTrace();

        if (!(throwable instanceof WebApplicationException))
        {
            Error error = Error.INTERNAL_SERVER_ERROR;
            return Response
                    .status(error.getHttpStatus())
                    .entity(GsonProvider.get().toJson(error))
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .build();
        }
        else
        {
            WebApplicationException exception = (WebApplicationException) throwable;
            int status = exception.getResponse().getStatus();

            Error error = Error.INTERNAL_SERVER_ERROR;
            switch (status)
            {
                case 400:
                    error = Error.BAD_REQUEST;
                    break;

                case 401:
                    error = Error.UNAUTHORIZED;
                    break;

                case 403:
                    error = Error.FORBIDDEN;
                    break;

                case 404:
                    error = Error.NOT_FOUND;
                    break;

                case 405:
                    error = Error.METHOD_NOT_ALLOWED;
                    break;
            }

            return Response
                    .status(error.getHttpStatus())
                    .entity(GsonProvider.get().toJson(error))
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .build();
        }
    }
}
