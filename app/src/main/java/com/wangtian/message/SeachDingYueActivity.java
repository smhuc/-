package com.wangtian.message;

import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.wangtian.message.util.Contact;
import com.wangtian.message.util.DateUtil;
import com.wangtian.message.util.HttpUtil;
import com.wangtian.message.util.Network;
import com.wangtian.message.util.NewToast;
import com.wangtian.message.view.XListView;
import com.wangtian.message.view.XListView.IXListViewListener;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView.OnEditorActionListener;
/**
 * 查询（订阅）
 * @author chen
 *
 */
public class SeachDingYueActivity extends Activity {

	private ImageView cancal;
	private EditText edit;
	private ArrayList<HashMap<String,Object>> list;
	private MyAdatper adapter;
	private ProgressDialog dialog;
	private String type;
	private Integer tagId;
	private XListView seach;
	private ArrayList<HashMap<String,Object>> temp_list;
	private PopupWindow pop;
	private int count = 1;//用于表示下一页
	private String state;
	private boolean downup;
	private String key;//现在在查询的关键字
	private int pagesize = 20;
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			downup = false;
			if (key != null && key.length() > 0 && state.equals("")) {
				// 结束
				seach.mScrollBack = 0;
				seach.mScroller.startScroll(0, 100, 0, 0 - 100, 400);
			} //
			switch (msg.what) {
			case 2328:
				if (state.equals("up")) { // 上拉
					if (temp_list.size() > 0) {
						list.addAll(temp_list);
						count += 1;
						adapter.notifyDataSetChanged();
					} else {
						NewToast.makeText(SeachDingYueActivity.this, "暂无更多数据", 0)
								.show();
					}
				} else {
					if (temp_list.size() > 0) {
						list.clear();
						count = 1;
						list.addAll(temp_list);
					}
					adapter.notifyDataSetChanged();
				}

				break;
			case 2222:
				NewToast.makeText(SeachDingYueActivity.this, "请检查网络连接", 1).show();
				break;
			case 555:
				NewToast.makeText(SeachDingYueActivity.this, "请检查网络连接", 1).show();
				break;
			case 2333:
				NewToast.makeText(SeachDingYueActivity.this, "暂无更多数据", 1).show();
			case 2444:
				NewToast.makeText(SeachDingYueActivity.this, "服务器暂无数据", 1).show();
				break;
			default:
				break;
			}
			seach.stopRefresh();
			seach.stopLoadMore();
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_seach_dingyue);
		init();
	}

	private void init() {
		findViewById(R.id.tx_cancal).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(type != null && (type.equals("topic") || type.equals("category"))){
					finish();
				}else{
					Intent intent = new Intent(SeachDingYueActivity.this, AddDingYueActivity.class);
					intent.putExtra("tagId", tagId);
					setResult(1000, intent);
					finish();
				}
			}
		});
		dialog = new ProgressDialog(SeachDingYueActivity.this);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);
		type = getIntent().getStringExtra("type");
		tagId = getIntent().getIntExtra("tagId",-1);
		edit = (EditText) findViewById(R.id.edit);
		cancal = (ImageView) findViewById(R.id.img_cancal);
		edit.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				if(s != null && s.length() > 0){
					cancal.setVisibility(View.VISIBLE);
				}else{
					cancal.setVisibility(View.GONE);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		cancal.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				edit.setText("");
				key = null;
			}
		});
		edit.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if(event != null && event.getKeyCode() ==  KeyEvent.KEYCODE_ENTER){
					if(key != null && key.equals(v.getText().toString().trim())){
					}else{
						key =  v.getText().toString().trim();
						if(type != null && (type.equals("topic") || type.equals("category"))){
							getDate(key,0,false);
						}else{
							getDate(key,0,false);
						}
					}
					return true;
				}else if(event == null && actionId == 3){
					if(key != null && key.equals(v.getText().toString().trim())){
					}else{
						key =  v.getText().toString().trim();
						if(type != null && (type.equals("topic") || type.equals("category"))){
							getDate(key,0,false);
						}else{
							getDate(key,0,false);
						}
					}
					return true;
				}
				return false;
			}
		});
		seach = (XListView) findViewById(R.id.list_seach);
		if(type != null && (type.equals("topic") || type.equals("category"))){//这个是专题的列表
			seach.setPullLoadEnable(true);
			seach.setPullRefreshEnable(true);
		}else{
			seach.setPullLoadEnable(false);
			seach.setPullRefreshEnable(false);
		}
		seach.setXListViewListener(new IXListViewListener() {
			
			@Override
			public void onRefresh() {
				if(type != null && (type.equals("topic") || type.equals("category"))){//这个是专题的列表
				handler.postDelayed(new Runnable() {
					public void run() {
						if (downup == false) {
							if (Network.checkNetWork(SeachDingYueActivity.this)) {
								// initializeData();
								state = "down";
								downup = true;
								getDate(key,0,true);
							} else {
								handler.sendEmptyMessage(555);
							}

						}
					}
				}, 2000);
				}
			}
			
			@Override
			public void onLoadMore() {
				if(type != null && (type.equals("topic") || type.equals("category"))){//这个是专题的列表
				handler.postDelayed(new Runnable() {
					public void run() {
						if (downup == false) {
							if (Network.checkNetWork(SeachDingYueActivity.this)) {
								// initializeData();
								state = "up";
								downup = true;
								getDate(key,count*pagesize,true);
							} else {
								handler.sendEmptyMessage(555);
							}
						}
					}
				}, 2000);
				}
			}
		});
		list = new ArrayList<HashMap<String,Object>>();
		adapter = new MyAdatper();
		seach.setAdapter(adapter);
	}
	class MyAdatper extends BaseAdapter{

		@Override
		public int getCount() {
			return list.size();
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
		public View getView(final int position, View convertView, ViewGroup parent) {
			TextView name,check;
			if(type != null && (type.equals("topic") || type.equals("category"))){//这个是专题的列表
				if(convertView ==null){
					convertView = getLayoutInflater().inflate(R.layout.info_list_item,null);
					name = (TextView) convertView.findViewById(R.id.tx_title);
					check = (TextView) convertView.findViewById(R.id.tx_time);
					convertView.setTag(R.id.tx_title, name);
					convertView.setTag(R.id.tx_time, check);
				}else{
					name = (TextView) convertView.getTag(R.id.tx_title);
					check = (TextView) convertView.getTag(R.id.tx_time);
				}
				if(position != 0){
					convertView.findViewById(R.id.view).setVisibility(View.GONE);
				}
				name.setText(list.get(position).get("name").toString());
				check.setText(DateUtil.getDate1(list.get(position).get("time").toString()));
				convertView.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(SeachDingYueActivity.this,InfoXiangQingActivity.class);
						intent.putExtra("id",list.get(position).get("id").toString());
						intent.putExtra("name",list.get(position).get("name").toString());
						startActivity(intent);
					}
				});
				return convertView;
			}else{//这个是订阅的列表
				if(convertView == null){
					convertView = getLayoutInflater().inflate(R.layout.list_contact_item, null);
					name = (TextView) convertView.findViewById(R.id.tx_name);
					check = (TextView) convertView.findViewById(R.id.tx_check);
					convertView.setTag(R.id.tx_name,name);
					convertView.setTag(R.id.tx_check,check);
				}else{
					name = (TextView) convertView.getTag(R.id.tx_name);
					check = (TextView) convertView.getTag(R.id.tx_check);
				}
				name.setText(list.get(position).get("name").toString());
				if(Integer.parseInt(list.get(position).get("check").toString()) == 1){
					check.setBackgroundResource(R.drawable.subscribe_btn_pre);
					check.setTextColor(getResources().getColor(R.color.text_hui1));
					check.setText("已订");
					check.setTag("false");
				}else{
					check.setBackgroundResource(R.drawable.subscribe_btn_nor);
					check.setTextColor(getResources().getColor(R.color.text_red));
					check.setText("订阅");
					check.setTag("true");
				}
				check.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						TextView text = (TextView) v;
						if(v.getTag().toString().equals("true")){
							addDingYue(Integer.parseInt(list.get(position).get("id").toString()),text,position);
						}else{
							DelectDingYue(Integer.parseInt(list.get(position).get("id").toString()),text,position);
						}
					}
				});
			}
			convertView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					initPop(list.get(position));
				}
			});
			return convertView;
		}
	}
	private void initPop(HashMap<String,Object> subscribe) {
		View view = getLayoutInflater().inflate(R.layout.item_pop_window,null);
		pop = new PopupWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);
		view.findViewById(R.id.img_cancal).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(pop != null && pop.isShowing()){
					pop.dismiss();
				}
			}
		});
		TextView title = (TextView) view.findViewById(R.id.tx_title);
		TextView china = (TextView) view.findViewById(R.id.tx_china);
		TextView type = (TextView) view.findViewById(R.id.tx_type);
		TextView area = (TextView) view.findViewById(R.id.tx_area);
		TextView lang = (TextView) view.findViewById(R.id.tx_lang);
		title.setText(subscribe.get("name").toString());
		china.setText(subscribe.get("abroad").toString());
		type.setText(subscribe.get("type").toString());
		area.setText(subscribe.get("region").toString());
		lang.setText(subscribe.get("language").toString());
		pop.showAtLocation(findViewById(R.id.lin_layout), Gravity.CENTER, 0, 0);
	}
	/**
	 * 获取信息
	 *  type 表示类别
	 */
	public void getDate(final String string,final int count,final boolean back) {
		dialog.setMessage("数据加载中...");
		dialog.show();
		new AsyncTask<Void, Void, String>() {
			
			@Override
			protected String doInBackground(Void... arg0) {
				String retSrc = null;
				try {
						
					JSONObject jsonObject = new JSONObject();
					if(type != null && (type.equals("topic") || type.equals("category"))){
//						jsonObject.put("groupId", MyApplication.user.getGroupid());
						jsonObject.put("reportType", type);
						jsonObject.put("keyWord", string);
						jsonObject.put("pageBegin", count+"");
						jsonObject.put("pageSize", pagesize+"");
						retSrc = HttpUtil.getPostJsonWithUrl(Contact.url+"Report/ReportList",jsonObject.toString());
					}else{
//						jsonObject.put("groupId", MyApplication.user.getGroupid());
						jsonObject.put("keyWord", string);
						jsonObject.put("tagId", tagId);
						jsonObject.put("userId",  MyApplication.user.getId());
						jsonObject.put("pageBegin", count+"");
						jsonObject.put("pageSize", pagesize+"");
						retSrc = HttpUtil.getPostJsonWithUrl(Contact.url+"Opinion/SubscribeList",jsonObject.toString());
					}
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
						JSONArray data = (JSONArray) object.get("data");
						if(type != null && (type.equals("topic") || type.equals("category"))){
							temp_list = new ArrayList<HashMap<String, Object>>();
							for (int i = 0; i < data.length(); i++) {
								HashMap<String,Object> report = new HashMap<String,Object>();
								JSONObject object2 = (JSONObject) data.get(i);
								report.put("name", object2.getString("name"));
								report.put("time", object2.getString("createTime"));
								report.put("id", object2.getString("id"));
								temp_list.add(report);
							}
							adapter.notifyDataSetChanged();
						}else{
							temp_list = new ArrayList<HashMap<String, Object>>();
							for (int i = 0; i < data.length(); i++) {
								HashMap<String,Object> report = new HashMap<String,Object>();
								JSONObject object2 = (JSONObject) data.get(i);
								report.put("name", object2.getString("name"));
								report.put("abroad",object2.getString("abroad"));
								report.put("check", object2.getInt("sub"));
								report.put("language", object2.getString("language"));
								report.put("region",object2.getString("region"));
								report.put("type",object2.getString("orientation"));
								report.put("id",object2.getString("id"));
//								subscribe.setHometagId(object.getInt("hometagId"));
								temp_list.add(report);
							}
							adapter.notifyDataSetChanged();
						}
						if(string != null && string.length() > 0 && data.length() < 1){
//							handler.sendEmptyMessage(2444);
							NewToast.makeText(SeachDingYueActivity.this, "暂无数据", 1).show();
						}
						if(back){
							handler.sendEmptyMessage(2328);
						}else{
							if(list == null){
								list = new ArrayList<HashMap<String, Object>>();
							}else{
								list.clear();
							}
							list.addAll(temp_list);
							adapter.notifyDataSetChanged();
						}
					}else if(code == 2){
						String msg = object.getString("msg");
						NewToast.makeText(SeachDingYueActivity.this,msg, 0).show();
					}else{
						NewToast.makeText(SeachDingYueActivity.this,"系统异常", 0).show();
					}
					if(dialog != null && dialog.isShowing()){
						dialog.dismiss();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					if(dialog != null && dialog.isShowing()){
						dialog.dismiss();
					}
					NewToast.makeText(SeachDingYueActivity.this,"数据获取失败", 0).show();
				}
			}
			
		}.execute();
	}
	/**
	 * 单个订阅
	 */
	public void addDingYue(final Integer id,final TextView text,final Integer position) {
		dialog.setMessage("订阅添加中...");
		dialog.show();
		new AsyncTask<Void, Void, String>() {
			
			@Override
			protected String doInBackground(Void... arg0) {
				String retSrc = null;
				try {
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("userId", MyApplication.user.getId());
					jsonObject.put("hometagId", tagId);
					jsonObject.put("groupArticleId", id);
					retSrc = HttpUtil.getPostJsonWithUrl(Contact.url+"Opinion/SaveOrUpdateSubscribe",jsonObject.toString());
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
						String msg = object.getString("msg");
						NewToast.makeText(SeachDingYueActivity.this,"订阅成功", 0).show();
						text.setBackgroundResource(R.drawable.subscribe_btn_pre);
						text.setTextColor(getResources().getColor(R.color.text_hui1));
						text.setText("已订");
						text.setTag("false");
						list.get(position).put("check", "1");
					}else if(code == 2){
						String msg = object.getString("msg");
						NewToast.makeText(SeachDingYueActivity.this,msg, 0).show();
					}else{
						NewToast.makeText(SeachDingYueActivity.this,"系统异常", 0).show();
					}
					if(dialog != null && dialog.isShowing()){
						dialog.dismiss();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					if(dialog != null && dialog.isShowing()){
						dialog.dismiss();
					}
					NewToast.makeText(SeachDingYueActivity.this,"订阅失败", 0).show();
				}
			}
			
		}.execute();
	}
	/**
	 * 单个取消订阅
	 */
	public void DelectDingYue(final Integer id,final TextView text,final Integer position) {
		dialog.setMessage("订阅取消中...");
		dialog.show();
		new AsyncTask<Void, Void, String>() {
			
			@Override
			protected String doInBackground(Void... arg0) {
				String retSrc = null;
				try {
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("userId", MyApplication.user.getId());
					jsonObject.put("hometagId", tagId);
					jsonObject.put("groupArticleId", id);
					retSrc = HttpUtil.getPostJsonWithUrl(Contact.url+"Opinion/DelSubscribeList?",jsonObject.toString());
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
						String msg = object.getString("msg");
						NewToast.makeText(SeachDingYueActivity.this,"取消订阅成功", 0).show();
						text.setBackgroundResource(R.drawable.subscribe_btn_nor);
						text.setTextColor(getResources().getColor(R.color.text_red));
						text.setText("订阅");
						text.setTag("true");
						list.get(position).put("check", "0");
					}else if(code == 2){
						String msg = object.getString("msg");
						NewToast.makeText(SeachDingYueActivity.this,msg, 0).show();
					}else{
						NewToast.makeText(SeachDingYueActivity.this,"系统异常", 0).show();
					}
					if(dialog != null && dialog.isShowing()){
						dialog.dismiss();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					if(dialog != null && dialog.isShowing()){
						dialog.dismiss();
					}
					NewToast.makeText(SeachDingYueActivity.this,"取消订阅失败", 0).show();
				}
			}
			
		}.execute();
	}

}
