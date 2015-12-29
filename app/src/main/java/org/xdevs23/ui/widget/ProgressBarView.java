package org.xdevs23.ui.widget;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import org.xdevs23.debugutils.Logging;

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
        if(progress > 1) progress /= 100;
        final float fProgress = progress;
        final RelativeLayout thisView = this;
        Logging.logd(fProgress);
        innerProgressBar.animate()
                .setDuration(80)
                .translationX(this.getWidth() - (this.getWidth() * progress))
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
                        if (fProgress == 1.0) thisView.animate().setDuration(240).alpha(0)
                                .setListener(new Animator.AnimatorListener() {
                                    @Override
                                    public void onAnimationStart(Animator animation) {

                                    }

                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        thisView.setVisibility(INVISIBLE);
                                    }

                                    @Override
                                    public void onAnimationCancel(Animator animation) {

                                    }

                                    @Override
                                    public void onAnimationRepeat(Animator animation) {

                                    }
                                }).start();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                }).start();
    }
}
