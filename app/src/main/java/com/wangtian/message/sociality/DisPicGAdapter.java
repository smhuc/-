package com.wangtian.message.sociality;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.wangtian.message.R;
import com.wangtian.message.adapter.BaseListViewAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @Author Archy Wang
 * @Date 2018/8/31
 * @Description
 */

public class DisPicGAdapter  extends BaseListViewAdapter<String> {

    @Override

    public View getView(int position, View convertView, ViewGroup parent) {
        DisPicGAdapterHolder holder;
        if (convertView==null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.dis_pic_item, parent, false);
            holder=new DisPicGAdapterHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder= (DisPicGAdapterHolder) convertView.getTag();
        }
        String item = getItem(position);
        Glide.with(parent.getContext()).load(item).error(R.drawable.empty_image).into(holder.mPicIv);


        return convertView;
    }

    static class DisPicGAdapterHolder {
        @BindView(R.id.pic_iv)
        ImageView mPicIv;

        DisPicGAdapterHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
