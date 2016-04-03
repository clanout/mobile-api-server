package com.clanout.apiserver.error;

import com.google.gson.annotations.SerializedName;

public enum Error
{
    /* Default HTTP Errors */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 0, "Internal Server Error"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, 0, "Bad Request"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, 0, "Unauthorized"),
    FORBIDDEN(HttpStatus.FORBIDDEN, 0, "Forbidden"),
    NOT_FOUND(HttpStatus.NOT_FOUND, 0, "Not Found"),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, 0, "Method Not Allowed"),

    /* Input Processing Errors */
    INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, 666, "Invalid access token"),
    MALFORMED_JSON(HttpStatus.BAD_REQUEST, 1000, "Malformed input"),

    INVALID_AUTH_TOKEN(HttpStatus.BAD_REQUEST, 1001, "Invalid auth token"),
    INVALID_AUTH_METHOD(HttpStatus.BAD_REQUEST, 1002, "Invalid auth method"),
    SESSION_CREATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, 1003, "Session creation failed"),
    INVALID_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, 1004, "Invalid refresh token"),
    SESSION_REFRESH_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, 1005, "Session refresh failed"),

    INVALID_INPUT_FIELDS(HttpStatus.BAD_REQUEST, 1006, "Invalid input fields")
    ;

    @SerializedName("status")
    private int httpStatus;

    @SerializedName("code")
    private int code;

    @SerializedName("message")
    private String message;

    Error(int httpStatus, int code, String message)
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
