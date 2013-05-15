package com.lynx.lib.location.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

public class LTBMapDigester {
	private static char _fldif[] = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/.".toCharArray();
    private static char a[] = {
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 
        'a', 'b', 'c', 'd', 'e', 'f'
    };
	
	private static char[] digest(byte abyte0[]) {
        char ac[] = new char[32];
        try {
            MessageDigest messagedigest = MessageDigest.getInstance("MD5");
            messagedigest.update(abyte0);
            byte abyte1[] = messagedigest.digest();
            int i = 0;
            for(int j = 0; j < 16; j++) {
                byte byte0 = abyte1[j];
                ac[i++] = a[byte0 >>> 4 & 15];
                ac[i++] = a[byte0 & 15];
            }

        }
        catch(Exception exception) {
            exception.printStackTrace();
        }
        return ac;
    }

    private static char[] _mthif(byte abyte0[]) {
        char ac[] = new char[((abyte0.length + 2) / 3) * 4];
        int i = 0;
        for(int j = 0; i < abyte0.length; j += 4) {
            boolean flag = false;
            boolean flag1 = false;
            int k = 255 & abyte0[i];
            k <<= 8;
            if(i + 1 < abyte0.length) {
                k |= 255 & abyte0[i + 1];
                flag1 = true;
            }
            k <<= 8;
            if(i + 2 < abyte0.length) {
                k |= 255 & abyte0[i + 2];
                flag = true;
            }
            ac[j + 3] = _fldif[flag ? 63 - (k & 63) : 64];
            k >>= 6;
            ac[j + 2] = _fldif[flag1 ? 63 - (k & 63) : 64];
            k >>= 6;
            ac[j + 1] = _fldif[63 - k & 63];
            k >>= 6;
            ac[j + 0] = _fldif[63 - k & 63];
            i += 3;
        }

        return ac;
    }

    public static String digest(String s) {
        try {
            String s1 = "UTF-8";
            char ac[] = digest((new StringBuilder()).append(s).append("webgis").toString().getBytes(s1));
            byte abyte0[] = s.getBytes(s1);
            byte abyte1[] = new byte[abyte0.length + 2];
            for(int i = 0; i < abyte0.length; i++)
                abyte1[i] = abyte0[i];

            abyte1[abyte0.length] = (byte)(255 & Integer.parseInt(String.copyValueOf(ac, 10, 2), 16));
            abyte1[abyte0.length + 1] = (byte)(255 & Integer.parseInt(String.copyValueOf(ac, 20, 2), 16));
            String s2 = "";
            s2 = (new StringBuilder()).append(s2).append((char)(255 & Integer.parseInt(String.copyValueOf(ac, 6, 2), 16))).toString();
            s2 = (new StringBuilder()).append(s2).append((char)(255 & Integer.parseInt(String.copyValueOf(ac, 16, 2), 16))).toString();
            s2 = (new StringBuilder()).append(s2).append((char)(255 & Integer.parseInt(String.copyValueOf(ac, 26, 2), 16))).toString();
            char ac1[] = digest((new StringBuilder()).append(s2).append("webgis").toString().getBytes("iso-8859-1"));
            int j = abyte1.length;
            int k = s2.length();
            byte abyte2[] = new byte[j + k];
            for(int l = 0; l < (j + 31) / 32; l++) {
                int j1 = l * 32;
                for(int k1 = 0; k1 < 32 && j1 + k1 < j; k1++)
                    abyte2[j1 + k1] = (byte)(ac1[k1] & 255 ^ abyte1[j1 + k1] & 255);

            }

            for(int i1 = 0; i1 < k; i1++)
                abyte2[j + i1] = (byte)s2.charAt(i1);

            String s3 = new String(_mthif(abyte2));
            return s3;
        }
        catch(UnsupportedEncodingException unsupportedencodingexception) {
            unsupportedencodingexception.printStackTrace();
        }
        return "UnsupportedEncodingException";
    }

    
}
