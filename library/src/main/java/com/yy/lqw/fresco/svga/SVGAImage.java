package com.yy.lqw.fresco.svga;

import android.graphics.Bitmap;

import com.facebook.imagepipeline.animated.base.AnimatedDrawableFrameInfo;
import com.yy.lqw.fresco.base.AbstractAnimatedImage;

import java.util.Arrays;

/**
 * Created by lunqingwen on 2016/8/31.
 */
public class SVGAImage extends AbstractAnimatedImage {
    public SVGAImage(SVGADescriptor descriptor) {
        super(descriptor);
    }

    @Override
    public void dispose() {

    }

    @Override
    public int getWidth() {
        return ((SVGADescriptor) mDescriptor).movie.viewBox.width;
    }

    @Override
    public int getHeight() {
        return ((SVGADescriptor) mDescriptor).movie.viewBox.height;
    }

    @Override
    public int getFrameCount() {
        return ((SVGADescriptor) mDescriptor).movie.frames;
    }

    @Override
    public int getDuration() {
        final int frames = ((SVGADescriptor) mDescriptor).movie.frames;
        final int fps = ((SVGADescriptor) mDescriptor).movie.fps;
        return (int) (1000.0f / fps * frames);
    }

    @Override
    public int[] getFrameDurations() {
        int[] durations = new int[((SVGADescriptor) mDescriptor).movie.frames];
        Arrays.fill(durations, 1000 / ((SVGADescriptor) mDescriptor).movie.fps);
        return durations;
    }

    @Override
    public int getLoopCount() {
        return LOOP_COUNT_INFINITE;
    }

    @Override
    public SVGAFrame getFrame(int frameNumber) {
        return new SVGAFrame(frameNumber, ((SVGADescriptor) mDescriptor));
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
        SVGAFrame frame = getFrame(frameNumber);
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
