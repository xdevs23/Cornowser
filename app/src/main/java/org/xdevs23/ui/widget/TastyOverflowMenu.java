package org.xdevs23.ui.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;

import org.xdevs23.android.app.XquidCompatActivity;

import io.xdevs23.cornowser.browser.R;

public class TastyOverflowMenu extends LinearLayout {

    public TastyOverflowMenu(Context context) {
        super(context);
        init(context);
    }

    public TastyOverflowMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TastyOverflowMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TastyOverflowMenu(Context context, AttributeSet attrs, int defStyleAttrs, int defStyleRes) {
        super(context, attrs, defStyleAttrs, defStyleRes);
        init(context);
    }

    public void init(Context context) {
        this.setGravity(Gravity.CENTER_HORIZONTAL);
        LayoutParams params = new LayoutParams((int)XquidCompatActivity.dp2px(context, 40),
                (int)XquidCompatActivity.dp2px(context, 40));

        this.setLayoutParams(params);

        // Taken from UBP
        this.setBackgroundResource(R.drawable.ic_action_overflow_default);

        this.getBackground().setColorFilter(ContextCompat.getColor(context, R.color.black),
                PorterDuff.Mode.MULTIPLY);


    }
}
