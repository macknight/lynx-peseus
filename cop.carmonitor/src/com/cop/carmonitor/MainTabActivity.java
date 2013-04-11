package com.cop.carmonitor;

import java.util.HashMap;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TabHost;

import com.cop.carmonitor.ui.BillFragment;
import com.cop.carmonitor.ui.DiagnoseFragment;
import com.cop.carmonitor.ui.MileageFragment;
import com.cop.carmonitor.ui.OilCostFragment;
import com.cop.carmonitor.ui.SettingsFragment;

/**
 * 
 * @author chris.liu
 * 
 */
public class MainTabActivity extends FragmentActivity implements
		RadioGroup.OnCheckedChangeListener {
	public static final String Tag = "MainTabActivity";

	private static final String TAB_TAG_OILCOST = "oilcost";
	private static final String TAB_TAG_MILEAGE = "mileage";
	private static final String TAB_TAG_BILL = "bill";
	private static final String TAB_TAG_DIAGNOSE = "diagnose";
	private static final String TAB_TAG_SETTINGS = "settings";

	private TabHost th;
	private TabManager tm;
	private RadioGroup rg_tab;
	private String cur_tag;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		th = (TabHost) findViewById(android.R.id.tabhost);
		th.setup();

		initTabs();
		th.setCurrentTabByTag(TAB_TAG_OILCOST);
		cur_tag = TAB_TAG_OILCOST;

		rg_tab = (RadioGroup) findViewById(R.id.rg_main_tabs);
		rg_tab.setOnCheckedChangeListener(this);

		if (savedInstanceState != null) {
			th.setCurrentTabByTag(savedInstanceState.getString("main_tab"));
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		String tag = null;
		switch (checkedId) {
		case R.id.rb_main_tab_oilcost:
			tag = TAB_TAG_OILCOST;
			break;
		case R.id.rb_main_tab_mileage:
			tag = TAB_TAG_MILEAGE;
			break;
		case R.id.rb_main_tab_bill:
			tag = TAB_TAG_BILL;
			break;
		case R.id.rb_main_tab_diagnose:
			tag = TAB_TAG_DIAGNOSE;
			break;
		case R.id.rb_main_tab_settings:
			tag = TAB_TAG_SETTINGS;
			break;
		}
		if (!cur_tag.equals(tag)) {
			th.setCurrentTabByTag(tag);
			cur_tag = tag;
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("main_tab", th.getCurrentTabTag());
	}

	private void initTabs() {
		tm = new TabManager(this, th, R.id.realtabcontent);

		tm.addTab(th.newTabSpec(TAB_TAG_OILCOST).setIndicator(TAB_TAG_OILCOST),
				0, OilCostFragment.class, null);
		tm.addTab(th.newTabSpec(TAB_TAG_MILEAGE).setIndicator(TAB_TAG_MILEAGE),
				1, MileageFragment.class, null);
		tm.addTab(th.newTabSpec(TAB_TAG_BILL).setIndicator(TAB_TAG_BILL), 2,
				BillFragment.class, null);
		tm.addTab(th.newTabSpec(TAB_TAG_DIAGNOSE)
				.setIndicator(TAB_TAG_DIAGNOSE), 3, DiagnoseFragment.class,
				null);
		tm.addTab(th.newTabSpec(TAB_TAG_SETTINGS)
				.setIndicator(TAB_TAG_SETTINGS), 4, SettingsFragment.class,
				null);
	}

	public static class TabManager implements TabHost.OnTabChangeListener {
		private final MainTabActivity activity;
		private final TabHost th;
		private final int container_id;
		private final HashMap<String, TabInfo> tabs = new HashMap<String, TabInfo>();
		TabInfo lst_tab;

		static final class TabInfo {
			private final String tag;
			private final int idx;
			private final Class<?> clss;
			private final Bundle args;
			private Fragment fragment;

			TabInfo(String tag, int idx, Class<?> clss, Bundle args) {
				this.tag = tag;
				this.idx = idx;
				this.clss = clss;
				this.args = args;
			}
		}

		static class DummyTabFactory implements TabHost.TabContentFactory {
			private final Context context;

			public DummyTabFactory(Context context) {
				this.context = context;
			}

			@Override
			public View createTabContent(String tag) {
				View v = new View(context);
				v.setMinimumWidth(0);
				v.setMinimumHeight(0);
				return v;
			}
		}

		public TabManager(MainTabActivity activity, TabHost th, int container_id) {
			this.activity = activity;
			this.th = th;
			this.container_id = container_id;
			th.setOnTabChangedListener(this);
		}

		public void addTab(TabHost.TabSpec tabSpec, int idx, Class<?> clss,
				Bundle args) {
			tabSpec.setContent(new DummyTabFactory(activity));
			String tag = tabSpec.getTag();

			TabInfo info = new TabInfo(tag, idx, clss, args);

			// Check to see if we already have a fragment for this tab, probably
			// from a previously saved state. If so, deactivate it, because our
			// initial state is that a tab isn't shown.
			info.fragment = activity.getSupportFragmentManager()
					.findFragmentByTag(tag);
			if (info.fragment != null && !info.fragment.isHidden()) {
				FragmentTransaction ft = activity.getSupportFragmentManager()
						.beginTransaction();
				ft.hide(info.fragment);
				ft.commit();
			}

			tabs.put(tag, info);
			th.addTab(tabSpec);
		}

		@Override
		public void onTabChanged(String tabId) {
			TabInfo new_tab = tabs.get(tabId);
			if (lst_tab != new_tab) {
				FragmentTransaction ft = activity.getSupportFragmentManager()
						.beginTransaction();
				if (lst_tab != null && new_tab != null) {
					if (lst_tab.idx < new_tab.idx) {
						ft.setCustomAnimations(R.anim.slide_left_enter,
								R.anim.slide_left_exit,
								R.anim.slide_right_enter,
								R.anim.slide_right_exit);
					} else {
						ft.setCustomAnimations(R.anim.slide_right_enter,
								R.anim.slide_right_exit,
								R.anim.slide_left_enter, R.anim.slide_left_exit);
					}
				}
				if (lst_tab != null) {
					if (lst_tab.fragment != null) {
						ft.hide(lst_tab.fragment);
					}
				}
				if (new_tab != null) {
					if (new_tab.fragment == null) {
						new_tab.fragment = Fragment.instantiate(activity,
								new_tab.clss.getName(), new_tab.args);
						ft.add(container_id, new_tab.fragment, new_tab.tag);
					} else {
						ft.show(new_tab.fragment);
					}
				}

				lst_tab = new_tab;
				ft.commit();
				activity.getSupportFragmentManager()
						.executePendingTransactions();
			}
		}
	}

}
