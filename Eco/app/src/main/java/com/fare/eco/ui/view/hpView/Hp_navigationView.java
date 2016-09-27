package com.fare.eco.ui.view.hpView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.fare.eco.ui.R;
import com.fare.eco.ui.activity.AboutActivity;
import com.fare.eco.ui.activity.navigationact.food.FoodListActivity;
import com.fare.eco.ui.adapter.ViewPaperAdapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * 首页导航栏菜单(ktv/...)
 * @author fare_wang
 * @since 2015.04.22
 *
 */
public class Hp_navigationView extends RelativeLayout implements View.OnClickListener {

	private ViewPager mViewPager;
	private ViewGroup group;
	private ViewPaperAdapter mAdapter;
	private ImageView[] mIndicator_list;
	private List<View> mView_list;
	private AtomicInteger atomicInt = new AtomicInteger(0);

	public Hp_navigationView(Context context) {
		this(context, null);
	}

	public Hp_navigationView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public Hp_navigationView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		inflate(context, R.layout.hp_vp_navigation, this);
		mViewPager = (ViewPager) findViewById(R.id.viewpager);
		group = (ViewGroup) findViewById(R.id.viewGroup);

		// TODO 作为参数，让其它类去定义View
		View view1 = inflate(context, R.layout.hp_vp_menu1, null);
		View view2 = inflate(context, R.layout.hp_vp_menu2, null);

		view1.findViewById(R.id.cate).setOnClickListener(this);
		view1.findViewById(R.id.movie).setOnClickListener(this);
		view1.findViewById(R.id.hotel).setOnClickListener(this);
		view1.findViewById(R.id.ktv).setOnClickListener(this);
		view1.findViewById(R.id.recom).setOnClickListener(this);
		view1.findViewById(R.id.circum).setOnClickListener(this);
		view1.findViewById(R.id.chit).setOnClickListener(this);
		view1.findViewById(R.id.take_out).setOnClickListener(this);

		view2.findViewById(R.id.snack).setOnClickListener(this);
		view2.findViewById(R.id.cake).setOnClickListener(this);
		view2.findViewById(R.id.recreation).setOnClickListener(this);
		view2.findViewById(R.id.beauty).setOnClickListener(this);
		view2.findViewById(R.id.serve).setOnClickListener(this);
		view2.findViewById(R.id.knead).setOnClickListener(this);
		view2.findViewById(R.id.travel).setOnClickListener(this);
		view2.findViewById(R.id.all).setOnClickListener(this);

		mView_list = new ArrayList<View>();
		mView_list.add(view1);
		mView_list.add(view2);

		setDotIndicator(context); // 圆点指示器

		mAdapter = new ViewPaperAdapter(mView_list);
		mViewPager.setAdapter(mAdapter);
	}
	
	/** 设置viewPagew的圆点指示器 */
	private void setDotIndicator(Context context) {
		mIndicator_list = new ImageView[mView_list.size()];

		for (int i = 0; i < mView_list.size(); i++) {
			ImageView image_dot = new ImageView(context);
			image_dot.setLayoutParams(new LayoutParams(20, 20));
			image_dot.setPadding(5, 5, 5, 5);
			mIndicator_list[i] = image_dot;
			if (i == 0) {
				mIndicator_list[i].setBackgroundResource(R.drawable.ic_viewpager_select);
			} else {
				mIndicator_list[i].setBackgroundResource(R.drawable.ic_viewpager_noselect);
			}
			group.addView(mIndicator_list[i]);
		}
		mViewPager.setOnPageChangeListener(new GuidePageChangeListener());
	}

	/** viewPager 监听 */
	private final class GuidePageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageSelected(int arg0) {
			atomicInt.getAndSet(arg0);
			for (int i = 0; i < mIndicator_list.length; i++) {
				mIndicator_list[arg0].setBackgroundResource(R.drawable.ic_viewpager_select);
				if (arg0 != i) {
					mIndicator_list[i].setBackgroundResource(R.drawable.ic_viewpager_noselect);
				}
			}
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}
	}
	
	private void startActivity(final Class<?> cla) {
		getContext().startActivity(new Intent(getContext(), cla));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cate:
			startActivity(FoodListActivity.class);
			break;
		case R.id.movie:
			startActivity(AboutActivity.class);
			break;
		case R.id.hotel:
			startActivity(AboutActivity.class);
			break;
		case R.id.ktv:
			startActivity(AboutActivity.class);
			break;
		case R.id.recom:
			getContext().startActivity(new Intent(getContext(), AboutActivity.class));
			break;
		case R.id.circum:
			getContext().startActivity(new Intent(getContext(), AboutActivity.class));
			break;
		case R.id.chit:
			getContext().startActivity(new Intent(getContext(), AboutActivity.class));
			break;
		case R.id.take_out:
			getContext().startActivity(new Intent(getContext(), AboutActivity.class));
			break;
		case R.id.snack:
			getContext().startActivity(new Intent(getContext(), AboutActivity.class));
			break;
		case R.id.cake:
			getContext().startActivity(new Intent(getContext(), AboutActivity.class));
			break;
		case R.id.recreation:
			getContext().startActivity(new Intent(getContext(), AboutActivity.class));
			break;
		case R.id.beauty:
			getContext().startActivity(new Intent(getContext(), AboutActivity.class));
			break;
		case R.id.serve:
			getContext().startActivity(new Intent(getContext(), AboutActivity.class));
			break;
		case R.id.knead:
			getContext().startActivity(new Intent(getContext(), AboutActivity.class));
			break;
		case R.id.travel:
			getContext().startActivity(new Intent(getContext(), AboutActivity.class));
			break;
		case R.id.all:
			getContext().startActivity(new Intent(getContext(), AboutActivity.class));
			break;
		default:
			break;
		}
	}
}
