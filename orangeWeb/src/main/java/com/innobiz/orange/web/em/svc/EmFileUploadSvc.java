package com.innobiz.orange.web.em.svc;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.innobiz.orange.web.bb.svc.BbBullFileSvc;
import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.files.DistHandler;
import com.innobiz.orange.web.cm.files.DistManager;
import com.innobiz.orange.web.cm.files.UploadManager;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.SessionUtil;
import com.innobiz.orange.web.cm.utils.StringUtil;
import com.innobiz.orange.web.cm.vo.CommonFileVo;
import com.innobiz.orange.web.ct.svc.CtBullMastSvc;
import com.innobiz.orange.web.dm.svc.DmFileSvc;
import com.innobiz.orange.web.em.utils.EmCmUtil;
import com.innobiz.orange.web.em.vo.EmTmpFileTVo;
import com.innobiz.orange.web.wf.svc.WfFileSvc;
import com.innobiz.orange.web.wh.svc.WhFileSvc;
import com.innobiz.orange.web.wj.svc.WjFileSvc;
import com.innobiz.orange.web.wl.svc.WlFileSvc;

@Service
public class EmFileUploadSvc {
	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(EmFileUploadSvc.class);
	
	/** 업로드 메니저 */
	@Resource(name = "uploadManager")
	private UploadManager uploadManager;
	
	/** 배포 매니저 */
	@Resource(name = "distManager")
	private DistManager distManager;
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;
	
	/** 게시파일 서비스 */
	@Resource(name = "bbBullFileSvc")
	private BbBullFileSvc bbBullFileSvc;
	
	/** 파일 서비스 */
	@Resource(name = "dmFileSvc")
	private DmFileSvc dmFileSvc;
	
	/** 커뮤니티 게시판 서비스 */
	@Autowired
	private CtBullMastSvc ctBullMastSvc;
	
	/** 업무일지 서비스 */
	@Resource(name = "wlFileSvc")
	private WlFileSvc wlFileSvc;
	
	/** 헬프데스크 서비스 */
	@Resource(name = "whFileSvc")
	private WhFileSvc whFileSvc;
	
	/** 업무관리 서비스*/
	@Resource(name = "wfFileSvc")
	private WfFileSvc wfFileSvc;
	
	/** 업무보고(다인) 서비스*/
	@Resource(name = "wjFileSvc")
	private WjFileSvc wjFileSvc;
	
	/** 파일을 새이름으로 복사 후 파일 배포 (WAS DISK) */
	public String copyAndDist(HttpServletRequest request, String path, String savePath, String ext) throws IOException, CmException {
		DistHandler distHandler = distManager.createHandler(path, SessionUtil.getLocale(request));  // 업로드 경로 설정
		
		// baseDir
		String wasCopyBaseDir = distManager.getWasCopyBaseDir();
		if (wasCopyBaseDir == null) {
			distManager.init();
			wasCopyBaseDir = distManager.getWasCopyBaseDir();
		}
		
		String distPath = distHandler.addWasList(savePath);
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("distPath = " + distPath);
		}
		distHandler.distribute();
		
		// 임시파일 삭제
		removeTempDir(savePath);
		return distPath;
	}
	
	/** 파일명 새로 생성 */
	public String getFileName(char prefix , String orginalDir){
		String fileName;
		int p = orginalDir.replace('\\', '/').lastIndexOf('/');
		if (p >= 0) fileName = orginalDir.substring(p + 1);
		else fileName = orginalDir;
		p = fileName.lastIndexOf('.');
		String ext = p <= 0 ? "" : fileName.substring(p);
		String newfileName = prefix + StringUtil.getNextHexa()+ext;
		
		return newfileName;
	}
	
	/** 임시파일정보 조회 */
	public EmTmpFileTVo getEmTmpFileVo(Integer tmpFileId) throws SQLException{
		if(tmpFileId==null) return null;
		EmTmpFileTVo emTmpFileTVo = new EmTmpFileTVo();
		emTmpFileTVo.setTmpFileId(tmpFileId);
		return (EmTmpFileTVo)commonDao.queryVo(emTmpFileTVo);
	}
	
	/** 임시파일의 이미지 정보 조회 */
	public Map<String, String> getImgFile(Integer tmpFileId){
		Map<String, String> returnMap = null;
		try{
			EmTmpFileTVo emTmpFileTVo = new EmTmpFileTVo();
			emTmpFileTVo.setTmpFileId(tmpFileId);
			emTmpFileTVo = (EmTmpFileTVo)commonDao.queryVo(emTmpFileTVo);
			if(emTmpFileTVo==null) return null;
			
			String filePath = emTmpFileTVo.getSavePath();
			File file = new File(filePath);
	    	if(file.isFile()){
	    		returnMap = new HashMap<String,String>();
	    		BufferedImage bimg = ImageIO.read(new File(filePath));
	    		returnMap.put("wdth", Integer.toString(bimg.getWidth()));
	    		returnMap.put("hght", Integer.toString(bimg.getHeight()));
	    		
	    		// 파일 새이름으로 복사 후 파일 배포
				//String newSavePath = emFileUploadSvc.copyAndDist(request, "/bb", emTmpFileTVo.getSavePath(), emTmpFileTVo.getFileExt());
				
	    		returnMap.put("imgPath", null);
				
	    	}
		}catch(Exception e){
			
			return null;
		}
		return null;
	}
	
	/** 임시 저장 디렉토리 삭제 */
	public void removeTempDir(String uploadDir) throws CmException{
		String path = uploadDir.substring(0,uploadDir.lastIndexOf("/"));
		File dir = new File(path);
		if(!dir.exists()) return;
		File[] files = dir.listFiles();
		for(int i=0;files!=null && i<files.length;i++){
			files[i].delete();
		}
		dir.delete();
	}
	
	/** 파일 목록 조회 */
	public void setFileList(ModelMap model, Map<String,String> paramMap) throws Exception{
		String refId=paramMap.get("refId"); // 레퍼런스ID(모듈별 pk)
		String refTyp=paramMap.get("refTyp"); // 모듈구분
		String compId=paramMap.get("compId");
		// 첨부파일 리스트 model에 추가
		if("bb".equals(refTyp)){// 게시판
			bbBullFileSvc.putFileListToModel(refId, model, compId);
		}else if("dm".equals(refTyp)){ // 문서관리
			dmFileSvc.putFileListToModel(refId, model, paramMap.get("tableName"), compId);
		}else if("ct".equals(refTyp)){ // 커뮤니티
			ctBullMastSvc.putFileListToModel(refId, model, compId);
		}else if("wl".equals(refTyp)){// 업무일지
			wlFileSvc.putFileListToModel(refId, model, compId);
		}else if("wh".equals(refTyp)){ // 헬프데스크
			whFileSvc.putFileListToModel(refId, model, compId, paramMap.get("statCd"));
		}else if("wf".equals(refTyp)){ // 업무관리
			wfFileSvc.putFileListToModel(paramMap.get("formNo"), refId, model, compId, null);
		}else if("wp".equals(refTyp)){ // 프로젝트 관리
			
		}else if("wj".equals(refTyp)){// 업무일지
			wjFileSvc.putFileListToModel(refId, model, compId, null);
		}
	}
	
	/** 모듈별 파일을 임시파일로 복사 */
	public void copyToTmpFile(HttpServletRequest request, ModelMap model, Map<String,String> paramMap) throws Exception{
		String refId=paramMap.get("refId");
		if(refId==null || refId.isEmpty()) return;
		String module=paramMap.get("module");
		//String compId=paramMap.get("compId");
		// 세션의 사용자 정보
		CommonFileVo fileVo = null;
		if("bb".equals(module)){
			
		}else if("dm".equals(module)){
			
		}else if("ct".equals(module)){
			
		}else if("wl".equals(module)){			
			fileVo = wlFileSvc.getFileVo(Integer.parseInt(refId));
		}
		
		if(fileVo!=null){
			// 파일 새이름으로 복사 후 파일 배포
			String newSavePath = copyFileTemp(request, fileVo.getSavePath());
			EmTmpFileTVo emTmpFileTVo = new EmTmpFileTVo();
			emTmpFileTVo.setTmpFileId(commonSvc.nextVal("EM_TMP_FILE_T").intValue());
			emTmpFileTVo.setDispNm(fileVo.getDispNm());
			String ext = EmCmUtil.getFileExtension(fileVo.getSavePath(), true);
			emTmpFileTVo.setFileExt(ext);
			emTmpFileTVo.setFileSize(Long.valueOf(fileVo.getFileSize()));
			emTmpFileTVo.setSavePath(newSavePath);
			commonDao.insert(emTmpFileTVo);
			model.put("tmpFileId", emTmpFileTVo.getTmpFileId());
		}
	}
	
	/** 모듈별 파일을 임시파일로 복사 후 파일 배포 (WAS DISK) */
	private String copyFileTemp(HttpServletRequest request, String savePath) throws IOException, CmException {
		
		DistHandler distHandler = distManager.createHandler("temp", SessionUtil.getLocale(request));  // 업로드 경로 설정
		
		// baseDir
		String wasCopyBaseDir = distManager.getWasCopyBaseDir();
		if (wasCopyBaseDir == null) {
			distManager.init();
			wasCopyBaseDir = distManager.getWasCopyBaseDir();
		}
		String distPath = distHandler.addWasList(wasCopyBaseDir+savePath);
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("distPath = " + distPath);
		}
		distHandler.distribute();
		// 임시파일 삭제
		//removeTempDir(savePath);
		//return distPath;
		
		/*
		DistHandler distHandler = distManager.createHandler("temp", SessionUtil.getLocale(request));  // 업로드 경로 설정
		
		// baseDir
		String wasCopyBaseDir = distManager.getWasCopyBaseDir();
		if (wasCopyBaseDir == null) {
			distManager.init();
			wasCopyBaseDir = distManager.getWasCopyBaseDir();
		}
		
		// 임시파일 경로
		String distPath=wasCopyBaseDir+distHandler.getTempDir();
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("distPath = " + distPath);
		}
		
		distHandler.copyFile(wasCopyBaseDir+savePath, distPath);*/
		return wasCopyBaseDir+distPath;
	}
}
