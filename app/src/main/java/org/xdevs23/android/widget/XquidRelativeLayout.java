package org.xdevs23.android.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import org.xdevs23.ui.utils.DpUtil;

public class XquidRelativeLayout extends RelativeLayout {

    private AttributeSet attrs;
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

    public static void addRuleLP(int rule, RelativeLayout.LayoutParams... params) {
        LayoutParams.addRule(rule, params);
    }

    public static class LayoutParams extends RelativeLayout.LayoutParams {

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
        public LayoutParams(RelativeLayout.LayoutParams source) {
            super(source);
        }

        public static RelativeLayout.LayoutParams getPredefinedLPFromViewMetrics(View view) {
            return new RelativeLayout.LayoutParams
                    (view.getLayoutParams().width, view.getLayoutParams().height);
        }

        public static void addRule(int rule, RelativeLayout.LayoutParams... params) {
            for(RelativeLayout.LayoutParams p : params)
                p.addRule(rule);
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
