package com.dh.perfectoffer.entity;

/**
 * 悬赏职位实体类
 * 
 * @author really
 * 
 */
public class RewardBean extends BaseBean {

	private String _id; // 自增主键
	private String rew_id;//
	private String post_name;//
	private String reward_salary;//
	private String create_time;//
	private String sub_name;//
	private String bounty;//

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

	public String getReward_salary() {
		return reward_salary;
	}

	public void setReward_salary(String reward_salary) {
		this.reward_salary = reward_salary;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public String getSub_name() {
		return sub_name;
	}

	public void setSub_name(String sub_name) {
		this.sub_name = sub_name;
	}

	public String getBounty() {
		return bounty;
	}

	public void setBounty(String bounty) {
		this.bounty = bounty;
	}

}
