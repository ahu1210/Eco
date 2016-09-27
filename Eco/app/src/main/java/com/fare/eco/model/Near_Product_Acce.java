package com.fare.eco.model;

import java.util.ArrayList;
import java.util.List;

import com.fare.eco.config.AllData;

/**
 * 封装NearFragment中GridView的数据
 * @author Xiao_V
 *
 */
public class Near_Product_Acce {

	private List<Near_Product> list_acce;

	public List<Near_Product> getList_acce() {
		
		list_acce=new ArrayList<Near_Product>();
		String[] str_x_url=AllData.str_x_url;
		String[] str_x_iamge_url=AllData.str_x_iamge_url;
		String[] str_x_describe=AllData.str_x_describe;
		int[] str_x_price=AllData.str_x_price;
		int[] str_x_sales=AllData.str_x_sales;
		int[] str_x_moods=AllData.str_x_moods;
		
		for (int i = 0; i < str_x_url.length; i++) {

			Near_Product product=new Near_Product();
			product.setUrl(str_x_url[i]);
			product.setIamge_url(str_x_iamge_url[i]);
			product.setDescribe(str_x_describe[i]);
			product.setPrice(str_x_price[i]);
			product.setSales(str_x_sales[i]);
			product.setMoods(str_x_moods[i]);
			
			list_acce.add(product);
		}
		return list_acce;
	}

	public void setList_acce(List<Near_Product> list_acce) {
		this.list_acce = list_acce;
	}
	
}
