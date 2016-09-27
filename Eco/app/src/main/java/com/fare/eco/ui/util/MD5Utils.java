package com.fare.eco.ui.util;

import java.security.MessageDigest;

import android.util.Base64;

public class MD5Utils {

	/** 异或加密的key */
	private static final char X = 'x';

	/** 授权 */
	private static final String SECRET_KEY = "b359ad4f-1a27-4dd6-ad74-4d2bfc3d8fae";

	/** md5加密 */
	public static String MD5(String inStr) {
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		char[] charArray = inStr.toCharArray();
		byte[] byteArray = new byte[charArray.length];

		for (int i = 0; i < charArray.length; i++) {
			byteArray[i] = (byte) charArray[i];
		}

		byte[] md5Bytes = md5.digest(byteArray);
		StringBuffer hexValue = new StringBuffer();

		for (int i = 0; i < md5Bytes.length; i++) {
			int val = ((int) md5Bytes[i]) & 0xff; // 高24位置0 防止出错
			if (val < 0xf) { // 保证格式(<16的数16进制只有1位) eg: 13--d,补位为 0d
				hexValue.append("0");
			}
			hexValue.append(Integer.toHexString(val)); // 转换为16进制的字符串
		}

		return hexValue.toString();
	}

	/** base64 encode */
	private static String encryptByBase64(String str) {
		System.out.println("加密前:" + str);
		String trans = Base64.encodeToString(str.getBytes(), Base64.DEFAULT);
		System.out.println("加密后:" + trans);
		return trans;
	}

	/** base64 decode */
	private static String decodeByBase64(String str) {
		String restore = "";
		restore = new String(Base64.decode(str, Base64.DEFAULT));
		return restore;
	}

	/** 异或加密解密,用于二次加密 */
	public static String xor(String inStr) {
		char[] a = inStr.toCharArray();
		for (int i = 0; i < a.length; i++) {
			a[i] = (char) (a[i] ^ X);
		}
		String s = new String(a);
		return s;
	}

	public static void main(String args[]) {
		String s = new String("我的钱m" + SECRET_KEY);
		System.out.println("原始：" + s);
		System.out.println("MD5后：" + MD5(s));
		System.out.println("异或加密MD5：" + xor(MD5(s)));
		System.out.println("异或解密为MD5：" + xor(xor(MD5(s))));
		decodeByBase64(encryptByBase64(s));
	}

}
