package com.wangtian.message;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import android.widget.TextView.OnEditorActionListener;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.wangtian.message.adapter.MyAdapter;
import com.wangtian.message.base.BaseMainMenuActivity;
import com.wangtian.message.util.Contact;
import com.wangtian.message.util.DateUtil;
import com.wangtian.message.util.HttpUtil;
import com.wangtian.message.util.Network;
import com.wangtian.message.util.NewToast;
import com.wangtian.message.view.EXListView;
import com.wangtian.message.view.EXListView.IXListViewListener;
/**
 * 收藏
 * @author chen
 *
 */
public class CollectActivity extends BaseMainMenuActivity implements IXListViewListener{

	private RelativeLayout no;
	private EXListView allcollectlist;
	private EditText seach;
	private ArrayList<HashMap<String,Object>> list;
	private ArrayList<HashMap<String,Object>> all_list;
	private ArrayList<HashMap<String,Object>> temp_list;
	private MyAdapter adapter;
	private int count = 1;//用于表示下一页
	private String state;
	private boolean downup;
	private ProgressDialog dialog;
	private int pageSize = 20;//每一页的数量
	private String key;
	private boolean show = true;
	private boolean ref = true;
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			downup = false;
			if (key != null && key.length() > 0 && state.equals("")) {
				// 结束
				allcollectlist.mScrollBack = 0;
				allcollectlist.mScroller.startScroll(0, 100, 0, 0 - 100, 400);
			} //
			switch (msg.what) {
			case 2328:
				ref = true;
				if (state.equals("up")) { // 上拉
					if (temp_list.size() > 0) {
						list.addAll(temp_list);
						count += 1;
						 update();
					} else {
//						if(show){
							NewToast.makeText(CollectActivity.this, "暂无更多数据", 0)
									.show();
//						}
					}
				} else {
					if (temp_list.size() > 0) {
						list.clear();
						count = 1;
						list.addAll(temp_list);
					}
					 update();
				}

				break;
			case 2222:
				NewToast.makeText(CollectActivity.this, "请检查网络连接", 1).show();
				break;
			case 555:
				NewToast.makeText(CollectActivity.this, "请检查网络连接", 1).show();
				break;
			case 2333:
//				if(show){
					NewToast.makeText(CollectActivity.this, "暂无更多数据", 0)
							.show();
//				}
			case 2444:
//				if(show){
					NewToast.makeText(CollectActivity.this, "暂无更多数据", 0)
							.show();
//				}
				break;
			default:
				break;
			}
			allcollectlist.stopRefresh();
			allcollectlist.stopLoadMore();
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_collect);
		setActivity(CollectActivity.this,"收藏");
		left(2);
		init();
	}

	private void init() {
		dialog = new ProgressDialog(CollectActivity.this);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);
		no = (RelativeLayout) findViewById(R.id.rel_no);
		allcollectlist = (EXListView) findViewById(R.id.list);
		allcollectlist.setPullLoadEnable(true);
		allcollectlist.setPullRefreshEnable(true);
		allcollectlist.setXListViewListener(this);
		allcollectlist.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(CollectActivity.this,WarnXiangQingActivity.class);
				intent.putExtra("articleid", list.get(position-1).get("articleid").toString());
				intent.putExtra("type", list.get(position-1).get("type").toString());
				intent.putExtra("show", false);
				startActivity(intent);
			}
		});
		seach = (EditText) findViewById(R.id.ed_seach);
		seach.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				
				if(event != null && event.getKeyCode() ==  KeyEvent.KEYCODE_ENTER){
					if(key != null && key.equals(v.getText().toString().trim())){
					}else{
						String tagid = null;
						key =  v.getText().toString().trim();
						if(all_list == null){
							all_list = new ArrayList<HashMap<String,Object>>();
							all_list.clear();
							all_list.addAll(list);
						}
						temp_list.clear();
						list.clear();
						 update();
						getDate(0,false);
					}
					return true;
				}else if(event == null && actionId == 3){
					if(key != null && key.equals(v.getText().toString().trim())){
					}else{
						String tagid = null;
						key =  v.getText().toString().trim();
						if(all_list == null){
							all_list = new ArrayList<HashMap<String,Object>>();
							all_list.clear();
							all_list.addAll(list);
						}
						temp_list.clear();
						list.clear();
						 update();
						getDate(0,false);
					}
					return true;
				}
				return false;
			}
		});
//		seach.setOnKeyListener(new OnKeyListener() {
//			
//			@Override
//			public boolean onKey(View v, int keyCode, KeyEvent event) {
//				Toast.makeText(getApplication(), "OnKeyListener", 0).show();
//				return false;
//			}
//		});
		findViewById(R.id.tx_cancal).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(list != null && all_list != null && adapter != null){
					list.clear();
					list.addAll(all_list);
					update();
				}
				key = null;
				seach.setText("");
				InputMethodManager im = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);   im.hideSoftInputFromWindow(getCurrentFocus().getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}
		});
		getAdapter();
	}
	private void update(){
		if(list != null && list.size() > 0 && adapter != null){
			adapter.notifyDataSetChanged();
			allcollectlist.setVisibility(View.VISIBLE);
			no.setVisibility(View.GONE);
		}else{
			allcollectlist.setVisibility(View.GONE);
			no.setVisibility(View.VISIBLE);
		}
	}
	public void getAdapter() {
		getDate(0, false);
		// step 1. create a MenuCreator
		SwipeMenuCreator creator = new SwipeMenuCreator() {

			@Override
			public void create(SwipeMenu menu) {
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
		// set creator
		allcollectlist.setMenuCreator(creator);

		// step 2. listener item click event
		allcollectlist
				.setOnMenuItemClickListener(new OnMenuItemClickListener() {
					@Override
					public boolean onMenuItemClick(int position,
							SwipeMenu menu, int index) {
						delsect(list.get(position).get("id").toString(),position);
						return false;
					}
				});
	}

	@Override
	public void onRefresh() {
		handler.postDelayed(new Runnable() {
			public void run() {
				if (downup == false) {
					if (Network.checkNetWork(CollectActivity.this)) {
						// initializeData();
						state = "down";
						downup = true;
						getDate(0,true);
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
						if (Network.checkNetWork(CollectActivity.this)) {
							// initializeData();
							state = "up";
							downup = true;
							getDate(count*pageSize,true);
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
	 *  
	 */
	public void getDate(final int pageBegin,final boolean back) {
		dialog.setMessage("数据加载中...");
		dialog.show();
		new AsyncTask<Void, Void, String>() {
			
			@Override
			protected String doInBackground(Void... arg0) {
				String retSrc = null;
				ref = false;
				try {
					JSONObject jsonObject = new JSONObject();
//					jsonObject.put("groupId", MyApplication.user.getGroupid());
					jsonObject.put("userId",  MyApplication.user.getId());
					jsonObject.put("pageBegin", pageBegin+"");
					jsonObject.put("keyWord", key);
					jsonObject.put("pageSize", pageSize+"");
					retSrc = HttpUtil.getPostJsonWithUrl(Contact.url+"Collect/CollectList",jsonObject.toString());
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
							if(data.length() > 19){
								show = true;
							}else{
								show = false;
							}
							for (int i = 0; i < data.length(); i++) {
								JSONObject object2 = (JSONObject) data.get(i);
								HashMap<String,Object> map = new HashMap<String, Object>();
								map.put("title", object2.getString("name"));
//								String[] strs = object2.getString("warnid").split(",");
//								StringBuffer buffer = new StringBuffer();
//								for (int j = 0; j < strs.length; j++) {
//									buffer.append(strs[j]+"      ");
//								}
//								map.put("time", buffer.toString());
								map.put("time", DateUtil.getDate(object2.getString("publishDate")));
//								map.put("pinlun",object2.getString("commentCount"));
								map.put("pinlun","");
								map.put("laiyuan", object2.getString("origin"));
								map.put("id", object2.getString("id"));
								map.put("articleid", object2.getString("articleid"));
								map.put("type", object2.getString("type"));
								String d = new JSONObject(object2.getString("tags")).getString("境内外");
								if("境外".equals(d)){
									map.put("tags",true);
								}else{
									map.put("tags",false);
								}
								temp_list.add(map);
							}
							if(!back && key != null && key.length() > 0 && data.length() < 1){
//								handler.sendEmptyMessage(2444);
								NewToast.makeText(CollectActivity.this, "暂无数据", 1).show();
							}
							if(back){
								handler.sendEmptyMessage(2328);
							}else{
								ref = true;
								if(list == null){
									list = new ArrayList<HashMap<String,Object>>();
								}
								list.addAll(temp_list);
								if(adapter == null){
									adapter = new MyAdapter(list,CollectActivity.this);
									allcollectlist.setAdapter(adapter);
								}else{
									 update();
								}
							}
					}else if(code == 2){
						ref = true;
						String msg = object.getString("msg");
						NewToast.makeText(CollectActivity.this,msg, 0).show();
					}else{
						ref = true;
						NewToast.makeText(CollectActivity.this,"系统异常", 0).show();
					}
					if(list != null && list.size() > 0){
						allcollectlist.setVisibility(View.VISIBLE);
						no.setVisibility(View.GONE);
					}else{
						allcollectlist.setVisibility(View.GONE);
						no.setVisibility(View.VISIBLE);
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
					NewToast.makeText(CollectActivity.this,"数据获取失败", 0).show();
				}
			}
			
		}.execute();
	}
	/**
	 * 删除收藏
	 *  
	 */
	public void delsect(final String id,final int position) {
		dialog.setMessage("收藏删除中...");
		dialog.show();
		new AsyncTask<Void, Void, String>() {
			
			@Override
			protected String doInBackground(Void... arg0) {
				String retSrc = null;
				try {
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("id", id);
					retSrc = HttpUtil.getPostJsonWithUrl(Contact.url+"Collect/CollectDel",jsonObject.toString());
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
						String id = (String) list.get(position).get("id");
						if(all_list != null ){
							for (int i = 0; i < all_list.size(); i++) {
								HashMap<String,Object> map = all_list.get(i);
								if(id.equals(map.get("id"))){
									all_list.remove(i);
									break;
								}
							}
						}
						list.remove(position);
//						 update();
						adapter = new MyAdapter(list, CollectActivity.this);
						allcollectlist.setAdapter(adapter);
						if(list != null && list.size() > 0){
							allcollectlist.setVisibility(View.VISIBLE);
							no.setVisibility(View.GONE);
						}else{
							allcollectlist.setVisibility(View.GONE);
							no.setVisibility(View.VISIBLE);
						}
						NewToast.makeText(CollectActivity.this,"删除成功", 0).show();
					}else if(code == 2){
						String msg = object.getString("msg");
						NewToast.makeText(CollectActivity.this,msg, 0).show();
					}else{
						NewToast.makeText(CollectActivity.this,"系统异常", 0).show();
					}
					if(list != null && list.size() > 0){
						allcollectlist.setVisibility(View.VISIBLE);
						no.setVisibility(View.GONE);
					}else{
						allcollectlist.setVisibility(View.GONE);
						no.setVisibility(View.VISIBLE);
					}
					if(dialog != null && dialog.isShowing()){
						dialog.dismiss();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					if(dialog != null && dialog.isShowing()){
						dialog.dismiss();
					}
					NewToast.makeText(CollectActivity.this,"删除失败", 0).show();
				}
			}
			
		}.execute();
	}
	
//	@Override
//	public boolean dispatchKeyEvent(KeyEvent event) {
//		Toast.makeText(getApplication(), "dispatchKeyEvent", 0).show();
//		return super.dispatchKeyEvent(event);
//	}

//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
////		if(keyCode == KeyEvent.KEYCODE_ENTER){
//			Toast.makeText(getApplication(), "onKeyDown"+keyCode, 0).show();
////		}
//		return super.onKeyDown(keyCode, event);
//	}
}
