package com.clanout.apiserver.error;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ClanoutException extends RuntimeException
{
    private static Logger LOG = LogManager.getRootLogger();

    private Error error;

    public ClanoutException(Error error)
    {
        this.error = error;
    }

    public ClanoutException(Exception e)
    {
        this.error = Error.INTERNAL_SERVER_ERROR;
        LOG.error("[SERVER ERROR]", e);
    }

    public Error getError()
    {
        return error;
    }
}
