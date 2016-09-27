package com.fare.eco.ui.util;

import com.fare.eco.ui.R;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ShowUtil {
	
	private static Toast mToast;
	private static Handler mHandler = new Handler();
	private static Runnable toastRun = new Runnable() {
		public void run() {
			mToast.cancel();
			mToast = null;// toast隐藏后，将其置为null
		}
	};

	/**
	 * 自定义Toast (建议字数小于20，如果大于20请修改布局)
	 * @param context
	 * @param message
	 */
	@SuppressLint("InflateParams")
	public static void showToast(Context context, String message) {
		if (TextUtils.isEmpty(message)) {
			return;
		}
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.custom_toast, null);// 自定义布局
		TextView text = (TextView) view.findViewById(R.id.toast_message);// 显示的提示文字
		text.setText(message);
		mHandler.removeCallbacks(toastRun);
		if (mToast == null) {// 只有mToast==null时才重新创建，否则只需更改提示文字
			mToast = new Toast(context);
		}
		mToast.setDuration(Toast.LENGTH_LONG);
		mToast.setGravity(Gravity.BOTTOM, 0, 250);
		mToast.setView(view);
		mHandler.postDelayed(toastRun, 3000);// 延迟3秒隐藏toast
		mToast.show();
	}
	
	public static void showToast(Context context, int messageId) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.custom_toast, null);// 自定义布局
		TextView text = (TextView) view.findViewById(R.id.toast_message);// 显示的提示文字
		text.setText(messageId);
		mHandler.removeCallbacks(toastRun);
		if (mToast == null) {// 只有mToast==null时才重新创建，否则只需更改提示文字
			mToast = new Toast(context);
		}
		mToast.setDuration(Toast.LENGTH_LONG);
		mToast.setGravity(Gravity.BOTTOM, 0, 180);
		mToast.setView(view);
		mHandler.postDelayed(toastRun, 3000);// 延迟3秒隐藏toast
		mToast.show();
	}
	
	public static void showLog(String tag, boolean debug, String msg, String flag) {
		if (!debug) {
			return;
		}
		switch (flag) {
		case "i": // info
			Log.i(tag, msg);
			break;
		case "e": // error
			Log.e(tag, msg);
			break;
		case "v": // verbose
			Log.v(tag, msg);
			break;
		case "w": // warn
			Log.w(tag, msg);
			break;
		case "d": // debug
			Log.d(tag, msg);
			break;

		default:
			Log.i(tag, msg);
			break;
		}
	}

    /**
     * Dialog
     * @param context
     * @param iconId
     * @param title
     * @param message
     * @param positiveBtnName
     * @param negativeBtnName
     * @param positiveBtnListener
     * @param negativeBtnListener
     * @return
     */
    public static Dialog createConfirmDialog(Context context,int iconId, String title, String message,
            String positiveBtnName,String negativeBtnName,
            android.content.DialogInterface.OnClickListener positiveBtnListener,
            android.content.DialogInterface.OnClickListener negativeBtnListener){
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setIcon(iconId);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(positiveBtnName, positiveBtnListener);
        builder.setNegativeButton(negativeBtnName, negativeBtnListener);
        dialog = builder.create();
        return dialog;
    }
    
    public void showDialog(Context context) {
		AlertDialog.Builder builder = new Builder(context);
		builder.setTitle("no zuo no die");
		builder.setIcon(R.drawable.filter);
		builder.setSingleChoiceItems(new String[] { "好的", "good", "牛B", "cow --" }, 0, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		AlertDialog create = builder.create();
		create.setCancelable(false);
		create.show();
	}

}
