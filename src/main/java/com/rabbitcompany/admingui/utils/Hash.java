package com.rabbitcompany.admingui.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hash {

    public String createLicenseKey(String username){
        return new StringBuilder(Hash(username, "SHA-1").toUpperCase().substring(0, 20)).insert(5, "-").insert(11, "-").insert(17, "-").toString();
    }

    public String Hash(String password, String algorithm){
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            return bytesToHex(digest.digest(password.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            return password;
        }
    }

    private String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
