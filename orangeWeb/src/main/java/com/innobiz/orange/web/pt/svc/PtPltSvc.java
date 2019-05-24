package com.innobiz.orange.web.pt.svc;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.innobiz.orange.web.cm.config.CacheConfig;
import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.utils.Hash;
import com.innobiz.orange.web.pt.secu.AuthCdDecider;
import com.innobiz.orange.web.pt.vo.PtMyPltRVo;
import com.innobiz.orange.web.pt.vo.PtPltDVo;
import com.innobiz.orange.web.pt.vo.PtPltSetupRVo;

/** 포틀릿 관련 서비스 */
@Service
public class PtPltSvc {
	
	/** 캐쉬 서비스 */
	@Autowired
	private PtCacheSvc ptCacheSvc;
	
	/** 사용자 개인 설정 서비스 */
	@Autowired
	private PtMyMnuSvc ptMyMnuSvc;
	
	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;
	
	/** 메뉴그룹ID 별 설정된 포틀릿 목록 조회(캐쉬) */
	public List<PtPltSetupRVo> getPltSetupByMnuGrpIdList(String mnuGrpId, String pltLoutCd, String langTypCd) throws SQLException{
		
		@SuppressWarnings("unchecked")
		List<PtPltSetupRVo> cachedList = (List<PtPltSetupRVo>)ptCacheSvc.getCache(CacheConfig.PLT_LAYOUT, langTypCd, mnuGrpId, 50);
		if(cachedList!=null) return cachedList;
		
		// 포틀릿설정관계(PT_PLT_SETUP_R) 테이블
		PtPltSetupRVo ptPltSetupRVo = new PtPltSetupRVo();
		ptPltSetupRVo.setMnuGrpId(mnuGrpId);
		ptPltSetupRVo.setQueryLang(langTypCd);
		if("FREE".equals(pltLoutCd)){
			ptPltSetupRVo.setWhereSqllet("AND ZIDX IS NOT NULL AND PLT_ID IN (SELECT PLT_ID FROM PT_PLT_D WHERE USE_YN='Y')");
			ptPltSetupRVo.setOrderBy("ZIDX");
		} else {
			ptPltSetupRVo.setWhereSqllet("AND ZIDX IS NULL AND PLT_ID IN (SELECT PLT_ID FROM PT_PLT_D WHERE USE_YN='Y')");
			ptPltSetupRVo.setOrderBy("PLT_ZONE_CD, SORT_ORDR");
		}
		
		// 포틀릿설정관계(PT_PLT_SETUP_R) 테이블 - 조회
		@SuppressWarnings("unchecked")
		List<PtPltSetupRVo> ptPltSetupRVoList = (List<PtPltSetupRVo>)commonDao.queryList(ptPltSetupRVo);
		
		ptCacheSvc.setCache(CacheConfig.PLT_LAYOUT, langTypCd, mnuGrpId, ptPltSetupRVoList);
		return ptPltSetupRVoList;
	}
	
	/** 설정된 포틀릿 목록을 포틀릿지역코드 별로 ModelMap 에 저장 */
	public void setPltZoneList(String mnuGrpId, String pltLoutCd, String langTypCd, ModelMap model,
			AuthCdDecider authCdDecider, String[] userAuthGrpIds, String userUid) throws SQLException{
		
		// userUid - 나의메뉴의 경우만 넘겨짐
		
		PtPltDVo ptPltDVo;
		PtPltSetupRVo ptPltSetupRVo;
		
		// 메뉴그룹ID 별 설정된 포틀릿 목록
		List<PtPltSetupRVo> ptPltSetupRVoList = null;
		
		// 포틀릿 맵 조회(캐쉬)
		Map<Integer, PtPltDVo> pltMap = getPltByPltIdMap(langTypCd);
		
		// 나의 메뉴의 경우
		if(userUid!=null){
			List<PtMyPltRVo> ptMyPltRVoList = ptMyMnuSvc.getMyPltList(userUid, pltLoutCd, langTypCd);
			if(ptMyPltRVoList != null){
				ptPltSetupRVoList = new ArrayList<PtPltSetupRVo>();
				for(PtMyPltRVo ptMyPltRVo : ptMyPltRVoList){
					ptPltSetupRVo = new PtPltSetupRVo();
					ptPltSetupRVo.fromMap(ptMyPltRVo.toMap());
					
					ptPltDVo = pltMap.get(Hash.hashId(ptPltSetupRVo.getPltId()));
					if(ptPltDVo != null){
						ptPltSetupRVo.setPtPltDVo(ptPltDVo);
						ptPltSetupRVoList.add(ptPltSetupRVo);
					}
				}
			}
		} else {
			// 메뉴그룹ID 별 설정된 포틀릿 목록 조회(캐쉬)
			ptPltSetupRVoList = getPltSetupByMnuGrpIdList(mnuGrpId, pltLoutCd, langTypCd);
		}
		
		// 화면구성을 위해 포틀릿지역코드:PLT_ZONE_CD 별로 목록을 나누어 모델에 세팅함
		List<PtPltSetupRVo> ptPltSetupRVoSplitedList = null;

		// 자유구성 인지 여부
		boolean isFreeLout = "FREE".equals(pltLoutCd);
		if(isFreeLout){
			ptPltSetupRVoSplitedList = new ArrayList<PtPltSetupRVo>();
			model.put("ptPltSetupRVoList", ptPltSetupRVoSplitedList);
		}
		
		String pltZoneCd, oldPltZoneCd=null;
		int i, size = ptPltSetupRVoList==null ? 0 : ptPltSetupRVoList.size();
		for(i=0;i<size;i++){
			ptPltSetupRVo = ptPltSetupRVoList.get(i);
			ptPltDVo = pltMap.get(Hash.hashId(ptPltSetupRVo.getPltId()));
			
			if(ptPltDVo!=null
					&& (authCdDecider==null || authCdDecider.hasUsrAuth(ptPltDVo.getPltId(), userAuthGrpIds))){
				
				ptPltSetupRVo.setPtPltDVo(ptPltDVo);
				if(!isFreeLout){
					pltZoneCd = ptPltSetupRVo.getPltZoneCd();
					if(oldPltZoneCd==null || !oldPltZoneCd.equals(pltZoneCd)){
						ptPltSetupRVoSplitedList = new ArrayList<PtPltSetupRVo>();
						model.put("ptPltSetupRVoList"+pltZoneCd, ptPltSetupRVoSplitedList);
						oldPltZoneCd = pltZoneCd;
					}
				}
				ptPltSetupRVoSplitedList.add(ptPltSetupRVo);
			}
		}
	}
	
	/** 개인화 설정된 포틀릿 목록을 포틀릿지역코드 별로 ModelMap 에 저장 */
	public void setMyPltZoneList(List<PtMyPltRVo> ptMyPltRVoList, String pltLoutCd, String langTypCd, ModelMap model,
			AuthCdDecider authCdDecider, String[] userAuthGrpIds) throws SQLException{
		
		if(ptMyPltRVoList==null || ptMyPltRVoList.isEmpty()) return;
		
		// 포틀릿 맵 조회(캐쉬)
		Map<Integer, PtPltDVo> pltMap = getPltByPltIdMap(langTypCd);
		
		PtPltDVo ptPltDVo;
		PtMyPltRVo ptMyPltRVo;
		PtPltSetupRVo ptPltSetupRVo;
		// 화면구성을 위해 포틀릿지역코드:PLT_ZONE_CD 별로 목록을 나누어 모델에 세팅함
		List<PtPltSetupRVo> ptPltSetupRVoSplitedList = null;

		// 자유구성 인지 여부
		boolean isFreeLout = "FREE".equals(pltLoutCd);
		if(isFreeLout){
			ptPltSetupRVoSplitedList = new ArrayList<PtPltSetupRVo>();
			model.put("ptPltSetupRVoList", ptPltSetupRVoSplitedList);
		}
		
		String pltZoneCd, oldPltZoneCd=null;
		int i, size = ptMyPltRVoList.size();
		for(i=0;i<size;i++){
			ptMyPltRVo = ptMyPltRVoList.get(i);
			ptPltDVo = pltMap.get(Hash.hashId(ptMyPltRVo.getPltId()));
			
			if(ptPltDVo!=null
					&& (authCdDecider==null || authCdDecider.hasUsrAuth(ptPltDVo.getPltId(), userAuthGrpIds))){
				
				if(!isFreeLout){
					pltZoneCd = ptMyPltRVo.getPltZoneCd();
					if(oldPltZoneCd==null || !oldPltZoneCd.equals(pltZoneCd)){
						ptPltSetupRVoSplitedList = new ArrayList<PtPltSetupRVo>();
						model.put("ptPltSetupRVoList"+pltZoneCd, ptPltSetupRVoSplitedList);
						oldPltZoneCd = pltZoneCd;
					}
				}
				
				ptPltSetupRVo = new PtPltSetupRVo();
				ptPltSetupRVo.fromMap(ptMyPltRVo.toMap());
				ptPltSetupRVo.setPtPltDVo(ptPltDVo);
				
				ptPltSetupRVoSplitedList.add(ptPltSetupRVo);
			}
		}
	}
	
	/** 포틀릿 Zone 설정 */
	public void setPltZone(String pltLoutCd, ModelMap model){

		if(pltLoutCd.startsWith("D2")){
			if(pltLoutCd.equals("D2R37")){
				model.put("pltZone", new String[]{"30%", "69%"});
			} else if(pltLoutCd.equals("D2R46")){
				model.put("pltZone", new String[]{"40%", "59%"});
			} else if(pltLoutCd.equals("D2R55")){
				model.put("pltZone", new String[]{"49.5%", "49.5%"});
			} else if(pltLoutCd.equals("D2R64")){
				model.put("pltZone", new String[]{"59%", "40%"});
			} else if(pltLoutCd.equals("D2R73")){
				model.put("pltZone", new String[]{"69%", "30%"});
			}
		} else if(pltLoutCd.startsWith("D3")){
			if(pltLoutCd.equals("D3R111")){
				model.put("pltZone", new String[]{"32.5%", "33%", "32.5%"});
			} else if(pltLoutCd.equals("D3R112")){
				model.put("pltZone", new String[]{"25%", "25%", "48%"});
			} else if(pltLoutCd.equals("D3R121")){
				model.put("pltZone", new String[]{"25%", "48%", "25%"});
			} else if(pltLoutCd.equals("D3R211")){
				model.put("pltZone", new String[]{"48%", "25%", "25%"});
			} else if(pltLoutCd.equals("D3R221")){
				model.put("pltZone", new String[]{"39%", "39%", "20%"});
			} else if(pltLoutCd.equals("D3R212")){
				model.put("pltZone", new String[]{"39%", "20%", "39%"});
			} else if(pltLoutCd.equals("D3R122")){
				model.put("pltZone", new String[]{"20%", "39%", "39%"});
			}
		}
	}
	
	/** 포틀릿 맵 조회(캐쉬) */
	public Map<Integer, PtPltDVo> getPltByPltIdMap(String langTypCd) throws SQLException{
		@SuppressWarnings("unchecked")
		Map<Integer, PtPltDVo> pltMap = (Map<Integer, PtPltDVo>)ptCacheSvc.getCache(CacheConfig.PLT, langTypCd, "MAP", 50);
		if(pltMap!=null) return pltMap;
		
		Map<Integer, PtPltDVo> ptPltDVoMap = new HashMap<Integer, PtPltDVo>();
		List<PtPltDVo> ptPltDVoList = new ArrayList<PtPltDVo>();
		loadPltFromDB(langTypCd, ptPltDVoList, ptPltDVoMap);
		
		return ptPltDVoMap;
	}
	
	/** 포틀릿 목록 조회(캐쉬) */
	public List<PtPltDVo> getPltList(String langTypCd) throws SQLException{
		@SuppressWarnings("unchecked")
		List<PtPltDVo> pltList = (List<PtPltDVo>)ptCacheSvc.getCache(CacheConfig.PLT, langTypCd, "LIST", 50);
		if(pltList!=null) return pltList;
		
		Map<Integer, PtPltDVo> ptPltDVoMap = new HashMap<Integer, PtPltDVo>();
		List<PtPltDVo> ptPltDVoList = new ArrayList<PtPltDVo>();
		loadPltFromDB(langTypCd, ptPltDVoList, ptPltDVoMap);
		
		return ptPltDVoList;
	}
	
	/** 포틀릿 정보 로드 */
	private void loadPltFromDB(String langTypCd, List<PtPltDVo> ptPltDVoList, Map<Integer, PtPltDVo> ptPltDVoMap) throws SQLException{
		
		PtPltDVo ptPltDVo = new PtPltDVo();
		ptPltDVo.setQueryLang(langTypCd);
		
		@SuppressWarnings("unchecked")
		List<PtPltDVo> pltList = (List<PtPltDVo>)commonDao.queryList(ptPltDVo);
		if(pltList!=null){
			for(PtPltDVo storedPtPltDVo : pltList){
				// 메모리 사이즈 점검
				storedPtPltDVo.setRegrUid(null);
				storedPtPltDVo.setRegDt(null);
				storedPtPltDVo.setModrUid(null);
				storedPtPltDVo.setModDt(null);
				storedPtPltDVo.setOpenCompNm(null);
				storedPtPltDVo.setPltCatNm(null);
				storedPtPltDVo.setRegrNm(null);
				storedPtPltDVo.setModrNm(null);
				
				ptPltDVoList.add(storedPtPltDVo);
				ptPltDVoMap.put(Hash.hashId(storedPtPltDVo.getPltId()), storedPtPltDVo);
			}
		}
		
		ptCacheSvc.setCache(CacheConfig.PLT, langTypCd, "MAP", ptPltDVoMap);
		ptCacheSvc.setCache(CacheConfig.PLT, langTypCd, "LIST", ptPltDVoList);
	}
}
