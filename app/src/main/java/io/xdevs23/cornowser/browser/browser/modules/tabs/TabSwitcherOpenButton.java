package io.xdevs23.cornowser.browser.browser.modules.tabs;


import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.xdevs23.ui.touch.PressHoverTouchListener;

import io.xdevs23.cornowser.browser.CornBrowser;
import io.xdevs23.cornowser.browser.R;

public class TabSwitcherOpenButton extends RelativeLayout {

    private Context myContext;
    private int pressedColor;

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
            pressedColor = ContextCompat.getColor(CornBrowser.getContext(), R.color.dark_semi_more_transparent);
            setGravity(Gravity.CENTER);

            ImageView img = new ImageView(getContext());
            img.setImageResource(R.drawable.omnibox_tabswitch_icon);
            addView(img);

            TextView t = new TextView(getContext());
            t.setTextSize(14f);
            t.setGravity(Gravity.CENTER);
            t.setMinimumWidth(getWidth());
            t.setTextColor(Color.BLACK);
            addView(t);

            t.bringToFront();

            setTabCount(0);

            setOnTouchListener(new PressHoverTouchListener(Color.TRANSPARENT,
                    pressedColor));
        }
    }

    public void setTabCount(int count) {
        String tabCount = String.valueOf(count);
        TextView t = ((TextView)getChildAt(1));
        ImageView img = ((ImageView)getChildAt(0));
        t.setText(tabCount);
        t.setTranslationX( Math.round( (img.getWidth() / 2) - (t.getWidth() / 2) ) );
    }

    public void applyLightTheme() {
        ((TextView) getChildAt(1)).setTextColor(Color.WHITE);
        ((ImageView)getChildAt(0)).setImageResource(R.drawable.omnibox_tabswitch_icon_light);
        pressedColor = ContextCompat.getColor(myContext, R.color.white_semi_more_transparent);
        setOnTouchListener(new PressHoverTouchListener(Color.TRANSPARENT, pressedColor));
    }

    public void applyDarkTheme() {
        ((TextView) getChildAt(1)).setTextColor(Color.BLACK);
        ((ImageView)getChildAt(0)).setImageResource(R.drawable.omnibox_tabswitch_icon);
        pressedColor = ContextCompat.getColor(myContext, R.color.dark_semi_more_transparent);
        setOnTouchListener(new PressHoverTouchListener(Color.TRANSPARENT, pressedColor));
    }

    public void applyTheme(boolean theme) {
        if(theme) applyLightTheme();
        else applyDarkTheme();
    }

}
