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
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.nero.howmuch.Reply;
import com.nero.howmuch.Utils;
import com.nero.howmuch.consumer.Consumer;
import com.nero.howmuch.consumer.ConsumerPost;
import com.nero.howmuch.seller.Seller;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class ConsumerThread extends Thread {
	
	Consumer consumer;
	ConsumerPost consumerPost;
	String checkname;
	StringBuffer ContactsXml;
	InputStream inputStream;
	Handler handler;
	Context mContext;
	String tagName = "";
	String action;
	String url;
	String path;
	int reply_id;
	Reply consumerReply;

	public ConsumerThread(Consumer consumer, String action, Handler handler) {
		this.consumer = consumer;
		this.action = action;
		this.handler = handler;
		this.url = Utils.userUrl;
	}

	public ConsumerThread(String name, String action, Handler handler) {
		this.checkname = name;
		this.action = action;
		this.handler = handler;
		this.url = Utils.userUrl;
	}

	public ConsumerThread(ConsumerPost consumerPost, String action, 
			String path, Handler handler) {
		this.consumerPost = consumerPost;
		this.url = Utils.postUrl;
		this.action = action;
		this.path = path;
		this.handler = handler;
	}

	// reply request thread
	public ConsumerThread(int reply_id, String action, Handler handler){
		this.reply_id = reply_id;
		this.action = action;
		this.handler = handler;
		this.url = Utils.replyUrl;
	}
	// consumer send reply constructor
	public ConsumerThread(Reply _consumerReply, String _path, String _action, Handler _handler){
		this.consumerReply = _consumerReply;
		this.action = _action;
		this.path = _path;
		this.url = Utils.replyUrl;
		this.handler = _handler;
	}
	@Override
	public void run() {

		// TODO Auto-generated method stub
		// HttpConnectAdapter.executeJoin(url,email_id,password,username,gender);
		// 硫�����濡�臾몄���蹂대�湲�
		HttpClient httpClient = new DefaultHttpClient();
		// String url =
		// "http://jsupi.iptime.org:4420/HowmuchServer/user.servlet";

		HttpPost httpPost = new HttpPost(url);
		System.out.println("action:" + action);
		if (action.equals(Consumer.CONSUMER_REGISTER)) {
			try {
				List<NameValuePair> postParameters = new ArrayList<NameValuePair>();

				NameValuePair actionNaValuePair = new BasicNameValuePair(
						"action", Consumer.CONSUMER_REGISTER);
				postParameters.add(actionNaValuePair);
				NameValuePair consumerNameValuePair = new BasicNameValuePair(
						"consumer_name", consumer.getConsumer_name());
				postParameters.add(consumerNameValuePair);
				NameValuePair consumerGcmIdValuePair = new BasicNameValuePair(
						"gcm_id", consumer.getGcm_id());
				postParameters.add(consumerGcmIdValuePair);

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
			}catch (Exception e) {
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
		} else if(action.equals(Consumer.CONSUMER_UPDATE_GCM)){
			try {
				List<NameValuePair> postParameters = new ArrayList<NameValuePair>();

				NameValuePair actionNaValuePair = new BasicNameValuePair(
						"action", Consumer.CONSUMER_UPDATE_GCM);
				postParameters.add(actionNaValuePair);
				NameValuePair consumerNameValuePair = new BasicNameValuePair(
						"consumer_name", consumer.getConsumer_name());
				postParameters.add(consumerNameValuePair);
				NameValuePair consumerGcmIdValuePair = new BasicNameValuePair(
						"gcm_id", consumer.getGcm_id());
				postParameters.add(consumerGcmIdValuePair);
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
			}catch (Exception e) {
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
		}else if (action.equals(Consumer.CONSUMER_CHECKNAME)) {
			try {
				List<NameValuePair> postParameters = new ArrayList<NameValuePair>();

				NameValuePair actionNaValuePair = new BasicNameValuePair(
						"action", Consumer.CONSUMER_CHECKNAME);
				postParameters.add(actionNaValuePair);
				NameValuePair consumerNameValuePair = new BasicNameValuePair(
						"consumer_name", checkname);
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
		} else if (action.equals(Consumer.CONSUMER_POST)) {
			// 援щℓ��post.
			try {
				MultipartEntity multipartEntity = new MultipartEntity(
						HttpMultipartMode.BROWSER_COMPATIBLE);
				List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
				//
				NameValuePair actionNaValuePair = new BasicNameValuePair(
						"action", Consumer.CONSUMER_POST);
				postParameters.add(actionNaValuePair);
				NameValuePair dateValuePair = new BasicNameValuePair("date",
						consumerPost.getDate());
				postParameters.add(dateValuePair);
				NameValuePair consumerNameValuePair = new BasicNameValuePair(
						"consumer_name", consumerPost.getConsumer_name());
				postParameters.add(consumerNameValuePair);
				NameValuePair titleValuePair = new BasicNameValuePair("title",
						consumerPost.getTitle());
				postParameters.add(titleValuePair);
				NameValuePair categoryValuePair = new BasicNameValuePair(
						"category", consumerPost.getCategory());
				postParameters.add(categoryValuePair);
				NameValuePair cityValuePair = new BasicNameValuePair("city",
						consumerPost.getCity());
				postParameters.add(cityValuePair);
				NameValuePair contentsValuePair = new BasicNameValuePair(
						"contents", consumerPost.getContents());
				postParameters.add(contentsValuePair);
				NameValuePair imgcntValuePair = new BasicNameValuePair(
						"image_count", String.valueOf(consumerPost
								.getPost_images_name().size()));
				postParameters.add(imgcntValuePair);
				if (consumerPost.getPost_images_name().size() > 0) {
					
					for (int i = 0; i < consumerPost.getPost_images_name()
							.size(); i++) {
						FileBody fb = null;
						File file = new File(path + "/"
								+ consumerPost.getPost_images_name().get(i));
						fb = new FileBody(file);
						multipartEntity.addPart("imagefb" + String.valueOf(i),
								fb);
						NameValuePair postImageValuePair = new BasicNameValuePair(
								"imagename" + String.valueOf(i), consumerPost
										.getPost_images_name().get(i));
						Log.d("image name", i+","+ consumerPost.getPost_images_name().get(i));
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

				List<Map<String, String>> consumerMap = parser.parsePostRowId();

				Message message = handler.obtainMessage(1);

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
		} else if(action.equals(Consumer.CONSUMER_REQUEST_REPLY)){
			try {
				List<NameValuePair> postParameters = new ArrayList<NameValuePair>();

				NameValuePair actionNaValuePair = new BasicNameValuePair(
						"action", Consumer.CONSUMER_REQUEST_REPLY);
				postParameters.add(actionNaValuePair);
				NameValuePair consumerReplyValuePair = new BasicNameValuePair(
						"reply_id", String.valueOf(reply_id));
				postParameters.add(consumerReplyValuePair);

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

				Message message = handler.obtainMessage(2);

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
			}catch (Exception e) {
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
		}else if(action.equals(Consumer.CONSUMER_SEND_REPLY)){
			try {
				MultipartEntity multipartEntity = new MultipartEntity(
						HttpMultipartMode.BROWSER_COMPATIBLE);
				List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
				//
				NameValuePair actionNaValuePair = new BasicNameValuePair(
						"action", Consumer.CONSUMER_SEND_REPLY);
				postParameters.add(actionNaValuePair);
				NameValuePair postValuePair = new BasicNameValuePair("post_id",String.valueOf(consumerReply.getPost_id()));
				postParameters.add(postValuePair);
				NameValuePair dateValuePair = new BasicNameValuePair("date",
						consumerReply.getDate());
				postParameters.add(dateValuePair);
				NameValuePair sellerNameValuePair = new BasicNameValuePair("seller_name",consumerReply.getSeller_name());
				postParameters.add(sellerNameValuePair);
				NameValuePair sellerNumValuePair = new BasicNameValuePair("seller_num",consumerReply.getSeller_num());
				postParameters.add(sellerNumValuePair);
				NameValuePair sellerPhoneValuePair = new BasicNameValuePair("seller_phone",consumerReply.getSeller_phone());
				postParameters.add(sellerPhoneValuePair);
				NameValuePair consumerNameValuePair = new BasicNameValuePair(
						"consumer_name", consumerReply.getConsumer_name());
				postParameters.add(consumerNameValuePair);
				NameValuePair contentsValuePair = new BasicNameValuePair(
						"contents", consumerReply.getContents());
				postParameters.add(contentsValuePair);
				
				NameValuePair pflagValuePair = new BasicNameValuePair("pflag",String.valueOf(consumerReply.getPflag()));
				postParameters.add(pflagValuePair);
				NameValuePair parentValuePair = new BasicNameValuePair("parent",String.valueOf(consumerReply.getParent()));
				postParameters.add(parentValuePair);
				if (consumerReply.getReply_images_name().size() > 0) {
					FileBody fb = null;
					for (int i = 0; i < consumerReply.getReply_images_name()
							.size(); i++) {
						File file = new File(path + "/"
								+ consumerReply.getReply_images_name().get(i));
						fb = new FileBody(file);
						multipartEntity.addPart("imagefb" + String.valueOf(i),
								fb);
						NameValuePair postImageValuePair = new BasicNameValuePair(
								"imagename" + String.valueOf(i), consumerReply
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

				List<Map<String, String>> consumerMap = parser.parseReplyRowId();

				Message message = handler.obtainMessage(1);

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
		}else if(action.equals(Consumer.CONSUMER_LOGOUT)){
			try {
				List<NameValuePair> postParameters = new ArrayList<NameValuePair>();

				NameValuePair actionNaValuePair = new BasicNameValuePair(
						"action", Consumer.CONSUMER_LOGOUT);
				postParameters.add(actionNaValuePair);
				NameValuePair consumerNameValuePair = new BasicNameValuePair(
						"consumer_name", checkname);
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
