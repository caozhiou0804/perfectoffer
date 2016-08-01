package com.dh.perfectoffer.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.dh.perfectoffer.R;
import com.dh.perfectoffer.VehicleApp;
import com.dh.perfectoffer.constant.Constant;
import com.dh.perfectoffer.demo.VehicleActivity;
import com.dh.perfectoffer.entity.BaseBean;
import com.dh.perfectoffer.entity.UserBean;
import com.dh.perfectoffer.entity.UserListBean;
import com.dh.perfectoffer.event.ComEvent;
import com.dh.perfectoffer.event.EventBus;
import com.dh.perfectoffer.event.framework.alibaba.fastjson.JSON;
import com.dh.perfectoffer.event.framework.net.exception.DataException;
import com.dh.perfectoffer.event.framework.net.fgview.Action;
import com.dh.perfectoffer.event.framework.net.fgview.BaseParser;
import com.dh.perfectoffer.event.framework.net.fgview.OnResponseListener;
import com.dh.perfectoffer.event.framework.net.fgview.Request;
import com.dh.perfectoffer.event.framework.net.fgview.Response;
import com.dh.perfectoffer.event.framework.util.CommonUtils;
import com.dh.perfectoffer.xutils.sample.utils.Preference;
import com.dh.perfectoffer.xutils.sample.utils.ToastUtil;
import com.dh.perfectoffer.xutils.view.ViewUtils;
import com.dh.perfectoffer.xutils.view.annotation.ViewInject;
import com.dh.perfectoffer.event.framework.net.fgview.Response.ErrorMsg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MorePasswordActivity extends VehicleActivity {
    @ViewInject(R.id.old_password)
    private EditText text_old;
    @ViewInject(R.id.new_password)
    private EditText text_new;
    @ViewInject(R.id.confirm_password)
    private EditText text_confirm;
    // @ViewInject(R.id.confirm)
    // private Button button;

    @ViewInject(R.id.btn_top_right)
    private Button btn_top_right;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.more_password);
        ViewUtils.inject(this);
        super.initTop();
        setTitle("更换密码");
        // setTitle(getResources().getString(R.string.pass_up));
        btn_top_right.setVisibility(View.VISIBLE);
        btn_top_right.setText("提交");
        btn_top_right.setTextSize(14);
        btn_top_right.setTextColor(getResources().getColor(R.color.white));
        btn_top_right.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (null == text_old.getText().toString()
                        || text_old.getText().toString().isEmpty()) {
                    ToastUtil.showLong(getApplicationContext(), getResources()
                            .getString(R.string.pass_null1));
                } else if (text_new.getText().toString().isEmpty()
                        || null == text_new.getText().toString()) {

                    ToastUtil.showLong(getApplicationContext(), getResources()
                            .getString(R.string.pass_null2));

                } else if (text_new.getText().toString().length() < 6) {
                    ToastUtil.showLong(getApplicationContext(), getResources()
                            .getString(R.string.pass_rule));
                } else if (text_old.getText().toString()
                        .equals(text_new.getText().toString())) {
                    ToastUtil.showLong(getApplicationContext(), getResources()
                            .getString(R.string.pass_name2));
                } else if (!text_new.getText().toString()
                        .equals(text_confirm.getText().toString())) {
                    ToastUtil.showLong(getApplicationContext(), getResources()
                            .getString(R.string.pass_confrim3));
                } else {

                    querypassword();
                    CommonUtils.hideSoftInput(v);
                }

            }

        });
    }

    // @OnClick(R.id.confirm)
    // public void onclickbtonclick(View view) {
    // if (null == text_old.getText().toString()
    // || text_old.getText().toString().isEmpty()) {
    // ToastUtil.showLong(getApplicationContext(), getResources()
    // .getString(R.string.pass_null1));
    // }
    //
    // else if (text_new.getText().toString().isEmpty()
    // || null == text_new.getText().toString()) {
    //
    // ToastUtil.showLong(getApplicationContext(), getResources()
    // .getString(R.string.pass_null2));
    //
    // } else if (text_new.getText().toString().length() < 6) {
    // ToastUtil.showLong(getApplicationContext(), getResources()
    // .getString(R.string.pass_rule));
    // } else if (text_old.getText().toString()
    // .equals(text_new.getText().toString())) {
    // ToastUtil.showLong(getApplicationContext(), getResources()
    // .getString(R.string.pass_name2));
    // }
    //
    // else if (!text_new.getText().toString()
    // .equals(text_confirm.getText().toString())) {
    // ToastUtil.showLong(getApplicationContext(), getResources()
    // .getString(R.string.pass_confrim3));
    // }
    //
    // else {
    //
    // querypassword();
    // CommonUtils.hideSoftInput(view);
    // }
    //
    // }

    private void querypassword() {

        HashMap<String, String> pram = new HashMap<String, String>();

        if (null == VehicleApp.getInstance().getUserBean()
                || TextUtils.isEmpty(VehicleApp.getInstance().getUserBean()
                .getUsertosk())) {
            launch(LoginActivity.class);
        }
        pram.put("username", VehicleApp.getInstance().getUserBean()
                .getUsertosk());

        pram.put("newpassword", text_new.getText().toString());
        pram.put("oldpassword", text_old.getText().toString());
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
        req.setUrl(Constant.URLIP + "hr/ModifyPassWordServlet");

        Action action = new Action(MorePasswordActivity.this);
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
                        ToastUtil.showLong(MorePasswordActivity.this,
                                getResources().getString(R.string.pass_su));
                        Preference.putString(Constant.PRE_USER_PASSWORD,
                                text_new.getText().toString());
                        querylogin();
                        // finish();
                    } else
                        ToastUtil.showLong(MorePasswordActivity.this,
                                bean.getErrorMsg());
                }
            }

            @Override
            public void onResponseDataError(Request<BaseBean> equest) {
                // ToastUtil.showLong(MorePasswordActivity.this, "请求失败");
            }

            @Override
            public void onResponseConnectionError(Request<BaseBean> request,
                                                  int statusCode) {
                // ToastUtil.showLong(MorePasswordActivity.this, "请求失败");
            }

            @Override
            public void onResponseFzzError(Request<BaseBean> request,
                                           ErrorMsg errorMsg) {
                // ToastUtil.showLong(MorePasswordActivity.this, "请求失败");
            }

        });

    }

    private void querylogin() {
        HashMap<String, String> pram = new HashMap<String, String>();
        pram.put("username", Preference.getString(Constant.PRE_USER_NAME));
        pram.put("userpass", text_new.getText().toString());

        // String bodyRequestParam = JSON.toJSONString(bean);

        // 请求parser
        Request<UserListBean> req = new Request<UserListBean>();
        req.setRequestMethod(Request.M_POST);
        req.setBaseParser(new BaseParser<UserListBean>() {
            @Override
            public UserListBean parseResDate(String resBody)
                    throws DataException {
                if (resBody != null && !resBody.equals("")) {
                    // Preference.putString(Constant.PRE_USER_NAME, resBody);
                    return JSON.parseObject(resBody, UserListBean.class);

                }
                return null;
            }
        });
        // req.setBodyRequestParam(bodyRequestParam);
        req.setRequestParams(pram);
        req.setUrl(Constant.URLIP + "hr/LoginServlet");

        Action action = new Action(MorePasswordActivity.this);
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
                        afeiDb.dropTableIfTableExist(UserBean.class);
                        afeiDb.save(bean.getUserList());
                        VehicleApp.getInstance().updateUserBean();
                        List<UserBean> useList = new ArrayList<UserBean>();
                        useList = afeiDb.findAll(UserBean.class);
                        if (useList.size() > 0) {
                            for (int i = 0; i < useList.size(); i++) {
                                Log.e("yinzl", useList.get(i).getBounty()
                                        .toString());
                            }
                        }
                        new Thread(new Runnable() {

                            @Override
                            public void run() {
                                // 推送一个事件
                                ComEvent appevent = new ComEvent(
                                        ComEvent.HISTROY);
                                EventBus.getDefault().post(appevent);

                            }
                        }).start();
                        new Thread(new Runnable() {

                            @Override
                            public void run() {
                                // 推送一个事件
                                ComEvent appevent = new ComEvent(
                                        ComEvent.RESUME);
                                EventBus.getDefault().post(appevent);
                                ComEvent applogin = new ComEvent(
                                        ComEvent.USERLOGIN);
                                EventBus.getDefault().post(applogin);
                                ComEvent appuser = new ComEvent(
                                        ComEvent.USERINFO);
                                EventBus.getDefault().post(appuser);

                            }
                        }).start();
                        finish();
                    } else
                        ToastUtil.showLong(MorePasswordActivity.this,
                                bean.getErrorMsg());
                }
                // initDate(bean);
            }

            @Override
            public void onResponseDataError(Request<UserListBean> equest) {
                // ToastUtil.showLong(LoginActivity.this, "请求失败");
            }

            @Override
            public void onResponseConnectionError(
                    Request<UserListBean> request, int statusCode) {
                // ToastUtil.showLong(LoginActivity.this, "请求失败");
            }

            @Override
            public void onResponseFzzError(Request<UserListBean> request,
                                           ErrorMsg errorMsg) {
                // ToastUtil.showLong(LoginActivity.this, "请求失败");
            }

        });

    }

}
