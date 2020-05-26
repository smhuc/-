package com.wangtian.message.util;

import org.xml.sax.XMLReader;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Editable;
import android.text.Html.TagHandler;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.View.OnClickListener;

import com.wangtian.message.bean.Collect;

public class MxgsaTagHandler implements TagHandler {
    private int sIndex = 0;
    private int eIndex = 0;
    private final Context mContext;
    private BackgroundColorSpan backgroundColorSpanYellow;
    private BackgroundColorSpan backgroundColorSpanRed;

    public MxgsaTagHandler(Context context) {
        mContext = context;
        backgroundColorSpanYellow = new BackgroundColorSpan(Color.YELLOW);
        backgroundColorSpanRed = new BackgroundColorSpan(Color.RED);
    }

    public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
        // TODO Auto-generated method stub
        if (tag.toLowerCase().equals("mark")) {
            if (opening) {
                sIndex = output.length();
            } else {
                eIndex = output.length();
                output.setSpan(backgroundColorSpanYellow, sIndex, eIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        } else
            if (tag.toLowerCase().equals("big")) {
            if (opening) {
                sIndex = output.length();
            } else {
                eIndex = output.length();
                output.setSpan(backgroundColorSpanRed, sIndex, eIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }

    private class MxgsaSpan extends ClickableSpan implements OnClickListener {
        @Override
        public void onClick(View widget) {
            // TODO Auto-generated method stub
            //具体代码，可以是跳转页面，可以是弹出对话框，下面是跳转页面
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(Color.YELLOW);
            ds.setUnderlineText(false);
        }
    }

}