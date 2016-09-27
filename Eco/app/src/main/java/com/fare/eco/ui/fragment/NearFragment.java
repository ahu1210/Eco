package com.fare.eco.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.fare.eco.externalLibrary.pullto.ILoadingLayout;
import com.fare.eco.externalLibrary.pullto.PullToRefreshBase;
import com.fare.eco.externalLibrary.pullto.PullToRefreshBase.OnRefreshListener;
import com.fare.eco.externalLibrary.pullto.PullToRefreshGridView;
import com.fare.eco.model.Near_Product_Acce;
import com.fare.eco.model.Near_Product;
import com.fare.eco.ui.BaseFragment;
import com.fare.eco.ui.R;
import com.fare.eco.ui.activity.Near_WebViewActivity;
import com.squareup.picasso.Picasso;

public class NearFragment extends BaseFragment {

	private LayoutInflater inflater;
	private PullToRefreshGridView mPtrGridView;
	private Display display;
	private List<Near_Product> image_filenames;
	private ShowGridAdapter mAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		this.inflater = inflater;
		return inflater.inflate(R.layout.fragment_near, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		display = getActivity().getWindowManager().getDefaultDisplay();
		Near_Product_Acce acce = new Near_Product_Acce();
		image_filenames = acce.getList_acce();
		initGrid(view);
		setListView();
	}

	@SuppressWarnings("deprecation")
	private void initGrid(View view) {
		mPtrGridView = (PullToRefreshGridView) view.findViewById(R.id.gv_near);
		int height = display.getHeight() * 5 / 12; // 当前屏幕尺寸的一半作为item的高度
		mAdapter = new ShowGridAdapter(image_filenames, getActivity(), height, 0);
		mPtrGridView.setAdapter(mAdapter);
	}

	private void setListView() {

		ILoadingLayout startLabels = mPtrGridView.getLoadingLayoutProxy();
		startLabels.setPullLabel("使劲往下拉"); // 刚下拉时，显示的提示
		startLabels.setRefreshingLabel("正在刷新"); // 刷新时
		startLabels.setReleaseLabel("不要再拉了"); // 下来达到一定距离时，显示的提示

		mPtrGridView.setOnRefreshListener(new OnRefreshListener<GridView>() {
			@Override
			public void onRefresh(PullToRefreshBase<GridView> refreshView) {
				String label = DateUtils.formatDateTime(getActivity()
						.getApplicationContext(), System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE
								| DateUtils.FORMAT_ABBREV_ALL);
				// 显示最后更新的时间
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

				if (mPtrGridView.isRefreshing()) {
					new GetDataTask().execute();
				}
			}
		});
	}

	private class GetDataTask extends AsyncTask<Void, Void, List<Near_Product>> {

		@Override
		protected List<Near_Product> doInBackground(Void... params) {
			List<Near_Product> refreshList = new ArrayList<Near_Product>();
			try {
				refreshList.add(new Near_Product());
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return refreshList;
		}

		@Override
		protected void onPostExecute(List<Near_Product> result) {
			image_filenames.addAll(result);
			mAdapter.notifyDataSetChanged();
			mPtrGridView.onRefreshComplete(); // 任务完成时调用,用于结束刷新
		}
	}

	class ShowGridAdapter extends BaseAdapter {

		private List<Near_Product> list;
		private Context mContext;
		private int height;

		public ShowGridAdapter(List<Near_Product> list, Context mContext, int height, int sex) {
			this.mContext = mContext;
			this.list = list;
			this.height = height;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int arg0) {
			return list.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			final Near_Product product = list.get(arg0);
			ViewHolder holder = null;
			if (arg1 == null) {
				holder = new ViewHolder();
				arg1 = inflater.inflate(R.layout.near_item, null);

				// 动态设置item的高度
				AbsListView.LayoutParams param = new AbsListView.LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT, height);
				arg1.setLayoutParams(param);
				holder.show_waterfall_image = (ImageView) arg1.findViewById(R.id.show_waterfall_image);
				holder.show_waterfall_text01 = (TextView) arg1.findViewById(R.id.show_waterfall_text01);
				holder.show_waterfall_text02 = (TextView) arg1.findViewById(R.id.show_waterfall_text02);
				holder.show_waterfall_text03 = (TextView) arg1.findViewById(R.id.show_waterfall_text03);
				arg1.setTag(holder);
			} else {
				holder = (ViewHolder) arg1.getTag();
			}


			Picasso.with(mContext).load(product.getIamge_url())
					.placeholder(R.drawable.ic_launcher)
					.error(R.drawable.ic_launcher).into(holder.show_waterfall_image);
			holder.show_waterfall_text01.setText(product.getDescribe());
			holder.show_waterfall_text02.setText("" + product.getPrice());
			holder.show_waterfall_text03.setText("" + product.getMoods());

			holder.show_waterfall_image.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) { // TODO
					Intent intent = new Intent(getActivity(), Near_WebViewActivity.class);
					intent.putExtra("productUrl", product.getUrl());
//					intent.setAction(Intent.ACTION_VIEW);
					mContext.startActivity(intent);
				}
			});
			
			return arg1;
		}


		class ViewHolder {
			ImageView show_waterfall_image;
			TextView show_waterfall_text01;
			TextView show_waterfall_text02;
			TextView show_waterfall_text03;
		}
	}
}
