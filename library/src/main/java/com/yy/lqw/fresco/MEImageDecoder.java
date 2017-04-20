package com.yy.lqw.fresco;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.facebook.common.logging.FLog;
import com.facebook.imageformat.ImageFormat;
import com.facebook.imagepipeline.animated.base.AnimatedImageResult;
import com.facebook.imagepipeline.animated.factory.AnimatedImageFactory;
import com.facebook.imagepipeline.common.ImageDecodeOptions;
import com.facebook.imagepipeline.decoder.DefaultImageDecoder;
import com.facebook.imagepipeline.image.CloseableAnimatedImage;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.image.EncodedImage;
import com.facebook.imagepipeline.image.QualityInfo;
import com.facebook.imagepipeline.platform.PlatformDecoder;
import com.google.gson.Gson;

import org.nustaq.serialization.FSTConfiguration;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Constructor;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by lunqingwen on 2016/8/30.
 * <p>
 * 支持ME项目的两种自定义动画格式：
 * <ol>
 * <li>SVGA</li>
 * <li>RF(Reusable frames)</li>
 * </ol>
 */
class MEImageDecoder extends DefaultImageDecoder {
    private static final Class<?> TAG = MEImageDecoder.class;

    // only support png image
    private static final String IMAGE_FILE_EXTENSION = ".png";

    // cache directory
    private static final String CACHE_DIR_NAME = "animation_cache";

    // cache file name, size_time_crc.fst
    private static final String FMT_CACHE_DIR = "%x_%x_%x";

    // temporary file prefix
    private static final String TMP_NAME_PREFIX = "dir_";

    // temporary file suffix
    private static final String TMP_NAME_SUFFIX = "_cache";

    // descriptor cache file suffix
    private static final String FST_CACHE_SUFFIX = ".fst";

    // decode config
    private static final Config[] sConfigs = {
            new Config("movie.spec", SVGADescriptor.class, SVGAImage.class),    // SVGA image
            new Config("rf.spec", RFDescriptor.class, RFImage.class)            // RF image
    };

    private static final Gson sGson = new Gson();
    private static final FSTConfiguration sFst = FSTConfiguration.createAndroidDefaultConfiguration();
    private final File mDiskCacheRoot;

    public MEImageDecoder(
            Context context,
            AnimatedImageFactory animatedImageFactory,
            PlatformDecoder platformDecoder,
            Bitmap.Config bitmapConfig) {
        super(animatedImageFactory, platformDecoder,
                bitmapConfig != null ? bitmapConfig : Bitmap.Config.ARGB_8888);
        mDiskCacheRoot = new File(context.getCacheDir(), CACHE_DIR_NAME);
        if (!mDiskCacheRoot.exists()) {
            mDiskCacheRoot.mkdirs();
        }
    }

    @Override
    public CloseableImage decode(
            EncodedImage encodedImage,
            int length,
            QualityInfo qualityInfo,
            ImageDecodeOptions options) {
        // Fresco默认不能处理的文件格式由这里进一步处理
        // SVGA, RF都是zip文件
        if (encodedImage.getImageFormat() == ImageFormat.UNKNOWN) {
            try {
                final long begin = System.currentTimeMillis();
                CloseableImage closeableImage = decodeImage(encodedImage);
                final long end = System.currentTimeMillis();
                FLog.d(TAG, "Image decoded, bein: %d, end: %d, diff: %d",
                        begin, end, end - begin);
                return closeableImage;
            } catch (Exception e) {
                FLog.e(TAG, e, "Decode image error");
            }
        }
        return super.decode(encodedImage, length, qualityInfo, options);
    }

    private CloseableImage decodeImage(EncodedImage encodedImage) throws IOException {
        ZipEntry ze;
        ZipInputStream zin = new ZipInputStream(encodedImage.getInputStream());
        while ((ze = zin.getNextEntry()) != null) {
            for (Config config : sConfigs) {
                if (ze.getName().equals(config.mDFName)) {
                    final String cacheKey = getCacheKey(ze.getSize(),
                            ze.getTime(), ze.getCrc());
                    AbstractAnimatedImage image = AnimatedCache.get(cacheKey);
                    if (image == null) {
                        final File imageDir = getImageDirectory(cacheKey);
                        if (!imageDir.exists()) {
                            extractImageToDisk(encodedImage, imageDir);
                        }

                        if (imageDir.exists()) {
                            final File dFile = new File(imageDir, config.mDFName);
                            image = decodeImage(imageDir, dFile, config.mDClass,
                                    config.mIClass);
                            AnimatedCache.put(cacheKey, image);
                        }
                    }

                    if (image != null) {
                        zin.close();
                        final AnimatedImageResult result = AnimatedImageResult.newBuilder(image)
                                .setFrameForPreview(image.getPreviewFrame())
                                .build();
                        return new CloseableAnimatedImage(result);
                    }
                }
            }
        }
        zin.close();
        return null;
    }

    /**
     * Decode animated image
     *
     * @param imageDir image cache directory
     * @param dFile    descriptor file
     * @param iClass   image class
     * @param dClass   descriptor clas
     * @param <I>      image type
     * @param <D>      descriptor type
     * @return animated image
     * @throws IOException
     */
    private <D extends AbstractDescriptor, I extends AbstractAnimatedImage> I decodeImage(
            File imageDir,
            File dFile,
            Class<D> dClass,
            Class<I> iClass) throws IOException {

        I image = null;
        D descriptor = decodeDescriptor(dFile, dClass);
        if (descriptor != null) {
            final File[] files = imageDir.listFiles();
            String name;
            for (File file : files) {
                name = file.getName();
                if (name.endsWith(IMAGE_FILE_EXTENSION)) {
                    final String key = name.substring(0, name.indexOf('.'));
                    final Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                    descriptor.cache.put(key, bitmap);
                }
            }

            try {
                Constructor<I> constructor = iClass.getDeclaredConstructor(dClass);
                constructor.setAccessible(true);
                image = constructor.newInstance(descriptor);
            } catch (Exception e) {
                FLog.e(TAG, e, "Create image class error");
            }
        }
        return image;
    }

    private String getCacheKey(long size, long time, long crc) {
        return String.format(FMT_CACHE_DIR, size, time, crc);
    }

    private File getImageDirectory(String cacheKey) {
        return new File(mDiskCacheRoot, cacheKey);
    }

    /**
     * Unzip image to disk
     *
     * @param encodedImage
     * @param directory
     * @throws IOException
     */
    private void extractImageToDisk(
            EncodedImage encodedImage,
            File directory) throws IOException {

        final File tmpDir = File.createTempFile(TMP_NAME_PREFIX,
                TMP_NAME_SUFFIX, mDiskCacheRoot);
        tmpDir.delete();
        tmpDir.mkdirs();
        if (tmpDir.exists()) {
            ZipInputStream zin = new ZipInputStream(encodedImage.getInputStream());
            ZipEntry ze;
            int size;
            byte[] buf = new byte[8192];
            while ((ze = zin.getNextEntry()) != null) {
                if (!ze.isDirectory()) {
                    File outFile = new File(tmpDir, ze.getName());
                    if (!outFile.getParentFile().equals(tmpDir)) {
                        continue;
                    }
                    BufferedOutputStream out = new BufferedOutputStream(
                            new FileOutputStream(outFile));
                    while ((size = zin.read(buf)) != -1) {
                        out.write(buf, 0, size);
                    }
                    out.close();
                }
            }
            zin.close();
            tmpDir.renameTo(directory);
        }
    }

    /**
     * Decode descriptor
     *
     * @param dFile descriptor cache file
     * @param clazz descriptor class
     * @param <D>   descriptor type
     * @return image descriptor
     * @throws IOException
     */
    private <D extends AbstractDescriptor> D decodeDescriptor(
            File dFile,
            Class<D> clazz) throws IOException {

        D descriptor = null;
        final File dCacheFile = new File(dFile.getPath() + FST_CACHE_SUFFIX);

        // try to decode from cache
        if (dCacheFile != null && dCacheFile.exists()) {
            InputStream cacheIn = new FileInputStream(dCacheFile);
            descriptor = decodeDescriptorFromCache(cacheIn);
            cacheIn.close();
        }

        // cache not found, decode from json file
        if (descriptor == null) {
            Reader reader = new FileReader(dFile);
            descriptor = decodeDescriptorFromJson(reader, clazz);
            if (descriptor != null && dFile != null) {
                cacheDescriptorForFastLoad(descriptor, dCacheFile);
            }
            reader.close();
        }

        if (descriptor.cache == null) {
            descriptor.initialize();
        }
        return descriptor;
    }

    /**
     * Save descriptor to cache in order to support fast serialization
     *
     * @param descriptor image descriptor
     * @param cacheFile  descriptor cache file
     * @throws IOException
     */
    private void cacheDescriptorForFastLoad(
            AbstractDescriptor descriptor,
            File cacheFile) throws IOException {

        File tmpFile = File.createTempFile(TMP_NAME_PREFIX,
                TMP_NAME_SUFFIX, mDiskCacheRoot);
        FileOutputStream out = new FileOutputStream(tmpFile);
        sFst.encodeToStream(out, descriptor);
        tmpFile.renameTo(cacheFile);
        out.close();
    }

    /**
     * Decode descriptor from cache
     *
     * @param in
     * @return success return not null otherwise return null
     */
    private <D> D decodeDescriptorFromCache(InputStream in) {
        Object descriptor = null;
        try {
            descriptor = sFst.decodeFromStream(in);
        } catch (Exception e) {
            FLog.e(TAG, e, "Decode descriptor from cache error");
        }
        return (D) descriptor;
    }

    /**
     * Decode descriptor from json
     *
     * @param reader
     * @param clazz
     * @return success return not null otherwise return null
     */
    private <D> D decodeDescriptorFromJson(Reader reader, Class<D> clazz) {
        D descriptor = null;
        try {
            descriptor = sGson.fromJson(reader, clazz);
        } catch (Exception e) {
            FLog.e(TAG, e, "Decode descriptor from json error");
        }
        return descriptor;
    }

    /**
     * Class of decode config
     */
    private static class Config {
        String mDFName;     // descriptor file name
        Class mDClass;      // descriptor class
        Class mIClass;      // image class

        public Config(String dfName, Class dClass, Class iClass) {
            mDFName = dfName;
            mDClass = dClass;
            mIClass = iClass;
        }
    }
}
