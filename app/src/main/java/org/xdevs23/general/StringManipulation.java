package org.xdevs23.general;

public class StringManipulation {

    public static String arrayToString(String[] array) {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < array.length; i++)
            sb.append(array[i]).append("\n");
        return sb.toString();
    }

}
