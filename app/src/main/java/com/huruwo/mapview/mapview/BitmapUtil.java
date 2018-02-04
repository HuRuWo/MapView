package com.huruwo.mapview.mapview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.AppCompatDrawableManager;

/**
 * @author: liuwan
 * @date: 2018-01-29
 * @action:
 */
public class BitmapUtil {

    /**
     * 是否touch在该path内部
     * @param x
     * @param y
     * @return
     */
    public static boolean  isTouch(int x, int y, Path mPath) {
        Region result = new Region();
        //构造一个区域对象。
        RectF r=new RectF();
        //计算path的边界
        mPath.computeBounds(r, true);
        //设置区域路径和剪辑描述的区域
        result.setPath(mPath, new Region((int)r.left,(int)r.top,(int)r.right,(int)r.bottom));
        return result.contains(x, y);
    }
}
