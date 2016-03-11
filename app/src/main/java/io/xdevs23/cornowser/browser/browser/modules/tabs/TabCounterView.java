package io.xdevs23.cornowser.browser.browser.modules.tabs;


import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

public class TabCounterView extends View {

    public int ix = 0;


    public TabCounterView(Context context) {
        super(context);
    }

    public TabCounterView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TabCounterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TabCounterView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setTabIndex(int index) {
        ix = index;
    }

    public int getTabIndex() {
        return ix;
    }

}
