package com.dh.perfectoffer.dhutils;

import android.annotation.SuppressLint;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import com.dh.perfectoffer.R;
import com.dh.perfectoffer.constant.Constant;
import com.dh.perfectoffer.event.framework.log.L;
import com.dh.perfectoffer.xutils.sample.utils.ToastUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * @description 升级APP业务处理
 * 
 * @version 1.0
 * 
 * @author yangfei
 * 
 * @update 2014-05-26
 * 
 * @company www.petalways.com
 */
@SuppressLint("HandlerLeak")
public class UpgradeAppBL {
	/* 下载中 */
	private static final int DOWNLOAD = 1;
	/* 下载结束 */
	private static final int DOWNLOAD_FINISH = 2;
	/* 下载失败 */
	private static final int DOWNLOAD_FAILED = 3;
	/* 安装失败 */
	private static final int INSTALL_FAILED = 4;
	/* 下载保存路径 */
	private String mSavePath = Constant.DOWNLOAD_PATH;
	private String mDownLoadPath = Constant.DH_PATH;
	/* 记录进度条数量 */
	private int progress;
	/* 是否取消更新 */
	private boolean cancelUpdate = false;

	private Context mContext;
	/* 更新进度条 */
	private ProgressBar mProgress;

	private String fileName = null;

	private Dialog mDownloadDialog;

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			// 安装失败
			case INSTALL_FAILED:
				// 设置进度条位置
				ToastUtil.showLong(
						mContext,
						mContext.getResources().getString(
								R.string.update_notfoundfile));
				break;
			// 下载失败
			case DOWNLOAD_FAILED:
				// 设置进度条位置
				ToastUtil.showLong(
						mContext,
						mContext.getResources().getString(
								R.string.update_download_fail));
				break;
			// 正在下载
			case DOWNLOAD:
				// 设置进度条位置
				mProgress.setProgress(progress);
				break;
			case DOWNLOAD_FINISH:
				// 安装文件
				installApk();
				break;
			default:
				break;
			}
		};
	};

	/**
	 * 显示软件下载对话框
	 */
	public void showDownloadDialog(String downloadUrl) {
		// 构造软件下载对话框
		Builder builder = new Builder(mContext);
		builder.setTitle(mContext.getResources().getString(
				R.string.update_dialog_check));
		// 给下载对话框增加进度条
		final LayoutInflater inflater = LayoutInflater.from(mContext);
		View v = inflater.inflate(R.layout.widget_softupdate_progress, null);
		mProgress = (ProgressBar) v.findViewById(R.id.update_progress);
		builder.setView(v);
		builder.setCancelable(false);
		// 取消更新
		builder.setNegativeButton(
				mContext.getResources().getString(R.string.cancel),
				new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						// 设置取消状态
						cancelUpdate = true;
					}
				});
		mDownloadDialog = builder.create();
		mDownloadDialog.show();
		// 下载文件
		downloadApk(downloadUrl);
	}

	public UpgradeAppBL(Context context) {
		this.mContext = context;
	}

	/**
	 * 下载apk文件
	 */
	private void downloadApk(String downloadUrl) {
		// 启动新线程下载软件
		new downloadApkThread(downloadUrl).start();
	}

	/**
	 * 下载文件线程
	 * 
	 */
	@SuppressLint("SimpleDateFormat")
	private class downloadApkThread extends Thread {

		private String downloadUrl = null;

		public downloadApkThread(String downloadUrl) {
			this.downloadUrl = downloadUrl;
		}

		@Override
		public void run() {
			try {
				// 判断SD卡是否存在，并且是否具有读写权限
				if (Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {
					// 获得存储卡的路径
					URL url = new URL(this.downloadUrl);
					// 创建连接
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					conn.connect();
					// 获取文件大小
					int length = conn.getContentLength();
					// 创建输入流
					InputStream is = conn.getInputStream();

					File fileDown = new File(mDownLoadPath);
					// 判断文件目录是否存在
					if (!fileDown.exists()) {
						fileDown.mkdir();
					}
					File file = new File(mSavePath);
					// 判断文件目录是否存在
					if (!file.exists()) {
						file.mkdir();
					}
					fileName = getApkName();
					File apkFile = new File(mSavePath, fileName);
					FileOutputStream fos = new FileOutputStream(apkFile);
					int count = 0;
					// 缓存
					byte buf[] = new byte[1024];
					// 写入到文件中
					do {
						int numread = is.read(buf);
						count += numread;
						// 计算进度条位置
						progress = (int) (((float) count / length) * 100);
						// 更新进度
						mHandler.sendEmptyMessage(DOWNLOAD);
						if (numread <= 0) {
							// 下载完成
							mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
							break;
						}
						// 写入文件
						fos.write(buf, 0, numread);
					} while (!cancelUpdate);// 点击取消就停止下载.
					fos.close();
					is.close();
				} else {
					mHandler.sendEmptyMessage(DOWNLOAD_FAILED);
				}
			} catch (MalformedURLException e) {
				L.e("yinzl", "MalformedURLException:" + e);
				mHandler.sendEmptyMessage(DOWNLOAD_FAILED);
			} catch (IOException e) {
				L.e("yinzl", "IOException:" + e);
				mHandler.sendEmptyMessage(DOWNLOAD_FAILED);
			} finally {
				// 取消下载对话框显示
				mDownloadDialog.dismiss();
			}
		}
	};

	@SuppressLint("SimpleDateFormat")
	private String getApkName() {
		String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date(System
				.currentTimeMillis()));
		fileName = "perfectoffer-" + date + ".apk";
		return fileName;
	}

	/**
	 * 安装APK文件
	 */
	private void installApk() {
		File apkfile = new File(mSavePath, this.fileName);
		if (!apkfile.exists()) {
			mHandler.sendEmptyMessage(INSTALL_FAILED);
			return;
		}
		// 通过Intent安装APK文件
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.parse("file://" + apkfile.toString()),
				"application/vnd.android.package-archive");
		mContext.startActivity(intent);
	}
}
