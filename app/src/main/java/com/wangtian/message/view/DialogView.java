package com.wangtian.message.view;

import android.app.Activity;
import android.app.Dialog;
import android.text.Html;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wangtian.message.R;


/**
 * dailog通用版 模板(双按钮 或单按钮)
 * 
 * @author zhoupan
 * @create 20150506
 * */
public class DialogView extends Dialog {
	Activity teldBaseActivity;
	View bgView;
	RelativeLayout bgRelayout;
	Button rightBtn;
	Button leftBtn;
	TextView textViewCon;
	OnBtnClickListener onBtnClick;

	DialogView dailogView;
	private boolean isSingleBtn;

	public DialogView(Activity teldBaseActivity) {
		super(teldBaseActivity, R.style.MyDialog);
		this.teldBaseActivity = teldBaseActivity;
		init();
	}



	public void setTitle(String titleStr) {
		findViewById(R.id.title_top).setVisibility(View.VISIBLE);
		((TextView) findViewById(R.id.title_top_con)).setText(titleStr);
		LinearLayout.LayoutParams layoutP = (LinearLayout.LayoutParams) textViewCon
				.getLayoutParams();
		layoutP.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
	}

	public void setTitle(int resId) {
		findViewById(R.id.title_top).setVisibility(View.VISIBLE);
		((TextView) findViewById(R.id.title_top_con)).setText(resId);
		LinearLayout.LayoutParams layoutP = (LinearLayout.LayoutParams) textViewCon
				.getLayoutParams();
		layoutP.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
	}

	private void init() {
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		View view = LayoutInflater.from(teldBaseActivity).inflate(
				R.layout.layout_defalut_dialog, null);
		rightBtn = (Button) view.findViewById(R.id.dialog_btn_confrim);
		leftBtn = (Button) view.findViewById(R.id.dialog_btn_cancel);
		textViewCon = (TextView) view.findViewById(R.id.dialog_tv_desc);
		textViewCon.setGravity(Gravity.CENTER);
		FrameLayout.LayoutParams linLayoutParams = new FrameLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		view.setLayoutParams(linLayoutParams);

		// linLayoutParams.
		// view.setMinimumWidth(ScreenUtils.SCREEN_WIDTH - ToolUtils.dp2px(32));
		setContentView(view);
		setCancelable(false);
		rightBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (null != onBtnClick) {
					onBtnClick.doConfirm();
				}
			}
		});

		leftBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (null != onBtnClick) {
					onBtnClick.doCancel();
				}

			}
		});
	}

	/**
	 * 设置内容文字
	 * */
	public void setContentText(int contentId) {
		textViewCon.setText(contentId);
	}

	/**
	 * 设置内容文字
	 * */
	public void setContentText(String conStr) {
		textViewCon.setText(conStr);
	}
	/**
	 * 设置内容文字
	 * */
	public void setContentTextType(String conStr) {
		textViewCon.setGravity(Gravity.LEFT);
		textViewCon.setText(conStr);
	}

	/**
	 * 设置内容文字
	 * */
	public void setContentHtmlText(String conStr) {
		textViewCon.setText(Html.fromHtml(conStr));
	}

	/**
	 * 设置按钮文字
	 * */
	public void setBtnText(int resLeftId, int resRightId) {
		rightBtn.setText(resLeftId);
		leftBtn.setText(resRightId);
	}

	/**
	 * 设置按钮文字
	 * */
	public void setBtnText(String resLeftStr, String resRightStr) {
		rightBtn.setText(resLeftStr);
		if (null != resRightStr) {
			leftBtn.setText(resRightStr);
		}
	}

	/**
	 * 设置按钮文字
	 * */
	public void setSingleBtnText(int resLeftId) {
		rightBtn.setText(resLeftId);
		isSingleBtn = true;
	}

	// 是否是单按钮
	public boolean isSingleBtn() {
		return isSingleBtn;
	}

	/**
	 * 设置按钮文字
	 * */
	public void setSingleBtnText(String resLeftStr) {
		rightBtn.setText(resLeftStr);
		isSingleBtn = true;
	}

	/**
	 * 设置按钮背景
	 * */
	public void setBtnBgResource(int resLeftId, int resRightId) {
		rightBtn.setBackgroundResource(resLeftId);
		leftBtn.setBackgroundResource(resRightId);
	}

	/**
	 * 设置按钮背景颜色
	 * */
	public void setBtnBgColor(int colorLeftId, int colorRightId) {
		rightBtn.setBackgroundColor(colorLeftId);
		leftBtn.setBackgroundColor(colorRightId);
	}



	public void setOnBtnClickListener(OnBtnClickListener onBtnClick) {
		this.onBtnClick = onBtnClick;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (isSingleBtn) {
			teldBaseActivity.onKeyDown(keyCode, event);
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onBackPressed() {
		if (isSingleBtn) {
			return;
		}

		dismiss();
		super.onBackPressed();
	}

	@Override
	public void dismiss() {
		super.dismiss();
	}

	public interface OnBtnClickListener {

		public void doConfirm();

		public void doCancel();
	}

	@Override
	public void show() {
		super.show();

	}
}
