package com.lynx.mapireader;

import android.app.Application;
import android.os.Build;

import com.lynx.lib.dataservice.HttpService;
import com.lynx.lib.dataservice.impl.DefaultHttpServiceImpl;

/**
 * 
 * @author chris.liu
 * 
 */
public class LCApplication extends Application {

	private static HttpService httpService = null;
	private static String userAgent = null;
	private static String token = null;

	@Override
	public void onCreate() {
		super.onCreate();

		httpService = new DefaultHttpServiceImpl();
		userAgent = String.format("mapi 1.0 peseus 1.0.0 %s %s Android %s",
				Build.MANUFACTURER, Build.MODEL, Build.VERSION.RELEASE);
	}

	public static HttpService httpservice() {
		return httpService;
	}

	public static String userAgent() {
		return userAgent;
	}

	public static String token() {
		return token;
	}

	public static void setToken(String token) {
		LCApplication.token = token;
	}
}
