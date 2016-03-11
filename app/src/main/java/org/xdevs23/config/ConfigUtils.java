package org.xdevs23.config;

import android.content.Context;

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
        if(forceDebug) AppConfig.dbgVer = AppConfig.debug;
        return ( AppConfig.dbgVer.equals(AppConfig.debug) );
    }

}
