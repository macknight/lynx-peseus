package com.lynx.mapireader.api;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.cop.mapireader.R;
import com.lynx.lib.dataservice.HttpService;
import com.lynx.lib.dataservice.handler.HttpCallback;
import com.lynx.lib.dataservice.impl.DefaultHttpServiceImpl;

/**
 * 
 * @author chris.liu
 * 
 */
public class HttpActivity extends Activity {

	private Button btnBack = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_http);

		btnBack = (Button) findViewById(R.id.btn_http_back);
		btnBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				HttpActivity.this.onBackPressed();
			}
		});
	}

	private void httpRequestTest() {
		HttpService httpService = new DefaultHttpServiceImpl();

		httpService.get("http://www.baidu.com", new HttpCallback<String>() {
			@Override
			public void onLoading(long count, long current) { // 每1秒钟自动被回调一次
				// tvContent.setText(current + "/" + count);
			}

			@Override
			public void onSuccess(String t) {
				// tvContent.setText(t == null ? "null" : t);
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
