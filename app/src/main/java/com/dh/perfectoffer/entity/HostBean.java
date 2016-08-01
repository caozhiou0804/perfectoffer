package com.dh.perfectoffer.entity;

/**
 * 公司实体类
 * 
 * @author really
 * 
 */
public class HostBean extends BaseBean {

	private String _id; // 自增主键
	private String post_name; // 自增主键

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getPost_name() {
		return post_name;
	}

	public void setPost_name(String post_name) {
		this.post_name = post_name;
	}

	@Override
	public String toString() {
		return "HostBean [_id=" + _id + ", post_name=" + post_name + "]";
	}

}
