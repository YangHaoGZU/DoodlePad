package com.yanghao123.stickerPad;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.MotionEvent;


/**
 * Created by yanghao on 2017/6/28.
 * 涂鸦的元素。
 */
public abstract class Sticker extends Drawable {
    private static final int DEFAULT_PAINT_COLOR = Color.parseColor("#f2a670");
    private static final float DEFAULT_STROKE_WIDTH_DP = 1.0f;

    // Element的画笔，目前只是用来画边框
    private Paint mPaint;
    // 边框宽度
    private float mStrokeWidth;

    // 当前坐标
    private float mPosX;
    private float mPosY;
    // 旋转角度
    private float mRotate = 0.0f;
    // 缩放大小
    private float mScale = 1.0f;
    // 反变换矩阵。
    // Sticker的缩放和旋转都是对画布进行的操作，它真实的位置信息并没有发生变化，这会给点击事件的处理造成困难
    // 所以使用这个矩阵来对点击事件的坐标进行变换，使其能对应到Element的包围盒里面
    private Matrix mInvertMatrix = new Matrix();

    // Element是否被选中
    private boolean mSelected = false;

    public Sticker(Resources res, float x, float y) {
        mStrokeWidth = ViewUtils.dp2px(res, DEFAULT_STROKE_WIDTH_DP);

        mPosX = x;
        mPosY = y;
        mRotate = 0;
        mScale = 1.0f;

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(DEFAULT_PAINT_COLOR);
        mPaint.setStrokeWidth(mStrokeWidth);
        mPaint.setStyle(Paint.Style.STROKE);
    }

    /**
     * 子类需要在适当的时候调用这个方法完成Bounds的限定
     */
    protected void updateBounds() {
        setBounds((int) mPosX, (int) mPosY, (int) mPosX + getIntrinsicWidth(), (int) mPosY + getIntrinsicHeight());
        updateMatrix();
    }

    /**
     * 子类需要重写这个方法，完成自身的绘制。
     */
    protected abstract void onDraw(Canvas canvas);

    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        canvas.save();
        canvas.scale(mScale, mScale, getBounds().centerX(), getBounds().centerY());
        canvas.rotate(mRotate, getBounds().centerX(), getBounds().centerY());
        if (mSelected) {
            //当画布缩放时，rect的线条也会缩放，但我们期望缩放时线条的粗细并不发生变化
            mPaint.setStrokeWidth(mStrokeWidth / mScale);
            canvas.drawRect(getBounds(), mPaint);
        }
        onDraw(canvas);
        canvas.restore();
    }

    @Override
    public void setAlpha(@IntRange(from = 0, to = 255) int alpha) {
        mPaint.setAlpha(alpha);
        invalidateSelf();
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
        invalidateSelf();
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    protected void updateMatrix() {
        mInvertMatrix.reset();
        mInvertMatrix.postScale(1.0f / mScale, 1.0f / mScale, getBounds().centerX(), getBounds().centerY());
        mInvertMatrix.postRotate(-mRotate, getBounds().centerX(), getBounds().centerY());
        invalidateSelf();
    }

    public void moveBy(float dx, float dy) {
        mPosY += dx;
        mPosY += dy;
        getBounds().offset((int) dx, (int) dy);
        updateMatrix();
    }

    public void scaleAndRotate(float dx, float dy) {
        float halfWidth = getIntrinsicWidth() / 2.0f;
        float halfHeight = getIntrinsicHeight() / 2.0f;
        float newRadius = PointF.length(dx - getBounds().centerX(), dy - getBounds().centerY());
        float oldRadius = PointF.length(halfWidth, halfHeight);
        mScale = newRadius / oldRadius;
        mRotate = (float) Math.toDegrees(Math.atan2(halfWidth, halfHeight)
                - Math.atan2(dx - getBounds().centerX(), dy - getBounds().centerY()));
        if (Math.abs(mRotate % 90) < 3) {
            mRotate = (float) (Math.round(mRotate / 90) * 90);
        }
        if (Math.abs(mRotate % 45) < 3) {
            mRotate = (float) (Math.round(mRotate / 45) * 45);
        }
        if (mScale < 0.1F) {
            mScale = 0.1F;
        }
        updateMatrix();
    }

    public boolean isHit(float x, float y) {
        float[] pts = new float[]{x, y};
        getMatrix().mapPoints(pts);

        return !getBounds().isEmpty() && getBounds().contains((int) pts[0], (int) pts[1]);
    }

    Matrix getMatrix() {
        return mInvertMatrix;
    }

    public float getScale() {
        return mScale;
    }

    public boolean isSelected() {
        return mSelected;
    }

    public void setSelected(boolean selected) {
        mSelected = selected;
        invalidateSelf();
    }

}
