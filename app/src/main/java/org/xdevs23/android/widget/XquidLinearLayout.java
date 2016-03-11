package org.xdevs23.android.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import org.xdevs23.ui.utils.DpUtil;

public class XquidLinearLayout extends LinearLayout {

    private AttributeSet attrs;
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


    public static class LayoutParams extends LinearLayout.LayoutParams {

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int w, int h) {
            super(w, h);
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }

        @TargetApi(Build.VERSION_CODES.KITKAT)
        public LayoutParams(LinearLayout.LayoutParams source) {
            super(source);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        public void setMarginsDp(int left, int top, int right, int bottom, Context context) {
            this.setMargins(
                    DpUtil.dp2px(context, left),
                    DpUtil.dp2px(context, top),
                    DpUtil.dp2px(context, right),
                    DpUtil.dp2px(context, bottom)
            );
        }
    }

}
