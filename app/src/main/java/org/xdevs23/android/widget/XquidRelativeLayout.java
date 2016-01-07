package org.xdevs23.android.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class XquidRelativeLayout extends RelativeLayout {

    private AttributeSet attrs  = null;
    private int defStyleAttr    = 0;
    private int defStyleRes     = 0;

    public XquidRelativeLayout(Context context) {
        super(context);
    }

    public XquidRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.attrs = attrs;
    }

    public XquidRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.attrs = attrs;
        this.defStyleAttr = defStyleAttr;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public XquidRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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
}
