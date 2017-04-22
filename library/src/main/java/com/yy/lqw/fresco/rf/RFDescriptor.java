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

    // width
    private int width = 0;

    // height
    private int height = 0;

    public int getWidth() {
        if (cache != null) {
            for (Bitmap bitmap : cache.values()) {
                width = bitmap.getWidth();
                break;
            }
        }
        return width;
    }

    public int getHeight() {
        if (cache != null) {
            for (Bitmap bitmap : cache.values()) {
                height = bitmap.getHeight();
                break;
            }
        }
        return height;
    }
}
