package com.yanghao123.stickerPad;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by yanghao on 2017/6/28.
 * 涂鸦贴纸。
 */

public class DoodleSticker extends StickerElement {

    private int mWidth;
    private int mHeight;
    private Paint mPaint;
    private List<Segment> mSegments = new LinkedList<>();
    private Segment mCurrentSegment;

    public DoodleSticker(Resources res, float x, float y, int width, int height) {
        super(res, x, y);
        mWidth = width;
        mHeight = height;
        updateBounds();

        mPaint = new Paint(Paint.DITHER_FLAG);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setColor(Color.YELLOW);
        mPaint.setStrokeWidth(12);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                start(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                moveTo(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_UP:
                end(event.getX(), event.getY());
                break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (isSelected()) {
            canvas.drawColor(0x33000000);
        }
        for (Segment seg : mSegments) {
            seg.draw(canvas);
        }
    }

    @Override
    public int getIntrinsicWidth() {
        return mWidth;
    }

    @Override
    public int getIntrinsicHeight() {
        return mHeight;
    }

    @Override
    public boolean isHit(float x, float y) {
        if (mSegments.isEmpty() || isSelected()) {
            return true;
        }
        for (Segment seg : mSegments) {
            if (seg.touched(x, y)) {
                return true;
            }
        }
        return false;
    }

    private void start(float x, float y) {
        mCurrentSegment = new Segment(new Path(), new Paint(mPaint));
        mSegments.add(mCurrentSegment);
        mCurrentSegment.start(x, y);
        invalidateSelf();
    }

    private void moveTo(float x, float y) {
        if (mCurrentSegment != null) {
            mCurrentSegment.moveTo(x, y);
            invalidateSelf();
        }
    }

    private void end(float x, float y) {
        if (mCurrentSegment != null) {
            mCurrentSegment.end(x, y);
            mCurrentSegment = null;
            invalidateSelf();
        }
    }

    private static class Segment {
        public List<PointF> mPoints = new ArrayList<>();
        public Path path;
        public Paint paint;

        Segment(Path path, Paint paint) {
            this.path = path;
            this.paint = paint;
        }

        public void start(float x, float y) {
            path.reset();
            path.moveTo(x, y);

            mPoints.add(new PointF(x, y));
        }

        public void moveTo(float x, float y) {
            PointF last = mPoints.get(mPoints.size() - 1);
            path.quadTo(last.x, last.y, x, y);

            mPoints.add(new PointF(x, y));
        }

        public void end(float x, float y) {
            moveTo(x, y);
        }

        public void draw(Canvas canvas) {
            canvas.drawPath(path, paint);
        }

        public boolean touched(float x, float y) {
            if (mPoints.size() <= 1) {
                return false;
            }
            for (int i = 0; i < mPoints.size() - 2; i++) {
                PointF start = mPoints.get(i);
                PointF end = mPoints.get(i + 1);
                float left;
                float right;
                if (start.x > end.x) {
                    left = end.x;
                    right = start.x;
                } else {
                    left = start.x;
                    right = end.x;
                }
                left -= paint.getStrokeWidth() / 2;
                right += paint.getStrokeWidth() / 2;
                float top;
                float bottom;
                if (start.y > end.y) {
                    top = end.y;
                    bottom = start.y;
                } else {
                    top = start.y;
                    bottom = end.y;
                }
                top -= paint.getStrokeWidth() / 2;
                bottom += paint.getStrokeWidth() / 2;
                if (x >= left && x <= right && y >= top && y <= bottom) {
                    return true;
                }
            }
            return false;
        }
    }
}
