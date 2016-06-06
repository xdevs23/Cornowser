package org.xdevs23.crypto;

public class HexUtils {

    public static String convertToHex(byte data[]) {
        StringBuilder sb = new StringBuilder();
        for (byte b : data) {
            int halfbyte = (b >>> 4) & 0x0F;
            int twohalfs = 0;
            do {
                sb.append((0 <= halfbyte) && (halfbyte <= 9)
                        ? (char)('0' +  halfbyte)
                        : (char)('a' + (halfbyte - 10)));
                halfbyte = b & 0x0F;
            } while (twohalfs++ < 1);
        }
        return sb.toString();
    }

    public static String convertToHex(String str) {
        return convertToHex(str.getBytes());
    }

}
