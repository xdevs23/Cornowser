package org.xdevs23.animation;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.View;

import org.xdevs23.general.ExtendedAndroidClass;
import org.xdevs23.threads.Sleeper;

public class ColorFader extends ExtendedAndroidClass {

    public int startColor, endColor;
    public float speed;
    public View animatingView;

    private Handler handler;

    private boolean isAnimating = false;


    public ColorFader(int startColor, int endColor,
                      Context context, View view,
                      float speed,
                      Handler handler) {
        super(context);
        this.handler    = handler;
        this.startColor = startColor;
        this.endColor   = endColor;
        this.speed      = speed;
        animatingView   = view;
    }

    private Runnable setColor(final int r, final int g, final int b) {
        return new Runnable() {
            @Override
            public void run() {
                animatingView.setBackgroundColor(
                        Color.argb(255, r, g, b)
                );
            }
        };
    }

    public void animate() {
        if(!isAnimating) {
            Runnable coloringRunnable = new Runnable() {
                @Override
                public void run() {
                    isAnimating = true;
                    int
                            r, g, b,

                            or, og, ob,

                            er, eg, eb;

                    or = Color.red(startColor);
                    og = Color.green(startColor);
                    ob = Color.blue(startColor);

                    er = Color.red(endColor);
                    eg = Color.green(endColor);
                    eb = Color.blue(endColor);

                    r = or;
                    g = og;
                    b = ob;

                    while (r != er || g != eg || b != eb) {
                        if (r != er) {
                            if (or < er) r++;
                            else r--;
                        }
                        if (g != eg) {
                            if (og < eg) g++;
                            else g--;
                        }
                        if (b != eb) {
                            if (ob < eb) b++;
                            else b--;
                        }
                        handler.post(setColor(r, g, b));
                        if (speed <= 1.0f)
                            Sleeper.sleep((int) (100 - (speed * 100) + (speed != 1f ? 0 : 1)));
                        else if (speed < 10.0f)
                            Sleeper.sleep(0, (int) (1_000_000 - (speed * 100_000)));
                        else Sleeper.sleep(0, 1);
                    }
                    handler.post(setColor(er, eg, eb));
                    isAnimating = false;
                }
            };
            Thread coloringThread = new Thread(coloringRunnable, "XD_" + this.getClass().getSimpleName());
            coloringThread.start();
        }
    }

    public static ColorFader createAnimation(int startColor, int endColor,
                                             Context context, View view,
                                             float speed,
                                             Handler handler) {
        return new ColorFader(startColor, endColor, context, view, speed, handler);
    }

    public static ColorFader createAnimation(Drawable startColor, int endColor,
                                             Context context, View view,
                                             float speed,
                                             Handler handler) {
        ColorDrawable colorDrawable = (ColorDrawable) startColor;
        return new ColorFader(colorDrawable.getColor(), endColor, context, view, speed, handler);
    }

    public static void animate(int startColor, int endColor,
                               Context context, View view,
                               float speed,
                               Handler handler) {
        createAnimation(startColor, endColor, context, view, speed, handler).animate();
    }

}
