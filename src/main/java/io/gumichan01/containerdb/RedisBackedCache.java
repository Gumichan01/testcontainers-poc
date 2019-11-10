package io.gumichan01.containerdb;

import com.google.gson.Gson;
import redis.clients.jedis.Jedis;
import sun.misc.Cache;

import java.util.Optional;

/**
 * An implementation of {@link Cache} that stores data in Redis.
 */
public final class RedisBackedCache {

    private final Jedis jedis;
    private final String cacheName;
    private final Gson gson;

    public RedisBackedCache(Jedis jedis, String cacheName) {
        this.jedis = jedis;
        this.cacheName = cacheName;
        this.gson = new Gson();
    }

    public RedisBackedCache(String address, Integer port) {
        this.jedis = new Jedis(address, port);
        this.cacheName = "test-redis";
        this.gson = new Gson();
    }

    public void put(String key, Object value) {
        String jsonValue = gson.toJson(value);
        this.jedis.hset(this.cacheName, key, jsonValue);
    }

    public <T> Optional<T> get(String key, Class<T> expectedClass) {
        String foundJson = this.jedis.hget(this.cacheName, key);

        if (foundJson == null) {
            return Optional.empty();
        }

        return Optional.of(gson.fromJson(foundJson, expectedClass));
    }
}
