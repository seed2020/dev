package com.innobiz.orange.web.pt.svc;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.innobiz.orange.web.cm.config.CacheConfig;
import com.innobiz.orange.web.cm.config.ServerConfig;
import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.utils.Hash;
import com.innobiz.orange.web.cm.utils.IdUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.pt.secu.AuthUrlMapper;
import com.innobiz.orange.web.pt.utils.LoutUtil;
import com.innobiz.orange.web.pt.utils.PtConstant;
import com.innobiz.orange.web.pt.vo.PtMnuDVo;
import com.innobiz.orange.web.pt.vo.PtMnuGrpBVo;
import com.innobiz.orange.web.pt.vo.PtMnuLoutCombDVo;
import com.innobiz.orange.web.pt.vo.PtMnuLoutDVo;
import com.innobiz.orange.web.pt.vo.PtPltDVo;

/** 메뉴 레이아웃 서비스 - 메뉴 및 레이아웃을 로드함 */
@Service
public class PtLoutSvc {
	
	/** contextProperties, 배포 서버에 관한 정보를 가지고 있음 */
	@Resource(name="contextProperties")
	private Properties contextProperties;
	
	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;
	
	/** 캐쉬 서비스 */
	@Autowired
	private PtCacheSvc ptCacheSvc;
	
	/** 포틀릿 관련 서비스 */
	@Autowired
	private PtPltSvc ptPltSvc;

	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
//	/** 나의메뉴 레이아웃 목록 */
//	private List<PtMnuLoutDVo> myPtMnuLoutDVoList = null;
	
	/** 메세지 */
	@Autowired
    private MessageProperties messageProperties;
	
	/** 레이아웃위치코드 별 레이아웃트리 맵 조회(캐쉬) - 레이아웃위치코드 - 아이콘레이아웃(icon:아이콘,left:왼쪽,right:오른쪽), 기본레이아웃(top:상단,main:메인,sub:서브), 관리자레이아웃(adm:관리자) */
	public Map<String, List<PtMnuLoutDVo>> getLoutTreeByLoutLocCdMap(String compId, String langTypCd) throws SQLException{
		@SuppressWarnings("unchecked")
		Map<String, List<PtMnuLoutDVo>> loutTreeMap = (Map<String, List<PtMnuLoutDVo>>)
				ptCacheSvc.getCache(CacheConfig.LAYOUT_GRP, langTypCd, compId+"_TREE", 100);
		if(loutTreeMap!=null) return loutTreeMap;
		
		Map<String, List<PtMnuLoutDVo>> loutTreeByLoutLocCdMap = new HashMap<String, List<PtMnuLoutDVo>>();
		Map<Integer, PtMnuLoutDVo> loutByLoutIdMap = new HashMap<Integer, PtMnuLoutDVo>();
		loadLoutTreeByLoutLocCdMap(compId, langTypCd, loutTreeByLoutLocCdMap, loutByLoutIdMap);
		ptCacheSvc.setCache(CacheConfig.LAYOUT_GRP, langTypCd, compId+"_TREE",  loutTreeByLoutLocCdMap);
		ptCacheSvc.setCache(CacheConfig.LAYOUT_GRP, langTypCd, compId+"_MAP",  loutByLoutIdMap);
		return loutTreeByLoutLocCdMap;
	}
	
	/** 레이아웃ID 별 레이아웃조회(캐쉬) */
	public Map<Integer, PtMnuLoutDVo> getLoutByLoutIdMap(String compId, String langTypCd) throws SQLException{
		@SuppressWarnings("unchecked")
		Map<Integer, PtMnuLoutDVo> loutMap = (Map<Integer, PtMnuLoutDVo>)
				ptCacheSvc.getCache(CacheConfig.LAYOUT_GRP, langTypCd, compId+"_MAP", 100);
		if(loutMap!=null) return loutMap;

		Map<String, List<PtMnuLoutDVo>> loutTreeByLoutLocCdMap = new HashMap<String, List<PtMnuLoutDVo>>();
		Map<Integer, PtMnuLoutDVo> loutByLoutIdMap = new HashMap<Integer, PtMnuLoutDVo>();
		loadLoutTreeByLoutLocCdMap(compId, langTypCd, loutTreeByLoutLocCdMap, loutByLoutIdMap);
		ptCacheSvc.setCache(CacheConfig.LAYOUT_GRP, langTypCd, compId+"_TREE",  loutTreeByLoutLocCdMap);
		ptCacheSvc.setCache(CacheConfig.LAYOUT_GRP, langTypCd, compId+"_MAP",  loutByLoutIdMap);
		return loutByLoutIdMap;
	}
	
	/** 레이아웃ID 에 의한 메뉴레이아웃상세 조회 */
	public PtMnuLoutDVo getLoutByLoutId(String compId, String mnuLoutId, String langTypCd) throws SQLException {
		Map<Integer, PtMnuLoutDVo> map = getLoutByLoutIdMap(compId, langTypCd);
		if(map!=null) return map.get(Hash.hashId(mnuLoutId));
		return null;
	}
	
	/** 레이아웃목록 과 레이아웃 트리맵 조회 */
	private void loadLoutTreeByLoutLocCdMap(String compId, String langTypCd,
			Map<String, List<PtMnuLoutDVo>> loutTreeByLoutLocCdMap,
			Map<Integer, PtMnuLoutDVo> loutByLoutIdMap) throws SQLException {
		
		PtMnuLoutDVo ptMnuLoutDVo;
		// 기본레이아웃 - 상단메뉴 목록 : top
		List<PtMnuLoutDVo> topList = new ArrayList<PtMnuLoutDVo>();
		// 기본레이아웃 - 메인메뉴 목록 : main
		List<PtMnuLoutDVo> mainList = new ArrayList<PtMnuLoutDVo>();
		// 기본레이아웃 - 하단메뉴 목록 : sub
		List<PtMnuLoutDVo> subList = new ArrayList<PtMnuLoutDVo>();
		// 아이콘레이아웃 - 아이콘(중앙) 목록 : icon
		List<PtMnuLoutDVo> iconList = new ArrayList<PtMnuLoutDVo>();
		// 아이콘레이아웃 - 왼쪽 목록 : icon
		List<PtMnuLoutDVo> leftList = new ArrayList<PtMnuLoutDVo>();
		// 아이콘레이아웃 - 오른쪽 목록 : icon
		List<PtMnuLoutDVo> rightList = new ArrayList<PtMnuLoutDVo>();
		// 관리자레이아웃 목록 : admin
		List<PtMnuLoutDVo> admList = new ArrayList<PtMnuLoutDVo>();
		// 모바일레이아웃 목록 : mobile
		List<PtMnuLoutDVo> mobileList = new ArrayList<PtMnuLoutDVo>();
		// 하단(모바일)레이아웃 목록 : footer
		List<PtMnuLoutDVo> footerList = new ArrayList<PtMnuLoutDVo>();
		
		// 메뉴그룹기본(PT_MNU_GRP_B) 테이블
		PtMnuGrpBVo ptMnuGrpBVo;
		
		// 아이콘, 또는 메인의 서브 메뉴 목록
		List<PtMnuLoutDVo> childList = null;
		
		// 메뉴레이아웃ID, 메뉴레이아웃부모ID, 레이아웃유형ID, 레이아웃위치코드, 레이아웃종류코드, 메뉴그룹구분코드
		String mnuLoutId, mnuLoutPid, loutCatId, loutLocCd, mnuLoutKndCd, mnuGrpTypCd;
		String topMnuLoutId=null, url;
		
		// 메뉴그룹ID 별 메뉴그룹 맵 조회(캐쉬)
		Map<Integer, PtMnuGrpBVo> mnuGrpByGrpIdMap = getMnuGrpByGrpIdMap(langTypCd);
		
		// 메뉴레이아웃ID 별 서브레이아웃트리 맵 조회(캐쉬) - 왼쪽메뉴의 전체 레이아웃 
		Map<Integer, List<PtMnuLoutCombDVo>> loutCombTreeByLoutIdMap = getLoutCombTreeByLoutIdMap(compId, langTypCd);
		List<PtMnuLoutCombDVo> ptMnuLoutCombDVoList;
		
		// 레이아웃 목록 조회(캐쉬)
		List<PtMnuLoutDVo> ptMnuLoutDVoList = getLoutList(compId, langTypCd);
		int i, size = ptMnuLoutDVoList==null ? 0 : ptMnuLoutDVoList.size();
		
		for(i=0;i<size;i++){
			
			ptMnuLoutDVo = ptMnuLoutDVoList.get(i);
			mnuLoutId = ptMnuLoutDVo.getMnuLoutId();//메뉴레이아웃ID - KEY
			loutCatId = ptMnuLoutDVo.getLoutCatId();//레이아웃유형ID - B:기본레이아웃, I:아이콘레이아웃, A:관리자레이아웃, M:모바일레이아웃
			loutLocCd = ptMnuLoutDVo.getLoutLocCd();//레이아웃위치코드 - 아이콘레이아웃(icon:아이콘,left:왼쪽,right:오른쪽), 기본레이아웃(top:상단,main:메인,sub:서브), 관리자레이아웃(adm:관리자)
			mnuLoutKndCd = ptMnuLoutDVo.getMnuLoutKndCd();//레이아웃종류코드 - F:폴더, G:메뉴그룹, C:메뉴조합
			
			// 메뉴그룹구분코드 설정 - 01:포털구성(포틀릿,메뉴), 02:포털구성(포틀릿), 03:포털구성(메뉴), 04:포털구성(URL), 11:외부팝업, 12:외부프레임(URL)
			if("G".equals(mnuLoutKndCd)){//레이아웃종류코드 - F:폴더, G:메뉴그룹, C:메뉴조합
				ptMnuGrpBVo = mnuGrpByGrpIdMap.get(Hash.hashId(ptMnuLoutDVo.getMnuGrpId()));
				if(ptMnuGrpBVo==null) continue;
				mnuGrpTypCd = ptMnuGrpBVo.getMnuGrpTypCd();//메뉴그룹구분코드
				
				// 나의메뉴 - 에 해당하는 레이아웃 세팅
				if(PtConstant.MNU_GRP_REF_MY.equals(ptMnuGrpBVo.getMdRid())){
					ptMnuLoutDVo.setMyMnu(true);
				}
				
				if("M".equals(loutCatId) && ptMnuGrpBVo.getMdRid()!=null){
					ptMnuLoutDVo.setImgKndVa("ico"+ptMnuGrpBVo.getMdRid().toLowerCase());
				}
			} else {
				mnuGrpTypCd = null;
			}
			
			// C:메뉴조합 의 경우 권한 체크를 위해 "메뉴레이아웃ID" 를 "메뉴그룹ID"에 넣어 줌
			// PtSecuSvc.setAuthedLoutList 에서 아래와 같이 사용
			// - authCdDecider.hasAdmAuth(ptMnuLoutDVo.getMnuGrpId(), admAuthGrpIds)
			if("C".equals(mnuLoutKndCd)){
				ptMnuLoutDVo.setMnuGrpId(mnuLoutId);
			}
			
			// 포틀릿이 없는 메뉴만의 구성인(레이아웃종류코드 - C:메뉴조합 or 메뉴그룹구분코드 - 03:포털구성(메뉴))일 경우
			// 메뉴가 없으면 제거
			if("C".equals(mnuLoutKndCd) || "03".equals(mnuGrpTypCd)){
				if((ptMnuLoutCombDVoList = loutCombTreeByLoutIdMap.get(Hash.hashId(mnuLoutId))) == null){
					continue;
				} else {
					// 첫번째 유효 URL 리턴 - 팝업제외
					url = getFirstValidUrl(ptMnuLoutCombDVoList);
					// url이 없으면 - 포틀릿 호출하는 화면으로 전환함
					if(url==null){
						url = LoutUtil.getMnuGrpUrl(
								ptMnuLoutDVo.getMnuGrpId()==null ? mnuLoutId : ptMnuLoutDVo.getMnuGrpId(), mnuLoutId);
					}
					ptMnuLoutDVo.setMnuUrl(url);
				}
			}
			
			// 아이콘 레이아웃의 경우
			if("I".equals(loutCatId)){
				
				//레이아웃위치코드 - 아이콘레이아웃(icon:아이콘,left:왼쪽,right:오른쪽), 기본레이아웃(top:상단,main:메인,sub:서브), 관리자레이아웃(adm:관리자)
				if("icon".equals(loutLocCd)){
					// 레이아웃종류코드 - F:폴더, G:메뉴그룹, C:메뉴조합
					if("F".equals(mnuLoutKndCd)){
						topMnuLoutId = mnuLoutId;
						childList = new ArrayList<PtMnuLoutDVo>();
						ptMnuLoutDVo.setChildList(childList);
						iconList.add(ptMnuLoutDVo);
						loutByLoutIdMap.put(Hash.hashId(mnuLoutId), ptMnuLoutDVo);
					} else {
						ptMnuLoutDVo.setTopMnuLoutId(topMnuLoutId);
						childList.add(ptMnuLoutDVo);
						loutByLoutIdMap.put(Hash.hashId(mnuLoutId), ptMnuLoutDVo);
					}
				} else if("left".equals(loutLocCd)){
					// 레이아웃종류코드 - F:폴더, G:메뉴그룹, C:메뉴조합
					if("G".equals(mnuLoutKndCd) || "C".equals(mnuLoutKndCd)){
						ptMnuLoutDVo.setTopMnuLoutId(mnuLoutId);
						leftList.add(ptMnuLoutDVo);
						loutByLoutIdMap.put(Hash.hashId(mnuLoutId), ptMnuLoutDVo);
					}
				} else if("right".equals(loutLocCd)){
					// 레이아웃종류코드 - F:폴더, G:메뉴그룹, C:메뉴조합
					if("G".equals(mnuLoutKndCd) || "C".equals(mnuLoutKndCd)){
						ptMnuLoutDVo.setTopMnuLoutId(mnuLoutId);
						rightList.add(ptMnuLoutDVo);
						loutByLoutIdMap.put(Hash.hashId(mnuLoutId), ptMnuLoutDVo);
					}
				} else if("home".equals(loutLocCd)){
					// 레이아웃종류코드 - F:폴더, G:메뉴그룹, C:메뉴조합
					if("G".equals(mnuLoutKndCd) || "C".equals(mnuLoutKndCd)){
						ptMnuLoutDVo.setTopMnuLoutId(mnuLoutId);
						loutByLoutIdMap.put(Hash.hashId(mnuLoutId), ptMnuLoutDVo);
					}
				}
				
			// 기본 레이아웃의 경우 (top,main,sub)
			} else if("B".equals(loutCatId)){
				
				//레이아웃위치코드 - 아이콘레이아웃(icon:아이콘,left:왼쪽,right:오른쪽), 기본레이아웃(top:상단,main:메인,sub:서브), 관리자레이아웃(adm:관리자)
				if("top".equals(loutLocCd)){
					// 레이아웃종류코드 - F:폴더, G:메뉴그룹, C:메뉴조합
					if("G".equals(mnuLoutKndCd) || "C".equals(mnuLoutKndCd)){
						ptMnuLoutDVo.setTopMnuLoutId(mnuLoutId);
						topList.add(ptMnuLoutDVo);
						loutByLoutIdMap.put(Hash.hashId(mnuLoutId), ptMnuLoutDVo);
					}
				//레이아웃위치코드 - 아이콘레이아웃(icon:아이콘,left:왼쪽,right:오른쪽), 기본레이아웃(top:상단,main:메인,sub:서브), 관리자레이아웃(adm:관리자)
				} else if("main".equals(loutLocCd)){
					
					mnuLoutPid = ptMnuLoutDVo.getMnuLoutPid();//메뉴레이아웃부모ID
					
					// 레이아웃종류코드 - F:폴더, G:메뉴그룹, C:메뉴조합
					if("F".equals(mnuLoutKndCd)){// main 에 폴더를 만든 경우 - 폴더 하위에 메뉴 그룹을 등록하기 위한 것
						topMnuLoutId = mnuLoutId;
						childList = new ArrayList<PtMnuLoutDVo>();
						ptMnuLoutDVo.setChildList(childList);
						mainList.add(ptMnuLoutDVo);
						loutByLoutIdMap.put(Hash.hashId(mnuLoutId), ptMnuLoutDVo);
					} else if("main".equals(mnuLoutPid)){// main 에 메뉴그룹을 직접 붙여 놓은 경우
						ptMnuLoutDVo.setTopMnuLoutId(mnuLoutId);
						mainList.add(ptMnuLoutDVo);
						loutByLoutIdMap.put(Hash.hashId(mnuLoutId), ptMnuLoutDVo);
						topMnuLoutId = null;
						childList = null;
					} else {// main에 폴더를 만들고 그 하위 붙인 메뉴그룹의 경우
						if(childList!=null){
							ptMnuLoutDVo.setTopMnuLoutId(topMnuLoutId);
							childList.add(ptMnuLoutDVo);
							loutByLoutIdMap.put(Hash.hashId(mnuLoutId), ptMnuLoutDVo);
						}
					}
				//레이아웃위치코드 - 아이콘레이아웃(icon:아이콘,left:왼쪽,right:오른쪽), 기본레이아웃(top:상단,main:메인,sub:서브), 관리자레이아웃(adm:관리자)
				} else if("sub".equals(loutLocCd)){
					if("G".equals(mnuLoutKndCd) || "C".equals(mnuLoutKndCd)){
						ptMnuLoutDVo.setTopMnuLoutId(mnuLoutId);
						subList.add(ptMnuLoutDVo);
						loutByLoutIdMap.put(Hash.hashId(mnuLoutId), ptMnuLoutDVo);
					}
				} else if("home".equals(loutLocCd)){
					// 레이아웃종류코드 - F:폴더, G:메뉴그룹, C:메뉴조합
					if("G".equals(mnuLoutKndCd) || "C".equals(mnuLoutKndCd)){
						ptMnuLoutDVo.setTopMnuLoutId(mnuLoutId);
						loutByLoutIdMap.put(Hash.hashId(mnuLoutId), ptMnuLoutDVo);
					}
				}
			// 관리자 레이아웃의 경우
			} else if("A".equals(loutCatId)){
				if("G".equals(mnuLoutKndCd) || "C".equals(mnuLoutKndCd)){
					ptMnuLoutDVo.setTopMnuLoutId(mnuLoutId);
					admList.add(ptMnuLoutDVo);
					loutByLoutIdMap.put(Hash.hashId(mnuLoutId), ptMnuLoutDVo);
				}
			// 모바일 레이아웃의 경우
			} else if("M".equals(loutCatId)){
				if("mobile".equals(loutLocCd)){
					if("G".equals(mnuLoutKndCd) || "C".equals(mnuLoutKndCd)){
						ptMnuLoutDVo.setTopMnuLoutId(mnuLoutId);
						
						mobileList.add(ptMnuLoutDVo);
						loutByLoutIdMap.put(Hash.hashId(mnuLoutId), ptMnuLoutDVo);
					}
				} else if("bottom".equals(loutLocCd)){
					if("G".equals(mnuLoutKndCd) || "C".equals(mnuLoutKndCd)){
						ptMnuLoutDVo.setTopMnuLoutId(mnuLoutId);
						footerList.add(ptMnuLoutDVo);
						loutByLoutIdMap.put(Hash.hashId(mnuLoutId), ptMnuLoutDVo);
					}
				}
			}
		}
		
		if(!ServerConfig.IS_MOBILE){
			// 빈 트리 제거
			removeEmptyChildList(iconList);
			removeEmptyChildList(mainList);
			
			// location map - setting
			loutTreeByLoutLocCdMap.put("top", topList);
			loutTreeByLoutLocCdMap.put("main", mainList);
			loutTreeByLoutLocCdMap.put("sub", subList);
			
			loutTreeByLoutLocCdMap.put("icon", iconList);
			loutTreeByLoutLocCdMap.put("left", leftList);
			loutTreeByLoutLocCdMap.put("right", rightList);
			
			loutTreeByLoutLocCdMap.put("adm", admList);
		} else {
			loutTreeByLoutLocCdMap.put("mobile", mobileList);
			loutTreeByLoutLocCdMap.put("footer", footerList);
		}
	}
	
	/** 레이아웃 목록 조회(캐쉬)  */
	private List<PtMnuLoutDVo> getLoutList(String compId, String langTypCd) throws SQLException{
		@SuppressWarnings("unchecked")
		List<PtMnuLoutDVo> loutList = (List<PtMnuLoutDVo>)ptCacheSvc.getCache(CacheConfig.LAYOUT_GRP, langTypCd, compId+"_LIST", 100);
		if(loutList!=null) return loutList;
		
		// 레이아웃 목록 캐쉬에서 조회(DB)
		loutList = getLoutListFromTable(compId, null, langTypCd);
		
		// 메뉴그룹ID 별 메뉴그룹 맵 조회(캐쉬)
		Map<Integer, PtMnuGrpBVo> mnuGrpByGrpIdMap = getMnuGrpByGrpIdMap(langTypCd);
		PtMnuLoutDVo ptMnuLoutDVo;
		PtMnuGrpBVo ptMnuGrpBVo;
		String mnuLoutKndCd, rescNm, mdRid;
		
		// 모듈참조ID별 메뉴레이아웃 맵 - 커뮤니티메뉴용
		Map<String, PtMnuLoutDVo> mnuLoutByMdRidMap = new HashMap<String, PtMnuLoutDVo>();
		
		// 서비스그룹의 URL 및 함수(팝업의 경우) 설정
		int i, size = loutList==null ? 0 : loutList.size();
		for(i=0;i<size;i++){
			ptMnuLoutDVo = loutList.get(i);
			mnuLoutKndCd = ptMnuLoutDVo.getMnuLoutKndCd();//레이아웃종류코드 - F:폴더, G:메뉴그룹, C:메뉴조합
			if("G".equals(mnuLoutKndCd)){//레이아웃종류코드 - F:폴더, G:메뉴그룹, C:메뉴조합
				
				// 메뉴그룹이 삭제되거나 사용안함 처리 된것 제거
				ptMnuGrpBVo = mnuGrpByGrpIdMap.get(Hash.hashId(ptMnuLoutDVo.getMnuGrpId()));
				if(ptMnuGrpBVo==null){
					loutList.remove(i);
					i--;
					size--;
					continue;
				}
				
				// 메모리 사이즈 점검
				ptMnuLoutDVo.setMnuLoutNm(null);
				ptMnuLoutDVo.setMnuLoutKndNm(null);
				
				// 서비스그룹을 그대로 링크한 경우 - 서비스 그룹명 세팅
				rescNm = ptMnuLoutDVo.getRescNm();
				if(rescNm==null || rescNm.isEmpty()){
					ptMnuLoutDVo.setRescNm(ptMnuGrpBVo.getRescNm());
				}
				
				//메뉴레이아웃상세VO 의 메뉴URL
				setPtMnuLoutDVoMnuUrl(ptMnuLoutDVo, ptMnuGrpBVo);
				
				// 모듈참조ID(커뮤니티메뉴 구별용) 가 있으면 모듈참조ID맵 에 세팅함 
				mdRid = ptMnuGrpBVo.getMdRid();
				if(mdRid!=null && !mdRid.isEmpty()){
					mnuLoutByMdRidMap.put(mdRid+"-"+ptMnuLoutDVo.getLoutCatId(), ptMnuLoutDVo);
				}
			}
			// home 으로 세팅된것 - 캐쉬 세팅
			if("home".equals(ptMnuLoutDVo.getLoutLocCd())){
				ptCacheSvc.setCache(CacheConfig.LAYOUT_GRP, langTypCd, compId+ptMnuLoutDVo.getLoutCatId()+"_HOME", ptMnuLoutDVo);
			}
		}
		
		ptCacheSvc.setCache(CacheConfig.LAYOUT_GRP, langTypCd, compId+"_LIST", loutList);
		ptCacheSvc.setCache(CacheConfig.LAYOUT_GRP, langTypCd, compId+"_RID_MAP", mnuLoutByMdRidMap);
		
		return loutList;
	}
	
	/** 모듈참조ID별 메뉴레이아웃 맵 리턴 */
	public PtMnuLoutDVo getMnuLoutByMdRid(String mdRid, String loutCatId, String compId, String langTypCd) throws SQLException{
		
		@SuppressWarnings("unchecked")
		Map<String, PtMnuLoutDVo> mnuLoutByMdRidMap = (Map<String, PtMnuLoutDVo>)ptCacheSvc.getCache(CacheConfig.LAYOUT_GRP, langTypCd, compId+"_RID_MAP", 50);
		if(mnuLoutByMdRidMap!=null){
			return mnuLoutByMdRidMap.get(mdRid+"-"+loutCatId);
		}
		
		// 캐쉬 만들기
		getLoutList(compId, langTypCd);
		
		@SuppressWarnings("unchecked")
		Map<String, PtMnuLoutDVo> mnuLoutByMdRidMap2 = (Map<String, PtMnuLoutDVo>)ptCacheSvc.getCache(CacheConfig.LAYOUT_GRP, langTypCd, compId+"_RID_MAP", 50);
		return mnuLoutByMdRidMap2.get(mdRid+"-"+loutCatId);
	}
	
	/** 홈으로 세팅된 레이아웃 리턴 */
	public PtMnuLoutDVo getHomePtMnuLoutDVo(String compId, String loutCatId, String langTypCd) throws SQLException{
		PtMnuLoutDVo ptMnuLoutDVo = (PtMnuLoutDVo)ptCacheSvc.getCache(CacheConfig.LAYOUT_GRP, langTypCd, compId+loutCatId+"_HOME", 100);
		if(ptMnuLoutDVo==null){
			getLoutList(compId, langTypCd);
			ptMnuLoutDVo = (PtMnuLoutDVo)ptCacheSvc.getCache(CacheConfig.LAYOUT_GRP, langTypCd, compId+loutCatId+"_HOME", 100);
		}
		return ptMnuLoutDVo;
	}
	
	/** 메뉴레이아웃상세VO 의 메뉴URL */
	private void setPtMnuLoutDVoMnuUrl(PtMnuLoutDVo ptMnuLoutDVo, PtMnuGrpBVo ptMnuGrpBVo){
		/**
		 * 03:포털구성(메뉴) 은 제외하고 세팅하며
		 *    - 이 경우 하위 메뉴트리가 구성되면 그중 첫번째 것을 세팅함 : loadLoutTreeByLoutLocCdMap
		 * */
		// 메뉴레이아웃ID
		String mnuLoutId = ptMnuLoutDVo.getMnuLoutId();
		// 메뉴그룹구분코드 - 01:포털구성(포틀릿,메뉴), 02:포털구성(포틀릿), 03:포털구성(메뉴), 04:포털구성(URL), 11:외부팝업, 12:외부프레임(URL)
		String  mnuGrpTypCd = ptMnuGrpBVo.getMnuGrpTypCd();
		ptMnuLoutDVo.setMnuGrpTypCd(mnuGrpTypCd);
		
		if(PtConstant.MNU_GRP_REF_MY.equals(ptMnuGrpBVo.getMdRid())){//나의메뉴
			ptMnuLoutDVo.setMnuFnc(LoutUtil.getMyLoutFnc(ptMnuGrpBVo.getMnuGrpId(), mnuLoutId));
			ptMnuLoutDVo.setMnuUrl(LoutUtil.getMnuGrpUrl(ptMnuGrpBVo.getMnuGrpId(), mnuLoutId));
		} else if("01".equals(mnuGrpTypCd) || "02".equals(mnuGrpTypCd)){//01:포털구성(포틀릿,메뉴), 02:포털구성(포틀릿)
			ptMnuLoutDVo.setMnuUrl(LoutUtil.getMnuGrpUrl(ptMnuGrpBVo.getMnuGrpId(), mnuLoutId));
		} else if("04".equals(mnuGrpTypCd)){//04:포털구성(URL)
			ptMnuLoutDVo.setMnuUrl(LoutUtil.addMenuId(ptMnuGrpBVo.getMnuUrl(), mnuLoutId));
		} else if("11".equals(mnuGrpTypCd)){//11:외부팝업
			ptMnuLoutDVo.setMnuUrl(ptMnuGrpBVo.getMnuUrl());
			ptMnuLoutDVo.setMnuFnc(LoutUtil.getMnuPopFnc(//팝업이면 호출 함수 설정함
					ptMnuGrpBVo.getMnuGrpId(),
					ptMnuGrpBVo.getMnuUrl(),
					ptMnuGrpBVo.getPopSetupCont(),
					null,
					false));
		} else if("12".equals(mnuGrpTypCd)){//12:외부프레임(URL)
			ptMnuLoutDVo.setMnuUrl(LoutUtil.getOuterFrameUrl(mnuLoutId, ptMnuGrpBVo.getMnuGrpId()));
		}
	}
	
	/** 자식을 안가지고 있는 폴더 제거 */
	private void removeEmptyChildList(List<PtMnuLoutDVo> loutList) {
		PtMnuLoutDVo ptMnuLoutDVo, childPtMnuLoutDVo;
		List<PtMnuLoutDVo> childList;
		int i, size = loutList==null ? 0 : loutList.size(), childSize;
		for(i=0;i<size;i++){
			ptMnuLoutDVo = loutList.get(i);
			if("F".equals(ptMnuLoutDVo.getMnuLoutKndCd())){//레이아웃종류코드 - F:폴더, G:메뉴그룹, C:메뉴조합
				childList = ptMnuLoutDVo.getChildList();
				childSize = childList==null ? 0 : childList.size();
				if(childSize==0){
					loutList.remove(i);
					i--;
					size--;
				} else if(childSize==1){
					childPtMnuLoutDVo = childList.get(0);
					ptMnuLoutDVo.setMnuUrl(childPtMnuLoutDVo.getMnuUrl());
					ptMnuLoutDVo.setMnuFnc(childPtMnuLoutDVo.getMnuFnc());
				} else {
					ptMnuLoutDVo.setMnuUrl("javascript:void(0);");
				}
			}
		}
	}
	
	/** 첫번째 유효 URL 리턴 - 팝업제외, 외부URL(http 로 시작)제외 */
	public String getFirstValidUrl(List<PtMnuLoutCombDVo> ptMnuLoutCombDVoList){
		String url = null, mnuTypCd;
		int i, size = ptMnuLoutCombDVoList==null ? 0 : ptMnuLoutCombDVoList.size();
		PtMnuLoutCombDVo ptMnuLoutCombDVo;
		PtMnuDVo ptMnuDVo;
		for(i=0;i<size;i++){
			ptMnuLoutCombDVo = ptMnuLoutCombDVoList.get(i);
			if("Y".equals(ptMnuLoutCombDVo.getFldYn())){
				if((url = getFirstValidUrl(ptMnuLoutCombDVo.getChildList()))!=null){
					return url;
				}
			} else {
				ptMnuDVo = ptMnuLoutCombDVo.getPtMnuDVo();
				if(ptMnuDVo==null) continue;
				mnuTypCd = ptMnuDVo.getMnuTypCd();
				if("IN_URL".equals(mnuTypCd) || "OUT_URL".equals(mnuTypCd)){
					url = ptMnuLoutCombDVo.getMnuUrl();
					if(url!=null && !url.isEmpty()){
						return url;
					}
				}
			}
		}
		return null;
	}
	
	/** URL과 파라미터를 기반으로 어떤 메뉴가 호출되었는지를 찾는 객체 */
	public AuthUrlMapper getAuthUrlMapper(String compId, String langTypCd) throws SQLException{
		AuthUrlMapper authUrlMapper = (AuthUrlMapper)ptCacheSvc.getCache(CacheConfig.LAYOUT, langTypCd, compId+"_MAPPER", 100);
		if(authUrlMapper!=null) return authUrlMapper;
		
		// 레이아웃그룹을 먼저 로드 해야함
		getLoutTreeByLoutLocCdMap(compId, langTypCd);
		
		authUrlMapper = new AuthUrlMapper(contextProperties.getProperty("cm.mnu.encoding", "UTF-8"));
		
		// 레이아웃콤보ID별 콤보레이아웃 맵 조회(캐쉬)
		Map<Integer, PtMnuLoutCombDVo> loutCombByCombIdMap = getLoutCombByCombIdMap(compId, langTypCd);
		Iterator<Entry<Integer, PtMnuLoutCombDVo>> iterator = loutCombByCombIdMap.entrySet().iterator();
		Entry<Integer, PtMnuLoutCombDVo> entry;
		String combId, url;
		PtMnuLoutCombDVo ptMnuLoutCombDVo;
		PtMnuDVo ptMnuDVo;
		String loutCatId;
		while(iterator.hasNext()){
			entry = iterator.next();
			ptMnuLoutCombDVo = entry.getValue();
			loutCatId = ptMnuLoutCombDVo.getLoutCatId();
			if(loutCatId==null || loutCatId.isEmpty()) continue;
			combId = ptMnuLoutCombDVo.getMnuLoutCombId();
			ptMnuDVo = ptMnuLoutCombDVo.getPtMnuDVo();
			if(ptMnuDVo!=null){
				//메뉴구분코드 - IN_URL:내부URL, IN_POP:내부팝업, OUT_URL:외부URL, OUT_POP:외부팝업
				url = "OUT_URL".equals(ptMnuDVo.getMnuTypCd()) ?
						LoutUtil.getOuterMnuUrl(combId, ptMnuDVo.getMnuId()) : ptMnuDVo.getMnuUrl();
				authUrlMapper.add(entry.getValue().getMnuLoutId(), combId, 
						ptMnuDVo.getMnuId(), loutCatId.charAt(0), url,
						"A".equals(ptMnuDVo.getMnuGrpMdCd()));//메뉴그룹모듈코드 - A:관리자, U:사용자, M:모바일
			}
		}
		
		// 레이아웃 목록 조회(캐쉬)
		List<PtMnuLoutDVo> ptMnuLoutDVoList = getLoutList(compId, langTypCd);
		if(ptMnuLoutDVoList!=null){
			for(PtMnuLoutDVo ptMnuLoutDVo : ptMnuLoutDVoList){
				loutCatId = ptMnuLoutDVo.getLoutCatId();
				if(loutCatId==null || loutCatId.isEmpty()) continue;
				if(ptMnuLoutDVo.getMnuUrl() != null && "G".equals(ptMnuLoutDVo.getMnuLoutKndCd())){//레이아웃종류코드 - F:폴더, G:메뉴그룹, C:메뉴조합
					authUrlMapper.add(ptMnuLoutDVo.getMnuLoutId(), ptMnuLoutDVo.getMnuLoutId(),
							ptMnuLoutDVo.getMnuGrpId(), loutCatId.charAt(0), ptMnuLoutDVo.getMnuUrl(),
							"A".equals(ptMnuLoutDVo.getLoutCatId()));//레이아웃유형ID - B:기본레이아웃, I:아이콘레이아웃, A:관리자레이아웃, M:모바일레이아웃
				}
			}
		}
		
		// 포틀릿 목록 조회(캐쉬)
		List<PtPltDVo> pltList = ptPltSvc.getPltList(langTypCd);
		if(pltList!=null){
			for(PtPltDVo ptPltDVo : pltList){
				if(ptPltDVo.getPltUrl() != null){
					authUrlMapper.add(null, ptPltDVo.getPltId(),
							ptPltDVo.getPltId(), 'P', ptPltDVo.getPltUrl(),
							false);
				}
			}
		}
		
		ptCacheSvc.setCache(CacheConfig.LAYOUT, langTypCd, compId+"_MAPPER", authUrlMapper);
		return authUrlMapper;
	}
	
	/** 레이아웃ID별 콤보레이아웃TREE 맵 조회(캐쉬) - 좌측 메뉴 */
	public Map<Integer, List<PtMnuLoutCombDVo>> getLoutCombTreeByLoutIdMap(String compId, String langTypCd) throws SQLException{
		
		@SuppressWarnings("unchecked")
		Map<Integer, List<PtMnuLoutCombDVo>> loutTreeMap =  (Map<Integer, List<PtMnuLoutCombDVo>>)
				ptCacheSvc.getCache(CacheConfig.LAYOUT, langTypCd, compId+"_TREE", 100);
		if(loutTreeMap!=null) return loutTreeMap;
		
		Map<Integer, List<PtMnuLoutCombDVo>> loutCombTreeByLoutIdMap = new HashMap<Integer, List<PtMnuLoutCombDVo>>();
		Map<Integer, PtMnuLoutCombDVo> loutCombByCombIdMap = new HashMap<Integer, PtMnuLoutCombDVo>();
		Map<String, List<PtMnuDVo>> ptMnuDVoListByMdRidMap = new HashMap<String, List<PtMnuDVo>>();
		
		// 회사별 메뉴그룹레이아웃
		createMnuLoutTreeAndMap(compId, langTypCd, loutCombTreeByLoutIdMap, loutCombByCombIdMap, ptMnuDVoListByMdRidMap);
		// 회사별 콤보레이아웃
		loadCombLoutTreeAndMap(compId, langTypCd, loutCombTreeByLoutIdMap, loutCombByCombIdMap);
		
		ptCacheSvc.setCache(CacheConfig.LAYOUT, langTypCd, compId+"_TREE", loutCombTreeByLoutIdMap);
		ptCacheSvc.setCache(CacheConfig.LAYOUT, langTypCd, compId+"_MAP", loutCombByCombIdMap);
		ptCacheSvc.setCache(CacheConfig.LAYOUT, langTypCd, compId+"_MD_MAP", ptMnuDVoListByMdRidMap);
		return loutCombTreeByLoutIdMap;
	}
	
	/** 레이아웃콤보ID별 콤보레이아웃 맵 조회(캐쉬) */
	public Map<Integer, PtMnuLoutCombDVo> getLoutCombByCombIdMap(String compId, String langTypCd) throws SQLException{
		
		@SuppressWarnings("unchecked")
		Map<Integer, PtMnuLoutCombDVo> loutMap =  (Map<Integer, PtMnuLoutCombDVo>)
				ptCacheSvc.getCache(CacheConfig.LAYOUT, langTypCd, compId+"_MAP", 100);
		if(loutMap!=null) return loutMap;
		
		Map<Integer, List<PtMnuLoutCombDVo>> loutCombTreeByLoutIdMap = new HashMap<Integer, List<PtMnuLoutCombDVo>>();
		Map<Integer, PtMnuLoutCombDVo> loutCombByCombIdMap = new HashMap<Integer, PtMnuLoutCombDVo>();
		Map<String, List<PtMnuDVo>> ptMnuDVoListByMdRidMap = new HashMap<String, List<PtMnuDVo>>();
		
		// 회사별 메뉴그룹레이아웃
		createMnuLoutTreeAndMap(compId, langTypCd, loutCombTreeByLoutIdMap, loutCombByCombIdMap, ptMnuDVoListByMdRidMap);
		// 회사별 콤보레이아웃
		loadCombLoutTreeAndMap(compId, langTypCd, loutCombTreeByLoutIdMap, loutCombByCombIdMap);
		
		ptCacheSvc.setCache(CacheConfig.LAYOUT, langTypCd, compId+"_TREE", loutCombTreeByLoutIdMap);
		ptCacheSvc.setCache(CacheConfig.LAYOUT, langTypCd, compId+"_MAP", loutCombByCombIdMap);
		ptCacheSvc.setCache(CacheConfig.LAYOUT, langTypCd, compId+"_MD_MAP", ptMnuDVoListByMdRidMap);
		return loutCombByCombIdMap;
	}
	
	/** 모듈ID 별 메뉴ID 목록 리턴(캐쉬)  */
	public List<PtMnuDVo> getMnuListByMdRid(String mdRid, String compId, String langTypCd) throws SQLException {
		
		@SuppressWarnings("unchecked")
		Map<String, List<PtMnuDVo>> listMap =  (Map<String, List<PtMnuDVo>>)
				ptCacheSvc.getCache(CacheConfig.LAYOUT, langTypCd, compId+"_MD_MAP", 100);
		if(listMap!=null) return listMap.get(mdRid);
		
		Map<Integer, List<PtMnuLoutCombDVo>> loutCombTreeByLoutIdMap = new HashMap<Integer, List<PtMnuLoutCombDVo>>();
		Map<Integer, PtMnuLoutCombDVo> loutCombByCombIdMap = new HashMap<Integer, PtMnuLoutCombDVo>();
		Map<String, List<PtMnuDVo>> ptMnuDVoListByMdRidMap = new HashMap<String, List<PtMnuDVo>>();
		
		// 회사별 메뉴그룹레이아웃
		createMnuLoutTreeAndMap(compId, langTypCd, loutCombTreeByLoutIdMap, loutCombByCombIdMap, ptMnuDVoListByMdRidMap);
		// 회사별 콤보레이아웃
		loadCombLoutTreeAndMap(compId, langTypCd, loutCombTreeByLoutIdMap, loutCombByCombIdMap);
		
		ptCacheSvc.setCache(CacheConfig.LAYOUT, langTypCd, compId+"_TREE", loutCombTreeByLoutIdMap);
		ptCacheSvc.setCache(CacheConfig.LAYOUT, langTypCd, compId+"_MAP", loutCombByCombIdMap);
		ptCacheSvc.setCache(CacheConfig.LAYOUT, langTypCd, compId+"_MD_MAP", ptMnuDVoListByMdRidMap);
		
		return ptMnuDVoListByMdRidMap.get(mdRid);
	}
	
	/** 메뉴그룹의 모듈 참조ID 별 메뉴 목록 */
	public List<PtMnuDVo> getMnuListByGrpMdRid(String mdRid, String compId, String langTypCd) throws SQLException {
		
		Map<String, PtMnuGrpBVo> mnuGrpByMdRidMap = getMnuGrpByMdRidMap(langTypCd);
		PtMnuGrpBVo ptMnuGrpBVo = mnuGrpByMdRidMap.get(mdRid);
		if(ptMnuGrpBVo==null) return null;
		
		Map<Integer, List<PtMnuDVo>> mnuListByGrpIdMap = getMnuListByGrpIdMap(langTypCd);
		List<PtMnuDVo> mnuList = mnuListByGrpIdMap.get(Hash.hashId(ptMnuGrpBVo.getMnuGrpId()));
		return mnuList;
	}
	
	/** 메뉴레이아웃(메뉴레이아웃조합상세) 맵 생성 - 레이아웃 관리에서 [메뉴그룹을 붙인 것]의 좌측 메뉴 */
	private void createMnuLoutTreeAndMap(String compId, String langTypCd, 
			Map<Integer, List<PtMnuLoutCombDVo>> loutCombTreeByLoutIdMap,
			Map<Integer, PtMnuLoutCombDVo> loutCombByCombIdMap,
			Map<String, List<PtMnuDVo>> ptMnuDVoListByMdRidMap) throws SQLException {
		
		PtMnuLoutDVo ptMnuLoutDVo;
		List<PtMnuDVo> mnuTree;
		List<PtMnuLoutCombDVo> mnuLoutTree;
		String mnuGrpId, mnuLoutId, loutCatId;
		
		// 메뉴그룹ID 별 메뉴트리맵 조회(캐쉬)
		Map<Integer, List<PtMnuDVo>> mnuTreeByGrpIdMap = getMnuTreeByGrpIdMap(langTypCd);
		
		// 레이아웃 목록 조회(캐쉬)
		List<PtMnuLoutDVo> loutList = getLoutList(compId, langTypCd);
		
		int i, size = loutList==null ? 0 : loutList.size();
		Integer index = 0;
		for(i=0;i<size;i++){
			ptMnuLoutDVo = loutList.get(i);
			// 레이아웃종류코드 - F:폴더, G:메뉴그룹, C:메뉴조합
			if("G".equals(ptMnuLoutDVo.getMnuLoutKndCd())){
				mnuLoutId = ptMnuLoutDVo.getMnuLoutId();
				mnuGrpId = ptMnuLoutDVo.getMnuGrpId();
				loutCatId = ptMnuLoutDVo.getLoutCatId();
				
				mnuTree = mnuTreeByGrpIdMap.get(Hash.hashId(mnuGrpId));
				mnuLoutTree = new ArrayList<PtMnuLoutCombDVo>();
				index = makeMnuLoutTree(mnuLoutId, mnuLoutId, loutCatId, index, mnuLoutTree, mnuTree, loutCombByCombIdMap, ptMnuDVoListByMdRidMap, null);
				if(!mnuLoutTree.isEmpty()) loutCombTreeByLoutIdMap.put(Hash.hashId(mnuLoutId), mnuLoutTree);
			}
		}
	}
	
	/** 메뉴을 이용한 레이아웃콤보 데이터 생성 - 레이아웃 관리에서 [메뉴그룹을 붙인 것]의 좌측 메뉴 */
	private Integer makeMnuLoutTree(String mnuLoutId, String mnuLoutCombPid, String loutCatId, Integer index,
			List<PtMnuLoutCombDVo> mnuLoutTree, List<PtMnuDVo> mnuTree, Map<Integer, PtMnuLoutCombDVo> loutCombByCombIdMap,
			Map<String, List<PtMnuDVo>> ptMnuDVoListByMdRidMap, Map<String, List<String>> mnuIdListByMdIdMap) {
		List<PtMnuLoutCombDVo> childTree;
		PtMnuLoutCombDVo ptMnuLoutCombDVo;
		
		// 모듈ID
		String mdId, mdRid;
		List<String> mnuIdList;
		List<PtMnuDVo> ptMnuDVoList;
		
		if(mnuIdListByMdIdMap==null){
			mnuIdListByMdIdMap = new HashMap<String, List<String>>();
		}
//		Map<String, List<String>> mnuIdListByMdIdMap = new HashMap<String, List<String>>();
		
		if(mnuTree!=null){
			for(PtMnuDVo ptMnuDVo : mnuTree){
				ptMnuLoutCombDVo = new PtMnuLoutCombDVo();
				ptMnuLoutCombDVo.setMnuLoutId(mnuLoutId);
				ptMnuLoutCombDVo.setLoutCatId(loutCatId);
				ptMnuLoutCombDVo.setMnuLoutCombPid(mnuLoutCombPid);
				ptMnuLoutCombDVo.setMnuLoutCombId(IdUtil.createId('M', ++index, 8));
				ptMnuLoutCombDVo.setFldYn(ptMnuDVo.getFldYn());
				ptMnuLoutCombDVo.setMnuId(ptMnuDVo.getMnuId());
				ptMnuLoutCombDVo.setRescNm(ptMnuDVo.getRescNm());
				ptMnuLoutCombDVo.setMnuRescId(ptMnuDVo.getRescId());
				ptMnuLoutCombDVo.setSortOrdr(index.toString());
				if("Y".equals(ptMnuDVo.getFldYn())){
					childTree = new ArrayList<PtMnuLoutCombDVo>();
					index = makeMnuLoutTree(mnuLoutId, ptMnuLoutCombDVo.getMnuLoutCombId(), loutCatId, index, childTree, ptMnuDVo.getChildList(), loutCombByCombIdMap, ptMnuDVoListByMdRidMap, mnuIdListByMdIdMap);
					if(!childTree.isEmpty()){
						ptMnuLoutCombDVo.setChildList(childTree);
						mnuLoutTree.add(ptMnuLoutCombDVo);
					} else {
						index--;
					}
				} else {
					setMnuUrlToComb(ptMnuLoutCombDVo, ptMnuDVo);
					mnuLoutTree.add(ptMnuLoutCombDVo);
					loutCombByCombIdMap.put(Hash.hashId(ptMnuLoutCombDVo.getMnuLoutCombId()), ptMnuLoutCombDVo);
					
					// 메뉴에 [모듈참조ID(BB,KM)]가 있으면 - ['모듈참조ID' 별 '메뉴ID' List] 에  '메뉴ID'를 담음 - 메뉴에 붙은 게시판 목록 중 권한있는 게시판목록 체크용
					mdId = ptMnuDVo.getMdId();
					mdRid = ptMnuDVo.getMdRid();
					// F로 시작하면 기능메뉴
					if(mdRid!=null && !mdRid.isEmpty() && mdId!=null && !mdId.isEmpty() && mdId.charAt(0)!='F'){
						
						mnuIdList = mnuIdListByMdIdMap.get(mdRid);
						ptMnuDVoList = ptMnuDVoListByMdRidMap.get(mdRid);
						
						if(mnuIdList==null){
							mnuIdList = new ArrayList<String>();
							mnuIdListByMdIdMap.put(mdRid, mnuIdList);
						}
						if(!mnuIdList.contains(ptMnuDVo.getMnuId())){
							mnuIdList.add(ptMnuDVo.getMnuId());
						}
						
						if(ptMnuDVoList==null){
							ptMnuDVoList = new ArrayList<PtMnuDVo>();
							ptMnuDVoListByMdRidMap.put(mdRid, ptMnuDVoList);
						}
						addIfNotHave(ptMnuDVoList, ptMnuDVo);
					}
					
				}
			}
		}
		
		return index;
	}
	
	/** 메뉴가 리스트에 없으면 더함 */
	private void addIfNotHave(List<PtMnuDVo> ptMnuDVoList, PtMnuDVo  ptMnuDVo){
		for(PtMnuDVo ptMnuDVoOfList : ptMnuDVoList){
			if(ptMnuDVo.getMnuId().equals(ptMnuDVoOfList.getMnuId())){
				return;
			}
		}
		ptMnuDVoList.add(ptMnuDVo);
	}
	
	/** 메뉴URL 및 함수 세팅 */
	public void setMnuUrlToComb(PtMnuLoutCombDVo ptMnuLoutCombDVo, PtMnuDVo ptMnuDVo){
		ptMnuLoutCombDVo.setPtMnuDVo(ptMnuDVo);
		
		//메뉴구분코드 - IN_URL:내부URL, IN_POP:내부팝업, OUT_URL:외부URL, OUT_POP:외부팝업
		String mnuTypCd = ptMnuDVo.getMnuTypCd();
		ptMnuLoutCombDVo.setMnuUrl(LoutUtil.addMenuId(ptMnuDVo.getMnuUrl(), ptMnuLoutCombDVo.getMnuLoutCombId()));
		if("IN_URL".equals(mnuTypCd)){
			ptMnuLoutCombDVo.setMnuUrl(LoutUtil.addMenuId(ptMnuDVo.getMnuUrl(), ptMnuLoutCombDVo.getMnuLoutCombId()));
		} else if("IN_POP".equals(mnuTypCd) || "OUT_POP".equals(mnuTypCd)){
			ptMnuLoutCombDVo.setMnuUrl(LoutUtil.addMenuId(ptMnuDVo.getMnuUrl(), ptMnuLoutCombDVo.getMnuLoutCombId()));
			ptMnuLoutCombDVo.setMnuFnc(LoutUtil.getMnuPopFnc(
					ptMnuLoutCombDVo.getMnuLoutCombId(),
					ptMnuDVo.getMnuUrl(),
					ptMnuDVo.getPopSetupCont(),
					ptMnuLoutCombDVo.getRescNm(),
					"IN_POP".equals(mnuTypCd) ));
		} else if("OUT_URL".equals(mnuTypCd)){
			ptMnuLoutCombDVo.setMnuUrl(LoutUtil.getOuterMnuUrl(
					ptMnuLoutCombDVo.getMnuLoutCombId(),
					ptMnuDVo.getMnuId()));
		}
	}
	
	/** 회사별 콤보레이아웃(메뉴레이아웃조합상세) 조회 - 레이아웃 관리에서 [메뉴조합 구성]의 좌측 메뉴 */
	private void loadCombLoutTreeAndMap(String compId, String langTypCd,
			Map<Integer, List<PtMnuLoutCombDVo>> loutCombTreeByLoutIdMap,
			Map<Integer, PtMnuLoutCombDVo> loutCombByCombIdMap) throws SQLException {
		
		// tree 구성용 맵
		Map<Integer, List<PtMnuLoutCombDVo>> treeMap = new HashMap<Integer, List<PtMnuLoutCombDVo>>();
		
		// 회사별 서브레이아웃(메뉴레이아웃조합상세) 조회(DB)
		List<PtMnuLoutCombDVo> allLoutCombList = getLoutCombListFromTable(compId, langTypCd);
		// 메뉴ID별 메뉴맵 조회(캐쉬)
		Map<Integer, PtMnuDVo> mnuByMnuIdMap = getMnuByMnuIdMap(langTypCd);
		
		// 부모ID
		List<String> pidList = new ArrayList<String>();
		
		String loutCatId = null;//레이아웃유형ID - B:기본레이아웃, I:아이콘레이아웃, A:관리자레이아웃, M:모바일레이아웃
		String mnuLoutId = null;//메뉴레이아웃ID
		String mnuLoutCombPid = null;//메뉴레이아웃조합부모ID
		String rescNm;
		List<PtMnuLoutCombDVo> loutList = null;
		
		// tree 구성용 - 부모ID기준으로 treeMap에 List로 담음
		if(allLoutCombList!=null && !allLoutCombList.isEmpty()){
			
			for(PtMnuLoutCombDVo ptMnuLoutCombDVo : allLoutCombList){
				
				// 메뉴에서 가져온 경우 - 메뉴의 리소스명 세팅
				rescNm = ptMnuLoutCombDVo.getRescNm();
				if(rescNm==null || rescNm.isEmpty()){
					ptMnuLoutCombDVo.setRescNm(ptMnuLoutCombDVo.getMnuRescNm());
				}
				
				// B:기본레이아웃, I:아이콘레이아웃, A:관리자레이아웃, M:모바일레이아웃 - 소트되어서 A먼저 나오며, A와 나머지를 구별 해야함
				if(loutCatId==null || !loutCatId.equals(ptMnuLoutCombDVo.getLoutCatId())){
					loutCatId = ptMnuLoutCombDVo.getLoutCatId();
					mnuLoutId = null;
				}
				
				// 메뉴레이아웃ID - map의 key
				if(mnuLoutId==null || !mnuLoutId.equals(ptMnuLoutCombDVo.getMnuLoutId())){
					mnuLoutId = ptMnuLoutCombDVo.getMnuLoutId();
					pidList.add(mnuLoutId);
					mnuLoutCombPid = null;
				}
				
				if(mnuLoutCombPid==null || !mnuLoutCombPid.equals(ptMnuLoutCombDVo.getMnuLoutCombPid())){
					mnuLoutCombPid = ptMnuLoutCombDVo.getMnuLoutCombPid();
					loutList = new ArrayList<PtMnuLoutCombDVo>();
					treeMap.put(Hash.hashId(mnuLoutCombPid), loutList);
				}
				
				loutList.add(ptMnuLoutCombDVo);
			}
			
			// tree 구성
			for(String pid : pidList){
				loutList = treeMap.get(Hash.hashId(pid));
				if(makeCombLoutTree(loutList, treeMap, loutCombByCombIdMap, mnuByMnuIdMap)){
					loutCombTreeByLoutIdMap.put(Hash.hashId(pid), loutList);
				}
			}
		}
	}
	
	/** 서브레이아웃 트리형태로 생성 - 레이아웃 관리에서 [메뉴조합 구성]의 좌측 메뉴
	 *  - 자식노드가 있는지 여부 리턴, 메뉴가 없으면 제거함 */
	private boolean makeCombLoutTree(List<PtMnuLoutCombDVo> loutList, Map<Integer, List<PtMnuLoutCombDVo>> treeMap,
			Map<Integer, PtMnuLoutCombDVo> loutCombByCombIdMap, Map<Integer, PtMnuDVo> mnuByMnuIdMap){
		List<PtMnuLoutCombDVo> childList;
		boolean hasChild = false;
		String mnuTypCd;
		PtMnuDVo ptMnuDVo;
		PtMnuLoutCombDVo ptMnuLoutCombDVo;
		int i, size = loutList==null ? 0 : loutList.size();
		for(i=0;i<size;i++){
			ptMnuLoutCombDVo = loutList.get(i);
			if("Y".equals(ptMnuLoutCombDVo.getFldYn())){
				childList = treeMap.get(Hash.hashId(ptMnuLoutCombDVo.getMnuLoutCombId()));
				if(childList!=null && !childList.isEmpty() && makeCombLoutTree(childList, treeMap, loutCombByCombIdMap, mnuByMnuIdMap)){
					hasChild = true;
					ptMnuLoutCombDVo.setChildList(childList);
				} else {
					loutList.remove(i);
					i--;
					size--;
				}
			} else {
				// 해당 메뉴가 없으면 제거
				ptMnuDVo = mnuByMnuIdMap.get(Hash.hashId(ptMnuLoutCombDVo.getMnuId()));
				if(ptMnuDVo==null){
					loutList.remove(i);
					i--;
					size--;
				} else {
					hasChild = true;
					ptMnuLoutCombDVo.setPtMnuDVo(ptMnuDVo);
					
					//메뉴구분코드 - IN_URL:내부URL, IN_POP:내부팝업, OUT_URL:외부URL, OUT_POP:외부팝업
					mnuTypCd = ptMnuDVo.getMnuTypCd();
					if("IN_URL".equals(mnuTypCd)){
						ptMnuLoutCombDVo.setMnuUrl(LoutUtil.addMenuId(ptMnuDVo.getMnuUrl(), ptMnuLoutCombDVo.getMnuLoutCombId()));
					} else if("IN_POP".equals(mnuTypCd) || "OUT_POP".equals(mnuTypCd)){
						ptMnuLoutCombDVo.setMnuUrl(LoutUtil.addMenuId(ptMnuDVo.getMnuUrl(), ptMnuLoutCombDVo.getMnuLoutCombId()));
						ptMnuLoutCombDVo.setMnuFnc(LoutUtil.getMnuPopFnc(
								ptMnuLoutCombDVo.getMnuLoutCombId(),
								ptMnuDVo.getMnuUrl(),
								ptMnuDVo.getPopSetupCont(),
								ptMnuLoutCombDVo.getRescNm(),
								"IN_POP".equals(mnuTypCd) ));
					} else if("OUT_URL".equals(mnuTypCd)){
						ptMnuLoutCombDVo.setMnuUrl(LoutUtil.getOuterMnuUrl(
								ptMnuLoutCombDVo.getMnuLoutCombId(),
								ptMnuDVo.getMnuId()));
					}
					loutCombByCombIdMap.put(Hash.hashId(ptMnuLoutCombDVo.getMnuLoutCombId()), ptMnuLoutCombDVo);
				}
			}
		}
		return hasChild;
	}
	
	/** 회사별 서브레이아웃(메뉴레이아웃조합상세) 조회(DB) */
	private List<PtMnuLoutCombDVo> getLoutCombListFromTable(String compId, String langTypCd) throws SQLException {
		// 메뉴레이아웃조합상세(PT_MNU_LOUT_COMB_D) 테이블 - 조합 조회
		PtMnuLoutCombDVo ptMnuLoutCombDVo = new PtMnuLoutCombDVo();
		ptMnuLoutCombDVo.setCompId(compId);
		if(ServerConfig.IS_MOBILE){
			ptMnuLoutCombDVo.setLoutCatId("M");
		} else {
			ptMnuLoutCombDVo.setWhereSqllet("AND LOUT_CAT_ID IN ('A','B','I')");
		}
		ptMnuLoutCombDVo.setQueryLang(langTypCd);
		ptMnuLoutCombDVo.setOrderBy("COMP_ID, LOUT_CAT_ID, MNU_LOUT_ID, MNU_LOUT_COMB_PID, SORT_ORDR");
		@SuppressWarnings("unchecked")
		List<PtMnuLoutCombDVo> ptMnuLoutCombDVoList = (List<PtMnuLoutCombDVo>)commonDao.queryList(ptMnuLoutCombDVo);
		return ptMnuLoutCombDVoList;
	}
	
	/** 레이아웃 목록 캐쉬에서 조회(DB) */
	@SuppressWarnings("unchecked")
	public List<PtMnuLoutDVo> getLoutListFromTable(String compId, String loutCatId, String langTypCd) throws SQLException {
		// 메뉴레이아웃상세(PT_MNU_LOUT_D) 테이블 - 아이콘 조회
		PtMnuLoutDVo ptMnuLoutDVo = new PtMnuLoutDVo();
		ptMnuLoutDVo.setCompId(compId);
		
		// loutCatId:레이아웃유형ID - B:기본레이아웃, I:아이콘레이아웃, A:관리자레이아웃, M:모바일레이아웃
		// - PtMnuLoutCtrl에서 호출할 때는 넘어오지만 캐쉬 로드 할 때는 NULL
		if(loutCatId == null){
			if(ServerConfig.IS_MOBILE){
				ptMnuLoutDVo.setLoutCatId("M");
			} else {
				ptMnuLoutDVo.setWhereSqllet("AND LOUT_CAT_ID IN ('A','B','I')");
			}
		} else {
			ptMnuLoutDVo.setLoutCatId(loutCatId);
		}
		ptMnuLoutDVo.setQueryLang(langTypCd);
		ptMnuLoutDVo.setOrderBy("LOUT_CAT_ID, LOUT_LOC_CD, SORT_ORDR");
		List<PtMnuLoutDVo> ptMnuLoutDVoList = (List<PtMnuLoutDVo>)commonDao.queryList(ptMnuLoutDVo);
		return ptMnuLoutDVoList;
	}
	
	/** 메뉴그룹ID 별 메뉴그룹 맵 조회(캐쉬) */
	private Map<Integer, PtMnuGrpBVo> getMnuGrpByGrpIdMap(String langTypCd) throws SQLException{
		@SuppressWarnings("unchecked")
		Map<Integer, PtMnuGrpBVo> map = (Map<Integer, PtMnuGrpBVo>)ptCacheSvc.getCache(CacheConfig.MENU_GRP, langTypCd, "MAP", 100);
		if(map!=null) return map;
		
		Map<Integer, PtMnuGrpBVo> mnuGrpMap = new HashMap<Integer, PtMnuGrpBVo>();
		Map<String, PtMnuGrpBVo> mnuGrpByMdRidMap = new HashMap<String, PtMnuGrpBVo>();
		loadMnuGrp(mnuGrpMap, mnuGrpByMdRidMap, langTypCd);
		
		ptCacheSvc.setCache(CacheConfig.MENU_GRP, langTypCd, "MAP", mnuGrpMap);
		ptCacheSvc.setCache(CacheConfig.MENU_GRP, langTypCd, "MD_RID_MAP", mnuGrpByMdRidMap);
		
		return mnuGrpMap;
	}
	
	/** 메뉴그룹ID 별 메뉴그룹 맵 조회(캐쉬) */
	public Map<String, PtMnuGrpBVo> getMnuGrpByMdRidMap(String langTypCd) throws SQLException{
		@SuppressWarnings("unchecked")
		Map<String, PtMnuGrpBVo> map = (Map<String, PtMnuGrpBVo>)ptCacheSvc.getCache(CacheConfig.MENU_GRP, langTypCd, "MD_RID_MAP", 100);
		if(map!=null) return map;
		
		Map<Integer, PtMnuGrpBVo> mnuGrpMap = new HashMap<Integer, PtMnuGrpBVo>();
		Map<String, PtMnuGrpBVo> mnuGrpByMdRidMap = new HashMap<String, PtMnuGrpBVo>();
		loadMnuGrp(mnuGrpMap, mnuGrpByMdRidMap, langTypCd);
		
		ptCacheSvc.setCache(CacheConfig.MENU_GRP, langTypCd, "MAP", mnuGrpMap);
		ptCacheSvc.setCache(CacheConfig.MENU_GRP, langTypCd, "MD_RID_MAP", mnuGrpByMdRidMap);
		
		return mnuGrpByMdRidMap;
	}
	
	/** 메뉴그룹을 조회하여 맵에 담음 */
	private void loadMnuGrp(Map<Integer, PtMnuGrpBVo> mnuGrpMap, Map<String, PtMnuGrpBVo> mnuGrpByMdRidMap, String langTypCd) throws SQLException{
		
		PtMnuGrpBVo ptMnuGrpBVo = new PtMnuGrpBVo();
		if(ServerConfig.IS_MOBILE){
			ptMnuGrpBVo.setMnuGrpMdCd("M");//메뉴그룹모듈코드 - A:관리자, U:사용자, M:모바일
		} else {
			ptMnuGrpBVo.setWhereSqllet("AND MNU_GRP_MD_CD IN ('A','U')");//메뉴그룹모듈코드 - A:관리자, U:사용자, M:모바일
		}
		ptMnuGrpBVo.setUseYn("Y");
		ptMnuGrpBVo.setQueryLang(langTypCd);
		ptMnuGrpBVo.setOrderBy("SORT_ORDR");
		@SuppressWarnings("unchecked")
		List<PtMnuGrpBVo> ptMnuGrpBVoList = (List<PtMnuGrpBVo>)commonDao.queryList(ptMnuGrpBVo);
		
		String mdRid;
		if(ptMnuGrpBVoList!=null){
			for(PtMnuGrpBVo storedPtMnuGrpBVo : ptMnuGrpBVoList){
				// 메모리 사이즈 점검
				storedPtMnuGrpBVo.setMnuGrpNm(null);
				storedPtMnuGrpBVo.setSysgYn(null);
				storedPtMnuGrpBVo.setRegrUid(null);
				storedPtMnuGrpBVo.setRegDt(null);
				storedPtMnuGrpBVo.setModrUid(null);
				storedPtMnuGrpBVo.setModDt(null);
				storedPtMnuGrpBVo.setMnuGrpTypNm(null);
				storedPtMnuGrpBVo.setPltLoutNm(null);
				storedPtMnuGrpBVo.setRegrNm(null);
				storedPtMnuGrpBVo.setModrNm(null);
				
				// 메뉴그룹ID 별 메뉴그룹 맵에 세팅
				mnuGrpMap.put(Hash.hashId(storedPtMnuGrpBVo.getMnuGrpId()), storedPtMnuGrpBVo);
				
				mdRid = storedPtMnuGrpBVo.getMdRid();
				if(mdRid!=null &&  !mdRid.isEmpty()){
					mnuGrpByMdRidMap.put(mdRid, storedPtMnuGrpBVo);
				}
			}
		}
	}
	
	/** 메뉴그룹ID로 메뉴그룹 조회(캐쉬) */
	public PtMnuGrpBVo getMnuGrpByGrpId(String mnuGrpId, String langTypCd) throws SQLException{
		Map<Integer, PtMnuGrpBVo> mnuGrpByGrpIdMap = getMnuGrpByGrpIdMap(langTypCd);
		if(mnuGrpByGrpIdMap!=null) return mnuGrpByGrpIdMap.get(Hash.hashId(mnuGrpId));
		return null;
	}
	
	/** 메뉴그룹ID 별 관리자 메뉴트리맵 조회(캐쉬) */
	@SuppressWarnings("unchecked")
	public Map<Integer, List<PtMnuDVo>> getMnuTreeByGrpIdMap(String langTypCd) throws SQLException{
		Map<Integer, List<PtMnuDVo>> treeMap = (Map<Integer, List<PtMnuDVo>>)
				ptCacheSvc.getCache(CacheConfig.MENU, langTypCd, "TREE", 100);
		if(treeMap!=null) return treeMap;
		
		Map<Integer, List<PtMnuDVo>> mnuTreeByGrpIdMap = new HashMap<Integer, List<PtMnuDVo>>();
		Map<Integer, List<PtMnuDVo>> mnuListByGrpIdMap = new HashMap<Integer, List<PtMnuDVo>>();
		Map<Integer, PtMnuDVo> mnuByMnuIdMap = new HashMap<Integer, PtMnuDVo>();
		loadMenuTreeAndMap(langTypCd, mnuTreeByGrpIdMap, mnuListByGrpIdMap, mnuByMnuIdMap);
		ptCacheSvc.setCache(CacheConfig.MENU, langTypCd, "TREE", mnuTreeByGrpIdMap);
		ptCacheSvc.setCache(CacheConfig.MENU, langTypCd, "LIST", mnuListByGrpIdMap);
		ptCacheSvc.setCache(CacheConfig.MENU, langTypCd, "MAP", mnuByMnuIdMap);
		return mnuTreeByGrpIdMap;
	}
	
	/** 메뉴ID별 메뉴맵 조회(캐쉬) */
	@SuppressWarnings("unchecked")
	public Map<Integer, PtMnuDVo> getMnuByMnuIdMap(String langTypCd) throws SQLException{
		Map<Integer, PtMnuDVo> mnuMap = (Map<Integer, PtMnuDVo>)
				ptCacheSvc.getCache(CacheConfig.MENU, langTypCd, "MAP", 100);
		if(mnuMap!=null) return mnuMap;
		
		Map<Integer, List<PtMnuDVo>> mnuTreeByGrpIdMap = new HashMap<Integer, List<PtMnuDVo>>();
		Map<Integer, List<PtMnuDVo>> mnuListByGrpIdMap = new HashMap<Integer, List<PtMnuDVo>>();
		Map<Integer, PtMnuDVo> mnuByMnuIdMap = new HashMap<Integer, PtMnuDVo>();
		loadMenuTreeAndMap(langTypCd, mnuTreeByGrpIdMap, mnuListByGrpIdMap, mnuByMnuIdMap);
		ptCacheSvc.setCache(CacheConfig.MENU, langTypCd, "TREE", mnuTreeByGrpIdMap);
		ptCacheSvc.setCache(CacheConfig.MENU, langTypCd, "LIST", mnuListByGrpIdMap);
		ptCacheSvc.setCache(CacheConfig.MENU, langTypCd, "MAP", mnuByMnuIdMap);
		return mnuByMnuIdMap;
	}
	
	/** 메뉴그룹ID 별 관리자 메뉴리스트 조회(캐쉬) */
	@SuppressWarnings("unchecked")
	public Map<Integer, List<PtMnuDVo>> getMnuListByGrpIdMap(String langTypCd) throws SQLException{
		Map<Integer, List<PtMnuDVo>> listMap = (Map<Integer, List<PtMnuDVo>>)
				ptCacheSvc.getCache(CacheConfig.MENU, langTypCd, "LIST", 100);
		if(listMap!=null) return listMap;
		
		Map<Integer, List<PtMnuDVo>> mnuTreeByGrpIdMap = new HashMap<Integer, List<PtMnuDVo>>();
		Map<Integer, List<PtMnuDVo>> mnuListByGrpIdMap = new HashMap<Integer, List<PtMnuDVo>>();
		Map<Integer, PtMnuDVo> mnuByMnuIdMap = new HashMap<Integer, PtMnuDVo>();
		loadMenuTreeAndMap(langTypCd, mnuTreeByGrpIdMap, mnuListByGrpIdMap, mnuByMnuIdMap);
		ptCacheSvc.setCache(CacheConfig.MENU, langTypCd, "TREE", mnuTreeByGrpIdMap);
		ptCacheSvc.setCache(CacheConfig.MENU, langTypCd, "LIST", mnuListByGrpIdMap);
		ptCacheSvc.setCache(CacheConfig.MENU, langTypCd, "MAP", mnuByMnuIdMap);
		return mnuListByGrpIdMap;
	}
	
	/** 사용자/관리자 별 메뉴를 TREE와 MAP으로 로드함(DB) */
	private void loadMenuTreeAndMap(String langTypCd,
			Map<Integer, List<PtMnuDVo>> mnuTreeByGrpIdMap,
			Map<Integer, List<PtMnuDVo>> mnuListByGrpIdMap,
			Map<Integer, PtMnuDVo> mnuByMnuIdMap) throws SQLException {
		
		// 메뉴상세(PT_MNU_D) 테이블 - 조회
		PtMnuDVo ptMnuDVo = new PtMnuDVo();
		ptMnuDVo.setUseYn("Y");
		ptMnuDVo.setQueryLang(langTypCd);
		if(ServerConfig.IS_MOBILE){
			ptMnuDVo.setMnuGrpMdCd("M");//메뉴그룹모듈코드 - A:관리자, U:사용자, M:모바일
			ptMnuDVo.setWhereSqllet("AND MNU_GRP_ID IN (SELECT MNU_GRP_ID FROM PT_MNU_GRP_B WHERE USE_YN = 'Y')");
		} else {
			ptMnuDVo.setWhereSqllet("AND MNU_GRP_MD_CD IN ('A','U') AND MNU_GRP_ID IN (SELECT MNU_GRP_ID FROM PT_MNU_GRP_B WHERE USE_YN = 'Y')");
		}
		ptMnuDVo.setOrderBy("MNU_GRP_MD_CD, MNU_GRP_ID, MNU_PID, SORT_ORDR");
		@SuppressWarnings("unchecked")
		List<PtMnuDVo> ptMnuDVoList = (List<PtMnuDVo>)commonDao.queryList(ptMnuDVo);
		
		String mnuGrpId = null, mnuPid = null;
		List<PtMnuDVo> mnuTreeList = null, rootTreeList, mnuList=null;
		Map<Integer, List<PtMnuDVo>> mnuGrpIdMap = null;
		
		PtMnuDVo myMnuSetupVo = null;
		
		// /pt/psn/my/setMyMnu.do
		
		// Tree 형태로 변환
		int i, size = ptMnuDVoList==null ? 0 : ptMnuDVoList.size();
		for(i=0;i<size;i++){
			ptMnuDVo = ptMnuDVoList.get(i);
			
			// 메모리 사이즈 점검
			ptMnuDVo.setMnuTypNm(null);
			ptMnuDVo.setRegrNm(null);
			ptMnuDVo.setModrNm(null);
			ptMnuDVo.setMnuNm(null);
			ptMnuDVo.setRegrUid(null);
			ptMnuDVo.setRegDt(null);
			ptMnuDVo.setModrUid(null);
			ptMnuDVo.setModDt(null);
			
			if(mnuGrpId==null || !mnuGrpId.equals(ptMnuDVo.getMnuGrpId())){
				// 트리 만들기 - 메뉴그룹ID 별로 메뉴 목록 세팅
				if(mnuGrpId!=null && mnuGrpIdMap!=null){
					rootTreeList = mnuGrpIdMap.get(Hash.hashId(mnuGrpId));
					if(rootTreeList!=null && !rootTreeList.isEmpty()){
						// 루트 메뉴 목록에 자식메뉴 목록을 붙여 tree 형태로 만듬
						if(makeMenuTree(rootTreeList, mnuGrpIdMap, mnuByMnuIdMap)){
							mnuTreeByGrpIdMap.put(Hash.hashId(mnuGrpId), rootTreeList);
						}
					}
				}
				// 트리 만들기 - 메뉴그룹ID 맵 생성
				mnuGrpIdMap = new HashMap<Integer, List<PtMnuDVo>>();
				mnuGrpId = ptMnuDVo.getMnuGrpId();
				mnuPid = null;
				
				// 리스트 만들기
				mnuList = mnuListByGrpIdMap.get(Hash.hashId(mnuGrpId));
				if(mnuList == null){
					mnuList = new ArrayList<PtMnuDVo>();
					mnuListByGrpIdMap.put(Hash.hashId(mnuGrpId), mnuList);
				}
			}
			
			// 트리 만들기 - 메뉴그룹ID 맵 에 넣을 메뉴목록 생성
			if(mnuPid==null || !mnuPid.equals(ptMnuDVo.getMnuPid())){
				mnuPid = ptMnuDVo.getMnuPid();
				mnuTreeList = new ArrayList<PtMnuDVo>();
				mnuGrpIdMap.put(Hash.hashId(mnuPid), mnuTreeList);
			}
			
			// 트리 만들기
			mnuTreeList.add(ptMnuDVo);
			
			// 리스트 만들기
			if(!"Y".equals(ptMnuDVo.getFldYn())){
				mnuList.add(ptMnuDVo);
			}
			
			if(myMnuSetupVo==null && PtConstant.URL_SET_MY_MNU.equals(ptMnuDVo.getMnuUrl())){
				myMnuSetupVo = ptMnuDVo;
			}
		}
		
		if(mnuGrpId!=null && mnuGrpIdMap!=null){
			rootTreeList = mnuGrpIdMap.get(Hash.hashId(mnuGrpId));
			// 루트 메뉴 목록에 자식메뉴 목록을 붙여 tree 형태로 만듬
			if(rootTreeList!=null && !rootTreeList.isEmpty()){
				if(makeMenuTree(rootTreeList, mnuGrpIdMap, mnuByMnuIdMap)){
					mnuTreeByGrpIdMap.put(Hash.hashId(mnuGrpId), rootTreeList);
				}
			}
		}
		
		if(myMnuSetupVo != null){
			// 나의메뉴 설정 - 메뉴로 처리
		}
	}
	
	/** 루트 메뉴 목록에 자식메뉴 목록을 붙여 tree 형태로 만듬 */
	private boolean makeMenuTree(List<PtMnuDVo> childList, 
			Map<Integer, List<PtMnuDVo>> mnuGrpIdMap, Map<Integer, PtMnuDVo> mnuByMnuIdMap){
		
		List<PtMnuDVo> subList;
		boolean hasChild = false;
		PtMnuDVo ptMnuDVo;
		int i, size = childList == null ? 0 : childList.size();
		for(i=0;i<size;i++){
			ptMnuDVo = childList.get(i);
			// 폴더도 멥에 넣어야 권한설정에서 - 폴더도 연동 가능함
			mnuByMnuIdMap.put(Hash.hashId(ptMnuDVo.getMnuId()), ptMnuDVo);
			if("Y".equals(ptMnuDVo.getFldYn())){
				subList = mnuGrpIdMap.get(Hash.hashId(ptMnuDVo.getMnuId()));
				if(subList!=null && !subList.isEmpty() && makeMenuTree(subList, mnuGrpIdMap, mnuByMnuIdMap)){
					ptMnuDVo.setChildList(subList);
					hasChild = true;
				} else {
					childList.remove(i);
					i--;
					size--;
				}
			} else {
				hasChild = true;
			}
		}
		return hasChild;
	}

	/** 레이아웃ID 로 메뉴그룹의 URL 구하기 */
	public String getMnuGrpUrlByMnuGrpId(String mnuGrpId, String langTypCd) throws SQLException{
		if(mnuGrpId==null || mnuGrpId.isEmpty()) return null;
		Map<Integer, PtMnuGrpBVo> mnuGrpByGrpIdMap = getMnuGrpByGrpIdMap(langTypCd);
		if(mnuGrpByGrpIdMap==null) return null;
		PtMnuGrpBVo ptMnuGrpBVo = mnuGrpByGrpIdMap.get(Hash.hashId(mnuGrpId));
		return ptMnuGrpBVo==null ? null : ptMnuGrpBVo.getMnuUrl();
	}
	/** 메뉴ID 로 메뉴 URL 구하기 */
	public String getMnuUrlByMnuId(String mnuId, String langTypCd) throws SQLException{
		if(mnuId==null || mnuId.isEmpty()) return null;
		Map<Integer, PtMnuDVo> mnuByMnuIdMap = getMnuByMnuIdMap(langTypCd);
		if(mnuByMnuIdMap==null) return null;
		PtMnuDVo ptMnuDVo = mnuByMnuIdMap.get(Hash.hashId(mnuId));
		return ptMnuDVo==null ? null : ptMnuDVo.getMnuUrl();
	}
	/** 메타테그 타이틀 설정 */
	public void setMetaTitle(HttpServletRequest request, String loutRescNm, String langTypCd) throws SQLException {
		// page TITLE 표시
		String uri = request.getRequestURI();
		if(uri.startsWith("/sh/")){
			Map<String, String> termMap = ptSysSvc.getTermMap("or.term", langTypCd);
			String siteName = termMap.get("siteName");
			if(siteName==null) siteName = messageProperties.getMessage("or.term.siteName", request);
			//pt.title.integratedSearch=통합검색
			String mdNm = messageProperties.getMessage("pt.title.integratedSearch", request);
			String metaTitle = mdNm+" | "+siteName;
			request.setAttribute("META_RESC", metaTitle);
			request.setAttribute("META_TITLE", metaTitle);
		} else {
			Map<String, String> termMap = ptSysSvc.getTermMap("or.term", langTypCd);
			String siteName = termMap.get("siteName");
			if(siteName==null) siteName = messageProperties.getMessage("or.term.siteName", request);
			String metaTitle = loutRescNm+" | "+siteName;
			request.setAttribute("META_RESC", metaTitle);
			request.setAttribute("META_TITLE", metaTitle);
		}
	}
}
