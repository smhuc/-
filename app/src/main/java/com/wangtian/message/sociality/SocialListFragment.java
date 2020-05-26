package com.wangtian.message.sociality;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.wangtian.message.MyApplication;
import com.wangtian.message.R;
import com.wangtian.message.base.BaseFragment;
import com.wangtian.message.netBean.SocialNetListBean;
import com.wangtian.message.netWork.NetWorkSubscriber;
import com.wangtian.message.netWork.NetWorkUtils;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @Author Archy Wang
 * @Date 2019/3/26
 * @Description
 */

public class SocialListFragment extends BaseFragment {
    @BindView(R.id.l_rv)
    LRecyclerView mLRv;
    @BindView(R.id.go_back_iv)
    ImageView mGoBackIv;
    Unbinder unbinder;
    private LRecyclerViewAdapter mLRecyclerViewAdapter;
    private ProgressDialog dialog;
    private int pageBegin = 1;
    private ArrayList<SocialNetListBean.IndexVOListBean> mData;
    private MultiItemTypeAdapter mMultiItemTypeAdapter;
    private int mCount;
    private static String TIP_ID = "TIP_ID";
    private boolean mIsSearch = false;
    private String mString;
    private String mKeyWord;

    public static SocialListFragment newInstance(String s) {
        SocialListFragment fragmentOne = new SocialListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TIP_ID, s);
        fragmentOne.setArguments(bundle);
        return fragmentOne;
    }

    @Override
    protected int getContentViewID() {
        return R.layout.new_list_fragment;
    }

    @Override
    protected void initViewsAndData(View view) {
        dialog = new ProgressDialog(mActivity);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        mKeyWord = "";
        if (getArguments() != null) {
            mString = getArguments().getString(TIP_ID);
            if (TextUtils.equals("ALL", mString)) {
                ClassTip = "";
            } else {
                ClassTip = mString;
            }
        }

        mLRv.setLScrollListener(new LRecyclerView.LScrollListener() {
            @Override
            public void onScrollUp() {

            }

            @Override
            public void onScrollDown() {

            }

            @Override
            public void onScrolled(int distanceX, int distanceY) {

            }

            @Override
            public void onScrollStateChanged(int state) {
//                if (state == RecyclerView.SCROLL_STATE_DRAGGING)
                {
                    final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mLRv.getLayoutManager();
                    int firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();
                    if (firstVisibleItem > 3) {
                        mGoBackIv.setVisibility(View.VISIBLE);
                    } else {
                        mGoBackIv.setVisibility(View.GONE);
                    }
                }
            }
        });
        mGoBackIv.setOnClickListener(v -> {
            mLRv.scrollToPosition(0);
        });


        LinearLayoutManager layout = new LinearLayoutManager(getContext());
        mLRv.setLayoutManager(layout);

//        mLRv.setLayoutManager(new LinearLayoutManager(getContext()));
        getListInfo(mKeyWord, true);

        View header = LayoutInflater.from(mActivity).inflate(R.layout.search_key_header, mLRv, false);
        TextView cancal = (TextView) header.findViewById(R.id.tx_cancal);
        EditText seach = (EditText) header.findViewById(R.id.ed_seach);
        seach.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(seach.getText().toString())) {
                    cancal.setVisibility(View.VISIBLE);
                } else {
                    cancal.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        header.findViewById(R.id.find_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pageBegin = 1;
                mKeyWord = seach.getText().toString().trim();
                MyApplication.socialKeyword = mKeyWord;
                getListInfo(mKeyWord, true);
            }
        });

        cancal.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                seach.setText("");
                pageBegin = 1;
                mKeyWord = "";
                MyApplication.socialKeyword = mKeyWord;
                getListInfo(mKeyWord, true);
            }
        });


        mData = new ArrayList<>();
        mMultiItemTypeAdapter = new MultiItemTypeAdapter(mActivity, mData);
        mMultiItemTypeAdapter.addItemViewDelegate(new SocialPicItemDelagate());
        mMultiItemTypeAdapter.addItemViewDelegate(new SocialItemDelagate());
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(mMultiItemTypeAdapter);
        if (TextUtils.equals("ALL", mString)) {
            mLRecyclerViewAdapter.addHeaderView(header);
        }
        mLRv.setAdapter(mLRecyclerViewAdapter);
        mLRv.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageBegin = 1;
//                seach.setText("");
//                mKeyWord = "";
                getListInfo(mKeyWord, false);
            }
        });

        mLRv.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

                if (mMultiItemTypeAdapter.getDatas().size() < mCount) {
                    // loading data
                    getListInfo(mKeyWord, false);
                } else {
                    mLRv.setNoMore(true);
                }
            }
        });
    }

    private String ClassTip = "";

    private void getListInfo(String keyWord, boolean showLoading) {
        HashMap<String, String> param = new HashMap<>();
        param.put("token", MyApplication.sLoginBean.getToken());
        param.put("page", String.valueOf(pageBegin));
        param.put("rowsPerPage", String.valueOf(20));
        param.put("keyword", TextUtils.isEmpty(keyWord) ? "" : keyWord);
        if (!TextUtils.isEmpty(ClassTip)) {
            param.put("classifyids", ClassTip);
        }


        NetWorkUtils.getInstance().getInterfaceService().getsocialList(param)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetWorkSubscriber<SocialNetListBean>() {
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
                               public void onNext(SocialNetListBean socialNetListBean) {
                                   mCount = socialNetListBean.getCount();
                                   if (showLoading) {
                                       dialog.dismiss();
                                   }
                                   if (socialNetListBean.getIndexVOList() != null) {
                                       if (pageBegin == 1) {
                                           mData.clear();
                                           mData.addAll(socialNetListBean.getIndexVOList());
                                       } else {
                                           mData.addAll(socialNetListBean.getIndexVOList());
                                       }
                                       pageBegin++;
                                       mLRv.refreshComplete(20);

                                       mMultiItemTypeAdapter.notifyDataSetChanged();
                                       mLRecyclerViewAdapter.notifyDataSetChanged();
                                       if (socialNetListBean.getIndexVOList().size() <= 0) {
                                           getActivity().runOnUiThread(new Runnable() {
                                               @Override
                                               public void run() {
                                                   Toast.makeText(getActivity(), "很遗憾,未查询到您要搜索的内容~", Toast.LENGTH_SHORT).show();
                                               }
                                           });
                                       }
                                   } else {
                                       getActivity().runOnUiThread(new Runnable() {
                                           @Override
                                           public void run() {
                                               Toast.makeText(getActivity(), "很遗憾,未查询到您要搜索的内容~", Toast.LENGTH_SHORT).show();
                                           }
                                       });
                                   }

                               }
                           }
                );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
