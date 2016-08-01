package com.dh.perfectoffer.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.dh.perfectoffer.R;

/**
 * 升级对话框
 * 
 * @author 超级小志
 * 
 */
public class UpdateTipDialog extends Dialog implements
	View.OnClickListener {

    private Button btn_ok;// 确定
    private Button btn_cancle;// 取消

    private TextView tv_title;// 标题
    private TextView tv_content;// 文字描述

    public UpdateTipDialog(Context context) {
	super(context, R.style.dialog_tip);
    }

    public UpdateTipDialog(Context context, int theme) {
	super(context, R.style.dialog_tip);
    }

    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	requestWindowFeature(Window.FEATURE_NO_TITLE);
	setContentView(R.layout.update_dialog_tip);

	btn_ok = (Button) findViewById(R.id.btn_ok);
	// btn_ok.setOnClickListener(this);
	btn_cancle = (Button) findViewById(R.id.btn_cancle);
	tv_title = (TextView) findViewById(R.id.tv_title);
	tv_content = (TextView) findViewById(R.id.tv_content);
	setCancelable(false);
	setCanceledOnTouchOutside(false);

    }

    public void showUpdateTip(String title, String msg,
	    View.OnClickListener onClickListener) {// 标题，内容，确定按钮点击事件
	show();
	tv_title.setText(title);
	tv_content.setText(msg);
	btn_ok.setOnClickListener((View.OnClickListener) onClickListener);
	btn_cancle.setOnClickListener(this);
    }

    public void showUpdateTip(String title, String msg, String str_cancle,
	    String str_ok, View.OnClickListener onClickListener) {// 标题，内容，两个按钮名字，确定按钮点击事件间
	show();
	tv_title.setText(title);
	tv_content.setText(msg);
	btn_ok.setText(str_ok);
	btn_cancle.setText(str_cancle);
	btn_ok.setOnClickListener((View.OnClickListener) onClickListener);
	btn_cancle.setOnClickListener(this);
    }

    public void showUpdateTip(String title, String msg, String str_cancle,
	    String str_ok,
	    View.OnClickListener onClickListenerCancle,
	    View.OnClickListener onClickListenerOk) {// 最全的自定义
	show();
	tv_title.setText(title);
	tv_content.setText(msg);
	btn_ok.setText(str_ok);
	btn_cancle.setText(str_cancle);
	btn_cancle.setOnClickListener(onClickListenerCancle);
	btn_ok.setOnClickListener((View.OnClickListener) onClickListenerOk);
    }

    public void showUpdateTip(View.OnClickListener onClickListener) {// 只使用检测更新
	show();
	btn_cancle.setOnClickListener(this);
	btn_ok.setOnClickListener((View.OnClickListener) onClickListener);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
	if (keyCode == KeyEvent.KEYCODE_BACK) {
	    return true;
	}
	/*
	 * if (keyCode == KeyEvent.KEYCODE_HOME) { //not use,is symbol }
	 */
	return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
	// TODO Auto-generated method stub
	dismiss();
    }
}
