package com.yy.lqw.fresco.descriptor;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lunqingwen on 2016/11/10.
 */

public abstract class AbstractDescriptor implements Serializable {
    // 位图缓存
    public transient Map<String, Bitmap> cache;

    public void initialize() {
        cache = new HashMap<>();
    }
}
