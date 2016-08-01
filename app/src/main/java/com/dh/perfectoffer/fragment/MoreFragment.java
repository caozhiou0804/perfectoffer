package com.dh.perfectoffer.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dh.perfectoffer.R;
import com.dh.perfectoffer.VehicleApp;
import com.dh.perfectoffer.activity.LoginActivity;
import com.dh.perfectoffer.activity.MoreFeedBackActivity;
import com.dh.perfectoffer.activity.MorePasswordActivity;
import com.dh.perfectoffer.activity.MorePhoneActivity;
import com.dh.perfectoffer.activity.MoreRelateActivity;
import com.dh.perfectoffer.constant.Constant;
import com.dh.perfectoffer.demo.FragmentTabActivity2;
import com.dh.perfectoffer.demo.VehicleFragment;
import com.dh.perfectoffer.dhutils.CommUtil;
import com.dh.perfectoffer.entity.UserBean;
import com.dh.perfectoffer.event.ComEvent;
import com.dh.perfectoffer.event.EventBus;
import com.dh.perfectoffer.event.framework.db.AfeiDb;
import com.dh.perfectoffer.view.UpdateTipDialog;
import com.dh.perfectoffer.xutils.sample.utils.Preference;
import com.dh.perfectoffer.xutils.view.ViewUtils;
import com.dh.perfectoffer.xutils.view.annotation.ViewInject;
import com.dh.perfectoffer.xutils.view.annotation.event.OnClick;

public class MoreFragment extends VehicleFragment {
    // @ViewInject(R.id.more_help)
    // private RelativeLayout more_help;
    @ViewInject(R.id.more_relate)
    private RelativeLayout more_relate;
    @ViewInject(R.id.more_password)
    private RelativeLayout more_password;
    @ViewInject(R.id.more_phone)
    private RelativeLayout more_phone;
    @ViewInject(R.id.more_update)
    private RelativeLayout more_update;
    @ViewInject(R.id.more_feedback)
    private RelativeLayout more_feedback;
    @ViewInject(R.id.more_exit)
    private RelativeLayout more_exit;
    @ViewInject(R.id.exit)
    private TextView exit;
    private AfeiDb afeiDb;
    private Context mAppContext;

    @Override
    public View onCreateView(LayoutInflater inflater,
	    @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	View view = inflater.inflate(R.layout.more_fragment, null);

	ViewUtils.inject(this, view);
	mAppContext = inflater.getContext().getApplicationContext();
	if (null != EventBus.getDefault()) {
	    EventBus.getDefault().unregister(this);

	}
	EventBus.getDefault().register(this);
	afeiDb = VehicleApp.getInstance().getAfeiDb();
	if (null == afeiDb) {
	    afeiDb = AfeiDb.create(getActivity(), Constant.DB_NAME, true);
	}
	if (null == VehicleApp.getInstance().getUserBean()
		|| TextUtils.isEmpty(VehicleApp.getInstance().getUserBean()
			.getUsertosk())) {

	    exit.setText(CommUtil.getInstance().getStringFromXml(getActivity(),
		    R.string.more_login));
	} else {
	    exit.setText(CommUtil.getInstance().getStringFromXml(getActivity(),
		    R.string.more_exit));
	}
	return view;
    }

    public void onEventMainThread(ComEvent event) {
	System.out.println("onEventMainThread:"
		+ Thread.currentThread().getId());
	if (event.getType() == ComEvent.USERLOGIN) {
	    if (null == VehicleApp.getInstance().getUserBean()
		    || TextUtils.isEmpty(VehicleApp.getInstance().getUserBean()
			    .getUsertosk())) {

		exit.setText(CommUtil.getInstance().getStringFromXml(
			getActivity(), R.string.more_login));
	    } else {
		exit.setText(CommUtil.getInstance().getStringFromXml(
			getActivity(), R.string.more_exit));
	    }
	}
    }

    @OnClick(R.id.more_password)
    public void onclickpassword(View view) {
	if (null == VehicleApp.getInstance().getUserBean()
		|| TextUtils.isEmpty(VehicleApp.getInstance().getUserBean()
			.getUsertosk())) {
	    launch(LoginActivity.class);
	    return;
	}
	Intent intent = new Intent(getActivity(), MorePasswordActivity.class);
	this.getActivity().startActivity(intent);
    }

    // @OnClick(R.id.more_help)
    // public void onclikhelp(View view) {
    // Intent intent=new Intent(getActivity(),
    // RecommendationActivity.class);
    // this.getActivity().startActivity(intent);
    // ToastUtil.showLong(getActivity(), CommUtil.getInstance()
    // .getStringFromXml(getActivity(), R.string.more_toast));
    // }

    @OnClick(R.id.more_relate)
    public void onclickrelate(View view) {

	Intent intent = new Intent(getActivity(), MoreRelateActivity.class);
	this.getActivity().startActivity(intent);
    }

    @OnClick(R.id.more_phone)
    public void onclickphone(View view) {
	if (null == VehicleApp.getInstance().getUserBean()
		|| TextUtils.isEmpty(VehicleApp.getInstance().getUserBean()
			.getUsertosk())) {
	    launch(LoginActivity.class);
	    return;
	}
	Intent intent = new Intent(getActivity(), MorePhoneActivity.class);
	this.getActivity().startActivity(intent);
    }

    @OnClick(R.id.more_feedback)
    public void onclickfeed(View view) {
	if (null == VehicleApp.getInstance().getUserBean()
		|| TextUtils.isEmpty(VehicleApp.getInstance().getUserBean()
			.getUsertosk())) {
	    launch(LoginActivity.class);
	    return;
	}
	Intent intent = new Intent(getActivity(), MoreFeedBackActivity.class);
	this.getActivity().startActivity(intent);
    }

    @OnClick(R.id.more_update)
    public void onclickupdate(View view) {
	// Intent intent=new Intent(getActivity(), RegisterActivity.class);
	// this.getActivity().startActivity(intent);
	// ToastUtil.showLong(getActivity(), "别点了,程序员放假了");
	FragmentTabActivity2.getInstance().checkVersion(
		CommUtil.getVersionCode(getActivity()) + "", true);
    }

    @OnClick(R.id.more_exit)
    public void onclickexit(View view) {
	// afeiDb.delete(Constant.DB_NAME);

	// ClearCacheRequest(mAppContext);
	// mAppContext.

	if (null == VehicleApp.getInstance().getUserBean()
		|| TextUtils.isEmpty(VehicleApp.getInstance().getUserBean()
			.getUsertosk())) {
	    Intent intent = new Intent(getActivity(), LoginActivity.class);

	    this.getActivity().startActivity(intent);
	} else {

	    final UpdateTipDialog dialog = new UpdateTipDialog(getActivity());
	    dialog.showUpdateTip(
		    "温馨提示",
		    CommUtil.getInstance().getStringFromXml(getActivity(),
			    R.string.more_shure),
		    CommUtil.getInstance().getStringFromXml(getActivity(),
			    R.string.more_cancle),
		    CommUtil.getInstance().getStringFromXml(getActivity(),
			    R.string.more_shure1),
		    new View.OnClickListener() {

			@Override
			public void onClick(View v) {
			    // TODO Auto-generated method stub
			    exit();
			    exit.setText(CommUtil.getInstance()
				    .getStringFromXml(getActivity(),
					    R.string.more_login));
			    dialog.dismiss();

			}
		    });

	    // new AlertDialog.Builder(getActivity())
	    // .setTitle(
	    // CommUtil.getInstance().getStringFromXml(
	    // getActivity(), R.string.more_shure))
	    // .setPositiveButton(
	    // CommUtil.getInstance().getStringFromXml(
	    // getActivity(), R.string.more_shure1),
	    // new DialogInterface.OnClickListener() {
	    // public void onClick(DialogInterface dialog,
	    // int which) {
	    // exit();
	    // exit.setText(CommUtil.getInstance()
	    // .getStringFromXml(getActivity(),
	    // R.string.more_login));
	    // }
	    // })
	    // .setNegativeButton(
	    // CommUtil.getInstance().getStringFromXml(
	    // getActivity(), R.string.more_cancle),
	    // new DialogInterface.OnClickListener() {
	    // public void onClick(DialogInterface dialog,
	    // int which) {
	    // // TODO Auto-generated method stub
	    // }
	    // }).show();

	}
    }

    private void exit() {
	afeiDb.dropTableIfTableExist(UserBean.class);
	VehicleApp.getInstance().setUserBean(null);
	Preference.putString(Constant.PRE_USER_PASSWORD, "");
	// Preference.putString(Constant.PRE_HISTROY_NAME, "");
	// Preference.putString(Constant.PRE_RESUME_NAME, "");
	mAppContext = null;
    }
}
