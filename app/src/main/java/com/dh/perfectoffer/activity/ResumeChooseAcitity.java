package com.dh.perfectoffer.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dh.perfectoffer.R;
import com.dh.perfectoffer.VehicleApp;
import com.dh.perfectoffer.adapter.ArrayListAdapter;
import com.dh.perfectoffer.adapter.ViewHolder;
import com.dh.perfectoffer.constant.Constant;
import com.dh.perfectoffer.demo.VehicleActivity;
import com.dh.perfectoffer.dhutils.CommUtil;
import com.dh.perfectoffer.entity.BaseBean;
import com.dh.perfectoffer.entity.ResumeTempBean;
import com.dh.perfectoffer.entity.RusumeListBean;
import com.dh.perfectoffer.event.ComEvent;
import com.dh.perfectoffer.event.EventBus;
import com.dh.perfectoffer.event.framework.alibaba.fastjson.JSON;
import com.dh.perfectoffer.event.framework.log.L;
import com.dh.perfectoffer.event.framework.net.exception.DataException;
import com.dh.perfectoffer.event.framework.net.fgview.Action;
import com.dh.perfectoffer.event.framework.net.fgview.BaseParser;
import com.dh.perfectoffer.event.framework.net.fgview.OnResponseListener;
import com.dh.perfectoffer.event.framework.net.fgview.Request;
import com.dh.perfectoffer.event.framework.net.fgview.Response;
import com.dh.perfectoffer.event.framework.net.fgview.Response.ErrorMsg;
import com.dh.perfectoffer.xutils.sample.utils.Preference;
import com.dh.perfectoffer.xutils.sample.utils.ToastUtil;
import com.dh.perfectoffer.xutils.view.ViewUtils;
import com.dh.perfectoffer.xutils.view.annotation.ViewInject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;


public class ResumeChooseAcitity extends VehicleActivity {
    @ViewInject(R.id.list)
    private ListView lv_resume;
    private ResumeAdapter resumeAdapter;
    private ArrayList<ResumeTempBean> resumeArrayList;
    @ViewInject(R.id.btn_top_right)
    protected Button btn_top_right;
    private String postId = "";
    // private ArrayList<ResumeTempChooseBean> resumeChooseArrayList;
    // private ArrayList<HistroyTempBean> hisArrayList;
    private String choose;
    private ResumeTempBean chooseBean;
    private int select = -1;

    private String date = "";

    public int getSelect() {
	return select;
    }

    public void setSelect(int select) {
	this.select = select;
    }

    DateFormat fmtDate = new SimpleDateFormat("yyyy-MM-dd");;
    // DateFormat fmtDate = DateFormat.getDateTimeInstance();
    Calendar date2 = Calendar.getInstance(Locale.CHINA);
    private boolean mFired = false;

    // DatePickerDialog.OnDateSetListener d2 = new
    // DatePickerDialog.OnDateSetListener() {
    //
    // @Override
    // public void onDateSet(DatePicker view, int year, int monthOfYear,
    // int dayOfMonth) {
    // if (!mFired) {
    // // 修改日历控件的年，月，日
    // // 这里的year,monthOfYear,dayOfMonth的值与DatePickerDialog控件设置的最新值一致
    // date2.set(Calendar.YEAR, year);
    // date2.set(Calendar.MONTH, monthOfYear);
    // date2.set(Calendar.DAY_OF_MONTH, dayOfMonth);
    // // 将页面TextView的显示更新为最新时间
    // updateLabeltime();
    // mFired = true;
    // } else {
    // return;
    // }
    // }
    // };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_rusemechoose);

	ViewUtils.inject(this);
	if (null != EventBus.getDefault()) {
	    EventBus.getDefault().unregister(this);
	}
	EventBus.getDefault().register(this);
	// choose=intent.getStringExtra("resumeId");
	super.initTop();
	// setTitle("简历选择");
	setTitle(CommUtil.getInstance().getStringFromXml(
		ResumeChooseAcitity.this, R.string.choose));
	initview();

    }

    private void initview() {
	btn_top_right.setBackgroundDrawable(getResources().getDrawable(
		R.drawable.icon_add_resume));
	btn_top_right.setVisibility(View.VISIBLE);
	btn_top_right.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		// TODO Auto-generated method stub
		if (null == VehicleApp.getInstance().getUserBean()
			|| TextUtils.isEmpty(VehicleApp.getInstance()
				.getUserBean().getUsertosk())) {
		    launch(LoginActivity.class);
		    return;
		}
		Intent intent = new Intent(ResumeChooseAcitity.this,
			RecommendationActivity.class);
		intent.putExtra("isAdd", true);
		launch(intent);
	    }
	});
	resumeArrayList = (ArrayList<ResumeTempBean>) getIntent()
		.getSerializableExtra("choose");
	// resumeArrayList.addAll(resumeArrayList);
	postId = getIntent().getStringExtra("postId");

	resumeAdapter = new ResumeAdapter(ResumeChooseAcitity.this);
	resumeAdapter.setList(resumeArrayList);
	lv_resume.setAdapter(resumeAdapter);

    }

    private void updateLabeltime() {

	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
	String str = formatter.format(curDate);
	String strTemp = fmtDate.format(date2.getTime());
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	Date d1 = null;

	Date d2 = null;
	try {

	    d1 = sdf.parse(strTemp);
	    d2 = sdf.parse(str);
	} catch (ParseException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	// 比较
	if (d1.getTime() - d2.getTime() >= 0) {
	    date = fmtDate.format(date2.getTime());
	    queryadd(date);
	} else
	    ToastUtil.showShort(ResumeChooseAcitity.this, getResources()
		    .getString(R.string.recom_time));

	// date.setText(fmtDate.format(date2.getTime()));
    }

    public void queryadd(String reservation_date) {
	if (null == VehicleApp.getInstance().getUserBean()
		|| TextUtils.isEmpty(VehicleApp.getInstance().getUserBean()
			.getUsertosk())) {
	    launch(LoginActivity.class);
	    return;
	}
	HashMap<String, String> pram = new HashMap<String, String>();
	pram.put("username", VehicleApp.getInstance().getUserBean()
		.getUsertosk());

	pram.put("fried_id", chooseBean.getFried_id());

	pram.put("rew_id", postId);
	pram.put("reservation_date", reservation_date);
	//
	// pram.put("user_age",
	// re_age.getText().toString());
	// pram.put("userpass", re_password.getText()
	// .toString());
	//
	// pram.put("rname",
	// re_name.getText().toString());
	// pram.put("user_phone",
	// re_phone.getText().toString());
	// Log.e("xipp:", sex);
	// String bodyRequestParam = JSON.toJSONString(bean);

	// pram.put("user_sex", sex);
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
	req.setUrl(Constant.URLIP + "hr/FriedSubmitServlet");

	Action action = new Action(ResumeChooseAcitity.this);
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
			ToastUtil.showLong(ResumeChooseAcitity.this,
				getResources().getString(R.string.recom_su));
			new Thread(new Runnable() {

			    @Override
			    public void run() {
				// 推送一个事件
				ComEvent appevent = new ComEvent(
					ComEvent.HISTROY);
				EventBus.getDefault().post(appevent);

			    }
			}).start();
			finish();
		    } else
			ToastUtil.showLong(ResumeChooseAcitity.this,
				bean.getErrorMsg());
		}
		// initDate(bean);
	    }

	    @Override
	    public void onResponseDataError(Request<BaseBean> equest) {
		// ToastUtil.showLong(ResumeChooseAcitity.this, "请求失败");
	    }

	    @Override
	    public void onResponseConnectionError(Request<BaseBean> request,
		    int statusCode) {
		// ToastUtil.showLong(ResumeChooseAcitity.this, "请求失败");
	    }

	    @Override
	    public void onResponseFzzError(Request<BaseBean> request,
		    ErrorMsg errorMsg) {
		// ToastUtil.showLong(ResumeChooseAcitity.this, "请求失败");
	    }

	});

    }

    private void initResume(ArrayList<ResumeTempBean> list) {
	resumeArrayList.clear();
	resumeArrayList.addAll(list);
	resumeAdapter.notifyDataSetChanged();
    }

    class ResumeAdapter extends ArrayListAdapter<ResumeTempBean> {

	public ResumeAdapter(Activity context) {
	    super(context);
	}

	public View getView(final int position, View convertView,
		ViewGroup parent) {
	    if (convertView == null) {
		convertView = LayoutInflater.from(mContext).inflate(
			R.layout.item_resumechoose, null);
	    }
	    final ImageView image = ViewHolder.get(convertView, R.id.iv_choose);

	    TextView tv_resume_name = ViewHolder.get(convertView,
		    R.id.tv_resume_name);

	    final ResumeTempBean bean = mList.get(position);
	    if (select == position) {
		chooseBean = bean;
		image.setBackgroundResource(R.drawable.register3);
	    } else
		image.setBackgroundResource(R.drawable.register2);
	    convertView.setOnClickListener(new OnClickListener() {
		@Override
		public String toString() {
		    // TODO Auto-generated method stub
		    return super.toString();
		}

		@Override
		public void onClick(View v) {
		    setSelect(position);
		    notifyDataSetChanged();
		    image.setBackgroundResource(R.drawable.register3);
		    chooseBean = bean;
		    mFired = false;
		    // new DatePickerDialog(ResumeChooseAcitity.this, d2, date2
		    // .get(Calendar.YEAR), date2.get(Calendar.MONTH),
		    // date2.get(Calendar.DAY_OF_MONTH)).show();
		    pickDate();

		}
	    });
	    if (bean != null) {
		tv_resume_name.setText(bean.getFriend_name());

	    }
	    return convertView;
	}
    }

    public void pickDate() {

	Calendar cal = Calendar.getInstance(Locale.CHINA);
	final DatePickerDialog mDialog = new DatePickerDialog(this, null,
		cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
		cal.get(Calendar.DAY_OF_MONTH));
	mDialog.setTitle("请选择预约时间");
	// 取消按钮，如果不需要直接不设置即可
	mDialog.setButton(
		DialogInterface.BUTTON_NEGATIVE,
		CommUtil.getInstance().getStringFromXml(
			ResumeChooseAcitity.this, R.string.more_cancle),
		new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {

		    }
		});
	// 手动设置按钮
	mDialog.setButton(
		DialogInterface.BUTTON_POSITIVE,
		CommUtil.getInstance().getStringFromXml(
			ResumeChooseAcitity.this, R.string.more_shure1),
		new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
			// 通过mDialog.getDatePicker()获得dialog上的DatePicker组件，然后可以获取日期信息
			DatePicker datePicker = mDialog.getDatePicker();
			int year = datePicker.getYear();
			int month = datePicker.getMonth();
			int day = datePicker.getDayOfMonth();
			if (!mFired) {
			    // 修改日历控件的年，月，日
			    // 这里的year,monthOfYear,dayOfMonth的值与DatePickerDialog控件设置的最新值一致
			    date2.set(Calendar.YEAR, year);
			    date2.set(Calendar.MONTH, month);
			    date2.set(Calendar.DAY_OF_MONTH, day);
			    // 将页面TextView的显示更新为最新时间
			    updateLabeltime();
			    mFired = true;
			} else {
			    return;
			}
		    }
		});

	mDialog.show();

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

		    RusumeListBean bean = JSON.parseObject(resBody,
			    RusumeListBean.class);
		    if (null != bean && bean.getSuccess().equals("1"))
			Preference.putString(Constant.PRE_RESUME_NAME
				+ Preference.getString(Constant.PRE_USER_NAME),
				resBody);
		    return JSON.parseObject(resBody, RusumeListBean.class);
		}
		return null;
	    }
	});
	req.setRequestParams(pram);
	req.setUrl(Constant.URLIP + "hr/FriedResumeListServlet");

	Action action = new Action(ResumeChooseAcitity.this);
	action.setDefaultLoadingTipOpen(false);
	action.setShowErrorDialog(true);
	action.execute(req, new OnResponseListener<RusumeListBean>() {
	    @Override
	    public void onResponseFinished(Request<RusumeListBean> request,
		    Response<RusumeListBean> response) {
		RusumeListBean bean = new RusumeListBean();
		ArrayList<ResumeTempBean> list = new ArrayList<ResumeTempBean>();
		bean = response.getT();
		if (null != bean) {
		    if (bean.getSuccess().equals("1")) {
			if (null != bean.getFriedResumeList()
				&& bean.getFriedResumeList().size() > 0) {
			    list.addAll(bean.getFriedResumeList());

			}
			initResume(list);
		    } else {
			ToastUtil.showShort(ResumeChooseAcitity.this,
				bean.getErrorMsg());
		    }
		}
	    }

	    @Override
	    public void onResponseDataError(Request<RusumeListBean> equest) {
	    }

	    @Override
	    public void onResponseConnectionError(
		    Request<RusumeListBean> request, int statusCode) {
	    }

	    @Override
	    public void onResponseFzzError(Request<RusumeListBean> request,
		    ErrorMsg errorMsg) {
	    }

	});
    }

    public void onEventMainThread(ComEvent event) {
	if (event.getType() == ComEvent.RESUME) {
	    L.e("yinzl", "刷新简历列表");
	    queryResumeList();
	}
    }
}
