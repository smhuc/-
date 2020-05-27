package com.wangtian.message;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wangtian.message.netBean.SocialNetListBean;
import com.wangtian.message.util.GlideUtils;
import com.wangtian.message.view.CommentPopWindow;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * @Author Archy Wang
 * @Date 2019/3/26
 * @Description
 */

public class ReplyItemDelagate implements ItemViewDelegate<SocialNetListBean.ReplyListBean> {
    private CommentPopWindow.LoadDataInterface loadDataInterface = null;

    public ReplyItemDelagate(CommentPopWindow.LoadDataInterface loadDataInterface) {
        this.loadDataInterface = loadDataInterface;
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.reply_item_pop;
    }

    @Override
    public boolean isForViewType(SocialNetListBean.ReplyListBean item, int position) {
        return true;
    }

    @Override
    public void convert(ViewHolder holder, SocialNetListBean.ReplyListBean replyListBean, int position) {
        holder.setText(R.id.content, !TextUtils.isEmpty(replyListBean.getContent()) ? replyListBean.getContent() : "");
        holder.setText(R.id.user_name, !TextUtils.isEmpty(replyListBean.getVc_publisher_nickname()) ? replyListBean.getVc_publisher_nickname() : "");
        holder.setText(R.id.user_update_time, !TextUtils.isEmpty(replyListBean.getDt_pubdate()) ? replyListBean.getDt_pubdate() : "");
        ImageView view = holder.getView(R.id.user_picture);
        String vc_imagesrc = !TextUtils.isEmpty(replyListBean.getVc_publisher_photo_url_local()) ? replyListBean.getVc_publisher_photo_url_local() : "";
        GlideUtils.loadCirclePic(holder.getConvertView().getContext(), vc_imagesrc, view);
        TextView tv = holder.getView(R.id.content_translate);
        tv.setText("翻译");
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("翻译".equals(tv.getText().toString())) {
                    tv.setText("原文");
                    loadDataInterface.loadTranslate(holder.getView(R.id.content),replyListBean.getContent());
                } else {
                    tv.setText("翻译");
                    holder.setText(R.id.content, !TextUtils.isEmpty(replyListBean.getContent()) ? replyListBean.getContent() : "");
                }

            }
        });
    }

}
