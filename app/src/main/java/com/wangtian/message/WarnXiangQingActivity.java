package com.wangtian.message;

import java.util.Timer;
import java.util.TimerTask;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushManager;
import com.wangtian.message.base.BaseMenuActivity;
import com.wangtian.message.bean.Article;
import com.wangtian.message.util.Contact;
import com.wangtian.message.util.HttpUtil;
import com.wangtian.message.util.NewToast;

public class WarnXiangQingActivity extends BaseMenuActivity {

	private PopupWindow pop;
//	private Animation animation;
	private ProgressDialog dialog;
	private WebView web;
	private Article article;
	private String HTML0 = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"><html xmlns=\"http://www.w3.org/1999/xhtml\"><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" /><meta id=\"viewport\" name=\"viewport\" content=\"width=device-width, maximum-scale=2.0,minimum-scale=1.1,initial-scale=";
	private String HTML1 = "\" /><title></title><style type=\"text/css\"><!--*{margin:0px;padding:0px;}li{list-style-type:none;}img{border:0px;width:90%;}body{margin:0px;padding:0px;font-size:12px;text-align:center;}#container{width:100%;margin:0px auto;overflow:hidden;zoom:1;background-image:url(images/bk-1.gif);background-repeat:no-repeat;}.head{width:90%;margin-top:20px;margin-left:5%;}.head_bt{width:100%;font-size:16px;font-weight:bold;}.head_ly{width:100%;color:#666666;margin-top:20px;}.video{margin-top:16px;}.content{width:96%;text-align:left;margin-top:18px;;margin-bottom:18px;margin-left:3%;line-height:26px;font-size:18px;letter-spacing:2px;}--></style></head><body><div id=\"container\"><div class=\"head\"><div class=\"head_bt\">";
	private String HTML2 = "</div><div class=\"head_ly\">";
	private String HTML3 = "</div></div><div class=\"content\">";
	private String HTML4 = "</div></div></body></html>";
	private String type,articleid,url;
	private ImageView right;
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				if(pop != null && pop.isShowing()){
					pop.dismiss();
				}
				break;

			default:
				break;
			}
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_warnxq);
		setActivity(WarnXiangQingActivity.this,"舆情详情");
		left(1).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
			right = (ImageView) right(3);
			right.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(pop != null && pop.isShowing()){
						pop.dismiss();
					}else{
						if(article.getCollectState() == 0){
							postCollect((ImageView)v);
						}else{
							cancalCollect((ImageView)v);
						}
					}
				}
			});
			if(getIntent().getBooleanExtra("show", true)){
				right.setVisibility(View.VISIBLE);
			}else{
				right.setVisibility(View.GONE);
			}
		init();
	}

	@Override
	protected void onStart() {
		super.onStart();
		XGPushClickedResult click = XGPushManager.onActivityStarted(this);
		if (click != null) {
			String customContent = click.getCustomContent();
			if (customContent != null && customContent.length() != 0) {
				try {
					JSONObject json = new JSONObject(customContent);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	}
	private TextView tx_collect;
	private void init() {
		dialog = new ProgressDialog(WarnXiangQingActivity.this);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);
		web = (WebView) findViewById(R.id.web);
		articleid = getIntent().getStringExtra("articleid");
		type = getIntent().getStringExtra("type");
		getDate();
		View view = getLayoutInflater().inflate(R.layout.collect_pop_window, null);
		tx_collect = (TextView) view.findViewById(R.id.tx_collect);
		pop = new PopupWindow(view, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, false);
		
	}
	/**
	 * 获取信息
	 */
	public void getDate() {
		dialog.setMessage("数据加载中...");
		dialog.show();
		new AsyncTask<Void, Void, String>() {
			
			@Override
			protected String doInBackground(Void... arg0) {
				String retSrc = null;
				try {
					JSONObject jsonObject = new JSONObject();
//					jsonObject.put("groupId", MyApplication.user.getGroupid());
					jsonObject.put("type", type);
					jsonObject.put("articleId", articleid);
					jsonObject.put("userId", MyApplication.user.getId());
					retSrc = HttpUtil.getPostJsonWithUrl(Contact.url+"Warn/WarnGet",jsonObject.toString());
					Log.e("retSrc", retSrc.toString());
				} catch (Exception e) {
					e.printStackTrace();
				}
				return retSrc;
			}

			@Override
			protected void onPostExecute(String result) {
				try {
					JSONObject object = new JSONObject(result);
					int code = object.getInt("code");
					if(code == 1){
						JSONObject data = (JSONObject) object.get("data");
						if(article == null){
							article = new Article();
						}
						article.setAuthor(data.getString("author"));
						article.setCommentCount(data.getInt("commentCount"));
						if(article.getCommentCount() > 0){
							article.setCmments(data.getString("cmments"));
						}
						article.setContent(data.getString("content"));
						article.setOrigin(data.getString("origin"));
						article.setPublishDate(data.getString("publishDate"));
						article.setRowKey(data.getString("rowKey"));
						article.setTags(data.getString("tags"));
						article.setType(data.getString("type"));
						article.setTitle(data.getString("title"));
						article.setUrl(data.getString("url"));
						article.setCollectState(data.getInt("collectState"));
						url = article.getUrl();
						getShareDate();
						if(getIntent().getBooleanExtra("show", true)){
							if(article.getCollectState() == 0){
								right.setImageResource(R.drawable.header_col_nor);
							}else{
								right.setImageResource(R.drawable.header_col_pre);
							}
						}
						web.loadDataWithBaseURL(null,HTML0+HTML1+article.getTitle()+HTML2+article.getOrigin()+"       "+article.getPublishDate()+HTML3+article.getContent()+HTML4, "text/html", "utf-8",null);
					}else if(code == 2){
						String msg = object.getString("msg");
						NewToast.makeText(WarnXiangQingActivity.this,msg, 0).show();
					}else{
						NewToast.makeText(WarnXiangQingActivity.this,"系统异常", 0).show();
					}
					if(dialog != null && dialog.isShowing()){
						dialog.dismiss();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					if(dialog != null && dialog.isShowing()){
						dialog.dismiss();
					}
					NewToast.makeText(WarnXiangQingActivity.this,"数据获取失败"+e.toString(), 0).show();
				}
			}
			
		}.execute();
	}
	/**
	 * 收藏
	 */
	public void postCollect(final ImageView view) {
		dialog.setMessage("收藏中...");
		dialog.show();
		new AsyncTask<Void, Void, String>() {
			
			@Override
			protected String doInBackground(Void... arg0) {
				String retSrc = null;
				try {
					JSONObject jsonObject = new JSONObject();
//					jsonObject.put("groupId", MyApplication.user.getGroupid());
					jsonObject.put("userId", MyApplication.user.getId());
					jsonObject.put("type", article.getType());
					jsonObject.put("name", article.getTitle());
					jsonObject.put("url", article.getUrl());
					jsonObject.put("source", "1");
					jsonObject.put("articleid", articleid);
					jsonObject.put("article_publish_date", article.getPublishDate());
					retSrc = HttpUtil.getPostJsonWithUrl(Contact.url+"Collect/CollectAdd",jsonObject.toString());
					Log.e("retSrc", retSrc.toString());
				} catch (Exception e) {
					e.printStackTrace();
				}
				return retSrc;
			}
			
			@Override
			protected void onPostExecute(String result) {
				try {
					JSONObject object = new JSONObject(result);
					int code = object.getInt("code");
					if(code == 1){
						article.setCollectState(1);
						tx_collect.setText("收藏成功");
						pop.showAtLocation(findViewById(R.id.lin_layout), Gravity.CENTER,0,0);
//						animation.start();
						Timer timer = new Timer();
						TimerTask task = new TimerTask() {
							
							@Override
							public void run() {
								handler.sendEmptyMessage(1);
							}
						};
						timer.schedule(task, 2000);
						view.setImageResource(R.drawable.header_col_pre);
					}else if(code == 2){
						String msg = object.getString("msg");
						NewToast.makeText(WarnXiangQingActivity.this,msg, 0).show();
					}else{
						NewToast.makeText(WarnXiangQingActivity.this,"系统异常", 0).show();
					}
					if(dialog != null && dialog.isShowing()){
						dialog.dismiss();
					}   
				} catch (JSONException e) {
					e.printStackTrace();
					if(dialog != null && dialog.isShowing()){
						dialog.dismiss();
					}
					NewToast.makeText(WarnXiangQingActivity.this,"数据获取失败", 0).show();
				}
			}
			
		}.execute();
	}
	/**
	 * 取消收藏
	 */
	public void cancalCollect(final ImageView view) {
		dialog.setMessage("取消收藏中...");
		dialog.show();
		new AsyncTask<Void, Void, String>() {
			
			@Override
			protected String doInBackground(Void... arg0) {
				String retSrc = null;
				try {
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("userId", MyApplication.user.getId());
					jsonObject.put("articleid", articleid);
					retSrc = HttpUtil.getPostJsonWithUrl(Contact.url+"Collect/OpinionCollectDel",jsonObject.toString());
					Log.e("retSrc", retSrc.toString());
				} catch (Exception e) {
					e.printStackTrace();
				}
				return retSrc;
			}
			
			@Override
			protected void onPostExecute(String result) {
				try {
					JSONObject object = new JSONObject(result);
					int code = object.getInt("code");
					if(code == 1){
						tx_collect.setText("取消收藏成功");
						article.setCollectState(0);
						pop.showAtLocation(findViewById(R.id.lin_layout), Gravity.CENTER,0,0);
//						animation.start();
						Timer timer = new Timer();
						TimerTask task = new TimerTask() {
							
							@Override
							public void run() {
								handler.sendEmptyMessage(1);
							}
						};
						timer.schedule(task, 2000);
						view.setImageResource(R.drawable.header_col_nor);
					}else if(code == 2){
						String msg = object.getString("msg");
						NewToast.makeText(WarnXiangQingActivity.this,msg, 0).show();
					}else{
						NewToast.makeText(WarnXiangQingActivity.this,"系统异常", 0).show();
					}
					if(dialog != null && dialog.isShowing()){
						dialog.dismiss();
					}   
				} catch (JSONException e) {
					e.printStackTrace();
					if(dialog != null && dialog.isShowing()){
						dialog.dismiss();
					}
					NewToast.makeText(WarnXiangQingActivity.this,"数据获取失败", 0).show();
				}
			}
			
		}.execute();
	}
	/**
	 * 获取分享的路径
	 */
	public void getShareDate() {
		dialog.setMessage("数据加载中...");
		dialog.show();
		new AsyncTask<Void, Void, String>() {
			
			@Override
			protected String doInBackground(Void... arg0) {
				String retSrc = null;
				try {
					JSONObject jsonObject = new JSONObject();
//					jsonObject.put("groupId", MyApplication.user.getGroupid());
					jsonObject.put("url", url);
					jsonObject.put("userId", MyApplication.user.getId());
					retSrc = HttpUtil.getPostJsonWithUrl(Contact.url+"Opinion/Shared",jsonObject.toString());
					Log.e("retSrc", retSrc.toString());
				} catch (Exception e) {
					e.printStackTrace();
				}
				return retSrc;
			}
			
			@Override
			protected void onPostExecute(String result) {
				try {
					JSONObject object = new JSONObject(result);
					int code = object.getInt("code");
					if(code == 1){
						String data = object.getString("data");
						Share(getResources().getString(R.string.app_name), article.getTitle(), Contact.url_no+"/shared/"+data.toString());
					}else if(code == 2){
						String msg = object.getString("msg");
						NewToast.makeText(WarnXiangQingActivity.this,msg, 0).show();
					}else{
						NewToast.makeText(WarnXiangQingActivity.this,"系统异常", 0).show();
					}
					if(dialog != null && dialog.isShowing()){
						dialog.dismiss();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					if(dialog != null && dialog.isShowing()){
						dialog.dismiss();
					}
					NewToast.makeText(WarnXiangQingActivity.this,"数据获取失败", 0).show();
				}
			}
			
		}.execute();
	}

	@Override
	public void finish() {
		if(pop != null && pop.isShowing()){
			pop.dismiss();
		}
		super.finish();
	}
	
}
