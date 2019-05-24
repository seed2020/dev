package com.innobiz.orange.web.wp.svc;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.files.CmMultipartFile;
import com.innobiz.orange.web.cm.files.DistHandler;
import com.innobiz.orange.web.cm.files.DistManager;
import com.innobiz.orange.web.cm.files.ZipUtil;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.SessionUtil;
import com.innobiz.orange.web.cm.vo.CommonFileVo;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.dm.vo.DmFileDVo;
import com.innobiz.orange.web.em.svc.EmAttachViewSvc;
import com.innobiz.orange.web.em.svc.EmFileUploadSvc;
import com.innobiz.orange.web.em.vo.EmTmpFileTVo;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtSysSvc;
import com.innobiz.orange.web.wp.vo.WpPrjAttFileDVo;

/** 프로잭트 관리 공통 서비스 */
@Service
public class WpCmSvc {
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;

	/** 배포 매니저 */
	@Resource(name = "distManager")
	private DistManager distManager;
	
	/** 문서뷰어 서비스 */
	@Resource(name = "emAttachViewSvc")
	private EmAttachViewSvc emAttachViewSvc;
	
	@Resource(name = "emFileUploadSvc")
	private EmFileUploadSvc emFileUploadSvc;
	
	/** 메세지 */
	@Autowired
	private MessageProperties messageProperties;
	
	/** 압축 파일관련 서비스 */
	@Resource(name = "zipUtil")
	private ZipUtil zipUtil;
	
	/** ID 통합 생성 */
	public String createId(String tableName) throws SQLException{
		if("WP_MP_B".equals(tableName)){
			return commonSvc.createId(tableName, 'M', 8);
		} else if("WP_PRJ_GRP_B".equals(tableName)){
			return commonSvc.createId(tableName, 'G', 8);
		}
		return null;
	}
	
	/** 첨부파일 리스트 model에 추가 */
	public void putFileListToModel(String prjNo, ModelMap model, String compId) throws SQLException {
		
		List<CommonFileVo> fileVoList = getFileVoList(prjNo);
		model.put("fileVoList", fileVoList);
		
		// 확장자 허용제한 
		ptSysSvc.setAttachExtMap(model, compId, "wp");
		
		// 다운로드|문서뷰어 사용여부
		emAttachViewSvc.chkAttachSetup(model, compId);
	}
	
	/** 첨부파일 목록 리턴 */
	private List<CommonFileVo> getFileVoList(String prjNo) throws SQLException {
		if (prjNo == null || prjNo.isEmpty()) return new ArrayList<CommonFileVo>();
		
		WpPrjAttFileDVo wpPrjAttFileDVo = new WpPrjAttFileDVo();
		wpPrjAttFileDVo.setPrjNo(prjNo);
		wpPrjAttFileDVo.setOrderBy("ATT_SEQ");
		@SuppressWarnings("unchecked")
		List<WpPrjAttFileDVo> voList = (List<WpPrjAttFileDVo>)commonSvc.queryList(wpPrjAttFileDVo);
		if(voList!=null && !voList.isEmpty()){
			CommonFileVo fvo;
			List<CommonFileVo> fileVoList = new ArrayList<CommonFileVo>();
			int dispOrdr = 1;
			for(WpPrjAttFileDVo vo : voList){
				fvo = new DmFileDVo();
				fvo.setFileId(Integer.valueOf(vo.getAttSeq()));
				fvo.setRefId(vo.getPrjNo());
				fvo.setDispNm(vo.getDispNm());
				fvo.setDispOrdr(dispOrdr++);
				fvo.setFileExt(vo.getFileExt());
				fvo.setFileSize(Long.valueOf(vo.getFileSize()));
				fvo.setSavePath(vo.getSavePath());
				fvo.setUseYn("Y");
				fileVoList.add(fvo);
			}
			return fileVoList;
		}
		
		return null;
	}
	
	public List<WpPrjAttFileDVo> saveAttachFiles(HttpServletRequest request, QueryQueue queryQueue, String prjNo) throws IOException, CmException, NumberFormatException, SQLException{
		String fileId = "wpfiles";
		// Multipart 파일 리스트
		List<MultipartFile> wpFileList = ((MultipartHttpServletRequest) request).getFiles(fileId + "_file");
		String path = "/wp/prj";
		// 파일 배포
		distribute(request, path, wpFileList);

		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);

		List<WpPrjAttFileDVo> deletedFileList = new ArrayList<WpPrjAttFileDVo>();
		String[] fileIds = request.getParameterValues(fileId + "_fileId");
		String[] valids = request.getParameterValues(fileId + "_valid");
		String[] useYns = request.getParameterValues(fileId + "_useYn");
		String[] tmpFileId = request.getParameterValues("tmpFileId");  // 임시파일ID
		// 임시파일VO
		EmTmpFileTVo emTmpFileTVo;
		CmMultipartFile file;
		
		WpPrjAttFileDVo wpPrjAttFileDVo;
		int insertCnt = 0;//, dispOrdr = 0;
		if(valids != null){
			for (int i = 0; i < valids.length; i++) {
				if ("N".equals(useYns[i])) {
					if ("Y".equals(valids[i])) continue;
					
					wpPrjAttFileDVo = new WpPrjAttFileDVo();
					wpPrjAttFileDVo.setPrjNo(prjNo);
					wpPrjAttFileDVo.setAttSeq(fileIds[i]);
					queryQueue.delete(wpPrjAttFileDVo);
					
					wpPrjAttFileDVo = (WpPrjAttFileDVo) commonSvc.queryVo(wpPrjAttFileDVo);
					if (wpPrjAttFileDVo != null) deletedFileList.add(wpPrjAttFileDVo);
					continue;
				}
				
				if ("N".equals(valids[i])) {
					continue;
				} else {
					
					if(tmpFileId!=null && tmpFileId[i]!=null && !tmpFileId[i].isEmpty()) {
						emTmpFileTVo = emFileUploadSvc.getEmTmpFileVo(Integer.parseInt(tmpFileId[i]));
						if(emTmpFileTVo==null) continue;
						
						// 파일 새이름으로 복사 후 파일 배포
						String newSavePath = emFileUploadSvc.copyAndDist(request, path, emTmpFileTVo.getSavePath(), emTmpFileTVo.getFileExt());
						
						wpPrjAttFileDVo = new WpPrjAttFileDVo();
						wpPrjAttFileDVo.setPrjNo(prjNo);
						wpPrjAttFileDVo.setDispNm(emTmpFileTVo.getDispNm());
						wpPrjAttFileDVo.setFileExt(emTmpFileTVo.getFileExt());
						if(emTmpFileTVo.getFileSize()!=null){
							wpPrjAttFileDVo.setFileSize(emTmpFileTVo.getFileSize().toString());
						}
						wpPrjAttFileDVo.setSavePath(newSavePath);
						
					} else {
						
						file = (CmMultipartFile) wpFileList.get(insertCnt++);
						if(file==null) continue;
						
						wpPrjAttFileDVo = new WpPrjAttFileDVo();
						wpPrjAttFileDVo.setPrjNo(prjNo);
						wpPrjAttFileDVo.setDispNm(file.getOriginalFilename());
						wpPrjAttFileDVo.setFileExt(file.getExt());
						wpPrjAttFileDVo.setFileSize(Long.toString(file.getSize()));
						wpPrjAttFileDVo.setSavePath(file.getSavePath());
					}
					
					wpPrjAttFileDVo.setRegrUid(userVo.getUserUid());
					wpPrjAttFileDVo.setRegDt("sysdate");
					
					queryQueue.insert(wpPrjAttFileDVo);
				}
			}
		}
		return deletedFileList;
	}
	
	/** 파일 배포 */
	private void distribute(HttpServletRequest request, String path, List<MultipartFile> mpFileList) throws IOException, CmException {
		if(mpFileList != null && !mpFileList.isEmpty()){
			DistHandler distHandler = distManager.createHandler(path, SessionUtil.getLocale(request));  // 업로드 경로 설정
			for (MultipartFile mpFile : mpFileList) {
				CmMultipartFile file = (CmMultipartFile) mpFile;
				String distPath = distHandler.addWasList(file.getSavePath());
				file.setSavePath(distPath);
			}
			distHandler.distribute();
		}
	}

	public ModelAndView getFileList(HttpServletRequest request, String fileIds,
			String actionParam) throws IOException, SQLException {
		// baseDir
		String wasCopyBaseDir = distManager.getWasCopyBaseDir();
		if (wasCopyBaseDir == null) {
			distManager.init();
			wasCopyBaseDir = distManager.getWasCopyBaseDir();
		}
		
		File ckFile;
		List<CommonFileVo> fileVoList = new ArrayList<CommonFileVo>();
		List<CommonFileVo> voList = getFileVoList(actionParam);
		if(voList!=null && !voList.isEmpty()){
			for (String fileId : fileIds.split(",")) {
				for(CommonFileVo vo : voList){
					if(Integer.parseInt(fileId) == vo.getFileId().intValue()){
						vo.setSavePath(wasCopyBaseDir + vo.getSavePath());
						ckFile = new File(vo.getSavePath());
						if (ckFile.isFile()) {
							fileVoList.add(vo);
						}
					}
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
	
}
