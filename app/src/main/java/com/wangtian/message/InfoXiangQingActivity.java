package com.wangtian.message;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import com.wangtian.message.base.BaseMenuActivity;
import com.wangtian.message.util.Contact;
import com.wangtian.message.util.HttpDownLoader;
import com.wangtian.message.util.NewToast;
import com.wangtian.message.util.WordToHtml;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebSettings.LayoutAlgorithm;

public class InfoXiangQingActivity extends BaseMenuActivity {

	private WebView web;
	private String nameStr = null;
	private ProgressDialog dialog;
	private String id;
	private String path = Environment.getExternalStorageDirectory().getPath()
			+ "/Message/word/";
	private String htmlpath = Environment.getExternalStorageDirectory().getPath()
			+ "/Message/html/my.html";
	private String imgpath = Environment.getExternalStorageDirectory().getPath()
			+ "/Message/image";
	private String name;
	private boolean word2007 = true;
	
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				int press = (int) msg.arg1;
				break;
			case 2:
				nameStr = path+name+".doc";
				if(nameStr != null && nameStr.length() > 1){
					String html = readDOCX(nameStr);
					try {
						if(word2007){
							File file = new File(htmlpath);
							File file1 = new File(Environment.getExternalStorageDirectory().getPath()
									+ "/Message/html");
							if(!file1.exists()){
								file1.mkdir();
							}
							if(!file.exists()){
								file.createNewFile();
							}
							FileOutputStream out = new FileOutputStream(file);
							out.write(html.getBytes());
							out.close();
						}else{
							WebSettings settings = web.getSettings();  
							settings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
							WordToHtml.convert2Html(nameStr, htmlpath);
						}
						web.loadUrl("file://"+htmlpath);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					String head = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"><html xmlns=\"http://www.w3.org/1999/xhtml\"><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" /><meta id=\"viewport\" name=\"viewport\" content=\"width=device-width, maximum-scale=2.0,minimum-scale=1.1,initial-scale= /><title></title><style type=\"text/css\"><!--*{margin:0px;padding:0px;}li{list-style-type:none;}img{border:0px;width:90%;}body{margin:0px;padding:0px;font-size:12px;text-align:center;}#container{width:100%;margin:0px auto;overflow:hidden;zoom:1;background-image:url(images/bk-1.gif);background-repeat:no-repeat;}.head{width:90%;margin-top:20px;margin-left:5%;}.head_bt{width:100%;font-size:16px;font-weight:bold;}.head_ly{width:100%;color:#666666;margin-top:20px;}.video{margin-top:16px;}.content{width:95%;text-align:left;margin-top:18px;margin-left:10%;line-height:26px;font-size:18px;letter-spacing:2px;}--></style></head><body><center><h3>";
					String end = "</h3></center></body></html>";
					web.loadDataWithBaseURL(null,head+"文档加载失败"+end, "text/html", "utf-8",null);
					}
//					web.loadDataWithBaseURL(null,htmlpath, "text/html", "utf-8",null);
				}
				if(dialog.isShowing()){
					dialog.dismiss();
				}
				findViewById(R.id.tx_back).setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						finish();
					}
				});
//				if(htmlPath != null){
//					web.loadUrl("file://"+htmlPath);
//				}
				break;
			case 3:
				NewToast.makeText(InfoXiangQingActivity.this,"下载失败", 0).show();
				break;
			default:
				break;
			}
		}
		
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.infoxq);
		Intent intent = this.getIntent();
		dialog = new ProgressDialog(InfoXiangQingActivity.this);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);
		dialog.setMessage("加载中...");
		dialog.show();
		id = intent.getStringExtra("id");
		name = intent.getStringExtra("name");
		setActivity(InfoXiangQingActivity.this, name);
		left(1);
		right(3).setVisibility(View.GONE);
		web = (WebView) findViewById(R.id.webview);
		Share(getResources().getString(R.string.app_name), name, Contact.url+"ReportShared/"+id);
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				HttpDownLoader downLoader = new HttpDownLoader();
				int a = downLoader.downLoadFile(Contact.url+"ReportDown/DownloadReport?c={\"id\":\""+id+"\"}", path, name+".doc",handler);
				if(a == 1){
					handler.sendEmptyMessage(2);
				}if(a == 0){
					handler.sendEmptyMessage(3);
				}
			}
		}).start();
	}
	

	// 解析docx
	public String readDOCX(String path) {
		String river = "";
		String HTML0 = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"><html xmlns=\"http://www.w3.org/1999/xhtml\"><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" /><meta id=\"viewport\" name=\"viewport\" content=\"width=device-width, maximum-scale=2.0,minimum-scale=1.1,initial-scale=";
		String HTML1 = "\" /><title></title><style type=\"text/css\"><!--*{margin:0px;padding:0px;}li{list-style-type:none;}img{border:0px;width:90%;}body{margin:0px;padding:0px;font-size:12px;text-align:center;}#container{width:100%;margin:0px auto;overflow:hidden;zoom:1;background-image:url(images/bk-1.gif);background-repeat:no-repeat;}.head{width:90%;margin-top:20px;margin-left:5%;}.head_bt{width:100%;font-size:16px;font-weight:bold;}.head_ly{width:100%;color:#666666;margin-top:20px;}.video{margin-top:16px;}.content{width:96%;text-align:left;margin-top:18px;;margin-bottom:18px;margin-left:3%;line-height:26px;font-size:18px;letter-spacing:2px;}--></style></head><body><div id=\"container\"><div class=\"head\"><div class=\"head_bt\">";
		String HTML2 = "</div><div class=\"head_ly\">";
		String HTML3 = "</div></div><div class=\"content\">";
		String HTML4 = "</div></div></body></html>";
		river = HTML0+HTML1+HTML2+HTML3;
		String color = null;
		String size = null;
		try {
			ZipFile xlsxFile = new ZipFile(new File(path));
			ZipEntry sharedStringXML = xlsxFile.getEntry("word/document.xml");
			InputStream inputStream = xlsxFile.getInputStream(sharedStringXML);
			
			/*获取wrod里面的图片*/
			 ZipInputStream  zis = new ZipInputStream (new FileInputStream(path));
			    int a = 0;
			    int c = 0;
			    ZipEntry ze = null;
			    byte[] b = new byte[1024];
			    while((ze = zis.getNextEntry()) != null)
			    {
			        System.out.println(ze.getName() + ":" + ze.getMethod());
			        /*将源压缩文件中每个文件写至新压缩文件*/
			        InputStream is = zis;
			        if(ze.getName().endsWith(".xml"))
			        {
			        }else{//非xml文件，二进制流数据
			            //替换图片
			            if(ze.getName().endsWith("jpeg")){
			            	File file = new File(imgpath);
			            	File file1 = new File(imgpath+"/"+name+a+".jpg");
			            	if(!file.exists()){
			            		file.mkdir();
			            	}
			            	if(file1.exists()){
			            		file1.createNewFile();
			            	}
			                FileOutputStream out = new FileOutputStream(file1);
			                int r = -1; 
			                while((r = is.read(b)) != -1){
			                	out.write(b, 0, r);
			                }

			                out.close();
			                a++;
			            }
			        }
			         
			        if(is != zis){
			            is.close();
			        }
			    }
			/*  到此*/
			XmlPullParser xmlParser = Xml.newPullParser();
			xmlParser.setInput(inputStream, "utf-8");
			int evtType = xmlParser.getEventType();
			while (evtType != XmlPullParser.END_DOCUMENT) {
				switch (evtType) {
				case XmlPullParser.START_TAG:
					String tag = xmlParser.getName();
					if(xmlParser.getAttributeCount() > 0){
						Log.e("tag-text", tag+"                   " +xmlParser.getAttributeValue(0)+"");
						if("http://schemas.openxmlformats.org/drawingml/2006/picture".equals(xmlParser.getAttributeValue(0)+"")){
							river+="<img src=\""+imgpath+"/"+name+c+".jpg\"/><br>";
							c+=1;
						}
//						if("color".equals(tag)){
//							color ="<font color=\"#" +xmlParser.getAttributeValue(0)+ "\">";
//						}
//						if("sz".equals(tag)){
//							size ="<font size=\"#" +Integer.parseInt(xmlParser.getAttributeValue(0))/2+ "\">";
//						}
					}
					if (tag.equalsIgnoreCase("t")) {
						Log.e("tag", river+"");
//						if(color != null){
//							if(size != null){
//								river += color+size+xmlParser.nextText() + "</font></font><br>";
//								size = null;
//							}else{
//								river += color+xmlParser.nextText() + "</font><br>";
//							}
//							color = null;
//						}else{
//							river += xmlParser.nextText() + "<br>";
//							if(size != null){
//								river += size+xmlParser.nextText() + "</font><br>";
//								size = null;
//							}else{
								river += xmlParser.nextText() + "<br>";
//							}
//						}
					}
					break;
				case XmlPullParser.END_TAG:
					
					break;
					
				default:
					break;
				}
				evtType = xmlParser.next();
			}
		} catch (ZipException e) {
			e.printStackTrace();
			word2007 = false;
		} catch (IOException e) {
			e.printStackTrace();
			word2007 = false;
		} catch (XmlPullParserException e) {
			word2007 = false;
			e.printStackTrace();
		}
		if (river == null) {
			river = "解析文件出现问题";
		}

		return river+"</body></html>";
	}
	public void onDestroy(){
		super.onDestroy();
	}
}
