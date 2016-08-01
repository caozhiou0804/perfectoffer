package com.dh.perfectoffer.demo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dh.perfectoffer.R;
import com.dh.perfectoffer.adapter.PostAdspter;
import com.dh.perfectoffer.constant.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostActivity extends Activity implements OnClickListener {
	private ListView listView = null;

	private RequestQueue mVolleyQueue;
	private ProgressDialog mProgress; // 加载dialog
	private final String TAG_REQUEST = "VOLLEY_TAG";
	private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(); // 包装显示内容
	private ImageView back_post, peo_but, bgimage;
	String post_id;

	String post_name = "";

	private TextView comname, sub_header;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		setContentView(R.layout.post);
		listView = (ListView) findViewById(R.id.list1);
		bgimage = (ImageView) findViewById(R.id.bgimage);
		mVolleyQueue = Volley.newRequestQueue(getApplicationContext());
		mVolleyQueue.start();
		View v = LayoutInflater.from(this).inflate(R.layout.post_head, null,
				false);
		listView.addHeaderView(v);
		// 数组赋值
		Intent intent = getIntent();
		post_id = intent.getStringExtra("post_id");
		post_name = intent.getStringExtra("post_name");
		back_post = (ImageView) findViewById(R.id.back_post);
		sub_header = (TextView) findViewById(R.id.sub_header);
		peo_but = (ImageView) findViewById(R.id.peo_but);
		queryArray(post_id);
		// 子公司名称
		// comname = (TextView) super.findViewById(R.id.zigongsi);
		// comname.setText(post_name);
		sub_header.setText(post_name);
		peo_but.setVisibility(View.INVISIBLE);// 隐藏图片
		// comname.setOnClickListener(this);
		back_post.setOnClickListener(this);

	}

	private List<Map<String, Object>> processJsonArray(String json) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			JSONArray jsonArray = new JSONArray(json);
			String postnames[] = new String[jsonArray.length()];
			String rew_ids[] = new String[jsonArray.length()];
			String reward_salarys[] = new String[jsonArray.length()];
			String create_times[] = new String[jsonArray.length()];
			String sub_names[] = new String[jsonArray.length()];
			String bountys[] = new String[jsonArray.length()];
			for (int i = 0; i < jsonArray.length(); i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				JSONObject obj = jsonArray.getJSONObject(i);
				rew_ids[i] = obj.opt("rew_id").toString();
				postnames[i] = obj.opt("post_name").toString();
				reward_salarys[i] = (String) obj.opt("reward_salary")
						.toString();
				create_times[i] = obj.opt("create_time").toString()
						.substring(0, 10);
				sub_names[i] = obj.opt("sub_name").toString();
				bountys[i] = obj.opt("bounty").toString();
				map.put("rew_id", rew_ids[i]);
				map.put("post_name", postnames[i]);
				map.put("reward_salary", reward_salarys[i]);
				map.put("create_time", create_times[i]);
				map.put("sub_name", sub_names[i]);
				map.put("bounty", "赏金：" + bountys[i]);
				list.add(map);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;

	}

	public void queryArray(final String post_id) {
		showProgress();
		String url = Constant.URLIP + "hr/SubServlet";
		StringRequest postRequest = new StringRequest(Request.Method.POST, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						// response
						if (!TextUtils.isEmpty(response)) {

							list = processJsonArray(response);
							listView.setVisibility(View.VISIBLE);
							bgimage.setVisibility(View.GONE);
							listView.setAdapter(new PostAdspter(
									PostActivity.this, list));
							listView.setOnItemClickListener(new OnItemClickListener() {

								@Override
								public void onItemClick(AdapterView<?> parent,
										View view, int position, long id) {
									TextView v = (TextView) view
											.findViewById(R.id.rew_id);// 获取绑定悬赏职位ID
									Intent intent = new Intent(
											PostActivity.this,
											OfferRewardActivity.class);
									intent.putExtra("rew_id", v.getText()
											.toString());
									startActivity(intent);
									// finish();
								}
							});

						} else {
							listView.setVisibility(View.GONE);
							bgimage.setVisibility(View.VISIBLE);
						}
						stopProgress();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						stopProgress();
						showToast(error.getMessage());
					}
				}) {

			@Override
			protected Map<String, String> getParams() {
				Map<String, String> params = new HashMap<String, String>();
				params.put("sub_id", post_id);
				return params;
			}

		};
		postRequest.setShouldCache(true);// 开启缓存
		postRequest.setTag(TAG_REQUEST);

		mVolleyQueue.add(postRequest);
	}

	public void onStop() {
		super.onStop();
		if (mProgress != null)
			mProgress.dismiss();
		mVolleyQueue.cancelAll(TAG_REQUEST);
	}

	private void showProgress() {
		mProgress = ProgressDialog.show(this, "", "正在加载...");
	}

	private void stopProgress() {
		mProgress.cancel();
	}

	private void showToast(String msg) {
		Toast.makeText(PostActivity.this, msg, Toast.LENGTH_LONG).show();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// case R.id.zigongsi:
		// Intent intent = new Intent(PostActivity.this,
		// SubsidiariesActivity.class);
		// intent.putExtra("post_id", post_id);// 参数赋值
		// intent.putExtra("post_name", post_name);// 参数赋值
		// startActivity(intent);
		// break;
		case R.id.back_post:
			finish();
			break;

		default:
			break;
		}

	}

}
