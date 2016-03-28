package io.xdevs23.cornowser.browser.browser.modules.adblock;

import org.xdevs23.debugutils.Logging;

import java.util.ArrayList;

// Use indexOf instead of contains for better performance
// See http://stackoverflow.com/a/18340277/4479004
@SuppressWarnings("IndexOfReplaceableByContains")
public final class AdBlockParser {

    private AdBlockParser() {

    }

    public static boolean isPredefinedAdHost(String s) {
        for ( String str : AdBlockConst.PREDEFINED_HOSTS ) {
            if(s.indexOf(str) != -1) return true;
        }
        return false;
    }

    public static String[] parseRawAdBlockList(String[] list) {
        long start = System.currentTimeMillis();
        ArrayList<String> filteredAdList = new ArrayList<String>();

        for ( String s : list ) {
            if(! ( s.length() < 3
                    || s.startsWith("#")
                    || s.startsWith(" ")
                    || s.indexOf(" localhost") != -1
                    || isPredefinedAdHost(s)
            )) filteredAdList.add(s.replace("\t", " ").replace("\r", "")
                    .replace("127.0.0.1 ", "").replace("0.0.0.0 ", ""));
        }

        String[] filteredList = filteredAdList.toArray(new String[0]);
        filteredAdList.clear();
        Logging.logd("Parsing raw adblock list took " + (System.currentTimeMillis() - start) + " ms");
        return filteredList;
    }

    public static boolean isHostListed(String host, String[] list) {
        if(host == null || host.isEmpty()
                || list == null || list.length < 1
                || list[0].equals("[none]")) return false;
        for ( String s : list )
            if(host.indexOf(s) != -1) return true;
        return false;
    }

}
