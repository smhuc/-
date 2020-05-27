package com.wangtian.message.sociality;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wangtian.message.MyApplication;
import com.wangtian.message.R;
import com.wangtian.message.netBean.BaiduBean;
import com.wangtian.message.netBean.SocialNetListBean;
import com.wangtian.message.netWork.NetWorkBaiduUtils;
import com.wangtian.message.netWork.NetWorkSubscriber;
import com.wangtian.message.netWork.NetWorkUtils;
import com.wangtian.message.util.GlideUtils;
import com.wangtian.message.util.MxgsaTagHandler;
import com.wangtian.message.util.ScreenParam;
import com.wangtian.message.view.CommentPopWindow;
import com.wangtian.message.view.NoScrollWebView;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SocialHtmlDetailActivity extends Activity implements CommentPopWindow.LoadDataInterface {
    private static final String TAG = SocialHtmlDetailActivity.class.getSimpleName();

    @BindView(R.id.img_left)
    ImageView mImgLeft;
    @BindView(R.id.rel_left)
    RelativeLayout mRelLeft;
    @BindView(R.id.tx_title)
    TextView mTxTitle;
    @BindView(R.id.img_right)
    ImageView mImgRight;
    @BindView(R.id.tx_right)
    TextView mTxRight;
    @BindView(R.id.rel_right)
    RelativeLayout mRelRight;

    @BindView(R.id.traslate_html_webview)
    NoScrollWebView mTraslateHtmlWebview;
    @BindView(R.id.traslate_tv)
    TextView mTraslateTv;
    @BindView(R.id.traslate_ll)
    LinearLayout mTraslateLl;
    @BindView(R.id.user_imge_iv)
    ImageView mUserImgeIv;
    @BindView(R.id.name_tv)
    TextView mNameTv;
    @BindView(R.id.english_name_tv)
    TextView mEnglishNameTv;
    @BindView(R.id.content_time_tv)
    TextView mContentTimeTv;
    @BindView(R.id.content_tv)
    TextView mContentTv;
    @BindView(R.id.gv)
    ArchyGridView mGv;
    @BindView(R.id.comment_num_tv)
    TextView mCommentNumTv;
    @BindView(R.id.retweet_num_tv)
    TextView mRetweetNumTv;
    @BindView(R.id.favorite_num_tv)
    TextView mFavoriteNumTv;
    @BindView(R.id.ll_comment_num_tv)
    LinearLayout mLlCommentNum;
    @BindView(R.id.root_ll)
    LinearLayout root;

    private ProgressDialog dialog;
    private static String DETAIL_MD5 = "DETAIL_MD5";
    private SocialNetListBean.IndexVOListBean mInformationListBean;
    private SocialNetListBean.ReplyListBean mReplyListBean;
    private boolean mShowTr = false;
    private String mDst;

    private String forward_count = "";
    private String retweet_count = "";
    private String favorites_count = "";
    private String reply_count = "";

    private int page = 1;
    private int mCount = 0;
    private CommentPopWindow commentPopWindow;
    private List<SocialNetListBean.ReplyListBean> mDatas = null;

    public static void start(Context context, String DetailMd5) {
        Intent intent = new Intent(context, SocialHtmlDetailActivity.class);
        intent.putExtra(DETAIL_MD5, DetailMd5);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_html_detail);
        ButterKnife.bind(this);
        ScreenParam.getInstance().init(this);
        mDatas = new ArrayList<>();
        mRelLeft.setVisibility(View.VISIBLE);
        mImgLeft.setVisibility(View.VISIBLE);
        mRelRight.setVisibility(View.VISIBLE);
        mTxRight.setVisibility(View.VISIBLE);
        mImgRight.setVisibility(View.GONE);
        mTxRight.setText("翻译");
        mRelLeft.setOnClickListener(view -> SocialHtmlDetailActivity.this.onBackPressed());
        mTxTitle.setText("社交详情");
        mRelRight.setOnClickListener(view -> {
            if (mTraslateTv.getVisibility() == View.GONE) {
                mTraslateLl.setVisibility(View.VISIBLE);
                mTraslateTv.setVisibility(View.VISIBLE);
                mTxRight.setText("原文");
            } else {
                mTraslateLl.setVisibility(View.GONE);
                mTraslateTv.setVisibility(View.GONE);
                mTxRight.setText("翻译");
            }
        });
        mRelRight.setOnClickListener(v -> {
            if (TextUtils.isEmpty(mDst)) {
                Toast.makeText(this, "获取翻译失败", Toast.LENGTH_SHORT).show();
                return;
            }
            if (mShowTr) {
                mShowTr = false;
                mContentTv.setText(Html.fromHtml(mInformationListBean.getText_post_content(), null,
                        new MxgsaTagHandler(SocialHtmlDetailActivity.this)));
            } else {
                mShowTr = true;
                mContentTv.setText(Html.fromHtml(mDst, null,
                        new MxgsaTagHandler(SocialHtmlDetailActivity.this)));
            }

        });

        getNet();

        mLlCommentNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String commentNum = mCommentNumTv.getText().toString().trim();
                if (!TextUtils.isEmpty(commentNum)) {
                    if (Long.parseLong(commentNum) <= 0) {
                        Toast.makeText(SocialHtmlDetailActivity.this, "暂无评论内容~", Toast.LENGTH_SHORT).show();
                    } else {
                        if (mDatas != null && mDatas.size() > 0) {
                            showPopContent();
                        } else {
                            Toast.makeText(SocialHtmlDetailActivity.this, "获取评论内容失败，请退出当前页面重进~", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        });
    }

    private void showPopContent() {
        if (commentPopWindow == null) {
            commentPopWindow = new CommentPopWindow(SocialHtmlDetailActivity.this, this, mCount);
        }
        commentPopWindow.setHeight(ScreenParam.height * 2 / 3);
        commentPopWindow.showAtLocation(root, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        Notify();
    }

    private void Notify() {
        if (commentPopWindow != null) {
            commentPopWindow.notifyData(mDatas);
        }
    }

    private void getNet() {
        dialog = new ProgressDialog(SocialHtmlDetailActivity.this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        String vc_md5 = getIntent().getStringExtra(DETAIL_MD5);
//        String vc_md5 = "0b11854597dc85313bb9ed25888384f8";
        HashMap<String, String> param = new HashMap<>();
        param.put("token", MyApplication.sLoginBean.getToken());
        param.put("dataId", vc_md5);
        param.put("queryKeyword", MyApplication.socialKeyword);
        NetWorkUtils.getInstance().getInterfaceService().getsocialDetail(param)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetWorkSubscriber<SocialNetListBean.IndexVOListBean>() {
                    @Override
                    public void onNext(SocialNetListBean.IndexVOListBean informationListBean) {
                        mInformationListBean = informationListBean;
                        GlideUtils.loadCirclePic(SocialHtmlDetailActivity.this, informationListBean.getVc_photo_url_local(), mUserImgeIv);
                        mNameTv.setText(informationListBean.getVc_nickname());
                        String name = !"0".equals(informationListBean.getVc_username()) ? informationListBean.getVc_username() : "";
                        mEnglishNameTv.setText(name);
                        mContentTimeTv.setText(informationListBean.getDt_pubdate());
                        String content = informationListBean.getText_post_content();
//                        String content = "<big>武漢</big>の空港で受け入れる<mark>日本</mark>の飛行機の便数が決まっているので、座席数第一で考えます。 https://t.co/CVEDqmMwf";
                        Log.d(TAG, "content = : " + content);
                        mContentTv.setText(Html.fromHtml(content, null, new MxgsaTagHandler(SocialHtmlDetailActivity.this)));
                        forward_count = !TextUtils.isEmpty((String) informationListBean.getVc_forward_count()) ? (String) informationListBean.getVc_forward_count() : "0";
                        retweet_count = !TextUtils.isEmpty(informationListBean.getVc_retweet_count()) ? informationListBean.getVc_retweet_count() : "0";
                        favorites_count = !TextUtils.isEmpty(informationListBean.getVc_favorites_count()) ? informationListBean.getVc_favorites_count() : "0";
                        reply_count = !TextUtils.isEmpty(informationListBean.getVc_reply_count()) ? informationListBean.getVc_reply_count() : "0";
                        mCommentNumTv.setText(reply_count);
                        mRetweetNumTv.setText("1".equals(informationListBean.getSecondType()) ? forward_count : retweet_count);
                        mFavoriteNumTv.setText(favorites_count);
                        if (TextUtils.equals(informationListBean.getText_picture_url_local(), "0")) {
                            mGv.setVisibility(View.GONE);
                        } else {
                            mGv.setVisibility(View.VISIBLE);
                            String[] split = informationListBean.getText_picture_url_local().split(",");
                            ArrayList<String> strings = new ArrayList<>();
                            for (String s : split) {
                                if (!TextUtils.isEmpty(s)) {
                                    strings.add(s);

                                }
                            }
                            DisPicGAdapter disPicGAdapter = new DisPicGAdapter();
                            disPicGAdapter.setData(strings);
                            mGv.setAdapter(disPicGAdapter);


                        }
                        tralateInfo(mInformationListBean.getText_post_content(), null);
                        getCommentContent(false);

                    }
                });
    }

    private void getCommentContent(boolean isRefresh) {
        String vc_md5 = getIntent().getStringExtra(DETAIL_MD5);
        HashMap<String, String> param = new HashMap<>();
        param.put("token", MyApplication.sLoginBean.getToken());
        param.put("dataId", vc_md5);
        param.put("queryKeyword", MyApplication.socialKeyword);
        param.put("cloundKeyword", "");//暂时没用
        param.put("page_comment", String.valueOf(page));
        param.put("rowsPerPage_comment", String.valueOf(10));
        NetWorkUtils.getInstance().getInterfaceService().getsocialDetailReply(param)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetWorkSubscriber<SocialNetListBean>() {

                    @Override
                    public void onNext(SocialNetListBean socialNetListBean) {
                        mCount = socialNetListBean.getCount();
                        Log.d(TAG, "mCount = " + mCount);
                        Log.d(TAG, "page = " + page);
                        List<SocialNetListBean.ReplyListBean> replyListBeans = socialNetListBean.getReplylist();
                        if (replyListBeans != null) {
                            if (page == 1) {
                                mDatas.clear();
                            }
                            mDatas.addAll(replyListBeans);
                            page++;
                        }
                        if (isRefresh) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Notify();
                                }
                            });
                        }
                    }
                });
    }


    public String getHtmlData(String content, String title, String time, String from) {
        String head = "<head>" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no\"> " +
                "<style>img{max-width: 100%; width:auto; height:auto!important;}</style>" +
                "</head>";
        return "<html>" + head + "<body>" + "<div>\n" +
                "  <p style=\"font-size: 34px; font-weight:900; color: #000000; margin-top: 10px;word-break:break-all;\">" + title + "</p>\n" +
                "</div>\n" +
                "  <p style=\"font-size: 20px; color: #999999;line-height: 0px\">" + from + "</p>" +
                "  <p style=\"font-size: 20px; color: #999999;line-height: 0px\">" + time + "</p>" +
                content + "</body></html>";

    }

    public void tralateInfo(String value, TextView tv) {
        HashMap<String, String> param = new HashMap<>();
        param.put("q", value);
        param.put("from", "auto");
        param.put("to", "zh");
        param.put("appid", "20190328000282044");
        param.put("salt", "1001");
        param.put("sign", getMD5Str("20190328000282044" + value + "1001" + "QmxpM5J5hoaO5kZvQsrK"));

        NetWorkBaiduUtils.getInstance().getInterfaceService().translate(param)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaiduBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(BaiduBean baiduBean) {

                        List<BaiduBean.TransResultBean> resultBeanList = baiduBean.getTrans_result();
                        if (resultBeanList == null) {
                            return;
                        }
                        if (tv != null) {
                            String content = "";
                            for (BaiduBean.TransResultBean transResultBean : baiduBean.getTrans_result()) {
                                content = content + transResultBean.getDst();
                            }
                            tv.setText(content);
                            return;
                        }
                        mDst = "";
                        for (BaiduBean.TransResultBean transResultBean : baiduBean.getTrans_result()) {
                            mDst = mDst + transResultBean.getDst();
                        }
                    }
                });
    }

    public static String getMD5Str(String str) {
        try {
            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 计算md5函数
            md.update(str.getBytes());
            // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
            return new BigInteger(1, md.digest()).toString(16);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String StripHT(String strHtml) {
        String txtcontent = strHtml.replaceAll("</?[^>]+>", ""); //剔出<html>的标签
        txtcontent = txtcontent.replaceAll("<a>\\s*|\t|\r|\n</a>", "");//去除字符串中的空格,回车,换行符,制表符
        return txtcontent;
    }

    @Override
    public void refresh() {
        page = 1;
        Log.d(TAG, "refresh: ");
        getCommentContent(true);
    }

    @Override
    public void loadMore() {
        Log.d(TAG, "loadMore: ");
        getCommentContent(true);
    }

    @Override
    public void loadTranslate(TextView tv, String content) {
        tralateInfo(content, tv);

    }
}
