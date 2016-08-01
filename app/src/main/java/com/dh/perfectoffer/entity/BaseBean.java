package com.dh.perfectoffer.entity;

import java.io.Serializable;

/**
 * @author 超级小志
 * 
 */
public class BaseBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private String success; // 是否成功（1：成功 0：失败）
	private String errorMsg; // 错误信息

	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "BaseBean [success=" + success + ", errorMsg=" + errorMsg + "]";
	}

}
