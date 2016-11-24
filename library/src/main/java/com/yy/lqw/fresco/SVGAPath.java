package com.yy.lqw.fresco;

import android.graphics.Path;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by lunqingwen on 2016/9/2.
 */
class SVGAPath {
    private static final List<Character> VALID_METHODS = Collections.unmodifiableList(
            Arrays.asList(
                    'M', 'L', 'H', 'V', 'C', 'S', 'Q', 'R', 'A',
                    'Z', 'm', 'l', 'h', 'v', 'c', 's', 'q', 'r', 'a', 'z'));

    private static final String SPLITTER = "[,\\s+]";

    /**
     * 从字符串格式的clipPath生成Path
     *
     * @param string
     * @return
     */
    static Path createPathFromString(String string) {
        if (string != null && !string.isEmpty()) {
            final Path path = new Path();
            final String[] items = string.split(SPLITTER);
            char method = '\0';
            float[] params = new float[6];
            int count = 0;
            for (String item : items) {
                item = item.trim();
                if (item.length() > 0) {
                    final char newMethod = item.charAt(0);
                    if (VALID_METHODS.contains(newMethod)) {
                        if (count > 0) {
                            operate(path, method, params, count);
                        }
                        method = newMethod;
                        count = 0;
                        params[count] = Float.valueOf(item.substring(1));
                    } else {
                        params[count] = Float.valueOf(item);
                    }
                    count++;
                }
            }
            if (count > 0) {
                operate(path, method, params, count);
            }
            return path;
        }
        return null;
    }

    /**
     * Path 操作
     *
     * @param path   {@link Path} 对象
     * @param method 方法缩写 {@link SVGAPath#VALID_METHODS}
     * @param params 参数
     * @param count  参数个数
     */
    private static void operate(Path path, char method, float[] params, int count) {
        switch (method) {
            case 'M':
                path.moveTo(params[0], params[1]);
                break;
            case 'm':
                path.rMoveTo(params[0], params[1]);
                break;
            case 'L':
                path.lineTo(params[0], params[1]);
                break;
            case 'l':
                path.rLineTo(params[0], params[1]);
                break;
            case 'C':
                path.cubicTo(params[0], params[1], params[2], params[3], params[4], params[5]);
                break;
            case 'c':
                path.rCubicTo(params[0], params[1], params[2], params[3], params[4], params[5]);
                break;
            case 'Q':
                path.quadTo(params[0], params[1], params[2], params[3]);
                break;
            case 'q':
                path.rQuadTo(params[0], params[1], params[2], params[3]);
                break;
            case 'H':
                path.lineTo(params[0], params[1]);
                break;
            case 'h':
                path.rLineTo(params[0], params[1]);
                break;
            case 'V':
                path.lineTo(params[0], params[1]);
                break;
            case 'v':
                path.rLineTo(params[0], params[1]);
                break;
            case 'Z':
            case 'z':
                path.close();
                break;
        }
    }
}
