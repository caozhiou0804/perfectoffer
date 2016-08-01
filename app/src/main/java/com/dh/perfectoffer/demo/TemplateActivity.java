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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
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
import com.dh.perfectoffer.xutils.sample.utils.SpinnerOption;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TemplateActivity extends Activity implements OnClickListener {
	private RequestQueue mVolleyQueue;
	private ProgressDialog mProgress; // 加载dialog
	private final String TAG_REQUEST = "VOLLEY_TAG";

	private final String DATABASE = "LOGIN_NAME";

	private ImageButton submbtn, cleartemp;
	private ImageView back_post, peo_but;
	private TextView sub_header;
	// 姓名
	private EditText empname, age, phone, schools;

	// 下啦赋值
	private Spinner sex, sub_name, post_name = null;
	List<SpinnerOption> lst1 = null;
	List<SpinnerOption> lst2 = null;
	List<SpinnerOption> lst3 = new ArrayList<SpinnerOption>();;

	private ArrayAdapter<SpinnerOption> adapter = null;// 要使用的Adapter

	private String rew_id, type = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		setContentView(R.layout.template);
		sex = (Spinner) super.findViewById(R.id.sex);
		sub_name = (Spinner) super.findViewById(R.id.sub_name);
		post_name = (Spinner) super.findViewById(R.id.post_name);

		cleartemp = (ImageButton) super.findViewById(R.id.cleartemp);
		submbtn = (ImageButton) super.findViewById(R.id.submbtn);
		empname = (EditText) super.findViewById(R.id.empname);
		age = (EditText) super.findViewById(R.id.age);
		phone = (EditText) super.findViewById(R.id.phone);
		schools = (EditText) super.findViewById(R.id.schools);
		back_post = (ImageView) findViewById(R.id.back_post);
		sub_header = (TextView) findViewById(R.id.sub_header);
		sub_header.setText("简历详情");
		peo_but = (ImageView) findViewById(R.id.peo_but);
		peo_but.setVisibility(View.INVISIBLE);// 隐藏图片

		Intent intent = getIntent();
		rew_id = intent.getStringExtra("rewardID");// 悬赏职位ID
		type = intent.getStringExtra("type");// 悬赏职位ID

		mVolleyQueue = Volley.newRequestQueue(getApplicationContext());
		mVolleyQueue.start();

		// 填充下拉框
		querySex();
		// 子公司
		querySubsidiaries();
		queryOffer("-99");

		back_post.setOnClickListener(this);

		// 赋联动事件
		sub_name.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// String msg = ((SpinnerOption) sub_name.getSelectedItem())
				// .getText();
				String vv = ((SpinnerOption) sub_name.getSelectedItem())
						.getValue();
				querySyspost(vv);

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}

		});

		cleartemp.setOnClickListener(this);
		// 提交事件
		submbtn.setOnClickListener(this);
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
		Toast.makeText(TemplateActivity.this, msg, Toast.LENGTH_LONG).show();
	}

	/**
	 * 性别
	 */
	public void querySex() {
		lst1 = new ArrayList<SpinnerOption>();
		SpinnerOption sexSpM = new SpinnerOption("1", "男");
		SpinnerOption sexSpW = new SpinnerOption("2", "女");
		lst1.add(sexSpM);
		lst1.add(sexSpW);
		adapter = new ArrayAdapter<SpinnerOption>(this,
				android.R.layout.simple_spinner_item, lst1);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sex.setAdapter(adapter);
	}

	/**
	 * 所属职位初始化值：请选择
	 */
	public void queryOffer(String str) {
		lst3.clear();
		SpinnerOption offer = null;
		if (str.equals("-99")) {
			offer = new SpinnerOption("0", "请选择");
			lst3.add(offer);
		} else {
			JSONArray json;
			try {
				json = new JSONArray(str);
				for (int i = 0; i < json.length(); i++) {
					// 获取每一个JsonObject对象
					JSONObject object = json.getJSONObject(i);
					offer = new SpinnerOption(object.getString("id"),
							object.getString("post_name"));
					lst3.add(offer);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}

		adapter = new ArrayAdapter<SpinnerOption>(this,
				android.R.layout.simple_spinner_item, lst3);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		post_name.setAdapter(adapter);
	}

	/**
	 * 
	 */
	public void querySubSp(String jsonArray) {
		lst2 = new ArrayList<SpinnerOption>();

		try {
			JSONArray json = new JSONArray(jsonArray);
			SpinnerOption one = new SpinnerOption("0", "请选择");
			lst2.add(one);
			for (int i = 0; i < json.length(); i++) {
				// 获取每一个JsonObject对象
				JSONObject object = json.getJSONObject(i);
				SpinnerOption subs = new SpinnerOption(object.getString("id"),
						object.getString("sub_name"));

				lst2.add(subs);

			}
			adapter = new ArrayAdapter<SpinnerOption>(this,
					android.R.layout.simple_spinner_item, lst2);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			sub_name.setAdapter(adapter);

		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 子公司
	 */
	public void querySubsidiaries() {
		String url = Constant.URLIP + "hr/SubsidiariesServlet";
		StringRequest postRequest = new StringRequest(Request.Method.POST, url,
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {

						if (!TextUtils.isEmpty(response)) {
							querySubSp(response);
						}

					}

				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						stopProgress();
						showToast(error.getMessage());
					}
				});
		postRequest.setShouldCache(true);// 开启缓存
		postRequest.setTag(TAG_REQUEST);

		mVolleyQueue.add(postRequest);
	}

	/**
	 * 子公司职位
	 */
	public void querySyspost(final String subid) {
		String url = Constant.URLIP + "hr/OfficeServlet";
		StringRequest postRequest = new StringRequest(Request.Method.POST, url,
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						if (!TextUtils.isEmpty(response)) {
							queryOffer(response);
						} else {
							queryOffer("-99");
						}

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
				params.put("sub_id", subid);
				return params;
			}
		};
		postRequest.setShouldCache(true);// 开启缓存
		postRequest.setTag(TAG_REQUEST);

		mVolleyQueue.add(postRequest);
	}

	// 提交事件
	private void verifiSubmit(final String json, final String code) {
		showProgress();
		String url = Constant.URLIP + "hr/SubmitServlet";
		StringRequest postRequest = new StringRequest(Request.Method.POST, url,
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						if (!TextUtils.isEmpty(response)) {
							if (response.equals("success")) {
								Intent intent = new Intent();
								if (code.equals("2")) {
									intent.putExtra("ret", "简历提交成功");
								} else {
									intent.putExtra("ret", "信息提交成功");
								}
								// 设置结果，并进行传送
								TemplateActivity.this.setResult(92, intent);
								TemplateActivity.this.finish();
							} else if (response.equals("exists")) {
								showToast("手机号码已存在");
							} else {
								showToast("提交失败");
							}
						} else {
							showToast("提交失败");
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
				params.put("json", json);
				return params;
			}
		};
		postRequest.setShouldCache(true);// 开启缓存
		postRequest.setTag(TAG_REQUEST);

		mVolleyQueue.add(postRequest);
	}

	private void submitMend() {
		MessageDialog dialog = new MessageDialog(TemplateActivity.this);

		dialog.ShowConfirm(10, "提示", "确认提交信息吗？", new IMessageDialogListener() {

			@Override
			public void onDialogClickOk(int requestCode) { // 投递简历
				// 验证表格
				String nameV = empname.getText().toString();
				String ageV = age.getText().toString();
				String phoneV = phone.getText().toString();
				String schoolsV = schools.getText().toString();
				String sub_nameV = ((SpinnerOption) sub_name.getSelectedItem())
						.getValue();
				String post_nameV = ((SpinnerOption) post_name
						.getSelectedItem()).getValue();
				String sexV = ((SpinnerOption) sex.getSelectedItem())
						.getValue();
				if (TextUtils.isEmpty(nameV)) {
					showToast("请输入姓名");
				} else if (TextUtils.isEmpty(ageV)) {
					showToast("请输入年龄");
				} else if (sub_nameV.equals("0")) {
					showToast("请选择公司");
				} else if (post_nameV.equals("0")) {
					showToast("请选择职位");
				} else if (TextUtils.isEmpty(phoneV)) {
					showToast("请输入手机号码");
				} else if (TextUtils.isEmpty(schoolsV)) {
					showToast("请输入学校名称/专业");
				} else {

					// 获取用户名
					SharedPreferences sp = getSharedPreferences(DATABASE,
							Activity.MODE_PRIVATE);
					String uname = sp.getString("uname", "");

					// 字符串处理
					try {
						String submitStr = new JSONStringer().object()
								.key("user_name").value(uname).key("sex")
								.value(sexV).key("age").value(ageV).key("name")
								.value(nameV).key("phone").value(phoneV)
								.key("post_id").value(post_nameV)
								.key("schools").value(schoolsV)
								.key("reward_id").value(rew_id).key("status")
								.value(type).endObject().toString();
						verifiSubmit(submitStr, type);
					} catch (JSONException e) {
					}

				}
			}

			@Override
			public void onDialogClickClose(int requestCode) {
			}

			@Override
			public void onDialogClickCancel(int requestCode) {

			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cleartemp:
			empname.setText("");
			age.setText("");
			phone.setText("");
			schools.setText("");
			sub_name.setSelection(0);
			sex.setSelection(0);
			break;
		case R.id.submbtn:
			submitMend();
			break;
		case R.id.back_post:
			finish();
			break;
		default:
			break;
		}

	}
}
