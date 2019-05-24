package com.innobiz.orange.web.ct.svc;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.files.CmMultipartFile;
import com.innobiz.orange.web.cm.files.DistHandler;
import com.innobiz.orange.web.cm.files.DistManager;
import com.innobiz.orange.web.cm.files.UploadHandler;
import com.innobiz.orange.web.cm.files.UploadManager;
import com.innobiz.orange.web.cm.utils.SessionUtil;
import com.innobiz.orange.web.cm.utils.StringUtil;
import com.innobiz.orange.web.cm.vo.CommonFileVo;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.ct.vo.CtEstbBVo;
import com.innobiz.orange.web.ct.vo.CtFileDVo;
import com.innobiz.orange.web.dm.vo.DmCommFileDVo;
import com.innobiz.orange.web.em.svc.EmAttachViewSvc;
import com.innobiz.orange.web.em.svc.EmFileUploadSvc;
import com.innobiz.orange.web.em.vo.EmTmpFileTVo;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtSysSvc;

@Service
public class CtFileSvc {
	
	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(CtFileSvc.class);
	
	/** 파일업로드 태그ID */
	public static final String FILES_ID = "ctfiles";
	
	/** 커뮤니티 공통 서비스 */
	@Autowired
	private CtCmSvc ctCmSvc;
	
	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;

	/** 업로드 메니저 */
	@Resource(name = "uploadManager")
	private UploadManager uploadManager;

	/** 배포 매니저 */
	@Resource(name = "distManager")
	private DistManager distManager;
	
	/** 공통 서비스 */
	@Autowired
	private CtCmntSvc ctCmntSvc;
	
	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
	/** 문서뷰어 서비스 */
	@Resource(name = "emAttachViewSvc")
	private EmAttachViewSvc emAttachViewSvc;
	
	/** 파일업로드 서비스 */
	@Resource(name = "emFileUploadSvc")
	private EmFileUploadSvc emFileUploadSvc;
	
	/** 커뮤니티첨부파일 저장 (DB) */
	public List<CommonFileVo> saveNotcFile(HttpServletRequest request, String refId,
			QueryQueue queryQueue,  String fncUid) throws Exception {

		//repetFlag 반복 [파일설정] 개별반영(indiv)/전체반영(all)
		//savePath 반복파일 모두 삭제 하기위한 path명
		// Multipart 파일 리스트
 		List<MultipartFile> mpFileList = ((MultipartHttpServletRequest) request).getFiles(FILES_ID + "_file");

		// 파일 배포
		distribute(request, "/ct", mpFileList);

		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);

		List<CommonFileVo> deletedFileList = new ArrayList<CommonFileVo>();
		String[] fileIds = request.getParameterValues(FILES_ID + "_fileId");
		String[] valids = request.getParameterValues(FILES_ID + "_valid");  // 신규추가된 파일이면 Y
		String[] useYns = request.getParameterValues(FILES_ID + "_useYn");  // 삭제된 파일이면 N
		String[] tmpFileId = request.getParameterValues("tmpFileId");  // 임시파일ID
		// 임시파일VO
		EmTmpFileTVo emTmpFileTVo;
		//String savePath = "";
		CmMultipartFile file;
		int insertCnt = 0, dispOrdr = 0;
		for (int i = 0; i < valids.length; i++) {
			if ("N".equals(useYns[i])) {
				if ("Y".equals(valids[i])) continue;
				
				CtFileDVo ctFindFile = new CtFileDVo();
				ctFindFile.setFileId(Integer.valueOf(fileIds[i]));
				CtFileDVo ctFileVo = (CtFileDVo) commonDao.queryVo(ctFindFile);
				if(ctFileVo != null){
					//String[] splitData = ctFileVo.getSavePath().split("\\/");
					//savePath = splitData[(splitData.length)-1];
				}
				
				CtFileDVo ctFileDVo = new CtFileDVo();
				ctFileDVo.setFileId(Integer.valueOf(fileIds[i]));
				queryQueue.delete(ctFileDVo);
				
				CtFileDVo fileDVo = (CtFileDVo) commonDao.queryVo(ctFileDVo);
				if(fileDVo != null) deletedFileList.add(fileDVo);
				continue;
			}
			
			dispOrdr++;
			
			if("N".equals(valids[i])){
				if("".equals(fileIds[i])) continue;
				
				CtFileDVo ctFileDVo = new CtFileDVo();
				ctFileDVo.setFileId(Integer.valueOf(fileIds[i]));
				ctFileDVo.setDispOrdr(dispOrdr);
				queryQueue.update(ctFileDVo);
			}else{
				CtFileDVo ctFileDVo = new CtFileDVo();
				
				if(tmpFileId!=null && tmpFileId[i]!=null && !tmpFileId[i].isEmpty()){
					emTmpFileTVo = emFileUploadSvc.getEmTmpFileVo(Integer.parseInt(tmpFileId[i]));
					if(emTmpFileTVo==null) continue;
					
					// 파일 새이름으로 복사 후 파일 배포
					String newSavePath = emFileUploadSvc.copyAndDist(request, "/ct", emTmpFileTVo.getSavePath(), emTmpFileTVo.getFileExt());
					ctFileDVo.setDispNm(emTmpFileTVo.getDispNm());
					ctFileDVo.setFileExt(emTmpFileTVo.getFileExt());
					ctFileDVo.setFileSize((long)emTmpFileTVo.getFileSize());
					ctFileDVo.setSavePath(newSavePath);
				}else{
					file = (CmMultipartFile) mpFileList.get(insertCnt++);
					if(file==null) continue;
					ctFileDVo.setDispNm(file.getOriginalFilename());
					ctFileDVo.setFileExt(file.getExt());
					ctFileDVo.setFileSize(file.getSize());
					ctFileDVo.setSavePath(file.getSavePath());
				}
				ctFileDVo.setFileId(ctCmSvc.createFileId("CT_FILE_D"));
				ctFileDVo.setRefId(refId);
				ctFileDVo.setDispOrdr(dispOrdr);
				ctFileDVo.setRegDt("sysdate");
				ctFileDVo.setRegrUid(userVo.getUserUid());
				ctFileDVo.setUseYn("Y");
				ctFileDVo.setCtFncUid(fncUid);
				
				queryQueue.insert(ctFileDVo);
			}
		}
		return deletedFileList;
	}
	
	
	
	/** 파일 업로드 (DISK) */
	public UploadHandler upload(HttpServletRequest request) throws CmException, IOException {
		UploadHandler uploadHandler = uploadManager.createHandler(request, "temp", "ct");
		uploadHandler.upload(); // 업로드 파일 정보
		return uploadHandler;
	}
	
	/** 파일 삭제 (DISK) */
	public void deleteDiskFiles(List<CommonFileVo> deletedFileList) {
		if (deletedFileList == null || deletedFileList.size() == 0) return;

		for (CommonFileVo fileVo : deletedFileList) {
			if (fileVo == null) continue;
			String savePath = fileVo.getSavePath();
			boolean deleted = new File(savePath).delete();
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("savePath = " + savePath + ", deleted = " + deleted);
			}
		}
	}
	
	/** 파일 배포 (DISK) */
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
	
	/** 커뮤니티첨부파일 저장 (DB) */
	public List<CommonFileVo> saveSurvFile(HttpServletRequest request, String refId,
			QueryQueue queryQueue,  String fncUid, String ctId) throws Exception {

		//repetFlag 반복 [파일설정] 개별반영(indiv)/전체반영(all)
		//savePath 반복파일 모두 삭제 하기위한 path명
		// Multipart 파일 리스트
 		List<MultipartFile> mpFileList = ((MultipartHttpServletRequest) request).getFiles(FILES_ID + "_file");

		// 파일 배포
		distribute(request, "/ct", mpFileList);

		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);

		List<CommonFileVo> deletedFileList = new ArrayList<CommonFileVo>();
		String[] fileIds = request.getParameterValues(FILES_ID + "_fileId");
		String[] valids = request.getParameterValues(FILES_ID + "_valid");  // 신규추가된 파일이면 Y
		String[] useYns = request.getParameterValues(FILES_ID + "_useYn");  // 삭제된 파일이면 N
		String[] ctSendYn = request.getParameterValues(FILES_ID + "_ctSendYn");  // 커뮤니티게시글 보내기 여부
		String[] tmpFileId = request.getParameterValues("tmpFileId");  // 임시파일ID
		// 임시파일VO
		EmTmpFileTVo emTmpFileTVo;
		//String savePath ="";
		CmMultipartFile file;
		// baseDir
		String wasCopyBaseDir = distManager.getWasCopyBaseDir();
		if (wasCopyBaseDir == null) {
			distManager.init();
			wasCopyBaseDir = distManager.getWasCopyBaseDir();
		}
		
		int insertCnt = 0, dispOrdr = 0;
		for (int i = 0; i < valids.length; i++) {
			if ("N".equals(useYns[i])) {
				if ("Y".equals(valids[i])) continue;
				
				CtFileDVo ctFindFile = new CtFileDVo();
				ctFindFile.setFileId(Integer.valueOf(fileIds[i]));
				CtFileDVo ctFileVo = (CtFileDVo) commonDao.queryVo(ctFindFile);
				if(ctFileVo != null){
					//String[] splitData = ctFileVo.getSavePath().split("\\/");
					//savePath = splitData[(splitData.length)-1];
				}
				
				CtFileDVo ctFileDVo = new CtFileDVo();
				ctFileDVo.setFileId(Integer.valueOf(fileIds[i]));
				queryQueue.delete(ctFileDVo);
				
				CtFileDVo fileDVo = (CtFileDVo) commonDao.queryVo(ctFileDVo);
				if(fileDVo != null) deletedFileList.add(fileDVo);
				continue;
			}
			
			dispOrdr++;
			
			if("N".equals(valids[i])){
				if("".equals(fileIds[i])) continue;
				
				CtFileDVo ctFileDVo = new CtFileDVo();
				ctFileDVo.setFileId(Integer.valueOf(fileIds[i]));
				ctFileDVo.setDispOrdr(dispOrdr);
				queryQueue.update(ctFileDVo);
			}else{
				
				CtFileDVo ctFileDVo = new CtFileDVo();
				
				/**
				 * 커뮤니티 보내기 처리시 첨부파일 복사 처리
				 */
				long filesize=0;
				if("Y".equals(ctSendYn[i])){
					CtFileDVo ctFindFile = new CtFileDVo();
					ctFindFile.setFileId(Integer.valueOf(fileIds[i]));
					CtFileDVo ctFileVo = (CtFileDVo) commonDao.queryVo(ctFindFile);
					File copyfile = new File(wasCopyBaseDir+ctFileVo.getSavePath());
					if(!copyfile.isFile()) continue;
					// 파일 새이름으로 복사 후 파일 배포
					String newSavePath = copyAndDist(request, "/ct", ctFileVo.getSavePath(), ctFileVo.getFileExt());
					
					ctFileDVo.setDispNm(ctFileVo.getDispNm());
					ctFileDVo.setFileExt(ctFileVo.getFileExt());
					ctFileDVo.setFileSize(ctFileVo.getFileSize());
					ctFileDVo.setSavePath(newSavePath);
					//ctFileDVo.setSavePath(ctFileVo.getSavePath());
					filesize=ctFileVo.getFileSize();
				}
				else{
					if(tmpFileId!=null && tmpFileId[i]!=null && !tmpFileId[i].isEmpty()){
						emTmpFileTVo = emFileUploadSvc.getEmTmpFileVo(Integer.parseInt(tmpFileId[i]));
						if(emTmpFileTVo==null) continue;
						
						// 파일 새이름으로 복사 후 파일 배포
						String newSavePath = emFileUploadSvc.copyAndDist(request, "/ct", emTmpFileTVo.getSavePath(), emTmpFileTVo.getFileExt());
						ctFileDVo.setDispNm(emTmpFileTVo.getDispNm());
						ctFileDVo.setFileExt(emTmpFileTVo.getFileExt());
						ctFileDVo.setFileSize((long)emTmpFileTVo.getFileSize());
						ctFileDVo.setSavePath(newSavePath);
					}else{
						file = (CmMultipartFile) mpFileList.get(insertCnt++);
						if(file==null) continue;
						ctFileDVo.setDispNm(file.getOriginalFilename());
						ctFileDVo.setFileExt(file.getExt());
						ctFileDVo.setFileSize(file.getSize());
						ctFileDVo.setSavePath(file.getSavePath());
						filesize=file.getSize();
					}
				}
				ctFileDVo.setFileId(ctCmSvc.createFileId("CT_FILE_D"));
				ctFileDVo.setRefId(refId);
				ctFileDVo.setDispOrdr(dispOrdr);
				ctFileDVo.setRegDt("sysdate");
				ctFileDVo.setRegrUid(userVo.getUserUid());
				ctFileDVo.setUseYn("Y");
				ctFileDVo.setCtFncUid(fncUid);
				ctFileDVo.setCtId(ctId);
				
				//첨부파일 용량 체크
				if(true != ctFileAttLimitChk(ctId, filesize, request)){
					//ct.msg.attFileOver = 파일첨부용량제한이 초과 되었습니다. \n 마스터에게 문의 바랍니다. 
					throw new CmException("ct.msg.attFileOver", request);
				}
				queryQueue.insert(ctFileDVo);
			}
		}
		return deletedFileList;
	}
	
	/** 파일을 새이름으로 복사 후 파일 배포 (WAS DISK) */
	public String copyAndDist(HttpServletRequest request, String path, String savePath, String ext) throws IOException, CmException {
		DistHandler distHandler = distManager.createHandler(path, SessionUtil.getLocale(request));  // 업로드 경로 설정
		
		// baseDir
		String wasCopyBaseDir = distManager.getWasCopyBaseDir();
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

		return newSavePath;
	}
	
//	/** 파일을 새이름으로 복사 후 파일 배포 (DISK) */
//	private String copyAndDist(HttpServletRequest request, String path, String savePath, String ext) throws IOException, CmException {
//		DistHandler distHandler = distManager.createHandler(path, SessionUtil.getLocale(request));  // 업로드 경로 설정
//
//		// 새이름
//		String newSavePath = savePath.replace('\\', '/').substring(0, savePath.lastIndexOf('/')) + "/F" + StringUtil.getNextHexa() + "." + ext;
//
//		/**
//		 *  커뮤니티 '보내기' 기능 처리시 첨부파일 대상 경로의 동기화가 필요하여 순서가 바뀜 
//		 *  새로운 첨부 경로를 받아온 후 파일복사 해야함.
//		 */
//		
//		// baseDir
//		String wasCopyBaseDir = distManager.getWasCopyBaseDir();
//		if (wasCopyBaseDir == null) {
//			distManager.init();
//			wasCopyBaseDir = distManager.getWasCopyBaseDir();
//		}
//		
//		String distPath = distHandler.addWasList(newSavePath);
//		if (LOGGER.isDebugEnabled()) {
//			LOGGER.debug("distPath = " + distPath);
//		}
//		
//		// 파일복사
//		distHandler.copyFile(savePath, wasCopyBaseDir+distPath);
//		
//		distHandler.distribute();
//		
//		return distPath;
//	}
	
	/** 일정첨부파일 저장 (DB) */
	public List<CommonFileVo> saveScdlFile(HttpServletRequest request, String refId,
			QueryQueue queryQueue, String repetFlag) throws Exception {

		//repetFlag 반복 [파일설정] 개별반영(indiv)/전체반영(all)
		//savePath 반복파일 모두 삭제 하기위한 path명
		// Multipart 파일 리스트
 		List<MultipartFile> mpFileList = ((MultipartHttpServletRequest) request).getFiles(FILES_ID + "_file");

		// 파일 배포
		distribute(request, "/ct", mpFileList);

		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);

		List<CommonFileVo> deletedFileList = new ArrayList<CommonFileVo>();
		String[] fileIds = request.getParameterValues(FILES_ID + "_fileId");
		String[] valids = request.getParameterValues(FILES_ID + "_valid");  // 신규추가된 파일이면 Y
		String[] useYns = request.getParameterValues(FILES_ID + "_useYn");  // 삭제된 파일이면 N
		String[] tmpFileId = request.getParameterValues("tmpFileId");  // 임시파일ID
		// 임시파일VO
		EmTmpFileTVo emTmpFileTVo;
		CmMultipartFile file;
		String savePath ="";
		
		String ctId=request.getParameter("ctId");
		String fncUid=request.getParameter("menuId");
		
		int insertCnt = 0, dispOrdr = 0;
		for (int i = 0; i < valids.length; i++) {
			if ("N".equals(useYns[i])) {
				if ("Y".equals(valids[i])) continue;
				
				
					CtFileDVo ctFindFile = new CtFileDVo();
					ctFindFile.setFileId(Integer.valueOf(fileIds[i]));
					CtFileDVo ctFileVo = (CtFileDVo) commonDao.queryVo(ctFindFile);
					if(ctFileVo != null){
						String[] splitData = ctFileVo.getSavePath().split("\\/");
						savePath = splitData[(splitData.length)-1];
					}

					// 일정첨부파일(CT_FILE_D) 테이블 - DELETE
					CtFileDVo ctSchdlFileDVo = new CtFileDVo();
					if(repetFlag.equalsIgnoreCase("indiv")){
						ctSchdlFileDVo.setFileId(Integer.valueOf(fileIds[i]));
					}else if(repetFlag.equalsIgnoreCase("all")){
						if(savePath == null || "".equals(savePath)){
							// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
							throw new CmException("pt.msg.nodata.passed", request);
						}
						ctSchdlFileDVo.setSavePath(savePath);
					}
					queryQueue.delete(ctSchdlFileDVo);
				
				continue;
			}

			dispOrdr++;
			if ("N".equals(valids[i])) {
				if ("".equals(fileIds[i])) continue;

				// 일정첨부파일(CT_FILE_D) 테이블 - UPDATE
				CtFileDVo scdlFileDVo = new CtFileDVo();
				scdlFileDVo.setFileId(Integer.valueOf(fileIds[i]));
				scdlFileDVo.setDispOrdr(dispOrdr);
				queryQueue.update(scdlFileDVo);
			} else {
				// 일정첨부파일(CT_FILE_D) 테이블 - INSERT
				CtFileDVo ctFileDVo = new CtFileDVo();
				if(tmpFileId!=null && tmpFileId[i]!=null && !tmpFileId[i].isEmpty()){
					emTmpFileTVo = emFileUploadSvc.getEmTmpFileVo(Integer.parseInt(tmpFileId[i]));
					if(emTmpFileTVo==null) continue;
					
					// 파일 새이름으로 복사 후 파일 배포
					String newSavePath = emFileUploadSvc.copyAndDist(request, "/ct", emTmpFileTVo.getSavePath(), emTmpFileTVo.getFileExt());
					ctFileDVo.setDispNm(emTmpFileTVo.getDispNm());
					ctFileDVo.setFileExt(emTmpFileTVo.getFileExt());
					ctFileDVo.setFileSize((long)emTmpFileTVo.getFileSize());
					ctFileDVo.setSavePath(newSavePath);
				}else{
					file = (CmMultipartFile) mpFileList.get(insertCnt++);
					if(file==null) continue;
					ctFileDVo.setDispNm(file.getOriginalFilename());
					ctFileDVo.setFileExt(file.getExt());
					ctFileDVo.setFileSize(file.getSize());
					ctFileDVo.setSavePath(file.getSavePath());
					//첨부파일 용량 체크
					if(true != ctFileAttLimitChk(ctId, file.getSize(), request)){
						//ct.msg.attFileOver = 파일첨부용량제한이 초과 되었습니다. \n 마스터에게 문의 바랍니다. 
						throw new CmException("ct.msg.attFileOver", request);
					}
				}
				
				ctFileDVo.setFileId(ctCmSvc.createFileId("CT_FILE_D"));
				ctFileDVo.setRefId(refId);
				ctFileDVo.setDispOrdr(dispOrdr);
				ctFileDVo.setUseYn("Y");
				ctFileDVo.setRegDt("sysdate");
				ctFileDVo.setCtFncUid(fncUid);	
				ctFileDVo.setRegrUid(userVo.getUserUid());
				ctFileDVo.setUseYn("Y");
				ctFileDVo.setCtId(ctId);
//				ctFileDVo.setRegrUid(userVo.getUserUid());
//				ctFileDVo.setRegDt("sysdate");
				
				queryQueue.insert(ctFileDVo);
			}
		}
		return deletedFileList;
	}

	
	/** 커뮤니티첨부파일 리턴 (DB) */
	public CommonFileVo getFileVo(Integer fileId) throws SQLException {
		
		CtFileDVo ctFileDVo = new CtFileDVo();
		ctFileDVo.setFileId(fileId);
		ctFileDVo = (CtFileDVo) commonDao.queryVo(ctFileDVo);
		
		return ctFileDVo;
	}
	
	/** 커뮤니티첨부파일 목록 리턴 (DB) */
	@SuppressWarnings("unchecked")
	public List<CommonFileVo> getFileVoList(String refId) throws Exception {
		if (refId == null) return new ArrayList<CommonFileVo>();
		
		CtFileDVo ctFileDVo = new CtFileDVo();
		ctFileDVo.setRefId(refId);
		return (List<CommonFileVo>) commonDao.queryList(ctFileDVo);
	}
	
	/** 커뮤니티첨부파일 리스트 model에 추가 */
	public void putFileListToModel(String refId, ModelMap model, String compId) throws Exception {
		List<CommonFileVo> fileVoList = getFileVoList(refId);
		model.put("fileVoList", fileVoList);
		model.put("filesId", FILES_ID);
		// 확장자 허용제한 
		ptSysSvc.setAttachExtMap(model, compId, "ct");
		
		// 다운로드|문서뷰어 사용여부
		emAttachViewSvc.chkAttachSetup(model, compId);
	}
	
	/** 커뮤니티첨부파일 삭제 (DB) */
	public List<CommonFileVo> deleteCtFile(String refId, QueryQueue queryQueue) throws Exception {
		
		CtFileDVo ctFileDVo = new CtFileDVo();
		ctFileDVo.setRefId(refId);
		queryQueue.delete(ctFileDVo);
		
		return getFileVoList(refId);
	}

	/** 커뮤니티첨부파일 첨부용량제한 확인  */
	private boolean ctFileAttLimitChk(String ctId, long attFileSize , HttpServletRequest request) throws Exception{
		
		//커뮤니티 총 용량
		int ctFileSum = ctCmntSvc.getFileAttSum(ctId);
		
		//커뮤니티 기본정보
		CtEstbBVo ctEstbBVo = ctCmntSvc.getCtEstbInfo(request, ctId);
		if(ctEstbBVo != null){
			int ctFileTotalSize = ctEstbBVo.getAttLimSize();
	
			if(ctFileTotalSize == -1){
				return true;
			}else{
				if(ctCmntSvc.getFileMBConvertByte(ctFileTotalSize) < (ctFileSum + attFileSize)){
					return false;
				}else{
					return true;
				}
			}
		}else{
			return true;
		}
		
	}
	
	/** 사진 저장 */
	public CommonFileVo savePhoto(HttpServletRequest request,  String photo,int imgfileId, CtFileDVo newCtFileDvo , QueryQueue queryQueue) throws IOException, CmException, SQLException {
		// 세션의 사용자 정보
		//UserVo userVo = LoginSession.getUser(request);

		// Multipart 파일 리스트
		CmMultipartFile mpFile = (CmMultipartFile) ((MultipartHttpServletRequest) request).getFile(photo);
		if (mpFile == null) return null;

		// 업로드 경로
		String path = "/images/upload/ct/image";

		// 배포
		DistHandler distHandler = distManager.createHandler(path, SessionUtil.getLocale(request));  // 업로드 경로 설정
		String distPath = distHandler.addWebList(mpFile.getSavePath());// file-tag 의 name
		distHandler.distribute();

		
		if(imgfileId!=-1){
			CtFileDVo ctFileDVo = new CtFileDVo();
			ctFileDVo.setFileId(imgfileId);
			ctFileDVo =  (CtFileDVo) commonDao.queryVo(ctFileDVo);
			
			if (ctFileDVo != null) {				
				queryQueue.delete(ctFileDVo);			
			}
		}		
			
		try {
			newCtFileDvo.setSavePath(distPath);		
			newCtFileDvo.setDispOrdr(0);	
			newCtFileDvo.setFileId(ctCmSvc.createFileId("CT_FILE_D"));
			newCtFileDvo.setDispNm(mpFile.getOriginalFilename());
			newCtFileDvo.setFileExt(mpFile.getExt());
			newCtFileDvo.setFileSize(mpFile.getSize());
			newCtFileDvo.setRefId("-1");
			newCtFileDvo.setUseYn("Y");	
			newCtFileDvo.setRegDt("sysdate");
			//newCtFileDvo.setImgUrl("");
			queryQueue.insert(newCtFileDvo);
		} catch (Exception e) {		
			e.printStackTrace();
		}		
		

		return newCtFileDvo;
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
	
	/** 파일 저장 - 기존일정 파일 복사 */
	public void saveFile(HttpServletRequest request, String refId, QueryQueue queryQueue, 
			List<DmCommFileDVo> copyFileList) throws IOException, CmException, SQLException {
		
		// 원본 파일경로 prefix
		String path = "/ct";
		// 원본 파일경로 prefix
		String originPath = path;
		
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
					
					fileVo = getFileVo(Integer.parseInt(fileIds[i]));
					
					if(fileVo == null) continue;
					// 파일VO 생성
					CtFileDVo ctFileDVo = new CtFileDVo();
					// 복사 파일 경로
					String newSavePath = getFileNewPath(request, path, originPath, fileVo.getSavePath(), fileVo.getFileExt());
					
					BeanUtils.copyProperties(fileVo, ctFileDVo, new String[]{"refId", "savePath", "regrUid", "regDt"});
					ctFileDVo.setFileId(ctCmSvc.createFileId("CT_FILE_D"));
					ctFileDVo.setRefId(refId);
					ctFileDVo.setSavePath(newSavePath);
					ctFileDVo.setRegrUid(userVo.getUserUid());
					ctFileDVo.setRegDt("sysdate");
					
					// 순서 변경
					ctFileDVo.setDispOrdr(dispOrdr);
					
					// 실제파일 복사 준비
					dmCommFileDVo = new DmCommFileDVo();
					dmCommFileDVo.setOriginSavePath(fileVo.getSavePath());
					dmCommFileDVo.setMdPath(path);
					dmCommFileDVo.setNewSavePath(ctFileDVo.getSavePath());
					
					if(copyFileList!=null) copyFileList.add(dmCommFileDVo);
					
					queryQueue.insert(ctFileDVo);
					
				} else {// 신규파일
					// 일정첨부파일(WC_SCDHL_FILE_D) 테이블 - INSERT
					CtFileDVo ctFileDVo = new CtFileDVo();
					if(tmpFileId!=null && tmpFileId[i]!=null && !tmpFileId[i].isEmpty()){
						emTmpFileTVo = emFileUploadSvc.getEmTmpFileVo(Integer.parseInt(tmpFileId[i]));
						if(emTmpFileTVo==null) continue;
						
						// 파일 새이름으로 복사 후 파일 배포
						String newSavePath = emFileUploadSvc.copyAndDist(request, "/ct", emTmpFileTVo.getSavePath(), emTmpFileTVo.getFileExt());
						ctFileDVo.setDispNm(emTmpFileTVo.getDispNm());
						ctFileDVo.setFileExt(emTmpFileTVo.getFileExt());
						ctFileDVo.setFileSize((long)emTmpFileTVo.getFileSize());
						ctFileDVo.setSavePath(newSavePath);
					}else{
						file = (CmMultipartFile) mpFileList.get(insertCnt++);
						if(file==null) continue;
						ctFileDVo.setDispNm(file.getOriginalFilename());
						ctFileDVo.setFileExt(file.getExt());
						ctFileDVo.setFileSize(file.getSize());
						ctFileDVo.setSavePath(file.getSavePath());
					}
					ctFileDVo.setFileId(ctCmSvc.createFileId("CT_FILE_D"));
					ctFileDVo.setRefId(refId);
					ctFileDVo.setDispOrdr(dispOrdr);
					ctFileDVo.setUseYn("Y");
					ctFileDVo.setRegrUid(userVo.getUserUid());
					ctFileDVo.setRegDt("sysdate");
					queryQueue.insert(ctFileDVo);
				}
			}
		}
	}
	
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
	
	/** 실제 파일 복사*/
	public void copyFileList(HttpServletRequest request, List<DmCommFileDVo> copyFileList) throws IOException, CmException {
		copyFileDist(request, "/ct", copyFileList);
	}
	
}
