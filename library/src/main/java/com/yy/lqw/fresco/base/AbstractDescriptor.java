package com.yy.lqw.fresco.base;

import android.graphics.Bitmap;

import com.facebook.common.internal.Preconditions;
import com.facebook.imagepipeline.image.CloseableImage;
import com.yy.lqw.fresco.format.MEImageFormats;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by lunqingwen on 2016/11/10.
 */

public abstract class AbstractDescriptor extends CloseableImage implements Serializable {

    // Bitmap缓存
    public transient Map<String, Bitmap> cache;

    // 图片格式
    public transient MEImageFormats.MEImageFormat format;

    @Override
    public int getSizeInBytes() {
        int size = 0;
        if (cache != null && !cache.isEmpty()) {
            for (Bitmap bitmap : cache.values()) {
                size += (bitmap.getWidth() * bitmap.getHeight() * getBitmapBytesPerPixel(bitmap));
            }
        }
        return size;
    }

    @Override
    public void close() {
        cache = null;
        format = null;
    }

    @Override
    public boolean isClosed() {
        return cache == null;
    }

    private int getBitmapBytesPerPixel(Bitmap bitmap) {
        Preconditions.checkNotNull(bitmap);
        int size = 0;
        switch (bitmap.getConfig()) {
            case ARGB_8888:
                size = 4;
                break;
            case ARGB_4444:
            case RGB_565:
                size = 2;
                break;
            case ALPHA_8:
                size = 1;
                break;
            default:
                size = 4;
        }
        return size;
    }
}
