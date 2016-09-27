package com.fare.eco.ui.view;

import com.fare.eco.externalLibrary.pullto.PullToRefreshListView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;

/**
 * 重写ListView，用于解决嵌套viewPager滑动冲突 (没用到)
 * 
 */
public class MyListView extends PullToRefreshListView {

	private GestureDetector mGestureDetector;

	public MyListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mGestureDetector = new GestureDetector(context, new YScrollDetector());
	}

	/** 重写拦截器，返回true表示自己处理Touch事件，false则交给子类处理 */
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return super.onInterceptTouchEvent(ev) && mGestureDetector.onTouchEvent(ev);
	}
	
	@Override
	public boolean onInterceptHoverEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return super.onInterceptHoverEvent(event);
	}

	/** return false when Xmove > Ymove */
	class YScrollDetector extends SimpleOnGestureListener {

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			return (Math.abs(distanceY) > Math.abs(distanceX));
		}
	}
}