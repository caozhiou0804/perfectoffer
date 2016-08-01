package com.dh.perfectoffer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.dh.perfectoffer.R;
import com.dh.perfectoffer.VehicleApp;
import com.dh.perfectoffer.constant.Constant;
import com.dh.perfectoffer.demo.VehicleActivity;
import com.dh.perfectoffer.entity.HistoryInfoBean;
import com.dh.perfectoffer.entity.HistroyTempBean;
import com.dh.perfectoffer.entity.ResumeTempBean;
import com.dh.perfectoffer.entity.RusumeListBean;
import com.dh.perfectoffer.event.framework.alibaba.fastjson.JSON;
import com.dh.perfectoffer.event.framework.net.exception.DataException;
import com.dh.perfectoffer.event.framework.net.fgview.Action;
import com.dh.perfectoffer.event.framework.net.fgview.BaseParser;
import com.dh.perfectoffer.event.framework.net.fgview.OnResponseListener;
import com.dh.perfectoffer.event.framework.net.fgview.Request;
import com.dh.perfectoffer.event.framework.net.fgview.Response;
import com.dh.perfectoffer.xutils.sample.utils.ToastUtil;
import com.dh.perfectoffer.xutils.view.ViewUtils;
import com.dh.perfectoffer.xutils.view.annotation.ViewInject;
import com.dh.perfectoffer.xutils.view.annotation.event.OnClick;
import com.dh.perfectoffer.event.framework.net.fgview.Response.ErrorMsg;
import java.util.ArrayList;
import java.util.HashMap;


public class HistoryResumeActivity extends VehicleActivity {
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
    // @ViewInject(R.id.office_title)
    // TextView office_title;
    @ViewInject(R.id.office_address)
    TextView address;
    @ViewInject(R.id.office_integration)
    TextView bounty;
    @ViewInject(R.id.office_time)
    TextView time;
    private String postId = "";

    @ViewInject(R.id.office_bt)
    TextView bt;
    private ArrayList<HistroyTempBean> hisArrayList;
    // private ResumeAdapter resumeAdapter;
    private ArrayList<ResumeTempBean> resumeArrayList;

    // private ArrayList<HistroyTempBean> hisArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_historyinfo);
	ViewUtils.inject(this);
	super.initTop();
	// setTitle(CommUtil.getInstance().getStringFromXml(HistoryResumeActivity.this,
	// R.string.info));
	setTitle("职位详情");
	postId = getIntent().getStringExtra("postId");
	if (!TextUtils.isEmpty(postId))
	    queryInfo(postId);
	// resumeArrayList = new ArrayList<ResumeTempBean>();
	// resumeAdapter = new ResumeAdapter(HistoryResumeActivity.this);
	// resumeAdapter.setList(resumeArrayList);
	// lv_resume.setAdapter(resumeAdapter);
    }

    @OnClick(R.id.office_bt)
    public void onclickrecombt(View view) {

	if (null == VehicleApp.getInstance().getUserBean()
		|| TextUtils.isEmpty(VehicleApp.getInstance().getUserBean()
			.getUsertosk())) {
	    launch(LoginActivity.class);
	    return;
	} else {
	    queryResumeList();
	}

    }

    private void queryResumeList() {// 获取简历列表
	if (null == VehicleApp.getInstance().getUserBean()
		|| TextUtils.isEmpty(VehicleApp.getInstance().getUserBean()
			.getUsertosk())) {
	    launch(LoginActivity.class);
	    return;
	}
	HashMap<String, String> pram = new HashMap<String, String>();
	pram.put("username", VehicleApp.getInstance().getUserBean()
		.getUsertosk());
	// 请求parser
	Request<RusumeListBean> req = new Request<RusumeListBean>();
	req.setRequestMethod(Request.M_POST);
	req.setBaseParser(new BaseParser<RusumeListBean>() {
	    @Override
	    public RusumeListBean parseResDate(String resBody)
		    throws DataException {
		if (resBody != null && !resBody.equals("")) {
		    return JSON.parseObject(resBody, RusumeListBean.class);
		}
		return null;
	    }
	});
	req.setRequestParams(pram);
	req.setUrl(Constant.URLIP + "hr/FriedResumeListServlet");

	Action action = new Action(HistoryResumeActivity.this);
	action.setDefaultLoadingTipOpen(true);
	action.setShowErrorDialog(true);
	action.execute(req, new OnResponseListener<RusumeListBean>() {
	    @Override
	    public void onResponseFinished(Request<RusumeListBean> request,
		    Response<RusumeListBean> response) {
		RusumeListBean bean = new RusumeListBean();
		bean = response.getT();
		if (null != bean) {
		    if (bean.getSuccess().equals("1")) {
			if (null != bean.getFriedResumeList()
				&& bean.getFriedResumeList().size() > 0) {
			    // initResume(bean.getFriedResumeList());
			    Intent intent = new Intent(
				    HistoryResumeActivity.this,
				    ResumeChooseAcitity.class);

			    intent.putExtra("choose", bean.getFriedResumeList());
			    intent.putExtra("postId", postId);

			    launch(intent);
			} else {
			    // ToastUtil.showShort(HistoryResumeActivity.this,
			    // "暂无简历");
			    Intent intent = new Intent(
				    HistoryResumeActivity.this,
				    RecommendationActivity.class);
			    intent.putExtra("choosename", "select");
			    intent.putExtra("choosepsotid", postId);
			    launch(intent);
			}
		    } else {
			ToastUtil.showShort(HistoryResumeActivity.this,
				bean.getErrorMsg());
		    }
		}
	    }

	    @Override
	    public void onResponseDataError(Request<RusumeListBean> equest) {
		// ToastUtil.showLong(HistoryResumeActivity.this, "请求失败");
	    }

	    @Override
	    public void onResponseConnectionError(
		    Request<RusumeListBean> request, int statusCode) {
		// ToastUtil.showLong(HistoryResumeActivity.this, "请求失败");
	    }

	    @Override
	    public void onResponseFzzError(Request<RusumeListBean> request,
		    ErrorMsg errorMsg) {
		// ToastUtil.showLong(HistoryResumeActivity.this, "请求失败");
	    }

	});
    }

    // private void initResume(ArrayList<ResumeTempBean> list) {
    // resumeArrayList.clear();
    // resumeArrayList.addAll(list);
    // resumeAdapter.notifyDataSetChanged();
    // }

    // class ResumeAdapter extends ArrayListAdapter<ResumeTempBean> {
    //
    // public ResumeAdapter(Activity context) {
    // super(context);
    // }
    //
    // public View getView(final int position, View convertView,
    // ViewGroup parent) {
    // if (convertView == null) {
    // convertView = LayoutInflater.from(mContext).inflate(
    // R.layout.item_resume, null);
    // }
    //
    // TextView tv_resume_name = ViewHolder.get(convertView,
    // R.id.tv_resume_name);
    //
    // final ResumeTempBean bean = mList.get(position);
    // convertView.setOnClickListener(new OnClickListener() {
    //
    // @Override
    // public void onClick(View v) {
    // Intent intent = new Intent();
    // intent.setClass(HistoryResumeActivity.this,
    // RecommendationActivity.class);
    // intent.putExtra("resumeId", bean.getFried_id());
    // intent.putExtra("resumeName", bean.getFriend_name());
    // launch(intent);
    // }
    // });
    // if (bean != null) {
    // tv_resume_name.setText(bean.getFriend_name());
    //
    // }
    // return convertView;
    // }
    //
    // }

    public void queryInfo(String postId) {

	HashMap<String, String> pram = new HashMap<String, String>();
	pram.put("rew_id", postId);
	// pram.put("userpass", password.getText().toString());

	// String bodyRequestParam = JSON.toJSONString(bean);

	// 请求parser
	Request<HistoryInfoBean> req = new Request<HistoryInfoBean>();
	req.setRequestMethod(Request.M_POST);
	req.setBaseParser(new BaseParser<HistoryInfoBean>() {
	    @Override
	    public HistoryInfoBean parseResDate(String resBody)
		    throws DataException {
		if (resBody != null && !resBody.equals("")) {
		    // Preference.putString(Constant.PRE_USER_NAME, resBody);
		    return JSON.parseObject(resBody, HistoryInfoBean.class);

		}
		return null;
	    }
	});
	// req.setBodyRequestParam(bodyRequestParam);
	req.setRequestParams(pram);
	req.setUrl(Constant.URLIP + "hr/RewardDetailsServlet");

	Action action = new Action(HistoryResumeActivity.this);
	action.setDefaultLoadingTipOpen(true);
	action.setShowErrorDialog(true);
	action.execute(req, new OnResponseListener<HistoryInfoBean>() {
	    @Override
	    public void onResponseFinished(Request<HistoryInfoBean> request,
		    Response<HistoryInfoBean> response) {
		HistoryInfoBean bean = new HistoryInfoBean();
		bean = response.getT();
		if (null != bean) {
		    if (bean.getSuccess().equals("1")) {
			// name，salary,
			// rule,claim,welfare,title,address,bounty,time
			name.setText(bean.getReward_title());
			if (!TextUtils.isEmpty(bean.getReward_salary())) {
			    if (!"面议".equals(bean.getReward_salary())) {
				salary.setText(bean.getReward_salary() + "/月");
			    } else {
				salary.setText(bean.getReward_salary());
			    }
			}
			rule.setText(bean.getRules_value());
			String str_claim = bean.getReward_claim_details()
				.toString().trim();
			str_claim = str_claim.replace("\\n", "\n");
			claim.setText(str_claim + "");
			String str_welfare = bean.getReward_welfare()
				.toString().trim();
			str_welfare = str_welfare.replace("\\n", "\n");
			welfare.setText(str_welfare + "");
			// welfare.setText(bean.getReward_welfare());
			// office_title.setText(bean.getPost_name());
			address.setText(bean.getSub_address());
			if (bean.getBounty().isEmpty()) {
			    bounty.setText("0");
			}
			bounty.setText(bean.getBounty());
			time.setText(bean.getCreate_time().split(" +")[0]);
			// List<HistoryInfobean> useList = new
			// ArrayList<HistoryInfobean>();
			// useList = afeiDb.findAll(UserBean.class);
			// if (useList.size() > 0) {
			// for (int i = 0; i < useList.size(); i++) {
			// Log.e("aaaaaaaaaaaaaaa", useList.get(i)
			// .getUser_age().toString());
			// }
			// }
			// finish();
		    } else
			ToastUtil.showLong(HistoryResumeActivity.this,
				bean.getErrorMsg());
		}
		// initDate(bean);
	    }

	    @Override
	    public void onResponseDataError(Request<HistoryInfoBean> equest) {
		// ToastUtil.showLong(HistoryResumeActivity.this, "请求失败");
	    }

	    @Override
	    public void onResponseConnectionError(
		    Request<HistoryInfoBean> request, int statusCode) {
		// ToastUtil.showLong(HistoryResumeActivity.this, "请求失败");
	    }

	    @Override
	    public void onResponseFzzError(Request<HistoryInfoBean> request,
		    ErrorMsg errorMsg) {
		// ToastUtil.showLong(HistoryResumeActivity.this, "请求失败");
	    }

	});

    }
}
