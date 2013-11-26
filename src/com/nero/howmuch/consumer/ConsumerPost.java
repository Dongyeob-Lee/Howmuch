package com.nero.howmuch.consumer;

import java.util.ArrayList;

import android.graphics.Bitmap;


public class ConsumerPost {
	private int id;
	private String date;
	private String consumer_name;
	private String title;
	private String category;
	private String city;
	private String contents;
	private ArrayList<String> post_images_name =new ArrayList<String>();
	private ArrayList<Bitmap> post_images = new ArrayList<Bitmap>();
	
	private int replyCnt;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getConsumer_name() {
		return consumer_name;
	}
	public void setConsumer_name(String consumer_name) {
		this.consumer_name = consumer_name;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getContents() {
		return contents;
	}
	public void setContents(String contents) {
		this.contents = contents;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public ArrayList<String> getPost_images_name() {
		return post_images_name;
	}
	public void setPost_images_name(ArrayList<String> post_images_name) {
		this.post_images_name = post_images_name;
	}
	public int getReplyCnt() {
		return replyCnt;
	}
	public void setReplyCnt(int replyCnt) {
		this.replyCnt = replyCnt;
	}
	public ArrayList<Bitmap> getPost_images() {
		return post_images;
	}
	public void setPost_images(ArrayList<Bitmap> post_images) {
		this.post_images = post_images;
	}
	
}
