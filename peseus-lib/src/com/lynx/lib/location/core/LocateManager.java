package com.lynx.lib.location.core;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.lynx.lib.location.entity.Cell;
import com.lynx.lib.location.entity.Cell.CellType;
import com.lynx.lib.location.entity.Coord;
import com.lynx.lib.location.entity.Coord.CoordSource;
import com.lynx.lib.location.entity.Wifi;
import com.lynx.lib.location.util.AMapDeseder;
import com.lynx.lib.location.util.AMapLocateResponse;
import com.lynx.lib.location.util.BMapDigester;
import com.lynx.lib.location.util.LocationUtil;

/**
 * 
 * @author chris.liu
 * 
 */
public class LocateManager {
	public static final String Tag = "LTLocateManager";

	public static final String LOCTYPE_CENTROID = "centroid";
	public static final String LOCTYPE_TOWER = "tower";
	public static final String LOCTYPE_UNKNOWN = "unknown";

	// the "waiting" location type means we made a request but are
	// still waiting on a response
	public static final String LOCTYPE_WAITING = "waiting";

	public static final String AMAP_KEY_SPEC = "autonavi00spas$#@!666666";
	public static final String PRODUCT_NAME = "autonavi";
	public static final String AMAP_LICENSE = "401FFB6E52385325E41206A6AFF7A316";

	private static final int LOC_REQ_FIN = 15;
	private static final int G_LOC_REQ_FIN = 1;
	private static final int B_LOC_REQ_FIN = 2;
	private static final int A_LOC_REQ_FIN = 3;
	private static final int GPS_LOC_REQ_FIN = 4;
	private static final int NETWORK_LOC_REQ_FIN = 5;

	private static final int FREQUENCY = 1000; // 监控频率
	private static final int LOC_TIMEOUT_DEF = 5000; // 默认定位总超时

	// private static final String G_LOC_URL = "http://www.google.com/loc/json";
	// private static final String G_LM_URL = "http://www.google.com/glm/mmap";
	private static final String B_LOC_URL = "http://loc.map.baidu.com/sdk.php";
	private static final String A_LOC_URL = "http://naps.amap.com/SAPS/r";

	private static LocationCenter loc_center;
	private static final List<Coord> coords = new ArrayList<Coord>();

	private static LocationManager location_manager;
	private static final Criteria criteria = new Criteria();

	private static LocateTask aloc_task = null;
	private static LocateTask bloc_task = null;

	private static long elapse_total = FREQUENCY;
	private static int cur_timeout = LOC_TIMEOUT_DEF;

	private static int loc_status = 0;

	private static Handler loc_handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what != LOC_REQ_FIN && msg.what != FREQUENCY) {
				loc_status += msg.what;
				Log.d(Tag, "cur status:" + msg.what + " done!");
			}
			if (msg.what == FREQUENCY && elapse_total < cur_timeout) { //
				elapse_total += FREQUENCY;
				Log.d(Tag, "cur elapse: " + elapse_total);
			}
			if (loc_status == LOC_REQ_FIN || elapse_total >= cur_timeout) {
				// 移除状态机
				if (monitor_task != null) {
					Log.d(Tag, "loc_handler--->monitor task cancel()");
					monitor_task.cancel();
					monitor_task = null;
				}
				if (monitor != null) {
					Log.d(Tag, "loc_handler--->monitor cancel()");
					monitor.cancel();
					monitor = null;
				}
				Log.d(Tag, "locate finished with status: " + loc_status
						+ " and elapse: " + elapse_total);
				elapse_total = 0;
				loc_status = 0;
				LocateManager.loc_center
						.sendEmptyMessage(LocationCenter.LOC_REQ_FIN);
			}
		}
	};

	/**
	 * 监控所有定位请求线程，完成后发送定位完成消息
	 * 
	 */
	private static Timer monitor;
	private static TimerTask monitor_task;

	private final LocationListener location_listener = new LocationListener() {
		@Override
		public void onLocationChanged(Location location) {
			double lat = LocationUtil.format(location.getLatitude(), 5);
			double lng = LocationUtil.format(location.getLongitude(), 5);
			Coord coord = new Coord(CoordSource.GPS, lat, lng,
					(int) location.getAccuracy(), System.currentTimeMillis()
							- location.getTime());
			if (location.getProvider().equals(LocationManager.NETWORK_PROVIDER)) {
				coord.setSource(CoordSource.NETWORK);
				coords.add(coord);
				loc_handler.sendEmptyMessage(NETWORK_LOC_REQ_FIN);
				return;
			}
			coords.add(coord);
			coord.setSource(CoordSource.GPS);
			loc_handler.sendEmptyMessage(GPS_LOC_REQ_FIN);
		}

		@Override
		public void onProviderDisabled(String provider) {
			Toast.makeText(loc_center.getContext(), "GPS服务未打开",
					Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onProviderEnabled(String provider) {
			Toast.makeText(loc_center.getContext(), "GPS定位开启一次，将在60s后关闭",
					Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// 在可用、暂时不可用和无服务三个状态直接切换时触发此函数
		}
	};

	public LocateManager(LocationCenter loc_center) {
		LocateManager.loc_center = loc_center;

		location_manager = (LocationManager) loc_center.getContext()
				.getSystemService(Context.LOCATION_SERVICE);

		// GPS精度要求
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setCostAllowed(false);
		criteria.setPowerRequirement(Criteria.POWER_LOW);
	}

	public List<Coord> getCoords() {
		return coords;
	}

	public void start() {
		Log.d(Tag, "start locate manager!");

		LocateManager.coords.clear();
		elapse_total = 0;
		loc_status = 0;

		gpsLocate();
		networkLocate();

		monitor_task = new TimerTask() {
			@Override
			public void run() {
				loc_handler.sendEmptyMessage(FREQUENCY);
			}
		};

		monitor = new Timer();
		monitor.schedule(monitor_task, FREQUENCY, FREQUENCY);
	}

	public void stop() {
		Log.d(Tag, "stop all locate request!");

		elapse_total = 0;
		loc_status = 0;

		// 移除状态机
		if (monitor != null) {
			Log.d(Tag, "stop--->monitor cancel()");
			monitor.cancel();
			monitor = null;
		}

		if (monitor_task != null) {
			Log.d(Tag, "stop--->monitor task cancel()");
			monitor_task.cancel();
			monitor_task = null;
		}

		// 移除gps定位监听
		location_manager.removeUpdates(location_listener);

		if (aloc_task != null) {
			aloc_task.stop();
			aloc_task = null;
		}

		if (bloc_task != null) {
			bloc_task.stop();
			bloc_task = null;
		}
	}

	public void cellLocate() {
		Log.d(Tag, "start cell coordinate request!");

		// 存在基站时，发送辅助定位请求
		if (LocationCenter.getCells() != null
				&& LocationCenter.getCells().size() > 0) {
			new GLocTask(LocationCenter.getCells(), null).locate();
		} else {
			Log.d(Tag, "no cell found!");
			loc_handler.sendEmptyMessage(G_LOC_REQ_FIN);
			return;
		}
	}

	public void wifiLocate() {
		Log.d(Tag, "start wifi coordinate request!");

		coords.clear();
		elapse_total = 0;
		loc_status = 0;

		// 存在基站时，发送辅助定位请求
		if (LocationCenter.getWifis() == null
				|| LocationCenter.getWifis().size() < 2) {
			// no cell and wifi, no need to do A-GPS request
			Log.d(Tag, "no cell and wifi found!");
			loc_handler.sendEmptyMessage(B_LOC_REQ_FIN);
			loc_handler.sendEmptyMessage(A_LOC_REQ_FIN);
			return;
		}

		aloc_task = new ALocTask(LocationCenter.getCells(),
				LocationCenter.getWifis(), LocationCenter.getNetworkInfo(),
				LocationCenter.getDhcpInfo());
		aloc_task.locate();

		bloc_task = new BLocTask(LocationCenter.getCells(),
				LocationCenter.getWifis());
		bloc_task.locate();
	}

	public void gpsLocate() {
		Log.d(Tag, "start gps coordinate request!");
		String provider = location_manager.getBestProvider(criteria, true);
		if (provider == null) {
			return;
		}
		// 获取坐标信息
		Location location = location_manager.getLastKnownLocation(provider);
		if (location != null
				&& SystemClock.elapsedRealtime() - location.getTime() < 30000) {
			long elapse = SystemClock.elapsedRealtime() - location.getTime();
			Coord coord = new Coord(CoordSource.GPS, location.getLatitude(),
					location.getLongitude(), (int) location.getAccuracy(),
					elapse);
			coords.add(coord);
			loc_handler.sendEmptyMessage(GPS_LOC_REQ_FIN);
		} else {
			location_manager.requestLocationUpdates(provider, 0, 0,
					location_listener);
		}
	}

	public void networkLocate() {
		Log.d(Tag, "start network coordinate request!");
		if (location_manager
				.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
			location_manager.requestLocationUpdates(
					LocationManager.NETWORK_PROVIDER, 0, 0, location_listener);
		}
	}

	private class GLocTask extends LocateTask {

		private List<Cell> cells = null;

		// private List<LTWifi> wifis = null;

		public GLocTask(List<Cell> cells, List<Wifi> wifis) {
			this.cells = cells;
			// this.wifis = wifis;
		}

		@Override
		public void locate() {
			// if no cell info exist but there more than two wifi found,
			// try wifi locate with a test cell information.
			Log.d(Tag, "begin to get locate from google");

			if (cells != null && cells.size() > 0) {

			}
		}

		@Override
		public void stop() {

		}
	} // GCellLocTask

	private class ALocTask extends LocateTask {
		private List<Cell> cells;
		private List<Wifi> wifis;
		private NetworkInfo network_info;
		private DhcpInfo dhcp_info;

		public ALocTask(List<Cell> cells, List<Wifi> wifis,
				NetworkInfo network_info, DhcpInfo dhcp_info) {
			this.cells = cells;
			this.wifis = wifis;
			this.network_info = network_info;
			this.dhcp_info = dhcp_info;
		}

		@Override
		public void locate() {
			Log.d(Tag, "begin to get locate from amap");

			if (this.wifis == null || this.wifis.size() == 0) {
				this.source = CoordSource.ACELL;
			} else {
				this.source = CoordSource.AWIFI;
			}

			new Thread() {
				@Override
				public void run() {
					HttpClient http_client = null;
					HttpPost http_post = null;
					try {
						long elapse = System.currentTimeMillis();
						String str_param = formatRequest();
						HttpParams http_params = new BasicHttpParams();
						HttpConnectionParams.setConnectionTimeout(http_params,
								cur_timeout);
						HttpConnectionParams.setSoTimeout(http_params,
								cur_timeout);
						http_client = new DefaultHttpClient(http_params);
						http_post = new HttpPost(A_LOC_URL);
						StringEntity param = new StringEntity(str_param,
								"UTF-8");
						param.setContentType("text/xml");
						http_post.setHeader("Content-Type",
								"application/soap+xml;charset=UTF-8");
						http_post.setEntity(param);
						HttpEntity http_entity = null;
						String charset = "";
						String stream_type = null;

						HttpResponse http_resp = http_client.execute(http_post);
						if ((http_resp.getHeaders("Content-Type") != null)) {
							String content_type = http_resp.getEntity()
									.getContentType().getValue();
							String[] tmp = content_type.split("\\;");
							for (int i = 0; i < tmp.length; ++i) {
								if (tmp[i].trim().toLowerCase()
										.startsWith("charset=")) {
									charset = tmp[i].trim().toLowerCase()
											.replace("charset=", "");
								}
							}
						}
						if ((http_resp.getHeaders("Content-Encoding") != null)) {
							Header[] headers = http_resp
									.getHeaders("Content-Encoding");
							for (int i = 0; i < headers.length; ++i) {
								if (headers[i].getValue() != null
										&& headers[i].getValue().length() > 0) {
									stream_type = headers[i].getValue();
									break;
								}
							}
						}
						if (http_resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
							http_entity = http_resp.getEntity();
							if (http_entity != null) {
								InputStream instream = http_entity.getContent();
								if (stream_type != null
										&& "gzip".equals(stream_type)) {
									instream = new GZIPInputStream(instream);
								}
								BufferedReader buf_reader = new BufferedReader(
										new InputStreamReader(instream, charset));
								String line = null;
								StringBuilder sb = new StringBuilder();
								while ((line = buf_reader.readLine()) != null) {
									sb.append(line);
								}
								if (!"".equals(sb.toString())) {
									String result = AMapDeseder
											.decrpyt(
													new AMapLocateResponse().ParseSapsXML(sb
															.toString()), "GBK");
									Coord coord = new AMapLocateResponse()
											.parseCoordFromXML(result);
									if (coord != null) {
										elapse = System.currentTimeMillis()
												- elapse;
										coord.setElapse(elapse);
										coord.setSource(CoordSource.AWIFI);
										Log.d(Tag,
												String.format(
														"get coord(%s) form GaoDe(%f, %f, %d)",
														source, coord.getLat(),
														coord.getLng(),
														coord.getAcc()));
										coords.add(coord);
									} else {
										Log.d(Tag, "no coord found from GaoDe!");
									}
								}
							}
						}
					} catch (Exception e) {
						Log.e(Tag, "gaode locate request error", e);
					} finally {
						if (http_post != null) {
							http_post.abort();
							http_post = null;
						}
						if (http_client != null) {
							http_client.getConnectionManager().shutdown();
							http_client = null;
						}
						loc_handler.sendEmptyMessage(A_LOC_REQ_FIN);
						Log.d(Tag, String.format(
								"get coord(%s) from GaoDe done!", source));
					}
				}
			}.start();
		}

		@Override
		public void stop() {

		}

		private String formatRequest() {
			StringBuilder sb_param = new StringBuilder();
			sb_param.append("<?xml version=\"1.0\" encoding=\"GBK\" ?>");
			sb_param.append("<location>");
			sb_param.append("<license>").append(AMAP_LICENSE)
					.append("</license>");
			sb_param.append("<src>").append(PRODUCT_NAME).append("</src>");
			sb_param.append("<imei>").append(LocationCenter.getDeviceId())
					.append("</imei>");
			sb_param.append("<network>");
			if (network_info != null && network_info.isAvailable()
					&& network_info.isConnected()) {
				TelephonyManager tel_manager = (TelephonyManager) loc_center
						.getContext().getSystemService(
								Context.TELEPHONY_SERVICE);
				sb_param.append(network_info.toString());
				sb_param.append(", countryiso: ")
						.append(tel_manager.getNetworkCountryIso())
						.append(", operatorname: ")
						.append(tel_manager.getNetworkOperatorName())
						.append(", line1number: ")
						.append(LocationCenter.getPhone());

				if (dhcp_info != null) {
					sb_param.append(
							", wifidns1: "
									+ LocationUtil
											.intToIpAddress(dhcp_info.dns1))
							.append(", wifidns2: "
									+ LocationUtil
											.intToIpAddress(dhcp_info.dns2))
							.append(", wifigateway: "
									+ LocationUtil
											.intToIpAddress(dhcp_info.gateway))
							.append(", wifiipaddr: "
									+ LocationUtil
											.intToIpAddress(dhcp_info.ipAddress));
				}
			}
			sb_param.append("</network>");

			if (cells != null && cells.size() > 0) {
				StringBuilder sb_cells = new StringBuilder();
				if (cells.get(0).getType() == CellType.GSM) {
					sb_cells.append("<cdma>0</cdma>");
					sb_cells.append("<mcc>").append(cells.get(0).getMCC())
							.append("</mcc>");
					sb_cells.append("<mnc>").append(cells.get(0).getMNC())
							.append("</mnc>");
					sb_cells.append("<lac>").append(cells.get(0).getLAC())
							.append("</lac>");
					sb_cells.append("<cellid>").append(cells.get(0).getCID())
							.append("</cellid>");
					sb_cells.append("<signal>").append(cells.get(0).getASU())
							.append("</signal>");
					/*
					 * if (cells.size() > 1) { sb_cells.append("<nb>"); for (int
					 * i=1; i<cells.size(); ++i) { Cell cell = cells.get(i);
					 * sb_cells.append("*"). append(cell.getMNC()).append(",").
					 * append(cell.getLAC()).append(",").
					 * append(cell.getCID()).append(",").append(cell.getASU());
					 * } sb_cells.append("</nb>"); }
					 */
				} else if (cells.get(0).getType() == CellType.CDMA) {
					sb_cells.append("<cdma>1</cdma>");
					Cell cell = cells.get(0);
					sb_cells.append("<mcc>").append(cell.getMCC())
							.append("</mcc>");
					sb_cells.append("<sid>").append(cell.getMNC())
							.append("</sid>");
					sb_cells.append("<nid>").append(cell.getCID())
							.append("</nid>");
					sb_cells.append("<bid>").append(cell.getLAC())
							.append("</bid>");
					sb_cells.append("<lon>").append("").append("</lon>");
					sb_cells.append("<lat>").append("").append("</lat>");
					sb_cells.append("<signal>").append(cell.getASU())
							.append("</signal>");
				}
				sb_param.append(sb_cells.toString());
			}

			if (this.wifis != null && this.wifis.size() > 0) {
				StringBuilder sb_wifis = new StringBuilder("<macs>");
				for (Wifi wifi : wifis) {
					sb_wifis.append(wifi.getMac()).append(",")
							.append(wifi.getDBm()).append("*");
				}
				sb_wifis.append("</macs>");
				sb_param.append(sb_wifis);
			}

			sb_param.append("<clienttime>")
					.append(LocationUtil.getCurrentTime())
					.append("</clienttime>");
			sb_param.append("</location>");

			StringBuilder sb2 = new StringBuilder();
			sb2.append("<?xml version=\"1.0\" encoding=\"GBK\" ?>");
			sb2.append("<saps>");
			sb2.append("<src>").append(PRODUCT_NAME).append("</src>");
			sb2.append("<sreq>")
					.append(AMapDeseder.encrypt(sb_param.toString()))
					.append("</sreq>");
			sb2.append("</saps>");
			return sb2.toString();
		}
	}

	private class BLocTask extends LocateTask {
		private List<Cell> cells;
		private List<Wifi> wifis;

		public BLocTask(List<Cell> cells, List<Wifi> wifis) {
			this.cells = cells;
			this.wifis = wifis;
		}

		@Override
		public void locate() {
			new Thread() {
				@Override
				public void run() {
					CoordSource source = CoordSource.UNKNOWN;
					if (wifis != null && wifis.size() > 0) {
						source = CoordSource.BWIFI;
					} else if (cells != null && cells.size() > 0) {
						source = CoordSource.BCELL;
					}

					long elapse = System.currentTimeMillis();
					HttpClient http_client = null;
					HttpPost http_post = null;
					try {
						String tmp = formatRequest();
						http_client = new DefaultHttpClient();

						List<BasicNameValuePair> params;
						http_post = new HttpPost(B_LOC_URL);
						params = new ArrayList<BasicNameValuePair>();
						params.add(new BasicNameValuePair("bloc", tmp));
						UrlEncodedFormEntity urlencodedformentity = new UrlEncodedFormEntity(
								params, "utf-8");
						http_post.setEntity(urlencodedformentity);

						HttpResponse http_resp = http_client.execute(http_post);
						elapse = System.currentTimeMillis() - elapse;
						if (http_resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
							String result = (EntityUtils.toString(
									http_resp.getEntity(), "utf-8"));
							JSONObject json = new JSONObject(result);
							int status_code = json.getJSONObject("result")
									.getInt("error");
							if (status_code != 167) {
								double lat = json.getJSONObject("content")
										.getJSONObject("point").getDouble("y");
								lat = LocationUtil.format(lat, 5);
								double lng = json.getJSONObject("content")
										.getJSONObject("point").getDouble("x");
								lng = LocationUtil.format(lng, 5);
								int acc = (int) json.getJSONObject("content")
										.getDouble("radius");
								Coord coord = new Coord(source, lat, lng, acc,
										elapse);
								Log.d(Tag, String.format(
										"get coord(%s) form Baidu(%f, %f, %d)",
										source, coord.getLat(), coord.getLng(),
										coord.getAcc()));
								coords.add(coord);
							}
						}
					} catch (Exception e) {
						Log.e(Tag, "baidu locate request error", e);
					} finally {
						if (http_post != null) {
							http_post.abort();
							http_post = null;
						}
						if (http_client != null) {
							http_client.getConnectionManager().shutdown();
						}
						loc_handler.sendEmptyMessage(B_LOC_REQ_FIN);
						Log.d(Tag, String.format(
								"get coord(%s) from Baidu done!", source));
					}
				}
			}.start();
		}

		@Override
		public void stop() {

		}

		public String formatRequest() {
			String tmp = "&cl=";
			if (cells == null || cells.size() == 0) {
				tmp += "0|0|-1|-1";
			} else {
				Cell cell = cells.get(0);
				tmp += cell.getMCC() + "|" + cell.getMNC() + "|"
						+ cell.getLAC() + "|" + cell.getCID() + "&clt=";
				for (int i = 0; i < cells.size(); ++i) {
					tmp += cells.get(i).getMCC() + "|" + cells.get(i).getMNC()
							+ "|" + cells.get(i).getLAC() + "|"
							+ cells.get(i).getCID() + "|1;";
				}
				tmp += "10";
			}

			if (wifis != null && wifis.size() != 0) {
				tmp += "&wf=";
				for (int i = 0; i < wifis.size(); ++i) {
					tmp += wifis.get(i).getMac().replaceAll(":", "") + ";"
							+ Math.abs(wifis.get(i).getDBm()) + ";|";
				}
				tmp = tmp.substring(0, tmp.length() - 1);
			}
			tmp += "&addr=detail&coor=gcj02&os=android&prod=default&im=";
			tmp += LocationCenter.getDeviceId();
			tmp = BMapDigester.digest(tmp) + "|tp=2";

			return tmp;
		}
	}

	private abstract class LocateTask {
		protected CoordSource source = CoordSource.UNKNOWN;

		/**
		 * start to locate current coordinate
		 */
		public abstract void locate();

		/**
		 * stop current locate action
		 */
		public abstract void stop();
	}

}
