package com.dh.perfectoffer.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.dh.perfectoffer.activity.CompanyActivity;
import com.dh.perfectoffer.adapter.ArrayListAdapter;
import com.dh.perfectoffer.adapter.ViewHolder;
import com.dh.perfectoffer.constant.Constant;
import com.dh.perfectoffer.dhutils.ImageLoaderUtil;
import com.dh.perfectoffer.entity.ComBean;
import com.dh.perfectoffer.entity.CompanyListBean;
import com.dh.perfectoffer.event.framework.db.AfeiDb;
import com.dh.perfectoffer.event.framework.util.StringUtils;
import com.dh.perfectoffer.xutils.view.ViewUtils;
import com.dh.perfectoffer.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;


public class CompanyFragment extends Fragment {

	@ViewInject(R.id.companyfragment)
	private ListView company_list;
	@ViewInject(R.id.ll_empty)
	private View ll_empty;// 简历数据空

	private HRAdapter mMessageAdapter;
	private ArrayList<CompanyListBean> mcompanyList;
	private ArrayList<ComBean> comlist;
	private CompanyListBean listbean;
	private AfeiDb afeiDb;
	private Context mAppContext;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.companyfragment, null);
		ViewUtils.inject(this, view);
		mAppContext = inflater.getContext().getApplicationContext();
		afeiDb = VehicleApp.getInstance().getAfeiDb();
		if (null == afeiDb) {
			afeiDb = AfeiDb.create(getActivity(), Constant.DB_NAME, true);
		}
		initview();
		return view;

	}

	public void refreshData(boolean showDialog) {
		initdata();
	}

	public void initview() {

		mcompanyList = new ArrayList<CompanyListBean>();
		mMessageAdapter = new HRAdapter(getActivity());
		mMessageAdapter.setList(mcompanyList);
		company_list.setAdapter(mMessageAdapter);
		initdata();
		company_list.setOnItemClickListener(new OnItemClickListener() {
          
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(), CompanyActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("companyid", mcompanyList.get(arg2).getId()
						.toString());
				bundle.putString("companyname", mcompanyList.get(arg2)
						.getName().toString());
				bundle.putString("companycontent", mcompanyList.get(arg2)
						.getContent().toString());
				bundle.putString("companyicon", mcompanyList.get(arg2)
						.getIcon().toString());
				intent.putExtras(bundle);
				//
				startActivity(intent);

			}
		});
	}

	private void initdata() {
		// afeiDb.dropTableIfTableExist(ComBean.class);
		// afeiDb.findAll(ComBean.class);
		List<ComBean> mList = new ArrayList<ComBean>();
		mList = afeiDb.findAll(ComBean.class);
		if (mList.size() > 0) {
			mcompanyList.clear();
			for (int i = 0; i < mList.size(); i++) {

				// companyList.get(i).getSub_id();
				// companyList.get(i).getSub_details();

				listbean = new CompanyListBean();
				listbean.setId(mList.get(i).getSub_id());
				listbean.setName(mList.get(i).getSub_name());
				listbean.setIcon(mList.get(i).getSub_icon());

				listbean.setNumber(mList.get(i).getSub_num());
				listbean.setContent(mList.get(i).getSub_details());
				mcompanyList.add(listbean);
			}
			mMessageAdapter.notifyDataSetChanged();
			ll_empty.setVisibility(View.GONE);
			company_list.setVisibility(View.VISIBLE);
		} else {

			ll_empty.setVisibility(View.VISIBLE);
			company_list.setVisibility(View.GONE);
		}
	}

	class HRAdapter extends ArrayListAdapter<CompanyListBean> {

		public HRAdapter(Activity context) {
			super(context);
		}

		public View getView(final int position, View convertView,
				ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.data_companylist, null);
			}

			TextView post_name = ViewHolder.get(convertView, R.id.company_name);
			TextView sub_name = ViewHolder
					.get(convertView, R.id.company_number);
			TextView reward_salary = ViewHolder.get(convertView,
					R.id.company_content);
			ImageView iv_company = ViewHolder.get(convertView, R.id.iv_company);
			final CompanyListBean bean = mList.get(position);
			if (bean != null) {
				post_name.setText(bean.getName());
				sub_name.setText(bean.getNumber());
				reward_salary.setText(bean.getContent());
				setImageViewByView(iv_company, R.drawable.icon_temp_company,
						Constant.URLIPPIC + bean.getIcon());
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
