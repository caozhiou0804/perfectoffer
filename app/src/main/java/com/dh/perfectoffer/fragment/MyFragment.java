package com.dh.perfectoffer.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dh.perfectoffer.R;
import com.dh.perfectoffer.VehicleApp;
import com.dh.perfectoffer.activity.IntegralActivity;
import com.dh.perfectoffer.activity.LoginActivity;
import com.dh.perfectoffer.activity.OfficeInfomationActivity;
import com.dh.perfectoffer.activity.RecommendationActivity;
import com.dh.perfectoffer.adapter.ArrayListAdapter;
import com.dh.perfectoffer.adapter.ViewHolder;
import com.dh.perfectoffer.constant.Constant;
import com.dh.perfectoffer.demo.VehicleFragment;
import com.dh.perfectoffer.dhutils.CommUtil;
import com.dh.perfectoffer.dhutils.ImageLoaderUtil;
import com.dh.perfectoffer.dhutils.ImageUtils;
import com.dh.perfectoffer.entity.BaseBean;
import com.dh.perfectoffer.entity.HeadPicBean;
import com.dh.perfectoffer.entity.HistroyListBean;
import com.dh.perfectoffer.entity.HistroyTempBean;
import com.dh.perfectoffer.entity.ResumeTempBean;
import com.dh.perfectoffer.entity.RusumeListBean;
import com.dh.perfectoffer.entity.SignBean;
import com.dh.perfectoffer.entity.TotalIntegralBean;
import com.dh.perfectoffer.entity.UserBean;
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
import com.dh.perfectoffer.event.framework.util.StringUtils;
import com.dh.perfectoffer.handmark.pulltorefresh.library.PullToRefreshBase;
import com.dh.perfectoffer.handmark.pulltorefresh.library.PullToRefreshListView;
import com.dh.perfectoffer.view.CircleImageView;
import com.dh.perfectoffer.xutils.sample.utils.Preference;
import com.dh.perfectoffer.xutils.sample.utils.PubUtils;
import com.dh.perfectoffer.xutils.sample.utils.ToastUtil;
import com.dh.perfectoffer.xutils.view.ViewUtils;
import com.dh.perfectoffer.xutils.view.annotation.ViewInject;
import com.dh.perfectoffer.xutils.view.annotation.event.OnClick;
import com.dh.perfectoffer.event.framework.net.fgview.Response.ErrorMsg;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * @author 超级小志
 *
 *         我的界面
 */
public class MyFragment extends VehicleFragment implements OnClickListener {

    private Context mAppContext;
    @ViewInject(R.id.iv_icon)
    private CircleImageView iv_icon;// 用户头像
    @ViewInject(R.id.tv_username)
    private TextView tv_username;// 用户昵称
    // @ViewInject(R.id.tv_phone)
    // private TextView tv_phone;// 用户手机号
    @ViewInject(R.id.tv_age)
    private TextView tv_age;// 用户年龄
    @ViewInject(R.id.tv_account_detail)
    private TextView tv_account;// 用户积分
    // @ViewInject(R.id.btn_add_resume)
    // private Button btn_add_resume;// 新建简历
    @ViewInject(R.id.add_resume_img)
    private ImageView add_resume_img;
    @ViewInject(R.id.add_resume_rl)
    private RelativeLayout add_resume_rl;
    @ViewInject(R.id.rl_resume)
    private RelativeLayout rl_resume;// 简历管理
    @ViewInject(R.id.rl_histroy)
    private RelativeLayout rl_histroy;// 投递历史
    @ViewInject(R.id.v_resume)
    private View v_resume;// 简历管理选中
    @ViewInject(R.id.v_histroy)
    private View v_histroy;// 投递历史选中
    @ViewInject(R.id.lv_resume)
    private ListView lv_resume;// 简历列表
    @ViewInject(R.id.ll_empty)
    private View ll_empty;// 简历数据空
    @ViewInject(R.id.lv_histroy)
    private PullToRefreshListView lv_histroy;// 投递历史列表
    private String iconPath;
    // 图像保存路径
    private String facePath;
    /* 获取拍照或相册图片 */
    private static final int REQUEST_CODE_PHOTO = 1;
    private static final int REQUEST_CODE_PICK = 2;
    private static final int REQUEST_CODE_EDITPIC = 3;
    private Bitmap m_bitmap;
    private UserBean user;
    private static final int GOTORESUME = 1;// 简历管理切换
    private static final int GOTOHISTROY = 2;// 投递历史切换
    private static final int REFERSHRESUME = 3;// 刷新简历页面
    private static final int REFERSHHISTROY = 4;// 刷新推荐历史
    private ResumeAdapter resumeAdapter;
    private HistroyAdapter histroyAdapter;
    private ArrayList<ResumeTempBean> resumeArrayList;
    private ArrayList<HistroyTempBean> hisArrayList;
    private int mCurrentPage = 1;
    private static final int PAGE_SIZE = 20;
    private byte[] imgBytes;
    private String fileMimeType;

    @ViewInject(R.id.integral_layout)
    private LinearLayout integral_layout;
    // 签到
    @ViewInject(R.id.img_sign_or_no)
    private ImageView img_sign_or_no;

    // 已签到
    private static final int SIGNED = 0x005;
    // 未签到
    private static final int NO_SIGN = 0x006;

    private static final int TOTAL_INTEGRAL_SUCCESS = 0x007;
    private static final int TOTAL_INTEGRAL_FAIL = 0x008;

    @SuppressLint("InflateParams")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {
	View view = inflater.inflate(R.layout.fragment_my, container, false);
	ViewUtils.inject(this, view);
	if (null != EventBus.getDefault()) {
	    EventBus.getDefault().unregister(this);

	}
	EventBus.getDefault().register(this);
	mAppContext = inflater.getContext().getApplicationContext();
	initView();
	user = VehicleApp.getInstance().getUserBean();
	if (null != user) {
	    L.e("yinzl", "用户信息" + user.getRname());
	    initUser(user);
	    myHandler.sendEmptyMessage(GOTORESUME);
	} else {
	    if (null == VehicleApp.getInstance().getUserBean()
		    || TextUtils.isEmpty(VehicleApp.getInstance().getUserBean()
			    .getUsertosk())) {
		launch(LoginActivity.class);
	    }
	}
	return view;
    }

    @OnClick(R.id.add_resume_img)
    public void onclickaddResume(View view) {
	if (null == VehicleApp.getInstance().getUserBean()
		|| TextUtils.isEmpty(VehicleApp.getInstance().getUserBean()
			.getUsertosk())) {
	    launch(LoginActivity.class);
	    return;
	}
	Intent intent = new Intent(getActivity(), RecommendationActivity.class);
	intent.putExtra("isAdd", true);
	launch(intent);
    }

    @OnClick(R.id.iv_icon)
    public void onclickchoicePic(View view) {
	if (null == VehicleApp.getInstance().getUserBean()
		|| TextUtils.isEmpty(VehicleApp.getInstance().getUserBean()
			.getUsertosk())) {
	    launch(LoginActivity.class);
	    return;
	}
	String path = CommUtil.getInstance().getExternCachePath();
	File file = new File(path);
	if (!file.exists())
	    file.mkdirs();
	if (path != null) {
	    iconPath = path + "icon.png";
	} else {
	    ToastUtil.showLong(getActivity(), CommUtil.getInstance()
		    .getStringFromXml(getActivity(), R.string.basic_null));
	}
	AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	builder.setTitle(CommUtil.getInstance().getStringFromXml(getActivity(),
		R.string.basic_select));
	builder.setItems(
		new String[] {

			CommUtil.getInstance().getStringFromXml(getActivity(),
				R.string.basic_check),

			CommUtil.getInstance().getStringFromXml(getActivity(),
				R.string.basic_cam) },
		new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int which) {
			if (which == 0) {
			    // 调取相册
			    Intent intent = new Intent(Intent.ACTION_PICK, null);
			    intent.setDataAndType(
				    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				    "image/*");
			    startActivityForResult(intent, REQUEST_CODE_PHOTO);
			} else {
			    // 调取相机
			    Intent intent = new Intent(
				    MediaStore.ACTION_IMAGE_CAPTURE);
			    // 下面这句指定调用相机拍照后的照片存储的路径
			    intent.putExtra(MediaStore.EXTRA_OUTPUT,
				    Uri.fromFile(new File(iconPath)));
			    startActivityForResult(intent, REQUEST_CODE_PICK);
			}
		    }
		})
		.setNegativeButton(
			CommUtil.getInstance().getStringFromXml(getActivity(),
				R.string.more_cancle), null).show();

    }

    // @OnClick(R.id.rl_resume)
    // private void onclickchooseResume(View view) {
    // if (null == VehicleApp.getInstance().getUserBean()
    // || TextUtils.isEmpty(VehicleApp.getInstance().getUserBean()
    // .getUsertosk())) {
    // launch(LoginActivity.class);
    // return;
    // }
    // myHandler.sendEmptyMessage(GOTORESUME);
    // }
    //
    // @OnClick(R.id.rl_histroy)
    // private void onclickchooseHistroy(View view) {
    // if (null == VehicleApp.getInstance().getUserBean()
    // || TextUtils.isEmpty(VehicleApp.getInstance().getUserBean()
    // .getUsertosk())) {
    // launch(LoginActivity.class);
    // return;
    // }
    // myHandler.sendEmptyMessage(GOTOHISTROY);
    // }

    @OnClick(R.id.integral_layout)
    public void onclickintentIntegral(View view) {
	Intent intent = new Intent(getActivity(), IntegralActivity.class);
	intent.putExtra("total_integral", tv_account.getText());
	launch(intent);
    }

    @OnClick(R.id.img_sign_or_no)
    public void onclicksign(View view) {
	querySign("2");
    }

    Handler myHandler = new Handler() {
	public void handleMessage(Message msg) {
	    switch (msg.what) {
	    case GOTORESUME:// 切换为简历管理

		add_resume_rl.setVisibility(View.VISIBLE);
		v_resume.setVisibility(View.VISIBLE);
		v_histroy.setVisibility(View.INVISIBLE);
		lv_resume.setVisibility(View.VISIBLE);
		lv_histroy.setVisibility(View.GONE);
		if (resumeArrayList.size() <= 0) {
		    queryResumeList();
		}
		if (!TextUtils
			.isEmpty(Preference.getString(Constant.PRE_RESUME_NAME
				+ Preference.getString(Constant.PRE_USER_NAME)))) {
		    try {
			RusumeListBean bean = JSON
				.parseObject(
					Preference
						.getString(Constant.PRE_RESUME_NAME
							+ Preference
								.getString(Constant.PRE_USER_NAME)),
					RusumeListBean.class);
			if (null != bean) {
			    if (bean.getSuccess().equals("1")) {
				if (null != bean.getFriedResumeList()
					&& bean.getFriedResumeList().size() > 0) {
				    resumeArrayList.clear();
				    resumeArrayList.addAll(bean
					    .getFriedResumeList());
				    L.e("yinzl", "使用换成简历");

				}

			    }
			}
		    } catch (Exception e) {
		    }

		}
		initResume(resumeArrayList);
		// ll_empty.setVisibility(View.VISIBLE);

		break;
	    case GOTOHISTROY:// 切换为投递历史
		add_resume_rl.setVisibility(View.GONE);
		v_resume.setVisibility(View.INVISIBLE);
		v_histroy.setVisibility(View.VISIBLE);
		lv_resume.setVisibility(View.GONE);
		ll_empty.setVisibility(View.GONE);
		lv_histroy.setVisibility(View.VISIBLE);
		if (hisArrayList.size() <= 0) {
		    mCurrentPage = 1;
		    queryHistroyList(mCurrentPage);
		    if (!TextUtils
			    .isEmpty(Preference.getString(Constant.PRE_HISTROY_NAME
				    + Preference
					    .getString(Constant.PRE_USER_NAME)))) {
			try {
			    HistroyListBean bean = JSON
				    .parseObject(
					    Preference
						    .getString(Constant.PRE_HISTROY_NAME
							    + Preference
								    .getString(Constant.PRE_USER_NAME)),
					    HistroyListBean.class);
			    if (null != bean) {
				if (bean.getSuccess().equals("1")) {
				    if (null != bean.getPostListHistory()
					    && bean.getPostListHistory().size() > 0) {

					L.e("yinzl", "使用本地换成历史推荐");
					L.e("yinzl", "使用本地换成历史推荐:"
						+ bean.getPostListHistory()
							.size());
					hisArrayList.clear();
					hisArrayList.addAll(bean
						.getPostListHistory());
					L.e("yinzl", "使用本地换成历史推荐:"
						+ hisArrayList.size());
					histroyAdapter.notifyDataSetChanged();
					setEmptyView();
				    }
				}
			    }
			} catch (Exception e) {
			    // TODO: handle exception
			}
		    }
		}

		break;
	    // 已签到
	    case SIGNED:
		img_sign_or_no.setImageDrawable(getResources().getDrawable(
			R.drawable.signed));
		break;
	    // 未签到
	    case NO_SIGN:
		String type_sign = (String) msg.obj;
		// 查询
		if ("1".equals(type_sign)) {
		    img_sign_or_no.setImageDrawable(getResources().getDrawable(
			    R.drawable.sign));
		}
		// 点击之后
		else {
		    img_sign_or_no.setImageDrawable(getResources().getDrawable(
			    R.drawable.signed));
		    int toalt = Integer
			    .valueOf(tv_account.getText().toString())
			    .intValue();
		    tv_account.setText(String.valueOf(toalt + 1));
		}
		break;
	    // 查询总页数成功
	    case TOTAL_INTEGRAL_SUCCESS:
		String toalt_integral = (String) msg.obj;
		tv_account.setText(toalt_integral);
		VehicleApp.getInstance().getUserBean()
			.setBounty(toalt_integral);
		break;
	    }
	    super.handleMessage(msg);
	}
    };

    private void initView() {
	resumeArrayList = new ArrayList<ResumeTempBean>();
	resumeAdapter = new ResumeAdapter(getActivity());
	resumeAdapter.setList(resumeArrayList);
	lv_resume.setAdapter(resumeAdapter);
	TextPaint tp = tv_account.getPaint();
	tp.setFakeBoldText(true);
	lv_histroy.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
	hisArrayList = new ArrayList<HistroyTempBean>();
	histroyAdapter = new HistroyAdapter(getActivity());
	histroyAdapter.setList(hisArrayList);
	lv_histroy.setAdapter(histroyAdapter);
	setEmptyView();
	lv_histroy
		.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
		    @Override
		    public void onPullDownToRefresh(
			    PullToRefreshBase<ListView> refreshView) {
			// queryHomeList();
		    }

		    @Override
		    public void onPullUpToRefresh(
			    PullToRefreshBase<ListView> refreshView) {
			mCurrentPage += 1;
			queryHistroyList(mCurrentPage);
		    }
		});
	rl_resume.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		if (null == VehicleApp.getInstance().getUserBean()
			|| TextUtils.isEmpty(VehicleApp.getInstance()
				.getUserBean().getUsertosk())) {
		    launch(LoginActivity.class);
		    return;
		}
		myHandler.sendEmptyMessage(GOTORESUME);
	    }
	});
	rl_histroy.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		// TODO Auto-generated method stub
		if (null == VehicleApp.getInstance().getUserBean()
			|| TextUtils.isEmpty(VehicleApp.getInstance()
				.getUserBean().getUsertosk())) {
		    launch(LoginActivity.class);
		    return;
		}
		myHandler.sendEmptyMessage(GOTOHISTROY);

	    }
	});
    }

    @Override
    public void onResume() {
	// TODO Auto-generated method stub
	user = VehicleApp.getInstance().getUserBean();
	if (null != user) {
	    initUser(user);
	    // L.e("yinzl", "用户信息" + user.getRname());
	    // initUser(user);
	    // queryResumeList();
	    // // myHandler.sendEmptyMessage(GOTORESUME);
	    // 查询签到
	    querySign("1");
	    // 查询总积分
	    queryTotalIntegral();
	}
	super.onResume();
    }

    private void initUser(UserBean bean) {
	if (!TextUtils.isEmpty(bean.getRname())) {
	    tv_username.setText(bean.getRname());
	}
	// if (!TextUtils.isEmpty(bean.getUser_phone())) {
	// tv_phone.setText(bean.getUser_phone());
	// }
	if (!TextUtils.isEmpty(bean.getUser_age())) {
	    tv_age.setText(bean.getUser_age()
		    + CommUtil.getInstance().getStringFromXml(getActivity(),
			    R.string.basic_age));
	}
	if (!TextUtils.isEmpty(bean.getBounty())) {
	    tv_account.setText(bean.getBounty());
	} else
	    tv_account.setText("0");
	if (!TextUtils.isEmpty(bean.getUser_pic())) {
	    setImageViewByView(iv_icon, R.drawable.user_default_photo,
		    Constant.URLIP_HEAD + bean.getUser_pic());
	} else {
	    iv_icon.setImageResource(R.drawable.user_default_photo);
	}

    }

    public void refreshData(boolean showDialog) {
	user = VehicleApp.getInstance().getUserBean();
	if (null != user) {
	    L.e("yinzl", "用户信息" + user.getRname());
	    initUser(user);
	}
    }

    @Override
    public void onClick(View v) {
	switch (v.getId()) {
	}

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

	Action action = new Action(getActivity());
	action.setDefaultLoadingTipOpen(false);
	action.setShowErrorDialog(true);
	action.execute(req, new OnResponseListener<RusumeListBean>() {
	    @Override
	    public void onResponseFinished(Request<RusumeListBean> request,
		    Response<RusumeListBean> response) {
		RusumeListBean bean = new RusumeListBean();
		bean = response.getT();
		if (null != bean) {
		    if (bean.getSuccess().equals("1")) {
			resumeArrayList.clear();
			if (null != bean.getFriedResumeList()
				&& bean.getFriedResumeList().size() > 0) {
			    resumeArrayList.addAll(bean.getFriedResumeList());

			}
			initResume(resumeArrayList);
		    } else {
			ToastUtil.showShort(getActivity(), bean.getErrorMsg());
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

    private void queryHistroyList(int page) {// 获取推荐历史

	if (null == VehicleApp.getInstance().getUserBean()
		|| TextUtils.isEmpty(VehicleApp.getInstance().getUserBean()
			.getUsertosk())) {
	    launch(LoginActivity.class);
	    return;
	}
	HashMap<String, String> pram = new HashMap<String, String>();
	pram.put("username", VehicleApp.getInstance().getUserBean()
		.getUsertosk());
	pram.put("page", page + "");
	pram.put("size", PAGE_SIZE + "");
	// 请求parser
	Request<HistroyListBean> req = new Request<HistroyListBean>();
	req.setRequestMethod(Request.M_POST);
	req.setBaseParser(new BaseParser<HistroyListBean>() {
	    @Override
	    public HistroyListBean parseResDate(String resBody)
		    throws DataException {
		if (resBody != null && !resBody.equals("")) {
		    if (mCurrentPage == 1) {
			Preference.putString(Constant.PRE_HISTROY_NAME
				+ Preference.getString(Constant.PRE_USER_NAME),
				resBody);
		    }
		    return JSON.parseObject(resBody, HistroyListBean.class);
		}
		return null;
	    }
	});
	req.setRequestParams(pram);
	req.setUrl(Constant.URLIP + "hr/ListHistoryServlet");

	Action action = new Action(getActivity());
	action.setDefaultLoadingTipOpen(false);
	action.setShowErrorDialog(true);
	action.execute(req, new OnResponseListener<HistroyListBean>() {
	    @Override
	    public void onResponseFinished(Request<HistroyListBean> request,
		    Response<HistroyListBean> response) {
		HistroyListBean bean = new HistroyListBean();
		bean = response.getT();
		if (null != bean) {
		    if (bean.getSuccess().equals("1")) {
			if (mCurrentPage == 1) {
			    hisArrayList.clear();
			}
			if (null != bean.getPostListHistory()
				&& bean.getPostListHistory().size() > 0) {

			    L.e("yinzl", "推荐历史数据："
				    + bean.getPostListHistory().size());

			    hisArrayList.addAll(bean.getPostListHistory());
			    if (!TextUtils.isEmpty(hisArrayList.get(0)
				    .getMy_bounty())) {

				user = VehicleApp.getInstance().getUserBean();
				if (null != user) {
				    user.setBounty(hisArrayList.get(0)
					    .getMy_bounty());
				    initUser(user);
				}

			    }
			    // if (!TextUtils.isEmpty(bean.getLastPage())
			    // && bean.getLastPage().equals("1")) {// 暂无更多
			    // L.e("yinzl", "不可上拉");
			    // lv_histroy.setPullLoadEnabled(false);
			    // } else {
			    // L.e("yinzl", "可上拉");
			    // lv_histroy.setPullLoadEnabled(true);
			    // }
			}
		    } else {
			ToastUtil.showShort(getActivity(), bean.getErrorMsg());
		    }
		}
		histroyAdapter.notifyDataSetChanged();
		lv_histroy.onRefreshComplete();
		if (null != bean && !TextUtils.isEmpty(bean.getLastPage())
			&& bean.getLastPage().equals("1")) {// 暂无更多
		    L.e("yinzl", "不可上拉");
		    lv_histroy.setPullLoadEnabled(false);
		} else {
		    L.e("yinzl", "可上拉");
		    lv_histroy.setPullLoadEnabled(true);
		}
		setEmptyView();
	    }

	    @Override
	    public void onResponseDataError(Request<HistroyListBean> equest) {
		if (mCurrentPage != 1) {
		    mCurrentPage--;
		}
		lv_histroy.onRefreshComplete();
		setEmptyView();
	    }

	    @Override
	    public void onResponseConnectionError(
		    Request<HistroyListBean> request, int statusCode) {
		if (mCurrentPage != 1) {
		    mCurrentPage--;
		}
		lv_histroy.onRefreshComplete();
		setEmptyView();
	    }

	    @Override
	    public void onResponseFzzError(Request<HistroyListBean> request,
		    ErrorMsg errorMsg) {
		if (mCurrentPage != 1) {
		    mCurrentPage--;
		}
		lv_histroy.onRefreshComplete();
		setEmptyView();
	    }

	});
    }

    /**
     * 签到接口 add by tongyq 2015.11.17
     */
    private void querySign(final String type) {

	if (null == VehicleApp.getInstance().getUserBean()
		|| TextUtils.isEmpty(VehicleApp.getInstance().getUserBean()
			.getUsertosk())) {
	    launch(LoginActivity.class);
	    return;
	}
	HashMap<String, String> pram = new HashMap<String, String>();
	pram.put("user_id", VehicleApp.getInstance().getUserBean().getId());
	pram.put("type", type);
	// 请求parser
	Request<SignBean> req = new Request<SignBean>();
	req.setRequestMethod(Request.M_POST);
	req.setBaseParser(new BaseParser<SignBean>() {
	    @Override
	    public SignBean parseResDate(String resBody) throws DataException {

		if (null != resBody && !"".equals(resBody)) {
		    return JSON.parseObject(resBody, SignBean.class);
		}
		return null;
	    }
	});
	req.setRequestParams(pram);
	req.setUrl(Constant.URLIP + "webhr/sign/rclsign.do?");

	Action action = new Action(getActivity());
	action.setDefaultLoadingTipOpen(false);
	action.setShowErrorDialog(true);
	action.execute(req, new OnResponseListener<SignBean>() {
	    @Override
	    public void onResponseFinished(Request<SignBean> request,
		    Response<SignBean> response) {
		SignBean bean = new SignBean();
		bean = response.getT();
		if (null != bean) {
		    if (null != bean.getMessage()) {
			// 已经签到
			if ("fail".equals(bean.getMessage())
				&& "该用户已签到".equals(bean.getError())) {
			    myHandler.sendEmptyMessage(SIGNED);
			}
			// 未签到
			else {
			    Message msg = myHandler.obtainMessage();
			    msg.what = NO_SIGN;
			    msg.obj = type;
			    myHandler.sendMessage(msg);
			}
		    }
		} else {
		    ToastUtil.showShort(getActivity(), bean.getError());
		}
	    }

	    @Override
	    public void onResponseDataError(Request<SignBean> equest) {
	    }

	    @Override
	    public void onResponseConnectionError(Request<SignBean> request,
		    int statusCode) {
	    }

	    @Override
	    public void onResponseFzzError(Request<SignBean> request,
		    ErrorMsg errorMsg) {
	    }

	});
    }

    /**
     * 查询总积分 add by tongyq 2015.11.18
     */
    private void queryTotalIntegral() {

	HashMap<String, String> pram = new HashMap<String, String>();
	pram.put("id", VehicleApp.getInstance().getUserBean().getId());
	// 请求parser
	Request<TotalIntegralBean> req = new Request<TotalIntegralBean>();
	req.setRequestMethod(Request.M_POST);
	req.setBaseParser(new BaseParser<TotalIntegralBean>() {
	    @Override
	    public TotalIntegralBean parseResDate(String resBody)
		    throws DataException {

		if (null != resBody && !"".equals(resBody)) {
		    return JSON.parseObject(resBody, TotalIntegralBean.class);
		}
		return null;
	    }
	});
	req.setRequestParams(pram);
	req.setUrl(Constant.URLIP + "webhr/jfDetails/zjf.do?");

	Action action = new Action(getActivity());
	action.setDefaultLoadingTipOpen(false);
	action.setShowErrorDialog(true);
	action.execute(req, new OnResponseListener<TotalIntegralBean>() {
	    @Override
	    public void onResponseFinished(Request<TotalIntegralBean> request,
		    Response<TotalIntegralBean> response) {
		TotalIntegralBean bean = new TotalIntegralBean();
		bean = response.getT();
		if (null != bean) {
		    Message msg = myHandler.obtainMessage();
		    msg.what = TOTAL_INTEGRAL_SUCCESS;
		    msg.obj = bean.getZjf();
		    myHandler.sendMessage(msg);
		}
	    }

	    @Override
	    public void onResponseDataError(Request<TotalIntegralBean> equest) {
	    }

	    @Override
	    public void onResponseConnectionError(
		    Request<TotalIntegralBean> request, int statusCode) {
	    }

	    @Override
	    public void onResponseFzzError(Request<TotalIntegralBean> request,
		    ErrorMsg errorMsg) {
	    }

	});
    }

    private void delateResume(String friedId, final int position) {// 获取简历列表
	if (null == VehicleApp.getInstance().getUserBean()
		|| TextUtils.isEmpty(VehicleApp.getInstance().getUserBean()
			.getUsertosk())) {
	    launch(LoginActivity.class);
	    return;
	}
	HashMap<String, String> pram = new HashMap<String, String>();
	pram.put("username", VehicleApp.getInstance().getUserBean()
		.getUsertosk());
	pram.put("fried_id", friedId);
	pram.put("type", "3");
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
	req.setRequestParams(pram);
	req.setUrl(Constant.URLIP + "hr/FriedResumeManageServlet");

	Action action = new Action(getActivity());
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
			Preference.putString(Constant.PRE_RESUME_NAME
				+ Preference.getString(Constant.PRE_USER_NAME),
				"");
			resumeArrayList.remove(position);
			if (resumeArrayList.size() <= 0) {
			    ll_empty.setVisibility(View.VISIBLE);
			    lv_resume.setVisibility(View.GONE);
			} else {
			    ll_empty.setVisibility(View.GONE);
			    lv_resume.setVisibility(View.VISIBLE);
			}
			resumeAdapter.notifyDataSetChanged();
			ToastUtil.showShort(
				getActivity(),
				CommUtil.getInstance().getStringFromXml(
					getActivity(), R.string.basic_delete));
		    } else {
			ToastUtil.showShort(getActivity(), bean.getErrorMsg());
		    }
		}
	    }

	    @Override
	    public void onResponseDataError(Request<BaseBean> equest) {
	    }

	    @Override
	    public void onResponseConnectionError(Request<BaseBean> request,
		    int statusCode) {
	    }

	    @Override
	    public void onResponseFzzError(Request<BaseBean> request,
		    ErrorMsg errorMsg) {
	    }

	});
    }

    private void initResume(ArrayList<ResumeTempBean> resumeArrayList) {
	if (v_resume.getVisibility() == View.VISIBLE) {
	    if (resumeArrayList.size() <= 0) {
		ll_empty.setVisibility(View.VISIBLE);
		lv_resume.setVisibility(View.GONE);
	    } else {
		ll_empty.setVisibility(View.GONE);
		lv_resume.setVisibility(View.VISIBLE);
	    }
	    resumeAdapter.notifyDataSetChanged();
	}
    }

    private void initHistroy(ArrayList<HistroyTempBean> list) {
	hisArrayList.clear();
	hisArrayList.addAll(list);
	histroyAdapter.notifyDataSetChanged();
    }

    class ResumeAdapter extends ArrayListAdapter<ResumeTempBean> {

	public ResumeAdapter(Activity context) {
	    super(context);
	}

	public View getView(final int position, View convertView,
		ViewGroup parent) {
	    if (convertView == null) {
		convertView = LayoutInflater.from(mContext).inflate(
			R.layout.item_resume, null);
	    }

	    TextView tv_resume_name = ViewHolder.get(convertView,
		    R.id.tv_resume_name);

	    final ResumeTempBean bean = mList.get(position);
	    convertView.setOnClickListener(new OnClickListener() {

		@Override
		public void onClick(View v) {
		    Intent intent = new Intent();
		    intent.setClass(getActivity(), RecommendationActivity.class);
		    intent.putExtra("resumeId", bean.getFried_id());
		    intent.putExtra("resumeName", bean.getFriend_name());
		    launch(intent);
		}
	    });
	    convertView.setOnLongClickListener(new OnLongClickListener() {

		@Override
		public boolean onLongClick(View v) {
		    new AlertDialog.Builder(getActivity())
			    .setTitle(
				    CommUtil.getInstance()
					    .getStringFromXml(getActivity(),
						    R.string.basic_shere)
					    + bean.getFriend_name() + "”？")
			    .setPositiveButton(
				    CommUtil.getInstance().getStringFromXml(
					    getActivity(), R.string.basic_de),
				    new DialogInterface.OnClickListener() {
					public void onClick(
						DialogInterface dialog,
						int which) {
					    delateResume(bean.getFried_id(),
						    position);
					}
				    })
			    .setNegativeButton(
				    CommUtil.getInstance()
					    .getStringFromXml(getActivity(),
						    R.string.more_cancle),
				    new DialogInterface.OnClickListener() {
					public void onClick(
						DialogInterface dialog,
						int which) {
					    // TODO Auto-generated method stub

					}
				    }).show();

		    return true;
		}
	    });
	    if (bean != null) {
		tv_resume_name.setText(bean.getFriend_name());

	    }
	    return convertView;
	}
    }

    class HistroyAdapter extends ArrayListAdapter<HistroyTempBean> {

	public HistroyAdapter(Activity context) {
	    super(context);
	}

	public View getView(final int position, View convertView,
		ViewGroup parent) {
	    if (convertView == null) {
		convertView = LayoutInflater.from(mContext).inflate(
			R.layout.item_histroy, null);
	    }

	    TextView tv_company_name = ViewHolder.get(convertView,
		    R.id.tv_company_name);
	    TextView tv_job_name = ViewHolder
		    .get(convertView, R.id.tv_job_name);
	    TextView tv_time = ViewHolder.get(convertView, R.id.tv_time);
	    TextView tv_friend_name = ViewHolder.get(convertView,
		    R.id.tv_friend_name);
	    TextView tv_account = ViewHolder.get(convertView, R.id.tv_account);

	    final HistroyTempBean bean = mList.get(position);

	    convertView.setOnClickListener(new OnClickListener() {

		@Override
		public void onClick(View v) {
		    Intent intent = new Intent();
		    intent.setClass(getActivity(),
			    OfficeInfomationActivity.class);
		    intent.putExtra("historyId", bean.getPost_id());
		    intent.putExtra("historyBounty", bean.getBounty());// 总积分
		    intent.putExtra("historyBounty2", bean.getBounty2());// 已获积分
		    intent.putExtra("historyFriedName", bean.getFried_name());// 被推荐人名字
		    intent.putExtra("historydate", bean.getReservation_date());// 预约时间
		    launch(intent);
		}
	    });
	    if (bean != null) {
		tv_company_name.setText(bean.getSub_name());
		tv_job_name.setText(bean.getPost_name());
		tv_time.setText("申请时间："
			+ bean.getCreate_time().split(" +")[0]);
		tv_friend_name.setText("被推荐人：" + bean.getFried_name());
		if (!TextUtils.isEmpty(bean.getBounty2()))
		    tv_account.setText(CommUtil.getInstance().getStringFromXml(
			    getActivity(), R.string.basic_boundy)
			    + bean.getBounty2());
		else
		    tv_account.setText(CommUtil.getInstance().getStringFromXml(
			    getActivity(), R.string.basic_boundy) + 0);

	    }
	    return convertView;
	}
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
	super.onActivityResult(requestCode, resultCode, data);
	if (resultCode == Activity.RESULT_OK) {
	    switch (requestCode) {

	    // 如果是直接从相册获取
	    case REQUEST_CODE_PHOTO:
		if (data != null) {
		    startPhotoZoom(data.getData());
		}
		break;
	    // 如果是调用相机拍照时
	    case REQUEST_CODE_PICK:
		if (resultCode == Activity.RESULT_OK) {
		    if (TextUtils.isEmpty(iconPath)) {
			iconPath = CommUtil.getInstance().getExternCachePath()
				+ "icon.png";
		    }
		    File temp = new File(iconPath);
		    startPhotoZoom(Uri.fromFile(temp));
		}
		break;
	    // 取得裁剪后的图片
	    case REQUEST_CODE_EDITPIC:
		/**
		 * 非空判断大家一定要验证，如果不验证的话， 在剪裁之后如果发现不满意，要重新裁剪，丢弃
		 * 当前功能时，会报NullException，只 在这个地方加下，大家可以根据不同情况在合适的 地方做判断处理类似情况
		 *
		 */
		if (data != null) {
		    setPicToView(data);
		}
		break;

	    default:
		break;

	    }

	}

    }

    /**
     *
     * 裁剪图片方法实现
     *
     *
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
	Intent intent = new Intent("com.android.camera.action.CROP");
	intent.setDataAndType(uri, "image/*");

	// 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪

	intent.putExtra("crop", "true");
	L.i("yinzl", "uri is ：" + uri.toString());
	// aspectX aspectY 是宽高的比例
	intent.putExtra("aspectX", 1);
	intent.putExtra("aspectY", 1);

	// outputX outputY 是裁剪图片宽高

	intent.putExtra("outputX", 150);

	intent.putExtra("outputY", 150);
	intent.putExtra("return-data", true);

	startActivityForResult(intent, REQUEST_CODE_EDITPIC);

    }

    /**
     *
     * 保存裁剪之后的图片数据
     *
     *
     * @param picdata
     */

    private void setPicToView(Intent picdata) {
	Bundle extras = picdata.getExtras();
	if (extras != null) {
	    m_bitmap = extras.getParcelable("data");
	    // photo = AsyncImageLoader.zoomImage(photo, 200, 200);

	    try {
		String path = CommUtil.getInstance().getExternCachePath();
		File file = new File(path);
		if (!file.exists()) {
		    file.mkdir();
		}

		facePath = path + "faceHead.jpg";
		File file2 = new File(facePath);
		if (file2.exists()) {
		    file2.delete();
		}
		// 图片保存到本地
		// FileUtils.getIns().createFile(m_bitmap, facePath);
		fileMimeType = PubUtils.getImgRealMimeType(PubUtils
			.getFileFormat(facePath));
	    } catch (Exception e) {
		// LogUtil.i("faceHead", e.getMessage());
		e.printStackTrace();
	    }
	    iv_icon.setImageBitmap(m_bitmap);

	    if (m_bitmap != null) {
		System.gc();
		System.gc();
		System.gc();
		imgBytes = ImageUtils.Bitmap2Bytes(m_bitmap);
		uploadUserHead(imgBytes, fileMimeType);
		m_bitmap.recycle();
		System.gc();

	    } else {
		PubUtils.popTipOrWarn(getActivity(), CommUtil.getInstance()
			.getStringFromXml(getActivity(), R.string.basic_null2));
	    }

	    m_bitmap = null;
	}

    }

    private void setEmptyView() {
	if (null == lv_histroy.getEmptyView()) {
	    View view = View.inflate(getActivity(), R.layout.empty, null);
	    ((TextView) view.findViewById(R.id.tv_no)).setText(CommUtil
		    .getInstance().getStringFromXml(getActivity(),
			    R.string.search_null2));
	    lv_histroy.setEmptyView(view);
	}
    }

    private void uploadUserHead(byte[] imgbytes, String fileMimeType) {
	if (null == VehicleApp.getInstance().getUserBean()
		|| TextUtils.isEmpty(VehicleApp.getInstance().getUserBean()
			.getUsertosk())) {
	    launch(LoginActivity.class);
	    return;
	}
	HashMap<String, String> pram = new HashMap<String, String>();
	pram.put("username", VehicleApp.getInstance().getUserBean()
		.getUsertosk());
	if (imgbytes == null) {
	    return;
	}
	if (imgbytes.length < 9) {
	    return;
	}
	// image/png
	final Request<HeadPicBean> req = new Request<HeadPicBean>();
	req.setUrl(Constant.URLIP + "hr/ModifyHeaderServlet");
	req.setRequestMethod(Request.M_POST); // 与POST 新增的区别，在这里改下即可
	req.setRequestContenType(Request.RCT_FORMDATA);
	req.setRequestParams(pram);
	req.setSingleFileDateByte(imgbytes, "jpg");
	req.setBaseParser(new BaseParser<HeadPicBean>() {
	    @Override
	    public HeadPicBean parseResDate(String resBody)
		    throws DataException {
		if (resBody != null && !resBody.equals("")) {
		    return JSON.parseObject(resBody, HeadPicBean.class);
		}
		return null;
	    }
	});

	Action action = new Action(this.getActivity());
	action.setDefaultLoadingTipOpen(true);
	action.execute(req, new OnResponseListener<HeadPicBean>() {

	    @Override
	    public void onResponseFinished(Request<HeadPicBean> request,
		    Response<HeadPicBean> response) {

		HeadPicBean bean = new HeadPicBean();
		bean = response.getT();
		if (null != bean) {
		    if (bean.getSuccess().equals("1")
			    && !TextUtils.isEmpty(bean.getPic_url())) {
			setImageViewByView(iv_icon,
				R.drawable.user_default_photo,
				Constant.URLIP_HEAD + bean.getPic_url());
			user.setUser_pic(bean.getPic_url());
			afeiDb.dropTableIfTableExist(UserBean.class);
			afeiDb.save(user);
		    } else {
		    }
		}

	    }

	    @Override
	    public void onResponseDataError(Request<HeadPicBean> equest) {
		// TODO Auto-generated method stub
	    }

	    @Override
	    public void onResponseConnectionError(Request<HeadPicBean> request,
		    int statusCode) {
	    }

	    @Override
	    public void onResponseFzzError(Request<HeadPicBean> request,
		    ErrorMsg errorMsg) {
	    }
	});
    }

    public void onEventMainThread(ComEvent event) {
	System.out.println("onEventMainThread:"
		+ Thread.currentThread().getId());
	if (event.getType() == ComEvent.HISTROY) {
	    L.e("yinzl", "刷新历史推荐");
	    mCurrentPage = 1;
	    queryHistroyList(mCurrentPage);
	} else if (event.getType() == ComEvent.RESUME) {
	    L.e("yinzl", "刷新简历列表");
	    queryResumeList();
	} else if (event.getType() == ComEvent.USERINFO) {
	    L.e("yinzl", "刷新个人信息");
	    user = VehicleApp.getInstance().getUserBean();
	    if (null != user) {
		initUser(user);
		// L.e("yinzl", "用户信息" + user.getRname());
		// initUser(user);
		// queryResumeList();
		// // myHandler.sendEmptyMessage(GOTORESUME);
	    }
	}
    }

    private void setImageViewByView(final CircleImageView view,
	    int DefaultIconId, String IconUrl) {
	if (!StringUtils.isBlank(IconUrl)) {
	    ImageLoaderUtil.getInstance().displayImage(IconUrl, view,
		    DefaultIconId); // 不使用原图
	} else {
	    view.setImageResource(DefaultIconId);
	}
    }
}
