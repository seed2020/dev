package com.innobiz.orange.web.pt.svc;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.innobiz.orange.web.cm.config.CacheConfig;
import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.SessionUtil;
import com.innobiz.orange.web.cm.utils.StringUtil;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.pt.secu.IpChecker;
import com.innobiz.orange.web.pt.secu.LastSessionChecker;
import com.innobiz.orange.web.pt.utils.PtConstant;
import com.innobiz.orange.web.pt.vo.PtCdBVo;
import com.innobiz.orange.web.pt.vo.PtLginImgDVo;
import com.innobiz.orange.web.pt.vo.PtLstSetupDVo;
import com.innobiz.orange.web.pt.vo.PtSysSetupDVo;
import com.innobiz.orange.web.pt.vo.PtTermSetupDVo;

/** 시스템설정 서비스 */
@Service
public class PtSysSvc {

	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(PtSysSvc.class);

	/** 캐쉬 서비스 */
	@Autowired
	private PtCacheSvc ptCacheSvc;

	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;

	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;

	/** 포털 공통 서비스 */
	@Autowired
	private PtCmSvc ptCmSvc;

	/** 캐쉬 만료 처리용 서비스 */
	@Autowired
	private PtCacheExpireSvc ptCacheExpireSvc;
	
	/** 메세지 */
	@Autowired
    private MessageProperties messageProperties;
	
	/** IP 체크용 객체 - IP 조회 및 정책 적용 */
	@Autowired
	private IpChecker ipChecker;
	
	/** 관리자 비밀번호가 세팅 되었는지 여부 */
	private boolean adminPw = false;
	
	/** 시스템프로퍼티상세 맵 조회 */
	public Map<String, String> getSysSetupMap(String setupClsId, boolean useCache) throws SQLException{
		if(useCache){
			@SuppressWarnings("unchecked")
			Map<String, String> map = (Map<String, String>)ptCacheSvc.getCache(CacheConfig.SYS_SETUP, "ko", setupClsId, 600);
			if(map!=null) return map;
		}
		
		Map<String, String> returnMap = new HashMap<String, String>();
		
		// 시스템설정상세(PT_SYS_SETUP_D) 테이블
		PtSysSetupDVo ptSysSetupDVo = new PtSysSetupDVo();
		ptSysSetupDVo.setSetupClsId(setupClsId);
		
		@SuppressWarnings("unchecked")
		List<PtSysSetupDVo> ptSysSetupDVoList = (List<PtSysSetupDVo>)commonDao.queryList(ptSysSetupDVo);
		
		ptSysSetupDVo = null;
		if(ptSysSetupDVoList!=null){
			for(PtSysSetupDVo storedPtSysSetupDVo: ptSysSetupDVoList){
				if(ptSysSetupDVo==null) ptSysSetupDVo = storedPtSysSetupDVo;
				returnMap.put(storedPtSysSetupDVo.getSetupId(), storedPtSysSetupDVo.getSetupVa());
			}
		}
		// 중복로그인 방지 - 방지가 아니면, 클리어함
		if(setupClsId.equals(PtConstant.PT_SYS_PLOC)){
			if(!"Y".equals(returnMap.get("blockDupLogin"))){
				LastSessionChecker.clearLastSession();
			}
		}
		if(useCache){
			ptCacheSvc.setCache(CacheConfig.SYS_SETUP, "ko", setupClsId, returnMap);
		}
		
		return returnMap;
	}
	
	/** 시스템프로퍼티상세 Integer 맵 조회 */
	public Map<String, Integer> getSysSetupIntMap(String setupClsId, boolean useCache) throws SQLException{
		if(useCache){
			@SuppressWarnings("unchecked")
			Map<String, Integer> map = (Map<String, Integer>)ptCacheSvc.getCache(CacheConfig.SYS_SETUP, "ko", setupClsId, 100);
			if(map!=null) return map;
		}
		
		Map<String, Integer> returnMap = new HashMap<String, Integer>();
		
		// 시스템설정상세(PT_SYS_SETUP_D) 테이블
		PtSysSetupDVo ptSysSetupDVo = new PtSysSetupDVo();
		ptSysSetupDVo.setSetupClsId(setupClsId);
		
		@SuppressWarnings("unchecked")
		List<PtSysSetupDVo> ptSysSetupDVoList = (List<PtSysSetupDVo>)commonDao.queryList(ptSysSetupDVo);
		
		ptSysSetupDVo = null;
		if(ptSysSetupDVoList!=null){
			for(PtSysSetupDVo storedPtSysSetupDVo: ptSysSetupDVoList){
				if(ptSysSetupDVo==null) ptSysSetupDVo = storedPtSysSetupDVo;
				try{
					returnMap.put(storedPtSysSetupDVo.getSetupId(),
							Integer.parseInt(storedPtSysSetupDVo.getSetupVa()));
				} catch(Exception ignore){}
			}
		}
		if(useCache){
			ptCacheSvc.setCache(CacheConfig.SYS_SETUP, "ko", setupClsId, returnMap);
		}
		
		return returnMap;
	}
	
//	/** 시스템프로퍼티상세 제거 */
//	public void removeSysSetupMap(String setupClsId) throws SQLException{
//		ptCacheSvc.removeCache(CacheConfig.SYS_SETUP, "ko", setupClsId);
//	}
	
	/** 용어 맵 조회 */
	public Map<String, String> getTermMap(String setupClsId, String langTypCd) throws SQLException{
		
		@SuppressWarnings("unchecked")
		Map<String, String> map = (Map<String, String>)ptCacheSvc.getCache(CacheConfig.TERMS, langTypCd, setupClsId, 100);
		if(map!=null) return map;
		
		Map<String, String> returnMap = new HashMap<String, String>();
		
		// 용어설정상세(PT_TERM_SETUP_D) 테이블
		PtTermSetupDVo ptTermSetupDVo = new PtTermSetupDVo();
		ptTermSetupDVo.setSetupClsId(setupClsId);
		ptTermSetupDVo.setLangTypCd(langTypCd);
		
		@SuppressWarnings("unchecked")
		List<PtTermSetupDVo> ptTermSetupDVoList = (List<PtTermSetupDVo>)commonDao.queryList(ptTermSetupDVo);
		
		if(ptTermSetupDVoList!=null){
			for(PtTermSetupDVo storedPtTermSetupDVo: ptTermSetupDVoList){
				returnMap.put(storedPtTermSetupDVo.getSetupId(), storedPtTermSetupDVo.getTermVa());
			}
		}
		
		ptCacheSvc.setCache(CacheConfig.TERMS, langTypCd, setupClsId, returnMap);
		
		return returnMap;
	}
	
	/** 용어설정 조회 - 용어설정이 없으면 메세지 프로퍼티에서 읽음 */
	public String getTerm(String termId, String langTypCd) throws SQLException{
		int p = termId.lastIndexOf('.');
		if(p<0) return null;
		Map<String, String> termMap = getTermMap(termId.substring(0, p), langTypCd);
		String termVa = termMap.get(termId.substring(p+1));
		if(termVa==null){
			termVa = messageProperties.getMessage(termId, SessionUtil.toLocale(langTypCd));
		}
		return termVa;
	}
	
	/** 시스템 프로퍼티 저장 내용을 QueryQueue에 저장 */
	public void setSysSetup(String setupClsId, QueryQueue queryQueue, boolean withoutPrefix, HttpServletRequest request) throws SQLException{
		
		Enumeration<String> enums = request.getParameterNames();
		String key;//, currDt = commonDao.querySysdate(new CommonVoImpl("YYYY-MM-DD"));
		int prefixLen = setupClsId==null ? 0 : setupClsId.length()+1;
		
		boolean deletePrevious = false;
		PtSysSetupDVo ptSysSetupDVo;
		String setupId;
		
		while(enums.hasMoreElements()){
			key = enums.nextElement();
			if(key!=null && (withoutPrefix || key.startsWith(setupClsId)) && !key.endsWith("SortOrdr")){
				
				if(!deletePrevious){
					// 시스템설정상세(PT_SYS_SETUP_D) 테이블 - 기존 데이터 삭제
					ptSysSetupDVo = new PtSysSetupDVo();
					ptSysSetupDVo.setSetupClsId(setupClsId);
					queryQueue.delete(ptSysSetupDVo);
					deletePrevious = true;
				}
				
				setupId = withoutPrefix ? key : key.substring(prefixLen);
				// 언더바(_)로 시작하면 저장 안함
				if(setupId.isEmpty() || setupId.charAt(0)=='_') continue;
				if(withoutPrefix && "menuId".equals(setupId)) continue;
				
				// 시스템설정상세(PT_SYS_SETUP_D) 테이블 - INSERT
				ptSysSetupDVo = new PtSysSetupDVo();
				ptSysSetupDVo.setSetupClsId(setupClsId);
				ptSysSetupDVo.setSetupId(setupId);
				ptSysSetupDVo.setSetupVa(request.getParameter(key));
				ptSysSetupDVo.setSortOrdr(request.getParameter(key+"SortOrdr"));
				queryQueue.insert(ptSysSetupDVo);
			}
		}
	}
	
	/** 패스워드 정책 조회  */
	public Map<String, String> getPwPolc(String compId) throws SQLException {
		
		Map<String, String> pwPolc = getSysSetupMap(PtConstant.PT_PW_PLOC+compId, true);
		if(pwPolc==null) pwPolc = new HashMap<String, String>();
		if(pwPolc.isEmpty()){
			// 설정된 정책이 없으면 기본 정책 리턴
			pwPolc.put("polcUseYn", "N");		//정책사용여부
			pwPolc.put("updatePeriod", "30");	//변경주기
			pwPolc.put("maxLength", "20");		//최대길이
			pwPolc.put("minLength", "6");		//최소길이
			pwPolc.put("numberMandatoryYn", "Y");		//숫자필수여부
			pwPolc.put("specailCharMandatoryYn", "N");	//특수기호필수여부
			return pwPolc;
		}
		return pwPolc;
	}
	
	/** 비밀번호 찾기 - 발송자 정보 조회  */
	public Map<String, String> getLostPwPolc() throws SQLException {
		Map<String, String> lostPwPolc = getSysSetupMap(PtConstant.PT_LOST_PW, true);
		if(lostPwPolc==null) lostPwPolc = new HashMap<String, String>();
		return lostPwPolc;
	}
	
	/** 패스워드 변경 정책에 위반 되었는지 리턴 */
	public boolean isPwPlocViolated(String lastChgDt, String compId){
		String nextPwChgDt = getNextChgPwDt(lastChgDt, compId, 0);
		if(nextPwChgDt==null) return true;
		
		if(nextPwChgDt.compareTo(StringUtil.getCurrYmd()) < 0){
			return true;
		} else {
			return false;
		}
//		if(lastChgDt==null || lastChgDt.isEmpty()) return true;
//		int updatePeriod = 30;
//		try{
//			updatePeriod = Integer.parseInt(getPwPolc(compId).get("updatePeriod"));
//		} catch(Exception e){}
//		
//		if(StringUtil.addDate(lastChgDt, updatePeriod).compareTo(StringUtil.getCurrYmd()) < 0){
//			return true;
//		} else {
//			return false;
//		}
	}
	
	/** 비밀번호 변경일 */
	public String getNextChgPwDt(String lastChgDt, String compId, int add){
		if(lastChgDt==null || lastChgDt.isEmpty()) return null;
		int updatePeriod = 30;
		try{
			updatePeriod = Integer.parseInt(getPwPolc(compId).get("updatePeriod"));
		} catch(Exception e){}
		return StringUtil.addDate(lastChgDt, updatePeriod + add);
	}
	
	/** IP 보안 정책 조회  */
	public Map<String, String> getSecuPolc() throws SQLException {
		Map<String, String> secuPolc = getSysSetupMap(PtConstant.PT_SECU_PLOC, true);
		if(secuPolc==null) secuPolc = new HashMap<String, String>();
		if(secuPolc.isEmpty()){
			// 설정된 정책이 없으면 기본 정책 리턴
			secuPolc.put("lginIpPolc", "N");	// 로그인 IP 정책 사용여부
			secuPolc.put("chkSesnIp", "Y");		// 세션 IP 정책  사용여부
			secuPolc.put("secuCookie", "N");	// 보안쿠키  사용여부
			secuPolc.put("headers", ipChecker.getIpHeader());
			return secuPolc;
		}
		return secuPolc;
	}
	
	/** 해외 IP 정책 조회  */
	public Map<String, String> getForeignIpBlockingPloc(boolean forMobile) throws SQLException {
		Map<String, String> foreignIpPloc = getSysSetupMap(forMobile ? PtConstant.MB_FOREIGN_IP_PLOC : PtConstant.PT_FOREIGN_IP_PLOC, true);
		if(foreignIpPloc==null) foreignIpPloc = new HashMap<String, String>();
		if(foreignIpPloc.isEmpty()){
			foreignIpPloc.put("chinese", "block");
			foreignIpPloc.put("foreign", "none");
			return foreignIpPloc;
		}
		return foreignIpPloc;
	}
	
	/** IP 정책 IpChecker 에 반영 */
	public void setIpChecker() throws SQLException{
		Map<String, String> secuPolc = getSecuPolc();
		String lginIpPolc = secuPolc.get("lginIpPolc");
		String ipRange    = secuPolc.get("ipRange");
		String chkSesnIp  = secuPolc.get("chkSesnIp");
		// HTTP 해더 설정
		ipChecker.setIpHeader(secuPolc.get("headers"));
		
		int seq = 0;
		String va, prefix = "lginIpRange";
		
		List<String> lginIpRangeList = new ArrayList<String>();
		while(true){
			va = secuPolc.get(prefix+(++seq));
			if(va==null) break;
			va = va.trim();
			if(!va.isEmpty()) lginIpRangeList.add(va);
		}
		
		seq = 0;
		prefix = "sessionIpExcp";
		List<String> sessionIpExcpList = new ArrayList<String>();
		while(true){
			va = secuPolc.get(prefix+(++seq));
			if(va==null) break;
			if(!va.isEmpty()) sessionIpExcpList.add(va);
		}
		
		// 로그인 정책 설정
		ipChecker.setLginPolc(lginIpPolc, ipRange, lginIpRangeList);
		
		//세션 IP 체크 설정 세팅
		ipChecker.setSesnIp(chkSesnIp, sessionIpExcpList);
	}
	/** 포틀릿 설정 조회  */
	public Map<String, String> getPltPolc() throws SQLException {
		
		Map<String, String> pltPolc = getSysSetupMap(PtConstant.PT_PLT_PLOC, false);
		if(pltPolc==null) pltPolc = new HashMap<String, String>();
		if(pltPolc.isEmpty()){
			// 설정된 정책이 없으면 기본 정책 리턴
			pltPolc.put("maxSetupCnt", "20");		//최대설정갯수
			pltPolc.put("defaultWidth", "400");		//기본넓이
			pltPolc.put("defaultHeight", "245");	//기본높이
			pltPolc.put("freeSetupMovePx", "4");	//Free설정이동픽셀
			return pltPolc;
		}
		return pltPolc;
	}
	
	/** 레이아웃 설정 조회  */
	public Map<String, String> getLayoutSetup() throws SQLException {
		Map<String, String> layout = getSysSetupMap(PtConstant.PT_LOUT_SETUP, true);
		if(layout==null) layout = new HashMap<String, String>();
		if(layout.isEmpty()){
			// 설정된 정책이 없으면 기본 정책 리턴
			layout.put("bascLoutUseYn", "Y");	// 기본레이아웃사용여부
			layout.put("icoLoutUseYn", "Y");	// 아이콘레이아웃사용여부
			layout.put("subMnuOption", "N");	// 서브 메뉴 옵션 - M:독립된 메뉴로 사용, S:메인 메뉴의 하위 메뉴로 사용, N:서브 메뉴 사용 안함
			return layout;
		}
		return layout;
	}
	
	/** 첨부 용량 제한 맵  */
	public Map<String, Integer> getAttachSizeMap(String langTypCd, String compId) throws SQLException {
		Map<String, Integer> fileSizeMap = getSysSetupIntMap(PtConstant.PT_ATTC_SIZE+compId, true);
		if(fileSizeMap==null || fileSizeMap.isEmpty()){
			fileSizeMap = new HashMap<String, Integer>();
			// 설정된 정책이 없으면 기본 정책 리턴
			List<PtCdBVo> pageRecSetupCdList = ptCmSvc.getCdList("PAGE_REC_SETUP_CD", langTypCd, "Y");
			if(pageRecSetupCdList!=null){
				for(PtCdBVo ptCdBVo : pageRecSetupCdList){
					fileSizeMap.put(ptCdBVo.getCd(), 20);
				}
			}
			ptCacheSvc.setCache(CacheConfig.SYS_SETUP, "ko", PtConstant.PT_ATTC_SIZE+compId, fileSizeMap);
		}
		return fileSizeMap;
	}
	
	/** 본문 용량 제한 맵  */
	public Map<String, Integer> getBodySizeMap(String langTypCd, String compId) throws SQLException {
		Map<String, Integer> bodySizeMap = getSysSetupIntMap(PtConstant.PT_BODY_SIZE+compId, true);
		if(bodySizeMap==null || bodySizeMap.isEmpty()){
			bodySizeMap = new HashMap<String, Integer>();
			// 설정된 정책이 없으면 기본 정책 리턴
			List<PtCdBVo> pageRecSetupCdList = ptCmSvc.getCdList("PAGE_REC_SETUP_CD", langTypCd, "Y");
			if(pageRecSetupCdList!=null){
				for(PtCdBVo ptCdBVo : pageRecSetupCdList){
					bodySizeMap.put(ptCdBVo.getCd(), 300);
				}
			}
			ptCacheSvc.setCache(CacheConfig.SYS_SETUP, "ko", PtConstant.PT_BODY_SIZE+compId, bodySizeMap);
		}
		return bodySizeMap;
	}
	
	/** 레이아웃 설정 조회  */
	public Map<String, String> getDefaultLayout() throws SQLException {
		Map<String, String> layout = getSysSetupMap(PtConstant.PT_LOUT_DEFLT, true);
		if(layout==null) layout = new HashMap<String, String>();
		if(layout.isEmpty()){
			// 설정된 정책이 없으면 기본 정책 리턴
			layout.put("loutCatId", "I");	// 디폴트레이아웃 - 아이콘레이아웃
			layout.put("skin", "blue");	// 디폴트스킨 - 블루
			return layout;
		}
		return layout;
	}
	
	/** 스킨 이미지 설정 조회  */
	public Map<String, String> getSkinImage(String compId, String cxPth) throws SQLException {
		Map<String, String> layout = getSysSetupMap(PtConstant.PT_SKIN_IMG+compId, true);
		if(layout==null) layout = new HashMap<String, String>();
		if(layout.isEmpty()){
			// 설정된 정책이 없으면 기본 정책 리턴
			if(cxPth==null) cxPth = "";
			for(String color : new String[]{"blue","green","pink","yellow"}){
								for(String lout : new String[]{"Icon","Basc"}){
					layout.put(color+lout+"Logo",
							cxPth+"/images/"+color+"/header_"+(lout.equals("Icon") ? "icon" : "text") + "/logo.png");
					if(lout.equals("Basc")){
						layout.put(color+lout+"Bg", cxPth+"/images/cm/bg_a.png");
					}
				}
			}
			return layout;
		}
		return layout;
	}
	
	/** 리스트 설정 조회(캐쉬) */
	@SuppressWarnings("unchecked")
	public List<PtLstSetupDVo> getPtLstSetupDVoList(String lstSetupMetaId) throws SQLException{
		List<PtLstSetupDVo> ptLstSetupDVoList = (List<PtLstSetupDVo>)ptCacheSvc.getCache(CacheConfig.LST_ENV, "ko", lstSetupMetaId, 60);
		if(ptLstSetupDVoList!=null) return ptLstSetupDVoList;
		
		// 설정 조회
		PtLstSetupDVo ptLstSetupDVo = new PtLstSetupDVo();
		ptLstSetupDVo.setLstSetupMetaId(lstSetupMetaId);
		ptLstSetupDVo.setDftVaYn("N");
		ptLstSetupDVo.setOrderBy("SORT_ORDR");
		ptLstSetupDVoList = (List<PtLstSetupDVo>)commonDao.queryList(ptLstSetupDVo);
		
		// 없으면 기본값 조회
		if(ptLstSetupDVoList==null || ptLstSetupDVoList.isEmpty()){
			ptLstSetupDVo = new PtLstSetupDVo();
			ptLstSetupDVo.setLstSetupMetaId(lstSetupMetaId);
			ptLstSetupDVo.setDftVaYn("Y");
			ptLstSetupDVoList = (List<PtLstSetupDVo>)commonDao.queryList(ptLstSetupDVo);
		}
		
		if(ptLstSetupDVoList==null){
			ptLstSetupDVoList = new ArrayList<PtLstSetupDVo>();
		}
		
		ptCacheSvc.setCache(CacheConfig.LST_ENV, "ko", lstSetupMetaId, ptLstSetupDVoList);
		return ptLstSetupDVoList;
	}
	
	// setEngSvrSetup.jsp 로 이동
//	/** 서버 설정 목록 - 메일연계호출, 메신저연계호출, 메일계정생성 도메인, 웹 도메인 */
//	public String[] getSvrEnvIds(){
//		return new String[]{ "mailCall", "smtpCall", "messengerCall", "webDomain", "imgDomain", "mobileDomain", "erpCall", "pushPort" };
//	}
	
	/** 서버 설정 목록 조회 - 메일연계호출, 메신저연계호출, 메일계정생성 도메인, 웹 도메인 */
	public Map<String, String> getSvrEnvMap() throws SQLException {
		Map<String, String> svrEnvMap = getSysSetupMap(PtConstant.PT_SVR_ENV, true);
		if(svrEnvMap==null) svrEnvMap = new HashMap<String, String>();
		if(svrEnvMap.isEmpty()){
			svrEnvMap.put("mailCall", "mail.gworange.com:4040");
//			svrEnvMap.put("mailSmtpPort", "25");// 메일 SMTP PORT
			svrEnvMap.put("messengerCall", "ep.gworange.com:12555");
//			svrEnvMap.put("mailDomain", "gworange.com");
			svrEnvMap.put("webDomain", "ep.gworange.com");
			//svrEnvMap.put("mobileDomain", "m.gworange.com");
			svrEnvMap.put("imgDomain", "ep.gworange.com");
			svrEnvMap.put("pushPort", "7080");
			svrEnvMap.put("default", "Y");
		}
		return svrEnvMap;
	}
	
	/** 시스템 정책 조회 */
	public Map<String, String> getSysPlocMap() throws SQLException {
		Map<String, String> sysPlocMap = getSysSetupMap(PtConstant.PT_SYS_PLOC, true);
		if(sysPlocMap==null) sysPlocMap = new HashMap<String, String>();
		if(sysPlocMap.isEmpty()){
			sysPlocMap.put("mailEnable", "Y");//메일 사용여부
			sysPlocMap.put("messengerEnable", "Y");//메신저 사용여부
			
			sysPlocMap.put("mobileEnable", "Y");//모바일 사용여부
			sysPlocMap.put("mobilePushEnable", "Y");//모바일 메세지 사용여부
			
			// Y 가 아니면 N
//			sysPlocMap.put("erpEnable", "N");//ERP 연계 사용여부
//			sysPlocMap.put("licenseByCompEnable", "N");//회사별 라이센스 사용여부
//			sysPlocMap.put("codeByCompEnable", "N");//회사별 조직코드 사용여부
//			sysPlocMap.put("seculByCompEnable", "N");//회사별 보안등급코드 사용여부
		}
		return sysPlocMap;
	}
	
	/** 관리자 비밀번호가 설정 되었는지 여부 */
	public boolean hasAdminPw(){
		if(adminPw) return true;
		try {
			Map<String, String> adminPwMap = getSysSetupMap(PtConstant.PT_ADMIN_PW, false);
			String encTxt = adminPwMap.get("value");
			adminPw = encTxt!=null && !encTxt.isEmpty();
			return adminPw;
		} catch (SQLException e) { e.printStackTrace(); }
		return true;
	}
	
	/** 어권별로 설정된 폰트 맵 조회  */
	public Map<String, String> getFontByLangMap() throws SQLException {
		Map<String, String> fontMap = getSysSetupMap(PtConstant.PT_LANG_FONT, true);
		if(fontMap==null) fontMap = new HashMap<String, String>();
		if(fontMap.isEmpty()){
			fontMap.put("ko", "바탕체|굴림체|돋움체|궁서체|Arial,geneva,sans-serif|Geneva,arial,sans-serif|Courier,monospace");
			return fontMap;
		}
		return fontMap;
	}
	
	/** 어권별 폰트 조회 */
	public String[] getFontFamilyByLang(String langTypCd) throws SQLException{
		Map<String, String> fontMap = getFontByLangMap();
		String fonts = fontMap.get(langTypCd);
		if(fonts!=null && !fonts.isEmpty()){
			return fonts.split("\\|");
		}
		return null;
	}

	/** 폰트 사이즈 배열 */
	private static String[] fonts = "7.5pt,8pt,9pt,10pt,11pt,12pt,14pt,16pt,18pt,20pt,22pt,26pt,28pt,36pt,48pt,72pt".split(",");
	/** 폰트 사이즈 배열 조회  */
	public String[] getFontSizeArray() throws SQLException {
		return fonts;
	}
	
	/** 로그인 이미지 조회 */
	public Map<String, String> getLoginImage() throws SQLException {
		Map<String, String> imageMap = getSysSetupMap(PtConstant.PT_LGIN_IMG, true);
		if(imageMap == null) imageMap = new HashMap<String, String>();
		
		if(imageMap.isEmpty()){
			
			// 현재일
			String ymd = StringUtil.getCurrYmd();
			
			// 기간설정 조회
			PtLginImgDVo ptLginImgDVo = new PtLginImgDVo();
			ptLginImgDVo.setStrtYmd(ymd);
			ptLginImgDVo.setEndYmd(ymd);
			ptLginImgDVo.setUseYn("Y");
			ptLginImgDVo.setOrderBy("LGIN_IMG_ID DESC");
			@SuppressWarnings("unchecked")
			List<PtLginImgDVo> ptLginImgDVoList = (List<PtLginImgDVo>)commonDao.queryList(ptLginImgDVo);
			
			// 기간설정이 있으면 - 첫번째 설정 리턴(마지막 등록한 것)
			if(ptLginImgDVoList != null && !ptLginImgDVoList.isEmpty()){
				ptLginImgDVo = ptLginImgDVoList.get(0);
				if(ptLginImgDVo.getLogoImgPath() != null){
					imageMap.put("logoImgPath", ptLginImgDVo.getLogoImgPath());
				}
				imageMap.put("bgImgPath", ptLginImgDVo.getBgImgPath());
				imageMap.put("topPx", ptLginImgDVo.getTopPx());
				imageMap.put("leftPx", ptLginImgDVo.getLeftPx());
				return imageMap;
			}
			
			// 디폴트 설정 조회
			ptLginImgDVo = new PtLginImgDVo();
			ptLginImgDVo.setUseYn("Y");
			ptLginImgDVo.setDftImgYn("Y");
			ptLginImgDVo = (PtLginImgDVo)commonDao.queryVo(ptLginImgDVo);
			// 디폴트 설정이 있으면 - 첫번째 설정 리턴(마지막 등록한 것)
			if(ptLginImgDVo != null){
				if(ptLginImgDVo.getLogoImgPath() != null){
					imageMap.put("logoImgPath", ptLginImgDVo.getLogoImgPath());
				}
				imageMap.put("bgImgPath", ptLginImgDVo.getBgImgPath());
				imageMap.put("topPx", ptLginImgDVo.getTopPx());
				imageMap.put("leftPx", ptLginImgDVo.getLeftPx());
				return imageMap;
			}
			
			// 디폴트 로그인 이미지 리턴
			imageMap.put("logoImgPath", "/images/login/logo.png");
			imageMap.put("bgImgPath", "/images/login/bg_a.png");
			imageMap.put("topPx", "0");
			imageMap.put("leftPx", "0");
			
			return imageMap;
		}
		return imageMap;
	}
	
	/** 모바일 로그인 이미지 조회 */
	public Map<String, String> getMobileLogin(String langTypCd) throws SQLException {
		Map<String, String> mobLginMap = getSysSetupMap(PtConstant.MB_MOB_LGIN, true);
		if(mobLginMap==null) mobLginMap = new HashMap<String, String>();
		
		if(mobLginMap.isEmpty()){
			mobLginMap.put("logoImgPath", "/images/blue/header_logo2.png");
			Map<String, String> orTermMap = getTermMap("or.term", langTypCd);
			mobLginMap.put("lginTitle", orTermMap.get("siteName"));
			mobLginMap.put("lginTitleColor", "#ed5f00");
		}
		return mobLginMap;
	}
	
	/** 첨부파일 확장자 맵  */
	public Map<String, String> getAttachExtMap(String compId) throws SQLException {
		return getSysSetupMap(PtConstant.PT_ATTC_EXT+compId, true);
	}
	
	/** 첨부파일 확장자 맵 세팅  */
	public void setAttachExtMap(ModelMap model, String compId, String module) throws SQLException {
		Map<String, String> fileExtMap = getSysSetupMap(PtConstant.PT_ATTC_EXT+compId, true);
		if(fileExtMap != null && fileExtMap.containsKey(module+".useYn") && "Y".equals(fileExtMap.get(module+".useYn")) && fileExtMap.get(module+".ext")!=null){
			model.put("exts", fileExtMap.get(module+".ext").trim());
			model.put("extsTyp", fileExtMap.get("extTyp"));
		}
	}
	
	/** 빈번한 로그인 사용자 조회 */
	public String getFrequentRefreshUser(String compId){
		try {
			Map<String, String> frequentRefresh = getSysSetupMap(PtConstant.PT_FREQUENT_REFRESH, true);
			return frequentRefresh.get(compId);
		} catch (SQLException e) { e.printStackTrace(); }
		return null;
	}
	
	/** 결재 본문 보기 사용자 조회 */
	public String getApViewBodyUser(String compId){
		try {
			Map<String, String> frequentRefresh = getSysSetupMap(PtConstant.AP_BODY_VIEW_USER, true);
			return frequentRefresh.get(compId);
		} catch (SQLException e) { e.printStackTrace(); }
		return null;
	}
	
	/** 비밀번호 정책예외 조회 */
	public List<String> getPtPwExceptionUser(){
		try {
			String setupClsId = "PT_PW_EXCEPTION";
			@SuppressWarnings("unchecked")
			List<String> list = (List<String>)ptCacheSvc.getCache(CacheConfig.SYS_SETUP, "ko", setupClsId, 600);
			if(list != null) return list;
			
			String value;
			list = new ArrayList<String>();
			Map<String, String> pwExceptionUserMap = getSysSetupMap(PtConstant.PT_PW_EXCEPTION_USER, true);
			
			if(pwExceptionUserMap!=null && !pwExceptionUserMap.isEmpty()){
				Entry<String, String> entry;
				Iterator<Entry<String, String>> iterator = pwExceptionUserMap.entrySet().iterator();
				while(iterator.hasNext()){
					entry = iterator.next();
					value = entry.getValue();
					
					if(value!=null && !value.isEmpty()){
						for(String odurUid : value.split(",")){
							if(odurUid!=null && !odurUid.isEmpty() && !list.contains(odurUid)){
								list.add(odurUid);
							}
						}
					}
				}
			}
			
			ptCacheSvc.setCache(CacheConfig.SYS_SETUP, "ko", setupClsId, list);
			return list;
			
		} catch (SQLException e) { e.printStackTrace(); }
		return null;
	}
	
	/** PC 알림 모듈 목록 - 관리자가 설정한 */
	public List<String> getPcNotiMds(Map<String, String> sysPlocMap){
		List<String> pcNotiMdList = new ArrayList<String>();
		for(String md : PtConstant.PC_NOTI_MDS){
			if("Y".equals(sysPlocMap.get("pcNoti"+md))){
				pcNotiMdList.add(md);
			}
		}
		return pcNotiMdList.isEmpty() ? null : pcNotiMdList;
	}
	
	
	/** 시스템 차단 여부 조회 */
	public boolean denyUseOfHaltedSystem(String userUid) throws SQLException{
		if("U0000001".equals(userUid)) return false;
		String haltUid = getSysHaltUid();
		if(haltUid==null) return false;
		return !haltUid.equals(userUid);
	}
	/** 시스템 차단 메세지 리턴 */
	public String getHaltMsg(String userUid, String langTypCd) throws SQLException{
		if("U0000001".equals(userUid)) return null;
		Map<String, String> sysHaltMap = getSysSetupMap(PtConstant.PT_SYS_HALT, true);
		String haltUid = sysHaltMap.get("haltUid");
		if(haltUid==null || haltUid.equals(userUid)) return null;
		try {
			return messageProperties.getMessage(sysHaltMap.get("msgId"), SessionUtil.toLocale(langTypCd));
		} catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	/** 시스템 차단 여부 */
	public boolean isSysInHalt() throws SQLException{
		return getSysHaltUid() != null;
	}
	/** 시스템 차단자 UID 리턴 */
	public String getSysHaltUid() throws SQLException {
		Map<String, String> sysHaltMap = getSysSetupMap(PtConstant.PT_SYS_HALT, true);
		String haltUid = sysHaltMap.get("haltUid");
		return (haltUid!=null && haltUid.isEmpty()) ? null : haltUid;
	}
	/** 시스템 차단 설정 */
	public void setSysHalt(String userUid, String module, String msgId) throws SQLException, CmException{
		
		if(userUid==null || userUid.isEmpty()) throw new CmException("NO userUid !");
		if(module==null || module.isEmpty()) throw new CmException("NO module !");
		if(msgId==null || msgId.isEmpty()) throw new CmException("NO msgId !");
		
		QueryQueue queryQueue = new QueryQueue();
		
		PtSysSetupDVo ptSysSetupDVo = new PtSysSetupDVo();
		ptSysSetupDVo.setSetupClsId(PtConstant.PT_SYS_HALT);
		ptSysSetupDVo.setSetupId("haltUid");
		ptSysSetupDVo.setSetupVa(userUid);
		queryQueue.store(ptSysSetupDVo);
		
		ptSysSetupDVo = new PtSysSetupDVo();
		ptSysSetupDVo.setSetupClsId(PtConstant.PT_SYS_HALT);
		ptSysSetupDVo.setSetupId("module");
		ptSysSetupDVo.setSetupVa(module.toLowerCase());
		queryQueue.store(ptSysSetupDVo);
		
		ptSysSetupDVo = new PtSysSetupDVo();
		ptSysSetupDVo.setSetupClsId(PtConstant.PT_SYS_HALT);
		ptSysSetupDVo.setSetupId("msgId");
		ptSysSetupDVo.setSetupVa(msgId);
		queryQueue.store(ptSysSetupDVo);
		
		String dbTime = ptCacheExpireSvc.getDbTime();
		ptCacheExpireSvc.expireAll(queryQueue, dbTime, CacheConfig.SYS_SETUP);
		commonSvc.execute(queryQueue);
		ptCacheExpireSvc.checkNow(CacheConfig.SYS_SETUP);
		
		LOGGER.fatal("[SYSTEM HALT] - module:"+module.toLowerCase()+"  haltUid:"+userUid);
	}
	/** 시스템 차단 해제 */
	public void clearSysHalt() throws SQLException{
		
		QueryQueue queryQueue = new QueryQueue();
		
		PtSysSetupDVo ptSysSetupDVo = new PtSysSetupDVo();
		ptSysSetupDVo.setSetupClsId(PtConstant.PT_SYS_HALT);
		queryQueue.delete(ptSysSetupDVo);
		
		String dbTime = ptCacheExpireSvc.getDbTime();
		ptCacheExpireSvc.expireAll(queryQueue, dbTime, CacheConfig.SYS_SETUP);
		commonSvc.execute(queryQueue);
		ptCacheExpireSvc.checkNow(CacheConfig.SYS_SETUP);
		
		LOGGER.fatal("[SYSTEM HALT] - released");
	}
	
	/** 메일용 SSL URL로 변환 */
	public String toZMailSslUrl(String url) throws SQLException{
		int p = url.indexOf("/zmail/"), q;
		if(p<0) return url;
		
		String sslUrl = url;
		
		if(url.startsWith("http:")){
			String domain;
			q = url.lastIndexOf(":", p);
			if(q>5){
				domain = url.substring(7, q);
				q = url.indexOf('/', 7);
			} else {
				q = url.indexOf('/', 7);
				domain = url.substring(7, q);
			}
			sslUrl = "https://"+domain+":"+PtConstant.MAIL_SSL_PORT+url.substring(q);
		} else {
			Map<String, String> svrEnvMap = getSvrEnvMap();
			String mailDomain = svrEnvMap.get("mailCall");
			if(mailDomain!=null && !mailDomain.isEmpty()){
				p = mailDomain.indexOf(':');
				if(p>0){
					sslUrl = "https://"+mailDomain.substring(0, p)+":"+PtConstant.MAIL_SSL_PORT+url;
				} else {
					sslUrl = "https://"+mailDomain+":"+PtConstant.MAIL_SSL_PORT+url;
				}
			}
		}
		return sslUrl;
	}
}
