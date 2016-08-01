package com.dh.perfectoffer.entity;

/**
 * 积分BEAN
 * 
 * @author tongyq 2015.11.18
 * 
 */
public class IntegralBean extends BaseBean {

	// 自增主键
	private String _id;
	// 用户ID
	private String user_id;
	//用户名
	private String user_name;
	//操作详情
	private String details;
	//积分变动
	private String jifen;
	//操作人
	private String creater;
	//时间
	private String create_date;
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		this.details = details;
	}
	public String getJifen() {
		return jifen;
	}
	public void setJifen(String jifen) {
		this.jifen = jifen;
	}
	public String getCreater() {
		return creater;
	}
	public void setCreater(String creater) {
		this.creater = creater;
	}
	
	public String getCreate_date() {
		return create_date;
	}
	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}
	
	@Override
	public String toString() {
		return "IntegralBean [_id=" + _id + ", user_id=" + user_id
				+ ", user_name=" + user_name + ", details=" + details
				+ ", jifen=" + jifen + ", creater=" + creater
				+ ", create_date=" + create_date + "]";
	}
	
	
}
