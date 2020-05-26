package com.wangtian.message.netWork;


import com.wangtian.message.netBean.BaiduBean;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * @Author Archy Wang
 * @Date 2017/11/20
 * @Description 借口地址
 */

public interface IBaiduNetInterface {

    @FormUrlEncoded
    @POST("api/trans/vip/translate")
    Observable<BaiduBean> translate(@FieldMap Map<String, String> param);


}
