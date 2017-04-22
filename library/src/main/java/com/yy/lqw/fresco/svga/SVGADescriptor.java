package com.yy.lqw.fresco.svga;

import android.graphics.Matrix;
import android.graphics.Path;

import com.yy.lqw.fresco.base.AbstractDescriptor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by lunqingwen on 2016/9/1.
 * <p/>
 * SVGA 描述符对象
 */
public class SVGADescriptor extends AbstractDescriptor {

    // 文件版本
    public String ver;

    // 子动画
    public List<Sprite> sprites;

    // 动画信息
    public Movie movie;

    // 动画相关图片文件
    public Map<String, String> images;

    @Override
    public int getWidth() {
        return movie.viewBox.width;
    }

    @Override
    public int getHeight() {
        return movie.viewBox.height;
    }

    /**
     * Sprite, 描述一个子动画
     */
    public static class Sprite implements Serializable {
        public List<Frame> frames;  // 子动画各个帧
        public String imageKey;     // 图片文件名
    }

    /**
     * 动画信息
     */
    public static class Movie implements Serializable {
        public int frames;      // 帧数
        public int fps;         // 帧率
        public Rect viewBox;    // 动画尺寸
    }

    /**
     * 子动画帧
     */
    public static class Frame implements Serializable {
        public Transform transform;
        public Rect layout;
        public float alpha;
        public String clipPath;
        private transient Path path;

        public Path getPath() {
            if (path == null && clipPath != null) {
                path = SVGAPath.createPathFromString(clipPath);
            }
            return path;
        }
    }

    /**
     * 动画变换描述
     */
    public static class Transform implements Serializable {
        public double a;
        public double b;
        public double c;
        public double d;
        public double tx;
        public double ty;
        private transient Matrix matrix;

        public Matrix toMatrix() {
            if (matrix == null) {
                float[] values = new float[9];
                values[0] = (float) a;
                values[1] = (float) c;
                values[2] = (float) tx;
                values[3] = (float) b;
                values[4] = (float) d;
                values[5] = (float) ty;
                values[6] = (float) 0.0;
                values[7] = (float) 0.0;
                values[8] = (float) 1.0;
                matrix = new Matrix();
                matrix.setValues(values);
            }
            return matrix;
        }
    }

    public static class Rect implements Serializable {
        public int x;
        public int y;
        public int height;
        public int width;
    }
}
