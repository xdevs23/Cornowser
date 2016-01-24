package org.xdevs23.ui.touch;

import android.support.annotation.ColorRes;
import android.view.MotionEvent;
import android.view.View;

import io.xdevs23.cornowser.browser.R;
import io.xdevs23.cornowser.browser.browser.modules.ColorUtil;

public class PressHoverTouchListener implements View.OnTouchListener {

    private int upColor;
    private int downColor;

    public PressHoverTouchListener(int upColor, int downColor) {
        this.upColor = upColor;
        this.downColor = downColor;
    }

    public PressHoverTouchListener(int upColor) {
        this.upColor = upColor;
        this.downColor = ColorUtil.getColor(R.color.grey_300);
    }

    // isRes needed for keeping different method signature than BluePressOnTouchListener(int)
    public PressHoverTouchListener(@ColorRes int upColor, @ColorRes int downColor, boolean isRes) {
        this(
                isRes ? ColorUtil.getColor(upColor) : upColor,
                isRes ? ColorUtil.getColor(downColor) : downColor
        );
    }

    public PressHoverTouchListener(@ColorRes int upColor, boolean isRes) {
        this(isRes ? upColor : 0, R.color.grey_300, isRes);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch(event.getAction()) {
            case MotionEvent.ACTION_HOVER_ENTER:
            case MotionEvent.ACTION_DOWN:
                v.setBackgroundColor(downColor);
                break;
            case MotionEvent.ACTION_OUTSIDE:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_HOVER_EXIT:
            case MotionEvent.ACTION_UP:
                v.setBackgroundColor(upColor);
                break;
            default: break;
        }
        return false;
    }

    public void setUpColor(int color) {
        this.upColor = color;
    }

    public void setDownColor(int color) {
        this.downColor = color;
    }

}
