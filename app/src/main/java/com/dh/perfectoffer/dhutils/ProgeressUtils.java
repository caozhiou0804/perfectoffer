package com.dh.perfectoffer.dhutils;

import com.dh.perfectoffer.R;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface.OnCancelListener;
import android.widget.TextView;

public class ProgeressUtils {

	/**
	 * 数据加载dialog
	 */
	private static Dialog mProgeressDialog;// 自定义dialog
	private static ProgeressUtils progreProgeressUtils = null;
	private static ProgressDialog mProgeressDialogMy;// 系统dialog

	public static ProgeressUtils getInstance() {
		if (null == progreProgeressUtils) {
			progreProgeressUtils = new ProgeressUtils();
		}
		return progreProgeressUtils;
	}

	public static ProgressDialog getmProgeressDialogMy() {
		return mProgeressDialogMy;
	}

	public static void setmProgeressDialogMy(ProgressDialog mProgeressDialogMy) {
		ProgeressUtils.mProgeressDialogMy = mProgeressDialogMy;
	}

	/**
	 * 弹出数据加载dialog
	 * 
	 * @return
	 * 
	 */
	public static void showLoadingDialog(String str, Context context) {
		showLoadingDialog(str, context, true);
	}

	public static void showLoadingDialog(String str, Context context,
			boolean iscancel) {
		if (mProgeressDialog == null) {
			mProgeressDialog = new Dialog(context, R.style.custom_dialog_style);
			mProgeressDialog.setContentView(R.layout.dialog_loading);
			mProgeressDialog.setCanceledOnTouchOutside(iscancel);
			if (str != null && str.length() > 0) {
				((TextView) mProgeressDialog
						.findViewById(R.id.dialog_loading_text)).setText(str);
			} else {
				((TextView) mProgeressDialog
						.findViewById(R.id.dialog_loading_text))
						.setText("正在加载");
			}
		}
		if (mProgeressDialog.isShowing()) {
			closeLoadingDialog();
			showLoadingDialog(str, context);
		} else {
			mProgeressDialog.show();
		}
	}

	/**
	 * 关闭数据加载dialog
	 * 
	 */
	public static void closeLoadingDialog() {
		try {
			if (mProgeressDialog != null) {
				mProgeressDialog.cancel();
				mProgeressDialog = null;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public static void showLoadingDialogMy(String str, Context context) {
		showLoadingDialogMy(str, context, true, null);
	}

	public static void showLoadingDialogMy(String str, Context context,
			boolean iscancel, OnCancelListener listener) {
		if (mProgeressDialogMy == null) {
			mProgeressDialogMy = new ProgressDialog(context);
			mProgeressDialogMy.setCanceledOnTouchOutside(false);
			mProgeressDialogMy.setTitle("");
			mProgeressDialogMy.setMessage(str);
			mProgeressDialogMy.setCancelable(iscancel);
			mProgeressDialogMy.setOnCancelListener(listener);
			mProgeressDialogMy.show();
		}

	}

	public void showLoadingDialogFabu(String str, Context context,
			boolean iscancel, OnCancelListener listener) {
		if (mProgeressDialogMy == null) {
			mProgeressDialogMy = new ProgressDialog(context);
			mProgeressDialogMy.setCanceledOnTouchOutside(false);
			mProgeressDialogMy.setTitle("");
			mProgeressDialogMy.setMessage(str);
			mProgeressDialogMy.setCancelable(iscancel);
			mProgeressDialogMy.setOnCancelListener(listener);
			mProgeressDialogMy.show();
			// mProgeressDialogMy = ProgressDialog.show(context, "", str, true,
			// iscancel, listener);
		}

	}

	/**
	 * 关闭数据加载dialog
	 * 
	 */
	public static void closeLoadingDialogMy() {
		try {
			if (mProgeressDialogMy != null) {
				mProgeressDialogMy.cancel();
				mProgeressDialogMy = null;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
