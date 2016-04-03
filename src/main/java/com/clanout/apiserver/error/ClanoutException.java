package com.clanout.apiserver.error;

public class ClanoutException extends RuntimeException
{
    private Error error;

    public ClanoutException(Error error)
    {
        this.error = error;
    }

    public Error getError()
    {
        return error;
    }
}
