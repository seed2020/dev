package com.innobiz.orange.web.wa.ctrl;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.innobiz.orange.web.ap.utils.SAXHandler;
import com.innobiz.orange.web.ap.utils.XMLElement;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.JsonUtil;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.ParamUtil;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.utils.PersonalUtil;
import com.innobiz.orange.web.wa.svc.WaDrivRecSvc;
import com.innobiz.orange.web.wa.vo.WaCarKndLVo;
import com.innobiz.orange.web.wa.vo.WaCorpBVo;
import com.innobiz.orange.web.wa.vo.WaDrivRecBVo;

/** 자동차 운행기록 */
@Controller
public class WaDrivRecCtrl {

	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(WaDrivRecCtrl.class);

	/** 메세지 */
	@Autowired
	private MessageProperties messageProperties;

	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 공통 서비스 */
	@Resource(name = "waDrivRecSvc")
	private WaDrivRecSvc waDrivRecSvc;
	
	/** 목록 */
	@RequestMapping(value = "/wa/driv/listDrivRec")
	public String listDrivRec(HttpServletRequest request,
			@RequestParam(value = "corpNo", required = false) String corpNo,
			ModelMap model) throws Exception {
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
				
		// 테이블관리 기본(WA_DRIV_REC_B) 테이블 - BIND
		WaDrivRecBVo waDrivRecBVo = new WaDrivRecBVo();
		waDrivRecBVo.setQueryLang(langTypCd);
		VoUtil.bind(request, waDrivRecBVo);
		
		waDrivRecBVo.setCompId(userVo.getCompId());
				
		Integer recodeCount = commonSvc.count(waDrivRecBVo);
		PersonalUtil.setPaging(request, waDrivRecBVo, recodeCount);
		
		@SuppressWarnings("unchecked")
		List<WaDrivRecBVo> list = (List<WaDrivRecBVo>) commonSvc.queryList(waDrivRecBVo);

		model.put("recodeCount", recodeCount);
		model.put("waDrivRecBVoList", list);
		
		WaCorpBVo waCorpBVo = new WaCorpBVo();
		waCorpBVo.setQueryLang(langTypCd);
		waCorpBVo.setCompId(userVo.getCompId());
		
		// 법인조회
		@SuppressWarnings("unchecked")
		List<WaCorpBVo> waCorpBVoList = (List<WaCorpBVo>)commonSvc.queryList(waCorpBVo);
		model.put("waCorpBVoList", waCorpBVoList);
		
		if(corpNo!=null && !corpNo.isEmpty()){
			WaCarKndLVo waCarKndLVo = new WaCarKndLVo();
			waCarKndLVo.setQueryLang(langTypCd);
			waCarKndLVo.setCompId(userVo.getCompId());
			waCarKndLVo.setCorpNo(corpNo);
			
			@SuppressWarnings("unchecked")
			List<WaCarKndLVo> waCarKndLVoList = (List<WaCarKndLVo>)commonSvc.queryList(waCarKndLVo);
			model.put("waCarKndLVoList", waCarKndLVoList);
		}
		
		// print css 적용
		if(request.getAttribute("printView")==null){
			request.setAttribute("printView", "print100");
		}
				
		return LayoutUtil.getJspPath("/wa/driv/listDrivRec");
	}
	
	/** 등록 화면 (사용자) */
	@RequestMapping(value = {"/wa/driv/setDrivRec", "/wa/driv/viewDrivRec"})
	public String setDrivRec(HttpServletRequest request,
			@RequestParam(value = "recNo", required = false) String recNo,
			ModelMap model) throws Exception {
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
					
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		boolean isNew=recNo == null || recNo.isEmpty();
		
		// 운행기록 조회
		WaDrivRecBVo waDrivRecBVo = null;
		// 수정인 경우
		if (!isNew) {
			// 운행기록 조회
			waDrivRecBVo = new WaDrivRecBVo();
			waDrivRecBVo.setCompId(userVo.getCompId());
			waDrivRecBVo.setRecNo(Integer.parseInt(recNo));
			waDrivRecBVo = (WaDrivRecBVo)commonSvc.queryVo(waDrivRecBVo);
			if(waDrivRecBVo==null){
				
			}
			model.put("waDrivRecBVo", waDrivRecBVo);
			
			XMLElement formBodyXML = SAXHandler.parse(waDrivRecBVo.getBodyXml());
			List<XMLElement> drivList = formBodyXML.getChildList("body/drivs");
			Integer recodeCount = drivList.size();
			model.put("formBodyXML", formBodyXML);
			PersonalUtil.setPaging(request, waDrivRecBVo, recodeCount);
			model.put("recodeCount", recodeCount);
		}else{
			model.put("formBodyXML", new XMLElement(null));
		}
		
		if(request.getRequestURI().startsWith("/wa/driv/view")){
			model.put("formBodyMode", "view");
			
			// print css 적용
			if(request.getAttribute("printView")==null){
				request.setAttribute("printView", "print100");
			}
		}else{
			model.put("formBodyMode", "set");
			
			WaCorpBVo waCorpBVo = new WaCorpBVo();
			waCorpBVo.setQueryLang(langTypCd);
			waCorpBVo.setCompId(userVo.getCompId());
			
			// 법인 목록조회
			@SuppressWarnings("unchecked")
			List<WaCorpBVo> waCorpBVoList = (List<WaCorpBVo>)commonSvc.queryList(waCorpBVo);
			model.put("waCorpBVoList", waCorpBVoList);
			
			if(waCorpBVoList!=null && waCorpBVoList.size()>0){
				if(!isNew){
					// 법인정보 조회
					waCorpBVo.setCorpNo(waDrivRecBVo.getCorpNo());
					waCorpBVo=(WaCorpBVo)commonSvc.queryVo(waCorpBVo);
					model.put("waCorpBVo", waCorpBVo);
				}
				
				String corpNo=!isNew ? waDrivRecBVo.getCorpNo() : waCorpBVoList.get(0).getCorpNo();
				WaCarKndLVo waCarKndLVo = new WaCarKndLVo();
				waCarKndLVo.setQueryLang(langTypCd);
				waCarKndLVo.setCompId(userVo.getCompId());
				waCarKndLVo.setCorpNo(corpNo);
				
				@SuppressWarnings("unchecked")
				List<WaCarKndLVo> waCarKndLVoList = (List<WaCarKndLVo>)commonSvc.queryList(waCarKndLVo);
				model.put("waCarKndLVoList", waCarKndLVoList);
				
				if(!isNew){
					// 법인정보 조회
					waCarKndLVo.setCarKndNo(waDrivRecBVo.getCarKndNo());
					waCarKndLVo=(WaCarKndLVo)commonSvc.queryVo(waCarKndLVo);
					model.put("waCarKndLVo", waCarKndLVo);
				}
			}
		}
		
		//model.addAttribute("paramsForList", ParamUtil.getQueryString(request, "recNo", "pageNo"));
		
		return LayoutUtil.getJspPath("/wa/driv/setDrivRec");
	}
	
	
	/** 저장 */
	@RequestMapping(value = "/wa/driv/transDrivRec", method = RequestMethod.POST)
	public String transDrivRec(HttpServletRequest request,
			@RequestParam(value = "recNo", required = false) String recNo,
			@RequestParam(value = "bodyXml", required = false) String bodyXml,
			ModelMap model) {
		
		try {
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			QueryQueue queryQueue = new QueryQueue();
			
			if (bodyXml == null) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			String listPage = ParamUtil.getRequestParam(request, "listPage", true);
			String viewPage = ParamUtil.getRequestParam(request, "viewPage", true);
			
			// 신규 여부
			boolean isNew=recNo==null || recNo.isEmpty();
			
			// 조회조건 매핑
			WaDrivRecBVo waDrivRecBVo = new WaDrivRecBVo();
			VoUtil.bind(request, waDrivRecBVo);
			
			if (isNew) {
				recNo=String.valueOf(commonSvc.nextVal("WA_DRIV_REC_B").intValue());
				waDrivRecBVo.setCompId(userVo.getCompId());
				// ID 생성
				waDrivRecBVo.setRecNo(Integer.parseInt(recNo));				
				// 등록자, 등록일시
				waDrivRecBVo.setRegrUid(userVo.getUserUid());
				waDrivRecBVo.setRegDt("sysdate");
				// INSERT
				queryQueue.insert(waDrivRecBVo);
				
				// 개인정보 저장
			} else{
				// 수정자, 수정일시
				waDrivRecBVo.setModrUid(userVo.getUserUid());
				waDrivRecBVo.setModDt("sysdate");
				// UPDATE
				queryQueue.update(waDrivRecBVo);
			}
			
			commonSvc.execute(queryQueue);
			// cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			if (!isNew) {
				model.put("todo", "parent.location.replace('" + viewPage + "');");
			}else{
				model.put("todo", "parent.location.replace('" + listPage + "');");
			}
			//model.put("todo", "parent.afterTrans(null);");
			
		} catch (CmException e) {
			LOGGER.error(e.getMessage());
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("exception", e);
		}

		return LayoutUtil.getResultJsp();
	}
	
	/** [AJAX] 삭제 (사용자) */
	@RequestMapping(value = "/wa/driv/transDrivRecDelAjx")
	public String transPsnCardDelAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) {
		
		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			JSONArray jsonArray = (JSONArray) object.get("recNos");
			if (jsonArray == null) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
						
			// 삭제
			QueryQueue queryQueue = new QueryQueue();
			
			WaDrivRecBVo waDrivRecBVo = null;
			String no;
			for (int i = 0; i < jsonArray.size(); i++) {
				no=((String)jsonArray.get(i)).trim();
				
				// 인사기록 삭제
				waDrivRecBVo = new WaDrivRecBVo();
				waDrivRecBVo.setCompId(userVo.getCompId());
				waDrivRecBVo.setRecNo(Integer.parseInt(no));
				queryQueue.delete(waDrivRecBVo);
			}
			
			commonSvc.execute(queryQueue);
			
			// cm.msg.del.success=삭제 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.del.success", request));
			model.put("result", "ok");
			
		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}
		
		return JsonUtil.returnJson(model);
	}
	
}
