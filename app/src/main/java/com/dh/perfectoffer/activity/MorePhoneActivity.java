package com.dh.perfectoffer.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.dh.perfectoffer.R;
import com.dh.perfectoffer.VehicleApp;
import com.dh.perfectoffer.constant.Constant;
import com.dh.perfectoffer.demo.VehicleActivity;
import com.dh.perfectoffer.dhutils.CommUtil;
import com.dh.perfectoffer.entity.BaseBean;
import com.dh.perfectoffer.entity.UserBean;
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

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MorePhoneActivity extends VehicleActivity {

    @ViewInject(R.id.phon_new)
    private EditText phone;
    // @ViewInject(R.id.phone_bt)
    // private Button phone_bt;
    @ViewInject(R.id.btn_top_right)
    private Button btn_top_right;

    private String value = "";
    private UserBean user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.more_phone);
        ViewUtils.inject(this);
        super.initTop();
        setTitle("更换手机号");
        btn_top_right.setVisibility(View.VISIBLE);
        btn_top_right.setText("提交");
        btn_top_right.setTextSize(14);
        btn_top_right.setTextColor(getResources().getColor(R.color.white));
        btn_top_right.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                value = phone.getText().toString();

                String regExp = "^(0|86|17951)?(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$";

                Pattern p = Pattern.compile(regExp);

                Matcher m = p.matcher(value);
                if (CommUtil.getInstance().isPhoneNumber(value) == true) {
                    // ToastUtil.showLong(MorePhoneActivity.this, "修改手机号成功");
                    // Intent intent=new Intent(MorePhoneActivity.this,
                    // MoreFragment.class);
                    // startActivity(intent);
                    if (value.equals(user.getUser_phone().toString())) {

                        ToastUtil
                                .showLong(
                                        MorePhoneActivity.this,
                                        CommUtil.getInstance()
                                                .getStringFromXml(
                                                        MorePhoneActivity.this,
                                                        R.string.ph_re));
                    } else {
                        queryphone();
                    }

                } else {
                    ToastUtil.showLong(
                            MorePhoneActivity.this,
                            CommUtil.getInstance().getStringFromXml(
                                    MorePhoneActivity.this, R.string.ph_con1));
                }
            }
        });
        // setTitle(CommUtil.getInstance().getStringFromXml(MorePhoneActivity.this,
        // R.string.ph_con));
        user = VehicleApp.getInstance().getUserBean();
    }

    // @OnClick(R.id.btn_top_right)
    // public void onclickphonebt(View view) {
    //
    //
    // }

    private void queryphone() {

        HashMap<String, String> pram = new HashMap<String, String>();

        if (null == VehicleApp.getInstance().getUserBean()
                || TextUtils.isEmpty(VehicleApp.getInstance().getUserBean()
                .getUsertosk())) {
            launch(LoginActivity.class);
        }
        pram.put("username", VehicleApp.getInstance().getUserBean()
                .getUsertosk());

        pram.put("newphone", value);
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
        req.setUrl(Constant.URLIP + "hr/ModifyPhoneServlet");

        Action action = new Action(MorePhoneActivity.this);
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
                        ToastUtil
                                .showLong(
                                        MorePhoneActivity.this,
                                        CommUtil.getInstance()
                                                .getStringFromXml(
                                                        MorePhoneActivity.this,
                                                        R.string.ph_su));
                        user.setUser_phone(value);
                        afeiDb.dropTableIfTableExist(UserBean.class);
                        afeiDb.save(user);
                        VehicleApp.getInstance().updateUserBean();
                        finish();
                    } else
                        ToastUtil.showLong(MorePhoneActivity.this,
                                bean.getErrorMsg());
                }
                // initDate(bean);
            }

            @Override
            public void onResponseDataError(Request<BaseBean> equest) {
                // ToastUtil.showLong(MorePhoneActivity.this, "请求失败");
            }

            @Override
            public void onResponseConnectionError(Request<BaseBean> request,
                                                  int statusCode) {
                // ToastUtil.showLong(MorePhoneActivity.this, "请求失败");
            }

            @Override
            public void onResponseFzzError(Request<BaseBean> request,
                                           ErrorMsg errorMsg) {
                // ToastUtil.showLong(MorePhoneActivity.this, "请求失败");
            }

        });

    }

}
