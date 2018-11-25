/*
 * GUID.java
 *
 * OldPortal Framework Library is available under the MIT License. See http://opensource.org/licenses/MIT for full text.
 *
 * Copyright (C) Dmitry Ognyannikov, 2005
 */
package com.github.dmogn.framework;

/**
 * GUID representation.
 *
 * @author Dmitry Ognyannikov
 */
public final class GUID implements Comparable {

    private byte bytes[] = new byte[16];
    private static java.security.SecureRandom random = new java.security.SecureRandom();//new String("SecureRandom()"+new java.util.Date().toString()).getBytes());

    // constructors:
    /**
     * Creates a new instance of GUID
     */
    public GUID() {
        clear();
    }

    public GUID(final GUID src) {
        for (int i = 0; i < src.bytes.length; i++) {
            bytes[i] = src.bytes[i];
        }
    }

    // methods:
    public void clear() {
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = 0;
        }
    }

    protected java.lang.Object clone() {
        return new GUID(this);
    }

    public boolean less(final GUID guid) {
        for (int i = 0; i < bytes.length; i++) {
            if (bytes[i] < guid.bytes[i]) {
                return true;
            }
        }
        return false;
    }

    public boolean isEmpty() {
        boolean ret = true;
        for (int i = 0; i < bytes.length; i++) {
            if (bytes[i] > 0) {
                ret = false;
            }
        }
        return ret;
    }

    static public GUID fromString(String guidString) {
        GUID ret = new GUID();
        if ((guidString == null) || (guidString.length() != ret.bytes.length * 2)) {
            return null;
        }
        ret.bytes = decodeHex(guidString.toCharArray());
        /*for (int i=0; i<ret.bytes.length; i++)
        {
          String byteString = guidString.substring(0,1);
          ret.bytes[i] = new Byte(byteString).byteValue();
        }//*/
        return ret;
    }

    static public GUID generateNew() {
        GUID ret = new GUID();
        random.nextBytes(ret.bytes);
        return ret;
    }

    /**
     * Used to build output as Hex
     */
    private static final char[] DIGITS = {
        '0', '1', '2', '3', '4', '5', '6', '7',
        '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
    };

    /**
     * Converts an array of bytes into an array of characters representing the
     * hexidecimal values of each byte in order. The returned array will be
     * double the length of the passed array, as it takes two characters to
     * represent any given byte.
     *
     * @param data a byte[] to convert to Hex characters
     * @return A char[] containing hexidecimal characters
     */
    public static char[] encodeHex(byte[] data) {

        int l = data.length;

        char[] out = new char[l << 1];

        // two characters form the hex value.
        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = DIGITS[(0xF0 & data[i]) >>> 4];
            out[j++] = DIGITS[0x0F & data[i]];
        }

        return out;
    }

    /**
     * Converts an array of characters representing hexidecimal values into an
     * array of bytes of those same values. The returned array will be half the
     * length of the passed array, as it takes two characters to represent any
     * given byte. An exception is thrown if the passed char array has an odd
     * number of elements.
     *
     * @param data An array of characters containing hexidecimal digits
     * @return A byte array containing binary data decoded from the supplied
     * char array.
     * @throws DecoderException Thrown if an odd number or illegal of characters
     * is supplied
     */
    public static byte[] decodeHex(char[] data) {

        int len = data.length;

        byte[] out = new byte[len >> 1];

        // two characters form the hex value.
        for (int i = 0, j = 0; j < len; i++) {
            int f = toDigit(data[j], j) << 4;
            j++;
            f = f | toDigit(data[j], j);
            j++;
            out[i] = (byte) (f & 0xFF);
        }

        return out;
    }

    /**
     * Converts a hexadecimal character to an integer.
     *
     * @param ch A character to convert to an integer digit
     * @param index The index of the character in the source
     * @return An integer
     * @throws DecoderException Thrown if ch is an illegal hex character
     */
    private static int toDigit(char ch, int index) {
        int digit = Character.digit(ch, 16);
        if (digit == -1) {
            return 0;//error
        }
        return digit;
    }

    public String toString() {
        /*String ret = "";
    for (int i=0; i<bytes.length; i++)
    {
      ret += byteToHexString(bytes[i]);
    }
    return ret;//*/
        return new String(encodeHex(bytes));
    }

    public boolean equalsGUID(GUID guid) {
        for (int i = 0; i < bytes.length; i++) {
            if (bytes[i] != guid.bytes[i]) {
                return false;
            }
        }
        return true;
    }

    public boolean equals(java.lang.Object src) {
        if (!(src instanceof GUID)) {
            return false;
        }
        return equalsGUID((GUID) src);
    }

    public int compareTo(java.lang.Object o) {
        if (!(o instanceof GUID)) {
            return -1;
        }
        GUID guid = (GUID) o;
        if (less(guid)) {
            return -1;
        } else if (equalsGUID(guid)) {
            return 0;
        } else {
            return +1;
        }
    }

}
