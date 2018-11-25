/* 
 * OldPortal Utilites Library is available under the MIT License. See http://opensource.org/licenses/MIT for full text.
 *
 * Copyright (C) Dmitry Ognyannikov, 2005
 */
package com.github.dmogn.util;

import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

public class ServiceFunctions {

    public ServiceFunctions() {
    }

    public static boolean isWindowsOS() {
        if (java.io.File.separatorChar == '\\') {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isUnixOS() {
        if (java.io.File.separatorChar == '/') {
            return true;
        } else {
            return false;
        }
    }

    public static void sort(Vector objects) {
        Object array[] = objects.toArray();
        Arrays.sort(array);
        objects.clear();
        for (int i = 0; i < array.length; i++) {
            objects.add(array[i]);
        }
    }

    public static void sort(Vector objects, Comparator comparator) {
        Object array[] = objects.toArray();
        Arrays.sort(array, comparator);
        objects.clear();
        for (int i = 0; i < array.length; i++) {
            objects.add(array[i]);
        }
    }

    public static void copyFile(java.io.File from, java.io.File to) throws Exception {
        if (!from.exists()) {
            throw new IllegalArgumentException("Source file must exist. file: " + from.getCanonicalPath());
        }

        if (to.exists()) {
            to.delete();
        }
        java.io.FileInputStream in = new java.io.FileInputStream(from);
        java.io.FileOutputStream out = new java.io.FileOutputStream(to);
        while (in.available() > 0) {
            out.write(in.read());
        }
        in.close();
        out.close();
    }

    private static java.security.SecureRandom random = null;

    public synchronized static long generateUniqueId() {
        if (random == null) {
            random = new java.security.SecureRandom();
        }

        return Math.abs(random.nextLong());
    }

    public static String passwordToChecksum(String password) {
        try {
            return passwordToChecksum(password, "UTF-8");
        } catch (java.io.UnsupportedEncodingException ex) {
            System.out.println("passwordToChecksum() exception:" + ex.toString());
            return null;
        }
    }

    public static String passwordToChecksum(String password, String charset) throws java.io.UnsupportedEncodingException {
        try {
            return passwordToChecksum(password, charset, "SHA");
        } catch (java.security.NoSuchAlgorithmException ex) {
            System.out.println("passwordToChecksum() exception:" + ex.toString());
            return null;
        }
    }

    public static String passwordToChecksum(String password, String charset, String algorithm) throws java.io.UnsupportedEncodingException, java.security.NoSuchAlgorithmException {
        return passwordToChecksum(password, charset, "SHA", "HEX");
    }

    public static String passwordToChecksum(String password, String charset, String algorithm, String encodeAlgorithm) throws java.io.UnsupportedEncodingException, java.security.NoSuchAlgorithmException {
        byte passwordBuffer[] = password.getBytes(charset);

        MessageDigest md = MessageDigest.getInstance(algorithm);
        md.update(passwordBuffer);
        byte[] passwordDigest = md.digest();

        if (encodeAlgorithm.equalsIgnoreCase("HEX")) {
            return StringToolkit.toHexString(passwordDigest);
        } else if (encodeAlgorithm.equalsIgnoreCase("Base64")) {
            return Base64.encode(passwordDigest);
        } else {
            throw new IllegalArgumentException("unknown encodeAlgorithm");
        }
    }

    private String signData(byte data[], PrivateKey privKey, String signatureAlgorithm) {
        try {
            Signature rsaSignature = Signature.getInstance(signatureAlgorithm);
            rsaSignature.initSign(privKey);
            rsaSignature.update(data);
            byte[] sign = rsaSignature.sign();

            String encsign = com.github.dmogn.util.StringToolkit.toHexString(sign);

            return encsign;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static boolean verifyDataSign(byte data[], PublicKey pubKey, String signature, String signatureAlgorithm) {
        try {
            Signature signatureImpl = Signature.getInstance(signatureAlgorithm);
            signatureImpl.initVerify(pubKey);
            signatureImpl.update(data);

            byte sign[] = com.github.dmogn.util.StringToolkit.fromHexString(signature);

            return signatureImpl.verify(sign);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static PublicKey loadPublicKey(String keyHexString, String algorithm) throws Exception {
        byte data[] = com.github.dmogn.util.StringToolkit.fromHexString(keyHexString);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(data);
        KeyFactory factory = KeyFactory.getInstance(algorithm);
        PublicKey key = factory.generatePublic(keySpec);
        return key;
    }
}
