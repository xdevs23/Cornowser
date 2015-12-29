package org.xdevs23.ui.widget;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import io.xdevs23.cornowser.browser.R;

public class ProgressBarView extends RelativeLayout {

    private RelativeLayout innerProgressBar;

    public void init(Context context) {
        innerProgressBar = new RelativeLayout(context);

        this.setBackgroundColor(ContextCompat.getColor(context, R.color.blue_600_t));

        LayoutParams p = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        p.setMargins(0, 0, 0, 0);

        innerProgressBar.setLayoutParams(p);

        innerProgressBar.setBackgroundColor(ContextCompat.getColor(context, R.color.blue_600));

        this.addView(innerProgressBar);

        this.setVisibility(INVISIBLE);
    }

    public ProgressBarView(Context context) {
        super(context);
        init(context);
    }

    public ProgressBarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ProgressBarView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    public void setProgress(int progress) {
        setProgress((float) progress / 100);
    }

    public void setProgress(float progress) {
        float nProgress = progress;
        if(progress > 1) nProgress /= 100;
        final float fProgress = nProgress;
        final ProgressBarView thisView = this;
        innerProgressBar.animate()
                .setDuration(80)
                .translationX(-(this.getWidth() - (this.getWidth() * nProgress)))
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        if (thisView.getVisibility() == INVISIBLE) {
                            thisView.setVisibility(VISIBLE);
                            thisView.animate().setDuration(80).alpha(1);
                        }
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        // Not needed
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        // Not needed
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                        // Not needed
                    }
                }).start();
    }

    public void endProgress() {
        final ProgressBarView thisView = this;
        innerProgressBar.animate()
                .setDuration(120)
                .translationX(0)
                .alpha(0)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        // Not needed
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        thisView.setVisibility(INVISIBLE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        // Not needed
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                        // Not needed
                    }
                }).start();
    }
}
