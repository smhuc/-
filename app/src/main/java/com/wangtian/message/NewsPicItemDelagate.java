package com.wangtian.message;

import android.text.Html;
import android.text.SpannableString;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.wangtian.message.netBean.NewListResponse;
import com.wangtian.message.util.GlideUtils;
import com.wangtian.message.util.Utility;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.HashSet;

/**
 * @Author Archy Wang
 * @Date 2019/3/26
 * @Description
 */

public class NewsPicItemDelagate implements ItemViewDelegate<NewListResponse.InformationListBean> {

    @Override
    public int getItemViewLayoutId() {
        return R.layout.news_pic_item_rv;
    }

    @Override
    public boolean isForViewType(NewListResponse.InformationListBean item, int position) {
        return !TextUtils.isEmpty(item.getVc_imagesrc()) && !TextUtils.equals(item.getVc_imagesrc(), "0");
    }

    @Override
    public void convert(ViewHolder holder, NewListResponse.InformationListBean informationListBean, int position) {

        String title = !TextUtils.isEmpty(informationListBean.getVc_title()) ? informationListBean.getVc_title() : "";
        holder.getView(R.id.root_ll).setOnClickListener(view -> HtmlDetailActivity.start(holder.getConvertView().getContext(), !TextUtils.isEmpty(informationListBean.getVc_md5()) ? informationListBean.getVc_md5() : ""));
        TextView tv = holder.getView(R.id.root_ll).findViewById(R.id.title_tv);
        tv.setText(Utility.getConvertContent(title));
        holder.setText(R.id.from_tv, !TextUtils.isEmpty(informationListBean.getVc_origin()) ? informationListBean.getVc_origin() : "");
        holder.setText(R.id.comment_num_tv, !TextUtils.isEmpty(informationListBean.getVc_comm_num()) ? "评论 " + informationListBean.getVc_comm_num() : "评论 0");
        holder.setText(R.id.time_tv, !TextUtils.isEmpty(informationListBean.getDt_pubdate()) ? informationListBean.getDt_pubdate() : "");
        ImageView view = holder.getView(R.id.pic_iv);
        String vc_imagesrc = !TextUtils.isEmpty(informationListBean.getVc_imagesrc()) ? informationListBean.getVc_imagesrc() : "";
        if (vc_imagesrc.contains(",")) {
            String[] split = vc_imagesrc.split(",");
            GlideUtils.loadNormalPic(holder.getConvertView().getContext(), split[0], view);
        } else {
            GlideUtils.loadNormalPic(holder.getConvertView().getContext(), vc_imagesrc, view);
        }
    }

}
