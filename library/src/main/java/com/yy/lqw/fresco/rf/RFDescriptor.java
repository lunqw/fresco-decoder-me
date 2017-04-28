package com.yy.lqw.fresco.rf;


import android.graphics.Bitmap;

import com.yy.lqw.fresco.base.AbstractDescriptor;

import java.util.List;

/**
 * Created by lunqingwen on 2016/11/10.
 */

public class RFDescriptor extends AbstractDescriptor {
    // fps
    public int fps;

    // frames
    public List<String> frames;

    // preview
    public String preview;

    // width
    private int width = 0;

    // height
    private int height = 0;

    @Override
    public int getWidth() {
        if (width == 0) {
            initSize();
        }
        return width;
    }

    @Override
    public int getHeight() {
        if (height == 0) {
            initSize();
        }
        return height;
    }

    private void initSize() {
        if (cache != null && !cache.values().isEmpty()) {
            final Bitmap bitmap = cache.values().iterator().next();
            width = bitmap.getWidth();
            height = bitmap.getHeight();
        }
    }
}
