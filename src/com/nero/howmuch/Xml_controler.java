package com.nero.howmuch;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.nero.howmuch.consumer.Consumer;
import com.nero.howmuch.seller.Seller;

import android.content.Context;
import android.util.Log;

public class Xml_controler {
//	public static final String path = Environment.getExternalStorageState()
//			+ "/howmuch";
	 
     
	public static boolean deleteConusmerDataXml(Context context){
		String path = context.getFilesDir().getAbsolutePath();
		String filename = "consumer.xml";
		File file = new File(path,filename);
		boolean result = false;
		if(file.exists()){
			result = file.delete();
		}
		return result;
	}
	public static boolean deleteSellerDataXml(Context context){
		String path = context.getFilesDir().getAbsolutePath();
		String filename = "seller.xml";
		File file = new File(path,filename);
		boolean result = false;
		if(file.exists()){
			result = file.delete();
		}
		return result;
	}
	public static Consumer getConsumerDateFromXml(Context context) {
		String path = context.getFilesDir().getAbsolutePath();
		Consumer consumer = new Consumer();
		String filename = "consumer.xml";
		File file = new File(path, filename);
		
		FileInputStream fis;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		if (file.exists()) {
			try {
				fis = new FileInputStream(path+"/"+filename);
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document dom = builder.parse(fis);
				Element root = dom.getDocumentElement();
				NodeList items = root.getElementsByTagName("data");
				for (int i = 0; i < items.getLength(); i++) {
					Node item = items.item(i);
					NodeList datas = item.getChildNodes();
					Log.d("data length",String.valueOf(datas.getLength()));
					for (int j = 0; j < datas.getLength(); j++) {
						Node data = datas.item(j);
						String nodename = data.getNodeName();
						Log.d("nodename", nodename);
						if (nodename.equals("#text")) {
							continue;
						}
						if (nodename.equalsIgnoreCase("name")) {
							consumer.setConsumer_name(data.getFirstChild()
									.getNodeValue());
							Log.d("name", consumer.getConsumer_name());
						}else if(nodename.equalsIgnoreCase("gcm_id")){
							consumer.setGcm_id(data.getFirstChild().getNodeValue());
							Log.d("gcm", consumer.getGcm_id());
						}
					}
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			consumer.setConsumer_name("empty");
		}

		return consumer;
	}

	public static boolean makeConsumerFile(Consumer consumer, Context context) {
		String path = context.getFilesDir().getAbsolutePath();
		
		StringBuffer stringBuffer = new StringBuffer();
		String filename = "consumer.xml";
		File dirfile = new File(path);
		Log.d("path", path);
		File file;
		if (!dirfile.exists()) {
			dirfile.mkdir();
		}
		file = new File(path, filename);
		stringBuffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		stringBuffer.append("<item>\n");
		stringBuffer.append("\t<data>\n");
		stringBuffer.append("\t\t<name>" + consumer.getConsumer_name()
				+ "</name>\n");
		stringBuffer.append("\t\t<gcm_id>"+consumer.getGcm_id()+"</gcm_id>\n");
		stringBuffer.append("\t</data>\n");
		stringBuffer.append("</item>");
		try {
			file.createNewFile();
			FileOutputStream fos = new FileOutputStream(path+"/"+filename);
			fos.write(stringBuffer.toString().getBytes());
			fos.close();
			return true;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public static Seller getSellerDateFromXml(Context context) {
		String path = context.getFilesDir().getAbsolutePath();
		Seller seller = new Seller();
		ArrayList<String> categories = new ArrayList<String>();
		String filename = "seller.xml";
		File file = new File(path, filename);
		FileInputStream fis;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		if (file.exists()) {
			try {
				fis = new FileInputStream(path+"/"+filename);
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document dom = builder.parse(fis);
				Element root = dom.getDocumentElement();
				NodeList items = root.getElementsByTagName("data");
				for (int i = 0; i < items.getLength(); i++) {
					Node item = items.item(i);
					NodeList datas = item.getChildNodes();
					for (int j = 0; j < datas.getLength(); j++) {
						Node data = datas.item(j);
						String nodename = data.getNodeName();
						if (nodename.equals("#text")) {
							continue;
						}
						if (nodename.equalsIgnoreCase("seller_name")) {
							seller.setSeller_name(data.getFirstChild()
									.getNodeValue());
						} else if (nodename.equalsIgnoreCase("seller_num")) {
							seller.setSeller_num(data.getFirstChild()
									.getNodeValue());
						} else if(nodename.equalsIgnoreCase("seller_pw")){
							seller.setSeller_pw(data.getFirstChild().getNodeValue());
						}else if(nodename.equalsIgnoreCase("seller_city")){
							seller.setSeller_city(data.getFirstChild().getNodeValue());
						}else if (nodename.equalsIgnoreCase("seller_address")) {
							seller.setSeller_address(data.getFirstChild()
									.getNodeValue());
						} else if (nodename.equalsIgnoreCase("seller_phone")) {
							seller.setSeller_phone(data.getFirstChild()
									.getNodeValue());
						} else if(nodename.equals("seller_category")){
							categories.add(data.getFirstChild().getNodeValue());
						}
						else if(nodename.equalsIgnoreCase("gcm_id")){
							seller.setGcm_id(data.getFirstChild().getNodeValue());
						}
					}
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			seller.setSeller_name("empty");
		}
		seller.setCategory(categories);
		return seller;
	}

	public static boolean makeSellerFile(Seller seller, Context context) {
//		String externalState = Environment.getExternalStorageState();
//		String path;
//		if(externalState.equals(Environment.MEDIA_MOUNTED)){
//		      //외부 저장 장치가 마운트 되어서 읽어올 준비가 되었을 때 (environment:컴퓨터,사용자환경)
//		      path = Environment.getExternalStorageDirectory().getAbsolutePath();
//		     }else{
//		      //마운트 되지 않았을 때 
//		      path=Environment.MEDIA_UNMOUNTED; 
//		     }
//		path = path+"/howmuch";
		String path = context.getFilesDir().getAbsolutePath();
		StringBuffer stringBuffer = new StringBuffer();
		String filename = "seller.xml";
		File dirfile = new File(path);
		File file;
		if (!dirfile.exists()) {
			dirfile.mkdir();
		}
		file = new File(path, filename);
		stringBuffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		stringBuffer.append("<item>\n");
		stringBuffer.append("\t<data>\n");
		stringBuffer.append("\t\t<seller_name>" + seller.getSeller_name()
				+ "</seller_name>\n");
		stringBuffer.append("\t\t<seller_num>" + seller.getSeller_num()
				+ "</seller_num>\n");
		stringBuffer.append("\t\t<seller_pw>"+seller.getSeller_pw()+"</seller_pw>\n");
		stringBuffer.append("\t\t<seller_city>" + seller.getSeller_city() + "</seller_city>\n");
		stringBuffer.append("\t\t<seller_address>" + seller.getSeller_address()
				+ "</seller_address>\n");
		stringBuffer.append("\t\t<seller_phone>" + seller.getSeller_phone()
				+ "</seller_phone>\n");
		for(int i=0; i<seller.getCategory().size(); i++){
			stringBuffer.append("\t\t<seller_category>"+seller.getCategory().get(i)+"</seller_category>\n");
		}
		stringBuffer.append("\t\t<gcm_id>"+seller.getGcm_id()+"</gcm_id>\n");
		stringBuffer.append("\t</data>\n");
		stringBuffer.append("</item>\n");
		try {
			file.createNewFile();
			FileOutputStream fos = new FileOutputStream(path+"/"+filename);
			fos.write(stringBuffer.toString().getBytes());
			fos.close();
			return true;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
}
