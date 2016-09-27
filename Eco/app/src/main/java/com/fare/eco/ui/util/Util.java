package com.fare.eco.ui.util;

import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;

public class Util {
	
	/** object转化为jsonStr */
	public static String createJsonString(String key, Object value) {
		JSONObject jsonObject = new JSONObject();

		try {
			jsonObject.put(key, value);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonObject.toString();
	}
	
	/** 3gwap"字符串常*/
	private static final String wapStr = "3gwap";
	/**保存APN信息的变量*/
	private static String apn;
	/** 判断apn是否3Gwap方式*/
	public static boolean is3Gwap(Context context) {
		if (null == apn) {
			getAPN(context);
		}
		if (null != apn && apn.equals(wapStr)) {
			return true;
		}
		return false;
	}

	/**
	 * 通过context取得ConnectivityManager中的NetworkInfo里关于apn的联网信
	 * @param context 浏览器对象上下文
	 * @return 代理模式
	 */
	private static String getAPN(Context context) {
		if (apn == null) {
			// 通过context得到ConnectivityManager连接管理
			ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			// 通过ConnectivityManager得到NetworkInfo网络信息
			NetworkInfo info = manager.getActiveNetworkInfo();
			// 获取NetworkInfo中的apn信息
			apn = info.getExtraInfo();
		}
		return apn;
	}

	/**
	 * 返回当前时间
	 * 
	 * @return "yyyy-MM-dd HH:mm:ss"格式的时间字符串
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getTime() {
		Calendar cale = Calendar.getInstance();
		Date tasktime = cale.getTime();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(tasktime);
	}

	/** 拆分字符串 */
	public static String[] split2(String str, String separatorChars) {
		return splitWorker(str, separatorChars, -1, false);
	}

	/**
	 * 拆分字符
	 * @param targetStr 目标字符串
	 * @param separatorChars 分隔符
	 * @param max 目标字符串的长度
	 * @param preserveAllTokens
	 * @return
	 */
	private static String[] splitWorker(String targetStr, String separatorChars,
			int max, boolean preserveAllTokens) {
		if (targetStr == null) {
			return null;
		}
		int len = targetStr.length();
		if (len == 0) {
			return new String[] { "" };
		}
		Vector<String> vector = new Vector<String>();
		int sizePlus1 = 1;
		int i = 0;
		int start = 0;
		boolean match = false;
		boolean lastMatch = false;
		if (separatorChars == null) {
			while (i < len) {
				if (targetStr.charAt(i) == '\r' || targetStr.charAt(i) == '\n'
						|| targetStr.charAt(i) == '\t') {
					if (match || preserveAllTokens) {
						lastMatch = true;
						if (sizePlus1++ == max) {
							i = len;
							lastMatch = false;
						}
						vector.addElement(targetStr.substring(start, i));
						match = false;
					}
					start = ++i;
				} else {
					lastMatch = false;
					match = true;
					i++;
				}
			}
		} else if (separatorChars.length() == 1) {
			char sep = separatorChars.charAt(0);
			while (i < len) {
				if (targetStr.charAt(i) == sep) {
					if (match || preserveAllTokens) {
						lastMatch = true;
						if (sizePlus1++ == max) {
							i = len;
							lastMatch = false;
						}
						vector.addElement(targetStr.substring(start, i));
						match = false;
					}
					start = ++i;
				} else {
					lastMatch = false;
					match = true;
					i++;
				}
			}
		} else {
			while (i < len) {
				int id = i + separatorChars.length() < len ? i
						+ separatorChars.length() : len;
				if (separatorChars.indexOf(targetStr.charAt(i)) >= 0
						&& separatorChars.equals(targetStr.substring(i, id))) {
					if (match || preserveAllTokens) {
						lastMatch = true;
						if (sizePlus1++ == max) {
							i = len;
							lastMatch = false;
						}
						vector.addElement(targetStr.substring(start, i));
						match = false;
					}
					i += separatorChars.length();
					start = i;
				} else {
					lastMatch = false;
					match = true;
					i++;
				}
			}
		}
		if (match || preserveAllTokens && lastMatch) {
			vector.addElement(targetStr.substring(start, i));
		}
		String[] ret = new String[vector.size()];
		vector.copyInto(ret);
		return ret;
	}

	/** gbk转换为utf8 */
	public static byte[] gbk2utf8(String gbkStr) {
		char c[] = gbkStr.toCharArray();
		byte[] fullByte = new byte[3 * c.length];
		for (int i = 0; i < c.length; i++) {
			int m = (int) c[i];
			String word = Integer.toBinaryString(m);
			StringBuffer sb = new StringBuffer();
			int len = 16 - word.length();
			for (int j = 0; j < len; j++) {
				sb.append("0");
			}
			sb.append(word);
			sb.insert(0, "1110");
			sb.insert(8, "10");
			sb.insert(16, "10");
			String s1 = sb.substring(0, 8);
			String s2 = sb.substring(8, 16);
			String s3 = sb.substring(16);
			byte b0 = Integer.valueOf(s1, 2).byteValue();
			byte b1 = Integer.valueOf(s2, 2).byteValue();
			byte b2 = Integer.valueOf(s3, 2).byteValue();
			byte[] bf = new byte[3];
			bf[0] = b0;
			fullByte[i * 3] = bf[0];
			bf[1] = b1;
			fullByte[i * 3 + 1] = bf[1];
			bf[2] = b2;
			fullByte[i * 3 + 2] = bf[2];
		}
		return fullByte;
	}

	public static boolean isValidUtf8(byte[] b, int aMaxCount) {
		int lLen = b.length, lCharCount = 0;
		for (int i = 0; i < lLen && lCharCount < aMaxCount; ++lCharCount) {
			byte lByte = b[i++];// to fast operation, ++ now, ready for the
			// following for(;;)
			if (lByte >= 0)
				continue;// >=0 is normal ascii
			if (lByte < (byte) 0xc0 || lByte > (byte) 0xfd)
				return false;
			int lCount = lByte > (byte) 0xfc ? 5 : lByte > (byte) 0xf8 ? 4
					: lByte > (byte) 0xf0 ? 3 : lByte > (byte) 0xe0 ? 2 : 1;
			if (i + lCount > lLen)
				return false;
			for (int j = 0; j < lCount; ++j, ++i)
				if (b[i] >= (byte) 0xc0)
					return false;
		}
		return true;
	}

	/** 获取url地址的文件名*/
	public static String getUrlPageName(String url) {
		int start = url.lastIndexOf('/') + 1;
		if (url.indexOf('.', start + 1) > -1) {
			return url.substring(start, url.indexOf('.', start + 1));
		}
		return url.substring(start);
	}

	/**
	 * 格式化时间
	 * 
	 * @param time 时间
	 * @return 返回标注格式时间
	 */
	public static String formatTime(String time) {
		// 201207181114
		String formatTime = "";
		int length = time.length();
		if (length == 8) {
			formatTime = time.substring(0, 4) + "-" + time.substring(4, 6)
					+ "-" + time.substring(6, 8);
		} else if (length == 12) {
			formatTime = time.substring(0, 4) + "-" + time.substring(4, 6)
					+ "-" + time.substring(6, 8) + " " + time.substring(8, 10)
					+ ":" + time.substring(10);
		} else if (length == 14) {
			formatTime = time.substring(0, 4) + "-" + time.substring(4, 6)
					+ "-" + time.substring(6, 8) + " " + time.substring(8, 10)
					+ ":" + time.substring(10, 12) + ":" + time.substring(12);
		}

		return formatTime;
	}

	/**
	 * MD5加密
	 */
	public static String getMD5Str(String sourceStr) {
		byte[] source = sourceStr.getBytes();
		String s = null;
		char hexDigits[] = { // 用来将字节转换成 16 进制表示的字符
		'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
				'e', 'f' };
		java.security.MessageDigest md = null;
		try {
			md = java.security.MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			// #debug
			e.printStackTrace();
		}
		if (md == null)
			return null;
		md.update(source);
		byte tmp[] = md.digest(); // MD5 的计算结果是一个 128 位的长整数，
		// 用字节表示就是 16 个字节
		char str[] = new char[16 * 2]; // 每个字节用 16 进制表示的话，使用两个字符，
		// 所以表示成 16 进制需要 32 个字符
		int k = 0; // 表示转换结果中对应的字符位置
		for (int i = 0; i < 16; i++) {
			// 从第一个字节开始，对 MD5 的每一个字节
			// 转换成 16 进制字符的转换
			byte byte0 = tmp[i]; // 取第 i 个字节
			str[k++] = hexDigits[byte0 >>> 4 & 0xf]; // 取字节中高 4 位的数字转换,
			// >>> 为逻辑右移，将符号位一起右移
			str[k++] = hexDigits[byte0 & 0xf]; // 取字节中低 4 位的数字转换
		}
		s = new String(str); // 换后的结果转换为字符串
		return s;
	}

	/**
	 * Object转String
	 * 
	 * @param object
	 * @return
	 */
	public static String toString(Object object) {
		if (object == null)
			return "";
		else
			return object.toString();
	}

	public static void saveCityName(Context context, String cityName) {
		PreferenceManager.getDefaultSharedPreferences(context).edit().putString("cityName", cityName).commit();
	}

	public static String getCityName(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context).getString("cityName", "北京");
	}

	//dp转化为px
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	//px转化为dp
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

}