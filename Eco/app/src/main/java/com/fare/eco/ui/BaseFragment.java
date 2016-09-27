package com.fare.eco.ui;

import com.fare.eco.model.FragmentCallback;
import com.fare.eco.model.FragmentInterface;
import com.fare.eco.ui.view.CustomLoadingDialog;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public abstract class BaseFragment extends Fragment implements
		FragmentInterface, OnTouchListener {

	private CustomLoadingDialog dialog;

	protected static final boolean DEBUG = true;

	/**
	 * 模拟后退键
	 */
	protected void back() {
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.popBackStackImmediate();
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		view.setOnTouchListener(this);
		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// 拦截触摸事件，防止传递到下一层的View
		return true;
	}

	/**
	 * 调用此方法会回调onFragmentCallback方法(在activity中实现) 用于fragment向activity中传递数据
	 * 
	 * @param id 标识
	 * @param args 数据
	 */
	public void dispatchCommand(int id, Bundle args) {
		if (getActivity() instanceof FragmentCallback) {
			FragmentCallback callback = (FragmentCallback) getActivity();
			callback.onFragmentCallback(this, id, args);
		} else {
			throw new IllegalStateException("The host activity does not implement FragmentCallback.");
		}
	}

	/**
	 * 自定义加载dialog
	 * @param content dialog提示
	 */
	protected void showLoadingDialog(String content, boolean isTouchCancelable) {
		if (dialog == null) {
			dialog = new CustomLoadingDialog(getActivity(), content);
		}
		dialog.setCanceledOnTouchOutside(isTouchCancelable);
		dialog.show();
	}

	/** 关闭dialog */
	protected void closeLoadingDialog() {
		if (dialog != null) {
			dialog.dismiss();
		}
	}

	@Override
	public void refreshViews() {

	}

	@Override
	public boolean commitData() {
		return false;
	}
}
