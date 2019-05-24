package com.innobiz.orange.web.pt.svc;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.innobiz.orange.web.cm.config.ServerConfig;
import com.innobiz.orange.web.cm.crypto.License;
import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.or.svc.OrCmSvc;
import com.innobiz.orange.web.or.vo.OrUserPwDVo;
import com.innobiz.orange.web.or.vo.OrUserPwHVo;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.sync.PushSyncSvc;
import com.innobiz.orange.web.pt.utils.PtConstant;
import com.innobiz.orange.web.pt.utils.PwUtil;
import com.innobiz.orange.web.pt.vo.PtUserSetupDVo;

/** 사용자 개인 설정 서비스 */
@Service
public class PtPsnSvc {
	
	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(PtPsnSvc.class);

	/** UTIL에서 참조용 : PersonalUtil */
	public static PtPsnSvc ins = null;
	
	/** 생성자 */
	public PtPsnSvc(){
		ins = this;
	}
	
//	/** 캐쉬 서비스 */
//	@Autowired
//	private PtCacheSvc ptCacheSvc;
	
	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;

	/** 조직 공통 서비스 */
	@Autowired
	private OrCmSvc orCmSvc;
	
	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 조직도 사용자 Push 방식 동기화 서비스 */
	@Autowired
	private PushSyncSvc pushSyncSvc;
	
	/** 메세지 프로퍼티 */
	@Autowired
	private MessageProperties messageProperties;

	/** context.properties */
	@Resource(name = "contextProperties")
	private Properties contextProperties;
	
	/** 개인화설정상세 맵 조회 */
	public Map<String, String> getUserSetupMap(HttpServletRequest request, String userUid, String setupClsId, boolean createNew) throws SQLException {
		
		if(userUid==null) userUid = LoginSession.getUser(request).getUserUid();
		String attId = toAttrId(setupClsId);
		
		if(attId!=null && !createNew){
			HttpSession session = request.getSession();
			@SuppressWarnings("unchecked")
			Map<String, Map<String, ?>> userSetupMap = (Map<String, Map<String, ?>>)session.getAttribute("userSetupMap");
			@SuppressWarnings("unchecked")
			Map<String, String> returnMap = (Map<String, String>)userSetupMap.get(attId);
			if(returnMap != null){
				return returnMap;
			}
		}
		
		// 사용자설정상세(PT_USER_SETUP_D) 테이블 - 조회
		List<PtUserSetupDVo> ptUserSetupDVoList = getPtUserSetupDVoList(userUid, setupClsId, null);
		Map<String, String> returnMap = new HashMap<String, String>();
		
		if(ptUserSetupDVoList!=null){
			for(PtUserSetupDVo storedPtUserSetupDVo: ptUserSetupDVoList){
				returnMap.put(storedPtUserSetupDVo.getSetupId(), storedPtUserSetupDVo.getSetupVa());
			}
		}
		return returnMap;
	}
	
	/** 사용자 설정 조회 */
	public Map<String, String> getUserSetup(String userUid, String setupClsId) throws SQLException{
		// 사용자설정상세(PT_USER_SETUP_D) 테이블 - 조회
		List<PtUserSetupDVo> ptUserSetupDVoList = getPtUserSetupDVoList(userUid, setupClsId, null);
		Map<String, String> returnMap = new HashMap<String, String>();
		
		if(ptUserSetupDVoList!=null){
			for(PtUserSetupDVo storedPtUserSetupDVo: ptUserSetupDVoList){
				returnMap.put(storedPtUserSetupDVo.getSetupId(), storedPtUserSetupDVo.getSetupVa());
			}
		}
		return returnMap;
	}
	
	/** 최종 로그인 언어 조회 */
	public String getLastLginLangTypCd(String userUid, boolean withDefault) throws SQLException{
		Map<String, String> setupMap = getUserSetup(userUid, PtConstant.PT_LOGIN);
		String langTypCd = setupMap.get("lastLangTypCd");
		if(!withDefault || langTypCd!=null) return langTypCd;
		langTypCd = contextProperties.getProperty("login.default.lang", "ko");
		return langTypCd;
	}
	
//	/** 개인화설정상세 맵 조회 */ - 사용안함
//	public Map<String, Integer> getUserSetupIntMap(HttpServletRequest request, String userUid, String setupClsId) throws SQLException{
//		
//		if(userUid==null) userUid = LoginSession.getUser(request).getUserUid();
//		String attId = toAttrId(setupClsId);
//		
//		if(attId!=null){
//			HttpSession session = request.getSession();
//			@SuppressWarnings("unchecked")
//			Map<String, Map<String, ?>> userSetupMap = (Map<String, Map<String, ?>>)session.getAttribute("userSetupMap");
//			@SuppressWarnings("unchecked")
//			Map<String, Integer> returnMap = (Map<String, Integer>)userSetupMap.get(attId);
//			if(returnMap != null){
//				return returnMap;
//			}
//		}
//		
//		// 사용자설정상세(PT_USER_SETUP_D) 테이블 - 조회
//		List<PtUserSetupDVo> ptUserSetupDVoList = getPtUserSetupDVoList(userUid, setupClsId, null);
//		Map<String, Integer> returnMap = new HashMap<String, Integer>();
//		
//		if(ptUserSetupDVoList!=null){
//			for(PtUserSetupDVo storedPtUserSetupDVo: ptUserSetupDVoList){
//				try{
//					returnMap.put(storedPtUserSetupDVo.getSetupId(), 
//							Integer.valueOf(storedPtUserSetupDVo.getSetupVa()) );
//				} catch(Exception ignore){}
//			}
//		}
//		return returnMap;
//	}
	
	/** 사용자설정상세(PT_USER_SETUP_D) 테이블 조회 */
	private List<PtUserSetupDVo> getPtUserSetupDVoList(String userUid, String setupClsId, String cacheYn) throws SQLException{
		
		// 사용자설정상세(PT_USER_SETUP_D) 테이블
		PtUserSetupDVo ptUserSetupDVo = new PtUserSetupDVo();
		ptUserSetupDVo.setUserUid(userUid);
		ptUserSetupDVo.setSetupClsId(setupClsId);
		ptUserSetupDVo.setCacheYn(cacheYn);
		
		@SuppressWarnings("unchecked")
		List<PtUserSetupDVo> ptUserSetupDVoList = (List<PtUserSetupDVo>)commonDao.queryList(ptUserSetupDVo);
		return ptUserSetupDVoList;
	}
	
	/** 설정분류ID 를 Attribute 명으로 전환 */
	public String toAttrId(String setupClsId){
		int p = setupClsId.lastIndexOf('.');
		if(p<0) return null;
		String attrId = setupClsId.substring(p+1);
		if(attrId.isEmpty()) return null;
		return attrId+"Map";
	}
	
	/** 사용자 개인 설정을 조회하여 map의 map 형태로 리턴함 */
	public Map<String, Map<String, ?>> getUserSetup(String userUid, String odurUid, List<String> delList, HttpServletRequest request) throws IOException, SQLException  {
		
		// 사용자설정상세(PT_USER_SETUP_D) 테이블 - 조회
		List<PtUserSetupDVo> ptUserSetupDVoList = getPtUserSetupDVoList(
				userUid, null, delList==null ? "Y" : null);
		
		if(!userUid.equals(odurUid)){
			List<PtUserSetupDVo> ptOdurSetupDVoList = getPtUserSetupDVoList(
					odurUid, "pt.pcNoti", delList==null ? "Y" : null);
			
			if(ptOdurSetupDVoList != null){
				if(ptUserSetupDVoList != null){
					ptUserSetupDVoList.addAll(ptOdurSetupDVoList);
				} else {
					ptUserSetupDVoList = ptOdurSetupDVoList;
				}
			}
		}
		
		// int map 인지 여부
		boolean isIntMap = false;
		
		String newSetupClsId, oldSetupClsId=null, ssnAttId=null;
		
		PtUserSetupDVo ptUserSetupDVo;
		Map<String, Integer> intMap = null;
		Map<String, String > strMap = null;
		Map<String, Map<String, ?>> returnMap = new HashMap<String, Map<String, ?>>();
		Map<String, Map<String, ?>> mobileMap = new HashMap<String, Map<String, ?>>();
		int i, size = ptUserSetupDVoList==null ? 0 : ptUserSetupDVoList.size();
		
		for(i=0; i<size;i++){
			ptUserSetupDVo = ptUserSetupDVoList.get(i);
			newSetupClsId = ptUserSetupDVo.getSetupClsId();
			
			// Map 생성, Map을 세션에 담음
			if(oldSetupClsId==null || !oldSetupClsId.equals(newSetupClsId)){
				
				ssnAttId = toAttrId(newSetupClsId);
				if(ssnAttId==null){
					oldSetupClsId = null;
					continue;
				}
				
				// Cnt 로 끝나면 int 맵 사용
				if(newSetupClsId.endsWith("Cnt")){
					isIntMap = true;
					intMap = new HashMap<String, Integer>();
					if(ServerConfig.IS_MOBILE && newSetupClsId!=null && newSetupClsId.startsWith("mb.")){
						mobileMap.put(ssnAttId, intMap);
					} else {
						returnMap.put(ssnAttId, intMap);
					}
				} else {
					isIntMap = false;
					strMap = new HashMap<String, String>();
					if(ServerConfig.IS_MOBILE && newSetupClsId!=null && newSetupClsId.startsWith("mb.")){
						mobileMap.put(ssnAttId, strMap);
					} else {
						returnMap.put(ssnAttId, strMap);
					}
				}
				// 삭제 목록 정리 - 캐쉬 필요 없는 것은 사용 후 삭제함 
				if(delList!=null){
					if(!"Y".equals(ptUserSetupDVo.getCacheYn())){
						delList.add(ssnAttId);
					}
				}
				oldSetupClsId = newSetupClsId;
			}
			
			// Map에 데이터를 담음
			if(isIntMap){
				try{
					intMap.put(ptUserSetupDVo.getSetupId(),
							Integer.valueOf(ptUserSetupDVo.getSetupVa()) );
				} catch(Exception ignore){}
			} else {
				strMap.put(ptUserSetupDVo.getSetupId(), ptUserSetupDVo.getSetupVa());
			}
		}
		
		if(ServerConfig.IS_MOBILE && !mobileMap.isEmpty()){
			returnMap.putAll(mobileMap);
		}
		
		orCmSvc.setIndex(odurUid, request);
		return returnMap;
	}
	
	/** 개인화설정상세 내용을 QueryQueue에 저장 */
	public void storeUserSetupToQueue(HttpServletRequest request, String setupClsId, String cacheYn, QueryQueue queryQueue) throws SQLException{

		List<String[]> list = new ArrayList<String[]>();
		String userUid = LoginSession.getUser(request).getUserUid();
		
		Enumeration<String> enums = request.getParameterNames();
		String key;
		int prefixLen = setupClsId==null ? 0 : setupClsId.length()+1;
		String setupId;
		
		while(enums.hasMoreElements()){
			key = enums.nextElement();
			if(key!=null && key.startsWith(setupClsId)){
				setupId = key.substring(prefixLen);
				// 언더바(_)로 시작하면 저장 안함
				if(setupId.isEmpty() || setupId.charAt(0)=='_') continue;
				list.add(new String[]{ setupId, request.getParameter(key) });
			}
		}
		
		storeUserSetupToQueue(list, userUid, setupClsId, cacheYn, queryQueue);
	}
	
	/** 개인화설정상세 내용을 QueryQueue에 저장 */
	public void storeUserSetupToQueue(List<String[]> list, String userUid, String setupClsId, String cacheYn, QueryQueue queryQueue) throws SQLException{

		String[] arr;
		String setupId, value;//, currDt = commonDao.querySysdate(new CommonVoImpl("YYYY-MM-DD"));
		
		boolean deletePrevious = false;
		PtUserSetupDVo ptUserSetupDVo;
		
		int i, size = list==null ? 0 : list.size();
		for(i=0;i<size;i++){
			
			arr = list.get(i);
			setupId		= arr[0];
			value		= arr[1];
			
			if(!deletePrevious){
				// 사용자설정상세(PT_USER_SETUP_D) 테이블 - 기존 데이터 삭제
				ptUserSetupDVo = new PtUserSetupDVo();
				ptUserSetupDVo.setUserUid(userUid);
				ptUserSetupDVo.setSetupClsId(setupClsId);
				queryQueue.delete(ptUserSetupDVo);
				deletePrevious = true;
			}
			
			// 사용자설정상세(PT_USER_SETUP_D) 테이블 - INSERT
			ptUserSetupDVo = new PtUserSetupDVo();
			ptUserSetupDVo.setUserUid(userUid);
			ptUserSetupDVo.setSetupClsId(setupClsId);
			ptUserSetupDVo.setSetupId(setupId);
			ptUserSetupDVo.setSetupVa(value);
			ptUserSetupDVo.setCacheYn(cacheYn);
			queryQueue.insert(ptUserSetupDVo);
		}
	}
	/** 사용자 설정 정보를 QueryQueue 에 담음 (사용자설정상세(PT_USER_SETUP_D) 테이블에 저장) */
	public void addUserSetup(QueryQueue queryQueue, String userUid, String setupClsId, String setupId, String setupVa, String cacheYn){
		// 사용자설정상세(PT_USER_SETUP_D) 테이블
		PtUserSetupDVo ptUserSetupDVo = new PtUserSetupDVo();
		ptUserSetupDVo.setUserUid(userUid);
		ptUserSetupDVo.setSetupClsId(setupClsId);
		ptUserSetupDVo.setSetupId(setupId);
		ptUserSetupDVo.setSetupVa(setupVa);
		ptUserSetupDVo.setCacheYn(cacheYn);
		queryQueue.store(ptUserSetupDVo);
	}

	/** 모듈별 레코드 수 설정 맵 리턴 */
	public Map<String, Integer> getPageRecCntMap(HttpServletRequest request) throws SQLException{
		return getPageRecCntMap(request.getSession());
	}

	/** 모듈별 레코드 수 설정 맵 리턴 */
	public Map<String, Integer> getPageRecCntMap(HttpSession session) throws SQLException{
		@SuppressWarnings("unchecked")
		Map<String, Map<String, ?>> userSetupMap = (Map<String, Map<String, ?>>)session.getAttribute("userSetupMap");
		
		@SuppressWarnings("unchecked")
		Map<String, Integer> pageRecCntMap = (Map<String, Integer>)userSetupMap.get("pageRecCntMap");
		
		// 시스템설정에서 페이지를 가져옴
		if(pageRecCntMap==null){
			pageRecCntMap = ptSysSvc.getSysSetupIntMap(PtConstant.PT_PAGE_RCNT, true);
		}
		return pageRecCntMap;
	}
	
	/** 비밀번호 변경 */
	public void changePw(String odurUid, String userUid, String oldPw, String newPw, 
			String compId, String logPrefix, Locale locale) throws SQLException, CmException, IOException{
		
		// 패스워드 정책 조회
		Map<String, String> pwPolc = ptSysSvc.getPwPolc(compId);
		checkPwPolc(pwPolc, odurUid, newPw, logPrefix, locale);
		
		// 기존 로그인 비밀번호가 같은지 확인 후 새 비밀번호 암호화하여 리턴
		String encryptedNewPw = getEncryptedSysPw(odurUid, oldPw, newPw, logPrefix, locale);
		
		QueryQueue queryQueue = new QueryQueue();
		
		String sysdate = commonDao.querySysdate(new CommonVoImpl("YYYY-MM-DD HH24:MI:SS"));
		
		// 사용자비밀번호상세(OR_USER_PW_D) 테이블 - UPDATE
		OrUserPwDVo orUserPwDVo = new OrUserPwDVo();
		orUserPwDVo.setOdurUid(odurUid);
		orUserPwDVo.setPwTypCd("SYS");//SYS:시스템 비밀번호, APV:결재 비밀번호
		orUserPwDVo.setModDt(sysdate);
		orUserPwDVo.setModrUid(userUid);
		orUserPwDVo.setPwEnc(encryptedNewPw);
		queryQueue.store(orUserPwDVo);
		
		// 사용자비밀번호이력(OR_USER_PW_H) 테이블
		// - 비밀번호 변경 내역의 저장
		// - 예전 비밀번호로 비밀번호 찾기 기능을 위한 것 - 구현여부 미정
		
		// 사용자비밀번호이력(OR_USER_PW_H) 테이블 - INSERT
		OrUserPwHVo orUserPwHVo = new OrUserPwHVo();
		orUserPwHVo.setOdurUid(odurUid);
		orUserPwHVo.setPwTypCd("SYS");//SYS:시스템 비밀번호, APV:결재 비밀번호
		orUserPwHVo.setModDt(sysdate);
		orUserPwHVo.setModrUid(userUid);
		orUserPwHVo.setPwEnc(encryptedNewPw);
		queryQueue.store(orUserPwHVo);
		
		String currYmd = sysdate.substring(0,10);
		
		// 사용자설정상세(PT_USER_SETUP_D) 테이블에 저장
		addUserSetup(queryQueue, odurUid, PtConstant.PT_LOGIN, "sysPwChgDt", currYmd, "N");
		
		// 일괄실행
		commonSvc.execute(queryQueue);
		
		// 사용자 조직도 동기화 PUSH - 사용자
		if(pushSyncSvc.hasSync()){
			pushSyncSvc.syncUsers(odurUid, null, null, null);
		}
	}
	
	
	/** 비밀번호 정책 체크 */
	private void checkPwPolc(Map<String, String> pwPolc, String odurUid, String newPw, String logPrefix, Locale locale) throws CmException{
		
		if("Y".equals(pwPolc.get("polcUseYn"))){
			
			int min = 6;
			int max = 20;
			try{
				min = Integer.parseInt(pwPolc.get("minLength"));
				max = Integer.parseInt(pwPolc.get("maxLength"));
			} catch(Exception e){
				e.printStackTrace();
			}
			
			// 길이체크
			if(!PwUtil.checkLength(newPw, min, max)){
				// pt.jsp.setPw.tx11={0}자 이상 {1}자 이하로 작성 합니다.
				String msg = messageProperties.getMessage("pt.jsp.setPw.tx11",
						new String[]{pwPolc.get("minLength"),pwPolc.get("maxLength")}, locale);
				LOGGER.error(logPrefix+" - LENGTH MIN:"+min+" MAX:"+max+" - odurUid:"+odurUid+"  newPw: "+newPw);
				throw new CmException(msg);
			}
			
			// 알파벳 체크
			if(!PwUtil.hasAlphabet(newPw)){
				// pt.jsp.setPw.tx51=1자리 이상의 알파벳을 포함해야 합니다.
				String msg = messageProperties.getMessage("pt.jsp.setPw.tx51", locale);
				LOGGER.error(logPrefix+" - NO ALPHABET - odurUid:"+odurUid+"  newPw: "+newPw);
				throw new CmException(msg);
			}
			
			// 숫자 체크
			if("Y".equals(pwPolc.get("numberMandatoryYn")) && !PwUtil.hasNumber(newPw)){
				// pt.jsp.setPw.tx21=1자리 이상의 숫자를 반듯이 포함 해야 합니다.
				String msg = messageProperties.getMessage("pt.jsp.setPw.tx21", locale);
				LOGGER.error(logPrefix+" - NO NUMBER - odurUid:"+odurUid+"  newPw: "+newPw);
				throw new CmException(msg);
			}
			
			// 특수문자 체크
			if("Y".equals(pwPolc.get("specailCharMandatoryYn")) && !PwUtil.hasSpcChar(newPw)){
				// pt.jsp.setPw.tx41=1자리 이상의 특수 문자를 포함해야 합니다.
				String msg = messageProperties.getMessage("pt.jsp.setPw.tx41", locale);
				LOGGER.error(logPrefix+" - NO SPECIAL CHAR - odurUid:"+odurUid+"  newPw: "+newPw);
				throw new CmException(msg);
			}
		}
	}
	
	/** 기존 로그인 비밀번호가 같은지 확인 후 새 비밀번호 암호화하여 리턴 */
	public String getEncryptedSysPw(String odurUid, String orgPw, String newPw, String logPrefix, Locale locale) throws SQLException, CmException, IOException{

		OrUserPwDVo orUserPwDVo;
		OrUserPwHVo orUserPwHVo;
		
		// 새로운 비밀번호 암호화(파라미터) - 비교를 위한것
		String encryptedNewPw = newPw==null ? null : License.ins.encryptPw(newPw, odurUid);
		
		// 사용자비밀번호상세(OR_USER_PW_D) 테이블
		orUserPwDVo = new OrUserPwDVo();
		orUserPwDVo.setOdurUid(odurUid);
		orUserPwDVo.setPwTypCd("SYS");//SYS:시스템 비밀번호, APV:결재 비밀번호
		
		// 사용자비밀번호상세(OR_USER_PW_D) 테이블 - 조회
		orUserPwDVo = (OrUserPwDVo)commonDao.queryVo(orUserPwDVo);
		
		if(orgPw!=null){
			// 기존 비밀번호 암호화(파라미터) - 비교를 위한것
			String encryptedOrgPw = License.ins.encryptPw(orgPw, odurUid);
			if(encryptedOrgPw!=null && orUserPwDVo!=null && !encryptedOrgPw.equals(orUserPwDVo.getPwEnc())){
				// pt.login.noUserNoPw=비밀번호가 다르거나 사용자 정보를 확인 할 수 없습니다.
				String msg = messageProperties.getMessage("pt.login.noUserNoPw", locale);
				LOGGER.error(logPrefix+" - PW NOT MATCHED - "+"  odurUid:"+odurUid);
				throw new CmException(msg);
			}
			if(newPw==null) return null;
		}
		
		// 사용자비밀번호이력(OR_USER_PW_H) 테이블 - 기존의 비밀 번호와 비교
		orUserPwHVo = new OrUserPwHVo();
		orUserPwHVo.setOdurUid(odurUid);
		orUserPwHVo.setPwTypCd("SYS");//SYS:시스템 비밀번호, APV:결재 비밀번호
		orUserPwHVo.setOrderBy("MOD_DT DESC");
		orUserPwHVo.setPageNo(1);
		orUserPwHVo.setPageRowCnt(3);
		
		// 시스템 설정 조회
		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
		
		// 사용자비밀번호이력(OR_USER_PW_H) 테이블 - 조회
		@SuppressWarnings("unchecked")
		List<OrUserPwHVo> orUserPwHVoList = "Y".equals(sysPlocMap.get("chgToOldPwEnable")) ? null
				: (List<OrUserPwHVo>)commonDao.queryList(orUserPwHVo);
		
		// 비밀번호 이력과 비교
		if(orUserPwHVoList!=null){
			for(OrUserPwHVo storedOrUserPwHVo : orUserPwHVoList){
				if(encryptedNewPw.equals(storedOrUserPwHVo.getPwEnc())){
					// pt.jsp.setPw.not.chg3=사용했던 비밀번호로 변경 할 수 없습니다.
					String msg = messageProperties.getMessage("pt.jsp.setPw.not.chg3", locale);
					LOGGER.error(logPrefix+" - CAN NOT TO OLD PW - "+"  odurUid:"+odurUid+" - date:"+storedOrUserPwHVo.getModDt());
					throw new CmException(msg);
				}
			}
		}
		
		return encryptedNewPw;
	}
}
