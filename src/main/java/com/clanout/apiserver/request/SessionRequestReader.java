package com.clanout.apiserver.request;

import com.clanout.apiserver.request.core.RequestFactory;
import com.clanout.apiserver.request.core.SessionRequest;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

public class SessionRequestReader implements MessageBodyReader<SessionRequest>
{
    @Context
    HttpHeaders httpHeaders;

    @Override
    public boolean isReadable(Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType)
    {
        return aClass == SessionRequest.class && mediaType.isCompatible(MediaType.APPLICATION_JSON_TYPE);
    }

    @Override
    public SessionRequest readFrom(Class<SessionRequest> aClass, Type type, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> multivaluedMap, InputStream inputStream)
            throws IOException, WebApplicationException
    {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder requestBuilder = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null)
        {
            requestBuilder.append(line.trim());
        }
        String request = requestBuilder.toString();
        return RequestFactory.createSessionRequest(httpHeaders, request);
    }
}
