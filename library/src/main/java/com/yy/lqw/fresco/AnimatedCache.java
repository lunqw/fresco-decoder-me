package com.yy.lqw.fresco;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by lunqingwen on 2016/11/11.
 */

class AnimatedCache {
    private static final Map<String, AbstractAnimatedImage> sCache = new ConcurrentHashMap<>();

    public static AbstractAnimatedImage get(String key) {
        return sCache.get(key);
    }

    public static void put(String key, AbstractAnimatedImage image) {
        sCache.put(key, image);
    }
}
