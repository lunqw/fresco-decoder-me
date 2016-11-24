package com.yy.lqw.fresco.rf;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.annotation.NonNull;

import com.facebook.imagepipeline.animated.base.AnimatedImageFrame;
import com.yy.lqw.fresco.decoder.MEImageDecoder;

/**
 * Created by lunqingwen on 2016/11/10.
 */

public class RFFrame implements AnimatedImageFrame {
    private static final Class<?> TAG = MEImageDecoder.class;
    private final int mFrameNumber;
    private final RFDescriptor mDescriptor;
    private final Canvas mCanvas = new Canvas();

    public RFFrame(int frameNumber, @NonNull RFDescriptor descriptor) {
        mFrameNumber = frameNumber;
        mDescriptor = descriptor;
    }

    @Override
    public void dispose() {

    }

    @Override
    public void renderFrame(int width, int height, Bitmap bitmap) {
//        final long begin = System.currentTimeMillis();
        mCanvas.setBitmap(bitmap);
        final String key = mDescriptor.frames.get(mFrameNumber);
        final Bitmap bmp = mDescriptor.cache.get(key);
        mCanvas.drawBitmap(bmp, 0, 0, null);
//        final long end = System.currentTimeMillis();
//        FLog.v(TAG, "Render frame, begin: %d, end: %d, diff: %d", begin, end, end - begin);
    }

    @Override
    public int getDurationMs() {
        return 1000 / mDescriptor.fps;
    }

    @Override
    public int getWidth() {
        return mDescriptor.width;
    }

    @Override
    public int getHeight() {
        return mDescriptor.height;
    }

    @Override
    public int getXOffset() {
        return 0;
    }

    @Override
    public int getYOffset() {
        return 0;
    }
}
