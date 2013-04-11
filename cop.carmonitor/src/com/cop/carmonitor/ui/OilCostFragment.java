package com.cop.carmonitor.ui;

import java.util.Random;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.cop.carmonitor.R;
import com.cop.widget.PieChartView;
import com.cop.widget.PieChartView.OnSliceClickListener;

/**
 * 
 * @author chris.liu
 * 
 */
public class OilCostFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d("chris", "onCreateView");
		View v = inflater.inflate(R.layout.fragment_oilcost, null);

		LinearLayout ll_container = (LinearLayout) v
				.findViewById(R.id.ll_oilcost_main_container);

		PieChartView p = new PieChartView(this.getActivity());
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, 0);
		p.setLayoutParams(lp);
		p.setBackgroundColor(Color.TRANSPARENT);
		p.setOnSliceClickListener(new OnSliceClickListener() {
			@Override
			public void onSliceClicked(PieChartView pieChart, int sliceNumber) {
				Toast.makeText(OilCostFragment.this.getActivity(),
						"slice: " + sliceNumber, Toast.LENGTH_SHORT).show();
			}
		});

		Random random = new Random();
		float[] data = new float[2 + random.nextInt(3)];
		for (int i = 0; i < data.length; i++) {
			data[i] = random.nextFloat();
		}
		p.setSlices(data);

		ll_container.addView(p);
		p.startAnimation();

		Button btn = (Button) v.findViewById(R.id.btn_oilcost_detail);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});
		return v;
	}

	@Override
	public void onViewStateRestored(Bundle savedInstanceState) {
		Log.d("chris", "onViewStateRestored");
		super.onViewStateRestored(savedInstanceState);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		Log.d("chris", "onSaveInstanceState");
	}

	@Override
	public void onResume() {
		Log.d("chris", "onResume");
		super.onResume();
	}
}
