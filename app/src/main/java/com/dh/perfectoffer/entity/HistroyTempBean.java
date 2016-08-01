package com.dh.perfectoffer.entity;

public class HistroyTempBean extends BaseBean {
	// post_id
	// sub_id
	// post_name
	// reward_salary
	// reward_claim
	// bounty
	// status
	// fried_name
	// bounty
	// 职位ID
	// 所属公司ID
	// 职位名称
	// 月薪
	// 职业要求，如：全职/2年/大专
	// 积分
	// 职位状态
	// 被推荐人
	// 已获取积分

	private String _id;
	private String post_id;// 职位ID
	private String sub_id;// 所属公司ID
	private String sub_name;// 所属公司

	public String getSub_name() {
		return sub_name;
	}

	public void setSub_name(String sub_name) {
		this.sub_name = sub_name;
	}

	private String post_name;// 职位名称
	private String reward_salary;// 月薪
	private String reward_claim;// 职业要求，如：全职/2年/大专
	private String bounty;// 积分
	private String status;// 职位状态
	private String fried_name;// 被推荐人
	private String bounty2;// 已获取积分
	private String create_time;// 推荐时间

	private String reservation_date;// 预约时间
	private String my_bounty;

	public String getMy_bounty() {
		return my_bounty;
	}

	public void setMy_bounty(String my_bounty) {
		this.my_bounty = my_bounty;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getPost_id() {
		return post_id;
	}

	public void setPost_id(String post_id) {
		this.post_id = post_id;
	}

	public String getSub_id() {
		return sub_id;
	}

	public void setSub_id(String sub_id) {
		this.sub_id = sub_id;
	}

	public String getPost_name() {
		return post_name;
	}

	public void setPost_name(String post_name) {
		this.post_name = post_name;
	}

	public String getReward_salary() {
		return reward_salary;
	}

	public void setReward_salary(String reward_salary) {
		this.reward_salary = reward_salary;
	}

	public String getReward_claim() {
		return reward_claim;
	}

	public void setReward_claim(String reward_claim) {
		this.reward_claim = reward_claim;
	}

	public String getBounty() {
		return bounty;
	}

	public void setBounty(String bounty) {
		this.bounty = bounty;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getFried_name() {
		return fried_name;
	}

	public void setFried_name(String fried_name) {
		this.fried_name = fried_name;
	}

	public String getBounty2() {
		return bounty2;
	}

	public void setBounty2(String bounty2) {
		this.bounty2 = bounty2;
	}

	public String getReservation_date() {
		return reservation_date;
	}

	public void setReservation_date(String reservation_date) {
		this.reservation_date = reservation_date;
	}

	@Override
	public String toString() {
		return "HistroyTempBean [_id=" + _id + ", post_id=" + post_id
				+ ", sub_id=" + sub_id + ", sub_name=" + sub_name
				+ ", post_name=" + post_name + ", reward_salary="
				+ reward_salary + ", reward_claim=" + reward_claim
				+ ", bounty=" + bounty + ", status=" + status + ", fried_name="
				+ fried_name + ", bounty2=" + bounty2 + ", create_time="
				+ create_time + ", reservation_date=" + reservation_date + "]";
	}

}
