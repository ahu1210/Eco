package com.fare.eco.ui.util;

import com.fare.eco.ui.R;
import com.fare.eco.ui.activity.MainActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcelable;

/**
 * 创建桌面快捷方式(作用不大)
 * @author Xiao_V
 *
 */
public class CreateDesktopShortcut {
	
	public CreateDesktopShortcut(Activity activity) {
		// intent进行隐式跳转,到桌面创建快捷方式
		Intent addIntent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
		// 不允许重建
		addIntent.putExtra("duplicate", false);
		// 设置快捷方式的名称
		String title = activity.getResources().getString(R.string.app_name);
		addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, title);
		// 设置快捷方式的图标
		Parcelable icon = Intent.ShortcutIconResource.fromContext(activity, R.drawable.ic_launcher);
		addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
		// 设置快捷方式的意图
		Intent myIntent = new Intent(activity, MainActivity.class);
		myIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, myIntent);
		// 发送广播
		activity.sendBroadcast(addIntent);
	}
}
