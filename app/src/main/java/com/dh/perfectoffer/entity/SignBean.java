package com.dh.perfectoffer.entity;

public class SignBean extends BaseBean {

	private static final long serialVersionUID = 1L;

	private String message;

	private String error;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

}
