package com.lynx.lib.dataservice.handler;

/**
 * 
 * @author chris.liu
 * @name HttpCallback.java
 * @update 2013-4-17 下午10:52:48
 * @param <T>
 *
 */
public abstract class HttpCallback<T> {
	private boolean progress = true;
	private int rate = 1000 * 1;// 每秒

	public boolean isProgress() {
		return progress;
	}

	public int rate() {
		return rate;
	}

	/**
	 * 
	 * @param progress
	 *            是否启用进度显示
	 * @param rate
	 *            进度更新频率
	 * @return
	 */
	public HttpCallback<T> progress(boolean progress, int rate) {
		this.progress = progress;
		this.rate = rate;

		return this;
	}

	public void onStart() {
	}

	public void onLoading(long count, long current) {
	};

	public void onSuccess(T t) {
	};

	public void onFailure(Throwable t, String strMsg) {
	};
}
