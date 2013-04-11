package com.lynx.demo.location;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.lynx.R;
import com.lynx.demo.DemoApplication;
import com.lynx.lib.location.LTLocStatusListener;
import com.lynx.lib.location.core.LTLocationCenter;
import com.lynx.lib.location.entity.LTAddress;
import com.lynx.lib.location.entity.LTCoord;
import com.lynx.lib.location.entity.LTLocEnum.LTCoordSource;
import com.lynx.lib.util.LTCommonUtil;
import com.mapbar.android.maps.GeoPoint;
import com.mapbar.android.maps.MapActivity;
import com.mapbar.android.maps.MapController;
import com.mapbar.android.maps.MapView;
import com.mapbar.android.maps.Overlay;

/**
 * 
 * @author chris.liu
 *
 */
public class PinOnMapbarActivity extends MapActivity implements LTLocStatusListener {
	private static final String Tag = "LocDemoActivity";
	
	private MapView mv;
	private MapController map_controller;
	private GeoPoint def_point;
	
	private ImageView iv_loc_state;
	private ImageView iv_refresh;
	private TextView tv_loc_info;
	private Animation animation;
	
	private LTLocationCenter loc_center = null;
	private Map<LTCoordSource, Bitmap> markers = null;
	private Map<LTCoordSource, Integer> colors = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pin_on_mapbar);
		
		LinearInterpolator lir = new LinearInterpolator();
		animation = (AnimationSet)AnimationUtils.loadAnimation(this, R.anim.anim_rotate);
		animation.setInterpolator(lir);
		
		loc_center = new LTLocationCenter(this.getApplicationContext());
		loc_center.addLocListener(this);
		
		tv_loc_info = (TextView)findViewById(R.id.tv_mapbar_loc_info);
		
		iv_loc_state = (ImageView)findViewById(R.id.iv_mapbar_loc_state);

		LinearLayout detail_panel = (LinearLayout)findViewById(R.id.ll_mapbar_tip_panel);
		if (DemoApplication.isDebug()) {
			detail_panel.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Intent intent = new Intent();
					intent.setClass(PinOnMapbarActivity.this, LocationInfoActivity.class);
					startActivity(intent);
				}
			});
		}
		else {
			detail_panel.setVisibility(View.GONE);
		}
		
		
		iv_refresh = (ImageView)findViewById(R.id.iv_mapbar_refresh);
		iv_refresh.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				Log.d(Tag, "start to refresh");
				iv_refresh.startAnimation(animation);
				if (!mv.getOverlays().isEmpty())
					mv.getOverlays().clear();
				tv_loc_info.setText("");
				iv_loc_state.setImageDrawable(getResources().getDrawable(R.drawable.gray_point));
				loc_center.start();
			}
		});
		
		mv = (MapView)findViewById(R.id.mv_mapbar);
		mv.setBuiltInZoomControls(true);
        map_controller = mv.getController();
        
        if (LTLocationCenter.getCoord() != null) {
        	def_point = new GeoPoint((int)(LTLocationCenter.getCoord().getLat() * 1E6), 
        			(int)(LTLocationCenter.getCoord().getLng() * 1E6));// set shanghai as the default center
        	updateLocate();
        }
        else {
        	def_point = new GeoPoint((int)(31.215999 * 1E6), (int)(121.419996 * 1E6));// set shanghai as the default center
        }
        
        if (LTLocationCenter.getAddress() != null) {
        	updateAddress();
        }
        map_controller.setCenter(def_point);
        map_controller.setZoom(mv.getMaxZoomLevel() - 1);
        
        initDebugPanel();
	}

	private void initDebugPanel() {
		Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.markers);
		List<Bitmap> tiles = LTCommonUtil.splitImage(bmp, 24, 32);
		markers = new HashMap<LTCoordSource, Bitmap>();
		colors = new HashMap<LTCoordSource, Integer>();
		LinearLayout debug_panel = (LinearLayout)findViewById(R.id.ll_mapbar_tip_panel);
		Random random = new Random();
		if (tiles.size() > 9) {
			markers.put(LTCoordSource.ACELL, tiles.get(0));
			colors.put(LTCoordSource.ACELL, Color.argb(60, random.nextInt(255), random.nextInt(255), random.nextInt(255)));
			markers.put(LTCoordSource.AWIFI, tiles.get(1));
			colors.put(LTCoordSource.AWIFI, Color.argb(60, random.nextInt(255), random.nextInt(255), random.nextInt(255)));
			markers.put(LTCoordSource.BCELL, tiles.get(2));
			colors.put(LTCoordSource.BCELL, Color.argb(60, random.nextInt(255), random.nextInt(255), random.nextInt(255)));
			markers.put(LTCoordSource.BWIFI, tiles.get(3));
			colors.put(LTCoordSource.BWIFI, Color.argb(60, random.nextInt(255), random.nextInt(255), random.nextInt(255)));
			markers.put(LTCoordSource.GCELL, tiles.get(4));
			colors.put(LTCoordSource.GCELL, Color.argb(60, random.nextInt(255), random.nextInt(255), random.nextInt(255)));
			markers.put(LTCoordSource.GWIFI, tiles.get(5));
			colors.put(LTCoordSource.GWIFI, Color.argb(60, random.nextInt(255), random.nextInt(255), random.nextInt(255)));
			markers.put(LTCoordSource.NETWORK, tiles.get(6));
			colors.put(LTCoordSource.NETWORK, Color.argb(60, random.nextInt(255), random.nextInt(255), random.nextInt(255)));
			markers.put(LTCoordSource.GPS, tiles.get(7));
			colors.put(LTCoordSource.GPS, Color.argb(60, random.nextInt(255), random.nextInt(255), random.nextInt(255)));
			markers.put(LTCoordSource.UNKNOWN, tiles.get(8));
			colors.put(LTCoordSource.UNKNOWN, Color.argb(60, random.nextInt(255), random.nextInt(255), random.nextInt(255)));
			for (LTCoordSource source : markers.keySet()) {
				TextView tv_tip = new TextView(this);
				LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				params.setMargins(5, 2, 5, 2);
				params.gravity = Gravity.LEFT;
				tv_tip.setLayoutParams(params);
				tv_tip.setGravity(Gravity.CENTER_VERTICAL);
				int color = colors.get(source);
				tv_tip.setTextColor(Color.rgb(Color.red(color), Color.green(color), Color.blue(color)));
				tv_tip.setTextSize(getResources().getDimension(R.dimen.font_size_xsmall));
				tv_tip.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
				tv_tip.setText(source.toString());
				BitmapDrawable drawable = new BitmapDrawable(getResources(), markers.get(source));
				tv_tip.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
				debug_panel.addView(tv_tip);
			}
		}
	}
	
	@Override
	public void onLocStatusChanged(int status) {
		switch (status) {
		case LTLocationCenter.LOC_REQ_FIN:
			updateLocate();
			break;
		case LTLocationCenter.LT_RGC_REQ_FIN:
			updateAddress();
			break;
		}
	}
	
	private void updateLocate() {
		try {
			GeoPoint geo_point = new GeoPoint((int)(LTLocationCenter.getCoord().getLat() * 1E6), 
					(int)(LTLocationCenter.getCoord().getLng() * 1E6));
			map_controller.animateTo(geo_point);
			
			List<Overlay> overlays = mv.getOverlays();
			overlays.clear();
			
			for (LTCoord coord : LTLocationCenter.getCoords()) {
				LTMapbarMarker marker = new LTMapbarMarker(coord);
				overlays.add(marker);
			}
			
			iv_loc_state.setImageDrawable(getResources().getDrawable(R.drawable.green_point));
			iv_refresh.clearAnimation();			
		}
		catch (Exception e) { }
	}
	
	private void updateAddress() {
		LTAddress addr = LTLocationCenter.getAddress();
		if (addr != null) {
			tv_loc_info.setText(addr.getStreet());
		}
	}
	
	private class LTMapbarMarker extends Overlay {
		private LTCoord coord;
		private float accuracy;
		
		public LTMapbarMarker(LTCoord coord) {
			this.coord = coord;
			this.accuracy = (int)coord.getAcc();
		}
		
		@Override
		public void draw(Canvas canvas, MapView mapView, boolean shadow) {
			super.draw(canvas, mapView, shadow);
			
			Point screen_coord = new Point();
			GeoPoint geo_point = new GeoPoint((int)(coord.getLat() * 1E6), 
					(int)(coord.getLng() * 1E6));
			mv.getProjection().toPixels(geo_point, screen_coord);
			
			Paint paint = new Paint();
			paint.setAntiAlias(true);
			paint.setStrokeWidth(2);
			paint.setStyle(Paint.Style.FILL_AND_STROKE);
			paint.setColor(colors.get(coord.getSource()));
			Paint paint_marker = new Paint();
			Bitmap bmp = markers.get(coord.getSource());
			if (bmp == null) {
				bmp = BitmapFactory.decodeResource(getResources(), R.drawable.cur_pos);	
			}
			canvas.drawBitmap(bmp, screen_coord.x - bmp.getWidth() / 2, 
					screen_coord.y - bmp.getHeight() / 2, paint_marker);
			canvas.drawCircle(screen_coord.x, screen_coord.y, 
					mv.getProjection().metersToEquatorPixels(this.accuracy), paint); 
		}
	}
}
