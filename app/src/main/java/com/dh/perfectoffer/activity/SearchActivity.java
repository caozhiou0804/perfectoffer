package com.dh.perfectoffer.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dh.perfectoffer.R;
import com.dh.perfectoffer.adapter.ArrayListAdapter;
import com.dh.perfectoffer.adapter.ViewHolder;
import com.dh.perfectoffer.constant.Constant;
import com.dh.perfectoffer.demo.VehicleActivity;
import com.dh.perfectoffer.dhutils.CommUtil;
import com.dh.perfectoffer.dhutils.ImageLoaderUtil;
import com.dh.perfectoffer.entity.HostBean;
import com.dh.perfectoffer.entity.PostBean;
import com.dh.perfectoffer.entity.SearchListBean;
import com.dh.perfectoffer.event.framework.alibaba.fastjson.JSON;
import com.dh.perfectoffer.event.framework.db.AfeiDb;
import com.dh.perfectoffer.event.framework.log.L;
import com.dh.perfectoffer.event.framework.net.exception.DataException;
import com.dh.perfectoffer.event.framework.net.fgview.Action;
import com.dh.perfectoffer.event.framework.net.fgview.BaseParser;
import com.dh.perfectoffer.event.framework.net.fgview.OnResponseListener;
import com.dh.perfectoffer.event.framework.net.fgview.Request;
import com.dh.perfectoffer.event.framework.net.fgview.Response;
import com.dh.perfectoffer.event.framework.net.fgview.Response.ErrorMsg;
import com.dh.perfectoffer.event.framework.util.CommonUtils;
import com.dh.perfectoffer.event.framework.util.StringUtils;
import com.dh.perfectoffer.handmark.pulltorefresh.library.PullToRefreshBase;
import com.dh.perfectoffer.handmark.pulltorefresh.library.PullToRefreshListView;
import com.dh.perfectoffer.xutils.sample.utils.ToastUtil;
import com.dh.perfectoffer.xutils.view.ViewUtils;
import com.dh.perfectoffer.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class SearchActivity extends VehicleActivity {
    private HotAdapter mMessageAdapter;
    private ArrayList<PostBean> mHrList;
    @ViewInject(R.id.pxlv_search)
    private PullToRefreshListView pxlv_search;// listview
    private LinearLayout linear_id;
    private LinearLayout ll_hot_search;
    private AfeiDb afeiDb;
    private LayoutInflater mInflater;
    private View mHeaderView;
    private LinearLayout ll_company;
    private RelativeLayout rl_job;
    private EditText et_search;
    private Button btn_search;
    private int mCurrentPage = 1;
    private static final int PAGE_SIZE = 20;
    private boolean isFirstQuery = true;
    private String searchStr = "";
    private RelativeLayout company_rl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_search);
	ViewUtils.inject(this);
	super.initTop();
	setTitle(CommUtil.getInstance().getStringFromXml(SearchActivity.this,
		R.string.search_title));
	initView();
    }

    private void initView() {
	if (mInflater == null) {
	    mInflater = (LayoutInflater) SearchActivity.this
		    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	mHeaderView = mInflater.inflate(R.layout.home_head, null);

	linear_id = (LinearLayout) mHeaderView.findViewById(R.id.ll_empty);
	linear_id.setVisibility(View.VISIBLE);
	ll_hot_search = (LinearLayout) mHeaderView
		.findViewById(R.id.ll_hot_search);
	ll_hot_search.setVisibility(View.GONE);
	ll_company = (LinearLayout) mHeaderView.findViewById(R.id.ll_company);
	rl_job = (RelativeLayout) mHeaderView.findViewById(R.id.rl_job);

	ll_company.setVisibility(View.GONE);
	rl_job.setVisibility(View.GONE);
	et_search = (EditText) mHeaderView.findViewById(R.id.et_search);
	company_rl = (RelativeLayout) mHeaderView.findViewById(R.id.company_rl);
	company_rl.setVisibility(View.GONE);
	et_search
		.setOnEditorActionListener(new TextView.OnEditorActionListener() {

		    public boolean onEditorAction(TextView v, int actionId,
			    KeyEvent event) {

			if (actionId == EditorInfo.IME_ACTION_SEARCH)

			{
			    L.e("yinzl", "搜索页面的搜索");
			    mCurrentPage = 1;
			    isFirstQuery = true;
			    searchStr = et_search.getText().toString().trim();
			    querySearchList(searchStr, mCurrentPage);
			    CommonUtils.hideSoftInput(v);
			    return true;

			}

			return false;

		    }

		});

	btn_search = (Button) mHeaderView.findViewById(R.id.btn_search);
	btn_search.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		mCurrentPage = 1;
		isFirstQuery = true;
		searchStr = et_search.getText().toString().trim();
		querySearchList(searchStr, mCurrentPage);
		CommonUtils.hideSoftInput(v);
	    }
	});
	pxlv_search.getRefreshableView().addHeaderView(mHeaderView);
	pxlv_search.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
	mHrList = new ArrayList<PostBean>();
	mMessageAdapter = new HotAdapter(SearchActivity.this);
	mMessageAdapter.setList(mHrList);
	pxlv_search.setAdapter(mMessageAdapter);
	pxlv_search
		.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
		    @Override
		    public void onPullDownToRefresh(
			    PullToRefreshBase<ListView> refreshView) {
		    }

		    @Override
		    public void onPullUpToRefresh(
			    PullToRefreshBase<ListView> refreshView) {
			mCurrentPage += 1;
			isFirstQuery = false;
			querySearchList(searchStr, mCurrentPage);
		    }
		});
	searchStr = getIntent().getStringExtra("searcheStr");
	if (!TextUtils.isEmpty(searchStr)) {
	    L.e("yinzl", "搜索文字" + searchStr);
	    et_search.setText(searchStr);
	}
	if (!TextUtils.isEmpty(getIntent().getStringExtra("isLastPage"))
		&& getIntent().getStringExtra("isLastPage").equals("1")) {
	    pxlv_search.setPullLoadEnabled(false);
	} else
	    pxlv_search.setPullLoadEnabled(true);

	ArrayList<HostBean> hostBeanList = (ArrayList<HostBean>) getIntent()
		.getSerializableExtra("hotList");
	if (null != hostBeanList && hostBeanList.size() > 0) {
	    initHotSearch(hostBeanList);
	}
	ArrayList<PostBean> postBeanList = (ArrayList<PostBean>) getIntent()
		.getSerializableExtra("searchList");
	if (null != postBeanList && postBeanList.size() > 0) {
	    L.e("yinzl", "搜索数据：" + postBeanList.size());
	    mHrList.clear();
	    mHrList.addAll(postBeanList);
	    mMessageAdapter.notifyDataSetChanged();
	}
	setEmptyView(mHrList);
    }

    private void querySearchList(String str_search, int currentPage) {
	if (currentPage == 1)
	    mHrList.clear();
	HashMap<String, String> pram = new HashMap<String, String>();
	if (!TextUtils.isEmpty(str_search))
	    pram.put("search_name",
		    CommUtil.getInstance().getNomalStr(str_search));
	pram.put("page", currentPage + "");
	pram.put("size", PAGE_SIZE + "");
	// 请求parser
	Request<SearchListBean> req = new Request<SearchListBean>();
	req.setRequestMethod(Request.M_POST);
	req.setBaseParser(new BaseParser<SearchListBean>() {
	    @Override
	    public SearchListBean parseResDate(String resBody)
		    throws DataException {
		if (resBody != null && !resBody.equals("")) {
		    return JSON.parseObject(resBody, SearchListBean.class);
		}
		return null;
	    }
	});

	req.setRequestParams(pram);
	req.setUrl(Constant.URLIP + "hr/SearchServlet");

	Action action = new Action(SearchActivity.this);
	action.setDefaultLoadingTipOpen(true);
	action.setShowErrorDialog(true);
	action.execute(req, new OnResponseListener<SearchListBean>() {
	    @Override
	    public void onResponseFinished(Request<SearchListBean> request,
		    Response<SearchListBean> response) {
		SearchListBean bean = new SearchListBean();
		bean = response.getT();
		if (null != bean && bean.getSuccess().equals("1")) {
		    // mHrList.clear();
		    // mHrList.addAll(bean);
		    if (null != bean.getPostListSearch()
			    && bean.getPostListSearch().size() > 0) {
			if (isFirstQuery) {
			    mHrList.clear();
			    L.e("yinzl", "刷新");
			}
			mHrList.addAll(bean.getPostListSearch());
			L.e("yinzl", "加载更多");
			mMessageAdapter.notifyDataSetChanged();
		    } else {
			ToastUtil.showShort(
				SearchActivity.this,
				CommUtil.getInstance().getStringFromXml(
					SearchActivity.this,
					R.string.search_null));
		    }
		} else {
		    ToastUtil.showShort(
			    SearchActivity.this,
			    CommUtil.getInstance().getStringFromXml(
				    SearchActivity.this, R.string.search_null));
		}
		pxlv_search.onRefreshComplete();
		if (null != bean && !TextUtils.isEmpty(bean.getLastPage())
			&& bean.getLastPage().equals("1")) {// 暂无更多
		    L.e("yinzl", "不可上拉");
		    pxlv_search.setPullLoadEnabled(false);
		} else {
		    L.e("yinzl", "可上拉");
		    pxlv_search.setPullLoadEnabled(true);
		}
		mMessageAdapter.notifyDataSetChanged();
		setEmptyView(mHrList);
	    }

	    // initDate(bean);

	    @Override
	    public void onResponseDataError(Request<SearchListBean> equest) {
		// ToastUtil.showLong(SearchActivity.this, "请求失败");
		pxlv_search.onRefreshComplete();
		mMessageAdapter.notifyDataSetChanged();
		setEmptyView(mHrList);
	    }

	    @Override
	    public void onResponseConnectionError(
		    Request<SearchListBean> request, int statusCode) {
		// ToastUtil.showLong(SearchActivity.this, "请求失败");
		pxlv_search.onRefreshComplete();
		mMessageAdapter.notifyDataSetChanged();
		setEmptyView(mHrList);
	    }

	    @Override
	    public void onResponseFzzError(Request<SearchListBean> request,
		    ErrorMsg errorMsg) {
		// ToastUtil.showLong(SearchActivity.this, "请求失败");
		pxlv_search.onRefreshComplete();
		mMessageAdapter.notifyDataSetChanged();
		setEmptyView(mHrList);
	    }

	});
    }

    // private void setEmptyView() {
    // L.e("yinzl", "没有数据");
    // linear_id.setVisibility(View.VISIBLE);
    // View view = View.inflate(SearchActivity.this, R.layout.empty, null);
    // ((TextView) view.findViewById(R.id.tv_no)).setText("暂无数据123");
    // }

    private void setEmptyView(List<PostBean> list) {
	if (null == list || list.size() <= 0) {
	    linear_id.setVisibility(View.VISIBLE);
	    View view = View.inflate(SearchActivity.this, R.layout.empty, null);
	    ((TextView) view.findViewById(R.id.tv_no)).setText(CommUtil
		    .getInstance().getStringFromXml(SearchActivity.this,
			    R.string.search_null2));
	} else {
	    linear_id.setVisibility(View.GONE);
	}
    }

    private void initHotSearch(final ArrayList<HostBean> list) {
	ll_hot_search.removeAllViews();
	TextView tvTitle = new TextView(SearchActivity.this);
	tvTitle.setText(CommUtil.getInstance().getStringFromXml(
		SearchActivity.this, R.string.search_hot));
	tvTitle.setTextColor(Color.parseColor("#cacaca"));
	ll_hot_search.addView(tvTitle);

	ArrayList<HostBean> lsitTemp = new ArrayList<HostBean>();
	if (list.size() > 3) {
	    for (int i = 0; i < 3; i++) {
		lsitTemp.add(list.get(i));
	    }
	} else {
	    lsitTemp.clear();
	    lsitTemp.addAll(list);
	}
	for (int i = 0; i < lsitTemp.size(); i++) {
	    TextView tv = new TextView(SearchActivity.this);
	    if (i != (lsitTemp.size() - 1)) {
		tv.setText(lsitTemp.get(i).getPost_name() + "、");
	    } else
		tv.setText(lsitTemp.get(i).getPost_name());
	    tv.setTextColor(Color.parseColor("#cacaca"));
	    setTextViewOnClick(tv);
	    ll_hot_search.addView(tv);
	}
    }

    private void setTextViewOnClick(final TextView tv) {
	if (!TextUtils.isEmpty(tv.getText().toString().trim())) {
	    tv.setOnClickListener(new OnClickListener() {

		@Override
		public void onClick(View v) {
		    mCurrentPage = 1;
		    isFirstQuery = true;
		    searchStr = tv.getText().toString().trim().replace("、", "");
		    et_search.setText(searchStr);
		    querySearchList(searchStr, mCurrentPage);
		}
	    });
	}
    }

    class HotAdapter extends ArrayListAdapter<PostBean> {

	public HotAdapter(Activity context) {
	    super(context);
	}

	public View getView(final int position, View convertView,
		ViewGroup parent) {
	    if (convertView == null) {
		convertView = LayoutInflater.from(mContext).inflate(
			R.layout.item_job, null);
	    }
	    final PostBean bean = mList.get(position);
	    convertView.setOnClickListener(new OnClickListener() {

		@Override
		public void onClick(View v) {
		    // TODO Auto-generated method stub
		    Intent intent = new Intent(SearchActivity.this,
			    HistoryResumeActivity.class);

		    L.e("yinzl", "搜索职位：" + bean.toString());
		    L.e("yinzl", "搜索postid：" + bean.getPost_id());
		    intent.putExtra("postId", bean.getPost_id());
		    launch(intent);
		}
	    });
	    TextView tv_job_name = ViewHolder
		    .get(convertView, R.id.tv_job_name);
	    ImageView iv_hot_icon = ViewHolder.get(convertView,
		    R.id.iv_hot_icon);
	    TextView tv_job_claim = ViewHolder.get(convertView,
		    R.id.tv_job_claim);
	    TextView tv_job_bounty = ViewHolder.get(convertView,
		    R.id.tv_job_bounty);

	    TextView tv_job_salary = ViewHolder.get(convertView,
		    R.id.tv_job_salary);
	    // ImageView iv_company_icon = ViewHolder.get(convertView,
	    // R.id.iv_company_icon);
	    TextView tv_company_name = ViewHolder.get(convertView,
		    R.id.tv_company_name);

	    if (bean != null) {
		// setImageViewByView(iv_company_icon,
		// R.drawable.icon_temp_company,
		// Constant.URLIPPIC + bean.getSub_icon());
		tv_job_name.setText(bean.getPost_name());
		tv_company_name.setText(bean.getSub_name());
		tv_job_claim.setText(CommUtil.getInstance().getStringFromXml(
			SearchActivity.this, R.string.com_requre)
			+ bean.getReward_claim());
		if (!TextUtils.isEmpty(bean.getBounty()))
		    tv_job_bounty.setText(CommUtil.getInstance()
			    .getStringFromXml(SearchActivity.this,
				    R.string.com_bounty)
			    + bean.getBounty());
		else
		    tv_job_bounty.setText(CommUtil.getInstance()
			    .getStringFromXml(SearchActivity.this,
				    R.string.com_bounty) + 0);
		// tv_job_salary.setText(CommUtil.getInstance().getStringFromXml(
		// SearchActivity.this, R.string.com_salary)
		// + bean.getReward_salary());
		tv_job_salary.setText(bean.getReward_salary());
		// if (bean.getIs_hot().equals("1")) {
		// iv_hot_icon.setVisibility(View.VISIBLE);
		// } else
		// iv_hot_icon.setVisibility(View.INVISIBLE);
	    }
	    return convertView;
	}

    }

    private void setImageViewByView(final ImageView view, int DefaultIconId,
	    String IconUrl) {
	if (!StringUtils.isBlank(IconUrl)) {
	    ImageLoaderUtil.getInstance().displayImage(IconUrl, view,
		    DefaultIconId); // 不使用原图
	} else {
	    view.setImageResource(DefaultIconId);
	}
    }
}
