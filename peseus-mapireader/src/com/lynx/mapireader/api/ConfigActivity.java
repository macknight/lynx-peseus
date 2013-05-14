package com.lynx.mapireader.api;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.cop.mapireader.R;
import com.lynx.lib.dataservice.HttpService;
import com.lynx.lib.dataservice.core.HttpParam;
import com.lynx.lib.dataservice.handler.HttpCallback;
import com.lynx.mapireader.LCApplication;
import com.lynx.mapireader.util.StaticParam;

/**
 * 
 * @author chris.liu
 * 
 */
public class ConfigActivity extends Activity {
	private Button btnBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_config);

		btnBack = (Button) findViewById(R.id.btn_config_back);
		btnBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ConfigActivity.this.onBackPressed();
			}
		});
	}

	private void getConfig() {
		LCApplication applocation = (LCApplication) getApplication();
		HttpService httpService = applocation.httpservice();

		HttpParam params = new HttpParam();
		httpService.post(StaticParam.URL_OTHER_CONFIG, params,
				new HttpCallback<String>() {
					@Override
					public void onLoading(long count, long current) {
						// 每1秒钟自动被回调一次
						// tvConsole.setText(current + "/" + count);
					}

					@Override
					public void onSuccess(String t) {
						Toast.makeText(ConfigActivity.this, t,
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onStart() {
						// 开始http请求的时候回调
					}

					@Override
					public void onFailure(Throwable t, String strMsg) {
						// 加载失败的时候回调
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
