package io.xdevs23.cornowser.browser.browser.modules;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.annotation.ColorRes;
import android.view.Window;
import android.widget.RelativeLayout;

import org.michaelevans.colorart.library.ColorArt;
import org.xdevs23.android.content.res.AssetHelper;
import org.xdevs23.animation.ColorFader;
import org.xdevs23.debugutils.Logging;
import org.xdevs23.ui.utils.BarColors;

import io.xdevs23.cornowser.browser.CornBrowser;
import io.xdevs23.cornowser.browser.R;
import io.xdevs23.cornowser.browser.browser.modules.ui.OmniboxControl;
import io.xdevs23.cornowser.browser.browser.xwalk.CrunchyWalkView;

public class WebThemeHelper {

    private static int currentColor = 0;

    private enum AllowedWordColors {
        black,
        white,
        red,
        green,
        blue,
        cyan,
        magenta,
        yellow,
        teal,
        bluegrey,
        bluegray
    }


    private static int rCol(@ColorRes int colRes) {
        return ColorUtil.getColor(colRes);
    }

    public static void setWebThemeColor(String color, RelativeLayout omnibox, Window window) {
        if(!CornBrowser.getBrowserStorage().getOmniColoringEnabled()) return;
        if(!color.contains("#")) {
            boolean isMatched = false;
            for ( AllowedWordColors c : AllowedWordColors.values() )
                if(color.equalsIgnoreCase(c.name().toLowerCase()))
                    isMatched = true;
            if(isMatched) {
                int col = 0;
                switch(AllowedWordColors.valueOf(color)) {
                    case black:     col = Color.BLACK;              break;
                    case white:     col = Color.WHITE;              break;
                    case red:       col = rCol(R.color.red_700);    break;
                    case green:     col = rCol(R.color.green_700);  break;
                    case blue:      col = rCol(R.color.blue_700);   break;
                    case cyan:      col = rCol(R.color.cyan_700);   break;
                    case magenta:   col = rCol(R.color.pink_600);   break;
                    case yellow:    col = rCol(R.color.yellow_800); break;
                    case bluegray:
                    case bluegrey:  col = rCol(R.color.blue_grey_700);  break;
                    case teal:      col = rCol(R.color.teal_700);       break;
                    default: break;
                }
                if(col != 0) setWebThemeColor(col, omnibox, window);
                else resetWebThemeColorAlt(omnibox, window);
            }
            return;
        }
        setWebThemeColor(Color.parseColor(color), omnibox, window);
    }

    public static void setWebThemeColor(int color, RelativeLayout omnibox, Window window) {
        if(!CornBrowser.getBrowserStorage().getOmniColoringEnabled()) return;
        if(currentColor == 0) currentColor = ((ColorDrawable)omnibox.getBackground()).getColor();
        ColorFader.createAnimation(
                CornBrowser.omnibox.getBackground(),
                color,
                window.getContext(),
                omnibox,
                1.6f,
                new Handler()
        ).animate();
        if(OmniboxControl.isTop()) BarColors.updateBarsColor(color, window);
        else BarColors.updateBarsColor(color, window, true, true, false);
    }

    public static void resetWebThemeColor(RelativeLayout omnibox) {
        if(currentColor != 0) ColorFader.createAnimation(
                CornBrowser.omnibox.getBackground(),
                currentColor,
                CornBrowser.getContext(),
                omnibox,
                1.6f,
                new Handler()
        ).animate();
        CornBrowser.resetBarColor();
    }

    public static void resetWebThemeColorAlt(RelativeLayout omnibox, Window window) {
        Bitmap b = CornBrowser.getWebEngine().getFavicon();
        if(b != null) {
            Logging.logd("[WebThemeHelper] Using favicon for background color");
            ColorArt c = new ColorArt(b);
            setWebThemeColor(c.getBackgroundColor(), omnibox, window);
        } else resetWebThemeColor(omnibox);
    }

    public static void tintNow() {
        if(!CornBrowser.getBrowserStorage().getOmniColoringEnabled()) return;
        try {
            if (!CornBrowser.getWebEngine().getUrl().toLowerCase().contains("cornhandler://"))
                CornHandler.sendRawJSRequest(CornBrowser.getWebEngine(), AssetHelper.getAssetString("appScripts/webThemeColorUtility.js",
                        CornBrowser.getWebEngine().getContext()));
        } catch(Exception ex) {
            Logging.logd("Warning: Did not succeed while starting tinting. Skipping.");
        }
    }

    // Overload for old calls
    @Deprecated
    public static void tintNow(CrunchyWalkView view) {
        tintNow();
    }

}
