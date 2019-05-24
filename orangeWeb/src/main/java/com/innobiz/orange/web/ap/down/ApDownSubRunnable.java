package com.innobiz.orange.web.ap.down;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.innobiz.orange.web.cm.utils.HttpClient;

/** 다운로드 서브 런어블 - http 호출로 해당 페이지를 조회하여 저장함 */
public class ApDownSubRunnable implements Runnable {

	/** 파일 생성 경로 */
	private File dir = null;
	/** 호출 URL */
	private String url = null;
	/** 세션ID */
	private String webId = null;
	/** 도메인 - html 편집용 */
	private String domain = null;
	/** 파일타입 */
	private String fileType = null;
	/** 루트 폴더 */
	private String rootDir = null;
	/** 메인 런어블 */
	private ApDownMainRunnable parentRunnable = null;
	/** mhtml 인코더 */
	private MhtmlEncoder mhtmlEncoder = null;
	/** Html 리소스 */
	private HtmlResource htmlResource = null;
	
	
	private boolean withImage = false;
	private boolean withFile  = false;
	
	/** 생성자 */
	public ApDownSubRunnable(ApDownMainRunnable parentRunnable){
		this.parentRunnable = parentRunnable;
	}
	/** 환경설정 */
	public void setConfig(Map<String, String> prop, File dir, MhtmlResource mhtmlResource, HtmlResource htmlResource){
		
		this.dir = dir;
		this.url = prop.get("url");
		this.webId = prop.get("webId");
		this.domain = prop.get("domain");
		this.fileType = prop.get("fileType");
		this.withFile  = "htmlImageFile".equals(fileType);
		this.withImage = withFile || "htmlImage".equals(fileType);
		this.rootDir = prop.get("rootDir");
		this.htmlResource = htmlResource;
		
		if(mhtmlResource!=null){
			mhtmlEncoder = new MhtmlEncoder(mhtmlResource, domain, fileType);
		}
	}
	
	/** 쓰레드 런 함수 */
	@Override
	public void run() {
		
		ApDownData data;
		HttpClient client = new HttpClient();
		String html, fileName;
		FileOutputStream out;
		
		boolean writeDone = false;
		boolean extMhtml = !fileType.startsWith("html");
		
		// 세션 유지용 쿠키 작업
		Map<String, String> header = new HashMap<String, String>();
		header.put("Cookie", "ORANGE_WEB_ID="+webId);
		
		while(true){
			// 중지된 경우
			if(parentRunnable.isStop()){
				break;
			}
			// 작업 갯수만큼 다 한경우
			if(!parentRunnable.canProcess()){
				break;
			}
			
			// 작업 데이터 조회
			data = parentRunnable.getNextData();
			if(data==null){
				// 작업 데이터 없으면 0.1초 쉼
				try{ Thread.sleep(100); }
				catch(Exception ignore){}
				
			} else {
				
				// http 호출해서 html 가져옴
				html = null;
				try {
					html = client.sendGet(url+data.apvNo, header, "UTF-8");
				} catch (IOException e) {
					ApDownSvc.getLoger().error("Ap download error - call html : "+e.getMessage());
				}
				if(html == null || !html.startsWith("<!DOCTYPE html>")){
					ApDownSvc.getLoger().error("Ap download error - not success doc : "+data.apvNo);
					parentRunnable.addErrCnt();
					continue;
				}
				
				// html 편집
				html = modifyHtml(html);
				if(extMhtml && mhtmlEncoder!=null){
					try {
						html = mhtmlEncoder.encode(html, data.apvNo, rootDir);
					} catch (IOException e) {
						ApDownSvc.getLoger().error("Ap download error - mhtml encoding : "+e.getMessage());
					}
				}
				
				
				// 파일 작성
				writeDone = false;
				out = null;
				try {
					fileName = escapeFileName(data.fileName);
					out = new FileOutputStream(new File(dir, fileName));
					out.write(html.getBytes("UTF-8"));
					out.close();
					writeDone = true;
					parentRunnable.addCmplCnt();
					
				} catch (Exception e) {
					ApDownSvc.getLoger().error("Ap download error - making file error : "+e.getMessage());
				} finally {
					try{ if(out!=null) out.close(); }
					catch(Exception ignore){}
				}
				
				if(!writeDone){
					parentRunnable.addErrCnt();
					ApDownSvc.getLoger().error("Ap download error - making file error : "+data.apvNo);
				}
			}
		}
		
		// 서브 쓰레드 완료됨 처리
		parentRunnable.exitSubTread();
		if(mhtmlEncoder!=null){
			mhtmlEncoder.release();
			mhtmlEncoder = null;
		}
	}

	/** 파일명 제외 문자 */
	private char[] fileNameEscapes = "#%{}\\<>*?/ $!'\":@".toCharArray();
	/** 파일명 제외 문자 제거 - 언더바(_)로 치환함 */
	private String escapeFileName(String fileName){
		for(char c : fileNameEscapes){
			fileName = fileName.replace(c, '_');
		}
		return fileName;
	}
	
	/** html 편집 - 이미지 경로 안깨지게 도메인 처리, javascript 오류 안나게 빈함수로 치환 */
	private String modifyHtml(String html){
		
		html = html.replaceAll("javascript:viewUserPop", "javascript:doNothing");
		html = html.replaceAll("javascript:openDocFrm", "javascript:doNothing");
		html = html.replaceAll("javascript:openDocView", "javascript:doNothing");
		html = html.replaceAll("javascript:openDetl", "javascript:doNothing");
		html = html.replaceAll("\\$\\(document\\).ready\\(function\\(\\)\\{", "doNothing\\(function\\(\\)\\{");
		html = html.replaceAll("\r\n\r\n", "\r\n");
		
		if(fileType.startsWith("mhtml")){
			html = html.replaceAll("overflow-y:visible; overflow-x:auto", "");
		}
		
		if(withImage){
			html = converHtml(html, true);
		} else {
			html = html.replaceAll("\"/images", "\"http://"+domain+"/images");
		}
		if(withFile){
			html = converHtml(html, false);
		} else {
			html = html.replaceAll("javascript:downAttchFile", "javascript:doNothing");
		}
		
		return html;
	}
	
	private String converHtml(String html, boolean isImage){
		
		StringBuilder builder = new StringBuilder(html.length()+1);
		String finding = isImage ? "\"/images/" : "\"javascript:downAttchFile";
		int start=0, end=0, prevEnd=0, findingLen = finding.length();
		String path;
		
		while((start = html.indexOf(finding, prevEnd)) > 0){
			
			end = html.indexOf('\"', start+findingLen);
			
			if(isImage){
				path = html.substring(start+1, end);
				path = htmlResource.getImageResource(path);
			} else {
				path = html.substring(start+27, end-3);
				path = htmlResource.getAttachResource(path);
			}
			
			builder.append(html, prevEnd, start+1);
			builder.append(path);
			builder.append("\" target=\"_blank");
			prevEnd = end;
		}
		
		if(prevEnd==0) return html;
		else builder.append(html, prevEnd, html.length());
		
		return builder.toString();
	}
	
}
