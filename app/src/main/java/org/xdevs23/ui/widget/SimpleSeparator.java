package org.xdevs23.ui.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import org.xdevs23.ui.utils.DpUtil;

import io.xdevs23.cornowser.browser.R;

public class SimpleSeparator extends View {

    private Context myContext;

    public enum SeparatorTheme {
        LIGHT,
        DARK,
        BLUE
    }

    public SimpleSeparator(Context context) {
        super(context);
        init(context);
    }

    public SimpleSeparator(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SimpleSeparator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SimpleSeparator(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    protected void init(Context context) {
        myContext = context;
        ViewGroup.LayoutParams p = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                DpUtil.dp2px(context, 1)
        );

        setLayoutParams(p);

        setSeparatorTheme(SeparatorTheme.DARK);
    }

    /**
     * Set color of separator
     * @param color Color
     * @return This
     */
    public SimpleSeparator setSeparatorColor(int color) {
        setBackgroundColor(color);
        return this;
    }

    /**
     * Set color of separator
     * @param colorRes Color (Resource)
     * @return This
     */
    public SimpleSeparator setSeparatorColorRes(@ColorRes int colorRes) {
        setBackgroundColor(ContextCompat.getColor(myContext, colorRes));
        return this;
    }

    public SimpleSeparator setSeparatorTheme(SeparatorTheme theme) {
        switch(theme) {
            case DARK : setSeparatorColorRes(R.color. dark_semi_more_transparent);  break;
            case LIGHT: setSeparatorColorRes(R.color.white_semi_more_transparent);  break;
            case BLUE : setSeparatorColorRes(R.color.blue_600);                     break;
            default   : break;
        }

        return this;
    }

    /**
     * Set height of separator
     * @param dpHeight Height in density pixels
     * @return This
     */
    public SimpleSeparator setSeparatorHeight(int dpHeight) {
        ViewGroup.LayoutParams p = getLayoutParams();

        p.height = DpUtil.dp2px(myContext, dpHeight);

        setLayoutParams(p);

        return this;
    }

    /**
     * Set width of separator
     * @param dpWidth Width in density pixels
     * @return This
     */
    public SimpleSeparator setSeparatorWidth(int dpWidth) {
        ViewGroup.LayoutParams p = getLayoutParams();

        p.width = DpUtil.dp2px(myContext, dpWidth);

        setLayoutParams(p);

        return this;
    }

    /**
     * Set whether the separator width matches the parent width or not
     * @param matches If true, the separator matches width of parent
     * @return This
     */
    public SimpleSeparator setMatchesParentWidth(boolean matches) {
        ViewGroup.LayoutParams p = getLayoutParams();

        if(matches) p.width = ViewGroup.LayoutParams.MATCH_PARENT;

        setLayoutParams(p);

        return this;
    }

}
