package com.yy.lqw.fresco;

import com.facebook.imageformat.ImageFormat;

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
        private AbstractDescriptor mDescriptor;
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

        public MEImageFormat(MEImageFormat format) {
            super(format.getName(), format.getFileExtension());
            mDescriptorFileName = format.getDescriptorFileName();
            mDescriptorClass = format.getDescriptorClass();
            mDescriptor = format.getDescriptor();
            mAnimatedImageClass = format.getAnimatedImageClass();
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

        public AbstractDescriptor getDescriptor() {
            return mDescriptor;
        }

        public void setDescriptor(AbstractDescriptor descriptor) {
            mDescriptor = descriptor;
        }
    }
}
