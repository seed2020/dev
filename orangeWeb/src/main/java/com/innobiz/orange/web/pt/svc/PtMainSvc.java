package com.innobiz.orange.web.pt.svc;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.innobiz.orange.web.ap.svc.ApBxSvc;
import com.innobiz.orange.web.ap.svc.ApCmSvc;
import com.innobiz.orange.web.ap.vo.ApOngdBVo;
import com.innobiz.orange.web.cm.config.ServerConfig;
import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.StringUtil;
import com.innobiz.orange.web.em.svc.EmailSvc;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.SecuUtil;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.wc.svc.WcScdManagerSvc;

/** 포털 메인 서비스 */
@Service
public class PtMainSvc {
	
	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(PtMainSvc.class);
	
	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;
	
//	/** 포털 보안 서비스 */
//	@Autowired
//	private PtSecuSvc ptSecuSvc;
	
	/** 이메일 서비스 */
	@Resource(name = "emEmailSvc")
	private EmailSvc emailSvc;
	
	/** 일정 서비스 */
	@Autowired
	private WcScdManagerSvc wcScdManagerSvc;
	
	/** 결재 함 서비스 */
	@Autowired
	private ApBxSvc apBxSvc;
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
	/** 결재 공통 서비스 */
	@Autowired
	private ApCmSvc apCmSvc;
	
	/** 메인 건수 조회 */
	public void getMainCnt(HttpServletRequest request, UserVo userVo, Map<String,String> rsltMap, boolean shouldQueryApCount, ModelMap model) throws SQLException {
		
		// 시스템 정책 조회
		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
		
		// 이메일 건수
		String cntId = "topMailCnt";
		if(emailSvc.isInService() && sysPlocMap!=null && "Y".equals(sysPlocMap.get("mailEnable"))
				&& userVo.hasMnuGrpMdRidOf("MAIL") && !"U0000001".equals(userVo.getUserUid())){
			try{
				rsltMap.put(cntId, emailSvc.getEmailCnt(userVo.getOdurUid(), null).trim());
			} catch(Exception e){
				LOGGER.error("MAIL COUNT ERROR - userUid:"+userVo.getOdurUid()+" / "
						+ e.getClass().getCanonicalName() +": "+ e.getMessage());
				rsltMap.put(cntId, "0");
			}
			
			// 승인메일건수
			if("Y".equals(sysPlocMap.get("apvMailEnable"))){
				cntId = "topApvMailCnt";
				try{
					String returnString=emailSvc.getEmailApvCnt(userVo.getOdurUid(), null).trim();
					if(!"0".equals(returnString)) rsltMap.put(cntId, emailSvc.getEmailApvCnt(userVo.getOdurUid(), null).trim());
				} catch(Exception e){
					LOGGER.error("APV MAIL COUNT ERROR - userUid:"+userVo.getOdurUid()+" / "
							+ e.getClass().getCanonicalName() +": "+ e.getMessage());
				}
			}
			
		} else {
			rsltMap.put(cntId, "0");
		}
		
		// 결재 건수
		cntId = "topAppCnt";
		try{
			if(userVo.hasMnuGrpMdRidOf("AP")){
				if(shouldQueryApCount){
					
					Map<String, String> optConfigMap = apCmSvc.getOptConfigMap(null, userVo.getCompId());
					boolean refVwEnable = "Y".equals(optConfigMap.get("refVwEnable"));
					boolean recvBxEnable = "Y".equals(optConfigMap.get("docCntInRecvBx"));
					
					String[] apBxIds = {"waitBx","ongoBx","apvdBx","myBx","rejtBx","postApvdBx","drftBx","deptBx","refVwBx","recvBx"};
					
					Integer recodeCount = 0;
					String langTypCd = LoginSession.getLangTypCd(request);
					boolean hasAdmin = SecuUtil.hasAuth(request, "A", null);
					
					for(String bxId : apBxIds){
						
						if(!refVwEnable && "refVwBx".equals(bxId)){
							continue;
						}
						if(!recvBxEnable && "recvBx".equals(bxId)){
							continue;
						}
						
						recodeCount = 0;
						
						// 진행문서기본(AP_ONGD_B) 테이블
						ApOngdBVo apOngdBVo = new ApOngdBVo();
						apOngdBVo.setBxId(bxId);
						apOngdBVo.setQueryLang(langTypCd);
						
						// 함별 조회 조건 세팅
						boolean valid = apBxSvc.setApvBx(userVo, langTypCd, bxId, apOngdBVo, hasAdmin, model);
						if(valid){
							// 카운트 조회
							recodeCount = commonSvc.count(apOngdBVo);
						} else {
							LOGGER.error("AP COUNT ERROR - not vaild - bxId:"+bxId+" userUid:"+userVo.getUserUid());
						}
						rsltMap.put(bxId, String.valueOf(recodeCount));
						if(bxId.equals("waitBx")){
							rsltMap.put(cntId, String.valueOf(recodeCount));
						}
					}
					
					rsltMap.put("uid", userVo.getUserUid());
					
				} else {
					Integer recodeCount = 0;
					String langTypCd = LoginSession.getLangTypCd(request);
					String bxId = "waitBx";
					
					// 진행문서기본(AP_ONGD_B) 테이블
					ApOngdBVo apOngdBVo = new ApOngdBVo();
					apOngdBVo.setBxId(bxId);
					apOngdBVo.setQueryLang(langTypCd);
					
					boolean hasAdmin = SecuUtil.hasAuth(request, "A", null);
					// 함별 조회 조건 세팅
					boolean valid = apBxSvc.setApvBx(userVo, langTypCd, bxId, apOngdBVo, hasAdmin, model);
					if(valid){
						// 카운트 조회
						recodeCount = commonSvc.count(apOngdBVo);
					} else {
						LOGGER.error("AP COUNT ERROR - not vaild - userUid:"+userVo.getUserUid());
					}
					rsltMap.put(cntId, String.valueOf(recodeCount));
				}
			} else {
				rsltMap.put(cntId, "0");
			}
			
		} catch(Exception e){
			LOGGER.error("AP COUNT ERROR - userUid:"+userVo.getUserUid()+" - "
					+ e.getClass().getCanonicalName() +": "+ e.getMessage());
			rsltMap.put(cntId, "0");
		}
		
		// 일정 건수
		cntId = "topSchdlCnt";
		try{
			if(userVo.hasMnuGrpMdRidOf("WC")){
				rsltMap.put(cntId, wcScdManagerSvc.getSchdlCnt(userVo));
			} else {
				rsltMap.put(cntId, "0");
			}
		} catch(Exception e){
			LOGGER.error("WC COUNT ERROR - userUid:"+userVo.getUserUid()+" - "
					+ e.getClass().getCanonicalName() +": "+ e.getMessage());
			rsltMap.put(cntId, "0");
		}
		
		// 겸직 결재 건수
		cntId = "topAdditionalCnt";
		
		try{
			Integer recodeCount = 0, sum = 0;
			String langTypCd = LoginSession.getLangTypCd(request);
			String bxId = "waitBx";
			String userAuth;
			String addtionalUrl = null, addtionalFnc = null;
			
			// 진행문서기본(AP_ONGD_B) 테이블
			ApOngdBVo apOngdBVo;
			
			List<UserVo> additionalDutyVoList = userVo.getAdditionalDutyVoList();
			if(additionalDutyVoList!=null && userVo.hasMnuGrpMdRidOf("AP")){
				
				int addtionalSize = additionalDutyVoList.size();
				boolean hasAdmin, valid;
				for(UserVo additionalDutyVo : additionalDutyVoList){
					
					// 현재 사용자는 카운트 안함
					if(userVo.getUserUid().equals(additionalDutyVo.getUserUid())){
						continue;
					}
					
					apOngdBVo = new ApOngdBVo();
					apOngdBVo.setBxId(bxId);
					apOngdBVo.setQueryLang(langTypCd);
					
					// 관리자 권한 체크
					userAuth = (String)request.getAttribute("_AUTH");
					hasAdmin = SecuUtil.hasAuth(additionalDutyVo, userAuth, "A", null);
					
					// 함별 조회 조건 세팅
					valid = apBxSvc.setApvBx(additionalDutyVo, langTypCd, bxId, apOngdBVo, hasAdmin, model);
					if(valid){
						// 카운트 조회
						recodeCount = commonSvc.count(apOngdBVo);
						sum += recodeCount;
						if(addtionalSize==2){
							if(ServerConfig.IS_MOBILE){
								addtionalFnc = "$m.menu.switchUser(event, '"+additionalDutyVo.getUserUid()+"', 'waitBx');";
							} else {
								addtionalUrl = "/cm/login/processAdurSwitch.do?userUid="+additionalDutyVo.getUserUid()+"&destination=waitBx";
							}
						}
					} else {
						LOGGER.error("AP COUNT ERROR - not vaild - userUid:"+additionalDutyVo.getUserUid());
					}
				}
				
				rsltMap.put(cntId, String.valueOf(sum));
				if(ServerConfig.IS_MOBILE){
					if(addtionalFnc!=null) {
						rsltMap.put("addtionalFnc", addtionalFnc);
					} else {
						rsltMap.put("addtionalFnc", "openAdditionalApWaitBx();");
					}
				} else {
					if(addtionalUrl!=null) {
						rsltMap.put("addtionalUrl", addtionalUrl);
					} else {
						rsltMap.put("addtionalUrl", "javascript:openAdditionalApWaitBx();");
					}
				}
			}
		} catch(Exception e){
			LOGGER.error("AP COUNT for Additional duty user ERROR - userUid:"+userVo.getUserUid()+" - "
					+ e.getClass().getCanonicalName() +": "+ e.getMessage());
			rsltMap.put(cntId, "0");
		}
		
	}
	
	/** 메인 결재 겸직 사용자 선택 팝업 */
	public void getMainApCntPop(HttpServletRequest request, UserVo userVo, ModelMap model) throws SQLException {
		
		List<Map<String, String>> apvMapList = new ArrayList<Map<String, String>>();
		
		try{
			String langTypCd = LoginSession.getLangTypCd(request);
			String bxId = "waitBx";
			String userAuth;
			
			Integer recodeCount;
			Map<String, String> apvMap;
			
			// 진행문서기본(AP_ONGD_B) 테이블
			ApOngdBVo apOngdBVo;
			
			List<UserVo> additionalDutyVoList = userVo.getAdditionalDutyVoList();
			if(additionalDutyVoList!=null){

				boolean hasAdmin, valid;
				for(UserVo additionalDutyVo : additionalDutyVoList){
					
					// 현재 사용자는 카운트 안함
					if(userVo.getUserUid().equals(additionalDutyVo.getUserUid())){
						continue;
					}
					
					apOngdBVo = new ApOngdBVo();
					apOngdBVo.setBxId(bxId);
					apOngdBVo.setQueryLang(langTypCd);
					
					// 관리자 권한 체크
					userAuth = (String)request.getAttribute("_AUTH");
					hasAdmin = SecuUtil.hasAuth(additionalDutyVo, userAuth, "A", null);
					
					// 함별 조회 조건 세팅
					valid = apBxSvc.setApvBx(additionalDutyVo, langTypCd, bxId, apOngdBVo, hasAdmin, model);
					if(valid){
						// 카운트 조회
						recodeCount = commonSvc.count(apOngdBVo);
						apvMap = new HashMap<String, String>();
						// 부서명
						apvMap.put("deptNm", additionalDutyVo.getDeptNm());
						apvMap.put("count", recodeCount.toString());
						if(ServerConfig.IS_MOBILE){
							apvMap.put("userUid", additionalDutyVo.getUserUid());
						} else {
							apvMap.put("url", "/cm/login/processAdurSwitch.do?userUid="+additionalDutyVo.getUserUid()+"&destination=waitBx");
						}
						
						apvMapList.add(apvMap);
					} else {
						LOGGER.error("AP COUNT ERROR - not vaild - userUid:"+additionalDutyVo.getUserUid());
					}
				}
				
			}
		} catch(Exception e){
			LOGGER.error("AP COUNT for Additional duty user ERROR - userUid:"+userVo.getUserUid()+" - "
					+ e.getClass().getCanonicalName() +": "+ e.getMessage());
		}
		
		if(!apvMapList.isEmpty()){
			model.put("apvMapList", apvMapList);
		}
	}
	
	/** 메인 건수 조회[건수,링크Url] */
	public void setMainTotalCnt(HttpServletRequest request, UserVo userVo, ModelMap model) throws Exception{
		String mailUrl = "/cm/zmailPop.do";
		mailUrl+= mailUrl.indexOf("?") > -1 ? "&" : "?";
		mailUrl+= "cmd=mail_link_2";
		//새메일[건수,링크Url]
		request.setAttribute("topMailAtts", new String[]{emailSvc.getEmailCnt(userVo.getUserUid(), null),mailUrl});
		
		//String appUrl = PtSecuSvc.ins.toAuthMenuUrl(userVo, "/ap/box/listWaitBx.do", null);
		String appUrl = PtSecuSvc.ins.toAuthMenuUrl(userVo, "/ap/box/listApvBx.do?bxId=waitBx", null);
		
		// 사용자 정보
		String langTypCd = LoginSession.getLangTypCd(request);
		// 진행문서기본(AP_ONGD_B) 테이블
		ApOngdBVo apOngdBVo = new ApOngdBVo();
		apOngdBVo.setBxId("waitBx");
		
		boolean hasAdmin = SecuUtil.hasAuth(request, "A", null);
		// 함별 조회 조건 세팅
		boolean valid = apBxSvc.setApvBx(userVo, langTypCd, apOngdBVo.getBxId(), apOngdBVo, hasAdmin, model);
		
		Integer recodeCount = 0;
		if(valid){
			// 카운트 조회
			recodeCount = commonDao.count(apOngdBVo);
		}
		//결재[건수,링크Url]
		request.setAttribute("topAppAtts", new String[]{String.valueOf(recodeCount.intValue()),appUrl});
		
		//일정[건수,링크Url]
		String schdlUrl = PtSecuSvc.ins.toAuthMenuUrl(userVo, "/wc/listMySchdl.do", null);
		schdlUrl+= schdlUrl.indexOf("?") > -1 ? "&" : "?";
		schdlUrl+= "schdlStartDt="+StringUtil.getCurrYmd();
		schdlUrl+= "&schdlEndDt="+StringUtil.getCurrYmd();
		request.setAttribute("topSchdlAtts", new String[]{wcScdManagerSvc.getSchdlCnt(userVo),schdlUrl});
	}
	
}
