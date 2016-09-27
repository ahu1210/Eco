package com.fare.eco.config;

import android.os.Environment;

public class Constants {
	
	/** 修改头像的广播action */
	public static final String CHANGE_AVATER_ACION = "com.fare.eco.chageAvater";
	
	/** SharedPreferences key for get ip and port */
    public final static String SHARE_NAME_IP = "ip&port";
    /** SharedPreferences key for titleBar color */
    public final static String SHARE_NAME_TITLEBAR_COLOR = "titleBarColor";

	public static final String SDCARD_PATH;
    public static final String SYS_DATA_PATH;
    public static final String APP_NAME = "Fare";
    public final static String SHARE_NAME_SPLASH = "splash";



    /** 内部存储 */
    public static String ECMC_ROOT_PATH = "/data/data/com.fare.taomi/";
	public static String ECMC_FILE_PATH = "/data/data/com.fare.taomi/files/";
	public static String ECMC_DB_PATH = "/data/data/com.fare.taomi/databases/";

    static {
        SDCARD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
        SYS_DATA_PATH = Environment.getDataDirectory().getAbsolutePath();
    }
    /** App root(sd卡下appName文件夹) */
    public static final String APP_ROOT = SDCARD_PATH + "/" + APP_NAME;
    /** Log path */
    public static final String LOG_PATH_DIR = APP_ROOT + "/Log/";
    /** 图片的保存路径(用户手动保存) */
    public final static String IMAGE_SAVE_DIR = APP_ROOT + "/save";
    /** camera图片 */
    public final static String CAMERA_SAVE_DIR = IMAGE_SAVE_DIR + "/camera";
    /** 图片的缓存路径 */
    public final static String IMAGE_CACHE_DIR = APP_ROOT + "/imageCache";
    /** 缩略图 Thumbnail */
    public final static String IMAGE_CACHE_THUMB_DIR = IMAGE_CACHE_DIR + "/thumbnail";
    
    public static final int ALBUM = 1;// 本地图片
    public static final int CAMERA = 2;// 拍照
    
    /** 授权 */
	public static final String SECRET_KEY = "b359ad4f-1a27-4dd6-ad74-4d2bfc3d8fae";

	public static final String[] path = {"http://g.hiphotos.baidu.com/image/pic/item/95eef01f3a292df526c5563fbe315c6034a87392.jpg",
		"http://b.hiphotos.baidu.com/image/pic/item/f9dcd100baa1cd116b52ed29bb12c8fcc2ce2dd6.jpg",
		"http://e.hiphotos.baidu.com/image/pic/item/bf096b63f6246b6090ea2d7de9f81a4c500fa2d7.jpg",
		"http://a.hiphotos.baidu.com/image/pic/item/42166d224f4a20a48e4d79cf92529822720ed063.jpg",
		"http://pic.wenwen.soso.com/p/20090123/20090123170106-1490076742.jpg",
		"http://b.hiphotos.baidu.com/image/pic/item/f9dcd100baa1cd116b52ed29bb12c8fcc2ce2dd6.jpg",
		"http://e.hiphotos.baidu.com/image/pic/item/bf096b63f6246b6090ea2d7de9f81a4c500fa2d7.jpg",
		"http://a.hiphotos.baidu.com/image/pic/item/42166d224f4a20a48e4d79cf92529822720ed063.jpg",
		"http://pic.wenwen.soso.com/p/20090123/20090123170106-1490076742.jpg",
		"http://b.hiphotos.baidu.com/image/pic/item/f9dcd100baa1cd116b52ed29bb12c8fcc2ce2dd6.jpg",
		"http://e.hiphotos.baidu.com/image/pic/item/bf096b63f6246b6090ea2d7de9f81a4c500fa2d7.jpg",
		"http://a.hiphotos.baidu.com/image/pic/item/42166d224f4a20a48e4d79cf92529822720ed063.jpg",
		"http://pic.wenwen.soso.com/p/20090123/20090123170106-1490076742.jpg",
		};
}
