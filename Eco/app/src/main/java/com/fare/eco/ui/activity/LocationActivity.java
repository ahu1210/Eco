package com.fare.eco.ui.activity;

import java.lang.reflect.Method;
import java.util.List;

import android.graphics.Point;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.InfoWindow.OnInfoWindowClickListener;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfigeration;
import com.baidu.mapapi.map.MyLocationConfigeration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.fare.eco.model.BaiduMarkInfo;
import com.fare.eco.ui.BaseActivity;
import com.fare.eco.ui.R;
import com.fare.eco.ui.adapter.MyOrientationListener;
import com.fare.eco.ui.adapter.MyOrientationListener.OnOrientationListener;

public class LocationActivity extends BaseActivity {

	private MapView mMapView = null;
	private BaiduMap mBaiduMap;
	private LocationClient mLocationClient;
	public MyLocationListener mMyLocationListener;
	private LocationMode mCurrentMode = LocationMode.NORMAL;
	/** 是否是第一次定位 */
	private volatile boolean isFristLocation = true;

	/** 最新一次的经纬度,当前的精度 */
	private double mCurrentLantitude = 31.9764150000;
	private double mCurrentLongitude = 118.8035280000;
	private float mCurrentAccracy;
	/** 方向传感器的监听器 */
	private MyOrientationListener myOrientationListener;
	/** 方向传感器X方向的值 */
	private int mXDirection;

	/** 地图定位的模式 */
	private String[] mStyles = new String[] { "地图模式【正常】", "地图模式【跟随】", "地图模式【罗盘】" };
	private int mCurrentStyle = 0;
	/** 初始化全局 bitmap信息，不用时及时 recycle */
	private BitmapDescriptor mIconMaker;
	/** 详细信息的 布局 */
	private RelativeLayout mMarkerInfoLy;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 在setContentView方法之前实现
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.location_baidu);

		isFristLocation = true;
		// 获取地图控件引用
		mMapView = (MapView) findViewById(R.id.id_bmapView);
		initMyLocation(); // 初定位
		mMarkerInfoLy = (RelativeLayout) findViewById(R.id.id_marker_info);
		// 获得地图的实例
		mBaiduMap = mMapView.getMap();
		mIconMaker = BitmapDescriptorFactory.fromResource(R.drawable.maker);
		MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(14.0f); // 地图的缩放级别
		mBaiduMap.setMapStatus(msu);

	}

	@Override
	protected void onStart() {
		// 开启图层定位
		if (!mLocationClient.isStarted()) {
			mLocationClient.start();
		}
		// 开启方向传感器
		initOritationListener(); // 方向传感器
		myOrientationListener.start();
		super.onStart();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mBaiduMap.setMyLocationEnabled(true);
		initMarkerClickEvent(); // 标记的点击事件
		initMapClickEvent(); // 地图的点击事件
		addInfosOverlay(BaiduMarkInfo.infos); // add mark
		mMapView.onResume();
	}

	/** 地图的点击事件 */
	private void initMapClickEvent() {
		mBaiduMap.setOnMapClickListener(new OnMapClickListener() {

			@Override
			public boolean onMapPoiClick(MapPoi arg0) {
				return false;
			}

			@Override
			public void onMapClick(LatLng arg0) {
				mMarkerInfoLy.setVisibility(View.GONE);
				mBaiduMap.hideInfoWindow(); // 隐藏InfoWindow

			}
		});
	}

	/** 初始化标记的点击事件 */
	private void initMarkerClickEvent() {
		// 对Marker的点击
		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			@Override
			public boolean onMarkerClick(final Marker marker) {
				// 获得marker中的数据
				BaiduMarkInfo info = (BaiduMarkInfo) marker.getExtraInfo().get("info");

				InfoWindow mInfoWindow; // 覆盖物上弹出的窗口
				TextView locTextView = new TextView(getApplicationContext());
				locTextView.setBackgroundResource(R.drawable.location_tips);
				locTextView.setPadding(30, 20, 30, 50);
				locTextView.setText(info.getName());

				// 将marker所在的经纬度的转化成屏幕上的坐标
				final LatLng ll = marker.getPosition();
				Point p = mBaiduMap.getProjection().toScreenLocation(ll);
				p.y -= 50;
				LatLng llInfo = mBaiduMap.getProjection().fromScreenLocation(p);
				// 为弹出的InfoWindow添加点击事件
				mInfoWindow = new InfoWindow(locTextView, llInfo, new OnInfoWindowClickListener() {
					@Override
					public void onInfoWindowClick() {
					mBaiduMap.hideInfoWindow(); // 隐藏InfoWindow
					}
				});
				// 显示InfoWindow
				mBaiduMap.showInfoWindow(mInfoWindow);
				// 设置详细信息布局为可见
				mMarkerInfoLy.setVisibility(View.VISIBLE);
				// 根据商家信息为详细信息布局设置信息
				popupInfo(mMarkerInfoLy, info);
				return true;
			}
		});
	}

	/**
	 * 根据info为布局上的控件设置信息
	 * 
	 * @param mMarkerInfo2布局
	 * @param info数据信息
	 */
	protected void popupInfo(RelativeLayout mMarkerLy, BaiduMarkInfo info) {
		ViewHolder viewHolder = null;
		if (mMarkerLy.getTag() == null) {
			viewHolder = new ViewHolder();
			viewHolder.infoImg = (ImageView) mMarkerLy.findViewById(R.id.info_img);
			viewHolder.infoName = (TextView) mMarkerLy.findViewById(R.id.info_name);
			viewHolder.infoDistance = (TextView) mMarkerLy.findViewById(R.id.info_distance);
			viewHolder.infoZan = (TextView) mMarkerLy.findViewById(R.id.info_zan);

			mMarkerLy.setTag(viewHolder);
		}
		viewHolder = (ViewHolder) mMarkerLy.getTag();
		viewHolder.infoImg.setImageResource(info.getImgId());
		viewHolder.infoDistance.setText(info.getDistance());
		viewHolder.infoName.setText(info.getName());
		viewHolder.infoZan.setText(info.getZan() + "");
	}

	private class ViewHolder {
		ImageView infoImg;
		TextView infoName;
		TextView infoDistance;
		TextView infoZan;
	}

	/** 初始化方向传感器 */
	private void initOritationListener() {
		myOrientationListener = new MyOrientationListener(
				getApplicationContext());
		myOrientationListener.setOnOrientationListener(new OnOrientationListener() {
			@Override
			public void onOrientationChanged(float x) {
				mXDirection = (int) x;
				// 构造定位数据
				MyLocationData locData = new MyLocationData.Builder()
						.accuracy(mCurrentAccracy)
						// 此处设置开发者获取到的方向信息，顺时针0-360
						.direction(mXDirection)
						.latitude(mCurrentLantitude)
						.longitude(mCurrentLongitude).build();
				// 设置定位数据
				mBaiduMap.setMyLocationData(locData);
				// 设置自定义图标
				BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory.fromResource(R.drawable.navi_map_gps_locked);
				MyLocationConfigeration config = new MyLocationConfigeration(mCurrentMode, true, mCurrentMarker);
				mBaiduMap.setMyLocationConfigeration(config);

					}
				});
	}

	/** 初始化定位 */
	private void initMyLocation() {
		// 定位初始化
		mLocationClient = new LocationClient(getApplicationContext()); // 用全局引用,不要用this
		mMyLocationListener = new MyLocationListener();
		mLocationClient.registerLocationListener(mMyLocationListener);
		// 设置定位的相关配置
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(15000); // 扫描间隔,>=1000时间歇性定位(会造成地图的移动),<1000时表示一次定位
		mLocationClient.setLocOption(option);
	}

	/** 初始化mark */
	public void addInfosOverlay(List<BaiduMarkInfo> infos) {
		mBaiduMap.clear();
		LatLng latLng = null;
		OverlayOptions overlayOptions = null;
		Marker marker = null;
		for (BaiduMarkInfo info : infos) {
			latLng = new LatLng(info.getLatitude(), info.getLongitude()); // 位置
			overlayOptions = new MarkerOptions().position(latLng).icon(mIconMaker).zIndex(5); // 图标
			marker = (Marker) (mBaiduMap.addOverlay(overlayOptions));
			Bundle bundle = new Bundle();
			bundle.putSerializable("info", info);
			marker.setExtraInfo(bundle);
		}
		// 将地图移到到最后一个经纬度位置
//		MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(latLng);
//		mBaiduMap.setMapStatus(u);
	}

	/** 实现实位回调监听 */
	public class MyLocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {

			// map view 销毁后不在处理新接收的位置
			if (location == null || mMapView == null)
				return;
			// 构造定位数据
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(mXDirection).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			mCurrentAccracy = location.getRadius();
			// 设置定位数据
			mBaiduMap.setMyLocationData(locData);
			mCurrentLantitude = location.getLatitude();
			mCurrentLongitude = location.getLongitude();
			// 设置自定义图标
			BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory.fromResource(R.drawable.navi_map_gps_locked);
			MyLocationConfigeration config = new MyLocationConfigeration(mCurrentMode, true, mCurrentMarker);
			mBaiduMap.setMyLocationConfigeration(config);
			// 第一次定位时，将地图位置移动到当前位置
			if (isFristLocation) {
				isFristLocation = false;
				requestLoc();
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/** 菜单项的图标 */
	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {

		if (featureId == Window.FEATURE_OPTIONS_PANEL && menu != null) {
			if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
				try {
					Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
					m.setAccessible(true);
					m.invoke(menu, true);
				} catch (Exception e) {
				}
			}

		}
		return super.onMenuOpened(featureId, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		case R.id.id_menu_map_addMaker:
			// addInfosOverlay(Info.infos);
			break;

		case R.id.id_menu_map_common: // 普通地图
			mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
			break;

		case R.id.id_menu_map_site:// 卫星地图
			mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
			break;

		case R.id.id_menu_map_traffic: // 开启交通图
			if (mBaiduMap.isTrafficEnabled()) {
				item.setTitle("开启实时交通");
				mBaiduMap.setTrafficEnabled(false);
			} else {
				item.setTitle("关闭实时交通");
				mBaiduMap.setTrafficEnabled(true);
			}
			break;

		case R.id.id_menu_map_myLoc:
			requestLoc();
			break;

		case R.id.id_menu_map_style:
			mCurrentStyle = (++mCurrentStyle) % mStyles.length;
			item.setTitle(mStyles[mCurrentStyle]);

			switch (mCurrentStyle) {
			case 0:
				mCurrentMode = LocationMode.NORMAL;
				break;
			case 1:
				mCurrentMode = LocationMode.FOLLOWING;
				break;
			case 2:
				mCurrentMode = LocationMode.COMPASS;
				break;
			}

			BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory.fromResource(R.drawable.navi_map_gps_locked);
			MyLocationConfigeration config = new MyLocationConfigeration(mCurrentMode, true, mCurrentMarker);
			mBaiduMap.setMyLocationConfigeration(config);
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	/** 定位 */
	private void requestLoc() {
		if (mCurrentLantitude <= 1e-8 || mCurrentLongitude <= 1e-8) {
			mCurrentLantitude = 31.9764150000;
			mCurrentLongitude = 118.8035280000;
		}
		LatLng ll = new LatLng(mCurrentLantitude, mCurrentLongitude);
		MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
		mBaiduMap.setMapStatus(u);
		// mBaiduMap.animateMapStatus(u); //动画拉伸到所在点
	}

	@Override
	protected void onPause() {
		super.onPause();
		mMapView.onPause();
	}

	@Override
	protected void onStop() {
		// 关闭图层定位
		mBaiduMap.setMyLocationEnabled(false);
		mLocationClient.stop();

		// 关闭方向传感器
		myOrientationListener.stop();
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mMapView.onDestroy();
		mIconMaker.recycle();
		mMapView = null;
	}

}