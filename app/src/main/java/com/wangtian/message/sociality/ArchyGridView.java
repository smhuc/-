package com.wangtian.message.sociality;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * @Author Archy Wang
 * @Date 2018/9/29
 * @Description
 */

public class ArchyGridView extends GridView {
    public ArchyGridView(Context context) {
        super(context);
    }

    public ArchyGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ArchyGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
