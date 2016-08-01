package com.dh.perfectoffer.activity;


import android.os.Bundle;
import android.widget.TextView;

import com.dh.perfectoffer.R;
import com.dh.perfectoffer.demo.VehicleActivity;
import com.dh.perfectoffer.dhutils.CommUtil;
import com.dh.perfectoffer.xutils.view.ViewUtils;
import com.dh.perfectoffer.xutils.view.annotation.ViewInject;

public class MoreRelateActivity extends VehicleActivity {

	@ViewInject(R.id.more_about)
	private TextView  aboutcoude;
	private String versionName;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.more_about);
		getAppInfo();
		ViewUtils.inject(this);
		aboutcoude.setText("version   "+versionName);
		super.initTop();
		setTitle(CommUtil.getInstance().getStringFromXml(
				MoreRelateActivity.this, R.string.more_relate));
	}
	private String getAppInfo() {
 		try {
 			String pkName = this.getPackageName();
 			 versionName = this.getPackageManager().getPackageInfo(
 					pkName, 0).versionName;
 			int versionCode = this.getPackageManager()
 					.getPackageInfo(pkName, 0).versionCode;
 			return pkName + "   " + versionName + "  " + versionCode;
 		} catch (Exception e) {
 		}
 		return null;
 	}


}
