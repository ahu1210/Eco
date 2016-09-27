package com.fare.eco.model;

import java.io.Serializable;

public class Near_Product implements Serializable {

	private static final long serialVersionUID = 6161111399370550196L;
	private String url;
	private String iamge_url;
	private String describe;
	private int price;
	private int sales;
	private int moods;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getIamge_url() {
		return iamge_url;
	}

	public void setIamge_url(String iamge_url) {
		this.iamge_url = iamge_url;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getSales() {
		return sales;
	}

	public void setSales(int sales) {
		this.sales = sales;
	}

	public int getMoods() {
		return moods;
	}

	public void setMoods(int moods) {
		this.moods = moods;
	}
}
