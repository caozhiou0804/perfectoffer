package com.dh.perfectoffer.entity;

import java.util.ArrayList;

/**
 * 首页实体类
 * 
 * @author really
 * 
 */
public class homeBean extends BaseBean {
	// comList
	// postList
	// hotList

	private String _id; // 自增主键
	private ArrayList<ComBean> comList; // 自增主键
	private ArrayList<PostBean> postList; // 自增主键
	private ArrayList<HostBean> hotList; // 自增主键

	@Override
	public String toString() {
		return "homeBean [_id=" + _id + ", comList=" + comList + ", postList="
				+ postList + ", hotList=" + hotList + "]";
	}

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public ArrayList<ComBean> getComList() {
		return comList;
	}

	public void setComList(ArrayList<ComBean> comList) {
		this.comList = comList;
	}

	public ArrayList<PostBean> getPostList() {
		return postList;
	}

	public void setPostList(ArrayList<PostBean> postList) {
		this.postList = postList;
	}

	public ArrayList<HostBean> getHotList() {
		return hotList;
	}

	public void setHotList(ArrayList<HostBean> hotList) {
		this.hotList = hotList;
	}

}
