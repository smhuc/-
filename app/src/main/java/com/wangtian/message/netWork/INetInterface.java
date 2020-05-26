package com.wangtian.message.netWork;


import com.wangtian.message.netBean.CategoryBean;
import com.wangtian.message.netBean.LoginBean;
import com.wangtian.message.netBean.NewListResponse;
import com.wangtian.message.netBean.ShareList;
import com.wangtian.message.netBean.SocialNetListBean;
import com.wangtian.message.netBean.UserListBean;

import java.util.ArrayList;
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

public interface INetInterface {

    @FormUrlEncoded
    @POST("app/loginApp/login")
    Observable<LoginBean> Login(@FieldMap Map<String, String> param);
    @FormUrlEncoded
    @POST("app/user/selectUsers")
    Observable<ArrayList<UserListBean>> getUserList(@FieldMap Map<String, String> param);
    @FormUrlEncoded
    @POST("app/user/deleteUsers")
    Observable<Object> delUser(@FieldMap Map<String, String> param);

    @FormUrlEncoded
    @POST("app/user/editUsers")
    Observable<Object> addUser(@FieldMap Map<String, String> param);



    @FormUrlEncoded
    @POST("app/information/informationList")
    Observable<NewListResponse> getNewsList(@FieldMap Map<String, String> param);
    @FormUrlEncoded
    @POST("app/sociality/socialityList")
    Observable<SocialNetListBean> getsocialList(@FieldMap Map<String, String> param);



    @FormUrlEncoded
    @POST("app/information/showInformation")
    Observable<NewListResponse.InformationListBean> getNewsDetail(@FieldMap Map<String, String> param);
    @FormUrlEncoded
    @POST("app/sociality/getSocilaDetail")
    Observable< SocialNetListBean.IndexVOListBean> getsocialDetail(@FieldMap Map<String, String> param);

    @FormUrlEncoded
    @POST("app/category/categoryList")
    Observable<ArrayList<CategoryBean>> getCategoryList(@FieldMap Map<String, String> param);










    @FormUrlEncoded
    @POST("app/sharinginforInformation/editShareHtml")
    Observable<Object> addShare(@FieldMap Map<String, String> param);
    @FormUrlEncoded
    @POST("app/sharinginforInformation/queryShareHtml")
    Observable<ShareList> getShareList(@FieldMap Map<String, String> param);
    @FormUrlEncoded
    @POST("app/sharinginforInformation/deleteShareHtml")
    Observable<Object> deleteShare(@FieldMap Map<String, String> param);


  /*
    @POST("api/auth/forget/verifycode")
    Observable<VerifyCodeVoModel> sendForgetVerifyCode(@Body Map<String, String> param);
    @POST("api/auth/register")
    Observable<UserVoModel> regester(@Body Map<String, String> param);
    @POST("api/auth/retrievepassword")
    Observable<UserVoModel> retrievepassword(@Body Map<String, String> param);

    @POST("api/auth/login")
    Observable<LoginBean> login(@Body Map<String, String> param);

    @POST("api/discovery/shortContentAdd")
    Observable<Object> sendContentAdd(@Body ShortContentIO param);

    @POST("api/discovery/topicList")
    Observable<TopicListBean> searchTopicList(@Body TopicSearchIO param);

    @POST
    Observable<ContentListBean> contentList(@Url String url, @Body SendContentBean param);

    @POST("api/user/signature")
    Observable<TencentSignatureBean>getTecentSignature();

    @POST("api/discovery/comment/commentAdd")
    Observable<Object>CommentParentAdd(@Body Map<String, String> param);
    @POST("api/discovery/comment/commentReplyAdd")
    Observable<Object>CommentChildAdd(@Body Map<String, String> param);

    @POST("api/discovery/comment/commentList")
    Observable<ParentCommentResponseBean>CommentParentList(@Body ParentCommentRequestBean parentCommentBean);
    @POST("api/discovery/comment/commentReplyList")
    Observable<ChildCommentResponseBean>CommentChildList(@Body ParentCommentRequestBean parentCommentBean);

    @POST("api/discovery/like/comment/add")
    Observable<Object>likeCommentParentList(@Body UserLikeIO userLikeIO);
    @POST("api/discovery/like/comment/delete")
    Observable<Object>deletelikeCommentParentList(@Body UserLikeIO userLikeIO);


    @POST("api/discovery/icon_favorite/add")
    Observable<Object>addlikeDiscovery(@Body Map<String, String> param);

    @POST("api/discovery/icon_favorite/delete")
    Observable<Object>deletelikeDiscovery(@Body Map<String, String> param);

    @POST("api/musicLib/userMusicAdd")
    Observable<Object>addMusic(@Body Map<String, String> param);

    @POST("api/musicLib/userMusicDelete")
    Observable<Object>deleteMusic(@Body Map<String, String> param);

    @POST("api/file/upload")
    Observable<FileUpdateVoModel> uploadFiles(@Body MultipartBody body);

    @POST("api/discovery/follow/add")
    Observable<UserFollowResult>discoveryFollowAdd(@Body Map<String, String> map);
    @POST("api/userFollow/verifycode")
    Observable<Object>invitSms(@Body Map<String, String> map);
    @POST("api/discovery/follow/delete")
    Observable<UserFollowResult>discoveryFollowDelete(@Body Map<String, String> map);


    @POST("api/userFollow/userFansList")
    Observable<UserListReponse>fanList(@Body CommomFormData commomFormData);

    @POST("api/userFollow/userFollowList")
    Observable<UserListReponse>followList(@Body CommomFormData commomFormData);

    @POST("api/user/userDetail")
    Observable<UserDetailInfo>userDetailInfo(@Body Map<String, String> map);

    @POST("api/musicLib/musicLibList")
    Observable<MusicListBean>allMusicList(@Body searchMusicBean map);
    @POST("api/musicLib/userMusicList")
    Observable<MusicListBean>collMusicList(@Body searchMusicBean map);

    @POST("api/message/messageList")
    Observable<SystemListBean>systemMessageList(@Body CommomFormData map);
    @POST("api/message/userLikeList")
    Observable<StartListBean>startMessageList(@Body CommomFormData map);
    @POST("api/message/userFollowList")
    Observable<UserListReponse>followMessageList(@Body CommomFormData map);

    @POST("api/message/commentResList")
    Observable<ParentCommentResponseBean>CommentMessageList(@Body CommomFormData map);

    @POST("api/message/commentList")
    Observable<MessageDetailApiResultList>MessageDetailList(@Body CommomFormData map);

    @POST("api/userFollow/userTbookAdds")
    Observable<Object>ContactInfoAdd(@Body HashMap<String, ArrayList<ContactInfo>> map);
    @POST("api/userFollow/userTbookList")
    Observable<UserTbookApiResultList>getContactInfoList(@Body CommomFormData map);



    @POST("api/discovery/addForwardNum")
    Observable<Object>addShareNum(@Body Map<String, String> map);


    @POST("api/message/message_not_read")
    Observable<ArrayList<NotReadMessageBean>>messageNotRead();*/

}
