package com.dh.perfectoffer.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.dh.perfectoffer.R;
import com.dh.perfectoffer.VehicleApp;
import com.dh.perfectoffer.activity.CompanyActivity;
import com.dh.perfectoffer.activity.HistoryResumeActivity;
import com.dh.perfectoffer.activity.SearchActivity;
import com.dh.perfectoffer.adapter.ArrayListAdapter;
import com.dh.perfectoffer.adapter.ViewHolder;
import com.dh.perfectoffer.constant.Constant;
import com.dh.perfectoffer.demo.FragmentTabActivity2;
import com.dh.perfectoffer.demo.VehicleFragment;
import com.dh.perfectoffer.dhutils.CommUtil;
import com.dh.perfectoffer.dhutils.ImageLoaderUtil;
import com.dh.perfectoffer.entity.ComBean;
import com.dh.perfectoffer.entity.CompanyBean;
import com.dh.perfectoffer.entity.HostBean;
import com.dh.perfectoffer.entity.PostBean;
import com.dh.perfectoffer.entity.SearchListBean;
import com.dh.perfectoffer.entity.homeBean;
import com.dh.perfectoffer.event.framework.alibaba.fastjson.JSON;
import com.dh.perfectoffer.event.framework.db.AfeiDb;
import com.dh.perfectoffer.event.framework.log.L;
import com.dh.perfectoffer.event.framework.net.exception.DataException;
import com.dh.perfectoffer.event.framework.net.fgview.Action;
import com.dh.perfectoffer.event.framework.net.fgview.BaseParser;
import com.dh.perfectoffer.event.framework.net.fgview.OnResponseListener;
import com.dh.perfectoffer.event.framework.net.fgview.Request;
import com.dh.perfectoffer.event.framework.net.fgview.Response;
import com.dh.perfectoffer.event.framework.util.CommonUtils;
import com.dh.perfectoffer.event.framework.util.StringUtils;
import com.dh.perfectoffer.handmark.pulltorefresh.library.PullToRefreshBase;
import com.dh.perfectoffer.handmark.pulltorefresh.library.PullToRefreshListView;
import com.dh.perfectoffer.xutils.sample.utils.Preference;
import com.dh.perfectoffer.xutils.sample.utils.ToastUtil;
import com.dh.perfectoffer.xutils.view.ViewUtils;
import com.dh.perfectoffer.xutils.view.annotation.ViewInject;
import com.dh.perfectoffer.event.framework.net.fgview.Response.ErrorMsg;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * @author 超级小志
 * 
 */
public class HomeFragment extends VehicleFragment implements OnClickListener,
	OnItemClickListener {

    private HotAdapter mMessageAdapter;
    private ArrayList<HostBean> mHotList;
    private ArrayList<PostBean> mHrList;
    private ArrayList<ComBean> mComList;

    private Context mAppContext;

    // @ViewInject(R.id.pxlv_person)
    // private PullToRefreshScrollView pslv_message;
    @ViewInject(R.id.pxlv_person)
    private PullToRefreshListView pslv_message;// listview
    private LinearLayout linear_id;
    private LinearLayout ll_hot_search;
    private AfeiDb afeiDb;
    private LayoutInflater mInflater;
    private View mHeaderView;
    private TextView tv_company_name1;
    private TextView tv_company_name2;
    private TextView tv_company_name3;
    private TextView tv_company_name4;
    private ImageView iv_company1;
    private ImageView iv_company2;
    private ImageView iv_company3;
    private ImageView iv_company4;
    private LinearLayout ll_company1;
    private LinearLayout ll_company2;
    private LinearLayout ll_company3;
    private LinearLayout ll_company4;
    private LinearLayout ll_company;
    private EditText et_search;
    private Button btn_search;
    private boolean isFirstSearch = true;

    private TextView more_tv;

    @SuppressLint("InflateParams")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {
	View view = inflater.inflate(R.layout.home, container, false);
	ViewUtils.inject(this, view);
	mAppContext = inflater.getContext().getApplicationContext();
	// 存入用户名及密码
	afeiDb = VehicleApp.getInstance().getAfeiDb();
	if (null == afeiDb) {
	    afeiDb = AfeiDb.create(getActivity(), Constant.DB_NAME, true);
	}
	initView();

	// if (!TextUtils.isEmpty(Preference.getString(Constant.PRE_OIL_NAME)))
	// {
	// List<CompanyBean> bean = JSON.parseArray(
	// Preference.getString(Constant.PRE_COMPANY_NAME),
	// CompanyBean.class);
	// initDate(bean);
	// }
	return view;
    }

    @Override
    public void onResume() {
	// TODO Auto-generated method stub
	super.onResume();
	et_search.setText("");
    }

    private void initDate(List<CompanyBean> bean) {
	afeiDb.dropTableIfTableExist(CompanyBean.class);
	for (int i = 0; i < bean.size(); i++) {
	    afeiDb.save(bean.get(i));
	}

	List<CompanyBean> companyList = new ArrayList<CompanyBean>();
	companyList = afeiDb.findAll(CompanyBean.class);
	if (companyList.size() > 0) {
	    for (int i = 0; i < companyList.size(); i++) {
	    }
	}

    }

    private void saveCompany(ArrayList<ComBean> list) {
	afeiDb.dropTableIfTableExist(ComBean.class);
	for (int i = 0; i < list.size(); i++) {
	    afeiDb.save(list.get(i));
	}
    }

    private void initView() {
	if (mInflater == null) {
	    mInflater = (LayoutInflater) getActivity().getSystemService(
		    Context.LAYOUT_INFLATER_SERVICE);
	}
	mHeaderView = mInflater.inflate(R.layout.home_head, null);

	linear_id = (LinearLayout) mHeaderView.findViewById(R.id.ll_empty);
	linear_id.setVisibility(View.VISIBLE);
	ll_hot_search = (LinearLayout) mHeaderView
		.findViewById(R.id.ll_hot_search);
	ll_hot_search.setVisibility(View.GONE);
	ll_company = (LinearLayout) mHeaderView.findViewById(R.id.ll_company);
	ll_company.setVisibility(View.GONE);
	ll_company1 = (LinearLayout) mHeaderView.findViewById(R.id.ll_company1);
	ll_company2 = (LinearLayout) mHeaderView.findViewById(R.id.ll_company2);
	ll_company3 = (LinearLayout) mHeaderView.findViewById(R.id.ll_company3);
	ll_company4 = (LinearLayout) mHeaderView.findViewById(R.id.ll_company4);
	tv_company_name1 = (TextView) mHeaderView
		.findViewById(R.id.tv_company_name1);
	tv_company_name2 = (TextView) mHeaderView
		.findViewById(R.id.tv_company_name2);
	tv_company_name3 = (TextView) mHeaderView
		.findViewById(R.id.tv_company_name3);
	tv_company_name4 = (TextView) mHeaderView
		.findViewById(R.id.tv_company_name4);
	iv_company1 = (ImageView) mHeaderView.findViewById(R.id.iv_company1);
	iv_company2 = (ImageView) mHeaderView.findViewById(R.id.iv_company2);
	iv_company3 = (ImageView) mHeaderView.findViewById(R.id.iv_company3);
	iv_company4 = (ImageView) mHeaderView.findViewById(R.id.iv_company4);
	et_search = (EditText) mHeaderView.findViewById(R.id.et_search);
	more_tv = (TextView) mHeaderView.findViewById(R.id.more_tv);

	et_search.setOnEditorActionListener(new OnEditorActionListener() {

	    public boolean onEditorAction(TextView v, int actionId,
		    KeyEvent event) {
		if (actionId == EditorInfo.IME_ACTION_SEARCH)

		{
		    L.e("yinzl", "home页面的搜索");
		    querySearchList(et_search.getText().toString().trim());
		    CommonUtils.hideSoftInput(v);
		    isFirstSearch = false;
		    return true;
		}

		return false;

	    }

	});

	btn_search = (Button) mHeaderView.findViewById(R.id.btn_search);
	btn_search.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		// TODO Auto-generated method stub
		querySearchList(et_search.getText().toString().trim());
		CommonUtils.hideSoftInput(v);
	    }
	});
	pslv_message.getRefreshableView().addHeaderView(mHeaderView);
	pslv_message.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
	mHrList = new ArrayList<PostBean>();
	mHotList = new ArrayList<HostBean>();
	mComList = new ArrayList<ComBean>();
	mMessageAdapter = new HotAdapter(getActivity());
	mMessageAdapter.setList(mHrList);
	pslv_message.setAdapter(mMessageAdapter);
	if (!TextUtils.isEmpty(Preference.getString(Constant.PRE_COMPANY_NAME))) {

	    try {
		homeBean bean = JSON.parseObject(
			Preference.getString(Constant.PRE_COMPANY_NAME),
			homeBean.class);
		if (null != bean) {
		    if (null != bean.getPostList()
			    && bean.getPostList().size() > 0) {
			mHrList.clear();
			mHrList.addAll(bean.getPostList());
			mMessageAdapter.notifyDataSetChanged();
		    }
		    if (null != bean.getComList()
			    && bean.getComList().size() > 0) {
			initCompany(bean.getComList());
		    } else
			ll_company.setVisibility(View.GONE);
		    if (null != bean.getHotList()
			    && bean.getHotList().size() > 0) {
			// initHotSearch(bean.getHotList());
		    }
		    setEmptyView(mHrList);
		}
	    } catch (Exception e) {
		// TODO: handle exception
	    }

	}
	pslv_message
		.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
		    @Override
		    public void onPullDownToRefresh(
			    PullToRefreshBase<ListView> refreshView) {
			// queryHomeList();
			queryCompanyList();
		    }

		    @Override
		    public void onPullUpToRefresh(
			    PullToRefreshBase<ListView> refreshView) {
		    }
		});
	pslv_message.setRefreshing();
	pslv_message.setOnItemClickListener(this);
	// pslv_message.setRefreshing();
    }

    private void setTextViewOnClick(final TextView tv) {
	if (!TextUtils.isEmpty(tv.getText().toString().trim())) {
	    tv.setOnClickListener(new OnClickListener() {

		@Override
		public void onClick(View v) {
		    et_search.setText(tv.getText().toString().trim()
			    .replace("、", ""));
		    querySearchList(tv.getText().toString().trim()
			    .replace("、", ""));
		    CommonUtils.hideSoftInput(v);
		}
	    });
	}
    }

    private void queryCompanyList() {

	// 请求parser
	Request<homeBean> req = new Request<homeBean>();
	req.setRequestMethod(Request.M_POST);
	req.setBaseParser(new BaseParser<homeBean>() {
	    @Override
	    public homeBean parseResDate(String resBody) throws DataException {
		if (resBody != null && !resBody.equals("")) {
		    Preference.putString(Constant.PRE_COMPANY_NAME, resBody);
		    return JSON.parseObject(resBody, homeBean.class);
		}
		return null;
	    }
	});
	req.setUrl(Constant.URLIP + "hr/ListServlet");

	Action action = new Action(getActivity());
	action.setDefaultLoadingTipOpen(false);
	action.setShowErrorDialog(true);
	action.execute(req, new OnResponseListener<homeBean>() {
	    @Override
	    public void onResponseFinished(Request<homeBean> request,
		    Response<homeBean> response) {
		homeBean bean = new homeBean();
		bean = response.getT();
		if (null != bean) {
		    if (null != bean.getPostList()
			    && bean.getPostList().size() > 0) {
			mHrList.clear();
			mHrList.addAll(bean.getPostList());
			mMessageAdapter.notifyDataSetChanged();
		    }
		    if (null != bean.getComList()
			    && bean.getComList().size() > 0) {
			initCompany(bean.getComList());
		    } else
			ll_company.setVisibility(View.GONE);
		    if (null != bean.getHotList()
			    && bean.getHotList().size() > 0) {
			// initHotSearch(bean.getHotList());
		    }

		}
		pslv_message.onRefreshComplete();
		setEmptyView(mHrList);
		// initDate(bean);
	    }

	    @Override
	    public void onResponseDataError(Request<homeBean> equest) {
		// ToastUtil.showLong(getActivity(), "请求失败");
		pslv_message.onRefreshComplete();
		setEmptyView(mHrList);
	    }

	    @Override
	    public void onResponseConnectionError(Request<homeBean> request,
		    int statusCode) {
		// ToastUtil.showLong(getActivity(), "请求失败");
		pslv_message.onRefreshComplete();
		setEmptyView(mHrList);
	    }

	    @Override
	    public void onResponseFzzError(Request<homeBean> request,
		    ErrorMsg errorMsg) {
		// ToastUtil.showLong(getActivity(), "请求失败");
		pslv_message.onRefreshComplete();
		setEmptyView(mHrList);
	    }
	});
    }

    private void initCompany(final ArrayList<ComBean> list) {
	saveCompany(list);
	if (list.size() > 3) {

	    ll_company.setVisibility(View.VISIBLE);
	    setImageViewByView(iv_company1, R.drawable.icon_temp_company,
		    Constant.URLIPPIC + list.get(0).getSub_icon());
	    setImageViewByView(iv_company2, R.drawable.icon_temp_company,
		    Constant.URLIPPIC + list.get(1).getSub_icon());
	    setImageViewByView(iv_company3, R.drawable.icon_temp_company,
		    Constant.URLIPPIC + list.get(2).getSub_icon());
	    setImageViewByView(iv_company4, R.drawable.icon_temp_company,
		    Constant.URLIPPIC + list.get(3).getSub_icon());
	    tv_company_name1.setText(list.get(0).getSub_name());
	    tv_company_name2.setText(list.get(1).getSub_name());
	    tv_company_name3.setText(list.get(2).getSub_name());
	    tv_company_name4.setText(list.get(3).getSub_name());

	    //
	    // setImageViewByView(iv_company1, R.drawable.icon_temp_company,
	    // Constant.URLIPPIC + list.get(0).getSub_icon());
	    // setImageViewByView(iv_company2, R.drawable.icon_temp_company,
	    // Constant.URLIPPIC + list.get(1).getSub_icon());
	    // setImageViewByView(iv_company3, R.drawable.icon_temp_company,
	    // Constant.URLIPPIC + list.get(2).getSub_icon());

	    ll_company1.setOnClickListener(new OnClickListener() {

		@Override
		public void onClick(View v) {
		    Intent intent = new Intent(getActivity(),
			    CompanyActivity.class);
		    intent.putExtra("companyid", list.get(0).getSub_id());
		    intent.putExtra("companycontent", list.get(0)
			    .getSub_details());
		    intent.putExtra("companyname", list.get(0).getSub_name());
		    intent.putExtra("companyicon", list.get(0).getSub_icon());
		    launch(intent);
		}
	    });
	    ll_company2.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
		    Intent intent = new Intent(getActivity(),
			    CompanyActivity.class);
		    intent.putExtra("companyid", list.get(1).getSub_id());
		    intent.putExtra("companycontent", list.get(1)
			    .getSub_details());
		    intent.putExtra("companyname", list.get(1).getSub_name());
		    intent.putExtra("companyicon", list.get(1).getSub_icon());
		    launch(intent);
		}
	    });
	    ll_company3.setOnClickListener(new OnClickListener() {

		@Override
		public void onClick(View v) {
		    Intent intent = new Intent(getActivity(),
			    CompanyActivity.class);
		    intent.putExtra("companyid", list.get(2).getSub_id());
		    intent.putExtra("companycontent", list.get(2)
			    .getSub_details());
		    intent.putExtra("companyname", list.get(2).getSub_name());
		    intent.putExtra("companyicon", list.get(2).getSub_icon());
		    launch(intent);

		}
	    });

	    ll_company4.setOnClickListener(new OnClickListener() {

		@Override
		public void onClick(View v) {
		    Intent intent = new Intent(getActivity(),
			    CompanyActivity.class);
		    intent.putExtra("companyid", list.get(3).getSub_id());
		    intent.putExtra("companycontent", list.get(3)
			    .getSub_details());
		    intent.putExtra("companyname", list.get(3).getSub_name());
		    intent.putExtra("companyicon", list.get(3).getSub_icon());
		    launch(intent);

		}
	    });
	    more_tv.setOnClickListener(new OnClickListener() {

		@Override
		public void onClick(View v) {
		    // TODO Auto-generated method stub
		    FragmentTabActivity2.getInstance().toNextTab(1);// 跳转公司页面
		    CommonUtils.hideSoftInput(v);
		}
	    });

	    // tv_company_name1.setText(list.get(0).getSub_name());
	    // tv_company_name2.setText(list.get(1).getSub_name());
	    // tv_company_name3.setText(list.get(2).getSub_name());
	    //
	    // }
	} else {
	    ll_company.setVisibility(View.GONE);
	}

	List<ComBean> companyList = new ArrayList<ComBean>();
	companyList = afeiDb.findAll(ComBean.class);
	if (null != companyList && companyList.size() > 0) {
	    L.e("yinzl", "公司个数：" + companyList.size());
	    FragmentTabActivity2.getInstance().refreshTabFragment();
	}
    }

    // private void initHotSearch(final ArrayList<HostBean> list) {
    // ll_hot_search.removeAllViews();
    // TextView tvTitle = new TextView(getActivity());
    // tvTitle.setText(CommUtil.getInstance().getStringFromXml(getActivity(),
    // R.string.search_hot));
    // tvTitle.setTextColor(Color.parseColor("#cacaca"));
    // ll_hot_search.addView(tvTitle);
    //
    // ArrayList<HostBean> lsitTemp = new ArrayList<HostBean>();
    // if (list.size() > 3) {
    // for (int i = 0; i < 3; i++) {
    // lsitTemp.add(list.get(i));
    // }
    // } else {
    // lsitTemp.clear();
    // lsitTemp.addAll(list);
    // }
    // mHotList.clear();
    // mHotList.addAll(lsitTemp);
    // for (int i = 0; i < lsitTemp.size(); i++) {
    // TextView tv = new TextView(getActivity());
    // if (i != (lsitTemp.size() - 1)) {
    // tv.setText(lsitTemp.get(i).getPost_name() + "、");
    // } else
    // tv.setText(lsitTemp.get(i).getPost_name());
    // tv.setTextColor(Color.parseColor("#cacaca"));
    // setTextViewOnClick(tv);
    // ll_hot_search.addView(tv);
    // }
    // }

    private void setEmptyView(List<PostBean> list) {
	if (null == list || list.size() <= 0) {
	    linear_id.setVisibility(View.VISIBLE);
	    View view = View.inflate(getActivity(), R.layout.empty, null);
	    ((TextView) view.findViewById(R.id.tv_no)).setText(CommUtil
		    .getInstance().getStringFromXml(getActivity(),
			    R.string.search_null2));
	} else {
	    linear_id.setVisibility(View.GONE);
	}
    }

    // private void setEmptyView() {
    // L.e("yinzl", "没有数据");
    // linear_id.setVisibility(View.VISIBLE);
    // View view = View.inflate(getActivity(), R.layout.empty, null);
    // ((TextView) view.findViewById(R.id.tv_no)).setText("暂无数据123");
    // }

    public void refreshData(boolean showDialog) {
    }

    @Override
    public void onClick(View v) {
	switch (v.getId()) {
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
		    Intent intent = new Intent(getActivity(),
			    HistoryResumeActivity.class);
		    intent.putExtra("postId", bean.getRew_id());
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
			getActivity(), R.string.com_requre)
			+ bean.getReward_claim());
		if (!TextUtils.isEmpty(bean.getBounty()))
		    tv_job_bounty.setText(CommUtil.getInstance()
			    .getStringFromXml(getActivity(),
				    R.string.com_bounty)
			    + bean.getBounty());
		else
		    tv_job_bounty.setText(CommUtil.getInstance()
			    .getStringFromXml(getActivity(),
				    R.string.com_bounty) + 0);
		// tv_job_salary.setText(CommUtil.getInstance().getStringFromXml(
		// getActivity(), R.string.com_salary)
		// + bean.getReward_salary());
		if (!TextUtils.isEmpty(bean.getReward_salary())) {
		    if (!"面议".equals(bean.getReward_salary())) {
			tv_job_salary.setText(bean.getReward_salary() + "/月");
		    } else {
			tv_job_salary.setText(bean.getReward_salary());
		    }
		}
		// if (bean.getIs_hot().equals("1")) {
		// iv_hot_icon.setVisibility(View.VISIBLE);
		// } else
		// iv_hot_icon.setVisibility(View.INVISIBLE);
		// tv_company_name.setText(bean.getReward_salary());
	    }
	    return convertView;
	}
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
	    long id) {
	// TextView v = (TextView) view.findViewById(R.id.rew_id);// 获取绑定悬赏职位ID
	// if (null != v) {
	// Intent intent = new Intent(getActivity(), OfferRewardActivity.class);
	// intent.putExtra("rew_id", v.getText().toString());
	// startActivity(intent);
	// }

    }

    private void querySearchList(final String str_search) {
	HashMap<String, String> pram = new HashMap<String, String>();
	if (!TextUtils.isEmpty(str_search))
	    pram.put("search_name",
		    CommUtil.getInstance().getNomalStr(str_search));
	pram.put("page", "1");
	pram.put("size", "20");
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

	Action action = new Action(getActivity());
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
			ArrayList<PostBean> lists = new ArrayList<PostBean>();
			lists = bean.getPostListSearch();
			Intent intent = new Intent(getActivity(),
				SearchActivity.class);
			intent.putExtra("searchList",
				(ArrayList<PostBean>) lists);
			intent.putExtra("hotList",
				(ArrayList<HostBean>) mHotList);
			intent.putExtra("searcheStr", str_search);
			if (!TextUtils.isEmpty(bean.getLastPage()))
			    intent.putExtra("isLastPage", bean.getLastPage());
			launch(intent);
		    } else {
			ToastUtil.showShort(
				getActivity(),
				CommUtil.getInstance().getStringFromXml(
					getActivity(), R.string.search_null));
		    }
		} else {
		    ToastUtil.showShort(
			    getActivity(),
			    CommUtil.getInstance().getStringFromXml(
				    getActivity(), R.string.search_null));
		}
	    }

	    // initDate(bean);

	    @Override
	    public void onResponseDataError(Request<SearchListBean> equest) {
		// ToastUtil.showLong(getActivity(), "请求失败");
	    }

	    @Override
	    public void onResponseConnectionError(
		    Request<SearchListBean> request, int statusCode) {
		// ToastUtil.showLong(getActivity(), "请求失败");
	    }

	    @Override
	    public void onResponseFzzError(Request<SearchListBean> request,
		    ErrorMsg errorMsg) {
		// ToastUtil.showLong(getActivity(), "请求失败");
	    }

	});
    }

    private void setImageViewByView(final ImageView view, int DefaultIconId,
	    String IconUrl) {
	if (!StringUtils.isBlank(IconUrl)) {
	    ImageLoaderUtil.getInstance().displayImage(IconUrl, view,
		    DefaultIconId); // 不使用原图
	    // ImageLoaderUtil.getInstance().loadImage(IconUrl,
	    // new ImageLoaderCompleteListener() {
	    //
	    // @Override
	    // public void onCompleteImageLoader(Bitmap bitmap) {
	    // // TODO Auto-generated method stub
	    // view.setBackgroundDrawable(new BitmapDrawable(
	    // bitmap));
	    // }
	    // });
	} else {
	    view.setImageResource(DefaultIconId);
	    // view.setBackgroundResource(DefaultIconId);
	}
    }
}
