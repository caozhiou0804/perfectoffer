package com.dh.perfectoffer.demo;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
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

import org.json.JSONException;
import org.json.JSONStringer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FriedActivity extends Activity implements OnClickListener {
	private RequestQueue mVolleyQueue;
	private ProgressDialog mProgress; // 加载dialog
	private final String TAG_REQUEST = "VOLLEY_TAG";

	private final String DATABASE = "LOGIN_NAME";

	private EditText friedname, friedage, friedschools, reservation,
			remark = null;
	// 下啦赋值
	private Spinner friedsex = null;

	private ImageButton fre_submbtn, fre_cleartemp;

	private ImageView back_post, peo_but;
	private TextView sub_header;

	private static final int SHOW_DATAPICK = 0;
	private static final int DATE_DIALOG_ID = 1;

	List<SpinnerOption> lst = null;
	private ArrayAdapter<SpinnerOption> adapter = null;// 要使用的Adapter

	private int mYear;

	private int mMonth;

	private int mDay;
	String rew_id = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		setContentView(R.layout.friend);
		friedsex = (Spinner) super.findViewById(R.id.friedsex);
		friedname = (EditText) super.findViewById(R.id.friedname);
		friedage = (EditText) super.findViewById(R.id.friedage);
		friedschools = (EditText) super.findViewById(R.id.friedschools);
		reservation = (EditText) super.findViewById(R.id.reservation);
		remark = (EditText) super.findViewById(R.id.remark);

		fre_submbtn = (ImageButton) super.findViewById(R.id.fre_submbtn);
		fre_cleartemp = (ImageButton) super.findViewById(R.id.fre_cleartemp);
		sub_header = (TextView) findViewById(R.id.sub_header);
		back_post = (ImageView) findViewById(R.id.back_post);
		sub_header.setText("朋友信息");
		back_post.setOnClickListener(this);
		peo_but = (ImageView) findViewById(R.id.peo_but);
		peo_but.setVisibility(View.INVISIBLE);// 隐藏图片

		Intent intent = getIntent();
		rew_id = intent.getStringExtra("rewardID");// 悬赏职位ID

		// 性别
		querySex();

		// 时间控件
		reservation.setOnClickListener(this);
		reservation.setOnFocusChangeListener(new HidOnFocus());

		// 时间初始化
		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);
		// setDateTime();

		// 按钮事件
		fre_cleartemp.setOnClickListener(this);
		mVolleyQueue = Volley.newRequestQueue(getApplicationContext());
		mVolleyQueue.start();
		fre_submbtn.setOnClickListener(this);
	}

	/**
	 * 性别
	 */
	public void querySex() {
		lst = new ArrayList<SpinnerOption>();
		SpinnerOption sexSpM = new SpinnerOption("1", "男");
		SpinnerOption sexSpW = new SpinnerOption("2", "女");
		lst.add(sexSpM);
		lst.add(sexSpW);
		adapter = new ArrayAdapter<SpinnerOption>(this,
				android.R.layout.simple_spinner_item, lst);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		friedsex.setAdapter(adapter);
	}

	private void setDateTime() {

		final Calendar c = Calendar.getInstance();

		mYear = c.get(Calendar.YEAR);

		mMonth = c.get(Calendar.MONTH);

		mDay = c.get(Calendar.DAY_OF_MONTH);

		updateDisplay();

	}

	class HidOnFocus implements OnFocusChangeListener {

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if (hasFocus == true) {
				hideIM(v);
			}

		}

	}

	/**
	 * 
	 * 更新日期
	 */

	private void updateDisplay() {

		reservation.setText(new StringBuilder().append(mYear).append("-")
				.append((mMonth + 1) < 10 ? "0" + (mMonth + 1) : (mMonth + 1))
				.append("-").append((mDay < 10) ? "0" + mDay : mDay));

	}

	@Override
	protected Dialog onCreateDialog(int id) {

		switch (id) {

		case DATE_DIALOG_ID:

			return new DatePickerDialog(this, mDateSetListener, mYear, mMonth,

			mDay);

		}

		return null;

	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {

		switch (id) {

		case DATE_DIALOG_ID:

			((DatePickerDialog) dialog).updateDate(mYear, mMonth, mDay);

			break;

		}

	}

	/**
	 * 
	 * 日期控件的事件
	 */

	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,

		int dayOfMonth) {

			mYear = year;

			mMonth = monthOfYear;

			mDay = dayOfMonth;

			updateDisplay();

		}

	};

	/**
	 * 
	 * 处理日期控件的Handler
	 */

	Handler saleHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {

			case FriedActivity.SHOW_DATAPICK:

				showDialog(DATE_DIALOG_ID);

				break;

			}

		}

	};

	// 隐藏手机键盘
	private void hideIM(View edt) {
		try {
			InputMethodManager im = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
			IBinder windowToken = edt.getWindowToken();

			if (windowToken != null) {
				im.hideSoftInputFromWindow(windowToken, 0);
			}
		} catch (Exception e) {

		}
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
		Toast.makeText(FriedActivity.this, msg, Toast.LENGTH_LONG).show();
	}

	// 提交信息并关联
	private void friedSubmit(final String json) {
		showProgress();
		String url = Constant.URLIP + "hr/FriedSubmitServlet";
		StringRequest postRequest = new StringRequest(Request.Method.POST, url,
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						if (!TextUtils.isEmpty(response)) {
							if (response.equals("success")) {
								Intent intent = new Intent();
								intent.putExtra("ret2", "介绍朋友成功");
								// 设置结果，并进行传送
								FriedActivity.this.setResult(93, intent);
								FriedActivity.this.finish();
							} else if (response.equals("nopass")) {
								showToast("您已经提交过一次，为防止重复请十分钟后再试");
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

	private void sendMessages(View v) {
		hideIM(v);
		Message msg = new Message();
		if (reservation.equals((EditText) v)) {
			msg.what = FriedActivity.SHOW_DATAPICK;
		}
		saleHandler.sendMessage(msg);
	}

	private void clearBut() {
		friedname.setText("");
		friedage.setText("");
		friedschools.setText("");
		reservation.setText("");
		friedsex.setSelection(0);
		remark.setText("");
	}

	private void verified() {
		// 验证
		MessageDialog dialog = new MessageDialog(FriedActivity.this);

		dialog.ShowConfirm(10, "提示", "确认提交信息吗？", new IMessageDialogListener() {

			@Override
			public void onDialogClickOk(int requestCode) { //
				// 验证表格
				String nameV = friedname.getText().toString();
				String ageV = friedage.getText().toString();
				String friedschoolsV = friedschools.getText().toString();
				String reservationV = reservation.getText().toString();
				String friedsexV = ((SpinnerOption) friedsex.getSelectedItem())
						.getValue();
				String remarkV = remark.getText().toString();
				if (TextUtils.isEmpty(nameV)) {
					showToast("请输入姓名");
				} else if (TextUtils.isEmpty(ageV)) {
					showToast("请输入年龄");
				} else if (TextUtils.isEmpty(friedschoolsV)) {
					showToast("请输入学校名称");
				} else if (TextUtils.isEmpty(reservationV)) {
					showToast("请输选择日期");
				} else {
					// 获取用户名
					SharedPreferences sp = getSharedPreferences(DATABASE,
							Activity.MODE_PRIVATE);
					String uname = sp.getString("uname", "");

					// 字符串处理
					try {
						String submitStr = new JSONStringer().object()
								.key("user_name").value(uname).key("sex")
								.value(friedsexV).key("age").value(ageV)
								.key("name").value(nameV).key("schools")
								.value(friedschoolsV).key("reservation_date")
								.value(reservationV).key("remark")
								.value(remarkV).key("reward_id").value(rew_id)
								.endObject().toString();
						friedSubmit(submitStr);
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
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.back_post:
			finish();
			break;
		case R.id.reservation:
			sendMessages(v);
			break;
		case R.id.fre_cleartemp:
			clearBut();
			break;
		case R.id.fre_submbtn:
			verified();
			break;
		default:
			break;
		}

	}
}
