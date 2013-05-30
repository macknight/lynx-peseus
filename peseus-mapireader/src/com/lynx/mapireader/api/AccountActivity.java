package com.lynx.mapireader.api;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.cop.mapireader.R;
import com.lynx.mapireader.LCApplication;
import com.lynx.mapireader.util.Rotate3D;

/**
 * 
 * @author chris.liu
 * 
 */
public class AccountActivity extends Activity {
	private GestureDetector gd;

	private Rotate3D upInAnim;
	private Rotate3D upOutAnim;
	private Rotate3D downInAnim;
	private Rotate3D downOutAnim;

	private LayoutInflater inflater;
	private LinearLayout llContainer;
	private View viewCur, viewNext;
	private Button btnBack;

	private static final int[] VIEWS = { R.layout.layout_account_register,
			R.layout.layout_account_update, R.layout.layout_account_upload };

	private static int curPage = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account);
		inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		llContainer = (LinearLayout) findViewById(R.id.ll_account_container);

		initAnim();

		viewNext = inflater.inflate(VIEWS[curPage], null);
		llContainer.addView(viewNext);
		viewCur = viewNext;

		gd = new GestureDetector(this, new SimpleOnGestureListener() {
			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2,
					float velocityX, float velocityY) {
				Log.d("Tag", velocityX + "");
				if (velocityY < 20 && curPage < VIEWS.length - 1) {
					viewCur.startAnimation(upOutAnim);
					llContainer.removeAllViews();
					curPage++;
					viewNext = inflater.inflate(VIEWS[curPage], null);
					llContainer.addView(viewNext);
					viewCur = viewNext;
					viewCur.startAnimation(downInAnim);
				} else if (velocityY > -20 && curPage > 0) {
					viewCur.startAnimation(downOutAnim);
					llContainer.removeAllViews();
					curPage--;
					viewNext = inflater.inflate(VIEWS[curPage], null);
					llContainer.addView(viewNext);
					viewCur = viewNext;
					viewCur.startAnimation(upInAnim);
				}
				return super.onFling(e1, e2, velocityX, velocityY);
			}
		});

		btnBack = (Button) findViewById(R.id.btn_back);
		btnBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AccountActivity.this.onBackPressed();
			}
		});
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return gd.onTouchEvent(event);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.slide_right_enter,
				R.anim.slide_right_exit);
	}

	public void initAnim() {
		int mCenterX = LCApplication.screenWidth() / 2;
		int mCenterY = LCApplication.screenHeight() / 2;

		int duration = 500;
		upInAnim = new Rotate3D(90, 0, mCenterX, mCenterY, Rotate3D.VERTICAL);
		upInAnim.setFillAfter(true);
		upInAnim.setDuration(duration);
		upOutAnim = new Rotate3D(0, 90, mCenterX, mCenterY, Rotate3D.VERTICAL);
		upOutAnim.setFillAfter(true);
		upOutAnim.setDuration(duration);
		downInAnim = new Rotate3D(-90, 0, mCenterX, mCenterY, Rotate3D.VERTICAL);
		downInAnim.setFillAfter(true);
		downInAnim.setDuration(duration);
		downOutAnim = new Rotate3D(0, -90, mCenterX, mCenterY,
				Rotate3D.VERTICAL);
		downOutAnim.setFillAfter(true);
		downOutAnim.setDuration(duration);
	}
}
