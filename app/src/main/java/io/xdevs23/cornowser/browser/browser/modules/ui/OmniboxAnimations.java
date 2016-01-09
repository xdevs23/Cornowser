package io.xdevs23.cornowser.browser.browser.modules.ui;

import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import org.xdevs23.debugutils.Logging;

import io.xdevs23.cornowser.browser.CornBrowser;

public class OmniboxAnimations {

    public static boolean isBottom = false;

    public static final int
            DEFAULT_ANIMATION_DURATION = 420
            ;

    public static void moveOmni(int posY) {
        float mov = (float) (isBottom ? CornBrowser.omnibox.getHeight() - posY : posY);
        CornBrowser.omnibox.setTranslationY(mov);
        CornBrowser.publicWebRenderLayout.setTranslationY(mov);
        ((RelativeLayout.LayoutParams) CornBrowser.publicWebRenderLayout.getLayoutParams())
                .setMargins(0, (isBottom ? posY : 0), 0, (isBottom ? 0 : posY));
        ((RelativeLayout.LayoutParams) CornBrowser.publicWebRenderLayout.getLayoutParams())
                .height = CornBrowser.publicWebRenderLayout.getHeight() + posY;
    }

    public static void animateOmni(int posY) {
        float mov = (float) (isBottom ? CornBrowser.omnibox.getHeight() - posY : posY);
        CornBrowser.omnibox.animate().translationY(mov);
        CornBrowser.publicWebRenderLayout.animate()
                .setDuration(DEFAULT_ANIMATION_DURATION)
                .translationY(mov);
        ((RelativeLayout.LayoutParams) CornBrowser.publicWebRenderLayout.getLayoutParams())
                .setMargins(0, (isBottom ? posY : 0), 0, (isBottom ? 0 : posY));
        ((RelativeLayout.LayoutParams) CornBrowser.publicWebRenderLayout.getLayoutParams())
                .height = CornBrowser.publicWebRenderLayout.getHeight() + posY;
    }

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
                    cy = (int)motionEvent.getRawY();
                    break;
                case MotionEvent.ACTION_UP:
                    if(-opos > oh / 2) animateOmni(-oh);
                    else animateOmni(0);
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
