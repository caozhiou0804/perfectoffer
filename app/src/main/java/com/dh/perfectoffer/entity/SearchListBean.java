package com.dh.perfectoffer.entity;

import java.util.ArrayList;

public class SearchListBean extends BaseBean {
	private String lastPage;

	public String getLastPage() {
		return lastPage;
	}

	public void setLastPage(String lastPage) {
		this.lastPage = lastPage;
	}

	private ArrayList<PostBean> postListSearch;

	public ArrayList<PostBean> getPostListSearch() {
		return postListSearch;
	}

	public void setPostListSearch(ArrayList<PostBean> postListSearch) {
		this.postListSearch = postListSearch;
	}

}
