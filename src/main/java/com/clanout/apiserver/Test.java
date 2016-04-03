package com.clanout.apiserver;

import com.clanout.application.library.redis.RedisDataSource;
import redis.clients.jedis.Jedis;

public class Test
{

    public static void main(String[] args) throws Exception
    {
        RedisDataSource.getInstance().init();

        Jedis redis = RedisDataSource.getInstance().getConnection();
        System.out.println(redis.get("profile_image:9276fdbb-df34-44a6-93b4-f26147738227"));

        RedisDataSource.getInstance().close();
    }
}
