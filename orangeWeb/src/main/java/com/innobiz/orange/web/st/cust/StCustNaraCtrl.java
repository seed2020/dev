package com.innobiz.orange.web.st.cust;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.innobiz.orange.web.cm.config.CustConfig;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.utils.HttpClient;
import com.innobiz.orange.web.cm.utils.JsonUtil;
import com.innobiz.orange.web.em.svc.EmSsoSvc;
import com.innobiz.orange.web.or.svc.OrCmSvc;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;

@Controller
public class StCustNaraCtrl {
	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(StCustNaraCtrl.class);
	
	/** 조직 공통 서비스 */
	@Autowired
	private EmSsoSvc emSsoSvc;
	
	/** 조직 공통 서비스 */
	@Autowired
	private OrCmSvc orCmSvc;
	
//	/** 메세지 */
//	@Autowired
//    private MessageProperties messageProperties;
	
	// 제휴소프트웨어인증키
	private final String swCrtcKey = "f6e0062e-988a-4f15-8083-ae81d3b4a510";
	
	// 테스트 여부
	private final boolean isTest=CustConfig.DEV_SVR;
		
	// 비즈플레이 URL
	private final String bizplayUrl = "https://www.bizplay.co.kr/BpCpldGateAPI"; // "https://www.bizplay.co.kr/BpCpldGate";
	
	/** [AJAX] - Bizplay 사이트 호출을 위한 파라미터 리턴 */
	@RequestMapping(value = "/cm/nara/getBizplayParamAjx")
	public String transWorkNoAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) throws SQLException, IOException{
		
		try {
			if(CustConfig.CUST_NARACELLAR || CustConfig.DEV_SVR){
				
				// 세션의 언어코드
				String langTypCd = LoginSession.getLangTypCd(request);
				
				// 세션의 사용자 정보
				UserVo userVo = LoginSession.getUser(request);
				
				// 연동랜덤키
				String randomKey = null;
				
				// 랜덤키 구분자리수 배열
				int[] randomSeperator=new int[]{8, 4, 4, 4, 12};
				
				for(int randomLen : randomSeperator){
					if(randomKey==null) randomKey=emSsoSvc.createRandomKey(randomLen);
					else randomKey+="-"+emSsoSvc.createRandomKey(randomLen);
				}
				
				// bizplay 사용자ID[그룹웨어 메일ID]
				String bpUsrId=null;
				
				// 제휴소프트웨어사용자ID
				String usrId=null;
				
				// 사용자 맵
				Map<String, Object> userMap = orCmSvc.getUserMap(userVo.getUserUid(), langTypCd);
				
				if(isTest){
					bpUsrId = "tyyoon@naracellar.com";
					usrId = "180501";
				}else{
					if(userMap==null){
						if("ko".equals(langTypCd))	throw new CmException("사용자가 없습니다.\n관리자에게 문의하세요.");
						else throw new CmException("You do not have any users.. \n Please contact your administrator.");
					}
					// bizplay 사용자ID[그룹웨어 메일ID]
					bpUsrId = (String)userMap.get("email");
					
					// 메일 주소가 등록되지 않으면 오류 처리
					if(bpUsrId==null){
						if("ko".equals(langTypCd))	throw new CmException("사용자의 이메일이 등록되지 않았습니다.\n관리자에게 문의하세요.");
						else throw new CmException("Your email is not registered. \n Please contact your administrator.");
					}
					
					// 제휴소프트웨어사용자ID
					usrId = (String)userMap.get("lginId");
				}
				
				// 리턴맵
				Map<String, String> returnMap = new HashMap<String, String>();
				returnMap.put("SW_CRTC_KEY", swCrtcKey);
				returnMap.put("CPLD_RDM_KEY", randomKey);
				returnMap.put("BP_USR_ID", bpUsrId);
				returnMap.put("USR_ID", usrId);
				
				Map<String, String> param = new HashMap<String, String>();
				param.put("JSONData", JsonUtil.toJson(returnMap));
				
				HttpClient http = new HttpClient();
				String result = http.sendPost(bizplayUrl, param, null, "UTF-8");
				if(result!=null){
					result=URLDecoder.decode(result, "UTF-8");
					JSONObject json = (JSONObject) JSONValue.parse(result);
					
					// json 파싱 오류 또는 null 이면 오류처리
					if(json==null){
						if("ko".equals(langTypCd))	throw new CmException("요청이 실패하였습니다.\n관리자에게 문의하세요.");
						else throw new CmException("The request failed. \n Please contact your administrator.");
					}					
					// 요청한 랜덤키와 리턴키가 다를경우 오류처리
					String returnKey=(String)json.get("CPLD_RDM_KEY");
					if(returnKey==null || !returnKey.equals(randomKey)){
						if("ko".equals(langTypCd))	throw new CmException("중복된 요청입니다.\n관리자에게 문의하세요.");
						else throw new CmException("Duplicate request. \n Please contact your administrator.");
					}
					
					// 응답 코드
					String rsltCd = (String)json.get("RSLT_CD"); 
					
					// 응답코드가 0000(완료) 일 경우 json 데이터 리턴
					if(rsltCd!=null && "0000".equals(rsltCd)){
						returnMap = new HashMap<String, String>();
						returnMap.put("SW_CRTC_KEY", swCrtcKey);
						returnMap.put("CPLD_RDM_KEY", (String)json.get("CPLD_RDM_KEY"));
						returnMap.put("BP_USR_ID", (String)json.get("BP_USR_ID")); // bizplay사용자ID
						returnMap.put("USR_ID", (String)json.get("USR_ID")); // 제휴상품사용자ID
						returnMap.put("BP_RETN_KEY", (String)json.get("BP_RETN_KEY")); // bizplay발급리턴키
						returnMap.put("RDM_VRFC_YN", "Y"); // 랜덤키검증결과
						
						String jsonData=JsonUtil.toJson(returnMap).toString();
						
						model.put("jsonData", jsonData);
						
					}else{
						// 응답 메시지
						String rsltMsg = (String)json.get("RSLT_MSG");
						model.put("message", rsltMsg);
					}
					
				}
			}
			
		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}
		
		return JsonUtil.returnJson(model);
	}
	
	/** 랜덤키 검증 페이지 */
	@RequestMapping(value = "/cm/nara/checkBizplayKey")
	public String checkBizplayKey(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "JSONData", required = false) String jsonData
			) throws SQLException, IOException{
		
		System.out.println("jsonData : "+jsonData);
		
		try {
			if(jsonData!=null && !jsonData.isEmpty()){
				JSONObject json = (JSONObject) JSONValue.parse(jsonData);
				if(json!=null){
					String ssoId=(String)json.get("CPLD_RDM_KEY");
					Map<String,String> returnMap = new HashMap<String, String>();
					returnMap.put("SW_CRTC_KEY", swCrtcKey);
					returnMap.put("BP_USR_ID", (String)json.get("BP_USR_ID"));
					returnMap.put("USR_ID", (String)json.get("USR_ID"));
					returnMap.put("BP_RETN_KEY", (String)json.get("BP_RETN_KEY")); // 리턴키
					returnMap.put("CPLD_RDM_KEY", ssoId);
					
					if(ssoId!=null && emSsoSvc.chkSsoKey(null, ssoId, null)){
						returnMap.put("RDM_VRFC_YN", "Y"); // 랜덤키 검증성공
					}else{
						returnMap.put("RDM_VRFC_YN", "N"); // 랜덤키 검증실패
					}
					
					PrintWriter write = response.getWriter();
					write.print(JsonUtil.toJson(returnMap).toString());
					write.flush();
					write.close();
				}
				
				return null;
			}
		}catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		response.sendError(403);
		return null;
	}
	
	/** [AJAX] - Bizplay 사이트 호출을 위한 파라미터 리턴 */
	@RequestMapping(value = "/cm/nara/getBizplayRandomAjx")
	public String getBizplayRandomAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = false) String data,
			ModelMap model) throws SQLException, IOException{
		
		try {
			if(CustConfig.CUST_NARACELLAR || CustConfig.DEV_SVR){
				// 세션의 언어코드
				String langTypCd = LoginSession.getLangTypCd(request);
				
				// 세션의 사용자 정보
				UserVo userVo = LoginSession.getUser(request);
				
				// 연동랜덤키
				String randomKey = null;
				
				// 랜덤키 구분자리수 배열
				int[] randomSeperator=new int[]{8, 4, 4, 4, 12};
				
				for(int randomLen : randomSeperator){
					if(randomKey==null) randomKey=emSsoSvc.createRandomKey(randomLen);
					else randomKey+="-"+emSsoSvc.createRandomKey(randomLen);
				}
				
				// 랜덤키 DB Table 에 저장
				String ssoId=emSsoSvc.getSsoKey(null, userVo, randomKey, null);
				
				// bizplay 사용자ID[그룹웨어 메일ID]
				String bpUsrId=null;
				
				// 제휴소프트웨어사용자ID
				String usrId=null;
				
				// 사용자 맵
				Map<String, Object> userMap = orCmSvc.getUserMap(userVo.getOdurUid(), langTypCd);
				
				if(isTest){
					bpUsrId = "tyyoon@naracellar.com";
					usrId = "180501";
				}else{
					// bizplay 사용자ID[그룹웨어 메일ID]
					bpUsrId = (String)userMap.get("email");
					// 제휴소프트웨어사용자ID
					usrId = (String)userMap.get("lginId");
				}
				
				// 리턴맵
				Map<String, String> returnMap = new HashMap<String, String>();
				returnMap.put("SW_CRTC_KEY", swCrtcKey);
				returnMap.put("CPLD_RDM_KEY", ssoId);
				returnMap.put("BP_USR_ID", bpUsrId);
				returnMap.put("USR_ID", usrId);
				
				model.put("returnJson", JsonUtil.toJson(returnMap));
				
			}
			
			
		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}
		
		return JsonUtil.returnJson(model);
	}
}
