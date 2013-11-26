package com.nero.howmuch.seller;

import java.util.ArrayList;

public class Seller {
	public static final String SELLER_REGISTER = "seller_register";
	public static final String SELLER_UPDATE = "seller_update";
	public static final String SELLER_CHECKNAME = "seller_checkname";
	public static final String SELLER_ADD_CATEGORY = "seller_add_category";
	public static final String SELLER_REMOVE_CATEGORY = "seller_remove_category";
	public static final String SELLER_REQUEST_POST = "seller_request_post";
	public static final String SELLER_SEND_REPLY = "seller_send_reply";
	public static final String SELLER_UPDATE_GCM = "seller_update_gcm";
	public static final String SELLER_REQUEST_REPLY = "seller_request_reply";
	public static final String SELLER_CHARGE_PLANE = "seller_charge_plane";
	public static final String SELLER_LOGIN = "seller_login";
	public static final String SELLER_LOGOUT = "seller_logout";
	public static final String SELLER_DELETE = "seller_delete";
	private String seller_name;
	private String seller_num;
	private String seller_pw;
	private String seller_city;
	private String seller_address;
	private String seller_phone;
	private String gcm_id;
	private ArrayList<String> category=new ArrayList<String>();
	private int plane;
	public String getSeller_name() {
		return seller_name;
	}
	public void setSeller_name(String seller_name) {
		this.seller_name = seller_name;
	}
	public String getSeller_num() {
		return seller_num;
	}
	public void setSeller_num(String seller_num) {
		this.seller_num = seller_num;
	}
	public String getSeller_address() {
		return seller_address;
	}
	public void setSeller_address(String seller_address) {
		this.seller_address = seller_address;
	}
	public String getSeller_phone() {
		return seller_phone;
	}
	public void setSeller_phone(String seller_phone) {
		this.seller_phone = seller_phone;
	}
	public String getGcm_id() {
		return gcm_id;
	}
	public void setGcm_id(String gcm_id) {
		this.gcm_id = gcm_id;
	}
	public String getSeller_city() {
		return seller_city;
	}
	public void setSeller_city(String seller_city) {
		this.seller_city = seller_city;
	}
	public ArrayList<String> getCategory() {
		return category;
	}
	public void setCategory(ArrayList<String> category) {
		this.category = category;
	}
	public int getPlane() {
		return plane;
	}
	public void setPlane(int plane) {
		this.plane = plane;
	}
	public String getSeller_pw() {
		return seller_pw;
	}
	public void setSeller_pw(String seller_pw) {
		this.seller_pw = seller_pw;
	}
}
