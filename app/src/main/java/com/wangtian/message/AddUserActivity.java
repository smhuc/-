package com.wangtian.message;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wangtian.message.netBean.UserListBean;
import com.wangtian.message.netWork.NetWorkSubscriber;
import com.wangtian.message.netWork.NetWorkUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AddUserActivity extends Activity {

    @BindView(R.id.img_left)
    ImageView mImgLeft;
    @BindView(R.id.rel_left)
    RelativeLayout mRelLeft;
    @BindView(R.id.tx_title)
    TextView mTxTitle;
    @BindView(R.id.img_right)
    ImageView mImgRight;
    @BindView(R.id.tx_right)
    TextView mTxRight;
    @BindView(R.id.rel_right)
    RelativeLayout mRelRight;
    @BindView(R.id.user_name_point_tv)
    TextView mUserNamePointTv;
    @BindView(R.id.user_name_et)
    EditText mUserNameEt;
    @BindView(R.id.password_point_tv)
    TextView mPasswordPointTv;
    @BindView(R.id.password_et)
    EditText mPasswordEt;
    @BindView(R.id.name_point_tv)
    TextView mNamePointTv;
    @BindView(R.id.name_et)
    EditText mNameEt;
    @BindView(R.id.card_num_et)
    EditText mCardNumEt;
    @BindView(R.id.email_et)
    EditText mEmailEt;
    @BindView(R.id.phone_et)
    EditText mPhoneEt;
    @BindView(R.id.user_del_tv)
    TextView mUserDelTv;
    @BindView(R.id.user_pass_rl)
    RelativeLayout user_pass_rl;
    private ProgressDialog dialog;

    public static void start(Context context, boolean type) {
        Intent intent = new Intent(context, AddUserActivity.class);
        intent.putExtra(TYPE, type);
        context.startActivity(intent);
    }

    public static void start(Context context, boolean type, UserListBean bean) {
        Intent intent = new Intent(context, AddUserActivity.class);
        intent.putExtra(TYPE, type);
        intent.putExtra(USER_INFO, bean);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        ButterKnife.bind(this);

        initData();

    }

    public static final String TYPE = "type";
    public static final String USER_INFO = "USER_INFO";

    private void initData() {
        mRelLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddUserActivity.this.finish();
            }
        });
        mRelLeft.setVisibility(View.VISIBLE);
        boolean type = getIntent().getBooleanExtra(TYPE, false);
        dialog = new ProgressDialog(AddUserActivity.this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        if (type) {

            UserListBean userListBean = (UserListBean) getIntent().getSerializableExtra(USER_INFO);
            mUserDelTv.setVisibility(View.GONE);
            mUserNameEt.setText(userListBean.getUsername());
            mPasswordEt.setText(userListBean.getPassword());
            mNameEt.setText(userListBean.getName());
            mCardNumEt.setText(userListBean.getIdCard());
            mEmailEt.setText(userListBean.getEmail());
            mPhoneEt.setText(userListBean.getOfficePhone());

            mUserNamePointTv.setVisibility(View.GONE);
            mPasswordPointTv.setVisibility(View.GONE);
            mNamePointTv.setVisibility(View.GONE);

            setEditText(mUserNameEt);
            setEditText(mPasswordEt);
            setEditText(mNameEt);
            setEditText(mCardNumEt);
            setEditText(mEmailEt);
            setEditText(mPhoneEt);

            mTxTitle.setText("成员信息");
            mRelRight.setVisibility(View.VISIBLE);
            mTxRight.setVisibility(View.VISIBLE);
            mImgRight.setVisibility(View.GONE);
            mTxRight.setText("完成");
            user_pass_rl.setVisibility(View.GONE);
            mRelRight.setOnClickListener(view -> AddUserActivity.this.finish());
            mUserDelTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.setMessage("删除中");
                    dialog.show();
                    HashMap<String, String> map = new HashMap<>();
                    map.put("userId",userListBean.getId());
                    map.put("token",MyApplication.sLoginBean.getToken());
                    NetWorkUtils.getInstance().getInterfaceService().delUser(map)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new NetWorkSubscriber<Object>() {
                                @Override
                                public void onError(Throwable e) {
                                    super.onError(e);
                                    dialog.dismiss();
                                }

                                @Override
                                public void onNext(Object o) {
                                    dialog.dismiss();
                                        AddUserActivity.this.finish();
                                }
                            });
                }
            });

        } else {
            mTxTitle.setText("添加成员");
            mRelRight.setVisibility(View.VISIBLE);
            mTxRight.setVisibility(View.VISIBLE);
            mImgRight.setVisibility(View.GONE);
            mTxRight.setText("完成");
            mRelRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String userName = mUserNameEt.getText().toString().trim();
                    if (TextUtils.isEmpty(userName)){
                        Toast.makeText(AddUserActivity.this, "请填写用户名", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String password = mPasswordEt.getText().toString().trim();
                    if (TextUtils.isEmpty(password)){
                        Toast.makeText(AddUserActivity.this, "请填写密码", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!checkUserName(userName)){
                        Toast.makeText(AddUserActivity.this, "用户名只能为字母、数字下划线，且以字母开头", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String name= mNameEt.getText().toString().trim();
                    if (TextUtils.isEmpty(name)){
                        Toast.makeText(AddUserActivity.this, "请填写姓名", Toast.LENGTH_SHORT).show();
                    }
                    dialog.setMessage("添加中");
                    dialog.show();
                    HashMap<String, String> map = new HashMap<>();
                    map.put("token",MyApplication.sLoginBean.getToken());
                    map.put("username",userName);
                    map.put("password",password);
                    map.put("name",name);
                    map.put("idCard",mCardNumEt.getText().toString().trim());
                    map.put("email",mEmailEt.getText().toString().trim());
                    map.put("officePhone",mPhoneEt.getText().toString().trim());
                    NetWorkUtils.getInstance().getInterfaceService().addUser(map)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new NetWorkSubscriber<Object>() {
                                @Override
                                public void onError(Throwable e) {
                                    super.onError(e);
                                    dialog.dismiss();
                                }

                                @Override
                                public void onNext(Object o) {
                                    dialog.dismiss();
                                    EventBus.getDefault().post("ADD_USER");
                                    AddUserActivity.this.finish();
                                }
                            });
                }
            });


        }


    }

    @Override
    protected void onDestroy() {
        if (dialog!=null&&dialog.isShowing()){
            dialog.dismiss();
        }
            super.onDestroy();
    }

    private void setEditText(EditText editText) {
        if (TextUtils.isEmpty(editText.getText().toString())||TextUtils.equals("null",editText.getText().toString())){
            editText.setText("");
        }
        editText.setFocusable(false);
        editText.setHint("");
        editText.setFocusableInTouchMode(false);
    }

    public static boolean checkUserName(String name) {
        boolean isurl = false;
        String regex = "[a-zA-Z]{1}[a-zA-Z0-9_]{1,100}";//设置正则表达式

        Pattern pat = Pattern.compile(regex.trim());//对比
        Matcher mat = pat.matcher(name.trim());
        isurl = mat.matches();//判断是否匹配
        if (isurl) {
            isurl = true;
        }
        return isurl;
    }

}
