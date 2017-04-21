package com.yy.lqw.fresco.sample;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.decoder.ImageDecoderConfig;
import com.yy.lqw.fresco.MEImageDecoderLite;
import com.yy.lqw.fresco.MEImageFormats;
import com.yy.lqw.fresco.ZipFormatChecker;

/**
 * Created by lunqingwen on 2016/11/24.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        final ImageDecoderConfig decoderConfig = ImageDecoderConfig.newBuilder()
                .addDecodingCapability(MEImageFormats.ZIP, new ZipFormatChecker(), new MEImageDecoderLite())
                .build();
        final ImagePipelineConfig imagePipelineConfig = ImagePipelineConfig.newBuilder(this)
                .setImageDecoderConfig(decoderConfig)
                .build();
        Fresco.initialize(this, imagePipelineConfig);
    }
}
