package com.clanout.apiserver.request;


import com.clanout.apiserver.request.core.Request;
import com.clanout.apiserver.request.core.RequestFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

@Provider
public class RequestReader implements MessageBodyReader<Request>
{
    @Override
    public boolean isReadable(Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType)
    {
        return aClass == Request.class && mediaType.isCompatible(MediaType.APPLICATION_JSON_TYPE);
    }

    @Override
    public Request readFrom(Class<Request> aClass, Type type, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> multivaluedMap, InputStream inputStream)
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
        return RequestFactory.createRequest(request);
    }
}
