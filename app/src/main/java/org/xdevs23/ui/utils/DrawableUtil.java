package org.xdevs23.ui.utils;


import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;

public class DrawableUtil {

    private DrawableUtil() {

    }

    public static Drawable getDrawable(@DrawableRes int drawable, Context context) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            return context.getResources().getDrawable(drawable, null);
        else return context.getResources().getDrawable(drawable);
    }

    public static Drawable tintDrawable(@DrawableRes int drawable, Context context, int color) {
        Drawable drawabler = getDrawable(drawable, context);
        drawabler.setColorFilter(color, PorterDuff.Mode.MULTIPLY);
        return drawabler;
    }
}
