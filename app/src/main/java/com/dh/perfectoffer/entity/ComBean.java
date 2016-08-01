package com.dh.perfectoffer.entity;

/**
 * 公司实体类
 * 
 * @author really
 * 
 */
public class ComBean extends BaseBean {

	// sub_id
	// sub_name
	// sub_type
	// sub_phone
	// sub_address
	// sub_details
	// sub_icon
	// sub_num
	// sub_status

	private String _id; // 自增主键
	private String sub_id; // 公司ID
	private String sub_name; // 公司name
	private String sub_type; // 公司性质

	private String sub_phone; // 公司电话

	private String sub_address; // 公司地址

	private String sub_details; // 公司详情

	private String sub_icon; // 公司ICON，URL

	private String sub_num; // 公司发布职位数

	private String sub_status; // 推荐状态

	@Override
	public String toString() {
		return "ComBean [_id=" + _id + ", sub_id=" + sub_id + ", sub_name="
				+ sub_name + ", sub_type=" + sub_type + ", sub_phone="
				+ sub_phone + ", sub_address=" + sub_address + ", sub_details="
				+ sub_details + ", sub_icon=" + sub_icon + ", sub_num="
				+ sub_num + ", sub_status=" + sub_status + "]";
	}

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getSub_id() {
		return sub_id;
	}

	public void setSub_id(String sub_id) {
		this.sub_id = sub_id;
	}

	public String getSub_name() {
		return sub_name;
	}

	public void setSub_name(String sub_name) {
		this.sub_name = sub_name;
	}

	public String getSub_type() {
		return sub_type;
	}

	public void setSub_type(String sub_type) {
		this.sub_type = sub_type;
	}

	public String getSub_phone() {
		return sub_phone;
	}

	public void setSub_phone(String sub_phone) {
		this.sub_phone = sub_phone;
	}

	public String getSub_address() {
		return sub_address;
	}

	public void setSub_address(String sub_address) {
		this.sub_address = sub_address;
	}

	public String getSub_details() {
		return sub_details;
	}

	public void setSub_details(String sub_details) {
		this.sub_details = sub_details;
	}

	public String getSub_icon() {
		return sub_icon;
	}

	public void setSub_icon(String sub_icon) {
		this.sub_icon = sub_icon;
	}

	public String getSub_num() {
		return sub_num;
	}

	public void setSub_num(String sub_num) {
		this.sub_num = sub_num;
	}

	public String getSub_status() {
		return sub_status;
	}

	public void setSub_status(String sub_status) {
		this.sub_status = sub_status;
	}

}
