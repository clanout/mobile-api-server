package com.clanout.apiserver._core;

public final class Constants
{
    public static final String VERSION = "v0.9";
    public static final String BASE_URI = "/clanout/" + VERSION + "/";

    public static final String HEADER_ACCESS_TOKEN = "Authorization";
    public static final String HEADER_REFRESH_TOKEN = "Authorization";
    public static final String HEADER_AUTH_METHOD = "X-Authentication-Method";
    public static final String HEADER_AUTH_TOKEN = "X-Authentication-Token";

    private Constants()
    {
    }
}
