package com.innobiz.orange.web.cu.ctrl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.tiles.autotag.core.runtime.annotation.Parameter;
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
import com.innobiz.orange.web.cm.config.CustConfig;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.JsonUtil;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.ParamUtil;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.cu.svc.CuNaraInsaSvc;
import com.innobiz.orange.web.cu.vo.CuPsnCardBVo;
import com.innobiz.orange.web.cu.vo.CuPsnImgDVo;
import com.innobiz.orange.web.cu.vo.CuPsnInfoDVo;
import com.innobiz.orange.web.or.svc.OrCmSvc;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.SecuUtil;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.utils.PersonalUtil;

/** 인사기록 */
@Controller
public class CuNaraInsaCtrl {

	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(CuNaraInsaCtrl.class);

	/** 메세지 */
	@Autowired
	private MessageProperties messageProperties;

	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 공통 서비스 */
	@Resource(name = "cuNaraInsaSvc")
	private CuNaraInsaSvc cuNaraInsaSvc;
	
	/** 조직도 서비스 */
	@Resource(name = "orCmSvc")
	private OrCmSvc orCmSvc;
	
	/** 목록 */
	@RequestMapping(value = {"/cu/mgm/insa/listPsnCard", "/cu/adm/insa/listPsnCard"})
	public String listPsnCard(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
				
		// 테이블관리 기본(BA_TBL_B) 테이블 - BIND
		CuPsnCardBVo cuPsnCardBVo = new CuPsnCardBVo();		
		VoUtil.bind(request, cuPsnCardBVo);
		cuPsnCardBVo.setQueryLang(langTypCd);
		cuPsnCardBVo.setCompId(userVo.getCompId());
		
		cuPsnCardBVo.setOrderBy("T.EIN, T.KO_NM ASC");
		
		// 회사 전체 조회여부
		boolean isAllComp=(request.getRequestURI().startsWith("/cu/mgm/") && SecuUtil.hasAuth(request, "A", null)) || 
				request.getRequestURI().startsWith("/cu/adm/");
		//boolean isCompAdmin = request.getRequestURI().startsWith("/cu/adm/") && SecuUtil.hasAuth(request, "S", null);
		if(!isAllComp)
			cuPsnCardBVo.setEinList(cuNaraInsaSvc.getEinList(userVo.getOrgId()));
		
		Integer recodeCount = commonSvc.count(cuPsnCardBVo);
		PersonalUtil.setPaging(request, cuPsnCardBVo, recodeCount);
		
		@SuppressWarnings("unchecked")
		List<CuPsnCardBVo> list = (List<CuPsnCardBVo>) commonSvc.queryList(cuPsnCardBVo);

		model.put("recodeCount", recodeCount);
		model.put("cuPsnCardBVoList", list);
		
		// print css 적용
		if(request.getAttribute("printView")==null){
			request.setAttribute("printView", "print100");
		}
		
		model.put("isAdmin", request.getRequestURI().startsWith("/cu/adm/"));
		return LayoutUtil.getJspPath("/cu/form/nara/listPsnCard");
	}
	
	/** 등록 화면 (사용자) */
	@RequestMapping(value = {"/cu/insa/setPsnCard", "/cu/insa/viewPsnCard", "/cu/mgm/insa/setPsnCard", 
			"/cu/mgm/insa/viewPsnCard","/cu/adm/insa/setPsnCard", "/cu/adm/insa/viewPsnCard"})
	public String setPsnCard(HttpServletRequest request,
			@RequestParam(value = "cardNo", required = false) String cardNo,
			ModelMap model) throws Exception {
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		// 관리자 여부
		boolean isMgm=!request.getRequestURI().startsWith("/cu/insa/");
		
		if(request.getRequestURI().startsWith("/cu/insa/") && !userVo.getUserUid().equals(userVo.getOdurUid())){
			model.put("message", "겸직은 등록할 수 없습니다.");
			model.put("todo", "history.go(-1);");
			return LayoutUtil.getResultJsp();
		}
		
		// 등록여부
		boolean isSet=request.getRequestURI().startsWith("/cu/insa/set") || 
				request.getRequestURI().startsWith("/cu/mgm/insa/set") || 
				request.getRequestURI().startsWith("/cu/adm/insa/set");
		
		// 사용자 정보조회
		Map<String, Object> userMap=orCmSvc.getUserMap(userVo.getUserUid(), langTypCd);
		// 상세보기여부
		if(!isMgm){
			if(userMap!=null && userMap.containsKey("ein") && userMap.get("ein")!=null){
				// 테이블관리 기본(BA_TBL_B) 테이블 - BIND
				CuPsnCardBVo cuPsnCardBVo = new CuPsnCardBVo();
				cuPsnCardBVo.setQueryLang(langTypCd);
				cuPsnCardBVo.setCompId(userVo.getCompId());
				cuPsnCardBVo.setEin((String)userMap.get("ein")); // 사번
				if(commonSvc.count(cuPsnCardBVo)==1){
					cuPsnCardBVo = (CuPsnCardBVo)commonSvc.queryVo(cuPsnCardBVo);
					cardNo=String.valueOf(cuPsnCardBVo.getCardNo());
				}else isSet=true;
			}
		}
		
		// 수정인 경우
		if (cardNo != null && !cardNo.isEmpty()) {
			CuPsnCardBVo cuPsnCardBVo = new CuPsnCardBVo();
			cuPsnCardBVo.setCompId(userVo.getCompId());
			cuPsnCardBVo.setCardNo(Integer.parseInt(cardNo));
			cuPsnCardBVo = (CuPsnCardBVo)commonSvc.queryVo(cuPsnCardBVo);
			
			if(cuPsnCardBVo==null){
				// cm.msg.noData=해당하는 데이터가 없습니다.
				throw new CmException("cm.msg.noData", request);
			}
			
			if(!isMgm && userMap!=null){ // 관리자가 아니면 사번을 조직정보에서 세팅
				if(userMap!=null && userMap.containsKey("ein") && userMap.get("ein")!=null && userMap.get("ein").equals(cuPsnCardBVo.getEin()))
					model.put("isModify", Boolean.TRUE);
				cuPsnCardBVo.setEin((String)userMap.get("ein"));
			}
			
			// 관리자 이면서 관리자(회사 관리자) 가 아닐경우 권한체크
			/*if(isMgm && !SecuUtil.hasAuth(request, "S", null) && cuPsnCardBVo.getEin()!=null && !cuNaraInsaSvc.isEinAuthChk(userVo.getCompId(), userVo.getOrgId(), cuPsnCardBVo.getEin())) {
				 //cm.msg.errors.403=해당 기능에 대한 권한이 없습니다.
				throw new CmException("cm.msg.errors.403", request);
			}*/

			// 이미지 조회
			CuPsnImgDVo cuPsnImgDVo = new CuPsnImgDVo();
			cuPsnImgDVo.setCardNo(Integer.parseInt(cardNo));
			cuPsnImgDVo = (CuPsnImgDVo)commonSvc.queryVo(cuPsnImgDVo);
			if(cuPsnImgDVo!=null) model.put("cuPsnImgDVo", cuPsnImgDVo);
			
			if(cuPsnImgDVo==null){ // 이미지가 없을 경우 조직도에서 조회
				cuNaraInsaSvc.setOrUserImg(model, isMgm ? cuNaraInsaSvc.getUserUid(userVo.getCompId(), cuPsnCardBVo.getEin()) : userVo.getUserUid());
			}
			
			// 개인정보 조회
			cuNaraInsaSvc.setPsnInfo(cuPsnCardBVo, cardNo);
			
			model.put("cuPsnCardBVo", cuPsnCardBVo);
			model.put("formBodyXML", SAXHandler.parse(cuPsnCardBVo.getBodyXml()));
			
		}else{
			if(!isMgm && userMap!=null){
				cuNaraInsaSvc.setOrUser(model, userMap);
				cuNaraInsaSvc.setOrUserImg(model, userVo.getUserUid());
			}
			model.put("formBodyXML", new XMLElement(null));
		}
		
		if(!isSet || request.getRequestURI().startsWith("/cu/mgm/insa/view") || request.getRequestURI().startsWith("/cu/adm/insa/view")){
			model.put("formBodyMode", "view");
			
			// print css 적용
			if(request.getAttribute("printView")==null){
				request.setAttribute("printView", "print100");
			}
		}
		else model.put("formBodyMode", "set");
		
		model.addAttribute("paramsForList", ParamUtil.getQueryString(request, "cardNo"));
		model.put("isMgm", isMgm);
		model.put("isAdmin", request.getRequestURI().startsWith("/cu/adm/"));
		model.put("custCode", CustConfig.CUST_CODE);
		
		return LayoutUtil.getJspPath("/cu/form/nara/setPsnCard");
	}
	
	
	/** 저장 */
	@RequestMapping(value = {"/cu/insa/transPsnCard", "/cu/mgm/insa/transPsnCard","/cu/adm/insa/transPsnCard"}, method = RequestMethod.POST)
	public String transPsnCard(HttpServletRequest request,
			@RequestParam(value = "cardNo", required = false) String cardNo,
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
			boolean isNew=cardNo==null || cardNo.isEmpty();
			
			// 조회조건 매핑
			CuPsnCardBVo cuPsnCardBVo = new CuPsnCardBVo();
			VoUtil.bind(request, cuPsnCardBVo);
			
			if (isNew) {
				cardNo=String.valueOf(commonSvc.nextVal("CU_PSN_CARD_B").intValue());
				cuPsnCardBVo.setCompId(userVo.getCompId());
				// ID 생성
				cuPsnCardBVo.setCardNo(Integer.parseInt(cardNo));				
				// 등록자, 등록일시
				cuPsnCardBVo.setRegrUid(userVo.getUserUid());
				cuPsnCardBVo.setRegDt("sysdate");
				// INSERT
				queryQueue.insert(cuPsnCardBVo);
				
				// 개인정보 저장
			} else{
				// 수정자, 수정일시
				cuPsnCardBVo.setModrUid(userVo.getUserUid());
				cuPsnCardBVo.setModDt("sysdate");
				// UPDATE
				queryQueue.update(cuPsnCardBVo);
			}
			
			// 개인정보 저장
			cuNaraInsaSvc.savePsnInfo(request, queryQueue, cardNo);
			
			String tmpImgId=ParamUtil.getRequestParam(request, "tmpImgId", false);
			// 이미지 저장
			if(tmpImgId != null && !tmpImgId.isEmpty()) cuNaraInsaSvc.setPsnImgDVo(request, queryQueue, cardNo, tmpImgId);
			
			commonSvc.execute(queryQueue);
			// cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			if (!isNew || request.getRequestURI().startsWith("/cu/insa/")) {
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
	@RequestMapping(value = {"/cu/insa/transPsnCardDelAjx", "/cu/mgm/insa/transPsnCardDelAjx","/cu/adm/insa/transPsnCardDelAjx"})
	public String transPsnCardDelAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) {
		
		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			JSONArray jsonArray = (JSONArray) object.get("cardNos");
			if (jsonArray == null) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
						
			// 삭제
			QueryQueue queryQueue = new QueryQueue();
			
			CuPsnCardBVo cuPsnCardBVo = null;
			CuPsnInfoDVo cuPsnInfoDVo = null;
			List<String> delImgPathList=new ArrayList<String>();
			String no, imgPath;
			for (int i = 0; i < jsonArray.size(); i++) {
				no=((String)jsonArray.get(i)).trim();
				
				if(request.getRequestURI().startsWith("/cu/insa/")){
					// 사용자 정보조회
					Map<String, Object> userMap=orCmSvc.getUserMap(userVo.getUserUid(), "ko");

					CuPsnCardBVo srchVo = new CuPsnCardBVo();
					srchVo.setCompId(userVo.getCompId());
					srchVo.setCardNo(Integer.parseInt(no));
					srchVo = (CuPsnCardBVo)commonSvc.queryVo(srchVo);
					if(srchVo==null || userMap==null || (!userMap.get("ein").equals(srchVo.getEin()))){
						// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
						throw new CmException("pt.msg.nodata.passed", request);
					}
				}
				
				//이미지 삭제
				imgPath=cuNaraInsaSvc.getImgPath(queryQueue, no);
				if(imgPath!=null) delImgPathList.add(imgPath);
				
				//개인정보 삭제
				cuPsnInfoDVo = new CuPsnInfoDVo();
				cuPsnInfoDVo.setCardNo(Integer.parseInt(no));
				queryQueue.delete(cuPsnInfoDVo);
				
				// 인사기록 삭제
				cuPsnCardBVo = new CuPsnCardBVo();
				cuPsnCardBVo.setCompId(userVo.getCompId());
				cuPsnCardBVo.setCardNo(Integer.parseInt(no));
				queryQueue.delete(cuPsnCardBVo);
			}
			
			commonSvc.execute(queryQueue);
			
			// 파일 삭제
			cuNaraInsaSvc.deleteDiskFiles(delImgPathList);
			
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
	
	/** [팝업] 이미지 선택 */
	@RequestMapping(value = {"/cu/insa/setImagePop", "/cu/insa/viewImagePop", "/cu/mgm/insa/setImagePop", 
			"/cu/mgm/insa/viewImagePop", "/cu/adm/insa/setImagePop", "/cu/adm/insa/viewImagePop"})
	public String setImagePop(HttpServletRequest request,
			@Parameter(name="cardNo", required=false) String cardNo,
			ModelMap model) throws Exception {
		
		if(cardNo!=null && !cardNo.isEmpty()){
			CuPsnImgDVo cuPsnImgDVo = new CuPsnImgDVo();
			cuPsnImgDVo.setCardNo(Integer.parseInt(cardNo));
			cuPsnImgDVo = (CuPsnImgDVo)commonSvc.queryVo(cuPsnImgDVo);
			if(cuPsnImgDVo!=null)	model.put("cuPsnImgDVo", cuPsnImgDVo);
			else {
				// 세션의 사용자 정보
				UserVo userVo = LoginSession.getUser(request);
				
				// 관리자 여부
				boolean isMgm=!request.getRequestURI().startsWith("/cu/insa/");
				String userUid=!isMgm ? userVo.getUserUid() : null;
				if(isMgm){
					CuPsnCardBVo cuPsnCardBVo = new CuPsnCardBVo();
					cuPsnCardBVo.setCompId(userVo.getCompId());
					cuPsnCardBVo.setCardNo(Integer.parseInt(cardNo));
					cuPsnCardBVo = (CuPsnCardBVo)commonSvc.queryVo(cuPsnCardBVo);
					userUid=cuNaraInsaSvc.getUserUid(userVo.getCompId(), cuPsnCardBVo.getEin());
				}
				
				cuNaraInsaSvc.setOrUserImg(model, userUid);
			}
		}
		if(request.getRequestURI().startsWith("/cu/insa/view") || 
				request.getRequestURI().startsWith("/cu/mgm/insa/view") ||
				request.getRequestURI().startsWith("/cu/adm/insa/view") ) model.put("isView", Boolean.TRUE);
		else model.put("isView", Boolean.FALSE);
		
		return LayoutUtil.getJspPath("/cu/form/nara/setImagePop");
	}
	
	/** [AJAX] 사용자 정보 조회 */
	@RequestMapping(value = {"/cu/mgm/insa/getUserMapAjx", "/cu/adm/insa/getUserMapAjx"})
	public String getUserMapAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = false) String data,
			ModelMap model) throws Exception {

		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String ein = (String) object.get("ein");
			if (ein == null || ein.isEmpty()) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 회사 전체 조회여부
			boolean isAllComp=(request.getRequestURI().startsWith("/cu/mgm/") && SecuUtil.hasAuth(request, "A", null)) || 
					request.getRequestURI().startsWith("/cu/adm/");
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			if(!isAllComp){
				List<String> einList=cuNaraInsaSvc.getEinList(userVo.getOrgId());
				
				boolean isEinChk=false;
				for(String chkEin : einList){
					if(chkEin.equals(ein)){
						isEinChk=true;
						break;
					}
				}
				
				if(!isEinChk){
					//cm.msg.noData=해당하는 데이터가 없습니다.
					throw new CmException("cm.msg.noData", request);
				}
				
			}
			
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			
			// 사용자 USERUID 조회
			String userUid=cuNaraInsaSvc.getUserUid(userVo.getCompId(), ein);
			
			if(userUid==null){
				//cm.msg.noData=해당하는 데이터가 없습니다.
				throw new CmException("cm.msg.noData", request);
			}
			
			// 사용자 정보조회
			Map<String, Object> userMap=orCmSvc.getUserMap(userUid, langTypCd);
			if(userMap!=null){
				cuNaraInsaSvc.setOrUser(model, userMap);
				cuNaraInsaSvc.setOrUserImg(model, userUid);
				
				model.put("result", "ok");
			}
						
			
		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}

		return JsonUtil.returnJson(model);
	}
	
	/** [AJAX] 사원번호 중복체크 */
	@RequestMapping(value = {"/cu/insa/isDupUserAjx", "/cu/adm/insa/isDupUserAjx"})
	public String isDupUserAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = false) String data,
			ModelMap model) throws Exception {

		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String ein = (String) object.get("ein");
			if (ein == null || ein.isEmpty()) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			// 테이블관리 기본(BA_TBL_B) 테이블 - BIND
			CuPsnCardBVo cuPsnCardBVo = new CuPsnCardBVo();
			cuPsnCardBVo.setCompId(userVo.getCompId());
			cuPsnCardBVo.setEin(ein); // 사번
			if(commonSvc.count(cuPsnCardBVo)>=1) model.put("dupYn", "Y");
			else model.put("dupYn", "N");
			
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
