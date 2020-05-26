package com.wangtian.message;

import com.wangtian.message.base.BaseActivity;
import com.wangtian.message.util.NewToast;
import com.wangtian.message.util.SharedPreUtils;
import com.wangtian.message.view.GestureLockView;
import com.wangtian.message.view.GestureLockView.OnGestureFinishListener;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 设置手势
 * @author chen
 *
 */
public class SetShouShiActivity extends BaseActivity {

	private GestureLockView lockView;
	private TextView contact;
	private boolean can = false;
	private SharedPreferences sp;
	private String str;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_shoushi);
		setActivity(SetShouShiActivity.this, "手势设置");
		left(2).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(contact.getText().toString().trim().equals("请再次绘制手势图案")){
					str = null;
					contact.setText("请绘制手势图案");
				}else{
					finish();
				}
			}
		});
		init();
	}

	private void init() {
		lockView = (GestureLockView) findViewById(R.id.glv);
		contact = (TextView) findViewById(R.id.tx_contact);
		lockView.setOnGestureFinishListener(new OnGestureFinishListener() {
			
			@Override
			public void OnGestureFinish(String success) {
				if(null !=success && !"".equals(success)){
					if(str != null){
							if(str.equals(success)){
								SharedPreUtils.getInstance().setStringCommit(Consts.USER_HAND,success);
								SharedPreUtils.getInstance().setBoolean("NO_USE_HAND",false);
								finish();
							}else{
								NewToast.makeText(SetShouShiActivity.this, "你两次输入的密码不一致，请重新输入", 1500).show();
								str = null;
								contact.setText("请绘制手势图案");
							}
					}else {
						str = success;
						contact.setText("请再次绘制手势图案");
					}
//					finish();
				}
				
			}
		},2);
	}


	
}
