package com.fare.eco.ui.activity;

import java.util.List;

import com.fare.eco.model.HpListProduct;
import com.fare.eco.ui.BaseActivity;
import com.fare.eco.ui.R;
import com.fare.eco.ui.util.ShowUtil;
import com.fare.eco.ui.view.TitleBarLayout;
import com.fare.eco.ui.view.TitleBarLayout.LeftAction;
import com.fare.eco.ui.view.TitleBarLayout.OnTitleBarLeftClickListener;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class AboutActivity extends BaseActivity implements View.OnClickListener{

	private static final String TAG = "AboutActivity";
	private static final boolean DEBUG = true;

	private TitleBarLayout titleBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);

		titleBar = (TitleBarLayout) findViewById(R.id.titleBar);
		titleBar.setTitle("关于淘米科技");
		titleBar.setLeftAction(LeftAction.BACK);

		titleBar.setOnTitleBarLeftClickListener(new OnTitleBarLeftClickListener() {

			@Override
			public void onLeftClickListener(View view) {
				finish();
				overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
			}
		});
		showLoadingDialog("正在加载...", false);
		
		List<HpListProduct> products = (List<HpListProduct>) getIntent().getSerializableExtra("products");
		if (products == null || products.isEmpty()) {
			if (DEBUG) Log.e(TAG, "products is null or empty");
		} else {
			String name = products.get(0).getName();
			name = (name == null) ? "来自其他类的跳转" : name;
			ShowUtil.showToast(this, "name = " + name);
		}

		setListener();
	}

	private void setListener() {
		findViewById(R.id.layout_official_website).setOnClickListener(this);
		findViewById(R.id.layout_rate_app).setOnClickListener(this);
		findViewById(R.id.layout_help).setOnClickListener(this);
		findViewById(R.id.layout_feedback).setOnClickListener(this);
		findViewById(R.id.layout_update).setOnClickListener(this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
		overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_official_website:
			ShowUtil.showToast(this, R.string.text_official_website);
			showLoadingDialog("正在加载...", false);
			break;
		case R.id.layout_rate_app:
			ShowUtil.showToast(this, R.string.text_rate_app);
			break;
		case R.id.layout_help:
			ShowUtil.showToast(this, R.string.text_about_help);
			break;
		case R.id.layout_feedback:
			ShowUtil.showToast(this, R.string.text_about_feedback);
			break;
		case R.id.layout_update:
			ShowUtil.showToast(this, R.string.text_about_update);
			break;

		default:
			break;
		}
	}
}
