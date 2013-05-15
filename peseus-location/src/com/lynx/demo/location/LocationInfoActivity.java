package com.lynx.demo.location;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.lynx.R;
import com.lynx.demo.DemoApplication;
import com.lynx.lib.location.core.LTLocationCenter;
import com.lynx.lib.location.entity.LTCell;
import com.lynx.lib.location.entity.LTCoord;
import com.lynx.lib.location.entity.LTWifi;

/**
 * 
 * @author chris.liu
 *
 */
public class LocationInfoActivity extends Activity {
	public static final String Tag = "LTLocationInfoActivity";
	
	private static final List<BasicNameValuePair> sys_infos = new ArrayList<BasicNameValuePair>();
	
	static {
		sys_infos.add(new BasicNameValuePair("product", android.os.Build.PRODUCT));
		sys_infos.add(new BasicNameValuePair("cpu_abi", android.os.Build.CPU_ABI));
		sys_infos.add(new BasicNameValuePair("tags", android.os.Build.TAGS));
		sys_infos.add(new BasicNameValuePair("version_codes_base", android.os.Build.VERSION_CODES.BASE + ""));
		sys_infos.add(new BasicNameValuePair("model", android.os.Build.MODEL));
		sys_infos.add(new BasicNameValuePair("sdk", android.os.Build.VERSION.SDK_INT + ""));
		sys_infos.add(new BasicNameValuePair("version_release", android.os.Build.VERSION.RELEASE));
		sys_infos.add(new BasicNameValuePair("device", android.os.Build.DEVICE));
		sys_infos.add(new BasicNameValuePair("display", android.os.Build.DISPLAY));
		sys_infos.add(new BasicNameValuePair("brand", android.os.Build.BRAND));
		sys_infos.add(new BasicNameValuePair("borad", android.os.Build.BOARD));
		sys_infos.add(new BasicNameValuePair("finger_print", android.os.Build.FINGERPRINT));
		sys_infos.add(new BasicNameValuePair("id", android.os.Build.ID));
		sys_infos.add(new BasicNameValuePair("manufacturer", android.os.Build.MANUFACTURER));
		sys_infos.add(new BasicNameValuePair("user", android.os.Build.USER));
	}
	
	private TableLayout tl_cell_info = null;
	private TableLayout tl_wifi_info = null;
	private TableLayout tl_coord_info = null;
	private TableLayout tl_sys_info = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.location_info);
		
		tl_cell_info = (TableLayout)findViewById(R.id.tl_loc_info_cell);
		tl_wifi_info = (TableLayout)findViewById(R.id.tl_loc_info_wifi);
		tl_coord_info = (TableLayout)findViewById(R.id.tl_loc_info_coord);
		tl_sys_info = (TableLayout)findViewById(R.id.tl_loc_info_sys);
		
		List<LTCell> cells = LTLocationCenter.getCells();
		float[] cell_weights = {0.5f, 0.5f, 1.0f, 1.0f, 1.0f, 0.5f};
		if (cells != null && cells.size() > 0) {
			for (int i=0; i<cells.size(); ++i) {
				List<String> data = new ArrayList<String>();
				LTCell cell = cells.get(i);
				data.add(cell.getType().getName());
				data.add(cell.getMCC() + "");
				data.add(cell.getMNC() + "");
				data.add(cell.getCID() + "");
				data.add(cell.getLAC() + "");
				data.add(cell.getASU() + "");
				addRow(tl_cell_info, data, i == cells.size()-1, i, cell_weights);
			}
		}
		
		List<LTWifi> wifis = LTLocationCenter.getWifis();
		float[] wifi_weights = {0.5f, 1.0f, 0.3f};
		if (wifis != null && wifis.size() > 0) {
			for (int i=0; i<wifis.size(); ++i) {
				List<String> data = new ArrayList<String>();
				LTWifi wifi = wifis.get(i);
				data.add(wifi.getSsid());
				data.add(wifi.getMac());
				data.add(wifi.getDBm() + "");
				addRow(tl_wifi_info, data, i == wifis.size()-1, i, wifi_weights);
			}
		}
		
		List<LTCoord> coords = LTLocationCenter.getCoords();
		float[] coord_weights = {1.0f, 1.0f, 1.0f, 0.7f, 1.0f};
		if (coords != null && coords.size() > 0) {
			for (int i=0; i<coords.size(); ++i) {
				List<String> data = new ArrayList<String>();
				LTCoord coord = coords.get(i);
				data.add(coord.getSource().getName());
				data.add(coord.getLat() + "");
				data.add(coord.getLng() + "");
				data.add(coord.getAcc() + "");
				data.add(coord.getElapse() + "");
				addRow(tl_coord_info, data, i == coords.size()-1, i, coord_weights);
			}
		}
		
		float[] sys_weights = {0.3f, 0.6f};
		for (int i=0; i<sys_infos.size(); ++i) {
			List<String> data = new ArrayList<String>();
			data.add(sys_infos.get(i).getName());
			data.add(sys_infos.get(i).getValue());
			addRow(tl_sys_info, data, i == sys_infos.size()-1, i, sys_weights);
		}
		
		if (DemoApplication.isDebug()) {
			Log.d(Tag, "debug model");
		}
		else {
			Log.d(Tag, "normal model");
		}
	}
	
	private void addRow(TableLayout parent, List<String> data, boolean is_last, int index, float[] weights) {
		int line_color = Color.rgb(0, 160, 160);
		int text_size = 12;
		int text_color = Color.BLACK;
		int text_bg_color_0 = Color.rgb(220, 220, 220);
		int text_bg_color_1 = Color.rgb(239, 228, 176);
		int text_bg_color = text_bg_color_0;
		View line = null;
		
		if (index % 2 == 0) {
			text_bg_color = text_bg_color_1;
		}
		else {
			text_bg_color = text_bg_color_0;
		}
		
		TableRow.LayoutParams params = null;
		
		TableRow tr = new TableRow(this);
		tr.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 
        		LayoutParams.WRAP_CONTENT));
		tr.setBackgroundColor(text_bg_color);
		
		for (int i=0; i<data.size(); ++i) {
			String str_value = data.get(i);
			TextView tv_value = new TextView(this);
			params = new TableRow.LayoutParams(0, LayoutParams.WRAP_CONTENT, weights[i]);
			tv_value.setLayoutParams(params);
			tv_value.setEllipsize(TextUtils.TruncateAt.END);
			tv_value.setSingleLine(true);
			tv_value.setGravity(Gravity.CENTER);
			tv_value.setTextSize(text_size);
			tv_value.setTextColor(text_color);
			tv_value.setText(str_value);
			tv_value.setBackgroundColor(Color.TRANSPARENT);
			tr.addView(tv_value);
			
			if (i < data.size() - 1) {
				line = new View(this);
				params = new TableRow.LayoutParams(1, LayoutParams.MATCH_PARENT);
				line.setLayoutParams(params);
				line.setBackgroundColor(line_color);
				tr.addView(line);
			}
		}		
		parent.addView(tr, new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.MATCH_PARENT));
	}
}
