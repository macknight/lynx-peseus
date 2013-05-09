package com.lynx.lib.dataservice.handler;

/**
 * 
 * @author chris.liu
 * @name EntityCallback.java
 * @update 2013-4-17 下午10:51:41
 * 
 */
public interface EntityCallback {
	void callback(long count, long current, boolean mustNotifyUI);
}
