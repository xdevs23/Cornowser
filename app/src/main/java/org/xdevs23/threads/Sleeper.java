package org.xdevs23.threads;

public class Sleeper {

    public static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch(InterruptedException e) { /* */ }
    }

    public static void sleep(int millis, int nanos) {
        try {
            Thread.sleep(millis, nanos);
        } catch(InterruptedException e) { /* */ }
    }

}
