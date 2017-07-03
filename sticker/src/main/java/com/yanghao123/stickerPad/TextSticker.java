package com.yanghao123.stickerPad;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by yanghao on 2017/7/3.
 */

public class TextSticker extends TransformSticker {
    private static final float DEFAULT_TEXT_SIZE_SP = 20.0f;
    private static final int DEFAULT_TEXT_COLOR = Color.YELLOW;

    private int mWidth;
    private int mHeight;

    private String mText;
    private Paint mTextPaint;

    public TextSticker(Resources res, float x, float y) {
        super(res, x, y);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTextSize(ViewUtils.sp2px(res, DEFAULT_TEXT_SIZE_SP));
        mTextPaint.setColor(DEFAULT_TEXT_COLOR);
    }

    public void setText(String text) {
        mText = text;
        mWidth = (int) mTextPaint.measureText(mText);
        mHeight = ViewUtils.sp2px(getResource(), DEFAULT_TEXT_SIZE_SP);
        updateBounds();
    }

    @Override
    protected void onDrawElement(Canvas canvas) {
        canvas.drawText(mText, getBounds().centerX(), getBounds().centerY() - ((mTextPaint.descent() + mTextPaint.ascent()) / 2), mTextPaint);
    }

    @Override
    public int getIntrinsicWidth() {
        return mWidth;
    }

    @Override
    public int getIntrinsicHeight() {
        return mHeight;
    }
}
