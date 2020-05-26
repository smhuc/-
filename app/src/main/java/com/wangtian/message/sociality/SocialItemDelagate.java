package com.wangtian.message.sociality;

import android.text.Html;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.wangtian.message.R;
import com.wangtian.message.netBean.SocialNetListBean;
import com.wangtian.message.util.Utility;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.HashSet;

/**
 * @Author Archy Wang
 * @Date 2019/3/26
 * @Description
 */

public class SocialItemDelagate implements ItemViewDelegate<SocialNetListBean.IndexVOListBean> {

    @Override
    public int getItemViewLayoutId() {
        return R.layout.news_item_rv;
    }

    @Override
    public boolean isForViewType(SocialNetListBean.IndexVOListBean item, int position) {
        return TextUtils.isEmpty(item.getText_picture_url_local()) || TextUtils.equals(item.getText_picture_url_local(), "0");
    }

    @Override
    public void convert(ViewHolder holder, SocialNetListBean.IndexVOListBean informationListBean, int position) {
        String title = !TextUtils.isEmpty(informationListBean.getText_post_content()) ? informationListBean.getText_post_content() : "";
        String name = !TextUtils.isEmpty(informationListBean.getVc_username()) ? !"0".equals(informationListBean.getVc_username()) ? informationListBean.getVc_username() : informationListBean.getVc_nickname() : "";
        String H5String = Utility.getData(title);
        final HashSet<String> keyWordsSet = Utility.getAllKeyWords(H5String);
        SpannableString spannableStr = new SpannableString(Html.fromHtml(H5String));

        for (String keyStr : keyWordsSet) { //为所有关键字增加点击事件
            Utility.findKeyAndSetEvent(spannableStr, spannableStr.toString(), keyStr, 0);
        }

        holder.getView(R.id.root_ll).setOnClickListener(view -> SocialHtmlDetailActivity.start(holder.getConvertView().getContext(), !TextUtils.isEmpty(informationListBean.getDataId()) ? informationListBean.getDataId() : ""));
        TextView tv = holder.getView(R.id.root_ll).findViewById(R.id.title_tv);
        tv.setText(spannableStr);
        holder.setText(R.id.from_tv, name);
        holder.setText(R.id.comment_num_tv, !TextUtils.isEmpty(informationListBean.getVc_reply_count()) ? "评论 " + informationListBean.getVc_reply_count() : "评论 0");
        holder.setText(R.id.time_tv, !TextUtils.isEmpty(informationListBean.getDt_pubdate()) ? informationListBean.getDt_pubdate() : "");
        holder.getView(R.id.haiwai_tv).setVisibility(View.GONE);
    }

}
