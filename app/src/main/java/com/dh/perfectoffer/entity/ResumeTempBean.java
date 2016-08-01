package com.dh.perfectoffer.entity;

public class ResumeTempBean extends BaseBean {

	private String _id;
	private String fried_id;
	private String friend_name;

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getFried_id() {
		return fried_id;
	}

	public void setFried_id(String fried_id) {
		this.fried_id = fried_id;
	}

	public String getFriend_name() {
		return friend_name;
	}

	public void setFriend_name(String friend_name) {
		this.friend_name = friend_name;
	}

	@Override
	public String toString() {
		return "ResumeTempBean [_id=" + _id + ", fried_id=" + fried_id
				+ ", friend_name=" + friend_name + "]";
	}

}
