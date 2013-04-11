package com.cop.carmonitor.ui;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.cop.carmonitor.R;
import com.cop.carmonitor.entity.OilBill;

/**
 * 
 * @author chris.liu
 * 
 */
public class BillFragment extends Fragment {

	private List<OilBill> billGroups = new ArrayList<OilBill>();
	private List<List<OilBill>> subBills = new ArrayList<List<OilBill>>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_bill, null);
		loadingData();
		ExpandableListView elvBill = (ExpandableListView) v
				.findViewById(R.id.elv_bill);

		MyexpandableListAdapter adapter = new MyexpandableListAdapter(
				this.getActivity());
		elvBill.setAdapter(adapter);

		return v;
	}

	private void loadingData() {
		billGroups.clear();
		subBills.clear();

		OilBill bill = null;
		List<OilBill> subBill = null;

		subBill = new ArrayList<OilBill>();
		bill = new OilBill(60.0, 580.8, 232242523452l);
		subBill.add(bill);
		bill = new OilBill(60.0, 580.8, 232242523452l);
		subBill.add(bill);
		subBills.add(subBill);
		bill = new OilBill(120.0, 1160.8, 232242523452l);
		billGroups.add(bill);

		subBill = new ArrayList<OilBill>();
		bill = new OilBill(60.0, 580.8, 232242523452l);
		subBill.add(bill);
		bill = new OilBill(60.0, 580.8, 232242523452l);
		subBill.add(bill);
		bill = new OilBill(60.0, 580.8, 232242523452l);
		subBill.add(bill);
		bill = new OilBill(60.0, 580.8, 232242523452l);
		subBill.add(bill);
		subBills.add(subBill);
		bill = new OilBill(240.0, 2320.8, 232242523452l);
		billGroups.add(bill);

		subBill = new ArrayList<OilBill>();
		bill = new OilBill(60.0, 580.8, 232242523452l);
		subBill.add(bill);
		bill = new OilBill(60.0, 580.8, 232242523452l);
		subBill.add(bill);
		bill = new OilBill(60.0, 580.8, 232242523452l);
		subBill.add(bill);
		subBills.add(subBill);
		bill = new OilBill(180.0, 2320.8, 232242523452l);
		billGroups.add(bill);
	}

	private class MyexpandableListAdapter extends BaseExpandableListAdapter {
		private LayoutInflater inflater;

		public MyexpandableListAdapter(Context context) {
			inflater = LayoutInflater.from(context);
		}

		@Override
		public int getGroupCount() {
			return billGroups.size();
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			return subBills.get(groupPosition).size();
		}

		@Override
		public Object getGroup(int groupPosition) {
			return billGroups.get(groupPosition);
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			return subBills.get(groupPosition).get(childPosition);
		}

		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.bill_item, null);
			}
			OilBill bill = billGroups.get(groupPosition);

			TextView tvAddtime = (TextView) convertView
					.findViewById(R.id.tv_bill_item_addtime);
			tvAddtime.setText(new Date(bill.getAddtime()).getMonth() + "ÔÂ");
			TextView tvOil = (TextView) convertView
					.findViewById(R.id.tv_bill_item_oil);
			tvOil.setText(bill.getOil() + "L");
			TextView tvCost = (TextView) convertView
					.findViewById(R.id.tv_bill_item_cost);
			tvCost.setText("£¤" + bill.getCost());
			// if (isExpanded) {
			// convertView.setBackground(background);
			// } else {
			// convertView.setBackground(background);
			// }
			return convertView;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.bill_sub_item, null);
			}
			OilBill bill = subBills.get(groupPosition).get(childPosition);

			TextView tvAddtime = (TextView) convertView
					.findViewById(R.id.tv_bill_sub_item_addtime);
			tvAddtime.setText(new Date(bill.getAddtime()).getMonth() + "ÔÂ");
			TextView tvOil = (TextView) convertView
					.findViewById(R.id.tv_bill_sub_item_oil);
			tvOil.setText(bill.getOil() + "L");
			TextView tvCost = (TextView) convertView
					.findViewById(R.id.tv_bill_sub_item_cost);
			tvCost.setText("£¤" + bill.getCost());
			return convertView;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}
	}
}
