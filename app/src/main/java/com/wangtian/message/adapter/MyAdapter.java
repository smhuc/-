package com.wangtian.message.adapter;
import java.util.ArrayList;
import java.util.HashMap;
import android.app.Activity;
import android.text.BoringLayout.Metrics;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wangtian.message.R;

	/**
	 * 内容的适配器
	 * @author chen
	 *
	 */
public class MyAdapter extends BaseAdapter{
		
		private ArrayList<HashMap<String,Object>> list;
		private Activity activity;
		private int with;

		public MyAdapter(ArrayList<HashMap<String,Object>> list,Activity activity){
			this.list = list;
			this.activity = activity;
			DisplayMetrics metrics = new DisplayMetrics();
			activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
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
			if(list == null){
				//lists = new ArrayList<>();
				list = new ArrayList<HashMap<String,Object>>();
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
			TextView title,laiyuan,pinlun,time;
			ImageView photo,type;
			if(convertView == null){
				convertView = activity.getLayoutInflater().inflate(R.layout.new_list_item, null);
				title = (TextView) convertView.findViewById(R.id.tx_news_title);
				laiyuan = (TextView) convertView.findViewById(R.id.tx_laiyuan);
				pinlun = (TextView) convertView.findViewById(R.id.tx_pinlun);
				time = (TextView) convertView.findViewById(R.id.tx_time);
				photo = (ImageView) convertView.findViewById(R.id.img_photo);
				type = (ImageView) convertView.findViewById(R.id.img_type);
				convertView.setTag(R.id.tx_news_title, title);
				convertView.setTag(R.id.tx_laiyuan, laiyuan);
				convertView.setTag(R.id.tx_pinlun, pinlun);
				convertView.setTag(R.id.tx_time, time);
				convertView.setTag(R.id.img_photo, photo);
				convertView.setTag(R.id.img_type, type);
			}else{
				title = (TextView) convertView.getTag(R.id.tx_news_title);
				laiyuan = (TextView) convertView.getTag(R.id.tx_laiyuan);
				pinlun = (TextView) convertView.getTag(R.id.tx_pinlun);
				time = (TextView) convertView.getTag(R.id.tx_time);
				photo = (ImageView) convertView.getTag(R.id.img_photo);
				type = (ImageView) convertView.getTag(R.id.img_type);
			}
			if(position != 0){
				convertView.findViewById(R.id.view).setVisibility(View.GONE);
			}
			laiyuan.setText(list.get(position).get("laiyuan").toString());
			pinlun.setText(list.get(position).get("pinlun").toString());
			time.setText(list.get(position).get("time").toString());
			title.setText(list.get(position).get("title").toString());
			int lenght = list.get(position).get("title").toString().getBytes().length;
			if(list.get(position).get("tags") != null && Boolean.parseBoolean(list.get(position).get("tags").toString())){
				if(lenght < with*2+1){
					title.setLines(1);
					title.setMaxEms(14);
					title.setText(list.get(position).get("title").toString());
				}else if(lenght > with*2 && list.get(position).get("title").toString().length() > with-2){
					title.setText(list.get(position).get("title").toString().substring(0, with-2)+"...");
				}
				type.setVisibility(View.VISIBLE);
			}
			return convertView;
		}
	}