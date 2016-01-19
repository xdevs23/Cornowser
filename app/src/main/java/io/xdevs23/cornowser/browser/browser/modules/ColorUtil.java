package io.xdevs23.cornowser.browser.browser.modules;

import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;

import io.xdevs23.cornowser.browser.CornBrowser;

public class ColorUtil {

    private ColorUtil() {}

    public static int getColor(@ColorRes int colres) {
        return ContextCompat.getColor(CornBrowser.getContext(), colres);
    }

}
