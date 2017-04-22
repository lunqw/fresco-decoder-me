package com.yy.lqw.fresco.svga;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;

import com.facebook.imagepipeline.animated.base.AnimatedImageFrame;

import java.util.Iterator;

/**
 * Created by lunqingwen on 2016/8/31.
 */
class SVGAFrame implements AnimatedImageFrame {
    private final SVGADescriptor mDescriptor;
    private final int mFrameNumber;
    private final Canvas mCanvas = new Canvas();
    private final Paint mPaint = new Paint();

    public SVGAFrame(int frameNumber, SVGADescriptor descriptor) {
        mFrameNumber = frameNumber;
        mDescriptor = descriptor;
    }

    @Override
    public void dispose() {

    }

    @Override
    public void renderFrame(int width, int height, Bitmap bitmap) {
        mCanvas.setBitmap(bitmap);
        final Iterator<SVGADescriptor.Sprite> iterator = mDescriptor.sprites.iterator();
        SVGADescriptor.Sprite sprite;
        SVGADescriptor.Frame frame;
        Bitmap subBitmap;

        while (iterator.hasNext()) {
            sprite = iterator.next();
            frame = sprite.frames.get(mFrameNumber);
            if (frame != null
                    && frame.layout != null
                    && frame.transform != null
                    && frame.alpha > 0
                    && (subBitmap = mDescriptor.cache.get(sprite.imageKey)) != null) {
                mPaint.setAlpha((int) (frame.alpha * 0xFF));
                final Matrix matrix = frame.transform.toMatrix();
                final Path path = frame.getPath();
                if (path != null) {
                    mCanvas.save();
                    mCanvas.setMatrix(matrix);
                    mCanvas.clipRect(0, 0, subBitmap.getWidth(), subBitmap.getHeight());
                    BitmapShader bs = new BitmapShader(subBitmap,
                            Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
                    mPaint.setShader(bs);
                    mCanvas.drawPath(path, mPaint);
                    mCanvas.restore();
                } else {
                    mCanvas.drawBitmap(subBitmap, matrix, mPaint);
                }
            }
        }
    }

    @Override
    public int getDurationMs() {
        return 1000 / mDescriptor.movie.fps;
    }

    @Override
    public int getWidth() {
        return mDescriptor.getWidth();
    }

    @Override
    public int getHeight() {
        return mDescriptor.getHeight();
    }

    @Override
    public int getXOffset() {
        return 0;
    }

    @Override
    public int getYOffset() {
        return 0;
    }
}
