package com.yy.lqw.fresco.decoder;

import com.facebook.cache.common.CacheKey;
import com.yy.lqw.fresco.base.AbstractDescriptor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by lunqingwen on 2017/4/22.
 */

class CacheManager {
    private static final Map<CacheKey, CacheItem> sCache = new ConcurrentHashMap<>();

    private CacheManager() {

    }

    static CacheItem get(CacheKey key) {
        CacheItem item = sCache.get(key);
        return item;
    }

    static void put(CacheKey key, CacheItem item) {
        sCache.put(key, item);
    }

    static class CacheItem {
        MEImageFormats.MEImageFormat mFormat;
        AbstractDescriptor mDescriptor;

        public CacheItem(MEImageFormats.MEImageFormat format, AbstractDescriptor descriptor) {
            mFormat = format;
            mDescriptor = descriptor;
        }
    }
}
