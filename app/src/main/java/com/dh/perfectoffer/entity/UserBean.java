package com.dh.perfectoffer.entity;

public class UserBean extends BaseBean {
	private String _id;
	private String id;
	private String usertosk;
	private String user_pic;
	private String rname;
	private String user_phone;
	private String user_age;
	private String bounty;
	private String user_sex;

	@Override
	public String toString() {
		return "UserBean [_id=" + _id + ", id=" + id + ", usertosk=" + usertosk
				+ ", user_pic=" + user_pic + ", rname=" + rname
				+ ", user_phone=" + user_phone + ", user_age=" + user_age
				+ ", bounty=" + bounty + ", user_sex=" + user_sex + "]";
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

	public String getUsertosk() {
		return usertosk;
	}

	public void setUsertosk(String usertosk) {
		this.usertosk = usertosk;
	}

	public String getUser_pic() {
		return user_pic;
	}

	public void setUser_pic(String user_pic) {
		this.user_pic = user_pic;
	}

	public String getRname() {
		return rname;
	}

	public void setRname(String rname) {
		this.rname = rname;
	}

	public String getUser_phone() {
		return user_phone;
	}

	public void setUser_phone(String user_phone) {
		this.user_phone = user_phone;
	}

	public String getUser_age() {
		return user_age;
	}

	public void setUser_age(String user_age) {
		this.user_age = user_age;
	}

	public String getBounty() {
		return bounty;
	}

	public void setBounty(String bounty) {
		this.bounty = bounty;
	}

	public String getUser_sex() {
		return user_sex;
	}

	public void setUser_sex(String user_sex) {
		this.user_sex = user_sex;
	}

}
