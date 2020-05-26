package com.wangtian.message;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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

public class AddShareActivity extends Activity {

    public static final String ADD_SHARE ="ADD_SHARE";
    @BindView(R.id.rel_left)
    RelativeLayout mRelLeft;
    @BindView(R.id.tx_title)
    TextView mTxTitle;
    @BindView(R.id.tx_right)
    TextView mTxRight;
    @BindView(R.id.rel_right)
    RelativeLayout mRelRight;
    @BindView(R.id.remark_ed)
    EditText mRemarkEd;
    @BindView(R.id.title_ed)
    EditText mTitleEd;
    @BindView(R.id.share_url_ed)
    EditText mShareUrlEd;
    private ProgressDialog dialog;

    public static void start(Context context){
        Intent intent = new Intent(context, AddShareActivity.class);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_share);
        ButterKnife.bind(this);
        initData();
    }
    public static boolean isHttpUrl(String urls) {
        boolean isurl = false;
        String regex = "[a-zA-z]+://[^\\s]*";//设置正则表达式

        Pattern pat = Pattern.compile(regex.trim());//对比
        Matcher mat = pat.matcher(urls.trim());
        isurl = mat.matches();//判断是否匹配
        if (isurl) {
            isurl = true;
        }
        return isurl;
    }



    private void initData() {
        dialog = new ProgressDialog(AddShareActivity.this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        mRelLeft.setOnClickListener(v->finish());
        mRelRight.setOnClickListener(v->{
            String title = mTitleEd.getText().toString().trim();
            String url = mShareUrlEd.getText().toString().trim();
            if (TextUtils.isEmpty(title)){
                Toast.makeText(this, "请输入分享标题", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(url)){
                Toast.makeText(this, "将完整的URL粘贴在此处", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!isHttpUrl(url)){
                Toast.makeText(this, "将正确的的URL粘贴在此处", Toast.LENGTH_SHORT).show();
                return;
            }


            dialog.setMessage("加载中");
            dialog.show();
            HashMap<String, String> map = new HashMap<>();
            map.put("VC_USERID",MyApplication.sLoginBean.getUser().getId());
            map.put("token",MyApplication.sLoginBean.getToken());
            map.put("VC_TITLE",title);
            map.put("VC_URL",url);
            map.put("VC_REMARKS",mRemarkEd.getText().toString().trim());
            map.put("DT_SHAREDATE", String.valueOf(System.currentTimeMillis()));
            NetWorkUtils.getInstance().getInterfaceService().addShare(map)
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
                            EventBus.getDefault().post(ADD_SHARE);
                            AddShareActivity.this.finish();
                        }
                    });



        });


    }
    @Override
    protected void onDestroy() {
        if (dialog!=null&&dialog.isShowing()){
            dialog.dismiss();
        }
        super.onDestroy();
    }

}
