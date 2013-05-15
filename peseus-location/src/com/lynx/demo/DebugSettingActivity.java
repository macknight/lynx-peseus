package com.lynx.demo;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;

import com.lynx.R;
import com.lynx.lib.location.entity.LTCoord;
import com.lynx.lib.location.entity.LTLocEnum.LTCoordSource;
import com.lynx.lib.util.LTCommonUtil;
import com.lynx.lib.util.LTDBDataPair;
import com.lynx.lib.util.LTDBManager;

/**
 * 
 * @author chris.liu
 *
 */
public class DebugSettingActivity extends Activity {
	
	private LTDBManager db_manager;
	
	private TextView tv_lst_coord_addtime;
	private TextView tv_lst_coord;
	private TextView tv_lst_addr_addtime;
	private TextView tv_lst_addr;
	
	private EditText et_fake_lat, et_fake_lng, et_fake_acc;
	private EditText et_debug_api_url;
	
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.debug_panel);
		
		db_manager = new LTDBManager(this);
		
		tv_lst_coord_addtime = (TextView)findViewById(R.id.tv_debug_lst_coord_addtime);
		tv_lst_coord = (TextView)findViewById(R.id.tv_debug_lst_coord);
		tv_lst_addr_addtime = (TextView)findViewById(R.id.tv_debug_lst_addr_addtime);
		tv_lst_addr = (TextView)findViewById(R.id.tv_debug_lst_addr);
		
		et_fake_lat = (EditText)findViewById(R.id.et_debug_fake_location_lat);
		et_fake_lng = (EditText)findViewById(R.id.et_debug_fake_location_lng);
		et_fake_acc = (EditText)findViewById(R.id.et_debug_fake_location_acc);
		et_debug_api_url = (EditText)findViewById(R.id.et_debug_api_url);
		
		LTDBDataPair data = db_manager.query(LTCommonUtil.DB_KEY_LST_COORD);
		if (data != null) {
			tv_lst_coord.setText(data.getVal());
			tv_lst_coord_addtime.setText(sdf.format(new Date(data.getAddTime())));
		}
		data = db_manager.query(LTCommonUtil.DB_KEY_LST_ADDR);
		if (data != null) {
			tv_lst_addr.setText(data.getVal());
			tv_lst_addr_addtime.setText(sdf.format(new Date(data.getAddTime())));
		}
		
		et_debug_api_url.setHint(DemoApplication.getDebugUrl());
		
		Button btn_submit = (Button)findViewById(R.id.bt_debug_location_fake_submit);
		btn_submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try {
					double lat = Double.parseDouble(et_fake_lat.getText().toString());
					double lng = Double.parseDouble(et_fake_lng.getText().toString());
					int acc = Integer.parseInt(et_fake_acc.getText().toString());
					LTCoord coord = new LTCoord(LTCoordSource.UNKNOWN, lat, lng, acc, 0);
					
					Log.d("chris", coord.toString());
					db_manager.update(new BasicNameValuePair(LTCommonUtil.DB_KEY_FAKE_COORD, coord.toString()));
				}
				catch (Exception e) {
					
				}
				
				Log.d("chris", et_debug_api_url.getText().toString());
				db_manager.update(new BasicNameValuePair(LTCommonUtil.DB_KEY_DEBUG_API_URL, et_debug_api_url.getText().toString()));
			}
		});
		
		Button btn_cancel = (Button)findViewById(R.id.bt_debug_location_fake_cancel);
		btn_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
			}
		});
		
		CheckBox cb_debug = (CheckBox)findViewById(R.id.cb_debug_isdebug);
		cb_debug.setChecked(DemoApplication.isDebug());
		cb_debug.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				db_manager.update(new BasicNameValuePair(LTCommonUtil.DB_KEY_IS_DEBUG, String.valueOf(isChecked)));
			}
		});
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		db_manager.closeDB();
	}
	
}
