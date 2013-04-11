package com.cop.carmonitor.ui;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TextView;

import com.cop.carmonitor.R;

/**
 * 
 * @author chris.liu
 * 
 */
public class AboutActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);

		TextView tv_version_info = (TextView) findViewById(R.id.tv_about_version_info);
		PackageInfo pinfo;
		try {
			pinfo = this.getPackageManager().getPackageInfo(
					this.getPackageName(), PackageManager.GET_CONFIGURATIONS);
			tv_version_info.setText(pinfo.versionName);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		WebView wv_about = (WebView) findViewById(R.id.wv_about);
		wv_about.setBackgroundColor(0x00000000);
		wv_about.getSettings().setDefaultTextEncodingName("utf-8");
		wv_about.loadUrl("file:///android_asset/about.html");
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.hold, R.anim.push_down_out);
	}

}