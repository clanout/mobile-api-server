package com.clanout.apiserver.util;

import com.clanout.apiserver.error.*;
import com.clanout.apiserver.error.Error;
import com.google.gson.*;

import java.lang.reflect.Type;

public class GsonProvider
{
    private static Gson gson;

    static
    {
        gson = com.clanout.application.library.util.gson.GsonProvider
                .getGsonBuilder()
                .registerTypeAdapter(Error.class, new ErrorSerializer())
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
    }

    public static Gson get()
    {
        return gson;
    }

    public static class ErrorSerializer implements JsonSerializer<Error>
    {
        @Override
        public JsonElement serialize(Error error, Type type, JsonSerializationContext jsonSerializationContext)
        {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("status", error.getHttpStatus());
            jsonObject.addProperty("code", error.getCode());
            jsonObject.addProperty("message", error.getMessage());

            return jsonObject;
        }
    }
}
