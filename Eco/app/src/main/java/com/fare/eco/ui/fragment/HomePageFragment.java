package com.fare.eco.ui.fragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.fare.eco.config.Constants;
import com.fare.eco.externalLibrary.imageload.core.ImageLoader;
import com.fare.eco.externalLibrary.imageload.core.listener.PauseOnScrollListener;
import com.fare.eco.externalLibrary.pullto.ILoadingLayout;
import com.fare.eco.externalLibrary.pullto.PullToRefreshBase;
import com.fare.eco.externalLibrary.pullto.PullToRefreshBase.OnRefreshListener;
import com.fare.eco.externalLibrary.pullto.PullToRefreshListView;
import com.fare.eco.externalLibrary.volley.Response;
import com.fare.eco.externalLibrary.volley.Response.Listener;
import com.fare.eco.externalLibrary.volley.VolleyError;
import com.fare.eco.manager.http.MineRequest;
import com.fare.eco.model.HpListProduct;
import com.fare.eco.ui.BaseFragment;
import com.fare.eco.ui.R;
import com.fare.eco.ui.activity.AboutActivity;
import com.fare.eco.ui.activity.navigationact.food.FoodDetailActivity;
import com.fare.eco.ui.util.BitmapUtils;
import com.fare.eco.ui.util.MD5Utils;
import com.fare.eco.ui.util.ShowUtil;
import com.fare.eco.ui.util.Util;
import com.fare.eco.ui.view.hpView.AdvertView;
import com.fare.eco.ui.view.hpView.Hp_navigationView;

/**
 * 首页
 * @author Xiao_V
 * @since 2015/6/1
 */
public class HomePageFragment extends BaseFragment {

	private static final String TAG = "HomePageFragment";

	/** listView相关 */
	private PullToRefreshListView mPtrListView;
	private ListView mListView;
	private MyAdapter adapter;
	private List<HpListProduct> products;
	/** 分页数据 */
	private int pageSize = 5, pageNum = 1;

	private Handler mHandler;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_homepage, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		initView(view); // 初始化变量
		setListView(); // 设置listView(下拉刷新,点击事件...)
	}

	private void initView(View view) {
		mPtrListView = (PullToRefreshListView) view.findViewById(R.id.lv_homepage);

		products = new ArrayList<HpListProduct>();
		adapter = new MyAdapter(products);

		products.clear();
		mHandler = new Handler();
	}

	private void setListView() {
		addHead(); // 添加listView的头布局
		setData(); // 加载数据
		mPtrListView.setAdapter(adapter);

		ILoadingLayout startLabels = mPtrListView.getLoadingLayoutProxy();  
        startLabels.setPullLabel("使劲往下拉"); // 刚下拉时，显示的提示  
        startLabels.setRefreshingLabel("正在刷新"); // 刷新时  
        startLabels.setReleaseLabel("不要再拉了"); // 下来达到一定距离时，显示的提示  

		mPtrListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(
						getActivity().getApplicationContext(),
						System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				// 显示最后更新的时间
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

				if (mPtrListView.isRefreshing()) {
					MineRequest mineRequest = new MineRequest();
					Map<String, String> map = new HashMap<String, String>();
					map.put("license", MD5Utils.MD5(Constants.SECRET_KEY));
					map.put("timeStamp", System.currentTimeMillis() + "");
					
					// TODO 可封装,map.putAll(map) 
					//map.put("uid", uid);
					//map.put(latitude, latitude);
					String jsonString = Util.createJsonString("requestData", map); // json key
					jsonString = MD5Utils.xor(jsonString); // 异或加密
					mineRequest.request4HpListProduct(getActivity(),createMyReqSuccessListener(),
							createMyReqErrorListener(), jsonString, pageSize, pageNum);
				}
			}
		});
	}

	private Listener<String> createMyReqSuccessListener() {
		return new Listener<String>() {
			@Override
			public void onResponse(String response) {
				if (DEBUG) Log.i(TAG, " begin to parse response and response is --->: " + response);
				long start = System.currentTimeMillis();
				try {
					JSONObject jsonObject = new JSONObject(response);
					JSONArray jsonArray = jsonObject.getJSONArray("products");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObject2 = jsonArray.getJSONObject(i);
						HpListProduct product = new HpListProduct();
						product.setId(Integer.parseInt(jsonObject2.getString("id")));
						product.setName(jsonObject2.getString("name"));
						product.setPrice(jsonObject2.getString("price"));
						product.setOriginal_price(jsonObject2.getString("original_price"));
						product.setDistance(jsonObject2.getString("distance"));
						product.setDescribes(jsonObject2.getString("describes"));
						product.setAddress(jsonObject2.getString("address"));
						product.setReview(jsonObject2.getString("review"));
						product.setSold(jsonObject2.getString("sold"));
						String[] imageUrls = new String[5];
						for (int j = 0; j < imageUrls.length; j++) {
							if (jsonObject2.getString("imgurl" + j) != null) {
								imageUrls[j] = jsonObject2.getString("imgurl" + j);
							}
						}
						product.setImgurls(imageUrls);
						products.add(product);
						long end = System.currentTimeMillis();
						ShowUtil.showLog(TAG, DEBUG, " list products --->: " + products.toString(), "i");
						
						if (end - start < 1500) { // TODO 常量表示,封装重复代码
							mHandler.postDelayed(new Runnable() {
								
								@Override
								public void run() {
									adapter.notifyDataSetChanged();
									mPtrListView.onRefreshComplete(); // 任务完成时调用,用于结束刷新
									ShowUtil.showToast(getActivity(), "refresh success！");
									pageSize += 5;
									pageNum++;
								}
							}, 1500);
						} else {
							adapter.notifyDataSetChanged();
							mPtrListView.onRefreshComplete();
							ShowUtil.showToast(getActivity(), "refresh success！");
						}
					}
					if (DEBUG) Log.i(TAG, " onRefreshComplete ");
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		};
	}

	private Response.ErrorListener createMyReqErrorListener() {
		return new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				if (DEBUG)  Log.e(TAG, "request fail");
				mHandler.postDelayed(requestErrorRunnable, 2500);
			}
		};
	}

	private Runnable requestErrorRunnable = new Runnable() {
		@Override
		public void run() {
			mPtrListView.onRefreshComplete();
			if (getActivity() != null) {
				ShowUtil.showToast(getActivity(), "刷新失败,请检查网络!");
			}
		}
	};

	private void setData() {
		products.add(new HpListProduct(250, "肯德基", "99", "9.9", "1.5km", "【胜太西路】50元代金券n张,可叠加,免预约",
				"address", "review-250","已售出225", Constants.path));
		products.add(new HpListProduct(250, "必胜客", "989", "9.98", "<500m", "【新街口】30元代金券n张,可叠加,免预约",
				"address", "review-250","已售出586", Constants.path));
		products.add(new HpListProduct(250, "麦当劳", "25", "9.98", "<500m", "【鼓楼】30元代金券n张,可叠加,免预约",
				"address", "review-250","已售出586", Constants.path));
		products.add(new HpListProduct(250, "小肥羊", "60", "9.98", "1.5m", "【新街口】30元代金券n张,可叠加,免预约",
				"address", "review-250","已售出586", Constants.path));
		products.add(new HpListProduct(250, "切糕", "30", "9.98", "<500m", "【河西】30元代金券n张,可叠加,免预约",
				"address", "review-250","已售出156", Constants.path));
	}

	private void addHead() {
		mListView = mPtrListView.getRefreshableView();
		AdvertView advertView = new AdvertView(getActivity());

		mListView.addHeaderView(advertView);

		Hp_navigationView headView2 = new Hp_navigationView(getActivity());
		mListView.addHeaderView(headView2);
		// TODO
		mListView.setOnScrollListener(new PauseOnScrollListener(ImageLoader.getInstance(), false, true));
	}

	class MyAdapter extends BaseAdapter {

		private List<HpListProduct> products;

		public MyAdapter(List<HpListProduct> products) {
			this.products = products;
		}

		@Override
		public int getCount() {
			return products.size();
		}

		@Override
		public Object getItem(int position) {
			return products.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@SuppressLint("InflateParams")
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder holder;

			if (convertView == null) {
				LayoutInflater inflater = HomePageFragment.this.getActivity().getLayoutInflater();
				convertView = inflater.inflate(R.layout.hp_item_recommend, null);
				holder = new ViewHolder();
				holder.imageView = (ImageView) convertView.findViewById(R.id.avater);
				holder.name = (TextView) convertView.findViewById(R.id.name);
				holder.distance = (TextView) convertView.findViewById(R.id.distance);
				holder.describe = (TextView) convertView.findViewById(R.id.describe);
				holder.price = (TextView) convertView.findViewById(R.id.price);
				holder.original_price = (TextView) convertView.findViewById(R.id.original_price);
				holder.sold = (TextView) convertView.findViewById(R.id.sold);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			HpListProduct product = products.get(position);

			BitmapUtils.loadImg(product.getImgurls()[position], holder.imageView);

			holder.name.setText(product.getName());
			holder.distance.setText(product.getDistance());
			holder.describe.setText(product.getDescribes());
			holder.price.setText(product.getPrice());
			holder.original_price.setText(product.getOriginal_price());
			holder.sold.setText(product.getSold());

			convertView.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getActivity(), FoodDetailActivity.class);
					intent.putExtra("products", (Serializable) products);
					startActivity(intent);
				}
			});

			return convertView;
		}

		class ViewHolder {
			ImageView imageView;
			TextView name, distance, describe, original_price, price, sold;
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mHandler != null) {
			mHandler.removeCallbacks(requestErrorRunnable);
		}
		if (mPtrListView != null) {
			mPtrListView.onRefreshComplete();
			mPtrListView = null;
		}
		mListView = null;
		adapter = null;
	}
}