package com.yy.lqw.fresco.rf;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.facebook.imagepipeline.animated.base.AnimatedDrawableFrameInfo;
import com.facebook.imagepipeline.animated.base.AnimatedImage;
import com.facebook.imagepipeline.animated.base.AnimatedImageFrame;

import java.util.Arrays;

/**
 * Created by lunqingwen on 2016/11/10.
 */

public class RFImage implements AnimatedImage {
    private final RFDescriptor mDescriptor;

    public RFImage(@NonNull RFDescriptor descriptor) {
        mDescriptor = descriptor;
        if (descriptor.width == 0 || descriptor.height == 0) {
            for (Bitmap bitmap : descriptor.cache.values()) {
                descriptor.width = bitmap.getWidth();
                descriptor.height = bitmap.getHeight();
                break;
            }
        }
    }

    @Override
    public void dispose() {

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
    public int getFrameCount() {
        return mDescriptor.frames.size();
    }

    @Override
    public int getDuration() {
        final float duration = 1000.0f / mDescriptor.fps * getFrameCount();
        return (int) duration;
    }

    @Override
    public int[] getFrameDurations() {
        int[] durations = new int[getFrameCount()];
        Arrays.fill(durations, 1000 / mDescriptor.fps);
        return durations;
    }

    @Override
    public int getLoopCount() {
        return LOOP_COUNT_INFINITE;
    }

    @Override
    public AnimatedImageFrame getFrame(int frameNumber) {
        return new RFFrame(frameNumber, mDescriptor);
    }

    @Override
    public boolean doesRenderSupportScaling() {
        return false;
    }

    @Override
    public int getSizeInBytes() {
        int totalSize = 0;
        for (Bitmap bitmap : mDescriptor.cache.values()) {
            // 暂时假设都是ARGB_8888，后面会优化
            totalSize += (bitmap.getWidth() * bitmap.getHeight() * 4);
        }
        return totalSize;
    }

    @Override
    public AnimatedDrawableFrameInfo getFrameInfo(int frameNumber) {
        final AnimatedImageFrame frame = getFrame(frameNumber);
        return new AnimatedDrawableFrameInfo(
                frameNumber,
                frame.getXOffset(),
                frame.getYOffset(),
                frame.getWidth(),
                frame.getHeight(),
                AnimatedDrawableFrameInfo.BlendOperation.NO_BLEND,
                AnimatedDrawableFrameInfo.DisposalMethod.DISPOSE_DO_NOT);
    }
}
