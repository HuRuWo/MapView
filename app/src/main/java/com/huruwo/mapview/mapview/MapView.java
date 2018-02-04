package com.huruwo.mapview.mapview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PathMeasure;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RawRes;
import com.huruwo.mapview.mapview.PathParser;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;



import java.util.ArrayList;
import java.util.List;

/**
 * @author: liuwan
 * @date: 2018-01-29
 * @action:
 */
public class MapView extends View implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {


    private Paint paintMap, paintText, paintPoint, paintBoder;
    private Context context;
    private List<PathItem> pathItems = new ArrayList<>();
    private String TAG = "ZTEMapView";
    private int widthSize = 0;
    private int heightSize = 0;
    private float scale = 1.0f;
    private float has_move_x = 0;
    private float has_move_y = 0;
    private float move_x = 0;
    private float move_y = 0;
    private OnPathClickListener onPathClickListener;
    private OnPathDrawListener onPathDrawListener;
    private boolean ispos = false;
    private float map_w, map_h;
    //手势监听器
    private GestureDetector mDetector;
    private List<float[]> points = new ArrayList<>();

    public MapView(Context context) {

        this(context, null);
    }

    public MapView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    private void init(Context context) {
        //关闭硬件加速
        //setBackgroundColor(getResources().getColor(R.color.colorAccent));
        this.context = context;

        mDetector = new GestureDetector(context, this);
        mDetector.setOnDoubleTapListener(this);

        setLayerType(LAYER_TYPE_SOFTWARE, null);
        paintMap = new Paint();
        paintMap.setAntiAlias(true);
        paintMap.setStyle(Paint.Style.FILL);
        paintMap.setStrokeWidth(5);

        paintText = new Paint();
        paintText.setAntiAlias(true);
        paintText.setStyle(Paint.Style.FILL);
        paintText.setStrokeWidth(5);
        paintText.setTextSize(20);
        paintText.setColor(Color.BLACK);

        paintPoint = new Paint();
        paintPoint.setAntiAlias(true);
        paintPoint.setStyle(Paint.Style.FILL);
        paintPoint.setStrokeWidth(5);
        paintPoint.setTextSize(20);
        paintPoint.setColor(Color.RED);


        paintBoder = new Paint();
        paintBoder.setAntiAlias(true);
        paintBoder.setColor(Color.WHITE);
        paintBoder.setStyle(Paint.Style.STROKE);
        paintBoder.setStrokeWidth(3);


    }


    public void loadMap(int mapres, String mapname) {
        parserPaths(context, mapres, mapname);
    }


    public interface OnPathClickListener {
        void onPathClick(PathItem p);
    }
    public interface OnPathDrawListener {
        void onPathDrawFinish(List<float[]> pos);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);


//        // 获取宽-测量规则的模式和大小
//        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        widthSize = MeasureSpec.getSize(widthMeasureSpec);
//
//        // 获取高-测量规则的模式和大小
//        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        heightSize = MeasureSpec.getSize(heightMeasureSpec);

        // 设置wrap_content的默认宽 / 高值
        // 默认宽/高的设定并无固定依据,根据需要灵活设置
        // 类似TextView,ImageView等针对wrap_content均在onMeasure()对设置默认宽 / 高值有特殊处理,具体读者可以自行查看
        int mWidth = (int) map_w;
        int mHeight = (int) map_h;

        // 当布局参数设置为wrap_content时，设置默认值
        if (getLayoutParams().width == ViewGroup.LayoutParams.WRAP_CONTENT && getLayoutParams().height == ViewGroup.LayoutParams.WRAP_CONTENT) {
            setMeasuredDimension(mWidth, mHeight);
            // 宽 / 高任意一个布局参数为= wrap_content时，都设置默认值
        } else if (getLayoutParams().width == ViewGroup.LayoutParams.WRAP_CONTENT) {
            setMeasuredDimension(mWidth, heightSize);
        } else if (getLayoutParams().height == ViewGroup.LayoutParams.WRAP_CONTENT) {
            setMeasuredDimension(widthSize, mHeight);
        }

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        widthSize = w;
        heightSize = h;
    }

    /**
     * ---------------------手势的处理---------------
     **/
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mDetector.onTouchEvent(event); //把手势相关操作返回给 手势操控类
    }


    /**
     * ---------------------手势的处理---------------
     **/

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        canvas.scale(scale, scale, 0, 0); //中心缩放 记得后面两个参数 为中点
//      canvas.drawLine(-widthSize,0,widthSize,0,paintMap);
//      canvas.drawLine(0,-heightSize,0,heightSize,paintMap);
        for (PathItem pathItem : pathItems) {
            if ((pathItems.indexOf(pathItem) > pathItems.size()/2-1)) {
            } else {
                pathItemDraw(canvas, pathItem);
            }
        }
        /**
         * 文字绘制
         */
        points.clear();
        for (PathItem pathItem : pathItems) {
            if (pathItems.indexOf(pathItem) > pathItems.size()/2-1) {
                pathCenterText(canvas, pathItem);
            } else {
                pathBoder(canvas, pathItem);
            }
        }

        if (!ispos&&points.size()>0) {
            if (onPathDrawListener != null) {
                onPathDrawListener.onPathDrawFinish(getPoints());
            }
            ispos = !ispos;
        }

        canvas.restore();
    }


    public float getMinScale() {

        Log.e("getMinScale", widthSize + "//" + map_w + "//" + heightSize + "//" + map_h);
        return Math.min(widthSize / map_w, heightSize / map_h);
    }

    public void setScacle(float scale) {
        this.scale = scale;
        invalidate();
    }


    public void pathItemDraw(final Canvas canvas, PathItem pathItem) {
        if (pathItem.getIsSelect()) {
            //首先绘制选中的背景阴影
            paintMap.clearShadowLayer();
            paintMap.setShadowLayer(8, 0, 0, Color.BLACK);
            canvas.drawPath(pathItem.getPath(), paintMap);
            //绘制具体显示的
            paintMap.clearShadowLayer();
            paintMap.setColor(Color.WHITE);
            canvas.drawPath(pathItem.getPath(), paintMap);

        } else {
            //绘制具体显示的
            paintMap.clearShadowLayer();
            paintMap.setColor(Color.parseColor(pathItem.getColor()));
            canvas.drawPath(pathItem.getPath(), paintMap);
        }


    }
    public void pathCenterText(Canvas canvas, PathItem pathItem) {

        PathMeasure measure = new PathMeasure(pathItem.getPath(), false);
        float[] pos1 = new float[2];
        measure.getPosTan(measure.getLength() / 2, pos1, null);

        /**
         * 计算字体的宽高
         */
        Rect rect = new Rect();
        paintText.getTextBounds(pathItem.getTitle(), 0, pathItem.getTitle().length(), rect);

        int w = rect.width();
        int h = rect.height();


        canvas.drawText(pathItem.getTitle(), pos1[0] - w / 2, pos1[1] + h, paintText);


        /**
         * 两点求中点
         */
        float[] pos2 = new float[2];
        measure.getPosTan(0, pos2, null);

        float[] point = new float[2];

        point[0] = (pos1[0] + pos2[0]) / 2;
        point[1] = (pos1[1] + pos2[1]) / 2;


        points.add(point);

    }
    public void pathBoder(Canvas canvas, PathItem pathItem) {

        canvas.drawPath(pathItem.getPath(), paintBoder);
    }


    public void setOnZteClickListener(final OnPathClickListener onPathClickListener) {
        this.onPathClickListener = onPathClickListener;
    }

    public void setOnPathDrawListener(OnPathDrawListener onPathDrawListener) {
        this.onPathDrawListener = onPathDrawListener;
    }

    /**
     * 解析path  异步解析
     */
    public void parserPaths(final Context context, @RawRes final int raw_res, final String mapname) {

        List<MapBean> mapBeans = DaoHepler.getAppDao().getMapBeanDao().queryRaw("where map_name = ?", mapname);

        if (mapBeans.size() > 0) {
            //把string转化成path
            MapBean mapBean = mapBeans.get(0);

            map_w = mapBean.getMap_w();
            map_h = mapBean.getMap_h();

            pathItems = DaoHepler.getAppDao().getPathItemDao()._queryMapBean_PathItems(mapBean.getId());

            if (pathItems.size() > 0) {

                for (PathItem pathItem : pathItems) {
                    pathItem.setPath(PathParser.createPathFromPathData(pathItem.getXmlvalue()));
                }
            }
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {


                    List<PathItem> pathlists = XmlUtil.getXmlValue(context, raw_res, mapname);

                    /**
                     * 获取宽高
                     */
                    MapBean mapBean = DaoHepler.getAppDao().getMapBeanDao().queryRaw("where map_name = ?", mapname).get(0);
                    map_w = mapBean.getMap_w();
                    map_h = mapBean.getMap_h();

                    Message message = new Message();
                    message.obj = pathlists;
                    message.what = 0;
                    handler.sendMessage(message);
                }
            }).start();
        }
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            pathItems = (List<PathItem>) msg.obj;
            Toast.makeText(context, getMinScale() + "", Toast.LENGTH_SHORT).show();
            scale = getMinScale();
            invalidate();
        }
    };


    /**
     * --------------------------------------------------------------------------------------------------------------------------------
     **/

    @Override
    public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
        Log.e("触摸点击", "onSingleTapConfirmed" + motionEvent.getY() + "---" + motionEvent.getY());
        float x = (motionEvent.getX() - has_move_x) / scale;
        float y = (motionEvent.getY() - has_move_y) / scale;
        for (PathItem pathItem : pathItems) {

            if (BitmapUtil.isTouch((int) x, (int) y, pathItem.getPath())) {
                if (onPathClickListener != null) {
                    onPathClickListener.onPathClick(pathItem);
                }
                pathItem.setIsSelect(true);
            } else {
                pathItem.setIsSelect(false);
            }

        }
        invalidate();
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent motionEvent) {
        Log.e("触摸点击", "onDoubleTap" + motionEvent.getY() + "---" + motionEvent.getY());
        /**
         * 双击放大
         */
        scale = scale * 1.1f;
        invalidate();
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent motionEvent) {
        Log.e("触摸点击", "onDoubleTapEvent" + motionEvent.getY() + "---" + motionEvent.getY());
        return false;
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {

        Log.e("触摸点击", "onDown" + motionEvent.getY() + "---" + motionEvent.getY());

        return true;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {
        Log.e("触摸点击", "onShowPress" + motionEvent.getY() + "---" + motionEvent.getY());
    }

    /**
     * 这个事件 在双击事件也会触发 所以为了区分 单击事件应该 放在onSingleTapConfirmed 中
     *
     * @param motionEvent
     * @return
     */
    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        Log.e("触摸点击", "onSingleTapUp" + motionEvent.getY() + "---" + motionEvent.getY());

        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float v, float v1) {
        Log.e("触摸点击", "onScroll" + e1.getY() + "---" + e2.getY());

        move_x = e2.getX() - e1.getX();
        move_y = e2.getY() - e1.getY();

        Log.e("计划移动", move_x + "----" + move_y);

        invalidate();

        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {
        Log.e("触摸点击", "onLongPress" + motionEvent.getY() + "---" + motionEvent.getY());

        /**
         * 长按恢复
         */
        has_move_x = 0;
        has_move_y = 0;
        move_x = 0;
        move_y = 0;

        scale = getMinScale();

        invalidate();

    }

    /**
     * 抬手位置
     *
     * @param e1
     * @param e2
     * @param v
     * @param v1
     * @return
     */
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float v, float v1) {
        Log.e("触摸点击", "onFling" + e1.getY() + "---" + e2.getY());

        return false;
    }

    public List<float[]> getPoints() {

        for (float[] point : points) {
            point[0] = point[0] * scale;
            point[1] = point[1] * scale;
        }

        return points;
    }

}
