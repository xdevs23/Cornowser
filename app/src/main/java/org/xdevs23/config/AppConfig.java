package org.xdevs23.config;

import io.xdevs23.cornowser.browser.updater.UpdaterStorage;

@SuppressWarnings("unused")
public final class AppConfig {
	
	private static final char dot = '.';

    private static final String
            debug   = "debug",
            alpha   = "alpharelease",
            beta    = "betarelease",
            rel     = "release",
            nightly = "nightly";
	
	public static final String
			    appName = "Cornowser",
			versionName = String.valueOf(Version.major)    + dot +
						  String.valueOf(Version.minor)    + dot +
						  String.valueOf(Version.build)    + dot +
						  String.valueOf(Version.revision),
			 mainDevUrl = "http://xdevs23.bplaced.com/",
			 updateRoot = UpdaterStorage.URL_RAW_CB_GITHUB_REPO  + "update/",
			 debugTag   = appName,        // Tag used in logcat
			 dbgVer     = debug // Set this to 'debug' to enable debug mode
	;
	
	public static final class Version {
		
		public enum VersionDifferences {
			major,
			minor,
			build,
		    revision,
			empty
		}

		public static final int
				major     =  1   ,
				minor     =  0   ,
				build     =  4   ,
		    	revision  =  1
		;
		
		public static final boolean
				useRev = true;
		
	}

}
