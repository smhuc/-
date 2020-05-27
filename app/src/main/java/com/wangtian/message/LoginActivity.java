package com.wangtian.message;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Random;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 登陆页
 *
 * @author chen
 */
public class LoginActivity extends BaseActivity implements OnClickListener {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private ImageView view, view2;
    private StringBuffer yan;
    private TextView text;
    private EditText name, psd, yanzhen;
    private SharedPreferences sp;
    private int count = 0;
    private long count_time;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setActivity(LoginActivity.this, "互联网信息监测移动应用");
        name = (EditText) findViewById(R.id.edit);
        view = (ImageView) findViewById(R.id.img_cancal);
        view.setOnClickListener(this);
        name.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s != null) {
                    view.setVisibility(View.VISIBLE);
                } else {
                    view.setVisibility(View.GONE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        dialog = new ProgressDialog(LoginActivity.this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        psd = (EditText) findViewById(R.id.edit_password);
        view2 = (ImageView) findViewById(R.id.img_password_cancal);
        view2.setOnClickListener(this);
        psd.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s != null) {
                    view2.setVisibility(View.VISIBLE);
                } else {
                    view2.setVisibility(View.GONE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        yanzhen = (EditText) findViewById(R.id.edit_yanzheng);
        text = (TextView) findViewById(R.id.img_yanzheng_cancal);
        text.setOnClickListener(this);
        onClick(text);
        findViewById(R.id.tx_login).setOnClickListener(this);


//		yanzhen.setText(yan.toString());

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_yanzheng_cancal://切换验证码
                String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
                Random random = new Random();
                yan = new StringBuffer();
                for (int i = 0; i < 4; i++) {
                    int number = random.nextInt(62);
                    yan.append(str.charAt(number));
                }
                text.setText(yan.toString());
                break;
            case R.id.tx_login:
                if (name != null && name.getText() != null && name.getText().length() > 0) {
                    if (psd.getText() != null && psd.getText().length() > 0) {
                        if (yanzhen.getText() != null && yanzhen.getText().length() > 0) {
                            if (yanzhen.getText().toString().trim().equalsIgnoreCase(yan.toString())) {
                                sp = getSharedPreferences("user", Activity.MODE_PRIVATE);
                                count = sp.getInt(name.getText().toString() + "count_psd", 0);

                                Editor ed = sp.edit();
                                if (count < 3) {//限制输入错误的次数
                                    login(name.getText().toString(), psd.getText().toString());
                                } else {
                                    count_time = sp.getLong(name.getText().toString() + "count_time", 0);
                                    if (System.currentTimeMillis() - count_time > 60 * 60 * 1000) {
                                        count = 0;
                                        login(name.getText().toString(), psd.getText().toString());
                                    } else {
                                        NewToast.makeText(LoginActivity.this, "该账号密码输入错误三次，请一小时以后再试", Toast.LENGTH_LONG).show();

                                    }
                                }
                            } else {
                                NewToast.makeText(LoginActivity.this, "您输入的验证码有误", Toast.LENGTH_LONG).show();
                                String str1 = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
                                Random random1 = new Random();
                                yan = new StringBuffer();
                                for (int i = 0; i < 4; i++) {
                                    int number = random1.nextInt(62);
                                    yan.append(str1.charAt(number));
                                }
                                text.setText(yan.toString());
                            }
                        } else {
//						NewToast.makeText(LoginActivity.this,"验证码不能为空",0).show();
                            NewToast.makeText(LoginActivity.this, "验证码不能为空", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        NewToast.makeText(LoginActivity.this, "密码不能为空", Toast.LENGTH_LONG).show();
                    }
                } else {
                    NewToast.makeText(LoginActivity.this, "账号不能为空", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.img_cancal:
                name.setText("");
                break;
            case R.id.img_password_cancal:
                psd.setText("");
                break;

            default:
                break;
        }
    }

    /**
     * 登陆的方法
     *
     * @param name
     * @param password
     */
    private void login(final String name, final String password) {
        dialog.setMessage("登录中...");
        dialog.show();
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("username", name);
        stringStringHashMap.put("password", password);
        stringStringHashMap.put("caCardId", "");

        NetWorkUtils.getInstance().getInterfaceService().Login(stringStringHashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetWorkSubscriber<LoginBean>() {
                    @Override
                    public void onNext(LoginBean loginBean) {
                        MyApplication.sLoginBean = loginBean;

                        Editor ed = sp.edit();
                        MyApplication.user = new User();
                        MyApplication.user.setId(loginBean.getUser().getId());
                        MyApplication.user.setIsdel(loginBean.getUser().getIsVisible());
                        MyApplication.user.setName(loginBean.getUser().getName());
                        MyApplication.user.setPassword(loginBean.getUser().getPassword());
                        MyApplication.user.setRemark(loginBean.getUser().getRemark());
                        MyApplication.user.setIsAppManage(loginBean.getUser().getIsAppManage());
                        if (!TextUtils.equals(MyApplication.user.getId(), sp.getString("id", ""))) {
                            XGPushManager.registerPush(LoginActivity.this, "*");
							/*if(!(MyApplication.user.getGroupid() == sp.getInt("groupid",-1))){
								XGPushManager.deleteTag(LoginActivity.this,getResources().getString(R.string.group)+MyApplication.user.getGroupid());
							}*/
                        }
                        ed.putString("last_user", name);
                        if (sp.getString(name, null) == null) {
                            ed.putString(name + "_psd", psd.getText().toString());
                        }
                        ed.putString("password", password);
//						ed.putInt("groupid", MyApplication.user.getGroupid());/**/
                        ed.putString("id", MyApplication.user.getId());
                        ed.putString("isdel", MyApplication.user.getIsdel());
                        ed.putString("name", MyApplication.user.getName());
                        ed.putString("password", MyApplication.user.getPassword());
                        ed.putString("remark", MyApplication.user.getRemark());
                        ed.commit();
                        String main = sp.getString(name + "_main", null);
                        XGPushManager.registerPush(LoginActivity.this, getResources().getString(R.string.ids) + MyApplication.user.getId() + "", new XGIOperateCallback() {

                            @Override
                            public void onSuccess(Object arg0, int arg1) {
//								XGPushManager.setTag(LoginActivity.this, getResources().getString(R.string.group)+MyApplication.user.getGroupid());
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
                        int a = 1;
                        if (show == 5) {
                            intent = new Intent(LoginActivity.this, ShareInfoActivity.class);
                        } else if (show == 3) {
                            intent = new Intent(LoginActivity.this, SocialListActivity.class);
                        } else if (show == 6) {
                            if (TextUtils.equals(MyApplication.user.getIsAppManage(), "01")) {
                                intent = new Intent(LoginActivity.this, UserManagerActivity.class);
                            } else {
                                sp.edit().putInt("show", 1).commit();
                                intent = new Intent(LoginActivity.this, NewsListActivity.class);
                            }


                        } else {
                            a = 1;
//							intent = new Intent(LoginActivity.this,DayHotActivity.class);
                            intent = new Intent(LoginActivity.this, NewsListActivity.class);
                        }
                        SharedPreUtils.getInstance().setStringCommit(Consts.USER_NAME, name);
                        SharedPreUtils.getInstance().setStringCommit(Consts.USER_PSD, password);


                        Editor ed1 = sp.edit();
                        ed1.putInt("show", a);
                        ed.putInt(name + "count_psd", 0);
                        ed.commit();
                        MyApplication.isshow = true;
                        startActivity(intent);
                        finish();

                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        yanzhen.setText("");
                        Editor ed = sp.edit();
                        count += 1;
                        ed.putInt(name + "count_psd", count);
                        if (count > 4) {
                            ed.putLong(name + "count_time", System.currentTimeMillis());
                        }
                        ed.commit();
                        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
                        Random random = new Random();
                        yan = new StringBuffer();
                        for (int i = 0; i < 4; i++) {
                            int number = random.nextInt(62);
                            yan.append(str.charAt(number));
                        }
                        text.setText(yan.toString());
                    }

                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                    }
                });


    }

    public static String doJsonPost(String urlPath, String json) throws IOException {
        String result = "";
        BufferedReader reader = null;
        try {
            URL url = new URL(urlPath);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setConnectTimeout(60 * 1000);
            conn.setReadTimeout(60 * 1000);
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Charset", "UTF-8");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setRequestProperty("accept", "application/json");
            if (json != null /*&& !TextUtils.isEmpty(json)*/) {
                byte[] writebytes = json.getBytes();
                conn.setRequestProperty("Content-Length", String.valueOf(writebytes.length));
                OutputStream outwritestream = conn.getOutputStream();
                outwritestream.write(json.getBytes());
                outwritestream.flush();
                outwritestream.close();
            }
            if (conn.getResponseCode() == 200) {
                reader = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()));
                result = reader.readLine();
            }
        } catch (IOException e) {
            throw e;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Log.d("LoginActivity", "doJsonPost--> urlPath=" + urlPath + "\n   result=" + result);
        return result;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
//				if(getIntent().getBooleanExtra("delect", false)){
//					finish();
//				}else{
            MyApplication.isshow = true;
            MyApplication.getInstance().exit();
            return false;
//				}
        }
        return super.onKeyDown(keyCode, event);
    }
}
