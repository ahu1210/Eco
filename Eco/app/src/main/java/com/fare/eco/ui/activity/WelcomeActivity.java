package com.fare.eco.ui.activity;

import java.util.ArrayList;

import com.fare.eco.config.Constants;
import com.fare.eco.ui.BaseActivity;
import com.fare.eco.ui.R;
import com.fare.eco.ui.adapter.ViewPaperAdapter;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * 引导页
 * @since 2015/9/23
 * @author Xiao_V
 *
 */
public class WelcomeActivity extends BaseActivity implements OnPageChangeListener, OnClickListener {
	private ViewPager viewPager;
	private PagerAdapter pagerAdapter;
	private Button startButton;
	private LinearLayout indicatorLayout;
	private ArrayList<View> views;
	private ImageView[] indicators = null;
	private int[] images;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_welcome);
		images = new int[] { R.drawable.welcome_01, R.drawable.welcome_02, R.drawable.welcome_03, R.drawable.welcome_04 };
		initView();
		// new CreateDesktopShortcut(this); 创建桌面快捷方式
	}

	// 初始化视图
	private void initView() {
		viewPager = (ViewPager) findViewById(R.id.viewpage);
		startButton = (Button) findViewById(R.id.start_Button);
		startButton.setOnClickListener(this);
		indicatorLayout = (LinearLayout) findViewById(R.id.indicator);

		views = new ArrayList<View>();
		indicators = new ImageView[images.length]; // 定义指示器数组大小
		for (int i = 0; i < images.length; i++) {
			// 循环加入图片
			ImageView imageView = new ImageView(this);
			imageView.setBackgroundResource(images[i]);
			views.add(imageView);
			// 循环加入指示器
			indicators[i] = new ImageView(this);
			indicators[i].setBackgroundResource(R.drawable.ic_viewpager_noselect);
			if (i == 0) {
				indicators[i].setBackgroundResource(R.drawable.ic_viewpager_select);
			}
			indicatorLayout.addView(indicators[i]);
		}
		pagerAdapter = new ViewPaperAdapter(views);
		viewPager.setAdapter(pagerAdapter);
		viewPager.setOnPageChangeListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.start_Button) {
			startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
			this.finish();
			SharedPreferences shared = getSharedPreferences(Constants.SHARE_NAME_SPLASH, Context.MODE_PRIVATE);
			Editor editor = shared.edit();
			editor.putBoolean("isFirstLaunch", false);
			editor.commit();
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int arg0) {
		// 显示最后一个图片时显示按钮
		if (arg0 == indicators.length - 1) {
			startButton.setVisibility(View.VISIBLE);
		} else {
			startButton.setVisibility(View.INVISIBLE);
		}
		// 更改指示器图片
		for (int i = 0; i < indicators.length; i++) {
			indicators[arg0].setBackgroundResource(R.drawable.ic_viewpager_select);
			if (arg0 != i) {
				indicators[i].setBackgroundResource(R.drawable.ic_viewpager_noselect);
			}
		}
	}
}
