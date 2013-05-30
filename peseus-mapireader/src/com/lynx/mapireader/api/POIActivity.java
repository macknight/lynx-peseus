package com.lynx.mapireader.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.SimpleAdapter;

import com.cop.mapireader.R;
import com.lynx.lib.dataservice.HttpService;
import com.lynx.lib.dataservice.core.HttpParam;
import com.lynx.lib.dataservice.handler.HttpCallback;
import com.lynx.mapireader.LCApplication;
import com.lynx.mapireader.util.URLs;
import com.lynx.widget.pulltorefreshlistview.PullToRefreshListView;

/**
 * 
 * @author chris.liu
 * 
 */
public class POIActivity extends Activity implements OnItemClickListener {
	private PullToRefreshListView ptrlvGeoArea;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_poi);

		ptrlvGeoArea = (PullToRefreshListView) findViewById(R.id.ptrlv_poi_geo_area);

		SimpleAdapter adapter = new SimpleAdapter(this, getData(),
				R.layout.layout_poi_geoarea_item, new String[] { "title" },
				new int[] { R.id.tv_poi_geo_area_item_title });

		ptrlvGeoArea.setAdapter(adapter);
		ptrlvGeoArea.setOnUpdateTask(null);
		ptrlvGeoArea.setOnItemClickListener(this);

		Button btn = (Button) findViewById(R.id.btn_back);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				POIActivity.this.onBackPressed();
			}
		});
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

	private void getGeoArea() {
		HttpService httpService = LCApplication.httpservice();
		HttpParam params = new HttpParam();
		params.put("token", LCApplication.token());
		params.put("ua", LCApplication.userAgent());
		httpService.get(URLs.URL_OTHER_CONFIG, new HttpCallback<String>() {
			@Override
			public void onStart() {

			}

			@Override
			public void onLoading(long count, long current) {

			}

			@Override
			public void onSuccess(String t) {

			}

			@Override
			public void onFailure(Throwable t, String strMsg) {

			}
		});
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.slide_right_enter,
				R.anim.slide_right_exit);
	}
}
