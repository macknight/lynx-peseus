package com.cop.widget;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;

import com.cop.carmonitor.R;

/**
 * 
 * @author chris.liu
 * 
 */
public class CornerAdapter extends SimpleAdapter {
	
	public CornerAdapter(Context context,
			List<? extends Map<String, ?>> data, int resource, String[] from,
			int[] to) {
		super(context, data, resource, from, to);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView != null) {
			return convertView;
		}
		
		convertView = super.getView(position, convertView, parent);
		if (getCount() > 1) {
			if (position == 0) {
				convertView
						.setBackgroundResource(R.drawable.sel_list_item_first);
			} else if (position == getCount() - 1) {
				convertView
						.setBackgroundResource(R.drawable.sel_list_item_last);
			} else {
				convertView
						.setBackgroundResource(R.drawable.sel_list_item_mid);
			}
		} else {
			convertView
					.setBackgroundResource(R.drawable.sel_list_item_single);
		}
		return convertView;
	}

}
