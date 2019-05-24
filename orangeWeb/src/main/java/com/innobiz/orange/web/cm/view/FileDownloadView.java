package com.innobiz.orange.web.cm.view;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.FileCopyUtils;
import org.springframework.web.servlet.view.AbstractView;

import com.innobiz.orange.web.cm.config.ServerConfig;
import com.innobiz.orange.web.cm.dao.LobHandler;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.j256.simplemagic.ContentInfo;
import com.j256.simplemagic.ContentInfoUtil;

public class FileDownloadView extends AbstractView {

	/** 메세지 처리용 프라퍼티 - 다국어 */
	@Resource(name = "messageProperties")
	private MessageProperties messageProperties;
	
	/** 생성자, 컨텐츠 타입 설정 */
	public FileDownloadView() {
		setContentType("application/octet-stream; charset=utf-8");
	}

	/** 다운로드 구현 */
	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response)	throws IOException, SQLException {
		
		File file = (File) model.get("downloadFile");
		byte[] bytes = (byte[]) model.get("downloadBytes");
		LobHandler lobHandler = (LobHandler) model.get("lobHandler");
		String realFileName = (String) model.get("realFileName");
		
		if(file != null || bytes != null){
			int length = bytes!=null ? bytes.length : (int) file.length();
			response.setContentLength(length);
			
			if(realFileName.endsWith(".apk")) {
				setContentType("application/vnd.android.package-archive; charset=utf-8");
			} else if(realFileName.endsWith(".ipa")) {
				setContentType("application/octet-stream");
			} else if(realFileName.endsWith(".chm")) {
				setContentType("application/vnd.ms-htmlhelp; charset=utf-8");
			} else {
				ContentInfoUtil util = new ContentInfoUtil();
				ContentInfo info = bytes!=null ? util.findMatch(bytes) : util.findMatch(file);
				if(info != null && info.getMimeType() != null)	setContentType(info.getMimeType());
				else setContentType("application/octet-stream; charset=utf-8");
			}
			
			
		}
		String userAgent = request.getHeader("User-Agent");
		if(userAgent==null || userAgent.isEmpty()) userAgent = "";
		
//		// - encCharSet -
//		// 한글 버전에는 EUC-KR 이지만 한글 케렉터셋 사용하지 않을 경우 대비하여
//		// 세션이 한글 케렉터 셋이거나, 한글 브라우저를 사용 할 경우 한글로 인코딩 함
//		String encCharSet = "UTF-8";
//		if("ko".equals(SessionUtil.getLangTypCd(request)) || "ko".equals(request.getLocale().getLanguage())){
//			encCharSet = "EUC-KR";
//		}
		
		// IE, Chrome 의 경우 CharSet 을 맞춰야 파일 다운로드명이 깨지지 않으며
		// 디폴트는 EUC-KR 이며
		// 한글을 사용하지 않는 경우를 대비하여 프로퍼티에서 설정 하도록 함
		//String encCharSet = MessageProperties.getContextProperties().getProperty("upload.filename.charset", "EUC-KR");
		
		String contDisp = "attachment";
		//System.out.println("userAgent : "+userAgent);
		if (userAgent.contains("Chrome")) {
			/*if(ServerConfig.IS_MOBILE){
				realFileName = URLEncoder.encode(realFileName, "UTF-8").replaceAll("\\+", "%20");
			} else{*/
				StringBuffer sb = new StringBuffer();
	            for (int i = 0; i < realFileName.length(); i++) {
	                char c = realFileName.charAt(i);
	                if (c > '~') {
	                    sb.append(URLEncoder.encode("" + c, "UTF-8"));
	                } else {
	                    sb.append(c);
	                }
	            }
	            realFileName = sb.toString();
			//}
			/*if(ServerConfig.IS_MOBILE){
				realFileName = new String( realFileName.getBytes("UTF-8"), "ISO-8859-1");
			} else {
				realFileName = URLEncoder.encode(realFileName, "UTF-8").replaceAll("\\+", "%20");
			}*/
		} else if (userAgent.contains("Safari")) {
			realFileName = new String( realFileName.getBytes("UTF-8"), "ISO-8859-1");
			if(ServerConfig.IS_MOBILE) contDisp = "inline";
			//setContentType("application/x-www-form-urlencoded; charset=UTF-8'"); // Safari 브라우져 한글 처리 
			//response.setHeader("Ajax", "true"); // Safari 브라우져 한글 처리 
		} else if (userAgent.contains("Firefox")) {
			realFileName = new String( realFileName.getBytes("UTF-8"), "ISO-8859-1");
		} else{
			realFileName = URLEncoder.encode(realFileName, "UTF-8").replaceAll("\\+", "%20");
		}
		
		response.setHeader("Content-Disposition", contDisp+";filename=\""	+ realFileName + "\";");
		 //response.setHeader("Content-Disposition", "inline;filename="+URLEncoder.encode(fileName, "UTF-8") + ";");
		response.setHeader("Accept-Ranges","bytes");
		response.setHeader("Content-Transfer-Encoding", "binary");//base64
		response.setHeader("Cache-Control","private");
		//response.setHeader("Cache-Control","no-store, no-cache");
		//response.setHeader("Cache-Control","must-revalidate, post-check=0, pre-check=0");
		response.setHeader("Expires", "-1");
		//response.setHeader("P3P","CP=ALL CURa ADMa DEVa TAla OUR BUS IND PHY ONL UNI PUR FIN COM NAV INT DEM CNT STA POL HEA PRE LOC OTC");
		response.setHeader("Pragma", "no-cache");
		response.setContentType(getContentType());
		//System.out.println("response.getContentType() : "+response.getContentType());
		OutputStream out = response.getOutputStream();
		InputStream fis = null;
		
		try {
			if(file != null || bytes != null){
				fis = (bytes!=null) ? new ByteArrayInputStream(bytes) : new FileInputStream(file);
				FileCopyUtils.copy(fis, out);
			} else {
				lobHandler.writeFile(out);
			}
		} finally {
			if (fis != null){
				try {
					fis.close();
				} catch (IOException ignore) {}
			}
		}
		out.flush();
	}
}
