package com.yanghao123.stickerPad;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by yanghao on 2017/6/27.
 */

public class StickerPad extends View {

    private LinkedList<Sticker> mElements = new LinkedList<>();
    private Sticker mSelectedElement;

    public StickerPad(Context context) {
        super(context);
    }

    public StickerPad(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public StickerPad(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void addElement(Sticker element) {
        element.setCallback(this);
        mElements.add(element);
        if (mSelectedElement != null) {
            mSelectedElement.setSelected(false);
        }
        mSelectedElement = element;
        element.setSelected(true);
        invalidate();
    }

    @Override
    protected boolean verifyDrawable(@NonNull Drawable who) {
        return mElements.contains(who) || super.verifyDrawable(who);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            Sticker selectElement = null;
            Iterator<Sticker> iterator = mElements.descendingIterator();
            while (iterator.hasNext()) {
                Sticker element = iterator.next();
                if (element.isHit(event.getX(), event.getY())) {
                    selectElement = element;
                    break;
                }
            }
            if (mSelectedElement != null) {
                mSelectedElement.setSelected(false);
            }
            mSelectedElement = selectElement;
            if (mSelectedElement != null) {
                mSelectedElement.setSelected(true);
            }
        }
        if (mSelectedElement != null) {
            return mSelectedElement.onTouchEvent(event);
        } else {
            return super.onTouchEvent(event);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (Sticker el : mElements) {
            el.draw(canvas);
        }
    }
}
