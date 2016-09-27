package com.fare.eco.model;

import java.io.Serializable;
import java.util.Arrays;

public class HpListProduct implements Serializable{

	private static final long serialVersionUID = 8434154L;

	private int id;
	private String name;
	private String price; // 价格
	private String original_price; // 原价
	private String distance; // 距离
	private String describes; // 描述
	private String address;
	private String review; // 访问量
	private String sold; // 已售
	private String[] imgurls; // 图片url
	
	public HpListProduct() {
		
	}

	public HpListProduct(int id, String name, String price,
			String original_price, String distance, String describes,
			String address, String review, String sold, String[] imgurls) {
		super();
		this.id = id;
		this.name = name;
		this.price = price;
		this.original_price = original_price;
		this.distance = distance;
		this.describes = describes;
		this.address = address;
		this.review = review;
		this.sold = sold;
		this.imgurls = imgurls;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getOriginal_price() {
		return original_price;
	}

	public void setOriginal_price(String original_price) {
		this.original_price = original_price;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getDescribes() {
		return describes;
	}

	public void setDescribes(String describes) {
		this.describes = describes;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getReview() {
		return review;
	}

	public void setReview(String review) {
		this.review = review;
	}

	public String getSold() {
		return sold;
	}

	public void setSold(String sold) {
		this.sold = sold;
	}

	public String[] getImgurls() {
		return imgurls;
	}

	public void setImgurls(String[] imgurls) {
		this.imgurls = imgurls;
	}

	@Override
	public String toString() {
		return "HpListProduct [id=" + id + ", name=" + name + ", price="
				+ price + ", original_price=" + original_price + ", distance="
				+ distance + ", describes=" + describes + ", address="
				+ address + ", review=" + review + ", sold=" + sold
				+ ", imgurls=" + Arrays.toString(imgurls) + "]";
	}
	
}
