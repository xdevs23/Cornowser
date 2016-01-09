package org.xdevs23.android.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class XquidLinearLayout extends LinearLayout {

    private AttributeSet attrs  = null;
    private int defStyleAttr    = 0;
    private int defStyleRes     = 0;

    public XquidLinearLayout(Context context) {
        super(context);
    }

    public XquidLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.attrs = attrs;
    }

    public XquidLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.attrs = attrs;
        this.defStyleAttr = defStyleAttr;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public XquidLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.attrs = attrs;
        this.defStyleAttr = defStyleAttr;
        this.defStyleRes = defStyleRes;
    }

    public AttributeSet getAttributeSet() {
        return attrs;
    }

    public int getDefStyleAttribute() {
        return defStyleAttr;
    }

    public int getDefStyleResource() {
        return defStyleRes;
    }

    public void setVerticalOrientation(boolean isVertical) {
        if(isVertical)
            this.setOrientation(LinearLayout.VERTICAL);
        else
            this.setOrientation(LinearLayout.HORIZONTAL);
    }
}
