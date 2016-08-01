package com.dh.perfectoffer.dhutils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;
import android.view.View;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 通用工具方法类
 * 
 * @author 超级小志 2015-5-28
 * 
 */
@SuppressLint("NewApi")
public class CommUtil {

	private static CommUtil commUtil = null;

	public static CommUtil getInstance() {
		if (null == commUtil) {
			commUtil = new CommUtil();
		}
		return commUtil;
	}

	public String getExternCachePath() {
		String path = Environment.getExternalStorageDirectory()
				.getAbsolutePath();
		if (path != null) {
			path += "/data/com.dh/cache/";
			File file = new File(path);
			if (!file.exists()) {
				if (file.mkdirs()) {
					return path;
				}
			} else {
				return path;
			}
		}
		return null;
	}

	public boolean hasSdcard() {
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	public static int getVersionCode(Activity activity) {
		int versionCode = 0;
		try {
			PackageInfo info = activity.getPackageManager().getPackageInfo(
					activity.getPackageName(), 0);
			// 当前应用的版本名称
			versionCode = info.versionCode;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return versionCode;
	}

	public boolean containsEmoji(String source) {
		int len = source.length();
		for (int i = 0; i < len; i++) {
			char codePoint = source.charAt(i);
			if (!isEmojiCharacter(codePoint)) {
				// 如果不能匹配,则该字符是Emoji表情 return

				return true; // true;
				// } } return false; }
			}
		}
		return false;

	}
	
	
	public boolean isMail(String email) {
		String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(email);

		return m.matches();
		}

	public boolean isEmail(String email) {
		final Pattern emailer = Pattern
				.compile("^([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)*@([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)+[\\.][A-Za-z]{2,3}([\\.][A-Za-z]{2})?$");

		if (email == null || email.trim().length() == 0)

			return false;

		return emailer.matcher(email).matches();
	}

	public boolean isPhoneNumber(String phone) {

		String regExp = "^(0|86|17951)?(13[0-9]|15[012356789]|17[0-9]|18[0-9]|14[57])[0-9]{8}$";

		Pattern p = Pattern.compile(regExp);

		Matcher m = p.matcher(phone);
		return m.find();

	}

	public void isLock(View v, boolean value) {
		v.setClickable(value);
		v.setFocusable(value);
		v.setEnabled(value);
	}

	public String getNomalStr(String source) {
		int len = source.length();
		String temp = "";
		for (int i = 0; i < len; i++) {
			char codePoint = source.charAt(i);
			if (isEmojiCharacter(codePoint)) { // 如果不能匹配,则该字符是Emoji表情 return
				// source=source.replace(source.substring(i, i + 1), "");
				temp = temp + source.substring(i, i + 1);
				// } } return false; }
			} else
				temp = temp + "@";
		}
		return temp;
	}

	/** * 判断是否是Emoji * @param codePoint 比较的单个字符 * @return */
	private boolean isEmojiCharacter(char codePoint) {
		return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA)
				|| (codePoint == 0xD)
				|| ((codePoint >= 0x20) && (codePoint <= 0xD7FF))
				|| ((codePoint >= 0xE000) && (codePoint <= 0xFFFD))
				|| ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
	}

	// 获取String.xml中的文字资源，RId代表string 的对应id
	public String getStringFromXml(Context context, int RId) {
		String str = "";
		str = context.getResources().getString(RId);
		return str;
	}
}
