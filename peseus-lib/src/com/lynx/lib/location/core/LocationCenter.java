package com.lynx.lib.location.core;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.lynx.lib.location.LocationListener;
import com.lynx.lib.location.entity.Address;
import com.lynx.lib.location.entity.Cell;
import com.lynx.lib.location.entity.Coord;
import com.lynx.lib.location.entity.Wifi;

/**
 * 
 * @author chris.liu
 * 
 */
public class LocationCenter extends Handler implements LocationListener {
	public static final String Tag = "LocationCenter";

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
	private CellManager cellManager = null;
	private WifiManager wifiManager = null;
	private LocateManager locManager = null;
	private RGCManager rgcManager = null;
	private ConnectivityManager connManager = null;

	private final ArrayList<LocationListener> locListeners = new ArrayList<LocationListener>();

	private static List<Cell> cells = null;
	private static List<Wifi> wifis = null;
	private static NetworkInfo networkInfo = null;
	private static DhcpInfo dhcpInfo = null;
	private static String phone = null;
	private static String deviceId = null;

	private static List<Coord> coords = null;
	private static Coord coord = null;
	private static Address addr = null;

	private static int cur_loop = 0;
	private long elapse_pre = 0; // 定位预处理耗时

	public LocationCenter(Context context) {
		this.context = context;
		cellManager = new CellManager(this);
		wifiManager = new WifiManager(this);
		locManager = new LocateManager(this);
		rgcManager = new RGCManager(this);
		connManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
	}

	@Override
	public void onLocationChanged(int status) {
		for (LocationListener l : locListeners) {
			l.onLocationChanged(status);
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
					Toast.makeText(context, "can't find any cell!",
							Toast.LENGTH_SHORT).show();
				}
			} else {
				cur_loop++;
				cellManager.start();
			}
			break;
		case WIFI_SCAN_FIN:
			Log.d(Tag, "wifi scan finished!");
			wifis = wifiManager.getWifis();
			dhcpInfo = wifiManager.getDhcpInfo();
			if (wifis == null || wifis.size() == 0) {
				Toast.makeText(context, "can't find any wifi!",
						Toast.LENGTH_SHORT).show();
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
				rgcManager.start();
			}
			break;
		case LT_LOC_REQ_FIN:

			break;
		case LT_RGC_REQ_FIN:
			Log.d(Tag, "rgc finish");
			addr = rgcManager.getAddress();
			rgcManager.stop();
			break;
		}

		onLocationChanged(msg.what);
	}

	public void addLocListener(LocationListener listener) {
		this.locListeners.add(listener);
	}

	public Context getContext() {
		return this.context;
	}

	/**
	 * get current coordinate of your cell phone.
	 * 
	 * @return
	 */
	public static List<Coord> getCoords() {
		return coords;
	}

	public static Coord getCoord() {
		return coord;
	}

	private Coord getOptimizeCoord() {
		if (coords != null && coords.size() > 0) {
			return coords.get(0);
		}
		return null;
	}

	/**
	 * get current cell information of your cell phone.
	 * 
	 * @return
	 */
	public static List<Cell> getCells() {
		return cells;
	}

	/**
	 * get current wifis your cell phone scanned.
	 * 
	 * @return
	 */
	public static List<Wifi> getWifis() {
		return wifis;
	}

	public static DhcpInfo getDhcpInfo() {
		return dhcpInfo;
	}

	public static NetworkInfo getNetworkInfo() {
		return networkInfo;
	}

	public static String getPhone() {
		return phone;
	}

	public static String getDeviceId() {
		return deviceId;
	}

	/**
	 * get current location address.
	 */
	public static Address getAddress() {
		return addr;
	}
}
