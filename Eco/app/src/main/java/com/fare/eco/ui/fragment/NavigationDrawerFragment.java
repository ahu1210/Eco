package com.fare.eco.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import com.fare.eco.externalLibrary.eventBus.EventBus;
import com.fare.eco.model.DrawerLayoutInfo;
import com.fare.eco.ui.R;
import com.fare.eco.ui.activity.AboutActivity;
import com.fare.eco.ui.activity.SelectCityActivity;
import com.fare.eco.ui.activity.mine.UserInfoActivity;
import com.fare.eco.ui.adapter.DrawerlayoutAdapter;
import com.fare.eco.ui.util.ShowUtil;
import com.fare.eco.ui.view.widget.CircleImageView;
import com.nineoldandroids.view.ViewHelper;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 抽屉布局:1.实例化抽屉的布局(NavigationDrawerFragment) 2.setLayoutId  3.initDrawerLayout
 * @author Xiao_V
 * @since 20/7/2
 *
 */
public class NavigationDrawerFragment extends Fragment implements OnItemClickListener {

	private String[] itemName_arr = { "我的团购", "我的好友", "旋转木马", "设置中心" };
	/** drawerLayout的id */
	private int drawerLayoutId;
	private DrawerLayout mDrawerLayout;
	/** 抽屉布局的listView */
	private ListView mDrawerList;
	/** 头像 */
	private CircleImageView userAvater;

	private TextView current_city;

	public interface OnDrawerStateChangedListener {
		void onDrawerIdle();

		void onDrawerDragging();

		void onDrawerSettle();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_drawer, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mDrawerList = (ListView) view.findViewById(R.id.drawerList);
		userAvater = (CircleImageView) view.findViewById(R.id.userAvater);
		current_city = (TextView) view.findViewById(R.id.current_city);

		DrawerlayoutAdapter adapter = new DrawerlayoutAdapter(getActivity(), getMenu());
		mDrawerList.setAdapter(adapter);
		mDrawerList.setOnItemClickListener(this);

		EventBus.getDefault().register(this); // 注册eventbus

		userAvater.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), UserInfoActivity.class);
				startActivity(intent);
			}
		});

		view.findViewById(R.id.select_city).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getActivity(), SelectCityActivity.class));
			}
		});
	}

	public void onEventMainThread(String result) {
		current_city.setText(result);
	}

	/** 初始化抽屉(实例化,动画效果,开关抽屉时的事件处理) */
	public void initDrawerLayout() {
		if (drawerLayoutId <= 0) {
			return;
		}
		mDrawerLayout = (DrawerLayout) getActivity().findViewById(drawerLayoutId);
		mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.RIGHT); // 右侧抽屉的模式(始终关闭)
		initEvents();
	}

	public int getDrawerLayoutId() {
		return drawerLayoutId;
	}

	public void setDrawerLayoutId(int drawerLayoutId) {
		this.drawerLayoutId = drawerLayoutId;
	}

	/** 判断抽屉是否打开 */
	public boolean isDrawerOpen() {
		return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(Gravity.LEFT);
	}

	/** 打开抽屉,先调用isDrawerOpen做非空验证 */
	public void openDrawer() {
		mDrawerLayout.openDrawer(Gravity.LEFT);
	}

	/** 关闭抽屉 */
	public void closeDrawer() {
		mDrawerLayout.closeDrawer(Gravity.LEFT);
	}

	/** 抽屉状态改变的监听事件 */
	private OnDrawerStateChangedListener mOnDrawerStateChangedListener;
	public void setOnDrawerStateChangedListener (OnDrawerStateChangedListener l) {
		mOnDrawerStateChangedListener = l;
	}

	private void initEvents() {
		mDrawerLayout.setDrawerListener(new DrawerListener() {
			@Override
			public void onDrawerStateChanged(int newState) {
				// 处理抽屉状态  STATE_IDLE(闲置) STATE_DRAGGING(拖动中) STATE_SETTLING(抽屉打开的过程中,不drag)
				// TODO 可以定义回调,在其它类中中实现
				switch (newState) {
				case DrawerLayout.STATE_IDLE:
					mOnDrawerStateChangedListener.onDrawerIdle();
					break;
				case DrawerLayout.STATE_DRAGGING:
					mOnDrawerStateChangedListener.onDrawerDragging();
					break;
				case DrawerLayout.STATE_SETTLING:
					mOnDrawerStateChangedListener.onDrawerSettle();
					break;

				default:
					break;
				}
			}

			/** 滑动时事件处理 */
			@Override
			public void onDrawerSlide(View drawerView, float slideOffset) {
				View mContent = mDrawerLayout.getChildAt(0);
				View mMenu = drawerView;
				float scale = 1 - slideOffset;
				float rightScale = 0.8f + scale * 0.2f; // 随slideOffset的变化而变化

				if (drawerView.getTag().equals("LEFT")) {

					float leftScale = 1 - 0.3f * scale;

					ViewHelper.setScaleX(mMenu, leftScale);
					ViewHelper.setScaleY(mMenu, leftScale);
					ViewHelper.setAlpha(mMenu, 0.6f + 0.4f * (1 - scale));
					ViewHelper.setTranslationX(mContent, mMenu.getMeasuredWidth() * (1 - scale));
					ViewHelper.setPivotX(mContent, 0);
					ViewHelper.setPivotY(mContent, mContent.getMeasuredHeight() / 2);
					mContent.invalidate();
					ViewHelper.setScaleX(mContent, rightScale);
					ViewHelper.setScaleY(mContent, rightScale);
				} else {
					ViewHelper.setTranslationX(mContent, -mMenu.getMeasuredWidth() * slideOffset);
					ViewHelper.setPivotX(mContent, mContent.getMeasuredWidth());
					ViewHelper.setPivotY(mContent, mContent.getMeasuredHeight() / 2);
					mContent.invalidate();
					ViewHelper.setScaleX(mContent, rightScale);
					ViewHelper.setScaleY(mContent, rightScale);
				}
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				ShowUtil.showToast(getActivity(), "抽屉开启!");
			}

			@Override
			public void onDrawerClosed(View drawerView) {
				// TODO 关闭抽屉时的处理操作
				ShowUtil.showToast(getActivity(), "抽屉关闭!");
				// mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.LEFT); // 关闭
			}
		});
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@SuppressWarnings("deprecation")
	public List<DrawerLayoutInfo> getMenu() {
		/** load data */
		List<DrawerLayoutInfo> items = new ArrayList<DrawerLayoutInfo>();
		items.add(new DrawerLayoutInfo(itemName_arr[0], getResources().getDrawable(R.drawable.ic_menu_check)));
		items.add(new DrawerLayoutInfo(itemName_arr[1], getResources().getDrawable(R.drawable.ic_menu_check)));
		items.add(new DrawerLayoutInfo(itemName_arr[2], getResources().getDrawable(R.drawable.ic_menu_check)));
		items.add(new DrawerLayoutInfo(itemName_arr[3], getResources().getDrawable(R.drawable.ic_menu_check)));
		return items;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		ShowUtil.showToast(getActivity(), position + "");
		switch (position) {
		case 0:
			Intent intent = new Intent(getActivity(), AboutActivity.class);
			startActivity(intent);
			break;
		case 1:
			Intent inten1 = new Intent(getActivity(), AboutActivity.class);
			startActivity(inten1);
			break;
		case 2:
			Intent intent2 = new Intent(getActivity(), AboutActivity.class);
			startActivity(intent2);
			break;
		case 3:
			Intent intent3 = new Intent(getActivity(), AboutActivity.class);
			startActivity(intent3);
			break;
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}
}