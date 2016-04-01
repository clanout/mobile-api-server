package com.clanout.apiserver.request.impl;

import com.clanout.apiserver.request.core.SessionRequest;
import com.google.gson.JsonObject;

public class GsonSessionRequestImpl extends GsonRequestImpl implements SessionRequest
{
    private String sessionUser;

    public GsonSessionRequestImpl(JsonObject jsonObject, String sessionUser)
    {
        super(jsonObject);
        this.sessionUser = sessionUser;
    }

    @Override
    public String getSessionUser()
    {
        return sessionUser;
    }
}
