package io.xdevs23.cornowser.browser.browser.modules;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.widget.RelativeLayout;

import org.michaelevans.colorart.library.ColorArt;
import org.xdevs23.android.content.res.AssetHelper;
import org.xdevs23.ui.utils.BarColors;

import io.xdevs23.cornowser.browser.CornBrowser;
import io.xdevs23.cornowser.browser.browser.xwalk.CrunchyWalkView;

public class WebThemeHelper {

    private static int currentColor = 0;

    public static void setWebThemeColor(String color, RelativeLayout omnibox, Window window) {
        if(!color.contains("#")) return;
        setWebThemeColor(Color.parseColor(color), omnibox, window);
    }

    public static void setWebThemeColor(int color, RelativeLayout omnibox, Window window) {
        if(currentColor == 0) currentColor = ((ColorDrawable)omnibox.getBackground()).getColor();
        omnibox.setBackgroundColor(color);
        BarColors.updateBarsColor(color, window);
    }

    public static void resetWebThemeColor(RelativeLayout omnibox) {
        if(currentColor != 0) omnibox.setBackgroundColor(currentColor);
        CornBrowser.resetBarColor();
    }

    public static void resetWebThemeColorAlt(RelativeLayout omnibox, Window window) {
        Bitmap b = CornBrowser.getWebEngine().getUIClient().getCurrentFavicon();
        if(b != null) {
            ColorArt c = new ColorArt(b);
            setWebThemeColor(c.getBackgroundColor(), omnibox, window);
        } else resetWebThemeColor(omnibox);
    }

    public static void tintNow(CrunchyWalkView view) {
        if(!view.getUrl().toLowerCase().contains("cornhandler://"))
            CornHandler.sendRawJSRequest(view, AssetHelper.getAssetString("appScripts/webThemeColorUtility.js",
                    view.getContext()));
    }

}
