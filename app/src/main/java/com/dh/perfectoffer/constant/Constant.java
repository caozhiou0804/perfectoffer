package com.dh.perfectoffer.constant;

import android.os.Environment;

/**
 * @author 超级小志
 * 
 */
public class Constant {
	// 数据库名称
	public static final String DB_NAME = "DH_DB.db";
	// 本地存储名称
	public static final String PRE_OIL_NAME = "oil";
	// 本地存储名称
	public static final String PRE_HOME_NAME = "home";
	// 本地存储名称
	public static final String PRE_COMPANY_NAME = "company";
	public static final String PRE_RESUME_NAME = "resume";
	public static final String PRE_HISTROY_NAME = "histroy";
	public static final String PRE_HOTJOB_NAME = "hotjob";

	// 本地存储名称
	public static final String PRE_USER_NAME = "user";
	public static final String PRE_USER_PASSWORD = "userpassword";
	// 开发
	// public static final String URLIP = "http://192.168.111.121:8080/";
	// public static final String URLIPPIC = "http://192.168.111.121:8080/hr";
	// 测试
	// public static final String URLIP = "http://192.168.111.121:8090/";
	// public static final String URLIP_HEAD = "http://192.168.111.121:8090/hr";
	// public static final String URLIPPIC = "http://192.168.111.121:8080/hr";//
	// web端图片下载
	// public static final String URLIP = "http://218.2.208.52:9444/";
	// public static final String URLIP_HEAD = "http://218.2.208.52:9444/hr";
	// public static final String URLIPPIC = "http://218.2.208.52:9444/webhr";//
	// web端图片下载
	// 内网
//	public static final String URLIP = "http://192.168.111.3:9004/";
//	public static final String URLIP_HEAD = "http://192.168.111.3:9004/hr";
//	public static final String URLIPPIC = "http://192.168.111.3:9004/webhr";

	// 外网
	 public static final String URLIP = "http://zghgm.org:8080/";
	 public static final String URLIP_HEAD = "http://zghgm.org:8080/hr";
	 public static final String URLIPPIC = "http://zghgm.org:8080/webhr";

	// http://218.2.208.54:9444
	// APP相关信息存储位置
	public final static String DH_PATH = Environment
			.getExternalStorageDirectory().getPath() + "/wireless/";
	// 下载的文件存储地址
	public final static String DOWNLOAD_PATH = DH_PATH + "download/";
}
