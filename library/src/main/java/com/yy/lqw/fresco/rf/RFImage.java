package com.yy.lqw.fresco.rf;

import com.facebook.imagepipeline.animated.base.AnimatedDrawableFrameInfo;
import com.facebook.imagepipeline.animated.base.AnimatedImage;
import com.facebook.imagepipeline.animated.base.AnimatedImageFrame;
import com.yy.lqw.fresco.base.Previewable;

import java.util.Arrays;

/**
 * Created by lunqingwen on 2016/11/10.
 */

public class RFImage implements AnimatedImage, Previewable {
    private final RFDescriptor mDescriptor;

    public RFImage(RFDescriptor descriptor) {
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
        return mDescriptor.getSizeInBytes();
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

    @Override
    public int getPreviewFrame() {
        final String preview = mDescriptor.preview;
        if (preview != null) {
            return mDescriptor.frames.indexOf(preview);
        }
        return 0;
    }
}
