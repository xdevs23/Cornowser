package org.xdevs23.config;

import android.content.Context;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConfigUtils {

    public static boolean forceDebug = false;
	
	public static String getVersionName(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0
            ).versionName;
        } catch(Exception ex) {
            return "";
        }
	}

    public static boolean isDebuggable() {
        String debuggers = ".*(debug|dbg|rc|pre|alpha|beta).*";
        return (forceDebug
                || ( ( !AppConfig.dbgVer.contains("release")) &&
                 ((Matcher)( Pattern.compile(debuggers).matcher(AppConfig.dbgVer) )) .matches() ) );
    }

}
