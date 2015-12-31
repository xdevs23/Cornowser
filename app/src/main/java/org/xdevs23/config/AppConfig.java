package org.xdevs23.config;

import android.os.Environment;

@SuppressWarnings("unused")
public final class AppConfig {
	
	private static final char dot = '.';
	
	public final
		static String
			    appName = "Cornowser",
			versionName = String.valueOf(Version.major)    + dot +
						  String.valueOf(Version.minor)    + dot +
						  String.valueOf(Version.build)    + dot +
						  String.valueOf(Version.revision),
			 mainDevUrl = "http://xdevs23.bplaced.com",
			 updateRoot = mainDevUrl   + "update/",
			 myDataRoot = Environment.getDataDirectory() + "data/io.xdevs23.cornowser.browser/",
			 debugTag   = appName,
			 dbgVer     = "nightly"
	;
	
	public static class Version {
		
		public enum VersionDifferences {
			major,
			minor,
			build,
		    revision,
			empty
		}
		
		public static int
				major     =  1   ,
				minor     =  0   ,
				build     =  0   ,
		    	revision  =  0
		;
		
		public static boolean
				useRev = false;
		
	}

}
