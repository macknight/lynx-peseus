package com.lynx.lib.location.core;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.telephony.CellLocation;
import android.telephony.NeighboringCellInfo;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;

import com.lynx.lib.location.entity.LTCell;
import com.lynx.lib.location.entity.LTLocEnum.LTCellType;

/**
 * 
 * @author chris.liu
 * 
 */
public class LTCellManager extends Handler {
	private LTLocationCenter loc_center;
	private TelephonyManager tel_manager;
	private final List<LTCell> cells = new ArrayList<LTCell>();
	private int asu = 0;

	public LTCellManager(LTLocationCenter loc_center) {
		this.loc_center = loc_center;
		tel_manager = (TelephonyManager) loc_center.getContext()
				.getSystemService(Context.TELEPHONY_SERVICE);

	}

	public List<LTCell> getCells() {
		return this.cells;
	}

	public void start() {
		cellScan();
	}

	public void cellScan() {
		int mcc = 0, mnc = 0, cid = 0, lac = 0, bid = 0, sid = 0, nid = 0;
		CellLocation cell_loc = tel_manager.getCellLocation();

		if (cell_loc == null) {
			loc_center.sendEmptyMessage(LTLocationCenter.CELL_SCAN_FIN);
			return;
		}

		switch (tel_manager.getPhoneType()) {
		case TelephonyManager.PHONE_TYPE_CDMA:
			mcc = 0;
			bid = 0;
			sid = 0;
			nid = 0;

			try {
				String s = tel_manager.getNetworkOperator();
				if (s == null || s.length() != 5 && s.length() != 6) {
					s = tel_manager.getSimOperator();
				}

				if (s.length() == 5 || s.length() == 6) {
					mcc = Integer.parseInt(s.substring(0, 3));
				}

				Method mBid = cell_loc.getClass().getMethod("getBaseStationId",
						new Class[0]);
				Method mSid = cell_loc.getClass().getMethod("getSystemId",
						new Class[0]);
				Method mNid = cell_loc.getClass().getMethod("getNetworkId",
						new Class[0]);
				bid = (Integer) mBid.invoke(cell_loc, new Object[0]);
				sid = (Integer) mSid.invoke(cell_loc, new Object[0]);
				nid = (Integer) mNid.invoke(cell_loc, new Object[0]);
				Method mLat = cell_loc.getClass().getMethod(
						"getBaseStationLatitude", new Class[0]);
				Method mLng = cell_loc.getClass().getMethod(
						"getBaseStationLongitude", new Class[0]);
				int lat = (Integer) mLat.invoke(cell_loc, new Object[0]);
				int lng = (Integer) mLng.invoke(cell_loc, new Object[0]);

				cells.clear();
				LTCell cell = new LTCell(LTCellType.CDMA, mcc, sid, bid, nid,
						0, lat, lng);
				cells.add(cell);
				loc_center.sendEmptyMessage(LTLocationCenter.CELL_SCAN_FIN);
			} catch (Exception e) {
				cells.clear();
				loc_center.sendEmptyMessage(LTLocationCenter.CELL_SCAN_FIN);
			}
			break;
		case TelephonyManager.PHONE_TYPE_GSM:
			// this is a GSM Cell Phone.
			cells.clear();
			if (cell_loc instanceof GsmCellLocation) {
				try {
					GsmCellLocation gsm_cell_loc = (GsmCellLocation) cell_loc;
					cid = gsm_cell_loc.getCid();
					lac = gsm_cell_loc.getLac();
					if (!(cid <= 0 || cid == 0xFFFF)) {
						String s = tel_manager.getNetworkOperator();
						if (s == null || s.length() != 5 && s.length() != 6) {
							s = tel_manager.getSimOperator();
						}
						if (s.length() == 5 || s.length() == 6) {
							mcc = Integer.parseInt(s.substring(0, 3));
							mnc = Integer.parseInt(s.substring(3, s.length()));
						}

						if (mcc == 0 && mnc == 0) {
							cells.clear();
							loc_center
									.sendEmptyMessage(LTLocationCenter.CELL_SCAN_FIN);
							return;
						}

						LTCell cell = new LTCell(LTCellType.GSM, mcc, mnc, cid,
								lac, asu, 0, 0);
						cells.add(cell);
					}
				} catch (Exception e) {
				}
			}

			try {
				List<NeighboringCellInfo> neighbor_cells = tel_manager
						.getNeighboringCellInfo();
				if (neighbor_cells != null) {
					Method method_lac = null;
					try {
						method_lac = NeighboringCellInfo.class
								.getMethod("getLac");
					} catch (Exception e) {
						method_lac = null;
					}

					for (NeighboringCellInfo n_cell : neighbor_cells) {
						cid = n_cell.getCid();
						lac = 0;
						if (method_lac != null) {
							try {
								lac = (Integer) method_lac.invoke(n_cell);
							} catch (Exception e) {
							}
						}

						if (!(cid <= 0) || cid == 0xFFFF) {
							LTCell cell = new LTCell(LTCellType.GSM, mcc, mnc,
									cid, lac, n_cell.getRssi(), 0, 0);
							cells.add(cell);
						}
					}
					loc_center.sendEmptyMessage(LTLocationCenter.CELL_SCAN_FIN);
				}
			} catch (Exception e) {
				cells.clear();
				loc_center.sendEmptyMessage(LTLocationCenter.CELL_SCAN_FIN);
			}
			break;
		default:
			cells.clear();
			loc_center.sendEmptyMessage(LTLocationCenter.CELL_SCAN_FIN);
		}
	}

	public void stop() {
		// do nothing now.
	}

	public static int dbm(int asu) {
		if (asu >= 0 && asu <= 31)
			return -113 + 2 * asu;
		else
			return 0;
	}
}
