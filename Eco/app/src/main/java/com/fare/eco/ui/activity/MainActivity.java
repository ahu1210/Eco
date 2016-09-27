package com.fare.eco.ui.activity;

import com.fare.eco.model.FragmentCallback;
import com.fare.eco.ui.R;
import com.fare.eco.ui.fragment.HomePageFragment;
import com.fare.eco.ui.fragment.MoreFragment;
import com.fare.eco.ui.fragment.NavigationDrawerFragment;
import com.fare.eco.ui.fragment.NavigationDrawerFragment.OnDrawerStateChangedListener;
import com.fare.eco.ui.fragment.NearFragment;
import com.fare.eco.ui.fragment.UserFragment;
import com.fare.eco.ui.util.BitmapUtils;
import com.fare.eco.ui.util.FragmentUtils;
import com.fare.eco.ui.util.ShowUtil;
import com.fare.eco.ui.view.HP_bottomView;
import com.fare.eco.ui.view.TitleBarLayout;
import com.fare.eco.ui.view.HP_bottomView.OnTabChangeListener;
import com.fare.eco.ui.view.TitleBarLayout.OnTitleBarLeftClickListener;
import com.fare.eco.ui.view.TitleBarLayout.OnTitleBarRightClickListener;
import com.fare.eco.ui.view.TitleBarLayout.RightAction;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * 主界面
 * @author Xiao_V
 * @since 2015/6/1
 *
 */
public class MainActivity extends FragmentActivity implements OnTabChangeListener, FragmentCallback, OnTitleBarRightClickListener {

	/** 抽屉的布局 */
	private NavigationDrawerFragment mDrawerFragment;
	private FragmentManager mFragmentManager;
	private Fragment mCurrentFragment;
	/** 底部栏 */
	private HP_bottomView mBottomView;
	private TitleBarLayout titleBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_topdrawer);
		setTitleBar();
		setBottomView();
	}

	@Override
	protected void onStart() {
		super.onStart();
		showAvater(R.drawable.a03); // 头像
	}

	@Override
	protected void onResume() {
		super.onResume();
		setDrawerLayout();
	}

	private void setTitleBar() {
		titleBar = (TitleBarLayout) findViewById(R.id.titleBar);
		titleBar.setTitle(getStringResources(R.string.bottom_homepage));
		titleBar.setRightAction(RightAction.ICON, R.drawable.location_img);
	}

	private void setDrawerLayout() {
		mDrawerFragment = (NavigationDrawerFragment) getFragmentManager().findFragmentById(R.id.fragment_drawer);
		mDrawerFragment.setDrawerLayoutId(R.id.drawerLayout);
		mDrawerFragment.setOnDrawerStateChangedListener(new OnDrawerStateChangedListener() {
			
			@Override
			public void onDrawerIdle() {
				// 闲置 
			}
			
			@Override
			public void onDrawerDragging() {
				// 拖动中
			}

			@Override
			public void onDrawerSettle() {
				// 抽屉打开的过程中,不drag
			}
		});
		mDrawerFragment.initDrawerLayout();
	}

	private void setBottomView() {
		mFragmentManager = getSupportFragmentManager();

		mBottomView = (HP_bottomView) findViewById(R.id.view_bottom);
		mBottomView.setOnTabChangeListener(this);
		mBottomView.setCurrentTab(0); // 回调tabChangeListener
		replaceFragment(HomePageFragment.class);
	}

	private void showAvater(int resId) {
		ImageView imageView = new ImageView(this);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(105, 105);
		imageView.setLayoutParams(params);
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resId);
		imageView.setImageBitmap(BitmapUtils.circleBitmap(bitmap));
		titleBar.setLeftCustomView(imageView);

		/** 头像的点击事件 */
		titleBar.setOnTitleBarLeftClickListener(new OnTitleBarLeftClickListener() {

			@Override
			public void onLeftClickListener(View view) {
				if (mDrawerFragment.isDrawerOpen()) {
					mDrawerFragment.closeDrawer();
				} else {
					mDrawerFragment.openDrawer();
				}
			}
		});
	}

	/** activity中实现,在fragment中通过dispatchCommand回调 */
	@Override
	public void onFragmentCallback(Fragment fragment, int id, Bundle args) {
		// TODO id定义为常量
		if (id == 250438) {
			mBottomView.setCurrentTab(0);
			ShowUtil.showToast(this, "跳转到第一个fragment");
		} else if (id == 540) {
			mBottomView.setCurrentTab(1);
			ShowUtil.showToast(this, "跳转到第2个fragment");
		}
	}

	@Override
	public void onTabChange(String tag) {
		if (titleBar == null) {
			titleBar = (TitleBarLayout) findViewById(R.id.titleBar);
		}
		if (tag != null) {
			if (getStringResources(R.string.bottom_homepage).equals(tag)) { // 首页
				replaceFragment(HomePageFragment.class);
				titleBar.setTitle(getStringResources(R.string.bottom_homepage));
				titleBar.setRightAction(RightAction.ICON, R.drawable.location_img);
			} else if (getStringResources(R.string.bottom_near).equals(tag)) { // 附近
				replaceFragment(NearFragment.class);
				titleBar.setTitle(getStringResources(R.string.bottom_near));
				titleBar.setRightAction(RightAction.TEXT, R.string.bottom_near);
			} else if (getStringResources(R.string.bottom_user).equals(tag)) { // 我的
				replaceFragment(UserFragment.class);
				titleBar.setTitle(getStringResources(R.string.bottom_user));
				titleBar.setRightAction(RightAction.TEXT, R.string.bottom_user);
			} else if (getStringResources(R.string.bottom_more).equals(tag)) { // 更多
				replaceFragment(MoreFragment.class);
				titleBar.setTitle(getStringResources(R.string.bottom_more));
				titleBar.setRightAction(RightAction.TEXT, R.string.bottom_more);
			}
			titleBar.setOnTitleBarRightClickListener(this);
		}
	}

	@Override
	public void onRightClickListener(View view) {
		String title = titleBar.getTitleTextView().getText().toString();
		if (getStringResources(R.string.bottom_homepage).equals(title)) {
			Intent intent = new Intent(MainActivity.this, LocationActivity.class);
			startActivity(intent);
		} else if (getStringResources(R.string.bottom_near).equals(title)){
			ShowUtil.showToast(MainActivity.this, title);
		} else if (getStringResources(R.string.bottom_user).equals(title)){
			ShowUtil.showToast(MainActivity.this, title);
		} else if (getStringResources(R.string.bottom_more).equals(title)){
			ShowUtil.showToast(MainActivity.this, title);
		}
	}

	private String getStringResources(int resId) {
		return this.getResources().getString(resId);
	}

	/** 替换fragment,如需数据传递可新加参数 */
	private void replaceFragment(Class<? extends Fragment> newFragment) {
		mCurrentFragment = FragmentUtils.switchFragment(mFragmentManager, R.id.container, mCurrentFragment, newFragment, null, false);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	/** 双击退出 */
	private long isExit = 0;
	@Override
	public void onBackPressed() {
		if (mDrawerFragment.isDrawerOpen()) {
			mDrawerFragment.closeDrawer();
		} else {
			long currentTime = System.currentTimeMillis();
			if ((currentTime - isExit) >= 2000) {
				ShowUtil.showToast(this, "再按一次退出程序");
				isExit = currentTime;
			} else {
				finish();
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.test, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_search:
			Intent intent = new Intent(this, LocationActivity.class);
			startActivity(intent);
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mFragmentManager = null;
	}
}