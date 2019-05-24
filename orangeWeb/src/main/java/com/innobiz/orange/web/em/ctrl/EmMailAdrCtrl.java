package com.innobiz.orange.web.em.ctrl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.tiles.autotag.core.runtime.annotation.Parameter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.JsonUtil;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.ParamUtil;
import com.innobiz.orange.web.cm.utils.StringUtil;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.em.svc.EmMailAdrTranSvc;
import com.innobiz.orange.web.em.vo.EmMailAdrTranBVo;
import com.innobiz.orange.web.em.vo.EmMailAdrTranTgtTVo;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.utils.PersonalUtil;

/** 메일 주소록 이관 */
@Controller
public class EmMailAdrCtrl {
	
	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(EmMailAdrCtrl.class);
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 메세지 처리용 프라퍼티 - 다국어 */
	@Autowired
	private MessageProperties messageProperties;
	
	/** 이관 서비스 */
	@Autowired
	private EmMailAdrTranSvc emMailAdrTranSvc;
	
	/** 메일 주소록 이관 목록 */
	@RequestMapping(value = "/pt/adm/org/listMailAdrTran")
	public String listMailAdrTran(HttpServletRequest request,
			ModelMap model) throws SQLException {
		
		EmMailAdrTranBVo emMailAdrTranBVo = new EmMailAdrTranBVo();
		// 카운트 조회
		Integer recodeCount = commonSvc.count(emMailAdrTranBVo);
		PersonalUtil.setPaging(request, emMailAdrTranBVo, recodeCount);
		model.put("recodeCount", recodeCount);
		
		if(recodeCount.intValue()>0){
			emMailAdrTranBVo.setOrderBy("TRAN_ID DESC");
			@SuppressWarnings("unchecked")
			List<EmMailAdrTranBVo> emMailAdrTranBVoList = (List<EmMailAdrTranBVo>)commonSvc.queryList(emMailAdrTranBVo);
			
			if(emMailAdrTranBVoList != null){
				model.put("emMailAdrTranBVoList", emMailAdrTranBVoList);
			}
		}
		
		// 진행중인 이관 확인
		HttpSession session = request.getSession();
		String processingTranId = (String)session.getAttribute("MAIL_TRAN_ID");
		if(processingTranId != null){
			model.put("processingTranId", processingTranId);
			session.removeAttribute("MAIL_TRAN_ID");
		}
				
		return LayoutUtil.getJspPath("/em/adm/mail/listMailAdrTran");
	}
	
	/** [팝업] - 이관 진행 내역 */
	@RequestMapping(value = {"/pt/adm/org/setTranPop", "/pt/adm/org/listTranProcPop"})
	public String setTranPop(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		if(request.getRequestURI().startsWith("/pt/adm/org/set")){
			Map<String,Object> params=new HashMap<String,Object>();
			//params.put("userUid", "U000000E");
			
			// 메일 주소록 사용자 목록
			Integer userCnt = emMailAdrTranSvc.getMailAdrCount("mailUserCnt", params);
			model.put("userCnt", userCnt);
			
			// 메일 주소록 그룹 목록
			Integer fldCnt = emMailAdrTranSvc.getMailAdrCount("groupCnt", params);
			model.put("fldCnt", fldCnt);
			
			// 메일 주소록 사용자 목록
			Integer adrCnt = emMailAdrTranSvc.getMailAdrCount("personCnt", params);
			model.put("adrCnt", adrCnt);
		}else{
			String tranId = ParamUtil.getRequestParam(request, "tranId", true);
			EmMailAdrTranBVo emMailAdrTranBVo = new EmMailAdrTranBVo();
			emMailAdrTranBVo.setTranId(tranId);
			emMailAdrTranBVo=(EmMailAdrTranBVo)commonSvc.queryVo(emMailAdrTranBVo);
			model.put("userCnt", emMailAdrTranBVo.getUserCnt());
			model.put("fldCnt", emMailAdrTranBVo.getFldCnt());
			model.put("adrCnt", emMailAdrTranBVo.getAdrCnt());
			model.put("tranProcStatCd", emMailAdrTranBVo.getTranProcStatCd());
		}
		
		return LayoutUtil.getJspPath("/em/adm/mail/listTranProcPop");
	}
	
	/** [AJAX] - 메일 주소록 이관 - 이관 실행*/
	@RequestMapping(value = {"/pt/adm/org/tranMailAdrTranAjx"})
	public String tranMailAdrTranAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) {
		
		try {
			// 파라미터 검사
			//JSONObject object = (JSONObject) JSONValue.parse(data);
			
			QueryQueue queryQueue = new QueryQueue();
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			String tranId = StringUtil.getCurrDateTime().replaceAll("[-: ]", "");//-:공백 제거
			
			// 이관 테이블 - INSERT
			EmMailAdrTranBVo emMailAdrTranBVo = new EmMailAdrTranBVo();
			//if(commonSvc.count(emMailAdrTranBVo)>0){
				// pt.msg.nodata.passed=이관된 이력이 있습니다.
			//	throw new CmException("이관된 이력이 있습니다.");
			//}
			
			emMailAdrTranBVo.setTranId(tranId);
			emMailAdrTranBVo.setUserUid(userVo.getUserUid());
			
			if(commonSvc.count(emMailAdrTranBVo)>0){
				// pt.msg.nodata.passed=이관ID가 중복되었습니다.
				throw new CmException("이관ID가 중복되었습니다.");
			}
			emMailAdrTranBVo.setTranStrtDt("sysdate");
			
			Map<String,Object> params=new HashMap<String,Object>();
			//params.put("userUid", "U000000E");
			
			// 메일 주소록 사용자 목록
			Integer userCnt = emMailAdrTranSvc.getMailAdrCount("mailUserCnt", params);
			if(userCnt==null || userCnt.intValue()==0){
				// pt.msg.nodata.passed=이관ID가 중복되었습니다.
				throw new CmException("이관할 사용자가 없습니다.");
			}
			// 메일 주소록 그룹 목록
			Integer fldCnt = emMailAdrTranSvc.getMailAdrCount("groupCnt", params);
			if(fldCnt==null || fldCnt.intValue()==0){
				// pt.msg.nodata.passed=이관ID가 중복되었습니다.
				throw new CmException("이관할 폴더 목록이 없습니다.");
			}
			// 메일 주소록 사용자 목록
			Integer adrCnt = emMailAdrTranSvc.getMailAdrCount("personCnt", params);
			emMailAdrTranBVo.setUserCnt(userCnt);
			emMailAdrTranBVo.setFldCnt(fldCnt);
			emMailAdrTranBVo.setAdrCnt(adrCnt);
			
			queryQueue.insert(emMailAdrTranBVo);
			
			commonSvc.execute(queryQueue);
			model.put("tranId", tranId);
			model.put("result", "ok");
			request.getSession().setAttribute("MAIL_TRAN_ID", tranId);
			emMailAdrTranSvc.addTranId(tranId);
			
		}catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}
		
		return JsonUtil.returnJson(model);
	}
	
	/** [AJX] 재실행 - 이관 실행 */
	@RequestMapping(value = "/pt/adm/org/transReMailAdrTranAjx")
	public String transReMailAdrTranAjx(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			ModelMap model, Locale locale) throws Exception{
		
		JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
		String tranId = (String)jsonObject.get("tranId");
		emMailAdrTranSvc.addTranId(tranId);
		
		return LayoutUtil.returnJson(model);
	}
	
	/** [AJX] 이관 진행 내역 */
	@RequestMapping(value = "/pt/adm/org/getMailAdrTranAjx")
	public String getMailAdrTranAjx(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			ModelMap model, Locale locale) throws Exception{
		
		JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
		String tranId = (String)jsonObject.get("tranId");
		// 이관기본(EM_MAIL_ADR_TRAN_B) 테이블 - 프로세스 완료여부 조회
		EmMailAdrTranBVo emMailAdrTranBVo = new EmMailAdrTranBVo();
		emMailAdrTranBVo.setTranId(tranId);
		emMailAdrTranBVo = (EmMailAdrTranBVo)commonSvc.queryVo(emMailAdrTranBVo);
		if(emMailAdrTranBVo != null){
			// 이관진행상태코드 - preparing:준비중, processing:진행중, completed:완료, error:에러
			String tranProcStatCd = emMailAdrTranBVo.getTranProcStatCd();
			if("preparing".equals(tranProcStatCd) || "processing".equals(tranProcStatCd)){
				model.put("completed", "N");
			}
			if("error".equals(tranProcStatCd)){
				model.put("hasError", "Y");
			}
			if("processing".equals(tranProcStatCd)){
				// 이관대상 저장여부 조회
				EmMailAdrTranTgtTVo emMailAdrTranTgtTVo = new EmMailAdrTranTgtTVo();
				emMailAdrTranTgtTVo.setTranId(emMailAdrTranBVo.getTranId());
				//model.put("totalCnt", commonSvc.count(emMailAdrTranTgtTVo));
				emMailAdrTranTgtTVo.setErrYn("N");
				model.put("processCnt", commonSvc.count(emMailAdrTranTgtTVo));
			}
			if("completed".equals(tranProcStatCd)){
				model.put("completed", "Y");
			}
		}
		
		return LayoutUtil.returnJson(model);
	}
	
	/** [AJAX] 이관이력 삭제 */
	@RequestMapping(value = "/pt/adm/org/transMailAdrTranDelAjx")
	public String transMailAdrTranDelAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = false) String data,
			ModelMap model) throws Exception {

		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			JSONArray jsonArray = (JSONArray)object.get("tranIds"); // 이력ID
			if (jsonArray == null || jsonArray.size() == 0) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 테이블 삭제
			QueryQueue queryQueue = new QueryQueue();
			// 이관이력VO
			EmMailAdrTranBVo emMailAdrTranBVo = null;
			EmMailAdrTranTgtTVo emMailAdrTranTgtTVo = null;
			String tranId = null;
			for(int i=0;i<jsonArray.size();i++){
				tranId = (String)jsonArray.get(i);
				// 이관기본
				emMailAdrTranBVo = new EmMailAdrTranBVo();
				emMailAdrTranBVo.setTranId(tranId);
				if(commonSvc.count(emMailAdrTranBVo)==0) continue;
				// 이관진행내역
				emMailAdrTranTgtTVo = new EmMailAdrTranTgtTVo();
				emMailAdrTranTgtTVo.setTranId(tranId);
				queryQueue.delete(emMailAdrTranTgtTVo);
				
				queryQueue.delete(emMailAdrTranBVo);
			}
			if(!queryQueue.isEmpty())
				commonSvc.execute(queryQueue);
			// cm.msg.del.success=삭제 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.del.success", request));
			model.put("result", "ok");

		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			model.put("message", e.getMessage());
		}

		return JsonUtil.returnJson(model);
	}
}
