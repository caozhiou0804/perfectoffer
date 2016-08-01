package com.dh.perfectoffer.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.dh.perfectoffer.R;
import com.dh.perfectoffer.VehicleApp;
import com.dh.perfectoffer.constant.Constant;
import com.dh.perfectoffer.demo.VehicleActivity;
import com.dh.perfectoffer.dhutils.CommUtil;
import com.dh.perfectoffer.entity.BaseBean;
import com.dh.perfectoffer.entity.FriedBean;
import com.dh.perfectoffer.entity.ResumeBean;
import com.dh.perfectoffer.event.ComEvent;
import com.dh.perfectoffer.event.EventBus;
import com.dh.perfectoffer.event.framework.alibaba.fastjson.JSON;
import com.dh.perfectoffer.event.framework.net.exception.DataException;
import com.dh.perfectoffer.event.framework.net.fgview.Action;
import com.dh.perfectoffer.event.framework.net.fgview.BaseParser;
import com.dh.perfectoffer.event.framework.net.fgview.OnResponseListener;
import com.dh.perfectoffer.event.framework.net.fgview.Request;
import com.dh.perfectoffer.event.framework.net.fgview.Response;
import com.dh.perfectoffer.view.WorkYearDialog;
import com.dh.perfectoffer.xutils.sample.utils.ToastUtil;
import com.dh.perfectoffer.xutils.view.ViewUtils;
import com.dh.perfectoffer.xutils.view.annotation.ViewInject;
import com.dh.perfectoffer.xutils.view.annotation.event.OnClick;
import com.dh.perfectoffer.event.framework.net.fgview.Response.ErrorMsg;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;


public class RecommendationActivity extends VehicleActivity {
    @ViewInject(R.id.recom_title)
    private EditText title;
    @ViewInject(R.id.recom_name)
    private EditText name;
    @ViewInject(R.id.recom_phone)
    private EditText phone;

    @ViewInject(R.id.recom_time)
    private TextView time;

    @ViewInject(R.id.recom_address)
    private EditText address;
    // @ViewInject(R.id.recom_date)
    // private TextView date;
    @ViewInject(R.id.recom_mailbox)
    private EditText mailbox;
    @ViewInject(R.id.recom_professional)
    private EditText professional;
    @ViewInject(R.id.recom_school)
    EditText school;
    @ViewInject(R.id.recom_remark)
    EditText remark;

    private String fromactivity;
    private boolean value = true;

    // @ViewInject(R.id.recom_img1)
    // private ImageView year1;
    // @ViewInject(R.id.recom_img2)
    // private ImageView year2;
    // @ViewInject(R.id.recom_img3)
    // private ImageView year3;
    // @ViewInject(R.id.recom_img4)
    // private ImageView year4;
    // @ViewInject(R.id.recom_lin1)
    // private LinearLayout img1;
    // @ViewInject(R.id.recom_lin2)
    // private LinearLayout img2;
    // @ViewInject(R.id.recom_lin3)
    // private LinearLayout img3;
    // @ViewInject(R.id.recom_lin4)
    // private LinearLayout img4;

    @ViewInject(R.id.recom_man)
    private ImageView man;
    @ViewInject(R.id.recom_woman)
    private ImageView woman;

    @ViewInject(R.id.recom_sex1)
    private LinearLayout sex1;
    @ViewInject(R.id.recom_sex2)
    private LinearLayout sex2;

    @ViewInject(R.id.age_limit)
    private TextView age_limit;

    @ViewInject(R.id.btn_top_right)
    private Button btn_add_resume;// 新建简历
    private String friendid = "";
    private String type = "1";
    private String year = "小于2年";
    private String sex = "1";

    private static String TAG_CLICK = "";// 判断是否新建简历
    private String select = "";

    private String Poseid = "";
    private String Friendid = "";

    private boolean ageselect = false;
    private boolean timeselect = false;
    private PopupWindow workStatusPop;
    // DateFormat fmtDateAndTime = DateFormat.getDateTimeInstance();
    DateFormat fmtDateAndTime = new SimpleDateFormat("yyyy-MM-dd");
    ;
    Calendar dateAndTime = Calendar.getInstance(Locale.CHINA);
    //
    // DatePickerDialog.OnDateSetListener d = new
    // DatePickerDialog.OnDateSetListener() {
    // @Override
    // public void onDateSet(DatePicker view, int year, int monthOfYear,
    // int dayOfMonth) {
    // if (!ageselect) {
    //
    // // 修改日历控件的年，月，日
    // // 这里的year,monthOfYear,dayOfMonth的值与DatePickerDialog控件设置的最新值一致
    // dateAndTime.set(Calendar.YEAR, year);
    // dateAndTime.set(Calendar.MONTH, monthOfYear);
    // dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
    // // 将页面TextView的显示更新为最新时间
    // updateLabel();
    // ageselect = true;
    // } else {
    // return;
    // }
    // }
    // };
    DateFormat fmtDate = new SimpleDateFormat("yyyy-MM-dd");
    ;
    // DateFormat fmtDate = DateFormat.getDateTimeInstance();
    Calendar date2 = Calendar.getInstance(Locale.CHINA);

    //
    // DatePickerDialog.OnDateSetListener d2 = new
    // DatePickerDialog.OnDateSetListener() {
    // @Override
    // public void onDateSet(DatePicker view, int year, int monthOfYear,
    // int dayOfMonth) {
    // if (!timeselect) {
    // // 修改日历控件的年，月，日
    // // 这里的year,monthOfYear,dayOfMonth的值与DatePickerDialog控件设置的最新值一致
    // date2.set(Calendar.YEAR, year);
    // date2.set(Calendar.MONTH, monthOfYear);
    // date2.set(Calendar.DAY_OF_MONTH, dayOfMonth);
    // // 将页面TextView的显示更新为最新时间
    //
    // updateLabeltime();
    //
    // timeselect = true;
    // } else {
    // return;
    // }
    // }
    // };

    //
    //
    // TimePickerDialog.OnTimeSetListener t = new
    // TimePickerDialog.OnTimeSetListener() {
    //
    // //同DatePickerDialog控件
    // @Override
    // public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
    // date2.set(Calendar.HOUR_OF_DAY, hourOfDay);
    // date2.set(Calendar.MINUTE, minute);
    // updateLabel();
    //
    // }
    // };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendation);
        ViewUtils.inject(this);
        super.initTop();
        Intent intent = getIntent();
        friendid = intent.getStringExtra("resumeId");
        title.setText(intent.getStringExtra("resumeName"));
        fromactivity = intent.getStringExtra("isAdd");
        select = intent.getStringExtra("choosename");

        Poseid = intent.getStringExtra("choosepsotid");
        // updateLabel();
        if (!TextUtils.isEmpty(friendid)) {
            setTitle(getResources().getString(R.string.recom_t1));
            value = true;
            // btn_add_resume.setText("完成");
            // btn_add_resume.setBackgroundResource(R.color.black);
            // btn_add_resume.setBackgroundResource(R.drawable.ruseme2);
            queryResume();
            btn_add_resume.setVisibility(View.VISIBLE);
            btn_add_resume.setText("编辑");
            btn_add_resume.setTextSize(14);
            btn_add_resume.setTextColor(getResources().getColor(R.color.white));
            btn_add_resume.setBackgroundResource(R.color.color_00000000);
            lockUnlock(false);

            TAG_CLICK = "1";
        } else {
            setTitle(getResources().getString(R.string.recom_t2));
            lockUnlock(true);
            btn_add_resume.setVisibility(View.VISIBLE);
            btn_add_resume.setText("完成");
            btn_add_resume.setTextSize(14);
            btn_add_resume.setTextColor(getResources().getColor(R.color.white));
            btn_add_resume.setBackgroundResource(R.color.color_00000000);
            // btn_add_resume.setBackgroundResource(R.drawable.ruseme1);

            TAG_CLICK = "2";
        }
        btn_add_resume.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if (TAG_CLICK == "2") {

                    if ("select".equals(select)) {
                        // new DatePickerDialog(RecommendationActivity.this, d2,
                        // date2.get(Calendar.YEAR), date2
                        // .get(Calendar.MONTH), date2
                        // .get(Calendar.DAY_OF_MONTH)).show();
                        if (isNoError()) {
                            timepick();
                            timeselect = false;
                        }
                    } else {
                        if (isNoError())
                            addResume();
                    }

                } else if (TAG_CLICK == "1") {
                    Log.e("xipp:", btn_add_resume.getText().toString());

                    btn_add_resume.setVisibility(View.VISIBLE);
                    btn_add_resume.setText("完成");
                    btn_add_resume.setTextSize(14);
                    btn_add_resume.setTextColor(getResources().getColor(
                            R.color.white));
                    btn_add_resume
                            .setBackgroundResource(R.color.color_00000000);
                    // btn_add_resume.setBackgroundResource(R.drawable.ruseme1);
                    TAG_CLICK = "3";

                    lockUnlock(true);

                } else {
                    type = "2";
                    if (isNoError())
                        addResume();
                }
            }
        });
    }

    private void lockUnlock(boolean value) {
        CommUtil.getInstance().isLock(name, value);
        CommUtil.getInstance().isLock(title, value);
        CommUtil.getInstance().isLock(phone, value);
        CommUtil.getInstance().isLock(time, value);
        CommUtil.getInstance().isLock(address, value);
        CommUtil.getInstance().isLock(mailbox, value);
        CommUtil.getInstance().isLock(professional, value);
        CommUtil.getInstance().isLock(school, value);
        CommUtil.getInstance().isLock(remark, value);
        CommUtil.getInstance().isLock(age_limit, value);
        // CommUtil.getInstance().isLock(img1, value);
        // CommUtil.getInstance().isLock(img2, value);
        // CommUtil.getInstance().isLock(img3, value);
        // CommUtil.getInstance().isLock(img4, value);
        CommUtil.getInstance().isLock(sex1, value);
        CommUtil.getInstance().isLock(sex2, value);

        name.setFocusableInTouchMode(value);

        title.setFocusableInTouchMode(value);

        phone.setFocusableInTouchMode(value);

        address.setFocusableInTouchMode(value);

        mailbox.setFocusableInTouchMode(value);

        professional.setFocusableInTouchMode(value);

        school.setFocusableInTouchMode(value);

        remark.setFocusableInTouchMode(value);

    }

    @OnClick(R.id.age_limit)
    public void onclickage_limit(View view) {
        showWorkDialog(RecommendationActivity.this);
    }

    @OnClick(R.id.recom_time)
    public void onclickselecttime(View view) {
        if ("".equals(time.getText())) {
            Calendar cal = Calendar.getInstance(Locale.CHINA);
            final DatePickerDialog mDialog = new DatePickerDialog(this, null,
                    cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH));
            agepick(mDialog);
        } else {
            String[] dataStr = time.getText().toString().split("-");
            final DatePickerDialog mOldDialog = new DatePickerDialog(this,
                    null, Integer.valueOf(dataStr[0]),
                    Integer.valueOf(dataStr[1]) - 1,
                    Integer.valueOf(dataStr[2]));
            agepick(mOldDialog);
        }
        // new DatePickerDialog(RecommendationActivity.this, d,
        // dateAndTime.get(Calendar.YEAR),
        // dateAndTime.get(Calendar.MONTH),
        // dateAndTime.get(Calendar.DAY_OF_MONTH)).show();
        ageselect = false;
    }

    // @OnClick(R.id.recom_date)
    // public void selectdate(View view) {
    //
    // new DatePickerDialog(RecommendationActivity.this, d2,
    // date2.get(Calendar.YEAR), date2.get(Calendar.MONTH),
    // date2.get(Calendar.DAY_OF_MONTH)).show();
    // timeselect = false;
    // }

    @OnClick(R.id.recom_sex1)
    public void onclicksexbt1(View view) {
        woman.setBackgroundResource(R.drawable.radiobtn_no_check);
        man.setBackgroundResource(R.drawable.radiobtn_check);
        sex = "1";

    }

    @OnClick(R.id.recom_sex2)
    public void onclicksexbt2(View view) {
        woman.setBackgroundResource(R.drawable.radiobtn_check);
        man.setBackgroundResource(R.drawable.radiobtn_no_check);

        sex = "0";

    }

    // public static boolean isEmail(String email) {
    //
    // if (email == null || email.trim().length() == 0)
    //
    // return false;
    //
    // return emailer.matcher(email).matches();
    // }
    private boolean isNoError() {
        if (title.getText().toString().trim().isEmpty()) {
            ToastUtil.showShort(RecommendationActivity.this, getResources()
                    .getString(R.string.recom_title));
            return false;
        }

        if (name.getText().toString().trim().isEmpty()) {
            ToastUtil.showShort(RecommendationActivity.this, this
                    .getResources().getString(R.string.recom_name));
            return false;
        }
        if (time.getText().toString().isEmpty()) {
            ToastUtil.showShort(RecommendationActivity.this, this
                    .getResources().getString(R.string.recom_age));
            return false;
        }
        if (school.getText().toString().trim().isEmpty()) {
            ToastUtil.showShort(RecommendationActivity.this, this
                    .getResources().getString(R.string.recom_school));
            return false;
        }
        if (professional.getText().toString().trim().isEmpty()) {
            ToastUtil.showShort(RecommendationActivity.this, getResources()
                    .getString(R.string.recom_professional));
            return false;
        }
        if (!CommUtil.getInstance().isPhoneNumber(phone.getText().toString())) {
            ToastUtil.showShort(RecommendationActivity.this, getResources()
                    .getString(R.string.recom_phone));
            return false;
        }
        if (!CommUtil.getInstance().isMail(mailbox.getText().toString())) {
            ToastUtil.showShort(RecommendationActivity.this, getResources()
                    .getString(R.string.recom_email));
            return false;
        }
        if (address.getText().toString().isEmpty()) {
            ToastUtil.showShort(RecommendationActivity.this, getResources()
                    .getString(R.string.recom_address));
            return false;
            // } if (date.getText().toString().isEmpty()) {
            // ToastUtil.showShort(RecommendationActivity.this,
            // "请输入预约时间");
            // return;
        } else if (CommUtil.getInstance().containsEmoji(
                title.getText().toString()) == true) {
            ToastUtil.showLong(getApplicationContext(), getResources()
                    .getString(R.string.recom_title2));
            return false;
        } else if (CommUtil.getInstance().containsEmoji(
                name.getText().toString()) == true) {
            ToastUtil.showLong(getApplicationContext(), getResources()
                    .getString(R.string.recom_name2));
            return false;
        } else if (CommUtil.getInstance().containsEmoji(
                school.getText().toString()) == true) {
            ToastUtil.showLong(getApplicationContext(), getResources()
                    .getString(R.string.recom_school2));
            return false;
        } else if (CommUtil.getInstance().containsEmoji(
                professional.getText().toString()) == true) {
            ToastUtil.showLong(getApplicationContext(), getResources()
                    .getString(R.string.recom_professional2));
            return false;
        } else if (CommUtil.getInstance().containsEmoji(
                address.getText().toString()) == true) {
            ToastUtil.showLong(getApplicationContext(), getResources()
                    .getString(R.string.recom_address2));
            return false;
        } else if (CommUtil.getInstance().containsEmoji(
                remark.getText().toString()) == true) {
            ToastUtil.showLong(getApplicationContext(), getResources()
                    .getString(R.string.recom_remark2));
            return false;
        }
        return true;
    }

    private void addResume() {

        HashMap<String, String> pram = new HashMap<String, String>();

        if (null == VehicleApp.getInstance().getUserBean()
                || TextUtils.isEmpty(VehicleApp.getInstance().getUserBean()
                .getUsertosk())) {
            launch(LoginActivity.class);
            return;
        }

        pram.put("fried_id", friendid);

        pram.put("type", type);
        pram.put("username", VehicleApp.getInstance().getUserBean()
                .getUsertosk());
        pram.put("friend_name", title.getText().toString().trim());
        pram.put("name", name.getText().toString().trim());
        pram.put("sex", sex);
        pram.put("age", time.getText().toString().trim());
        pram.put("schools", school.getText().toString().trim());
        pram.put("specialty", professional.getText().toString().trim());
        pram.put("phone", phone.getText().toString());
        pram.put("email", mailbox.getText().toString().trim());
        pram.put("address", address.getText().toString().trim());
        pram.put("work_year", year);
        // pram.put("reservation_date", date.getText().toString());
        pram.put("remark", remark.getText().toString().trim());

        // 请求parser
        Request<FriedBean> req = new Request<FriedBean>();
        req.setRequestMethod(Request.M_POST);
        req.setBaseParser(new BaseParser<FriedBean>() {
            @Override
            public FriedBean parseResDate(String resBody) throws DataException {
                if (resBody != null && !resBody.equals("")) {
                    // Preference.putString(Constant.PRE_USER_NAME, resBody);
                    return JSON.parseObject(resBody, FriedBean.class);
                }
                return null;
            }
        });
        // req.setBodyRequestParam(bodyRequestParam);
        req.setRequestParams(pram);
        req.setUrl(Constant.URLIP + "hr/FriedResumeManageServlet");

        Action action = new Action(RecommendationActivity.this);
        action.setDefaultLoadingTipOpen(true);
        action.setShowErrorDialog(true);
        action.execute(req, new OnResponseListener<FriedBean>() {
            @Override
            public void onResponseFinished(Request<FriedBean> request,
                                           Response<FriedBean> response) {
                FriedBean bean = new FriedBean();
                bean = response.getT();
                if (null != bean) {
                    if (bean.getSuccess().equals("1")) {
                        if (type == "1") {
                            if ("select".equals(select)) {
                                Friendid = bean.getFried_id();
                                if (!TextUtils.isEmpty(Poseid)
                                        && !TextUtils.isEmpty(Friendid))
                                    queryadd(fmtDate.format(date2.getTime()));
                            } else {
                                ToastUtil.showLong(
                                        RecommendationActivity.this,
                                        getResources().getString(
                                                R.string.recom_re1));
                                finish();
                            }

                        }
                        if (type == "2") {
                            ToastUtil.showLong(RecommendationActivity.this,
                                    getResources()
                                            .getString(R.string.recom_re2));
                            finish();
                        }
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                // 推送一个事件
                                ComEvent appevent = new ComEvent(
                                        ComEvent.RESUME);
                                EventBus.getDefault().post(appevent);

                            }
                        }).start();

                    } else
                        ToastUtil.showLong(RecommendationActivity.this,
                                bean.getErrorMsg());
                }
                // initDate(bean);
            }

            @Override
            public void onResponseDataError(Request<FriedBean> equest) {
                // ToastUtil.showLong(RecommendationActivity.this, "请求失败");
            }

            @Override
            public void onResponseConnectionError(Request<FriedBean> request,
                                                  int statusCode) {
                // ToastUtil.showLong(RecommendationActivity.this, "请求失败");
            }

            @Override
            public void onResponseFzzError(Request<FriedBean> request,
                                           ErrorMsg errorMsg) {
                // ToastUtil.showLong(RecommendationActivity.this, "请求失败");
            }
        });
    }

    private void updateLabel() {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        String str = formatter.format(curDate);
        String strTemp = fmtDateAndTime.format(dateAndTime.getTime());
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
        if (d1.getTime() - d2.getTime() <= 0) {
            time.setText(fmtDateAndTime.format(dateAndTime.getTime()));

        } else
            ToastUtil.showShort(RecommendationActivity.this, getResources()
                    .getString(R.string.recom_time));

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
            e.printStackTrace();
        }
        // 比较
        if (d1.getTime() - d2.getTime() >= 0) {
            // date.setText(fmtDate.format(date2.getTime()));
            addResume();
        } else
            ToastUtil.showShort(RecommendationActivity.this, getResources()
                    .getString(R.string.recom_time));

        // date.setText(fmtDate.format(date2.getTime()));
    }

    private void queryResume() {
        HashMap<String, String> pram = new HashMap<String, String>();
        pram.put("fried_id", friendid);

        // pram.put("userpass", password.getText().toString());

        // String bodyRequestParam = JSON.toJSONString(bean);

        // 请求parser
        Request<ResumeBean> req = new Request<ResumeBean>();
        req.setRequestMethod(Request.M_POST);
        req.setBaseParser(new BaseParser<ResumeBean>() {
            @Override
            public ResumeBean parseResDate(String resBody) throws DataException {
                if (resBody != null && !resBody.equals("")) {
                    // Preference.putString(Constant.PRE_USER_NAME, resBody);
                    return JSON.parseObject(resBody, ResumeBean.class);
                }
                return null;
            }
        });

        // req.setBodyRequestParam(bodyRequestParam);
        req.setRequestParams(pram);
        req.setUrl(Constant.URLIP + "hr/FriedResumeDetailsServlet");

        Action action = new Action(RecommendationActivity.this);
        action.setDefaultLoadingTipOpen(true);
        action.setShowErrorDialog(true);
        action.execute(req, new OnResponseListener<ResumeBean>() {
            @Override
            public void onResponseFinished(Request<ResumeBean> request,
                                           Response<ResumeBean> response) {
                ResumeBean bean = new ResumeBean();
                bean = response.getT();
                if (null != bean) {

                    name.setText(bean.getName().trim());
                    phone.setText(bean.getPhone());
                    address.setText(bean.getAddress().trim());
                    // date.setText(bean.getReservation_date().split(" +")[0]);
                    mailbox.setText(bean.getEmail().trim());
                    professional.setText(bean.getSpecialty().trim());
                    school.setText(bean.getSchools().trim());
                    time.setText(bean.getAge().split(" +")[0]);
                    if (getResources().getString(R.string.recom_year1).equals(
                            bean.getWork_year())) {
                        year = getResources().getString(R.string.recom_year1);
                        age_limit.setText(year);
                    } else if (getResources().getString(R.string.recom_year2)
                            .equals(bean.getWork_year())) {
                        year = getResources().getString(R.string.recom_year2);
                        age_limit.setText(year);
                    } else if (getResources().getString(R.string.recom_year3)
                            .equals(bean.getWork_year())) {
                        year = getResources().getString(R.string.recom_year3);
                        age_limit.setText(year);
                    } else if (getResources().getString(R.string.recom_year4)
                            .equals(bean.getWork_year())) {
                        year = getResources().getString(R.string.recom_year4);
                        age_limit.setText(year);
                    }
                    if ("0".equals(bean.getSex())) {
                        // year1.setBackgroundResource(R.drawable.)

                        woman.setBackgroundResource(R.drawable.radiobtn_check);
                        man.setBackgroundResource(R.drawable.radiobtn_no_check);
                        sex = "0";
                    } else if ("1".equals(bean.getSex())) {
                        man.setBackgroundResource(R.drawable.radiobtn_check);
                        woman.setBackgroundResource(R.drawable.radiobtn_no_check);
                        sex = "1";
                    }
                    remark.setText(bean.getRemark().trim());

                } else
                    ToastUtil.showLong(RecommendationActivity.this,
                            bean.getErrorMsg());

            }

            // initDate(bean);
            // }

            @Override
            public void onResponseDataError(Request<ResumeBean> equest) {
                // ToastUtil.showLong(RecommendationActivity.this, "请求失败");
            }

            @Override
            public void onResponseConnectionError(Request<ResumeBean> request,
                                                  int statusCode) {
                // ToastUtil.showLong(RecommendationActivity.this, "请求失败");
            }

            @Override
            public void onResponseFzzError(Request<ResumeBean> request,
                                           ErrorMsg errorMsg) {
                // ToastUtil.showLong(RecommendationActivity.this, "请求失败");
            }

        });

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

        pram.put("fried_id", Friendid);
        pram.put("rew_id", Poseid);
        pram.put("reservation_date", reservation_date);

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

        Action action = new Action(RecommendationActivity.this);
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
                        ToastUtil.showLong(RecommendationActivity.this,
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
                        ToastUtil.showLong(RecommendationActivity.this,
                                bean.getErrorMsg());
                }
                // initDate(bean);
            }

            @Override
            public void onResponseDataError(Request<BaseBean> equest) {
                // ToastUtil.showLong(RecommendationActivity.this, "请求失败");
            }

            @Override
            public void onResponseConnectionError(Request<BaseBean> request,
                                                  int statusCode) {
                // ToastUtil.showLong(RecommendationActivity.this, "请求失败");
            }

            @Override
            public void onResponseFzzError(Request<BaseBean> request,
                                           ErrorMsg errorMsg) {
                // ToastUtil.showLong(RecommendationActivity.this, "请求失败");
            }

        });

    }

    public void timepick() {

        Calendar cal = Calendar.getInstance();
        final DatePickerDialog mDialog = new DatePickerDialog(this, null,
                cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH));
        mDialog.setTitle("请选择预约时间");
        // 取消按钮，如果不需要直接不设置即可
        mDialog.setButton(
                DialogInterface.BUTTON_NEGATIVE,
                CommUtil.getInstance().getStringFromXml(
                        RecommendationActivity.this, R.string.more_cancle),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        // 手动设置按钮
        mDialog.setButton(
                DialogInterface.BUTTON_POSITIVE,
                CommUtil.getInstance().getStringFromXml(
                        RecommendationActivity.this, R.string.more_shure1),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 通过mDialog.getDatePicker()获得dialog上的DatePicker组件，然后可以获取日期信息
                        DatePicker datePicker = mDialog.getDatePicker();
                        int year = datePicker.getYear();
                        int month = datePicker.getMonth();
                        int day = datePicker.getDayOfMonth();

                        if (!timeselect) {
                            // 修改日历控件的年，月，日
                            // 这里的year,monthOfYear,dayOfMonth的值与DatePickerDialog控件设置的最新值一致
                            date2.set(Calendar.YEAR, year);
                            date2.set(Calendar.MONTH, month);
                            date2.set(Calendar.DAY_OF_MONTH, day);
                            // 将页面TextView的显示更新为最新时间

                            updateLabeltime();

                            timeselect = true;
                        } else {
                            return;
                        }

                    }
                });

        mDialog.show();
    }

    public void agepick(final DatePickerDialog mDialog) {

        // 取消按钮，如果不需要直接不设置即可
        mDialog.setButton(
                DialogInterface.BUTTON_NEGATIVE,
                CommUtil.getInstance().getStringFromXml(
                        RecommendationActivity.this, R.string.more_cancle),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        // 手动设置按钮
        mDialog.setButton(
                DialogInterface.BUTTON_POSITIVE,
                CommUtil.getInstance().getStringFromXml(
                        RecommendationActivity.this, R.string.more_shure1),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 通过mDialog.getDatePicker()获得dialog上的DatePicker组件，然后可以获取日期信息
                        DatePicker datePicker = mDialog.getDatePicker();
                        int year = datePicker.getYear();
                        int month = datePicker.getMonth();
                        int day = datePicker.getDayOfMonth();

                        if (!ageselect) {

                            // 修改日历控件的年，月，日
                            // 这里的year,monthOfYear,dayOfMonth的值与DatePickerDialog控件设置的最新值一致
                            dateAndTime.set(Calendar.YEAR, year);
                            dateAndTime.set(Calendar.MONTH, month);
                            dateAndTime.set(Calendar.DAY_OF_MONTH, day);
                            // 将页面TextView的显示更新为最新时间
                            updateLabel();
                            ageselect = true;
                        } else {

                            return;
                        }

                    }
                });

        mDialog.show();
    }

    public void showWorkDialog(Context context) {
        final String[] status_datas = new String[]{
                getResources().getString(R.string.recom_year1),
                getResources().getString(R.string.recom_year2),
                getResources().getString(R.string.recom_year3),
                getResources().getString(R.string.recom_year4)};
        final WorkYearDialog dialog = new com.dh.perfectoffer.view.WorkYearDialog(context,
                status_datas);
        dialog.showWorkDialog("工作经验",
                new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        // TODO Auto-generated method stub
                        age_limit.setText(status_datas[position]);
                        year = status_datas[position];
                        dialog.dismiss();
                    }
                });
    }

    // private void initWorkExperiencePop() {
    // final String status_datas[] = new String[] {
    // getResources().getString(R.string.recom_year1),
    // getResources().getString(R.string.recom_year2),
    // getResources().getString(R.string.recom_year3),
    // getResources().getString(R.string.recom_year4) };
    // View view = LayoutInflater.from(this).inflate(
    // R.layout.work_experience_pop_list, null);
    // ListView lv_work_pop = (ListView) view.findViewById(R.id.lv_work_pop);
    // workStatusPop = new PopupWindow(view, getWindowManager()
    // .getDefaultDisplay().getWidth() * 1 / 2,
    // LayoutParams.WRAP_CONTENT);
    //
    // view.setOnTouchListener(new OnTouchListener() {
    //
    // @Override
    // public boolean onTouch(View v, MotionEvent event) {
    // if (workStatusPop != null && workStatusPop.isShowing()) {
    // workStatusPop.dismiss();
    // }
    // return false;
    // }
    // });
    // List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
    // for (int i = 0; i < status_datas.length; i++) {
    //
    // Map<String, Object> map = new HashMap<String, Object>();
    // map.put("name", status_datas[i]);
    // data.add(map);
    // }
    // SimpleAdapter simpleAdapter = new SimpleAdapter(
    // getApplicationContext(), data, R.layout.item_work_pop,
    // new String[] { "name" }, new int[] { R.id.tv_work_pop });
    //
    // lv_work_pop.setAdapter(simpleAdapter);
    // workStatusPop.setFocusable(true);
    // workStatusPop.setOutsideTouchable(true);
    // lv_work_pop.setOnItemClickListener(new OnItemClickListener() {
    //
    // @Override
    // public void onItemClick(AdapterView<?> parent, View view,
    // int position, long id) {
    // // TODO Auto-generated method stub
    // age_limit.setText(status_datas[position]);
    // year = status_datas[position];
    // workStatusPop.dismiss();
    // }
    // });
    // }

}
