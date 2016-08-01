package com.dh.perfectoffer.entity;

/**
 * 油价查询实体类
 * 
 * @author really
 * 
 */
public class OilBean extends BaseBean {


	private String _id; // 自增主键
	private String province;// 省份
	private String oil90;// #90
	private String oil93;// #93
	private String oil97;// #97
	private String oil0;// 柴油？
	private String addtime;// 显示日期

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getOil90() {
		return oil90;
	}

	public void setOil90(String oil90) {
		this.oil90 = oil90;
	}

	public String getOil93() {
		return oil93;
	}

	public void setOil93(String oil93) {
		this.oil93 = oil93;
	}

	public String getOil97() {
		return oil97;
	}

	public void setOil97(String oil97) {
		this.oil97 = oil97;
	}

	public String getOil0() {
		return oil0;
	}

	public void setOil0(String oil0) {
		this.oil0 = oil0;
	}

	public String getAddtime() {
		return addtime;
	}

	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}

	@Override
	public String toString() {
		return "HrVOBean [province=" + province + ", oil90=" + oil90
				+ ", oil93=" + oil93 + ", oil97=" + oil97 + ", oil0=" + oil0
				+ ", addtime=" + addtime + "]";
	}

}
