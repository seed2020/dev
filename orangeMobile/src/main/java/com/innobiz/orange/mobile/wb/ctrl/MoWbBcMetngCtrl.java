package com.innobiz.orange.mobile.wb.ctrl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.innobiz.orange.mobile.cm.utils.MoLayoutUtil;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.files.UploadHandler;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.JsonUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.ParamUtil;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.CommonFileVo;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.wb.svc.WbBcMetngFileSvc;
import com.innobiz.orange.web.wb.svc.WbCmSvc;
import com.innobiz.orange.web.wb.svc.WbMetngSvc;
import com.innobiz.orange.web.wb.vo.WbBcAgntAdmBVo;
import com.innobiz.orange.web.wb.vo.WbBcMetngAtndRVo;
import com.innobiz.orange.web.wb.vo.WbBcMetngDVo;
import com.innobiz.orange.web.wb.vo.WbMetngClsCdBVo;

/** 관련미팅 */
@Controller
public class MoWbBcMetngCtrl {

	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 공통 서비스 */
	@Autowired
	private WbCmSvc wbCmSvc;
	
	/** 관련미팅 관리 */
	@Autowired
	private WbMetngSvc wbMetngSvc;
	
	/** 메세지 */
	@Autowired
	private MessageProperties messageProperties;
	
	/** 첨부파일 서비스 */
	@Autowired
	private WbBcMetngFileSvc wbBcMetngFileSvc;
	
	/** 관련 미팅 목록 조회 */
	@RequestMapping(value = {"/wb/listMetng","/wb/listAgntMetng"})
	public String listMetng(HttpServletRequest request,
			@RequestParam(value = "schOpenYn", required = false) String schOpenYn,
			@RequestParam(value = "schBcRegrUid", required=false) String schBcRegrUid,
			ModelMap model) throws Exception {
		
		// 요청경로 세팅
		String path = wbCmSvc.getRequestPath(request, model , "Metng");

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
		
		model.put("paramsForList", ParamUtil.getQueryString(request, "schOpenYn","schBcRegrUid", "noCache"));

		return MoLayoutUtil.getJspPath("/wb/listMetng");
	}
	
	/** 상세보기 */
	@RequestMapping(value = {"/wb/viewMetng","/wb/viewAgntMetng"})
	public String viewMetng(HttpServletRequest request,
			@RequestParam(value = "schOpenYn", required = false) String schOpenYn,
			@RequestParam(value = "bcMetngDetlId", required = true) String bcMetngDetlId,
			ModelMap model) throws Exception {
		
		// 요청경로 세팅
		String path = wbCmSvc.getRequestPath(request, model , "Metng");
				
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

		// 리소스기본(WB_BC_B) 테이블 - UPDATE or INSERT
		QueryQueue queryQueue = new QueryQueue();
		wbBcMetngDVo.setReadCnt("");
		queryQueue.update(wbBcMetngDVo);
		commonSvc.execute(queryQueue);
		
		model.put("paramsForList", ParamUtil.getQueryString(request, "bcMetngDetlId","schBcRegrUid", "noCache"));

		return MoLayoutUtil.getJspPath("/wb/viewMetng");
	}
	
	
	/** 미팅 삭제 */
	@RequestMapping(value = {"/wb/transMetngDel","/wb/transAgntMetngDel"})
	public String transMetngDel(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) throws Exception {
		
		try {

			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String delList	 = (String) object.get("delList");
			String schBcRegrUid = (String) object.get("schBcRegrUid");
			
			if (delList == null) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 요청경로 세팅
			String path = wbCmSvc.getRequestPath(request, model , "Metng");

			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);

			// 리소스기본(BA_RESC_B) 테이블 - UPDATE or INSERT
			QueryQueue queryQueue = new QueryQueue();
			WbBcMetngDVo wbBcMetngDVo = new WbBcMetngDVo();
			if("transMetngDel".equals(path)){
				wbBcMetngDVo.setCompId(userVo.getCompId());
				wbBcMetngDVo.setModrUid(userVo.getUserUid());
			}else if("transAgntMetngDel".equals(path)){
				/** 사용자의 명함 대리 관리자 정보 */
				Map<String,Object> agntInfoMap = wbCmSvc.getAgntInfoMap(request , schBcRegrUid , userVo.getUserUid() , "RW" );
				//대리관리자일 경우 대리권한 부여자의 회사ID를 조회한다.
				wbBcMetngDVo.setCompId((String)agntInfoMap.get("compId"));
				wbBcMetngDVo.setModrUid(schBcRegrUid);
			}
			
			//미팅 및 참석자 삭제
			wbMetngSvc.deleteMetng(queryQueue, delList , wbBcMetngDVo);
			
			commonSvc.execute(queryQueue);
			model.put("message", messageProperties.getMessage("cm.msg.del.success", request));
			model.put("result", "ok");
		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			model.put("message", e.getMessage());
		}

		return JsonUtil.returnJson(model);
	}
	
	/** 등록 수정 화면 출력 */
	@RequestMapping(value = {"/wb/setMetng","/wb/setAgntMetng"})
	public String setMetng(HttpServletRequest request,
			@RequestParam(value = "bcMetngDetlId", required = false) String bcMetngDetlId,
			ModelMap model) throws Exception {
		
		// 요청경로 세팅
		String path = wbCmSvc.getRequestPath(request, model , "Metng");
		
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
		
//		List<WbBcMetngAtndRVo> wbBcMetngAtndRVoList = null;
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

			/** 참석자 조회 */
			if(wbBcMetngDVo.getWbBcMetngAtndRVo() != null){
				List<WbBcMetngAtndRVo> wbBcMetngAtndRVoList = wbBcMetngDVo.getWbBcMetngAtndRVo();
				model.put("wbBcMetngAtndRVoList", wbBcMetngAtndRVoList);
			}
		}
		model.put("schBcRegrUid", schBcRegrUid);
		model.put("wbBcMetngDVo", wbBcMetngDVo);
		
		// 첨부파일 리스트 model에 추가
		wbBcMetngFileSvc.putFileListToModel(wbBcMetngDVo.getBcMetngDetlId() != null ? wbBcMetngDVo.getBcMetngDetlId() : null, model, userVo.getCompId());
				
		// UI 구성용 - 빈 VO 하나 더함
		/*if(wbBcMetngAtndRVoList==null) wbBcMetngAtndRVoList = new ArrayList<WbBcMetngAtndRVo>();
		wbBcMetngAtndRVoList.add(new WbBcMetngAtndRVo());
		model.put("wbBcMetngAtndRVoList", wbBcMetngAtndRVoList);*/
				
		/** 분류코드 조회 */
		WbMetngClsCdBVo wbMetngClsCdBVo = new WbMetngClsCdBVo();
		wbMetngClsCdBVo.setQueryLang(langTypCd);
		wbMetngClsCdBVo.setCompId(userVo.getCompId());
		@SuppressWarnings("unchecked")
		List<WbMetngClsCdBVo> wbMetngClsCdBVoList = (List<WbMetngClsCdBVo>)commonSvc.queryList(wbMetngClsCdBVo);
		model.put("wbMetngClsCdBVoList", wbMetngClsCdBVoList);
		
		model.put("params", ParamUtil.getQueryString(request));
		model.put("paramsForList", ParamUtil.getQueryString(request, "bcMetngDetlId","schBcRegrUid"));
		model.put("nonPageParams", ParamUtil.getQueryString(request, "menuId","schBcRegrUid","pageNo"));
		return MoLayoutUtil.getJspPath("/wb/setMetng");
	}
	
	/** 등록 수정 (저장) */
	@RequestMapping(value = {"/wb/transMetngPost","/wb/transAgntMetngPost"})
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
			
			String listPage = ParamUtil.getRequestParam(request, "listPage", true);
			String viewPage = ParamUtil.getRequestParam(request, "viewPage", true);
			
			String bcMetngDetlId = ParamUtil.getRequestParam(request, "bcMetngDetlId", false);
			String delList = ParamUtil.getRequestParam(request, "delList", false);
			
			// 세션의 언어코드
			//String langTypCd = LoginSession.getLangTypCd(request);
						
			// 리소스기본(WB_BC_B) 테이블 - UPDATE or INSERT
			QueryQueue queryQueue = new QueryQueue();
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			WbBcMetngDVo wbBcMetngDVo = new WbBcMetngDVo();
			VoUtil.bind(request, wbBcMetngDVo);
			
			if("transMetngPost".equals(path)){
				wbBcMetngDVo.setCompId(userVo.getCompId());
			}else if("transAgntMetngPost".equals(path)){
				String schBcRegrUid = ParamUtil.getRequestParam(request, "schBcRegrUid", true);
				/** 사용자의 명함 대리 관리자 정보 */
				Map<String,Object> agntInfoMap = wbCmSvc.getAgntInfoMap(request , schBcRegrUid , userVo.getUserUid() , "RW" );
				//대리관리자일 경우 대리권한 부여자의 회사ID를 조회한다.
				wbBcMetngDVo.setCompId((String)agntInfoMap.get("compId"));
				wbBcMetngDVo.setCopyRegrUid(schBcRegrUid);
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
				model.put("todo", "$m.nav.prev(event, '" + listPage + "');");
			} else {// 수정일 경우 상세보기 화면으로 이동
				model.put("todo", "$m.nav.prev(event, '" + viewPage + "');");
			}
		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			model.put("exception", e);
		}finally {
			if (uploadHandler != null) try { uploadHandler.removeTempDir(); } catch (CmException ignored) {}
		}

		return MoLayoutUtil.getResultJsp();
	}
	
}
