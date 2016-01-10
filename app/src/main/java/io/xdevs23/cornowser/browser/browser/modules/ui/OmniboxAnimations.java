package io.xdevs23.cornowser.browser.browser.modules.ui;

import android.view.MotionEvent;
import android.view.View;

import io.xdevs23.cornowser.browser.CornBrowser;

public class OmniboxAnimations {

    public static final int
            DEFAULT_ANIMATION_DURATION = 420
            ;

    private static boolean isBottom() {
        return CornBrowser.getBrowserStorage().getOmniboxPosition();
    }

    public static void moveOmni(int posY) {
        if(isBottom()) return;
        float mov = (float) (posY);
        CornBrowser.omnibox.setTranslationY(mov);
        CornBrowser.publicWebRenderLayout
                .setTranslationY(mov + CornBrowser.omnibox.getHeight());
    }

    public static void animateOmni(int posY) {
        float mov = (float) posY;
        CornBrowser.omnibox.animate().translationY(mov - (isBottom() ? CornBrowser.omnibox.getHeight() : 0));
        CornBrowser.publicWebRenderLayout.animate()
                .setDuration(DEFAULT_ANIMATION_DURATION)
                .translationY( (mov + (isBottom() ? 0 : CornBrowser.omnibox.getHeight())) );
    }

    // Main listener for controlling omnibox show/hide animations

    public static final View.OnTouchListener
            mainOnTouchListener = new View.OnTouchListener() {
        int
                oh = 0,
                cy = 0,
                ny = 0,
                opos = 0;

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch(motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    oh = CornBrowser.omnibox.getHeight();
                    if(opos > -oh)cy = (int)motionEvent.getRawY();
                    else cy = (int)motionEvent.getRawY() - opos;
                    break;
                case MotionEvent.ACTION_UP:
                    if(-opos > oh / 2) animateOmni(isBottom() ? -oh : 0);
                    else animateOmni(isBottom() ? 0 : -oh);
                    break;
                case MotionEvent.ACTION_MOVE:
                    ny = ((int)motionEvent.getRawY()) - cy;
                    opos = ny;
                    if(opos > 0  ) opos = 0;
                    if(opos < -oh) opos = -oh;

                    moveOmni(opos);
                    break;
                default: break;
            }
            return false;
        }

    };

}
