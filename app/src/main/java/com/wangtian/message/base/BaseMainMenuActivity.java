package com.wangtian.message.base;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.tencent.android.tpush.XGPushManager;
import com.wangtian.message.CollectActivity;
import com.wangtian.message.Consts;
import com.wangtian.message.LoginActivity;
import com.wangtian.message.MyApplication;
import com.wangtian.message.NewsListActivity;
import com.wangtian.message.R;
import com.wangtian.message.SetShouShiActivity;
import com.wangtian.message.SetingMainActivity;
import com.wangtian.message.ShareInfoActivity;
import com.wangtian.message.UserManagerActivity;
import com.wangtian.message.WarnMainActivity;
import com.wangtian.message.sociality.SocialListActivity;
import com.wangtian.message.util.SharedPreUtils;

import java.util.ArrayList;

import static com.wangtian.message.Consts.HAND_NOTICE_SHOW;

public class BaseMainMenuActivity extends FragmentActivity implements OnClickListener{
	private Activity activity;
	private PopupWindow pop;
	public static SlidingMenu menu;
	private SharedPreferences sp;
	private long time = 0;
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				showPop();
				break;

			default:
				break;
			}
		}
		
	};
	private LinearLayout mShareInfoLl;
	private LinearLayout mUserManagerLL;

	/**
	 * 这里是传activity过，便于操作公共的界面
	 *			 必须放在这里面调用的其他方法的前面
	 * @param activity
	 */
	public void setActivity(Activity activity,String title) {
		this.activity = activity;
		MyApplication.getInstance().addActivity(activity);
		TextView text = (TextView) activity.findViewById(R.id.tx_title);
		text.setText(title);
		menu = new SlidingMenu(this);
        menu.setMode(SlidingMenu.LEFT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        menu.setShadowWidthRes(R.dimen.slidingmenu_open);
        menu.setShadowDrawable(R.drawable.shadow);
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        menu.setFadeDegree(0.35f);
        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        menu.setMenu(R.layout.slide_menu);
        sp = getSharedPreferences("user", Activity.MODE_PRIVATE);
        initMenu(menu);
        if(MyApplication.isshow){//判断只进一次
        	String last = sp.getString("last_user", null);
        	boolean show = true;
        	String shoushi = null;
        	if(last != null){
        		show = sp.getBoolean(last+"_pop",true);
        		shoushi = SharedPreUtils.getInstance().getString(Consts.USER_HAND,"");
        	}
 	       MyApplication.isshow = false;
 	       if(!(SharedPreUtils.getInstance().getBoolean(HAND_NOTICE_SHOW,false))){
	        new Thread(new Runnable() {
				
				@Override
				public void run() {
					try {
						Thread.sleep(1000);
						handler.sendEmptyMessage(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}).start();
 	       }
        }
	}
	private LinearLayout hotday,warn,info,collect;
	private ArrayList<LinearLayout> menuList= new ArrayList<LinearLayout>();
	public void initMenu(SlidingMenu menu) {
		String last = sp.getString("last_user", null);
		TextView name = (TextView) menu.findViewById(R.id.tx_name);
		if(last != null){
			name.setText(last);
		}
		TextView exit = (TextView) menu.findViewById(R.id.tx_exit);
		hotday = (LinearLayout) menu.findViewById(R.id.lin_hotday);
		warn = (LinearLayout) menu.findViewById(R.id.lin_warn);
		info = (LinearLayout) menu.findViewById(R.id.lin_info);
		collect = (LinearLayout) menu.findViewById(R.id.lin_collect);
		mShareInfoLl = (LinearLayout) menu.findViewById(R.id.share_info_ll);
		mUserManagerLL = (LinearLayout) menu.findViewById(R.id.user_manager_ll);
		menuList.add(hotday);
		menuList.add(info);
		menuList.add(warn);
		menuList.add(collect);
		menuList.add(mShareInfoLl);
		menuList.add(mUserManagerLL);



		LinearLayout set = (LinearLayout) menu.findViewById(R.id.lin_set);
		SharedPreferences sp = activity.getSharedPreferences("frist", Activity.MODE_PRIVATE);
		int a = sp.getInt("show", 0);
		hotday.setBackground(null);
		warn.setBackground(null);
		info.setBackground(null);
		collect.setBackground(null);
		mShareInfoLl.setBackground(null);

		if (this instanceof NewsListActivity){
			setMenuBg(hotday);
		}
		if (this instanceof SocialListActivity){
			setMenuBg(info);
		}
		if (this instanceof UserManagerActivity){
			setMenuBg(mUserManagerLL);
		}
		if (this instanceof ShareInfoActivity){
			setMenuBg(mShareInfoLl);
		}



		/*switch (a){
			case 1:

				break;
			case 2:
				setMenuBg(warn);
				break;
			case 3:
				setMenuBg(info);
				break;
			case 4:
				setMenuBg(collect);
				break;
			case 5:
				setMenuBg(mShareInfoLl);
				break;
			case 6:
				setMenuBg(mUserManagerLL);
				break;
			default:
				break;
		}*/
		hotday.setOnClickListener(this);
		set.setOnClickListener(this);
		exit.setOnClickListener(this);
		warn.setOnClickListener(this);
		info.setOnClickListener(this);
		collect.setOnClickListener(this);
		mShareInfoLl.setOnClickListener(this);
		mUserManagerLL.setOnClickListener(this);

		if(TextUtils.equals(MyApplication.user.getIsAppManage(),"01")) {
	                      mUserManagerLL.setVisibility(View.VISIBLE);
		}else {
		mUserManagerLL.setVisibility(View.GONE);
		}


	}
	public void setMenuBg(View view){
		for (LinearLayout linearLayout : menuList) {
			if (view.getId()==linearLayout.getId()){
				linearLayout.setBackgroundColor(getResources().getColor(R.color.bg_select));
			}else {
				linearLayout.setBackground(null);
			}
		}
	}


	/**
	 * 在每个模块的要弹出手势设置窗口的整体布局里面的id必须是lin_layout
	 */
	public void showPop() {
		View view = activity.getLayoutInflater().inflate(R.layout.login_pop_window,null);
		pop = new PopupWindow(view, LayoutParams.MATCH_PARENT,  LayoutParams.MATCH_PARENT, true);
		view.findViewById(R.id.tx_never).setOnClickListener(this);
		view.findViewById(R.id.tx_ok).setOnClickListener(this);
		view.findViewById(R.id.tx_cancal).setOnClickListener(this);
	    pop.showAtLocation(this.activity.findViewById(R.id.lin_layout), Gravity.CENTER, 0, 0);
	}
	/**
	 * 这里对应的是标题栏的左边的空间 , 不调用则左边没有控件
	 * @param back 0 是表示  左边没有控件   1表示 是箭头    2  表示 是其他
	 * @return  
	 */
	public View left(int back){
		RelativeLayout rel_back = (RelativeLayout) activity.findViewById(R.id.rel_left);
		ImageView img_back = (ImageView) activity.findViewById(R.id.img_left);
		if(back == 0){
			return null;
		}else if(back == 1){
			img_back.setVisibility(View.VISIBLE);
			rel_back.setVisibility(View.VISIBLE);
			rel_back.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					activity.finish();
				}
			});
			return img_back;
		}else if(back == 2){
			rel_back.setVisibility(View.VISIBLE);
			img_back.setImageResource(R.drawable.nav_menu);
			rel_back.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(menu.isShown()){
						menu.showSecondaryMenu();
					}else{
						menu.showMenu();
					}
				}
			});
			return rel_back;
		}
		return null;
	}
	/**
	 * 这里对应的是标题栏的右边的空间 , 不调用则右边没有控件
	 * @param seach 0 是表示  左边没有控件   1表示 是搜索    2  表示 是其他
	 * @return  
	 */
	public View right(int seach){
		RelativeLayout back = (RelativeLayout) activity.findViewById(R.id.rel_right);
		ImageView img_back = (ImageView) activity.findViewById(R.id.img_right);
		if(seach == 0){
			return null;
		}else if(seach == 1){
			back.setVisibility(View.VISIBLE);
			return back;
		}else if(seach == 2){
			back.setVisibility(View.VISIBLE);
			return img_back;
		}
		return null;
	}
	@Override
	public void onClick(View v) {
		SharedPreferences sp1 = activity.getSharedPreferences("frist", Activity.MODE_PRIVATE);//这个是用于控制菜单的点击的效果的
		Editor ed1 = sp1.edit();
		switch (v.getId()) {
		case R.id.tx_ok:
			Intent intent = new Intent(activity,SetShouShiActivity.class);
			startActivity(intent);
		case R.id.tx_cancal:
			if(pop !=  null && pop.isShowing()){
				pop.dismiss();
			}
			break;
		case R.id.tx_never:
			if(pop !=  null && pop.isShowing()){
				pop.dismiss();
			}
			SharedPreUtils.getInstance().setBoolean(HAND_NOTICE_SHOW,true);
			break;
		case R.id.lin_set:
			intent = new Intent(activity, SetingMainActivity.class);
			startActivity(intent);
			break;
		case R.id.lin_collect:
			setMenuBg(v);
			ed1.putInt("show", 4);
			ed1.commit();
			intent = new Intent(activity, CollectActivity.class);
			startActivity(intent);
			break;
		case R.id.share_info_ll:
			setMenuBg(v);
//			ed1.putInt("show", 5);
//			ed1.commit();
			intent = new Intent(activity, ShareInfoActivity.class);
			startActivity(intent);
			/*v.setBackgroundColor(getResources().getColor(R.color.bg_select));
			*/
			break;
		case R.id.user_manager_ll:
			setMenuBg(v);
//			ed1.putInt("show", 6);
//			ed1.commit();

			intent = new Intent(activity, UserManagerActivity.class);
			startActivity(intent);
			break;
		case R.id.lin_info:
			setMenuBg(v);
//			ed1.putInt("show", 3);
//			ed1.commit();
			v.setBackgroundColor(getResources().getColor(R.color.bg_select));
			intent = new Intent(activity, SocialListActivity.class);
			startActivity(intent);
			break;
		case R.id.lin_warn:
			setMenuBg(v);
			ed1.putInt("show", 2);
			ed1.commit();
			intent = new Intent(activity, WarnMainActivity.class);
			startActivity(intent);
			v.setBackgroundColor(getResources().getColor(R.color.bg_select));
			break;
		case R.id.lin_hotday:
			setMenuBg(v);
//			ed1.putInt("show", 1);
//			ed1.commit();
			v.setBackgroundColor(getResources().getColor(R.color.bg_select));
			intent = new Intent(activity, NewsListActivity.class);
			startActivity(intent);
			break;
		case R.id.tx_exit:
			initpop();
			break;
		case R.id.tx_exit_ok:
			XGPushManager.registerPush(activity,"*");
//			XGPushManager.deleteTag(activity,"group"+MyApplication.user.getGroupid());
			intent = new Intent(activity, LoginActivity.class);
			startActivity(intent);
			SharedPreUtils.getInstance().setStringCommit(Consts.USER_PSD,"");
			SharedPreUtils.getInstance().setStringCommit(Consts.USER_NAME,"");
			SharedPreUtils.getInstance().setStringCommit(Consts.USER_HAND,"");
		{
			SharedPreferences sp = activity.getSharedPreferences("frist", Activity.MODE_PRIVATE);
			 sp.edit().putInt("show", 1).commit();
		}

//		case R.id.tx_cancal:
//			if(pop != null && pop.isShowing()){
//				pop.dismiss();
//			}
			break;
		default:
			break;
		}
	}
	/**
	 * 注销的窗口
	 */
	public void initpop(){
		View view = activity.getLayoutInflater().inflate(R.layout.exit_pop_window,null);
		pop = new PopupWindow(view, LayoutParams.MATCH_PARENT,  LayoutParams.MATCH_PARENT, true);
		view.findViewById(R.id.tx_exit_ok).setOnClickListener(this);
		view.findViewById(R.id.tx_cancal).setOnClickListener(this);

	    pop.showAtLocation(this.activity.findViewById(R.id.lin_layout), Gravity.CENTER, 0, 0);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			if(System.currentTimeMillis() - time < 2000){
				MyApplication.isshow = true;
				MyApplication.getInstance().exit();
			}else{
				time = System.currentTimeMillis();
				Toast.makeText(activity, "连续点击两次退出整个应用", 0).show();
			}
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
	
}
