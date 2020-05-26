package com.wangtian.message;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wangtian.message.adapter.UserManagerAdapter;
import com.wangtian.message.base.BaseMainMenuActivity;
import com.wangtian.message.netBean.UserListBean;
import com.wangtian.message.netWork.NetWorkSubscriber;
import com.wangtian.message.netWork.NetWorkUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class UserManagerActivity extends BaseMainMenuActivity {

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
    @BindView(R.id.user_lv)
    ListView mUserLv;
    private UserManagerAdapter mUserManagerAdapter;

    private int page=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_manager);
        ButterKnife.bind(this);
        setActivity(UserManagerActivity.this, "我的组织");
        EventBus.getDefault().register(this);
        left(2).setOnClickListener(v -> menu.toggle(menu.isShown()));
        initData();
        getNetInfo();

    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void messaAction(String event){
        if (TextUtils.equals("ADD_USER",event)){
             page=1;
            getNetInfo();
        }
    }


    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private void getNetInfo() {
        HashMap<String, String> param = new HashMap<>();
        param.put("token", MyApplication.sLoginBean.getToken());
        param.put("page", String.valueOf(page));
        param.put("rowsPerPage", String.valueOf(20));


        NetWorkUtils.getInstance().getInterfaceService().getUserList(param)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetWorkSubscriber<ArrayList<UserListBean>>() {
                    @Override
                    public void onNext(ArrayList<UserListBean> userListBeans) {
                        if (userListBeans!=null&&userListBeans.size()>0){
                            if (page==1){
                                mUserManagerAdapter.setData(userListBeans);
                            }else {
                                mUserManagerAdapter.addData(userListBeans);
                            }
                            page++;

                        }
                    }
                });


    }

    private void initData() {
       /* mRelLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserManagerActivity.this.finish();
            }
        });
        mRelLeft.setVisibility(View.VISIBLE);*/

        mRelRight.setVisibility(View.VISIBLE);
        mTxRight.setVisibility(View.VISIBLE);
        mImgRight.setVisibility(View.GONE);
        mTxRight.setText("添加");
        mRelRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               AddUserActivity.start(UserManagerActivity.this,false);

            }
        });
        mUserManagerAdapter = new UserManagerAdapter();
        mUserLv.setAdapter(mUserManagerAdapter);
        mUserLv.setOnScrollListener(new AbsListView.OnScrollListener() {
            int firstItem;
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                int lastVisibleIndex=mUserLv.getLastVisiblePosition();
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                        && lastVisibleIndex+1 >= mUserManagerAdapter.getData().size()) {
                   getNetInfo();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                firstItem=firstVisibleItem;
            }
        });
        mUserLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AddUserActivity.start(UserManagerActivity.this,true,mUserManagerAdapter.getItem(i));
            }
        });

    }
}
