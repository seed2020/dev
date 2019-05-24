package com.innobiz.orange.web.bb.svc;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.innobiz.orange.web.bb.vo.BaBullFileDVo;
import com.innobiz.orange.web.bb.vo.BbBullLVo;
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
import com.innobiz.orange.web.em.svc.EmAttachViewSvc;
import com.innobiz.orange.web.em.svc.EmFileUploadSvc;
import com.innobiz.orange.web.em.svc.EmSendSvc;
import com.innobiz.orange.web.em.vo.EmSendFileDVo;
import com.innobiz.orange.web.em.vo.EmTmpFileTVo;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtSysSvc;

/** 게시파일 서비스 */
@Service
public class BbBullFileSvc {

	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(BbBullFileSvc.class);

	/** 파일업로드 태그ID */
	public static final String FILES_ID = "bbfiles";

	/** 게시판 공통 서비스 */
	@Autowired
	private BbCmSvc bbCmSvc;

	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;

	/** 업로드 메니저 */
	@Resource(name = "uploadManager")
	private UploadManager uploadManager;

	/** 배포 매니저 */
	@Resource(name = "distManager")
	private DistManager distManager;
	
	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
	/** 문서뷰어 서비스 */
	@Resource(name = "emAttachViewSvc")
	private EmAttachViewSvc emAttachViewSvc;
	
	/** 파일업로드 서비스 */
	@Resource(name = "emFileUploadSvc")
	private EmFileUploadSvc emFileUploadSvc;
	
	/** 보내기 서비스 */
	@Autowired
	private EmSendSvc emSendSvc;
	
	/** 파일 업로드 (DISK) */
	public UploadHandler upload(HttpServletRequest request) throws CmException, IOException {
		UploadHandler uploadHandler = uploadManager.createHandler(request, "temp", "bb");
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
			if(fileVo==null || fileVo.getSavePath()==null) continue;
			String savePath = fileVo.getSavePath();
			boolean deleted = new File(wasCopyBaseDir+savePath).delete();
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("savePath = " + savePath + ", deleted = " + deleted);
			}
		}
	}

	/** 파일 배포 (DISK) */
	public void distribute(HttpServletRequest request, String path, List<MultipartFile> mpFileList) throws IOException, CmException {
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

	/** 게시물첨부파일 저장 (DB) */
	public List<CommonFileVo> saveBullFile(HttpServletRequest request, String refId, QueryQueue queryQueue) throws IOException, CmException, SQLException {
		
		// Multipart 파일 리스트
		List<MultipartFile> mpFileList = ((MultipartHttpServletRequest) request).getFiles(FILES_ID + "_file");

		// 파일 배포
		distribute(request, "/bb", mpFileList);

		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);

		List<CommonFileVo> deletedFileList = new ArrayList<CommonFileVo>();
		String[] fileIds = request.getParameterValues(FILES_ID + "_fileId");
		String[] valids = request.getParameterValues(FILES_ID + "_valid");  // 신규추가된 파일이면 Y
		String[] useYns = request.getParameterValues(FILES_ID + "_useYn");  // 삭제된 파일이면 N
		String[] tmpFileId = request.getParameterValues("tmpFileId");  // 임시파일ID
		String sendNo = request.getParameter("sendNo"); // 보내기번호
		String emailNo = request.getParameter("emailNo"); // 이메일번호
		
		// 메일여부
		Boolean isMail=sendNo!=null && !sendNo.isEmpty() ? null : true;
		// 테이블번호
		String no=isMail==null ? sendNo : emailNo;
		
		// 임시저장폴더
		String tmpDir=uploadManager.getTempDir();
		// 임시파일VO
		EmTmpFileTVo emTmpFileTVo;
		// 보내기 파일VO
		EmSendFileDVo emSendFileDVo;
		CmMultipartFile file;
		int insertCnt = 0, dispOrdr = 0;
		if(valids!=null){
			for (int i = 0; i < valids.length; i++) {
				if ("N".equals(useYns[i])) {
					if ("Y".equals(valids[i])) continue;
	
					// 게시물첨부파일(BA_BULL_FILE_D) 테이블 - DELETE
					BaBullFileDVo baBullFileDVo = new BaBullFileDVo();
					baBullFileDVo.setFileId(Integer.valueOf(fileIds[i]));
					queryQueue.delete(baBullFileDVo);
	
					// 게시물첨부파일(BA_BULL_FILE_D) 테이블 - SELECT
					BaBullFileDVo bullFileVo = (BaBullFileDVo) commonDao.queryVo(baBullFileDVo);
					if (bullFileVo != null) deletedFileList.add(bullFileVo);
					continue;
				}
	
				dispOrdr++;
				if ("N".equals(valids[i])) {
					if ("".equals(fileIds[i])) continue;
					if((sendNo!=null && !sendNo.isEmpty()) || (emailNo!=null && !emailNo.isEmpty())){ 
						// 게시물첨부파일(BA_BULL_FILE_D) 테이블 - INSERT
						BaBullFileDVo baBullFileDVo = new BaBullFileDVo();
						emSendFileDVo = emSendSvc.getFileVo(no, Integer.parseInt(fileIds[i]), isMail);
						if(emSendFileDVo==null) continue;
						// 저장경로
						String savePath=sendNo!=null && !sendNo.isEmpty() ? emSendFileDVo.getSavePath() : emSendSvc.saveByteToFile(tmpDir, emSendFileDVo.getFileCont(), emSendFileDVo.getFileExt());
						// 파일 새이름으로 복사 후 파일 배포
						String newSavePath = sendNo!=null && !sendNo.isEmpty() ? emSendSvc.copyAndDist(request, "/bb", savePath, emSendFileDVo.getFileExt()) 
								: emFileUploadSvc.copyAndDist(request, "/bb", savePath, emSendFileDVo.getFileExt());;
						
						baBullFileDVo.setDispNm(emSendFileDVo.getDispNm());
						baBullFileDVo.setFileExt(emSendFileDVo.getFileExt());
						baBullFileDVo.setFileSize((long)emSendFileDVo.getFileSize());
						baBullFileDVo.setSavePath(newSavePath);
						baBullFileDVo.setFileId(bbCmSvc.createFileId());
						baBullFileDVo.setRefId(refId);					
						baBullFileDVo.setDispOrdr(dispOrdr);					
						baBullFileDVo.setUseYn("Y");
						baBullFileDVo.setRegrUid(userVo.getUserUid());
						baBullFileDVo.setRegDt("sysdate");
		
						queryQueue.insert(baBullFileDVo);
					}else{
						// 게시물첨부파일(BA_BULL_FILE_D) 테이블 - UPDATE
						BaBullFileDVo baBullFileDVo = new BaBullFileDVo();
						baBullFileDVo.setFileId(Integer.valueOf(fileIds[i]));
						baBullFileDVo.setDispOrdr(dispOrdr);
						queryQueue.update(baBullFileDVo);
					}
					
				} else {
					// 게시물첨부파일(BA_BULL_FILE_D) 테이블 - INSERT
					BaBullFileDVo baBullFileDVo = new BaBullFileDVo();
					if(tmpFileId!=null && tmpFileId[i]!=null && !tmpFileId[i].isEmpty()){
						emTmpFileTVo = emFileUploadSvc.getEmTmpFileVo(Integer.parseInt(tmpFileId[i]));
						if(emTmpFileTVo==null) continue;
						
						// 파일 새이름으로 복사 후 파일 배포
						String newSavePath = emFileUploadSvc.copyAndDist(request, "/bb", emTmpFileTVo.getSavePath(), emTmpFileTVo.getFileExt());
						baBullFileDVo.setDispNm(emTmpFileTVo.getDispNm());
						baBullFileDVo.setFileExt(emTmpFileTVo.getFileExt());
						baBullFileDVo.setFileSize((long)emTmpFileTVo.getFileSize());
						baBullFileDVo.setSavePath(newSavePath);
					}else{
						//if(mpFileList==null || mpFileList.size()==0 || mpFileList.size()<(insertCnt+1)) continue;
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
	
					queryQueue.insert(baBullFileDVo);
				}
			}
		}
		return deletedFileList;
	}

	/** 게시물첨부파일 리턴 (DB) */
	public CommonFileVo getFileVo(Integer fileId) throws SQLException {
		// 게시물첨부파일(BA_BULL_FILE_D) 테이블 - SELECT
		BaBullFileDVo baBullFileDVo = new BaBullFileDVo();
		baBullFileDVo.setFileId(fileId);
		baBullFileDVo = (BaBullFileDVo) commonDao.queryVo(baBullFileDVo);
		return baBullFileDVo;
	}

	/** 게시물첨부파일 목록 리턴 (DB) */
	@SuppressWarnings("unchecked")
	public List<CommonFileVo> getFileVoList(String refId) throws SQLException {
		if (refId == null) return new ArrayList<CommonFileVo>();
		
		// 게시물첨부파일(BA_BULL_FILE_D) 테이블 - SELECT
		BaBullFileDVo baBullFileDVo = new BaBullFileDVo();
		baBullFileDVo.setRefId(refId);
		return (List<CommonFileVo>) commonDao.queryList(baBullFileDVo);
	}

	/** 게시물첨부파일 리스트 model에 추가 */
	public void putFileListToModel(String bullId, ModelMap model, String compId) throws SQLException {
		List<CommonFileVo> fileVoList = getFileVoList(bullId);
		model.put("fileVoList", fileVoList);
		model.put("filesId", FILES_ID);
		
		// 확장자 허용제한 
		ptSysSvc.setAttachExtMap(model, compId, "bb");
		
		// 다운로드|문서뷰어 사용여부
		emAttachViewSvc.chkAttachSetup(model, compId);
	}

	/** 게시물첨부파일 삭제 (DB) */
	public List<CommonFileVo> deleteBullFile(String refId, QueryQueue queryQueue) throws SQLException {
		// 게시물첨부파일(BA_BULL_FILE_D) 테이블 - DELETE
		BaBullFileDVo baBullFileDVo = new BaBullFileDVo();
		baBullFileDVo.setRefId(refId);
		queryQueue.delete(baBullFileDVo);

		return getFileVoList(refId);
	}

	/** 게시파일 참조ID 수정 (DB) */
	public void updateRefId(String refId, String newRefId, QueryQueue queryQueue) {
		BaBullFileDVo baBullFileDVo = new BaBullFileDVo();
		baBullFileDVo.setRefId(refId);
		baBullFileDVo.setNewRefId(newRefId);
		queryQueue.update(baBullFileDVo);
	}

	/** 게시물첨부파일 복사 (DB, DISK) */
	public void copyBullFile(HttpServletRequest request, String refId, String newRefId, QueryQueue queryQueue) throws IOException, CmException, SQLException  {
		// baseDir
		String wasCopyBaseDir = distManager.getWasCopyBaseDir();
		if (wasCopyBaseDir == null) {
			distManager.init();
			wasCopyBaseDir = distManager.getWasCopyBaseDir();
		}
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);

		// 게시물첨부파일 목록
		List<CommonFileVo> fileVoList = getFileVoList(refId);

		File file = null;
		for (CommonFileVo fileVo : fileVoList) {
			file = new File(wasCopyBaseDir+fileVo.getSavePath());
			if(!file.isFile()) continue;

			// 게시물첨부파일(BA_BULL_FILE_D) 테이블 - INSERT
			BaBullFileDVo baBullFileDVo = new BaBullFileDVo();
			BeanUtils.copyProperties(fileVo, baBullFileDVo, new String[]{"refId", "savePath", "regrUid", "regDt"});
			baBullFileDVo.setFileId(bbCmSvc.createFileId());
			baBullFileDVo.setRefId(newRefId);
			
			// 파일 새이름으로 복사 후 파일 배포
			String newSavePath = copyAndDist(request, "/bb", fileVo.getSavePath(), fileVo.getFileExt());
			
			baBullFileDVo.setSavePath(newSavePath);
			//baBullFileDVo.setSavePath(fileVo.getSavePath());
			baBullFileDVo.setRegrUid(userVo.getUserUid());
			baBullFileDVo.setRegDt("sysdate");
			queryQueue.insert(baBullFileDVo);
		}
	}
	
	/** 게시물 목록을 파라미터로 받아 - 첨부파일 수를 조회하여 세팅함 */
	public void setFileCount(List<? extends BbBullLVo> bbBullLVoList) throws SQLException{
		if(bbBullLVoList==null || bbBullLVoList.isEmpty()) return;
		
		List<Integer> bullIdList = new ArrayList<Integer>();
		Map<Integer, BbBullLVo> bbBullLVoMap = new HashMap<Integer, BbBullLVo>();
		for(BbBullLVo bbBullLVo : bbBullLVoList){
			if(bbBullLVo.getBullId()!=null){
				bullIdList.add(bbBullLVo.getBullId());
				bbBullLVoMap.put(bbBullLVo.getBullId(), bbBullLVo);
			}
		}
		if(bullIdList.isEmpty()) return;
		
		BaBullFileDVo baBullFileDVo = new BaBullFileDVo();
		baBullFileDVo.setBullIdList(bullIdList);
		baBullFileDVo.setInstanceQueryId("com.innobiz.orange.web.bb.dao.BaBullFileDDao.countBaBullFileDByBullId");
		@SuppressWarnings("unchecked")
		List<BaBullFileDVo> baBullFileDVoList = (List<BaBullFileDVo>)commonDao.queryList(baBullFileDVo);
		if(baBullFileDVoList != null){
			BbBullLVo bbBullLVo;
			for(BaBullFileDVo fileVo : baBullFileDVoList){
				bbBullLVo = bbBullLVoMap.get(Integer.valueOf(fileVo.getRefId()));
				if(bbBullLVo != null) bbBullLVo.setFileCnt(fileVo.getFileCnt());
			}
		}
	}
	
	/** 파일을 새이름으로 복사 후 파일 배포 (WAS DISK) */
	private String copyAndDist(HttpServletRequest request, String path, String savePath, String ext) throws IOException, CmException {
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
	
	/** 파일ID 리턴 */
	public String getFilesId(){
		return FILES_ID;
	}

}
