package com.wangtian.message;

import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.wangtian.message.base.BaseMenuActivity;
import com.wangtian.message.bean.Article;
import com.wangtian.message.bean.Comment;
import com.wangtian.message.util.Contact;
import com.wangtian.message.util.HttpUtil;
import com.wangtian.message.util.NewToast;
import com.wangtian.message.util.Utility;

public class HotXiangQingActivity extends BaseMenuActivity {

	private Article article;
	private String url;
	private ScrollView scrollview;
	private WebView web;
	private String HTML0 = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"><html xmlns=\"http://www.w3.org/1999/xhtml\"><head><meta http-equiv=\"Content-Type\"content=\"text/html; charset=utf-8\"/><meta id=\"viewport\"name=\"viewport\"content=\"width=device-width, maximum-scale=1.0,minimum-scale=1.0,initial-scale=\"/><title></title><style type=\"text/css\"><!--* { margin: 0px; padding: 0px; }li { list-style-type: none; }img { border: 0px; width: 90%; }body { margin: 0px; padding: 0px; font-size: 11px; }#container { width: 100%; margin: 0px auto; overflow: hidden; zoom: 1; background-image: url(images/bk-1.gif); background-repeat: no-repeat; }.head { width: 90%; margin-top: 20px; margin-left: 5%; }.head_bt { width: 100%; font-size: 16px; font-weight: bold; }.head_ly { width: 100%; color: #666666; margin-top: 20px; }.video { margin-top: 16px; }.content { width: 96%; text-align: left; margin-top: 18px; margin-bottom: 18px; margin-left: 3%; line-height: 26px; font-size: 16px; letter-spacing: 2px; }--></style></head><body><div id=\"container\"><div class=\"head\"><div class=\"head_bt\">";
	private String HTML1 = "</div><div class=\"head_ly\">";
	private String HTML3 = "</div></div><div class=\"content\">";
	private String HTML4 = "</div></div></body></html>";
	private ProgressDialog dialog;
	private LinearLayout lin;
	private ArrayList<Comment> temp_comments;
	private ArrayList<Comment> comments;
	private MyPinLunAdapter adapter;
	private ListView plist;
	private RelativeLayout rel_never;
	private TextView more;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hotxq);
		setActivity(HotXiangQingActivity.this,"热点详情");
		left(1);
		
		ImageView img = (ImageView) right(3);
		img.setImageResource(R.drawable.header_comment_ico_red);
		img.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				scrollview.scrollTo(0, web.getHeight());
			}
		});
		init();
	}

	private void init() {
		article = (Article) getIntent().getSerializableExtra("data");
		rel_never = (RelativeLayout) findViewById(R.id.rel_never);
		url = article.getUrl();
		web = (WebView) findViewById(R.id.web);
		dialog = new ProgressDialog(HotXiangQingActivity.this);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);
		lin = (LinearLayout) findViewById(R.id.lin_pinlun);
		scrollview = (ScrollView) findViewById(R.id.scrollview);
		plist = (ListView) findViewById(R.id.list_pinlun);
		getDate();
		getShareDate();
		web.getSettings().setDefaultTextEncodingName("utf-8") ;
		web.getSettings().setSupportZoom(false);
		web.getSettings().setBuiltInZoomControls(false);
		web.setVerticalScrollBarEnabled(false);
		web.setVerticalScrollbarOverlay(false);
		web.setScrollbarFadingEnabled(false);
		more = (TextView) findViewById(R.id.tx_more);
		more.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(temp_comments.size() > 2){
					comments.clear();
					comments.addAll(temp_comments);
					adapter.notifyDataSetChanged();
					Utility.setListViewHeightBasedOnChildren(plist);
				}else{
					NewToast.makeText(getApplication(),"没有更多评论", 0).show();
				}
			}
		});
	}
	/**
	 * 获取详情
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
					jsonObject.put("url", url);
					retSrc = HttpUtil.getPostJsonWithUrl(Contact.url+"Opinion/Get",jsonObject.toString());
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
//								if(article.getCommentCount() > 0){
								if(data.getString("cmments") != null){
									article.setCmments(data.getString("cmments"));
								}
//								}
								article.setContent(data.getString("content"));
								article.setOrigin(data.getString("origin"));
								article.setPublishDate(data.getString("publishDate"));
								article.setRowKey(data.getString("rowKey"));
								article.setTags(data.getString("tags"));
								article.setTitle(data.getString("title"));
								article.setUrl(data.getString("url"));
//								web.loadDataWithBaseURL(null,HTML0+HTML1+article.getTitle()+HTML2+article.getOrigin()+"       "+article.getPublishDate()+HTML3+article.getContent()+HTML4, "text/html", "utf-8",null);
								web.loadDataWithBaseURL(null,HTML0+article.getTitle()+HTML1+article.getOrigin()+"       "+article.getPublishDate()+HTML3+article.getContent()+HTML4, "text/html", "utf-8",null);
					}else if(code == 2){
						String msg = object.getString("msg");
						NewToast.makeText(HotXiangQingActivity.this,msg, 0).show();
					}else{
						NewToast.makeText(HotXiangQingActivity.this,"系统异常", 0).show();
					}
					if(temp_comments == null){
						temp_comments = new ArrayList<Comment>();
					}
					if(comments == null){
						comments = new ArrayList<Comment>();
					}
//					if(article.getCommentCount() > 0){
					if(article.getCmments() != null){
//						adapter = new MyPinLunAdapter(new JSONArray(article.getCmments()));
						JSONArray array = new JSONArray(article.getCmments());
						for (int i = 0; i < array.length(); i++) {
							Comment comment = new Comment();
							JSONObject object2 = array.getJSONObject(i);
							comment.setArticleId(object2.getString("articleId"));
							comment.setAuthor(object2.getString("author"));
							comment.setCommentCount(object2.getInt("commentCount"));
//							comment.setContent(object2.getString("content"));
							comment.setPublishDate(object2.getString("publishDate"));
							comment.setRowkey(object2.getString("rowkey"));
//							comment.setTags(object2.getString("tags"));
							comment.setTitle(object2.getString("title"));
							comment.setUrl(object2.getString("url"));
							temp_comments.add(comment);
							if(i < 2){
								comments.add(comment);
							}
						}
						if(temp_comments.size() < 1){
							rel_never.setVisibility(View.VISIBLE);
							more.setVisibility(View.GONE);
						}else{
							rel_never.setVisibility(View.GONE);
							more.setVisibility(View.VISIBLE);
						}
						adapter = new MyPinLunAdapter(comments);
						plist.setAdapter(adapter);
						Utility.setListViewHeightBasedOnChildren(plist);
					}else{
						rel_never.setVisibility(View.VISIBLE);
						lin.setVisibility(View.GONE);
					}
					if(dialog != null && dialog.isShowing()){
						dialog.dismiss();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					more.setVisibility(View.GONE);
					if(dialog != null && dialog.isShowing()){
						dialog.dismiss();
					}
				}
			}
			
		}.execute();
	}
	/**
	 * 获取分享的路径
	 */
	StringBuffer buffer;
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
					jsonObject.put("url", url.toString());
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
						NewToast.makeText(HotXiangQingActivity.this,msg, 0).show();
					}else{
						NewToast.makeText(HotXiangQingActivity.this,"系统异常", 0).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
//					if(dialog != null && dialog.isShowing()){
//						dialog.dismiss();
//					}
				}
			}
			
		}.execute();
	}
	class MyPinLunAdapter extends BaseAdapter{
		
//		private JSONArray array;
		private ArrayList<Comment> comments;
		
		
		public MyPinLunAdapter(ArrayList<Comment> comments) {
			super();
			this.comments = comments;
		}
		@Override
		public int getCount() {
			if(comments == null){
				return 0;
			}
			return comments.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView name,time,content;
			if(convertView == null){
				convertView = getLayoutInflater().inflate(R.layout.pinlun_item, null);
				name = (TextView) convertView.findViewById(R.id.tx_name);
				time = (TextView) convertView.findViewById(R.id.tx_time);
				content = (TextView) convertView.findViewById(R.id.tx_content);
				convertView.setTag(R.id.tx_name, name);
				convertView.setTag(R.id.tx_time, time);
				convertView.setTag(R.id.tx_content, content);
			}else{
				name = (TextView) convertView.getTag(R.id.tx_name);
				time = (TextView) convertView.getTag(R.id.tx_time);
				content = (TextView) convertView.getTag(R.id.tx_content);
			}
				name.setText(comments.get(position).getAuthor());
				time.setText(zhuanhua(comments.get(position).getPublishDate()));
				content.setText(comments.get(position).getTitle());
//				Utility.setListViewHeightBasedOnChildren(plist);
			return convertView;
		}
		
	}
	private String zhuanhua(String date){
		if(date != null){
			String[] strs = date.split("-");
			Calendar calendar = Calendar.getInstance();
			StringBuffer buffer = new StringBuffer();
			int year  = calendar.get(Calendar.YEAR);
			int a = year - Integer.parseInt(strs[0]);
			if(a > 0){
				return a+"年前";
			}else{
				int month = calendar.get(Calendar.MONTH);
				int b = month+1 - Integer.parseInt(strs[1]);
				if(b > 0){
					return b+"个月前";
				}else{
					int day = calendar.get(Calendar.DAY_OF_MONTH);
					String[] str = strs[2].split(" ");
					String[] st = str[1].split(":");
					if(st.length >1){
						buffer.append("  "+st[0]+":"+st[1]);
					}
					int c = day - Integer.parseInt(str[0]);
					if(c > 0){
						return c+"天前";
					}else{
						int hour = calendar.get(Calendar.HOUR_OF_DAY);
						int d = hour - Integer.parseInt(st[0]);
						if(d > 0){
							return d +"个小时前";
						}else{
							int minute = calendar.get(Calendar.MINUTE);
							int e = minute - Integer.parseInt(st[1]);
							return e+"分钟前";
						}
					}
				}
			}
		}else{
			return null;
		}
	}
}
