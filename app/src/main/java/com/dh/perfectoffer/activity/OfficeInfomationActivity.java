package com.dh.perfectoffer.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dh.perfectoffer.R;
import com.dh.perfectoffer.VehicleApp;
import com.dh.perfectoffer.constant.Constant;
import com.dh.perfectoffer.demo.VehicleActivity;
import com.dh.perfectoffer.entity.OfficeInfoBean;
import com.dh.perfectoffer.event.framework.alibaba.fastjson.JSON;
import com.dh.perfectoffer.event.framework.net.exception.DataException;
import com.dh.perfectoffer.event.framework.net.fgview.Action;
import com.dh.perfectoffer.event.framework.net.fgview.BaseParser;
import com.dh.perfectoffer.event.framework.net.fgview.OnResponseListener;
import com.dh.perfectoffer.event.framework.net.fgview.Request;
import com.dh.perfectoffer.event.framework.net.fgview.Response;
import com.dh.perfectoffer.event.framework.net.fgview.Response.ErrorMsg;
import com.dh.perfectoffer.xutils.sample.utils.ToastUtil;
import com.dh.perfectoffer.xutils.view.ViewUtils;
import com.dh.perfectoffer.xutils.view.annotation.ViewInject;
import com.dh.perfectoffer.xutils.view.annotation.event.OnClick;

import java.util.HashMap;


public class OfficeInfomationActivity extends VehicleActivity {
	@ViewInject(R.id.offic_name)
	TextView name;

	@ViewInject(R.id.office_salary)
	TextView salary;
	@ViewInject(R.id.offic_rule)
	TextView rule;
	@ViewInject(R.id.office_claim)
	TextView claim;
	@ViewInject(R.id.office_welfare)
	TextView welfare;
	@ViewInject(R.id.office_title)
	TextView title;
	@ViewInject(R.id.office_address)
	TextView address;
	@ViewInject(R.id.office_bounty2)
	TextView bounty2;
	@ViewInject(R.id.office_bounty)
	TextView bounty;
	@ViewInject(R.id.office_friendname)
	TextView friendname;

	@ViewInject(R.id.office_rulevalue)
	TextView rulevalue;
	@ViewInject(R.id.office_bt)
	Button Recommendation;

	@ViewInject(R.id.btn_top_right)
	private Button btn_top_right;
	private String histroyId = "";
	private String historyBounty = "";
	private String historyBounty2 = "";
	private String historyFriedName = "";
	private String historydate = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_officeinfomation);
		ViewUtils.inject(this);
		super.initTop();
		 setTitle("推荐详情");
//		setTitle(CommUtil.getInstance().getStringFromXml(
//				OfficeInfomationActivity.this, R.string.off_title));
		btn_top_right.setVisibility(View.VISIBLE);
		histroyId = getIntent().getStringExtra("historyId");
		historyBounty = getIntent().getStringExtra("historyBounty");// 总积分
		historyBounty2 = getIntent().getStringExtra("historyBounty2");// 已获积分
		historyFriedName = getIntent().getStringExtra("historyFriedName");// 被推荐人名字
		historydate = getIntent().getStringExtra("historydate");// 被推荐人名字

		if (!TextUtils.isEmpty(histroyId))
			queryinfomation(histroyId);

	}

	@OnClick(R.id.btn_top_right)
	public void onclickrightbt(View view) {

	}

	private void queryinfomation(String postId) {
		if (null == VehicleApp.getInstance().getUserBean()
				|| TextUtils.isEmpty(VehicleApp.getInstance().getUserBean()
						.getUsertosk())) {
			launch(LoginActivity.class);
			return;
		}
		HashMap<String, String> pram = new HashMap<String, String>();
		pram.put("username", VehicleApp.getInstance().getUserBean()
				.getUsertosk());

		pram.put("rew_id", postId);

		// String bodyRequestParam = JSON.toJSONString(bean);

		// 请求parser
		Request<OfficeInfoBean> req = new Request<OfficeInfoBean>();
		req.setRequestMethod(Request.M_POST);

		req.setBaseParser(new BaseParser<OfficeInfoBean>() {
			@Override
			public OfficeInfoBean parseResDate(String resBody)
					throws DataException {
				if (resBody != null && !resBody.equals("")) {
					// Preference.putString(Constant.PRE_USER_NAME, resBody);
					return JSON.parseObject(resBody, OfficeInfoBean.class);
				}
				return null;
			}
		});

		// req.setBodyRequestParam(bodyRequestParam);
		req.setRequestParams(pram);
		req.setUrl(Constant.URLIP + "hr/ListHistoryDetailsServlet");

		Action action = new Action(OfficeInfomationActivity.this);
		action.setDefaultLoadingTipOpen(true);
		action.setShowErrorDialog(true);
		action.execute(req, new OnResponseListener<OfficeInfoBean>() {
			@Override
			public void onResponseFinished(Request<OfficeInfoBean> request,
					Response<OfficeInfoBean> response) {
				OfficeInfoBean bean = new OfficeInfoBean();
				bean = response.getT();
				if (null != bean) {

					// rule claim welfare title integration time Recommendation
					// name.setText(bean.getPost_name());
					// claim.setText(bean.getReward_claim_details());

					String str_claim = bean.getReward_claim_details()
							.toString().trim();
					str_claim = str_claim.replace("\\n", "\n");
					claim.setText(str_claim + "");

					String str_welfare = bean.getReward_welfare().toString()
							.trim();
					str_welfare = str_welfare.replace("\\n", "\n");
					welfare.setText(str_welfare + "");

					// welfare.setText(bean.getReward_welfare());
					// title.setText(bean.getReward_title());
					// title.setText(bean.)
					title.setText(bean.getPost_name());
					// claim.setText(bean.getReward_claim_details());

					salary.setText(bean.getReward_salary());

					int bounty_count = 0;// 全部积分
					int bounty2_count = 0;// 已获积分

					if (!TextUtils.isEmpty(historyBounty)) {
						bounty_count = Integer.valueOf(historyBounty)
								.intValue();
					}
					if (!TextUtils.isEmpty(historyBounty2)) {
						bounty2_count = Integer.valueOf(historyBounty2)
								.intValue();
					}
					bounty.setText(bounty2_count + "");
					// bounty2.setText(bounty_count - bounty2_count);
					bounty2.setText(((bounty_count > bounty2_count) ? (bounty_count - bounty2_count)
							: 0)
							+ "");

					// String s = String.valueOf(bounty3);
					address.setText(bean.getSub_address());
					friendname.setText(historyFriedName);
					rulevalue.setText(bean.getRules_value());
				} else
					ToastUtil.showLong(OfficeInfomationActivity.this,
							bean.getErrorMsg());
			}

			// initDate(bean);
			// }

			@Override
			public void onResponseDataError(Request<OfficeInfoBean> equest) {
				
				// ToastUtil.showLong(OfficeInfomationActivity.this, "请求失败");
				
			}
			
			@Override
			public void onResponseConnectionError(
					Request<OfficeInfoBean> request, int statusCode) {
				// ToastUtil.showLong(OfficeInfomationActivity.this, "请求失败");
			}

			@Override
			public void onResponseFzzError(Request<OfficeInfoBean> request,
					ErrorMsg errorMsg) {
				// ToastUtil.showLong(OfficeInfomationActivity.this, "请求失败");
			}

		});

	}
}
