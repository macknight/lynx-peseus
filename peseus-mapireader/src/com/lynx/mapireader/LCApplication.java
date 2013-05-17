package com.lynx.mapireader;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.DisplayMetrics;

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
	private static String token = "64ac15fa75ccaa3483bf5d04fe2875dae4c4b5f50e19d1db5e193c1534986d7b";

	private static int screenWidth = 0;
	private static int screenHeight = 0;

	private static boolean debug = false;

	@Override
	public void onCreate() {
		super.onCreate();
		SharedPreferences preferences = getSharedPreferences("settings",
				MODE_PRIVATE);

		httpService = new DefaultHttpServiceImpl();
		userAgent = String.format("mapi 1.0 peseus 1.0.0 %s %s Android %s",
				Build.MANUFACTURER, Build.MODEL, Build.VERSION.RELEASE);

		debug = preferences.getBoolean("debug", false);

		DisplayMetrics dm = getResources().getDisplayMetrics();
		screenWidth = dm.widthPixels;
		screenHeight = dm.heightPixels;
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

	public static boolean debug() {
		return debug;
	}

	public static int screenWidth() {
		return screenWidth;
	}

	public static int screenHeight() {
		return screenHeight;
	}
}
