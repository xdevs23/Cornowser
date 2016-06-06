package org.xdevs23.general;

import java.net.URLEncoder;

public class URLEncode {

    public static String encode(String stringToEncode) {
        try {
            return URLEncoder.encode(stringToEncode, "utf-8");
        } catch(Exception ex) {
            return stringToEncode;
        }
    }

}
