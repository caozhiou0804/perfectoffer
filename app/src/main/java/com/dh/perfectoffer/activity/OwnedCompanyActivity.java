package com.dh.perfectoffer.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dh.perfectoffer.R;
import com.dh.perfectoffer.VehicleApp;
import com.dh.perfectoffer.adapter.ArrayListAdapter;
import com.dh.perfectoffer.adapter.ViewHolder;
import com.dh.perfectoffer.constant.Constant;
import com.dh.perfectoffer.demo.VehicleActivity;
import com.dh.perfectoffer.dhutils.ImageLoaderUtil;
import com.dh.perfectoffer.entity.ComBean;
import com.dh.perfectoffer.entity.CompanyListBean;
import com.dh.perfectoffer.event.EventBus;
import com.dh.perfectoffer.event.OwnedCompanyEvent;
import com.dh.perfectoffer.event.framework.db.AfeiDb;
import com.dh.perfectoffer.event.framework.util.StringUtils;
import com.dh.perfectoffer.xutils.view.ViewUtils;
import com.dh.perfectoffer.xutils.view.annotation.ViewInject;
import com.dh.perfectoffer.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;
import java.util.List;


public class OwnedCompanyActivity extends VehicleActivity {

    @ViewInject(R.id.company_list)
    private ListView company_list;
    @ViewInject(R.id.ll_empty)
    private View ll_empty;

    @ViewInject(R.id.btn_title_btn_back_layout)
    private ImageView btn_title_btn_back_layout;

    private OwneCompanyAdapter mAdapter;
    private ArrayList<CompanyListBean> mCompanyList;
    private CompanyListBean listBean;
    private AfeiDb afeiDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_owned_company);
	ViewUtils.inject(this);
	afeiDb = VehicleApp.getInstance().getAfeiDb();
	if (null == afeiDb) {
	    afeiDb = AfeiDb.create(OwnedCompanyActivity.this);
	}
	initView();
    }

    private void initView() {
	mCompanyList = new ArrayList<CompanyListBean>();
	mAdapter = new OwneCompanyAdapter(OwnedCompanyActivity.this);
	mAdapter.setList(mCompanyList);
	company_list.setAdapter(mAdapter);
	initData();
	company_list.setOnItemClickListener(new OnItemClickListener() {

	    @Override
	    public void onItemClick(AdapterView<?> parent, View view,
		    int position, long id) {
		// TODO Auto-generated method stub
		OwnedCompanyEvent ownedCompanyEvent = new OwnedCompanyEvent();
		ownedCompanyEvent.setBean(mCompanyList.get(position));
		EventBus.getDefault().post(ownedCompanyEvent);
		OwnedCompanyActivity.this.finish();
	    }
	});
    }

    @OnClick(R.id.btn_title_btn_back_layout)
    public void onclickback(View view) {
	OwnedCompanyActivity.this.finish();
    }

    private void initData() {
	List<ComBean> mList = new ArrayList<ComBean>();
	mList = afeiDb.findAll(ComBean.class);
	if (mList.size() > 0) {
	    mCompanyList.clear();
	    for (int i = 0; i < mList.size(); i++) {
		listBean = new CompanyListBean();
		listBean.setId(mList.get(i).getSub_id());
		listBean.setName(mList.get(i).getSub_name());
		listBean.setIcon(mList.get(i).getSub_icon());

		listBean.setNumber(mList.get(i).getSub_num());
		listBean.setContent(mList.get(i).getSub_details());
		mCompanyList.add(listBean);
	    }
	    mAdapter.notifyDataSetChanged();
	    ll_empty.setVisibility(View.GONE);
	    company_list.setVisibility(View.VISIBLE);
	} else {
	    ll_empty.setVisibility(View.VISIBLE);
	    company_list.setVisibility(View.GONE);
	}
    }

    class OwneCompanyAdapter extends ArrayListAdapter<CompanyListBean> {

	public OwneCompanyAdapter(Activity context) {
	    super(context);
	    // TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	    if (null == convertView) {
		convertView = LayoutInflater.from(mContext).inflate(
			R.layout.item_owned_company, null);

	    }

	    TextView post_name = ViewHolder.get(convertView, R.id.company_name);
	    ImageView iv_company = ViewHolder.get(convertView, R.id.iv_company);
	    final CompanyListBean bean = mList.get(position);
	    if (null != bean) {
		post_name.setText(bean.getName());
		setImageViewByView(iv_company, R.drawable.icon_temp_company,
			Constant.URLIPPIC + bean.getIcon());
	    }

	    return convertView;
	}

	private void setImageViewByView(final ImageView view,
		int DefaultIconId, String IconUrl) {
	    if (!StringUtils.isBlank(IconUrl)) {
		ImageLoaderUtil.getInstance().displayImage(IconUrl, view,
			DefaultIconId); // 不使用原图
	    } else {
		view.setImageResource(DefaultIconId);
	    }
	}
    }

}
