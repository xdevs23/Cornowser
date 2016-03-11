package org.xdevs23.ui.utils;

import android.content.Context;

import org.xdevs23.android.app.XquidCompatActivity;

public class DpUtil {

    private DpUtil() {

    }

    public static float dp2pxf(Context context, int dp) {
        return XquidCompatActivity.dp2px(context, dp);
    }

    public static int dp2px(Context context, int dp) {
        return (int)dp2pxf(context, dp);
    }

    public static double dp2pxd(Context context, double dp) {
        return (double)XquidCompatActivity.dp2px(context, (float)dp);
    }

}
