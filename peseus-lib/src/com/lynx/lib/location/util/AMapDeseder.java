package com.lynx.lib.location.util;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

public class AMapDeseder {
	private static String Algorithm = "DESede";
	private static Cipher cipher_en = null;
	private static Cipher cipher_de = null;
	
	public static void setKeySpec(String keyspec) {
		try {
			SecureRandom sr = new SecureRandom();
			DESedeKeySpec spec = new DESedeKeySpec(keyspec.getBytes("utf-8"));
			SecretKeyFactory sk_factory = SecretKeyFactory.getInstance(Algorithm);
			SecretKey sk = sk_factory.generateSecret(spec);
			cipher_en = Cipher.getInstance(Algorithm);
			cipher_en.init(1, sk, sr);
			cipher_de = Cipher.getInstance(Algorithm);
			cipher_de.init(2, sk, sr);
		}
		catch (Exception e) {
			
		}
	} // LTAMapDeseder
	
	public static String encrypt(String input) {
		try {
			byte[] bytes = input.getBytes("utf-8");
			bytes = cipher_en.doFinal(bytes);
			return byte2hex(bytes);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String decrpyt(String input, String encoding) {
		try {
			byte[] bytes = cipher_de.doFinal(hex2byte(input));
			return new String(bytes, encoding);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String byte2hex(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		String str = null;
		for (int i=0; i<bytes.length; ++i) {
			if ((str = Integer.toHexString(bytes[i] & 0xFF)).length() == 1) {
				sb.append("0").append(str);
			}
			else {
				sb.append(str);
			}
		}
		
		return sb.toString();
	} // byte2hex
	
	public static byte[] hex2byte(String str) {
		if (str == null) {
			return null;
		}
		int length = str.trim().length();
		String tmp = str.trim();
		if (length == 0 || length % 2 == 1) {
			return null;
		}
		byte[] bytes = new byte[length / 2];
		try {
			for (int i=0; i<length; i+=2) {
				bytes[i / 2] = (byte)Integer.decode("0X" + tmp.substring(i, i + 2)).intValue();
			}
			return bytes;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
