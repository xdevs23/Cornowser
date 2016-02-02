package io.xdevs23.cornowser.browser.browser.modules.ui;

import io.xdevs23.cornowser.browser.CornBrowser;

public class OmniboxControl {

    public static boolean isBottom() {
        return CornBrowser.getBrowserStorage().getOmniboxPosition();
    }

    public static boolean isTop() {
        return !isBottom();
    }

    
    public static int getOmniboxHeight() {
        return CornBrowser.omnibox.getHeight();
    }

    public static int getOmniboxPositionInt() {
        return (isBottom() ? 1 : 0);
    }

}
