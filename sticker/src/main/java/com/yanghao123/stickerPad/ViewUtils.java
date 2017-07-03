package com.yanghao123.stickerPad;

import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * Created by yanghao on 2017/6/28.
 */

public class ViewUtils {
    public static int dp2px(Resources res, float dp) {
        DisplayMetrics metrics = res.getDisplayMetrics();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, metrics));
    }

    public static int sp2px(Resources res, float sp) {
        DisplayMetrics metrics = res.getDisplayMetrics();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, metrics));
    }
}
