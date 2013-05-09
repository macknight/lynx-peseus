package com.lynx.mapireader;

import android.app.Application;

import com.lynx.lib.dataservice.HttpService;
import com.lynx.lib.dataservice.impl.DefaultHttpServiceImpl;

/**
 * 
 * @author chris.liu
 * 
 */
public class LCApplication extends Application {

	private static HttpService httpService = null;

	@Override
	public void onCreate() {
		super.onCreate();

		httpService = new DefaultHttpServiceImpl();
	}

	public HttpService httpservice() {
		return httpService;
	}

}
