package com.dh.perfectoffer.entity;

/**
 * 公司实体类
 * 
 * @author really
 * 
 */
public class PostBean extends BaseBean {
	// rew_id
	// sub_id
	// post_name
	// reward_salary
	// reward_claim
	// bounty
	// status
	private String _id; // 自增主键
	private String rew_id; // 职位id，职位id
	private String post_id; // 职位id，搜索职位id
	private String sub_id; // 自增主键
	private String post_name; // 自增主键
	private String reward_salary; // 自增主键
	private String reward_claim; // 自增主键
	private String bounty; // 自增主键
	private String status; // 自增主键
	private String is_hot;// 1代表热推，0代表不是热推
	private String sub_icon;// 公司icon
	private String sub_name;// 公司name

	public String getPost_id() {
		return post_id;
	}

	public void setPost_id(String post_id) {
		this.post_id = post_id;
	}

	public String getSub_icon() {
		return sub_icon;
	}

	public void setSub_icon(String sub_icon) {
		this.sub_icon = sub_icon;
	}

	public String getSub_name() {
		return sub_name;
	}

	public void setSub_name(String sub_name) {
		this.sub_name = sub_name;
	}

	public String getIs_hot() {
		return is_hot;
	}

	public void setIs_hot(String is_hot) {
		this.is_hot = is_hot;
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

	@Override
	public String toString() {
		return "PostBean [_id=" + _id + ", rew_id=" + rew_id + ", post_id="
				+ post_id + ", sub_id=" + sub_id + ", post_name=" + post_name
				+ ", reward_salary=" + reward_salary + ", reward_claim="
				+ reward_claim + ", bounty=" + bounty + ", status=" + status
				+ ", is_hot=" + is_hot + ", sub_icon=" + sub_icon
				+ ", sub_name=" + sub_name + "]";
	}

}
