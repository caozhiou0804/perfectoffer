package com.dh.perfectoffer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.dh.perfectoffer.R;
import com.dh.perfectoffer.VehicleApp;
import com.dh.perfectoffer.constant.Constant;
import com.dh.perfectoffer.demo.VehicleActivity;
import com.dh.perfectoffer.dhutils.ProgeressUtils;
import com.dh.perfectoffer.entity.UserBean;
import com.dh.perfectoffer.entity.UserListBean;
import com.dh.perfectoffer.event.ComEvent;
import com.dh.perfectoffer.event.EventBus;
import com.dh.perfectoffer.event.framework.alibaba.fastjson.JSON;
import com.dh.perfectoffer.event.framework.db.AfeiDb;
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
import com.dh.perfectoffer.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LoginActivity extends VehicleActivity {
    @ViewInject(R.id.login_name)
    EditText name;
    @ViewInject(R.id.login_password)
    EditText password;
    @ViewInject(R.id.login_bt)
    Button loginbt;
    private ArrayList<UserListBean> bean;
    public AfeiDb afeiDb;
    @ViewInject(R.id.btn_title_btn_back_layout)
    private RelativeLayout btn_title_btn_back_layout;

    private UserBean user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ViewUtils.inject(this);
        super.initTop();
        afeiDb = VehicleApp.getInstance().getAfeiDb();
        if (null == afeiDb) {
            afeiDb = AfeiDb.create(LoginActivity.this, Constant.DB_NAME, true);
        }
        user = VehicleApp.getInstance().getUserBean();
    }

    @OnClick(R.id.login_register)
    public void onclickloginregister(View view) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_title_btn_back_layout)
    public void onclickback(View view) {
        LoginActivity.this.finish();
    }

    @OnClick(R.id.login_bt)
    public void onclickloginbt(View view) {
        boolean isCanLogin = true;
        if (name.getText().toString().trim().isEmpty()) {
            name.setError(getResources().getString(R.string.login_n));
            isCanLogin = false;
        } else {
            name.setError(null);
        }

        if (password.getText().toString().isEmpty()) {
            password.setError(getResources().getString(R.string.login_p));
            isCanLogin = false;
        } else {
            password.setError(null);
        }
        if (isCanLogin)
            querylogin();

    }

    private void querylogin() {

        HashMap<String, String> pram = new HashMap<String, String>();
        pram.put("username", name.getText().toString().trim());
        pram.put("userpass", password.getText().toString());
        // String bodyRequestParam = JSON.toJSONString(bean);
        ProgeressUtils.showLoadingDialogMy("正在登录，请稍候...", LoginActivity.this);
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

        Action action = new Action(LoginActivity.this);
        action.setDefaultLoadingTipOpen(false);
        action.setShowErrorDialog(true);
        action.execute(req, new OnResponseListener<UserListBean>() {
            @Override
            public void onResponseFinished(Request<UserListBean> request,
                                           Response<UserListBean> response) {
                ProgeressUtils.closeLoadingDialogMy();
                UserListBean bean = new UserListBean();
                bean = response.getT();
                if (null != bean) {
                    if (bean.getSuccess().equals("1")) {
                        ToastUtil.showLong(LoginActivity.this, getResources()
                                .getString(R.string.login_s));

                        // Intent intent=new Intent(LoginActivity.this,
                        // FragmentTabActivity2.class);
                        // startActivity(intent);
                        // finish();
                        // initDate();
                        Preference.putString(Constant.PRE_USER_NAME, name
                                .getText().toString().trim());
                        Preference.putString(Constant.PRE_USER_PASSWORD,
                                password.getText().toString());
                        afeiDb.dropTableIfTableExist(UserBean.class);
                        afeiDb.save(bean.getUserList());
                        VehicleApp.getInstance().updateUserBean();
                        List<UserBean> useList = new ArrayList<UserBean>();
                        useList = afeiDb.findAll(UserBean.class);

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

                            }
                        }).start();
                        finish();
                    } else
                        ToastUtil.showLong(LoginActivity.this,
                                bean.getErrorMsg());
                }
                // initDate(bean);
            }

            @Override
            public void onResponseDataError(Request<UserListBean> equest) {
                // ToastUtil.showLong(LoginActivity.this, "请求失败");
                ProgeressUtils.closeLoadingDialogMy();
            }

            @Override
            public void onResponseConnectionError(
                    Request<UserListBean> request, int statusCode) {
                // ToastUtil.showLong(LoginActivity.this, "请求失败");
                ProgeressUtils.closeLoadingDialogMy();
            }

            @Override
            public void onResponseFzzError(Request<UserListBean> request,
                                           ErrorMsg errorMsg) {
                // ToastUtil.showLong(LoginActivity.this, "请求失败");
                ProgeressUtils.closeLoadingDialogMy();
            }

        });

    }

    // private void initDate() {
    // afeiDb.dropTableIfTableExist(UserListBean.class);
    // for (int i = 0; i < bean.size(); i++) {
    // afeiDb.save(bean.get(i));
    // }
    //
    // List<UserListBean> useList = new ArrayList<UserListBean>();
    // useList = afeiDb.findAll(UserListBean.class);
    // if (useList.size() > 0) {
    // for (int i = 0; i < useList.size(); i++) {
    //
    // Log.e("aaaaaaaaaaaaaaa", useList.get(i).getUserList().getRname());
    // }
    // }
    //
    // }

}
