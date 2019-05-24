package com.innobiz.orange.web.cm.files;

import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

import com.innobiz.orange.web.cm.utils.FileCleaner;
import com.innobiz.orange.web.cm.utils.StringUtil;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtSysSvc;

/** 업로드 메니저, UploadHandler를 생성함 */
@Repository
public class UploadManager {
	
	/** 컨텍스트 프라퍼티 */
	@Resource(name = "contextProperties")
	private Properties contextProperties;

	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
	/** 업로드 루트 경로 */
	private String uploadBaseDir = null;
	
	/** 배포 경로 리턴 */
	public String getDistDir(String dir){
		return getDistDir(dir, hasOption(dir, "yearDir"), hasOption(dir, "dateDir"), hasOption(dir, "tempDir"));
	}
	
	/** 배포 경로 리턴 */
	public String getDistDir(String dir, boolean hasYearDir, boolean hasDateDir, boolean hasTempDir) {
		return getDistDir(dir, hasYearDir, hasDateDir, hasTempDir, 0);
	}
	
	/** 배포 경로 리턴 */
	private String  getDistDir(String dir, boolean hasYearDir, boolean hasDateDir, boolean hasTempDir, int add) {
		String distDir = dir.replace('\\', '/');
		String dateDir = hasYearDir ? StringUtil.toUploadDirYear() :
			hasDateDir ? StringUtil.toUploadDirDate(add) : "";
		String tempDir = hasTempDir ? "/"+StringUtil.getNextHexa() : "";
		return (distDir.isEmpty() || distDir.startsWith("/") ? "" : "/")+distDir+dateDir+tempDir;
	}
	
	/** 업로드 루트 경로 리턴 */
	public String getUploadBaseDir(){
		return uploadBaseDir;
	}
	
	/** UploadHandler 생성 */
	public UploadHandler createHandler(HttpServletRequest request, String path, String module){
		if(uploadBaseDir == null) init();
		UploadHandler handler = new UploadHandler(this, request, path, hasOption(path, "yearDir"), hasOption(path, "dateDir"), hasOption(path, "tempDir"));
		try {
			UserVo userVo = LoginSession.getUser(request);
			Map<String, Integer> maxSizeMap = ptSysSvc.getAttachSizeMap(LoginSession.getLangTypCd(request), userVo.getCompId());
			Integer maxMegaBytes = maxSizeMap==null ? null : maxSizeMap.get(module);
			if(maxMegaBytes!=null) handler.setMaxMegaBytes(maxMegaBytes);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return handler;
	}
	
	/** contextProperties 에 날짜폴더옵션과, 임시경로폴더옵션의 유무를 체크함 */
	private boolean hasOption(String path, String whichOption){
		// 설정 체크 : upload.[path].dateDir, file.[path].tempDir
		// images/upload/or/stamp >> 설정이 없을 경우 뒤에서 경로를 하나씩 잘라내면서 옵션 체크함
		int p;
		String yn, dotPath = path.replace('/', '.');
		while((yn = contextProperties.getProperty("upload."+dotPath+"."+whichOption))==null){
			if((p = dotPath.lastIndexOf('.'))<0) break;
			dotPath = dotPath.substring(0, p);
		}
		// 디폴트 설정 체크 : upload.default.dateDir, file.default.tempDir
		if(yn==null) yn = contextProperties.getProperty("upload.default."+whichOption);
		return "Y".equals(yn);
	}
	
	/** 설정의 초기화 */
	public void init(){
		String base = contextProperties.getProperty("upload.base.dir", "D:/gwOrange/upload");
		uploadBaseDir = base;
	}
	
	/** 임시 디렉토리 리턴 */
	public String getTempDir(){
		if(uploadBaseDir==null) init();
		return uploadBaseDir + getDistDir("temp", false, true, true);
	}
	
	/** 임시 디렉토리 삭제 (0시20분) - 참조 : cron = "분 시 월 요일 년도" */
	@Scheduled(cron = "20 0 * * * *")
	public void deleteYesterdayDir(){
		if(uploadBaseDir==null) init();
		FileCleaner.delete(uploadBaseDir + getDistDir("temp", false, true, false, 24 * 60 * 60 * -1000));
	}
}
