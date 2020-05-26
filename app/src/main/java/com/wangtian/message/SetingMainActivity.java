package com.wangtian.message;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.wangtian.message.base.BaseMenuActivity;
import com.wangtian.message.sociality.SocialListActivity;
import com.wangtian.message.util.NewToast;

import java.io.File;
import java.io.IOException;
/**
 * 设置
 * @author chen
 *
 */
public class SetingMainActivity extends BaseMenuActivity implements OnClickListener{

	private TextView main;
	private ImageView news;
	private SharedPreferences sp;
	private PopupWindow pop;
	private boolean has = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_seting);
		setActivity(SetingMainActivity.this, "设置");
		left(1);
		init();
	}

	private void init() {
		main = (TextView) findViewById(R.id.tx_main);
		sp = getSharedPreferences("user", Activity.MODE_PRIVATE);
		String last = sp.getString("last_user", null);
		String mains = null;
//		setMainName();
		findViewById(R.id.lin_aboutme).setOnClickListener(this);
		findViewById(R.id.lin_setmain).setOnClickListener(this);
		findViewById(R.id.lin_setshoushi).setOnClickListener(this);
		findViewById(R.id.lin_update).setOnClickListener(this);

		news = (ImageView) findViewById(R.id.img_new);
		
	}

	@Override
	protected void onResume() {
		super.onResume();
		setMainName();
	}

	private void setMainName() {
		String mains;SharedPreferences sp = getSharedPreferences("frist", Activity.MODE_PRIVATE);
		int show = sp.getInt("show", 1);
		Intent intent;
		if(show==5){
			mains="资讯分享";
		}else if(show==3){
			mains="社    交";
		}else if(show==6){
			mains="用户管理";
		}else {
			mains="今日热点";
		}
		main.setText(mains);
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.lin_aboutme:
			intent = new Intent(SetingMainActivity.this,AboutMeActivity.class);
			startActivity(intent);
			break;
		case R.id.lin_setmain:
			intent = new Intent(SetingMainActivity.this,MainSetActivity.class);
			intent.putExtra("main", main.getText().toString().trim());
			startActivityForResult(intent, 1000);
			break;
		case R.id.lin_setshoushi:
			intent = new Intent(SetingMainActivity.this,ShouShiSetMainActivity.class);
			startActivity(intent);
			break;
		case R.id.lin_update:
			initPop();
			break;
		default:
			break;
		}
		super.onClick(v);
	}

	private void initPop() {
		if(has){
			View view = getLayoutInflater().inflate(R.layout.login_pop_window, null);
			view.findViewById(R.id.tx_never).setVisibility(View.GONE);
			TextView textView = (TextView) view.findViewById(R.id.tx_title);
			textView.setText("有最新的版本，是否更新？");
			view.findViewById(R.id.tx_cancal).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(pop != null && pop.isShowing()){
						pop.dismiss();
					}
				}
			});
			view.findViewById(R.id.tx_ok).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(pop != null && pop.isShowing()){
						pop.dismiss();
					}
				}
			});
			pop = new PopupWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);
			pop.showAtLocation(findViewById(R.id.lin_layout), Gravity.CENTER, 0, 0);
		}else{
			NewToast.makeText(SetingMainActivity.this, "您的是最新版本！", 0).show();
		}
	}
	private void down(String downUrl){
		Intent i = new Intent(Intent.ACTION_VIEW); 
		String filePath = "/sdcard/Message.apk"; 
		File file = new File(filePath);
		if(!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		i.setDataAndType(Uri.parse("file://" + filePath),"application/vnd.android.package-archive"); 
		startActivity(i);

	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == 1000 && resultCode == 1000){
			main.setText(data.getStringExtra("main"));
		}
	}

	
}
