package com.old;

public class ReadTestMessageResponse {

	public ReadTestMessageResponse() {
	}
	
	public ReadTestMessageResponse(String message) {
		super();
		this.message = message;
	}

	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
