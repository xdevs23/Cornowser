package org.xdevs23.android.view;


import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

public abstract class LPView extends View {

    public LPView(Context context) {
        super(context);
        init();
    }

    public LPView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LPView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @SuppressWarnings("unused")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LPView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public abstract void init();

}
