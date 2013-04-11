package com.cop.carmonitor.ui;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cop.carmonitor.R;
import com.cop.widget.CornerAdapter;

/**
 * 
 * @author chris.liu
 * 
 */
public class SettingsFragment extends Fragment implements OnItemClickListener {

	private int[] moreItemIcons = { R.drawable.settings_items_privacy_icon, -1,
			R.drawable.settings_items_skin_icon,
			R.drawable.settings_items_officialweibo_icon,
			R.drawable.settings_items_feedback_icon, -1,
			R.drawable.settings_items_version,
			R.drawable.settings_items_about_icon, -1,
			R.drawable.settings_items_accountsafe_icon };

	private String[] moreItemTitles = { "浏览设置", "", "软件推荐", "好友分享", "反馈", "",
			"版本更新", "关于我们", "", "关注我们" };

	private String[] moreItemMethods = { "setting", "", "market", "shareApp",
			"feedback", "", "update", "about", "", "donate" };

	private HashMap<String, String> moreItemMethodMap = new HashMap<String, String>();

	// private ProgressDialog pd;

	private List<List<Map<String, Object>>> listDatas = new ArrayList<List<Map<String, Object>>>();
	private LinearLayout cornerContainer = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_settings, null);

		cornerContainer = (LinearLayout) v
				.findViewById(R.id.ll_settings_main_container);
		for (int i = 0; i < moreItemTitles.length; ++i) {
			if (!"".equals(moreItemTitles[i])) {
				moreItemMethodMap.put(moreItemTitles[i], moreItemMethods[i]);
			}
		}

		setListData();

		int size = listDatas.size();
		for (int i = 0; i < size; ++i) {
			generateSubSettingListView(i, size, listDatas.get(i));
		}

		return v;
	}

	public void setListData() {
		List<Map<String, Object>> list_data = new ArrayList<Map<String, Object>>();
		Map<String, Object> map;
		for (int i = 0; i < moreItemTitles.length; i++) {
			if ("".equals(moreItemTitles[i])) {
				listDatas.add(list_data);
				list_data = new ArrayList<Map<String, Object>>();
			} else {
				map = new HashMap<String, Object>();
				map.put("icon", Integer.valueOf(moreItemIcons[i]));
				map.put("text", moreItemTitles[i]);
				list_data.add(map);
			}
		}

		listDatas.add(list_data);
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view,
			int position, long id) {
		TextView tv = (TextView) view
				.findViewById(R.id.tv_settings_main_item_text);
		String key = tv.getText().toString();
		Class<?> clss = this.getClass();
		try {
			Method m = clss.getMethod(moreItemMethodMap.get(key));
			m.invoke(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private ListView generateSubSettingListView(int groupId, int size,
			List<Map<String, Object>> data) {
		ListView listView;
		LayoutParams lp;
		CornerAdapter adapter;

		listView = new ListView(this.getActivity());
		lp = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		if (groupId == 0 && groupId == (size - 1)) {
			lp.setMargins(10, 12, 10, 12);
		} else if (groupId == 0) {
			lp.setMargins(10, 12, 10, 6);
		} else if (groupId == (size - 1)) {
			lp.setMargins(10, 6, 10, 12);
		} else {
			lp.setMargins(10, 6, 10, 12);
		}

		listView.setLayoutParams(lp);
		listView.setCacheColorHint(0);
		listView.setDivider(null);
		listView.setVerticalScrollBarEnabled(false);
		listView.setSelector(R.drawable.sel_list_item_def);
		listView.setOnItemClickListener(this);
		listView.setScrollbarFadingEnabled(false);
		cornerContainer.addView(listView);

		adapter = new CornerAdapter(this.getActivity(), data,
				R.layout.settings_item, new String[] { "icon", "text" },
				new int[] { R.id.iv_settings_main_item_icon,
						R.id.tv_settings_main_item_text });
		listView.setAdapter(adapter);
		int height = data.size()
				* (int) getResources().getDimension(
						R.dimen.settings_item_height);
		height += 1;
		listView.getLayoutParams().height = height;

		return listView;
	}

	public void setting() {
		//
		Toast.makeText(this.getActivity(), "invoke method setting()",
				Toast.LENGTH_SHORT).show();
	}

	public void test() {
		Toast.makeText(this.getActivity(), "invoke method test()",
				Toast.LENGTH_SHORT).show();
	}

	public void test1() {
		Toast.makeText(this.getActivity(), "invoke method test()",
				Toast.LENGTH_SHORT).show();
	}

	public void test2() {
		Toast.makeText(this.getActivity(), "invoke method test()",
				Toast.LENGTH_SHORT).show();
	}

	public void test3() {
		Toast.makeText(this.getActivity(), "invoke method test()",
				Toast.LENGTH_SHORT).show();
	}

	public void market() {
		//
		Toast.makeText(this.getActivity(), "invoke method market()",
				Toast.LENGTH_SHORT).show();
	}

	public void shareApp() {
		//
		Toast.makeText(this.getActivity(), "invoke method shareApp()",
				Toast.LENGTH_SHORT).show();
	}

	public void feedback() {
		//
		Toast.makeText(this.getActivity(), "invoke method feedback()",
				Toast.LENGTH_SHORT).show();
	}

	public void update() {
		//
		Toast.makeText(this.getActivity(), "invoke method update()",
				Toast.LENGTH_SHORT).show();
	}

	public void about() {
		//
		Intent intent = new Intent(this.getActivity(), AboutActivity.class);
		startActivity(intent);
		this.getActivity().overridePendingTransition(R.anim.push_up_in,
				R.anim.hold);
	}

	public void donate() {
		//
		Toast.makeText(this.getActivity(), "invoke method donate()",
				Toast.LENGTH_SHORT).show();
	}

}
