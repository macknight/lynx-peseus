package com.lynx.lib.location;

import android.location.Location;

/**
 * 
 * @author chris.liu
 * 
 */
public interface LocationService {

	/**
	 * get the best coordinate by server, if can be decided, return only one
	 * coordinate result, else, return a coordinate list
	 * 
	 * @param coords
	 * @return
	 */
	public Location getLocate();

	/**
	 * get revese geo-coding
	 * 
	 * @param coord
	 * @return
	 */
	public Location getRgc();
}
