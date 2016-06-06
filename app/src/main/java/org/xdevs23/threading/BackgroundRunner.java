package org.xdevs23.threading;

import android.os.Handler;

import org.xdevs23.debugutils.StackTraceParser;

/**
 * A class used to run something in background
 */
public class BackgroundRunner {

    private BackgroundRunner() {

    }

    public synchronized static void runInBackground(final Runnable runnable, final Handler handler) {
        Runnable wrap = new Runnable() {
            @Override
            public void run() {
                try {
                    runnable.run();
                } catch(Exception ex) {
                    StackTraceParser.logStackTrace(ex);
                }
            }
        };
        Thread thread = new Thread(wrap, "xd" + BackgroundRunner.class.getSimpleName());
        thread.start();
    }

}
