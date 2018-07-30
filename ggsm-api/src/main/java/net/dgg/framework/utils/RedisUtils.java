package net.dgg.framework.utils;

import net.dgg.framework.redis.RedisFactory;
import redis.clients.jedis.JedisCluster;

public class RedisUtils {

    private static JedisCluster jedisCluster;

    // private static JedisPool jedisPool;

    static{
        init();
    }

    private static void init(){
        jedisCluster = RedisFactory.getJedisCluster();
        //jedisPool = RedisFactory.getJedisPool();
    }

    public static String get(String key){
        return jedisCluster.get(key);
    }

    public static void set(String key,String value){
        jedisCluster.set(key, value);
    }

    public static void expire(String key,int seconds){
        jedisCluster.expire(key, seconds);
    }

    public static void del(String key){
        jedisCluster.del(key);
    }

    public static String getRedisPriperty(String key){
        return ResourceUtils.getResource("redis").getValue(key);
    }

    public static boolean exists(String key){
        return jedisCluster.exists(key);
    }

}
