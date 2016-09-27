package com.fare.eco.config;

import android.os.Environment;

/************************************************************************************************
 * 文件名称: FusionCode.java 文件描述: 定义常量值 作 者： ZhuYujie 创建时间: 2011-12-14上午11:03:57 备
 * 注:
 ************************************************************************************************/
public class FusionCode {
	/**
	 * 获取JSONObject
	 */
	public static final int RETURN_JSONOBJECT = 300;

	/**
	 * 取得单张图片
	 */
	public static final int RETURN_BITMAP = 301;

	/**
	 * http 返回xml格式
	 */
	public static final int RETURN_XML = 302;

	/**
	 * http 返回string格式
	 */
	public static final int RETURN_GETBYTES = 303;

	/**
	 * 网络连接错误
	 */
	public static final int NETWORK_ERROR = 310;

	/**
	 * 网络连接超时
	 */
	public static final int NETWORK_TIMEOUT_ERROR = 311;

	/**
	 * 用于刷新页面的通知
	 */
	public static final int REFRESH_PAGE = 312;

	/**
	 * 升级提示状态吗
	 */
	public static final int UPDATE_CODE = 320;

	/**
	 * 启动加载完成
	 */
	public static final int LOADING_FINISH = 321;

	/**
	 * 强制升级
	 */
	public static final int FOURCE_UPDATE = 322;

	public static final int HOME_PAGE = 323;

	/**
	 * 升级检测完成的状态码
	 */
	public static final int UPDATE_FINISH = 324;

	/**
	 * 网络超时时间
	 */
	public final static int MAX_CONNECTION_TIMEOUT = 30000;

	/**
	 * 发退出程序的广播标示
	 */
	public final static String ACTION_FINISH_SELF = "gsmcc_finish";

	/**
	 * 设备宽度
	 */
	public static int deviceWidth = 0;
	public static int deviceHeight = 0;

	public final static int SCREEN_HHH = 5;
	public final static int SCREEN_HH = 4;
	public final static int SCREEN_H = 3;
	public final static int SCREEN_M = 2;
	public final static int SCREEN_L = 1;

	public static String ECMC_ROOT_PATH = "/data/data/com.onezerooneone.snailCommune/";
	public static String ECMC_FILE_PATH = "/data/data/com.onezerooneone.snailCommune/files/";
	public static String ECMC_DB_PATH = "/data/data/com.onezerooneone.snailCommune/databases/";
	public static String SDCARD_FILE_PATH = Environment
			.getExternalStorageDirectory() + "/onezerooneone/snailCommune";

	public final static String ECMC_WEBVIEW_CACH_PATH = "/data/data/com.onezerooneone.snailCommune/cache/webviewCache/";

	public final static String TERMINAL = "android";
	public final static String DATA_BYTE = "UTF-8";

	public final static String XML_HEAD = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

	public final static String HTTP_CONTENT_TYPE_OCTET_STREAM = "application/octet-stream";
	public final static String HTTP_CONTENT_TYPE_TEXT_PLANE = "text/plain; charset=utf-8";
	public final static String HTTP_CONTENT_TYPE_FORM = "application/x-www-form-urlencoded";

	public final static String DB_NAME = "lanecar.db";

	public final static int DB_VERSION = 1;

	/**
	 * 搜索操作的ID
	 */
	public final static int SEARCH_ID = 998;
	/**
	 * webview 页面的ID
	 */
	public final static int WEBVIEW_ID = 888;

	/**
	 * 启动gif 动画的状态码
	 */
	public final static int SHOW_GIFVIEW = 1;
	/**
	 * 关闭gif动画的状态码
	 */
	public final static int CLOSE_GIFVIEW = 1 << 1;

	/**
	 * 分页大小
	 */
	public final static int pageSize = 100;

	/**
	 * 图片下载成功刷新界面
	 */
	public static final int DOWNLOAD_IMAGE_FINISH = 11111;

}
