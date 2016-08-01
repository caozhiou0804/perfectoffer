package com.dh.perfectoffer.entity;

/**
 * 违章查询实体类
 * 
 * @author really
 * 
 */
public class IllegalBean extends BaseBean {

	private String _id; // 自增主键
	// private String carorg;// 查询的交管局
	// private String lsprefix;// 车牌前缀
	// private String lsnum;// 车牌
	// private String lstype;// 车辆类型
	// private String engineno;// 发动机号
	// private String frameno;// 车架号
	private String time;//
	private String address;// 地点
	private String content;// 违章内容
	private String number;// 违章编号
	private String agency;// 采集机关
	private String price;// 罚款金额
	private String legalnum;//
	private String score;// 扣分

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getAgency() {
		return agency;
	}

	public void setAgency(String agency) {
		this.agency = agency;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getLegalnum() {
		return legalnum;
	}

	public void setLegalnum(String legalnum) {
		this.legalnum = legalnum;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

}
