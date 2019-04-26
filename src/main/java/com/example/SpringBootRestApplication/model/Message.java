package com.example.SpringBootRestApplication.model;

public class Message {
	private Long errorcode;
	private MessageType type;
	private String message;
	
	private Message(Builder builder) {
		this.errorcode = builder.errorcode;
		this.type = builder.type;
		this.message = builder.message;
	}
	
	public Long getErrorcode() {
		return errorcode;
	}

	public MessageType getType() {
		return type;
	}

	public String getMessage() {
		return message;
	}

	public static class Builder{
		private Long errorcode;
		private MessageType type;
		private String message;
		
		public Builder(long errorcode,MessageType type,String message) {
			this.errorcode = errorcode;
			this.message = message;
			this.type = type;
		}
		
		public Message build() {
			return new Message(this);
		}
	}
}
