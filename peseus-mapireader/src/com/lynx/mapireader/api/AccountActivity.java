package com.lynx.mapireader.api;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cop.mapireader.R;
import com.lynx.lib.dataservice.HttpService;
import com.lynx.lib.dataservice.NetworkUtil;
import com.lynx.lib.dataservice.NetworkUtil.NetworkState;
import com.lynx.lib.dataservice.core.HttpParam;
import com.lynx.lib.dataservice.handler.HttpCallback;
import com.lynx.mapireader.LCApplication;

/**
 * 
 * @author chris.liu
 * 
 */
public class AccountActivity extends Activity {

	private static final int PIC_PRE_WIDTH = 90;
	private static final int PIC_PRE_HEIGHT = 90;

	private Button btnBack;
	private ImageView ivUploadPreView;
	private EditText etUploadFileName;
	private Button btnLoginViaName, btnLoginViaEmail, btnRegister,
			btnUploadProfile, btnChooseFile;

	private static Bitmap bmpProfile;

	private static final String LOGIN_URL = "http://58.210.101.202:59102/test/account/login";
	// private static final String UPLOAD_PROFILE_URL =
	// "http://58.210.101.202:59102/test/account/uploadprofile";
	private static final String UPLOAD_PROFILE_URL = "http://localhost:8080/test/account/uploadprofile";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account);

		ivUploadPreView = (ImageView) findViewById(R.id.iv_account_uploadprofile_preview);
		etUploadFileName = (EditText) findViewById(R.id.et_account_uploadprofile_filename);

		btnBack = (Button) findViewById(R.id.btn_account_back);
		btnBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AccountActivity.this.onBackPressed();
			}
		});

		btnLoginViaName = (Button) findViewById(R.id.btn_account_name_login);
		btnLoginViaName.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				NetworkState networkState = NetworkUtil
						.state(AccountActivity.this);
				if (networkState == NetworkState.NETWORK_NONE) {
					Toast.makeText(AccountActivity.this, "未连接网络",
							Toast.LENGTH_SHORT).show();
				} else {
					login("name", "chris", "111");
				}
			}
		});

		btnRegister = (Button) findViewById(R.id.btn_account_register);
		btnRegister.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				NetworkState networkState = NetworkUtil
						.state(AccountActivity.this);
				if (networkState == NetworkState.NETWORK_NONE) {
					Toast.makeText(AccountActivity.this, "未连接网络",
							Toast.LENGTH_SHORT).show();
				} else {
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("name", "cop_mobi");
					params.put("email", "cop_mobi@gmail.com");
					params.put("pwd", "111");
					params.put("obd", "2454-1234-5234-8445");
					params.put("buy_date", 1341246365624l);
					params.put("sex", 0);
					params.put("price", 254300.0);
					register(params);
				}
			}
		});

		btnUploadProfile = (Button) findViewById(R.id.btn_account_uploadprofile);
		btnUploadProfile.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				uploadProfile(1, bmpProfile);
			}
		});

		btnChooseFile = (Button) findViewById(R.id.btn_account_choose_file);
		btnChooseFile.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				chooseFile();
			}
		});

		btnChooseFile.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					v.setBackgroundResource(R.drawable.btn_more_sel);
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					v.setBackgroundResource(R.drawable.btn_more);
				}
				return false;
			}
		});
	}

	private void login(String key, String val, String pwd) {
		LCApplication applocation = (LCApplication) getApplication();
		HttpService httpService = applocation.httpservice();

		HttpParam params = new HttpParam();
		params.put(key, val);
		params.put("pwd", pwd);
		httpService.post(LOGIN_URL, params, new HttpCallback<String>() {
			@Override
			public void onLoading(long count, long current) {
				// 每1秒钟自动被回调一次
				// tvConsole.setText(current + "/" + count);
			}

			@Override
			public void onSuccess(String t) {
				Toast.makeText(AccountActivity.this, t, Toast.LENGTH_SHORT)
						.show();
			}

			@Override
			public void onStart() {
				// 开始http请求的时候回调
			}

			@Override
			public void onFailure(Throwable t, String strMsg) {
				// 加载失败的时候回调
				Toast.makeText(AccountActivity.this, "网络异常", Toast.LENGTH_SHORT)
						.show();
			}
		});
	}

	private void register(Map<String, Object> params) {

	}

	private void uploadProfile(int uid, Bitmap profile) {
		LCApplication applocation = (LCApplication) getApplication();
		HttpService httpService = applocation.httpservice();

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		profile.compress(Bitmap.CompressFormat.PNG, 100, baos);
		InputStream sbs = new ByteArrayInputStream(baos.toByteArray());

		HttpParam params = new HttpParam();
		params.put("uid", uid + "");
		params.put("profile", sbs);
		httpService.post(UPLOAD_PROFILE_URL, params,
				new HttpCallback<String>() {
					@Override
					public void onLoading(long count, long current) {
						// 每1秒钟自动被回调一次
						// tvConsole.setText(current + "/" + count);
					}

					@Override
					public void onSuccess(String t) {
						Toast.makeText(AccountActivity.this, t,
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onStart() {
						// 开始http请求的时候回调
					}

					@Override
					public void onFailure(Throwable t, String strMsg) {
						// 加载失败的时候回调
					}
				});
	}

	private void chooseFile() {
		Intent intent = new Intent();
		intent.setType("image/*");
		/* 使用Intent.ACTION_GET_CONTENT这个Action */
		intent.setAction(Intent.ACTION_GET_CONTENT);
		/* 取得相片后返回本画面 */
		startActivityForResult(intent, 1);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			Uri uri = data.getData();
			ContentResolver cr = this.getContentResolver();
			try {
				bmpProfile = BitmapFactory
						.decodeStream(cr.openInputStream(uri));
				ivUploadPreView.setImageBitmap(bmpProfile);
				etUploadFileName.setText(uri.getLastPathSegment());
			} catch (FileNotFoundException e) {
				Log.e("Exception", e.getMessage(), e);
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
