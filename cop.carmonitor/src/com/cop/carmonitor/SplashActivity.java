package com.cop.carmonitor;

import java.lang.reflect.Method;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

/**
 * 
 * @author chris.liu
 * 
 */
public class SplashActivity extends Activity {

	public static final int DELAY = 1000;
	private Animation animRotate;

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 1) {
				startActivity(new Intent(SplashActivity.this,
						MainTabActivity.class));
				try {
					Method m = Activity.class.getDeclaredMethod(
							"overridePendingTransition", Integer.TYPE,
							Integer.TYPE);
					m.invoke(SplashActivity.this, R.anim.splash_fade,
							R.anim.splash_hold);
				} catch (Exception e) {

				}
				SplashActivity.this.finish();
			} else {
				SplashActivity.this.finish();
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		LinearInterpolator lir = new LinearInterpolator();
		animRotate = (AnimationSet) AnimationUtils.loadAnimation(this,
				R.anim.rotate);
		animRotate.setInterpolator(lir);

		ImageView ivIcon = (ImageView) findViewById(R.id.iv_splash_icon);
		ivIcon.startAnimation(animRotate);
		
		handler.sendEmptyMessageDelayed(1, DELAY);
	}

}
