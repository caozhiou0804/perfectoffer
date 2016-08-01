package com.dh.perfectoffer.adapter;

import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dh.perfectoffer.R;

public class PostAdspter extends BaseAdapter {

	private List<Map<String, Object>> data;
	private LayoutInflater layoutInflater;
	private Context context;

	public PostAdspter(Context context, List<Map<String, Object>> data) {
		this.context = context;
		this.data = data;
		this.layoutInflater = LayoutInflater.from(context);
	}

	public final class ListShow {
		public TextView post_name, sub_name, reward_salary, bounty,
				create_time, rew_id;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ListShow listview = null;
		if (convertView == null) {
			listview = new ListShow();
			convertView = layoutInflater.inflate(
					R.layout.delivery_history_list, null);
			listview.post_name = (TextView) convertView
					.findViewById(R.id.post_name);
			listview.sub_name = (TextView) convertView
					.findViewById(R.id.sub_name);
			listview.reward_salary = (TextView) convertView
					.findViewById(R.id.reward_salary);
			listview.bounty = (TextView) convertView.findViewById(R.id.bounty);
			listview.create_time = (TextView) convertView
					.findViewById(R.id.create_time);
			listview.rew_id = (TextView) convertView.findViewById(R.id.rew_id);
			convertView.setTag(listview);
		} else {
			listview = (ListShow) convertView.getTag();
		}

		listview.post_name
				.setText((String) data.get(position).get("post_name"));
		listview.sub_name.setText((String) data.get(position).get("sub_name"));
		listview.reward_salary.setText((String) data.get(position).get(
				"reward_salary"));
		listview.bounty.setText((String) data.get(position).get("bounty"));
		listview.create_time.setText((String) data.get(position).get(
				"create_time"));
		listview.rew_id.setText((String) data.get(position).get("rew_id"));
		return convertView;
	}

}
