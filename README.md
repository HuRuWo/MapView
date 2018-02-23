# MapView
最好的yibu更新

地图区域点击，地图热点 最好的地图自定义 功能完善 性能良好 包括地图标志物添加 可随便使用，不受限制

# 前言

最近制作一款和地图相关的软件，需要在地图上点击不同的色块进入不同的子地图。并且需要在地图上添加标志物。

显然当前的第三方地图sdk无法完成，所以需要自定义View。

# 效果展示

![gif.gif](https://github.com/HuRuWo/MapView/blob/master/sceen/gif.gif)

![click.gif](https://github.com/HuRuWo/MapView/blob/master/sceen/click.gif)

# 思路

-- 地图
1. 解析SVG获得地图的path
2. 自定义View绘制path
3. 3.点击事件 计算点位置所落地点
4. 由标记的出每个区域中心的位置

-- 标志物

需要覆盖在view的指定位置，有可能是图片/View或者其他东西

# MapView

## 解析SVG图片

### 首先把SVG转化为

通过[Android SVG to VectorDrawable](http://inloop.github.io/svg2android/)
转化为android格式的文件，复制后导入工程。



## 编写工具类解析svg的path

这个方法在包`android.support.v4.graphics`下面

**原理则是:**
取出path的点，按svg图片规则读取成android里面的绘图规则连线，成为一个整的path。
PathParser

## 自定义View绘制取出来的Path

这个就很简单了,只需要使用`canvas.drawPath`

核心ondraw:
```
        canvas.save();
        canvas.scale(scale, scale, 0, 0); //中心缩放 
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

        if (!ispos) {
            if (onPathDrawListener != null) {
                onPathDrawListener.onPathDrawFinish(getPoints());
            }
            ispos = !ispos;
        }

        canvas.restore();
```
其中调用的三个绘图方法:
```
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
```

## path路径的点击事件

1.定义点击事件回调:
```
public interface OnPathClickListener {
        void onPathClick(PathItem p);
    }
```
2.View点击事件辅助工具`GestureDetector`
```
mDetector = new GestureDetector(context, this);
        mDetector.setOnDoubleTapListener(this);
```

3.View继承接口

包括普通点击事件以及单双击事件
```
implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener
```
4.方法实现
```
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

   

```

可以看到核心的单击事件，判断是否属于某个path区域,并回调。
```
if (BitmapUtil.isTouch((int) x, (int) y, pathItem.getPath())) {
                if (onPathClickListener != null) {
                    onPathClickListener.onPathClick(pathItem);
                }
                pathItem.setIsSelect(true);
            } else {
                pathItem.setIsSelect(false);
            }
```
5.最后记得把view触控事件交给`GestureDetector`
```
 /**
     * ---------------------手势的处理---------------
     **/
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mDetector.onTouchEvent(event); //把手势相关操作返回给 手势操控类
    }
```

-----------------------

以上就是绘制地图的方法，接下绘制地图标志物。

# 地图标志物

## 自定义ViewGroup绘制遮盖物

MapView获取区域中点的位置:

写一个getMapPoint方法，获取各个市区中心点位置，方便获取遮盖物。

然后在MapView的父布局添加子View

1.定义绘图完成，计算完成回调:

```
   private OnPathDrawListener onPathDrawListener;
    public interface OnPathDrawListener {
        void onPathDrawFinish(List<float[]> pos);
    }
```
计算地图中的市区中点
```
float[] pos2 = new float[2];
        measure.getPosTan(0, pos2, null);

        float[] point = new float[2];

        point[0] = (pos1[0] + pos2[0]) / 2;
        point[1] = (pos1[1] + pos2[1]) / 2;


        points.add(point);

```
```
if (onPathDrawListener != null) {
                onPathDrawListener.onPathDrawFinish(getPoints());
            }
```

# 获得回调，绘制遮盖View

```
ztemap.setOnPathDrawListener(new ZTEMapView.OnPathDrawListener() {
          @Override
          public void onPathDrawFinish(List<float[]> pos) {
              for (float[] point : points) {
            PointView PointView = new PointView(getContext());
            PointView.scrollTo((int) -point[0]+width/2, (int) -point[1]+hight/2);
            addView(PointView);
        }
          }
      });
```

PointView为自定义的波浪View

---------------------
到此为止，整个图绘制完成了。


# View加载优化

1.优化地图资源加载速度

由于每次从xml资源取得path路径的string是非常耗时的io操作。所以可以使用数据库缓存得到的path路径资源。只需要第一次加载之后，都从数据库获取。
具体看demo即可。

2.标志物的加载需要在地图的加载之后，所以需要设置延迟，以防界面卡顿。具体逻辑及时监听地图的
```
addOnGlobalLayoutListener
```

3.动画绘制的优化


# 项目地址

[HuRuWo的MapView](https://github.com/HuRuWo/MapView)