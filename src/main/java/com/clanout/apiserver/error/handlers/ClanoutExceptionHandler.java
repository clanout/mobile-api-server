package com.clanout.apiserver.error.handlers;

import com.clanout.apiserver.error.*;
import com.clanout.apiserver.error.Error;
import com.clanout.apiserver.util.GsonProvider;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ClanoutExceptionHandler implements ExceptionMapper<ClanoutException>
{
    @Override
    public Response toResponse(ClanoutException exception)
    {
        Error error = exception.getError();
        return Response
                .status(error.getHttpStatus())
                .entity(GsonProvider.get().toJson(error))
                .type(MediaType.APPLICATION_JSON_TYPE)
                .build();
    }
}