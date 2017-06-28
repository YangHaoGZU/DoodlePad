package com.yanghao123.stickerPad;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

/**
 * Created by yanghao on 2017/6/28.
 */

public class DrawableSticker extends TransformSticker {

    private Drawable mSticker;

    public DrawableSticker(Resources res, float x, float y, Drawable sticker) {
        super(res, x, y);
        mSticker = sticker;
        mSticker.setFilterBitmap(true);
        updateBounds();
    }

    @Override
    protected void onDrawElement(Canvas canvas) {
        mSticker.setBounds(getBounds());
        mSticker.draw(canvas);
    }

    @Override
    public int getIntrinsicWidth() {
        return mSticker.getIntrinsicWidth();
    }

    @Override
    public int getIntrinsicHeight() {
        return mSticker.getIntrinsicHeight();
    }
}
