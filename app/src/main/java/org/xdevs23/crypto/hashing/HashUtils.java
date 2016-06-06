package org.xdevs23.crypto.hashing;

import org.xdevs23.crypto.HexUtils;
import org.xdevs23.debugutils.StackTraceParser;

import java.security.MessageDigest;

public class HashUtils {

    public static final class HashTypes {
        public static final String
                MD2         = "MD2",
                MD4         = "MD4",
                MD5         = "MD5",
                MD6         = "MD6",
                SHA1        = "SHA-1",
                SHA128      = "SHA-128",
                SHA256      = "SHA-256",
                SHA512      = "SHA-512",
                BLAKE256    = "BLAKE-256",
                BLAKE512    = "BLAKE-512",
                ECOH        = "ECOH",
                FSB         = "FSB",
                GOST        = "GOST",
                RIPEMD      = "RIPEMD",
                RIPEMD128   = "RIPEMD-128",
                RIPEMD160   = "RIPEMD-160",
                RIPEMD320   = "RIPEMD-320",
                SWIFFT      = "SWIFFT",
                TIGER       = "Tiger",
                WHIRLPOOL   = "Whirlpool"
                        ;


    }

    public static String hash(byte[] data, String type, String defaultValue) {
        try {
            MessageDigest md = MessageDigest.getInstance(type);
            md.update(data, 0, data.length);
            byte[] hash = md.digest();
            return HexUtils.convertToHex(hash);
        } catch(Exception ex) {
            StackTraceParser.logStackTrace(ex);
            return defaultValue;
        }
    }

    public static String hash(String text, String type, String charsetName, String defaultValue) {
        try {
            MessageDigest md = MessageDigest.getInstance(type);
            md.update(text.getBytes(charsetName), 0, text.length());
            byte[] hash = md.digest();
            return HexUtils.convertToHex(hash);
        } catch(Exception ex) {
            StackTraceParser.logStackTrace(ex);
            return defaultValue;
        }
    }

    public static String hash(String text, String hashType) {
        return hash(text, hashType, "utf-8", "");
    }

}
