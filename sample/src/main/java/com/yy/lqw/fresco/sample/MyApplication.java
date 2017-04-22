package com.yy.lqw.fresco.sample;

import android.app.Application;

import com.facebook.common.logging.FLog;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.decoder.ImageDecoderConfig;
import com.yy.lqw.fresco.decoder.MEImageDecoder;
import com.yy.lqw.fresco.format.MEImageFormats;
import com.yy.lqw.fresco.format.MEImageChecker;

/**
 * Created by lunqingwen on 2016/11/24.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        final ImageDecoderConfig decoderConfig = ImageDecoderConfig.newBuilder()
                .addDecodingCapability(MEImageFormats.ZIP, new MEImageChecker(), new MEImageDecoder())
                .build();
        final ImagePipelineConfig imagePipelineConfig = ImagePipelineConfig.newBuilder(this)
                .setImageDecoderConfig(decoderConfig)
                .build();
        FLog.setMinimumLoggingLevel(FLog.DEBUG);
        Fresco.initialize(this, imagePipelineConfig);
    }
}
