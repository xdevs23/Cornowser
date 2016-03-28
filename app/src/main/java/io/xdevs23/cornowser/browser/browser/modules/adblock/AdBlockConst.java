package io.xdevs23.cornowser.browser.browser.modules.adblock;


public final class AdBlockConst {

    /**
     * These are hosts files
     * They are downloaded and processed to later be used by the ad blocker
     */
    public static final String[] HOST_FILES_ADBLOCK = new String[] {
            "http://adaway.org/hosts.txt",
            "http://winhelp2002.mvps.org/hosts.txt",
            "http://hosts-file.net/ad_servers.txt",
            "https://pgl.yoyo.org/adservers/serverlist.php?hostformat=hosts&showintro=0&mimetype=plaintext"
    };

    /**
     * There are some conflicts with the listed websites below.
     * So in order to keep them working, this whitelist is required.
     * Please add hosts here if a website does not load with AdBlock enabled.
     */
    public static final String[] WHITELISTED_HOSTS = new String[] {
            "html5test.com",
            "crashlytics.com",
            ".bplaced.com",
            "bplaced.net",
    };

    /**
     * Here are some pre-defined ad hosts which can be blocked without a hosts file
     * This increases loading speed hence they are multiple times defined in the hosts files
     */
    public static final String[] PREDEFINED_HOSTS = new String[] {
            // This blocks many ad hosts like doubleclick, mediafarm and more
            "302br.net"
    };

    public static final boolean
            NET_BEHAVIOR_ALL  = true,
            NET_BEHAVIOR_WIFI = false;

}
