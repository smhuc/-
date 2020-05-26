package com.wangtian.message;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wangtian.message.base.BaseMenuActivity;
import com.wangtian.message.bean.Subscribe;
import com.wangtian.message.bean.Tag;
import com.wangtian.message.util.Contact;
import com.wangtian.message.util.HttpUtil;
import com.wangtian.message.util.NewToast;
/**
 * 添加订阅
 * @author chen
 *
 */
public class AddDingYueActivity extends BaseMenuActivity implements OnClickListener{

	private MyAdatper cadapter;
	private ArrayList<Subscribe> list;
	private PopupWindow pop;
	private ProgressDialog dialog;
	private ListView type,contact;
	private ArrayList<Tag> list1;
	private Integer tagId;
	private Integer old_tagId;
	private MyTypeAdapter typeAdapter;
	private boolean can = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add);
		setActivity(AddDingYueActivity.this, "订阅");
		left(1);
		right(1).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(AddDingYueActivity.this,SeachDingYueActivity.class);
				intent.putExtra("tagId", tagId);
//				startActivity(intent);
				startActivityForResult(intent, 1000);
			}
		});
		init();
	}

	private void init() {
		dialog = new ProgressDialog(AddDingYueActivity.this);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);
		findViewById(R.id.tx_all_check).setOnClickListener(this);
		findViewById(R.id.tx_all_checked).setOnClickListener(this);
		type = (ListView) findViewById(R.id.list_type);
		getAll();
		contact = (ListView) findViewById(R.id.list_contact);
	}

	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == 1000 &&  resultCode == 1000){
			tagId = data.getIntExtra("tagId",-1);
			can = true;
			getDateByTag(tagId);
			typeAdapter.notifyDataSetChanged();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.tx_all_check:
			DelectAll();
			break;
		case R.id.tx_all_checked:
			AddAll();
			break;

		default:
			break;
		}
		super.onClick(v);
	
	}
	class MyTypeAdapter extends BaseAdapter{

		private ArrayList<Tag> lists;
		private boolean frist = true; 
		private View oldview;
		
		public MyTypeAdapter(ArrayList<Tag> list) {
			super();
			this.lists = list;
		}

		@Override
		public int getCount() {
			if(lists == null){
				lists = new ArrayList<Tag>();
			}
			return lists.size();
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
			convertView = getLayoutInflater().inflate(R.layout.list_type_item, null);
			TextView type = (TextView) convertView.findViewById(R.id.tx_type);
			convertView.setTag(R.id.tx_type,type);
			convertView.setTag(R.layout.list_type_item,position);
			type.setTextColor(getResources().getColor(R.color.text_hui));
			type.setText(lists.get(position).getName());
			convertView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					if((Integer)oldview.getTag(R.layout.list_type_item) !=(Integer)v.getTag(R.layout.list_type_item)){
						TextView type = (TextView) v.getTag(R.id.tx_type);
						v.setBackgroundResource(R.drawable.subscribe_bg_column_up);
						type.setTextColor(getResources().getColor(R.color.text_red));
						v.setTag(R.id.tx_type,type);
						v.setTag(R.layout.list_type_item,position);
						if(oldview != null){
							TextView type1 = (TextView) oldview.getTag(R.id.tx_type);
							oldview.setBackgroundResource(R.drawable.subscribe_bg_column_under);
							type1.setTextColor(getResources().getColor(R.color.text_hui));
						}
						oldview = v;
						tagId = null;
						can = false;
						getDateByTag(lists.get(position).getId());
					}
				}
			});
			if(can && tagId != null && tagId == lists.get(position).getId()){
				convertView.setBackgroundResource(R.drawable.subscribe_bg_column_up);
				type.setTextColor(getResources().getColor(R.color.text_red));
				if(oldview != null){
					oldview.setBackgroundResource(R.drawable.subscribe_bg_column_under);
					TextView type1 = (TextView) oldview.getTag(R.id.tx_type);
					type1.setTextColor(getResources().getColor(R.color.text_hui));
				}
				oldview = convertView;
			}
			if(frist && position == 0){
				convertView.setBackgroundResource(R.drawable.subscribe_bg_column_up);
				type.setTextColor(getResources().getColor(R.color.text_red));
				convertView.setTag(R.id.tx_type,type);
				convertView.setTag(R.layout.list_type_item,position);
				oldview = convertView;
			}
			if(oldview != null){
				int count = (Integer) oldview.getTag(R.layout.list_type_item);
				if(count == position){
					convertView.setBackgroundResource(R.drawable.subscribe_bg_column_up);
					type.setTextColor(getResources().getColor(R.color.text_red));
					convertView.setTag(R.id.tx_type,type);
					convertView.setTag(R.layout.list_type_item,position);
				}
			}
			if(position == 1){
				frist = false;
			}
			return convertView;
		}
		
	}
	/**
	 * 订阅的具体内容
	 * @author chen
	 *
	 */
	class MyAdatper extends BaseAdapter{

		@Override
		public int getCount() {
			if(list == null){
				list = new ArrayList<Subscribe>();
			}
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
			RelativeLayout rel;
			if(convertView == null){
				convertView = getLayoutInflater().inflate(R.layout.list_contact_item, null);
				name = (TextView) convertView.findViewById(R.id.tx_name);
				check = (TextView) convertView.findViewById(R.id.tx_check);
				rel = (RelativeLayout) convertView.findViewById(R.id.rel_check);
				convertView.setTag(R.id.tx_name,name);
				convertView.setTag(R.id.tx_check,check);
				convertView.setTag(R.id.rel_check,rel);
			}else{
				name = (TextView) convertView.getTag(R.id.tx_name);
				check = (TextView) convertView.getTag(R.id.tx_check);
				rel = (RelativeLayout) convertView.getTag(R.id.rel_check);
			}
			name.setText(list.get(position).getName());
			if(list.get(position).getSub() == 1){
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
			rel.setTag(check);
			rel.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					RelativeLayout rel = (RelativeLayout) v;
					TextView text = (TextView) rel.getTag();
					if(text.getTag().toString().equals("true")){
						addDingYue(list.get(position).getId(),text, position,rel);
					}else{
						DelectDingYue(list.get(position).getId(),text,position,rel);
					}
				}
			});
			convertView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					initPop(list.get(position));
				}
			});
			return convertView;
		}
	}
	private void initPop(Subscribe subscribe) {
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
		title.setText(subscribe.getName());
		china.setText(subscribe.getAbroad());
		type.setText(subscribe.getOrientation());
		area.setText(subscribe.getRegion());
		lang.setText(subscribe.getLanguage());
		pop.showAtLocation(findViewById(R.id.lin_layout), Gravity.CENTER, 0, 0);
	}
	/**
	 * 获取所有的栏目的方法
	 */
	public void getAll() {
		dialog.setMessage("数据加载中...");
		dialog.show();
		new AsyncTask<Void, Void, String>() {
			
			@Override
			protected String doInBackground(Void... arg0) {
				String retSrc = null;
				try {
					JSONObject jsonObject = new JSONObject();
//					jsonObject.put("groupId", MyApplication.user.getGroupid());
					retSrc = HttpUtil.getPostJsonWithUrl(Contact.url+"Opinion/TagList",jsonObject.toString());
					Log.e("retSrc", retSrc.toString());
				} catch (Exception e) {
					e.printStackTrace();
				}
				return retSrc;
			}

			@Override
			protected void onPostExecute(String result) {
				try {
					JSONObject json = new JSONObject(result);
					int code = json.getInt("code");
					if(code == 1){
						JSONArray data = json.getJSONArray("data");
						list1 = new ArrayList<Tag>();
						for (int i = 0; i < data.length(); i++) {
							Tag tag = new Tag();
							JSONObject object = (JSONObject) data.get(i);
							tag.setGroupid(object.getInt("groupid"));
							tag.setId(object.getInt("id"));
							tag.setName(object.getString("name"));
							list1.add(tag);
						}
						typeAdapter = new MyTypeAdapter(list1);
						type.setAdapter(typeAdapter);
						if(list1 != null && list1.size() > 0){
							getDateByTag(list1.get(0).getId());
						}
					}else if(code == 2){
						NewToast.makeText(AddDingYueActivity.this,"数据获取失败", 0).show();
					}else{
						NewToast.makeText(AddDingYueActivity.this,"系统异常", 0).show();
					}
//					if(dialog != null && dialog.isShowing()){
//						dialog.dismiss();
//					}
				} catch (JSONException e) {
					e.printStackTrace();
					if(dialog != null && dialog.isShowing()){
						dialog.dismiss();
					}
					NewToast.makeText(AddDingYueActivity.this,"栏目获取失败", 0).show();
				}
			}
			
		}.execute();
	}
	/**
	 * 获取所有的数据的方法
	 */
	public void getDateByTag(final int tagid) {
		dialog.setMessage("数据加载中...");
		dialog.show();
//		can = false;
		new AsyncTask<Void, Void, String>() {
			
			@Override
			protected String doInBackground(Void... arg0) {
				String retSrc = null;
				try {
					JSONObject jsonObject = new JSONObject();
//					jsonObject.put("groupId", MyApplication.user.getGroupid());
					jsonObject.put("keyWord", null);
					jsonObject.put("tagId", tagid+"");
					tagId = tagid;
					jsonObject.put("userId",  MyApplication.user.getId());
					retSrc = HttpUtil.getPostJsonWithUrl(Contact.url+"Opinion/SubscribeList",jsonObject.toString());
					Log.e("retSrc", retSrc.toString());
				} catch (Exception e) {
					e.printStackTrace();
				}
				return retSrc;
			}
			
			@Override
			protected void onPostExecute(String result) {
				try {
					JSONObject json = new JSONObject(result);
					int code = json.getInt("code");
					if(code == 1){
						JSONArray data = json.getJSONArray("data");
						list = new ArrayList<Subscribe>();
						for (int i = 0; i < data.length(); i++) {
							Subscribe subscribe = new Subscribe();
							JSONObject object = (JSONObject) data.get(i);
							subscribe.setAbroad(object.getString("abroad"));
							subscribe.setAddress(object.getString("address"));
							subscribe.setConcept(object.getString("concept"));
							subscribe.setGroupId(object.getInt("groupId"));
//							subscribe.setHometagId(object.getInt("hometagId"));
							subscribe.setId(object.getInt("id"));
							subscribe.setLanguage(object.getString("language"));
							subscribe.setLevel(object.getString("level"));
							subscribe.setName(object.getString("name"));
							subscribe.setOrientation(object.getString("orientation"));
							subscribe.setRegion(object.getString("region"));
							subscribe.setSub(object.getInt("sub"));
							subscribe.setType(object.getString("type"));
							list.add(subscribe);
						}
						cadapter = new MyAdatper();
						contact.setAdapter(cadapter);
						old_tagId = tagId;
//						can = true;
					}else if(code == 2){
						NewToast.makeText(AddDingYueActivity.this,"数据获取失败", 0).show();
					}else{
						NewToast.makeText(AddDingYueActivity.this,"系统异常", 0).show();
					}
					if(dialog != null && dialog.isShowing()){
						dialog.dismiss();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					if(dialog != null && dialog.isShowing()){
						dialog.dismiss();
					}
					NewToast.makeText(AddDingYueActivity.this,"数据获取失败", 0).show();
				}
			}
			
		}.execute();
	}
	/**
	 * 单个订阅
	 */
	public void addDingYue(final Integer id,final TextView text,final Integer position,final RelativeLayout rel) {
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
						NewToast.makeText(AddDingYueActivity.this,"订阅成功", 0).show();
						text.setBackgroundResource(R.drawable.subscribe_btn_pre);
						text.setTextColor(getResources().getColor(R.color.text_hui1));
						text.setText("已定");
						text.setTag("false");
						rel.setTag(text);
						list.get(position).setSub(1);
						Subscribe subscribe = list.get(position);
						list.remove(subscribe);
						list.add(0, subscribe);
						cadapter.notifyDataSetChanged();
					}else if(code == 2){
						String msg = object.getString("msg");
						NewToast.makeText(AddDingYueActivity.this,msg, 0).show();
					}else{
						NewToast.makeText(AddDingYueActivity.this,"系统异常", 0).show();
					}
					if(dialog != null && dialog.isShowing()){
						dialog.dismiss();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					if(dialog != null && dialog.isShowing()){
						dialog.dismiss();
					}
					NewToast.makeText(AddDingYueActivity.this,"订阅失败", 0).show();
				}
			}
			
		}.execute();
	}
	/**
	 * 单个取消订阅
	 */
	public void DelectDingYue(final Integer id,final TextView text,final Integer position,final RelativeLayout rel) {
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
					retSrc = HttpUtil.getPostJsonWithUrl(Contact.url+"Opinion/DelSubscribeList",jsonObject.toString());
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
						NewToast.makeText(AddDingYueActivity.this,"取消订阅成功", 0).show();
						text.setBackgroundResource(R.drawable.subscribe_btn_nor);
						text.setTextColor(getResources().getColor(R.color.text_red));
						text.setText("订阅");
						text.setTag("true");
						rel.setTag(text);
						list.get(position).setSub(2);
						Subscribe subscribe = list.get(position);
						list.remove(subscribe);
						list.add(subscribe);
						cadapter.notifyDataSetChanged();
					}else if(code == 2){
						String msg = object.getString("msg");
						NewToast.makeText(AddDingYueActivity.this,msg, 0).show();
					}else{
						NewToast.makeText(AddDingYueActivity.this,"系统异常", 0).show();
					}
					if(dialog != null && dialog.isShowing()){
						dialog.dismiss();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					if(dialog != null && dialog.isShowing()){
						dialog.dismiss();
					}
					NewToast.makeText(AddDingYueActivity.this,"取消订阅失败", 0).show();
				}
			}
			
		}.execute();
	}
	/**
	 * 订阅全部取消
	 */
	public void DelectAll() {
		dialog.setMessage("订阅取消中...");
		dialog.show();
		new AsyncTask<Void, Void, String>() {
			
			@Override
			protected String doInBackground(Void... arg0) {
				String retSrc = null;
				try {
					JSONObject jsonObject = new JSONObject();
//					jsonObject.put("groupId", MyApplication.user.getGroupid());
					jsonObject.put("hometagId", tagId);
					retSrc = HttpUtil.getPostJsonWithUrl(Contact.url+"Opinion/DelAllSubscribeList",jsonObject.toString());
					can = false;
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
						NewToast.makeText(AddDingYueActivity.this,"取消订阅成功", 0).show();
						for (int i = 0; i < list.size(); i++) {
							list.get(i).setSub(0);
						}
						cadapter.notifyDataSetChanged();
					}else if(code == 2){
						String msg = object.getString("msg");
						NewToast.makeText(AddDingYueActivity.this,msg, 0).show();
					}else{
						NewToast.makeText(AddDingYueActivity.this,"系统异常", 0).show();
					}
					if(dialog != null && dialog.isShowing()){
						dialog.dismiss();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					if(dialog != null && dialog.isShowing()){
						dialog.dismiss();
					}
					NewToast.makeText(AddDingYueActivity.this,"取消订阅失败", 0).show();
				}
			}
			
		}.execute();
	}
	/**
	 * 订阅全部
	 */
	public void AddAll() {
		dialog.setMessage("全部订阅中...");
		dialog.show();
		new AsyncTask<Void, Void, String>() {
			
			@Override
			protected String doInBackground(Void... arg0) {
				String retSrc = null;
				try {
					JSONObject jsonObject = new JSONObject();
//					jsonObject.put("groupId", MyApplication.user.getGroupid());
					jsonObject.put("hometagId", tagId);
					retSrc = HttpUtil.getPostJsonWithUrl(Contact.url+"Opinion/SaveAllSubscribe",jsonObject.toString());
					can = false;
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
						NewToast.makeText(AddDingYueActivity.this,"订阅成功", 0).show();
						for (int i = 0; i < list.size(); i++) {
							list.get(i).setSub(1);
						}
						cadapter.notifyDataSetChanged();
					}else if(code == 2){
						String msg = object.getString("msg");
						NewToast.makeText(AddDingYueActivity.this,msg, 0).show();
					}else{
						NewToast.makeText(AddDingYueActivity.this,"系统异常", 0).show();
					}
					if(dialog != null && dialog.isShowing()){
						dialog.dismiss();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					if(dialog != null && dialog.isShowing()){
						dialog.dismiss();
					}
					NewToast.makeText(AddDingYueActivity.this,"订阅失败", 0).show();
				}
			}
			
		}.execute();
	}
}
