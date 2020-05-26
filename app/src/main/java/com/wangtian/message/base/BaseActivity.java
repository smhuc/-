package com.wangtian.message.base;

import com.wangtian.message.MyApplication;
import com.wangtian.message.R;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BaseActivity extends Activity{
	private Activity activity;

	/**
	 * 这里是传activity过，便于操作公共的界面
	 *			 必须放在这里面调用的其他方法的前面
	 * @param activity
	 */
	public void setActivity(Activity activity,String title) {
		MyApplication.getInstance().addActivity(activity);
		this.activity = activity;
		TextView text = (TextView) activity.findViewById(R.id.tx_title);
		text.setText(title);
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
			return img_back;
		}
		return null;
	}
	/**
	 * 这里对应的是标题栏的右边的空间 , 不调用则右边没有控件
	 * @param seach 0 是表示  左边没有控件   1表示 是箭头    2  表示 是其他
	 * @return  
	 */
	public View right(int seach){
		RelativeLayout back = (RelativeLayout) activity.findViewById(R.id.rel_right);
		ImageView img_back = (ImageView) activity.findViewById(R.id.img_right);
		if(seach == 0){
			return null;
		}else if(seach == 1){
			back.setVisibility(View.VISIBLE);
			return img_back;
		}else if(seach == 2){
			back.setVisibility(View.VISIBLE);
			return img_back;
		}
		return null;
	}
}
