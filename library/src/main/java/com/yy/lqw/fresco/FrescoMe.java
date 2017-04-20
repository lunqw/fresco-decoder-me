package com.yy.lqw.fresco;

import android.content.Context;
import android.graphics.Bitmap;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.animated.factory.AnimatedImageFactory;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.facebook.imagepipeline.platform.PlatformDecoder;

/**
 * Created by lunqingwen on 2016/10/14.
 */
public class FrescoMe {
    public static void initialize(Context context) {
        ImagePipelineFactory.initialize(context);
        AnimatedImageFactory factory = ImagePipelineFactory.getInstance()
                .getAnimatedFactory()
                .getAnimatedImageFactory();
        PlatformDecoder platformDecoder = ImagePipelineFactory.getInstance()
                .getPlatformDecoder();
        MEImageDecoder decoder = new MEImageDecoder(
                context, factory, platformDecoder, Bitmap.Config.ARGB_8888);
        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(context)
                .setImageDecoder(decoder)
                .build();
        Fresco.initialize(context, config);
    }
}
