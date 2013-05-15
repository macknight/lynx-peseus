package com.lynx.lib.location.core;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.lynx.lib.location.LTLocStatusListener;
import com.lynx.lib.location.entity.LTAddress;
import com.lynx.lib.location.entity.LTCell;
import com.lynx.lib.location.entity.LTCoord;
import com.lynx.lib.location.entity.LTLocEnum.LTCoordSource;
import com.lynx.lib.location.entity.LTWifi;
import com.lynx.lib.util.LTCommonUtil;
import com.lynx.lib.util.LTDBDataPair;
import com.lynx.lib.util.LTDBManager;

/**
 * 
 * @author chris.liu
 *
 */
public class LTLocationCenter extends Handler implements LTLocStatusListener {
	public static final String Tag = "LTLocationCenter";
	
	public static final int CELL_SCAN_FIN = 1;
	public static final int WIFI_SCAN_FIN = 2;
	public static final int LOC_REQ_FIN = 4;
	public static final int LT_LOC_REQ_FIN = 5;
	public static final int LT_RGC_REQ_FIN = 6;
	
	public static final int TIMEOUT_TOTAL = 12000;
	public static final int INTERVAL_CELL = 200;
	public static final int TIMEOUT_CELL = 1200;
	public static final int TIMEOUT_WIFI = 8000;
	
	private Context context = null;
	private LTCellManager cellManager = null;
	private LTWifiManager wifiManager = null;
	private LTLocateManager locManager = null;
	private LTRGCManager rgcManager = null;
	private ConnectivityManager connManager = null;
	private LTDBManager dbManager = null;
	
	private final ArrayList<LTLocStatusListener> locListeners = new ArrayList<LTLocStatusListener>();
	
	private static List<LTCell> cells = null;
	private static List<LTWifi> wifis = null;
	private static NetworkInfo networkInfo = null;
	private static DhcpInfo dhcpInfo = null;
	private static String phone = null;
	private static String deviceId = null;
	
	private static List<LTCoord> coords = null;
	private static LTCoord coord = null;
	private static LTAddress addr = null;
	
	
	private static int cur_loop = 0;
	private long elapse_pre = 0; // 定位预处理耗时
	
	
	public LTLocationCenter(Context context) {
		this.context = context;
		cellManager = new LTCellManager(this);
		wifiManager = new LTWifiManager(this);
		locManager = new LTLocateManager(this);
		rgcManager = new LTRGCManager(this);
		connManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		
		dbManager = new LTDBManager(context);
	}
	
	@Override
	public void onLocStatusChanged(int status) {
		for (LTLocStatusListener l : locListeners) {
			l.onLocStatusChanged(status);
		}
	}
	
	public void start() {
		cellManager.cellScan();
		wifiManager.wifiScan();
		networkInfo = connManager.getActiveNetworkInfo();
		locManager.start();
	}
	
	@Override
	public void handleMessage(Message msg) {
		switch (msg.what) {
		case CELL_SCAN_FIN:
			if (cur_loop == TIMEOUT_CELL / INTERVAL_CELL) {
				Log.d(Tag, String.format("cell scan(%d/6) finished!", cur_loop));
				cells = cellManager.getCells();
				locManager.cellLocate();
				cur_loop = 0;
				if (cells == null || cells.size() == 0) {
					Toast.makeText(context, "can't find any cell!", Toast.LENGTH_SHORT).show();
				}
			}
			else {
				cur_loop ++;
				cellManager.start();
			}
			break;
		case WIFI_SCAN_FIN:
			Log.d(Tag, "wifi scan finished!");
			wifis = wifiManager.getWifis();
			dhcpInfo = wifiManager.getDhcpInfo();
			if (wifis == null || wifis.size() == 0) {
				Toast.makeText(context, "can't find any wifi!", Toast.LENGTH_SHORT).show();
			}
			locManager.wifiLocate();
			break;
		case LOC_REQ_FIN:
			Log.d(Tag, "locate finished(LocateCenter)!");
			coords = locManager.getCoords();
			locManager.stop();
			// do locate from l.api.dianping.com
			// whatever msg the client got will pass to the server
			elapse_pre = System.currentTimeMillis() - elapse_pre;
			coord = getOptimizeCoord();
			
			if (coord != null) {
				dbManager.update(new BasicNameValuePair(LTCommonUtil.DB_KEY_LST_COORD, coord.toString()));
				rgcManager.start();
			}
			else {
				LTDBDataPair lst_coord = dbManager.query(LTCommonUtil.DB_KEY_LST_COORD);
				if (lst_coord != null && lst_coord.getVal() != null) {
					if (System.currentTimeMillis() - lst_coord.getAddTime() < 300000) {
						// last locate occurs in 5 minutes
						try {
							JSONObject jo_coord = new JSONObject(lst_coord.getVal());
							coord = new LTCoord();
							coord.setSource(LTCoordSource.valueOf(jo_coord.getString("source")));
							coord.setLat(jo_coord.getDouble("lat"));
							coord.setLng(jo_coord.getDouble("lng"));
							coord.setAcc(jo_coord.getInt("acc"));
							coord.setElapse(jo_coord.getLong("elapse"));
						}
						catch (JSONException e) {
							
						}
					}
				}
			}
			break;
		case LT_LOC_REQ_FIN:
			
			break;
		case LT_RGC_REQ_FIN:
			Log.d(Tag, "rgc finish");
			addr = rgcManager.getAddress();
			rgcManager.stop();
			
			if (addr != null) {
				dbManager.update(new BasicNameValuePair(LTCommonUtil.DB_KEY_LST_ADDR, addr.toString()));
			}
			else {
				LTDBDataPair lst_addr = dbManager.query(LTCommonUtil.DB_KEY_LST_ADDR);
				if (lst_addr != null && lst_addr.getVal() != null) {
					if (System.currentTimeMillis() - lst_addr.getAddTime() < 300000) {
						// last locate occurs in 5 minutes
						try {
							JSONObject jo_addr = new JSONObject(lst_addr.getVal());
							addr = new LTAddress();
							addr.setCityName(jo_addr.getString("city_name"));
							addr.setGeoRegion(jo_addr.getString("geo_region"));
							addr.setStreet(jo_addr.getString("street"));
						}
						catch (JSONException e) {
							
						}
					}
				}
			}
			
			break;
		}
		
		onLocStatusChanged(msg.what);
	}
	
	public void addLocListener(LTLocStatusListener listener) {
		this.locListeners.add(listener);
	}

	public Context getContext() {
		return this.context;
	}

	/**
	 * get current coordinate of your cell phone.
	 * @return
	 */
	public static List<LTCoord> getCoords() {
		return coords;
	}
	
	public static LTCoord getCoord() {
		return coord;
	}
	
	private LTCoord getOptimizeCoord() {
		if (coords != null && coords.size() > 0) {
			return coords.get(0);
		}
		return null;
	}
	
	/**
	 * get current cell information of your cell phone.
	 * @return
	 */
	public static List<LTCell> getCells() {
		return cells;
	}
	
	/**
	 * get current wifis your cell phone scanned.
	 * @return
	 */
	public static List<LTWifi> getWifis() {
		return wifis;
	}
	
	public static  DhcpInfo getDhcpInfo() {
		return dhcpInfo;
	}
	
	public static NetworkInfo getNetworkInfo() {
		return networkInfo;
	}
	
	public static String getPhone() {
		return phone;
	}
	
	public static  String getDeviceId() {
		return deviceId;
	}
	
	/**
	 * get current location address.
	 */
	public static LTAddress getAddress() {
		return addr;
	}
}
