package com.dh.perfectoffer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dh.perfectoffer.R;
import com.dh.perfectoffer.constant.Constant;
import com.dh.perfectoffer.demo.VehicleActivity;
import com.dh.perfectoffer.dhutils.CommUtil;
import com.dh.perfectoffer.entity.BaseBean;
import com.dh.perfectoffer.entity.CompanyListBean;
import com.dh.perfectoffer.event.EventBus;
import com.dh.perfectoffer.event.OwnedCompanyEvent;
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


public class RegisterActivity extends VehicleActivity {

	@ViewInject(R.id.radioGroup)
	private RadioGroup radiogroup;
	@ViewInject(R.id.register_bt)
	private Button re_bt;
	@ViewInject(R.id.register_age)
	private EditText re_age;
	@ViewInject(R.id.register_name)
	private EditText re_name;
	@ViewInject(R.id.register_phone)
	private EditText re_phone;
	@ViewInject(R.id.register_input)
	private EditText re_input;
	@ViewInject(R.id.register_password)
	private EditText re_password;
	@ViewInject(R.id.register_confirm)
	private EditText re_confirm;
	private String sex = "1";

	// 所属公司
	@ViewInject(R.id.re_owned_company_rel)
	private RelativeLayout re_owned_company_rel;
	@ViewInject(R.id.owned_company_tv)
	private TextView owned_company_tv;

	private CompanyListBean companyListBean;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		ViewUtils.inject(this);
		// 注册订阅事件
		if (null != EventBus.getDefault()) {
			EventBus.getDefault().unregister(this);
		}
		EventBus.getDefault().register(this);
		super.initTop();
		setTitle(getResources().getString(R.string.login_register));
		RadioGroup group = (RadioGroup) this.findViewById(R.id.radioGroup);

		radiogroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				int radioButtonId = group.getCheckedRadioButtonId();
				// 根据ID获取RadioButton的实例
				RadioButton rb = (RadioButton) RegisterActivity.this
						.findViewById(radioButtonId);
				sex = rb.getText().toString();
				if (getResources().getString(R.string.re_woman).equals(
						rb.getText().toString())) {
					sex = "0";
				} else {
					sex = "1";
				}
			}

		});

	}

	@OnClick(R.id.register_bt)
	public void onclickloginbt(View view) {

		// String value = re_phone.getText().toString();
		//
		// String regExp =
		// "^(0|86|17951)?(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$";
		//
		// Pattern p = Pattern.compile(regExp);
		//
		// Matcher m = p.matcher(value);

		if (null == re_input.getText().toString().trim()
				|| "".equals(re_input.getText().toString().trim())) {
			ToastUtil.showShort(RegisterActivity.this, getResources()
					.getString(R.string.re_user));
		} else if (null == re_password.getText().toString()
				|| "".equals(re_password.getText().toString())) {
			ToastUtil.showShort(RegisterActivity.this, getResources()
					.getString(R.string.re_pass2));
		} else if (!re_confirm.getText().toString()
				.equals(re_password.getText().toString())) {
			ToastUtil.showShort(RegisterActivity.this, getResources()
					.getString(R.string.re_confrim2));
		} else if (null == re_name.getText().toString().trim()
				|| "".equals(re_name.getText().toString().trim())) {
			ToastUtil.showShort(RegisterActivity.this, getResources()
					.getString(R.string.re_name2));
		} else if (re_phone.getText().toString().isEmpty()) {
			ToastUtil.showShort(RegisterActivity.this, getResources()
					.getString(R.string.re_phone2));
		}

		else if (null == re_age.getText().toString().trim()
				|| "".equals(re_age.getText().toString().trim())) {
			ToastUtil.showShort(RegisterActivity.this, getResources()
					.getString(R.string.re_age2));
		} else if (re_password.getText().toString().length() < 6) {
			ToastUtil.showLong(getApplicationContext(), getResources()
					.getString(R.string.re_pass3));
		} else if (CommUtil.getInstance().isPhoneNumber(
				re_phone.getText().toString()) == false) {
			ToastUtil.showShort(RegisterActivity.this, getResources()
					.getString(R.string.re_true));
		}

		else if (Integer.valueOf(re_age.getText().toString().trim()).intValue() <= 9
				|| Integer.valueOf(re_age.getText().toString().trim())
						.intValue() > 99) {
			ToastUtil.showShort(RegisterActivity.this, getResources()
					.getString(R.string.re_age3));
		}

		else if (CommUtil.getInstance().containsEmoji(
				re_name.getText().toString()) == true) {
			ToastUtil.showLong(getApplicationContext(), getResources()
					.getString(R.string.re_name3));
		}
		else if(null == companyListBean){
			ToastUtil.showLong(getApplicationContext(), getResources()
					.getString(R.string.owned_company_please));
		}

		// else if
		// (CommUtil.getInstance().contains(re_name.getText().toString()).equals("0"))
		// {
		// ToastUtil.showLong(getApplicationContext(), "输入的真实姓名含有特殊字符");
		// Log.e("xipp", CommUtil.getInstance()
		// .contains(re_name.getText().toString())+"初體驗故意");
		// }
		else {
			queryregisterlist();
		}
	}

	private void queryregisterlist() {
		//
		// RegisterBean bean = new RegisterBean();
		// bean.setUsername(re_input.getText().toString().trim());
		// bean.setUser_age(re_age.getText().toString().trim());
		// bean.setUserpass(re_password.getText().toString());
		// bean.setRname(re_name.getText().toString().trim());
		// bean.setUser_phone(re_phone.getText().toString().trim());
		// bean.setUser_sex(sex.trim());

		// try {
		// // sex = new String("男".getBytes("utf-8"), "utf-8");
		// } catch (UnsupportedEncodingException e1) {
		// // TODO Auto-generated catch block
		// e1.printStackTrace();
		// }
		HashMap<String, String> pram = new HashMap<String, String>();

		pram.put("username", re_input.getText().toString().trim());

		pram.put("user_age", re_age.getText().toString());
		pram.put("userpass", re_password.getText().toString());

		pram.put("rname", re_name.getText().toString().trim());
		pram.put("user_phone", re_phone.getText().toString());
		// Log.e("xipp:", sex);
		// String bodyRequestParam = JSON.toJSONString(bean);

		pram.put("user_sex", sex);
		pram.put("company", companyListBean.getId());
		// 请求parser
		Request<BaseBean> req = new Request<BaseBean>();
		req.setRequestMethod(Request.M_POST);
		req.setBaseParser(new BaseParser<BaseBean>() {

			@Override
			public BaseBean parseResDate(String resBody) throws DataException {
				if (resBody != null && !resBody.equals("")) {
					return JSON.parseObject(resBody, BaseBean.class);
				}
				return null;
			}
		});
		// req.setBodyRequestParam(bodyRequestParam);
		req.setRequestParams(pram);
		req.setUrl(Constant.URLIP + "hr/RegisterServlet");

		Action action = new Action(RegisterActivity.this);
		action.setDefaultLoadingTipOpen(true);
		action.setShowErrorDialog(true);
		action.execute(req, new OnResponseListener<BaseBean>() {
			@Override
			public void onResponseFinished(Request<BaseBean> request,
					Response<BaseBean> response) {
				BaseBean bean = new BaseBean();
				bean = response.getT();
				if (null != bean) {
					if (bean.getSuccess().equals("1")) {
						ToastUtil.showLong(RegisterActivity.this,
								getResources().getString(R.string.re_su));
						finish();
					} else
						ToastUtil.showLong(RegisterActivity.this,
								bean.getErrorMsg());
				}
				// initDate(bean);
			}

			@Override
			public void onResponseDataError(Request<BaseBean> equest) {
				// ToastUtil.showLong(RegisterActivity.this, "请求失败");
			}

			@Override
			public void onResponseConnectionError(Request<BaseBean> request,
					int statusCode) {
				// ToastUtil.showLong(RegisterActivity.this, "请求失败");
			}

			@Override
			public void onResponseFzzError(Request<BaseBean> request,
					ErrorMsg errorMsg) {
				// ToastUtil.showLong(RegisterActivity.this, "请求失败");
			}

		});
	}
	
	@OnClick(R.id.re_owned_company_rel)
	public void onclickintentOwnedCompanyActivity(View view){
		Intent intent = new Intent(RegisterActivity.this, OwnedCompanyActivity.class);
		launch(intent);
	}

	public void onEventMainThread(OwnedCompanyEvent event) {
		if (null != event.getBean()) {
			companyListBean = event.getBean();
			owned_company_tv.setText(companyListBean.getName());
		}

	}

	@Override
	protected void onDestroy() {
		// 注销订阅事件
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}
}
