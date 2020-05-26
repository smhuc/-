package com.wangtian.message.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wangtian.message.R;
import com.wangtian.message.netBean.UserListBean;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @Author Archy Wang
 * @Date 2019/3/20
 * @Description
 */

public class UserManagerAdapter extends BaseListViewAdapter<UserListBean> {
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_manager_list_item, parent, false);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
            holder.mNameTv.setText(getItem(position).getName());

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.name_tv)
        TextView mNameTv;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
