package com.fare.eco.manager.http;

import com.fare.eco.externalLibrary.volley.RequestQueue;
import com.fare.eco.externalLibrary.volley.toolbox.Volley;

import android.content.Context;

public class RequestManager {

	/** requestQueue */
	private static RequestQueue mRequestQueue;

	private RequestManager() {

	}

	public static void init(Context context) {
		mRequestQueue = Volley.newRequestQueue(context);
	}

	public static RequestQueue getRequestQueue() {
		if (mRequestQueue != null) {
			return mRequestQueue;
		} else {
			throw new IllegalStateException("Not initialized");
		}
	}
}
