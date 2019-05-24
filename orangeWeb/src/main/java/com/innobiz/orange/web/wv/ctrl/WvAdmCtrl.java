package com.innobiz.orange.web.wv.ctrl;

import java.sql.SQLException;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
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
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.wv.svc.WvSurvSvc;
import com.innobiz.orange.web.wv.vo.WvSurvPopupDVo;

/** 설문조사 - 관리자 */
@Controller
public class WvAdmCtrl {
	
	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(WvAdmCtrl.class);
	
	/** 공통 서비스 */
	@Autowired
	private WvSurvSvc wvSurvSvc;
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 메세지 */
	@Autowired
	private MessageProperties messageProperties;
	
//	/** 설문 공통 서비스 */
//	@Autowired
//	private WvCmSvc wvCmSvc;
	
	/** 팝업설정 */
	@RequestMapping(value = "/wv/adm/setPopupPop")
	public String setPopupPop(HttpServletRequest request,
			@RequestParam(value = "survId", required = false) String survId,
			ModelMap model) throws SQLException {

		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		WvSurvPopupDVo wvSurvPopupDVo = new WvSurvPopupDVo();
		wvSurvPopupDVo.setSurvId(survId);
		wvSurvPopupDVo.setCompId(userVo.getCompId());
		wvSurvPopupDVo =  (WvSurvPopupDVo) commonSvc.queryVo(wvSurvPopupDVo);
		
		model.put("wvSurvPopupDVo", wvSurvPopupDVo);
		
		return LayoutUtil.getJspPath("/wv/adm/setPopupPop");
	}
	
	
	/** [AJAX] - 팝업설정 저장 */
	@RequestMapping(value = "/wv/adm/transPopupAjx")
	public String transPopupAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) {
		try {
			
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			
			String survId = (String) object.get("survId");
			if (survId == null || survId.isEmpty()) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 파라미터 맵으로 변환
			Map<String,Object> paramMap = JsonUtil.jsonToMap(object);
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			QueryQueue queryQueue = new QueryQueue();
			
			WvSurvPopupDVo wvSurvPopupDVo = new WvSurvPopupDVo();
			wvSurvSvc.setParamToVo(paramMap, wvSurvPopupDVo); // 맵 => vo 
			wvSurvPopupDVo.setCompId(userVo.getCompId());
			wvSurvPopupDVo.setModrUid(userVo.getUserUid());
			wvSurvPopupDVo.setModDt("sysdate");
		
			queryQueue.store(wvSurvPopupDVo);
			commonSvc.execute(queryQueue);
			
			// cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			model.put("result", "ok");
			
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}
		
		return JsonUtil.returnJson(model);
	}
	

}
