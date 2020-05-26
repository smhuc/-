package com.wangtian.message.base;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX.Req;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.bean.StatusCode;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.wangtian.message.CollectActivity;
import com.wangtian.message.LoginActivity;
import com.wangtian.message.MyApplication;
import com.wangtian.message.NewsListActivity;
import com.wangtian.message.R;
import com.wangtian.message.SetingMainActivity;
import com.wangtian.message.WarnMainActivity;
import com.wangtian.message.sociality.SocialListActivity;
import com.wangtian.message.util.NewToast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class BaseMenuActivity extends Activity implements OnClickListener{
	private Activity activity;
	private PopupWindow pop;
	private SharedPreferences sp;
	private final UMSocialService mController = UMServiceFactory
	            .getUMSocialService("com.umeng.share");
	private Tencent mTencent;
	private String title,contact,url;
//	private IWeiboShareAPI  mWeiboShareAPI = null;
	
    public final String SCOPE = 
            "email,direct_messages_read,direct_messages_write,"
            + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
            + "follow_app_official_microblog," + "invitation_write";

	/**
	 * 这里是传activity过，便于操作公共的界面
	 *			 必须放在这里面调用的其他方法的前面
	 * @param activity
	 */
	public void setActivity(Activity activity,String title) {
		this.activity = activity;
		MyApplication.getInstance().addActivity(activity);
		TextView text = (TextView) activity.findViewById(R.id.tx_title);
		text.setText(title);
		SlidingMenu menu = new SlidingMenu(this);
        menu.setMode(SlidingMenu.LEFT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        menu.setShadowWidthRes(R.dimen.slidingmenu_open);
        menu.setShadowDrawable(R.drawable.shadow);
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        menu.setFadeDegree(0.35f);
        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        menu.setMenu(R.layout.slide_menu);
        initMenu(menu);
        regto();
        mTencent = Tencent.createInstance("1104707860", this.getApplicationContext());
        
	}
	
	
/**
 * 获取分享的应用图标
 * @return
 */
	private Bitmap getBitmap() {
		File file = new File(getExternalCacheDir()+File.pathSeparator+"Message"+File.pathSeparator+"share.png");
		if(file.exists()){
		}else{
			AssetManager manager = activity.getAssets();
			InputStream in = null;
			FileOutputStream out = null;
			try {
				in = manager.open("ic_launcher.png");
				File file2 = new File(getExternalCacheDir()+File.pathSeparator+"Message");
				file2.mkdir();
				file.createNewFile();
				out = new FileOutputStream(file);
				byte[] lenght = new byte[1024];
				while (in.read(lenght) != -1) {
					out.write(lenght);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				try {
					if(out != null){
						out.close();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
		return bitmap;
	}
    
    
	public final static String apikey = "wxf4f9119ca062e1a6";//微信的key
	private IWXAPI api;
	private void regto(){
		api = WXAPIFactory.createWXAPI(activity, apikey);
		boolean a = api.registerApp(apikey);
	}
	/**
	 * 分享到微信
	 */
	private void share(boolean friend){
//		WXTextObject textObject = new WXTextObject();
//		textObject.text =contact;
		WXWebpageObject web = new WXWebpageObject();
		web.webpageUrl = url;
		WXMediaMessage message = new WXMediaMessage();
		if(friend){
			message.description = title;
			message.title = contact;
		}else{
			message.description = contact;
			message.title = title;
		}
		message.mediaObject = web;
		Bitmap bitmap = getBitmap();
		if(bitmap != null){
			message.thumbData = getBitmapBytes(bitmap, false);
		}
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.message = message;
		req.transaction = String.valueOf(System.currentTimeMillis());
		if(api.getWXAppSupportAPI() < 0x21020001){//小于是不支持微信朋友圈的
			Toast.makeText(activity, "您的微信版本过低，不支持朋友圈的分享", 1).show();
		}
		if(friend){
			req.scene = Req.WXSceneTimeline;
		}else{
			req.scene = Req.WXSceneSession;
		}
		boolean a = api.sendReq(req);
		if(a){
			Toast.makeText(activity, "分享中...", 0).show();
		}else{
			Toast.makeText(activity, "分享失败", 0).show();
		}
	}
	private byte[] getBitmapBytes(Bitmap bitmap, boolean paramBoolean) {
        Bitmap localBitmap = Bitmap.createBitmap(80, 80, Bitmap.Config.RGB_565);
        Canvas localCanvas = new Canvas(localBitmap);
        int i;
        int j;
        if (bitmap.getHeight() > bitmap.getWidth()) {
            i = bitmap.getWidth();
            j = bitmap.getWidth();
        } else {
            i = bitmap.getHeight();
            j = bitmap.getHeight();
        }
        while (true) {
            localCanvas.drawBitmap(bitmap, new Rect(0, 0, i, j), new Rect(0, 0,80, 80), null);
            if (paramBoolean)
                bitmap.recycle();
            ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
            localBitmap.compress(Bitmap.CompressFormat.JPEG, 100,
                    localByteArrayOutputStream);
            localBitmap.recycle();
            byte[] arrayOfByte = localByteArrayOutputStream.toByteArray();
            try {
                localByteArrayOutputStream.close();
                return arrayOfByte;
            } catch (Exception e) {
                e.printStackTrace();
            }
            i = bitmap.getHeight();
            j = bitmap.getHeight();
        }
        }

	/**
	 * 初始化分享，添加分享的内容
	 * @param title   分享的标题
	 * @param contact  分享的内容
	 * @param url  分享后跳转的url
	 */
	public void Share(String title,String contact,String url) {
		//新浪分享
		if(title.length() > 50){
			this.title = title.subSequence(0, 40)+"...";
		}else{
			this.title = title;
		}
		if(contact.length() > 200){
			this.contact = contact.subSequence(0, 140)+"...";
		}else{
			this.contact = contact;
		}
		this.url = url;
		
	}
	private LinearLayout hotday,warn,info,collect;
	public void initMenu(SlidingMenu menu) {
		sp = getSharedPreferences("user", Activity.MODE_PRIVATE);
		String last = sp.getString("last_user", null);
		TextView name = (TextView) menu.findViewById(R.id.tx_name);
		if(last != null){
			name.setText(last);
		}
		TextView exit = (TextView) menu.findViewById(R.id.tx_exit);
		hotday = (LinearLayout) menu.findViewById(R.id.lin_hotday);
		warn = (LinearLayout) menu.findViewById(R.id.lin_warn);
		info = (LinearLayout) menu.findViewById(R.id.lin_info);
		collect = (LinearLayout) menu.findViewById(R.id.lin_collect);
		SharedPreferences sp = activity.getSharedPreferences("frist", Activity.MODE_PRIVATE);
		int a = sp.getInt("show", 0);
		hotday.setBackground(null);
		warn.setBackground(null);
		info.setBackground(null);
		collect.setBackground(null);
		if(a == 1){
			hotday.setBackgroundColor(getResources().getColor(R.color.bg_select));
		}else if(a == 2){
			warn.setBackgroundColor(getResources().getColor(R.color.bg_select));
		}else if(a == 3){
			info.setBackgroundColor(getResources().getColor(R.color.bg_select));
		}else if(a == 4){
			collect.setBackgroundColor(getResources().getColor(R.color.bg_select));
		}
		LinearLayout set = (LinearLayout) menu.findViewById(R.id.lin_set);
		hotday.setOnClickListener(this);
		set.setOnClickListener(this);
		exit.setOnClickListener(this);
		warn.setOnClickListener(this);
		info.setOnClickListener(this);
		collect.setOnClickListener(this);
	}
	/**
	 * 这里对应的是标题栏的左边的空间 , 不调用则左边没有控件
	 * @param back 0 是表示  左边没有控件   1表示 是箭头    2  表示 是其他
	 * @return  
	 */
	public ImageView left(int back){
		RelativeLayout rel_back = (RelativeLayout) activity.findViewById(R.id.rel_left);
		ImageView img_back = (ImageView) activity.findViewById(R.id.img_left);
		if(back == 0){
			return null;
		}else if(back == 1){
			rel_back.setVisibility(View.VISIBLE);
			rel_back.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					activity.finish();
				}
			});
			return img_back;
		}else if(back == 2){
			return img_back;
		}
		return null;
	}
	/**
	 * 这里对应的是标题栏的右边的空间 , 不调用则右边没有控件
	 * @param seach 0 是表示  左边没有控件   1表示 是搜索    2  表示 是其他   3表示  有两个控件   返回的是前面一个
	 * @return  
	 */
	public View right(int seach){
		RelativeLayout back = (RelativeLayout) activity.findViewById(R.id.rel_right);
		ImageView img_back = (ImageView) activity.findViewById(R.id.img_right);
		if(seach == 0){
			return null;
		}else if(seach == 1){
			back.setVisibility(View.VISIBLE);
			img_back.setVisibility(View.VISIBLE);
			return back;
		}else if(seach == 2){
			img_back.setVisibility(View.VISIBLE);
			return img_back;
		}else if(seach == 3){
			img_back.setVisibility(View.VISIBLE);
			back.setVisibility(View.VISIBLE);
			ImageView share = (ImageView) activity.findViewById(R.id.img_right1);
			RelativeLayout rel_share = (RelativeLayout) activity.findViewById(R.id.rel_right1);
			rel_share.setVisibility(View.VISIBLE);
			share.setVisibility(View.VISIBLE);
			rel_share.setOnClickListener(this);
			return img_back;
		}
		return null;
	}
	@Override
	public void onClick(View v) {
		Intent intent;
		SharedPreferences sp = activity.getSharedPreferences("frist", Activity.MODE_PRIVATE);
		Editor ed = sp.edit();
		switch (v.getId()) {
		case R.id.lin_set:
			intent = new Intent(activity, SetingMainActivity.class);
			startActivity(intent);
			break;
		case R.id.lin_collect:
			hotday.setBackground(null);
			warn.setBackground(null);
			info.setBackground(null);
			ed.putInt("show", 4);
			ed.commit();
			v.setBackgroundColor(getResources().getColor(R.color.bg_select));
			intent = new Intent(activity, CollectActivity.class);
			startActivity(intent);
			break;
		case R.id.lin_info:
			hotday.setBackground(null);
			warn.setBackground(null);
			collect.setBackground(null);
			ed.putInt("show", 3);
			ed.commit();
			v.setBackgroundColor(getResources().getColor(R.color.bg_select));
			intent = new Intent(activity, SocialListActivity.class);
			startActivity(intent);
			break;
		case R.id.lin_warn:
			hotday.setBackground(null);
			info.setBackground(null);
			collect.setBackground(null);
			ed.putInt("show", 2);
			ed.commit();
			intent = new Intent(activity, WarnMainActivity.class);
			startActivity(intent);
			v.setBackgroundColor(getResources().getColor(R.color.bg_select));
			break;
		case R.id.lin_hotday:
			warn.setBackground(null);
			info.setBackground(null);
			collect.setBackground(null);
			ed.putInt("show", 1);
			ed.commit();
			v.setBackgroundColor(getResources().getColor(R.color.bg_select));
			intent = new Intent(activity, NewsListActivity.class);
			startActivity(intent);
			break;
		case R.id.tx_exit:
			initpop();
			break;
		case R.id.tx_exit_ok:
			XGPushManager.registerPush(activity,"*");
//			XGPushManager.deleteTag(activity,"group"+MyApplication.user.getGroupid());
			intent = new Intent(activity, LoginActivity.class);
			startActivity(intent);
		case R.id.tx_cancal:
			if(pop != null && pop.isShowing()){
				pop.dismiss();
			}
			break;
		case R.id.rel_right1:
			initshare();
			break;
		case R.id.tx_friend:
			if(pop != null && pop.isShowing()){
				pop.dismiss();
				share(true);
			}
			break;
		case R.id.tx_weixin:
			if(pop != null && pop.isShowing()){
				pop.dismiss();
				share(false);
			}
			break;
		case R.id.tx_qq:
			if(pop != null && pop.isShowing()){
				pop.dismiss();
			}
			Bundle bundle = new Bundle();
			bundle.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
			bundle.putString(QQShare.SHARE_TO_QQ_TITLE, title);
			bundle.putString(QQShare.SHARE_TO_QQ_SUMMARY,  contact);
			bundle.putString(QQShare.SHARE_TO_QQ_TARGET_URL,  url);
			File file = new File(getExternalCacheDir()+File.pathSeparator+"Message"+File.pathSeparator+"share.png");
			if(file.exists()){
				
			}else{
				AssetManager manager = activity.getAssets();
				InputStream in = null;
				FileOutputStream out = null;
				try {
					in = manager.open("ic_launcher.png");
					File file2 = new File(getExternalCacheDir()+File.pathSeparator+"Message");
					file2.mkdir();
					file.createNewFile();
					out = new FileOutputStream(file);
					byte[] lenght = new byte[1024];
					while (in.read(lenght) != -1) {
						out.write(lenght);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally{
					try {
						if(out != null){
							out.close();
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						in.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, file.getAbsolutePath());
			bundle.putString(QQShare.SHARE_TO_QQ_APP_NAME,  "剑鱼");
			mTencent.shareToQQ(this, bundle , new IUiListener() {
					
					@Override
					public void onError(UiError arg0) {
						NewToast.makeText(activity, "分享失败", 1).show();
					}
					
					@Override
					public void onComplete(Object arg0) {
						NewToast.makeText(activity,  "分享成功", 1).show();
					}
					
					@Override
					public void onCancel() {
						// TODO Auto-generated method stub
					}
				} );
			break;
		case R.id.tx_sina:
			if(pop != null && pop.isShowing()){
				pop.dismiss();
			}
			 // 添加新浪SSO授权
	        mController.getConfig().setSsoHandler(new SinaSsoHandler());
	        SinaShareContent content = new SinaShareContent();
	        content.setShareContent(contact+url);
	        content.setTargetUrl(url);
	        content.setTitle(contact);
	        mController.setShareMedia(content);
			mController.postShare(activity, SHARE_MEDIA.SINA, new SnsPostListener() {

		            @Override
		            public void onStart() {
		            }

		            @Override
		            public void onComplete(SHARE_MEDIA platform, int eCode, SocializeEntity entity) {
		                String showText = "分享成功";
		                if (eCode != StatusCode.ST_CODE_SUCCESSED) {
		                    showText = "分享失败 [" + eCode + "]";
		                }
		                Toast.makeText(activity, showText, Toast.LENGTH_SHORT).show();
		            }
		        });
			break;
		case R.id.tx_qzone:
			if(pop != null && pop.isShowing()){
				pop.dismiss();
			}
			Bundle params = new Bundle();
			params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE,QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
		    params.putString(QzoneShare.SHARE_TO_QQ_TITLE, contact);//必填
		    params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, contact);//选填
		    params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, url);//必填
		    ArrayList<String> list = new ArrayList<String>();
		    File file1 = new File(getExternalCacheDir()+File.pathSeparator+"Message"+File.pathSeparator+"share.png");
			if(file1.exists()){
				
			}else{
				AssetManager manager = activity.getAssets();
				InputStream in = null;
				FileOutputStream out = null;
				try {
					in = manager.open("ic_launcher.png");
					File file2 = new File(getExternalCacheDir()+File.pathSeparator+"Message");
					file2.mkdir();
					file1.createNewFile();
					out = new FileOutputStream(file1);
					byte[] lenght = new byte[1024];
					while (in.read(lenght) != -1) {
						out.write(lenght);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally{
					try {
						if(out != null){
							out.close();
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						in.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		    list.add(file1.getAbsolutePath());
		    params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL,list);
		    mTencent.shareToQzone(activity, params, new IUiListener() {
				
				@Override
				public void onError(UiError arg0) {
					NewToast.makeText(activity, "分享失败", 1).show();
				}
				
				@Override
				public void onComplete(Object arg0) {
					NewToast.makeText(activity,  "分享成功", 1).show();
				}
				
				@Override
				public void onCancel() {
//					NewToast.makeText(activity, "取消分享", 1).show();
				}
			});
			break;
		case R.id.tx_mail:
			if(pop != null && pop.isShowing()){
//				mPlatform = SHARE_MEDIA.EMAIL;
				pop.dismiss();
			}
			Intent data=new Intent(Intent.ACTION_SENDTO); 
			data.setData(Uri.parse("mailto:")); 
			data.putExtra(Intent.EXTRA_SUBJECT, contact); 
			data.putExtra(Intent.EXTRA_TEXT, url); 
			startActivity(data); 

			break;
		case R.id.img_close:
			if(pop != null && pop.isShowing()){
				pop.dismiss();
			}
			break;

		default:
			break;
		}
	}
	
	/**
	 * 分享的窗口
	 */
	private void initshare() {
		View view = activity.getLayoutInflater().inflate(R.layout.share_pop_window,null);
		pop = new PopupWindow(view, LayoutParams.MATCH_PARENT,  LayoutParams.MATCH_PARENT, true);
		view.findViewById(R.id.tx_friend).setOnClickListener(this);
		view.findViewById(R.id.tx_weixin).setOnClickListener(this);
		view.findViewById(R.id.tx_qq).setOnClickListener(this);
		view.findViewById(R.id.tx_sina).setOnClickListener(this);
		view.findViewById(R.id.tx_qzone).setOnClickListener(this);
		view.findViewById(R.id.tx_mail).setOnClickListener(this);
		view.findViewById(R.id.img_close).setOnClickListener(this);
	    pop.showAtLocation(this.activity.findViewById(R.id.lin_layout), Gravity.CENTER, 0, 0);
	}
	/**
	 * 注销的窗口
	 */
	public void initpop(){
		View view = activity.getLayoutInflater().inflate(R.layout.exit_pop_window,null);
		pop = new PopupWindow(view, LayoutParams.MATCH_PARENT,  LayoutParams.MATCH_PARENT, true);
		view.findViewById(R.id.tx_exit_ok).setOnClickListener(this);
		view.findViewById(R.id.tx_cancal).setOnClickListener(this);
		SharedPreferences sp = activity.getSharedPreferences("frist", Activity.MODE_PRIVATE);
		Editor ed = sp.edit();
		ed.putInt("show", 0);
		ed.commit();
	    pop.showAtLocation(this.activity.findViewById(R.id.lin_layout), Gravity.CENTER, 0, 0);
	}
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode > 10000){
			if (null != mTencent) {
				mTencent.onActivityResult(requestCode, resultCode, data);
			}
		}else if(requestCode < 10000){
			 UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode);
			 if(ssoHandler != null){
			      ssoHandler.authorizeCallBack(requestCode, resultCode, data);
			  }
		}
	}
}
