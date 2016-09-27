package com.fare.eco.manager.http;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.fare.eco.config.Constants;
import com.fare.eco.externalLibrary.volley.AuthFailureError;
import com.fare.eco.externalLibrary.volley.Request.Method;
import com.fare.eco.externalLibrary.volley.Response.ErrorListener;
import com.fare.eco.externalLibrary.volley.Response.Listener;
import com.fare.eco.externalLibrary.volley.toolbox.StringRequest;

/**
 * 封装请求
 * @author Xiao_V
 * @since 2015/8/15 20:18
 */
public class MineRequest {

	private static final String TAG = MineRequest.class.getSimpleName();
	private static final boolean DEBUG = true;
	private static MineRequest mInstance;

	public static MineRequest getInstance(){
		if(mInstance == null) {
			mInstance = new MineRequest();
		}
		return mInstance;
	}

	/** 获取服务器的url */
    private String getUrl(Context context, String postfix) {
        SharedPreferences sp = context.getSharedPreferences(Constants.SHARE_NAME_IP, Context.MODE_PRIVATE);
        String ip = sp.getString("ip", "192.168.1.100");
        String port = sp.getString("port", "8080");
	    String path = "http://"+ ip + ":" + port + postfix;
        return path;
    }

    public void request4HpListProduct(Context context, Listener<String> listener, ErrorListener errorListener, 
    		final String jsonData, final int pageSize, final int pageNum){
    	
    	String path = getUrl(context, "/MeiTuan_server/servlet/Hp_productAction?action_flag=queryByPage");
    	
    	StringRequest request = new StringRequest(Method.POST, path, listener, errorListener){
    		@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> map = new HashMap<String, String>();
				map.put("request4HpListProduct", jsonData); // form params
				map.put("pageSize", String.valueOf(pageSize));
				map.put("pageNum", String.valueOf(pageNum));
				return map;
			}
    	};
		if (DEBUG) Log.v(TAG, request.toString());
		RequestManager.getRequestQueue().add(request);
	}
	
}