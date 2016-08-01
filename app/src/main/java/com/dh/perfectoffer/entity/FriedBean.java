package com.dh.perfectoffer.entity;

/**
 * 公司实体类
 * 
 * @author really
 * 
 */
public class FriedBean extends BaseBean {

	private String _id; // 自增主键
	private String fried_id; // 返回的friedid

	@Override
	public String toString() {
		return "FriedBean [_id=" + _id + ", fried_id=" + fried_id + "]";
	}

	public String getFried_id() {
		return fried_id;
	}

	public void setFried_id(String fried_id) {
		this.fried_id = fried_id;
	}

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

}
