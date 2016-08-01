package com.dh.perfectoffer.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.WindowManager;

import com.dh.perfectoffer.R;
import com.dh.perfectoffer.VehicleApp;
import com.dh.perfectoffer.constant.Constant;
import com.dh.perfectoffer.demo.FragmentTabActivity2;
import com.dh.perfectoffer.entity.UserBean;
import com.dh.perfectoffer.entity.UserListBean;
import com.dh.perfectoffer.event.framework.alibaba.fastjson.JSON;
import com.dh.perfectoffer.event.framework.db.AfeiDb;
import com.dh.perfectoffer.event.framework.net.exception.DataException;
import com.dh.perfectoffer.event.framework.net.fgview.Action;
import com.dh.perfectoffer.event.framework.net.fgview.BaseParser;
import com.dh.perfectoffer.event.framework.net.fgview.OnResponseListener;
import com.dh.perfectoffer.event.framework.net.fgview.Request;
import com.dh.perfectoffer.event.framework.net.fgview.Response;
import com.dh.perfectoffer.event.framework.net.fgview.Response.ErrorMsg;
import com.dh.perfectoffer.xutils.sample.utils.Preference;

import java.util.HashMap;


public class SplashActivity extends Activity {
	private static final String TAG = SplashActivity.class.getSimpleName();
	private String username = "";
	private String password = "";
	public AfeiDb afeiDb;
	private Context mAppContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_splash);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);// 全屏
		afeiDb = VehicleApp.getInstance().getAfeiDb();
		if (null == afeiDb) {
			afeiDb = AfeiDb.create(this, Constant.DB_NAME, true);
		}
		initLogin();
		new Thread() {
			public void run() {
				try {
					// 欢迎页面停留时间，也可以用handler.sendMessageDelayed来写
					sleep(2500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				jump();
			}
		}.start();

	}

	private void initLogin() {
		username = Preference.getString(Constant.PRE_USER_NAME);
		password = Preference.getString(Constant.PRE_USER_PASSWORD);
		if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
			querylogin(username, password);
		}
	}

	private void querylogin(final String userName, final String pwd) {
		HashMap<String, String> pram = new HashMap<String, String>();
		pram.put("username", userName);
		pram.put("userpass", pwd);

		// 请求parser
		Request<UserListBean> req = new Request<UserListBean>();
		req.setRequestMethod(Request.M_POST);
		req.setBaseParser(new BaseParser<UserListBean>() {
			@Override
			public UserListBean parseResDate(String resBody)
					throws DataException {
				if (resBody != null && !resBody.equals("")) {
					Preference.putString(Constant.PRE_USER_NAME, resBody);
					return JSON.parseObject(resBody, UserListBean.class);
				}
				return null;
			}
		});
		// req.setBodyRequestParam(bodyRequestParam);
		req.setRequestParams(pram);
		req.setUrl(Constant.URLIP + "hr/LoginServlet");

		Action action = new Action(SplashActivity.this);
		action.setDefaultLoadingTipOpen(false);
		action.setShowErrorDialog(false);
		action.execute(req, new OnResponseListener<UserListBean>() {
			@Override
			public void onResponseFinished(Request<UserListBean> request,
					Response<UserListBean> response) {
				UserListBean bean = new UserListBean();
				bean = response.getT();
				if (null != bean) {
					if (bean.getSuccess().equals("1")) {
						Preference.putString(Constant.PRE_USER_NAME, userName);
						Preference.putString(Constant.PRE_USER_PASSWORD, pwd);
						afeiDb.dropTableIfTableExist(UserBean.class);
						afeiDb.save(bean.getUserList());
						VehicleApp.getInstance().updateUserBean();
						// new Thread(new Runnable() {
						//
						// @Override
						// public void run() {
						// // 推送一个事件
						// ComEvent appevent = new ComEvent(
						// ComEvent.HISTROY);
						// L.e("yinzl", "通知刷新历史推荐");
						// EventBus.getDefault().post(appevent);
						// ComEvent appeventRes = new ComEvent(
						// ComEvent.RESUME);
						// L.e("yinzl", "通知刷新简历列表");
						// EventBus.getDefault().post(appeventRes);
						//
						// ComEvent applogin = new ComEvent(
						// ComEvent.USERLOGIN);
						// L.e("yinzl", "通知刷新用户信息");
						// EventBus.getDefault().post(applogin);
						//
						// }
						// }).start();
						// List<UserBean> useList = new ArrayList<UserBean>();
						// useList = afeiDb.findAll(UserBean.class);

					} else {
						unLogin();
					}
				} else
					unLogin();
				// initDate(bean);
			}

			@Override
			public void onResponseDataError(Request<UserListBean> equest) {
				unLogin();
			}

			@Override
			public void onResponseConnectionError(
					Request<UserListBean> request, int statusCode) {
				unLogin();
			}

			@Override
			public void onResponseFzzError(Request<UserListBean> request,
					ErrorMsg errorMsg) {
				unLogin();
			}
		});

	}

	private void unLogin() {

		afeiDb.dropTableIfTableExist(UserBean.class);
		VehicleApp.getInstance().setUserBean(null);
		Preference.putString(Constant.PRE_USER_PASSWORD, "");
		// Preference.putString(Constant.PRE_HISTROY_NAME, "");
		// Preference.putString(Constant.PRE_RESUME_NAME, "");
		mAppContext = null;
	}

	private void jump() {
		Intent intent = new Intent();
		intent.setClass(SplashActivity.this, FragmentTabActivity2.class);
		startActivity(intent);
		finish();
	}

	@Override
	protected void onResume() {

		super.onResume();

	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			// do something...
			return true;

		}
		return super.onKeyDown(keyCode, event);
	}

}
