package com.yy.lqw.fresco.decoder;

import com.facebook.imageformat.ImageFormat;
import com.yy.lqw.fresco.base.AbstractAnimatedImage;
import com.yy.lqw.fresco.base.AbstractDescriptor;
import com.yy.lqw.fresco.rf.RFDescriptor;
import com.yy.lqw.fresco.rf.RFImage;
import com.yy.lqw.fresco.svga.SVGADescriptor;
import com.yy.lqw.fresco.svga.SVGAImage;

/**
 * Created by lunqingwen on 2017/4/20.
 */

public final class MEImageFormats {
    public static final ImageFormat ZIP = new ImageFormat("ZIP", "zip");

    public static final MEImageFormat SVGA = new MEImageFormat("SVGA", "svga",
            "movie.spec", SVGADescriptor.class, SVGAImage.class);

    public static final MEImageFormat RF = new MEImageFormat("RF", "rf",
            "rf.spec", RFDescriptor.class, RFImage.class);

    public static class MEImageFormat extends ImageFormat {
        private String mDescriptorFileName;
        private Class<? extends AbstractDescriptor> mDescriptorClass;
        private Class<? extends AbstractAnimatedImage> mAnimatedImageClass;

        public MEImageFormat(
                String name,
                String fileExtension,
                String descriptorFileName,
                Class<? extends AbstractDescriptor> descriptorClass,
                Class<? extends AbstractAnimatedImage> animatedImageClass) {
            super(name, fileExtension);
            mDescriptorFileName = descriptorFileName;
            mDescriptorClass = descriptorClass;
            mAnimatedImageClass = animatedImageClass;
        }

        public String getDescriptorFileName() {
            return mDescriptorFileName;
        }

        public Class<? extends AbstractDescriptor> getDescriptorClass() {
            return mDescriptorClass;
        }

        public Class<? extends AbstractAnimatedImage> getAnimatedImageClass() {
            return mAnimatedImageClass;
        }
    }
}
