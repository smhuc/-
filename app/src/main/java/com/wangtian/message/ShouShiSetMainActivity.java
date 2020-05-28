package com.wangtian.message;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.wangtian.message.base.BaseMenuActivity;
import com.wangtian.message.util.SharedPreUtils;

/**
 * 设置--设置手势
 *
 * @author chen
 */
public class ShouShiSetMainActivity extends BaseMenuActivity {

    private LinearLayout lin_all;
    private PopupWindow pop;
    private boolean can;
    private SharedPreferences sp;
    private String last;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setshoushi_main);
        setActivity(ShouShiSetMainActivity.this, "手势设置");
        left(1);

    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    private void init() {
        String string = SharedPreUtils.getInstance().getString(Consts.USER_HAND, "");
        can = SharedPreUtils.getInstance().getBoolean("NO_USE_HAND", false);
        String shoushi = SharedPreUtils.getInstance().getString(Consts.USER_HAND, "");
        CheckBox box = (CheckBox) findViewById(R.id.check_showall);
        if (!can && !TextUtils.isEmpty(shoushi)) {
            box.setChecked(true);
        } else {
            box.setChecked(false);
        }
        lin_all = (LinearLayout) findViewById(R.id.lin_all);
        findViewById(R.id.lin_update).setOnClickListener((View v) -> {
//				initpop();
            Intent intent = new Intent(ShouShiSetMainActivity.this, PasswordActivity.class);
            startActivity(intent);
        });
//		GestureLockView lvg = (GestureLockView) findViewById(R.id.glv);
        box.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//				Editor ed = sp.edit();
                if (isChecked) {
                    SharedPreUtils.getInstance().setBoolean("NO_USE_HAND", false);
//					lin_all.setVisibility(View.VISIBLE);
                    String string = SharedPreUtils.getInstance().getString(Consts.USER_HAND, "");
                    if (TextUtils.isEmpty(string)) {
                        Intent intent = new Intent(ShouShiSetMainActivity.this, SetShouShiActivity.class);
                        startActivity(intent);
                    }
                } else {
                    SharedPreUtils.getInstance().setBoolean("NO_USE_HAND", true);
                    lin_all.setVisibility(View.GONE);
                    SharedPreUtils.getInstance().setString(Consts.USER_HAND, "");
                }
//				ed.putBoolean(last+"_pop", isChecked);
//				ed.commit();
            }
        });
        if (box.isChecked()) {
            lin_all.setVisibility(View.VISIBLE);
        } else {
            lin_all.setVisibility(View.GONE);
        }
    }

}
