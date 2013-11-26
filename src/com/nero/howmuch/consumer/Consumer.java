package com.nero.howmuch.consumer;

public class Consumer {
	public static final String CONSUMER_REGISTER = "consumer_register";
	public static final String CONSUMER_UPDATE_GCM = "consumer_update_gcm";
	public static final String CONSUMER_CHECKNAME = "consumer_checkname";
	public static final String CONSUMER_POST = "consumer_post";
	public static final String CONSUMER_REQUEST_REPLY = "consumer_request_reply";
	public static final String CONSUMER_SEND_REPLY = "consumer_send_reply";
	public static final String CONSUMER_LOGOUT = "consumer_logout";
	private String Consumer_name;
	private String gcm_id;
	public String getConsumer_name() {
		return Consumer_name;
	}

	public void setConsumer_name(String consumer_name) {
		Consumer_name = consumer_name;
	}

	public String getGcm_id() {
		return gcm_id;
	}

	public void setGcm_id(String gcm_id) {
		this.gcm_id = gcm_id;
	}
	
}
