package org.xdevs23.debugutils;


public class DebugTestException extends Exception {

    public static void printActualStack() {
        try {
            throw new DebugTestException();
        } catch(DebugTestException e) { StackTraceParser.logStackTrace(e); }
    }

}
