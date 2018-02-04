package com.huruwo.mapview.mapview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018\2\4 0004.
 */

public class MapFrameLayout extends FrameLayout  {

    private int width, hight;
    private List<float[]> points = new ArrayList<>();

    public MapFrameLayout(Context context) {
        super(context);
    }

    public MapFrameLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MapFrameLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public List<float[]> getPoints() {
        return points;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        hight = h;

    }

    public void setPoints(List<float[]> points) {
        this.points = points;
        for (float[] point : points) {
            PointView ztePointView = new PointView(getContext());
            ztePointView.scrollTo((int) -point[0]+width/2, (int) -point[1]+hight/2);
            addView(ztePointView);
        }
    }
}
