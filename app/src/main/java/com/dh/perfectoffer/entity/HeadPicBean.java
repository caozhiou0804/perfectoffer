package com.dh.perfectoffer.entity;

/**
 * 头像实体类
 * 
 * @author really
 * 
 */
public class HeadPicBean extends BaseBean {

	private String _id; // 自增主键
	private String pic_url; // 自增主键

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getPic_url() {
		return pic_url;
	}

	public void setPic_url(String pic_url) {
		this.pic_url = pic_url;
	}

}
