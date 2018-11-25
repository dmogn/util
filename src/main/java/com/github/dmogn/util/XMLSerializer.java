/* 
 * OldPortal Utilites Library is available under the MIT License. See http://opensource.org/licenses/MIT for full text.
 *
 * Copyright (C) Dmitry Ognyannikov, 2005
 */
package com.github.dmogn.util;

/**
 * Serialize strings from and to XML strings.
 *
 * @author Dmitry Ognyannikov
 */
public class XMLSerializer {

    public XMLSerializer() {
    }

    static public org.jdom.Element saveString(String fieldName, String string) {
        org.jdom.Element element = new org.jdom.Element(fieldName);
        org.jdom.Element elementString = new org.jdom.Element("EncodedString");
        element.addContent(elementString);
        org.jdom.CDATA data = new org.jdom.CDATA(encodeString(string));
        elementString.addContent(data);
        return element;
    }

    static public String loadString(org.jdom.Element element) {
        if (element == null) {
            return "";
        }

        org.jdom.Element encodedStringCDATA = element.getChild("EncodedString");
        String encodedString = encodedStringCDATA.getText();
        return decodeString(encodedString);
    }

    static public String encodeString(String str) {
        StringBuilder ret = new StringBuilder((int) (str.length() * 1.1));
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == '\\') {
                ret.append("\\\\");
            } else if (c == ']') {
                ret.append("\\]");
            } else if (c == '\t') {
                ret.append("\\t");
            } else if (c == '\r') {
                ret.append("\\r");
            } else if (c == '\n') {
                ret.append("\\n");
            } else if (c == ' ') {
                ret.append("\\ ");
            } else {
                ret.append(c);
            }
        }
        return ret.toString();
    }

    static public String decodeString(String str) {
        StringBuilder ret = new StringBuilder(str.length());
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == '\\') {
                if (i > str.length() - 2) {
                    return ret.toString();//error
                } else {
                    char c2 = str.charAt(i + 1);
                    if (c2 == '\\') {
                        ret.append('\\');
                    } else if (c2 == ']') {
                        ret.append(']');
                    } else if (c2 == 't') {
                        ret.append('\t');
                    } else if (c2 == 'r') {
                        ret.append('\r');
                    } else if (c2 == 'n') {
                        ret.append('\n');
                    } else if (c2 == ' ') {
                        ret.append(' ');
                    } else {
                        continue;//error - unknown symbol
                    }
                    i++;
                }
            } else {
                ret.append(c);
            }
        }
        return ret.toString();
    }

}
