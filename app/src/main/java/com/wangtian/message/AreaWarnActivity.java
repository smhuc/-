package com.wangtian.message;

import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import android.widget.AdapterView.OnItemClickListener;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.wangtian.message.adapter.MyAdapter;
import com.wangtian.message.base.BaseMenuActivity;
import com.wangtian.message.util.Contact;
import com.wangtian.message.util.HttpUtil;
import com.wangtian.message.util.Network;
import com.wangtian.message.util.NewToast;
import com.wangtian.message.view.EXListView;
import com.wangtian.message.view.EXListView.IXListViewListener;
/**
 * 预警里面的地区动态列表
 * @author chen
 */
public class AreaWarnActivity extends BaseMenuActivity {

	private EXListView listView;
	private MyAdapter adapter;
	private ArrayList<HashMap<String,Object>> list;
	private ArrayList<HashMap<String,Object>> temp_list;
	private int id;
	private ProgressDialog dialog;
	private int count = 1;//用于表示下一页
	private String state,warnid;
	private boolean downup;
//	private String keyword;
//	private String guize;
	private int pageSize = 20;//每一页的数量
	private boolean ref = true;
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			downup = false;
			if (state.equals("")) {
				// 结束
				listView.mScrollBack = 0;
				listView.mScroller.startScroll(0, 100, 0, 0 - 100, 400);
			} //
			switch (msg.what) {
			case 2328:
				ref = true;
				if (state.equals("up")) { // 上拉
					if (temp_list.size() > 0) {
						list.addAll(temp_list);
						count += 1;
						adapter.notifyDataSetChanged();
					} else {
						NewToast.makeText(AreaWarnActivity.this, "暂无更多数据", 0)
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
				NewToast.makeText(AreaWarnActivity.this, "请检查网络连接", 1).show();
				break;
			case 555:
				NewToast.makeText(AreaWarnActivity.this, "请检查网络连接", 1).show();
				break;
			case 2333:
				NewToast.makeText(AreaWarnActivity.this, "暂无更多数据", 1).show();
			case 2444:
				NewToast.makeText(AreaWarnActivity.this, "服务器暂无数据", 1).show();
				break;
			default:
				break;
			}
			listView.stopRefresh();
			listView.stopLoadMore();
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_area);
		setActivity(AreaWarnActivity.this,getIntent().getStringExtra("name"));
		left(1);
		id = getIntent().getIntExtra("id", -1);
		warnid = getIntent().getStringExtra("warnid");
//		keyword = getIntent().getStringExtra("key");
//		guize = getIntent().getStringExtra("guize");
		dialog = new ProgressDialog(AreaWarnActivity.this);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);
		getDate(0+"",false);
		listView = (EXListView) findViewById(R.id.list);
		listView.setPullRefreshEnable(true);
		listView.setPullLoadEnable(true);
		listView.setXListViewListener(new IXListViewListener() {
			
			@Override
			public void onRefresh() {
				handler.postDelayed(new Runnable() {
					public void run() {
						if (downup == false) {
							if (Network.checkNetWork(AreaWarnActivity.this)) {
								// initializeData();
								state = "down";
								downup = true;
								getDate("0",true);
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
							if (Network.checkNetWork(AreaWarnActivity.this)) {
								state = "up";
								downup = true;
								getDate(pageSize*count+0+"",true);
							} else {
								handler.sendEmptyMessage(555);
							}
						}
					}
				}, 2000);
				}
				
			}
		});
		getAdapter();
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(AreaWarnActivity.this,WarnXiangQingActivity.class);
				intent.putExtra("articleid", list.get(position-1).get("articleid").toString());
				intent.putExtra("type", list.get(position-1).get("type").toString());
				startActivity(intent);
			}
		});
	}

	public void getAdapter() {
		SwipeMenuCreator creator = new SwipeMenuCreator() {

			@Override
			public void create(SwipeMenu menu) {
				// create "delete" item
				SwipeMenuItem deleteItem = new SwipeMenuItem(
						getApplicationContext());
				deleteItem.setBackground(new ColorDrawable(Color.rgb(255, 33, 0)));
				deleteItem.setWidth(160);
				deleteItem.setTitle("删除");
				deleteItem.setTitleSize(18);
				deleteItem.setTitleColor(getResources().getColor(R.color.white));
				menu.addMenuItem(deleteItem);
			}
		};
		listView.setMenuCreator(creator);
		listView.setOnMenuItemClickListener(new OnMenuItemClickListener() {
					@Override
					public boolean onMenuItemClick(int position,
							SwipeMenu menu, int index) {
						delect(list.get(position).get("id").toString(),position);
						return false;
					}
				});
	}
	/**
	 * 获取信息
	 */
	public void getDate(final String pageBegin,final boolean back) {
		dialog.setMessage("数据加载中...");
		dialog.show();
		new AsyncTask<Void, Void, String>() {
			
			@Override
			protected String doInBackground(Void... arg0) {
				String retSrc = null;
				ref = false;
				try {
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("topicid", id);
					jsonObject.put("pageBegin", pageBegin+"");
					jsonObject.put("pageSize", pageSize+"");
					jsonObject.put("warnid", warnid);
					retSrc = HttpUtil.getPostJsonWithUrl(Contact.url+"Warn/WarnNewsList",jsonObject.toString());
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
							temp_list = new ArrayList<HashMap<String,Object>>();
							for (int i = 0; i < data.length(); i++) {
								JSONObject object2 = (JSONObject) data.get(i);
								HashMap<String,Object> map = new HashMap<String, Object>();
								map.put("title", object2.getString("article_title"));
//								map.put("time", object2.getString("article_publish_date"));
								String[] strs = object2.getString("warnid").split(",");
								StringBuffer buffer = new StringBuffer();
								for (int j = 0; j < strs.length; j++) {
									buffer.append(strs[j]+"      ");
								}
								map.put("time", buffer.toString());
//								map.put("time", guize +"    "+keyword);
//								map.put("pinlun",object2.getString("gourp_articleid"));
								String str = object2.getString("article_publish_date");
								String frist = str.substring(0,4);
								String two = str.substring(4, 6);
								String three = str.substring(6,str.length());
								map.put("pinlun",frist+"年"+two+"月"+three+"日");
								map.put("laiyuan", object2.getString("article_origin"));
								map.put("type", object2.getString("type"));
								map.put("articleid", object2.getString("articleid"));
								map.put("id", object2.getString("id"));
								temp_list.add(map);
							}
					}else if(code == 2){
						ref = true;
						String msg = object.getString("msg");
						NewToast.makeText(AreaWarnActivity.this,msg, 0).show();
					}else{
						ref = true;
						NewToast.makeText(AreaWarnActivity.this,"系统异常", 0).show();
					}
					
					if(back){
						handler.sendEmptyMessage(2328);
					}else{
						ref = true;
						if(list == null){
							list = new ArrayList<HashMap<String,Object>>();
						}
						list.clear();
						list.addAll(temp_list);
						adapter = new MyAdapter(list, AreaWarnActivity.this);
						listView.setAdapter(adapter);
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
					NewToast.makeText(AreaWarnActivity.this,"数据获取失败", 0).show();
				}
			}
		}.execute();
	}
	/**
	 * 删除预警
	 */
	public void delect(final String id,final int position) {
		dialog.setMessage("预警删除中...");
		dialog.show();
		new AsyncTask<Void, Void, String>() {
			
			@Override
			protected String doInBackground(Void... arg0) {
				String retSrc = null;
				try {
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("id", id);
					retSrc = HttpUtil.getPostJsonWithUrl(Contact.url+"Warn/WarnNewsDel",jsonObject.toString());
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
						NewToast.makeText(AreaWarnActivity.this,"删除成功", 0).show();
						list.remove(position);
//						adapter.notifyDataSetChanged();
						adapter = new MyAdapter(list, AreaWarnActivity.this);
						listView.setAdapter(adapter);
					}else if(code == 2){
						String msg = object.getString("msg");
						NewToast.makeText(AreaWarnActivity.this,msg, 0).show();
					}else{
						NewToast.makeText(AreaWarnActivity.this,"系统异常", 0).show();
					}
					
					if(dialog != null && dialog.isShowing()){
						dialog.dismiss();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					if(dialog != null && dialog.isShowing()){
						dialog.dismiss();
					}
					NewToast.makeText(AreaWarnActivity.this,"删除失败", 0).show();
				}
			}
		}.execute();
	}
}
