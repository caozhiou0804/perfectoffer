package com.dh.perfectoffer;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;

import com.dh.perfectoffer.constant.Constant;
import com.dh.perfectoffer.entity.UserBean;
import com.dh.perfectoffer.event.framework.db.AfeiDb;
import com.dh.perfectoffer.event.framework.exception.CrashHandler;
import com.dh.perfectoffer.xutils.sample.utils.Preference;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class VehicleApp extends Application {
	private static VehicleApp instance;

	private AfeiDb afeiDb;
	private UserBean userBean;

	public UserBean getUserBean() {
		if (null == userBean) {
			List<UserBean> useList = new ArrayList<UserBean>();
			useList = afeiDb.findAll(UserBean.class);
			if (null != useList && useList.size() > 0) {
				userBean = useList.get(0);
			}
		}
		return userBean;
	}

	public void setUserBean(UserBean userBean) {
		this.userBean = userBean;
	}

	public void updateUserBean() {
		List<UserBean> useList = new ArrayList<UserBean>();
		useList = afeiDb.findAll(UserBean.class);
		if (null != useList && useList.size() > 0) {
			setUserBean(useList.get(0));
		}

	}

	private List<Activity> activitys = new LinkedList<Activity>();

	private List<Activity> activityList = new LinkedList<Activity>();

	// 设置登录前的activity
	private Activity activity;

	private boolean openCrashHandler = false;

	public boolean appSwitch = false;

	private boolean isShowAssistant = true;

	public void onCreate() {
		super.onCreate();
		instance = this;
		afeiDb = AfeiDb.create(this, Constant.DB_NAME, true);

		// 全局捕获异常错误信息存至SD卡
		if (openCrashHandler) {
			CrashHandler crashHandler = CrashHandler.getInstance();
			crashHandler.init(getApplicationContext());
		}
		// 初始化 异步加载图片
		initImageLoader(getApplicationContext());
	}

	public static VehicleApp getInstance() {
		return instance;
	}

	private void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you
		// may tune some of them,
		// or you can create default configuration by
		// ImageLoaderConfiguration.createDefault(this);
		// method.
		// ImageLoaderConfiguration config = new
		// ImageLoaderConfiguration.Builder(
		// context).threadPriority(Thread.NORM_PRIORITY - 2)
		// .denyCacheImageMultipleSizesInMemory()
		// .discCacheFileNameGenerator(new Md5FileNameGenerator())
		// .tasksProcessingOrder(QueueProcessingType.LIFO)
		// .writeDebugLogs() // Remove for release app
		// .build();
		// // Initialize ImageLoader with configuration.
		// ImageLoader.getInstance().init(config);
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
				.cacheInMemory(false)
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
				.bitmapConfig(Bitmap.Config.RGB_565)// 防止内存溢出的，图片太多就这这个。还有其他设置
				.cacheOnDisc(true).build();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context)
				.threadPoolSize(3)
				// default
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.denyCacheImageMultipleSizesInMemory()
				// .memoryCache(new LruMemoryCache((int) (6 * 1024 * 1024)))
				.memoryCache(new WeakMemoryCache())
				.memoryCacheSize((int) (2 * 1024 * 1024))
				.memoryCacheSizePercentage(13)
				// default
				// .discCache(new UnlimitedDiscCache(cacheDir))
				// default
				.discCacheSize(50 * 1024 * 1024).discCacheFileCount(100)
				.discCacheFileNameGenerator(new HashCodeFileNameGenerator())
				.defaultDisplayImageOptions(defaultOptions).writeDebugLogs() // Remove
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}

	// 添加Activity到容器中
	public void addActivity(Activity activity) {
		if (activitys != null && activitys.size() > 0) {
			if (!activitys.contains(activity)) {
				activitys.add(activity);
			}
		} else {
			activitys.add(activity);
		}

	}

	public List<Activity> getActivitys() {
		return activitys;
	}

	public void exit() {
		if (activitys != null && activitys.size() > 0) {
			for (Activity activity : activitys) {
				activity.finish();
			}
		}
		System.exit(0);

		// 直接在服务中，暂停了。。。

		// 以便下次打开下载管理时，可以置成下载状态中。。。。
		// 发送广播，让其全部暂停掉下载...
		/*
		 * sendBroadcast(new
		 * Intent(DownloadService.PAUSEALLRECEIVER_ACTION).putExtra("isExit",
		 * true));
		 */
		// Log.i(TAG, "发送广播方式退出了.....");
		// 怎么停止掉服务...

		// Process.killProcess(Process.myPid());//停止掉本应用进程，即退出了应用
	}

	public void finishAll() {
		if (activitys != null && activitys.size() > 0) {
			for (Activity activity : activitys) {
				activity.finish();
			}
		}
	}

	public AfeiDb getAfeiDb() {
		return afeiDb;
	}

	// 添加Activity到容器中
	public void addActivityToList(Activity activity) {
		if (activityList != null && activityList.size() > 0) {
			if (!activityList.contains(activity)) {
				activityList.add(activity);
			}
		} else {
			activityList.add(activity);
		}

	}

	public List<Activity> getActivityLists() {
		return activityList;
	}

	public void finishAllActivityLists() {
		if (activityList != null && activityList.size() > 0) {
			for (Activity activity : activityList) {
				activity.finish();
			}
		}
	}

	public String getVehicleCity() {
		return Preference.getString("vehicleCity");
	}

	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	public boolean isAppSwitch() {
		return appSwitch;
	}

	public void setAppSwitch(boolean appSwitch) {
		this.appSwitch = appSwitch;
	}

	public void setShowAssistant(boolean isShowAssistant) {
		this.isShowAssistant = isShowAssistant;
	}

	public boolean isShowAssistant() {
		return isShowAssistant;
	}

}
