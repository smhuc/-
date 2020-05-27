package com.wangtian.message;

import com.tencent.android.tpush.XGPushManager;
import com.tencent.android.tpush.service.XGPushService;
import com.wangtian.message.util.SharedPreUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 引导页
 *
 * @author chen
 */
public class FristActivity extends Activity {

    private SharedPreferences sp;
    private String last, shoushi;
    private int count;
    private boolean can = true;
    private boolean show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frist);
        TextView version = (TextView) findViewById(R.id.tx_version);
        ImageView frist = (ImageView) findViewById(R.id.img_frist);
        PackageManager manager = getPackageManager();
        sp = getSharedPreferences("user", Activity.MODE_PRIVATE);
        last = SharedPreUtils.getInstance().getString(Consts.USER_NAME, "");
        count = sp.getInt(last + "count", 0);
        long time = sp.getLong(last + "time", 0);
        long a = System.currentTimeMillis() - time;
        if (a > 3 * 60 * 60 * 1000) {
            count = 0;
            Editor ed = sp.edit();
            ed.putInt(last + "count", 0);
            ed.commit();
        }
        if (last != null) {
            shoushi = SharedPreUtils.getInstance().getString(Consts.USER_HAND, "");
        }
        // 开启logcat输出，方便debug，发布时请关闭
        // XGPushConfig.enableDebug(this, true);
        // 如果需要知道注册是否成功，请使用registerPush(getApplicationContext(), XGIOperateCallback)带callback版本
        // 如果需要绑定账号，请使用registerPush(getApplicationContext(),account)版本
        // 具体可参考详细的开发指南
        // 传递的参数为ApplicationContext
        Context context = getApplicationContext();
        XGPushManager.registerPush(context);

        // 2.36（不包括）之前的版本需要调用以下2行代码
        Intent service = new Intent(context, XGPushService.class);
        context.startService(service);


        // 其它常用的API：
        // 绑定账号（别名）注册：registerPush(context,account)或registerPush(context,account, XGIOperateCallback)，其中account为APP账号，可以为任意字符串（qq、openid或任意第三方），业务方一定要注意终端与后台保持一致。
        // 取消绑定账号（别名）：registerPush(context,"*")，即account="*"为取消绑定，解绑后，该针对该账号的推送将失效
        // 反注册（不再接收消息）：unregisterPush(context)
        // 设置标签：setTag(context, tagName)
        // 删除标签：deleteTag(context, tagName)
		/*if(last != null){
			show = sp.getBoolean(last+"_pop",true);
		}*/
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    if (can) {
                        Intent intent;
                        boolean no_use_hand = SharedPreUtils.getInstance().getBoolean("NO_USE_HAND", false);
                        if (last != null && !TextUtils.isEmpty(shoushi) && !no_use_hand) {
                            intent = new Intent(FristActivity.this, OtherLoginActivity.class);
                            intent.putExtra("shoushi", SharedPreUtils.getInstance().getString(Consts.USER_HAND, ""));
                        } else {
                            intent = new Intent(FristActivity.this, LoginActivity.class);
                        }
                        startActivity(intent);
                        finish();
                    }
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }).start();
        try {
            @SuppressLint("WrongConstant")
            PackageInfo info = manager.getPackageInfo("com.wangtian.message", PackageInfo.CONTENTS_FILE_DESCRIPTOR);
            version.setText("版本号：V" + info.versionName);
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
		/*frist.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				can = false;
				Intent intent; 
				if(last != null && shoushi != null && count < 3 && show){
					intent = new Intent(FristActivity.this,OtherLoginActivity.class);
					intent.putExtra("shoushi", SharedPreUtils.getInstance().getString(Consts.USER_HAND,""));
				}else{
					intent = new Intent(FristActivity.this,LoginActivity.class);
				}
				startActivity(intent);
				finish();
			}
		});*/
    }
}
