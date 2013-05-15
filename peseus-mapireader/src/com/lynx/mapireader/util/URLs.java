package com.lynx.mapireader.util;

/**
 * 
 * @author chris.liu
 * 
 */
public class URLs {

	public static final String BASIC_URL = "http://58.210.101.202:59102";
	public static final String DOMAIN = "test";

	public static final String URL_ACCOUNT_LOGIN = String.format(
			"%s/%s/account/login", BASIC_URL, DOMAIN);
	public static final String URL_ACCOUNT_REGISTER = String.format(
			"%s/%s/account/register", BASIC_URL, DOMAIN);
	public static final String URL_ACCOUNT_UPDATE = String.format(
			"%s/%s/account/update", BASIC_URL, DOMAIN);
	public static String URL_ACCOUNT_UPLOAD = String.format(
			"%s/%s/account/upload", BASIC_URL, DOMAIN);

	public static final String URL_MYCAR_UPLOAD = String.format(
			"%s/%s/mycar/upload", BASIC_URL, DOMAIN);
	public static String URL_MYCAR_GET = String.format("%s/%s/mycar/get",
			BASIC_URL, DOMAIN);
	public static final String URL_MYCAR_DIAGNOSE = String.format(
			"%s/%s/mycar/diagnose", BASIC_URL, DOMAIN);

	public static final String URL_OILBILL_GET = String.format(
			"%s/%s/oilbill/get", BASIC_URL, DOMAIN);
	public static final String URL_OILBILL_ADD = String.format(
			"%s/%s/oilbill/add", BASIC_URL, DOMAIN);
	public static final String URL_OILBILL_UPDATE = String.format(
			"%s/%s/oilbill/update", BASIC_URL, DOMAIN);
	public static final String URL_OILBILL_DELETE = String.format(
			"%s/%s/oilbill/delete", BASIC_URL, DOMAIN);

	public static final String URL_OTHER_CONFIG = String.format(
			"%s/%s/other/config", BASIC_URL, DOMAIN);
	public static final String URL_OTHER_FEEDBACK = String.format(
			"%s/%s/other/feedback", BASIC_URL, DOMAIN);
}
