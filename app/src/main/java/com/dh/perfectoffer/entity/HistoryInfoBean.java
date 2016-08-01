package com.dh.perfectoffer.entity;

public class HistoryInfoBean extends BaseBean {
	private String _id;
	private String rew_id;//悬赏职位ID
	private String post_name; //职位名称
	private String create_time;//发布时间
	private String bounty;//积分
	private String reward_title;//职位标题
	private String reward_salary;//月薪
	private String reward_welfare;//福利 
	private String sub_address;//公司地址
	private String rules_value;//积分规则
	private String reward_claim;//要求
	private String reward_claim_details;//要求详情

	public String getReward_claim_details() {
		return reward_claim_details;
	}

	public void setReward_claim_details(String reward_claim_details) {
		this.reward_claim_details = reward_claim_details;
	}

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getRew_id() {
		return rew_id;
	}

	public void setRew_id(String rew_id) {
		this.rew_id = rew_id;
	}

	public String getPost_name() {
		return post_name;
	}

	public void setPost_name(String post_name) {
		this.post_name = post_name;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public String getBounty() {
		return bounty;
	}

	public void setBounty(String bounty) {
		this.bounty = bounty;
	}

	public String getReward_title() {
		return reward_title;
	}

	public void setReward_title(String reward_title) {
		this.reward_title = reward_title;
	}

	public String getReward_salary() {
		return reward_salary;
	}

	public void setReward_salary(String reward_salary) {
		this.reward_salary = reward_salary;
	}

	public String getReward_welfare() {
		return reward_welfare;
	}

	public void setReward_welfare(String reward_welfare) {
		this.reward_welfare = reward_welfare;
	}

	public String getSub_address() {
		return sub_address;
	}

	public void setSub_address(String sub_address) {
		this.sub_address = sub_address;
	}

	public String getRules_value() {
		return rules_value;
	}

	public void setRules_value(String rules_value) {
		this.rules_value = rules_value;
	}

	public String getReward_claim() {
		return reward_claim;
	}

	public void setReward_claim(String reward_claim) {
		this.reward_claim = reward_claim;
	}

	@Override
	public String toString() {
		return "HistoryInfoBean [_id=" + _id + ", rew_id=" + rew_id
				+ ", post_name=" + post_name + ", create_time=" + create_time
				+ ", bounty=" + bounty + ", reward_title=" + reward_title
				+ ", reward_salary=" + reward_salary + ", reward_welfare="
				+ reward_welfare + ", sub_address=" + sub_address
				+ ", rules_value=" + rules_value + ", reward_claim="
				+ reward_claim + ", reward_claim_details="
				+ reward_claim_details + "]";
	}



}
