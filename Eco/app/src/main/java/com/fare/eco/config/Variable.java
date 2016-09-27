package com.fare.eco.config;

import java.util.Map;

/**
 * 系统可变常量
 * 
 * @author ZhuYujie
 * 
 */
public final class Variable {

	public static int SYS_VERSION = 2;

	public static String SERVER_SOFT_URL = "http://app.mysnail.cn/snail";
	// public static String SERVER_SOFT_URL = "http://180.96.23.202:80/snail";

	public static String SERVER_IMAGE_URL = "";

	public static String SERVER_WEB_URL = "http://web.mysnail.cn/";

	public static String DEVICE_ID = "";

	public static String LINE1_NUNMBER = "";
	/**
	 * 是否进行更新的请求
	 */
	public static boolean isCheckUpdate = false;

	public static boolean net_proxy = false;

	public static int NETWORK_TYPE = 0;

	public final static class Size {
		public static int SCREEN_WIDTH = 0;
		public static int SCREEN_HEIGHT = 0;
		public static int CONTENT_HEIGHT = 0;
		public static int STATUS_HEIGHT = 0;
		public static int SCREEN_SIZE = 1;
		public static float density;
	}

	/**
	 * http 超时时间
	 */
	public static int HTTP_TIMEOUT = 1000 * 30;

	public static int SOFT = 1;

	public static String UA = "Mozilla/5.0 (Windows NT 5.1; rv:5.0) Gecko/20100101 Firefox/5.0";

	public final static class Session {

		public static boolean IS_LOGIN = false;
		public static boolean IS_DOWNLOADING = false;

	}

	public static int NomarlRoadSpeedGate = 20;

	public static Map allbrand = null;

	public static double lat = 39.914714;

	public static double lng = 116.403119;

	// 以下省市信息均为定位信息，用户的地区信息请到loginmanager内查询
	public static String city = "";

	public static String province = "";

	public static String cityCode = "";

	public static String provinceCode = "";

	/** 接口鉴权的KEY */
	public final static String KEY = "A9b8C7d6KUIJHFDNjhdiosfHIkljmklJKJklIOWERjkljk890934sdfsjLjksff";

	/**
	 * 头像图片压缩长宽
	 */
	public final static int head_img_length = 400;

	/** 默认签名 */
	public final static String DEFAULT_SIGNATURE = "就是这么任性，就是什么也不写";

	/** 车辆方向 */
	public static float degree = 0;

	/** 首页是否刷新 */
	public static boolean isHomeRefresh = true;

	/** 微信分享 正式 wxe00fbb673e49d88b 2cbdd7fdc39c1d132429562ad2bd2fa3 */
	/** 微信分享 zwj wxb0ab069ace8c0237 ee47d4e62bbf04f22927509e5ef4398e */
	public static String shareID = "wxe00fbb673e49d88b";
	public static String shareSecret = "2cbdd7fdc39c1d132429562ad2bd2fa3";
}
