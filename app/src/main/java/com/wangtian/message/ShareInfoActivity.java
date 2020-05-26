package com.wangtian.message;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.wangtian.message.base.BaseMainMenuActivity;
import com.wangtian.message.bean.ShareNetBean;
import com.wangtian.message.netBean.ShareList;
import com.wangtian.message.netWork.NetWorkSubscriber;
import com.wangtian.message.netWork.NetWorkUtils;
import com.wangtian.message.view.DialogView;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.wangtian.message.AddShareActivity.*;

public class ShareInfoActivity extends BaseMainMenuActivity {

    @BindView(R.id.l_rv)
    LRecyclerView mLRv;
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
    private ProgressDialog dialog;
    private ArrayList<ShareNetBean> mData;
    private LRecyclerViewAdapter mLRecyclerViewAdapter;
    private int pageBegin=1;
    private CommonAdapter<ShareNetBean> mCommonAdapter;
    private int mCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_info);
        ButterKnife.bind(this);
        setActivity(ShareInfoActivity.this, "分享");

        left(2).setOnClickListener(v -> menu.toggle(menu.isShown()));
        mImgRight.setBackgroundResource(R.drawable.btn_add);
        mImgRight.setVisibility(View.VISIBLE);
        mRelRight.setVisibility(View.VISIBLE);
        mRelRight.setOnClickListener(v-> start(ShareInfoActivity.this));
        EventBus.getDefault().register(this);
        initView();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventMessage(String even){
        switch (even){
            case ADD_SHARE:
                pageBegin=1;
                getListInfo("", false);
                break;
            default:
                break;
        }
    }
    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();

    }

    private void initView() {
        dialog = new ProgressDialog(this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        String keyWord = "";
        mLRv.setLayoutManager(new LinearLayoutManager(this));
        getListInfo(keyWord, true);

        View header = LayoutInflater.from(this).inflate(R.layout.search_key_header, mLRv, false);


        mData = new ArrayList<ShareNetBean>();

        mCommonAdapter = new CommonAdapter<ShareNetBean>(this, R.layout.share_item, mData) {
            @Override
            protected void convert(ViewHolder holder, ShareNetBean shareNetBean, int position) {
                TextView shareNameTv = (TextView) holder.getView(R.id.share_name_tv);
                shareNameTv.setText(shareNetBean.getName()+" "+shareNetBean.getUserName());
                ImageView shareDeleteIv = (ImageView) holder.getView(R.id.share_delete_iv);
                shareDeleteIv.setVisibility(TextUtils.equals(MyApplication.sLoginBean.getUser().getId(), shareNetBean.getVc_USERID()) ? View.VISIBLE : View.GONE);
                shareDeleteIv.setOnClickListener(v -> {

                    final DialogView dialog = new DialogView(ShareInfoActivity.this);
                    dialog.setBtnText("确定", "取消");
                    dialog.setContentText("确定要删除该条分享吗？");

                    dialog.setOnBtnClickListener(new DialogView.OnBtnClickListener() {
                        @Override
                        public void doConfirm() {
                            dialog.dismiss();
                            HashMap<String, String> param = new HashMap<>();
                            param.put("token", MyApplication.sLoginBean.getToken());
                            param.put("vc_UserId", MyApplication.sLoginBean.getUser().getId());
                            param.put("nm_Id", shareNetBean.getNm_Id());
                            NetWorkUtils.getInstance().getInterfaceService().deleteShare(param)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new NetWorkSubscriber<Object>() {
                                        @Override
                                        public void onNext(Object o) {
                                            Toast.makeText(mContext, "删除成功", Toast.LENGTH_SHORT).show();
                                            pageBegin=1;
                                            getListInfo(keyWord, false);
                                        }
                                    });
                        }

                        @Override
                        public void doCancel() {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();










                });
                TextView timeTv = (TextView) holder.getView(R.id.time_tv);
                timeTv.setText(shareNetBean.getDt_SHAREDATE());
                TextView adviceTv = (TextView) holder.getView(R.id.advice_tv);
                if (TextUtils.isEmpty(shareNetBean.getVc_REMARKS())) {
                    adviceTv.setVisibility(View.GONE);
                } else {
                    adviceTv.setText(shareNetBean.getVc_REMARKS());
                }

                LinearLayout shareContentLl = (LinearLayout) holder.getView(R.id.share_content_ll);
                shareContentLl.setOnClickListener(v -> {
                    Intent intent = new Intent(ShareInfoActivity.this, WebViewActivity.class);
                    intent.putExtra("URL", shareNetBean.getVc_URL());
                    startActivity(intent);
                });
                TextView shareTitleTv = (TextView) holder.getView(R.id.share_title_tv);
                shareTitleTv.setText(shareNetBean.getVc_TITLE());
                TextView shareUrlTv = (TextView) holder.getView(R.id.share_url_tv);
                shareUrlTv.setText(shareNetBean.getVc_URL());
            }
        };
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(mCommonAdapter);
//        mLRecyclerViewAdapter.addHeaderView(header);
        mLRv.setAdapter(mLRecyclerViewAdapter);
        mLRv.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageBegin=1;
                getListInfo(keyWord, false);
            }
        });

        mLRv.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

                if (mCommonAdapter.getDatas().size() < mCount) {
                    // loading data
                    getListInfo( keyWord,false);
                } else {
                    mLRv.setNoMore(true);
                }
            }
        });
    }

    private void getListInfo(String keyWord, boolean showLoading) {
        HashMap<String, String> param = new HashMap<>();
        param.put("token", MyApplication.sLoginBean.getToken());
        param.put("page", String.valueOf(pageBegin));
        param.put("rowsPerPage", String.valueOf(20));

        NetWorkUtils.getInstance().getInterfaceService().getShareList(param)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetWorkSubscriber<ShareList>() {
                               @Override
                               public void onStart() {
                                   super.onStart();
                                   if (showLoading) {
                                       dialog.setMessage("数据加载中...");
                                       dialog.show();
                                   }
                               }

                               @Override
                               public void onError(Throwable e) {
                                   super.onError(e);
                                   if (showLoading) {
                                       dialog.dismiss();
                                   }
                               }

                               @Override
                               public void onNext(ShareList shareList) {
                                  /*
                                   */
                                   mCount = shareList.getCount();

                                   if (showLoading) {
                                       dialog.dismiss();
                                   }
                                   ArrayList<ShareNetBean> shareNetBeans=shareList.getSharingInformationList();
                                   if (shareNetBeans != null && shareNetBeans.size() > 0) {
                                       if (pageBegin == 1) {
                                           mData.clear();
                                           mData.addAll(shareNetBeans);
                                       } else {
                                           mData.addAll(shareNetBeans);
                                       }
                                       pageBegin++;
                                       mLRv.refreshComplete(20);

                                       mCommonAdapter.notifyDataSetChanged();
                                       mLRecyclerViewAdapter.notifyDataSetChanged();
                                   }


                               }
                           }
                );
    }
}
