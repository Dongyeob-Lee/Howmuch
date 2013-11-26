package com.nero.howmuch;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public class Reply implements Parcelable{
	public static final int PARENT = 0;
	public static final int CHILD = 1;
	private int id;
	private int post_id;
	private String consumer_name;
	private String seller_name;
	private String date;
	private String contents;
	private ArrayList<String> reply_images_name = new ArrayList<String>();
	private int send_flag; //if send flag is '0' , this reply is received, else if flag is '1', this reply is sended by me.
	private String seller_num;
	private String seller_phone;
	private int pflag;
	private int parent;
	public Reply(){}
	
	public Reply(Reply reply){
		super();
		this.id = reply.getId();
		this.post_id = reply.getPost_id();
		this.consumer_name = reply.getConsumer_name();
		this.seller_name = reply.getSeller_name();
		this.date = reply.getDate();
		this.contents = reply.getContents();
		this.reply_images_name = reply.getReply_images_name();
		this.send_flag = reply.getSend_flag();
		this.seller_num = reply.getSeller_num();
		this.seller_phone = reply.getSeller_phone();
		this.pflag = reply.getPflag();
		this.parent = reply.getParent();
	}
	public Reply(Parcel in){
		readFromParcel(in);
	}
	private void readFromParcel(Parcel in){
		id = in.readInt();
		post_id = in.readInt();
		consumer_name = in.readString();
		seller_name = in.readString();
		date = in.readString();
		contents = in.readString();
		in.readStringList(reply_images_name);
		send_flag = in.readInt();
		seller_num = in.readString();
		seller_phone = in.readString();
		pflag = in.readInt();
		parent = in.readInt();
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeInt(post_id);
		dest.writeString(consumer_name);
		dest.writeString(seller_name);
		dest.writeString(date);
		dest.writeString(contents);
		dest.writeStringList(reply_images_name);
		dest.writeInt(send_flag);
		dest.writeString(seller_num);
		dest.writeString(seller_phone);
		dest.writeInt(pflag);
		dest.writeInt(parent);
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Parcelable.Creator<Reply> CREATOR = new Parcelable.Creator() {

		@Override
		public Object createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new Reply(source);
		}

		@Override
		public Object[] newArray(int size) {
			// TODO Auto-generated method stub
			return new Reply[size];
		}

	};
	public int getSend_flag() {
		return send_flag;
	}
	public void setSend_flag(int send_flag) {
		this.send_flag = send_flag;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getPost_id() {
		return post_id;
	}
	public void setPost_id(int post_id) {
		this.post_id = post_id;
	}
	public String getConsumer_name() {
		return consumer_name;
	}
	public void setConsumer_name(String consumer_name) {
		this.consumer_name = consumer_name;
	}
	public String getSeller_name() {
		return seller_name;
	}
	public void setSeller_name(String seller_name) {
		this.seller_name = seller_name;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getContents() {
		return contents;
	}
	public void setContents(String contents) {
		this.contents = contents;
	}
	public ArrayList<String> getReply_images_name() {
		return reply_images_name;
	}
	public void setReply_images_name(ArrayList<String> reply_images_name) {
		this.reply_images_name = reply_images_name;
	}

	public String getSeller_num() {
		return seller_num;
	}

	public void setSeller_num(String seller_num) {
		this.seller_num = seller_num;
	}

	public String getSeller_phone() {
		return seller_phone;
	}

	public void setSeller_phone(String seller_phone) {
		this.seller_phone = seller_phone;
	}

	public int getPflag() {
		return pflag;
	}

	public void setPflag(int pflag) {
		this.pflag = pflag;
	}

	public int getParent() {
		return parent;
	}

	public void setParent(int parent) {
		this.parent = parent;
	}
	
}
