package com.wangtian.message;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.wangtian.message.base.BaseMenuActivity;

/**
 * 设置默认主页
 * @author chen
 *
 */
public class MainSetActivity extends BaseMenuActivity{

	private TextView frist,two,three,four;
	private ImageView ifrist,itwo,ithree,ifour;
	private String main,last;
	private SharedPreferences sp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setmain);
		setActivity(MainSetActivity.this, "默认主页设置");
		init();
		left(1).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainSetActivity.this, SetingMainActivity.class);
				String main_temp = sp.getString(last+"_main", null);
				if(main_temp != null && main_temp.equals(main)){
					intent.putExtra("main", main);
				}else{
					intent.putExtra("main", main_temp);
				}
				setResult(1000, intent);
				finish();
			}
		});
	}




	private void init() {
		sp = getSharedPreferences("user", Activity.MODE_PRIVATE);
		last = sp.getString("last_user", null);
		main = getIntent().getStringExtra("main");
		findViewById(R.id.lin_frist).setOnClickListener(this);
		frist = (TextView) findViewById(R.id.tx_frist);
		ifrist = (ImageView) findViewById(R.id.img_frist);
		findViewById(R.id.lin_two).setOnClickListener(this);
		two = (TextView) findViewById(R.id.tx_two);
		itwo = (ImageView) findViewById(R.id.img_two);
		findViewById(R.id.lin_three).setOnClickListener(this);
		three = (TextView) findViewById(R.id.tx_three);
		ithree = (ImageView) findViewById(R.id.img_three);
		findViewById(R.id.lin_four).setOnClickListener(this);
		four = (TextView) findViewById(R.id.tx_four);
		ifour = (ImageView) findViewById(R.id.img_four);
		SharedPreferences sp1 = getSharedPreferences("frist", Activity.MODE_PRIVATE);//这个是用于控制菜单的点击的效果的
		int show = sp1.getInt("show", 1);
		if(6==show){
			ifour.setVisibility(View.VISIBLE);
			four.setTextColor(getResources().getColor(R.color.text_red));
		}else if(3==show){
			ithree.setVisibility(View.VISIBLE);
			three.setTextColor(getResources().getColor(R.color.text_red));
		}else if(show==5){
			itwo.setVisibility(View.VISIBLE);
			two.setTextColor(getResources().getColor(R.color.text_red));
		}else{
			ifrist.setVisibility(View.VISIBLE);
			frist.setTextColor(getResources().getColor(R.color.text_red));
		}


		if(TextUtils.equals(MyApplication.user.getIsAppManage(),"01")) {
			findViewById(R.id.lin_four).setVisibility(View.VISIBLE);
		}else {
			findViewById(R.id.lin_four).setVisibility(View.GONE);
		}

	}

	@Override
	public void onClick(View v) {
		SharedPreferences sp = getSharedPreferences("frist", Activity.MODE_PRIVATE);
		Editor ed = sp.edit();
/*

		int show = sp.getInt("show", 1);
		Intent intent;
		int a=1;
		if(show==5){
			intent = new Intent(OtherLoginActivity.this,ShareInfoActivity.class);
		}else if(show==3){
			intent = new Intent(OtherLoginActivity.this,SocialListActivity.class);
		}else if(show==6){
			intent = new Intent(OtherLoginActivity.this,UserManagerActivity.class);
		}else {
			a = 1;
//							intent = new Intent(OtherLoginActivity.this,DayHotActivity.class);
			intent = new Intent(OtherLoginActivity.this,NewsListActivity.class);
		}
		*/



		switch (v.getId()) {
		case R.id.lin_frist:
			main = "今日热点";
			ed.putInt("show", 1).commit();
			if(last != null){
			frist.setTextColor(getResources().getColor(R.color.text_red));
			ifrist.setVisibility(View.VISIBLE);
			two.setTextColor(getResources().getColor(R.color.text_black));
			itwo.setVisibility(View.GONE);
			three.setTextColor(getResources().getColor(R.color.text_black));
			ithree.setVisibility(View.GONE);
			four.setTextColor(getResources().getColor(R.color.text_black));
			ifour.setVisibility(View.GONE);
			}
			break;
		case R.id.lin_two:
			main = "资讯分享";
			if(last != null){
				ed.putInt("show", 5).commit();
			two.setTextColor(getResources().getColor(R.color.text_red));
			itwo.setVisibility(View.VISIBLE);
			frist.setTextColor(getResources().getColor(R.color.text_black));
			ifrist.setVisibility(View.GONE);
			three.setTextColor(getResources().getColor(R.color.text_black));
			ithree.setVisibility(View.GONE);
			four.setTextColor(getResources().getColor(R.color.text_black));
			ifour.setVisibility(View.GONE);
			}
			break;
		case R.id.lin_three:
			main = "社交";
			if(last != null){
				ed.putInt("show", 3).commit();
			three.setTextColor(getResources().getColor(R.color.text_red));
			ithree.setVisibility(View.VISIBLE);
			frist.setTextColor(getResources().getColor(R.color.text_black));
			ifrist.setVisibility(View.GONE);
			two.setTextColor(getResources().getColor(R.color.text_black));
			itwo.setVisibility(View.GONE);
			four.setTextColor(getResources().getColor(R.color.text_black));
			ifour.setVisibility(View.GONE);
			}
			break;
		case R.id.lin_four:
			main = "用户管理";
			if(last != null){
				ed.putInt("show", 6).commit();
			four.setTextColor(getResources().getColor(R.color.text_red));
			ifour.setVisibility(View.VISIBLE);
			frist.setTextColor(getResources().getColor(R.color.text_black));
			ifrist.setVisibility(View.GONE);
			two.setTextColor(getResources().getColor(R.color.text_black));
			itwo.setVisibility(View.GONE);
			three.setTextColor(getResources().getColor(R.color.text_black));
			ithree.setVisibility(View.GONE);
			}
			break;

		default:
			break;
		}
//		if(last != null){
//			Editor ed = sp.edit();
//			ed.putString(last+"_main", main);
//			ed.commit();
//		}
		super.onClick(v);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			Intent intent = new Intent(MainSetActivity.this, SetingMainActivity.class);
			String main_temp = sp.getString(last+"_main", null);
			if(main_temp != null && main_temp.equals(main)){
				intent.putExtra("main", main);
			}else{
				intent.putExtra("main", main_temp);
			}
			setResult(1000, intent);
			finish();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	
}
