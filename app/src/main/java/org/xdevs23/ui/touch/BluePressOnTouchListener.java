package org.xdevs23.ui.touch;

import android.support.annotation.ColorRes;

import io.xdevs23.cornowser.browser.R;
import io.xdevs23.cornowser.browser.browser.modules.ColorUtil;

public class BluePressOnTouchListener extends PressHoverTouchListener {

    public BluePressOnTouchListener(int upColor) {
        super(upColor);
        setDownColor(ColorUtil.getColor(R.color.blue_300));
    }

    private BluePressOnTouchListener(int upColor, int downColor) {
        super(upColor, downColor);
    }

    private BluePressOnTouchListener(@ColorRes int upColor, @ColorRes int downColor, boolean isRes) {
        super(upColor, downColor, isRes);
    }

    public BluePressOnTouchListener(@ColorRes int upColor, boolean isRes) {
        this(isRes ? ColorUtil.getColor(upColor) : upColor);
    }

}
