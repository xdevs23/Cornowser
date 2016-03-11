package io.xdevs23.cornowser.browser.browser.modules.adblock;

import java.util.ArrayList;

public final class AdBlockParser {

    private AdBlockParser() {

    }

    public static String[] parseRawAdBlockList(String[] list) {
        ArrayList<String> filteredAdList = new ArrayList<String>();

        for ( String s : list ) {
            if(! ( s.startsWith("#")
                || s.startsWith(" ")
                || s.length() < 3
                || s.contains(" localhost")
            )) filteredAdList.add(s.replace("\t", " ").replace("\r", "")
                    .replace("127.0.0.1 ", "").replace("0.0.0.0 ", ""));
        }

        String[] filteredList = filteredAdList.toArray(new String[0]);
        filteredAdList.clear();
        return filteredList;
    }

    public static boolean isHostListed(String host, String[] list) {
        for ( String s : list ) if(host.contains(s)) return true;
        return false;
    }

}
