package com.dh.perfectoffer.entity;

import java.util.ArrayList;

public class CheckVersionBean extends BaseBean {
	private String version_lable;// 是否成功（1：更新 0：不需要更新）
	private String version_url;// 版本地址

	public String getVersion_lable() {
		return version_lable;
	}

	public void setVersion_lable(String version_lable) {
		this.version_lable = version_lable;
	}

	public String getVersion_url() {
		return version_url;
	}

	public void setVersion_url(String version_url) {
		this.version_url = version_url;
	}
}
