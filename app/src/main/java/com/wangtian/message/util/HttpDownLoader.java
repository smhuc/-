package com.wangtian.message.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import android.os.Handler;
import android.os.Message;


public class HttpDownLoader {
	private URL url = null;
	public static int state = 0;


	// 下载其他格式的文件,并将其写入SD卡中
	// 返回值：-1代表下载文件出错，0代表正常下载，1代表文件已存在于SD卡上
	public int downLoadFile(String strurl, String path, String fileName,Handler handler) {

		InputStream inputstream = null;
		try {

			url = new URL(strurl);
			HttpURLConnection urlConn = (HttpURLConnection) url
					.openConnection();
			inputstream = urlConn.getInputStream();
			File file = inputSD(path, fileName, inputstream, handler,
					urlConn.getContentLength());
			urlConn.disconnect();
			if (file == null) {
				return 0;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		} finally {
			try {
				if (inputstream != null) {
					inputstream.close();

				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return 1;
	}
	// 在sd卡的路径下创建文件
	public File createSDFile(String filename) throws IOException {
		File file = new File(filename);
		file.createNewFile();
		return file;
	}

	// 在sd卡的路径下创建目录
	public File createSDDir(String dirname) {
		File dir = new File(dirname);
		dir.mkdirs();
		return dir;
	}

	// 判断文件是否已经存在
	public boolean isFileExist(String filename) {
		File file = new File(filename);
		return file.exists();
	}

	// 将一个InputStream里的数据写进SD卡
	public File inputSD(String path, String fileName, InputStream inputstream,
			Handler handler, int size) {
		File temmpfile = null;
		File file = null;
		OutputStream outputstream = null;
		try {
			createSDDir(path);
			temmpfile = createSDFile(path + fileName);

			outputstream = new FileOutputStream(temmpfile);
			byte[] buffer = new byte[10240];
			int len;
			long total = 0;
			while ((len = inputstream.read(buffer)) != -1) {
				outputstream.write(buffer, 0, len);
				total += len;
				Message msg = new Message();
				msg.what = 1;
				msg.arg1 = (int) (total * 100 / size);
				handler.sendMessage(msg);
				outputstream.flush();
			}
			file = createSDFile(path + fileName);
		} catch (IOException e) {
			try {
				temmpfile.deleteOnExit();
			} catch (Exception e2) {
			}
			e.printStackTrace();
		} finally {

			try {
				outputstream.close();
				temmpfile.deleteOnExit();
			} catch (IOException e) {

				e.printStackTrace();
			}

		}
		return file;
	}

	public static boolean isExist(String path, String fileName) {
		if (new File(path + fileName).exists()) {

			return true;
		} else {
			return false;
		}
	}
}
