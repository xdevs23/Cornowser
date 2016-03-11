package org.xdevs23.ui.utils;

import android.os.Build;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class LayoutPUtil {

    public static RelativeLayout.LayoutParams relativeFromParams(ViewGroup.LayoutParams layoutParams) {
        return (RelativeLayout.LayoutParams)layoutParams;
    }

    public static LinearLayout.LayoutParams linearFromParams(ViewGroup.LayoutParams layoutParams) {
        return (LinearLayout.LayoutParams)layoutParams;
    }

    public static ViewGroup.LayoutParams relativeToParams(RelativeLayout.LayoutParams layoutParams) {
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                layoutParams.width,
                layoutParams.height
        );
        params.layoutAnimationParameters = layoutParams.layoutAnimationParameters;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
            params.resolveLayoutDirection(layoutParams.getLayoutDirection());

        return params;
    }

    public static ViewGroup.LayoutParams linearToParams(LinearLayout.LayoutParams layoutParams) {
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                layoutParams.width,
                layoutParams.height
        );
        params.layoutAnimationParameters = layoutParams.layoutAnimationParameters;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
            params.resolveLayoutDirection(layoutParams.getLayoutDirection());

        return params;
    }

    public static int getWidth(ViewGroup.LayoutParams params) {
        return params.width;
    }

    public static int getHeight(ViewGroup.LayoutParams params) {
        return params.height;
    }

}
