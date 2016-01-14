package org.xdevs23.rey.material.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import io.xdevs23.cornowser.browser.CornBrowser;

public class ProgressView extends com.rey.material.widget.ProgressView {


    public ProgressView(Context context) {
        super(context);
    }

    public ProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ProgressView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void setProgress(float percentage) {
        if(this.getVisibility() == View.INVISIBLE)
            this.setVisibility(View.VISIBLE);
        super.setProgress(percentage);
    }
}
