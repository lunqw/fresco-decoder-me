package com.yy.lqw.fresco;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lunqingwen on 2016/11/10.
 */

abstract class AbstractDescriptor implements Serializable {
    // preview
    public String preview;

    // 位图缓存
    public transient Map<String, Bitmap> cache;

    public void initialize() {
        cache = new HashMap<>();
    }
}
