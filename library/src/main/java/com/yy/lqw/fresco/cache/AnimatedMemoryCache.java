package com.yy.lqw.fresco.cache;

import com.facebook.imagepipeline.animated.base.AnimatedImage;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by lunqingwen on 2016/11/11.
 */

public class AnimatedMemoryCache {
    private static final Map<String, AnimatedImage> sCache = new ConcurrentHashMap<>();

    public static AnimatedImage get(String key) {
        return sCache.get(key);
    }

    public static void put(String key, AnimatedImage image) {
        sCache.put(key, image);
    }
}
