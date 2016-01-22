package org.xdevs23.ui.touch;

import android.support.annotation.ColorRes;
import android.view.MotionEvent;
import android.view.View;

import io.xdevs23.cornowser.browser.R;
import io.xdevs23.cornowser.browser.browser.modules.ColorUtil;

public class BluePressOnTouchListener implements View.OnTouchListener {

    private int upColor;

    public BluePressOnTouchListener(int upcolor) {
        this.upColor = upcolor;
    }

    // isRes needed for keeping different method signature than BluePressOnTouchListener(int)
    public BluePressOnTouchListener(@ColorRes int upcolor, boolean isRes) {
        this(ColorUtil.getColor(upcolor));
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch(event.getAction()) {
            case MotionEvent.ACTION_HOVER_ENTER:
            case MotionEvent.ACTION_DOWN:
                v.setBackgroundColor(ColorUtil.getColor(R.color.blue_300));
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
}
