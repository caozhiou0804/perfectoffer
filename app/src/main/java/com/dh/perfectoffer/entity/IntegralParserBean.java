package com.dh.perfectoffer.entity;

import java.util.ArrayList;

public class IntegralParserBean extends BaseBean {

	//总页数
	private int total;
	//当前页数
	private int page;
	//返回集合
	private ArrayList<IntegralBean> rows;
	//总记录数
	private String records;
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public ArrayList<IntegralBean> getRows() {
		return rows;
	}
	public void setRows(ArrayList<IntegralBean> rows) {
		this.rows = rows;
	}
	public String getRecords() {
		return records;
	}
	public void setRecords(String records) {
		this.records = records;
	}
	
	
}
