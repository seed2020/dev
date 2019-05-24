package com.innobiz.orange.web.wo.svc;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
import com.innobiz.orange.web.or.vo.OrUserBVo;
import com.innobiz.orange.web.or.vo.OrUserRoleRVo;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtSysSvc;
import com.innobiz.orange.web.wo.vo.WoOnecAttFileDVo;
import com.innobiz.orange.web.wo.vo.WoOnecCdDVo;

@Service
public class WoCmSvc {
	
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

	public void setCdList(ModelMap model) throws SQLException{
		
		WoOnecCdDVo woOnecCdDVo = new WoOnecCdDVo();
		woOnecCdDVo.setOrderBy("CLS_CD, SORT_ORDR");
		@SuppressWarnings("unchecked")
		List<WoOnecCdDVo> woOnecCdDVoList = (List<WoOnecCdDVo>)commonSvc.queryList(woOnecCdDVo);
		
		if(woOnecCdDVoList !=null && !woOnecCdDVoList.isEmpty()){
			
			Map<String, String> cdMap = null;
			List<WoOnecCdDVo> cdList = null;
			String clsCd, oldClsCd = null;
			for(WoOnecCdDVo vo : woOnecCdDVoList){
				clsCd = vo.getClsCd();
				if(oldClsCd==null || !oldClsCd.equals(clsCd)){
					cdMap = new HashMap<String, String>();
					cdList = new ArrayList<WoOnecCdDVo>();
					
					model.put(clsCd+"Map", cdMap);
					model.put(clsCd+"List", cdList);
					oldClsCd = clsCd;
				}
				cdMap.put(vo.getCd(), vo.getCdVa());
				cdList.add(vo);
			}
		}
		
	}

	
	public void putFileListToModel(String onecNo, ModelMap model, String compId) throws SQLException {
		List<CommonFileVo> fileVoList = getFileVoList(onecNo);
		model.put("fileVoList", fileVoList);
		
		// 확장자 허용제한 
		ptSysSvc.setAttachExtMap(model, compId, "wp");
		
		// 다운로드|문서뷰어 사용여부
		emAttachViewSvc.chkAttachSetup(model, compId);
	}
	
	/** 첨부파일 목록 리턴 */
	private List<CommonFileVo> getFileVoList(String onecNo) throws SQLException {
		if (onecNo == null || onecNo.isEmpty()) return new ArrayList<CommonFileVo>();
		
		WoOnecAttFileDVo woOnecAttFileDVo = new WoOnecAttFileDVo();
		woOnecAttFileDVo.setOnecNo(onecNo);
		woOnecAttFileDVo.setOrderBy("ATT_SEQ");
		@SuppressWarnings("unchecked")
		List<WoOnecAttFileDVo> voList = (List<WoOnecAttFileDVo>)commonSvc.queryList(woOnecAttFileDVo);
		if(voList!=null && !voList.isEmpty()){
			CommonFileVo fvo;
			List<CommonFileVo> fileVoList = new ArrayList<CommonFileVo>();
			int dispOrdr = 1;
			for(WoOnecAttFileDVo vo : voList){
				fvo = new DmFileDVo();
				fvo.setFileId(Integer.valueOf(vo.getAttSeq()));
				fvo.setRefId(vo.getOnecNo());
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
	
	public List<WoOnecAttFileDVo> saveAttachFiles(HttpServletRequest request, QueryQueue queryQueue, String onecNo) throws IOException, CmException, NumberFormatException, SQLException{
		String fileId = "wofiles";
		// Multipart 파일 리스트
		List<MultipartFile> woFileList = ((MultipartHttpServletRequest) request).getFiles(fileId + "_file");
		String path = "/wo/onec";
		// 파일 배포
		distribute(request, path, woFileList);

		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);

		List<WoOnecAttFileDVo> deletedFileList = new ArrayList<WoOnecAttFileDVo>();
		String[] fileIds = request.getParameterValues(fileId + "_fileId");
		String[] valids = request.getParameterValues(fileId + "_valid");
		String[] useYns = request.getParameterValues(fileId + "_useYn");
		String[] tmpFileId = request.getParameterValues("tmpFileId");  // 임시파일ID
		// 임시파일VO
		EmTmpFileTVo emTmpFileTVo;
		CmMultipartFile file;
		
		WoOnecAttFileDVo woOnecAttFileDVo;
		int insertCnt = 0;//, dispOrdr = 0;
		if(valids != null){
			for (int i = 0; i < valids.length; i++) {
				if ("N".equals(useYns[i])) {
					if ("Y".equals(valids[i])) continue;
					
					woOnecAttFileDVo = new WoOnecAttFileDVo();
					woOnecAttFileDVo.setOnecNo(onecNo);
					woOnecAttFileDVo.setAttSeq(fileIds[i]);
					queryQueue.delete(woOnecAttFileDVo);
					
					woOnecAttFileDVo = (WoOnecAttFileDVo) commonSvc.queryVo(woOnecAttFileDVo);
					if (woOnecAttFileDVo != null) deletedFileList.add(woOnecAttFileDVo);
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
						
						woOnecAttFileDVo = new WoOnecAttFileDVo();
						woOnecAttFileDVo.setOnecNo(onecNo);
						woOnecAttFileDVo.setDispNm(emTmpFileTVo.getDispNm());
						woOnecAttFileDVo.setFileExt(emTmpFileTVo.getFileExt());
						if(emTmpFileTVo.getFileSize()!=null){
							woOnecAttFileDVo.setFileSize(emTmpFileTVo.getFileSize().toString());
						}
						woOnecAttFileDVo.setSavePath(newSavePath);
						
					} else {
						
						file = (CmMultipartFile) woFileList.get(insertCnt++);
						if(file==null) continue;
						
						woOnecAttFileDVo = new WoOnecAttFileDVo();
						woOnecAttFileDVo.setOnecNo(onecNo);
						woOnecAttFileDVo.setDispNm(file.getOriginalFilename());
						woOnecAttFileDVo.setFileExt(file.getExt());
						woOnecAttFileDVo.setFileSize(Long.toString(file.getSize()));
						woOnecAttFileDVo.setSavePath(file.getSavePath());
					}
					
					woOnecAttFileDVo.setRegrUid(userVo.getUserUid());
					woOnecAttFileDVo.setRegDt("sysdate");
					
					queryQueue.insert(woOnecAttFileDVo);
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
	
	/** 역할코드에 해당하는 사용자 목록 */
	public List<OrUserBVo> getUserListByRoleCd(String compId, String roleCd, String langTypCd) throws SQLException{
		
		OrUserRoleRVo orUserRoleRVo = new OrUserRoleRVo();
		orUserRoleRVo.setRoleCd(roleCd);
		@SuppressWarnings("unchecked")
		List<OrUserRoleRVo> userRoleList = (List<OrUserRoleRVo>)commonSvc.queryList(orUserRoleRVo);
		if(userRoleList!=null && !userRoleList.isEmpty()){
			List<String> userUidList = new ArrayList<String>();
			for(OrUserRoleRVo vo : userRoleList){
				userUidList.add(vo.getUserUid());
			}
			
			OrUserBVo orUserBVo = new OrUserBVo();
			orUserBVo.setCompId(compId);
			orUserBVo.setQueryLang(langTypCd);
			orUserBVo.setUserUidList(userUidList);
			@SuppressWarnings("unchecked")
			List<OrUserBVo> userList = (List<OrUserBVo>)commonSvc.queryList(orUserBVo);
			if(userList!=null && !userList.isEmpty()){
				return userList;
			}
		}
		return null;
	}
}
