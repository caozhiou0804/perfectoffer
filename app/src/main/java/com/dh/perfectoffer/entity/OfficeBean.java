package com.dh.perfectoffer.entity;

public class OfficeBean extends BaseBean {
	private String _id;
	private String post_id;// 悬赏职位ID
	private String sub_id;// 所属公司ID
	private String post_name;// 职位名称
	private String reward_salary;// 月薪
	private String reward_claim; // 职业要求，如：全职/2年/大专
	private String bounty; // 积分
	private String status; // 职位状态

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

	@Override
	public String toString() {
		return "OfficeListBean [_id=" + _id + ", post_id=" + post_id
				+ ", sub_id=" + sub_id + ", post_name=" + post_name
				+ ", reward_salary=" + reward_salary + ", reward_claim="
				+ reward_claim + ", bounty=" + bounty + ", status=" + status
				+ "]";
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
