package com.yy.lqw.fresco;

import com.facebook.imagepipeline.animated.base.AnimatedImage;

/**
 * Created by lunqingwen on 2016/11/24.
 */

abstract class AbstractAnimatedImage implements AnimatedImage {
    protected AbstractDescriptor mDescriptor;

    public AbstractAnimatedImage(AbstractDescriptor descriptor) {
        mDescriptor = descriptor;
    }

    public int getPreviewFrame() {
        return 0;
    }
}
