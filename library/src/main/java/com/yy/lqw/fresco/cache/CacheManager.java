package com.yy.lqw.fresco.cache;

import com.facebook.cache.common.CacheKey;
import com.facebook.common.internal.Preconditions;
import com.facebook.common.references.CloseableReference;
import com.facebook.imagepipeline.cache.MemoryCache;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.facebook.imagepipeline.image.CloseableImage;
import com.yy.lqw.fresco.base.AbstractDescriptor;

/**
 * Created by lunqingwen on 2017/4/23.
 */

public class CacheManager {
    private static MemoryCache<CacheKey, CloseableImage> sMemoryCache;

    public static AbstractDescriptor get(CacheKey cacheKey) {
        Preconditions.checkNotNull(cacheKey);

        if (sMemoryCache == null) {
            sMemoryCache = ImagePipelineFactory.getInstance().getBitmapMemoryCache();
        }

        AbstractDescriptor descriptor = null;
        final CloseableReference<CloseableImage> ref = sMemoryCache.get(cacheKey);
        if (ref != null && ref.isValid()) {
            descriptor = (AbstractDescriptor) ref.get();
        }
        CloseableReference.closeSafely(ref);
        return descriptor;
    }

    public static void cache(CacheKey cacheKey, AbstractDescriptor descriptor) {
        Preconditions.checkNotNull(cacheKey);
        Preconditions.checkNotNull(descriptor);

        final CloseableReference<CloseableImage> ref =
                CloseableReference.of((CloseableImage) descriptor);
        CloseableReference.closeSafely(sMemoryCache.cache(cacheKey, ref));
        CloseableReference.closeSafely(ref);
    }
}
