package com.wangtian.message;

import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView.OnEditorActionListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wangtian.message.base.BaseMainMenuActivity;
import com.wangtian.message.bean.User;
import com.wangtian.message.bean.Warn;
import com.wangtian.message.util.Contact;
import com.wangtian.message.util.HttpUtil;
import com.wangtian.message.util.Network;
import com.wangtian.message.util.NewToast;
import com.wangtian.message.view.XListView;
import com.wangtian.message.view.XListView.IXListViewListener;
/**
 * 舆情预警
 * @author chen
 */
public class WarnMainActivity extends BaseMainMenuActivity implements IXListViewListener{

	private TextView info1,info2,num;
	private View view1,view2;
	private LinearLayout seach;
	private MyAdapter adapter;
	private XListView listview ;
	private ArrayList<Warn> list;
	private ArrayList<Warn> all_list;//查询之前的
	private ArrayList<Warn> temp_list;
	private ProgressDialog dialog;
	private int count = 1;//用于表示下一页
	private String state;
	private boolean downup;
	private String key;
	private EditText edit;
	private int pagesize = 20;
	private com.wangtian.message.adapter.MyAdapter myadapter;
	private ArrayList<HashMap<String,Object>> new_list;
	private ArrayList<HashMap<String,Object>> temp_new_list;
	private boolean ref = true;
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			downup = false;
			if (key != null && key.length() > 0 && state.equals("")) {
				// 结束
				listview.mScrollBack = 0;
				listview.mScroller.startScroll(0, 100, 0, 0 - 100, 400);
			} //
			switch (msg.what) {
			case 2328:
				ref = true;
				if (state.equals("up")) { // 上拉
						if (temp_list != null && temp_list.size() > 0) {
							list.addAll(temp_list);
							count += 1;
							adapter.notifyDataSetChanged();
						} else if (temp_new_list != null && temp_new_list.size() > 0) {
							new_list.addAll(temp_new_list);
							count += 1;
							myadapter.notifyDataSetChanged();
						} else {
							NewToast.makeText(WarnMainActivity.this, "暂无更多数据", 0)
							.show();
						}
				} else {
					if (temp_list != null && temp_list.size() > 0) {
						list.clear();
						count = 1;
						list.addAll(temp_list);
						adapter.notifyDataSetChanged();
					}else if (temp_new_list != null && temp_new_list.size() > 0) {
						new_list.clear();
						count = 1;
						new_list.addAll(temp_new_list);
						myadapter.notifyDataSetChanged();
					}
				}

				break;
			case 2222:
				NewToast.makeText(WarnMainActivity.this, "请检查网络连接", 1).show();
				break;
			case 555:
				NewToast.makeText(WarnMainActivity.this, "请检查网络连接", 1).show();
				break;
			case 2333:
				NewToast.makeText(WarnMainActivity.this, "暂无更多数据", 1).show();
			case 2444:
				NewToast.makeText(WarnMainActivity.this, "服务器暂无数据", 1).show();
				break;
			default:
				break;
			}
			listview.stopRefresh();
			listview.stopLoadMore();
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_warnmain);
		setActivity(WarnMainActivity.this,"舆情预警");
		left(2);
		init();
	}

	private void init() {
		dialog = new ProgressDialog(WarnMainActivity.this);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);
		seach = (LinearLayout) findViewById(R.id.lin_seach);
		findViewById(R.id.lin_info1).setOnClickListener(this);
		info1 = (TextView) findViewById(R.id.tx_info1);
		view1 = findViewById(R.id.view_info1);
		findViewById(R.id.lin_info2).setOnClickListener(this);
		info2 = (TextView) findViewById(R.id.tx_info2);
		view2 = findViewById(R.id.view_info2);
		num = (TextView) findViewById(R.id.tx_num);
		listview = (XListView) findViewById(R.id.list);
		listview.setPullLoadEnable(true);
		listview.setPullRefreshEnable(true);
		listview.setXListViewListener(this);
		getDate("",0+"", false);
		getNum();
		edit = (EditText) findViewById(R.id.ed_seach);
		edit.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if(event != null && event.getKeyCode() ==  KeyEvent.KEYCODE_ENTER){
					if(key != null && key.equals(v.getText().toString().trim())){
					}else{
						key =  v.getText().toString().trim();
						if(all_list == null){
							all_list = new ArrayList<Warn>();
						}
						all_list.clear();
						all_list.addAll(list);
						getDate(key,0+"",false);
					}
					return true;
				}else if(event == null && actionId == 3){
					if(key != null && key.equals(v.getText().toString().trim())){
					}else{
						key =  v.getText().toString().trim();
						if(all_list == null){
							all_list = new ArrayList<Warn>();
						}
						all_list.clear();
						all_list.addAll(list);
						getDate(key,0+"",false);
					}
					return true;
				}
				return false;
			}
		});
		findViewById(R.id.tx_cancal).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				edit.setText("");
				if(all_list !=  null){
					list.clear();
					list.addAll(all_list);
				}
				key = null;
				adapter.notifyDataSetChanged();
			}
		});
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent;
				if(view1.isShown()){
					intent = new Intent(WarnMainActivity.this,AreaWarnActivity.class);
					intent.putExtra("id", list.get(position-1).getId());
					intent.putExtra("name", list.get(position-1).getName());
					intent.putExtra("warnid", list.get(position-1).getWarnid());
//					intent.putExtra("key", list.get(position-1).getKeyword());
//					intent.putExtra("guize", list.get(position-1).getRule());
				}else{
					intent = new Intent(WarnMainActivity.this,WarnXiangQingActivity.class);
					intent.putExtra("articleid", new_list.get(position-1).get("articleid").toString());
					intent.putExtra("type", new_list.get(position-1).get("type").toString());
				}
				startActivity(intent);
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.lin_info1:
			temp_new_list = null;
			info1.setTextColor(getResources().getColor(R.color.text_red));
			view1.setVisibility(View.VISIBLE);
			info2.setTextColor(getResources().getColor(R.color.text_hui));
			view2.setVisibility(View.GONE);
			seach.setVisibility(View.VISIBLE);
			key = "";
			getDate("",0+"", false);
			break;
		case R.id.lin_info2:
			if(list != null){
				list.clear();
			}
			temp_list = null;
			num.setVisibility(View.GONE);
			info2.setTextColor(getResources().getColor(R.color.text_red));
			view2.setVisibility(View.VISIBLE);
			info1.setTextColor(getResources().getColor(R.color.text_hui));
			view1.setVisibility(View.GONE);
			seach.setVisibility(View.GONE);
			getNewDate("0", false);
//			myadapter = new com.wangtian.message.adapter.MyAdapter(new_list,WarnMainActivity.this);
//			listview.setAdapter(myadapter);
			break; 

		default:
			break;
		}
		super.onClick(v);
	}
	class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			if(list == null){
				list = new ArrayList<Warn>();
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
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView title,area,key,num;
			if(convertView == null){
				convertView = getLayoutInflater().inflate(R.layout.wran_all_item, null);
				title = (TextView) convertView.findViewById(R.id.tx_title);
				area = (TextView) convertView.findViewById(R.id.tx_area);
				key = (TextView) convertView.findViewById(R.id.tx_key);
				num = (TextView) convertView.findViewById(R.id.tx_num);
				convertView.setTag(R.id.tx_title,title);
				convertView.setTag(R.id.tx_area,area);
				convertView.setTag(R.id.tx_key,key);
				convertView.setTag(R.id.tx_num,num);
			}else{
				title = (TextView) convertView.getTag(R.id.tx_title);
				area = (TextView) convertView.getTag(R.id.tx_area);
				key = (TextView) convertView.getTag(R.id.tx_key);
				num = (TextView) convertView.getTag(R.id.tx_num);
			}
			if(position != 0){
				convertView.findViewById(R.id.view).setVisibility(View.GONE);
			}
			title.setText(list.get(position).getName());
			area.setText(list.get(position).getRule().toString());
			key.setText(list.get(position).getKeyword().toString());
			int number = Integer.parseInt(list.get(position).getArticlecount().toString());
			if(number < 100){
				num.setText(number+"");
			}else{
				num.setText("99+");
			}
			return convertView;
		}
		
	}
	@Override
	public void onRefresh() {
		handler.postDelayed(new Runnable() {
			public void run() {
				if (downup == false) {
					if (Network.checkNetWork(WarnMainActivity.this)) {
						// initializeData();
						state = "down";
						downup = true;
						if(view1.isShown()){
							key = "";
							getDate("","0",true);
						}else{
							getNewDate("0", true);
						}
					} else {
						handler.sendEmptyMessage(555);
					}
				}
			}
		}, 2000);
	}

	@Override
	public void onLoadMore() {
		if(ref){
		handler.postDelayed(new Runnable() {
			public void run() {
				if (downup == false) {
					if (Network.checkNetWork(WarnMainActivity.this)) {
						// initializeData();
						state = "up";
						downup = true;
						if(view1.isShown()){
							key = "";
							getDate("",count*pagesize+"",true);
						}else{
							getNewDate(count*pagesize+"", true);
						}
					} else {
						handler.sendEmptyMessage(555);
					}
				}
			}
		}, 2000);
		}
	}
	/**
	 * 获取信息
	 */
	public void getDate(final String string,final String pageBegin,final boolean back) {
		if(dialog == null){
			dialog = new ProgressDialog(WarnMainActivity.this);
		}
		dialog.setMessage("数据加载中...");
		dialog.show();
		if(MyApplication.user == null){
			if(MyApplication.user == null ){
				SharedPreferences sp = getSharedPreferences("user", Activity.MODE_PRIVATE);
				MyApplication.user = new User();
//				MyApplication.user.setGroupid(sp.getInt("groupid",-1));
				MyApplication.user.setId(sp.getString("id",""));
				MyApplication.user.setIsdel(sp.getString("isdel",null));
				MyApplication.user.setName(sp.getString("name",null));
				MyApplication.user.setPassword(sp.getString("password",null));
				MyApplication.user.setRemark(sp.getString("remark",null));
			}
		}
		new AsyncTask<Void, Void, String>() {
			
			@Override
			protected String doInBackground(Void... arg0) {
				String retSrc = null;
				ref = false;
				try {
					JSONObject jsonObject = new JSONObject();
//					jsonObject.put("groupId", MyApplication.user.getGroupid());
					jsonObject.put("pageBegin", pageBegin+"");
					jsonObject.put("keyWord", string);
					jsonObject.put("pageSize ", pagesize+"");
					retSrc = HttpUtil.getPostJsonWithUrl(Contact.url+"Warn/WarnList",jsonObject.toString());
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
					temp_list = new ArrayList<Warn>();
					if(code == 1){
							JSONArray data = (JSONArray) object.get("data");
							for (int i = 0; i < data.length(); i++) {
								Warn warn = new Warn();
								JSONObject object2 = (JSONObject) data.get(i);
								warn.setEnable(object2.getInt("enable"));
								warn.setWarnid(object2.getString("warnid"));
								warn.setDesc(object2.getString("desc"));
								warn.setFirstStartTime(object2.getString("firstStartTime"));
								warn.setGroupid(object2.getInt("groupid"));
								warn.setId(object2.getInt("id"));
								warn.setName(object2.getString("name"));
								warn.setRule(object2.getString("rule"));
								warn.setKeyword(object2.getString("keyword"));
								warn.setArticlecount(object2.getString("articlecount"));
								warn.setRemove(object2.getInt("remove"));
								warn.setStartDate(object2.getString("startDate"));
								temp_list.add(warn);
							}
							if(string != null && string.length() > 0 && data.length() < 1){
//								handler.sendEmptyMessage(2444);
								NewToast.makeText(WarnMainActivity.this, "暂无数据", 1).show();
							}
						if(back){
							handler.sendEmptyMessage(2328);
						}else{
							ref = true;
							if(list == null){
								list = new ArrayList<Warn>();
							}
							list.clear();
							list.addAll(temp_list);
							if(adapter == null){
								adapter = new MyAdapter();
							}
							listview.setAdapter(adapter);
						}
					}else if(code == 2){
						ref = true;
						String msg = object.getString("msg");
						NewToast.makeText(WarnMainActivity.this,msg, 0).show();
					}else{
						ref = true;
						NewToast.makeText(WarnMainActivity.this,"系统异常", 0).show();
					}
					if(dialog != null && dialog.isShowing()){
						dialog.dismiss();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					ref = true;
					if(dialog != null && dialog.isShowing()){
						dialog.dismiss();
					}
					NewToast.makeText(WarnMainActivity.this,"数据获取失败", 0).show();
				}
			}
			
		}.execute();
	}
	/**
	 * 获取最新信息
	 */
	public void getNewDate(final String pageBegin,final boolean back) {
		dialog.setMessage("数据加载中...");
		dialog.show();
		new AsyncTask<Void, Void, String>() {
			
			@Override
			protected String doInBackground(Void... arg0) {
				String retSrc = null;
				ref = false;
				try {
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("userId", MyApplication.user.getId());
					jsonObject.put("pageBegin", pageBegin+"");
					jsonObject.put("pageSize", pagesize+"");
					retSrc = HttpUtil.getPostJsonWithUrl(Contact.url+"Warn/NewWarnNewList",jsonObject.toString());
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
					temp_new_list = new ArrayList<HashMap<String,Object>>();
					if(code == 1){
						JSONArray data = (JSONArray) object.get("data");
						for (int i = 0; i < data.length(); i++) {
							JSONObject object2 = (JSONObject) data.get(i);
							HashMap<String,Object> map = new HashMap<String, Object>();
							map.put("title", object2.getString("article_title"));
//							map.put("time", object2.getString("article_publish_date"));
							String[] strs = object2.getString("warnid").split(",");
							StringBuffer buffer = new StringBuffer();
							for (int j = 0; j < strs.length; j++) {
								buffer.append(strs[j]+"      ");
							}
							map.put("time", buffer.toString());
//							map.put("pinlun",object2.getString("gourp_articleid"));
							String str = object2.getString("article_publish_date");
							String frist = str.substring(0,5-1);
							String two = str.substring(4, 7-1);
							String three = str.substring(6,str.length());
							map.put("pinlun",frist+"-"+two+"-"+three);
							map.put("laiyuan", object2.getString("article_origin"));
							map.put("type", object2.getString("type"));
							map.put("articleid", object2.getString("articleid"));
							map.put("id", object2.getString("id"));
							temp_new_list.add(map);
						}
					}else if(code == 2){
						String msg = object.getString("msg");
						NewToast.makeText(WarnMainActivity.this,msg, 0).show();
						ref = true;
					}else{
						NewToast.makeText(WarnMainActivity.this,"系统异常", 0).show();
						ref = true;
					}
					
					if(back){
						handler.sendEmptyMessage(2328);
					}else{
						ref = true;
						if(new_list == null){
							new_list = new ArrayList<HashMap<String,Object>>();
						}
						new_list.clear();
						new_list.addAll(temp_new_list);
						if(myadapter == null){
							myadapter = new com.wangtian.message.adapter.MyAdapter(new_list,WarnMainActivity.this);
						}
						listview.setAdapter(myadapter);
						myadapter.notifyDataSetChanged();
					}
					if(dialog != null && dialog.isShowing()){
						dialog.dismiss();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					ref = true;
					if(dialog != null && dialog.isShowing()){
						dialog.dismiss();
					}
					NewToast.makeText(WarnMainActivity.this,"数据获取失败", 0).show();
				}
			}
			
		}.execute();
	}
	/**
	 * 获取数量
	 */
	public void getNum() {
		dialog.setMessage("数据加载中...");
		dialog.show();
		new AsyncTask<Void, Void, String>() {
			
			@Override
			protected String doInBackground(Void... arg0) {
				String retSrc = null;
				try {
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("userId", MyApplication.user.getId());
					retSrc = HttpUtil.getPostJsonWithUrl(Contact.url+"Warn/NewWarnCount",jsonObject.toString());
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
					temp_list = new ArrayList<Warn>();
					if(code == 1){
						int data = object.getInt("data");
						if(data > 0){
							android.widget.FrameLayout.LayoutParams layoutParams = new android.widget.FrameLayout.LayoutParams(dip2px(16), dip2px(16),Gravity.CENTER);
							layoutParams.leftMargin = dip2px(32);
							layoutParams.bottomMargin = dip2px(8);
							num.setLayoutParams(layoutParams);
							num.setVisibility(View.VISIBLE);
							num.setText(data+"");
						}else if(data > 10 && data < 100){
							android.widget.FrameLayout.LayoutParams layoutParams = new android.widget.FrameLayout.LayoutParams(dip2px(18), dip2px(18),Gravity.CENTER);
							layoutParams.leftMargin = dip2px(32);
							layoutParams.bottomMargin = dip2px(8);
							num.setLayoutParams(layoutParams);
							num.setVisibility(View.VISIBLE);
							num.setText(data+"");
						}else if(data > 99){
							num.setVisibility(View.VISIBLE);
							android.widget.FrameLayout.LayoutParams layoutParams = new android.widget.FrameLayout.LayoutParams(dip2px(20), dip2px(20),Gravity.CENTER);
							layoutParams.leftMargin = dip2px(32);
							layoutParams.bottomMargin = dip2px(8);
							num.setLayoutParams(layoutParams);
							num.setText("99+");
						}else{
							num.setVisibility(View.GONE);
						}
//						num.setVisibility(View.VISIBLE);
//						android.widget.FrameLayout.LayoutParams layoutParams = new android.widget.FrameLayout.LayoutParams(dip2px(20), dip2px(20),Gravity.CENTER);
//						layoutParams.leftMargin = dip2px(32);
//						layoutParams.bottomMargin = dip2px(8);
//						num.setLayoutParams(layoutParams);
//						num.setText("99+");
					}else if(code == 2){
						String msg = object.getString("msg");
						NewToast.makeText(WarnMainActivity.this,msg, 0).show();
					}else{
						NewToast.makeText(WarnMainActivity.this,"系统异常", 0).show();
					}
					if(dialog != null && dialog.isShowing()){
						dialog.dismiss();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					if(dialog != null && dialog.isShowing()){
						dialog.dismiss();
					}
					NewToast.makeText(WarnMainActivity.this,"数据获取失败", 0).show();
				}
			}
			
		}.execute();
	}
	public int dip2px(float dipValue){ 
        final float scale = getResources().getDisplayMetrics().density; 
        return (int)(dipValue * scale + 0.5f); 
	} 
	
	public int px2dip(float pxValue){ 
	      final float scale = getResources().getDisplayMetrics().density; 
	      return (int)(pxValue / scale + 0.5f); 
	}
}
