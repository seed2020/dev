package com.innobiz.orange.web.wb.ctrl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.tiles.autotag.core.runtime.annotation.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.files.UploadHandler;
import com.innobiz.orange.web.cm.files.ZipUtil;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.ParamUtil;
import com.innobiz.orange.web.cm.utils.SessionUtil;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.CommonFileVo;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.em.svc.EmAttachViewSvc;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtSysSvc;
import com.innobiz.orange.web.wb.svc.WbBcMetngFileSvc;
import com.innobiz.orange.web.wb.svc.WbCmSvc;
import com.innobiz.orange.web.wb.svc.WbMetngSvc;
import com.innobiz.orange.web.wb.vo.WbBcAgntAdmBVo;
import com.innobiz.orange.web.wb.vo.WbBcMetngAtndRVo;
import com.innobiz.orange.web.wb.vo.WbBcMetngDVo;
import com.innobiz.orange.web.wb.vo.WbMetngClsCdBVo;

/** 관련미팅 */
@Controller
public class WbBcMetngCtrl {

	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(WbBcMetngCtrl.class);
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 공통 서비스 */
	@Autowired
	private WbCmSvc wbCmSvc;
	
	/** 관련미팅 관리 */
	@Autowired
	private WbMetngSvc wbMetngSvc;
	
	/** 첨부파일 서비스 */
	@Autowired
	private WbBcMetngFileSvc wbBcMetngFileSvc;
	
	/** 메세지 */
	@Autowired
    private MessageProperties messageProperties;
	
	/** 압축 파일관련 서비스 */
	@Resource(name = "zipUtil")
	private ZipUtil zipUtil;
	
	/** 문서뷰어 서비스 */
	@Resource(name = "emAttachViewSvc")
	private EmAttachViewSvc emAttachViewSvc;
	
	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
	/** 관련 미팅 목록 조회 */
	@RequestMapping(value = {"/wb/listMetng","/wb/listAgntMetng","/wb/adm/listAllMetng"})
	public String listMetng(HttpServletRequest request,
			@RequestParam(value = "schOpenYn", required = false) String schOpenYn,
			@RequestParam(value = "schBcRegrUid", required=false) String schBcRegrUid,
			ModelMap model) throws Exception {
		
		// 요청경로 세팅
		String path = wbCmSvc.getRequestPath(request, model , "Metng");
		//관리자 (사용자 권한 체크)
		if("listAllMetng".equals(path) ) wbCmSvc.checkUserAuth(request, "A", null);
				
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		// 조회조건 매핑
		WbBcMetngDVo wbBcMetngDVo = new WbBcMetngDVo();
		VoUtil.bind(request, wbBcMetngDVo);
		wbBcMetngDVo.setQueryLang(langTypCd);
		
		boolean flag = false;
		if("listMetng".equals(path)){
			wbBcMetngDVo.setCompId(userVo.getCompId());//회사ID
			if(schOpenYn == null || "".equals(schOpenYn) || "N".equals(schOpenYn)){
				wbBcMetngDVo.setRegrUid(userVo.getUserUid());
			}
			flag = true;
		}else if("listAgntMetng".equals(path)){
			// 대리명함 조회UID가 있을경우 명함 등록자UID로 세팅한다.
			schBcRegrUid = wbCmSvc.getSchBcRegrUid(schBcRegrUid, userVo, langTypCd, model);
			if(schBcRegrUid != null && !schBcRegrUid.isEmpty() ) {
				/** 사용자의 명함 대리 관리자 정보 */
				Map<String,Object> agntInfoMap = wbCmSvc.getAgntInfoMap(request ,schBcRegrUid , userVo.getUserUid() , null );
				wbBcMetngDVo.setRegrUid(schBcRegrUid);
				//대리관리자일 경우 대리권한 부여자의 회사ID를 조회한다.
				wbBcMetngDVo.setCompId((String)agntInfoMap.get("compId"));
				model.put("schBcRegrUid", schBcRegrUid);
				flag = true;
			}
			// 대리명함 조회UID가 없을경우 대리관리자 목록이 없는 것이므로 명함 조회를 하지 않는다.
		}else if("listAllMetng".equals(path)){
			wbBcMetngDVo.setCompId(userVo.getCompId());//회사ID
			flag = true;
		}
		if(flag){
			Map<String,Object> rsltMap = wbMetngSvc.getWbBcMetngMapList(request , wbBcMetngDVo );
			model.put("recodeCount", rsltMap.get("recodeCount"));
			model.put("wbBcMetngDVoList", rsltMap.get("wbBcMetngDVoList"));
		}
		
		/** 분류코드 조회 */
		WbMetngClsCdBVo wbMetngClsCdBVo = new WbMetngClsCdBVo();
		wbMetngClsCdBVo.setQueryLang(langTypCd);
		wbMetngClsCdBVo.setCompId(userVo.getCompId());
		@SuppressWarnings("unchecked")
		List<WbMetngClsCdBVo> wbMetngClsCdBVoList = (List<WbMetngClsCdBVo>)commonSvc.queryList(wbMetngClsCdBVo);
		model.put("wbMetngClsCdBVoList", wbMetngClsCdBVoList);
		
		model.put("paramsForList", ParamUtil.getQueryString(request, "schOpenYn","schBcRegrUid"));
		model.put("params", ParamUtil.getQueryString(request));
		
		// get 파라미터를 post 파라미터로 전달하기 위해
		model.put("paramEntryList", ParamUtil.getEntryMapList(request));
				
		// print css 적용
		if(request.getAttribute("printView")==null){
			request.setAttribute("printView", "print100");
		}
				
		return LayoutUtil.getJspPath("/wb/listMetng");
	}
	
	/** 상세보기 */
	@RequestMapping(value = {"/wb/viewMetng","/wb/viewAgntMetng","/wb/adm/viewAllMetng"})
	public String viewMetng(HttpServletRequest request,
			@RequestParam(value = "schOpenYn", required = false) String schOpenYn,
			@RequestParam(value = "bcMetngDetlId", required = true) String bcMetngDetlId,
			ModelMap model) throws Exception {
		
		// 요청경로 세팅
		String path = wbCmSvc.getRequestPath(request, model , "Metng");
		//관리자 (사용자 권한 체크)
		if("viewAllMetng".equals(path) ) wbCmSvc.checkUserAuth(request, "A", null);
				
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		WbBcMetngDVo wbBcMetngDVo = new WbBcMetngDVo();
		wbBcMetngDVo.setQueryLang(langTypCd);
		
		if("viewMetng".equals(path)){
			if(schOpenYn == null || "".equals(schOpenYn)){//공개여부
				wbBcMetngDVo.setRegrUid(userVo.getUserUid());
			}else{
				wbBcMetngDVo.setSchOpenYn(schOpenYn);
			}
			wbBcMetngDVo.setCompId(userVo.getCompId());//회사ID
		}else if("viewAgntMetng".equals(path)){
			//대리명함
			String schBcRegrUid = ParamUtil.getRequestParam(request, "schBcRegrUid", true);
			/** 사용자의 명함 대리 관리자 정보 */
			Map<String,Object> agntInfoMap = wbCmSvc.getAgntInfoMap(request ,schBcRegrUid , userVo.getUserUid() , null );
			// 대리명함 여부를 판단해 등록자UID를 세팅한다.
			wbBcMetngDVo.setRegrUid(schBcRegrUid);
			//대리관리자일 경우 대리권한 부여자의 회사ID를 조회한다.
			wbBcMetngDVo.setCompId((String)agntInfoMap.get("compId"));
			
			WbBcAgntAdmBVo wbBcAgntAdmBVo = (WbBcAgntAdmBVo)agntInfoMap.get("wbBcAgntAdmBVo");
			model.put("wbBcAgntAdmBVo", wbBcAgntAdmBVo);
			model.put("schBcRegrUid", wbBcAgntAdmBVo.getRegrUid());
		}
		
		wbBcMetngDVo.setBcMetngDetlId(bcMetngDetlId);
		// 미팅정보 조회
		wbBcMetngDVo = wbMetngSvc.getWbMetngInfoVo(wbBcMetngDVo, langTypCd);
		
		if(wbBcMetngDVo == null){
			//cm.msg.notValidCall=파라미터가 잘못되었거나 보안상의 이유로 해당 기능을 수행할 수 없습니다.
			String msg = messageProperties.getMessage("cm.msg.notValidCall", request);
			throw new CmException(msg);
		}
		
		model.put("wbBcMetngDVo", wbBcMetngDVo);
		model.put("wbBcMetngAtndRVoList", wbBcMetngDVo.getWbBcMetngAtndRVo());
		
		// 첨부파일 리스트 model에 추가
		wbBcMetngFileSvc.putFileListToModel(wbBcMetngDVo.getBcMetngDetlId() != null ? wbBcMetngDVo.getBcMetngDetlId() : null, model, userVo.getCompId());
		
		if(!"viewAllMetng".equals(path) ){
			// 리소스기본(WB_BC_B) 테이블 - UPDATE or INSERT
			QueryQueue queryQueue = new QueryQueue();
			wbBcMetngDVo.setReadCnt("");
			queryQueue.update(wbBcMetngDVo);
			commonSvc.execute(queryQueue);
		}
		
		model.put("paramsForList", ParamUtil.getQueryString(request, "bcMetngDetlId","schBcRegrUid"));
		model.put("params", ParamUtil.getQueryString(request));
		
		// print css 적용
		if(request.getAttribute("printView")==null){
			request.setAttribute("printView", "print100");
		}
				
		return LayoutUtil.getJspPath("/wb/viewMetng");
	}
	
	/** [POPUP] 상세보기 */
	@RequestMapping(value = {"/wb/viewMetngPop", "/wb/adm/viewMetngPop"})
	public String viewMetngPop(HttpServletRequest request,
			@RequestParam(value = "schOpenYn", required = false) String schOpenYn,
			@RequestParam(value = "bcMetngDetlId", required = true) String bcMetngDetlId,
			ModelMap model) throws Exception {
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		WbBcMetngDVo wbBcMetngDVo = new WbBcMetngDVo();
		wbBcMetngDVo.setQueryLang(langTypCd);
		
		wbBcMetngDVo.setCompId(userVo.getCompId());//회사ID
		/*if(schOpenYn == null || "".equals(schOpenYn)){
			wbBcMetngDVo.setRegrUid(userVo.getUserUid());
		}else{
			wbBcMetngDVo.setSchOpenYn(schOpenYn);
		}*/
		
		wbBcMetngDVo.setBcMetngDetlId(bcMetngDetlId);
		// 미팅정보 조회
		wbBcMetngDVo = wbMetngSvc.getWbMetngInfoVo(wbBcMetngDVo, langTypCd);
		
		if(wbBcMetngDVo == null){
			//cm.msg.notValidCall=파라미터가 잘못되었거나 보안상의 이유로 해당 기능을 수행할 수 없습니다.
			String msg = messageProperties.getMessage("cm.msg.notValidCall", request);
			throw new CmException(msg);
		}
		
		model.put("wbBcMetngDVo", wbBcMetngDVo);
		model.put("wbBcMetngAtndRVoList", wbBcMetngDVo.getWbBcMetngAtndRVo());
		
		// 첨부파일 리스트 model에 추가
		wbBcMetngFileSvc.putFileListToModel(wbBcMetngDVo.getBcMetngDetlId() != null ? wbBcMetngDVo.getBcMetngDetlId() : null, model, userVo.getCompId());
		
		// 리소스기본(WB_BC_B) 테이블 - UPDATE or INSERT
		QueryQueue queryQueue = new QueryQueue();
		wbBcMetngDVo.setReadCnt("");
		queryQueue.update(wbBcMetngDVo);
		commonSvc.execute(queryQueue);
		
		return LayoutUtil.getJspPath("/wb/viewMetngPop");
	}
	
	/** 등록 수정 화면 출력 */
	@RequestMapping(value = {"/wb/setMetng","/wb/setAgntMetng","/wb/adm/setAllMetng"})
	public String setMetng(HttpServletRequest request,
			@RequestParam(value = "bcMetngDetlId", required = false) String bcMetngDetlId,
			ModelMap model) throws Exception {
		
		// 요청경로 세팅
		String path = wbCmSvc.getRequestPath(request, model , "Metng");
		//관리자 (사용자 권한 체크)
		if("setAllMetng".equals(path) ) wbCmSvc.checkUserAuth(request, "A", null);
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		String schBcRegrUid = "";
		String compId = userVo.getCompId();
		//대리명함일시
		if("setAgntMetng".equals(path)){
			schBcRegrUid = ParamUtil.getRequestParam(request, "schBcRegrUid", true);
			/** 사용자의 명함 대리 관리자 정보 */
			Map<String,Object> agntInfoMap = wbCmSvc.getAgntInfoMap(request , schBcRegrUid , userVo.getUserUid() , "RW" );
			WbBcAgntAdmBVo wbBcAgntAdmBVo = (WbBcAgntAdmBVo)agntInfoMap.get("wbBcAgntAdmBVo");
			model.put("wbBcAgntAdmBVo", wbBcAgntAdmBVo);
			compId = (String)agntInfoMap.get("compId");
		}
		
		// 미팅 정보
		WbBcMetngDVo wbBcMetngDVo = new WbBcMetngDVo();
		
		List<WbBcMetngAtndRVo> wbBcMetngAtndRVoList = null;
		// 수정
		if (bcMetngDetlId != null && !bcMetngDetlId.isEmpty()) {
			wbBcMetngDVo.setQueryLang(langTypCd);
			wbBcMetngDVo.setCompId(userVo.getCompId());
			if("setMetng".equals(path) ){//개인명함
				wbBcMetngDVo.setCompId(userVo.getCompId());//회사ID			
				wbBcMetngDVo.setRegrUid(userVo.getUserUid());
			}else if("setAgntMetng".equals(path)){//대리명함
				wbBcMetngDVo.setCompId(compId);
				wbBcMetngDVo.setRegrUid(schBcRegrUid);
			}
			
			wbBcMetngDVo.setBcMetngDetlId(bcMetngDetlId);
			/** 미팅 조회 */
			wbBcMetngDVo = wbMetngSvc.getWbMetngInfoVo(wbBcMetngDVo, langTypCd);
			
			if("setAllMetng".equals(path)) schBcRegrUid = wbBcMetngDVo.getRegrUid();//등록자의 UID를 세팅한다.
			
			/** 참석자 조회 */
			wbBcMetngAtndRVoList = wbBcMetngDVo.getWbBcMetngAtndRVo();
		}
		model.put("schBcRegrUid", schBcRegrUid);
		model.put("wbBcMetngDVo", wbBcMetngDVo);
		
		// 첨부파일 리스트 model에 추가
		wbBcMetngFileSvc.putFileListToModel(wbBcMetngDVo.getBcMetngDetlId() != null ? wbBcMetngDVo.getBcMetngDetlId() : null, model, userVo.getCompId());
				
		// UI 구성용 - 빈 VO 하나 더함
		if(wbBcMetngAtndRVoList==null) wbBcMetngAtndRVoList = new ArrayList<WbBcMetngAtndRVo>();
		wbBcMetngAtndRVoList.add(new WbBcMetngAtndRVo());
		model.put("wbBcMetngAtndRVoList", wbBcMetngAtndRVoList);
				
		/** 분류코드 조회 */
		WbMetngClsCdBVo wbMetngClsCdBVo = new WbMetngClsCdBVo();
		wbMetngClsCdBVo.setQueryLang(langTypCd);
		wbMetngClsCdBVo.setCompId(userVo.getCompId());
		@SuppressWarnings("unchecked")
		List<WbMetngClsCdBVo> wbMetngClsCdBVoList = (List<WbMetngClsCdBVo>)commonSvc.queryList(wbMetngClsCdBVo);
		model.put("wbMetngClsCdBVoList", wbMetngClsCdBVoList);
		
		model.put("params", ParamUtil.getQueryString(request));
		model.put("paramsForList", ParamUtil.getQueryString(request, "bcMetngDetlId","schBcRegrUid"));
		
		// 시스템 정책 조회
		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
				
		// 타사 조직도 조회
		if("Y".equals(sysPlocMap.get("globalOrgChartEnable"))){
			request.setAttribute("globalOrgChartEnable", Boolean.TRUE);
		}
				
		return LayoutUtil.getJspPath("/wb/setMetng");
	}
	
	/** 등록 수정 (저장) */
	@RequestMapping(value = {"/wb/transMetng","/wb/transAgntMetng","/wb/adm/transAllMetng"})
	public String transMetng(HttpServletRequest request,
			ModelMap model) throws Exception {
		UploadHandler uploadHandler = null;
		try {
			// Multipart 파일 업로드
			uploadHandler = wbBcMetngFileSvc.upload(request);

			// MultipartRequest
			request = uploadHandler.getMultipartRequest();
			
			// 요청경로 세팅
			String path = wbCmSvc.getRequestPath(request, model , "Metng");
			//관리자 (사용자 권한 체크)
			if("transAllMetng".equals(path) ) wbCmSvc.checkUserAuth(request, "A", null);
			
			String listPage = ParamUtil.getRequestParam(request, "listPage", true);
			String viewPage = ParamUtil.getRequestParam(request, "viewPage", true);
			
			String bcMetngDetlId = ParamUtil.getRequestParam(request, "bcMetngDetlId", false);
			String delList = ParamUtil.getRequestParam(request, "delList", false);
			
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
						
			// 리소스기본(WB_BC_B) 테이블 - UPDATE or INSERT
			QueryQueue queryQueue = new QueryQueue();
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			WbBcMetngDVo wbBcMetngDVo = new WbBcMetngDVo();
			VoUtil.bind(request, wbBcMetngDVo);
			
			if("transMetng".equals(path)){
				wbBcMetngDVo.setCompId(userVo.getCompId());
			}else if("transAgntMetng".equals(path)){
				String schBcRegrUid = ParamUtil.getRequestParam(request, "schBcRegrUid", true);
				/** 사용자의 명함 대리 관리자 정보 */
				Map<String,Object> agntInfoMap = wbCmSvc.getAgntInfoMap(request , schBcRegrUid , userVo.getUserUid() , "RW" );
				//대리관리자일 경우 대리권한 부여자의 회사ID를 조회한다.
				wbBcMetngDVo.setCompId((String)agntInfoMap.get("compId"));
				wbBcMetngDVo.setCopyRegrUid(schBcRegrUid);
			}else{
				wbBcMetngDVo.setCompId(userVo.getCompId());
			}
			
			//수정이면서 관리자가 수정할 경우
			if ( bcMetngDetlId != null && !bcMetngDetlId.isEmpty() && "transAllMetng".equals(path)) {
				WbBcMetngDVo storedWbBcMetngDVo = new WbBcMetngDVo();
				storedWbBcMetngDVo.setCompId(userVo.getCompId());
				storedWbBcMetngDVo.setBcMetngDetlId(bcMetngDetlId);
				storedWbBcMetngDVo.setQueryLang(langTypCd);
				storedWbBcMetngDVo = (WbBcMetngDVo) commonSvc.queryVo(storedWbBcMetngDVo);
				wbBcMetngDVo.setCopyRegrUid(storedWbBcMetngDVo.getRegrUid());
				wbBcMetngDVo.setCompId(storedWbBcMetngDVo.getCompId());
			}
			
			// 삭제
			String[] delIds = delList==null || delList.isEmpty() ? new String[]{} : delList.split(",");
			wbBcMetngDVo.setDelList(delIds);
			wbBcMetngDVo.setCompId(userVo.getCompId());//회사ID
			wbMetngSvc.saveMetng(request, queryQueue , wbBcMetngDVo , userVo);
			
			// 첨부파일 저장
			List<CommonFileVo> deletedFileList = wbBcMetngFileSvc.saveBcFile(request, wbBcMetngDVo.getBcMetngDetlId(), queryQueue);
						
			commonSvc.execute(queryQueue);
			
			// 파일 삭제
			wbBcMetngFileSvc.deleteDiskFiles(deletedFileList);
						
			// cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			if (bcMetngDetlId == null || bcMetngDetlId.isEmpty()) {// 등록일 경우 목록 화면으로 이동
				model.put("todo", "parent.location.replace('" + listPage + "');");
			} else {// 수정일 경우 상세보기 화면으로 이동
				model.put("todo", "parent.location.replace('" + viewPage + "');");
			}
		} catch (CmException e) {
			LOGGER.error(e.getMessage());
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("exception", e);
		}finally {
			if (uploadHandler != null) try { uploadHandler.removeTempDir(); } catch (CmException ignored) {}
		}

		return LayoutUtil.getResultJsp();
	}
	
	/** 첨부파일 다운로드 (사용자) */
	@RequestMapping(value = {"/mt/downFile","/mt/preview/downFile"}, method = RequestMethod.POST)
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
			
			// fileId
			String[] fileIdArray = fileIds.split(",");
			List<CommonFileVo> fileVoList = new ArrayList<CommonFileVo>();
			for (String fileId : fileIdArray) {
				// 게시물첨부파일
				CommonFileVo fileVo = wbBcMetngFileSvc.getFileVo(Integer.valueOf(fileId));
				if (fileVo != null) {
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
			
		} catch (CmException e) {
			LOGGER.error(e.getMessage());
			ModelAndView mv = new ModelAndView("cm/result/commonResult");
			mv.addObject("message", e.getMessage());
			return mv;
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			ModelAndView mv = new ModelAndView("cm/result/commonResult");
			mv.addObject("message", e.getMessage());
		}
		return null;
	}
	
	/** 미팅 삭제 */
	@RequestMapping(value = {"/wb/transMetngDel","/wb/transAgntMetngDel","/wb/adm/transAllMetngDel"})
	public String transMetngDel(HttpServletRequest request,
			@RequestParam(value = "delList", required = true) String delList,
			ModelMap model) throws Exception {
		
		try {
			String listPage = ParamUtil.getRequestParam(request, "listPage", true);
			
			// 요청경로 세팅
			String path = wbCmSvc.getRequestPath(request, model , "Metng");
			//관리자 (사용자 권한 체크)
			if("transAllMetngDel".equals(path) ) wbCmSvc.checkUserAuth(request, "A", null);
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);

			// 리소스기본(BA_RESC_B) 테이블 - UPDATE or INSERT
			QueryQueue queryQueue = new QueryQueue();
			WbBcMetngDVo wbBcMetngDVo = new WbBcMetngDVo();
			if("transMetngDel".equals(path)){
				wbBcMetngDVo.setCompId(userVo.getCompId());
				wbBcMetngDVo.setModrUid(userVo.getUserUid());
			}else if("transAgntMetngDel".equals(path)){
				String schBcRegrUid = ParamUtil.getRequestParam(request, "schBcRegrUid", true);
				/** 사용자의 명함 대리 관리자 정보 */
				Map<String,Object> agntInfoMap = wbCmSvc.getAgntInfoMap(request , schBcRegrUid , userVo.getUserUid() , "RW" );
				//대리관리자일 경우 대리권한 부여자의 회사ID를 조회한다.
				wbBcMetngDVo.setCompId((String)agntInfoMap.get("compId"));
				wbBcMetngDVo.setModrUid(schBcRegrUid);
			}else{
				wbBcMetngDVo.setCompId(userVo.getCompId());
			}
			
			//미팅 및 참석자 삭제
			wbMetngSvc.deleteMetng(queryQueue, delList , wbBcMetngDVo);
			
			commonSvc.execute(queryQueue);
			model.put("message", messageProperties.getMessage("cm.msg.del.success", request));
			model.put("todo", "parent.location.replace('" + listPage + "');");
		} catch (CmException e) {
			LOGGER.error(e.getMessage());
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("exception", e);
		}

		return LayoutUtil.getResultJsp();
	}
	
	
	
	
	/** 엑셀파일 다운로드 (사용자) */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/wb/excelDownLoadMetng", method = RequestMethod.POST)
	public ModelAndView excelDownLoad(HttpServletRequest request,
			@RequestParam(value = "schOpenYn", required = false) String schOpenYn,
			@RequestParam(value = "schBcRegrUid", required=false) String schBcRegrUid,
			@Parameter(name="ext", required=false) String ext,
			ModelMap model) throws Exception {
		
		try {
			
			String listPage = ParamUtil.getRequestParam(request, "listPage", false);
			
			//관리자 (사용자 권한 체크)
			if("listAllMetng".equals(listPage) ) wbCmSvc.checkUserAuth(request, "A", null);
					
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			// 조회조건 매핑
			WbBcMetngDVo wbBcMetngDVo = new WbBcMetngDVo();
			VoUtil.bind(request, wbBcMetngDVo);
			wbBcMetngDVo.setQueryLang(langTypCd);
			
			boolean flag = false;
			if("listMetng".equals(listPage)){
				wbBcMetngDVo.setCompId(userVo.getCompId());//회사ID
				if(schOpenYn == null || "".equals(schOpenYn) || "N".equals(schOpenYn)){
					wbBcMetngDVo.setRegrUid(userVo.getUserUid());
				}
				flag = true;
			}else if("listAgntMetng".equals(listPage)){
				// 대리명함 조회UID가 있을경우 명함 등록자UID로 세팅한다.
				schBcRegrUid = wbCmSvc.getSchBcRegrUid(schBcRegrUid, userVo, langTypCd, model);
				if(schBcRegrUid != null && !schBcRegrUid.isEmpty() ) {
					/** 사용자의 명함 대리 관리자 정보 */
					Map<String,Object> agntInfoMap = wbCmSvc.getAgntInfoMap(request ,schBcRegrUid , userVo.getUserUid() , null );
					wbBcMetngDVo.setRegrUid(schBcRegrUid);
					//대리관리자일 경우 대리권한 부여자의 회사ID를 조회한다.
					wbBcMetngDVo.setCompId((String)agntInfoMap.get("compId"));
					model.put("schBcRegrUid", schBcRegrUid);
					flag = true;
				}
				// 대리명함 조회UID가 없을경우 대리관리자 목록이 없는 것이므로 명함 조회를 하지 않는다.
			}else if("listAllMetng".equals(listPage)){
				wbBcMetngDVo.setCompId(userVo.getCompId());//회사ID
				flag = true;
			}
			
			if(!flag){
				ModelAndView mv = new ModelAndView("cm/result/commonResult");
				//cm.msg.notValidCall=파라미터가 잘못되었거나 보안상의 이유로 해당 기능을 수행할 수 없습니다.
				mv.addObject("message",messageProperties.getMessage("cm.msg.notValidCall", request));
				return mv;
			}
			
			//목록 조회
			List<WbBcMetngDVo> list = (List<WbBcMetngDVo>)commonSvc.queryList(wbBcMetngDVo);
			
			if(list.size() == 0 ){
				ModelAndView mv = new ModelAndView("cm/result/commonResult");
				mv.addObject("message", messageProperties.getMessage("em.msg.noExcelData", request));
				return mv;
			}
			
			// 파일 목록조회
			ModelAndView mv = new ModelAndView("excelDownloadView");
			
			//보여줄 목록 정의
			String[][] columns = new String[][]{{"metngSubj","cols.subj"},{"bcNm","wb.cols.bc"},{"compNm","cols.comp"},{"metngYmd","cols.metngDt"},{"metngClsNm","wb.cols.cls"},{"regrNm","cols.regr"},{"regDt","cols.regDt"},{"readCnt","cols.readCnt"}};
			
			//컬럼명
			List<String> colNames = new ArrayList<String>();
			//데이터
			List<String> colValue = null;
			Map<String,Object> colValues = new HashMap<String,Object>();
			Map<String,Object> tempMap = null;
			for(int i=0;i<list.size();i++){
				tempMap = VoUtil.toMap(list.get(i), null);
				colValue = new ArrayList<String>();
				for(String[] column : columns){
					colValue.add((String)tempMap.get(column[0]));
					if( i == 0 )	colNames.add(messageProperties.getMessage(column[1], request)); //컬럼명 세팅
				}
				colValues.put("col"+i,colValue);//데이터 세팅
			}
			
			//시트명 세팅
			String sheetNm = "wb.jsp."+listPage+".title";
			
			mv.addObject("sheetName", messageProperties.getMessage(sheetNm, request));//시트명
			mv.addObject("colNames", colNames);//컬럼명
			mv.addObject("colValues", colValues);//데이터
			mv.addObject("fileName", messageProperties.getMessage(sheetNm, request));//파일명
			mv.addObject("ext", ext == null ? "xlsx" : ext);//파일 확장자(없으면 xls)
			
			return mv;
			
		} catch (CmException e) {
			LOGGER.error(e.getMessage());
			ModelAndView mv = new ModelAndView("cm/result/commonResult");
			mv.addObject("message", e.getMessage());
			return mv;
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			ModelAndView mv = new ModelAndView("cm/result/commonResult");
			mv.addObject("message", e.getMessage());
		}
		return null;
	}
	
	
}
