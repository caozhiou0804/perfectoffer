package com.dh.perfectoffer.demo;//package com.dh.demo;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.app.ProgressDialog;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.Window;
//import android.widget.EditText;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.StringRequest;
//import com.android.volley.toolbox.Volley;
//import com.dh.constant.Constant;
//import com.dh.perfectoffer.R;
//
//public class RegisterActivity extends Activity implements OnClickListener {
//	private EditText newUser, newPassword, confirmPassword;
//	private ImageButton registerBtn, clearBtn;
//	private ImageView peo_but, back_post;
//	private TextView sub_header;
//	private ProgressDialog mProgress;
//	private RequestQueue mVolleyQueue;
//	private final String TAG_REQUEST = "VOLLEY_TAG";
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
//		setContentView(R.layout.register);
//		newUser = (EditText) findViewById(R.id.newUser_input);
//		newPassword = (EditText) findViewById(R.id.newPassword_input);
//		confirmPassword = (EditText) findViewById(R.id.Confirm_input);
//		registerBtn = (ImageButton) findViewById(R.id.registerbtn);
//		sub_header = (TextView) findViewById(R.id.sub_header);
//		back_post = (ImageView) findViewById(R.id.back_post);
//		peo_but = (ImageView) findViewById(R.id.peo_but);
//		clearBtn = (ImageButton) findViewById(R.id.clearbtn);
//
//		sub_header.setText("用户注册");
//		peo_but.setVisibility(View.INVISIBLE);// 隐藏图片
//		back_post.setOnClickListener(this);
//		// 声明volley队列
//		mVolleyQueue = Volley.newRequestQueue(getApplicationContext());
//		registerBtn.setOnClickListener(this);
//
//		clearBtn.setOnClickListener(this);
//	}
//
//	private void showDialog(String str) {
//		AlertDialog.Builder builder = new AlertDialog.Builder(this);
//		builder.setTitle("注册");
//		builder.setMessage(str);
//		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				dialog.dismiss();
//				Intent intent = new Intent();
//			//	intent.setClass(RegisterActivity.this, LoginActivity.class);
//				startActivity(intent);
//				finish();
//			}
//		});
//		AlertDialog dialog = builder.create();
//		dialog.show();
//	}
//
//	private void showProgress() {
//		mProgress = ProgressDialog.show(this, "", "正在加载...");
//	}
//
//	private void stopProgress() {
//		mProgress.cancel();
//	}
//
//	private void showToast(String msg) {
//		Toast.makeText(RegisterActivity.this, msg, Toast.LENGTH_LONG).show();
//	}
//
//	@Override
//	public void onClick(View v) {
//		switch (v.getId()) {
//		case R.id.back_post:
//			finish();
//			break;
//		case R.id.registerbtn:
//			submitBut();
//			break;
//		case R.id.clearbtn:
//			cleanMethod();
//			break;
//		default:
//			break;
//		}
//
//	}
//
//	private void cleanMethod() {
//		newUser.setText("");
//		newPassword.setText("");
//		confirmPassword.setText("");
//
//	}
//
//	private void submitBut() {
//		final String newusername = newUser.getText().toString();
//		final String newpassword = newPassword.getText().toString();
//		String confirmpwd = confirmPassword.getText().toString();
//		if (newpassword.equals("") || newusername.equals("")) {
//			showToast("请输入用户名和密码！");
//		} else
//
//		if (newpassword.equals(confirmpwd)) {
//			showProgress();
//			String resUrl = Constant.URLIP + "hr/RegisterServlet";
//			StringRequest postRequest = new StringRequest(Request.Method.POST,
//					resUrl, new Response.Listener<String>() {
//						@Override
//						public void onResponse(String response) {
//							// response
//							if (response.equals("success")) {
//								showDialog("注册成功");
//							} else if (response.equals("exists")) {
//								showToast("用户名已存在");
//							} else {
//								showToast("注册失败");
//							}
//							stopProgress();
//
//						}
//					}, new Response.ErrorListener() {
//						@Override
//						public void onErrorResponse(VolleyError error) {
//							// error
//							stopProgress();
//							showToast(error.getMessage());
//						}
//					}) {
//
//				@Override
//				protected Map<String, String> getParams() {
//					Map<String, String> params = new HashMap<String, String>();
//					params.put("username", newusername);
//					params.put("userpass", newpassword);
//					return params;
//				}
//
//			};
//			postRequest.setShouldCache(true);// 开启缓存
//			postRequest.setTag(TAG_REQUEST);
//			mVolleyQueue.add(postRequest);
//
//		} else {
//			showToast("您两次输入的密码不一致！");
//		}
//
//	}
//
//}
