package com.lynx.lib.util;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class LTCommonUtil {
	public static final String DB_KEY_IS_DEBUG = "is_debug";
	public static final String DB_KEY_LST_COORD = "lst_coord";
	public static final String DB_KEY_LST_ADDR = "lst_addr";
	public static final String DB_KEY_FAKE_COORD = "fake_coord";
	public static final String DB_KEY_DEBUG_API_URL = "debug_url";
	
	public static final int HTTP_CONN_TIMEOUT = 4000;
	public static final int HTTP_SOCKET_TIMEOUT = 4000;
	
	
	public static List<Bitmap> splitImage(Drawable drawable, int width, int height) {
		Bitmap bmp = ((BitmapDrawable)drawable).getBitmap();
		return splitImage(bmp, width, height);
	}
	
	public static List<Bitmap> splitImage(Bitmap bmp, int width, int height) {
		List<Bitmap> tiles = new ArrayList<Bitmap>();
		int w = bmp.getWidth();
		int h = bmp.getHeight();
		int row = w / width;
		int col = h / height;
		for (int i=0; i<col; ++i) {
			for (int j=0; j<row; ++j) {
				int x = j * width;
				int y = i * height;
				Bitmap tile = Bitmap.createBitmap(bmp, x, y, width, height);
				tiles.add(tile);
			}
		}
		return tiles;
	}
}
