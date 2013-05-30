package com.lynx.lib.location.core;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.lynx.lib.location.entity.Address;
import com.lynx.lib.location.entity.Coord;

/**
 * 
 * @author chris.liu
 *
 */
public class RGCManager {
	public static final String Tag = "LTRGCManager";
	
	private static final int RGC_CONN_TIMEOUT = 2000;
	private static final int RGC_SOCKET_TIMEOUT = 2100;
	
	private static final String G_GEOCODE_URL = "http://maps.googleapis.com/maps/api/geocode/json?latlng=#lat#,#lng#&sensor=false&language=zh-CN";
	
	private static Address addr;
	private GRGCTask grgc_task;
	private static LocationCenter loc_center;
	
	public RGCManager(LocationCenter loc_center) {
		RGCManager.loc_center = loc_center;
	}
	
	public Address getAddress() {
		return addr;
	}
	
	public void start() {
		grgc_task = new GRGCTask();
		grgc_task.setCoord(LocationCenter.getCoord());
		grgc_task.run();
	}
	
	public void stop() {
		grgc_task = null;
	}
	
	private static String getCityNameByCache(Coord coord) {
		
		return null;
	}
	
	
	private static class GRGCTask implements Runnable {
		private Coord coord;
		
		public GRGCTask() {}
		
		public void setCoord(Coord coord) {
			this.coord = coord;
		}
		
		@Override
		public void run() {
			Log.d(Tag, "start to do GeoCoding from google");
			if (coord == null) {
				loc_center.sendEmptyMessage(LocationCenter.LT_RGC_REQ_FIN);
				return;
			}
			
			String city  = getCityNameByCache(coord);
			String region = null;
			String street = null;
			HttpClient http_client = null;
			HttpGet http_get = null;
			try {
				HttpParams http_params = new BasicHttpParams();
				HttpConnectionParams.setConnectionTimeout(http_params, RGC_CONN_TIMEOUT);
				HttpConnectionParams.setSoTimeout(http_params, RGC_SOCKET_TIMEOUT);
				http_client = new DefaultHttpClient(http_params);
				http_client = new DefaultHttpClient();
				String url = G_GEOCODE_URL.replace("#lat#", coord.getLat() + "").replaceAll("#lng#", coord.getLng() + "");
				Log.d(Tag, new String(url));
				http_get = new HttpGet(url);
				HttpResponse resp = http_client.execute(http_get);
				HttpEntity entity = resp.getEntity();
				byte[] buf = null;
				if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					buf = EntityUtils.toByteArray(entity);
				}
				entity.consumeContent();
				if (buf != null) {
					JSONObject jo_result = new JSONObject(new String(buf, "utf-8"));
					String status = jo_result.getString("status");
					if ("OK".equals(status)) {
						int cur_length = 0;
						try {
							JSONArray ja_results = jo_result.getJSONArray("results");
							for (int i=0; i<ja_results.length(); ++i) {
								JSONArray ja_addr_components = ja_results.getJSONObject(i).getJSONArray("address_components");
								if (ja_addr_components.length() < cur_length) {
									continue;
								}
								cur_length = ja_addr_components.length();
								for (int j=0; j<ja_addr_components.length(); ++j) {
									String long_name = ja_addr_components.getJSONObject(j).getString("long_name");
//									String short_name = ja_addr_components.getJSONObject(j).getString("short_name");
									JSONArray ja_type = ja_addr_components.getJSONObject(j).getJSONArray("types");
									for (int k=0; k<ja_type.length(); ++k) {
										if ("locality".equals(ja_type.getString(k))) { // city
											if (city == null) {
												city = long_name;
											}
										}
										else if ("sublocality".equals(ja_type.getString(k))) { // region
											region = long_name;
										}
										else if ("route".equals(ja_type.getString(k))) { // street
											street = long_name;
										}
									}
								}
							}
						}
						catch (JSONException e) {
							e.printStackTrace();
						}
						
						if (city == null) {
							city = getCityNameByCache(coord);
						}
						
						if (street == null) {
							street = coord.getLat() + "," + coord.getLng();
						}
						
						addr = new Address(city, region, street);
						return;
					}
				}
			}
			catch (Exception e) {
				e.printStackTrace();
				if (city != null) {
					addr = new Address("", "", 
							coord.getLat() + "," + coord.getLng());
				}
				
			}
			finally {
				if (http_get != null) {
					http_get.abort();
					http_get = null;
				}
				if (http_client != null) {
					http_client.getConnectionManager().shutdown();
					http_client = null;
				}
				
				loc_center.sendEmptyMessage(LocationCenter.LT_RGC_REQ_FIN);
			}
		}
	}
}
