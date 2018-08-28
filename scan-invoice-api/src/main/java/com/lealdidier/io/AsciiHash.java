package com.lealdidier.io;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.function.Function;

public class AsciiHash implements Function<String, String> {
    private static final char[] hex = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    public static String byteArray2Hex(byte[] bytes) {
    StringBuffer sb = new StringBuffer(bytes.length * 2);
    for(final byte b : bytes) {
        sb.append(hex[(b & 0xF0) >> 4]);
        sb.append(hex[b & 0x0F]);
    }
    return sb.toString();
}
    @Override
    public String apply(String s) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(s.getBytes());
            return byteArray2Hex(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
