package com.innobiz.orange.mobile.ct.ctrl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.innobiz.orange.mobile.cm.utils.MoLayoutUtil;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.files.DistManager;
import com.innobiz.orange.web.cm.files.ZipUtil;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.SessionUtil;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.CommonFileVo;
import com.innobiz.orange.web.ct.svc.CtAdmNotcSvc;
import com.innobiz.orange.web.ct.svc.CtFileSvc;
import com.innobiz.orange.web.ct.vo.CtAdmNotcBVo;
import com.innobiz.orange.web.em.svc.EmAttachViewSvc;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.utils.PersonalUtil;

/** 관리자 커뮤니티 공지사항 */
@Controller
public class MoCtAdmNotcCtrl {
	
	/** 관리자 커뮤니티 공지사항 서비스 */
	@Autowired
	private CtAdmNotcSvc ctAdmNotcSvc;
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 배포 매니저 */
	@Resource(name = "distManager")
	private DistManager distManager;
	
	/** 압축 파일관련 서비스 */
	@Resource(name = "zipUtil")
	private ZipUtil zipUtil;
	
	/** 메세지 */
	@Autowired
	private MessageProperties messageProperties;
	
	/** 게시물 첨부파일  */
	@Autowired
	private CtFileSvc ctFileSvc;
	
	/** 문서뷰어 서비스 */
	@Resource(name = "emAttachViewSvc")
	private EmAttachViewSvc emAttachViewSvc;

	/** 커뮤니티 공지사항 목록(사용자) */
	@RequestMapping(value = "/ct/notc/listNotc")
	public String listNotc(HttpServletRequest request,
			ModelMap model) throws Exception {
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		CtAdmNotcBVo ctAdmNotcBVo = new CtAdmNotcBVo();
		ctAdmNotcBVo.setCompId(userVo.getCompId());
		//만료된 공지사항 제외
		ctAdmNotcBVo.setSchExpr("N");
		VoUtil.bind(request, ctAdmNotcBVo);
		
		// 게시물(BB_X000X_L) 테이블 - COUNT
		Integer recodeCount = commonSvc.count(ctAdmNotcBVo);
		PersonalUtil.setPaging(request, ctAdmNotcBVo, recodeCount);
		
		// 게시물 목록
		List<CtAdmNotcBVo> ctAdmNotcMapList = ctAdmNotcSvc.getCtAdmNotcVoList(ctAdmNotcBVo);
		
		model.put("recodeCount", recodeCount);
		model.put("ctAdmNotcMapList", ctAdmNotcMapList);
		
		return MoLayoutUtil.getJspPath("/ct/notcpr/listNotc");
				
	}
	
	/** 게시물 조회*/
	@RequestMapping(value = "/ct/notc/viewNotc")
	public String viewNotc(HttpServletRequest request,
			@RequestParam(value = "bullId", required = true) String bullId,
			ModelMap model) throws Exception {
		
		if (bullId.isEmpty()) {
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 게시물 테이블
		CtAdmNotcBVo ctAdmNotcBVo = ctAdmNotcSvc.getCtAdmNotcBVo(Integer.parseInt(bullId), langTypCd);
		if (ctAdmNotcBVo == null) {
			// ct.msg.bullNotExists=게시물이 존재하지 않습니다.
			throw new CmException("ct.msg.bullNotExists", request);
		}
		model.put("ctAdmNotcBVo", ctAdmNotcBVo);
		
		
		//조회이력 저장
		if(ctAdmNotcSvc.saveReadHst(bullId, userVo.getUserUid(), userVo.getCompId())){
			ctAdmNotcSvc.addReadCnt(Integer.parseInt(bullId));
		}
		
		// 게시물첨부파일 리스트 model에 추가
		ctAdmNotcSvc.putFileListToModel(bullId, model, userVo.getCompId());
		
		return MoLayoutUtil.getJspPath("/ct/notcpr/viewNotc");
		
	}
	
	/** 첨부파일 다운로드 */
	@RequestMapping(value = {"/ct/downFile","/ct/preview/downFile"})
	public ModelAndView downFile(HttpServletRequest request,
			@RequestParam(value = "fileIds", required = true) String fileIds) throws Exception {
		try {
			if (fileIds.isEmpty()) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
						
			// 다운로드 체크
			emAttachViewSvc.chkAttachDown(request, userVo.getCompId());
			
			// baseDir
			String wasCopyBaseDir = distManager.getWasCopyBaseDir();
			if (wasCopyBaseDir == null) {
				distManager.init();
				wasCopyBaseDir = distManager.getWasCopyBaseDir();
			}
			
			// fileId
			String[] fileIdArray = fileIds.split(",");
			List<CommonFileVo> fileVoList = new ArrayList<CommonFileVo>();
			for (String fileId : fileIdArray) {
				// 게시물첨부파일
				CommonFileVo fileVo = ctFileSvc.getFileVo(Integer.valueOf(fileId));
				if (fileVo != null) {
					fileVo.setSavePath(wasCopyBaseDir+fileVo.getSavePath());
					//fileVo.setSavePath(fileVo.getSavePath());
					File file = new File(fileVo.getSavePath());
					if (file.isFile()) {
						fileVoList.add(fileVo);
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
		}catch (CmException e) {
			ModelAndView mv = new ModelAndView("cm/result/commonResult");
			mv.addObject("message", e.getMessage());
			return mv;
		} catch (Exception e) {
			ModelAndView mv = new ModelAndView("cm/result/commonResult");
			mv.addObject("message", e.getMessage());
		}
		
		return null;
	}
}
