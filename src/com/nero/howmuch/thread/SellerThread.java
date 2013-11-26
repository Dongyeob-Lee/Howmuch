package com.nero.howmuch.thread;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.nero.howmuch.Reply;
import com.nero.howmuch.Utils;
import com.nero.howmuch.consumer.ConsumerPost;
import com.nero.howmuch.seller.Seller;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class SellerThread extends Thread {
	String url;
	Seller seller;
	Reply sellerReply;
	String seller_num;
	String post_id;
	int tmp_category;
	int plane;
	StringBuffer ContactsXml;
	InputStream inputStream;
	Handler handler;
	Context mContext;
	String tagName = "";
	String action;
	String path;
	int reply_id;

	public SellerThread(Seller seller, String action, Handler handler) {
		this.url = Utils.userUrl;
		this.seller = seller;
		this.action = action;
		this.handler = handler;
	}

	public SellerThread(String key, String action, Handler handler) {
		if (action.equals(Seller.SELLER_REQUEST_POST)) {
			this.post_id = key;
			this.url = Utils.postUrl;
		} else {
			this.seller_num = key;
			this.url = Utils.userUrl;
		}
		this.action = action;
		this.handler = handler;
	}

	public SellerThread(String seller_num, int cateogry, String action,
			Handler handler) {
		this.seller_num = seller_num;
		this.tmp_category = cateogry;
		this.action = action;
		this.url = Utils.userUrl;
		this.handler = handler;
	}

	// seller reply constructor
	public SellerThread(Reply _sellerReply, String path, String _action,
			Handler _handler) {
		this.sellerReply = _sellerReply;
		this.action = _action;
		this.path = path;
		this.url = Utils.replyUrl;
		this.handler = _handler;
	}

	// request consumer request
	public SellerThread(int _reply_id, String _action, Handler _handler) {
		this.reply_id = _reply_id;
		this.action = _action;
		this.handler = _handler;
		this.url = Utils.replyUrl;
	}

	// charge plane
	public SellerThread(int plane, String seller_num, String _action,
			Handler _handler) {
		this.seller_num = seller_num;
		this.action = _action;
		this.plane = plane;
		this.url = Utils.userUrl;
		this.handler = _handler;
	}

	@Override
	public void run() {

		// TODO Auto-generated method stub
		// HttpConnectAdapter.executeJoin(url,email_id,password,username,gender);
		// 硫�����濡�臾몄���蹂대�湲�
		HttpClient httpClient = new DefaultHttpClient();

		HttpPost httpPost = new HttpPost(url);
		System.out.println("action:" + action);
		if (action.equals(Seller.SELLER_REGISTER)) {
			try {
				List<NameValuePair> postParameters = new ArrayList<NameValuePair>();

				NameValuePair actionNaValuePair = new BasicNameValuePair(
						"action", Seller.SELLER_REGISTER);
				postParameters.add(actionNaValuePair);
				NameValuePair sellerNameValuePair = new BasicNameValuePair(
						"seller_name", seller.getSeller_name());
				postParameters.add(sellerNameValuePair);
				NameValuePair sellerPwValuePair = new BasicNameValuePair(
						"seller_pw", seller.getSeller_pw());
				postParameters.add(sellerPwValuePair);
				NameValuePair sellerNumValuePair = new BasicNameValuePair(
						"seller_num", seller.getSeller_num());
				postParameters.add(sellerNumValuePair);
				NameValuePair sellerCityValuePair = new BasicNameValuePair(
						"seller_city", seller.getSeller_city());
				postParameters.add(sellerCityValuePair);
				NameValuePair sellerAddressValuePair = new BasicNameValuePair(
						"seller_address", seller.getSeller_address());
				postParameters.add(sellerAddressValuePair);
				NameValuePair sellerPhoneValuePair = new BasicNameValuePair(
						"seller_phone", seller.getSeller_phone());
				postParameters.add(sellerPhoneValuePair);
				NameValuePair sellerGcmIdValuePair = new BasicNameValuePair(
						"gcm_id", seller.getGcm_id());
				postParameters.add(sellerGcmIdValuePair);

				UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(
						postParameters, "euc-kr");
				httpPost.setEntity(urlEncodedFormEntity);

				// ///////////////
				HttpResponse response;
				XmlPullParserFactory factory = XmlPullParserFactory
						.newInstance();
				factory.setNamespaceAware(true);

				response = httpClient.execute(httpPost);
				InputStream inputStream = response.getEntity().getContent();

				UserXmlParser parser = new UserXmlParser(inputStream);

				List<Map<String, String>> sellerMap = parser.parse();

				Message message = handler.obtainMessage(1);

				message.obj = sellerMap;

				handler.sendMessage(message);
				// ///////////////////

			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (XmlPullParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// 짹�����첩쩔���⒱�
				Message message = handler.obtainMessage(100);
				Bundle bundle = new Bundle();
				bundle.putString("result", "error");
				message.setData(bundle);
				handler.sendMessage(message);
			} finally {
				httpClient.getConnectionManager().shutdown();
				Log.d("httpClient", "shut down");
			}
		} else if (action.equals(Seller.SELLER_LOGIN)) {
			try {
				List<NameValuePair> postParameters = new ArrayList<NameValuePair>();

				NameValuePair actionNaValuePair = new BasicNameValuePair(
						"action", Seller.SELLER_LOGIN);
				postParameters.add(actionNaValuePair);

				NameValuePair sellerNumValuePair = new BasicNameValuePair(
						"seller_num", seller.getSeller_num());
				postParameters.add(sellerNumValuePair);

				NameValuePair pwValuePair = new BasicNameValuePair("seller_pw",
						seller.getSeller_pw());
				postParameters.add(pwValuePair);

				NameValuePair sellerGcmIdValuePair = new BasicNameValuePair(
						"gcm_id", seller.getGcm_id());
				postParameters.add(sellerGcmIdValuePair);

				UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(
						postParameters, "euc-kr");
				httpPost.setEntity(urlEncodedFormEntity);

				// ///////////////
				HttpResponse response;
				XmlPullParserFactory factory = XmlPullParserFactory
						.newInstance();
				factory.setNamespaceAware(true);

				response = httpClient.execute(httpPost);
				InputStream inputStream = response.getEntity().getContent();

				UserXmlParser parser = new UserXmlParser(inputStream);

				List<Map<String, String>> sellerMap = parser.sellerLoginParse();

				Message message = handler.obtainMessage(1);

				message.obj = sellerMap;

				handler.sendMessage(message);
				// ///////////////////

			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (XmlPullParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// 짹�����첩쩔���⒱�
				Message message = handler.obtainMessage(100);
				Bundle bundle = new Bundle();
				bundle.putString("result", "error");
				message.setData(bundle);
				handler.sendMessage(message);
			} finally {
				httpClient.getConnectionManager().shutdown();
				Log.d("httpClient", "shut down");
			}
		} else if (action.equals(Seller.SELLER_UPDATE)) {
			try {
				List<NameValuePair> postParameters = new ArrayList<NameValuePair>();

				NameValuePair actionNaValuePair = new BasicNameValuePair(
						"action", Seller.SELLER_UPDATE);
				postParameters.add(actionNaValuePair);
				NameValuePair sellerNameValuePair = new BasicNameValuePair(
						"seller_name", seller.getSeller_name());
				postParameters.add(sellerNameValuePair);
				NameValuePair sellerNumValuePair = new BasicNameValuePair(
						"seller_num", seller.getSeller_num());
				postParameters.add(sellerNumValuePair);
				NameValuePair sellerCityValuePair = new BasicNameValuePair(
						"seller_city", seller.getSeller_city());
				postParameters.add(sellerCityValuePair);
				NameValuePair sellerAddressValuePair = new BasicNameValuePair(
						"seller_address", seller.getSeller_address());
				postParameters.add(sellerAddressValuePair);
				NameValuePair sellerPhoneValuePair = new BasicNameValuePair(
						"seller_phone", seller.getSeller_phone());
				postParameters.add(sellerPhoneValuePair);

				UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(
						postParameters, "euc-kr");
				httpPost.setEntity(urlEncodedFormEntity);

				// ///////////////
				HttpResponse response;
				XmlPullParserFactory factory = XmlPullParserFactory
						.newInstance();
				factory.setNamespaceAware(true);

				response = httpClient.execute(httpPost);
				InputStream inputStream = response.getEntity().getContent();

				UserXmlParser parser = new UserXmlParser(inputStream);

				List<Map<String, String>> sellerMap = parser.parse();

				Message message = handler.obtainMessage(1);

				message.obj = sellerMap;

				handler.sendMessage(message);
				// ///////////////////

			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (XmlPullParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// 짹�����첩쩔���⒱�
				Message message = handler.obtainMessage(100);
				Bundle bundle = new Bundle();
				bundle.putString("result", "error");
				message.setData(bundle);
				handler.sendMessage(message);
			} finally {
				httpClient.getConnectionManager().shutdown();
				Log.d("httpClient", "shut down");
			}
		} else if (action.equals(Seller.SELLER_UPDATE_GCM)) {
			try {
				List<NameValuePair> postParameters = new ArrayList<NameValuePair>();

				NameValuePair actionNaValuePair = new BasicNameValuePair(
						"action", Seller.SELLER_UPDATE_GCM);
				postParameters.add(actionNaValuePair);
				NameValuePair sellerNameValuePair = new BasicNameValuePair(
						"seller_num", seller.getSeller_num());
				postParameters.add(sellerNameValuePair);
				NameValuePair sellerGcmIdValuePair = new BasicNameValuePair(
						"gcm_id", seller.getGcm_id());
				postParameters.add(sellerGcmIdValuePair);
				UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(
						postParameters, "euc-kr");

				httpPost.setEntity(urlEncodedFormEntity);

				// ///////////////
				HttpResponse response;
				XmlPullParserFactory factory = XmlPullParserFactory
						.newInstance();
				factory.setNamespaceAware(true);

				response = httpClient.execute(httpPost);
				InputStream inputStream = response.getEntity().getContent();

				UserXmlParser parser = new UserXmlParser(inputStream);

				List<Map<String, String>> consumerMap = parser.parse();

				Message message = handler.obtainMessage(1);

				message.obj = consumerMap;

				handler.sendMessage(message);
				// ///////////////////

			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (XmlPullParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// 짹�����첩쩔���⒱�
				Message message = handler.obtainMessage(100);
				Bundle bundle = new Bundle();
				bundle.putString("result", "error");
				message.setData(bundle);
				handler.sendMessage(message);
			} finally {
				httpClient.getConnectionManager().shutdown();
				Log.d("httpClient", "shut down");
			}
		} else if (action.equals(Seller.SELLER_CHECKNAME)) {
			try {
				List<NameValuePair> postParameters = new ArrayList<NameValuePair>();

				NameValuePair actionNaValuePair = new BasicNameValuePair(
						"action", Seller.SELLER_CHECKNAME);
				postParameters.add(actionNaValuePair);
				NameValuePair consumerNameValuePair = new BasicNameValuePair(
						"seller_num", seller_num);
				postParameters.add(consumerNameValuePair);

				UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(
						postParameters, "euc-kr");

				httpPost.setEntity(urlEncodedFormEntity);

				// ///////////////
				HttpResponse response;
				XmlPullParserFactory factory = XmlPullParserFactory
						.newInstance();
				factory.setNamespaceAware(true);

				response = httpClient.execute(httpPost);
				InputStream inputStream = response.getEntity().getContent();

				UserXmlParser parser = new UserXmlParser(inputStream);

				List<Map<String, String>> sellerMap = parser.parse();

				Message message = handler.obtainMessage(1);

				message.obj = sellerMap;

				handler.sendMessage(message);
				// ///////////////////

			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (XmlPullParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// 짹�����첩쩔���⒱�
				Message message = handler.obtainMessage(100);
				Bundle bundle = new Bundle();
				bundle.putString("result", "error");
				message.setData(bundle);
				handler.sendMessage(message);
			} finally {
				httpClient.getConnectionManager().shutdown();
				Log.d("httpClient", "shut down");
			}
		} else if (action.equals(Seller.SELLER_LOGOUT)) {
			try {
				List<NameValuePair> postParameters = new ArrayList<NameValuePair>();

				NameValuePair actionNaValuePair = new BasicNameValuePair(
						"action", Seller.SELLER_LOGOUT);
				postParameters.add(actionNaValuePair);
				NameValuePair consumerNameValuePair = new BasicNameValuePair(
						"seller_num", seller_num);
				postParameters.add(consumerNameValuePair);

				UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(
						postParameters, "euc-kr");

				httpPost.setEntity(urlEncodedFormEntity);

				// ///////////////
				HttpResponse response;
				XmlPullParserFactory factory = XmlPullParserFactory
						.newInstance();
				factory.setNamespaceAware(true);

				response = httpClient.execute(httpPost);
				InputStream inputStream = response.getEntity().getContent();

				UserXmlParser parser = new UserXmlParser(inputStream);

				List<Map<String, String>> sellerMap = parser.parse();

				Message message = handler.obtainMessage(1);

				message.obj = sellerMap;

				handler.sendMessage(message);
				// ///////////////////

			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (XmlPullParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// 짹�����첩쩔���⒱�
				Message message = handler.obtainMessage(100);
				Bundle bundle = new Bundle();
				bundle.putString("result", "error");
				message.setData(bundle);
				handler.sendMessage(message);
			} finally {
				httpClient.getConnectionManager().shutdown();
				Log.d("httpClient", "shut down");
			}
		} else if (action.equals(Seller.SELLER_DELETE)) {
			try {
				List<NameValuePair> postParameters = new ArrayList<NameValuePair>();

				NameValuePair actionNaValuePair = new BasicNameValuePair(
						"action", Seller.SELLER_DELETE);
				postParameters.add(actionNaValuePair);
				NameValuePair consumerNameValuePair = new BasicNameValuePair(
						"seller_num", seller_num);
				postParameters.add(consumerNameValuePair);

				UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(
						postParameters, "euc-kr");

				httpPost.setEntity(urlEncodedFormEntity);

				// ///////////////
				HttpResponse response;
				XmlPullParserFactory factory = XmlPullParserFactory
						.newInstance();
				factory.setNamespaceAware(true);

				response = httpClient.execute(httpPost);
				InputStream inputStream = response.getEntity().getContent();

				UserXmlParser parser = new UserXmlParser(inputStream);

				List<Map<String, String>> sellerMap = parser.parse();

				Message message = handler.obtainMessage(1);

				message.obj = sellerMap;

				handler.sendMessage(message);
				// ///////////////////

			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (XmlPullParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// 짹�����첩쩔���⒱�
				Message message = handler.obtainMessage(100);
				Bundle bundle = new Bundle();
				bundle.putString("result", "error");
				message.setData(bundle);
				handler.sendMessage(message);
			} finally {
				httpClient.getConnectionManager().shutdown();
				Log.d("httpClient", "shut down");
			}
		} else if (action.equals(Seller.SELLER_ADD_CATEGORY)) {
			try {
				List<NameValuePair> postParameters = new ArrayList<NameValuePair>();

				NameValuePair actionNaValuePair = new BasicNameValuePair(
						"action", Seller.SELLER_ADD_CATEGORY);
				postParameters.add(actionNaValuePair);
				NameValuePair sellerNameValuePair = new BasicNameValuePair(
						"seller_num", seller_num);
				postParameters.add(sellerNameValuePair);
				NameValuePair sellerCategoryValuePair = new BasicNameValuePair(
						"category", String.valueOf(tmp_category));
				postParameters.add(sellerCategoryValuePair);

				UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(
						postParameters, "euc-kr");

				httpPost.setEntity(urlEncodedFormEntity);

				// ///////////////
				HttpResponse response;
				XmlPullParserFactory factory = XmlPullParserFactory
						.newInstance();
				factory.setNamespaceAware(true);

				response = httpClient.execute(httpPost);
				InputStream inputStream = response.getEntity().getContent();

				UserXmlParser parser = new UserXmlParser(inputStream);

				List<Map<String, String>> sellerMap = parser.parse();

				Message message = handler.obtainMessage(1);

				message.obj = sellerMap;

				handler.sendMessage(message);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (XmlPullParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// 짹�����첩쩔���⒱�
				Message message = handler.obtainMessage(100);
				Bundle bundle = new Bundle();
				bundle.putString("result", "error");
				message.setData(bundle);
				handler.sendMessage(message);
			} finally {
				httpClient.getConnectionManager().shutdown();
				Log.d("httpClient", "shut down");
			}
		} else if (action.equals(Seller.SELLER_REMOVE_CATEGORY)) {
			try {
				List<NameValuePair> postParameters = new ArrayList<NameValuePair>();

				NameValuePair actionNaValuePair = new BasicNameValuePair(
						"action", Seller.SELLER_REMOVE_CATEGORY);
				postParameters.add(actionNaValuePair);
				NameValuePair sellerNameValuePair = new BasicNameValuePair(
						"seller_num", seller_num);
				postParameters.add(sellerNameValuePair);
				NameValuePair sellerCategoryValuePair = new BasicNameValuePair(
						"category", String.valueOf(tmp_category));
				postParameters.add(sellerCategoryValuePair);

				UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(
						postParameters, "euc-kr");

				httpPost.setEntity(urlEncodedFormEntity);

				// ///////////////
				HttpResponse response;
				XmlPullParserFactory factory = XmlPullParserFactory
						.newInstance();
				factory.setNamespaceAware(true);

				response = httpClient.execute(httpPost);
				InputStream inputStream = response.getEntity().getContent();

				UserXmlParser parser = new UserXmlParser(inputStream);

				List<Map<String, String>> sellerMap = parser.parse();

				Message message = handler.obtainMessage(123);

				message.obj = sellerMap;

				handler.sendMessage(message);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (XmlPullParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// 짹�����첩쩔���⒱�
				Message message = handler.obtainMessage(100);
				Bundle bundle = new Bundle();
				bundle.putString("result", "error");
				message.setData(bundle);
				handler.sendMessage(message);
			} finally {
				httpClient.getConnectionManager().shutdown();
				Log.d("httpClient", "shut down");
			}
		} else if (action.equals(Seller.SELLER_REQUEST_POST)) {
			try {
				List<NameValuePair> postParameters = new ArrayList<NameValuePair>();

				NameValuePair actionNaValuePair = new BasicNameValuePair(
						"action", Seller.SELLER_REQUEST_POST);
				postParameters.add(actionNaValuePair);
				NameValuePair sellerNameValuePair = new BasicNameValuePair(
						"post_id", post_id);
				postParameters.add(sellerNameValuePair);

				UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(
						postParameters, "euc-kr");

				httpPost.setEntity(urlEncodedFormEntity);

				// ///////////////
				HttpResponse response;
				XmlPullParserFactory factory = XmlPullParserFactory
						.newInstance();
				factory.setNamespaceAware(true);

				response = httpClient.execute(httpPost);
				InputStream inputStream = response.getEntity().getContent();

				UserXmlParser parser = new UserXmlParser(inputStream);

				ConsumerPost consumerPost = parser.parseConsumerPost();

				Message message = handler.obtainMessage(1);

				message.obj = consumerPost;

				handler.sendMessage(message);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (XmlPullParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// 짹�����첩쩔���⒱�
				Message message = handler.obtainMessage(100);
				Bundle bundle = new Bundle();
				bundle.putString("result", "error");
				message.setData(bundle);
				handler.sendMessage(message);
			} finally {
				httpClient.getConnectionManager().shutdown();
				Log.d("httpClient", "shut down");
			}
		} else if (action.equals(Seller.SELLER_SEND_REPLY)) {
			try {
				MultipartEntity multipartEntity = new MultipartEntity(
						HttpMultipartMode.BROWSER_COMPATIBLE);
				List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
				//
				NameValuePair actionNaValuePair = new BasicNameValuePair(
						"action", Seller.SELLER_SEND_REPLY);
				postParameters.add(actionNaValuePair);
				NameValuePair postValuePair = new BasicNameValuePair("post_id",
						String.valueOf(sellerReply.getPost_id()));
				postParameters.add(postValuePair);
				NameValuePair dateValuePair = new BasicNameValuePair("date",
						sellerReply.getDate());
				postParameters.add(dateValuePair);
				NameValuePair sellerNameValuePair = new BasicNameValuePair(
						"seller_name", sellerReply.getSeller_name());
				postParameters.add(sellerNameValuePair);
				NameValuePair sellerNumValuePair = new BasicNameValuePair(
						"seller_num", sellerReply.getSeller_num());
				postParameters.add(sellerNumValuePair);
				NameValuePair sellerPhoneValuePair = new BasicNameValuePair(
						"seller_phone", sellerReply.getSeller_phone());
				postParameters.add(sellerPhoneValuePair);
				NameValuePair consumerNameValuePair = new BasicNameValuePair(
						"consumer_name", sellerReply.getConsumer_name());
				postParameters.add(consumerNameValuePair);
				NameValuePair contentsValuePair = new BasicNameValuePair(
						"contents", sellerReply.getContents());
				postParameters.add(contentsValuePair);
				NameValuePair pflagValuePair = new BasicNameValuePair("pflag",String.valueOf(sellerReply.getPflag()));
				System.out.println("pflag :"+String.valueOf(sellerReply.getPflag()));
				postParameters.add(pflagValuePair);
				NameValuePair parentValuePair = new BasicNameValuePair("parent",String.valueOf(sellerReply.getParent()));
				postParameters.add(parentValuePair);
				if (sellerReply.getReply_images_name().size() > 0) {
					FileBody fb = null;
					for (int i = 0; i < sellerReply.getReply_images_name()
							.size(); i++) {
						File file = new File(path + "/"
								+ sellerReply.getReply_images_name().get(i));
						fb = new FileBody(file);
						multipartEntity.addPart("imagefb" + String.valueOf(i),
								fb);
						NameValuePair postImageValuePair = new BasicNameValuePair(
								"imagename" + String.valueOf(i), sellerReply
										.getReply_images_name().get(i));
						postParameters.add(postImageValuePair);
						NameValuePair postImageSizeValuePair = new BasicNameValuePair(
								"imagesize" + String.valueOf(i),
								String.valueOf(file.length()));
						postParameters.add(postImageSizeValuePair);
					}

				}
				for (NameValuePair nvp : postParameters) {
					multipartEntity.addPart(nvp.getName(), new StringBody(
							URLEncoder.encode(nvp.getValue(), "EUC-KR")));
				}

				httpPost.setEntity(multipartEntity);
				HttpParams params = httpClient.getParams();
				params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION,
						HttpVersion.HTTP_1_1);
				HttpConnectionParams.setConnectionTimeout(params, 10000);
				HttpConnectionParams.setSoTimeout(params, 10000);

				HttpResponse response;
				XmlPullParserFactory factory = XmlPullParserFactory
						.newInstance();
				factory.setNamespaceAware(true);

				response = httpClient.execute(httpPost);
				InputStream inputStream = response.getEntity().getContent();

				UserXmlParser parser = new UserXmlParser(inputStream);

				List<Map<String, String>> consumerMap = parser
						.parseReplyRowId();

				Message message = handler.obtainMessage(3);

				message.obj = consumerMap;

				handler.sendMessage(message);

			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// 짹�����첩쩔���⒱�
				Message message = handler.obtainMessage(100);
				Bundle bundle = new Bundle();
				bundle.putString("result", "error");
				message.setData(bundle);
				handler.sendMessage(message);
			} finally {
				httpClient.getConnectionManager().shutdown();
				Log.d("httpClient", "shut down");
			}
		} else if (action.equals(Seller.SELLER_REQUEST_REPLY)) {
			try {
				List<NameValuePair> postParameters = new ArrayList<NameValuePair>();

				NameValuePair actionNaValuePair = new BasicNameValuePair(
						"action", Seller.SELLER_REQUEST_REPLY);
				postParameters.add(actionNaValuePair);
				NameValuePair sellerReplyValuePair = new BasicNameValuePair(
						"reply_id", String.valueOf(reply_id));
				postParameters.add(sellerReplyValuePair);

				UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(
						postParameters, "euc-kr");

				httpPost.setEntity(urlEncodedFormEntity);

				// ///////////////
				HttpResponse response;
				XmlPullParserFactory factory = XmlPullParserFactory
						.newInstance();
				factory.setNamespaceAware(true);

				response = httpClient.execute(httpPost);
				InputStream inputStream = response.getEntity().getContent();

				UserXmlParser parser = new UserXmlParser(inputStream);

				Reply reply = parser.parseReply();

				Message message = handler.obtainMessage(3);

				message.obj = reply;

				handler.sendMessage(message);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (XmlPullParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// 짹�����첩쩔���⒱�
				Message message = handler.obtainMessage(100);
				Bundle bundle = new Bundle();
				bundle.putString("result", "error");
				message.setData(bundle);
				handler.sendMessage(message);
			} finally {
				httpClient.getConnectionManager().shutdown();
				Log.d("httpClient", "shut down");
			}
		} else if (action.equals(Seller.SELLER_CHARGE_PLANE)) {
			try {
				List<NameValuePair> postParameters = new ArrayList<NameValuePair>();

				NameValuePair actionNaValuePair = new BasicNameValuePair(
						"action", Seller.SELLER_CHARGE_PLANE);
				postParameters.add(actionNaValuePair);
				NameValuePair sellerNameValuePair = new BasicNameValuePair(
						"seller_num", seller_num);
				postParameters.add(sellerNameValuePair);
				NameValuePair sellerPlaneValuePair = new BasicNameValuePair(
						"plane", String.valueOf(plane));
				postParameters.add(sellerPlaneValuePair);
				Log.d("seller_num", seller_num);
				Log.d("plane", String.valueOf(plane));
				UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(
						postParameters, "euc-kr");

				httpPost.setEntity(urlEncodedFormEntity);

				// ///////////////
				HttpResponse response;
				XmlPullParserFactory factory = XmlPullParserFactory
						.newInstance();
				factory.setNamespaceAware(true);

				response = httpClient.execute(httpPost);
				InputStream inputStream = response.getEntity().getContent();

				UserXmlParser parser = new UserXmlParser(inputStream);

				List<Map<String, String>> sellerMap = parser.parseChargePlane();

				Message message = handler.obtainMessage(3);

				message.obj = sellerMap;

				handler.sendMessage(message);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (XmlPullParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// 짹�����첩쩔���⒱�
				Message message = handler.obtainMessage(100);
				Bundle bundle = new Bundle();
				bundle.putString("result", "error");
				message.setData(bundle);
				handler.sendMessage(message);
			} finally {
				httpClient.getConnectionManager().shutdown();
				Log.d("httpClient", "shut down");
			}
		}
	}
}
