package com.wangtian.message;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wangtian.message.netBean.BaiduBean;
import com.wangtian.message.netBean.NewListResponse;
import com.wangtian.message.netWork.NetWorkBaiduUtils;
import com.wangtian.message.netWork.NetWorkSubscriber;
import com.wangtian.message.netWork.NetWorkUtils;
import com.wangtian.message.view.NoScrollWebView;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class HtmlDetailActivity extends Activity {

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
    @BindView(R.id.html_webview)
    WebView mHtmlWebview;
    @BindView(R.id.traslate_html_webview)
    NoScrollWebView mTraslateHtmlWebview;
    @BindView(R.id.traslate_tv)
    TextView mTraslateTv;
    @BindView(R.id.traslate_ll)
    LinearLayout mTraslateLl;
    private ProgressDialog dialog;
    private static String DETAIL_MD5 = "DETAIL_MD5";
    private NewListResponse.InformationListBean mInformationListBean;
    private String dst = "";

    public static void start(Context context, String DetailMd5) {
        Intent intent = new Intent(context, HtmlDetailActivity.class);
        intent.putExtra(DETAIL_MD5, DetailMd5);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.html_detail);
        ButterKnife.bind(this);
        mRelLeft.setVisibility(View.VISIBLE);
        mImgLeft.setVisibility(View.VISIBLE);
        mRelRight.setVisibility(View.VISIBLE);
        mTxRight.setVisibility(View.VISIBLE);
        mImgRight.setVisibility(View.GONE);
        mTxRight.setText("翻译");
        mRelLeft.setOnClickListener(view -> HtmlDetailActivity.this.onBackPressed());
        mTxTitle.setText("详情");
        mRelRight.setOnClickListener(view -> {
//            if (TextUtils.isEmpty(dst)) {
//                Toast.makeText(HtmlDetailActivity.this, "无法获取翻译内容", Toast.LENGTH_SHORT).show();
//                return;
//            }
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
        initWebview();

        getNet();
    }

    private void getNet() {
        dialog = new ProgressDialog(HtmlDetailActivity.this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        String vc_md5 = getIntent().getStringExtra(DETAIL_MD5);
//        String vc_md5 = "0b11854597dc85313bb9ed25888384f8";
        HashMap<String, String> param = new HashMap<>();
        param.put("token", MyApplication.sLoginBean.getToken());
        param.put("vc_md5", vc_md5);
        param.put("queryKeyword", MyApplication.newsKeyword);
        NetWorkUtils.getInstance().getInterfaceService().getNewsDetail(param)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetWorkSubscriber<NewListResponse.InformationListBean>() {
                    @Override
                    public void onNext(NewListResponse.InformationListBean informationListBean) {
                        mInformationListBean = informationListBean;
                        mHtmlWebview.loadDataWithBaseURL(null,
                                getHtmlData(informationListBean.getCl_bodytext(), informationListBean.getVc_title()
                                        , informationListBean.getDt_pubdate(), informationListBean.getVc_origin()), "text/html", "utf-8", null);

                        tralateInfo();
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

    public void tralateInfo() {
        HashMap<String, String> param = new HashMap<>();
        String htmlData = getHtmlData(mInformationListBean.getCl_bodytext(), mInformationListBean.getVc_title()
                , mInformationListBean.getDt_pubdate(), mInformationListBean.getVc_origin());
        String value = htmlData;
        param.put("q", value);
        param.put("from", "auto");
        param.put("to", "zh");
        param.put("appid", "20190328000282044");
        param.put("salt", "1001");
        param.put("sign", getMD5Str("20190328000282044" + value + "1001" + "QmxpM5J5hoaO5kZvQsrK"));

        NetWorkBaiduUtils.getInstance().getInterfaceService().translate(param)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetWorkSubscriber<BaiduBean>() {
                    @Override
                    public void onNext(BaiduBean baiduBean) {
                        List<BaiduBean.TransResultBean> resultBeanList = baiduBean.getTrans_result();
                        if (resultBeanList == null) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(HtmlDetailActivity.this, "获取翻译失败", Toast.LENGTH_SHORT).show();
                                }
                            });
                            return;
                        }
                        for (BaiduBean.TransResultBean transResultBean : baiduBean.getTrans_result()) {
                            dst = dst + transResultBean.getDst();
                        }
                        mTraslateHtmlWebview.loadDataWithBaseURL(null,
                                getHtmlData(dst, "", "", ""), "text/html", "utf-8", null);


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

    private void initWebview() {
        WebSettings settings = mHtmlWebview.getSettings();
        // 设置WebView支持JavaScript
        settings.setJavaScriptEnabled(true);
        //支持自动适配
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setSupportZoom(true);  //支持放大缩小
        settings.setBuiltInZoomControls(true); //显示缩放按钮
        settings.setBlockNetworkImage(true);// 把图片加载放在最后来加载渲染
        settings.setAllowFileAccess(true); // 允许访问文件
        settings.setSaveFormData(true);
        settings.setGeolocationEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);/// 支持通过JS打开新窗口
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        //设置不让其跳转浏览器
        mHtmlWebview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });

        // 添加客户端支持
        mHtmlWebview.setWebChromeClient(new WebChromeClient());
        // mWebView.loadUrl(TEXTURL);

        //不加这个图片显示不出来
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            mHtmlWebview.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        mHtmlWebview.getSettings().setBlockNetworkImage(false);
        mHtmlWebview.getSettings().setDefaultTextEncodingName("utf-8");

        //允许cookie 不然有的网站无法登陆
        CookieManager mCookieManager = CookieManager.getInstance();
        mCookieManager.setAcceptCookie(true);
        mCookieManager.setAcceptThirdPartyCookies(mHtmlWebview, true);
    }
}
