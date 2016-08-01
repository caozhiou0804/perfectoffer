package com.dh.perfectoffer.fragment;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dh.perfectoffer.R;
import com.dh.perfectoffer.adapter.ArrayListAdapter;
import com.dh.perfectoffer.adapter.ViewHolder;
import com.dh.perfectoffer.entity.IllegalBean;
import com.dh.perfectoffer.event.framework.alibaba.fastjson.JSON;
import com.dh.perfectoffer.event.framework.net.exception.DataException;
import com.dh.perfectoffer.event.framework.net.fgview.Action;
import com.dh.perfectoffer.event.framework.net.fgview.BaseParser;
import com.dh.perfectoffer.event.framework.net.fgview.OnResponseListener;
import com.dh.perfectoffer.event.framework.net.fgview.Request;
import com.dh.perfectoffer.event.framework.net.fgview.Response;
import com.dh.perfectoffer.event.framework.net.fgview.Response.ErrorMsg;
import com.dh.perfectoffer.event.framework.util.Density;
import com.dh.perfectoffer.handmark.pulltorefresh.library.PullToRefreshBase;
import com.dh.perfectoffer.handmark.pulltorefresh.library.PullToRefreshSwipteListView;
import com.dh.perfectoffer.handmark.pulltorefresh.library.SwipeListView;
import com.dh.perfectoffer.xutils.sample.utils.ToastUtil;
import com.dh.perfectoffer.xutils.view.ViewUtils;
import com.dh.perfectoffer.xutils.view.annotation.ViewInject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * 违章查询
 * 
 * @author wang
 * 
 */
public class IllegalFragment extends Fragment implements OnClickListener,
		OnItemClickListener {
	private ListView xlv_messages;
	private HRAdapter mMessageAdapter;
	private ArrayList<IllegalBean> mHrList;

	private boolean isUpRefresh = false;

	private int mCurrentPage = 1;
	private static final int PAGE_SIZE = 15;

	@ViewInject(R.id.edit_lsprefix)
	private EditText edit_lsprefix;

	@ViewInject(R.id.edit_lsnum)
	private EditText edit_lsnum;

	@ViewInject(R.id.edit_lstype)
	private EditText edit_lstype;

	@ViewInject(R.id.edit_engineno)
	private EditText edit_engineno;

	@ViewInject(R.id.edit_frameno)
	private EditText edit_frameno;

	@ViewInject(R.id.but_query)
	private Button but_query;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.db_fragment, container, false);
		ViewUtils.inject(this, view);
		initView();
		return view;
	}

	private void initView() {

		pslv_message.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
		xlv_messages = pslv_message.getRefreshableView();
		((SwipeListView) xlv_messages).setRightViewWidth(Density.of(
				getActivity(), 80));
		xlv_messages.setSelector(new ColorDrawable(0));

		mHrList = new ArrayList<IllegalBean>();
		mMessageAdapter = new HRAdapter(getActivity());
		mMessageAdapter.setList(mHrList);
		xlv_messages.setAdapter(mMessageAdapter);

		pslv_message
				.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<SwipeListView>() {

					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<SwipeListView> refreshView) {
						isUpRefresh = true;
						mCurrentPage = 1;
						// executeHttp(mCurrentPage);
						queryCouponListByOrderId(true);
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<SwipeListView> refreshView) {
						isUpRefresh = false;
						mCurrentPage += 1;
						queryCouponListByOrderId(true);
						// executeHttp(mCurrentPage);
					}
				});
		xlv_messages.setOnItemClickListener(this);
		// pslv_message.setRefreshing();
		but_query.setOnClickListener(this);
	}

	Handler myHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				String message = "";
				message = (String) msg.obj;
				ToastUtil.showLong(IllegalFragment.this.getActivity(), message);
				break;
			}
			super.handleMessage(msg);
		}
	};

	private void queryCouponListByOrderId(boolean dialog) {

		// 结果解析parser
		// GetQueryListParser parser = new GetQueryListParser();
		// 请求parser
		Request<List<IllegalBean>> req = new Request<List<IllegalBean>>();
		req.setRequestMethod(Request.M_POST);
		req.setBaseParser(new BaseParser<List<IllegalBean>>() {

			@Override
			public List<IllegalBean> parseResDate(String resBody)
					throws DataException {
				Log.d("wang", resBody);
				if (resBody != null && !resBody.equals("")) {
					try {
						JSONObject jsonObject = new JSONObject(resBody);
						String error = jsonObject.optString("error");
						String msg = jsonObject.optString("msg");
						String data = jsonObject.optString("data");
						if (error.equals("0")) {
							if (!TextUtils.isEmpty(data)) {
								return JSON.parseArray(data, IllegalBean.class);
							}
						} else {
							Message message = new Message();
							message.what = 1;
							message.obj = msg;
							myHandler.sendMessage(message);
							// ToastUtil.showLong(IllegalFragment.this.getActivity(),
							// msg);
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				return null;
			}
		});
		HashMap<String, String> params = new HashMap<String, String>();

		String lsprefix = edit_lsprefix.getText().toString();
		String lsnum = edit_lsnum.getText().toString();
		String lstype = edit_lstype.getText().toString();
		String engineno = edit_engineno.getText().toString();
		String frameno = edit_frameno.getText().toString();
		params.put("appkey", "1307ee261de8bbcf83830de89caae73f");
		params.put("carorg", "shanghai");
		params.put("lsprefix", lsprefix);
		params.put("lsnum", lsnum);
		params.put("lstype", lstype);
		params.put("engineno", engineno);
		params.put("frameno", frameno);
		req.setUrl("http://api.46644.com/illegal/query/");
		req.setRequestParams(params);

		if (!TextUtils.isEmpty(lsprefix) && !TextUtils.isEmpty(lsnum)
				&& !TextUtils.isEmpty(lstype) && !TextUtils.isEmpty(engineno)
				&& !TextUtils.isEmpty(frameno)) {
			Action action = new Action(getActivity());
			if (!dialog) {
				action.setDefaultLoadingTipOpen(false);
			}
			action.execute(req, new OnResponseListener<List<IllegalBean>>() {
				@Override
				public void onResponseFinished(
						Request<List<IllegalBean>> request,
						Response<List<IllegalBean>> response) {
					List<IllegalBean> resList = new ArrayList<IllegalBean>();
					resList = response.getT();
					// L.e("yinzl", "获取到的第一个数据为：" + resList.get(0).toString());
					if (null == resList || resList.size() <= 0) {
						if (!isUpRefresh) {
							mCurrentPage--;
						} else {
							mHrList.clear();
						}
					} else {
						if (isUpRefresh) {// 是上拉，插入到前页
							mHrList.clear();
						}
						mHrList.addAll(resList);
					}
					mMessageAdapter.notifyDataSetChanged();
					pslv_message.onRefreshComplete();
					if (null == resList || (resList.size() < PAGE_SIZE)) {
						pslv_message.setPullLoadEnabled(false);
					} else {
						pslv_message.setPullLoadEnabled(true);
					}
					setEmptyView();
				}

				@Override
				public void onResponseDataError(
						Request<List<IllegalBean>> equest) {
					// TODO Auto-generated method stub
					ToastUtil.showLong(getActivity(), "请求失败");
					if (mCurrentPage != 1) {
						mCurrentPage--;
					}
					pslv_message.onRefreshComplete();
					setEmptyView();
				}

				@Override
				public void onResponseConnectionError(
						Request<List<IllegalBean>> request, int statusCode) {
					ToastUtil.showLong(getActivity(), "请求失败");
					if (mCurrentPage != 1) {
						mCurrentPage--;
					}
					pslv_message.onRefreshComplete();
					setEmptyView();
				}

				@Override
				public void onResponseFzzError(
						Request<List<IllegalBean>> request, ErrorMsg errorMsg) {
					ToastUtil.showLong(getActivity(), "请求失败");
					if (mCurrentPage != 1) {
						mCurrentPage--;
					}
					pslv_message.onRefreshComplete();
					setEmptyView();
					// btnController(true);
					// if (page.equals(page0)) {
					// lv_voucher.setVisibility(View.GONE);
					// ll_no_data.setVisibility(View.VISIBLE);
					// }
				}

			});
		} else {
			mMessageAdapter.notifyDataSetChanged();
			pslv_message.onRefreshComplete();
			pslv_message.setPullLoadEnabled(true);
			setEmptyView();
		}

	}

	private void setEmptyView() {
		if (null == pslv_message.getEmptyView()) {
			View view = View.inflate(getActivity(), R.layout.empty, null);
			((TextView) view.findViewById(R.id.tv_no)).setText("暂无数据");
			pslv_message.setEmptyView(view);
		}
	}

	@ViewInject(R.id.pxlv_person)
	private PullToRefreshSwipteListView pslv_message;

	class HRAdapter extends ArrayListAdapter<IllegalBean> {

		public HRAdapter(Activity context) {
			super(context);
		}

		public View getView(final int position, View convertView,
				ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.home_item_notification, null);
			}

			final ImageView iv_icon = ViewHolder.get(convertView, R.id.iv_icon);
			TextView illegal_time = ViewHolder.get(convertView,
					R.id.illegal_time);
			TextView illegal_address = ViewHolder.get(convertView,
					R.id.illegal_address);
			TextView illegal_content = ViewHolder.get(convertView,
					R.id.illegal_content);
			Button btn_delete = ViewHolder.get(convertView, R.id.btn_delete);

			final IllegalBean bean = mList.get(position);
			if (bean != null) {

				illegal_time.setText(bean.getTime());
				illegal_address.setText(bean.getAddress());
				illegal_content.setText(bean.getContent());
				btn_delete.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
					}
				});
				if (0 != convertView.getScrollX()) {
					convertView.scrollTo(0, 0);
				}
			}
			return convertView;
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.but_query:
			queryCouponListByOrderId(true);
			break;

		default:
			break;
		}

	}
}
