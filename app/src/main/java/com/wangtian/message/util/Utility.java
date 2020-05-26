package com.wangtian.message.util;

import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashSet;

public class Utility {
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
     * @param keyStr         关键字
     * @param preEndIndex    关键词上一次出现时的结束索引/关键字本次在原始字符串中的结束索引
     */
    public static void findKeyAndSetEvent(SpannableString spannableString, String tempSplitedStr, final String keyStr,
                                          int preEndIndex) {
        final int startIndex = tempSplitedStr.indexOf(keyStr);     //起始索引
        if (startIndex != -1) {
            final int endIndex = startIndex + keyStr.length() - 1;    //终止索引,
            int startIndexInOgirinal = 0;

            if (preEndIndex == 0) {    //关键字第一次出现
                startIndexInOgirinal = startIndex;
                preEndIndex = endIndex;
            } else {      //关键字不是第一次出现
                startIndexInOgirinal = startIndex + preEndIndex + 1;    //加1 是因为截取的字符串索引又是从0开始
                preEndIndex = startIndexInOgirinal + keyStr.length() - 1;   //减1 是因为起始索引已经占了一个索引
            }

            spannableString.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    //点击事件弹窗+请求服务器数据
//                    Toast.makeText(ShowH5TextActivity.this, "点我干嘛？关键字：" + keyStr, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    //super.updateDrawState(ds);
                    ds.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                    ds.setTextSize(58);
                    ds.setColor(Color.RED);      //更改超链接颜色(此颜色要与H5中关键字的 font 颜色一致)
                    ds.setUnderlineText(false);     //不展示下划线
                }
            }, startIndexInOgirinal, preEndIndex + 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            //}, startIndexInOgirinal, preEndIndex, Spanned.SPAN_INCLUSIVE_INCLUSIVE);  //这样的话，非第一次出现的只会将第一个字符加上超链接

            tempSplitedStr = tempSplitedStr.substring(endIndex + 1);  //截取字符串，+1 表示从关键词后面截取，不含关键字；不加1 的话从关键词最后一个字开始截取
            findKeyAndSetEvent(spannableString, tempSplitedStr, keyStr, preEndIndex);     //递归调用
        }
    }

    /**
     * 获取关键字，并使用Set存储，实现去重
     */
    public static HashSet<String> getAllKeyWords(String H5String) {
        HashSet<String> keysSet = new HashSet<>();
        Document document = Jsoup.parse(H5String);
        Elements elementsList = document.getElementsByTag("big"); //在JSOUP中，Elements类继承自ArrayList
        if (null != elementsList) {
            for (Element element : elementsList) {
                keysSet.add(element.text());
            }
        }

        return keysSet;
    }
}
