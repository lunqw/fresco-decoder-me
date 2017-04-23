package com.yy.lqw.fresco.sample;

import android.app.Application;
import android.util.Log;

import com.facebook.common.logging.FLog;
import com.facebook.common.memory.MemoryTrimType;
import com.facebook.common.memory.MemoryTrimmable;
import com.facebook.common.memory.MemoryTrimmableRegistry;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.decoder.ImageDecoderConfig;
import com.yy.lqw.fresco.decoder.MEImageDecoder;
import com.yy.lqw.fresco.format.MEImageChecker;
import com.yy.lqw.fresco.format.MEImageFormats;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lunqingwen on 2016/11/24.
 */

public class MyApplication extends Application {
    private static final String TAG = "MyApplication";
    private List<MemoryTrimmable> mMemoryTrimmables = new ArrayList<>();

    private final MemoryTrimmableRegistry mFrescoMemoryTrimmableRegistry =
            new MemoryTrimmableRegistry() {
                @Override
                public void registerMemoryTrimmable(MemoryTrimmable trimmable) {
                    Log.d(TAG, "registerMemoryTrimmable");
                    synchronized (mMemoryTrimmables) {
                        mMemoryTrimmables.add(trimmable);
                    }
                }

                @Override
                public void unregisterMemoryTrimmable(MemoryTrimmable trimmable) {
                    Log.d(TAG, "unregisterMemoryTrimmable");
                    synchronized (mMemoryTrimmables) {
                        mMemoryTrimmables.remove(trimmable);
                    }
                }
            };

    @Override
    public void onCreate() {
        super.onCreate();

        final ImageDecoderConfig decoderConfig = ImageDecoderConfig.newBuilder()
                .addDecodingCapability(MEImageFormats.ZIP, new MEImageChecker(), new MEImageDecoder())
                .build();
        final ImagePipelineConfig imagePipelineConfig = ImagePipelineConfig.newBuilder(this)
                .setImageDecoderConfig(decoderConfig)
                .setMemoryTrimmableRegistry(mFrescoMemoryTrimmableRegistry)
                .build();
        FLog.setMinimumLoggingLevel(FLog.DEBUG);
        Fresco.initialize(this, imagePipelineConfig);
    }

    @Override
    public void onTrimMemory(int level) {
        Log.w(TAG, "onTrimMemory, level: " + level);
        final MemoryTrimType trimType = level < TRIM_MEMORY_UI_HIDDEN
                ? MemoryTrimType.OnSystemLowMemoryWhileAppInForeground
                : MemoryTrimType.OnSystemLowMemoryWhileAppInBackground;
        List<MemoryTrimmable> memoryTrimmables;
        synchronized (mMemoryTrimmables) {
            memoryTrimmables = new ArrayList<>(mMemoryTrimmables);
        }

        for (MemoryTrimmable trimmable : memoryTrimmables) {
            trimmable.trim(trimType);
        }
        super.onTrimMemory(level);
    }


}
