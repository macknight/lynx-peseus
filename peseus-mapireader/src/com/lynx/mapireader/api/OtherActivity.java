package com.lynx.mapireader.api;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.cop.mapireader.R;
import com.lynx.lib.dataservice.HttpService;
import com.lynx.lib.dataservice.NetworkManager;
import com.lynx.lib.dataservice.NetworkManager.NetworkState;
import com.lynx.lib.dataservice.core.HttpParam;
import com.lynx.lib.dataservice.handler.HttpCallback;
import com.lynx.mapireader.LCApplication;
import com.lynx.mapireader.util.URLs;

/**
 * 
 * @author chris.liu
 * 
 */
public class OtherActivity extends Activity {

	private NetworkManager networkManager;
	private TextView tvResult = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_other);

		networkManager = new NetworkManager(this);

		tvResult = (TextView) findViewById(R.id.tv_other_result);
		tvResult.setMovementMethod(ScrollingMovementMethod.getInstance());

		Button btn = (Button) findViewById(R.id.btn_back);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				OtherActivity.this.onBackPressed();
			}
		});

		btn = (Button) findViewById(R.id.btn_other_config);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (networkManager.state() == NetworkState.NETWORK_NONE) {
					tvResult.setText("未连接网络");
				} else {
					getConfig();
				}

			}
		});
		btn = (Button) findViewById(R.id.btn_other_feedback);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (networkManager.state() == NetworkState.NETWORK_NONE) {
					tvResult.setText("未连接网络");
				} else {
					feedback("加油，希望越来越好");
				}

			}
		});

	}

	private void getConfig() {
		HttpService httpService = LCApplication.httpservice();
		HttpParam params = new HttpParam();
		params.put("token", LCApplication.token());
		params.put("ua", LCApplication.userAgent());
		httpService.get(URLs.URL_OTHER_CONFIG, new HttpCallback<String>() {
			@Override
			public void onStart() {
				tvResult.setText("开始网络请求");
			}

			@Override
			public void onLoading(long count, long current) {
				tvResult.setText("网络请求中...");
			}

			@Override
			public void onSuccess(String t) {
				tvResult.setText(t == null ? "null" : t);
			}

			@Override
			public void onFailure(Throwable t, String strMsg) {
				tvResult.setText("网络请求失败");
			}
		});
	}

	private void feedback(String content) {
		HttpService httpService = LCApplication.httpservice();
		HttpParam params = new HttpParam();
		params.put("token", LCApplication.token());
		params.put("ua", LCApplication.userAgent());
		params.put("content", content);
		httpService.post(URLs.URL_OTHER_FEEDBACK, new HttpCallback<String>() {
			@Override
			public void onStart() {
				tvResult.setText("开始网络请求");
			}

			@Override
			public void onLoading(long count, long current) {
				tvResult.setText("网络请求中...");
			}

			@Override
			public void onSuccess(String t) {
				tvResult.setText(t == null ? "null" : t);
			}

			@Override
			public void onFailure(Throwable t, String strMsg) {
				tvResult.setText("网络请求失败");
			}
		});
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.slide_right_enter,
				R.anim.slide_right_exit);
	}
}
