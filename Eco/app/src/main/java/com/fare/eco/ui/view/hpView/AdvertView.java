package com.fare.eco.ui.view.hpView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.fare.eco.ui.R;
import com.fare.eco.ui.adapter.ViewPaperAdapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * 广告view
 * @author fare_wang
 * @since 2015.04.22
 *
 */
public class AdvertView extends RelativeLayout implements Runnable {

	private ViewPager mViewPager;
	private AtomicInteger atomicInt = new AtomicInteger(0);
	private boolean isContinue = true;
	private ViewGroup group;
	private ViewPaperAdapter mAdapter;
	private List<View> images; // 图片list
	private ImageView[] dot; // 底部圆点导航
	private ImageView fork;
	private RelativeLayout layout;

	/** 图片资源 */
	private int[] resId = { R.drawable.advert0,
			R.drawable.advert1,
			R.drawable.advert2,
			R.drawable.advert3 }; 

	private final Handler viewHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			mViewPager.setCurrentItem(msg.what);
			super.handleMessage(msg);
		}
	};

	public AdvertView(Context context) {
		this(context, null);
	}

	public AdvertView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public AdvertView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		inflate(context, R.layout.layout_advert, this);

		mViewPager = (ViewPager) findViewById(R.id.advert);
		group = (ViewGroup)findViewById(R.id.navigation);
		images = new ArrayList<View>();

		fork = (ImageView) findViewById(R.id.fork);
		layout = (RelativeLayout) findViewById(R.id.advert_layout);

		// dismiss global layout
		fork.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				layout.setVisibility(View.GONE);
			}
		});

		for (int i = 0; i < resId.length; i++) {
			ImageView image = new ImageView(context);
			image.setBackgroundResource(resId[i]);
			images.add(image);
		}

		dot = new ImageView[images.size()];
		for (int i = 0; i < images.size(); i++) {
			ImageView imageView = new ImageView(context);
			imageView.setLayoutParams(new LayoutParams(20, 20));
			imageView.setPadding(5, 5, 5, 5);
			dot[i] = imageView;
			if (i == 0) {
				dot[i].setBackgroundResource(R.drawable.ic_viewpager_select);
			} else {
				dot[i].setBackgroundResource(R.drawable.ic_viewpager_noselect);
			}
			group.addView(dot[i]);
		}

		mViewPager.setOnPageChangeListener(new GuidePageChangeListener());
		mAdapter = new ViewPaperAdapter(images);
		mViewPager.setAdapter(mAdapter);
		mViewPager.setOnTouchListener(new GuideOnTouchListener());
		new Thread(this).start();
	}

	private final class GuidePageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageSelected(int arg0) {
			atomicInt.getAndSet(arg0);
			for (int i = 0; i < dot.length; i++) {
				dot[arg0].setBackgroundResource(R.drawable.ic_viewpager_select);
				if (arg0 != i) {
					dot[i].setBackgroundResource(R.drawable.ic_viewpager_noselect);
				}
			}
		}
	}

	private final class GuideOnTouchListener implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
			case MotionEvent.ACTION_MOVE:
				isContinue = false;
				break;
			case MotionEvent.ACTION_UP:
				isContinue = true;
				break;
			default:
				isContinue = true;
				break;
			}
			return false;
		}

	}

	private void whatOption() {
		atomicInt.incrementAndGet();
		if (atomicInt.get() > dot.length - 1) {
			atomicInt.getAndAdd(-4);
		}
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {

		}
	}

	@Override
	public void run() {
		while (true) {
			if (isContinue) {
				viewHandler.sendEmptyMessage(atomicInt.get());
				whatOption();
			}
		}
	}
}
