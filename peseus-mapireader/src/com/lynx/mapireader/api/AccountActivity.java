package com.lynx.mapireader.api;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
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
import com.lynx.mapireader.util.Rotate3D;

/**
 * 
 * @author chris.liu
 * 
 */
public class AccountActivity extends Activity {
	private GestureDetector gd;

	private Rotate3D leftInAnim;
	private Rotate3D leftOutAnim;
	private Rotate3D rightInAnim;
	private Rotate3D rightOutAnim;

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
				if (velocityX < 20 && curPage < VIEWS.length - 1) {
					viewCur.startAnimation(leftOutAnim);
					llContainer.removeAllViews();
					curPage++;
					viewNext = inflater.inflate(VIEWS[curPage], null);
					llContainer.addView(viewNext);
					viewCur = viewNext;
					viewCur.startAnimation(rightInAnim);
				} else if (velocityX > -20 && curPage > 0) {
					viewCur.startAnimation(rightOutAnim);
					llContainer.removeAllViews();
					curPage--;
					viewNext = inflater.inflate(VIEWS[curPage], null);
					llContainer.addView(viewNext);
					viewCur = viewNext;
					viewCur.startAnimation(leftInAnim);
				}
				return super.onFling(e1, e2, velocityX, velocityY);
			}
		});

		btnBack = (Button) findViewById(R.id.btn_account_back);
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
		DisplayMetrics dm = new DisplayMetrics();
		dm = getResources().getDisplayMetrics();
		int mCenterX = dm.widthPixels / 2;
		int mCenterY = dm.heightPixels / 2;

		int duration = 500;
		leftInAnim = new Rotate3D(-90, 0, mCenterX, mCenterY);
		leftInAnim.setFillAfter(true);
		leftInAnim.setDuration(duration);
		leftOutAnim = new Rotate3D(0, -90, mCenterX, mCenterY);
		leftOutAnim.setFillAfter(true);
		leftOutAnim.setDuration(duration);
		rightInAnim = new Rotate3D(90, 0, mCenterX, mCenterY);
		rightInAnim.setFillAfter(true);
		rightInAnim.setDuration(duration);
		rightOutAnim = new Rotate3D(0, 90, mCenterX, mCenterY);
		rightOutAnim.setFillAfter(true);
		rightOutAnim.setDuration(duration);
	}
}
