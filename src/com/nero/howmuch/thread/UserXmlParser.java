package com.nero.howmuch.thread;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.nero.howmuch.Reply;
import com.nero.howmuch.Utils;
import com.nero.howmuch.consumer.ConsumerPost;

import android.util.Log;

public class UserXmlParser {
	public static final String item = "item";
	public final String TAG = "Parser";
	public static final String ITEMS = "items";
	public static final String ITEM = "item";
	public static final String ITEMNUM = "itemnum";
	InputStream inputStream;

	public UserXmlParser(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public List<Map<String, String>> parse() {

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		List<Map<String, String>> items = new ArrayList<Map<String, String>>();

		try {

			DocumentBuilder builder = factory.newDocumentBuilder();
			Log.d("inputstream", inputStream.toString());
			Document document = builder.parse(inputStream);

			Element root = document.getDocumentElement();

			NodeList itemList = root.getElementsByTagName(item);

			Map<String, String> itemMap = null;

			for (int i = 0; i < itemList.getLength(); i++) {

				UserItem userItem;

				Node item = itemList.item(i);

				userItem = getCosumerItemFromXml(item);

				itemMap = new HashMap<String, String>();
				itemMap.put("title", userItem.getTitle());
				itemMap.put("result", userItem.getResult());
				items.add(itemMap);
			}

		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return items;
	}
	private UserItem getCosumerItemFromXml(Node node) {

		UserItem userItem = new UserItem();

		NodeList item = node.getChildNodes();

		for (int i = 0; i < item.getLength(); i++) {

			Node property = item.item(i);

			String name = property.getNodeName();

			if (name.equalsIgnoreCase("title")) {
				Node firstChild = property.getFirstChild();
				String title = null;
				if (firstChild != null) {
					title = firstChild.getNodeValue();
				}
				userItem.setTitle(title);
			} else if (name.equalsIgnoreCase("result")) {
				Node firstChild = property.getFirstChild();
				String result = null;
				if (firstChild != null) {
					result = firstChild.getNodeValue();
				}
				userItem.setResult(result);
			}
		}

		return userItem;
	}
	public List<Map<String, String>> sellerLoginParse() {

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		List<Map<String, String>> items = new ArrayList<Map<String, String>>();

		try {

			DocumentBuilder builder = factory.newDocumentBuilder();
			Log.d("inputstream", inputStream.toString());
			Document document = builder.parse(inputStream);

			Element root = document.getDocumentElement();

			NodeList itemList = root.getElementsByTagName(item);

			Map<String, String> itemMap = null;

			for (int i = 0; i < itemList.getLength(); i++) {

				UserItem userItem;

				Node item = itemList.item(i);

				userItem = getSellerLoginItemFromXml(item);

				itemMap = new HashMap<String, String>();
				itemMap.put("title", userItem.getTitle());
				itemMap.put("result", userItem.getResult());
				itemMap.put("seller_name", userItem.getSeller_name());
				itemMap.put("seller_city", userItem.getSeller_city());
				itemMap.put("seller_address", userItem.getSeller_address());
				itemMap.put("seller_phone", userItem.getSeller_phone());
				itemMap.put("plane", userItem.getPlane());
				items.add(itemMap);
			}

		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return items;
	}
	private UserItem getSellerLoginItemFromXml(Node node) {

		UserItem userItem = new UserItem();

		NodeList item = node.getChildNodes();

		for (int i = 0; i < item.getLength(); i++) {

			Node property = item.item(i);

			String name = property.getNodeName();

			if (name.equalsIgnoreCase("title")) {
				Node firstChild = property.getFirstChild();
				String title = null;
				if (firstChild != null) {
					title = firstChild.getNodeValue();
				}
				userItem.setTitle(title);
			} else if (name.equalsIgnoreCase("result")) {
				Node firstChild = property.getFirstChild();
				String result = null;
				if (firstChild != null) {
					result = firstChild.getNodeValue();
				}
				userItem.setResult(result);
			}else if(name.equals("seller_name")){
				Node firstChild = property.getFirstChild();
				String seller_name = null;
				if(firstChild!=null){
					seller_name = firstChild.getNodeValue();
				}
				userItem.setSeller_name(seller_name);
			}else if(name.equals("seller_city")){
				Node firstChild = property.getFirstChild();
				String seller_city = null;
				if(firstChild!=null){
					seller_city = firstChild.getNodeValue();
				}
				userItem.setSeller_city(seller_city);
			}else if(name.equals("seller_address")){
				Node firstChild = property.getFirstChild();
				String seller_address = null;
				if(firstChild!=null){
					seller_address = firstChild.getNodeValue();
				}
				userItem.setSeller_address(seller_address);
			}else if(name.equals("plane")){
				Node firstChild = property.getFirstChild();
				String plane = null;
				if(firstChild!=null){
					plane = firstChild.getNodeValue();
				}
				userItem.setPlane(plane);
			}else if(name.equals("seller_phone")){
				Node firstChild = property.getFirstChild();
				String seller_phone = null;
				if(firstChild!=null){
					seller_phone = firstChild.getNodeValue();
				}
				userItem.setSeller_phone(seller_phone);
			}

		}

		return userItem;
	}
	
	
	
	public List<Map<String, String>> parsePostRowId() {

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		List<Map<String, String>> items = new ArrayList<Map<String, String>>();

		try {

			DocumentBuilder builder = factory.newDocumentBuilder();
			Log.d("inputstream", inputStream.toString());
			Document document = builder.parse(inputStream);

			Element root = document.getDocumentElement();

			NodeList itemList = root.getElementsByTagName(item);

			Map<String, String> itemMap = null;

			for (int i = 0; i < itemList.getLength(); i++) {

				UserItem userItem;

				Node item = itemList.item(i);

				userItem = getPostItemFromXml(item);

				itemMap = new HashMap<String, String>();
				itemMap.put("title", userItem.getTitle());
				itemMap.put("result", userItem.getResult());
				itemMap.put("post_id", userItem.getPost_id());
				items.add(itemMap);
			}

		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return items;
	}
	public List<Map<String, String>> parseReplyRowId() {

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		List<Map<String, String>> items = new ArrayList<Map<String, String>>();

		try {

			DocumentBuilder builder = factory.newDocumentBuilder();
			Log.d("inputstream", inputStream.toString());
			Document document = builder.parse(inputStream);

			Element root = document.getDocumentElement();

			NodeList itemList = root.getElementsByTagName(item);

			Map<String, String> itemMap = null;

			for (int i = 0; i < itemList.getLength(); i++) {

				UserItem userItem;

				Node item = itemList.item(i);

				userItem = getReplyItemFromXml(item);

				itemMap = new HashMap<String, String>();
				itemMap.put("title", userItem.getTitle());
				itemMap.put("result", userItem.getResult());
				itemMap.put("reply_id", userItem.getPost_id());
				items.add(itemMap);
			}

		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return items;
	}
	private UserItem getReplyItemFromXml(Node node) {

		UserItem userItem = new UserItem();

		NodeList item = node.getChildNodes();

		for (int i = 0; i < item.getLength(); i++) {

			Node property = item.item(i);

			String name = property.getNodeName();

			if (name.equalsIgnoreCase("title")) {
				Node firstChild = property.getFirstChild();
				String title = null;
				if (firstChild != null) {
					title = firstChild.getNodeValue();
				}
				userItem.setTitle(title);
			} else if (name.equalsIgnoreCase("result")) {
				Node firstChild = property.getFirstChild();
				String result = null;
				if (firstChild != null) {
					result = firstChild.getNodeValue();
				}
				userItem.setResult(result);
			} else if (name.equalsIgnoreCase("reply_id")) {
				Node firstChild = property.getFirstChild();
				String post_id = null;
				if (firstChild != null) {
					post_id = firstChild.getNodeValue();
				}
				userItem.setPost_id(post_id);
			}

		}

		return userItem;
	}
	private UserItem getPostItemFromXml(Node node) {

		UserItem userItem = new UserItem();

		NodeList item = node.getChildNodes();

		for (int i = 0; i < item.getLength(); i++) {

			Node property = item.item(i);

			String name = property.getNodeName();

			if (name.equalsIgnoreCase("title")) {
				Node firstChild = property.getFirstChild();
				String title = null;
				if (firstChild != null) {
					title = firstChild.getNodeValue();
				}
				userItem.setTitle(title);
			} else if (name.equalsIgnoreCase("result")) {
				Node firstChild = property.getFirstChild();
				String result = null;
				if (firstChild != null) {
					result = firstChild.getNodeValue();
				}
				userItem.setResult(result);
			} else if (name.equalsIgnoreCase("post_id")) {
				Node firstChild = property.getFirstChild();
				String post_id = null;
				if (firstChild != null) {
					post_id = firstChild.getNodeValue();
				}
				userItem.setPost_id(post_id);
			}

		}

		return userItem;
	}
	
	
	
	
	
	private ConsumerPost getCosumerPostFromXml(Node node) {

		ConsumerPost consumerPost = new ConsumerPost();

		NodeList item = node.getChildNodes();

		for (int i = 0; i < item.getLength(); i++) {

			Node property = item.item(i);

			String name = property.getNodeName();

			if (name.equalsIgnoreCase("post_id")) {
				Node firstChild = property.getFirstChild();
				int id=0;
				if (firstChild != null) {
					id = Integer.parseInt(firstChild.getNodeValue());
				}
				consumerPost.setId(id);
			} else if (name.equalsIgnoreCase("date")) {
				Node firstChild = property.getFirstChild();
				String date = null;
				if (firstChild != null) {
					date = firstChild.getNodeValue();
				}
				consumerPost.setDate(date);
			} else if(name.equalsIgnoreCase("consumer_name")){
				Node firstChild = property.getFirstChild();
				String consumer_name = null;
				if (firstChild != null) {
					consumer_name = firstChild.getNodeValue();
				}
				consumerPost.setConsumer_name(consumer_name);
			}else if(name.equalsIgnoreCase("title")){
				Node firstChild = property.getFirstChild();
				String title = null;
				if (firstChild != null) {
					title = firstChild.getNodeValue();
				}
				consumerPost.setTitle(title);
			}else if(name.equalsIgnoreCase("city")){
				Node firstChild = property.getFirstChild();
				String city = null;
				if (firstChild != null) {
					city = firstChild.getNodeValue();
				}
				consumerPost.setCity(city);
			}else if(name.equalsIgnoreCase("category")){
				Node firstChild = property.getFirstChild();
				String category = null;
				if (firstChild != null) {
					category = firstChild.getNodeValue();
				}
				consumerPost.setCategory(category);
			}else if(name.equalsIgnoreCase("contents")){
				Node firstChild = property.getFirstChild();
				String contents = null;
				if (firstChild != null) {
					contents = firstChild.getNodeValue();
				}
				consumerPost.setContents(contents);
			}else if(name.equalsIgnoreCase("image")){
				Node firstChild = property.getFirstChild();
				String image = null;
				if (firstChild != null) {
					image = firstChild.getNodeValue();
				}
				consumerPost.getPost_images_name().add(image);
				Log.d("image", image);
			}

		}

		return consumerPost;
	}
	public ConsumerPost parseConsumerPost() {

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		ConsumerPost consumerPost=null;
		try {

			DocumentBuilder builder = factory.newDocumentBuilder();
			Log.d("inputstream", inputStream.toString());
			Document document = builder.parse(inputStream);

			Element root = document.getDocumentElement();

			NodeList itemList = root.getElementsByTagName(item);
			Log.d("itemlist size", "length"+itemList.getLength());
			for (int i = 0; i < itemList.getLength(); i++) {

				
				Log.d("userXmlParser", "itemList cnt:"+i);
				Node item = itemList.item(i);

				consumerPost = getCosumerPostFromXml(item);

//				itemMap = new HashMap<String, String>();
//				itemMap.put("title", userItem.getTitle());
//				itemMap.put("result", userItem.getResult());
//				items.add(itemMap);
			}

		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return consumerPost;

	}
	public Reply parseReply() {

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		Reply reply=null;
		try {

			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(inputStream);

			Element root = document.getDocumentElement();

			NodeList itemList = root.getElementsByTagName(item);
			for (int i = 0; i < itemList.getLength(); i++) {
				Node item = itemList.item(i);
				reply = getReplyFromXml(item);
			}

		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return reply;

	}
	private Reply getReplyFromXml(Node node) {

		Reply reply = new Reply();

		NodeList item = node.getChildNodes();

		for (int i = 0; i < item.getLength(); i++) {

			Node property = item.item(i);

			String name = property.getNodeName();
			if(name.equalsIgnoreCase("reply_id")){
				Node firstChild = property.getFirstChild();
				int id=0;
				if (firstChild != null) {
					id = Integer.parseInt(firstChild.getNodeValue());
				}
				reply.setId(id);
			}else if (name.equalsIgnoreCase("post_id")) {
				Node firstChild = property.getFirstChild();
				int id=0;
				if (firstChild != null) {
					id = Integer.parseInt(firstChild.getNodeValue());
				}
				reply.setPost_id(id);
			} else if (name.equalsIgnoreCase("date")) {
				Node firstChild = property.getFirstChild();
				String date = null;
				if (firstChild != null) {
					date = firstChild.getNodeValue();
				}
				reply.setDate(date);
			} else if(name.equalsIgnoreCase("consumer_name")){
				Node firstChild = property.getFirstChild();
				String consumer_name = null;
				if (firstChild != null) {
					consumer_name = firstChild.getNodeValue();
				}
				reply.setConsumer_name(consumer_name);
			}else if(name.equalsIgnoreCase("seller_name")){
				Node firstChild = property.getFirstChild();
				String seller_name = null;
				if (firstChild != null) {
					seller_name = firstChild.getNodeValue();
				}
				reply.setSeller_name(seller_name);
			}else if(name.equalsIgnoreCase("seller_num")){
				Node firstChild = property.getFirstChild();
				String seller_num = null;
				if (firstChild != null) {
					seller_num = firstChild.getNodeValue();
				}
				reply.setSeller_num(seller_num);
			}else if(name.equalsIgnoreCase("seller_phone")){
				Node firstChild = property.getFirstChild();
				String seller_phone = null;
				if (firstChild != null) {
					seller_phone = firstChild.getNodeValue();
				}
				reply.setSeller_phone(seller_phone);
			}else if(name.equalsIgnoreCase("contents")){
				Node firstChild = property.getFirstChild();
				String contents = null;
				if (firstChild != null) {
					contents = firstChild.getNodeValue();
				}
				reply.setContents(contents);
			}else if(name.equalsIgnoreCase("image")){
				Node firstChild = property.getFirstChild();
				String image = null;
				if (firstChild != null) {
					image = firstChild.getNodeValue();
				}
				if(image!=null){
					reply.setReply_images_name(Utils.getImgNameToken(image));
				}else{
					reply.setReply_images_name(new ArrayList<String>());
				}
			}else if(name.equalsIgnoreCase("pflag")){
				Node firstChild = property.getFirstChild();
				String pflag = null;
				if(firstChild!=null){
					pflag = firstChild.getNodeValue();
				}
				reply.setPflag(Integer.parseInt(pflag));
			}else if(name.equalsIgnoreCase("parent")){
				Node firstChild = property.getFirstChild();
				String parent = null;
				if(firstChild!=null){
					parent = firstChild.getNodeValue();
				}
				reply.setParent(Integer.parseInt(parent));
			}
		}
		return reply;
	}
	public List<Map<String, String>> parseChargePlane() {

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		List<Map<String, String>> items = new ArrayList<Map<String, String>>();

		try {

			DocumentBuilder builder = factory.newDocumentBuilder();
			Log.d("inputstream", inputStream.toString());
			Document document = builder.parse(inputStream);

			Element root = document.getDocumentElement();

			NodeList itemList = root.getElementsByTagName(item);

			Map<String, String> itemMap = null;

			for (int i = 0; i < itemList.getLength(); i++) {

				UserItem userItem;

				Node item = itemList.item(i);

				userItem = getPlaneItemFromXml(item);

				itemMap = new HashMap<String, String>();
				itemMap.put("title", userItem.getTitle());
				itemMap.put("result", userItem.getResult());
				itemMap.put("plane", userItem.getPlane());
				items.add(itemMap);
			}

		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return items;
	}
	private UserItem getPlaneItemFromXml(Node node) {

		UserItem userItem = new UserItem();

		NodeList item = node.getChildNodes();

		for (int i = 0; i < item.getLength(); i++) {

			Node property = item.item(i);

			String name = property.getNodeName();

			if (name.equalsIgnoreCase("title")) {
				Node firstChild = property.getFirstChild();
				String title = null;
				if (firstChild != null) {
					title = firstChild.getNodeValue();
				}
				userItem.setTitle(title);
			} else if (name.equalsIgnoreCase("result")) {
				Node firstChild = property.getFirstChild();
				String result = null;
				if (firstChild != null) {
					result = firstChild.getNodeValue();
				}
				userItem.setResult(result);
			} else if (name.equalsIgnoreCase("plane")){
				Node firstChild = property.getFirstChild();
				String plane = null;
				if(firstChild!=null){
					plane = firstChild.getNodeValue();
				}
				userItem.setPlane(plane);
			}
		}
		return userItem;
	}
}