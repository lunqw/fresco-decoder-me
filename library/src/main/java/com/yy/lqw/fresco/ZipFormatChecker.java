package com.yy.lqw.fresco;


import com.facebook.imageformat.ImageFormat;

import javax.annotation.Nullable;

/**
 * Created by lunqingwen on 2017/4/22.
 */

public class ZipFormatChecker implements ImageFormat.FormatChecker {
    private static final int ZIP_HEADER_LENGTH = 4;

    @Override
    public int getHeaderSize() {
        return ZIP_HEADER_LENGTH;
    }

    @Nullable
    @Override
    public ImageFormat determineFormat(byte[] headerBytes, int headerSize) {
        if (headerBytes[0] == 0x50
                && headerBytes[1] == 0x4b
                && headerBytes[2] == 0x03
                && headerBytes[3] == 0x04) {
            return MEImageFormats.ZIP;
        }
        return null;
    }
}
