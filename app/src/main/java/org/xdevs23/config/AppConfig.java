package org.xdevs23.config;

import android.os.Environment;

import io.xdevs23.cornowser.browser.updater.UpdaterStorage;

@SuppressWarnings("unused")
public final class AppConfig {
	
	private static final char dot = '.';
	
	public static final String
			    appName = "Cornowser",
			versionName = String.valueOf(Version.major)    + dot +
						  String.valueOf(Version.minor)    + dot +
						  String.valueOf(Version.build)    + dot +
						  String.valueOf(Version.revision),
			 mainDevUrl = "http://xdevs23.bplaced.com/",
			 updateRoot = UpdaterStorage.URL_RAW_CB_GITHUB_REPO  + "update/",
			 debugTag   = appName,        // Tag used in logcat
			 dbgVer     = "alpharelease"  // Set this to 'debug' to enable debug mode
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
				build     =  3   ,
		    	revision  =  1
		;
		
		public static final boolean
				useRev = true;
		
	}

}
