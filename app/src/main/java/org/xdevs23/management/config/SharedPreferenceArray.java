package org.xdevs23.management.config;

public class SharedPreferenceArray {

    public static String getPreferenceString(String[] array) {
        if(array == null)     return "";
        if(array.length == 0) return "";
        StringBuilder sb = new StringBuilder();
        for ( String s : array) {
            String aps;
            if(s == null) return "";
            else aps = s;
            if(s.contains(":")) aps = s.replace(":", "::");
            if(s.contains("|")) aps = aps.replace("|", "||");
            sb.append(aps).append("|:|");
        }
        sb.delete(sb.length() - 3, sb.length());
        return sb.toString();
    }

    public static String[] getStringArray(String preferenceString) {
        if(preferenceString == null || preferenceString.isEmpty())
            return new String[] {""};
        String[] array = preferenceString.split("([|][:][|])");
        for ( int i = 0; i < array.length; i++ ) {
            String aps = array[i];
            if(aps.contains(":")) aps = aps.replace("::", ":");
            if(aps.contains("|")) aps = aps.replace("||", "|");
            array[i] = aps;
        }
        return array;
    }

}
