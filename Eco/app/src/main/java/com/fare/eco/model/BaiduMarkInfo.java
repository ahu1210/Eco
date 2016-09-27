package com.fare.eco.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fare.eco.ui.R;


public class BaiduMarkInfo implements Serializable {
	private static final long serialVersionUID = -758459502806858414L;

	private double latitude;
	private double longitude;
	private int imgId;

	/** 商家信息 */
	private String name;
	private String distance;
	private int zan;

	public static List<BaiduMarkInfo> infos = new ArrayList<BaiduMarkInfo>();

	/** 单独写 */
	static {
		infos.add(new BaiduMarkInfo(118.804929, 31.971666, R.drawable.a01, "英伦贵族小旅馆",
				"距离209米", 1456));
		infos.add(new BaiduMarkInfo(118.822895, 31.96992, R.drawable.a02, "沙井国际洗浴会所",
				"距离897米", 456));
		infos.add(new BaiduMarkInfo(118.789514, 32.006089, R.drawable.a03, "五环服装城",
				"距离249米", 1456));
		infos.add(new BaiduMarkInfo(118.79012, 32.047087, R.drawable.a04, "老米家泡馍小炒",
				"距离679米", 1456));
	}

	public BaiduMarkInfo() {

	}

	public BaiduMarkInfo(double longitude, double latitude, int imgId, String name,
			String distance, int zan) {
		super();
		this.longitude = longitude;
		this.latitude = latitude;
		this.imgId = imgId;
		this.name = name;
		this.distance = distance;
		this.zan = zan;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getName() {
		return name;
	}

	public int getImgId() {
		return imgId;
	}

	public void setImgId(int imgId) {
		this.imgId = imgId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public int getZan() {
		return zan;
	}

	public void setZan(int zan) {
		this.zan = zan;
	}

}
