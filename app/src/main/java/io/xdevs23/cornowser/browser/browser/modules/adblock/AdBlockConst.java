package io.xdevs23.cornowser.browser.browser.modules.adblock;


public final class AdBlockConst {

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
            "html5test.com"
    };

}
