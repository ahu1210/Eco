package com.fare.eco.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.fare.eco.config.Constants;
import com.fare.eco.config.FusionCode;
import com.fare.eco.manager.http.CookieManager;
import com.fare.eco.model.City;
import com.fare.eco.model.Province;
import com.fare.eco.ui.view.CustomLoadingDialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;

@SuppressLint("SimpleDateFormat")
public abstract class BaseActivity extends Activity  {

	protected final boolean DEBUG = true;
	
	private CustomLoadingDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	/**
	 * 退出程序
	 */
	@SuppressWarnings("deprecation")
	public void exit() {
		// 重置标记位，并清空Cookie
		// resetSessionData();
		CookieManager.getInstance().removeSessionCookie();
		try {
			NotificationManager nm = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
			nm.cancelAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
		int apiLevel = -1;
		try {
			apiLevel = Integer.parseInt(android.os.Build.VERSION.SDK);
		} catch (NumberFormatException e) {
		}
		// android2.2以下版本
		if (apiLevel > 2 && apiLevel < 8) {
			final String name = getPackageName();
			ActivityManager activityManager = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
			activityManager.restartPackage(name);
		} else {
			getApplicationContext().sendBroadcast(new Intent(FusionCode.ACTION_FINISH_SELF));
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 启动app 应用的入口
	 * 
	 * @param packName
	 */
	public void startAPP(String packName) {
		try {
			PackageManager packageManager = this.getPackageManager();
			Intent intent = new Intent();
			intent = packageManager.getLaunchIntentForPackage(packName);
			startActivity(intent);
		} catch (Exception e) {
			Log.e("startAPP", "e=" + e.getMessage());
		}
	}

	/**
	 * 自定义加载dialog
	 * @param content dialog提示
	 */
	protected void showLoadingDialog(String content, boolean isTouchCancelable) {
		if (dialog == null) {
			dialog = new CustomLoadingDialog(this, content);
		}
		dialog.setCanceledOnTouchOutside(isTouchCancelable);
		dialog.show();
	}

	/** 关闭dialog 不需要判断 isShowing()*/
	protected void closeLoadingDialog() {
		if (dialog != null) {
			dialog.dismiss();
		}
	}

	/** 获取所有省份 */
	public ArrayList<Province> getAllProvince() {
		ArrayList<Province> modelsList = new ArrayList<Province>();
		try {
			InputStream input = this.getResources().getAssets().open("province.xml");
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(input);
			Element rootElement = doc.getDocumentElement();
			NodeList items = rootElement.getElementsByTagName("ROW");
			for (int i = 0; i < items.getLength(); i++) {
				Province models = new Province();
				Node item = items.item(i);
				NodeList properties = item.getChildNodes();
				String nbid = "";
				for (int j = 0; j < properties.getLength(); j++) {
					Node property = properties.item(j);
					String nodeName = property.getNodeName();
					if (nodeName.equals("PROVINCEID")) {
						nbid = property.getFirstChild().getNodeValue();
						models.setId(nbid);
					} else if (nodeName.equals("PROVNAME")) {
						models.setName(property.getFirstChild().getNodeValue());
					}
				}
				modelsList.add(models);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return modelsList;
	}

	/**
	 * 获取给定省的城市
	 * @param rpid 省的id,由getAllProvince获取
	 * @return
	 */
	public ArrayList<City> getCity(String rpid) {
		ArrayList<City> modelsList = new ArrayList<City>();
		try {
			InputStream input = this.getResources().getAssets().open("city.xml");
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(input);
			Element rootElement = doc.getDocumentElement();
			NodeList items = rootElement.getElementsByTagName("ROW");
			for (int i = 0; i < items.getLength(); i++) {
				City models = new City();
				Node item = items.item(i);
				NodeList properties = item.getChildNodes();
				String nbid = "";
				for (int j = 0; j < properties.getLength(); j++) {
					Node property = properties.item(j);
					String nodeName = property.getNodeName();
					if (nodeName.equals("PROVINCEID")) {
						String pid = property.getFirstChild().getNodeValue();
						if (!pid.equals(rpid)) {
							nbid = null;
							break;
						}
					} else if (nodeName.equals("CITYID")) {
						nbid = property.getFirstChild().getNodeValue();
						models.setId(nbid);
					} else if (nodeName.equals("CNAME")) {
						models.setName(property.getFirstChild().getNodeValue());
					}
				}
				if (nbid != null)
					modelsList.add(models);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return modelsList;
	}

	/** 获取和保存当前屏幕的截图,返回保存的路径 */
	@SuppressWarnings("deprecation")
	public String getScreenImage(Context mContext) {
		// 构建Bitmap
		int w = this.getWindowManager().getDefaultDisplay().getWidth();
		int h = this.getWindowManager().getDefaultDisplay().getHeight();
		Bitmap Bmp = Bitmap.createBitmap(w, h, Config.ARGB_8888);
		// 获取屏幕
		View decorview = this.getWindow().getDecorView();
		decorview.setDrawingCacheEnabled(true);
		Bmp = decorview.getDrawingCache();
		// 图片存储路径
		String SavePath = Constants.APP_ROOT + "/Screen";
		// 保存Bitmap
		try {
			File path = new File(SavePath);
			// 文件
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			String filepath = SavePath + "/Screen_" + sdf.format(new Date()) + ".png";
			File file = new File(filepath);
			if (!path.exists()) {
				path.mkdirs();
			}
			if (!file.exists()) {
				file.createNewFile();
			}
			FileOutputStream fos = null;
			fos = new FileOutputStream(file);
			if (null != fos) {
				Bmp.compress(Bitmap.CompressFormat.PNG, 90, fos);
				fos.flush();
				fos.close();
			}
			return filepath;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
}