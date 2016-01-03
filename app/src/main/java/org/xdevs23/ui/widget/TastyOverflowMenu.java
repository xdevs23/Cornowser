package org.xdevs23.ui.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import org.xdevs23.android.app.XquidCompatActivity;

import io.xdevs23.cornowser.browser.R;

public class TastyOverflowMenu extends ImageButton {

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
        int ah = (int)XquidCompatActivity.dp2px(context, 32);
        RelativeLayout.LayoutParams params =
                new RelativeLayout.LayoutParams(
                        ah,
                        ah);

        this.setLayoutParams(params);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_action_overflow_default);

        // Taken from UBP
        this.setImageBitmap(Bitmap.createScaledBitmap(bitmap, ah, ah, false));

        this.setColorFilter(ContextCompat.getColor(context, R.color.black),
                PorterDuff.Mode.MULTIPLY);
        this.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.full_transparent));
        this.setMaxHeight(ah);
        this.setMaxWidth (ah);
    }
}
