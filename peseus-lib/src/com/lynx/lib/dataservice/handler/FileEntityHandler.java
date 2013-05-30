package com.lynx.lib.dataservice.handler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;

import android.text.TextUtils;

/**
 * 
 * @author chris.liu
 * @name FileEntityHandler.java
 * @update 2013-4-17 下午10:51:49
 *
 */
public class FileEntityHandler {
	private boolean stop = false;

	public boolean isStop() {
		return stop;
	}

	public void setStop(boolean stop) {
		this.stop = stop;
	}

	@SuppressWarnings("resource")
	public Object handleEntity(HttpEntity entity, EntityCallback callback,
			String target, boolean isResume) throws IOException {
		if (TextUtils.isEmpty(target) || target.trim().length() == 0) {
			return null;
		}
		File targetFile = new File(target);
		if (!targetFile.exists()) {
			targetFile.createNewFile();
		}
		
		if (stop) {
			return targetFile;
		}
		
		long cur = 0;
		FileOutputStream outstream = null;
		if (isResume) {
			cur = targetFile.length();
			outstream = new FileOutputStream(target, true);
		} else {
			outstream = new FileOutputStream(target);
		}
		
		if (stop) {
			return targetFile;
		}
		
		InputStream instream = entity.getContent();
		long count = entity.getContentLength() + cur;
		
		if (cur >= count || stop) {
			return targetFile;
		}
		
		int readLen = 0;
		byte[] buf = new byte[1024];
		while (!stop && !(cur >= count) && ((readLen = instream.read(buf, 0, 1024)) > 0)) {
			outstream.write(buf, 0, readLen);
			cur += readLen;
			callback.callback(count, cur, false);
		}
		callback.callback(count, cur, true);
		
		if (stop && cur < count) {
			throw new IOException("user stop download thread");
		}
		
		return targetFile;
	}
}
