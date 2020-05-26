package com.wangtian.message.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.socialize.weixin.view.WXCallbackActivity;
import com.wangtian.message.base.BaseMenuActivity;
public class WXEntryActivity extends WXCallbackActivity {
	
	IWXAPI mWxApi;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		IWXAPI mWxApi = WXAPIFactory.createWXAPI(this, BaseMenuActivity.apikey, false);
		mWxApi.handleIntent(getIntent(), this);
		finish();
	}
	/***
	 * 请求微信的相应码
	 * @author YOLANDA
	 * @param arg0
	 */
	@Override
	public void onResp(BaseResp baseResp) {
		switch (baseResp.errCode) {
		case BaseResp.ErrCode.ERR_OK:
			Toast.makeText(WXEntryActivity.this, "分享成功", 0).show();
			break;
		case BaseResp.ErrCode.ERR_USER_CANCEL:
			Toast.makeText(WXEntryActivity.this, "取消", 0).show();;//取消
			break;
		case BaseResp.ErrCode.ERR_AUTH_DENIED:
			Toast.makeText(WXEntryActivity.this, "被拒绝", 0).show();;//被拒绝
			break;
		default:
			Toast.makeText(WXEntryActivity.this, "返回", 0).show();;//返回
			break;
		}
	}

}