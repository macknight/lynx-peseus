package com.lynx.lib.dataservice.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;


/**
 * 
 * @author chris.liu
 * @name HttpParam.java
 * @update 2013-4-17 下午9:49:29
 */
public class HttpParam {
	private static final String ENCODING = "UTF-8";

	protected ConcurrentHashMap<String, String> urlParams;
	protected ConcurrentHashMap<String, FileWrapper> fileParams;

	public HttpParam() {
		init();
	}

	public HttpParam(Map<String, String> params) {
		init();

		for (Map.Entry<String, String> entry : params.entrySet()) {
			put(entry.getKey(), entry.getValue());
		}
	}

	public HttpParam(String key, String value) {
		init();
		put(key, value);
	}

	public HttpParam(Object... keyVals) {
		init();
		int len = keyVals.length;
		if (len % 2 != 0) {
			throw new IllegalArgumentException(
					"supplied arguments must be even");
		}

		for (int i = 0; i < len; i += 2) {
			String key = String.valueOf(keyVals[i]);
			String val = String.valueOf(keyVals[i + 1]);
			put(key, val);
		}
	}

	public void put(String key, String val) {
		if (key != null && val != null) {
			urlParams.put(key, val);
		}
	}

	public void put(String key, File file) throws FileNotFoundException {
		put(key, new FileInputStream(file), file.getName());
	}

	public void put(String key, InputStream instream) {
		put(key, instream, null);
	}

	public void put(String key, InputStream instream, String filename) {
		put(key, instream, filename, null);
	}

	public void put(String key, InputStream instream, String filename,
			String contentType) {
		if (key != null && instream != null) {
			fileParams.put(key,
					new FileWrapper(instream, filename, contentType));
		}
	}

	public void remove(String key) {
		urlParams.remove(key);
		fileParams.remove(key);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (ConcurrentHashMap.Entry<String, String> entry : urlParams
				.entrySet()) {
			if (sb.length() > 0)
				sb.append("&");

			sb.append(entry.getKey());
			sb.append("=");
			sb.append(entry.getValue());
		}

		for (ConcurrentHashMap.Entry<String, FileWrapper> entry : fileParams
				.entrySet()) {
			if (sb.length() > 0)
				sb.append("&");

			sb.append(entry.getKey());
			sb.append("=");
			sb.append("FILE");
		}

		return sb.toString();
	}

	public HttpEntity getEntity() {
		HttpEntity entity = null;

		if (!fileParams.isEmpty()) {
			MultiPartEntity multiPartEntity = new MultiPartEntity();
			// Add string params
			for (ConcurrentHashMap.Entry<String, String> entry : urlParams
					.entrySet()) {
				multiPartEntity.addPart(entry.getKey(), entry.getValue());
			}
			// Add file params
			int currentIndex = 0;
			int lastIndex = fileParams.entrySet().size() - 1;
			for (ConcurrentHashMap.Entry<String, FileWrapper> entry : fileParams
					.entrySet()) {
				FileWrapper file = entry.getValue();
				if (file.instream != null) {
					boolean isLast = currentIndex == lastIndex;
					if (file.contentType != null) {
						multiPartEntity.addPart(entry.getKey(),
								file.getFileName(), file.instream,
								file.contentType, isLast);
					} else {
						multiPartEntity.addPart(entry.getKey(),
								file.getFileName(), file.instream, isLast);
					}
				}
				currentIndex++;
			}

			entity = multiPartEntity;
		} else {
			try {
				entity = new UrlEncodedFormEntity(getParamsList(), ENCODING);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return entity;
	}

	private void init() {
		urlParams = new ConcurrentHashMap<String, String>();
		fileParams = new ConcurrentHashMap<String, FileWrapper>();
	}

	protected List<BasicNameValuePair> getParamsList() {
		List<BasicNameValuePair> lparams = new LinkedList<BasicNameValuePair>();

		for (ConcurrentHashMap.Entry<String, String> entry : urlParams
				.entrySet()) {
			lparams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}

		return lparams;
	}

	public String getParamString() {
		return URLEncodedUtils.format(getParamsList(), ENCODING);
	}

	private static class FileWrapper {
		public InputStream instream;
		public String fileName;
		public String contentType;

		public FileWrapper(InputStream instream, String filename,
				String contentType) {
			this.instream = instream;
			this.fileName = filename;
			this.contentType = contentType;
		}

		public String getFileName() {
			if (fileName != null) {
				return fileName;
			} else {
				return "nofilename";
			}
		}
	}
}
