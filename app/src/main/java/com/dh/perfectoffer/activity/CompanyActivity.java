package com.dh.perfectoffer.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.dh.perfectoffer.R;
import com.dh.perfectoffer.adapter.ArrayListAdapter;
import com.dh.perfectoffer.adapter.ViewHolder;
import com.dh.perfectoffer.constant.Constant;
import com.dh.perfectoffer.demo.VehicleActivity;
import com.dh.perfectoffer.dhutils.CommUtil;
import com.dh.perfectoffer.dhutils.ImageLoaderUtil;
import com.dh.perfectoffer.entity.OfficeBean;
import com.dh.perfectoffer.entity.OfficeListBean;
import com.dh.perfectoffer.event.framework.alibaba.fastjson.JSON;
import com.dh.perfectoffer.event.framework.net.exception.DataException;
import com.dh.perfectoffer.event.framework.net.fgview.Action;
import com.dh.perfectoffer.event.framework.net.fgview.BaseParser;
import com.dh.perfectoffer.event.framework.net.fgview.OnResponseListener;
import com.dh.perfectoffer.event.framework.net.fgview.Request;
import com.dh.perfectoffer.event.framework.net.fgview.Response;
import com.dh.perfectoffer.event.framework.net.fgview.Response.ErrorMsg;
import com.dh.perfectoffer.event.framework.util.StringUtils;
import com.dh.perfectoffer.xutils.sample.utils.ToastUtil;
import com.dh.perfectoffer.xutils.view.ViewUtils;
import com.dh.perfectoffer.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.HashMap;


public class CompanyActivity extends VehicleActivity {
    private String comid;//

    @ViewInject(R.id.company_name)
    private TextView company_name;
    @ViewInject(R.id.company_content)
    private TextView company_content;
    @ViewInject(R.id.companylist)
    private ListView company_list;
//    @ViewInject(R.id.icon)
//    private ImageView icon;
    @ViewInject(R.id.company_empty)
    private View empty;

    private CompanyAdapter mAdapter;
    private ArrayList<OfficeBean> mPostList;
    private View mHeaderView;
    private LinearLayout linear_id;

    // private String postId = "";
    private String companyicon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	setContentView(R.layout.companyactivity);
	ViewUtils.inject(this);
	super.initTop();
	 setTitle("公司详情");
//	setTitle(CommUtil.getInstance().getStringFromXml(CompanyActivity.this,
//		R.string.com_titie));
	// querycompanylist();
	// Intent intent=getIntent();

	Bundle bundle = this.getIntent().getExtras();
	comid = bundle.getString("companyid");
	company_name.setText(bundle.getString("companyname"));
	company_content.setText(bundle.getString("companycontent"));
	companyicon = bundle.getString("companyicon");
	initData();
    }

    public void initData() {
	// empty.setVisibility(View.VISIBLE);
	// company_list.setVisibility(View.GONE);
	// mHeaderView = LayoutInflater.inflate(R.layout.home_head, null);

	// linear_id = (LinearLayout) findViewById(R.id.ll_empty);
	// linear_id.setVisibility(View.VISIBLE);
	mPostList = new ArrayList<OfficeBean>();
	mAdapter = new CompanyAdapter(CompanyActivity.this);
	mAdapter.setList(mPostList);
	company_list.setAdapter(mAdapter);
//	setImageViewByView(icon, R.drawable.icon_temp_company,
//		Constant.URLIPPIC + companyicon);

	querycompanylist();

	// mList=new ArrayList<OfficeBean>();
	// list=new ArrayList<OfficeAdapterBean>();
	// empty.setVisibility(View.GONE);
	// company_list.setVisibility(View.VISIBLE);
	// mAdapter = new CompanyAdapter(CompanyActivity.this);
	// mAdapter.setList(list);
	// company_list.setAdapter(mAdapter);
	// company_list.setOnItemClickListener(new OnItemClickListener() {
	//
	// @Override
	// public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
	// long arg3) {
	// // TODO Auto-generated method stub
	// Intent intent = new Intent(CompanyActivity.this,
	// HistoryResumeActivity.class);
	// intent.putExtra("postId", postId);
	// Log.e("xipp:", bean.getRew_id().toString());
	// launch(intent);
	// }
	// });
    }

    private void setImageViewByView(final ImageView view, int DefaultIconId,
	    String IconUrl) {
	if (!StringUtils.isBlank(IconUrl)) {
	    ImageLoaderUtil.getInstance().displayImage(IconUrl, view,
		    DefaultIconId); // 不使用原图
	    // ImageLoaderUtil.getInstance().loadImage(IconUrl,
	    // new ImageLoaderCompleteListener() {
	    //
	    // @Override
	    // public void onCompleteImageLoader(Bitmap bitmap) {
	    // view.setBackgroundDrawable(new BitmapDrawable(
	    // bitmap));
	    // }
	    // });
	} else {
	    view.setImageResource(DefaultIconId);
	    // view.setBackgroundResource(DefaultIconId);
	}
    }

    private void querycompanylist() {
	HashMap<String, String> pram = new HashMap<String, String>();
	pram.put("sub_id", comid);

	pram.put("size", "100");
	pram.put("page", "1");
	// pram.put("userpass", password.getText().toString());
	// String bodyRequestParam = JSON.toJSONString(bean);
	// 请求parser
	Request<OfficeListBean> req = new Request<OfficeListBean>();
	req.setRequestMethod(Request.M_POST);
	req.setBaseParser(new BaseParser<OfficeListBean>() {
	    @Override
	    public OfficeListBean parseResDate(String resBody)
		    throws DataException {
		if (resBody != null && !resBody.equals("")) {
		    // Preference.putString(Constant.PRE_USER_NAME, resBody);
		    return JSON.parseObject(resBody, OfficeListBean.class);
		}
		return null;
	    }
	});
	// req.setBodyRequestParam(bodyRequestParam);
	req.setRequestParams(pram);
	req.setUrl(Constant.URLIP + "hr/SubServlet");

	Action action = new Action(CompanyActivity.this);
	action.setDefaultLoadingTipOpen(true);
	action.setShowErrorDialog(true);
	action.execute(req, new OnResponseListener<OfficeListBean>() {
	    @Override
	    public void onResponseFinished(Request<OfficeListBean> request,
		    Response<OfficeListBean> response) {
		OfficeListBean bean = new OfficeListBean();
		bean = response.getT();
		if (null != bean) {

		    if (bean.getSuccess().equals("1")) {
			if (null != bean.getPostList()
				&& bean.getPostList().size() > 0) {
			    // list = new ArrayList<OfficeAdapterBean>();
			    mPostList.clear();
			    mPostList.addAll(bean.getPostList());
			    empty.setVisibility(View.GONE);
			    company_list.setVisibility(View.VISIBLE);
			    mAdapter.notifyDataSetChanged();
			} else {
			    empty.setVisibility(View.VISIBLE);
			    company_list.setVisibility(View.GONE);
			}

		    } else {

			ToastUtil.showLong(CompanyActivity.this,
				bean.getErrorMsg());
		    }
		}

	    }

	    @Override
	    public void onResponseDataError(Request<OfficeListBean> equest) {
		// ToastUtil.showLong(CompanyActivity.this, "请求失败");
	    }

	    @Override
	    public void onResponseConnectionError(
		    Request<OfficeListBean> request, int statusCode) {
		// ToastUtil.showLong(CompanyActivity.this, "请求失败");
	    }

	    @Override
	    public void onResponseFzzError(Request<OfficeListBean> request,
		    ErrorMsg errorMsg) {
		// ToastUtil.showLong(CompanyActivity.this, "请求失败");
	    }

	});

    }

    class CompanyAdapter extends ArrayListAdapter<OfficeBean> {

	public CompanyAdapter(Activity context) {
	    super(context);
	}

	public View getView(final int position, View convertView,
		ViewGroup parent) {
	    if (convertView == null) {
		convertView = LayoutInflater.from(mContext).inflate(
			R.layout.item_company, null);
	    }
	    TextView jog_name = ViewHolder.get(convertView, R.id.tv_job_name);
	    TextView job_claim = ViewHolder.get(convertView, R.id.tv_job_claim);
	    TextView job_bounty = ViewHolder.get(convertView,
		    R.id.tv_job_bounty);
	    TextView job_salary = ViewHolder.get(convertView,
		    R.id.tv_job_salary);

	    //
	    // TextView create_time = ViewHolder
	    // .get(convertView, R.id.create_time);
	    // TextView rew_id = ViewHolder.get(convertView, R.id.rew_id);
	    final OfficeBean bean = mList.get(position);

	    if (bean != null) {
		convertView.setOnClickListener(new OnClickListener() {

		    @Override
		    public void onClick(View v) {
			// TODO Auto-generated method stub
			// TODO Auto-generated method stub
			Intent intent = new Intent(CompanyActivity.this,
				HistoryResumeActivity.class);
			intent.putExtra("postId", bean.getPost_id());
			// Log.e("xipp:", bean.getRew_id().toString());
			launch(intent);
		    }
		});
		if (!bean.getBounty().isEmpty()) {
		    job_bounty.setText(CommUtil.getInstance().getStringFromXml(
			    CompanyActivity.this, R.string.com_bounty)
			    + bean.getBounty());
		} else
		    job_bounty.setText(CommUtil.getInstance().getStringFromXml(
			    CompanyActivity.this, R.string.com_bounty)
			    + "0");
		jog_name.setText(bean.getPost_name());
		job_claim.setText(CommUtil.getInstance().getStringFromXml(
			CompanyActivity.this, R.string.com_requre)
			+ bean.getReward_claim());
		if (!TextUtils.isEmpty(bean.getReward_salary())) {
		    if (!"面议".equals(bean.getReward_salary())) {
			job_salary.setText(bean.getReward_salary() + "/月");
		    } else {
			job_salary.setText(bean.getReward_salary());
		    }
		}

	    }
	    return convertView;
	}
    }
}
