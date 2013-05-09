package com.lynx.lib.dataservice;

import java.io.File;

import org.apache.http.Header;
import org.apache.http.HttpEntity;

import com.lynx.lib.dataservice.core.HttpParam;
import com.lynx.lib.dataservice.handler.HttpCallback;
import com.lynx.lib.dataservice.handler.HttpHandler;

/**
 * 
 * @author chris.liu
 * @name HttpService.java
 * @update 2013-4-17 下午11:03:01
 * 
 */
public interface HttpService {
	/********************************************************************/
	/*
	 * 添加GET异步请求
	 */
	/*******************************************************************/
	void get(String url, HttpCallback<? extends Object> callBack);

	void get(String url, HttpParam params,
			HttpCallback<? extends Object> callBack);

	void get(String url, Header[] headers, HttpParam params,
			HttpCallback<? extends Object> callBack);

	/********************************************************************/
	/*
	 * 添加GET同步请求
	 */
	/*******************************************************************/
	Object getSync(String url);

	Object getSync(String url, HttpParam params);

	Object getSync(String url, Header[] headers, HttpParam params);

	/********************************************************************/
	/*
	 * 添加POST异步请求
	 */
	/*******************************************************************/
	void post(String url, HttpCallback<? extends Object> callBack);

	void post(String url, HttpParam params,
			HttpCallback<? extends Object> callBack);

	void post(String url, HttpEntity entity, String contentType,
			HttpCallback<? extends Object> callBack);

	<T> void post(String url, Header[] headers, HttpParam params,
			String contentType, HttpCallback<T> callBack);

	void post(String url, Header[] headers, HttpEntity entity,
			String contentType, HttpCallback<? extends Object> callBack);

	/********************************************************************/
	/*
	 * 添加POST同步请求
	 */
	/*******************************************************************/
	Object postSync(String url);

	Object postSync(String url, HttpParam params);

	Object postSync(String url, HttpEntity entity, String contentType);

	Object postSync(String url, Header[] headers, HttpParam params,
			String contentType);

	Object postSync(String url, Header[] headers, HttpEntity entity,
			String contentType);

	/********************************************************************/
	/*
	 * 添加PUT异步请求
	 */
	/*******************************************************************/
	void put(String url, HttpCallback<? extends Object> callBack);

	void put(String url, HttpParam params,
			HttpCallback<? extends Object> callBack);

	void put(String url, HttpEntity entity, String contentType,
			HttpCallback<? extends Object> callBack);

	void put(String url, Header[] headers, HttpEntity entity,
			String contentType, HttpCallback<? extends Object> callBack);

	/********************************************************************/
	/*
	 * 添加put同步请求
	 */
	/*******************************************************************/
	Object putSync(String url);

	Object putSync(String url, HttpParam params);

	Object putSync(String url, HttpEntity entity, String contentType);

	Object putSync(String url, Header[] headers, HttpEntity entity,
			String contentType);

	/********************************************************************/
	/*
	 * 删除异步请求
	 */
	/*******************************************************************/
	void delete(String url, HttpCallback<? extends Object> callBack);

	void delete(String url, Header[] headers,
			HttpCallback<? extends Object> callBack);

	/********************************************************************/
	/*
	 * 删除同步请求
	 */
	/*******************************************************************/
	Object deleteSync(String url);

	Object deleteSync(String url, Header[] headers);

	/********************************************************************/
	/*
	 * 下載
	 */
	/*******************************************************************/
	HttpHandler<File> download(String url, String target,
			HttpCallback<File> callback);

	HttpHandler<File> download(String url, String target, boolean isResume,
			HttpCallback<File> callback);

	HttpHandler<File> download(String url, HttpParam params, String target,
			HttpCallback<File> callback);

	HttpHandler<File> download(String url, HttpParam params, String target,
			boolean isResume, HttpCallback<File> callback);
}
