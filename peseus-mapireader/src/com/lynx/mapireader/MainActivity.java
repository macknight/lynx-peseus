package com.lynx.mapireader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
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
import com.lynx.mapireader.api.ConfigActivity;
import com.lynx.mapireader.api.FeedbackActivity;
import com.lynx.mapireader.api.HttpActivity;
import com.lynx.mapireader.api.VersionActivity;
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
				R.layout.api_item, new String[] { "icon", "title", "desc",
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
		// Inflate the menu; this adds items to the action bar if it is present.
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
		map.put("icon", R.drawable.mapi_items_config);
		map.put("title", "配置");
		map.put("desc", "app配置接口,config");
		map.put("status", R.drawable.star_gray);
		data.add(map);

		map = new HashMap<String, Object>();
		map.put("icon", R.drawable.mapi_items_feedback);
		map.put("title", "反馈");
		map.put("desc", "用户意见反馈,feedback");
		map.put("status", R.drawable.star_gray);
		data.add(map);

		map = new HashMap<String, Object>();
		map.put("icon", R.drawable.mapi_items_version);
		map.put("title", "版本更新");
		map.put("desc", "检查app是否有版本更新");
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
		case 3: // config
			intent.setClass(this, ConfigActivity.class);
			break;
		case 4: // feedback
			intent.setClass(this, FeedbackActivity.class);
			break;
		case 5: // version
			intent.setClass(this, VersionActivity.class);
			break;
		default:
			intent.setClass(this, HttpActivity.class);
			break;
		}
		startActivity(intent);
		overridePendingTransition(R.anim.slide_left_enter,
				R.anim.slide_right_exit);
	}
}
