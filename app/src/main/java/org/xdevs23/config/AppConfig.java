package org.xdevs23.config;

import io.xdevs23.cornowser.browser.updater.UpdaterStorage;

@SuppressWarnings("unused")
public final class AppConfig {

    protected static final String
            debug   = "debug",
            alpha   = "alpharelease",
            beta    = "betarelease",
            rel     = "release",
            nightly = "nightly";
	
	public static final String
			    appName = "Cornowser",
			 mainDevUrl = "http://xdevs23.bplaced.com/",
			 updateRoot = UpdaterStorage.URL_RAW_CB_GITHUB_REPO  + "update/",
			 debugTag   = appName        // Tag used in logcat
	;

    public static String dbgVer = beta; // Change to debug to use debug mode

}
