package com.dh.perfectoffer.entity;

import java.util.ArrayList;

public class HistroyListBean extends BaseBean {

	private String lastPage;

	public String getLastPage() {
		return lastPage;
	}

	public void setLastPage(String lastPage) {
		this.lastPage = lastPage;
	}

	private ArrayList<HistroyTempBean> postListHistory;

	public ArrayList<HistroyTempBean> getPostListHistory() {
		return postListHistory;
	}

	public void setPostListHistory(ArrayList<HistroyTempBean> postListHistory) {
		this.postListHistory = postListHistory;
	}

	@Override
	public String toString() {
		return "HistroyListBean [postListHistory=" + postListHistory + "]";
	}

}
