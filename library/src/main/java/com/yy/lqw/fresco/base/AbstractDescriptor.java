package com.yy.lqw.fresco.base;

import android.graphics.Bitmap;

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
        return 0;
    }

    @Override
    public void close() {

    }

    @Override
    public boolean isClosed() {
        return false;
    }
}
