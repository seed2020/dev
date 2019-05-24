package com.innobiz.orange.web.pt.sso;

import java.io.IOException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;

import com.innobiz.orange.web.ap.vo.ApErpIntgBVo;
import com.innobiz.orange.web.ap.vo.ApOngdBVo;
import com.innobiz.orange.web.cm.config.ServerConfig;
import com.innobiz.orange.web.cm.crypto.CryptoSvc;
import com.innobiz.orange.web.cm.crypto.License;
import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.utils.HttpClient;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.or.vo.OrOdurBVo;
import com.innobiz.orange.web.or.vo.OrOrgBVo;
import com.innobiz.orange.web.or.vo.OrUserBVo;
import com.innobiz.orange.web.or.vo.OrUserPwDVo;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtSecuSvc;
import com.innobiz.orange.web.pt.svc.PtSysSvc;
import com.innobiz.orange.web.pt.utils.PtConstant;

public class SsoParamClientErp extends SsoParamClientImpl implements SsoParamClient {
	
	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(SsoParamClientErp.class);
	
	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;

	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
	/** 포털 보안 서비스 */
	@Autowired
	private PtSecuSvc ptSecuSvc;
	
	/** 암호화 서비스 */
	@Autowired
	private CryptoSvc cryptoSvc;
	
	/** 메세지 */
	@Autowired
    private MessageProperties messageProperties;

	/** 호출 경로 */
	private String callPath = null;
	
	/** 원타임 목록 - 중복 onetime 허용 안할 목적 */
	private LinkedList<String> ontimeList = new LinkedList<String>();
	
	/** 호출 경로 세팅 */
	public void setCallPath(String callPath){
		this.callPath = callPath;
	}
	
	/** SSO 및 연계 처리 */
	@Override
	public boolean checkSso(HttpServletRequest request, HttpServletResponse response) {
		
		// TEST
		
		// - 결재 화면 1건
		//   http://localhost:8080/ap/box/setDoc.do?erpOnetime=18
		// - 결재 임시 저장(다건)
		//   http://localhost:8080/cm/ap/processErpTemp.do?erpOnetime=401
		// - 메일 보내기
		//   http://ep.innogw.com/cm/zmailPop.do?erpOnetime=431
		//   >> 테스트용
		//   http://ep.innogw.com/em/zmailPop.do?erpOnetime=427
		
		// 결재 추가 인터페이스
		//   http://localhost:8080/ap/interface1.do?erpOnetime=18
		//   http://localhost:8080/ap/interface2.do?erpOnetime=18
		//   http://localhost:8080/ap/interface3.do?erpOnetime=18
		
		// SSO 체크
		String paramName = getParamName();
		if(paramName == null) return false;
		String uri = request.getRequestURI();
		boolean isErpDoc = false;
		boolean isErpTemp = false;
		boolean isErpMail = false;
		boolean errToLogin = false;
		boolean forDebug = false;
		
		int apInterfaceNo = 0;// 결재 추가 인터페이스 번호
		
		if(uri.indexOf("/ap/box/setDoc.do")>=0){
			isErpDoc = true;
			request.setAttribute("BY_SSO_TYPE", "ERP-AP-DOC");
			
		} else if( uri.indexOf("/ap/interface1.do")>=0
				|| uri.indexOf("/ap/interface2.do")>=0
				|| uri.indexOf("/ap/interface3.do")>=0){
			isErpDoc = true;
			request.setAttribute("BY_SSO_TYPE", "ERP-AP-DOC");
			
			if(uri.indexOf("/ap/interface1.do")>=0){
				apInterfaceNo = 1;
			} else if(uri.indexOf("/ap/interface2.do")>=0){
				apInterfaceNo = 2;
			} else if(uri.indexOf("/ap/interface3.do")>=0){
				apInterfaceNo = 3;
			}
		} else if(uri.indexOf("/cm/ap/processErpTemp.do")>=0){
			isErpTemp = true;
			request.setAttribute("BY_SSO_TYPE", "ERP-AP-TEMP");
		} else if(uri.indexOf("/cm/zmailPop.do")>=0){
			isErpMail = true;
			errToLogin = true;
			request.setAttribute("BY_SSO_TYPE", "ERP-MAIL");
		} else if(uri.indexOf("/em/zmailPop.do")>=0){
			isErpMail = true;
			errToLogin = true;
			forDebug = true;
			request.setAttribute("BY_SSO_TYPE", "ERP-MAIL");
		} else if(uri.indexOf(PtConstant.URL_SSO_LOGIN)>=0){
			return true;
		} else {
			return false;
		}
		// erp 비밀번호 사용
		boolean erpPwEnable = false;
		
		// 시스템 정책 조회
		try {
			Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
			if(!"Y".equals(sysPlocMap.get("erpEnable"))){
				if(apInterfaceNo==0){
					return errorSso(request, errToLogin);
				}
			}
			
			// erp 비밀번호 사용
			erpPwEnable = "Y".equals(sysPlocMap.get("erpPwEnable"));
			
			boolean useSSL = "Y".equals(sysPlocMap.get("useSSL"));
			if(useSSL && !request.isSecure() && request.getServerPort() == 80){
				String erpOnetime = request.getParameter(paramName);
				String redirectUrl = "https://"+request.getServerName()+uri+"?"+paramName+"="+erpOnetime;
				request.setAttribute("SSO_REDIRECT", redirectUrl);
				return true;
			}
			
			boolean erpAuthEnable = "Y".equals(sysPlocMap.get("erpAuthEnable"));
			if(erpAuthEnable){
				// 결재만 열리는 권한으로 SSO 처리
				request.setAttribute("SSO_USER_STAT_CD", "96");//96:SSO - 권한 제어용
			}
			
		} catch (SQLException se) {
			return errorSso(request, errToLogin);
		}
		
		String erpOnetime = request.getParameter(paramName);
		if(!erpPwEnable && isDupOnetime(erpOnetime)){
			HttpSession session = request.getSession(false);
			if(session!=null) session.invalidate();
			
			session = request.getSession(true);
			// ap.msg.dupOnetime=중복된 원타임ID 입니다.
			session.setAttribute("loginMessage", messageProperties.getMessage("ap.msg.dupOnetime", request));
			return errorSso(request, errToLogin);
		}
		
		JSONObject jsonObject = null;
		
		// 연계번호
		String intgNo = null, langTypCd = null, apvLnGrpId=null;//20141003
		// 연계번호 조회
		String json = null;
		
		try {
			
			if(erpPwEnable){
				// 패스워드 사용시 - erpOnetime 두번 호출 하지 않도록 - 세션에 저장함 
				if(erpOnetime!=null && erpOnetime.equals(request.getSession(true).getAttribute("SSO_ONETIME"))){
					json = (String)request.getSession(true).getAttribute("SSO_JSON");
				}
			}
			
			// 서버 설정 목록 조회
			Map<String, String> svrEnvMap = ptSysSvc.getSvrEnvMap();
			
			String url = null;
			// 결재 추가 연계의 경우 - URL을 통체로 받음
			if(apInterfaceNo > 0){
				url = svrEnvMap.get("apInterface"+apInterfaceNo);
				if(url==null || url.isEmpty()){
					LOGGER.error("ERP SSO fail - no interface url "+apInterfaceNo);
					return errorSso(request, errToLogin);
				}
				if(url.indexOf("erpOnetime=")>0){
					url = url + erpOnetime;
				} else {
					url = url + (url.indexOf('?')>0 ? '&' : '?') + "erpOnetime="+erpOnetime;
				}
				
			} else {
				
				String erpSvr = request.getParameter("erpSvr");//ERP 서버가 여럿일 경우 서버 정보를 파라미터로 받음
				String erpDomain = (erpSvr==null || erpSvr.isEmpty()) ? svrEnvMap.get("erpCall") : erpSvr;
				
				// 2018-08-17 : 한서버에 erp 여러개가 깔려 있을 경우 구분자 필요해서
				// erpdbnm 으로 넘어온 파라미터는 그대로 넘기는 로직 추가함
				String erpdbnm = request.getParameter("erpdbnm");
				if(erpdbnm!=null){
					if(erpdbnm.isEmpty()) erpdbnm = null;
					else erpdbnm = "&erpdbnm="+URLEncoder.encode(erpdbnm, "EUC-KR");
				}
				
				if(erpDomain==null || erpDomain.isEmpty()){
					LOGGER.error("ERP SSO fail - no erp domain");
					return errorSso(request, errToLogin);
				}
				url = (erpDomain.startsWith("http://") ? "" : "http://")
						+ erpDomain + callPath + "?" + paramName + "=" + erpOnetime
						+ (erpdbnm==null ? "" : erpdbnm);
			}
			
			if(!erpPwEnable || json==null){
				if(ServerConfig.IS_LOC || ServerConfig.IS_DEV){
					if(isErpDoc){
						json = "{\"lginId\":\"k01\",\"intgNo\":\""+erpOnetime+"\",\"langTypCd\":\"ko\"}";
					} else if(isErpTemp){
						json = "{\"lginId\":\"k01\",\"intgNos\":\""+erpOnetime+"\",\"langTypCd\":\"ko\"}";
					} else if(isErpMail){
						json = "{\"lginId\":\"k01\",\"emailId\":\""+erpOnetime+"\",\"langTypCd\":\"ko\"}";
					}
				} else {
					try{
						HttpClient http = new HttpClient();
						json = http.sendGet(url, "UTF-8");
						json = json==null ? null : json.trim();
					} catch(Exception e){
						Throwable root = toRootException(e);
						LOGGER.error("ERP SSO fail - call url fail : "+root.getClass().getCanonicalName()+" : "+root.getMessage());
					}
				}
			}
			
			if(json==null || json.isEmpty()){
				LOGGER.error("ERP SSO fail - empty json - "+paramName+":"+erpOnetime);
				if(apInterfaceNo > 0){
					return errorSso(request, errToLogin);
				}
				return false;
			}
			
			// JSON 파싱
			jsonObject = (JSONObject)JSONValue.parse(json);
			if(jsonObject==null){
				LOGGER.error("ERP SSO fail - json parsing - "+paramName+":"+erpOnetime+" / "+json);
				if(apInterfaceNo > 0){
					return errorSso(request, errToLogin);
				}
				return false;
			}
			
			// ERP 결재 임시저장의 경우
			if(isErpTemp){
				if(createSession(request, response, jsonObject, json, false)){
					if(erpPwEnable && isDupOnetime(erpOnetime)){
						HttpSession session = request.getSession(false);
						if(session!=null) session.invalidate();
						
						session = request.getSession(true);
						// ap.msg.dupOnetime=중복된 원타임ID 입니다.
						session.setAttribute("loginMessage", messageProperties.getMessage("ap.msg.dupOnetime", request));
						return errorSso(request, errToLogin);
					}
					
					String intgNos = (String)jsonObject.get("intgNos");
					if(intgNos!=null && !intgNos.isEmpty()){
						request.getSession().setAttribute("intgNos", intgNos);
					} else {
						intgNos = (String)jsonObject.get("intgNo");
						request.getSession().setAttribute("intgNos", intgNos);
					}
					
					langTypCd = (String)jsonObject.get("langTypCd");
					request.getSession(true).setAttribute("langTypCd", langTypCd);
				}
				return false;
			// ERP 메일 보내기
			} else if(isErpMail){
				if(createSession(request, response, jsonObject, json, erpPwEnable)){
					if(erpPwEnable && isDupOnetime(erpOnetime)){
						HttpSession session = request.getSession(false);
						if(session!=null) session.invalidate();
						
						session = request.getSession(true);
						// ap.msg.dupOnetime=중복된 원타임ID 입니다.
						session.setAttribute("loginMessage", messageProperties.getMessage("ap.msg.dupOnetime", request));
						return errorSso(request, errToLogin);
					}
					
					if(forDebug){
						request.setAttribute("SSO_REDIRECT", "/cm/zmailPop.do?emailId="+jsonObject.get("emailId"));
						return true;
					} else {
						request.setAttribute("emailId", jsonObject.get("emailId"));
						return true;
					}
				} else if(erpPwEnable && request.getSession(true).getAttribute("ERO_SSO_LGIN_ID")!=null){
					request.getSession(true).setAttribute("SSO_ONETIME", erpOnetime);
					request.getSession(true).setAttribute("SSO_JSON", json);
					return errorErpPw(request, paramName, erpOnetime);
				} else {
					return errorSso(request, errToLogin);
				}
			}
			
			
			intgNo = (String)jsonObject.get("intgNo");
			if(intgNo==null || intgNo.isEmpty()){
				// 연계번호 - 연계 파라미터에 없음
				LOGGER.error("ERP SSO fail - empty intgNo - "+paramName+":"+erpOnetime+" / "+json);
				return errorSso(request, errToLogin);
			}
			
			langTypCd = (String)jsonObject.get("langTypCd");
			if(langTypCd==null || langTypCd.isEmpty()){
				// 언어구분코드 - 연계 파라미터에 없음
				LOGGER.error("ERP SSO fail - empty langTypCd - "+paramName+":"+erpOnetime+" / "+json);
				return errorSso(request, errToLogin);
			}
			
			apvLnGrpId = (String)jsonObject.get("apvLnGrpId");
			
			// ERP연계기본(AP_ERP_INTG_B) 테이블
			ApErpIntgBVo apErpIntgBVo = new ApErpIntgBVo();
			apErpIntgBVo.setIntgNo(intgNo);
			apErpIntgBVo.setQueryLang(langTypCd);
			apErpIntgBVo = (ApErpIntgBVo)commonDao.queryVo(apErpIntgBVo);
			if(apErpIntgBVo==null){
				// 연계테이블에 데이터 없음
				LOGGER.error("ERP SSO fail - no data in AP_ERP_INTG_B - "+paramName+":"+erpOnetime+" - intgNo:"+intgNo + " / "+json);
				return errorSso(request, errToLogin);
			}
			
			boolean apNoData = false;
			
			// ERP연계상태코드 - 신청:req, 결재중:ongo ,반려:rejt, 승인:apvd, 취소:cncl
			String erpIntgStatCd = apErpIntgBVo.getIntgStatCd();
			String apvNo = apErpIntgBVo.getApvNo();
			if("req".equals(erpIntgStatCd)){
			} else if(!"ongo".equals(erpIntgStatCd)
					&& !"temp".equals(erpIntgStatCd) && !"cncl".equals(erpIntgStatCd)
					&& !"rejt".equals(erpIntgStatCd) && !"apvd".equals(erpIntgStatCd) ){
				// 연계상태코드 - 연계 테이블에 적절하지 않음
				LOGGER.error("ERP SSO fail - invalid Stat. in AP_ERP_INTG_B - "+paramName+":"+erpOnetime+" - intgNo:"+intgNo+", erpIntgStatCd:"+erpIntgStatCd
						+" -- erpIntgStatCd has to one of req, ongo, apvd, rejt, cncl" + "\n/ "+json);
				return errorSso(request, errToLogin);
			} else if(apvNo==null || apvNo.isEmpty()){
				if(!"cncl".equals(erpIntgStatCd)){
					// 결재번호 - 결재번호 없음
					LOGGER.error("ERP SSO fail - empty apvNo in AP_ERP_INTG_B - "+paramName+":"+erpOnetime+" - intgNo:"+intgNo + " / "+json);
					return errorSso(request, errToLogin);
				}
			} else {// 결재번호가 있을 때
				
				ApOngdBVo apOngdBVo = new ApOngdBVo();
				apOngdBVo.setApvNo(apvNo);
				if(commonDao.count(apOngdBVo)==0){
					apNoData = true;
				}
			}
			
			// 세션 생성
			boolean result = createSession(request, response, jsonObject, json, erpPwEnable);
			
			if(result){
				
				UserVo userVo = LoginSession.getUser(request);
				
				String redirectUrl = null;
				if("req".equals(erpIntgStatCd) || "temp".equals(erpIntgStatCd)){
					boolean isReq = "req".equals(erpIntgStatCd);
					
					String bxId = isReq ? "myBx" : "drftBx";
					String apvLnGrpParam = (apvLnGrpId==null || apvLnGrpId.isEmpty()) ? ""
							: "&apvLnGrpId="+apvLnGrpId;
					
					if(isReq || (apvNo==null || apvNo.isEmpty())){
						redirectUrl = "/ap/box/setDoc.do?bxId="+bxId + "&intgNo="+intgNo + apvLnGrpParam;
					} else {
						redirectUrl = "/ap/box/setDoc.do?bxId="+bxId + "&apvNo="+apvNo;
					}
					
					redirectUrl = ptSecuSvc.toAuthMenuUrl(userVo, redirectUrl, "/ap/box/listApvBx.do?bxId="+bxId);
					request.setAttribute("SSO_REDIRECT", redirectUrl);
					
				} else if("cncl".equals(erpIntgStatCd) || apNoData){
					// ap.msg.delDoc=삭제된 문서 입니다.
					String message = messageProperties.getMessage("ap.msg.delDoc", request);
					request.setAttribute("SSO_MESSAGE", message);
					
					redirectUrl = "/ap/box/listApvBx.do?bxId=waitBx";
					redirectUrl = ptSecuSvc.toAuthMenuUrl(userVo, redirectUrl, redirectUrl);
					request.setAttribute("SSO_REDIRECT", redirectUrl);
					
					if("cncl".equals(erpIntgStatCd)){
						LOGGER.error("ERP SSO - deleted doc : "+json);
					} else {
						LOGGER.error("ERP SSO - no data AP_ONGD_B - apvNo:"+apvNo + " / "+json);
					}
					return true;
				} else {
					String bxId = "ongo".equals(erpIntgStatCd) ? "ongoBx" :
						"cncl".equals(erpIntgStatCd) ? "myBx" :
							"rejt".equals(erpIntgStatCd) ? "rejtBx" :
								"apvd".equals(erpIntgStatCd) ? "apvdBx" : "";
					redirectUrl = "/ap/box/viewIntgDoc.do?bxId="+bxId+"&apvNo="+apvNo;
					
					redirectUrl = ptSecuSvc.toAuthMenuUrl(userVo, redirectUrl, "/ap/box/listApvBx.do?bxId="+bxId);
					request.setAttribute("SSO_REDIRECT", redirectUrl);
				}
			} else if(erpPwEnable && request.getSession(true).getAttribute("ERO_SSO_LGIN_ID")!=null){
				request.getSession(true).setAttribute("SSO_ONETIME", erpOnetime);
				request.getSession(true).setAttribute("SSO_JSON", json);
				return errorErpPw(request, paramName, erpOnetime);
			}
			return result;
			
		} catch(Exception e){
			Throwable root = toRootException(e);
			LOGGER.error("ERP SSO fail - "+json+"\r\n - "+root.getClass().getCanonicalName()+": "+root.getMessage()+"\r\n - "+uri);
			root.printStackTrace();
			return errorSso(request, errToLogin);
		}
	}
	
	private boolean isDupOnetime(String erpOnetime){
		if(ServerConfig.IS_LOC || ServerConfig.IS_DEV) return false;
		if(ontimeList.contains(erpOnetime)){
			return true;
		} else {
			ontimeList.add(erpOnetime);
			if(ontimeList.size()>10) ontimeList.removeFirst();
		}
		return false;
	}
	
	private boolean errorSso(HttpServletRequest request, boolean toLogin){
		if(toLogin){
			request.setAttribute("SSO_REDIRECT", PtConstant.URL_LOGIN);
			return true;
		}
		return false;
	}
	private boolean errorErpPw(HttpServletRequest request, String ssoParamName, String ssoParamValue){
		request.setAttribute("SSO_REDIRECT", PtConstant.URL_SSO_LOGIN);
		
		HttpSession session = request.getSession(true);
		session.setAttribute("ERO_SSO_URI", request.getRequestURI());
		session.setAttribute("ERO_SSO_PARAM", ssoParamName);
		session.setAttribute("ERO_SSO_VALUE", ssoParamValue);
		return true;
	}
	
	/** SSO에 의한 세션 생성 */
	private boolean createSession(HttpServletRequest request, HttpServletResponse response,
			JSONObject jsonObject, String json, boolean checkPw) throws SQLException, CmException, IOException {
		
		// 사용자구분 - userUid:사용자UID, lginId:로그인ID, rid:사용자참조ID, ein:사번
		// 부서구분 - orgId:조직ID, orgRid:조직참조ID
		String userTypCd=null, userTypVa=null, deptTypCd=null, deptTypVa=null;
		String langTypCd = (String)jsonObject.get("langTypCd");
		
		// 부서 조회
		OrOrgBVo orOrgBVo = null;
		String orgId = (String)jsonObject.get("orgId");
		String orgRid = (String)jsonObject.get("orgRid");
		if(orgId!=null && !orgId.isEmpty()){
			deptTypCd = "orgId";
			deptTypVa = orgId;
			orOrgBVo = new OrOrgBVo();
			orOrgBVo.setOrgId(deptTypVa);
			orOrgBVo.setQueryLang(langTypCd);
			orOrgBVo = (OrOrgBVo)commonDao.queryVo(orOrgBVo);
			if(orOrgBVo==null){
				// 부서정보 없음
				LOGGER.error("ERP SSO fail - no dept in OR_ORG_B by orgId:"+deptTypVa + " / "+json);
				return false;
			}
		} else if(jsonObject.get("orgRid")!=null){
			deptTypCd = "orgRid";
			deptTypVa = orgRid;
			orOrgBVo = new OrOrgBVo();
			orOrgBVo.setRid(deptTypVa);
			@SuppressWarnings("unchecked")
			List<OrOrgBVo> orOrgBVoList = (List<OrOrgBVo>)commonDao.queryList(orOrgBVo);
			if(orOrgBVoList==null || orOrgBVoList.isEmpty()){
				// 부서정보 없음
				LOGGER.error("ERP SSO fail - no dept in OR_ORG_B by orgRid:"+deptTypVa + " / "+json);
				return false;
			} else if(orOrgBVoList.size()>1){
				// 1개 이상의 부서가 메핑됨 - 부서참조코드 수정 필요
				LOGGER.warn("ERP SSO warn - "+orOrgBVoList.size()+" dept's in OR_ORG_B by orgRid:"+deptTypVa + " / "+json);
				orOrgBVo = orOrgBVoList.get(0);
			} else {
				orOrgBVo = orOrgBVoList.get(0);
			}
		}
		
		String paramUserUid = (String)jsonObject.get("userUid");
		
		// 원직자 정보 조회
		OrOdurBVo orOdurBVo = new OrOdurBVo();
		// userUid:사용자UID, lginId:로그인ID, rid:사용자참조ID, ein:사번
		if(jsonObject.get("lginId")!=null){
			userTypCd = "lginId";
			userTypVa = (String)jsonObject.get("lginId");
			orOdurBVo.setLginId(userTypVa);
		} else if(jsonObject.get("ein")!=null){
			userTypCd = "ein";
			userTypVa = (String)jsonObject.get("ein");
			orOdurBVo.setEin(userTypVa);
		} else if(jsonObject.get("rid")!=null){
			userTypCd = "rid";
			userTypVa = (String)jsonObject.get("rid");
			orOdurBVo.setRid(userTypVa);
		} else if(paramUserUid==null){
			LOGGER.error("ERP SSO fail - not valid user key / "+json);
			return false;
		}
		
		if(paramUserUid==null || checkPw){
			@SuppressWarnings("unchecked")
			List<OrOdurBVo> orUserBVoList = (List<OrOdurBVo>)commonDao.queryList(orOdurBVo);
			if(orUserBVoList==null || orUserBVoList.isEmpty()){
				// 사용자 없음
				LOGGER.error("ERP SSO fail - no user in OR_ODUR_B by "+userTypCd+":"+userTypVa + " / "+json);
				return false;
			} else if(orUserBVoList.size()>1){
				// 1명 이상의 사용자가 메핑됨
				LOGGER.warn("ERP SSO warn - "+orUserBVoList.size()+" users in OR_ODUR_B by "+userTypCd+":"+userTypVa + " / "+json);
				orOdurBVo = orUserBVoList.get(0);
			} else {
				orOdurBVo = orUserBVoList.get(0);
			}
		}
		
		// 사용자기본(OR_USER_B) 테이블 - 조회
		OrUserBVo orUserBVo = new OrUserBVo();
		if(paramUserUid!=null){
			orUserBVo.setUserUid(paramUserUid);
		} else {
			orUserBVo.setOdurUid(orOdurBVo.getOdurUid());
		}
		if(orOrgBVo!=null){
			orUserBVo.setOrgId(orOrgBVo.getOrgId());
		}
		orUserBVo.setOrderBy("ADU_TYP_CD");
		@SuppressWarnings("unchecked")
		List<OrUserBVo> orUserBVoList = (List<OrUserBVo>)commonDao.queryList(orUserBVo);
		if(orUserBVoList==null || orUserBVoList.isEmpty()){
			LOGGER.error("ERP SSO fail - no user in OR_USER_B - odurUid:"+orOdurBVo.getOdurUid()
					+(orOrgBVo==null ? "" : ", orgId:"+orOrgBVo.getOrgId())+" by "+userTypCd+":"+userTypVa
					+(orOrgBVo==null ? "" : ", "+deptTypCd+":"+deptTypVa) + " / "+json);
			return false;
		} else if(orUserBVoList.size()>1){
			LOGGER.warn("ERP SSO warn - "+orUserBVoList.size()+" users in OR_USER_B - odurUid:"+orOdurBVo.getOdurUid()
					+(orOrgBVo==null ? "" : ", orgId:"+orOrgBVo.getOrgId())+" by "+userTypCd+":"+userTypVa
					+(orOrgBVo==null ? "" : ", "+deptTypCd+":"+deptTypVa) + " / "+json);
			orUserBVo = orUserBVoList.get(0);
		} else {
			orUserBVo = orUserBVoList.get(0);
		}
		
		String userUid = orUserBVo.getUserUid();
		// [시스템 옵션] ERP 결재 연계 비밀번호 확인
		if(checkPw){
			
			// 세션과 동일 계정인지 확인 - 겸직 포함
			boolean userConfirmed = false;
			UserVo userVo = LoginSession.getUser(request);
			
			if(userVo != null){
				if(userVo.getUserUid().equals(userUid)) userConfirmed = true;
				if(!userConfirmed){
					String[][] adurs = userVo.getAdurs();
					if(adurs != null){
						for(int i=0; i<adurs.length; i++){
							if(userUid.equals(adurs[i][1])){
								userConfirmed = true;
								break;
							}
						}
					}
				}
			}
			
			// 세션이 없거나 동일인 아니면
			if(!userConfirmed && orOdurBVo != null){
				
				boolean pwConfirmed = false;
				String password = null;
				if(request.getParameter("secu") != null){
					try {
						JSONObject jsonPwObject = cryptoSvc.processRsa(request);
						password = (String)jsonPwObject.get("password");
					} catch(CmException e){
						//pt.login.fail.decrypt=복호화에 실패하였습니다.
						String message = messageProperties.getMessage("pt.login.fail.decrypt", request);
						request.getSession(true).setAttribute("ERO_SSO_MSG", message);
					}
				}
				
				if(password != null){
					
					// 로그인 패스워드 암호화
					String encPw = License.ins.encryptPw(password, orOdurBVo.getOdurUid());
					
					// 사용자비밀번호상세(OR_USER_PW_D) 테이블 - 조회
					OrUserPwDVo orUserPwDVo = new OrUserPwDVo();
					orUserPwDVo.setOdurUid(orOdurBVo.getOdurUid());
					orUserPwDVo.setPwTypCd("SYS");//SYS:시스템 비밀번호, APV:결재 비밀번호
					orUserPwDVo = (OrUserPwDVo)commonDao.queryVo(orUserPwDVo);
					
					if(encPw!=null && encPw.equals(orUserPwDVo.getPwEnc())){
						pwConfirmed = true;
					} else {
						//pt.login.noUserNoPw=비밀번호가 다르거나 사용자 정보를 확인 할 수 없습니다.
						String message = messageProperties.getMessage("pt.login.noUserNoPw", request);
						request.getSession(true).setAttribute("ERO_SSO_MSG", message);
					}
				}
				
				if(!pwConfirmed){
					request.getSession(true).setAttribute("ERO_SSO_LGIN_ID", orOdurBVo.getLginId());
					return false;
				}
			}
		}
		boolean result = super.createSession(request, response, userUid, langTypCd, orUserBVo);
		return result;
	}
	
	/** 루트 Exception 으로 변환 */
	private Throwable toRootException(Throwable throwable){
		Throwable parent = null;
		while((parent = throwable.getCause()) != null){
			throwable = parent;
		}
		return throwable;
	}
}
