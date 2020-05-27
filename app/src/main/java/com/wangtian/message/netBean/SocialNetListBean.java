package com.wangtian.message.netBean;

import java.util.List;

/**
 * @Author Archy Wang
 * @Date 2019/4/1
 * @Description
 */

public class SocialNetListBean {

    /**
     * count : 1187
     * IndexVOList : [{"pageNum":1,"pageSize":10,"total":0,"list":[],"rows":[],"where":"","params":[],"solrkey":null,"type":"2","vc_userid":"954812350409990144","vc_target_user_id":"edd9b7e6f12323e902197132ad349ebc","vc_nickname":"#Uygurtimes 专注维吾尔族人与东突厥斯坦相关的话题","vc_username":"uygurtimes","vc_post_type":"1","vc_photo_url":"https://pbs.twimg.com/profile_images/1095826720215703552/Bbr3tA7U_normal.jpg","vc_photo_url_local":"http://101.96.131.114:9999/photos/twitter/954812350409990144/d3dcd36f6e892b0b9ad77623389003f5.jpg","vc_postid":null,"text_post_content":"RT @PeteCIrwin: Diplomats from these countries were invited to Xinjiang this week:- Pakistan- Venezuela- Cuba- Egypt- Cambodia- Russ\u2026","replay_content":null,"text_picture_url":"0","text_picture_url_local":"0","vc_reply_count":"0","vc_forward_count":null,"vc_favorites_count":"0","vc_video_url":"0","vc_video_url_local":"0","vc_video_picture_url":"0","vc_video_picture_url_local":"0","dt_pubdate":"2019-02-22 23:54:21","dt_gather_time":"2019-03-11 08:33:21","vc_md5":"512f54dfe66c9e1e04322649a78662dc","vc_forward_url":null,"vc_forward_title":null,"vc_forward_content":null,"cloundKeyword":null,"vc_lebal":null,"vc_retweet_username":"PeteCIrwin","vc_retweet_nickname":null,"vc_tweetid":"1098974283961896962","vc_tweet_url":"https://www.twitter.com/uygurtimes/status/1098974283961896962","vc_retweet_count":"125","id":null,"vc_title":null,"text_description_snippet":null,"vc_video_id":null,"vc_channel_thumbnail_local":null,"vc_video_local":null,"vc_like_count":null,"vc_dislike_count":null,"vc_view_count":null,"vc_comment_count":null,"page_Rocommend":0,"rowsPerPage_Rocommend":0,"page_Hot":0,"rowsPerPage_Hot":0,"page_Account":0,"rowsPerPage_Account":0,"keywords":null,"keywordName":null,"dataId":"1098974283961896962","start_cjsj":null,"end_cjsj":null,"queryKeyword":null,"searchType":null,"searchIsTrue":null,"queryLogic":null,"vc_mtlx":null,"vc_ssyz":null,"vc_ssyw":null,"vc_suzz":null,"menuIdChild":null,"menuId":null,"vc_jkzt":null,"currentPage":0,"rowsPerPage":0,"classifyids":"2c929ebd6720c002016720c0dc8c00003","classifynumber":"1","keyword":null,"token":null,"page":0,"isgz":null}]
     */

    private int count;
    private List<IndexVOListBean> IndexVOList;
    private List<ReplyListBean> replylist;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<IndexVOListBean> getIndexVOList() {
        return IndexVOList;
    }

    public void setIndexVOList(List<IndexVOListBean> IndexVOList) {
        this.IndexVOList = IndexVOList;
    }

    public List<ReplyListBean> getReplylist() {
        return replylist;
    }

    public void setReplylist(List<ReplyListBean> replylist) {
        this.replylist = replylist;
    }

    public static class ReplyListBean {
        /**{
         "pageNum": 1,
         "pageSize": 10,
         "total": 0,
         "list": [],
         "rows": [],
         "where": "",
         "params": [],
         "content": "@BorisJohnson @COP26 @sciencemuseum You are mad to believe these climate change nutters. By all means protect habitat and find/use alternative energy: after all one day the oil will run out. The only current option for our energy needs will be nuclear (preferably fusion - a long way off).",
         "vc_target_user_id": "38c45f017898c01cbc2a74716dfd9671",
         "vc_postid": null,
         "vc_publisher_url": null,
         "vc_publisher_nickname": "john carins",
         "vc_publisher_photo_url_local": "twitter/3131144855/a7383357a9c5dee5922aba1d7bb86a89.png",
         "text_picture_url_local": null,
         "text_content": "0",
         "dt_pubdate": "2020-02-04 20:04:44",
         "vc_tweetid": "1224663865557950469",
         "vc_tweeturl": "https://www.twitter.com/borisjohnson/status/1224663865557950469",
         "vc_publisherid": "839507779438198785",
         "vc_publisher_username": "john_carins",
         "vc_contentid": "1224665086847000576",
         "vc_reply_user": "borisjohnson",
         "vc_favorites_count": "2",
         "vc_reply_count": "0",
         "vc_picture_url_local": "0",
         "vc_video_url_local": "0",
         "vc_video_id": null,
         "vc_author_id": null,
         "vc_author__home_page": null,
         "vc_author_text": null,
         "vc_author_thumbnail_local": null,
         "vc_reply_to_id": null,
         "vc_comment_id": null,
         "vc_like_count": null,
         "vc_published_time_text": null,
         "type": "0",
         "secondType": "2",
         "dataId": null,
         "queryKeyword": null,
         "page_comment": 0,
         "rowsPerPage_comment": 0,
         "cloundKeyword": null,
         "keywords": "0",
         "keywordName": null
         }*/

        private int pageNum;
        private int pageSize;
        private int total;
        private String content;//内容
        private String vc_publisher_photo_url_local;// 头像地址
        private String vc_publisher_nickname;//昵称
        private String dt_pubdate;// 发布时间

        public int getPageNum() {
            return pageNum;
        }

        public void setPageNum(int pageNum) {
            this.pageNum = pageNum;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getVc_publisher_photo_url_local() {
            return vc_publisher_photo_url_local;
        }

        public void setVc_publisher_photo_url_local(String vc_publisher_photo_url_local) {
            this.vc_publisher_photo_url_local = vc_publisher_photo_url_local;
        }

        public String getVc_publisher_nickname() {
            return vc_publisher_nickname;
        }

        public void setVc_publisher_nickname(String vc_publisher_nickname) {
            this.vc_publisher_nickname = vc_publisher_nickname;
        }

        public String getDt_pubdate() {
            return dt_pubdate;
        }

        public void setDt_pubdate(String dt_pubdate) {
            this.dt_pubdate = dt_pubdate;
        }
    }

    public static class IndexVOListBean {
        /**
         * pageNum : 1
         * pageSize : 10
         * total : 0
         * list : []
         * rows : []
         * where :
         * params : []
         * solrkey : null
         * type : 2
         * vc_userid : 954812350409990144
         * vc_target_user_id : edd9b7e6f12323e902197132ad349ebc
         * vc_nickname : #Uygurtimes 专注维吾尔族人与东突厥斯坦相关的话题
         * vc_username : uygurtimes
         * vc_post_type : 1
         * vc_photo_url : https://pbs.twimg.com/profile_images/1095826720215703552/Bbr3tA7U_normal.jpg
         * vc_photo_url_local : http://101.96.131.114:9999/photos/twitter/954812350409990144/d3dcd36f6e892b0b9ad77623389003f5.jpg
         * vc_postid : null
         * text_post_content : RT @PeteCIrwin: Diplomats from these countries were invited to Xinjiang this week:- Pakistan- Venezuela- Cuba- Egypt- Cambodia- Russ…
         * replay_content : null
         * text_picture_url : 0
         * text_picture_url_local : 0
         * vc_reply_count : 0
         * vc_forward_count : null
         * vc_favorites_count : 0
         * vc_video_url : 0
         * vc_video_url_local : 0
         * vc_video_picture_url : 0
         * vc_video_picture_url_local : 0
         * dt_pubdate : 2019-02-22 23:54:21
         * dt_gather_time : 2019-03-11 08:33:21
         * vc_md5 : 512f54dfe66c9e1e04322649a78662dc
         * vc_forward_url : null
         * vc_forward_title : null
         * vc_forward_content : null
         * cloundKeyword : null
         * vc_lebal : null
         * vc_retweet_username : PeteCIrwin
         * vc_retweet_nickname : null
         * vc_tweetid : 1098974283961896962
         * vc_tweet_url : https://www.twitter.com/uygurtimes/status/1098974283961896962
         * vc_retweet_count : 125
         * id : null
         * vc_title : null
         * text_description_snippet : null
         * vc_video_id : null
         * vc_channel_thumbnail_local : null
         * vc_video_local : null
         * vc_like_count : null
         * vc_dislike_count : null
         * vc_view_count : null
         * vc_comment_count : null
         * page_Rocommend : 0
         * rowsPerPage_Rocommend : 0
         * page_Hot : 0
         * rowsPerPage_Hot : 0
         * page_Account : 0
         * rowsPerPage_Account : 0
         * keywords : null
         * keywordName : null
         * dataId : 1098974283961896962
         * start_cjsj : null
         * end_cjsj : null
         * queryKeyword : null
         * searchType : null
         * searchIsTrue : null
         * queryLogic : null
         * vc_mtlx : null
         * vc_ssyz : null
         * vc_ssyw : null
         * vc_suzz : null
         * menuIdChild : null
         * menuId : null
         * vc_jkzt : null
         * currentPage : 0
         * rowsPerPage : 0
         * classifyids : 2c929ebd6720c002016720c0dc8c00003
         * classifynumber : 1
         * keyword : null
         * token : null
         * page : 0
         * isgz : null
         */

        private int pageNum;
        private int pageSize;
        private int total;
        private String where;
        private Object solrkey;
        private String type;
        private String vc_userid;
        private String vc_target_user_id;
        private String vc_nickname;
        private String vc_username;
        private String vc_post_type;
        private String vc_photo_url;
        private String vc_photo_url_local;
        private Object vc_postid;
        private String text_post_content;
        private Object replay_content;
        private String text_picture_url;
        private String text_picture_url_local;
        private String vc_reply_count;
        private Object vc_forward_count;
        private String vc_favorites_count;
        private String vc_video_url;
        private String vc_video_url_local;
        private String vc_video_picture_url;
        private String vc_video_picture_url_local;
        private String dt_pubdate;
        private String dt_gather_time;
        private String vc_md5;
        private Object vc_forward_url;
        private Object vc_forward_title;
        private Object vc_forward_content;
        private Object cloundKeyword;
        private Object vc_lebal;
        private String vc_retweet_username;
        private Object vc_retweet_nickname;
        private String vc_tweetid;
        private String vc_tweet_url;
        private String vc_retweet_count;
        private Object id;
        private Object vc_title;
        private Object text_description_snippet;
        private Object vc_video_id;
        private Object vc_channel_thumbnail_local;
        private Object vc_video_local;
        private Object vc_like_count;
        private Object vc_dislike_count;
        private Object vc_view_count;
        private Object vc_comment_count;
        private int page_Rocommend;
        private int rowsPerPage_Rocommend;
        private int page_Hot;
        private int rowsPerPage_Hot;
        private int page_Account;
        private int rowsPerPage_Account;
        private Object keywords;
        private Object keywordName;
        private String dataId;
        private Object start_cjsj;
        private Object end_cjsj;
        private Object queryKeyword;
        private Object searchType;
        private Object searchIsTrue;
        private Object queryLogic;
        private Object vc_mtlx;
        private Object vc_ssyz;
        private Object vc_ssyw;
        private Object vc_suzz;
        private Object menuIdChild;
        private Object menuId;
        private Object vc_jkzt;
        private int currentPage;
        private int rowsPerPage;
        private String classifyids;
        private String classifynumber;
        private Object keyword;
        private Object token;
        private int page;
        private Object isgz;
        private List<?> list;
        private List<?> rows;
        private List<?> params;
        private String secondType;

        public String getSecondType() {
            return secondType;
        }

        public void setSecondType(String secondType) {
            this.secondType = secondType;
        }

        public int getPageNum() {
            return pageNum;
        }

        public void setPageNum(int pageNum) {
            this.pageNum = pageNum;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public String getWhere() {
            return where;
        }

        public void setWhere(String where) {
            this.where = where;
        }

        public Object getSolrkey() {
            return solrkey;
        }

        public void setSolrkey(Object solrkey) {
            this.solrkey = solrkey;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getVc_userid() {
            return vc_userid;
        }

        public void setVc_userid(String vc_userid) {
            this.vc_userid = vc_userid;
        }

        public String getVc_target_user_id() {
            return vc_target_user_id;
        }

        public void setVc_target_user_id(String vc_target_user_id) {
            this.vc_target_user_id = vc_target_user_id;
        }

        public String getVc_nickname() {
            return vc_nickname;
        }

        public void setVc_nickname(String vc_nickname) {
            this.vc_nickname = vc_nickname;
        }

        public String getVc_username() {
            return vc_username;
        }

        public void setVc_username(String vc_username) {
            this.vc_username = vc_username;
        }

        public String getVc_post_type() {
            return vc_post_type;
        }

        public void setVc_post_type(String vc_post_type) {
            this.vc_post_type = vc_post_type;
        }

        public String getVc_photo_url() {
            return vc_photo_url;
        }

        public void setVc_photo_url(String vc_photo_url) {
            this.vc_photo_url = vc_photo_url;
        }

        public String getVc_photo_url_local() {
            return vc_photo_url_local;
        }

        public void setVc_photo_url_local(String vc_photo_url_local) {
            this.vc_photo_url_local = vc_photo_url_local;
        }

        public Object getVc_postid() {
            return vc_postid;
        }

        public void setVc_postid(Object vc_postid) {
            this.vc_postid = vc_postid;
        }

        public String getText_post_content() {
            return text_post_content;
        }

        public void setText_post_content(String text_post_content) {
            this.text_post_content = text_post_content;
        }

        public Object getReplay_content() {
            return replay_content;
        }

        public void setReplay_content(Object replay_content) {
            this.replay_content = replay_content;
        }

        public String getText_picture_url() {
            return text_picture_url;
        }

        public void setText_picture_url(String text_picture_url) {
            this.text_picture_url = text_picture_url;
        }

        public String getText_picture_url_local() {
            return text_picture_url_local;
        }

        public void setText_picture_url_local(String text_picture_url_local) {
            this.text_picture_url_local = text_picture_url_local;
        }

        public String getVc_reply_count() {
            return vc_reply_count;
        }

        public void setVc_reply_count(String vc_reply_count) {
            this.vc_reply_count = vc_reply_count;
        }

        public Object getVc_forward_count() {
            return vc_forward_count;
        }

        public void setVc_forward_count(Object vc_forward_count) {
            this.vc_forward_count = vc_forward_count;
        }

        public String getVc_favorites_count() {
            return vc_favorites_count;
        }

        public void setVc_favorites_count(String vc_favorites_count) {
            this.vc_favorites_count = vc_favorites_count;
        }

        public String getVc_video_url() {
            return vc_video_url;
        }

        public void setVc_video_url(String vc_video_url) {
            this.vc_video_url = vc_video_url;
        }

        public String getVc_video_url_local() {
            return vc_video_url_local;
        }

        public void setVc_video_url_local(String vc_video_url_local) {
            this.vc_video_url_local = vc_video_url_local;
        }

        public String getVc_video_picture_url() {
            return vc_video_picture_url;
        }

        public void setVc_video_picture_url(String vc_video_picture_url) {
            this.vc_video_picture_url = vc_video_picture_url;
        }

        public String getVc_video_picture_url_local() {
            return vc_video_picture_url_local;
        }

        public void setVc_video_picture_url_local(String vc_video_picture_url_local) {
            this.vc_video_picture_url_local = vc_video_picture_url_local;
        }

        public String getDt_pubdate() {
            return dt_pubdate;
        }

        public void setDt_pubdate(String dt_pubdate) {
            this.dt_pubdate = dt_pubdate;
        }

        public String getDt_gather_time() {
            return dt_gather_time;
        }

        public void setDt_gather_time(String dt_gather_time) {
            this.dt_gather_time = dt_gather_time;
        }

        public String getVc_md5() {
            return vc_md5;
        }

        public void setVc_md5(String vc_md5) {
            this.vc_md5 = vc_md5;
        }

        public Object getVc_forward_url() {
            return vc_forward_url;
        }

        public void setVc_forward_url(Object vc_forward_url) {
            this.vc_forward_url = vc_forward_url;
        }

        public Object getVc_forward_title() {
            return vc_forward_title;
        }

        public void setVc_forward_title(Object vc_forward_title) {
            this.vc_forward_title = vc_forward_title;
        }

        public Object getVc_forward_content() {
            return vc_forward_content;
        }

        public void setVc_forward_content(Object vc_forward_content) {
            this.vc_forward_content = vc_forward_content;
        }

        public Object getCloundKeyword() {
            return cloundKeyword;
        }

        public void setCloundKeyword(Object cloundKeyword) {
            this.cloundKeyword = cloundKeyword;
        }

        public Object getVc_lebal() {
            return vc_lebal;
        }

        public void setVc_lebal(Object vc_lebal) {
            this.vc_lebal = vc_lebal;
        }

        public String getVc_retweet_username() {
            return vc_retweet_username;
        }

        public void setVc_retweet_username(String vc_retweet_username) {
            this.vc_retweet_username = vc_retweet_username;
        }

        public Object getVc_retweet_nickname() {
            return vc_retweet_nickname;
        }

        public void setVc_retweet_nickname(Object vc_retweet_nickname) {
            this.vc_retweet_nickname = vc_retweet_nickname;
        }

        public String getVc_tweetid() {
            return vc_tweetid;
        }

        public void setVc_tweetid(String vc_tweetid) {
            this.vc_tweetid = vc_tweetid;
        }

        public String getVc_tweet_url() {
            return vc_tweet_url;
        }

        public void setVc_tweet_url(String vc_tweet_url) {
            this.vc_tweet_url = vc_tweet_url;
        }

        public String getVc_retweet_count() {
            return vc_retweet_count;
        }

        public void setVc_retweet_count(String vc_retweet_count) {
            this.vc_retweet_count = vc_retweet_count;
        }

        public Object getId() {
            return id;
        }

        public void setId(Object id) {
            this.id = id;
        }

        public Object getVc_title() {
            return vc_title;
        }

        public void setVc_title(Object vc_title) {
            this.vc_title = vc_title;
        }

        public Object getText_description_snippet() {
            return text_description_snippet;
        }

        public void setText_description_snippet(Object text_description_snippet) {
            this.text_description_snippet = text_description_snippet;
        }

        public Object getVc_video_id() {
            return vc_video_id;
        }

        public void setVc_video_id(Object vc_video_id) {
            this.vc_video_id = vc_video_id;
        }

        public Object getVc_channel_thumbnail_local() {
            return vc_channel_thumbnail_local;
        }

        public void setVc_channel_thumbnail_local(Object vc_channel_thumbnail_local) {
            this.vc_channel_thumbnail_local = vc_channel_thumbnail_local;
        }

        public Object getVc_video_local() {
            return vc_video_local;
        }

        public void setVc_video_local(Object vc_video_local) {
            this.vc_video_local = vc_video_local;
        }

        public Object getVc_like_count() {
            return vc_like_count;
        }

        public void setVc_like_count(Object vc_like_count) {
            this.vc_like_count = vc_like_count;
        }

        public Object getVc_dislike_count() {
            return vc_dislike_count;
        }

        public void setVc_dislike_count(Object vc_dislike_count) {
            this.vc_dislike_count = vc_dislike_count;
        }

        public Object getVc_view_count() {
            return vc_view_count;
        }

        public void setVc_view_count(Object vc_view_count) {
            this.vc_view_count = vc_view_count;
        }

        public Object getVc_comment_count() {
            return vc_comment_count;
        }

        public void setVc_comment_count(Object vc_comment_count) {
            this.vc_comment_count = vc_comment_count;
        }

        public int getPage_Rocommend() {
            return page_Rocommend;
        }

        public void setPage_Rocommend(int page_Rocommend) {
            this.page_Rocommend = page_Rocommend;
        }

        public int getRowsPerPage_Rocommend() {
            return rowsPerPage_Rocommend;
        }

        public void setRowsPerPage_Rocommend(int rowsPerPage_Rocommend) {
            this.rowsPerPage_Rocommend = rowsPerPage_Rocommend;
        }

        public int getPage_Hot() {
            return page_Hot;
        }

        public void setPage_Hot(int page_Hot) {
            this.page_Hot = page_Hot;
        }

        public int getRowsPerPage_Hot() {
            return rowsPerPage_Hot;
        }

        public void setRowsPerPage_Hot(int rowsPerPage_Hot) {
            this.rowsPerPage_Hot = rowsPerPage_Hot;
        }

        public int getPage_Account() {
            return page_Account;
        }

        public void setPage_Account(int page_Account) {
            this.page_Account = page_Account;
        }

        public int getRowsPerPage_Account() {
            return rowsPerPage_Account;
        }

        public void setRowsPerPage_Account(int rowsPerPage_Account) {
            this.rowsPerPage_Account = rowsPerPage_Account;
        }

        public Object getKeywords() {
            return keywords;
        }

        public void setKeywords(Object keywords) {
            this.keywords = keywords;
        }

        public Object getKeywordName() {
            return keywordName;
        }

        public void setKeywordName(Object keywordName) {
            this.keywordName = keywordName;
        }

        public String getDataId() {
            return dataId;
        }

        public void setDataId(String dataId) {
            this.dataId = dataId;
        }

        public Object getStart_cjsj() {
            return start_cjsj;
        }

        public void setStart_cjsj(Object start_cjsj) {
            this.start_cjsj = start_cjsj;
        }

        public Object getEnd_cjsj() {
            return end_cjsj;
        }

        public void setEnd_cjsj(Object end_cjsj) {
            this.end_cjsj = end_cjsj;
        }

        public Object getQueryKeyword() {
            return queryKeyword;
        }

        public void setQueryKeyword(Object queryKeyword) {
            this.queryKeyword = queryKeyword;
        }

        public Object getSearchType() {
            return searchType;
        }

        public void setSearchType(Object searchType) {
            this.searchType = searchType;
        }

        public Object getSearchIsTrue() {
            return searchIsTrue;
        }

        public void setSearchIsTrue(Object searchIsTrue) {
            this.searchIsTrue = searchIsTrue;
        }

        public Object getQueryLogic() {
            return queryLogic;
        }

        public void setQueryLogic(Object queryLogic) {
            this.queryLogic = queryLogic;
        }

        public Object getVc_mtlx() {
            return vc_mtlx;
        }

        public void setVc_mtlx(Object vc_mtlx) {
            this.vc_mtlx = vc_mtlx;
        }

        public Object getVc_ssyz() {
            return vc_ssyz;
        }

        public void setVc_ssyz(Object vc_ssyz) {
            this.vc_ssyz = vc_ssyz;
        }

        public Object getVc_ssyw() {
            return vc_ssyw;
        }

        public void setVc_ssyw(Object vc_ssyw) {
            this.vc_ssyw = vc_ssyw;
        }

        public Object getVc_suzz() {
            return vc_suzz;
        }

        public void setVc_suzz(Object vc_suzz) {
            this.vc_suzz = vc_suzz;
        }

        public Object getMenuIdChild() {
            return menuIdChild;
        }

        public void setMenuIdChild(Object menuIdChild) {
            this.menuIdChild = menuIdChild;
        }

        public Object getMenuId() {
            return menuId;
        }

        public void setMenuId(Object menuId) {
            this.menuId = menuId;
        }

        public Object getVc_jkzt() {
            return vc_jkzt;
        }

        public void setVc_jkzt(Object vc_jkzt) {
            this.vc_jkzt = vc_jkzt;
        }

        public int getCurrentPage() {
            return currentPage;
        }

        public void setCurrentPage(int currentPage) {
            this.currentPage = currentPage;
        }

        public int getRowsPerPage() {
            return rowsPerPage;
        }

        public void setRowsPerPage(int rowsPerPage) {
            this.rowsPerPage = rowsPerPage;
        }

        public String getClassifyids() {
            return classifyids;
        }

        public void setClassifyids(String classifyids) {
            this.classifyids = classifyids;
        }

        public String getClassifynumber() {
            return classifynumber;
        }

        public void setClassifynumber(String classifynumber) {
            this.classifynumber = classifynumber;
        }

        public Object getKeyword() {
            return keyword;
        }

        public void setKeyword(Object keyword) {
            this.keyword = keyword;
        }

        public Object getToken() {
            return token;
        }

        public void setToken(Object token) {
            this.token = token;
        }

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public Object getIsgz() {
            return isgz;
        }

        public void setIsgz(Object isgz) {
            this.isgz = isgz;
        }

        public List<?> getList() {
            return list;
        }

        public void setList(List<?> list) {
            this.list = list;
        }

        public List<?> getRows() {
            return rows;
        }

        public void setRows(List<?> rows) {
            this.rows = rows;
        }

        public List<?> getParams() {
            return params;
        }

        public void setParams(List<?> params) {
            this.params = params;
        }
    }
}
