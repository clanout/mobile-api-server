package com.clanout.apiserver.request.impl;

import com.clanout.apiserver.request.core.Request;
import com.clanout.apiserver.util.GsonProvider;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class GsonRequestImpl implements Request
{
    protected JsonObject data;

    public GsonRequestImpl(JsonObject jsonObject)
    {
        data = jsonObject;
    }

    @Override
    public String get(String key)
    {
        JsonElement element = data.get(key);
        if(element != null)
        {
            return element.getAsString();
        }
        else
        {
            return null;
        }
    }

    @Override
    public List<String> getList(String key)
    {
        JsonElement element = data.get(key);
        if (element != null && element.isJsonArray())
        {
            List<String> result = new ArrayList<>();

            JsonArray array = element.getAsJsonArray();
            for (JsonElement jsonElement : array)
            {
                result.add(jsonElement.getAsString());
            }

            return result;
        }

        return null;
    }

    @Override
    public Map<String, String> getMap(String key)
    {
        JsonElement element = data.get(key);
        try
        {
            Type type = new TypeToken<Map<String, String>>()
            {
            }.getType();
            return GsonProvider.get().fromJson(element, type);
        }
        catch (Exception e)
        {
            return null;
        }
    }
}
