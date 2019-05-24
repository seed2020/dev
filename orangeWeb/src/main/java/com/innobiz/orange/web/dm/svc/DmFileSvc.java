package com.innobiz.orange.web.dm.svc;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.innobiz.orange.web.bb.svc.BbBullFileSvc;
import com.innobiz.orange.web.bb.svc.BbCmSvc;
import com.innobiz.orange.web.bb.vo.BaBullFileDVo;
import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.files.CmMultipartFile;
import com.innobiz.orange.web.cm.files.DistHandler;
import com.innobiz.orange.web.cm.files.DistManager;
import com.innobiz.orange.web.cm.files.UploadHandler;
import com.innobiz.orange.web.cm.files.UploadManager;
import com.innobiz.orange.web.cm.files.ZipUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.SessionUtil;
import com.innobiz.orange.web.cm.utils.StringUtil;
import com.innobiz.orange.web.cm.vo.CommonFileVo;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.dm.vo.DmCommFileDVo;
import com.innobiz.orange.web.dm.vo.DmFileDVo;
import com.innobiz.orange.web.em.svc.EmAttachViewSvc;
import com.innobiz.orange.web.em.svc.EmFileUploadSvc;
import com.innobiz.orange.web.em.vo.EmTmpFileTVo;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtSysSvc;

/** 파일 서비스 */
@Service
public class DmFileSvc {

	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(DmFileSvc.class);

	/** 파일업로드 태그ID */
	public static final String FILES_ID = "dmfiles";

	/** 공통 서비스 */
	@Autowired
	private DmCmSvc dmCmSvc;

	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;

	/** 업로드 메니저 */
	@Resource(name = "uploadManager")
	private UploadManager uploadManager;

	/** 배포 매니저 */
	@Resource(name = "distManager")
	private DistManager distManager;
	
	/** 메세지 */
	@Autowired
    private MessageProperties messageProperties;
	
	/** 압축 파일관련 서비스 */
	@Resource(name = "zipUtil")
	private ZipUtil zipUtil;
	
	/** 게시판 공통 서비스 */
	@Autowired
	private BbCmSvc bbCmSvc;
	
	/** 게시파일 서비스 */
	@Resource(name = "bbBullFileSvc")
	private BbBullFileSvc bbBullFileSvc;
	
	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
	/** 문서뷰어 서비스 */
	@Resource(name = "emAttachViewSvc")
	private EmAttachViewSvc emAttachViewSvc;
	
	/** 파일업로드 서비스 */
	@Resource(name = "emFileUploadSvc")
	private EmFileUploadSvc emFileUploadSvc;
	
	/** 파일 업로드 */
	public UploadHandler upload(HttpServletRequest request) throws CmException, IOException {
		UploadHandler uploadHandler = uploadManager.createHandler(request, "temp", "dm");
		uploadHandler.upload(); // 업로드 파일 정보
		return uploadHandler;
	}
	
	/** 파일 업로드 */
	public UploadHandler upload(HttpServletRequest request, String prefix) throws CmException, IOException {
		UploadHandler uploadHandler = uploadManager.createHandler(request, "temp", prefix);
		uploadHandler.upload(); // 업로드 파일 정보
		return uploadHandler;
	}

	/** 파일 삭제 (DISK) */
	public void deleteDiskFiles(List<CommonFileVo> deletedFileList) {
		if (deletedFileList == null || deletedFileList.size() == 0) return;
		// baseDir
		String wasCopyBaseDir = distManager.getWasCopyBaseDir();
		if (wasCopyBaseDir == null) {
			distManager.init();
			wasCopyBaseDir = distManager.getWasCopyBaseDir();
		}
		for (CommonFileVo fileVo : deletedFileList) {
			String savePath = fileVo.getSavePath();
			boolean deleted = new File(wasCopyBaseDir+savePath).delete();
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("savePath = " + savePath + ", deleted = " + deleted);
			}
		}
	}

	/** 파일 배포 */
	private void distribute(HttpServletRequest request, String path, List<MultipartFile> mpFileList) throws IOException, CmException {
		DistHandler distHandler = distManager.createHandler(path, SessionUtil.getLocale(request));  // 업로드 경로 설정
		
		for (MultipartFile mpFile : mpFileList) {
			CmMultipartFile file = (CmMultipartFile) mpFile;
			String distPath = distHandler.addWasList(file.getSavePath());
			file.setSavePath(distPath);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("distPath = " + distPath);
			}
		}
		distHandler.distribute();
	}

	/** 파일 저장 (DB) */
	public List<CommonFileVo> saveDmFile(HttpServletRequest request, String refId, QueryQueue queryQueue, String tableName, String path) throws IOException, CmException, SQLException {

		// Multipart 파일 리스트
		List<MultipartFile> mpFileList = ((MultipartHttpServletRequest) request).getFiles(FILES_ID + "_file");
		if(path == null) path = "/dm/doc";
		// 파일 배포
		distribute(request, path, mpFileList);

		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);

		List<CommonFileVo> deletedFileList = new ArrayList<CommonFileVo>();
		String[] fileIds = request.getParameterValues(FILES_ID + "_fileId");
		String[] valids = request.getParameterValues(FILES_ID + "_valid");
		String[] useYns = request.getParameterValues(FILES_ID + "_useYn");
		String[] tmpFileId = request.getParameterValues("tmpFileId");  // 임시파일ID
		// 임시파일VO
		EmTmpFileTVo emTmpFileTVo;
		CmMultipartFile file;
		int insertCnt = 0, dispOrdr = 0;
		if(valids != null){
			for (int i = 0; i < valids.length; i++) {
				if ("N".equals(useYns[i])) {
					if ("Y".equals(valids[i])) continue;
	
					// 첨부파일(DM_FILE_D) 테이블 - DELETE
					DmFileDVo dmFileDVo = new DmFileDVo();
					if(tableName != null) dmFileDVo.setTableName(tableName);
					dmFileDVo.setFileId(Integer.valueOf(fileIds[i]));
					queryQueue.delete(dmFileDVo);
	
					// 첨부파일(DM_FILE_D) 테이블 - SELECT
					DmFileDVo dmFileVo = (DmFileDVo) commonDao.queryVo(dmFileDVo);
					if (dmFileVo != null) deletedFileList.add(dmFileVo);
					continue;
				}
	
				dispOrdr++;
				if ("N".equals(valids[i])) {
					if ("".equals(fileIds[i])) continue;
	
					// 첨부파일(DM_FILE_D) 테이블 - UPDATE
					DmFileDVo dmFileDVo = new DmFileDVo();
					if(tableName != null) dmFileDVo.setTableName(tableName);
					dmFileDVo.setDispOrdr(dispOrdr);
					queryQueue.update(dmFileDVo);
				} else {
					// 첨부파일(DM_FILE_D) 테이블 - INSERT
					DmFileDVo dmFileDVo = new DmFileDVo();
					
					if(tmpFileId!=null && tmpFileId[i]!=null && !tmpFileId[i].isEmpty()){
						emTmpFileTVo = emFileUploadSvc.getEmTmpFileVo(Integer.parseInt(tmpFileId[i]));
						if(emTmpFileTVo==null) continue;
						
						// 파일 새이름으로 복사 후 파일 배포
						String newSavePath = emFileUploadSvc.copyAndDist(request, path, emTmpFileTVo.getSavePath(), emTmpFileTVo.getFileExt());
						dmFileDVo.setDispNm(emTmpFileTVo.getDispNm());
						dmFileDVo.setFileExt(emTmpFileTVo.getFileExt());
						dmFileDVo.setFileSize((long)emTmpFileTVo.getFileSize());
						dmFileDVo.setSavePath(newSavePath);
					}else{
						file = (CmMultipartFile) mpFileList.get(insertCnt++);
						if(file==null) continue;
						dmFileDVo.setDispNm(file.getOriginalFilename());
						dmFileDVo.setFileExt(file.getExt());
						dmFileDVo.setFileSize(file.getSize());
						dmFileDVo.setSavePath(file.getSavePath());
					}
					
					if(tableName != null) dmFileDVo.setTableName(tableName);
					dmFileDVo.setFileId(dmCmSvc.createFileId("DM_FILE_D"));
					dmFileDVo.setRefId(refId);
					dmFileDVo.setDispOrdr(dispOrdr);
					dmFileDVo.setUseYn("Y");
					dmFileDVo.setRegrUid(userVo.getUserUid());
					dmFileDVo.setRegDt("sysdate");
	
					queryQueue.insert(dmFileDVo);
				}
			}
		}
		return deletedFileList;
	}

	/** 첨부파일 리턴 */
	public CommonFileVo getFileVo(Integer fileId, String tableName) throws SQLException {
		// 첨부파일(DM_FILE_D) 테이블 - SELECT
		DmFileDVo dmFileDVo = new DmFileDVo();
		if(tableName !=null) dmFileDVo.setTableName(tableName);
		dmFileDVo.setFileId(fileId);
		dmFileDVo = (DmFileDVo) commonDao.queryVo(dmFileDVo);
		return dmFileDVo;
	}

	/** 첨부파일 목록 리턴 */
	@SuppressWarnings("unchecked")
	private List<CommonFileVo> getFileVoList(String refId, String tableName) throws SQLException {
		if (refId == null) return new ArrayList<CommonFileVo>();
		
		// 첨부파일(DM_FILE_D) 테이블 - SELECT
		DmFileDVo dmFileDVo = new DmFileDVo();
		if(tableName != null) dmFileDVo.setTableName(tableName);
		dmFileDVo.setRefId(refId);
		return (List<CommonFileVo>) commonDao.queryList(dmFileDVo);
	}
	
	/** 첨부파일 목록 리턴 */
	public Integer getFileVoListCnt(String refId, String tableName) throws SQLException {
		if (refId == null) return 0;
		
		// 첨부파일(DM_FILE_D) 테이블 - SELECT
		DmFileDVo dmFileDVo = new DmFileDVo();
		if(tableName != null) dmFileDVo.setTableName(tableName);
		dmFileDVo.setRefId(refId);
		return commonDao.count(dmFileDVo);
	}

	/** 첨부파일 리스트 model에 추가 */
	public void putFileListToModel(String refId, ModelMap model, String tableName, String compId) throws SQLException {
		List<CommonFileVo> fileVoList = getFileVoList(refId, tableName);
		model.put("fileVoList", fileVoList);
		model.put("filesId", FILES_ID);
		// 확장자 허용제한 
		ptSysSvc.setAttachExtMap(model, compId, "dm");
		
		// 다운로드|문서뷰어 사용여부
		emAttachViewSvc.chkAttachSetup(model, compId);
	}

	/** 첨부파일 삭제 (DB) */
	public List<CommonFileVo> deleteDmFile(String refId, QueryQueue queryQueue, String tableName) throws SQLException {
		// 첨부파일(DM_FILE_D) 테이블 - DELETE
		DmFileDVo dmFileDVo = new DmFileDVo();
		if(tableName != null) dmFileDVo.setTableName(tableName);
		dmFileDVo.setRefId(refId);
		queryQueue.delete(dmFileDVo);

		return getFileVoList(refId, tableName);
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
	
	/** 첨부파일 복사 (DB, DISK) - 문서 ==> 게시판 */
	public void copyBullFile(HttpServletRequest request, String refId, String newRefId, QueryQueue queryQueue, String tableName, List<DmCommFileDVo> copyFileList, String docTyp) throws SQLException, IOException, CmException {
		// baseDir
		String wasCopyBaseDir = distManager.getWasCopyBaseDir();
		if (wasCopyBaseDir == null) {
			distManager.init();
			wasCopyBaseDir = distManager.getWasCopyBaseDir();
		}
				
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		String originPath = getFilePrefix(docTyp);
				
		// 첨부파일 목록
		List<CommonFileVo> fileVoList = getFileVoList(refId, tableName);
		DmCommFileDVo dmCommFileDVo;
		File file = null;
		for (CommonFileVo fileVo : fileVoList) {
			file = new File(wasCopyBaseDir+fileVo.getSavePath());
			if(!file.isFile()) continue;
			// 파일 새이름으로 복사 후 파일 배포
			//String newSavePath = copyAndDist(request, "/dm/doc", fileVo.getSavePath(), fileVo.getFileExt(), wasCopyBaseDir);
			// 복사할 새파일이름 정의
			String newSavePath = getFileNewPath(request, "/bb", originPath, fileVo.getSavePath(), fileVo.getFileExt());

			// 게시물첨부파일(BA_BULL_FILE_D) 테이블 - INSERT
			BaBullFileDVo baBullFileDVo = new BaBullFileDVo();
			BeanUtils.copyProperties(fileVo, baBullFileDVo, new String[]{"refId", "savePath", "regrUid", "regDt"});
			baBullFileDVo.setFileId(bbCmSvc.createFileId());
			baBullFileDVo.setRefId(newRefId);
			baBullFileDVo.setSavePath(newSavePath);
			baBullFileDVo.setRegrUid(userVo.getUserUid());
			baBullFileDVo.setRegDt("sysdate");
			queryQueue.insert(baBullFileDVo);
			
			// db 저장 이후 실제파일 복사 준비
			dmCommFileDVo = new DmCommFileDVo();
			dmCommFileDVo.setOriginSavePath(fileVo.getSavePath());
			dmCommFileDVo.setMdPath("/bb");
			dmCommFileDVo.setNewSavePath(newSavePath);
			
			copyFileList.add(dmCommFileDVo);
		}
	}
	
	/** 첨부파일 복사 (DB, DISK) */
	public void copyDmFile(HttpServletRequest request, String refId, String newRefId, 
			QueryQueue queryQueue, String fromTblName, String toTblName, List<DmCommFileDVo> copyFileList) throws SQLException, IOException, CmException {
		if(copyFileList == null) return;
		// baseDir
		String wasCopyBaseDir = distManager.getWasCopyBaseDir();
		if (wasCopyBaseDir == null) {
			distManager.init();
			wasCopyBaseDir = distManager.getWasCopyBaseDir();
		}
		
		// 복사할 대상의 파일경로 prefix
		String path = getFilePrefix(toTblName == null ? "psn" : "doc");
		// 원본 파일경로 prefix
		String originPath = getFilePrefix(fromTblName == null ? "psn" : "doc");
				
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);

		// 첨부파일 목록
		List<CommonFileVo> fileVoList = getFileVoList(refId, fromTblName);
		DmCommFileDVo dmCommFileDVo;
		File file = null;
		for (CommonFileVo fileVo : fileVoList) {
			file = new File(wasCopyBaseDir+fileVo.getSavePath());
			if(!file.isFile()) continue;
			// 파일 새이름으로 복사 후 파일 배포
			//String newSavePath = copyAndDist(request, "/dm/doc", fileVo.getSavePath(), fileVo.getFileExt(), wasCopyBaseDir);
			// 복사할 새파일이름 정의
			String newSavePath = getFileNewPath(request, path, originPath, fileVo.getSavePath(), fileVo.getFileExt());

			// 첨부파일(DM_FILE_D) 테이블 - INSERT
			DmFileDVo dmFileVo = new DmFileDVo();			
			BeanUtils.copyProperties(fileVo, dmFileVo, new String[]{"refId", "savePath", "regrUid", "regDt"});
			if(toTblName != null) dmFileVo.setTableName(toTblName);
			dmFileVo.setFileId(dmCmSvc.createFileId("DM_FILE_D"));
			dmFileVo.setRefId(newRefId);
			dmFileVo.setSavePath(newSavePath);
			dmFileVo.setRegrUid(userVo.getUserUid());
			dmFileVo.setRegDt("sysdate");
			queryQueue.insert(dmFileVo);
			
			// db 저장 이후 실제파일 복사 준비
			dmCommFileDVo = new DmCommFileDVo();
			dmCommFileDVo.setOriginSavePath(fileVo.getSavePath());
			dmCommFileDVo.setMdPath("/dm/doc");
			dmCommFileDVo.setNewSavePath(newSavePath);
			
			copyFileList.add(dmCommFileDVo);
		}
	}
	
	/** 첨부파일 복사 (게시판,결재) */
	public void copySendFile(HttpServletRequest request, String refId,
			QueryQueue queryQueue, List<CommonFileVo> fileVoList, String tableName, String tabId, String docTyp, List<DmCommFileDVo> copyFileList, String regrUid) throws SQLException, IOException, CmException {
		// baseDir
		String wasCopyBaseDir = distManager.getWasCopyBaseDir();
		if (wasCopyBaseDir == null) {
			distManager.init();
			wasCopyBaseDir = distManager.getWasCopyBaseDir();
		}
		
		// 복사할 대상의 파일경로 prefix
		String path = getFilePrefix(tabId);
		// 원본 파일경로 prefix
		String originPath = getFilePrefix(docTyp);
				
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);

		// 첨부파일 목록
		DmCommFileDVo dmCommFileDVo;
		File file = null;
		for (CommonFileVo fileVo : fileVoList) {
			file = new File(wasCopyBaseDir+fileVo.getSavePath());
			if(!file.isFile()) continue;
			// 파일 새이름으로 복사 후 파일 배포
			//String newSavePath = copyAndDist(request, "/dm/doc", fileVo.getSavePath(), fileVo.getFileExt(), wasCopyBaseDir);
			// 복사할 새파일이름 정의
			String newSavePath = getFileNewPath(request, path, originPath, fileVo.getSavePath(), fileVo.getFileExt());

			// 첨부파일(DM_FILE_D) 테이블 - INSERT
			DmFileDVo dmFileVo = new DmFileDVo();			
			BeanUtils.copyProperties(fileVo, dmFileVo, new String[]{"refId", "savePath", "regrUid", "regDt"});
			if(tableName != null) dmFileVo.setTableName(tableName);
			dmFileVo.setFileId(dmCmSvc.createFileId("DM_FILE_D"));
			dmFileVo.setRefId(refId);
			dmFileVo.setSavePath(newSavePath);
			if(regrUid == null || regrUid.isEmpty()) dmFileVo.setRegrUid(userVo.getUserUid());
			else dmFileVo.setRegrUid(regrUid);
			dmFileVo.setRegDt("sysdate");
			queryQueue.insert(dmFileVo);
			
			// db 저장 이후 실제파일 복사 준비
			dmCommFileDVo = new DmCommFileDVo();
			dmCommFileDVo.setOriginSavePath(fileVo.getSavePath());
			dmCommFileDVo.setMdPath("/dm/doc");
			dmCommFileDVo.setNewSavePath(newSavePath);
			
			copyFileList.add(dmCommFileDVo);
		}
	}
	/** 파일 저장 경로 Prefix*/
	public String getFilePrefix(String tabId){
		String path = "/dm/doc";
		if("brd".equals(tabId))//게시판
			path = "/bb";
		else if("psn".equals(tabId))//게시판
			path = "/dm/psn";
		return path;
	}
	
	/** 실제 파일 복사*/
	public void copyFileList(HttpServletRequest request, String tabId, List<DmCommFileDVo> copyFileList) throws IOException, CmException {
		copyFileDist(request, getFilePrefix(tabId), copyFileList);
	}
	
	/** 파일을 새이름으로 복사 후 파일 배포 (WAS DISK) */
	/*private String copyAndDist(HttpServletRequest request, String path, String savePath, String ext, String wasCopyBaseDir) throws IOException, CmException {
		DistHandler distHandler = distManager.createHandler(path, SessionUtil.getLocale(request));  // 업로드 경로 설정
		
		// baseDir
		if (wasCopyBaseDir == null) {
			distManager.init();
			wasCopyBaseDir = distManager.getWasCopyBaseDir();
		}
				
		// 새이름
		String newSavePath = savePath.replace('\\', '/').substring(0, savePath.lastIndexOf('/')) + "/F" + StringUtil.getNextHexa() + "." + ext;

		// 파일복사
		distHandler.copyFile(wasCopyBaseDir+savePath, wasCopyBaseDir+newSavePath);
		
		String distPath = distHandler.addWasList(newSavePath);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("distPath = " + distPath);
		}
		distHandler.distribute();
		
		return newSavePath;//distPath;
	}*/
	
	/** 파일을 새이름으로 복사 후 파일 배포 (WAS DISK) */
	private void copyFileDist(HttpServletRequest request, String path, List<DmCommFileDVo> copyFileList) throws IOException, CmException {
		DistHandler distHandler = distManager.createHandler(path, SessionUtil.getLocale(request));  // 업로드 경로 설정
		
		// baseDir
		String wasCopyBaseDir = distManager.getWasCopyBaseDir();
		if (wasCopyBaseDir == null) {
			distManager.init();
			wasCopyBaseDir = distManager.getWasCopyBaseDir();
		}
		
		for(DmCommFileDVo fileVo : copyFileList){
			// 파일복사
			distHandler.copyFile(wasCopyBaseDir+fileVo.getOriginSavePath(), wasCopyBaseDir+fileVo.getNewSavePath());
		}
	}
	
	/** 파일복사를 위한 경로 및 파일명 설정 및 파일 배포 (WAS DISK) */
	private String getFileNewPath(HttpServletRequest request, String path, String originPath, String savePath, String ext) throws IOException, CmException {
		DistHandler distHandler = distManager.createHandler(path, SessionUtil.getLocale(request));  // 업로드 경로 설정
		
		// 새이름
		String newSavePath = savePath.replace('\\', '/').substring(0, savePath.lastIndexOf('/')) + "/F" + StringUtil.getNextHexa() + "." + ext;
		
		// 원본경로 prefix가 있으면...
		if(originPath != null && !originPath.isEmpty()){
			// 신규 파일경로 prefix가 해당 업무 path 와 같지 않으면 동일한 path 로 맞춰주기 위해 replace 한다. 
			if(!newSavePath.startsWith(path)){
				newSavePath = newSavePath.replace(originPath,path);
			}
		}
		
		String distPath = distHandler.addWasList(newSavePath);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("distPath = " + distPath);
		}
		distHandler.distribute();
		
		return newSavePath;//distPath;
	}
	
	/** 첨부파일 목록 조회 */
	public ModelAndView getFileList(HttpServletRequest request, String fileIds, 
			String actionParam, String tableName, List<String> fileNmList, String compId) throws SQLException, IOException, CmException {
		
		// 다운로드 체크
		emAttachViewSvc.chkAttachDown(request, compId);
					
		List<CommonFileVo> fileVoList = new ArrayList<CommonFileVo>();
		// fileId
		String[] fileIdArray = fileIds.split(",");
		
		// baseDir
		String wasCopyBaseDir = distManager.getWasCopyBaseDir();
		if (wasCopyBaseDir == null) {
			distManager.init();
			wasCopyBaseDir = distManager.getWasCopyBaseDir();
		}
		
		CommonFileVo fileVo = null; 
		for (String fileId : fileIdArray) {
			// 첨부파일
			fileVo = getFileVo(Integer.valueOf(fileId), tableName);
			
			if (fileVo != null) {
				fileVo.setSavePath(wasCopyBaseDir+fileVo.getSavePath());
				File file = new File(fileVo.getSavePath());
				if (file.isFile()) {
					fileVoList.add(fileVo);
					if(fileNmList != null) fileNmList.add(fileVo.getDispNm());
				}
			}
		}
		
		// 파일이 몇개인가
		if (fileVoList.size() == 0) {
			ModelAndView mv = new ModelAndView("cm/result/commonResult");
			Locale locale = SessionUtil.getLocale(request);
			// cm.msg.file.fileNotFound=요청한 파일을 찾을 수 없습니다.
			mv.addObject("message", messageProperties.getMessage("cm.msg.file.fileNotFound", locale));
			return mv;
			
		} else if (fileVoList.size() == 1) {
			CommonFileVo fileVoVo = fileVoList.get(0);
			String savePath = fileVoVo.getSavePath();
			File file = new File(savePath);
			ModelAndView mv = new ModelAndView("fileDownloadView");
			mv.addObject("downloadFile", file);
			mv.addObject("realFileName", fileVoVo.getDispNm());
			return mv;
			
		} else {
			File zipFile = zipUtil.makeZipFile(fileVoList, "files.zip");
			ModelAndView mv = new ModelAndView("fileDownloadView");
			mv.addObject("downloadFile", zipFile);
			mv.addObject("realFileName", zipFile.getName());
			return mv;
		}
					
	}
	
	/** 게시물첨부파일VO 생성 */
	public void setBaFileVo(HttpServletRequest request, BaBullFileDVo baBullFileDVo, CommonFileVo fileVo, String path, String originPath, String refId, UserVo userVo) throws IOException, CmException, SQLException {
		String newSavePath = getFileNewPath(request, path, originPath, fileVo.getSavePath(), fileVo.getFileExt());
		// 게시물첨부파일(BA_BULL_FILE_D) 테이블 - INSERT
		BeanUtils.copyProperties(fileVo, baBullFileDVo, new String[]{"refId", "savePath", "regrUid", "regDt"});
		baBullFileDVo.setFileId(bbCmSvc.createFileId());
		baBullFileDVo.setRefId(refId);
		baBullFileDVo.setSavePath(newSavePath);
		baBullFileDVo.setRegrUid(userVo.getUserUid());
		baBullFileDVo.setRegDt("sysdate");
	}
	
	/** 문서첨부파일VO 생성 */
	public void setDmFileVo(HttpServletRequest request, DmFileDVo dmFileDVo, CommonFileVo fileVo, String path, String originPath, String refId, UserVo userVo) throws IOException, CmException, SQLException {
		String newSavePath = getFileNewPath(request, path, originPath, fileVo.getSavePath(), fileVo.getFileExt());
		// 게시물첨부파일(BA_BULL_FILE_D) 테이블 - INSERT
		BeanUtils.copyProperties(fileVo, dmFileDVo, new String[]{"refId", "savePath", "regrUid", "regDt"});
		dmFileDVo.setFileId(dmCmSvc.createFileId("DM_FILE_D"));
		dmFileDVo.setRefId(refId);
		dmFileDVo.setSavePath(newSavePath);
		dmFileDVo.setRegrUid(userVo.getUserUid());
		dmFileDVo.setRegDt("sysdate");
	}
	
	/** 문서 보내기 - 파일 저장 */
	public void saveDmSendFile(HttpServletRequest request, String refId, QueryQueue queryQueue, 
			String docTyp, String tabId, String tableName, List<DmCommFileDVo> copyFileList) throws IOException, CmException, SQLException {
		
		// 원본 파일경로 prefix
		String path = getFilePrefix(tabId);
		// 원본 파일경로 prefix
		String originPath = getFilePrefix(docTyp);
		
		// Multipart 파일 리스트
		List<MultipartFile> mpFileList = ((MultipartHttpServletRequest) request).getFiles(FILES_ID + "_file");
		
		// 파일 배포
		distribute(request, path, mpFileList);

		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);

		//List<CommonFileVo> fileVoList = new ArrayList<CommonFileVo>();
		String[] fileIds = request.getParameterValues(FILES_ID + "_fileId");
		String[] valids = request.getParameterValues(FILES_ID + "_valid");
		String[] useYns = request.getParameterValues(FILES_ID + "_useYn");
		String[] tmpFileId = request.getParameterValues("tmpFileId");  // 임시파일ID
		// 임시파일VO
		EmTmpFileTVo emTmpFileTVo;
		CmMultipartFile file;
		int insertCnt = 0, dispOrdr = 0;
		DmCommFileDVo dmCommFileDVo = null;
		if(valids != null){
			for (int i = 0; i < valids.length; i++) {
				// 삭제파일 제외
				if ("N".equals(useYns[i])) continue;
				dispOrdr++;
				if ("N".equals(valids[i])) {//기존 파일
					if ("".equals(fileIds[i])) continue;
					CommonFileVo fileVo = null;
					
					//복사원본이 게시판이면
					if("brd".equals(docTyp)) fileVo = bbBullFileSvc.getFileVo(Integer.parseInt(fileIds[i]));
					else fileVo = getFileVo(Integer.parseInt(fileIds[i]), "doc".equals(docTyp) ? tableName : null); 
					
					if(fileVo == null) continue;
					if("brd".equals(tabId)){
						BaBullFileDVo baBullFileDVo = new BaBullFileDVo();
						// 게시판 파일VO 생성
						this.setBaFileVo(request, baBullFileDVo, fileVo, path, originPath, refId, userVo);
						// 순서 변경
						baBullFileDVo.setDispOrdr(dispOrdr);
						// 목록에 저장
						//fileVoList.add(baBullFileDVo);
						
						// 실제파일 복사 준비
						dmCommFileDVo = new DmCommFileDVo();
						dmCommFileDVo.setOriginSavePath(fileVo.getSavePath());
						dmCommFileDVo.setMdPath(path);
						dmCommFileDVo.setNewSavePath(baBullFileDVo.getSavePath());
						
						copyFileList.add(dmCommFileDVo);
						
						queryQueue.insert(baBullFileDVo);
					}else{
						DmFileDVo dmFileDVo = new DmFileDVo();
						// 파일VO 생성
						setDmFileVo(request, dmFileDVo, fileVo, path, originPath, refId, userVo);
						
						if(tableName != null && "doc".equals(tabId)) dmFileDVo.setTableName(tableName);
						// 순서 변경
						dmFileDVo.setDispOrdr(dispOrdr);
						// 목록에 저장
						//fileVoList.add(dmFileDVo);
						
						// 실제파일 복사 준비
						dmCommFileDVo = new DmCommFileDVo();
						dmCommFileDVo.setOriginSavePath(fileVo.getSavePath());
						dmCommFileDVo.setMdPath(path);
						dmCommFileDVo.setNewSavePath(dmFileDVo.getSavePath());
						
						copyFileList.add(dmCommFileDVo);
						
						queryQueue.insert(dmFileDVo);
					}
					
				} else {// 신규파일
					if("brd".equals(tabId)){
						// 첨부파일(BA_BULL_FILE_D)
						BaBullFileDVo baBullFileDVo = new BaBullFileDVo();
						if(tmpFileId!=null && tmpFileId[i]!=null && !tmpFileId[i].isEmpty()){
							emTmpFileTVo = emFileUploadSvc.getEmTmpFileVo(Integer.parseInt(tmpFileId[i]));
							if(emTmpFileTVo==null) continue;
							
							// 파일 새이름으로 복사 후 파일 배포
							String newSavePath = emFileUploadSvc.copyAndDist(request, path, emTmpFileTVo.getSavePath(), emTmpFileTVo.getFileExt());
							baBullFileDVo.setDispNm(emTmpFileTVo.getDispNm());
							baBullFileDVo.setFileExt(emTmpFileTVo.getFileExt());
							baBullFileDVo.setFileSize((long)emTmpFileTVo.getFileSize());
							baBullFileDVo.setSavePath(newSavePath);
						}else{
							file = (CmMultipartFile) mpFileList.get(insertCnt++);
							if(file==null) continue;
							baBullFileDVo.setDispNm(file.getOriginalFilename());
							baBullFileDVo.setFileExt(file.getExt());
							baBullFileDVo.setFileSize(file.getSize());
							baBullFileDVo.setSavePath(file.getSavePath());
						}
						baBullFileDVo.setFileId(bbCmSvc.createFileId());
						baBullFileDVo.setRefId(refId);
						baBullFileDVo.setDispOrdr(dispOrdr);
						baBullFileDVo.setUseYn("Y");
						baBullFileDVo.setRegrUid(userVo.getUserUid());
						baBullFileDVo.setRegDt("sysdate");
						//fileVoList.add(baBullFileDVo);
						
						queryQueue.insert(baBullFileDVo);
					}else{
						// 첨부파일(DM_FILE_D)
						DmFileDVo dmFileDVo = new DmFileDVo();
						if(tmpFileId!=null && tmpFileId[i]!=null && !tmpFileId[i].isEmpty()){
							emTmpFileTVo = emFileUploadSvc.getEmTmpFileVo(Integer.parseInt(tmpFileId[i]));
							if(emTmpFileTVo==null) continue;
							
							// 파일 새이름으로 복사 후 파일 배포
							String newSavePath = emFileUploadSvc.copyAndDist(request, path, emTmpFileTVo.getSavePath(), emTmpFileTVo.getFileExt());
							dmFileDVo.setDispNm(emTmpFileTVo.getDispNm());
							dmFileDVo.setFileExt(emTmpFileTVo.getFileExt());
							dmFileDVo.setFileSize((long)emTmpFileTVo.getFileSize());
							dmFileDVo.setSavePath(newSavePath);
						}else{
							file = (CmMultipartFile) mpFileList.get(insertCnt++);
							if(file==null) continue;
							dmFileDVo.setDispNm(file.getOriginalFilename());
							dmFileDVo.setFileExt(file.getExt());
							dmFileDVo.setFileSize(file.getSize());
							dmFileDVo.setSavePath(file.getSavePath());
						}
						
						if(tableName != null && "doc".equals(tabId)) dmFileDVo.setTableName(tableName);
						dmFileDVo.setFileId(dmCmSvc.createFileId("DM_FILE_D"));
						dmFileDVo.setRefId(refId);
						dmFileDVo.setDispOrdr(dispOrdr);
						dmFileDVo.setUseYn("Y");
						dmFileDVo.setRegrUid(userVo.getUserUid());
						dmFileDVo.setRegDt("sysdate");
						//fileVoList.add(dmFileDVo);
						
						queryQueue.insert(dmFileDVo);
					}
	
					//queryQueue.insert(dmFileDVo);
				}
			}
		}
		//return fileVoList;
	}
	
	/** 파일 목록 세팅 */
	public void setFileDVoList(CmMultipartFile file, List<CommonFileVo> fileVoList, String tgtTyp){
		
	}
	
	/** 첨부파일 목록 리턴 */
	public List<CommonFileVo> getDmFileVoList(String refId, String tableName) throws SQLException {
		return getFileVoList(refId, tableName);
	}
	
	
}
