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
public class FeedbackActivity extends Activity {
	private Button btnBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback);

		btnBack = (Button) findViewById(R.id.btn_feedback_back);
		btnBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				FeedbackActivity.this.onBackPressed();
			}
		});
	}
}
