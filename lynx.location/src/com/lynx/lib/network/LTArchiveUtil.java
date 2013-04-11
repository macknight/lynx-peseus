package com.lynx.lib.network;

/**
 * 
 * @author chris.liu
 *
 */
public class LTArchiveUtil {
	private static final String[] hexDigits = { "0", "1", "2", "3", "4", "5", 
	    "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };
	
	public static int shortHash(String name) {
		int i = name.hashCode();
		i = 0xFFFF & i ^ i >>> 16;
		return i;
	}
	
	public static String byteArrayToHexString(byte[] b) {
		StringBuilder sb = new StringBuilder();
		for (int i=0; i<b.length; ++i) {
			sb.append(byteToHexString(b[i]));
		}
		return sb.toString();
	}
	
	private static String byteToHexString(byte b) {
		int n = b & 0xFF;
		int d1 = n >> 4;
		int d2 = n & 0xF;
		return hexDigits[d1] + hexDigits[d2];
	}
}
