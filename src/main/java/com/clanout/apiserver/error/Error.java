package com.clanout.apiserver.error;

import com.clanout.application.framework.module.InvalidFieldException;
import com.google.gson.annotations.SerializedName;

public class Error
{
    /* Default HTTP Errors */
    public static final Error INTERNAL_SERVER_ERROR = new Error(HttpStatus.INTERNAL_SERVER_ERROR, 0, "Internal Server Error");
    public static final Error BAD_REQUEST = new Error(HttpStatus.BAD_REQUEST, 0, "Bad Request");
    public static final Error UNAUTHORIZED = new Error(HttpStatus.UNAUTHORIZED, 0, "Unauthorized");
    public static final Error FORBIDDEN = new Error(HttpStatus.FORBIDDEN, 0, "Forbidden");
    public static final Error NOT_FOUND = new Error(HttpStatus.NOT_FOUND, 0, "Not Found");
    public static final Error METHOD_NOT_ALLOWED = new Error(HttpStatus.METHOD_NOT_ALLOWED, 0, "Method Not Allowed");

    /* Input Processing Errors */
    public static final Error INVALID_ACCESS_TOKEN = new Error(HttpStatus.UNAUTHORIZED, 1001, "Invalid access token");
    public static final Error MALFORMED_JSON = new Error(HttpStatus.BAD_REQUEST, 1002, "Malformed input");

    public static Error of(InvalidFieldException e)
    {
        String message = "Invalid " + e.getFieldName();
        return new Error(HttpStatus.BAD_REQUEST, 1003, message);
    }

    /* Auth Errors */
    public static final Error INVALID_AUTH_TOKEN = new Error(HttpStatus.BAD_REQUEST, 2001, "Invalid auth token");
    public static final Error INVALID_AUTH_METHOD = new Error(HttpStatus.BAD_REQUEST, 2002, "Invalid auth method");
    public static final Error SESSION_CREATION_FAILED = new Error(HttpStatus.INTERNAL_SERVER_ERROR, 2003, "Session creation failed");
    public static final Error INVALID_REFRESH_TOKEN = new Error(HttpStatus.BAD_REQUEST, 2004, "Invalid refresh token");
    public static final Error SESSION_REFRESH_FAILED = new Error(HttpStatus.INTERNAL_SERVER_ERROR, 2005, "Session refresh failed");

    /* User Errors */
    public static final Error NO_PROFILE_IMAGE = new Error(HttpStatus.NOT_FOUND, 3001, "No profile pic");

    /* Plan Errors */
    public static final Error PLAN_NOT_FOUND = new Error(HttpStatus.NOT_FOUND, 4001, "Plan not found");
    public static final Error PLAN_EDIT_PERMISSION_DENIED = new Error(HttpStatus.FORBIDDEN, 4002, "Only attendees can edit plans");
    public static final Error PLAN_DELETE_PERMISSION_DENIED = new Error(HttpStatus.FORBIDDEN, 4003, "Only the creator can delete the plan");


    @SerializedName("status")
    private int httpStatus;

    @SerializedName("code")
    private int code;

    @SerializedName("message")
    private String message;

    private Error(int httpStatus, int code, String message)
    {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }

    public int getHttpStatus()
    {
        return httpStatus;
    }

    public int getCode()
    {
        return code;
    }

    public String getMessage()
    {
        return message;
    }
}
