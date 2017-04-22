package com.yy.lqw.fresco.base;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by lunqingwen on 2016/11/10.
 */

public abstract class AbstractDescriptor implements Serializable {
    // preview
    public String preview;

    // 位图缓存
    public transient Map<String, Bitmap> cache;
}
