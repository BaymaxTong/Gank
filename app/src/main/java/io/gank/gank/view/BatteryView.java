package io.gank.gank.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * 自定义View
 * Created by baymax on 2016/7/18.
 */
public class BatteryView extends View {

    public BatteryView(Context context) {
        super(context);
    }

    public BatteryView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BatteryView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private Paint mPaint;
    // 淡白色
    private static final int WHITE_COLOR = 0xffFFFFFF;
    // 橙色
    private static final int ORANGE_COLOR = 0xffFFCC66;
    // 棕色
    private static final int RED_COLOR = 0xFFFC3F30;
    // 黑色
    private static final int BLACK_COLOR = 0xFF3F4749;
    // 蓝色
    private static final int GREEN_COLOR = 0xFF71D86B;

    // 当前进度
    private int mProgress;
    private int mTotalWidth, mTotalHeight;//总的宽与高

    /**
     * 画笔初始化
     */
    private void initPaint() {
        if (mPaint == null) {
            mPaint = new Paint();
        } else {
            mPaint.reset();
        }
        mPaint.setAntiAlias(true);//边缘无锯齿
    }

    /**
     * 设置进度
     *
     * @param progress
     */
    public void setProgress(int progress) {
        this.mProgress = progress;
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBackground(canvas);
        drawProgress(canvas);
    }

    private void drawProgress(Canvas canvas) {
        for(int i = 1;i <= mProgress; i++){
            if(i % 3 == 1){
                drawBattery(canvas, i, RED_COLOR);
            }else if(i % 3 == 2){
                drawBattery(canvas, i, ORANGE_COLOR);
            }else if(i % 3 == 0){
                drawBattery(canvas, i, GREEN_COLOR);
            }
        }
    }

    private void drawBattery(Canvas canvas, int index, int color){
        //绘制电池的最内框形状
        initPaint();
        mPaint.setColor(color);
        mPaint.setStyle(Paint.Style.FILL);
        RectF rectF = new RectF(mTotalWidth/2 - 230 + (index-1)*46, mTotalHeight/2 - 80, mTotalWidth/2 - 230 + index*46, mTotalHeight/2 + 80);
        canvas.drawRoundRect(rectF,30,30,mPaint);
        //
    }

    private void drawBackground(Canvas canvas) {
        //绘制圆形背景
        initPaint();
        mPaint.setColor(BLACK_COLOR);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(mTotalWidth/2, mTotalHeight/2, 400,mPaint);
        //绘制电池的最外框形状
        initPaint();
        mPaint.setColor(WHITE_COLOR);
        mPaint.setStyle(Paint.Style.FILL);
        RectF rectF = new RectF(mTotalWidth/2 - 250, mTotalHeight/2 - 100, mTotalWidth/2 + 250, mTotalHeight/2 + 100);
        canvas.drawRoundRect(rectF,30,30,mPaint);
        //绘制电池的最内框形状
        initPaint();
        mPaint.setColor(BLACK_COLOR);
        mPaint.setStyle(Paint.Style.FILL);
        RectF rectFin = new RectF(mTotalWidth/2 - 230, mTotalHeight/2 - 80, mTotalWidth/2 + 230, mTotalHeight/2 + 80);
        canvas.drawRoundRect(rectFin,30,30,mPaint);
        //绘制电池的头部
        initPaint();
        mPaint.setColor(WHITE_COLOR);
        mPaint.setStyle(Paint.Style.FILL);
        RectF rectHeader = new RectF(mTotalWidth/2 + 230, mTotalHeight/2 - 30, mTotalWidth/2 + 270, mTotalHeight/2 + 30);
        canvas.drawRoundRect(rectHeader,20,20,mPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTotalWidth = w;
        mTotalHeight = h;
    }
}
