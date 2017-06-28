package com.yanghao123.stickerPad;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * Created by yanghao on 2017/6/28.
 * 可以变换
 */

public abstract class TransformSticker extends Sticker {
    private static final float DEFAULT_ACTION_RADIUS_DP = 10.0f;

    private static final int TOUCH_MODE_NON = 0;
    private static final int TOUCH_MODE_MOVE = 1;
    private static final int TOUCH_MODE_SCALE_AND_ROTATE = 2;

    private final Drawable mActionRemove;
    private final Drawable mActionScaleAndRotate;
    private float mActionRadius;

    private int mTouchMode = TOUCH_MODE_NON;
    private GestureDetector mGestureDetector;

    public TransformSticker(Resources res, float x, float y) {
        super(res, x, y);
        mActionRemove = ResourcesCompat.getDrawable(res, R.drawable.edit_action_delete, null);
        mActionScaleAndRotate = ResourcesCompat.getDrawable(res, R.drawable.edit_action_rotation_scale, null);

        mActionRadius = ViewUtils.dp2px(res, DEFAULT_ACTION_RADIUS_DP);
        mGestureDetector = new GestureDetector(new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                float x = e.getX();
                float y = e.getY();
                mTouchMode = TOUCH_MODE_MOVE;
                if (isHitActionRemove(x, y)) {
                    // TODO 添加移除元素的回调
                    mTouchMode = TOUCH_MODE_NON;
                } else if (isHitActionScaleAndRotate(x, y)) {
                    mTouchMode = TOUCH_MODE_SCALE_AND_ROTATE;
                }
                return mTouchMode != TOUCH_MODE_NON;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                if (mTouchMode == TOUCH_MODE_MOVE) {
                    moveBy(-distanceX, -distanceY);
                } else if (mTouchMode == TOUCH_MODE_SCALE_AND_ROTATE) {
                    scaleAndRotate(e2.getX(), e2.getY());
                }
                return true;
            }

        });
    }

    protected abstract void onDrawElement(Canvas canvas);

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        onDrawElement(canvas);
        if (isSelected()) {
            //当画布缩放标也会缩放，但我们期望缩放时图标并不发生变化
            float radius = mActionRadius / getScale();
            mActionRemove.setBounds((int) (getBounds().left - radius),
                    (int) (getBounds().top - radius),
                    (int) (getBounds().left + radius),
                    (int) (getBounds().top + radius));
            mActionRemove.draw(canvas);

            mActionScaleAndRotate.setBounds((int) (getBounds().right - radius),
                    (int) (getBounds().bottom - radius),
                    (int) (getBounds().right + radius),
                    (int) (getBounds().bottom + radius));
            mActionScaleAndRotate.draw(canvas);
        }
    }

    @Override
    public boolean isHit(float x, float y) {
        boolean hit = false;
        if (isSelected()) {
            hit = isHitActionRemove(x, y) || isHitActionScaleAndRotate(x, y);
        }
        return hit || super.isHit(x, y);
    }

    private boolean isHitActionRemove(float x, float y) {
        float[] pts = new float[]{x, y};
        getMatrix().mapPoints(pts);
        return !mActionRemove.getBounds().isEmpty()
                && mActionRemove.getBounds().contains((int) pts[0], (int) pts[1]);
    }

    private boolean isHitActionScaleAndRotate(float x, float y) {
        float[] pts = new float[]{x, y};
        getMatrix().mapPoints(pts);
        return !mActionScaleAndRotate.getBounds().isEmpty()
                && mActionScaleAndRotate.getBounds().contains((int) pts[0], (int) pts[1]);
    }
}
