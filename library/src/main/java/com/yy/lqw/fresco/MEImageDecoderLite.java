package com.yy.lqw.fresco;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.facebook.common.logging.FLog;
import com.facebook.imageformat.ImageFormat;
import com.facebook.imagepipeline.animated.base.AnimatedImageResult;
import com.facebook.imagepipeline.common.ImageDecodeOptions;
import com.facebook.imagepipeline.decoder.ImageDecoder;
import com.facebook.imagepipeline.image.CloseableAnimatedImage;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.image.EncodedImage;
import com.facebook.imagepipeline.image.QualityInfo;
import com.google.gson.Gson;

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

public class MEImageDecoderLite implements ImageDecoder {
    private static final Class<?> TAG = MEImageDecoderLite.class;
    private static final String IMAGE_FILE_EXTENSION = ".png";
    private static final MEImageFormats.MEImageFormat[] FORMATS = {
            MEImageFormats.SVGA,
            MEImageFormats.RF
    };
    private static final Gson GSON = new Gson();

    @Override
    public CloseableImage decode(
            EncodedImage encodedImage,
            int length,
            QualityInfo qualityInfo,
            ImageDecodeOptions options) {
        CloseableImage closeableImage = null;
        final ImageFormat format = encodedImage.getImageFormat();
        if (format == MEImageFormats.SVGA || format == MEImageFormats.RF) {

        } else if (encodedImage.getImageFormat() == MEImageFormats.ZIP) {
            try {
                closeableImage = decodeZipBaseImage(encodedImage, length,
                        qualityInfo, options);
            } catch (Exception e) {
                FLog.e(TAG, "Decode image error", e);
            }
            return closeableImage;
        }
        return closeableImage;
    }

    private CloseableImage decodeZipBaseImage(
            EncodedImage encodedImage,
            int length,
            QualityInfo qualityInfo,
            ImageDecodeOptions options) throws IOException {

        AbstractDescriptor descriptor = null;
        ZipEntry ze;
        ZipInputStream zin = new ZipInputStream(encodedImage.getInputStream());
        Map<String, Bitmap> cache = new HashMap<>();
        while ((ze = zin.getNextEntry()) != null) {
            final String name = ze.getName();
            if (name.endsWith(IMAGE_FILE_EXTENSION)) {
                final String key = name.substring(0, name.indexOf('.'));
                final Bitmap bitmap = BitmapFactory.decodeStream(zin);
                cache.put(key, bitmap);
                continue;
            }

            for (MEImageFormats.MEImageFormat fmt : FORMATS) {
                if (name.equals(fmt.getDescriptorFileName())) {
                    InputStreamReader reader = new InputStreamReader(zin);
                    descriptor = decodeDescriptorFromJson(reader, fmt.getDescriptorClass());
                    descriptor.cache = cache;
                    MEImageFormats.MEImageFormat format = new MEImageFormats.MEImageFormat(fmt);
                    format.setDescriptor(descriptor);
                    encodedImage.setImageFormat(format);
                    continue;
                }
            }
        }
        zin.close();
//        if (descriptor != null) {
//            zin = new ZipInputStream(encodedImage.getInputStream());
//            while ((ze = zin.getNextEntry()) != null) {
//                final String name = ze.getName();
//                if (name.endsWith(IMAGE_FILE_EXTENSION)) {
//                    final Bitmap bitmap = BitmapFactory.decodeStream(zin);
//                    final String key = name.substring(0, name.indexOf('.'));
//                    descriptor.cache.put(key, bitmap);
//                }
//            }
//            zin.close();
//            format.setDescriptor(descriptor);
//            encodedImage.setImageFormat(format);
//            return decodeMeImageByDescriptor(format, descriptor);
//        }
        return null;
    }

    private CloseableImage decodeMeImageByDescriptor(
            MEImageFormats.MEImageFormat format,
            AbstractDescriptor descriptor) {
        try {
            Constructor<? extends AbstractAnimatedImage> constructor = format.getAnimatedImageClass()
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
     * Decode descriptor from json
     *
     * @param reader
     * @param clazz
     * @return success return not null otherwise return null
     */
    private <D extends AbstractDescriptor> D decodeDescriptorFromJson(
            Reader reader,
            Class<D> clazz) {
        D descriptor = null;
        try {
            descriptor = GSON.fromJson(reader, clazz);
        } catch (Exception e) {
            FLog.e(TAG, e, "Decode descriptor from json error");
        }
        return descriptor;
    }
}
