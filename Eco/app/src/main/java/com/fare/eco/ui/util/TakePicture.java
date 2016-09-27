package com.fare.eco.ui.util;

import java.io.File;
import com.fare.eco.config.Constants;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;

/**
 * 从相册或拍照获取图片的工具类
 * TODO(avoid memory leak) 单例模式持有Activity的引用,容易引起内存泄露
 * @author Xiao_V
 * @since 2015/7/8
 *
 */
public class TakePicture {

	/** 拍照还是相册获取图片 */
	private int flag = Constants.ALBUM;
	/** 默认路径 */
	private String defaultFilePath = null;
	/** 图片的uri */
	private Uri uri;
	/** 单例 */
	static TakePicture instance = null;
	/** 同步锁 */
	private static Object obj = new Object();

	private TakePicture() {
		
	}

	/** 获取实例 */
	public static TakePicture getInstance() {
		synchronized (obj) {
			if (instance == null) {
				instance = new TakePicture();
			}
		}
		return instance;
	}

	/** TODO 此方法过于2B,后期要修改整体方案 */
	public static void destroyInstance() {
		instance = null;
	}

	/**
	 * 相机获取图片 defaultFilePath拍照保存的路径
	 * 
	 * @param activity
	 * @param requestCode
	 *            请求code
	 */
	public void takePictureFromCamare(Activity activity, int requestCode) {
		defaultFilePath = Constants.CAMERA_SAVE_DIR + "/" + FileUtil.getUniqueName(".jpeg", "pic");
		flag = Constants.CAMERA;
		takePictureFromCamare(activity, defaultFilePath, requestCode);
	}

	/**
	 * 图库获取图片
	 * 
	 * @param activity
	 * @param requestCode
	 *            请求code
	 */
	public void takePictureFromAlbum(Activity activity, int requestCode) {
		try {
			Intent intent = new Intent();
			
			// getImagePath时不需要判断版本,通用
			intent.setAction(Intent.ACTION_PICK); // 选择数据
			intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			
//			if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
//				intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
//			} else {
//				intent.setAction(Intent.ACTION_GET_CONTENT); // 弹出一个窗口,让用户选择一种数据(默认选择image)
//			}
//			intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");

			activity.startActivityForResult(intent, requestCode);
		} catch (ActivityNotFoundException e) {
			ShowUtil.showToast(activity, "找不到可用相册");
		}
	}

	/**
	 * 相机获取图片
	 * 
	 * @param activity
	 * @param filePath 图片保存文件路径
	 * @param requestCode
	 *            请求code
	 */
	public void takePictureFromCamare(Activity activity, String filePath, int requestCode) {
		if (!checkDir()) {
			return;
		}
		if (TextUtils.isEmpty(filePath)) {
			defaultFilePath = filePath = Constants.CAMERA_SAVE_DIR + "/" + FileUtil.getUniqueName(".jpeg", "pic");
		}
		flag = Constants.CAMERA;;
		try {
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			File file = new File(filePath); // 保存拍照的照片
			uri = Uri.fromFile(file);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
			activity.startActivityForResult(intent, requestCode);
		} catch (ActivityNotFoundException e) {
			ShowUtil.showToast(activity, "找不到可用相机");
		}
	}

	/**
	 * 裁剪图片（调用系统裁剪图片）
	 */
	public void cropImage(Activity activity, int width, int height,
			int requestCode) {
		cropImage(activity, uri, width, height, requestCode);
	}

	/**
	 * 裁剪图片（调用系统裁剪图片）
	 */
	public void cropImage(Activity activity, Uri uri, int width, int height,
			int requestCode) {
		if (uri == null) {
			return;
		}
		this.uri = uri;
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "false");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", width);
		intent.putExtra("outputY", height);
		intent.putExtra("return-data", true);
		activity.startActivityForResult(intent, requestCode);
	}

	/**
	 * 获取图片的路径
	 * @return
	 */
	public String getImagePath() {
		return defaultFilePath;
	}

	/**
	 * 获取图片路径的URI
	 * @return
	 */
	public Uri getImageUri() {
		return uri;
	}

	public int getType() {
		return flag;
	}

	private boolean checkDir() {
	    FileUtil.ensureAppPath(Constants.IMAGE_CACHE_DIR);
        FileUtil.ensureAppPath(Constants.IMAGE_CACHE_THUMB_DIR);
        return true;
	}

}
