package com.cop.carmonitor.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cop.carmonitor.R;

/**
 * 
 * @author chris.liu
 *
 */
public class MileageFragment extends Fragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_mileage, null);
		
		return v;
	}
}
