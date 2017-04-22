package com.yy.lqw.fresco.decoder;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.facebook.cache.common.CacheKey;
import com.facebook.common.logging.FLog;
import com.facebook.imagepipeline.animated.base.AnimatedImageResult;
import com.facebook.imagepipeline.common.ImageDecodeOptions;
import com.facebook.imagepipeline.decoder.ImageDecoder;
import com.facebook.imagepipeline.image.CloseableAnimatedImage;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.image.EncodedImage;
import com.facebook.imagepipeline.image.QualityInfo;
import com.google.gson.Gson;
import com.yy.lqw.fresco.base.AbstractAnimatedImage;
import com.yy.lqw.fresco.base.AbstractDescriptor;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by lunqingwen on 2017/4/21.
 */

public class MEImageDecoder implements ImageDecoder {
    private static final Class<?> TAG = MEImageDecoder.class;
    private static final String IMAGE_FILE_EXTENSION = ".png";

    // 目前支持的图片格式
    private static final MEImageFormats.MEImageFormat[] FORMATS = {
            MEImageFormats.SVGA,
            MEImageFormats.RF
    };

    private final Gson sGson = new Gson();

    @Override
    public CloseableImage decode(
            EncodedImage encodedImage,
            int length,
            QualityInfo qualityInfo,
            ImageDecodeOptions options) {
        CloseableImage closeableImage = null;
        CacheManager.CacheItem cacheItem = null;
        final CacheKey cacheKey = encodedImage.getEncodedCacheKey();
        if (cacheKey != null) {
            cacheItem = CacheManager.get(cacheKey);
        }
        final long beginMillis = System.currentTimeMillis();
        FLog.d(TAG, "Decode image begin");
        if (cacheItem != null) {
            FLog.i(TAG, "Decode image from cache, key: %s", cacheKey);
            closeableImage = createImageFromDescriptor(cacheItem.mFormat, cacheItem.mDescriptor);
        } else if (encodedImage.getImageFormat() == MEImageFormats.ZIP) {
            FLog.i(TAG, "Decode image from zip file");
            closeableImage = decodeZipBaseImage(encodedImage, length,
                    qualityInfo, options);
        }
        FLog.d(TAG, "Decode image end, time used: %d", System.currentTimeMillis() - beginMillis);
        return closeableImage;
    }

    /**
     * 解析zip格式图片文件
     *
     * @param encodedImage
     * @param length
     * @param qualityInfo
     * @param options
     * @return 成功返回CloseableImage对象，失败返回null
     */
    private CloseableImage decodeZipBaseImage(
            EncodedImage encodedImage,
            int length,
            QualityInfo qualityInfo,
            ImageDecodeOptions options) {

        final ZipInputStream zin = new ZipInputStream(encodedImage.getInputStream());
        ZipEntry ze;
        MEImageFormats.MEImageFormat format = null;
        AbstractDescriptor descriptor = null;
        final Map<String, Bitmap> bitmapCache = new HashMap<>();
        try {
            while ((ze = zin.getNextEntry()) != null) {
                final String name = ze.getName();
                if (name.endsWith(IMAGE_FILE_EXTENSION)) {
                    final String key = name.substring(0, name.indexOf('.'));
                    final Bitmap bitmap = BitmapFactory.decodeStream(zin);
                    bitmapCache.put(key, bitmap);
                    continue;
                }

                for (int i = 0; i < FORMATS.length; i++) {
                    if (name.equals(FORMATS[i].getDescriptorFileName())) {
                        final InputStreamReader reader = new InputStreamReader(zin);
                        format = FORMATS[i];
                        descriptor = decodeDescriptor(reader, format.getDescriptorClass());
                        if (descriptor != null) {
                            descriptor.cache = bitmapCache;
                            final CacheKey cacheKey = encodedImage.getEncodedCacheKey();
                            if (cacheKey != null) {
                                CacheManager.put(cacheKey, new CacheManager.CacheItem(format, descriptor));
                            }
                        }
                        continue;
                    }
                }
            }
        } catch (IOException e) {
            FLog.e(TAG, "Decode image error", e);
        } finally {
            try {
                zin.close();
            } catch (IOException e) {
            }
        }
        if (format != null && descriptor != null) {
            return createImageFromDescriptor(format, descriptor);
        }
        return null;
    }

    /**
     * 通过描述符创建CloseableImage对象
     *
     * @param format     图片格式
     * @param descriptor 描述符对象
     * @return 成功返回CloseableImage对象， 失败返回null
     */
    private CloseableImage createImageFromDescriptor(
            MEImageFormats.MEImageFormat format,
            AbstractDescriptor descriptor) {
        try {
            final Constructor<? extends AbstractAnimatedImage> constructor = format.getAnimatedImageClass()
                    .getConstructor(format.getDescriptorClass());
            constructor.setAccessible(true);
            final AbstractAnimatedImage image = constructor.newInstance(descriptor);
            final AnimatedImageResult result = AnimatedImageResult.newBuilder(image)
                    .setFrameForPreview(image.getPreviewFrame())
                    .build();
            return new CloseableAnimatedImage(result);
        } catch (Exception e) {
            FLog.e(TAG, "Create image class error", e);
        }
        return null;
    }

    /**
     * 解析图片描述符文件，生成描述符对象
     *
     * @param reader reader
     * @param clazz  描述符类对象
     * @param <D>
     * @return 成功返回描述符对象，失败返回null
     */
    private <D extends AbstractDescriptor> D decodeDescriptor(
            Reader reader,
            Class<D> clazz) {
        D descriptor = null;
        try {
            descriptor = sGson.fromJson(reader, clazz);
        } catch (Exception e) {
            FLog.e(TAG, e, "Decode descriptor from json error");
        }
        return descriptor;
    }
}
