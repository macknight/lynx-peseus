package com.lynx.mapireader;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.cop.mapireader.R;

/**
 * 
 * @author chris.liu
 * 
 */
public class SettingsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

		Button btn = (Button) findViewById(R.id.btn_back);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SettingsActivity.this.onBackPressed();
			}
		});

		// FragmentManager fragmentManager = getSupportFragmentManager();
		// FragmentTransaction ft = fragmentManager.beginTransaction();
		// Fragment fragment1 = new DemoFragment();
		// ft.replace(R.id.ll_settings_container, fragment1);
		// ft.addToBackStack(null);
		// ft.commit();
	}

	private class DemoFragment extends PreferenceFragment {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);

			// ---load the preferences from an XML file---
			addPreferencesFromResource(R.xml.preferences);
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.hold, R.anim.push_up_out);
	}

}
