package com;

public class OrderMessageResponse implements Message {

	private String response;
	
	public OrderMessageResponse() {}
	
	public OrderMessageResponse(String response) {
		super();
		this.response = response;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}
	
}
