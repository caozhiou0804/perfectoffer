/**
 * 
 */
package com.dh.perfectoffer.xutils.sample.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * @author 超级小志
 * 
 */
public class ToastUtil {

	public static void showShort(Context context, String info) {
		if (null != context) {
			Toast.makeText(context, info, Toast.LENGTH_SHORT).show();
		}
	}

	public static void showShort(Context context, int infoID) {
		if (null != context) {
			Toast.makeText(context, infoID, Toast.LENGTH_SHORT).show();
		}
	}

	public static void showLong(Context context, String info) {
		if (null != context) {
			Toast.makeText(context, info, Toast.LENGTH_LONG).show();
		}
	}

	public static void showLong(Context context, int infoID) {
		if (null != context) {
			Toast.makeText(context, infoID, Toast.LENGTH_LONG).show();
		}
	}

	public static void showTime(Context context, String info, int time) {
		if (null != context) {
			Toast.makeText(context, info, time).show();
		}
	}

	public static void showTime(Context context, int infoID, int time) {
		if (null != context) {
			Toast.makeText(context, infoID, time).show();
		}
	}

}
