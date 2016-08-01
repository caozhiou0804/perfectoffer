package com.dh.perfectoffer.demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.dh.perfectoffer.R;
import com.dh.perfectoffer.VehicleApp;
import com.dh.perfectoffer.constant.Constant;
import com.dh.perfectoffer.entity.HrVOBean;
import com.dh.perfectoffer.event.framework.alibaba.fastjson.JSON;
import com.dh.perfectoffer.event.framework.db.AfeiDb;
import com.dh.perfectoffer.event.framework.log.L;
import com.dh.perfectoffer.event.framework.net.exception.DataException;
import com.dh.perfectoffer.event.framework.net.fgview.Action;
import com.dh.perfectoffer.event.framework.net.fgview.BaseParser;
import com.dh.perfectoffer.event.framework.net.fgview.OnResponseListener;
import com.dh.perfectoffer.event.framework.net.fgview.Request;
import com.dh.perfectoffer.event.framework.net.fgview.Response;
import com.dh.perfectoffer.xutils.sample.utils.ToastUtil;
import com.dh.perfectoffer.event.framework.net.fgview.Response.ErrorMsg;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends Activity {
	private TextView tv;
	private AfeiDb afeiDb;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		tv = (TextView) findViewById(R.id.tv_test);
		queryCouponListByOrderId(true);
		// 存入用户名及密码
		afeiDb = VehicleApp.getInstance().getAfeiDb();
		if (null == afeiDb) {
			afeiDb = AfeiDb.create(MainActivity.this, Constant.DB_NAME, true);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void queryCouponListByOrderId(boolean dialog) {

		// 结果解析parser
		// GetQueryListParser parser = new GetQueryListParser();
		// 请求parser
		Request<List<HrVOBean>> req = new Request<List<HrVOBean>>();
		req.setRequestMethod(Request.M_GET);
		req.setBaseParser(new BaseParser<List<HrVOBean>>() {

			@Override
			public List<HrVOBean> parseResDate(String resBody)
					throws DataException {
				if (resBody != null && !resBody.equals("")) {
					return JSON.parseArray(resBody, HrVOBean.class);
				}
				return null;
			}
		});
		HashMap<String, String> params = new HashMap<String, String>();

		// params.put("page", page);
		// params.put("size", size);
		req.setUrl("http://10.202.7.8:8080/hr/ListServlet");
		req.setRequestParams(params);

		Action action = new Action(this);
		if (!dialog) {
			action.setDefaultLoadingTipOpen(true);
		}
		action.setShowErrorDialog(true);
		action.execute(req, new OnResponseListener<List<HrVOBean>>() {
			@Override
			public void onResponseFinished(Request<List<HrVOBean>> request,
					Response<List<HrVOBean>> response) {
				List<HrVOBean> list = new ArrayList<HrVOBean>();
				list = response.getT();
				ToastUtil.showLong(MainActivity.this, "list size is :"
						+ list.get(0).toString());
				L.e("yinzl", "获取到的第一个数据为：" + list.get(0).toString());
				afeiDb.dropTableIfTableExist(HrVOBean.class);
				// afeiDb.save(list.get(0));
				for (int i = 0; i < list.size(); i++) {
					afeiDb.save(list.get(i));
				}
				List<HrVOBean> listtemp = new ArrayList<HrVOBean>();
				listtemp = afeiDb.findAll(HrVOBean.class);
				String temp = "";
				if (listtemp.size() > 0) {
					for (int i = 0; i < listtemp.size(); i++) {
						temp += listtemp.get(i).toString() + "\n\n\n";
					}
				}
				listtemp = afeiDb.findAllByWhereStr(HrVOBean.class,
						" sub_id = '2'");
				temp += "查询sub id =2的数据" + listtemp.get(0).toString();
				tv.setText(temp);

			}

			@Override
			public void onResponseDataError(Request<List<HrVOBean>> equest) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onResponseConnectionError(
					Request<List<HrVOBean>> request, int statusCode) {
			}

			@Override
			public void onResponseFzzError(Request<List<HrVOBean>> request,
					ErrorMsg errorMsg) {
				// btnController(true);
				// if (page.equals(page0)) {
				// lv_voucher.setVisibility(View.GONE);
				// ll_no_data.setVisibility(View.VISIBLE);
				// }
			}

		});
	}
}
