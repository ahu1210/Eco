package com.fare.eco.ui.fragment;

import com.fare.eco.config.Constants;
import com.fare.eco.ui.BaseFragment;
import com.fare.eco.ui.R;
import com.fare.eco.ui.activity.AboutActivity;
import com.fare.eco.ui.activity.MainActivity;
import com.fare.eco.ui.activity.SettingActivity;
import com.fare.eco.ui.util.ShowUtil;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class MoreFragment extends BaseFragment implements OnClickListener {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_more, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		view.findViewById(R.id.layout_about).setOnClickListener(this);
		view.findViewById(R.id.layout_exit).setOnClickListener(this);
		view.findViewById(R.id.layout_account).setOnClickListener(this);
		view.findViewById(R.id.layout_general).setOnClickListener(this);
		view.findViewById(R.id.layout_privacy).setOnClickListener(this);
		view.findViewById(R.id.layout_notification).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.layout_about:
				startActivity(new Intent(getActivity(), AboutActivity.class));
				getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
				break;

			case R.id.layout_exit:
				dispatchCommand(250438, null);
				break;

			case R.id.layout_notification:
				dispatchCommand(540, null);
				break;

			case R.id.layout_account:
				Intent intent = new Intent(getActivity(), SettingActivity.class);
				startActivity(intent);
				break;
			case R.id.layout_general: // 清楚本地缓存
				SharedPreferences preferences = getActivity().getSharedPreferences(Constants.SHARE_NAME_SPLASH, Context.MODE_PRIVATE);
				preferences.edit().clear().apply();
				SharedPreferences preferences2 = getActivity().getSharedPreferences(Constants.SHARE_NAME_TITLEBAR_COLOR, Context.MODE_PRIVATE);
				preferences2.edit().clear().apply();
				ShowUtil.showToast(getActivity(), "数据重置,请重启应用");
				break;
			case R.id.layout_privacy: // 设置主题颜色 TODO 判断设置的颜色和原有的颜色是否相同
				SharedPreferences sp = getActivity().getSharedPreferences(Constants.SHARE_NAME_TITLEBAR_COLOR, Context.MODE_PRIVATE);
				int titleBarColor = sp.getInt("color", 0);
				if (titleBarColor != R.color.red) {
					sp.edit().putInt("color", R.color.red).apply();
					getActivity().finish();
					startActivity(new Intent(getActivity(), MainActivity.class));
				} else {
					ShowUtil.showToast(getActivity(), "主题已设置为红色,清除本地缓存还原");
				}
				break;

			default:
				break;
		}
	}

}