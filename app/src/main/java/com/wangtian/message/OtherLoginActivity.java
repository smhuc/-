package com.wangtian.message;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;

import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushManager;
import com.wangtian.message.base.BaseActivity;
import com.wangtian.message.bean.User;
import com.wangtian.message.netBean.LoginBean;
import com.wangtian.message.netWork.NetWorkSubscriber;
import com.wangtian.message.netWork.NetWorkUtils;
import com.wangtian.message.sociality.SocialListActivity;
import com.wangtian.message.util.NewToast;
import com.wangtian.message.util.SharedPreUtils;
import com.wangtian.message.view.GestureLockView;
import com.wangtian.message.view.GestureLockView.OnGestureFinishListener;

import java.util.HashMap;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 手势解密
 * @author chen
 *
 */
public class OtherLoginActivity extends BaseActivity{

	private GestureLockView lockView;
	private SharedPreferences sp;
	private int count= 0;
	private String last;
	private ProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_other);
		MyApplication.getInstance().addActivity(OtherLoginActivity.this);
		setActivity(OtherLoginActivity.this, "解锁");
		init();
		dialog = new ProgressDialog(OtherLoginActivity.this);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);
	}
	/**
	 * 登陆的方法
	 * @param name
	 * @param password
	 */
	private void login(final String name,final String password){
		dialog.setMessage("登录中...");
		dialog.show();
		HashMap<String, String> stringStringHashMap = new HashMap<>();
		stringStringHashMap.put("username",name);
		stringStringHashMap.put("password",password);
		stringStringHashMap.put("caCardId","");
		NetWorkUtils.getInstance().getInterfaceService().Login(stringStringHashMap)
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new NetWorkSubscriber<LoginBean>() {
					@Override
					public void onNext(LoginBean loginBean) {
						MyApplication.sLoginBean=loginBean;

						MyApplication.user = new User();
						MyApplication.user.setId(loginBean.getUser().getId());
						MyApplication.user.setIsdel(loginBean.getUser().getIsVisible());
						MyApplication.user.setName(loginBean.getUser().getName());
						MyApplication.user.setPassword(loginBean.getUser().getPassword());
						MyApplication.user.setRemark(loginBean.getUser().getRemark());
						MyApplication.user.setIsAppManage(loginBean.getUser().getIsAppManage());
						if(!TextUtils.equals(MyApplication.user.getId() , sp.getString("id",""))){
							XGPushManager.registerPush(OtherLoginActivity.this,"*");
							/*if(!(MyApplication.user.getGroupid() == sp.getInt("groupid",-1))){
								XGPushManager.deleteTag(OtherLoginActivity.this,getResources().getString(R.string.group)+MyApplication.user.getGroupid());
							}*/
						}
						String main = sp.getString(name+"_main", null);
						XGPushManager.registerPush(OtherLoginActivity.this,getResources().getString(R.string.ids)+MyApplication.user.getId()+"",new XGIOperateCallback() {

							@Override
							public void onSuccess(Object arg0, int arg1) {
//								XGPushManager.setTag(OtherLoginActivity.this, getResources().getString(R.string.group)+MyApplication.user.getGroupid());
								Log.e("user", arg0.toString());
							}

							@Override
							public void onFail(Object arg0, int arg1, String arg2) {
//								Toast.makeText(getBaseContext(), arg1+"   "+arg2, 0).show();
								Log.e("user", arg2);
							}
						});

						SharedPreferences sp = getSharedPreferences("frist", Activity.MODE_PRIVATE);
						int show = sp.getInt("show", 1);
						Intent intent;
						int a=1;
						if(show==5){
							intent = new Intent(OtherLoginActivity.this,ShareInfoActivity.class);
						}else if(show==3){
							intent = new Intent(OtherLoginActivity.this,SocialListActivity.class);
						}else if(show==6){

							if(TextUtils.equals(MyApplication.user.getIsAppManage(),"01")) {
								intent = new Intent(OtherLoginActivity.this,UserManagerActivity.class);
							}else {
								sp.edit().putInt("show", 1).commit();
								intent = new Intent(OtherLoginActivity.this,NewsListActivity.class);
							}
						}else {
							a = 1;
//							intent = new Intent(OtherLoginActivity.this,DayHotActivity.class);
							intent = new Intent(OtherLoginActivity.this,NewsListActivity.class);
						}
						MyApplication.isshow = true;
						startActivity(intent);
						finish();

					}

					@Override
					public void onError(Throwable e) {
						super.onError(e);
					}

					@Override
					public void onCompleted() {
						super.onCompleted();
						dialog.dismiss();
					}
				});













	}
	private void init() {
		sp = getSharedPreferences("user", Activity.MODE_PRIVATE);
		last = sp.getString("last_user", null);
		count = sp.getInt(last+"count", 0);
		lockView = (GestureLockView) findViewById(R.id.glv);
		final String pwds = getIntent().getStringExtra("shoushi");
		findViewById(R.id.tx_tologin).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(OtherLoginActivity.this,LoginActivity.class);
//				intent.putExtra("delect", true);
				startActivity(intent);
			}
		});
		lockView.setOnGestureFinishListener(new OnGestureFinishListener() {
			
			@Override
			public void OnGestureFinish(String success) {
//				if(count < 3){//限制手势输入次数
					if(null !=success && !"".equals(success) && pwds.equals(success)){
						//Intent loginIntent = new Intent(mContext,MainActivity.class);
						//startActivity(loginIntent);
	//					isnotif = false;
//						NewToast.makeText(OtherLoginActivity.this, "解锁成功", NewToast.LENGTH_SHORT).show();
	//					finish();

						login(SharedPreUtils.getInstance().getString(Consts.USER_NAME,""),SharedPreUtils.getInstance().getString(Consts.USER_PSD,""));
					}else{
						NewToast.makeText(OtherLoginActivity.this, "解锁失败", NewToast.LENGTH_SHORT).show();
						/*count+=1;
						Editor ed = sp.edit();
						if(count > 2){
							Intent intent = new Intent(OtherLoginActivity.this,OtherLoginActivity.class);
							startActivity(intent);
							ed.putLong(last+"time", System.currentTimeMillis());
							ed.commit();
						}
						ed.putInt(last+"count", count);
						ed.commit();*/
					}

//				}
/*else{
//					NewToast.makeText(OtherLoginActivity.this, "手势已被锁住，你可切换", 0).show();
					Intent intent = new Intent(OtherLoginActivity.this,OtherLoginActivity.class);
					startActivity(intent);
				}*/
			}
		},2);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			MyApplication.isshow = true;
			MyApplication.getInstance().exit();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
}
