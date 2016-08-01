package com.dh.perfectoffer.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.dh.perfectoffer.R;

public class WorkYearDialog extends Dialog implements
	View.OnClickListener {

    private TextView tv_title;// 标题
    private ListView lv_work_dialog;

    private Context context;

    private String[] status_datas;

    public WorkYearDialog(Context context, String[] status_datas) {
	super(context, R.style.dialog_tip);
	this.context = context;
	this.status_datas = status_datas;
	// TODO Auto-generated constructor stub
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	requestWindowFeature(Window.FEATURE_NO_TITLE);
	setContentView(R.layout.work_year_dialog);
	tv_title = (TextView) findViewById(R.id.tv_title);
	lv_work_dialog = (ListView) findViewById(R.id.lv_work_dialog);
	setCancelable(false);
	setCanceledOnTouchOutside(true);
	initData();
    }

    private void initData() {
	List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
	for (int i = 0; i < status_datas.length; i++) {

	    Map<String, Object> map = new HashMap<String, Object>();
	    map.put("name", status_datas[i]);
	    data.add(map);
	}
	SimpleAdapter simpleAdapter = new SimpleAdapter(
		context.getApplicationContext(), data, R.layout.item_work_pop,
		new String[] { "name" }, new int[] { R.id.tv_work_pop });

	lv_work_dialog.setAdapter(simpleAdapter);
    }

    public void showWorkDialog(String titile,
	    android.widget.AdapterView.OnItemClickListener onItemClickListener) {
	show();
	tv_title.setText(titile);
	lv_work_dialog.setOnItemClickListener(onItemClickListener);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
	if (keyCode == KeyEvent.KEYCODE_BACK) {
	    dismiss();
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
