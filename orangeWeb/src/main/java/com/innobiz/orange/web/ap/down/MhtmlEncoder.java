package com.innobiz.orange.web.ap.down;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import com.ibm.icu.text.SimpleDateFormat;
import com.innobiz.orange.web.ap.utils.Base64;
import com.innobiz.orange.web.cm.utils.StringUtil;

public class MhtmlEncoder {

	private static String LN = "\r\n";
	
	private MhtmlResource resource;
	
	private String date = null;
	private String boundary = null;
	private String domain = null;
	
	public MhtmlEncoder(MhtmlResource resource, String domain, String fileType){
		this.resource = resource;
		this.domain = domain;
		
		SimpleDateFormat sf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
		date = sf.format(new Date());
		
		boundary = "----=_NextPart_"+StringUtil.getNextHexa().toUpperCase()+"."+StringUtil.getNextHexa().toUpperCase();
	}
	
	public String encode(String html, String apvNo, String baseDir) throws IOException{
		
		StringBuilder builder = new StringBuilder(32 * 1024 * 1024);
		
		builder.append("From: Orange Groupward").append(LN);
		builder.append("Subject: Approved Document").append(LN);
		builder.append("Date: ").append(date).append(LN);
		builder.append("MIME-Version: 1.0").append(LN);
		builder.append("Content-Type: multipart/related;").append(LN);
		builder.append("	type=\"text/html\";").append(LN);
		builder.append("	boundary=\"").append(boundary).append("\"").append(LN);
		builder.append(LN);
		builder.append(LN);
		
		// html part
		builder.append("--").append(boundary).append(LN);
		builder.append("Content-Type: text/html;").append(LN);
		builder.append("	charset=\"utf-8\"").append(LN);
		builder.append("Content-Transfer-Encoding: base64").append(LN);
		builder.append("Content-Location: http://").append(domain).append("/ap/box/setDoc.do?apvNo=").append(apvNo).append(LN);
		builder.append(LN);
		
		/*
		ArrayList<String> urlList = null, pathList = null;
		// mhtml 파일 함께 묶기
		if(withFile){
			urlList = new ArrayList<String>();
			pathList = new ArrayList<String>();
			
			StringBuilder htmlBuilder = new StringBuilder(html.length()+1);
			String temp, path, name, url;
			int scriptS=0, scriptE=0, prevEnd=0;
			boolean hasFile = false;
			while((scriptS=html.indexOf("javascript:downAttchFile", scriptE))>0){
				
				scriptE = html.indexOf('"', scriptS);
				if(scriptE>scriptS){
					temp = html.substring(scriptS+26, scriptE-3);
					name = temp.substring(temp.lastIndexOf('/')+1);
					path = baseDir+temp;
					url  = "http://"+domain+"/ap/box/"+name;
					
					hasFile = false;
					if(new File(path).isFile()){
						urlList .add( url);
						pathList.add(path);
						hasFile = true;
					} else {
						url = "javascript:doNothing()";
					}
					
					htmlBuilder.append(html, prevEnd, scriptS);
					htmlBuilder.append(url);
					if(hasFile) htmlBuilder.append("\" target=\"_blank");
					prevEnd = scriptE;
				}
			}
			if(prevEnd>0){
				htmlBuilder.append(html, prevEnd, html.length());
				html = htmlBuilder.toString();
			}
		}
		*/
		
		String base64Html = Base64.encode(html.getBytes("UTF-8"), 19);
		builder.append(base64Html).append(LN);
		
		int p=0, q=0;
		String rescPath = null, rescContent, contentType;
		String urlPrefix = "\"http://"+domain;
		int urlPrefixLen = urlPrefix.length();
		
		ArrayList<String> addList = new ArrayList<String>();
		
		while((p = html.indexOf(urlPrefix, q+1)) > 0){
			
			q = html.indexOf('\"', p+urlPrefixLen);
			if(q<0){
				q = p+urlPrefixLen;
				continue;
			}
			
			rescPath = html.substring(p+1, q);
			if(addList.contains(rescPath)){
				continue;
			}
			
			addList.add(rescPath);
			rescContent = resource.getResource(rescPath);
			if(rescContent == null) continue;
			contentType = resource.getContentType(rescPath);
			if(contentType == null) contentType = "";
			
			builder.append(LN);
			builder.append("--").append(boundary).append(LN);
			builder.append("Content-Type: ").append(contentType).append(LN);
			builder.append("Content-Transfer-Encoding: base64").append(LN);
			builder.append("Content-Location: ").append(rescPath).append(LN);
			builder.append(LN);
			
			builder.append(rescContent).append(LN);
		}
		/*
		// 다운로드 파일
		if(withFile && urlList!=null && !urlList.isEmpty()){
			
			int i, size = urlList.size();
			String url, path, encoded;
			
			for(i=0; i<size; i++){
				
				url  = urlList.get(i);
				path = pathList.get(i);
				
				encoded = null;
				try{
					encoded = Base64.encode(readStream(new FileInputStream(path)), 19);
				} catch(Exception ignore){}
				if(encoded == null) {
					continue;
				}
				
				builder.append(LN);
				builder.append("--").append(boundary).append(LN);
				builder.append("Content-Type: application/download").append(LN);
				builder.append("Content-Transfer-Encoding: base64").append(LN);
				builder.append("Content-Location: ").append(url).append(LN);
				builder.append(LN);
				
				builder.append(encoded).append(LN);
			}
		}
		*/
		
		builder.append(LN);
		builder.append("--").append(boundary).append("--").append(LN);
		
		return builder.toString();
	}
	
	public void release(){
		resource = null;
	}
	
//	private byte[] readStream(InputStream in) throws IOException {
//		try {
//			int len;
//			byte[] bytes = new byte[2048];
//			ByteArrayOutputStream out = new ByteArrayOutputStream();
//			while((len = in.read(bytes, 0, 2048))>0) out.write(bytes, 0, len);
//			return out.toByteArray();
//		} finally {
//			if(in!=null){
//				try{ in.close(); }
//				catch(Exception ignore){}
//			}
//		}
//	}
}