package com.hexx.rxjavaretrofitdemo.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by HE on 2018/2/1.
 */

public class ToastUtil {
    public static void show(Context context, String content) {
        Toast.makeText(context, content, Toast.LENGTH_LONG).show();
    }
}
