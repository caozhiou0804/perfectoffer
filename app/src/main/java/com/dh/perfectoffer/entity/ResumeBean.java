package com.dh.perfectoffer.entity;

public class ResumeBean extends BaseBean {
	private String _id;
	private String fried_id;// 简历ID
	private String sex;// 性别
	private String name;// 姓名
	private String age;// 年龄
	private String schools;// 学校
	private String specialty;// 专业
	private String phone;// 联系电话
	private String email;// 邮箱
	private String address;// 地址
	private String work_year;// 工作年限s
	private String reservation_date;// reservation_date
	private String remark;// 备注

	@Override
	public String toString() {
		return "ResumeBean [_id=" + _id + ", fried_id=" + fried_id + ", sex="
				+ sex + ", name=" + name + ", age=" + age + ", schools="
				+ schools + ", specialty=" + specialty + ", phone=" + phone
				+ ", email=" + email + ", address=" + address + ", work_year="
				+ work_year + ", reservation_date=" + reservation_date
				+ ", remark=" + remark + "]";
	}

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

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getSchools() {
		return schools;
	}

	public void setSchools(String schools) {
		this.schools = schools;
	}

	public String getSpecialty() {
		return specialty;
	}

	public void setSpecialty(String specialty) {
		this.specialty = specialty;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getWork_year() {
		return work_year;
	}

	public void setWork_year(String work_year) {
		this.work_year = work_year;
	}

	public String getReservation_date() {
		return reservation_date;
	}

	public void setReservation_date(String reservation_date) {
		this.reservation_date = reservation_date;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
