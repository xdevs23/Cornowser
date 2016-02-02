package org.xdevs23.debugutils;

import android.util.Log;

import org.xdevs23.config.AppConfig;
import org.xdevs23.config.ConfigUtils;

public class Logging {

    public static void logt(String msg) {
        Log.d(AppConfig.debugTag, msg);
    }

    public static void logt(int i) {
        logt(String.valueOf(i));
    }

    public static void logt(boolean c) {
        logt(String.valueOf(c));
    }

    public static void logt(long l) {
        logt(String.valueOf(l));
    }

    public static void logt(short s) {
        logt(String.valueOf(s));
    }

    public static void logt(char[] chars) {
        logt(String.copyValueOf(chars));
    }

    public static void logt(Object obj) {
        logt(String.valueOf(obj));
    }

    public static void logd(String msg) {
        if(ConfigUtils.isDebuggable())logt(msg);
    }

    public static void logd(Object obj) {
        if(ConfigUtils.isDebuggable())logt(obj);
    }

}
