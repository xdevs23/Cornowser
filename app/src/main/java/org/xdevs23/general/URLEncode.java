package org.xdevs23.general;

import java.net.URLEncoder;

public class URLEncode {

    public static String encode(String s) {
        try {
            return URLEncoder.encode(s, "utf-8");
        } catch(Exception ex) {
            return s;
        }
    }

}
