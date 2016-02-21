package io.xdevs23.cornowser.browser.browser.modules.tabs;


import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.xdevs23.ui.touch.PressHoverTouchListener;

import io.xdevs23.cornowser.browser.CornBrowser;
import io.xdevs23.cornowser.browser.R;
import io.xdevs23.cornowser.browser.browser.modules.ColorUtil;

public class TabSwitcherOpenButton extends RelativeLayout {

    private String tabCount = "0";
    private Context myContext;

    public TabSwitcherOpenButton(Context context) {
        super(context);
        init(context);
    }

    public TabSwitcherOpenButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TabSwitcherOpenButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TabSwitcherOpenButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    public void init(Context context) {
        if (myContext == null) myContext = context;

        if(CornBrowser.getContext() != null) {
            setGravity(Gravity.CENTER);

            ImageView img = new ImageView(getContext());
            img.setImageResource(R.drawable.omnibox_tabswitch_icon);
            addView(img);

            TextView t = new TextView(getContext());
            t.setTextSize(14f);
            t.setText(tabCount);
            t.setGravity(Gravity.CENTER);
            t.setMinimumWidth(getWidth());
            t.setTextColor(Color.BLACK);
            addView(t);

            t.bringToFront();

            setOnTouchListener(new PressHoverTouchListener(Color.TRANSPARENT,
                    ColorUtil.getColor(R.color.dark_semi_transparent)));
        }
    }

    public void setTabCount(int count) {
        tabCount = String.valueOf(count);
        TextView t = ((TextView)getChildAt(1));
        t.setText(tabCount);
        t.setTranslationX( Math.round( (getWidth() / 2) - (t.getWidth() / 2) ) );
    }

    public void setIconColor(@ColorRes int color) {
        ((ImageView)getChildAt(0)).setColorFilter(ColorUtil.getColor(color),
                PorterDuff.Mode.MULTIPLY);
    }

}
