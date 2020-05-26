package com.wangtian.message;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.wangtian.message.base.BaseActivity;
import com.wangtian.message.util.NewToast;
import com.wangtian.message.util.SharedPreUtils;

public class PasswordActivity extends BaseActivity {
	private EditText psd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_psd);
		setActivity(PasswordActivity.this, "验证密码");
		left(1);
		psd = (EditText) findViewById(R.id.edit_password);
		findViewById(R.id.tx_login).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SharedPreferences sp = getSharedPreferences("user", Activity.MODE_PRIVATE);
				String psw = SharedPreUtils.getInstance().getString(Consts.USER_PSD,"");
				if(psd.getText().toString().equals(psw)){
					Intent intent = new Intent(PasswordActivity.this,SetShouShiActivity.class);
					startActivity(intent);
					finish();
				}else{
					NewToast.makeText(PasswordActivity.this, "您的密码输入有误，不能修改", 1500).show();
				}
			}
		});
	}

	
}
