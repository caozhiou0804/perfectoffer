package com.dh.perfectoffer.demo;

/*
 * Copyright (C), 2014-2014, 联创车盟汽车服务有限公司
 * FileName: VehicleActivity
 * Author:   jun.w
 * Date:     2014/11/3 16:10
 * Description: Activity基类
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dh.perfectoffer.event.framework.net.fgview.Response.ErrorMsg;
import com.dh.perfectoffer.R;
import com.dh.perfectoffer.VehicleApp;
import com.dh.perfectoffer.constant.Constant;
import com.dh.perfectoffer.event.framework.db.AfeiDb;

public class VehicleActivity extends FragmentActivity implements
		View.OnClickListener {

	protected Button btn_top_right;

	private WindowManager windowManager;
	private WindowManager.LayoutParams fluctuateParam;
	private boolean isShowFluctuate;
	public AfeiDb afeiDb;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		VehicleApp.getInstance().addActivity(this);
		afeiDb = VehicleApp.getInstance().getAfeiDb();
		if (null == afeiDb) {
			afeiDb = AfeiDb.create(this, Constant.DB_NAME, true);
		}
	}

	protected void initTop() {
		RelativeLayout btn_back = (RelativeLayout) this
				.findViewById(R.id.btn_title_btn_back_layout);
		if (null != btn_back) {
			btn_back.setOnClickListener(this);
		}
		btn_top_right = (Button) findViewById(R.id.btn_top_right);
		if (null != btn_top_right) {
			btn_top_right.setOnClickListener(this);
		}
	}

	public void launch(Class<? extends Activity> clazz) {
		launch(new Intent(this, clazz));
	}

	public void launch(Intent intent) {
		startActivity(intent);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		VehicleApp.getInstance().getActivitys().remove(this); // 退出时从集合中拿出
	}

	public void setTitle(String title) {
		super.setTitle(title);
		((TextView) this.findViewById(R.id.tv_title_name)).setText(title);
	}

	public void setTitle(int titleId) {
		super.setTitle(titleId);
		((TextView) this.findViewById(R.id.tv_title_name)).setText(titleId);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btn_title_btn_back_layout) {
			onBackPressed();
			return;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	public interface OnLoginFinishLintener {
		public void onSuccess();

		public void onFailure(ErrorMsg errorMsg);
	}

}
