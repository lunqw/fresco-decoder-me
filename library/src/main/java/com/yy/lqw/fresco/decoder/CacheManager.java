package com.yy.lqw.fresco.decoder;

import com.facebook.cache.common.CacheKey;
import com.yy.lqw.fresco.base.AbstractDescriptor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by lunqingwen on 2017/4/22.
 */

class CacheManager {
    private static final Map<CacheKey, AbstractDescriptor> sCache = new ConcurrentHashMap<>();

    static AbstractDescriptor get(CacheKey key) {
        AbstractDescriptor item = sCache.get(key);
        return item;
    }

    static void put(CacheKey key, AbstractDescriptor item) {
        sCache.put(key, item);
    }
}
