package com.feilong.radarviewlib;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class RadarView extends View {
    private static final String TAG = "RadarView";

    private int mBaseColor = Color.BLUE;
    private int mAreaColor = Color.BLUE;
    private int mDotColor = Color.RED;
    private int mOutTextSize;
    /**
     * 最外层顶端的点
     */
    private List<PointF> mEndPoints;
    private int EDG_COUNT = 6;
    /**
     * 圈数，限制 3--5 圈
     */
    private int mLoop = 5;
    /**
     * 步长，限制 50--100
     */
    private float mStep = 60;
    /**
     * 「半径」长度
     */
    private float mLength = mStep * mLoop;
    private String[] mTexts = new String[]{
            "输出",
            "参团",
            "生存",
            "发育",
            "KDA",
            "经济",
    };
    private Paint mTextPaint;
    private Paint mDotPaint;
    private Paint mAreaPaint;
    private Paint mBasePaint;

    /**
     * 6条线上的点的 con 值，从 y 轴负方向开始画线，即竖直的上方
     */
    private static final PointF[] UNIT_POINTS = {
            new PointF(0, -1),
            new PointF(-(float) (Math.cos(Math.PI / 6)), -(float) (Math.cos(Math.PI / 3))),
            new PointF(-(float) (Math.cos(Math.PI / 6)), (float) (Math.cos(Math.PI / 3))),
            new PointF(0, 1),
            new PointF((float) (Math.cos(Math.PI / 6)), (float) (Math.cos(Math.PI / 3))),
            new PointF((float) (Math.cos(Math.PI / 6)), -(float) (Math.cos(Math.PI / 3))),

//            new PointF(0, -1),
//            new PointF((float) (Math.cos(Math.toRadians(30))), -(float) (Math.sin(Math.toRadians(30)))),
//            new PointF((float) (Math.cos(Math.toRadians(30))), (float) (Math.sin(Math.toRadians(30)))),
//            new PointF(0, 1),
//            new PointF((float) -(Math.cos(Math.toRadians(30))), -(float) (Math.sin(Math.toRadians(30)))),
//            new PointF((float) -(Math.cos(Math.toRadians(30))), (float) (Math.sin(Math.toRadians(30)))),
    };

    public RadarView(Context context) {
        super(context);
    }

    public RadarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RadarView);
        mBaseColor = ta.getColor(R.styleable.RadarView_baseColor, mBaseColor);
        mAreaColor = ta.getColor(R.styleable.RadarView_areaColor, mAreaColor);
        mDotColor = ta.getColor(R.styleable.RadarView_dotColor, mDotColor);
        mOutTextSize = ta.getDimensionPixelOffset(R.styleable.RadarView_outTextSize, mOutTextSize);
        ta.recycle();

        //初始化底部网状画笔
        mBasePaint = new Paint();
        mBasePaint.setAntiAlias(true);
        mBasePaint.setColor(mBaseColor);
        mBasePaint.setStyle(Paint.Style.STROKE);

        //初始化区域画笔
        mAreaPaint = new Paint();
        mAreaPaint.setAntiAlias(true);
        mAreaPaint.setColor(mAreaColor);
        mAreaPaint.setStyle(Paint.Style.FILL);
        mAreaPaint.setAlpha(100);

        //初始化点画笔
        mDotPaint = new Paint();
        mDotPaint.setAntiAlias(true);
        mDotPaint.setColor(mDotColor);
        mDotPaint.setStyle(Paint.Style.FILL);
        mDotPaint.setStrokeWidth(10);

        //初始化文字画笔
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(mOutTextSize);
        mTextPaint.setColor(mBaseColor);

        //初始化最外层六个点
        mEndPoints = new ArrayList<>();
        PointF pointF;
        for (int i = 0; i < EDG_COUNT; i++) {
            pointF = new PointF();
            pointF.x = UNIT_POINTS[i].x * mLength;
            pointF.y = UNIT_POINTS[i].y * mLength;
            mEndPoints.add(pointF);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        //保证宽高相等
        setMeasuredDimension(width > height ? width : height, width > height ? width : height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        //摆放子view的位置，如果没有子view，则不需要写任何逻辑
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //将坐标原点移动到中心
        canvas.translate(getWidth() / 2, getWidth() / 2);
        Path path = new Path();
        //画骨架,6条线
        for (int i = 0; i < mEndPoints.size(); i++) {
            path.moveTo(0, 0);
            path.lineTo(mEndPoints.get(i).x, mEndPoints.get(i).y);

            //画文字
            Rect rect = new Rect();
            mTextPaint.getTextBounds(mTexts[i], 0, mTexts[i].length(), rect);
            canvas.drawText(mTexts[i], mEndPoints.get(i).x > 0 ? mEndPoints.get(i).x + 10 : mEndPoints.get(i).x - rect.width(),
                    mEndPoints.get(i).y > 0 ? mEndPoints.get(i).y + rect.height() : mEndPoints.get(i).y - 10, mTextPaint);
        }
        canvas.drawPath(path, mBasePaint);
        path.reset();

        //画圈
        PointF firstPoint = mEndPoints.get(0);
        for (int i = mLoop; i >= 1; i--) {
            float rate = i / (float) mLoop;
            float firstX = firstPoint.x * rate;
            float firstY = firstPoint.y * rate;
            path.moveTo(firstX, firstY);
            for (int j = 1; j < mEndPoints.size(); j++) {
                PointF endPoint = mEndPoints.get(j);
                path.lineTo(endPoint.x * rate, endPoint.y * rate);
            }
            path.lineTo(firstX, firstY);
        }
        canvas.drawPath(path, mBasePaint);
        path.reset();

        //画区域
        List<PointF> pointFS = getRandomPoint();
        for (int i = 0; i < pointFS.size(); i++) {
            canvas.drawPoint(pointFS.get(i).x, pointFS.get(i).y, mDotPaint);
            path.lineTo(pointFS.get(i).x, pointFS.get(i).y);
        }
        path.lineTo(pointFS.get(0).x, pointFS.get(0).y);
        canvas.drawPath(path, mAreaPaint);

    }

    /**
     * 生成随机点
     *
     * @return
     */
    private List<PointF> getRandomPoint() {
        List<PointF> focused = new ArrayList<>(mEndPoints.size());
        PointF point;
        for (PointF pointF : mEndPoints) {
            point = new PointF();
            float random = 0;
            while (random < 0.2 || random > 0.8) {
                random = (float) Math.random();
            }
            point.x = (random * pointF.x);
            point.y = (random * pointF.y);
            focused.add(point);
        }
        return focused;
    }

    /**
     * 随机绘制一次
     */
    public void radomArea() {
        invalidate();
    }

}
