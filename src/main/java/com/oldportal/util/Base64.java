/* 
 * OldPortal Utilites Library is available under the MIT License. See http://opensource.org/licenses/MIT for full text.
 *
 * Copyright (C) Dmitry Ognyannikov, 2005
*/

package com.oldportal.util;

public final class  Base64 {

    static private final int  BASELENGTH         = 255;
    static private final int  LOOKUPLENGTH       = 64;
//    static private final int  TWENTYFOURBITGROUP = 24;
//    static private final int  EIGHTBIT           = 8;
//    static private final int  SIXTEENBIT         = 16;
//    static private final int  SIXBIT             = 6;
//    static private final int  FOURBYTE           = 4;
    static private final int  SIGN               = -128;
    static private final char PAD                = '=';
//    static private final boolean fDebug          = false;

    static final private byte [] base64Alphabet        = new byte[BASELENGTH];
    static final private char [] lookUpBase64Alphabet  = new char[LOOKUPLENGTH];

        static {
                for (int i = 0; i<BASELENGTH; i++) base64Alphabet[i] = -1;
                for (int i = 'Z'; i >= 'A'; i--)   base64Alphabet[i] = (byte) (i-'A');
                for (int i = 'z'; i>= 'a'; i--)    base64Alphabet[i] = (byte) ( i-'a' + 26);
                for (int i = '9'; i >= '0'; i--)   base64Alphabet[i] = (byte) (i-'0' + 52);
                base64Alphabet['+']  = 62;
                base64Alphabet['/']  = 63;
                for (int i = 0; i<=25; i++) lookUpBase64Alphabet[i] = (char)('A'+i);
                for (int i = 26,  j = 0; i<=51; i++, j++) lookUpBase64Alphabet[i] = (char)('a'+ j);
                for (int i = 52,  j = 0; i<=61; i++, j++) lookUpBase64Alphabet[i] = (char)('0' + j);
                lookUpBase64Alphabet[62] = (char)'+';
                lookUpBase64Alphabet[63] = (char)'/';
        }

        protected static boolean isWhiteSpace(char octect) {
                return (octect == 0x20 || octect == 0xd || octect == 0xa || octect == 0x9);
        }

        protected static boolean isPad(char octect) {
                return (octect == PAD);
        }

        protected static boolean isData(char octect) {
                return (base64Alphabet[octect] != -1);
        }

        protected static boolean isBase64(char octect) {
                return (isWhiteSpace(octect) || isPad(octect) || isData(octect));
        }


        private static final char PAD64 = '=';
        private static final char[] BASE64 = {
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
        'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
        'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
        'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f',
        'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
        'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
        'w', 'x', 'y', 'z', '0', '1', '2', '3',
        '4', '5', '6', '7', '8', '9', '+', '/'
        };



        public static String encode(byte[] in) {

                if (in == null || in.length == 0) return null;
                int len =  in.length;
                int size  = (len / 3 + (len % 3 == 0 ? 0 : 1)) * 4;
                if (size == 0) return "";

                char[] out = new char[size];
                int i = 0, j = 0;
                byte k, l, b1, b2, b3, val1, val2, val3;
                while(len > 0){
                        if(len < 3) break;
                        b1 = in[i++];
                        b2 = in[i++];
                        b3 = in[i++];
                        l  = (byte)(b2 & 0x0f);
                        k  = (byte)(b1 & 0x03);
                        val1 = ((b1 & SIGN)==0)?(byte)(b1>>2):(byte)((b1)>>2^0xc0);
                        val2 = ((b2 & SIGN)==0)?(byte)(b2>>4):(byte)((b2)>>4^0xf0);
                        val3 = ((b3 & SIGN)==0)?(byte)(b3>>6):(byte)((b3)>>6^0xfc);
                        out[j++] = BASE64[ val1 ];
                        out[j++] = BASE64[ val2 | ( k<<4 )];
                        out[j++] = BASE64[ (l <<2 ) | val3 ];
                        out[j++] = BASE64[ b3 & 0x3f ];
                        len -= 3;
                }
                if(len == 2){
                        b1 = in[i];
                        b2 = in[i + 1];
                        l = ( byte ) ( b2 &0x0f );
                        k = ( byte ) ( b1 &0x03 );
                        val1 = ((b1 & SIGN)==0)?(byte)(b1>>2):(byte)((b1)>>2^0xc0);
                        val2 = ((b2 & SIGN)==0)?(byte)(b2>>4):(byte)((b2)>>4^0xf0);
                        out[j++] = BASE64[ val1 ];
                        out[j++] = BASE64[ val2 | ( k<<4 )];
                        out[j++] = BASE64[ l<<2 ];
                        out[j++] = PAD64;
                }else if (len == 1){
                        b1 = in[i];
                        k = (byte) ( b1 &0x03 );
                        val1 = ((b1 & SIGN)==0)?(byte)(b1>>2):(byte)((b1)>>2^0xc0);
                        out[j++] = BASE64[ val1 ];
                        out[j++] = BASE64[ k<<4 ];
                        out[j++] = PAD64;
                        out[j++] = PAD64;
                }

                return new String(out);
        }

        public static  byte[] decode(String str) throws Exception{
                char[] in = str.toCharArray();
                int l = in.length;
                int size = (l / 4 + (l % 4 == 0 ? 0 : 1)) * 3;
                byte[] buff = new byte[size];
                byte[] b = new byte[4];
                int i = 0, j = 0, k = 0, pad = 0;
                while(i < l){
                        char c = in[i++];
                        if(c >= 'A' && c <= 'Z'){
                                b[k++] = (byte)(c - 'A');
                        }else if(c >= 'a' && c <= 'z'){
                                b[k++] = (byte)(c - 'a' + 26);
                        }else if(c >= '0' && c <= '9'){
                                b[k++] = (byte)(c - '0' + 52);
                        }else if(c == '+'){
                                b[k++] = 62;
                        }else if(c == '/'){
                                b[k++] = 63;
                        }else if (c == PAD64){
                                switch(k){
                                        case 0: throw new Exception("Bad Base64 padding");
                                        case 1: pad = 2;break;
                                        case 2: pad = 2;break;
                                        case 3: pad = 1;break;
                                }
                                while(k < 4){
                                        b[k++] = 0;
                                        i++;
                                }
                        }else if(c == ' ' || c == '\r' || c == 'n'){
                                i++;
                                continue;
                        }else{
                                throw new Exception("Not a base64 symbol");
                        }
                        if(k == 4){
                                buff[j++] = (byte)((b[0] << 2) | (b[1] >> 4));
                                buff[j++] = (byte)(((b[1] & 0xf) << 4) | ((b[2] >> 2)& 0xf));
                                buff[j++] = (byte)((b[2] << 6) | (b[3]));
                                k = 0;
                        }
                }
                j -= pad;
                byte[] out = new byte[j];
                System.arraycopy(buff,0,out,0,j);
                return out;

        }


        protected static int removeWhiteSpace(char[] data) {
                if (data == null) return 0;
                int len = data.length;
                int j = 0;
                for (int i = 0; i < len; i++)
                        switch(data[i]){
                                case 0x20:
                                case 0x0D:
                                case 0x0A:
                                case 0x09:
                                        break;
                                default:
                                        data[j++] = data[i];
                        }
                return j;
        }
}