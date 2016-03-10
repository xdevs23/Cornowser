package org.xdevs23.management.config;

public class SharedPreferenceArray {

    public static String getPreferenceString(String[] array) {
        StringBuilder sb = new StringBuilder();
        for ( String s : array)
            sb.append(s.replace(":", "::").replace("|", "||"))
                    .append("|:|");
        sb.delete(sb.length() - 3, sb.length());
        return sb.toString();
    }

    public static String[] getStringArray(String preferenceString) {
        if(preferenceString.isEmpty()) return null;
        String[] array = preferenceString.split("([|][:][|])");
        for ( int i = 0; i < array.length; i++ )
            array[i] = array[i].replace("::", ":").replace("||", "|");
        return array;
    }

}
