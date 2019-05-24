package com.innobiz.orange.web.or.svc;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.innobiz.orange.web.cm.config.CacheConfig;
import com.innobiz.orange.web.cm.crypto.CryptoSvc;
import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.exception.LicenseLimitException;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.ArrayUtil;
import com.innobiz.orange.web.cm.utils.EscapeUtil;
import com.innobiz.orange.web.cm.utils.FinderUtil;
import com.innobiz.orange.web.cm.utils.Hash;
import com.innobiz.orange.web.cm.utils.StringUtil;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.CommonVo;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.or.vo.OrOdurBVo;
import com.innobiz.orange.web.or.vo.OrOdurSecuDVo;
import com.innobiz.orange.web.or.vo.OrOrgApvDVo;
import com.innobiz.orange.web.or.vo.OrOrgBVo;
import com.innobiz.orange.web.or.vo.OrOrgCntcDVo;
import com.innobiz.orange.web.or.vo.OrOrgTreeVo;
import com.innobiz.orange.web.or.vo.OrRescBVo;
import com.innobiz.orange.web.or.vo.OrUserBVo;
import com.innobiz.orange.web.or.vo.OrUserImgDVo;
import com.innobiz.orange.web.or.vo.OrUserPinfoDVo;
import com.innobiz.orange.web.or.vo.OrUserPwDVo;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtCacheSvc;
import com.innobiz.orange.web.pt.svc.PtCmSvc;
import com.innobiz.orange.web.pt.svc.PtSysSvc;
import com.innobiz.orange.web.pt.vo.PtCdBVo;
import com.innobiz.orange.web.pt.vo.PtCompBVo;
import com.innobiz.orange.web.pt.vo.PtLstSetupDVo;

/** 조직 공통 서비스 */
@Service
public class OrCmSvc {

//	/** Logger */
//	private static final Logger LOGGER = Logger.getLogger(OrCmSvc.class);
	
	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;

	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;

	/** 포털 공통 서비스 */
	@Autowired
	private PtCmSvc ptCmSvc;

	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
	/** 캐쉬 서비스 */
	@Autowired
	private PtCacheSvc ptCacheSvc;
	
	/** 암호화 서비스 */
	@Autowired
	private CryptoSvc cryptoSvc;
	
	/** 조회용 사용자 상태 코드 - 02:근무중, 03:휴직, 04:정직, (01:사용신청, 05:퇴직, 99:삭제 제외) */
	private List<String> viewUserStatCdList = null;
	
	public OrCmSvc(){
		viewUserStatCdList = new ArrayList<String>();
		viewUserStatCdList.add("02");
		viewUserStatCdList.add("03");
		viewUserStatCdList.add("04");
	}
	
	/** 조회용 사용자 상태 코드 저장 - 02:근무중, 03:휴직, 04:정직, (01:사용신청, 05:퇴직, 99:삭제 제외) */
	public List<String> getViewUserStatCdList(){
		return viewUserStatCdList;
	}
	
	/** 상위 부서 라인 리턴 */
	public List<OrOrgTreeVo> getOrgUpLine(String orgId, String langTypCd) throws SQLException{
		Map<Integer, OrOrgTreeVo> orgMap = getOrgMap(langTypCd);
		List<OrOrgTreeVo> orOrgTreeVoList = new ArrayList<OrOrgTreeVo>();
		
		OrOrgTreeVo orTreeVo;
		while((orTreeVo = orgMap.get(Hash.hashId(orgId)))!=null){
			orOrgTreeVoList.add(orTreeVo);
			orgId = orTreeVo.getOrgPid();
		}
		return orOrgTreeVoList;
	}
	
	/** 상위 부서 라인 리턴 */
	public List<String> getOrgUpIdLine(String orgId, String langTypCd) throws SQLException{
		if(orgId==null) return null;
		Map<Integer, OrOrgTreeVo> orgMap = getOrgMap(langTypCd);
		List<String> returnList = new ArrayList<String>();
		
		OrOrgTreeVo orTreeVo;
		while((orTreeVo = orgMap.get(Hash.hashId(orgId)))!=null){
			returnList.add(orTreeVo.getOrgId());
			orgId = orTreeVo.getOrgPid();
		}
		return returnList;
	}
	
	/** 하위 부서 ID 목록 리턴 */
	public List<String> getOrgSubIdList(String orgId, String langTypCd) throws SQLException{
		Map<Integer, List<String>> subIdListMap = getOrgSubListMap(langTypCd);
		List<String> returnList = new ArrayList<String>();
		collectOrgSubIds(orgId, subIdListMap, returnList);
		return returnList;
	}
	
	/** 서브 조직 ID 모음 */
	private void collectOrgSubIds(String orgId, Map<Integer, List<String>> subIdListMap, List<String> returnList){
		List<String> subIdList = subIdListMap.get(Hash.hashId(orgId));
		if(subIdList!=null){
			for(String id : subIdList){
				returnList.add(id);
				collectOrgSubIds(id, subIdListMap, returnList);
			}
		}
	}
	
	/** 조직이 속한 회사,기관,부서 정보 조회(캐쉬) - 조직구분코드 - C:회사, G:기관, D:부서 */
	public OrOrgTreeVo getOrgByOrgTypCd(String orgId, String orgTypCd, String langTypCd) throws SQLException{
		Map<Integer, OrOrgTreeVo> orgMap = getOrgMap(langTypCd);
		
		String storedOrgTypCd;
		OrOrgTreeVo orTreeVo = null;
		while((orTreeVo = orgMap.get(Hash.hashId(orgId)))!=null){
			storedOrgTypCd = orTreeVo.getOrgTypCd();
			if(orgTypCd==null || orgTypCd.isEmpty()){
				return orTreeVo;
			} else if(orgTypCd.equals("D")){
				if(storedOrgTypCd.equals("D") || storedOrgTypCd.equals("G") || storedOrgTypCd.equals("C")){
					return orTreeVo;
				}
			} else if(orgTypCd.equals("G")){
				if(storedOrgTypCd.equals("G") || storedOrgTypCd.equals("C")){
					return orTreeVo;
				}
			} else if(orgTypCd.equals("C")){
				if(storedOrgTypCd.equals("C")){
					return orTreeVo;
				}
			} else {
				return orTreeVo;
			}
			orgId = orTreeVo.getOrgPid();
		}
		return null;
	}
	
	/** 해당 조직의 리소스명 리턴 */
	public String getOrgRescNmByOrgTypCd(String orgId, String orgTypCd, String langTypCd) throws SQLException{
		OrOrgTreeVo orTreeVo = getOrgByOrgTypCd(orgId, orgTypCd, langTypCd);
		return orTreeVo==null ? null : orTreeVo.getRescNm();
	}
	
	/** 조직 맵 리턴(캐쉬) */
	private Map<Integer, OrOrgTreeVo> getOrgMap(String langTypCd) throws SQLException{
		@SuppressWarnings("unchecked")
		Map<Integer, OrOrgTreeVo> orgMap = (Map<Integer, OrOrgTreeVo>)
				ptCacheSvc.getCache(CacheConfig.ORG, langTypCd, "MAP", 10);
		if(orgMap!=null) return orgMap;
		
		orgMap = new HashMap<Integer, OrOrgTreeVo>();
		Map<Integer, List<String>> subIdListMap = new HashMap<Integer, List<String>>();
		loadOrgMap(langTypCd, orgMap, subIdListMap);
		ptCacheSvc.setCache(CacheConfig.ORG, langTypCd, "MAP",  orgMap);
		ptCacheSvc.setCache(CacheConfig.ORG, langTypCd, "SUB_ID_LIST_MAP",  subIdListMap);
		return orgMap;
	}
	
	/** 서브 조직 목록 맵 리턴(캐쉬) */
	private Map<Integer, List<String>> getOrgSubListMap(String langTypCd) throws SQLException{
		@SuppressWarnings("unchecked")
		Map<Integer, List<String>> subIdListMap = (Map<Integer, List<String>>)
				ptCacheSvc.getCache(CacheConfig.ORG, langTypCd, "SUB_ID_LIST_MAP", 10);
		if(subIdListMap!=null) return subIdListMap;
		
		Map<Integer, OrOrgTreeVo> orgMap = new HashMap<Integer, OrOrgTreeVo>();
		subIdListMap = new HashMap<Integer, List<String>>();
		loadOrgMap(langTypCd, orgMap, subIdListMap);
		ptCacheSvc.setCache(CacheConfig.ORG, langTypCd, "MAP",  orgMap);
		ptCacheSvc.setCache(CacheConfig.ORG, langTypCd, "SUB_ID_LIST_MAP",  subIdListMap);
		return subIdListMap;
	}
	
	/** 조직 맵, 서브 조직 목록 맵 조회 */
	private void loadOrgMap(String langTypCd, 
			Map<Integer, OrOrgTreeVo> orgMap, Map<Integer, List<String>> subIdListMap) throws SQLException{
		
		OrOrgBVo orOrgBVo = new OrOrgBVo();
		orOrgBVo.setQueryLang(langTypCd);
		orOrgBVo.setOrderBy("ORG_PID, SORT_ORDR");
		@SuppressWarnings("unchecked")
		List<OrOrgBVo> orOrgBVoList = (List<OrOrgBVo>)commonDao.queryList(orOrgBVo);
		
		OrOrgTreeVo orTreeVo;
		String orgPid = null;
		List<String> idList = null;
		int i, size = orOrgBVoList==null ? 0 : orOrgBVoList.size();
		for(i=0;i<size;i++){
			orOrgBVo = orOrgBVoList.get(i);
			
			if(orgPid==null || !orgPid.equals(orOrgBVo.getOrgPid())){
				orgPid = orOrgBVo.getOrgPid();
				idList = new ArrayList<String>();
				subIdListMap.put(Hash.hashId(orgPid), idList);
			}
			idList.add(orOrgBVo.getOrgId());
			
			orTreeVo = new OrOrgTreeVo();
			orTreeVo.setOrgId(orOrgBVo.getOrgId());
			orTreeVo.setOrgPid(orOrgBVo.getOrgPid());
			orTreeVo.setOrgTypCd(orOrgBVo.getOrgTypCd());
			orTreeVo.setRescId(orOrgBVo.getRescId());
			orTreeVo.setRescNm(orOrgBVo.getRescNm());
			orTreeVo.setCompId(orOrgBVo.getCompId());
			orTreeVo.setSortOrdr(orOrgBVo.getSortOrdr());
			orTreeVo.setOrgAbbrRescNm(orOrgBVo.getOrgAbbrRescNm());
			orTreeVo.setDisable("Y".equals(orOrgBVo.getUseYn()) ? Boolean.FALSE : Boolean.TRUE);
			
			// 무한루프 방지
			if(!orTreeVo.getOrgId().equals(orgPid)){
				orgMap.put(Hash.hashId(orTreeVo.getOrgId()), orTreeVo);
			}
		}
	}
	
	/** 리소스기본(OR_RESC_B)에 저장할 다건의 리소스 데이터를 QueryQueue 에 저장 */
	public void collectOrRescBVoList(HttpServletRequest request, QueryQueue queryQueue, List<? extends CommonVo> commonVoList
			, String validCol, String rescIdCol, String rescVaCol) throws SQLException{
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		UserVo userVo = LoginSession.getUser(request);
		
		OrRescBVo orRescBVo, firstOrRescBVo;
		String rescId, rescVa;
		
		// 값이 유효한지 체크할 컬럼
		String[] validColVas = request.getParameterValues(validCol);
		
		// 리소스ID, 없으면 생성함
		String[] rescIds = request.getParameterValues("rescId");
		boolean isNewResc = false;
		
		CommonVo commonVo;
		
		// 회사별 설정된 리소스의 어권 정보
		List<PtCdBVo> langTypCdList = ptCmSvc.getLangTypCdListByCompId(userVo.getCompId(), langTypCd);
		
		int i, j, size = langTypCdList==null ? 0 : langTypCdList.size();
		String[] langs = new String[size];
		String[][] rescVa2s = new String[size][];
		String[] rescVas;
		for(j=0;j<size;j++){
			langs[j] = langTypCdList.get(j).getCd();
			rescVa2s[j] = request.getParameterValues("rescVa_"+langs[j]);
		}
		
		int commonIndex = 0;
		for(i=0;i<validColVas.length;i++){
			
			// 유효체크 파라미터가 없으면 - 무시
			if(validColVas[i]==null || validColVas[i].isEmpty()) continue;
			
			// 리소스ID 없으면 생성
			rescId = i<rescIds.length ? rescIds[i] : null;
			if(rescId==null || rescId.isEmpty()){
				rescId = createId("OR_RESC_B");
				isNewResc = true;
			} else {
				isNewResc = false;
			}
			
			// 코드 테이블에 한글값 또는, 첫번째 값을 넣으려는 목적
			firstOrRescBVo = null;
			
			// 어권 만큼 돌면서
			for(j=0;j<size;j++){
				
				rescVas = j<rescVa2s.length ? rescVa2s[j] : null;
				rescVa = (rescVas!=null && i<rescVas.length) ? rescVas[i] : null;
				if(rescVa==null) continue;
				
				orRescBVo = new OrRescBVo();
				orRescBVo.setRescId(rescId);
				orRescBVo.setLangTypCd(langs[j]);
				orRescBVo.setRescVa(rescVa);
				
				if(isNewResc){
					queryQueue.insert(orRescBVo);
				} else {
					queryQueue.store(orRescBVo);
				}
				
				if(firstOrRescBVo==null || "ko".equals(langs[j])){
					firstOrRescBVo = orRescBVo;
				}
			}
			
			
			if(commonVoList.size()>commonIndex){
				commonVo = commonVoList.get(commonIndex++);
				
				// 리소스ID, 리소스값을 
				if(firstOrRescBVo!=null){
					VoUtil.setValue(commonVo, rescIdCol, rescId);
					VoUtil.setValue(commonVo, rescVaCol, firstOrRescBVo.getRescVa());
				}
				
				if(isNewResc){
					VoUtil.setValue(commonVo, "regDt", "sysdate");
					VoUtil.setValue(commonVo, "regrUid", userVo.getUserUid());
					queryQueue.insert(commonVo);
				} else {
					queryQueue.store(commonVo);
				}
				
			}
		}
	}
	
	/** 리소스기본(OR_RESC_B)에 저장할 리소스 데이터를 QueryQueue 에 저장 */
	public OrRescBVo collectOrRescBVo(HttpServletRequest request, QueryQueue queryQueue, String prefix, List<PtCdBVo> langTypCdList) throws SQLException{
		
		OrRescBVo orRescBVo, returnOrRescBVo = null;
		boolean emptyPrefix = prefix==null || prefix.isEmpty(), isNewResc = false;
		String rescId = request.getParameter(emptyPrefix ? "rescId" : prefix+"RescId"), rescVa;
		if(rescId==null || rescId.isEmpty()){
			rescId = createId("OR_RESC_B");
			isNewResc = true;
		}
		
		for(PtCdBVo ptCdBVo : langTypCdList){
			rescVa = request.getParameter(emptyPrefix ? "rescVa_"+ptCdBVo.getCd() : prefix+"RescVa_"+ptCdBVo.getCd());
			if(rescVa==null || rescVa.isEmpty()) continue;
			
			orRescBVo = new OrRescBVo();
			orRescBVo.setRescId(rescId);
			orRescBVo.setLangTypCd(ptCdBVo.getCd());
			orRescBVo.setRescVa(rescVa);
			if(isNewResc){
				queryQueue.insert(orRescBVo);
			} else {
				queryQueue.store(orRescBVo);
			}
			if(returnOrRescBVo==null || "ko".equals(ptCdBVo.getCd())){
				returnOrRescBVo = orRescBVo;
			}
		}
		return returnOrRescBVo;
	}
	
	/** 리소스기본(OR_RESC_B) 테이블에 저장할 단건의 리소스 데이터를 QueryQueue 에 저장 */
	public OrRescBVo collectOrRescBVo(Map<String, String> paramMap, String compId, String langTypCd, QueryQueue queryQueue) throws SQLException{
		
		// 리소스ID 가 없음
		boolean emptyId = false;
		// rescId 받음 : 없으면 생성
		String rescId = paramMap.get("rescId"), rescVa;
		if(rescId==null || rescId.isEmpty()){
			rescId = createId("OR_RESC_B");
			emptyId = true;
		}
		
		// 첫번째 리소스 값
		OrRescBVo orRescBVo, firstOrRescBVo = null;
		
		// rescVa 받아서 not empty 면 QueryQueue 에 담음
		PtCdBVo ptCdBVo;
		List<PtCdBVo> langTypCdList = ptCmSvc.getLangTypCdListByCompId(compId, langTypCd);
		int i, size = langTypCdList==null ? 0 : langTypCdList.size();
		for(i=0;i<size;i++){
			ptCdBVo = langTypCdList.get(i);
			rescVa = paramMap.get("rescVa_"+ptCdBVo.getCd());
			if(rescVa!=null && !rescVa.isEmpty()){
				
				orRescBVo = new OrRescBVo();
				orRescBVo.setRescId(rescId);
				orRescBVo.setLangTypCd(ptCdBVo.getCd());
				orRescBVo.setRescVa(rescVa);
				
				if(firstOrRescBVo==null || "ko".equals(ptCdBVo.getCd())){
					firstOrRescBVo = orRescBVo;
				}
				
				if(emptyId){
					queryQueue.insert(orRescBVo);
				} else {
					queryQueue.store(orRescBVo);
				}
			}
		}
		
		return firstOrRescBVo;
	}
	
	/** 사용자 개인정보 암호화 */
	public void encryptUserPinfo(OrUserPinfoDVo orUserPinfoDVo) throws IOException, CmException{
		Map<String, Object> map = VoUtil.toMap(orUserPinfoDVo, null);
		List<String> encAttrList = VoUtil.collect(OrUserPinfoDVo.class, "set", null, null, new String[]{"Enc"});
		Map<String, Object> encMap = new HashMap<String, Object>();
		int i, size = encAttrList==null ? 0 : encAttrList.size();
		String key, value, encValue;
		Object object;
		for(i=0;i<size;i++){
			key = encAttrList.get(i);
			object = map.get(key.substring(0, key.length()-3));
			if(object!=null && (value = object.toString())!=null){
				encValue = cryptoSvc.encryptPersanal(value);
				encMap.put(key, encValue);
			}
		}
		if(!encMap.isEmpty()){
			VoUtil.fromMap(orUserPinfoDVo, encMap);
		}
	}
	
	/** 사용자 개인정보 복호화 */
	public void decryptUserPinfo(OrUserPinfoDVo orUserPinfoDVo) throws IOException, CmException{
		Map<String, Object> decMap = new HashMap<String, Object>();
		decryptUserPinfoToMap(orUserPinfoDVo, decMap);
		if(!decMap.isEmpty()){
			VoUtil.fromMap(orUserPinfoDVo, decMap);
		}
	}
	
	/** 사용자 개인정보 복호화 */
	public OrUserPinfoDVo getDecryptUserPinfo(OrUserPinfoDVo orUserPinfoDVo) throws IOException, CmException{
		Map<String, Object> decMap = new HashMap<String, Object>();
		decryptUserPinfoToMap(orUserPinfoDVo, decMap);
		if(!decMap.isEmpty()){
			orUserPinfoDVo = new OrUserPinfoDVo();
			VoUtil.fromMap(orUserPinfoDVo, decMap);
			return orUserPinfoDVo;
		}
		return null;
	}
	
	/** 사용자 정보 조회 */
	public Map<String, Object> getUserMapForSSO(String userUid, String langTypCd) throws SQLException, CmException, IOException {
		Map<String, Object> map = getUserMap(userUid, langTypCd);
		return map==null ? null : removeUnNecessaryUserInfo(map);
	}
	
	/** 사용자 불필요 정보 제거 */
	private Map<String, Object> removeUnNecessaryUserInfo(Map<String, Object> userInfoMap){
		for(String attr : new String[]{"rescId","orgRescId","deptRescId","userNm"}){
			userInfoMap.remove(attr);
		}
		return userInfoMap;
	}
	
	/** 원직자 보안 정보 - lginIpExYn:로그인IP제외대상여부, sesnIpExYn:세션IP제외대상여부, useMobYn:모바일사용여부, useMsgLginYn:메세지로그인여부, useMsgrYn:메신저사용여부, useMailYn:메일사용여부 */
	public Map<String, String> getOdurSecuMap(String odurUid) throws SQLException{
		
		OrOdurSecuDVo orOdurSecuDVo = new OrOdurSecuDVo();
		orOdurSecuDVo.setOdurUid(odurUid);
		@SuppressWarnings("unchecked")
		List<OrOdurSecuDVo> orOdurSecuDVoList = (List<OrOdurSecuDVo>)commonDao.queryList(orOdurSecuDVo);
		
		Map<String, String> odurSecuMap = new HashMap<String, String>();
		if(orOdurSecuDVoList != null){
			for(OrOdurSecuDVo storedOrOdurSecuDVo : orOdurSecuDVoList){
				odurSecuMap.put(storedOrOdurSecuDVo.getSecuId(), storedOrOdurSecuDVo.getSecuVa());
			}
		}
		
		return odurSecuMap;
	}
	
	/** 사용자 정보 조회 */
	public Map<String, Object> getUserMap(String userUid, String langTypCd) throws SQLException, CmException, IOException{
		
		// 사용자기본(OR_USER_B) 테이블
		OrUserBVo orUserBVo = new OrUserBVo();
		if(userUid!=null) orUserBVo.setUserUid(userUid);
		orUserBVo.setQueryLang(langTypCd);
		orUserBVo = (OrUserBVo)commonDao.queryVo(orUserBVo);
		if(orUserBVo==null) return null;
		
		OrOdurBVo orOdurBVo = new OrOdurBVo();
		orOdurBVo.setOdurUid(orUserBVo.getOdurUid());
		orOdurBVo.setQueryLang(langTypCd);
		orOdurBVo = (OrOdurBVo)commonDao.queryVo(orOdurBVo);
		if(orOdurBVo==null) return null;
		
		Map<String, Object> userInfoMap = new HashMap<String, Object>();
		VoUtil.toMap(orUserBVo, userInfoMap);
		
		userInfoMap.put("rid", orOdurBVo.getRid());
		userInfoMap.put("ein", orOdurBVo.getEin());
		userInfoMap.put("lginId", orOdurBVo.getLginId());
		userInfoMap.put("genCd", orOdurBVo.getGenCd());
		userInfoMap.put("genNm", orOdurBVo.getGenNm());
		userInfoMap.put("entraYmd", orOdurBVo.getEntraYmd()); // 입사일자 추가
		
		// 사용자개인정보상세(OR_USER_PINFO_D) 테이블
		OrUserPinfoDVo orUserPinfoDVo = new OrUserPinfoDVo();
		orUserPinfoDVo.setOdurUid(orUserBVo.getOdurUid());
		orUserPinfoDVo.setQueryLang(langTypCd);
		orUserPinfoDVo = (OrUserPinfoDVo)commonDao.queryVo(orUserPinfoDVo);
		if(orUserPinfoDVo!=null){
			decryptUserPinfoToMap(orUserPinfoDVo, userInfoMap);
		}
		userInfoMap.remove("ssn");
		
		OrUserPwDVo orUserPwDVo = new OrUserPwDVo();
		orUserPwDVo.setOdurUid(orUserBVo.getOdurUid());
		orUserPwDVo.setPwTypCd("SYS");
		orUserPwDVo = (OrUserPwDVo)commonDao.queryVo(orUserPwDVo);
		if(orUserPwDVo!=null){
			userInfoMap.put("pw", orUserPwDVo.getPwEnc());
		}
		
		return userInfoMap;
	}
	
	/** 사용자 정보 조회 - 동기화용 */
	public List<Map<String, Object>> getUserMapListForSync(OrUserBVo orUserBVo, String langTypCd) throws SQLException, CmException, IOException {
		
		// 사용자기본(OR_USER_B) 테이블
		orUserBVo.setQueryLang(langTypCd);
		@SuppressWarnings("unchecked")
		List<OrUserBVo> orUserBVoList = (List<OrUserBVo>)commonDao.queryList(orUserBVo);
		if(orUserBVoList==null || orUserBVoList.isEmpty()) return null;
		
		// 원직자UID 목록
		List<String> odurUidList = getOdurUidList(orUserBVoList);
		
		// 원직자기본(OR_ODUR_B) 테이블
		OrOdurBVo orOdurBVo = new OrOdurBVo();
		orOdurBVo.setOdurUidList(odurUidList);
		orOdurBVo.setQueryLang(langTypCd);
		@SuppressWarnings("unchecked")
		List<OrOdurBVo> orOdurBVoList = (List<OrOdurBVo>)commonDao.queryList(orOdurBVo);
		Map<Integer, OrOdurBVo> orOdurBVoMap = toOrOdurBVoMap(orOdurBVoList);
		
		// 사용자개인정보상세(OR_USER_PINFO_D) 테이블
		OrUserPinfoDVo orUserPinfoDVo = new OrUserPinfoDVo();
		orUserPinfoDVo.setOdurUidList(odurUidList);
		orUserPinfoDVo.setQueryLang(langTypCd);
		@SuppressWarnings("unchecked")
		List<OrUserPinfoDVo> orUserPinfoDVoList = (List<OrUserPinfoDVo>)commonDao.queryList(orUserPinfoDVo);
		Map<Integer, OrUserPinfoDVo> orUserPinfoDVoMap = toOrUserPinfoDVoMap(orUserPinfoDVoList);
		
		// 사용자비밀번호상세(OR_USER_PW_D) 테이블
		OrUserPwDVo orUserPwDVo = new OrUserPwDVo();
		orUserPwDVo.setOdurUidList(odurUidList);
		orUserPwDVo.setPwTypCd("SYS");
		@SuppressWarnings("unchecked")
		List<OrUserPwDVo> orUserPwDVoList = (List<OrUserPwDVo>)commonDao.queryList(orUserPwDVo);
		Map<Integer, String> pwMap = toPwMap(orUserPwDVoList);
		
		// 사용자이미지상세(OR_USER_IMG_D) 테이블
		OrUserImgDVo orUserImgDVo = new OrUserImgDVo();
		orUserImgDVo.setUserImgTypCd("03");//01:도장, 02:싸인, 03:사진
		@SuppressWarnings("unchecked")
		List<OrUserImgDVo> orUserImgDVoList = (List<OrUserImgDVo>)commonDao.queryList(orUserImgDVo);
		Map<Integer, String> imgMap = toImgMap(orUserImgDVoList);
		
		Integer key;
		String pw, imgPath;
		Map<String, Object> userInfoMap;
		List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
		for(OrUserBVo storedOrUserBVo : orUserBVoList){
			
			key = Hash.hashUid(storedOrUserBVo.getOdurUid());
			orOdurBVo = orOdurBVoMap.get(key);
			orUserPinfoDVo = orUserPinfoDVoMap.get(key);
			
			if(orOdurBVo!=null){
				userInfoMap = new HashMap<String, Object>();
				VoUtil.toMap(storedOrUserBVo, userInfoMap);
				removeUnNecessaryUserInfo(userInfoMap);
				
				userInfoMap.put("rid", orOdurBVo.getRid());
				userInfoMap.put("ein", orOdurBVo.getEin());
				userInfoMap.put("lginId", orOdurBVo.getLginId());
				userInfoMap.put("genCd", orOdurBVo.getGenCd());
				userInfoMap.put("genNm", orOdurBVo.getGenNm());
				
				if(orUserPinfoDVo!=null){
					decryptUserPinfoToMap(orUserPinfoDVo, userInfoMap);
					userInfoMap.remove("ssn");
				}
				
				pw = pwMap.get(key);
				if(pw!=null){
					userInfoMap.put("pw", pw);
				}
				imgPath = imgMap.get(Hash.hashUid(storedOrUserBVo.getUserUid()));
				if(imgPath == null) imgPath = imgMap.get(key);
				if(imgPath!=null){
					userInfoMap.put("imgPath", imgPath);
				}
				
				setOrRescB(orOdurBVo.getRescId(), userInfoMap);
				
				returnList.add(userInfoMap);
			}
		}
		
		return returnList;
	}
	
	/** 조직 언어별 리소스 세팅 - 조직명, 사용자명 세팅용 */
	private void setOrRescB(String rescId, Map<String, Object> map) throws SQLException{
		OrRescBVo orRescBVo = new OrRescBVo();
		orRescBVo.setRescId(rescId);
		@SuppressWarnings("unchecked")
		List<OrRescBVo> list = (List<OrRescBVo>)commonDao.queryList(orRescBVo);
		if(list!=null){
			for(OrRescBVo vo : list){
				map.put("rescNm-"+vo.getLangTypCd(), vo.getRescVa());
			}
		}
	}
	
	/** 비밀번호 맵으로 전환 */
	private Map<Integer, String> toPwMap(List<OrUserPwDVo> orUserPwDVoList){
		Map<Integer, String> pwMap = new HashMap<Integer, String>();
		if(orUserPwDVoList!=null){
			for(OrUserPwDVo orUserPwDVo : orUserPwDVoList){
				pwMap.put(Hash.hashUid(orUserPwDVo.getOdurUid()), orUserPwDVo.getPwEnc());
			}
		}
		return pwMap;
	}
	/** 비밀번호 맵으로 전환 */
	private Map<Integer, String> toImgMap(List<OrUserImgDVo> orUserImgDVoList){
		Map<Integer, String> pwMap = new HashMap<Integer, String>();
		if(orUserImgDVoList!=null){
			for(OrUserImgDVo orUserImgDVo : orUserImgDVoList){
				pwMap.put(Hash.hashUid(orUserImgDVo.getUserUid()), orUserImgDVo.getImgPath());
			}
		}
		return pwMap;
	}
	
	/** OrUserPinfoDVo 맵으로 전환 */
	private Map<Integer, OrUserPinfoDVo> toOrUserPinfoDVoMap(List<OrUserPinfoDVo> orUserPinfoDVoList){
		Map<Integer, OrUserPinfoDVo> orUserPinfoDVoMap = new HashMap<Integer, OrUserPinfoDVo>();
		for(OrUserPinfoDVo orUserPinfoDVo : orUserPinfoDVoList){
			orUserPinfoDVoMap.put(Hash.hashUid(orUserPinfoDVo.getOdurUid()), orUserPinfoDVo);
		}
		return orUserPinfoDVoMap;
	}
	
	/** OrOdurBVo 맵으로 전환 */
	private Map<Integer, OrOdurBVo> toOrOdurBVoMap(List<OrOdurBVo> orOdurBVoList){
		Map<Integer, OrOdurBVo> orOdurBVoMap = new HashMap<Integer, OrOdurBVo>();
		for(OrOdurBVo orOdurBVo : orOdurBVoList){
			orOdurBVoMap.put(Hash.hashUid(orOdurBVo.getOdurUid()), orOdurBVo);
		}
		return orOdurBVoMap;
	}
	
	/** 원직자UID 목록 리턴 */
	private List<String> getOdurUidList(List<OrUserBVo> orUserBVoList){
		List<String> odurUidList = new ArrayList<String>();
		for(OrUserBVo orUserBVo : orUserBVoList){
			odurUidList.add(orUserBVo.getOdurUid());
		}
		return odurUidList;
	}
	
	/** 사용자 개인정보 복호화 */
	public void decryptUserPinfoToMap(OrUserPinfoDVo orUserPinfoDVo, Map<String, Object> decMap) throws IOException, CmException{
		Map<String, Object> map = VoUtil.toMap(orUserPinfoDVo, null);
		Iterator<Entry<String, Object>> iterator = map.entrySet().iterator();
		Entry<String, Object> entry;
		String key, value, decValue;
		Object object;
		while(iterator.hasNext()){
			entry = iterator.next();
			key = entry.getKey();
			if(key.endsWith("Enc")){
				object = entry.getValue();
				if(object!=null && (value = object.toString())!=null){
					if(value.isEmpty()){
						decValue = value;
					} else {
						try{
							decValue = cryptoSvc.decryptPersanal(value);
							decMap.put(key.substring(0, key.length()-3), decValue);
						} catch(Exception e){
							//System.out.println(key.substring(0, key.length()-3)+":"+value);
							e.printStackTrace();
						}
					}
					
				}
			}
		}
	}
	
	/** 원직자UID에 해당하는 사용자 정보 조회 - 조회 후 복호화하여 원직자UID 를 키로 맵에 담음 */
	public Map<Integer, Map<String, Object>> queryUserPsnInfoMap(List<OrUserBVo> orUserBVoList) throws SQLException, IOException, CmException{
		
		Map<Integer, Map<String, Object>> map = new HashMap<Integer, Map<String, Object>>();
		if(orUserBVoList==null || orUserBVoList.isEmpty()){
			return map;
		}
		// 원직자UID 모음
		List<String> odurUidList = new ArrayList<String>();
		for(OrUserBVo storedOrUserBVo : orUserBVoList){
			if(!odurUidList.contains(storedOrUserBVo.getOdurUid())){
				odurUidList.add(storedOrUserBVo.getOdurUid());
			}
		}
		// 사용자개인정보상세(OR_USER_PINFO_D) 테이블 - 조회
		OrUserPinfoDVo orUserPinfoDVo = new OrUserPinfoDVo();
		orUserPinfoDVo.setOdurUidList(odurUidList);
		@SuppressWarnings("unchecked")
		List<OrUserPinfoDVo> orUserPinfoDVoList = (List<OrUserPinfoDVo>)commonDao.queryList(orUserPinfoDVo);
		
		Map<String, Object> decMap;
		if(orUserPinfoDVoList!=null){
			for(OrUserPinfoDVo storedOrUserPinfoDVo : orUserPinfoDVoList){
				decMap = new HashMap<String, Object>();
				// 복호화
				decryptUserPinfoToMap(storedOrUserPinfoDVo, decMap);
				// map 에 담음
				map.put(Hash.hashUid(storedOrUserPinfoDVo.getOdurUid()), decMap);
			}
		}
		return map;
	}
	
	/** 조직도의 임직원 검색 - 조직도 우측 */
	public void setUserMapByOrgId(String orgId, String userStatCd, String langTypCd, ModelMap model) throws CmException, SQLException, IOException {
		
		boolean isRoot = "ROOT".equals(orgId);
		boolean dispAllUsersEnable = model.get("dispAllUsersEnable") != null;
		
		OrUserBVo orUserBVo = new OrUserBVo();
		orUserBVo.setQueryLang(langTypCd);
		orUserBVo.setOrgId(isRoot && dispAllUsersEnable ? null : orgId);
		
		//사용자상태코드 - 01:사용신청, 02:근무중, 03:휴직, 04:정직, 05:퇴직, 99:삭제
		if("02".equals(userStatCd)){// 사용자 선택 : 02:근무중
			orUserBVo.setUserStatCd(userStatCd);
		} else if("999".equals(userStatCd)){/** 문서관리(개인) 사용자 상태 코드 - 05:퇴직, 11:해제, 99:삭제, (02:근무중, 03:휴직, 04:정직 제외) */
			orUserBVo.setUserStatCdList(ArrayUtil.toList(new String[]{"05","11","99"}, false));
		} else {// 기본 조직도 : 02:근무중, 03:휴직, 04:정직
			orUserBVo.setUserStatCdList(viewUserStatCdList);
		}
		
		// 리스트 환경 설정 - 세팅
		List<PtLstSetupDVo> ptLstSetupDVoList = null;
		
		if(!Boolean.TRUE.equals(model.get("demoSite"))){
			ptLstSetupDVoList = setListQueryOptions(orUserBVo, "OR_ORGC");
			model.put("ptLstSetupDVoList", ptLstSetupDVoList);
			
			if(dispAllUsersEnable){
				orUserBVo.setOrderBy("RESC_NM");
			}
		} else {
			// 데모용 하드코딩
			ptLstSetupDVoList = getDemoListSetupList();
			model.put("ptLstSetupDVoList", ptLstSetupDVoList);
		}
		
		if(ptLstSetupDVoList!=null && !ptLstSetupDVoList.isEmpty()
				&& !(isRoot && !dispAllUsersEnable)){
			
			// 목록 조회
			@SuppressWarnings("unchecked")
			List<OrUserBVo> orUserBVoList = (List<OrUserBVo>)commonDao.queryList(orUserBVo);
			
			// 개인정보 맵 조회
			Map<Integer, Map<String, Object>> userPsnInfoMap = queryUserPsnInfoMap(orUserBVoList);
			
			// 사용자 정보 및 개인정보를 맵으로 합쳐 uInfoList 에 저장함
			Map<String, Object> pInfo, uInfo;
			List<Map<String, Object>> orUserMapList = new ArrayList<Map<String, Object>>();
			for(OrUserBVo storedOrUserBVo :orUserBVoList){
				uInfo = VoUtil.toMap(storedOrUserBVo, null);
				pInfo = userPsnInfoMap.get(Hash.hashUid(storedOrUserBVo.getOdurUid()));
				if(pInfo!=null) uInfo.putAll(pInfo);
				orUserMapList.add(uInfo);
			}
			model.put("orUserMapList", orUserMapList);
		}
	}
	
	private List<PtLstSetupDVo> getDemoListSetupList(){
		// 데모용 하드코딩
		List<PtLstSetupDVo> ptLstSetupDVoList = new ArrayList<PtLstSetupDVo>();
		
		PtLstSetupDVo ptLstSetupDVo;
		
		ptLstSetupDVo = new PtLstSetupDVo();
		ptLstSetupDVo.setAtrbId("rescNm");
		ptLstSetupDVo.setMsgId("or.cols.name");
		ptLstSetupDVo.setWdthPerc("");
		ptLstSetupDVo.setAlnVa("center");
//		ptLstSetupDVo.setSortOptVa("value");
//		ptLstSetupDVo.setDataSortVa("");
		ptLstSetupDVo.setDispYn("Y");
		ptLstSetupDVoList.add(ptLstSetupDVo);
		

		ptLstSetupDVo = new PtLstSetupDVo();
		ptLstSetupDVo.setAtrbId("titleNm");
		ptLstSetupDVo.setMsgId("or.term.title");
		ptLstSetupDVo.setWdthPerc("30%");
		ptLstSetupDVo.setAlnVa("center");
//		ptLstSetupDVo.setSortOptVa("value");
//		ptLstSetupDVo.setDataSortVa("");
		ptLstSetupDVo.setDispYn("Y");
		ptLstSetupDVoList.add(ptLstSetupDVo);
		
		ptLstSetupDVo = new PtLstSetupDVo();
		ptLstSetupDVo.setAtrbId("positNm");
		ptLstSetupDVo.setMsgId("or.term.posit");
		ptLstSetupDVo.setWdthPerc("30%");
		ptLstSetupDVo.setAlnVa("center");
//		ptLstSetupDVo.setSortOptVa("value");
//		ptLstSetupDVo.setDataSortVa("");
		ptLstSetupDVo.setDispYn("Y");
		ptLstSetupDVoList.add(ptLstSetupDVo);

		ptLstSetupDVo = new PtLstSetupDVo();
		ptLstSetupDVo.setAtrbId("sortOrdr");
		ptLstSetupDVo.setMsgId("cols.sortOrdr");
		ptLstSetupDVo.setWdthPerc("");
		ptLstSetupDVo.setAlnVa("center");
		ptLstSetupDVo.setSortOptVa("value");
		ptLstSetupDVo.setDataSortVa("asc");
		ptLstSetupDVo.setDispYn("N");
		ptLstSetupDVoList.add(ptLstSetupDVo);
		
		return ptLstSetupDVoList;
	}
	
	/** 사용자 목록에 해당하는 사용자 정보맵을 model에 담음 - 설정에 따른 화면 구현용 */
	public void setUserMapByUserUids(String lstSetupMetaId, List<String> userUidList, String langTypCd,
			ModelMap model) throws SQLException, CmException, IOException  {
		
		if(lstSetupMetaId==null || lstSetupMetaId.isEmpty()) return;
		
		// 리스트 환경 설정 - 세팅
		List<PtLstSetupDVo> ptLstSetupDVoList = setListQueryOptions(null, lstSetupMetaId);
		model.put("ptLstSetupDVoList", ptLstSetupDVoList);
		
		if(userUidList==null || userUidList.isEmpty()) return;
		
		// 사용자기본(OR_USER_B) 테이블 조회 후 맵으로 전환
		OrUserBVo orUserBVo = new OrUserBVo();
		orUserBVo.setQueryLang(langTypCd);
		orUserBVo.setUserUidList(userUidList);
		@SuppressWarnings("unchecked")
		List<OrUserBVo> orUserBVoList = (List<OrUserBVo>)commonDao.queryList(orUserBVo);
		Map<Integer, OrUserBVo> userMap = new HashMap<Integer, OrUserBVo>();
		int i, size = orUserBVoList==null ? 0 : orUserBVoList.size();
		for(i=0;i<size;i++){
			orUserBVo = orUserBVoList.get(i);
			userMap.put(Hash.hashUid(orUserBVo.getUserUid()), orUserBVo);
		}
		// 개인정보 맵 조회
		Map<Integer, Map<String, Object>> userPsnInfoMap = queryUserPsnInfoMap(orUserBVoList);
		
		// 사용자 정보 및 개인정보를 맵으로 합쳐 uInfoList 에 저장함
		Map<String, Object> pInfo, uInfo;
		List<Map<String, Object>> orUserMapList = new ArrayList<Map<String, Object>>();
		for(String userUid : userUidList){
			orUserBVo = userMap.get(Hash.hashUid(userUid));
			if(orUserBVo!=null){
				uInfo = VoUtil.toMap(orUserBVo, null);
				pInfo = userPsnInfoMap.get(Hash.hashUid(orUserBVo.getOdurUid()));
				if(pInfo!=null) uInfo.putAll(pInfo);
				orUserMapList.add(uInfo);
			}
		}
		model.put("orUserMapList", orUserMapList);
	}
	
	/** 리스트 환경 설정 - 에 따른 정렬순서 세팅 */
	public List<PtLstSetupDVo> setListQueryOptions(OrUserBVo orUserBVo, String lstSetupMetaId) throws SQLException{
		// 리스트 옵션 조회
		List<PtLstSetupDVo> ptLstSetupDVoList = ptSysSvc.getPtLstSetupDVoList(lstSetupMetaId);
		if(orUserBVo==null) return ptLstSetupDVoList;
		
		// 정렬순서용 설정 찾기
		PtLstSetupDVo ptLstSetupDVo = null;
		for(PtLstSetupDVo storedPtLstSetupDVo : ptLstSetupDVoList){
			if(storedPtLstSetupDVo.getDataSortVa() != null && !storedPtLstSetupDVo.getDataSortVa().isEmpty()){
				ptLstSetupDVo = storedPtLstSetupDVo;
				break;
			}
		}
		
		// 정렬순서 세팅
		if(ptLstSetupDVo!=null){
			String atrbId = ptLstSetupDVo.getAtrbId();//속성ID
			String sortOptVa = ptLstSetupDVo.getSortOptVa();//정렬옵션값 - code:코드 테이블의 정렬순서, name:텍스트순
			String dataSortVa = ptLstSetupDVo.getDataSortVa();//데이터정렬값 - asc:내림차순, desc:올림차순
			dataSortVa = dataSortVa==null||dataSortVa.isEmpty() ? "ASC" : dataSortVa.toUpperCase();
			
			if("code".equals(sortOptVa)){
				String column = StringUtil.fromCamelNotation(atrbId.substring(0, atrbId.length()-2)+"Cd");
				orUserBVo.setSortOrdrOpt("(SELECT SORT_ORDR FROM PT_CD_B WHERE CLS_CD='"+column+"' AND CD=T."+column+")");
				orUserBVo.setOrderBy("SORT_ORDR "+dataSortVa);
			} else {
				orUserBVo.setOrderBy(StringUtil.fromCamelNotation(atrbId)+" "+dataSortVa);
			}
		}
		
		return ptLstSetupDVoList;
	}
	
	
	/** 리소스기본(OR_RESC_B) 테이블 조회 : 모델에 rescId+"_"+langTypCd 로 세팅함 */
	public void queryRescBVo(String rescId, ModelMap model) throws SQLException {
		
		// 회사명 언어별 리소스 조회
		if(rescId!=null && !rescId.isEmpty()){
			OrRescBVo orRescBVo = new OrRescBVo();
			orRescBVo.setRescId(rescId);
			@SuppressWarnings("unchecked")
			List<OrRescBVo> orRescBVoList = (List<OrRescBVo>)commonDao.queryList(orRescBVo);
			
			// JSP 출력을 위해 출력용 파라미터로 넘김
			int i, size = orRescBVoList==null ? 0 : orRescBVoList.size();
			for(i=0;i<size;i++){
				orRescBVo = orRescBVoList.get(i);
				model.put(orRescBVo.getRescId()+"_"+orRescBVo.getLangTypCd(), orRescBVo.getRescVa());
			}
		}
	}
	
	/** 조직도 트리의 아이콘 조회 - javascript의 object 선언 형태 */
	public String getOrgTreeIcon(String langTypCd) throws SQLException{
		// 조직구분코드 - 조회
		List<PtCdBVo> orgTypCdList = ptCmSvc.getCdList("ORG_TYP_CD", langTypCd, "Y");
		StringBuilder builder = new StringBuilder(64);
		builder.append('{');
		int i, size = orgTypCdList==null ? 0 : orgTypCdList.size();
		PtCdBVo cdVo;
		for(i=0;i<size;i++){
			cdVo = orgTypCdList.get(i);
			if(i>0) builder.append(',');
			builder.append('\"').append(cdVo.getCd()).append("\":\"")
				.append(EscapeUtil.escapeValue(cdVo.getRescNm())).append('\"');
		}
		builder.append('}');
		return builder.toString();
	}
	
	/** 계열사 전사 보기 */
	public List<OrOrgBVo> getAffiliateOrgTreeList(String compId, String useYn, String langTypCd) throws SQLException{
		// 계열사
		PtCompBVo ptCompBVo = ptCmSvc.getPtCompBVo(compId, langTypCd);
		List<String> affiliateIds = ptCompBVo.getAffiliateIds();
		if(affiliateIds==null) return getOrgTreeList(compId, useYn, langTypCd);
		
		//조직기본(OR_ORG_B) 테이블
		OrOrgBVo orOrgBVo = new OrOrgBVo();
		orOrgBVo.setCompIdList(affiliateIds);
		orOrgBVo.setUseYn(useYn);
		orOrgBVo.setQueryLang(langTypCd);
		
		orOrgBVo.setOrderBy("T.ORG_PID, T.SORT_ORDR");
		@SuppressWarnings("unchecked")
		List<OrOrgBVo> orOrgBVoList = (List<OrOrgBVo>)commonDao.queryList(orOrgBVo);
		return orOrgBVoList;
	}
	
	/** 조직도 트리 조회 */
	public List<OrOrgBVo> getOrgTreeList(String compId, String useYn, String langTypCd) throws SQLException{
		//조직기본(OR_ORG_B) 테이블
		OrOrgBVo orOrgBVo = new OrOrgBVo();
		orOrgBVo.setCompId(compId);
		orOrgBVo.setUseYn(useYn);
		orOrgBVo.setQueryLang(langTypCd);
		
		orOrgBVo.setOrderBy("T.ORG_PID, T.SORT_ORDR");
		@SuppressWarnings("unchecked")
		List<OrOrgBVo> orOrgBVoList = (List<OrOrgBVo>)commonDao.queryList(orOrgBVo);
		return orOrgBVoList;
	}
	
	/** 조직도 트리 조회 */
	public List<OrOrgBVo> getForeignOrgTreeList(String compId, String useYn, String langTypCd) throws SQLException{
		
		// 시스템 정책 조회
		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
		
		// 계열사 사용
		if("Y".equals(sysPlocMap.get("affiliatesEnable"))){
			
			// 계열사 중 자기 회사를 제외한것
			PtCompBVo ptCompBVo = ptCmSvc.getPtCompBVo(compId, langTypCd);
			
			// 자신을 제외한 계열사 compId
			List<String> foreignCompIds = new ArrayList<String>();
			if(ptCompBVo.getAffiliateIds()!=null){
				for(String affiliateId : ptCompBVo.getAffiliateIds()){
					if(!compId.equals(affiliateId)){
						foreignCompIds.add(affiliateId);
					}
				}
			}
			
			if(foreignCompIds.isEmpty()) return null;
			
			//조직기본(OR_ORG_B) 테이블
			OrOrgBVo orOrgBVo = new OrOrgBVo();
			orOrgBVo.setCompIdList(foreignCompIds);
			orOrgBVo.setUseYn(useYn);
			orOrgBVo.setQueryLang(langTypCd);
			
			orOrgBVo.setOrderBy("T.ORG_PID, T.SORT_ORDR");
			@SuppressWarnings("unchecked")
			List<OrOrgBVo> orOrgBVoList = (List<OrOrgBVo>)commonDao.queryList(orOrgBVo);
			return orOrgBVoList;
			
		} else {
			
			//조직기본(OR_ORG_B) 테이블
			OrOrgBVo orOrgBVo = new OrOrgBVo();
			orOrgBVo.setForeignCompId(compId);
			orOrgBVo.setUseYn(useYn);
			orOrgBVo.setQueryLang(langTypCd);
			
			orOrgBVo.setOrderBy("T.ORG_PID, T.SORT_ORDR");
			@SuppressWarnings("unchecked")
			List<OrOrgBVo> orOrgBVoList = (List<OrOrgBVo>)commonDao.queryList(orOrgBVo);
			return orOrgBVoList;
		}
	}
	
	/** 하나의 부서(파트포함) 조직도 조회 */
	public List<OrOrgBVo> getOneTreeList(String orgId, String langTypCd) throws SQLException{
		
		Map<Integer, OrOrgTreeVo> orgMap = getOrgMap(langTypCd);
		Map<Integer, List<String>> orgSubListMap = getOrgSubListMap(langTypCd);
		List<OrOrgBVo> returnList = new ArrayList<OrOrgBVo>();
		setOneTreeList(orgId, returnList, orgSubListMap, orgMap, true);
		if(!returnList.isEmpty()){
			returnList.get(0).setOrgPid("ROOT");
		}
		return returnList;
	}
	/** 하나의 부서(파트포함) 트리 정보를 returnList 에 담음 */
	private void setOneTreeList(String orgId, List<OrOrgBVo> returnList, 
			Map<Integer, List<String>> orgSubListMap, Map<Integer, OrOrgTreeVo> orgMap,
			boolean first){
		int hashId = Hash.hashId(orgId);
		OrOrgTreeVo orOrgTreeVo = orgMap.get(hashId);
		boolean isPart = false;
		if(orOrgTreeVo!=null){
			isPart = first || "P".equals(orOrgTreeVo.getOrgTypCd());
			if(isPart){
				OrOrgBVo orOrgBVo = new OrOrgBVo();
				orOrgBVo.fromMap(orOrgTreeVo.toMap());
				returnList.add(orOrgBVo);
			}
		}
		if(isPart){
			List<String> orgSubList = orgSubListMap.get(hashId);
			if(orgSubList!=null){
				for(String subOrgId : orgSubList){
					setOneTreeList(subOrgId, returnList, orgSubListMap, orgMap, false);
				}
			}
		}
	}
	
	/** 상위 조직도 조회 */
	public List<OrOrgBVo> getUpTreeList(String orgId, String langTypCd) throws SQLException {
		List<OrOrgBVo> list = new ArrayList<OrOrgBVo>();
		LinkedList<OrOrgTreeVo> treeList = new LinkedList<OrOrgTreeVo>();
		
		OrOrgBVo orOrgBVo;
		OrOrgTreeVo orOrgTreeVo;
		Map<Integer, OrOrgTreeVo> orgMap = getOrgMap(langTypCd);
		while((orOrgTreeVo = orgMap.get(Hash.hashId(orgId))) != null){
			treeList.addFirst(orOrgTreeVo);
			orgId = orOrgTreeVo.getOrgPid();
		}
		
		if(treeList.isEmpty()) return null;
		int i, size = treeList.size();
		for(i=0;i<size;i++){
			orOrgBVo = new OrOrgBVo();
			orOrgBVo.fromMap(treeList.get(i).toMap());
			list.add(orOrgBVo);
		}
		
		return list;
	}
	/** 상위 & 방계 조직도 조회 */
	public List<OrOrgBVo> getUpAndBrotherTreeList(String orgId, boolean noPart, String langTypCd) throws SQLException {
		List<OrOrgBVo> list = new ArrayList<OrOrgBVo>();
		LinkedList<OrOrgTreeVo> treeList = new LinkedList<OrOrgTreeVo>();
		
		Map<Integer, OrOrgTreeVo> orgMap = getOrgMap(langTypCd);
		
		// 파트인지 - 파트가 아닐때까지 올림
		String currOrgid = orgId;
		OrOrgTreeVo orOrgTreeVo;
		if(noPart){
			while((orOrgTreeVo = orgMap.get(Hash.hashId(currOrgid))) != null){
				if("P".equals(orOrgTreeVo.getOrgTypCd())){
					currOrgid = orOrgTreeVo.getOrgPid();
				} else {
					break;
				}
			}
		}
		
		// 상위
		boolean first = true;
		OrOrgBVo orOrgBVo;
		OrOrgTreeVo pOrOrgTreeVo = null, subOrOrgTreeVo;
		while((orOrgTreeVo = orgMap.get(Hash.hashId(currOrgid))) != null){
			treeList.addFirst(orOrgTreeVo);
			currOrgid = orOrgTreeVo.getOrgPid();
			if(first) first = false;
			else if(pOrOrgTreeVo == null) pOrOrgTreeVo = orOrgTreeVo;
		}
		
		// 방계
		if(pOrOrgTreeVo != null){
			Map<Integer, List<String>> orgSubListMap = getOrgSubListMap(langTypCd);
			List<String> orgSubList = orgSubListMap.get(Hash.hashId(pOrOrgTreeVo.getOrgId()));
			if(orgSubList!=null && !orgSubList.isEmpty()){
				treeList.removeLast();
				for(String subOrgId : orgSubList){
					subOrOrgTreeVo = orgMap.get(Hash.hashId(subOrgId));
					if(!subOrOrgTreeVo.isDisable()){
						treeList.add(subOrOrgTreeVo);
					}
				}
			}
		}
		
		if(treeList.isEmpty()) return null;
		int i, size = treeList.size();
		for(i=0;i<size;i++){
			orOrgBVo = new OrOrgBVo();
			orOrgBVo.fromMap(treeList.get(i).toMap());
			list.add(orOrgBVo);
		}
		
		return list;
	}
	
	/** 하위 조직도 조회 */
	public List<OrOrgBVo> getDownTreeList(String orgId, String langTypCd) throws SQLException{
		Map<Integer, OrOrgTreeVo> orgMap = getOrgMap(langTypCd);
		Map<Integer, List<String>> orgSubListMap = getOrgSubListMap(langTypCd);
		List<OrOrgBVo> returnList = new ArrayList<OrOrgBVo>();
		setDownTreeList(orgId, returnList, orgSubListMap, orgMap);
		if(!returnList.isEmpty()){
			returnList.get(0).setOrgPid("ROOT");
		}
		return returnList;
	}
	/** 하위 조직도 정보를 returnList 에 담음 */
	private void setDownTreeList(String orgId, List<OrOrgBVo> returnList, 
			Map<Integer, List<String>> orgSubListMap, Map<Integer, OrOrgTreeVo> orgMap){
		int hashId = Hash.hashId(orgId);
		OrOrgTreeVo orOrgTreeVo = orgMap.get(hashId);
		if(orOrgTreeVo==null || Boolean.TRUE.equals(orOrgTreeVo.isDisable())) return;
		
		OrOrgBVo orOrgBVo = new OrOrgBVo();
		orOrgBVo.fromMap(orOrgTreeVo.toMap());
		//if(orOrgTreeVo.isDisable()!=null && orOrgTreeVo.isDisable()) orOrgBVo.setUseYn("N"); // 메신저 조직도 동기화시 사용여부가 필요해서 추가됨.
		returnList.add(orOrgBVo);
		
		List<String> orgSubList = orgSubListMap.get(hashId);
		if(orgSubList!=null){
			for(String subOrgId : orgSubList){
				setDownTreeList(subOrgId, returnList, orgSubListMap, orgMap);
			}
		}
	}
	
	/** 라이센스 체크용 - 사용자가 라이센스 없는 사용자면 userIndex : -1 로 세팅됨 */
	public void setIndex(String odurUid, HttpServletRequest request) throws IOException, SQLException {
		if(!FinderUtil.initialized()) setUsers();
		Integer index = FinderUtil.find(odurUid, true);
		if(index<0){
			throw new IOException(new LicenseLimitException());
		} else {
			request.setAttribute("userIndex", FinderUtil.find(odurUid, true));
		}
	}
	
	/** 라이센스 체크용 - 사용자UID를 FinderUtil 에 저장함 */
	public void setUsers() throws SQLException {
		OrUserBVo orUserBVo = new OrUserBVo();
		orUserBVo.setAduTypCd("01");//겸직구분코드 - 01:원직, 02:겸직, 03:파견직
		orUserBVo.setUserStatCdList(viewUserStatCdList);
		orUserBVo.setOrderBy("REG_DT, USER_UID");
		@SuppressWarnings("unchecked")
		List<OrUserBVo> orUserBVoList = (List<OrUserBVo>)commonDao.queryList(orUserBVo);
		
		FinderUtil.setUsers(orUserBVoList);
	}
	
	/** 결재용 조직 상세 정보 조회 */
	public void setOrgDetl(String orgId, String langTypCd, ModelMap model) throws SQLException{
		
		if(orgId==null || orgId.isEmpty()) return;
		
		// 조직기본(OR_ORG_B) 테이블 - 조회
		OrOrgBVo orOrgBVo = new OrOrgBVo();
		orOrgBVo.setOrgId(orgId);
		orOrgBVo.setQueryLang(langTypCd);
		orOrgBVo = (OrOrgBVo)commonDao.queryVo(orOrgBVo);
		model.put("orOrgBVo", orOrgBVo);
		
		// 조직연락처상세(OR_ORG_CNTC_D) 테이블 - 조회
		OrOrgCntcDVo orOrgCntcDVo = new OrOrgCntcDVo();
		orOrgCntcDVo.setOrgId(orgId);
		orOrgCntcDVo.setQueryLang(langTypCd);
		orOrgCntcDVo = (OrOrgCntcDVo)commonDao.queryVo(orOrgCntcDVo);
		if(orOrgCntcDVo!=null) model.put("orOrgCntcDVo", orOrgCntcDVo);
		
		// 조직결재상세(OR_ORG_APV_D) 테이블 - 조회
		OrOrgApvDVo orOrgApvDVo = new OrOrgApvDVo();
		orOrgApvDVo.setOrgId(orgId);
		orOrgApvDVo.setQueryLang(langTypCd);
		orOrgApvDVo = (OrOrgApvDVo)commonDao.queryVo(orOrgApvDVo);
		if(orOrgApvDVo!=null){
			model.put("orOrgApvDVo", orOrgApvDVo);
			
			List<String> rescIdList = new ArrayList<String>();
			if(orOrgApvDVo.getHodpPositRescId()!=null && !orOrgApvDVo.getHodpPositRescId().isEmpty()){
				rescIdList.add(orOrgApvDVo.getHodpPositRescId());
			}
			if(orOrgBVo.getOrgAbbrRescId()!=null && !orOrgBVo.getOrgAbbrRescId().isEmpty()){
				rescIdList.add(orOrgBVo.getOrgAbbrRescId());
			}
			if(orOrgApvDVo.getSendrNmRescId()!=null && !orOrgApvDVo.getSendrNmRescId().isEmpty()){
				rescIdList.add(orOrgApvDVo.getSendrNmRescId());
			}
			// 리소스 조회 모델에 세팅
			if(!rescIdList.isEmpty()){
				OrRescBVo orRescBVo = new OrRescBVo();
				orRescBVo.setRescIdList(rescIdList);
				@SuppressWarnings("unchecked")
				List<OrRescBVo> rescBVoList = (List<OrRescBVo>)commonDao.queryList(orRescBVo);
				if(rescBVoList!=null){
					for(OrRescBVo storedOrRescBVo : rescBVoList){
						model.put(storedOrRescBVo.getRescId()+"_"+storedOrRescBVo.getLangTypCd(), storedOrRescBVo.getRescVa());
					}
				}
			}
		}
	}
	
	/** 이미지(관인, 서명인) 관리할 부서 조회 */
	public List<OrOrgTreeVo> getOrgListToMngImage(String deptId, String langTypCd) throws SQLException {
		
		List<OrOrgTreeVo> returnList = new ArrayList<OrOrgTreeVo>();
		
		OrOrgTreeVo orOrgTreeVo;
		Map<Integer, OrOrgTreeVo> orgMap = getOrgMap(langTypCd);
		
		int hashId, deptHashId = Hash.hashId(deptId);
		List<Integer> dupCheckList = new ArrayList<Integer>();
		
		// 해당 부서를 문서과로 설정한 기관 조회
		OrOrgApvDVo orOrgApvDVo = new OrOrgApvDVo();
		orOrgApvDVo.setCrdOrgId(deptId);
		@SuppressWarnings("unchecked")
		List<OrOrgApvDVo> crdOrOrgApvDVoList = (List<OrOrgApvDVo>)commonDao.queryList(orOrgApvDVo);
		if(crdOrOrgApvDVoList!=null){
			for(OrOrgApvDVo crdOrOrgApvDVo : crdOrOrgApvDVoList){
				hashId = Hash.hashId(crdOrOrgApvDVo.getOrgId());
				orOrgTreeVo = orgMap.get(hashId);
				if(orOrgTreeVo!=null){
					if("C".equals(orOrgTreeVo.getOrgTypCd()) || "G".equals(orOrgTreeVo.getOrgTypCd())){// 조직구분코드 - C:회사, G:기관, D:부서, P:파트
						if(deptHashId!=hashId && !dupCheckList.contains(hashId)){
							returnList.add(orOrgTreeVo);
							dupCheckList.add(hashId);
						}
					}
				}
			}
		}
		
		// 해당 부서를 대리과로 지정한 문서과의 기관 조회
		orOrgApvDVo = new OrOrgApvDVo();
		orOrgApvDVo.setAgnCrdOrgId(deptId);
		@SuppressWarnings("unchecked")
		List<OrOrgApvDVo> agnCrdOrOrgApvDVoList = (List<OrOrgApvDVo>)commonDao.queryList(orOrgApvDVo);
		if(agnCrdOrOrgApvDVoList!=null){
			for(OrOrgApvDVo agnCrdOrOrgApvDVo : agnCrdOrOrgApvDVoList){
				hashId = Hash.hashId(agnCrdOrOrgApvDVo.getOrgId());
				orOrgTreeVo = orgMap.get(hashId);
				if(orOrgTreeVo!=null){
					if("C".equals(orOrgTreeVo.getOrgTypCd()) || "G".equals(orOrgTreeVo.getOrgTypCd())){// 조직구분코드 - C:회사, G:기관, D:부서, P:파트
						if(deptHashId!=hashId && !dupCheckList.contains(hashId)){
							returnList.add(orOrgTreeVo);
							dupCheckList.add(hashId);
						}
					}
				}
			}
		}
		
		orOrgTreeVo = orgMap.get(deptHashId);
		returnList.add(orOrgTreeVo);
		
		return returnList;
	}
	
	/** 부서 정보 조회(캐쉬) */
	public OrOrgTreeVo getOrgTreeVo(String deptId, String langTypCd) throws SQLException{
		Map<Integer, OrOrgTreeVo> orgMap = getOrgMap(langTypCd);
		return orgMap.get(Hash.hashId(deptId));
	}
	
	/** 사용중인 사용자 목로(UID) 조회 */
	public List<String> getActiveUserList(String compId) throws SQLException{
		
		List<String> licensedUserList = new ArrayList<String>();
		
		OrUserBVo orUserBVo = new OrUserBVo();
		orUserBVo.setCompId(compId);
		orUserBVo.setAduTypCd("01");//겸직구분코드 - 01:원직, 02:겸직, 03:파견직
		
		// 사용자상태코드 - 01:사용신청, 02:근무중, 03:휴직, 04:정직, 05:퇴직, 11:해제, 99:삭제
		List<String> userStatCdList = new ArrayList<String>();
		userStatCdList.add("02");
		userStatCdList.add("03");
		userStatCdList.add("04");
		
		@SuppressWarnings("unchecked")
		List<OrUserBVo> orUserBVoList = (List<OrUserBVo>)commonDao.queryList(orUserBVo);
		if(orUserBVoList != null){
			for(OrUserBVo storedOrUserBVo : orUserBVoList){
				licensedUserList.add(storedOrUserBVo.getUserUid());
			}
		}
		
		return licensedUserList;
	}
	
	/** 부서별, 역할코드별 사용자 목록 조회 */
	public List<OrUserBVo> getUserListByDeptIdRoleCd(String deptId, String roleCd) throws SQLException{
		OrUserBVo orUserBVo = new OrUserBVo();
		orUserBVo.setDeptId(deptId);
		orUserBVo.setRoleCd(roleCd);
		@SuppressWarnings("unchecked")
		List<OrUserBVo> orUserBVoList = (List<OrUserBVo>)commonDao.queryList(orUserBVo);
		return orUserBVoList;
	}
	
	/** 이메일 조회 */
	public String getEmail(String odurUid) throws SQLException, IOException, CmException{
		// 사용자개인정보상세(OR_USER_PINFO_D) 테이블 - 조회
		OrUserPinfoDVo orUserPinfoDVo = new OrUserPinfoDVo();
		orUserPinfoDVo.setOdurUid(odurUid);
		orUserPinfoDVo = (OrUserPinfoDVo)commonDao.queryVo(orUserPinfoDVo);
		if(orUserPinfoDVo != null && orUserPinfoDVo.getEmailEnc()!=null){
			return cryptoSvc.decryptPersanal(orUserPinfoDVo.getEmailEnc());
		}
		return null;
	}
	
	/** ID 통합 생성 */
	public String createId(String tableName) throws SQLException{
		
		if("OR_RESC_B".equals(tableName)){
			return commonSvc.createId(tableName, 'R', 8);
		} else if("OR_ORG_B".equals(tableName)){
			return commonSvc.createId(tableName, 'O', 8);
		} else if("OR_USER_B".equals(tableName)){
			return commonSvc.createId(tableName, 'U', 8);
		} else if("OR_OFSE_D".equals(tableName)){
			return commonSvc.createId(tableName, 'F', 8);
		}
		return null;
	}
}
