package com.innobiz.orange.web.cm.files;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.innobiz.orange.web.cm.config.ServerConfig;

/** 배포 매니저, DistHandler(배포핸들러)를 생성함 */
@Repository
public class DistManager {

	/** contextProperties, 배포 서버에 관한 정보를 가지고 있음 */
	@Resource(name="contextProperties")
	private Properties contextProperties;
	
	/** 컨텍스트 프라퍼티 */
	@Resource(name = "uploadManager")
	private UploadManager uploadManager;
	
	/** WEB 배포 정보 목록 */
	private List<DistTarget> webDistTargetList = null;
	
	/** WAS 배포 정보 목록 */
	private List<DistTarget> wasDistTargetList = null;

	/** WEB 배포 루트 경로 */
	private String webDistBaseDir = null;
	
	/** WAS 배포 루트 경로 */
	private String wasDistBaseDir = null;
	
	/** WEB 카피 루트 경로 */
	private String webCopyBaseDir = null;
	
	/** WAS 카피 루트 경로 */
	private String wasCopyBaseDir = null;
	
	/** 배포핸들러를  생성 */
	public DistHandler createHandler(String dir, Locale locale){
		return new DistHandler(this, dir, locale);
	}
	
	/** 배포할 웹서버 목록 */
	protected List<DistTarget> getWebList(){
		if(webDistTargetList == null) init();
		return webDistTargetList;
	}
	
	/** 배포할 와스 목록 */
	protected List<DistTarget> getWasList(){
		if(wasDistTargetList == null) init();
		return wasDistTargetList;
	}
	
	/** 배포 경로 리턴 */
	public String getDistDir(String dir){
		return uploadManager.getDistDir(dir);
	}
	
	/** 설정의 초기화 */
	public void init(){
		
		String servers;
		servers = contextProperties.getProperty("distribute.web.servers","");
		webDistTargetList = toTargetList(servers);
		
		servers = contextProperties.getProperty("distribute.was.servers","");
		wasDistTargetList = toTargetList(servers);
		
		webDistBaseDir = contextProperties.getProperty("distribute.web.ftp.root","");
		wasDistBaseDir = contextProperties.getProperty("distribute.was.ftp.root","");
		
		webCopyBaseDir = contextProperties.getProperty("distribute.web.local.root","D:/gwOrange/htdocs");
		wasCopyBaseDir = contextProperties.getProperty("distribute.was.local.root","D:/gwOrange/upload");
		
		if(ServerConfig.IS_LOC && ServerConfig.IS_MOBILE){
			// 모바일 개발PC에서 해당 경로로 변경 - 로컬 개발용 하드코딩
			webCopyBaseDir = webCopyBaseDir.replace("orangeWeb", "orangeMobile");
		}
	}
	
	/** Target 리스트로 변환 */
	private List<DistTarget> toTargetList(String servers){
		List<DistTarget> list = new ArrayList<DistTarget>();
		String[] arr = servers.split(","), arr2;
		for(int i=0;i<arr.length;i++){
			arr2 = arr[i].split(":");
			if(arr2.length==3){
				list.add(new DistTarget(arr2[0], arr2[1], arr2[2]));
			}
		}
		return list;
	}
	
	/** 컨텍스트 프로퍼티 값 리턴 */
	public String getContextProperty(String key){
		return contextProperties.getProperty(key);
	}
	
	/** WAS 가 공유디스크를 사용하는지 여부 */
	public boolean isWebSharedDisk(){
		return "Y".equals(contextProperties.getProperty("distribute.web.shared.disk"));
	}
	
	/** WEB 이 공유디스크를 사용하는지 여부 */
	public boolean isWasSharedDisk(){
		return "Y".equals(contextProperties.getProperty("distribute.was.shared.disk"));
	}

	/** WEB 배포 루트 경로 */
	public String getWebDistBaseDir() {
		if(webDistBaseDir == null) init();
		return webDistBaseDir;
	}

	/** WAS 배포 루트 경로 */
	public String getWasDistBaseDir() {
		if(wasDistBaseDir == null) init();
		return wasDistBaseDir;
	}

	/** WEB Copy 루트 경로 */
	public String getWebCopyBaseDir() {
		if(webCopyBaseDir == null) init();
		return webCopyBaseDir;
	}

	/** WAS Copy 루트 경로 */
	public String getWasCopyBaseDir() {
		if(wasCopyBaseDir == null) init();
		return wasCopyBaseDir;
	}
}
