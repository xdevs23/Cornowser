package io.xdevs23.cornowser.browser.browser.modules.ui;

import android.animation.Animator;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewPropertyAnimator;

import org.xdevs23.debugutils.Logging;

import io.xdevs23.cornowser.browser.CornBrowser;

public class OmniboxAnimations {

    public static final int
            DEFAULT_ANIMATION_DURATION = 420
            ;

    private static Handler handler = new Handler();
    private static Runnable longPressRunnable = new Runnable() {
        @Override
        public void run() {
            Logging.logd("Long press detected");
        }
    };


    public static boolean isBottom() {
        return OmniboxControl.isBottom();
    }

    public static boolean isTop() {
        return !isBottom();
    }

    public static int getOmniboxPositionInt() {
        return OmniboxControl.getOmniboxPositionInt();
    }

    protected static int getOmniHeight() {
        return OmniboxControl.getOmniboxHeight();
    }

    protected static ViewPropertyAnimator omniboxAnimate() {
        return CornBrowser.omnibox.animate().setDuration(DEFAULT_ANIMATION_DURATION);
    }

    protected static ViewPropertyAnimator webAnimate() {
        return CornBrowser.publicWebRenderLayout.animate().setDuration(DEFAULT_ANIMATION_DURATION);
    }

    public static void moveOmni(int posY) {
        if(isBottom()) return;
        float mov = (float) posY;
        CornBrowser.omnibox.bringToFront();
        CornBrowser.omnibox.setTranslationY(mov);
        CornBrowser.publicWebRenderLayout
                .setTranslationY(mov + CornBrowser.omnibox.getHeight());
    }

    public static void animateOmni(int posY) {
        float mov = (float) posY;
        if(mov > 0) CornBrowser.omnibox.bringToFront();
        omniboxAnimate().translationY(mov + (isBottom() ? CornBrowser.omnibox.getHeight() : 0))
                // This listener is to fix glitches
                .setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                CornBrowser.omnibox.bringToFront();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                CornBrowser.omnibox.bringToFront();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                // Not necessary
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                // Not necessary
            }
        });
        if(isTop())
            CornBrowser.publicWebRenderLayout
                .setTranslationY((mov + CornBrowser.omnibox.getHeight()));
    }

    public static void resetOmni() {
        CornBrowser.omnibox.bringToFront();
        omniboxAnimate().translationY(0);
        if(isTop()) CornBrowser.publicWebRenderLayout.setTranslationY(getOmniHeight());
        else        CornBrowser.publicWebRenderLayout.setTranslationY(0);
    }

    // Main listener for controlling omnibox show/hide animations

    public static final View.OnTouchListener
            mainOnTouchListener = new View.OnTouchListener() {
        int
                oh   = 0,
                cy   = 0,
                ny   = 0,
                opos = 0;

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch(motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    handler.postDelayed(longPressRunnable, 1200);
                    CornBrowser.getTabSwitcher().hideSwitcher();
                    oh = CornBrowser.omnibox.getHeight();
                    if(-opos <= oh / 2) cy = (int)motionEvent.getRawY();
                    else cy = (int)motionEvent.getRawY() - opos;
                    break;
                case MotionEvent.ACTION_UP:
                    handler.removeCallbacks(longPressRunnable);
                    if(-opos > oh / 2) {
                        animateOmni( (isBottom() ? 0 : -oh) );
                        opos =       (isBottom() ? 0 : -oh)  ;
                    } else {
                        animateOmni( (isBottom() ? -oh : 0) );
                        opos =       (isBottom() ? -oh : 0)  ;
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    ny = ((int)motionEvent.getRawY()) - cy;
                    opos = ny;
                    if(opos > 0  ) opos = 0;
                    if(opos < -oh) opos = -oh;

                    moveOmni(opos);
                    handler.removeCallbacks(longPressRunnable);
                    break;
                default: break;
            }
            return false;
        }

    };

}
