package com.hexx.rxjavaretrofitdemo.utils;

import android.content.Context;

/**
 * Created by User on 2018/2/2.
 */

public class DisplayUtil {
    static public int px2dp(Context context, float px) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    public static int dp2px(Context context, float dp) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
