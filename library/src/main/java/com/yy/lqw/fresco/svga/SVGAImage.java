package com.yy.lqw.fresco.svga;

import com.facebook.imagepipeline.animated.base.AnimatedDrawableFrameInfo;
import com.facebook.imagepipeline.animated.base.AnimatedImage;
import com.yy.lqw.fresco.base.Previewable;

import java.util.Arrays;

/**
 * Created by lunqingwen on 2016/8/31.
 */
public class SVGAImage implements AnimatedImage, Previewable {
    private final SVGADescriptor mDescriptor;

    public SVGAImage(SVGADescriptor descriptor) {
        mDescriptor = descriptor;
    }

    @Override
    public void dispose() {

    }

    @Override
    public int getWidth() {
        return mDescriptor.getWidth();
    }

    @Override
    public int getHeight() {
        return mDescriptor.getHeight();
    }

    @Override
    public int getFrameCount() {
        return mDescriptor.movie.frames;
    }

    @Override
    public int getDuration() {
        final int frames = mDescriptor.movie.frames;
        final int fps = mDescriptor.movie.fps;
        return (int) (1000.0f / fps * frames);
    }

    @Override
    public int[] getFrameDurations() {
        int[] durations = new int[mDescriptor.movie.frames];
        Arrays.fill(durations, 1000 / mDescriptor.movie.fps);
        return durations;
    }

    @Override
    public int getLoopCount() {
        return LOOP_COUNT_INFINITE;
    }

    @Override
    public SVGAFrame getFrame(int frameNumber) {
        return new SVGAFrame(frameNumber, mDescriptor);
    }

    @Override
    public boolean doesRenderSupportScaling() {
        return false;
    }

    @Override
    public int getSizeInBytes() {
        return mDescriptor.getSizeInBytes();
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

    @Override
    public int getPreviewFrame() {
        return 0;
    }
}
