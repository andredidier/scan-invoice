package com.lealdidier.io;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;

import java.io.IOException;
import java.time.Duration;

public class Cacheable<T, R> implements IOFunction<T, R> {
    private final String cacheName;
    private final Class<T> tClass;
    private final Class<R> rClass;
    private final IOFunction<T,R> inner;
    private final long entries;
    private final IOFunction<CacheConfigurationBuilder<T,R>, CacheConfigurationBuilder<T,R>> configurationBuilder;

    private static transient CacheManager manager;

    public Cacheable(String cacheName, Class<T> tClass, Class<R> rClass, IOFunction<T, R> inner,
                     long entries, IOFunction<CacheConfigurationBuilder<T, R>, CacheConfigurationBuilder<T, R>> configurationBuilder) {
        this.cacheName = cacheName;
        this.tClass = tClass;
        this.rClass = rClass;
        this.inner = inner;
        this.entries = entries;
        this.configurationBuilder = configurationBuilder;
    }

    public Cacheable(String cacheName, Class<T> tClass, Class<R> rClass, IOFunction<T, R> inner, long entries) {
        this(cacheName, tClass, rClass, inner, entries, IOFunction.identity());
    }

    public Cacheable(String cacheName, Class<T> tClass, Class<R> rClass, IOFunction<T, R> inner, long entries, Duration timeToIdle) {
        this(cacheName, tClass, rClass, inner, entries, b -> b.withExpiry(ExpiryPolicyBuilder.timeToIdleExpiration(timeToIdle)));
    }

    private Cache<T,R> cache() throws IOException {
        synchronized (Cacheable.class) {
            if (manager == null) {
                manager = CacheManagerBuilder.newCacheManagerBuilder().build(true);
            }
        }
        synchronized (manager) {
            Cache cache = manager.getCache(cacheName, tClass, rClass);
            if (cache == null) {
                cache = manager.createCache(cacheName,
                        configurationBuilder
                                .apply(CacheConfigurationBuilder.newCacheConfigurationBuilder(
                                        tClass, rClass, ResourcePoolsBuilder.heap(entries))).build());
            }
            return cache;
        }
    }

    @Override
    public R apply(T t) throws IOException {
        Cache<T,R> c = cache();
        R r = c.get(t);
        if (r == null) {
            r = inner.apply(t);
            c.put(t, r);
        }
        return r;
    }
}
