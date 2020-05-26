package com.wangtian.message.netBean;

import com.wangtian.message.bean.ShareNetBean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @Author Archy Wang
 * @Date 2019/4/1
 * @Description
 */

public class ShareList implements Serializable{
    private int count;
    private ArrayList<ShareNetBean> sharingInformationList;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ArrayList<ShareNetBean> getSharingInformationList() {
        return sharingInformationList;
    }

    public void setSharingInformationList(ArrayList<ShareNetBean> sharingInformationList) {
        this.sharingInformationList = sharingInformationList;
    }
}
