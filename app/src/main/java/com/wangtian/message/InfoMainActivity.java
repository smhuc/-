package com.wangtian.message;

import java.util.ArrayList;
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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.wangtian.message.base.BaseMainMenuActivity;
import com.wangtian.message.bean.Report;
import com.wangtian.message.util.Contact;
import com.wangtian.message.util.DateUtil;
import com.wangtian.message.util.HttpUtil;
import com.wangtian.message.util.Network;
import com.wangtian.message.util.NewToast;
import com.wangtian.message.view.EXListView;
import com.wangtian.message.view.EXListView.IXListViewListener;
/**
 * 信息报告
 * @author chen
 *
 */
public class InfoMainActivity extends BaseMainMenuActivity implements OnItemClickListener{

	private TextView info1,info2;
	private View view1,view2;
	private EXListView swlist;
	private EXListView list1;
	private ArrayList<Report> list;
	private ArrayList<Report> temp_list;
	private int count = 1;//用于表示下一页
	private String state;
	private boolean downup;
	private MyAdapter adapter;
	private ProgressDialog dialog;
	private String type = "category";//传这个参数是获取简报
	private String type1 = "topic";//传这个参数是获取专题
	private int pagesize = 20;
	private boolean ref = true;
	
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			downup = false;
			if (state.equals("")) {
				// 结束
				list1.mScrollBack = 0;
				list1.mScroller.startScroll(0, 100, 0, 0 - 100, 400);
				swlist.mScrollBack = 0;
				swlist.mScroller.startScroll(0, 100, 0, 0 - 100, 400);
			} //
			switch (msg.what) {
			case 2328:
				if (state.equals("up")) { // 上拉
					ref = true;
					if (temp_list.size() > 0) {
						list.addAll(temp_list);
						count += 1;
						adapter.notifyDataSetChanged();
					} else {
						NewToast.makeText(InfoMainActivity.this, "暂无更多数据", 0)
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
				NewToast.makeText(InfoMainActivity.this, "请检查网络连接", 1).show();
				break;
			case 555:
				NewToast.makeText(InfoMainActivity.this, "请检查网络连接", 1).show();
				break;
			case 2333:
				NewToast.makeText(InfoMainActivity.this, "暂无更多数据", 1).show();
			case 2444:
				NewToast.makeText(InfoMainActivity.this, "服务器暂无数据", 1).show();
				break;
			default:
				break;
			}
			list1.stopRefresh();
			list1.stopLoadMore();
			swlist.stopRefresh();
			swlist.stopLoadMore();
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_infomain);
		setActivity(InfoMainActivity.this,"信息报告");
		left(2);
		right(1).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(InfoMainActivity.this, SeachDingYueActivity.class);
				if(!view1.isShown()){
					intent.putExtra("type", "topic");
				}else{
					intent.putExtra("type",type);
				}
				startActivity(intent);
			}
		});
		init();
	}

	private void init() {
		findViewById(R.id.lin_info1).setOnClickListener(this);
		info1 = (TextView) findViewById(R.id.tx_info1);
		view1 = findViewById(R.id.view_info1);
		findViewById(R.id.lin_info2).setOnClickListener(this);
		info2 = (TextView) findViewById(R.id.tx_info2);
		view2 = findViewById(R.id.view_info2);
		swlist = (EXListView) findViewById(R.id.list);
		list1 = (EXListView) findViewById(R.id.list1);
		dialog = new ProgressDialog(InfoMainActivity.this);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);
		swlist.setPullRefreshEnable(true);
		swlist.setPullLoadEnable(true);
		list1.setPullRefreshEnable(true);
		list1.setPullLoadEnable(true);
		list1.setXListViewListener(new IXListViewListener() {//这个是专题报告
			
			@Override
			public void onRefresh() {
				handler.postDelayed(new Runnable() {
					public void run() {
						if (downup == false) {
							if (Network.checkNetWork(InfoMainActivity.this)) {
								// initializeData();
								state = "down";
								downup = true;
								getDate(type1,0,true);
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
								if (Network.checkNetWork(InfoMainActivity.this)) {
									// initializeData();
									state = "up";
									downup = true;
									getDate(type1,count*pagesize,true);
								} else {
									handler.sendEmptyMessage(555);
								}
							}
						}
					}, 2000);
				}
			}
		});
		swlist.setXListViewListener(new EXListView.IXListViewListener() {//这个是信息简报
			
			@Override
			public void onRefresh() {
				handler.postDelayed(new Runnable() {
					public void run() {
						if (downup == false) {
							if (Network.checkNetWork(InfoMainActivity.this)) {
								// initializeData();
								state = "down";
								downup = true;
								getDate(type,0,true);
							} else {
								handler.sendEmptyMessage(555);
							}
							
						}
					}
				}, 2000);
			}
			
			@Override
			public void onLoadMore() {
				handler.postDelayed(new Runnable() {
					public void run() {
						if (downup == false) {
							if (Network.checkNetWork(InfoMainActivity.this)) {
								// initializeData();
								state = "up";
								downup = true;
								getDate(type,count*pagesize,true);
							} else {
								handler.sendEmptyMessage(555);
							}
						}
					}
				}, 2000);
			}
		});
		getDate(type,0,false);
		list1.setOnItemClickListener(this);
		swlist.setOnItemClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.lin_info1:
			info1.setTextColor(getResources().getColor(R.color.text_red));
			view1.setVisibility(View.VISIBLE);
			info2.setTextColor(getResources().getColor(R.color.text_hui));
			view2.setVisibility(View.GONE);
			if(list != null){
				list.clear();
			}
			getDate(type,0,false);
			count = 1;
			swlist.setVisibility(View.VISIBLE);
			list1.setVisibility(View.GONE);
			adapter.notifyDataSetChanged();
			break;
		case R.id.lin_info2:
			info2.setTextColor(getResources().getColor(R.color.text_red));
			view2.setVisibility(View.VISIBLE);
			info1.setTextColor(getResources().getColor(R.color.text_hui));
			view1.setVisibility(View.GONE);
			if(list != null){
				list.clear();
			}
			getDate(type1,0,false);
			count = 1;
			swlist.setVisibility(View.GONE);
			list1.setVisibility(View.VISIBLE);
			adapter.notifyDataSetChanged();
			break;

		default:
			break;
		}
		super.onClick(v);
	}
	public void getAdapter() {
		adapter = new MyAdapter();
		swlist.setAdapter(adapter);
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
		swlist.setMenuCreator(creator);
		swlist.setOnMenuItemClickListener(new OnMenuItemClickListener() {
					@Override
					public boolean onMenuItemClick(int position,
							SwipeMenu menu, int index) {
						delect(list.get(position).getId()+"", position);
						return false;
					}
				});
		list1.setMenuCreator(creator);
		list1.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(int position,
					SwipeMenu menu, int index) {
				delect(list.get(position).getId()+"", position);
				return false;
			}
		});
	}
	class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			if(list == null){
				list = new ArrayList<Report>();
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
			TextView title,time;
			if(convertView ==null){
				convertView = getLayoutInflater().inflate(R.layout.info_list_item,null);
				title = (TextView) convertView.findViewById(R.id.tx_title);
				time = (TextView) convertView.findViewById(R.id.tx_time);
				convertView.setTag(R.id.tx_title, title);
				convertView.setTag(R.id.tx_time, time);
			}else{
				title = (TextView) convertView.getTag(R.id.tx_title);
				time = (TextView) convertView.getTag(R.id.tx_time);
			}
			if(position != 0){
				convertView.findViewById(R.id.view).setVisibility(View.GONE);
			}
			title.setText(list.get(position).getName().toString());
			time.setText(DateUtil.getDate1(list.get(position).getCreateTime()));
			return convertView;
		}
	}
	/**
	 * 获取信息
	 *  type 表示类别
	 */
	public void getDate(final String string,final int pageBegin,final boolean back) {
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
					jsonObject.put("reportType", string);
					jsonObject.put("pageBegin", pageBegin+"");
					jsonObject.put("keyWord", "");
					jsonObject.put("pageSize ", pagesize+"");
					retSrc = HttpUtil.getPostJsonWithUrl(Contact.url+"Report/ReportList",jsonObject.toString());
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
							temp_list = new ArrayList<Report>();
							for (int i = 0; i < data.length(); i++) {
								Report report = new Report();
								JSONObject object2 = (JSONObject) data.get(i);
								report.setCreateTime(object2.getString("createTime"));
								report.setName(object2.getString("name"));
								report.setPath(object2.getString("path"));
								report.setId(object2.getInt("id"));
								report.setGroupid(object2.getInt("groupid"));
								temp_list.add(report);
							}
					}else if(code == 2){
						ref = true;
						String msg = object.getString("msg");
						NewToast.makeText(InfoMainActivity.this,msg, 0).show();
					}else{
						ref = true;
						NewToast.makeText(InfoMainActivity.this,"系统异常", 0).show();
					}
					
					if(back){
						handler.sendEmptyMessage(2328);
					}else{
						ref = true;
						if(list == null){
							list = new ArrayList<Report>();
						}
						list.addAll(temp_list);
						if(string.equals(type)){//表示是简报的数据
							getAdapter();
						}else{
							list1.setAdapter(new MyAdapter());
						}
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
					NewToast.makeText(InfoMainActivity.this,"数据获取失败", 0).show();
				}
			}
			
		}.execute();
	}
	/**
	 * 删除信息报告
	 */
	public void delect(final String id,final int position) {
		dialog.setMessage("信息删除中...");
		dialog.show();
		new AsyncTask<Void, Void, String>() {
			
			@Override
			protected String doInBackground(Void... arg0) {
				String retSrc = null;
				try {
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("id", id);
					retSrc = HttpUtil.getPostJsonWithUrl(Contact.url+"Report/ReportDel",jsonObject.toString());
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
						list.remove(position);
//						adapter.notifyDataSetChanged();
						adapter = new MyAdapter();
						swlist.setAdapter(adapter);
						list1.setAdapter(adapter);
						NewToast.makeText(InfoMainActivity.this,"删除成功", 0).show();
					}else if(code == 2){
						String msg = object.getString("msg");
						NewToast.makeText(InfoMainActivity.this,msg, 0).show();
					}else{
						NewToast.makeText(InfoMainActivity.this,"系统异常", 0).show();
					}
					
					if(dialog != null && dialog.isShowing()){
						dialog.dismiss();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					if(dialog != null && dialog.isShowing()){
						dialog.dismiss();
					}
					NewToast.makeText(InfoMainActivity.this,"删除失败", 0).show();
				}
			}
		}.execute();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent(InfoMainActivity.this,InfoXiangQingActivity.class);
		intent.putExtra("id",list.get(position-1).getId()+"");
		intent.putExtra("name",list.get(position-1).getName());
		startActivity(intent);
		
	}
}
