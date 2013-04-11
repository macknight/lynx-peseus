package com.lynx.demo;

import android.app.Application;
import android.util.Log;

import com.lynx.lib.location.util.LTAMapDeseder;
import com.lynx.lib.util.LTCommonUtil;
import com.lynx.lib.util.LTDBDataPair;
import com.lynx.lib.util.LTDBManager;

/**
 * 
 * @author chris.liu
 *
 */
public class DemoApplication extends Application {
	public static DemoApplication loc_demo_app;
	
	String BMAP_KEY = "80656E9C0A1F4BB0122B837D6FA9FF916D811EAB";
	boolean bmap_key_right = true;
	
	public static String AMAP_KEY_SPEC = "autonavi00spas$#@!666666";
	public static String PRODUCT_NAME = "autonavi";
	public static String AMAP_LICENSE = "401FFB6E52385325E41206A6AFF7A316";
	
	public static String APP_FLODER = "lynx";
	
	
	private static LTDBManager db_manager;
	
	private static boolean is_debug = false;
	private static LTDBDataPair lst_coord = null;
	private static LTDBDataPair lst_addr = null;
	private static String debug_url = null;
	
	@Override
	public void onCreate() {
		super.onCreate();
		loc_demo_app = this;
		
		LTAMapDeseder.setKeySpec(AMAP_KEY_SPEC);
		
		db_manager = new LTDBManager(this);
		
	}
	
	public static boolean isDebug() {
		if (db_manager.query(LTCommonUtil.DB_KEY_IS_DEBUG) == null) {
			is_debug = false;
		}
		else {
			Log.d("chris", db_manager.query(LTCommonUtil.DB_KEY_IS_DEBUG).getVal());
			is_debug = Boolean.parseBoolean(db_manager.query(LTCommonUtil.DB_KEY_IS_DEBUG).getVal());
		}
		return is_debug;
	}
	
	public static String getDebugUrl() {
		if (db_manager.query(LTCommonUtil.DB_KEY_DEBUG_API_URL) == null) {
			debug_url = null;
		}
		else {
			debug_url = db_manager.query(LTCommonUtil.DB_KEY_DEBUG_API_URL).getVal();
		}
		return debug_url;
	}
	
	public static LTDBDataPair getLstCoord() {
		lst_coord = db_manager.query(LTCommonUtil.DB_KEY_LST_COORD);
		return lst_coord;
	}
	
	public static LTDBDataPair getLstAddr() {
		lst_addr = db_manager.query(LTCommonUtil.DB_KEY_LST_ADDR);
		return lst_addr;
	}
	
	@Override
	public void onTerminate() {
		super.onTerminate();
		db_manager.closeDB();
	}
	
}
