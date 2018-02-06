package com.demo.compressimgdemo.bottomsheet;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by n on 2017/10/25.
 */

public class MeasureUtil {

    public static int dp(Context context, int size) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, size, context.getResources().getDisplayMetrics());
    }

    public static int sp(Context context, int size) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, size, context.getResources().getDisplayMetrics());
    }
}
