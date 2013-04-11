package com.cop.carmonitor.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.cop.carmonitor.R;

/**
 * 
 * @author chris.liu
 * 
 */
public class DiagnoseFragment extends Fragment {

	private boolean isDiagnosing = false;
	private ListView lv_diagnose = null;
	private ImageButton ib_diagnose = null;
	private Animation animRotate = null;
	private List<Map<String, Object>> DIAGNOSE_DATA = new ArrayList<Map<String, Object>>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_diagnose, null);
		
		lv_diagnose = (ListView) v.findViewById(R.id.lv_diagnose_main);
		initData();
		SimpleAdapter adapter = new SimpleAdapter(this.getActivity(), DIAGNOSE_DATA,
				R.layout.diagnose_main_item, new String[] {"icon", "desc"}, 
				new int[] {R.id.iv_diagnose_main_item_icon, R.id.tv_diagnose_main_item_text});
		lv_diagnose.setAdapter(adapter);
		
		LinearInterpolator lir = new LinearInterpolator();
		animRotate = (AnimationSet) AnimationUtils.loadAnimation(
				this.getActivity(), R.anim.rotate);
		animRotate.setInterpolator(lir);

		ib_diagnose = (ImageButton) v.findViewById(R.id.ib_diagnose_main_start);

		ib_diagnose.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!isDiagnosing) {
					v.startAnimation(animRotate);
					diagnose();
				}
			}
		});
		return v;
	}

	private void initData() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("icon", R.drawable.diagnose_fdjdzzz);
		map.put("desc", "发动机电子装置");
		DIAGNOSE_DATA.add(map);
		
		map = new HashMap<String, Object>();
		map.put("icon", R.drawable.diagnose_bsxdzsb);
		map.put("desc", "变速箱电子设备");
		DIAGNOSE_DATA.add(map);
		
		map = new HashMap<String, Object>();
		map.put("icon", R.drawable.diagnose_zydzmk);
		map.put("desc", "中央电子设备");
		DIAGNOSE_DATA.add(map);
		
		map = new HashMap<String, Object>();
		map.put("icon", R.drawable.diagnose_syj);
		map.put("desc", "收音机");
		DIAGNOSE_DATA.add(map);
		
		map = new HashMap<String, Object>();
		map.put("icon", R.drawable.diagnose_aqqn);
		map.put("desc", "收音机");
		DIAGNOSE_DATA.add(map);
		
		map = new HashMap<String, Object>();
		map.put("icon", R.drawable.diagnose_ckccmdzsb);
		map.put("desc", "收音机");
		DIAGNOSE_DATA.add(map);
		
		map = new HashMap<String, Object>();
		map.put("icon", R.drawable.diagnose_dlzx);
		map.put("desc", "收音机");
		DIAGNOSE_DATA.add(map);
		
		map = new HashMap<String, Object>();
		map.put("icon", R.drawable.diagnose_fqds);
		map.put("desc", "收音机");
		DIAGNOSE_DATA.add(map);
		
		map = new HashMap<String, Object>();
		map.put("icon", R.drawable.diagnose_jsyccmdzsb);
		map.put("desc", "收音机");
		DIAGNOSE_DATA.add(map);
		
		map = new HashMap<String, Object>();
		map.put("icon", R.drawable.diagnose_sjzxzdjk);
		map.put("desc", "收音机");
		DIAGNOSE_DATA.add(map);
		
		map = new HashMap<String, Object>();
		map.put("icon", R.drawable.diagnose_tcfzzz);
		map.put("desc", "收音机");
		DIAGNOSE_DATA.add(map);
		
		map = new HashMap<String, Object>();
		map.put("icon", R.drawable.diagnose_ybb);
		map.put("desc", "收音机");
		DIAGNOSE_DATA.add(map);
		
		map = new HashMap<String, Object>();
		map.put("icon", R.drawable.diagnose_yhcmdzsb);
		map.put("desc", "收音机");
		DIAGNOSE_DATA.add(map);
		
		map = new HashMap<String, Object>();
		map.put("icon", R.drawable.diagnose_zdqdzxt);
		map.put("desc", "收音机");
		DIAGNOSE_DATA.add(map);
		
		map = new HashMap<String, Object>();
		map.put("icon", R.drawable.diagnose_zhcmdzsb);
		map.put("desc", "收音机");
		DIAGNOSE_DATA.add(map);
		
		map = new HashMap<String, Object>();
		map.put("icon", R.drawable.diagnose_zks);
		map.put("desc", "中控锁");
		DIAGNOSE_DATA.add(map);
	}
	
	private void diagnose() {
//		isDiagnosing = true;

//		ib_diagnose.clearAnimation();
//		isDiagnosing = false;
	}
}
