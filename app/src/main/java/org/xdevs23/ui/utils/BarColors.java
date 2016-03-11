package org.xdevs23.ui.utils;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.view.Window;
import android.view.WindowManager;

import io.xdevs23.cornowser.browser.R;

public class BarColors {

    private BarColors() {

    }

    /**
     * Enable system bars coloring
     * @param window Window
     */
    public static void enableBarColoring(Window window) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            disableTranslucentBars(window);
        }
    }

    /**
     * Disable system bars coloring
     * @param window Window
     */
    public static void disableBarColoring(Window window) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            window.clearFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
    }

    /**
     * Enable translucent bars (disables bar coloring)
     * @param window Window
     */
    public static void enableTranslucentBars(Window window) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            disableBarColoring(window);
        }
    }

    /**
     * Disable translucent bars
     * @param window Window
     */
    public static void disableTranslucentBars(Window window) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    /**
     * Enable bar coloring and set a color
     * @param window Window
     * @param colorID Color resource
     */
    public static void enableBarColoring(Window window, @ColorRes int colorID) {
        enableBarColoring(window);
        updateBarsColor(window, colorID);
    }

    /**
     * Set status bar color
     * @param color Color
     * @param window Window
     */
    public static void setStatusBarColor(int color, Window window) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            window.setStatusBarColor(color);
    }

    /**
     * Set navigation bar color
     * @param color Color
     * @param window Window
     */
    public static void setNavigationBarColor(int color, Window window) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            window.setNavigationBarColor(color);
    }

    /**
     * Set bars color without brightness check
     * @param color Color
     * @param window Window
     * @param applyDarken Use darker color
     * @param navbar Tint navigation bar
     * @param statusbar Tint status bar
     */
    public static void updateBarsColorWoCheck(int color, Window window, boolean applyDarken,
                                              boolean navbar, boolean statusbar) {
        if(statusbar)
            setStatusBarColor    ((applyDarken ? getDarkerColor(color) : color), window);
        if(navbar)
            setNavigationBarColor((applyDarken ? getDarkerColor(color) : color), window);
    }

    /**
     * Set bars color
     * @param color Color
     * @param window Window
     * @param applyDarken Use darker color
     * @param navbar Tint navigation bar
     * @param statusbar Tint status bar
     */
    public static void updateBarsColor(int color, Window window, boolean applyDarken,
                                       boolean navbar, boolean statusbar) {
        int sColor = color;
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        if(hsv[2] > 0.56f && hsv[1] < 0.12) hsv[2] = 0.56f;
        sColor = Color.HSVToColor(hsv);
        updateBarsColorWoCheck(sColor, window, applyDarken, navbar, statusbar);
    }

    /**
     * Set both navigation bar and status bar color
     * @param color Color
     * @param window Window
     * @param applyDarken Use darker color
     * @param tintBoth Tint both (use true. Otherwise nothing will be tinted)
     */
    public static void updateBarsColor(int color, Window window, boolean applyDarken, boolean tintBoth) {
        if(tintBoth) updateBarsColor(color, window, applyDarken, true, true);
    }

    /**
     * Set status bar color
     * @param color Color
     * @param window Window
     * @param applyDarken Use darker color
     */
    public static void updateBarsColor(int color, Window window, boolean applyDarken) {
        updateBarsColor(color, window, applyDarken, false, true);
    }

    /**
     * Set status bar color and use darker color
     * @param color Color
     * @param window Window
     */
    public static void updateBarsColor(int color, Window window) {
        updateBarsColor(color, window, true);
    }

    /**
     * Set status bar color using color resource
     * @param window Window
     * @param colorID Color resource
     * @param applyDarken Use darker color
     * @param navbar Tint navigation bar
     * @param statusbar Tint status bar
     */
    public static void updateBarsColor(Window window, @ColorRes int colorID, boolean applyDarken,
                                       boolean navbar, boolean statusbar) {
        updateBarsColor(ContextCompat.getColor(window.getContext(), colorID), window, applyDarken,
                navbar, statusbar);
    }

    /**
     * Set status bar color
     * @param window Window
     * @param colorID Color resource
     * @param applyDarken Use darker color
     */
    public static void updateBarsColor(Window window, @ColorRes int colorID, boolean applyDarken) {
        updateBarsColor(ContextCompat.getColor(window.getContext(), colorID), window);
    }

    /**
     * Set status bar color without use of darker color
     * @param window Window
     * @param colorID Color resource
     */
    public static void updateBarsColor(Window window, @ColorRes int colorID) {
        updateBarsColor(window, colorID, false);
    }

    /**
     * LOLLIPOP ONLY
     *
     * Apply the color of one window to another
     * @param src Source window
     * @param dest Window to tint
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void updateBarsColor(Window src, Window dest) {
        updateBarsColor(src.getStatusBarColor(), dest, false);
    }

    /**
     * Get a darker color
     * @param color Color
     * @return Darker color
     */
    public static int getDarkerColor(int color) {
        float[] hsvc = new float[3];
        Color.colorToHSV(color, hsvc);
        hsvc[2] *= 0.78;
        return Color.HSVToColor(hsvc);
    }

    /**
     * Reset the color to default (black)
     * @param window Window
     */
    public static void resetBarsColor(Window window) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(window.getContext(), R.color.black));
            window.setNavigationBarColor(ContextCompat.getColor(window.getContext(), R.color.black));
        }
    }

}
