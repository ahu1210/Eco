package com.fare.eco.ui.view;

import android.app.Dialog;
import com.fare.eco.ui.R;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 自定义dialog,用于加载界面提示
 * @author Xiao_V
 * @since 2015/7/15
 */
public class CustomLoadingDialog extends Dialog {

	private AnimationDrawable ad;
	private String content;

	public CustomLoadingDialog(Context context, String content) {
		super(context, R.style.loadingDialogStyle);
		this.content = content;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_loading);
		TextView tv = (TextView) findViewById(R.id.tv);
		ImageView imageView = (ImageView) findViewById(R.id.progressBar1);
		ad = (AnimationDrawable) imageView.getBackground();
		tv.setText(content);
		if (ad != null) {
			ad.start();
		}
		LinearLayout dialogContent = (LinearLayout) this.findViewById(R.id.LinearLayout);
		dialogContent.getBackground().setAlpha(165); // 透明度
	}
	
	@Override
	public void dismiss() {
		super.dismiss();
		if (ad != null && ad.isRunning()) {
			ad.stop();
		}
	}
}
