package com.lynx.mapireader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleAdapter;

import com.cop.mapireader.R;
import com.lynx.mapireader.api.AccountActivity;
import com.lynx.mapireader.api.BizActivity;
import com.lynx.mapireader.api.HttpActivity;
import com.lynx.mapireader.api.OtherActivity;
import com.lynx.mapireader.api.POIActivity;
import com.lynx.widget.pulltorefreshlistview.PullToRefreshListView;

/**
 * 
 * @author chris.liu
 * @name MainActivity.java
 * @update 2013-4-17 下午11:33:17
 * 
 */
public class MainActivity extends Activity implements OnItemClickListener {

	private LayoutAnimationController lac;
	private PullToRefreshListView ptrlvMain;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ptrlvMain = (PullToRefreshListView) findViewById(R.id.ptrlv_main);

		Animation anim = AnimationUtils.loadAnimation(this, R.anim.item_float);
		lac = new LayoutAnimationController(anim);
		lac.setOrder(Animation.ZORDER_BOTTOM);

		SimpleAdapter adapter = new SimpleAdapter(this, getData(),
				R.layout.layout_main_api_item, new String[] { "icon", "title", "desc",
						"status" }, new int[] { R.id.iv_api_item_icon,
						R.id.tv_api_item_title, R.id.tv_api_item_desc,
						R.id.iv_api_item_status });

		ptrlvMain.setAdapter(adapter);
		ptrlvMain.setOnUpdateTask(null);
		ptrlvMain.setLayoutAnimation(lac);
		ptrlvMain.setOnItemClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private List<Map<String, Object>> getData() {
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("icon", R.drawable.mapi_items_account);
		map.put("title", "账号");
		map.put("desc", "账号相关接口调用,包括register,login,bind等");
		map.put("status", R.drawable.star_green);
		data.add(map);

		map = new HashMap<String, Object>();
		map.put("icon", R.drawable.mapi_items_biz);
		map.put("title", "业务");
		map.put("desc", "app业务相关接口访问...");
		map.put("status", R.drawable.star_orange);
		data.add(map);

		map = new HashMap<String, Object>();
		map.put("icon", R.drawable.mapi_items_about);
		map.put("title", "POI");
		map.put("desc", "poi业务相关接口访问...");
		map.put("status", R.drawable.star_orange);
		data.add(map);

		map = new HashMap<String, Object>();
		map.put("icon", R.drawable.mapi_items_config);
		map.put("title", "其他");
		map.put("desc", "config,用户意见反馈,版本更新等");
		map.put("status", R.drawable.star_gray);
		data.add(map);

		map = new HashMap<String, Object>();
		map.put("icon", R.drawable.mapi_items_network);
		map.put("title", "通用网络请求");
		map.put("desc", "通用网络请求入口");
		map.put("status", R.drawable.star_green);
		data.add(map);

		return data;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_settings:
			Intent intent = new Intent();
			intent.setClass(this, SettingsActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.push_up_in, R.anim.hold);
			break;
		}
		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent();
		switch (position) {
		case 1: // account
			intent.setClass(this, AccountActivity.class);
			break;
		case 2: // biz
			intent.setClass(this, BizActivity.class);
			break;
		case 3: // poi
			intent.setClass(this, POIActivity.class);
			break;
		case 4: // other
			intent.setClass(this, OtherActivity.class);
			break;
		default: // http
			intent.setClass(this, HttpActivity.class);
			break;
		}
		startActivity(intent);
		overridePendingTransition(R.anim.slide_left_enter,
				R.anim.slide_left_exit);
	}
}
