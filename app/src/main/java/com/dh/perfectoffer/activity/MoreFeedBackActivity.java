package com.dh.perfectoffer.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dh.perfectoffer.R;
import com.dh.perfectoffer.VehicleApp;
import com.dh.perfectoffer.constant.Constant;
import com.dh.perfectoffer.demo.VehicleActivity;
import com.dh.perfectoffer.dhutils.CommUtil;
import com.dh.perfectoffer.entity.BaseBean;
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
import com.dh.perfectoffer.event.framework.net.fgview.Response.ErrorMsg;

import java.util.HashMap;

public class MoreFeedBackActivity extends VehicleActivity {

    @ViewInject(R.id.input)
    private EditText editext;
    // @ViewInject(R.id.submit)
    // private Button submit;
    @ViewInject(R.id.tv_length)
    private TextView tv_length;

    @ViewInject(R.id.btn_top_right)
    private Button btn_top_right;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.more_feedback);
        ViewUtils.inject(this);
        super.initTop();
        setTitle(CommUtil.getInstance().getStringFromXml(
                MoreFeedBackActivity.this, R.string.feedback));
        btn_top_right.setVisibility(View.VISIBLE);
        btn_top_right.setText("提交");
        btn_top_right.setTextSize(14);
        btn_top_right.setTextColor(getResources().getColor(R.color.white));
        btn_top_right.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if (null == editext.getText().toString()
                        || editext.getText().toString().trim().isEmpty()) {
                    ToastUtil.showLong(
                            getApplicationContext(),
                            CommUtil.getInstance()
                                    .getStringFromXml(
                                            MoreFeedBackActivity.this,
                                            R.string.back_t1));
                } else {

                    queryfeedback();
                }

            }

        });
        editext.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub
                tv_length.setText(s.length() + "/" + "140");
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });
    }

    // @OnClick(R.id.submit)
    // public void onclickbtonclick(View view) {
    // if (null == editext.getText().toString()
    // || editext.getText().toString().trim().isEmpty()) {
    // ToastUtil.showLong(
    // getApplicationContext(),
    // CommUtil.getInstance().getStringFromXml(
    // MoreFeedBackActivity.this, R.string.back_t1));
    // } else {
    //
    // queryfeedback();
    // }
    // }

    private void queryfeedback() {

        HashMap<String, String> pram = new HashMap<String, String>();

        if (null == VehicleApp.getInstance().getUserBean()
                || TextUtils.isEmpty(VehicleApp.getInstance().getUserBean()
                .getUsertosk())) {
            launch(LoginActivity.class);
        }
        pram.put("username", VehicleApp.getInstance().getUserBean()
                .getUsertosk());

        // pram.put("feedback", editext.getText().toString());
        pram.put("feedback",
                CommUtil.getInstance()
                        .getNomalStr(editext.getText().toString()));
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
        req.setUrl(Constant.URLIP + "hr/FeedbackServlet");

        Action action = new Action(MoreFeedBackActivity.this);
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
                        ToastUtil.showLong(
                                getApplicationContext(),
                                CommUtil.getInstance().getStringFromXml(
                                        MoreFeedBackActivity.this,
                                        R.string.back_t2));
                        finish();
                    } else
                        ToastUtil.showLong(MoreFeedBackActivity.this,
                                bean.getErrorMsg());
                }
                // initDate(bean);
            }

            @Override
            public void onResponseDataError(Request<BaseBean> equest) {
                // ToastUtil.showLong(MoreFeedBackActivity.this, "请求失败");
            }

            @Override
            public void onResponseConnectionError(Request<BaseBean> request,
                                                  int statusCode) {
                // ToastUtil.showLong(MoreFeedBackActivity.this, "请求失败");
            }

            @Override
            public void onResponseFzzError(Request<BaseBean> request,
                                           ErrorMsg errorMsg) {
                // ToastUtil.showLong(MoreFeedBackActivity.this, "请求失败");
            }

        });

    }
}
