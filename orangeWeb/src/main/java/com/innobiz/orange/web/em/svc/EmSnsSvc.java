package com.innobiz.orange.web.em.svc;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.innobiz.orange.web.cm.files.DistHandler;
import com.innobiz.orange.web.cm.files.DistManager;
import com.innobiz.orange.web.cm.files.UploadManager;
import com.innobiz.orange.web.cm.utils.EscapeUtil;
import com.innobiz.orange.web.cm.utils.RC4;
import com.innobiz.orange.web.cm.utils.SessionUtil;
import com.innobiz.orange.web.cm.utils.StringUtil;
import com.innobiz.orange.web.em.utils.EmConstant;
import com.innobiz.orange.web.pt.svc.PtSysSvc;

/** SNS 서비스 */
@Service
public class EmSnsSvc {
	
	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
	/** 업로드 메니저 */
	@Resource(name = "uploadManager")
	private UploadManager uploadManager;
	
	/** 배포 매니저 */
	@Resource(name = "distManager")
	private DistManager distManager;
	
	/** 환경 설정 로드 */
	public Map<String, String> getEnvConfigMap(ModelMap model, String compId) throws SQLException {
		Map<String, String> envConfigMap = ptSysSvc.getSysSetupMap(EmConstant.EM_SYS_CONFIG+compId, true);
		/*if(envConfigMap == null || envConfigMap.isEmpty()){
			
			envConfigMap = new HashMap<String, String>();
			envConfigMap.put("appId", "Y");// 참석자수락여부
			ptCacheSvc.setCache(CacheConfig.SYS_SETUP, "ko", EmConstant.BB_SYS_CONFIG+compId, envConfigMap);
		}*/
		if(model!=null) model.put("envConfigMap", envConfigMap);
		
		return envConfigMap;
	}
	
	/** HTML 생성 */
	public void createSnsFile(HttpServletRequest request, String path, 
			String fileName, String title, String cont, String thumImg){
		try{
			// 서버 설정 목록 조회
			Map<String, String> svrEnvMap = ptSysSvc.getSvrEnvMap();
			String webDomain = svrEnvMap.get("webDomain");
			if(webDomain==null) return;
			String urlPrefix=request.getScheme()+"://"+webDomain;
			
			DistHandler distHandler = distManager.createHandler(path, SessionUtil.getLocale(request));  // 업로드 경로 설정
			
			String thumUrl=thumImg==null || thumImg.isEmpty() ? null : urlPrefix+thumImg;
			String logoImg=(String)request.getAttribute("_logoImg");
			createHtml(path, fileName, title, cont, urlPrefix, distHandler, thumUrl, logoImg);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/** HTML 생성 */
	public void createHtml(String path, String fileName, String title, String cont, 
			String urlPrefix, DistHandler distHandler, String thumUrl, String logoImg){
		if(cont==null || cont.isEmpty()) return;
		try{
			// baseDir
			String webCopyBaseDir = distManager.getWebCopyBaseDir();
			if (webCopyBaseDir == null) {
				distManager.init();
				webCopyBaseDir = distManager.getWebCopyBaseDir();
			}
			String ext=EmConstant.SNS_EXT;
			// 파일명 변환			
			fileName=RC4.getEncrypt(fileName)+ext;
			String tmpFileNm="F"+StringUtil.getNextHexa();
			// 임시저장경로
			String tmpDir=uploadManager.getTempDir();
			
			File file=new File(tmpDir);
			if(!file.isDirectory()) file.mkdirs();
			
			String savePath=tmpDir+File.separator+tmpFileNm;
			savePath = savePath.replace('\\', '/');
			
			// BufferedWriter 와 FileWriter를 조합하여 사용 (속도 향상)
			//BufferedWriter fw = new BufferedWriter(new FileWriter(savePath, true));
			// 한글깨짐으로 인해 BufferedWriter ... 조합으로 변경
			BufferedWriter fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(savePath), "UTF-8"));
			
			if(title!=null) title=EscapeUtil.escapeHTML(filterHtml(title, null));
			String description=null;
			// 글자수 제한 100자
			if(cont!=null) {
				description=filterHtml(cont, null); // Html Tag 제거
				description=EscapeUtil.unescapeHTML(description); // HTML 에 대한 unescape
				description=EscapeUtil.escapeValue(StringUtil.cutString(description, 140, true)); // 특수코드 변환
				description=description.replace(" ", " ");
				if(thumUrl==null){ // 섬네일 이미지가 없으면 내용에서 동영상 또는 이미지를 가져온다
					String medeaSrc = getMedeaSrc(cont);
					if(medeaSrc!=null){
						if(medeaSrc.indexOf("youtube.com")>-1) thumUrl=getThumbnailUrl(medeaSrc);
						else thumUrl=medeaSrc;
					}
				}
			}
			// 섬네일 이미지 여부
			boolean isThumImg=thumUrl!=null;
			//if(!isThumImg && logoImg!=null){ // 섬네일이 없으면 그룹웨어 로고를 세팅한다
			//	thumUrl=urlPrefix+logoImg;
			//}
			StringBuilder sb = new StringBuilder();
			sb.append("<!DOCTYPE HTML>\n");
			sb.append("<html lang=\"ko\">\n");
			sb.append("<head>\n");
			sb.append("<meta name=\"viewport\" content=\"width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1\" />\n");
			sb.append("<meta charset=\"utf-8\">\n");
			//sb.append("<meta charset=\"euc-kr\">\n");
			//sb.append("<meta name=\"Content-Type\" content=\"text/html; charset=utf-8\" />");
			sb.append("<meta http-equiv=\"X-UA-Compatible\" content=\"IE=Edge\"/>\n");
			if(title!=null) sb.append("<title>"+title+"</title>\n");
			//sb.append("<meta name=\"apple-mobile-web-app-capable\" content=\"yes\" />");			
			
			// 현재시간
			long curTime = System.currentTimeMillis();
			// URL을 변경해주어 캐쉬에 영향 받지 않도록 한다
			String url=(urlPrefix+path+"/"+fileName);
			url+="?id="+curTime;
			if(isThumImg){
				thumUrl+=thumUrl.indexOf("?")>-1 ? "&" : "?";
				thumUrl+="id="+curTime;
			}
			// 페이스북
			if(title!=null) sb.append("<meta property=\"og:title\" content=\""+title+"\" />\n");
			//sb.append("<meta property=\"fb:app_id\" content=\"000000000000000\" />");  // app id
			sb.append("<meta property=\"og:type\" content=\"website\">\n");
			//sb.append("<meta property=\"og:site_name\" content=\"사이트명\" />");
			if(fileName!=null) sb.append("<meta property=\"og:url\" content=\""+url+"\" />\n");
			if(thumUrl!=null) sb.append("<meta property=\"og:image\" content=\""+thumUrl+"\">\n");
			if(description!=null) sb.append("<meta property=\"og:description\" content=\""+description+"\" />\n");
			
			// 트위터
			sb.append("<meta name=\"twitter:card\" content=\""+(isThumImg ? "summary_large_image" : "summary")+"\" />\n"); // summary_large_image 
			if(title!=null) sb.append("<meta name=\"twitter:title\" content=\""+title+"\" />\n");
			//<meta name="twitter:site"			content="그룹웨어">
			//<meta name="twitter:creator"		content="홍길동">
			if(thumUrl!=null) sb.append("<meta name=\"twitter:image\" content=\""+thumUrl+"\" />\n");
			if(description!=null) sb.append("<meta name=\"twitter:description\" content=\""+description+"\" />\n");
			
			sb.append("</head>\n");
			sb.append("<body>\n");
			sb.append(cont);
			sb.append("\n");
			sb.append("</body>\n</html>\n");
			
			// 파일안에 문자열 쓰기
			fw.write(sb.toString());
			fw.flush();
			
			// 객체 닫기
			fw.close(); 
			
			// 실제 저장될 웹서버 경로
			String newSavePath=path+File.separator+fileName;
			newSavePath = newSavePath.replace('\\', '/');
			
			// 기존 파일 삭제
			file=new File(webCopyBaseDir+newSavePath);
			if(file.isFile()) file.delete();
			
			distHandler.addWebList(savePath, newSavePath); // 배포목록에 추가
			distHandler.distribute(); // 파일 배포
		}catch(Exception e){
			e.printStackTrace();
		}

	}
	
	/** HTML - 특정 태그 제외하고 필터링 */
	public String filterHtml(String str, String strAllowTag){
		String pattern = "<(\\/?)(?!\\/####)([^<|>]+)?>";
		String[] allowTags = strAllowTag==null ? null : strAllowTag.split(",");
		StringBuffer buffer = new StringBuffer();
		if(allowTags!=null){
			for (int i = 0; i < allowTags.length; i++){
				 buffer.append("|" + allowTags[i].trim() + "(?!\\w)");
			 }
		}
		pattern = pattern.replace("####",buffer.toString());
		String html = str.replaceAll(pattern,"");
		return html;

	}
	
	/** HTML - 이미지 동영상 src 추출 [LIST] */
	public List<String> getMedeaSrcList(String str) {
		Pattern nonValidPattern = Pattern.compile("<[img|iframe][^>]*src=[\"']?([^>\"']+)[\"']?[^>]*>");
		List<String> result = new ArrayList<String>();
		Matcher matcher = nonValidPattern.matcher(str);
		while (matcher.find()) {
			result.add(matcher.group(1));
		}
		return result;
	}
	
	/** HTML - 이미지 동영상 src 추출 */
	public String getMedeaSrc(String str) {
		Pattern nonValidPattern = Pattern.compile("<[img|iframe][^>]*src=[\"']?([^>\"']+)[\"']?[^>]*>");
		Matcher matcher = nonValidPattern.matcher(str);
		while (matcher.find()) {
			return matcher.group(1);
		}
		return null;
	}
	
	/** HTML - 동영상에서 섬네일 url 찾기[유투브] */
	public String getThumbnailUrl(String url) {
		if(url.indexOf("youtube.com")>-1){
			String id=url.substring(url.lastIndexOf("/")+1);
			if(id==null || id.isEmpty()) return null;
			return "https://img.youtube.com/vi/"+id+"/0.jpg";
		}
		return null;
	}
	
	/** 배열에 해당 값이 있는지 여부 리턴 - indexOf */
	public boolean isIndexWithArray(String[] arr, String finding){
		if(finding==null || arr==null) return false;
		for(int i=0;i<arr.length;i++){
			if(finding.indexOf(arr[i])>-1) return true;
		}
		return false;
	}
	
	
}
