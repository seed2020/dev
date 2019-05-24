package com.innobiz.orange.web.ap.down;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.tiles.autotag.core.runtime.annotation.Parameter;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.innobiz.orange.web.ap.vo.ApOngdBVo;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.StringUtil;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;

/** 문서 다운로드 컨트롤 */
@Controller
public class ApDownCtrl {

	/** 메세지 */
	@Autowired
    private MessageProperties messageProperties;
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
//	/** 결재 공통 서비스 */
//	@Autowired
//	private ApCmSvc apCmSvc;
	
//	/** 결재 함 서비스 */
//	@Autowired
//	private ApBxSvc apBxSvc;

	/** 문서 다운로드 서비스 */
	@Autowired
	private ApDownSvc apDownSvc;

	/** [팝업] 다운로드 팝업 */
	@RequestMapping(value = "/ap/adm/box/setDownloadPop")
	public String setDownloadPop(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		// 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		Map<String, String> prop = apDownSvc.getRunningProp();
		if(prop==null){
			prop = new HashMap<String, String>();
			prop.put("durEndDt", StringUtil.getCurrYmd());
			prop.put("baseDir", apDownSvc.getRootDir()+"/ap/"+userVo.getCompId()+"/");
			prop.put("storeDir", "download");
		}
		
		model.put("prop", prop);
		
		return LayoutUtil.getJspPath("/ap/adm/box/setDownloadPop");
	}
	/** [AJAX] 다운로드 건수 조회 */
	@RequestMapping(value = "/ap/adm/box/getDownTgtAjx")
	public String getDocNoAjx(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			Locale locale,
			ModelMap model) throws Exception {
		
		String message = null;
		JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
		UserVo userVo = LoginSession.getUser(request);
		
		try {
			
			String durStrtDt = (String)jsonObject.get("durStrtDt");
			String durEndDt = (String)jsonObject.get("durEndDt");
			
			ApOngdBVo apOngdBVo = apDownSvc.createDownVo(userVo.getCompId(), durStrtDt, durEndDt);
			
			Integer tgtCnt = commonSvc.count(apOngdBVo);
			model.put("tgtCnt", tgtCnt);
			
		} catch(Exception e){
			message = e.getMessage();
			if(message==null || message.isEmpty()) message = e.getClass().getCanonicalName();
		}
		
		return LayoutUtil.returnJson(model, message);
	}
	
	/** [AJAX] 다운로드 시작 */
	@RequestMapping(value = "/ap/adm/box/processDownAjx")
	public String processDownAjx(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			Locale locale,
			ModelMap model) throws Exception {
		
		String message = null;
		JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
		UserVo userVo = LoginSession.getUser(request);
		
		try {
			
			Map<String, String> map = new HashMap<String, String>();
			map.put("compId", userVo.getCompId());
			map.put("durStrtDt", (String)jsonObject.get("durStrtDt"));
			map.put("durEndDt",  (String)jsonObject.get("durEndDt"));
			map.put("storeDir",  (String)jsonObject.get("storeDir"));
			map.put("tgtCnt",    (String)jsonObject.get("tgtCnt"));
			map.put("fileType",  (String)jsonObject.get("fileType"));
			map.put("webId",     getCookie(request, "ORANGE_WEB_ID"));
			map.put("rootDir",   apDownSvc.getRootDir());
			map.put("baseDir",   apDownSvc.getRootDir()+"/ap/"+userVo.getCompId()+"/");
			
			if(!apDownSvc.startDown(map)){
				// ap.msg.downloadProcessing=다운로드가 이미 진행 중입니다.
				model.put("message", messageProperties.getMessage("ap.msg.downloadProcessing", request));
			}
			
		} catch(Exception e){
			message = e.getMessage();
			if(message==null || message.isEmpty()) message = e.getClass().getCanonicalName();
		}
		
		return LayoutUtil.returnJson(model, message);
	}
	
	/** [AJAX] 다운로드 수 조회 /진행 상황 표시용 */
	@RequestMapping(value = "/ap/adm/box/getDownProcCntAjx")
	public String getDownProcCntAjx(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			Locale locale,
			ModelMap model) throws Exception {
		
		String message = null;
		try {
			
			int cnt = apDownSvc.getCmplCnt();
			model.put("cmplCnt", Integer.toString(cnt));
			cnt = apDownSvc.getErrCnt();
			model.put("errCnt", Integer.toString(cnt));
			cnt = apDownSvc.getTgtCnt();
			model.put("tgtCnt", Integer.toString(cnt));
			
			if(apDownSvc.isStop()){
				model.put("stop", "Y");
			}
			
		} catch(Exception e){
			message = e.getMessage();
			if(message==null || message.isEmpty()) message = e.getClass().getCanonicalName();
		}
		
		return LayoutUtil.returnJson(model, message);
	}
	
	/** [AJAX] 다운로드 중지 */
	@RequestMapping(value = "/ap/adm/box/processDownStopAjx")
	public String processDownStopAjx(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			Locale locale,
			ModelMap model) throws Exception {
		
		apDownSvc.stopDown();
		
		return LayoutUtil.returnJson(model);
	}
	
	/** [AJAX] 다운로드 중지 */
	@RequestMapping(value = "/ap/adm/box/processDownDropAjx")
	public String processDownDropAjx(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			Locale locale,
			ModelMap model) throws Exception {
		
		apDownSvc.stopDrop();
		
		return LayoutUtil.returnJson(model);
	}
	
	/** 쿠키값 리턴 */
	private String getCookie(HttpServletRequest request, String cookieName){
		Cookie[] cookies = request.getCookies();
		if(cookies!=null){
			for(Cookie cookie : cookies){
				if(cookieName.equals(cookie.getName())) return cookie.getValue();
			}
		}
		return null;
	}
}
