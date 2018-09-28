/* 
 * OldPortal Utilites Library is available under the MIT License. See http://opensource.org/licenses/MIT for full text.
 *
 * Copyright (C) Dmitry Ognyannikov, 2005
*/

package com.oldportal.util;

/**
 *
 * @author Dmitry Ognyannikov
 */
public class StringToolkit {
    // constructors:
    /** Creates a new instance of StringToolkit */
    public StringToolkit() {
    }

    // members:

    // methods:
    public static int findFirst(String body, String key)
    {
        return findFirstFrom(body, key, 0);
    }

    public static int findFirstFrom(String body, String key, int beginIndex)
    {
        return findFirstFromTo(body, key, beginIndex, body.length()-1);
    }

    public static int findFirstFromTo(String body, String key, int beginIndex, int endIndex)
    {
        if (key.length() < 1)
            return -1;

        if (beginIndex > body.length()-key.length())
            return -1;

        char bodyArray[] = body.toCharArray();
        char keyArray[] = key.toCharArray();
        for (int i=beginIndex; i<=endIndex; i++)
        {
            if (bodyArray[i] == keyArray[0])
            {
                if (i+keyArray.length-1 >= bodyArray.length)
                    return -1;//out of length

                for (int g=0; g<keyArray.length; g++)
                {
                    if (bodyArray[i+g] != keyArray[g])
                        break;

                    if (g == keyArray.length-1)
                        return i;
                }
            }
        }

        return -1;
    }

    public static int findLastFromTo(String body, String key, int beginIndex, int endIndex)
    {
        if (key.length() < 1)
            return -1;

        if (beginIndex > body.length()-key.length())
            return -1;

        char bodyArray[] = body.toCharArray();
        char keyArray[] = key.toCharArray();
        for (int i=endIndex; i>=beginIndex; i--)
        {
            if (bodyArray[i] == keyArray[0])
            {
                if (i+keyArray.length-1 >= bodyArray.length)
                    return -1;//out of length

                for (int g=0; g<keyArray.length; g++)
                {
                    if (bodyArray[i+g] != keyArray[g])
                        break;

                    if (g == keyArray.length-1)
                        return i;
                }
            }
        }

        return -1;
    }

    public static int findLastTo(String body, String key, int endIndex)
    {
        return findLastFromTo(body, key, 0, endIndex);
    }

    public static int findFirstIgnoreCase(String body, String key)
    {
        return findFirstFromIgnoreCase(body, key, 0);
    }

    public static int findFirstFromIgnoreCase(String body, String key, int beginIndex)
    {
        if (key.length() < 1)
            return -1;

        if (beginIndex > body.length()-key.length())
            return -1;

        char bodyArray[] = body.toCharArray();
        char keyArray[] = key.toCharArray();
        for (int i=beginIndex; i<bodyArray.length; i++)
        {
            if (Character.toLowerCase(bodyArray[i]) == Character.toLowerCase(keyArray[0]))
            {
                if (i+keyArray.length-1 >= bodyArray.length)
                    return -1;//out of length

                for (int g=0; g<keyArray.length; g++)
                {
                    if (Character.toLowerCase(bodyArray[i+g]) != Character.toLowerCase(keyArray[g]))
                        break;

                    if (g == keyArray.length-1)
                        return i;
                }
            }
        }

        return -1;
    }

    public static int findFirstFromToIgnoreCase(String body, String key, int beginIndex, int endIndex)
    {
        if (key.length() < 1)
            return -1;

        if (beginIndex > body.length()-key.length())
            return -1;

        char bodyArray[] = body.toCharArray();
        char keyArray[] = key.toCharArray();
        for (int i=beginIndex; i<=endIndex; i++)
        {
            if (Character.toLowerCase(bodyArray[i]) == Character.toLowerCase(keyArray[0]))
            {
                if (i+keyArray.length-1 >= bodyArray.length)
                    return -1;//out of length

                for (int g=0; g<keyArray.length; g++)
                {
                    if (Character.toLowerCase(bodyArray[i+g]) != Character.toLowerCase(keyArray[g]))
                        break;

                    if (g == keyArray.length-1)
                        return i;
                }
            }
        }

        return -1;
    }


    public static String replaceFirst(String body, String key, String replacement)
    {
        return replaceFirstFrom(body,  key, replacement, 0);
    }

    public static String replaceFirstFrom(String body, String key, String replacement, int beginIndex)
    {
        int keyBeginIndex = findFirstFrom(body, key, beginIndex);
        if (keyBeginIndex < 0)
            return body;

        int size = body.length() - key.length() + replacement.length();

        char bodyArray[] = body.toCharArray();
        char replacementArray[] = replacement.toCharArray();

        char ret[] = new char[size];

        int retIndex = 0;

        for (int i=0; i<keyBeginIndex; i++)
        {
            ret[retIndex] = bodyArray[i];
            retIndex++;
        }

        for (int i=0; i<replacementArray.length; i++)
        {
            ret[retIndex] = replacementArray[i];
            retIndex++;
        }

        for (int i=keyBeginIndex+key.length(); i<bodyArray.length; i++)
        {
            ret[retIndex] = bodyArray[i];
            retIndex++;
        }

        return new String(ret);
    }

    public static String replaceAll(String body, String key, String replacement)
    {
        String retBody = new String(body);
        int keyBeginIndex = findFirst(retBody, key);
        if (keyBeginIndex < 0)
            return retBody;

        while ((keyBeginIndex >= 0) && (keyBeginIndex < retBody.length()))
        {
            retBody = replaceFirstFrom(retBody, key, replacement, keyBeginIndex);
            keyBeginIndex = findFirstFrom(retBody, key, keyBeginIndex+replacement.length());
        }

        return retBody;
    }



    public static String getTextBetweenKeys(String body, String beginKey, String endKey)
    {
        int beginIndex = findFirst(body, beginKey);
        if (beginIndex < 0)
            return "";

        int endIndex = findFirstFrom(body, endKey, beginIndex+beginKey.length());
        if (endIndex < 0)
            return "";

        return body.substring(beginIndex+beginKey.length(), endIndex);
    }

    public static String replaceTextBetweenKeys(String body, String replacement, String beginKey, String endKey)
    {
        int beginIndex = findFirst(body, beginKey);
        if (beginIndex < 0)
            return body;

        int endIndex = findFirstFrom(body, endKey, beginIndex+beginKey.length());
        if (endIndex < 0)
            return body;

        String ret = body.substring(0, beginIndex+beginKey.length());

        ret += replacement;

        ret += body.substring(endIndex);

        return ret;
    }

    public static String getTextBetweenKeysIgnoreCase(String body, String beginKey, String endKey)
    {
        int beginIndex = findFirstIgnoreCase(body, beginKey);
        if (beginIndex < 0)
            return "";

        int endIndex = findFirstFromIgnoreCase(body, endKey, beginIndex+beginKey.length());
        if (endIndex < 0)
            return "";

        return body.substring(beginIndex+beginKey.length(), endIndex);
    }

    public static String replaceTextBetweenKeysIgnoreCase(String body, String replacement, String beginKey, String endKey)
    {
        int beginIndex = findFirstIgnoreCase(body, beginKey);
        if (beginIndex < 0)
            return body;

        int endIndex = findFirstFromIgnoreCase(body, endKey, beginIndex+beginKey.length());
        if (endIndex < 0)
            return body;

        String ret = body.substring(0, beginIndex+beginKey.length());

        ret += replacement;

        ret += body.substring(endIndex);

        return ret;
    }

    public static int calcStringsCount(String text)
    {
        if (text.length() == 0)
            return 0;

        int count = 1;
        int length = text.length();
        for (int i=0; i<length; i++)
        {
            if (text.charAt(i) == '\n')
                count ++;
        }
        return count;
    }


    public static String toHexString(byte[] data) {
        return new String(encodeHex(data));
    }

    public static byte[] fromHexString(String hexString) {
        return decodeHex(hexString.toCharArray());
    }


    /**
     * Used to build output as Hex
     */
    private static final char[] DIGITS = {
        '0', '1', '2', '3', '4', '5', '6', '7',
           '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
    };

    /**
     * Converts an array of bytes into an array of characters representing the hexidecimal values of each byte in order.
     * The returned array will be double the length of the passed array, as it takes two characters to represent any
     * given byte.
     *
     * @param data
     *                  a byte[] to convert to Hex characters
     * @return A char[] containing hexidecimal characters
     */
    public static char[] encodeHex(byte[] data) {
        int l = data.length;

           char[] out = new char[l << 1];

           // two characters form the hex value.
           for (int i = 0, j = 0; i < l; i++) {
               out[j++] = DIGITS[(0xF0 & data[i]) >>> 4 ];
               out[j++] = DIGITS[ 0x0F & data[i] ];
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
     * @return A byte array containing binary data decoded from
     *         the supplied char array.
     * @throws DecoderException Thrown if an odd number or illegal of characters
     *         is supplied
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
    private static int toDigit(char ch, int index){
        int digit = Character.digit(ch, 16);
        if (digit == -1) {
            return 0;//error
        }
        return digit;
    }

    private static final char rusCharsSmall[] = {'а', 'б', 'в', 'г', 'д', 'е', 'ё', 'ж', 'з', 'и', 'й', 'к', 'л', 'м', 'н', 'о', 'п', 'р', 'с', 'т', 'у', 'ф', 'х', 'ц', 'ч', 'ш', 'щ', 'ъ', 'ы', 'ь', 'э', 'ю', 'я'};
    private static final String translitCharsSmall[] = {"a", "b", "v", "g", "d", "e", "yo", "zh", "z", "i", "j", "k", "l", "m", "n", "o", "p", "r", "s", "t", "u", "f", "kh", "c", "ch", "sh", "sch", "#", "y", "\'", "je", "yu", "ya"};
    /** Convert cyrillyc (Russian) text to English characters translit */
    public static String toEnglishTranslit(String text)
    {
        StringBuilder ret = new StringBuilder();
        int size = text.length();
        for (int i=0; i<size; i++)
        {
            ret.append(charToTranslit(text.charAt(i)));
        }
        return ret.toString();
    }

    private static String charToTranslit(char c)
    {
        if (!Character.isLetter(c))
            return Character.toString(c);

        char cSmall = Character.toLowerCase(c);
        for (int i=0; i<rusCharsSmall.length; i++)
        {
            if (cSmall == rusCharsSmall[i])
            {
                if (Character.isLowerCase(c))
                    return translitCharsSmall[i];
                else
                    return translitCharsSmall[i].toUpperCase();
            }
        }

        return Character.toString(c);
    }

    public static String wordWrapStringToMultistring(String text)
    {
        return wordWrapStringToMultistring(text, 80);
    }

    public static String wordWrapStringToMultistring(String text, int maxStringSymbols)
    {
        return wordWrapStringToMultistring(text, maxStringSymbols, "\n");
    }

    public static String wordWrapStringToMultistring(String text, int maxStringSymbols, String delimiter)
    {
        if (text == null)
            return text;

        StringBuilder ret = new StringBuilder(text.length() + 10);
        int size = text.length();
        int lastSpaceIndex = 0;
        int currentStringBeginIndex = 0;
        for (int i=0; i<size; i++)
        {
            if (i-currentStringBeginIndex > maxStringSymbols)
            {
                if (i-currentStringBeginIndex > maxStringSymbols)
                {
                    if (currentStringBeginIndex >= lastSpaceIndex)
                    {
                        ret.append(text.substring(currentStringBeginIndex, i));
                        ret.append(delimiter);
                        currentStringBeginIndex = i + 1;
                        lastSpaceIndex = i;
                    }
                    else
                    {
                        ret.append(text.substring(currentStringBeginIndex, lastSpaceIndex));
                        ret.append(delimiter);
                        currentStringBeginIndex = lastSpaceIndex + 1;
                        lastSpaceIndex = i;
                    }
                }
            }
            
            if (Character.isSpaceChar(text.charAt(i)))
            {
//                if (i-currentStringBeginIndex > maxStringSymbols)
//                {
//                    if (currentStringBeginIndex >= lastSpaceIndex)
//                    {
//                        ret.append(text.substring(currentStringBeginIndex, i));
//                        ret.append(delimiter);
//                        currentStringBeginIndex = i + 1;
//                        lastSpaceIndex = i;
//                    }
//                    else
//                    {
//                        ret.append(text.substring(currentStringBeginIndex, lastSpaceIndex));
//                        ret.append(delimiter);
//                        currentStringBeginIndex = lastSpaceIndex + 1;
//                        lastSpaceIndex = i;
//                    }
//                }

                lastSpaceIndex = i;
            }
        }

        if (currentStringBeginIndex < size-1)
            ret.append(text.substring(currentStringBeginIndex));

        return ret.toString();
    }

}
