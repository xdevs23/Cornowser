package org.xdevs23.general;

public class StringManipulation {

    public static String arrayToString(String[] array) {
        StringBuilder sb = new StringBuilder();
        for (String anArray : array) sb.append(anArray).append("\n");
        return sb.toString();
    }

    public static String getDomainFromUrl(String u) {
        if(u == null || u.isEmpty()) return "";
        String  uwp = u.replaceAll("[^ ]*(://)", "");
        String pu;
        if(uwp.contains("/")) pu = uwp.split("/")[0];
        else pu = uwp;
        return pu;
    }
    
}
