package com.dh.perfectoffer.event;

public class ComEvent {

	public static final int HISTROY = 1;// 刷新推荐历史
	public static final int RESUME = 2;// 刷新推荐历史
	public static final int USERINFO = 3;// 刷新个人信息
	public static final int USERLOGIN = 4;// 刷新更多
	private int type;

	public ComEvent(int type) {
		this.type = type;
	}

	public int getType() {
		return type;
	}
}
