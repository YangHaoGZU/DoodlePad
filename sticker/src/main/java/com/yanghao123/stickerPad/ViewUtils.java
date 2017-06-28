package com.yanghao123.stickerPad;

import android.content.res.Resources;

/**
 * Created by yanghao on 2017/6/28.
 */

public class ViewUtils {
    public static int dp2px(Resources res, float dp) {
        float scale = res.getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
