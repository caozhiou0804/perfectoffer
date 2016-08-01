package com.dh.perfectoffer.entity;

public class CompanyListBean extends BaseBean{
	private String _id; // 自增主键
	private String id;
	private String name;
	private String number;
	private String content;
	private String icon;
	@Override
	public String toString() {
		return "CompanyListBean [_id=" + _id + ", id=" + id + ", name=" + name
				+ ", number=" + number + ", content=" + content + ", icon="
				+ icon + "]";
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	

}
