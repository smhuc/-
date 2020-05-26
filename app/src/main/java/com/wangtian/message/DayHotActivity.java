package com.wangtian.message;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wangtian.message.base.BaseMainMenuActivity;
import com.wangtian.message.bean.Article;
import com.wangtian.message.bean.Tag;
import com.wangtian.message.netBean.NewListResponse;
import com.wangtian.message.netWork.NetWorkSubscriber;
import com.wangtian.message.netWork.NetWorkUtils;
import com.wangtian.message.util.DateUtil;
import com.wangtian.message.util.Network;
import com.wangtian.message.util.NewToast;
import com.wangtian.message.view.HorizontalListView;
import com.wangtian.message.view.XListView;
import com.wangtian.message.view.XListView.IXListViewListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 今日热点的主页
 * @author chen
 *
 */
public class DayHotActivity extends BaseMainMenuActivity{

	private HorizontalListView horizontal;
	private XListView listview;
	private MyHorAdapter hadapter;
	private ArrayList<Tag> hlist;
	private ArrayList<Tag> alllist;
	private MyListAdapter ladapter;
	private LinearLayout lin_seach,add,adds;
	private ArrayList<Article> llist;
	private ArrayList<Article> all_llist;//查询前的列表
	private PopupWindow pop;
	private EditText seach;
	private ImageView more;
	private String state;
	private boolean downup;
	private HashSet<String> set;
	private ArrayList<Article> temp_llist;
	private int count = 1;//用于表示下一页
	private ProgressDialog dialog;
	private SharedPreferences sp;
	private String name; //选中的栏目的名字
	private boolean delete = false; //判断选中的栏目是否被删除
	private GridAdapter gridAdapter;
	private String key;
	private int pagesize = 20;
	private String tagid;
	private boolean ref = true;//用来控制一次只能执行一次请求
	
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
					if (temp_llist.size() > 0) {
						llist.addAll(temp_llist);
						count += 1;
						ladapter.notifyDataSetChanged();
					} else {
						NewToast.makeText(DayHotActivity.this, "暂无更多数据", 0)
								.show();
					}
				} else {
					if (temp_llist.size() > 0) {
						llist.clear();
						count = 1;
						llist.addAll(temp_llist);
					}
					ladapter.notifyDataSetChanged();
				}

				break;
			case 2222:
				NewToast.makeText(DayHotActivity.this, "请检查网络连接", 1).show();
				break;
			case 555:
				NewToast.makeText(DayHotActivity.this, "请检查网络连接", 1).show();
				break;
			case 2333:
				NewToast.makeText(DayHotActivity.this, "暂无更多数据", 1).show();
			case 2444:
				NewToast.makeText(DayHotActivity.this, "服务器暂无数据", 1).show();
				break;
			default:
				break;
			}
			listview.stopRefresh();
			listview.stopLoadMore();
		}
	};
	private int page=0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_hot);
		setActivity(DayHotActivity.this,"今日热点");
		left(2).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				/*if(menu.isShown()){
					if(hlist.size() > 1){
						if(pop != null && pop.isShowing()){
							pop.dismiss();
						}
						menu.showSecondaryMenu();
						more.setImageResource(R.drawable.hottoday_cbb_nor);
						horizontal.setVisibility(View.VISIBLE);
						adds.setVisibility(View.GONE);
					}else{
						NewToast.makeText(DayHotActivity.this, "栏目不能少于两个", 0).show();
					}
				}else{
					menu.showMenu();
					
				}*/

				menu.toggle(menu.isShown());

			}
		});
		RelativeLayout rel = (RelativeLayout) findViewById(R.id.rel_right);
		rel.setVisibility(View.VISIBLE);
		findViewById(R.id.img_right).setVisibility(View.GONE);
		findViewById(R.id.tx_right).setVisibility(View.VISIBLE);
		rel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(DayHotActivity.this, AddDingYueActivity.class);
				startActivity(intent);
			}
		});
		init();
	}
	
	private void init() {
		dialog = new ProgressDialog(DayHotActivity.this);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);
		horizontal = (HorizontalListView) findViewById(R.id.horizontal);
		hlist = new ArrayList<Tag>();
		sp = getSharedPreferences("tag", Activity.MODE_PRIVATE);
		set = (HashSet<String>) sp.getStringSet("tag"+MyApplication.user.getId(), null);
		gridAdapter = new GridAdapter();//这个是所有栏目的适配
		if(set == null){
			getAll();
		}else{
			Iterator<String> next = set.iterator();
			hlist = new ArrayList<Tag>();
			while (next.hasNext()) {
				String temp = next.next();
				String[] temps = temp.split("_");
				Tag tag = new Tag();
				tag.setId(Integer.parseInt(temps[0]));
				tag.setName(temps[1]);
				tag.setTime(Long.parseLong(temps[2]));
				hlist.add(tag);
			}
			for (int i = 0; i < hlist.size(); i++) {
				for (int j = i+1; j < hlist.size(); j++) {
					if(hlist.get(i).getTime() > hlist.get(j).getTime()){
						Tag temp = hlist.get(i);
						hlist.set(i, hlist.get(j));
						hlist.set(j, temp);
					}
				}
			}
		}
		hadapter = new MyHorAdapter();
		horizontal.setAdapter(hadapter);
		horizontal.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				TextView textView = (TextView) v.getTag(R.id.tx_title);
				TextView textView1 = (TextView) hadapter.oldView.getTag(R.id.tx_title);
				if(!textView.getText().toString().equals(textView1.getText().toString())){
					key = null;
					seach.setText("");
					View view = (View) v.getTag(R.id.bottom);
					view.setVisibility(View.VISIBLE);
					name = hlist.get(position).getName();
					textView.setTextColor(getResources().getColor(R.color.title_red));
					View view1 = (View) hadapter.oldView.getTag(R.id.bottom);
					view1.setVisibility(View.INVISIBLE);
					textView1.setTextColor(getResources().getColor(R.color.text_bw));
					hadapter.oldView = v;
					if(hlist.get(position).getName().equals("订阅")){
						add.setVisibility(View.VISIBLE);
						lin_seach.setVisibility(View.GONE);
					}else{
						add.setVisibility(View.GONE);
						lin_seach.setVisibility(View.VISIBLE);
					}
					llist.clear();
					count = 0;
					postData("0", null, hlist.get(position).getId().toString(),false);
				}
			}
		});
		more = (ImageView) findViewById(R.id.img_more);
		adds = (LinearLayout) findViewById(R.id.lin_adds);
		more.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ImageView img = (ImageView) v;
				if(horizontal.isShown()){
					img.setImageResource(R.drawable.hottoday_cbb_pre);
					horizontal.setVisibility(View.GONE);
					adds.setVisibility(View.VISIBLE);
					initPop(v);
				}else{
					if(hlist.size() < 2){
						NewToast.makeText(DayHotActivity.this,"栏目不能小于两个", 0).show();
					}else{
						sp = getSharedPreferences("tag", Activity.MODE_PRIVATE);
						Editor ed = sp.edit();
						ed.putStringSet("tag"+MyApplication.user.getId(), set);
						ed.commit();
						img.setImageResource(R.drawable.hottoday_cbb_nor);
						horizontal.setVisibility(View.VISIBLE);
						adds.setVisibility(View.GONE);
						if(pop != null && pop.isShowing()){
							pop.dismiss();
						}
					}
				}
			}
		});
		seach = (EditText) findViewById(R.id.ed_seach);
//		seach.setOnEditorActionListener(new OnEditorActionListener() {
//
//			@Override
//			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//				if(event != null && event.getKeyCode() ==  KeyEvent.KEYCODE_ENTER){
//					String tagid = null;
//					if(key != null && key.equals(v.getText().toString().trim())){
//					}else{
//						for (int i = 0; i < hlist.size(); i++) {
//							if(hlist.get(i).getName().equals(name)){
//								tagid = hlist.get(i).getId()+"";
//							}
//						}
//						key =  v.getText().toString().trim();
//						if(all_llist == null){
//							all_llist = new ArrayList<Article>();
//						}
//						all_llist.clear();
//						all_llist.addAll(llist);
//						postData("0",key, tagid, false);
//					}
//					return true;
//				}else if(event == null && actionId == 3){
//					String tagid = null;
//					if(key != null && key.equals(v.getText().toString().trim())){
//					}else{
//						for (int i = 0; i < hlist.size(); i++) {
//							if(hlist.get(i).getName().equals(name)){
//								tagid = hlist.get(i).getId()+"";
//							}
//						}
//						key =  v.getText().toString().trim();
//						if(all_llist == null){
//							all_llist = new ArrayList<Article>();
//						}
//						all_llist.clear();
//						all_llist.addAll(llist);
//						postData("0",key, tagid, false);
//					}
//					return true;
//				}
//				return false;
//			}
//		});
		TextView cancal = (TextView) findViewById(R.id.tx_cancal);
		cancal.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				seach.setText("");
				if(all_llist !=  null){
					llist.clear();
					llist.addAll(all_llist);
					all_llist.clear();
				}
				key = null;
				ladapter.notifyDataSetChanged();
			}
		});
		listview = (XListView) findViewById(R.id.listview);
		RelativeLayout rel = (RelativeLayout) findViewById(R.id.rel);
		listview.setPullLoadEnable(true);
		listview.setPullRefreshEnable(true);
		listview.setXListViewListener(new IXListViewListener() {
			
			@Override
			public void onRefresh() {
				handler.postDelayed(new Runnable() {
					public void run() {
						if (downup == false) {
							if (Network.checkNetWork(DayHotActivity.this)) {
								// initializeData();
								state = "down";
								downup = true;
								dialog.setMessage("数据加载中...");
								dialog.show();
								postData("0", key, tagid,true);
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
								if (Network.checkNetWork(DayHotActivity.this)) {
									// initializeData();
									state = "up";
									downup = true;
									dialog.setMessage("数据加载中...");
									dialog.show();
									postData(count*pagesize+"", key, tagid,true);
								} else {
									handler.sendEmptyMessage(555);
								}
							}
						}
					}, 2000);
//			}else{
//				Toast.makeText(DayHotActivity.this, "请求中。。。", 0).show();
//				listview.stopLoadMore();
			}
			}
		});
		llist = new ArrayList<Article>();
		ladapter = new MyListAdapter();
		listview.setAdapter(ladapter);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				Intent intent = new Intent(DayHotActivity.this, HotXiangQingActivity.class);
				intent.putExtra("data", llist.get(position-1));
				startActivity(intent);
			}
		});
		lin_seach = (LinearLayout) findViewById(R.id.lin_seach);
		add = (LinearLayout) findViewById(R.id.lin_add);
		add.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(DayHotActivity.this, AddDingYueActivity.class);
				startActivity(intent);
			}
		});
		if(hlist != null && hlist.size() > 0 && hlist.get(0).getName().equals("订阅")){
			add.setVisibility(View.VISIBLE);
			lin_seach.setVisibility(View.GONE);
		}
		postData("0", key, tagid,true);
	}
	/**
	 * 增加栏目的窗口
	 */
	private void initPop(View anchor) {
		View view = getLayoutInflater().inflate(R.layout.pop_add_title, null);
		GridView grid = (GridView) view.findViewById(R.id.grid);
		if(alllist == null){
			getAll();
		}
		grid.setAdapter(gridAdapter);
		pop = new PopupWindow(view,LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, false);
		pop.showAsDropDown(anchor);
	}
	/**
	 * 增加栏目的适配
	 */
	class GridAdapter extends BaseAdapter{

		private int a = 0;
		@Override
		public int getCount() {
			if(alllist == null ){
				alllist = new ArrayList<Tag>();
			}
			return alllist.size();
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
			TextView text = new TextView(DayHotActivity.this);
			text.setGravity(Gravity.CENTER);
			text.setText(alllist.get(position).getName());
			boolean has = false;
			for (int i = 0; i < hlist.size(); i++) {
				if(hlist.get(i).getName().equals(alllist.get(position).getName())){
					has = true;
					continue;
				}
			}
			if(!has){
				text.setBackgroundResource(R.drawable.hottoday_column_ico_nor);
				text.setTextColor(getResources().getColor(R.color.text_hui9));
				text.setTag(false);
			}else{
				text.setBackgroundResource(R.drawable.hottoday_column_ico_pre);
				text.setTextColor(getResources().getColor(R.color.white));
				text.setTag(true);
			}
			text.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					TextView text = (TextView) v;
					sp = getSharedPreferences("tag", Activity.MODE_PRIVATE);
					Editor ed = sp.edit();
						if(text.getTag().toString().equals("true")){
							a = hlist.size();
								text.setBackgroundResource(R.drawable.hottoday_column_ico_nor);
								text.setTextColor(getResources().getColor(R.color.text_hui9));
								text.setTag(false);
								for (int i = 0; i < hlist.size(); i++) {
									if(hlist.get(i).getName().equals(alllist.get(position).getName())){
										if(name.equals(hlist.get(i).getName())){
											delete = true;
										}
										hlist.remove(i);
										i -= 1;
										continue;
									}
								}
						}else{
							text.setBackgroundResource(R.drawable.hottoday_column_ico_pre);
							text.setTextColor(getResources().getColor(R.color.white));
							text.setTag(true);
							if(name.equals(alllist.get(position).getName())){
								delete = false;
							}
							alllist.get(position).setTime(System.currentTimeMillis());
							hlist.add(alllist.get(position));
						}
						set = new HashSet<String>();
						for (int i = 0; i < hlist.size(); i++) {
							String temp = hlist.get(i).getId()+"_"+hlist.get(i).getName()+"_"+hlist.get(i).getTime();
							set.add(temp);
						}
						ed.putStringSet("tag"+MyApplication.user.getId(), set);
						ed.commit();
						hadapter.notifyDataSetChanged();
				}
			});
			return text;
		}
		
	}
	/**
	 * 栏目的适配器
	 * @author chen
	 */
	class MyHorAdapter extends BaseAdapter{
		
		private View oldView;
		private boolean frist = true;
		
		@Override
		public int getCount() {
			if(hlist == null){
				hlist = new ArrayList<Tag>();
			}
			return hlist.size();
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
			TextView title;
			View view;
//			if(convertView == null){
				convertView = getLayoutInflater().inflate(R.layout.hor_item_title, null);
				title = (TextView) convertView.findViewById(R.id.tx_title);
				view = convertView.findViewById(R.id.bottom);
				convertView.setTag(R.id.tx_title, title);
				convertView.setTag(R.id.bottom, view);
				convertView.setTag(R.layout.hor_item_title, position);
//			}else{
//				title = (TextView) convertView.getTag(R.id.tx_title);
//				view = (View) convertView.getTag(R.id.bottom);
//			}
			title.setText(hlist.get(position).getName());
			
			if(delete && position == 0){
				view.setVisibility(View.VISIBLE);
				title.setTextColor(getResources().getColor(R.color.title_red));
				oldView = convertView;
				name = hlist.get(position).getName();
				frist = false;
				postData("0", null, hlist.get(position).getId()+"",false);
			}else if(name == hlist.get(position).getName()){
				view.setVisibility(View.VISIBLE);
				title.setTextColor(getResources().getColor(R.color.title_red));
				oldView = convertView;
				frist = false;
			}
			if(frist && position == 0){
				view.setVisibility(View.VISIBLE);
				title.setTextColor(getResources().getColor(R.color.title_red));
				oldView = convertView;
				name = hlist.get(position).getName();
				postData("0", null, hlist.get(position).getId().toString(),false);
				frist = false;
			}
			return convertView;
		}
	}
	/**
	 * 内容的适配器
	 * @author chen
	 *
	 */
	class MyListAdapter extends BaseAdapter{

		private int with;
		
		public MyListAdapter() {
			super();
			DisplayMetrics metrics = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(metrics);
			int with1 = metrics.widthPixels;
			if(with1 < 720){
				with = 30;
			}else if(with1 < 1080){
				with = 33;
			}else if(with1 < 1440){
				with = 36;
			}else{
				with = 39;
			}
		}

		@Override
		public int getCount() {
			if(llist == null){
				//llist = new ArrayList<>();
				llist = new ArrayList<Article>();
			}
			return llist.size();
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
			TextView title,laiyuan,pinlun,time,baozhi;
			ImageView type;
			LinearLayout dingyue;
			if(convertView == null){
				convertView = getLayoutInflater().inflate(R.layout.hot_list_item, null);
				dingyue = (LinearLayout) convertView.findViewById(R.id.lin_dingyue);
				baozhi = (TextView) convertView.findViewById(R.id.tx_baozhi);
				title = (TextView) convertView.findViewById(R.id.tx_news_title);
				laiyuan = (TextView) convertView.findViewById(R.id.tx_laiyuan);
				pinlun = (TextView) convertView.findViewById(R.id.tx_pinlun);
				time = (TextView) convertView.findViewById(R.id.tx_time);
				type = (ImageView) convertView.findViewById(R.id.img_type);
				convertView.setTag(R.id.tx_news_title, title);
				convertView.setTag(R.id.tx_laiyuan, laiyuan);
				convertView.setTag(R.id.tx_pinlun, pinlun);
				convertView.setTag(R.id.tx_time, time);
				convertView.setTag(R.id.img_type, type);
				convertView.setTag(R.id.lin_dingyue, dingyue);
				convertView.setTag(R.id.tx_baozhi, baozhi);
			}else{
				title = (TextView) convertView.getTag(R.id.tx_news_title);
				laiyuan = (TextView) convertView.getTag(R.id.tx_laiyuan);
				pinlun = (TextView) convertView.getTag(R.id.tx_pinlun);
				time = (TextView) convertView.getTag(R.id.tx_time);
				type = (ImageView) convertView.getTag(R.id.img_type);
				dingyue = (LinearLayout) convertView.getTag(R.id.lin_dingyue);
				baozhi = (TextView) convertView.getTag(R.id.tx_baozhi);
			}
			DisplayMetrics metrics = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(metrics);
			laiyuan.setWidth(metrics.widthPixels/2-200);
			if(name.equals("订阅")){
				dingyue.setVisibility(View.VISIBLE);
				baozhi.setText(llist.get(position).getOrigin().toString());
			}else{
				dingyue.setVisibility(View.GONE);
			}			title.setText(llist.get(position).getTitle().toString());
			laiyuan.setText(llist.get(position).getOrigin().toString());
			pinlun.setText("评论   "+llist.get(position).getCommentCount()+"");
			time.setText(DateUtil.getDate(llist.get(position).getPublishDate()));
			try {
				String d = new JSONObject(llist.get(position).getTags()).getString("境内外");
				if("境外".equals(d)){
					type.setVisibility(View.VISIBLE);
				}else{
					type.setVisibility(View.GONE);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return convertView;
		}
	}
	/**
	 * 获取数据的方法
	 * @param pageBegin 第几页的数据
	 * @param keyWord 关键字
	 * @param tagId 标签id
	 * @param back 是否需要进行处理，只用于上下刷新
	 */
	public void postData(final String pageBegin, String keyWord,final String tagId,final boolean back) {
		ref = false;
		if(keyWord != null && keyWord.length() > 0){
		}else{
			seach.setText("");
		}
		dialog.setMessage("数据加载中...");
		dialog.show();


		HashMap<String, String> param = new HashMap<>();
		param.put("token", MyApplication.sLoginBean.getToken());
		param.put("page", String.valueOf(pageBegin));
		param.put("rowsPerPage", String.valueOf(20));
		param.put("keyword", TextUtils.isEmpty(keyWord)?"":keyWord);


		NetWorkUtils.getInstance().getInterfaceService().getNewsList(param)
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(/*new NetWorkSubscriber<ArrayList<UserListBean>>() {
					@Override
					public void onNext(ArrayList<UserListBean> userListBeans) {
						if (userListBeans!=null&&userListBeans.size()>0){
							if (page==0){
								mUserManagerAdapter.setData(userListBeans);
							}else {
								mUserManagerAdapter.addData(userListBeans);
							}
							page++;

						}
					}
				}*/
						new NetWorkSubscriber<NewListResponse>() {
							@Override
							public void onError(Throwable e) {
								super.onError(e);
								dialog.dismiss();
							}
							@Override
							public void onNext(NewListResponse newListResponse) {
								dialog.dismiss();
								/*for (NewListResponse.InformationListBean informationListBean : newListResponse.getInformationList()) {
									Article article = new Article();
									article.setAuthor(object.getString("author"));
									article.setCmments(object.getString("cmments"));
									article.setCommentCount(object.getInt("commentCount"));
									article.setContent(object.getString("content"));
									article.setOrigin(object.getString("origin"));
									article.setPublishDate(object.getString("publishDate"));
									article.setRowKey(object.getString("rowKey"));
									article.setTags(object.getString("tags"));
									article.setTitle(object.getString("title"));
									article.setUrl(object.getString("url"));
									temp_llist.add(article);
								}*/





							}
						}
				);







//		new AsyncTask<Void, Void, String>() {
//
//			@Override
//			protected String doInBackground(Void... arg0) {
//				String retSrc = null;
//				try {
//					JSONObject jsonObject = new JSONObject();
////					jsonObject.put("groupId", MyApplication.user.getGroupid());//用户id
//					jsonObject.put("pageBegin", pageBegin);
//					jsonObject.put("pageSize", pagesize+"");
//					jsonObject.put("keyWord", keyWord);
//					key = keyWord;
//					jsonObject.put("tagId", tagId);
//					tagid = tagId;
//					retSrc = HttpUtil.getPostJsonWithUrl(Contact.url+"Opinion/HotList",jsonObject.toString());
//					Log.e("retSrc", retSrc.toString());
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				return retSrc;
//			}
//
//			@Override
//			protected void onPostExecute(String result) {
//				try {
//					JSONObject json = new JSONObject(result);
//					int code = json.getInt("code");
//					if(code == 1){
//						JSONArray data = json.getJSONArray("data");
//						temp_llist = new ArrayList<Article>();
//						if(pageBegin.equals("0")){
//							llist.clear();
//						}
//						for (int i = 0; i < data.length(); i++) {
//							JSONObject object = (JSONObject) data.get(i);
//							Article article = new Article();
//							article.setAuthor(object.getString("author"));
//							article.setCmments(object.getString("cmments"));
//							article.setCommentCount(object.getInt("commentCount"));
//							article.setContent(object.getString("content"));
//							article.setOrigin(object.getString("origin"));
//							article.setPublishDate(object.getString("publishDate"));
//							article.setRowKey(object.getString("rowKey"));
//							article.setTags(object.getString("tags"));
//							article.setTitle(object.getString("title"));
//							article.setUrl(object.getString("url"));
//							temp_llist.add(article);
//						}
//						if(keyWord != null && keyWord.length() > 0 && (data == null || data.length() < 1)){
////							handler.sendEmptyMessage(2444);
//							NewToast.makeText(DayHotActivity.this, "暂无数据", 1).show();
//						}
//						if(back){
//							handler.sendEmptyMessage(2328);
//						}else{
//							ref = true;
//							llist.addAll(temp_llist);
//							ladapter.notifyDataSetChanged();
//						}
//						if(dialog != null && dialog.isShowing()){
//							dialog.dismiss();
//						}
//					}else if(code == 2){
//						NewToast.makeText(DayHotActivity.this,"数据获取失败", 0).show();
//						ref = true;
//					}else{
//						NewToast.makeText(DayHotActivity.this,"系统异常", 0).show();
//						ref = true;
//					}
//				} catch (JSONException e) {
//					e.printStackTrace();
//					if(dialog != null && dialog.isShowing()){
//						dialog.dismiss();
//					}
//					ref = true;
//					NewToast.makeText(DayHotActivity.this,"数据获取失败", 0).show();
//				}
//			}
//
//		}.execute();
	}
	/**
	 * 获取所有的栏目的方法
	 */
	public void getAll() {
		/*dialog.setMessage("数据加载中...");
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
					if (TextUtils.isEmpty(result)){
						dialog.dismiss();
						return;
					}
					JSONObject json = new JSONObject(result);
					int code = json.getInt("code");
					if(code == 1){
						JSONArray data = json.getJSONArray("data");
						alllist = new ArrayList<Tag>();
						for (int i = 0; i < data.length(); i++) {
							Tag tag = new Tag();
							JSONObject object = (JSONObject) data.get(i);
							tag.setGroupid(object.getInt("groupid"));
							tag.setId(object.getInt("id"));
							tag.setName(object.getString("name"));
							tag.setTime(System.currentTimeMillis());
							alllist.add(tag);
						}
						sp = getSharedPreferences("tag", Activity.MODE_PRIVATE);
						HashSet<String> set = (HashSet<String>) sp.getStringSet("tag"+MyApplication.user.getId(), null);
						if(set == null){
							hlist.addAll(alllist);
						}else{
							for (int i = 0; i < hlist.size(); i++) {
								for (int j = 0; j < alllist.size(); j++) {
									if(hlist.get(i).getName().equals(alllist.get(j).getName())){
										alllist.get(j).setTime(hlist.get(i).getTime());
										break;
									}
								}
							}
							for (int i = 0; i < alllist.size(); i++) {
								
								for (int j = i+1; j < alllist.size(); j++) {
									if(alllist.get(i).getTime() > alllist.get(j).getTime()){
										Tag temp = alllist.get(i);
										alllist.set(i, alllist.get(j));
										alllist.set(j, temp);
									}
								}
							}
						}
						gridAdapter.notifyDataSetChanged();
						hadapter.notifyDataSetChanged();
					}else if(code == 2){
						NewToast.makeText(DayHotActivity.this,"数据获取失败", 0).show();
					}else{
						NewToast.makeText(DayHotActivity.this,"系统异常", 0).show();
					}
					if(dialog != null && dialog.isShowing()){
						dialog.dismiss();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					if(dialog != null && dialog.isShowing()){
						dialog.dismiss();
					}
					NewToast.makeText(DayHotActivity.this,"栏目获取失败", 0).show();
				}
			}
			
		}.execute();*/
	}
}
