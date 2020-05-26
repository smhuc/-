package com.wangtian.message.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import com.wangtian.message.MyApplication;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;


public class HttpUtil {
	public static String getPostJsonWithUrl(String url,String jsonstr) {
		String retSrc = "";
		try {
			if (Network.checkNetWork(MyApplication.getContext())) {
				
				// 绑定到请求 Entry
				List<NameValuePair> parameters = new ArrayList<NameValuePair>();
				parameters.add(new BasicNameValuePair("c", jsonstr));
				HttpPost request = new HttpPost(url);
				UrlEncodedFormEntity ent = new UrlEncodedFormEntity(parameters,
						"UTF-8");
				request.setEntity(ent);
//				request.setHeader("Accept", "application/json");
				request.setHeader("Accept", "image/gif, image/x-xbitmap, image/jpeg, image/pjpeg, application/x-shockwave-flash, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, application/x-silverlight, */*");
				// 发送请求
				HttpResponse httpResponse = new DefaultHttpClient()
						.execute(request);
				// 得到应答的字符串，这也是一个 JSON 格式保存的数据
				retSrc = EntityUtils.toString(httpResponse.getEntity());
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return retSrc;
	}
	public static String getPostJsonArrayWithUrl(String type,String url, String method, JSONArray array,Context context){
		String retSrc = "";
		JSONObject object = new JSONObject();
		try {
			if (Network.checkNetWork(MyApplication.getContext())) {
				if (!TextUtils.isEmpty(method)) {
					object.put("type",type);
					object.put("action", "");
					object.put("method", method);
					object.put("data", array);
				}
				// 绑定到请求 Entry
				List<NameValuePair> parameters = new ArrayList<NameValuePair>();
				parameters.add(new BasicNameValuePair("c", object.toString()));
				Log.e("request", object.toString());
				HttpPost request = new HttpPost(url);
				UrlEncodedFormEntity ent = new UrlEncodedFormEntity(parameters,
						"UTF-8");
//				BasicHttpParams params = new BasicHttpParams();
				request.setEntity(ent);
				request.setHeader("Accept", "application/json");
				// 发送请求
				HttpResponse httpResponse = new DefaultHttpClient()
						.execute(request);
				// 得到应答的字符串，这也是一个 JSON 格式保存的数据
				retSrc = EntityUtils.toString(httpResponse.getEntity());
			}else{
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("code", 2);
				jsonObject.put("msg", "网络连接失败");
				retSrc = jsonObject.toString();
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(Exception e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return retSrc;
	}
	
}
