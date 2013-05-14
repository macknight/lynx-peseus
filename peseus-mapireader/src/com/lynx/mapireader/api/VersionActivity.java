package com.lynx.mapireader.api;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.cop.mapireader.R;

/**
 * 
 * @author chris.liu
 * 
 */
public class VersionActivity extends Activity {
	private Button btnBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_version);

		btnBack = (Button) findViewById(R.id.btn_version_back);
		btnBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				VersionActivity.this.onBackPressed();
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
