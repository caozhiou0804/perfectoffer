package com.dh.perfectoffer.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.dh.perfectoffer.R;
import com.dh.perfectoffer.VehicleApp;
import com.dh.perfectoffer.adapter.ArrayListAdapter;
import com.dh.perfectoffer.adapter.ViewHolder;
import com.dh.perfectoffer.constant.Constant;
import com.dh.perfectoffer.demo.VehicleActivity;
import com.dh.perfectoffer.entity.IntegralBean;
import com.dh.perfectoffer.entity.IntegralParserBean;
import com.dh.perfectoffer.event.framework.alibaba.fastjson.JSON;
import com.dh.perfectoffer.event.framework.net.exception.DataException;
import com.dh.perfectoffer.event.framework.net.fgview.Action;
import com.dh.perfectoffer.event.framework.net.fgview.BaseParser;
import com.dh.perfectoffer.event.framework.net.fgview.OnResponseListener;
import com.dh.perfectoffer.event.framework.net.fgview.Request;
import com.dh.perfectoffer.event.framework.net.fgview.Response;
import com.dh.perfectoffer.event.framework.net.fgview.Response.ErrorMsg;
import com.dh.perfectoffer.handmark.pulltorefresh.library.PullToRefreshBase;
import com.dh.perfectoffer.handmark.pulltorefresh.library.PullToRefreshListView;
import com.dh.perfectoffer.xutils.view.ViewUtils;
import com.dh.perfectoffer.xutils.view.annotation.ViewInject;
import com.dh.perfectoffer.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
public class IntegralActivity extends VehicleActivity {

	// ListView
	@ViewInject(R.id.pxlv_intergral)
	private PullToRefreshListView integral_list;
	// 无数据布局
	@ViewInject(R.id.ll_empty)
	private View ll_empty;
	// 返回
	@ViewInject(R.id.btn_title_btn_back_layout)
	private ImageView btn_title_btn_back_layout;
	@ViewInject(R.id.integral_tv)
	private TextView integral_tv;
	// 积分界面
	@ViewInject(R.id.integral_layout)
	private LinearLayout integral_layout;

	private ArrayList<IntegralBean> mIntegralList;
	private IntegralAdapter mAdapter;

	private int page = 1;
	private int rows = 10;
	private int total = 1;
	private String total_integral;

	public static final int MODEL_REQ_SUCCESS = 0x10010;
	public static final int MODEL_REQ_FAILED = 0x10011;
	public static final int MODEL_REQ_SUCCESS_MORE = 0x10012;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_integral);
		ViewUtils.inject(this);
		total_integral = getIntent().getStringExtra("total_integral");
		initView();
		refresh();
	}

	private void initView() {
		mIntegralList = new ArrayList<IntegralBean>();
		mAdapter = new IntegralAdapter(IntegralActivity.this);
		mAdapter.setList(mIntegralList);
		integral_list.setAdapter(mAdapter);
		integral_tv.setText(total_integral);
	}

	@OnClick(R.id.btn_title_btn_back_layout)
	public void onclickback(View view) {
		IntegralActivity.this.finish();
	}

	@OnClick(R.id.ll_empty)
	public void onclickemptyReq(View view) {
		queryIntegralList(1, rows);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		queryIntegralList(1, rows);
	}

	private void refresh() {
		integral_list.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
		integral_list
				.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						// TODO Auto-generated method stub
						page += 1;
						queryIntegralList(page, rows);
					}
				});
		// integral_list.setRefreshing();
	}

	private void queryIntegralList(final int page, int rows) {
		if (null == VehicleApp.getInstance().getUserBean()
				|| TextUtils.isEmpty(VehicleApp.getInstance().getUserBean()
						.getUsertosk())) {
			launch(LoginActivity.class);
			return;
		}
		// 请求parser
		Request<IntegralParserBean> req = new Request<IntegralParserBean>();
		req.setRequestMethod(Request.M_POST);
		req.setBaseParser(new BaseParser<IntegralParserBean>() {

			@Override
			public IntegralParserBean parseResDate(String resBody)
					throws DataException {
				if (null != resBody && !"".equals(resBody)) {
					return JSON.parseObject(resBody, IntegralParserBean.class);
				}
				return null;
			}
		});
		HashMap<String, String> pram = new HashMap<String, String>();
		pram.put("user_id", VehicleApp.getInstance().getUserBean().getId());
		pram.put("page", String.valueOf(page));
		pram.put("rows", String.valueOf(rows));
		req.setRequestParams(pram);
		req.setUrl(Constant.URLIP + "webhr/jfDetails/getList.do?");

		Action action = new Action(IntegralActivity.this);
		action.setDefaultLoadingTipOpen(false);
		action.setShowErrorDialog(true);
		action.execute(req, new OnResponseListener<IntegralParserBean>() {

			@Override
			public void onResponseFinished(Request<IntegralParserBean> request,
					Response<IntegralParserBean> response) {
				IntegralParserBean bean = new IntegralParserBean();
				bean = response.getT();
				// 请求到数据
				if (null != bean) {
					total = bean.getTotal();
					if (null != bean.getRows() && bean.getRows().size() > 0) {
						if (page == 1) {
							Message msg = mHander.obtainMessage();
							msg.what = MODEL_REQ_SUCCESS;
							msg.obj = bean.getRows();
							mHander.sendMessage(msg);
						} else {
							Message msg = mHander.obtainMessage();
							msg.what = MODEL_REQ_SUCCESS_MORE;
							msg.obj = bean.getRows();
							mHander.sendMessage(msg);
						}
					} else {
						mHander.sendEmptyMessage(MODEL_REQ_FAILED);
					}
				}
				// 未请求到数据
				else {
					mHander.sendEmptyMessage(MODEL_REQ_FAILED);
				}

			}

			@Override
			public void onResponseDataError(Request<IntegralParserBean> equest) {
				// TODO Auto-generated method stub
				mHander.sendEmptyMessage(MODEL_REQ_FAILED);
			}

			@Override
			public void onResponseConnectionError(
					Request<IntegralParserBean> request, int statusCode) {
				// TODO Auto-generated method stub
				mHander.sendEmptyMessage(MODEL_REQ_FAILED);
			}

			@Override
			public void onResponseFzzError(Request<IntegralParserBean> request,
					ErrorMsg errorMsg) {
				// TODO Auto-generated method stub
				mHander.sendEmptyMessage(MODEL_REQ_FAILED);
			}
		});
	}

	Handler mHander = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			try {
				switch (msg.what) {
				// 请求成功有数据
				case MODEL_REQ_SUCCESS:
					integral_list.onRefreshComplete();
					if (page < total) {
						integral_list.setPullLoadEnabled(true);
					} else {
						integral_list.setPullLoadEnabled(false);
					}
					List<IntegralBean> IntegralBeanList = (List<IntegralBean>) msg.obj;
					if (null != IntegralBeanList && IntegralBeanList.size() > 0) {
						mIntegralList.clear();
						mIntegralList.addAll(IntegralBeanList);
						mAdapter.notifyDataSetChanged();
					}
					break;
				// 分页请求成功
				case MODEL_REQ_SUCCESS_MORE:
					integral_list.onRefreshComplete();
					if (page < total) {
						integral_list.setPullLoadEnabled(true);
					} else {
						integral_list.setPullLoadEnabled(false);
					}
					List<IntegralBean> MoreIntegralBeanList = (List<IntegralBean>) msg.obj;
					mIntegralList.addAll(MoreIntegralBeanList);
					mAdapter.notifyDataSetChanged();
					break;
				// 未请求到数据/请求报错
				case MODEL_REQ_FAILED:
					integral_list.onRefreshComplete();
					if (page > 1)
						page--;
					setEmpterView(mIntegralList);
					break;

				default:
					break;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	};

	private void setEmpterView(List<IntegralBean> list) {
		if (null == list || list.size() <= 0) {
			ll_empty.setVisibility(View.VISIBLE);
			integral_layout.setVisibility(View.GONE);
		} else {
			ll_empty.setVisibility(View.GONE);
			integral_layout.setVisibility(View.VISIBLE);
		}
	}

	class IntegralAdapter extends ArrayListAdapter<IntegralBean> {

		public IntegralAdapter(Activity context) {
			super(context);
			// TODO Auto-generated constructor stub
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			if (null == convertView) {
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.item_integral, null);
			}
			final IntegralBean bean = mList.get(position);
			TextView tv_details_name = ViewHolder.get(convertView,
					R.id.tv_details_name);
			TextView tv_time = ViewHolder.get(convertView, R.id.tv_time);
			TextView integral_tv = ViewHolder
					.get(convertView, R.id.integral_tv);
			if (null != bean) {
				if (!TextUtils.isEmpty(bean.getDetails()))
					tv_details_name.setText(bean.getDetails());
				if (!TextUtils.isEmpty(bean.getCreate_date()))
					tv_time.setText(bean.getCreate_date());
				if (!TextUtils.isEmpty(bean.getJifen())) {
					integral_tv.setText(bean.getJifen());
					if (bean.getJifen().contains("+")) {
						integral_tv.setTextColor(getResources().getColor(
								R.color.bluish_green));
					} else if (bean.getJifen().contains("-")) {
						integral_tv.setTextColor(getResources().getColor(
								R.color.red));
					}
				}
			}
			return convertView;
		}
	}

}
