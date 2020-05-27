package com.wangtian.message.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.wangtian.message.R;
import com.wangtian.message.ReplyItemDelagate;
import com.wangtian.message.netBean.SocialNetListBean;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.List;

public class CommentPopWindow extends PopupWindow {
    private static final String TAG = CommentPopWindow.class.getSimpleName();

    private LRecyclerView mLRv;

    private View view;
    private Context mContext;

    private ArrayList<SocialNetListBean.ReplyListBean> mData;
    private MultiItemTypeAdapter mMultiItemTypeAdapter;
    private LRecyclerViewAdapter mLRecyclerViewAdapter;

    private int mCount;
    public LoadDataInterface loadDataInterface = null;

    public void setLoadDataInterface(LoadDataInterface dataInterface) {
        this.loadDataInterface = dataInterface;
    }

    public CommentPopWindow(Context context, LoadDataInterface dataInterface, int mCount) {
        super(context);
        this.loadDataInterface = dataInterface;
        this.mContext = context;
        this.mCount = mCount;
        initView();
        initPopWindow();
    }


    private void initView() {
        mData = new ArrayList<>();
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.comment_popupwindow, null);
        mLRv = view.findViewById(R.id.lr_rv_pop);
        mLRv.setLayoutManager(new LinearLayoutManager(mContext));
        mMultiItemTypeAdapter = new MultiItemTypeAdapter(mContext, mData);
        mMultiItemTypeAdapter.addItemViewDelegate(new ReplyItemDelagate(loadDataInterface));
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(mMultiItemTypeAdapter);

        mLRv.setAdapter(mLRecyclerViewAdapter);
        mLRv.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (loadDataInterface != null) {
                    loadDataInterface.refresh();
                }
            }
        });
        mLRv.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

                if (mMultiItemTypeAdapter.getDatas().size() < mCount) {
                    if (loadDataInterface != null) {
                        loadDataInterface.loadMore();
                    }
                } else {
                    mLRv.setNoMore(true);
                }
            }
        });
    }

    public void notifyData(List<SocialNetListBean.ReplyListBean> datas) {
        Log.d(TAG, "notifyData: mCount = " + mCount);
        mData.clear();
        mData.addAll(datas);
        mLRv.refreshComplete(10);
        mMultiItemTypeAdapter.notifyDataSetChanged();
        mLRecyclerViewAdapter.notifyDataSetChanged();
        backgroundAlpha((Activity) mContext, 0.5f);//0.0-1.0
    }

    public interface LoadDataInterface {
        void refresh();

        void loadMore();

        void loadTranslate(TextView tv, String content);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        backgroundAlpha((Activity) mContext, 1f);//0.0-1.0
    }

    private void initPopWindow() {
        this.setContentView(view);
        // 设置弹出窗体的宽
        this.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        // 设置弹出窗体的高
        this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        // 设置弹出窗体可点击()
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.mypopwindow_anim_style);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00FFFFFF);
        //设置弹出窗体的背景
        this.setBackgroundDrawable(dw);
    }

    /**
     * 设置添加屏幕的背景透明度(值越大,透明度越高)
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(Activity context, float bgAlpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        context.getWindow().setAttributes(lp);
    }
}