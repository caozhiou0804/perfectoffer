package com.dh.perfectoffer.entity;

import java.util.ArrayList;

public class OfficeListBean extends BaseBean {

	private ArrayList<OfficeBean> postList;
	private String lastPage;

	public ArrayList<OfficeBean> getPostList() {
		return postList;
	}

	public void setPostList(ArrayList<OfficeBean> postList) {
		this.postList = postList;
	}

	public String getLastPage() {
		return lastPage;
	}

	public void setLastPage(String lastPage) {
		this.lastPage = lastPage;
	}

}
