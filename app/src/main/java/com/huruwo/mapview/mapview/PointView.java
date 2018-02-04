package com.huruwo.mapview.mapview;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: liuwan
 * @date: 2018-02-03
 * @action:
 */
public class PointView extends View{
    private Context mContext;           // 上下文
    private int mWidth, mHeight;        // 宽高

    /**
     * 最大宽度
     */
    private Integer mMaxWidth = 100;
    /**
     * 扩散速度
     */
    private int mDiffuseSpeed = 10;
    /**
     * 扩散圆宽度
     */
    private int mDiffuseWidth = 3;

    // 透明度集合
    private List<Integer> mAlphas = new ArrayList<>();
    // 扩散圆半径集合
    private List<Integer> mWidths = new ArrayList<>();

    public PointView(Context context) {
        this(context, null);
    }

    public PointView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PointView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        mAlphas.add(255);
        mWidths.add(0);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawBitmapDrawable(canvas);
    }


    public void drawBitmapDrawable(Canvas canvas) {

        // 定义画笔

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        // 获得资源

        Resources rec = getResources();

        // BitmapDrawable

        BitmapDrawable bitmapDrawable = (BitmapDrawable) rec.getDrawable(R.drawable.map_point_red);

        int drawHight = bitmapDrawable.getBitmap().getHeight();
        int drawWidth = bitmapDrawable.getBitmap().getWidth();

        // 得到Bitmap

        Bitmap bitmap = bitmapDrawable.getBitmap();


        for (int i = 0; i < mAlphas.size(); i++) {

            Integer alpha = mAlphas.get(i);
            paint.setAlpha(alpha);
            // 绘制扩散圆
            Integer width = mWidths.get(i);
            RectF rectF2 = new RectF(mWidth / 2 - width, mHeight / 2 + drawHight / 2 - width/2, mWidth / 2 + width, mHeight / 2 + drawHight / 2 + width/2);
            canvas.drawOval(rectF2, paint);

            /**
             * 改变数值
             */
            if (alpha > 0 && width < mMaxWidth) {
                mAlphas.set(i, alpha - 25 > 0 ? alpha - 25 : 1);
                mWidths.set(i, width + mDiffuseSpeed);
            }

        }


        if (mWidths.get(mWidths.size() - 1) >= 50) {
            mAlphas.add(255);
            mWidths.add(0);
        }

        if (mWidths.size() >= 2) {

            if(mWidths.get(0)>=100) {
                mWidths.remove(0);
                mAlphas.remove(0);
            }
        }


        /**
         * 阴影
         */
        float f_shadow = 0.1f;
        paint.setColor(mContext.getResources().getColor(R.color.color_shape));
        RectF rectF1 = new RectF(mWidth / 2 - 100 * f_shadow, mHeight / 2 + drawHight / 2 - 50 * f_shadow, mWidth / 2 + 100 * f_shadow, mHeight / 2 + drawHight / 2 + 50 * f_shadow);
        canvas.drawOval(rectF1, paint);

        /**
         * 图片
         */
        paint.setColor(mContext.getResources().getColor(R.color.color_gray));
        canvas.drawBitmap(bitmap, (mWidth - drawWidth) / 2, (mHeight - drawHight) / 2, paint);


        invalidate();


    }



}
