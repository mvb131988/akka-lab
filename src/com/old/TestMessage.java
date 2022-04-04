package com.old;

public class TestMessage {

	private long id;
	private String message;

	public TestMessage() {
	}
	
	public TestMessage(long id) {
		super();
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
