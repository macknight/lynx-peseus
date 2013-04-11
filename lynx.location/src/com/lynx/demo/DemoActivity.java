package com.lynx.demo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.lynx.R;
import com.lynx.demo.location.PinOnMapbarActivity;

/**
 * 
 * @author chris.liu
 * 
 */
public class DemoActivity extends Activity {
	public static final String Tag = "LTLocationDemoActivity";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.demo_main);

		ImageButton ib_pin_on_mapbar = (ImageButton) findViewById(R.id.ib_location_pin_on_mapbar);
		ib_pin_on_mapbar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(DemoActivity.this, PinOnMapbarActivity.class);
				startActivity(intent);
			}

		});
		ib_pin_on_mapbar.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					v.setBackgroundResource(R.drawable.btn_location_sel);
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					v.setBackgroundResource(R.drawable.btn_location_def);
				}
				return false;
			}
		});

		TextView tv_location_pin_on_mapbar = (TextView) findViewById(R.id.tv_location_pin_on_mapbar);
		tv_location_pin_on_mapbar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(DemoActivity.this, PinOnMapbarActivity.class);
				startActivity(intent);
			}
		});
		tv_location_pin_on_mapbar.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					((TextView) v).setTextColor(Color.YELLOW);
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					((TextView) v).setTextColor(Color.BLACK);
				}
				return false;
			}
		});
		
		
		ImageButton ib_network = (ImageButton) findViewById(R.id.ib_network_demo);
		ib_network.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(DemoActivity.this, PinOnMapbarActivity.class);
				startActivity(intent);
			}

		});
		ib_network.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					v.setBackgroundResource(R.drawable.btn_network_sel);
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					v.setBackgroundResource(R.drawable.btn_network_def);
				}
				return false;
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.location_demo, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == R.id.menu_settings) { // setting

		} else if (item.getItemId() == R.id.menu_debug) { // debug
			Intent intent = new Intent();
			intent.setClass(DemoActivity.this, DebugSettingActivity.class);
			startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}

}
