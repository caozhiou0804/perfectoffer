package com.dh.perfectoffer.demo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dh.perfectoffer.R;
import com.dh.perfectoffer.constant.Constant;
import com.dh.perfectoffer.xutils.sample.utils.IMessageDialogListener;
import com.dh.perfectoffer.xutils.sample.utils.MessageDialog;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.HashMap;
import java.util.Map;

public class OfferRewardActivity extends Activity implements OnClickListener {

	private RequestQueue mVolleyQueue;
	private ProgressDialog mProgress; // 加载dialog
	private final String TAG_REQUEST = "VOLLEY_TAG";

	private final String DATABASE = "LOGIN_NAME";

	// 悬赏标题，发布时间，赏金，职位,月薪，福利，要求，描述详情
	private TextView rew_title, published, bounty, rewpost, rewardsalary,
			rewardwelfare, rewardclaim, claimdetails, address, sub_header;

	private ImageButton resume, intrfriend = null;
	private ImageView back_post, peo_but;
	private String rew_id = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		setContentView(R.layout.offer_reward);

		rew_title = (TextView) findViewById(R.id.rew_title);
		published = (TextView) findViewById(R.id.published);
		bounty = (TextView) findViewById(R.id.bounty);
		rewpost = (TextView) findViewById(R.id.rewpost);
		rewardsalary = (TextView) findViewById(R.id.rewardsalary);
		rewardwelfare = (TextView) findViewById(R.id.rewardwelfare);
		rewardclaim = (TextView) findViewById(R.id.rewardclaim);
		claimdetails = (TextView) findViewById(R.id.claimdetails);
		address = (TextView) findViewById(R.id.address);
		sub_header = (TextView) findViewById(R.id.sub_header);
		back_post = (ImageView) findViewById(R.id.back_post);
		sub_header.setText("悬赏详情");
		peo_but = (ImageView) findViewById(R.id.peo_but);
		peo_but.setVisibility(View.INVISIBLE);// 隐藏图片

		Intent intent = getIntent();
		rew_id = intent.getStringExtra("rew_id");// 悬赏职位ID
		mVolleyQueue = Volley.newRequestQueue(getApplicationContext());
		mVolleyQueue.start();
		// 根据悬赏职位ID获取悬赏详情
		queryOffer(rew_id);

		// 按钮事件
		resume = (ImageButton) super.findViewById(R.id.resume);
		resume.setOnClickListener(this);

		back_post.setOnClickListener(this);

		intrfriend = (ImageButton) super.findViewById(R.id.intrfriend);
		intrfriend.setOnClickListener(this);
	}

	public void onDestroy() {
		super.onDestroy();
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
		Toast.makeText(OfferRewardActivity.this, msg, Toast.LENGTH_LONG).show();
	}

	public void queryOffer(final String rew_id) {
		String url = Constant.URLIP + "hr/RewardDetailsServlet";
		showProgress();
		StringRequest postRequest = new StringRequest(Request.Method.POST, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						// response
						if (!TextUtils.isEmpty(response)) {
							rew_title.setText(jsonTo(response, "reward_title"));
							published.setText(jsonTo(response, "create_time")
									.substring(0, 10));
							bounty.setText(jsonTo(response, "bounty"));
							rewpost.setText(jsonTo(response, "post_name"));
							rewardsalary.setText(jsonTo(response,
									"reward_salary"));
							String rewstr = jsonTo(response, "reward_welfare");
							rewardwelfare.setText(toHtml(rewstr));
							rewardclaim.setText(toHtml(jsonTo(response,
									"reward_claim")));
							claimdetails.setText(toHtml(jsonTo(response,
									"reward_claim_details")));
							address.setText(jsonTo(response, "sub_address"));
						}
						stopProgress();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						// error
						stopProgress();
						showToast(error.getMessage());
					}
				}) {

			@Override
			protected Map<String, String> getParams() {
				Map<String, String> params = new HashMap<String, String>();
				params.put("rew_id", rew_id);
				return params;
			}

		};
		postRequest.setShouldCache(true);// 开启缓存
		postRequest.setTag(TAG_REQUEST);
		mVolleyQueue.add(postRequest);

	}

	public String jsonTo(String str, String parma) {
		JSONObject jsonObject;
		String name = "";
		try {
			jsonObject = new JSONObject(str);
			name = jsonObject.getString(parma);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return name;
	}

	private void isLogin(final String rew_id) {
		String url = Constant.URLIP + "hr/IsLoginServlet";
		// 获取SharedPreferences对象
		SharedPreferences sp = getSharedPreferences(DATABASE,
				Activity.MODE_PRIVATE);
		final String uname = sp.getString("uname", "");

		StringRequest postRequest = new StringRequest(Request.Method.POST, url,
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						// response

						if (!TextUtils.isEmpty(uname)) {
							if (response.equals("success")) {
								MessageDialog dialog = new MessageDialog(
										OfferRewardActivity.this);

								dialog.ShowConfirm(10, "提示", "确认投递简历吗？",
										new IMessageDialogListener() {

											@Override
											public void onDialogClickOk(
													int requestCode) { // 投递简历
												// 判断是否已经投过
												// 字符串处理
												try {
													String submitStr = new JSONStringer()
															.object()
															.key("user_name")
															.value(uname)
															.key("reward_id")
															.value(rew_id)
															.key("status")
															.value("1")
															.endObject()
															.toString();
													voteresume(submitStr);
												} catch (JSONException e) {
												}
											}

											@Override
											public void onDialogClickClose(
													int requestCode) {
											}

											@Override
											public void onDialogClickCancel(
													int requestCode) {

											}
										});

							} else { // 跳转模板填充页面
								showToast("请完善简历信息");
								 Intent intent = new Intent(
								 OfferRewardActivity.this,
								 TemplateActivity.class);
								 intent.putExtra("rewardID", rew_id);
								 intent.putExtra("type", "2");
								 startActivityForResult(intent, 92);

							}
						} else { // 登录页面
//							 showToast("请先登录");
//							 Intent intent = new Intent(
//							 OfferRewardActivity.this,
//							 LoginActivity.class);
//							 startActivity(intent);
//							 finish();
						}
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) { // error
						showToast(error.getMessage());
					}
				}) {
			@Override
			protected Map<String, String> getParams() {
				Map<String, String> params = new HashMap<String, String>();
				params.put("uname", uname);
				return params;
			}
		};
		postRequest.setShouldCache(true);// 开启缓存
		postRequest.setTag(TAG_REQUEST);
		mVolleyQueue.add(postRequest);

	}

	// 回调函数
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (null != data) {
			String ret = "回调函数";
			switch (requestCode) {
			case 92:
				ret = data.getStringExtra("ret");
				break;
			case 93:
				ret = data.getStringExtra("ret2");
				break;
			default:
				break;
			}
			showToast(ret);
		}

	}

	// 投递简历
	public void voteresume(final String json) {
		showProgress();
		String url = Constant.URLIP + "hr/SubmitServlet";
		StringRequest postRequest = new StringRequest(Request.Method.POST, url,
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						if (response != null && !response.equals("")) {
							if (response.equals("success")) {
								showToast("投过成功");
							} else if (response.equals("exists")) {
								showToast("已经投过该职位");
							} else {
								showToast("投递失败");
							}
						} else {
						}
						stopProgress();

					}

				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						if (null != error.getMessage()) {
						}
						stopProgress();
						showToast(error.getMessage());
					}
				}) {
			@Override
			protected Map<String, String> getParams() {
				Map<String, String> params = new HashMap<String, String>();
				params.put("json", json);
				return params;
			}
		};
		postRequest.setShouldCache(true);// 开启缓存
		postRequest.setTag(TAG_REQUEST);

		mVolleyQueue.add(postRequest);
	}

	// 介绍朋友
	private void intrfriendClick(final String rew_id) {
		String url = Constant.URLIP + "hr/IsLoginServlet";
		// 获取SharedPreferences对象
		SharedPreferences sp = getSharedPreferences(DATABASE,
				Activity.MODE_PRIVATE);
		final String uname = sp.getString("uname", "");

		StringRequest postRequest = new StringRequest(Request.Method.POST, url,
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						// response

						if (!TextUtils.isEmpty(response)) {
							if (response.equals("success")) {
								// 跳转介绍朋友页面；参数：职位ID
								Intent intent = new Intent(
										OfferRewardActivity.this,
										FriedActivity.class);
								intent.putExtra("rewardID", rew_id);
								startActivityForResult(intent, 93);

							} else { // 跳转模板填充页面
								showToast("请先完善简历信息");
								Intent intent = new Intent(
										OfferRewardActivity.this,
										TemplateActivity.class);
								intent.putExtra("rewardID", rew_id);
								intent.putExtra("type", "3");
								startActivityForResult(intent, 92);

							}
						} else { // 登录页面
//							showToast("请先登录");
//							Intent intent = new Intent(
//									OfferRewardActivity.this,
//									LoginActivity.class);
//							startActivity(intent);
							// finish();
						}
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) { // error
						showToast(error.getMessage());
					}
				}) {
			@Override
			protected Map<String, String> getParams() {
				Map<String, String> params = new HashMap<String, String>();
				params.put("uname", uname);
				return params;
			}
		};
		postRequest.setShouldCache(true);// 开启缓存
		postRequest.setTag(TAG_REQUEST);
		mVolleyQueue.add(postRequest);
	}

	private String toHtml(String str) {
		String ret = "";
		String strs[] = str.split("；");
		for (int i = 0; i < strs.length; i++) {
			if (i == strs.length - 1)
				ret += strs[i];
			else
				ret += strs[i] + "；\n";
		}
		return ret;

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.intrfriend:
			// 跳转介绍朋友
			/*
			 * 验证 1.是否登录 2.简历信息
			 */
			intrfriendClick(rew_id);
			break;
		case R.id.resume:
			// 判断是否登录
			isLogin(rew_id);
			break;
		case R.id.back_post:
			finish();
			break;
		default:
			break;
		}

	}
}
