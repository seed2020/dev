package com.innobiz.orange.web.pt.svc;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.innobiz.orange.web.cm.config.CacheConfig;
import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.StringUtil;
import com.innobiz.orange.web.pt.vo.PtCdBVo;
import com.innobiz.orange.web.pt.vo.PtCompBVo;
import com.innobiz.orange.web.pt.vo.PtCompLangRVo;
import com.innobiz.orange.web.pt.vo.PtSsoMsgTVo;
import com.innobiz.orange.web.pt.vo.PtSysSetupDVo;

/** 포털 공통 서비스(코드,회사정보 등) */
@Service
public class PtCmSvc {
	
	/** 캐쉬 서비스 */
	@Autowired
	private PtCacheSvc ptCacheSvc;
	
	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;

	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
	/** 코드조회 */
	public PtCdBVo getCd(String clsCd, String langTypCd, String cd) throws SQLException{
		Map<String, PtCdBVo> map = getCdMap(clsCd, langTypCd);
		if(map==null) return null;
		return map.get(cd);
	}
	
	/** 코드 리소스 조회 */
	public String getCdRescNm(String clsCd, String langTypCd, String cd) throws SQLException{
		Map<String, PtCdBVo> map = getCdMap(clsCd, langTypCd);
		if(map==null) return null;
		PtCdBVo ptCdBVo = map.get(cd);
		if(ptCdBVo==null) return null;
		return ptCdBVo.getRescNm();
	}
	
	/** 코드 맵 조회 */
	public Map<String, PtCdBVo> getCdMap(String clsCd, String langTypCd) throws SQLException {
		@SuppressWarnings("unchecked")
		Map<String, PtCdBVo> map = (Map<String, PtCdBVo>)ptCacheSvc.getCache(CacheConfig.CODE_MAP, langTypCd, clsCd, 200);
		if(map!=null) return map;
		
		map = new HashMap<String, PtCdBVo>();
		List<PtCdBVo> list = getAllCdList(clsCd, langTypCd);
		if(list!=null){
			for(PtCdBVo ptCdBVo : list){
				// 메모리 사이즈 점검
				ptCdBVo.setRegrUid(null);
				ptCdBVo.setRegDt(null);
				ptCdBVo.setModrUid(null);
				ptCdBVo.setModDt(null);
				ptCdBVo.setRegrNm(null);
				ptCdBVo.setModrNm(null);
				
				map.put(ptCdBVo.getCd(), ptCdBVo);
			}
			ptCacheSvc.setCache(CacheConfig.CODE_MAP, langTypCd, clsCd, map);
			return map;
		}
		return null;
	}
	
	/** 코드 목록 조회 - 사용안함 포함 */
	public List<PtCdBVo> getAllCdList(String clsCd, String langTypCd) throws SQLException {
		
		@SuppressWarnings("unchecked")
		List<PtCdBVo> list = (List<PtCdBVo>)ptCacheSvc.getCache(CacheConfig.CODE, langTypCd, clsCd, 200);
		if(list!=null) return copyPtCdBVoList(list);
		
		// 시스템 정책 조회
		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
		
		PtCdBVo ptCdBVo = new PtCdBVo();
		ptCdBVo.setClsCd(clsCd);
		ptCdBVo.setQueryLang(langTypCd);
		if("GRADE_CD".equals(clsCd) || "TITLE_CD".equals(clsCd) || "POSIT_CD".equals(clsCd) || "DUTY_CD".equals(clsCd)){
			if("Y".equals(sysPlocMap.get("codeByCompEnable"))){
				ptCdBVo.setOrderBy("COMP_IDS, SORT_ORDR, CD");
			} else {
				ptCdBVo.setOrderBy("SORT_ORDR, CD");
			}
		} else if("SECUL_CD".equals(clsCd)){
			if("Y".equals(sysPlocMap.get("seculByCompEnable"))){
				ptCdBVo.setOrderBy("COMP_IDS, SORT_ORDR, CD");
			} else {
				ptCdBVo.setOrderBy("SORT_ORDR, CD");
			}
		} else {
			ptCdBVo.setOrderBy("SORT_ORDR, CD");
		}
		
		@SuppressWarnings("unchecked")
		List<PtCdBVo> storedList = (List<PtCdBVo>)commonDao.queryList(ptCdBVo);
		ptCacheSvc.setCache(CacheConfig.CODE, langTypCd, clsCd, storedList);
		return copyPtCdBVoList(storedList);
	}
	/** 코드 목록 조회 */
	public List<PtCdBVo> getCdList(String clsCd, String langTypCd, String useYn) throws SQLException {
		if(useYn==null) return getAllCdList(clsCd, langTypCd);
		List<PtCdBVo> returnList = new ArrayList<PtCdBVo>();
		for(PtCdBVo ptCdBVo : getAllCdList(clsCd, langTypCd)){
			if(useYn!=null && !useYn.equals(ptCdBVo.getUseYn())){
				continue;
			}
			returnList.add(ptCdBVo);
		}
		return returnList;
	}
	/** 코드 목록을 복사함 */
	private List<PtCdBVo> copyPtCdBVoList(List<PtCdBVo> list){
		if(list!=null){
			List<PtCdBVo> returnList = new ArrayList<PtCdBVo>();
			for(PtCdBVo ptCdBVo: list){
				returnList.add(ptCdBVo);
			}
			return returnList;
		}
		return null;
	}
	
	/** 해당 회사로 세팅된 코드만 조회 - in */
	public List<PtCdBVo> getCdListByCompId(String clsCd, String langTypCd, String compId, String useYn) throws SQLException{
		String compIds;
		List<PtCdBVo> returnList = new ArrayList<PtCdBVo>();
		for(PtCdBVo ptCdBVo : getAllCdList(clsCd, langTypCd)){
			if(useYn!=null && !useYn.equals(ptCdBVo.getUseYn())){
				continue;
			}
			compIds = ptCdBVo.getCompIds();
			if(compId==null || compIds==null || compIds.isEmpty()){
				returnList.add(ptCdBVo);
			} else if(isInString(compIds, compId)){
				returnList.add(ptCdBVo);
			}
		}
		return returnList;
	}
	
	/** 해당 회사로 세팅된 코드만 조회 - equals */
	public List<PtCdBVo> getCdListEqCompId(String clsCd, String langTypCd, String compId, String useYn) throws SQLException{
		String compIds;
		List<PtCdBVo> returnList = new ArrayList<PtCdBVo>();
		for(PtCdBVo ptCdBVo : getAllCdList(clsCd, langTypCd)){
			if(useYn!=null && !useYn.equals(ptCdBVo.getUseYn())){
				continue;
			}
			if(compId==null){
				returnList.add(ptCdBVo);
			} else {
				compIds = ptCdBVo.getCompIds();
				if(compIds!=null && compIds.equals(compId)){
					returnList.add(ptCdBVo);
				}
			}
		}
		return returnList;
	}
	
	/** 필터된 코드 목록 조회 (compId, refVa1, refVa2 에 해당하는 코드 목록 조회) */
	public List<PtCdBVo> getFilteredCdList(String clsCd, String langTypCd, String compId, String refVa1, String refVa2, String useYn) throws SQLException{
		
		List<PtCdBVo> returnList = new ArrayList<PtCdBVo>();
		for(PtCdBVo ptCdBVo : getAllCdList(clsCd, langTypCd)){
			if(compId!=null && !isInString(ptCdBVo.getCompIds(), compId)){
				continue;
			}
			if(refVa1!=null && !isInString(ptCdBVo.getRefVa1(), refVa1)){
				continue;
			}
			if(refVa2!=null && !isInString(ptCdBVo.getRefVa2(), refVa2)){
				continue;
			}
			if(useYn!=null && !useYn.equals(ptCdBVo.getUseYn())){
				continue;
			}
			returnList.add(ptCdBVo);
		}
		return returnList;
	}
	
	/** 회사목록을 리턴, compNm 이 null이 아니면 like 검색해서 리턴함 */
	public List<PtCompBVo> getFilteredCompList(String compNm, String useYn, String langTypCd) throws SQLException{
		setCompCache(langTypCd);
		
		@SuppressWarnings("unchecked")
		List<PtCompBVo> list = (List<PtCompBVo>)ptCacheSvc.getCache(CacheConfig.COMP, langTypCd, "LIST", 30);
		// compNm==null 일 경우도 목록을 복사해서 보냄
		List<PtCompBVo> returnList = new ArrayList<PtCompBVo>();
		if(list != null){
			for(PtCompBVo ptCompBVo : list){
				if(useYn!=null){
					if(!useYn.equals(ptCompBVo.getUseYn())){
						continue;
					}
				}
				if(compNm!=null){
					if(ptCompBVo.getRescNm()!=null && ptCompBVo.getRescNm().indexOf(compNm) < 0){
						continue;
					}
				}
				returnList.add(ptCompBVo);
			}
		}
		return returnList;
	}
	
//	/** 첫번째 회사 리턴 */
//	public String getFirstCompId(String langTypCd) throws SQLException{
//		List<PtCompBVo> ptCompBVoList = getFilteredCompList(null, "Y", langTypCd);
//		if(ptCompBVoList!=null && !ptCompBVoList.isEmpty()){
//			return ptCompBVoList.get(0).getCompId();
//		}
//		return null;
//	}
	
	/** 회사정보 조회 */
	public PtCompBVo getPtCompBVo(String compId, String langTypCd) throws SQLException{
		setCompCache(langTypCd);
		@SuppressWarnings("unchecked")
		Map<String, PtCompBVo> PtCompBVoMap = (Map<String, PtCompBVo>)ptCacheSvc.getCache(CacheConfig.COMP, langTypCd, "MAP", 30);
		return PtCompBVoMap==null ? null : PtCompBVoMap.get(compId);
	}
	
	/** 회사정보 맵 조회 */
	public Map<String, PtCompBVo> getPtCompBVoMap(String langTypCd) throws SQLException{
		setCompCache(langTypCd);
		@SuppressWarnings("unchecked")
		Map<String, PtCompBVo> PtCompBVoMap = (Map<String, PtCompBVo>)ptCacheSvc.getCache(CacheConfig.COMP, langTypCd, "MAP", 30);
		return PtCompBVoMap;
	}
	
	/** 회사별로 설정된 언어 정보 조회 */
	public List<PtCdBVo> getLangTypCdListByCompId(String compId, String langTypCd) throws SQLException{
		
		@SuppressWarnings("unchecked")
		List<PtCdBVo> list = (List<PtCdBVo>)ptCacheSvc.getCache(CacheConfig.COMP_LANG, langTypCd, compId, 30);
		if(list!=null) return copyPtCdBVoList(list);
		
		// 회사언어관계(PT_COMP_LANG_R) 테이블 - SELECT
		PtCompLangRVo ptCompLangRVo = new PtCompLangRVo();
		ptCompLangRVo.setCompId(compId);
		@SuppressWarnings("unchecked")
		List<PtCompLangRVo> ptCompLangRVoList = (List<PtCompLangRVo>)commonDao.queryList(ptCompLangRVo);
		
		// langTypCdList 에 임시 저장 - 코드테이블의 순서로 맞추기 위한것
		List<String> langTypCdList = new ArrayList<String>();
		if(ptCompLangRVoList!=null){
			for(PtCompLangRVo storedPtCompLangRVo : ptCompLangRVoList){
				langTypCdList.add(storedPtCompLangRVo.getLangTypCd());
			}
		}
		
		// 리턴할 언어코드 목록
		List<PtCdBVo> ptCdBVoList = new ArrayList<PtCdBVo>();
		for(PtCdBVo ptCdBVo : getAllCdList("LANG_TYP_CD", langTypCd)){
			if(langTypCdList.contains(ptCdBVo.getCd())){
				ptCdBVoList.add(ptCdBVo);
			}
		}
		// 캐쉬에 저장
		ptCacheSvc.setCache(CacheConfig.COMP_LANG, langTypCd, compId, ptCdBVoList);
		
		return copyPtCdBVoList(ptCdBVoList);
	}
	
	/** 회사 정보를 캐쉬함 */
	private void setCompCache(String langTypCd) throws SQLException{
		
		if(ptCacheSvc.getCache(CacheConfig.COMP, langTypCd, "LIST", 30)==null){
			PtCompBVo ptCompBVo = new PtCompBVo();
			ptCompBVo.setDelYn("N");
			ptCompBVo.setQueryLang(langTypCd);
			ptCompBVo.setOrderBy("SORT_ORDR");
			
			@SuppressWarnings("unchecked")			
			List<PtCompBVo> list = (List<PtCompBVo>)commonDao.queryList(ptCompBVo);
			ptCacheSvc.setCache(CacheConfig.COMP, langTypCd, "LIST", list);
			
			Map<String, PtCompBVo> ptCompBVoMap = new HashMap<String, PtCompBVo>();
			for(PtCompBVo stoaredPtCompBVo : list){
				ptCompBVoMap.put(stoaredPtCompBVo.getCompId(), stoaredPtCompBVo);
			}
			ptCacheSvc.setCache(CacheConfig.COMP, langTypCd, "MAP", ptCompBVoMap);
			
			
			// 계열사 세팅
			
			// 시스템설정상세(PT_SYS_SETUP_D) 테이블 - 계열사 설정 조회
			PtSysSetupDVo ptSysSetupDVo = new PtSysSetupDVo();
			ptSysSetupDVo.setSetupClsId(CacheConfig.COMP_AFFILIATES);
			@SuppressWarnings("unchecked")
			List<PtSysSetupDVo> ptSysSetupDVoList = (List<PtSysSetupDVo>)commonDao.queryList(ptSysSetupDVo);
			
			if(ptSysSetupDVoList != null){
				String setupVa;
				List<String> affiliateIds, affiliateNms;
				
				for(PtSysSetupDVo storedPtSysSetupDVo : ptSysSetupDVoList){
					setupVa = storedPtSysSetupDVo.getSetupVa();
					if(setupVa==null || setupVa.isEmpty()) continue;
					
					affiliateIds = new ArrayList<String>();
					affiliateNms = new ArrayList<String>();
					for(String affiliateId : setupVa.split(",")){
						affiliateId = affiliateId.trim();
						if(affiliateId.isEmpty()) continue;
						
						ptCompBVo = ptCompBVoMap.get(affiliateId);
						if(ptCompBVo==null) continue;
						
						affiliateIds.add(affiliateId);
						affiliateNms.add(ptCompBVo.getRescNm());
						ptCompBVo.setAffiliateIds(affiliateIds);
						ptCompBVo.setAffiliateNms(affiliateNms);
					}
				}
			}
			
		}
	}
	
	/** sso 메세지 생성 - msgId 리턴 */
	public String createSsoMsg(String msgVa) throws SQLException{
		String msgId = StringUtil.getNextIntHexa();
		PtSsoMsgTVo ptSsoMsgTVo = new PtSsoMsgTVo();
		ptSsoMsgTVo.setMsgId(msgId);
		ptSsoMsgTVo.setMsgVa(msgVa);
		ptSsoMsgTVo.setRegDt("sysdate");
		commonDao.insert(ptSsoMsgTVo);
		return msgId;
	}
	/** sso 메세지 조회 */
	public String getSsoMsg(String msgId) throws SQLException{
		PtSsoMsgTVo ptSsoMsgTVo = new PtSsoMsgTVo();
		ptSsoMsgTVo.setMsgId(msgId);
		ptSsoMsgTVo = (PtSsoMsgTVo)commonDao.queryVo(ptSsoMsgTVo);
		return ptSsoMsgTVo==null ? null : ptSsoMsgTVo.getMsgVa();
	}
	/** sso 메세지 조회 및 삭제 */
	public String removeSsoMsg(String msgId) throws SQLException{
		PtSsoMsgTVo ptSsoMsgTVo = new PtSsoMsgTVo();
		ptSsoMsgTVo.setMsgId(msgId);
		ptSsoMsgTVo = (PtSsoMsgTVo)commonDao.queryVo(ptSsoMsgTVo);
		if(ptSsoMsgTVo == null) return null;
		
		String msgVa = ptSsoMsgTVo.getMsgVa();
		commonDao.delete(ptSsoMsgTVo);
		return msgVa;
	}
	
	/** [파라미터]srcText에 [파라미터]searchWord가 있는지 검사함 */
	private boolean isInString(String srcText, String searchWord){
		if(srcText == null || srcText.isEmpty()) return true;
		for(String srcWord : srcText.split(",")){
			if(searchWord.equals(srcWord.trim())) return true;
		}
		return false;
	}
	
	/** ID 통합 생성 */
	public String createId(String tableName) throws SQLException{
		if("PT_RESC_B".equals(tableName)){
			return commonSvc.createId(tableName, 'R', 8);
		} else if("PT_RESC_D".equals(tableName)){
			return commonSvc.createId(tableName, 'R', 8);
		} else if("PT_MNU_GRP_B".equals(tableName)){
			return commonSvc.createId(tableName, 'G', 8);
		} else if("PT_MNU_D".equals(tableName)){
			return commonSvc.createId(tableName, 'M', 8);
		} else if("PT_MNU_LOUT_D".equals(tableName)){
			return commonSvc.createId(tableName, 'L', 8);
		} else if("PT_MNU_LOUT_COMB_D".equals(tableName)){
			return commonSvc.createId(tableName, 'C', 8);
		} else if("PT_AUTH_GRP_B".equals(tableName)){
			return commonSvc.createId(tableName, 'A', 8);
		} else if("PT_PLT_D".equals(tableName)){
			return commonSvc.createId(tableName, 'P', 8);
		} else if("PT_LGIN_IMG_D".equals(tableName)){
			return commonSvc.createId(tableName, 'I', 8);
		} else if("PT_MY_MNU_D".equals(tableName)){
			return commonSvc.createId(tableName, 'Y', 8);
		}
		return null;
	}
}
