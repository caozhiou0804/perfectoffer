package com.dh.perfectoffer.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.dh.perfectoffer.R;
import com.dh.perfectoffer.demo.EventBusActivity;
import com.dh.perfectoffer.demo.VehicleFragment;
import com.dh.perfectoffer.event.ApptestEvent;
import com.dh.perfectoffer.event.EventBus;
import com.dh.perfectoffer.xutils.view.ViewUtils;
import com.dh.perfectoffer.xutils.view.annotation.ViewInject;
import com.dh.perfectoffer.xutils.view.annotation.event.OnClick;


/**
 * @author 超级小志
 *
 */
public class EventBusFragment extends VehicleFragment {

	// private HttpHandler handler;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.event_fragment, container, false);
		ViewUtils.inject(this, view);
		// 注册订阅事件
		if (null != EventBus.getDefault()) {
			EventBus.getDefault().unregister(this);
			
		}
		EventBus.getDefault().register(this);
		return view;
	}

	@ViewInject(R.id.download_addr_edit)
	private EditText downloadAddrEdit;

	@ViewInject(R.id.download_btn)
	private Button downloadBtn;

	@OnClick(R.id.download_btn)
	public void onclickdownload(View view) {
		Intent intent = new Intent(this.getActivity(), EventBusActivity.class);
		this.getActivity().startActivity(intent);
	}

	public void onEventMainThread(ApptestEvent event) {
		System.out.println("onEventMainThread:"
				+ Thread.currentThread().getId());
		if (event.getType() == ApptestEvent.TEST) {
			Log.e("yinzl", "apptestevent 接受");
			if (!TextUtils.isEmpty(event.getStr()))
				downloadAddrEdit.setText(event.getStr());
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		// 注销订阅事件
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}
	public void refreshData(boolean showDialog) {
	}
}
