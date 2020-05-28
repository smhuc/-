package com.wangtian.message.util;

import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class Utility {
    private final static String TAG_BIG = "big";
    private final static String TAG_MARK = "mark";
    private static String TAG = Utility.class.getSimpleName();

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    public static String getData(String content) {
        content = content.replaceAll("\"", "'");
        return "<html><head></head><body><p>"
                + content
                + "</p>"
                + "</body>" + "</html>";
    }


    /**
     * 找出单个关键字每一次出现的位置并为其增加点击事件
     *
     * @param tempSplitedStr 被切割后的新字符串
     * @param key            关键字
     * @param value          关键字
     * @param preEndIndex    关键词上一次出现时的结束索引/关键字本次在原始字符串中的结束索引
     */
    public static void findKeyAndSetEvent(SpannableString spannableString, String tempSplitedStr, String key, String value,
                                          int preEndIndex) {

        //起始索引
        final int startIndex = tempSplitedStr.indexOf(value);
        if (startIndex != -1) {
            //终止索引
            final int endIndex = startIndex + value.length() - 1;
            int startIndexInOgirinal = 0;

            if (preEndIndex == 0) {
                //关键字第一次出现
                startIndexInOgirinal = startIndex;
                preEndIndex = endIndex;
            } else {
                //关键字不是第一次出现
                startIndexInOgirinal = startIndex + preEndIndex + 1;    //加1 是因为截取的字符串索引又是从0开始
                preEndIndex = startIndexInOgirinal + value.length() - 1;   //减1 是因为起始索引已经占了一个索引
            }
            if (TAG_MARK.equals(key)) {
                BackgroundColorSpan yellow = new BackgroundColorSpan(Color.YELLOW);
                spannableString.setSpan(yellow, startIndexInOgirinal, preEndIndex + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else if (TAG_BIG.equals(key)) {
                spannableString.setSpan(new ClickableSpan() {
                    @Override
                    public void onClick(View widget) {
                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
                        //super.updateDrawState(ds);
                        ds.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                        ds.setTextSize(48f);
                        ds.setColor(Color.RED);
                        ds.setUnderlineText(false);
                    }
                }, startIndexInOgirinal, preEndIndex + 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                //}, startIndexInOgirinal, preEndIndex, Spanned.SPAN_INCLUSIVE_INCLUSIVE);  //这样的话，非第一次出现的只会将第一个字符加上超链接

            }

            tempSplitedStr = tempSplitedStr.substring(endIndex + 1);  //截取字符串，+1 表示从关键词后面截取，不含关键字；不加1 的话从关键词最后一个字开始截取
            findKeyAndSetEvent(spannableString, tempSplitedStr, key, value, preEndIndex);     //递归调用
        }
    }

    public static String removeTag(String H5String) {
        Whitelist whitelist = Whitelist.none();
        whitelist.removeTags(TAG_BIG, TAG_MARK);
        return Jsoup.clean(H5String, whitelist);
    }

    /**
     * 获取关键字，并使用Set存储，实现去重
     */
    public static List<Object> getAllKeyWords(String H5String) {
        List<Object> list = new ArrayList<>();
        Document document = Jsoup.parse(H5String);
        Elements elementsListB = document.getElementsByTag(TAG_BIG);
        Elements elementsListM = document.getElementsByTag(TAG_MARK);
        if (null != elementsListB) {
            for (Element element : elementsListB) {
                list.add(new Object(element.tagName(), element.text()));
            }
        }
        if (null != elementsListM) {
            for (Element element : elementsListM) {
                list.add(new Object(element.tagName(), element.text()));
            }
        }
        return list;
    }

    public static SpannableString getConvertContent(String H5String) {
        final List<Object> list = Utility.getAllKeyWords(H5String);
        Log.d(TAG, "size = " + list.size());
        SpannableString spannableStr = new SpannableString(Html.fromHtml(H5String));
        for (Object object : list) {
            Utility.findKeyAndSetEvent(spannableStr, spannableStr.toString(), object.key, object.value, 0);
        }
        return spannableStr;
    }

    private static class Object {
        public String key;
        public String value;

        public Object(String key, String value) {
            this.key = key;
            this.value = value;
        }
    }
}
