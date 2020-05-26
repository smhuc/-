package com.wangtian.message.util;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.PicturesManager;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.hwpf.usermodel.Picture;
import org.apache.poi.hwpf.usermodel.PictureType;
import org.w3c.dom.Document;
/**
 * 读取word2003
 * @author chen
 *
 */
public class WordToHtml {

	private static final String encoding = "UTF-8";
	
	private static void writeFile(String content, String path) {   
		String HTML0 = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"><html xmlns=\"http://www.w3.org/1999/xhtml\"><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" /><meta id=\"viewport\" name=\"viewport\" content=\"width=device-width, maximum-scale=2.0,minimum-scale=1.1,initial-scale=";
		String HTML1 = "\" /><title></title><style type=\"text/css\"><!--*{margin:0px;padding:0px;}li{list-style-type:none;}img{border:0px;width:90%;}body{margin:0px;padding:0px;font-size:12px;text-align:center;}#container{width:100%;margin:0px auto;overflow:hidden;zoom:1;background-image:url(images/bk-1.gif);background-repeat:no-repeat;}.head{width:90%;margin-top:20px;margin-left:5%;}.head_bt{width:100%;font-size:16px;font-weight:bold;}.head_ly{width:100%;color:#666666;margin-top:20px;}.video{margin-top:16px;}.content{width:96%;text-align:left;margin-top:18px;;margin-bottom:18px;margin-left:3%;line-height:26px;font-size:18px;letter-spacing:2px;}--></style></head><body><div id=\"container\"><div class=\"head\"><div class=\"head_bt\">";
		String HTML2 = "</div><div class=\"head_ly\">";
		String HTML3 = "</div></div><div class=\"content\">";
		String HTML4 = "</div></div></body></html>";
		String river = HTML0+HTML1+HTML2+HTML3;
		        FileOutputStream fos = null;   
		        BufferedWriter bw = null;   
		        try {   
		            File file = new File(path);   
		            fos = new FileOutputStream(file);
		            fos.write(river.getBytes());
		            bw = new BufferedWriter(new OutputStreamWriter(fos,encoding));   
		            bw.write(content); 
		            fos.write("</body></html>".getBytes());
		        } catch (FileNotFoundException fnfe) {   
		           fnfe.printStackTrace();   
		        } catch (IOException ioe) {   
		            ioe.printStackTrace();   
		        } finally {   
		            try {   
		                if (bw != null)   
		                    bw.close();   
		                if (fos != null)  
		                    fos.close();   
		            } catch (IOException ie) {   
		            }   
		        }   
		} 
	
		public static void convert2Html(String fileName, String outPutFile)
				throws TransformerException, IOException,
				ParserConfigurationException {
			HWPFDocument wordDocument = new HWPFDocument(new FileInputStream(fileName));//WordToHtmlUtils.loadDoc(new FileInputStream(inputFile));
			WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(
					DocumentBuilderFactory.newInstance().newDocumentBuilder()
							.newDocument());
			File file = new File(fileName);
			final String savePath = file.getParentFile().getAbsolutePath();
			 wordToHtmlConverter.setPicturesManager( new PicturesManager()
	         {

				public String savePicture(byte[] arg0, PictureType arg1,
						String arg2) {
					return savePath + "/"+ arg2;
				}

				public String savePicture(byte[] arg0, PictureType arg1,
						String arg2, float arg3, float arg4) {
					// TODO Auto-generated method stub
					return null;
				}
	         } );
			wordToHtmlConverter.processDocument(wordDocument);
			//save pictures
			List<Picture> pics=wordDocument.getPicturesTable().getAllPictures();
			if(pics!=null){
				for(int i=0;i<pics.size();i++){
					Picture pic = (Picture)pics.get(i);
					System.out.println();
					try {
						pic.writeImageContent(new FileOutputStream(
								savePath + "/" + pic.suggestFullFileName()));
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}  
				}
			}
			Document htmlDocument = wordToHtmlConverter.getDocument();
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			DOMSource domSource = new DOMSource(htmlDocument);
			StreamResult streamResult = new StreamResult(out);

			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer serializer = tf.newTransformer();
			serializer.setOutputProperty(OutputKeys.ENCODING, encoding);
			serializer.setOutputProperty(OutputKeys.INDENT, "yes");
			serializer.setOutputProperty(OutputKeys.METHOD, "html");
			serializer.transform(domSource, streamResult);
			out.close();
			writeFile(new String(out.toByteArray()), outPutFile);
		}
}
