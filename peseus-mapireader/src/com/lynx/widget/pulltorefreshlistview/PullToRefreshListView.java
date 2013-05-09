package com.lynx.widget.pulltorefreshlistview;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cop.mapireader.R;

/**
 * 
 * @author chris.liu
 * 
 */
public class PullToRefreshListView extends RefreshableListView {

	public PullToRefreshListView(final Context context, AttributeSet attrs) {
		super(context, attrs);

		setContentView(R.layout.pull_to_refresh_listview);
		mListHeaderView.setBackgroundColor(Color.TRANSPARENT);
		setOnHeaderViewChangedListener(new OnHeaderViewChangedListener() {

			@Override
			public void onViewChanged(View v, boolean canUpdate) {
				Log.d("View", "onViewChanged" + canUpdate);
				TextView tv = (TextView) v.findViewById(R.id.tv_refresh_tip);
				ImageView img = (ImageView) v
						.findViewById(R.id.iv_refresh_icon);
				Animation anim;
				if (canUpdate) {
					anim = AnimationUtils.loadAnimation(context,
							R.anim.rotate_up);
					tv.setText(R.string.refresh_release_tip);
				} else {
					tv.setText(R.string.refresh_pull_down_tip);
					anim = AnimationUtils.loadAnimation(context,
							R.anim.rotate_down);
				}
				img.startAnimation(anim);
			}

			@Override
			public void onViewUpdating(View v) {
				Log.d("View", "onViewUpdating");
				TextView tv = (TextView) v.findViewById(R.id.tv_refresh_tip);
				ImageView img = (ImageView) v
						.findViewById(R.id.iv_refresh_icon);
				ProgressBar pb = (ProgressBar) v
						.findViewById(R.id.pb_refresh_loading);
				pb.setVisibility(View.VISIBLE);
				tv.setText(R.string.loading_tip);
				img.clearAnimation();
				img.setVisibility(View.INVISIBLE);
			}

			@Override
			public void onViewUpdateFinish(View v) {
				Log.d("View", "onViewUpdateFinish");
				TextView tv = (TextView) v.findViewById(R.id.tv_refresh_tip);
				ImageView img = (ImageView) v
						.findViewById(R.id.iv_refresh_icon);
				ProgressBar pb = (ProgressBar) v
						.findViewById(R.id.pb_refresh_loading);

				tv.setText(R.string.refresh_pull_down_tip);
				pb.setVisibility(View.INVISIBLE);
				tv.setVisibility(View.VISIBLE);
				img.setVisibility(View.VISIBLE);
			}
		});
	}
}
