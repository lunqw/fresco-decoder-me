package com.yy.lqw.fresco.cache;

import com.facebook.imagepipeline.animated.base.AnimatedImage;
import com.yy.lqw.fresco.base.AbstractAnimatedImage;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by lunqingwen on 2016/11/11.
 */

public class AnimatedMemoryCache {
    private static final Map<String, AbstractAnimatedImage> sCache = new ConcurrentHashMap<>();

    public static AbstractAnimatedImage get(String key) {
        return sCache.get(key);
    }

    public static void put(String key, AbstractAnimatedImage image) {
        sCache.put(key, image);
    }
}
