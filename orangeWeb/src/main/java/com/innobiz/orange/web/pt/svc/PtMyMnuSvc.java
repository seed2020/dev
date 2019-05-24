package com.innobiz.orange.web.pt.svc;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.utils.Hash;
import com.innobiz.orange.web.pt.secu.AuthCdDecider;
import com.innobiz.orange.web.pt.secu.IpChecker;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.utils.LoutUtil;
import com.innobiz.orange.web.pt.utils.PtConstant;
import com.innobiz.orange.web.pt.vo.PtMnuDVo;
import com.innobiz.orange.web.pt.vo.PtMnuLoutCombDVo;
import com.innobiz.orange.web.pt.vo.PtMnuLoutDVo;
import com.innobiz.orange.web.pt.vo.PtMyMnuDVo;
import com.innobiz.orange.web.pt.vo.PtMyPltRVo;

/** 나의메뉴 서비스 */
@Service
public class PtMyMnuSvc {
	
	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;
	
	/** 사용자 개인 설정 서비스 */
	@Autowired
	private PtPsnSvc ptPsnSvc;
	
	/** 포털 보안 서비스 */
	@Resource(name = "ptSecuSvc")
	private PtSecuSvc ptSecuSvc;
	
	/** 메뉴 레이아웃 서비스 */
	@Autowired
	private PtLoutSvc ptLoutSvc;
	
	/** IP 체크용 객체 - IP 조회 및 정책 적용 */
	@Autowired
	private IpChecker ipChecker;
	
	/** 개인화 메뉴 설정 정보 리턴 */
	public Map<String, String> getMyMnuSetup(HttpServletRequest request, String userUid) throws SQLException{
		Map<String, String> myMnuMap = ptPsnSvc.getUserSetupMap(request, userUid, PtConstant.PT_MY_MNU, false);
		if(myMnuMap.isEmpty()){
			return null;
		}
		return myMnuMap;
	}
	
	/** userUid 별 설정된 포틀릿 목록 조회 */
	public List<PtMyPltRVo> getMyPltList(String userUid, String pltLoutCd, String langTypCd) throws SQLException{
		
		PtMyPltRVo ptMyPltRVo = new PtMyPltRVo();
		ptMyPltRVo.setUserUid(userUid);
		ptMyPltRVo.setQueryLang(langTypCd);
		if("FREE".equals(pltLoutCd)){
			ptMyPltRVo.setWhereSqllet("AND ZIDX IS NOT NULL AND PLT_ID IN (SELECT PLT_ID FROM PT_PLT_D WHERE USE_YN='Y')");
			ptMyPltRVo.setOrderBy("ZIDX");
		} else {
			ptMyPltRVo.setWhereSqllet("AND ZIDX IS NULL AND PLT_ID IN (SELECT PLT_ID FROM PT_PLT_D WHERE USE_YN='Y')");
			ptMyPltRVo.setOrderBy("PLT_ZONE_CD, SORT_ORDR");
		}
		
		@SuppressWarnings("unchecked")
		List<PtMyPltRVo> ptMyPltRVoList = (List<PtMyPltRVo>)commonDao.queryList(ptMyPltRVo);
		return ptMyPltRVoList;
	}

	/** 권한 있는 나의메뉴 설정 목록 조회 */
	public List<PtMyMnuDVo> getPtMyMnuDVoList(UserVo userVo, String langTypCd, AuthCdDecider authCdDecider) throws SQLException{
		PtMyMnuDVo ptMyMnuDVo = new PtMyMnuDVo();
		ptMyMnuDVo.setUserUid(userVo.getUserUid());
		ptMyMnuDVo.setQueryLang(langTypCd);
		ptMyMnuDVo.setOrderBy("MNU_LOUT_COMB_PID, SORT_ORDR");
		@SuppressWarnings("unchecked")
		List<PtMyMnuDVo> ptMyMnuDVoList = (List<PtMyMnuDVo>)commonDao.queryList(ptMyMnuDVo);
		
		if(ptMyMnuDVoList==null) return null;
		// 권한 있는 것 추리기
		String[] userAuthGrpIds = userVo.getUserAuthGrpIds();
		List<PtMyMnuDVo> returnList = new ArrayList<PtMyMnuDVo>();
		for(PtMyMnuDVo storedPtMyMnuDVo : ptMyMnuDVoList){
			if("Y".equals(storedPtMyMnuDVo.getFldYn())){
				returnList.add(storedPtMyMnuDVo);
			} else {
				if(authCdDecider == null || authCdDecider.hasUsrAuth(storedPtMyMnuDVo.getMnuId(), userAuthGrpIds)){
					returnList.add(storedPtMyMnuDVo);
				}
			}
		}
		return returnList.isEmpty() ? null : returnList;
	}
	
	/** 마이메뉴 조회 */
	public PtMyMnuDVo getPtMyMnuDVo(UserVo userVo, String mnuLoutCombId, String langTypCd) throws SQLException{
		PtMyMnuDVo ptMyMnuDVo = new PtMyMnuDVo();
		ptMyMnuDVo.setUserUid(userVo.getUserUid());
		ptMyMnuDVo.setMnuLoutCombId(mnuLoutCombId);
		ptMyMnuDVo.setQueryLang(langTypCd);
		
		ptMyMnuDVo = (PtMyMnuDVo)commonDao.queryVo(ptMyMnuDVo);
		return ptMyMnuDVo;
	}
	
	/** 권한 있는 나의메뉴 설정 목록 조회 - 메뉴 구성용 트리로 구성해서 리턴 */
	public PtMnuLoutCombDVo getMyPtMnuLoutCombDVoList(UserVo userVo, String langTypCd, AuthCdDecider authCdDecider,
			String menuId, List<PtMnuLoutCombDVo> returnList) throws SQLException{
		
		List<PtMyMnuDVo> ptMyMnuDVoList = getPtMyMnuDVoList(userVo, langTypCd, authCdDecider);
		if(ptMyMnuDVoList==null) return null;
		
		PtMnuDVo ptMnuDVo;
		Map<Integer, PtMnuDVo> mnuByMnuIdMap = ptLoutSvc.getMnuByMnuIdMap(langTypCd);
		String[] userAuthGrpIds = userVo.getUserAuthGrpIds();
		
		Integer hashId;
		String mnuLoutCombPid = userVo.getUserUid();
		List<PtMnuLoutCombDVo> childList = null, brotherList = returnList;
		Map<Integer, List<PtMnuLoutCombDVo>> treeMap = new HashMap<Integer, List<PtMnuLoutCombDVo>>();
		treeMap.put(Hash.hashId(userVo.getUserUid()), returnList);
		PtMnuLoutCombDVo ptMnuLoutCombDVo, returnPtMnuLoutCombDVo = null;
		for(PtMyMnuDVo ptMyMnuDVo : ptMyMnuDVoList){
			
			ptMnuLoutCombDVo = new PtMnuLoutCombDVo();
			ptMnuLoutCombDVo.setMnuLoutCombPid(ptMyMnuDVo.getMnuLoutCombPid());
			ptMnuLoutCombDVo.setMnuLoutCombId(ptMyMnuDVo.getMnuLoutCombId());
			ptMnuLoutCombDVo.setFldYn(ptMyMnuDVo.getFldYn());
			ptMnuLoutCombDVo.setMnuId(ptMyMnuDVo.getMnuId());
			ptMnuLoutCombDVo.setRescNm(ptMyMnuDVo.getRescNm());
			ptMnuLoutCombDVo.setMnuRescId(ptMyMnuDVo.getRescId());
			if(ptMnuLoutCombDVo.getRescNm()==null || ptMnuLoutCombDVo.getRescNm().isEmpty()){
				ptMnuLoutCombDVo.setRescNm(ptMyMnuDVo.getMnuRescNm());
			}
			
			if(menuId!=null && menuId.equals(ptMnuLoutCombDVo.getMnuLoutCombId())){
				returnPtMnuLoutCombDVo = ptMnuLoutCombDVo;
			}
			
			if(!mnuLoutCombPid.equals(ptMnuLoutCombDVo.getMnuLoutCombPid())){
				mnuLoutCombPid = ptMnuLoutCombDVo.getMnuLoutCombPid();
				hashId = Hash.hashId(mnuLoutCombPid);
				brotherList = treeMap.get(hashId);
				if(brotherList == null){
					brotherList = new ArrayList<PtMnuLoutCombDVo>();
					treeMap.put(hashId, brotherList);
				}
			}
			
			if("Y".equals(ptMyMnuDVo.getFldYn())){
				hashId = Hash.hashId(ptMnuLoutCombDVo.getMnuLoutCombId());
				childList = treeMap.get(hashId);
				if(childList == null){
					childList = new ArrayList<PtMnuLoutCombDVo>();
					treeMap.put(hashId, childList);
				}
				ptMnuLoutCombDVo.setChildList(childList);
				brotherList.add(ptMnuLoutCombDVo);
			} else {
				ptMnuDVo = mnuByMnuIdMap.get(Hash.hashId(ptMnuLoutCombDVo.getMnuId()));
				if(ptMnuDVo != null){
					if(authCdDecider==null || authCdDecider.hasUsrAuth(ptMnuDVo.getMnuId(), userAuthGrpIds)){
						ptLoutSvc.setMnuUrlToComb(ptMnuLoutCombDVo, ptMnuDVo);
						brotherList.add(ptMnuLoutCombDVo);
					}
				}
			}
		}
		removeEmptyFolder(returnList);
		
		return returnPtMnuLoutCombDVo;
	}
	
	/** 빈 자식 폴더 제거 - 메뉴가 없는 것은 return true */
	private boolean removeEmptyFolder(List<PtMnuLoutCombDVo> childList){
		if(childList==null || childList.isEmpty()) return true;
		int i, size = childList.size();
		PtMnuLoutCombDVo ptMnuLoutCombDVo;
		boolean isEmpty = true;
		for(i=0;i<size;i++){
			ptMnuLoutCombDVo = childList.get(i);
			if(!"Y".equals(ptMnuLoutCombDVo.getFldYn())){
				isEmpty = false;
			} else {
				if(removeEmptyFolder(ptMnuLoutCombDVo.getChildList())){
					childList.remove(i);
					i--;
					size--;
				} else {
					isEmpty = false;
				}
			}
		}
		return isEmpty;
	}
	
	/** 나의메뉴 - 첫째 메뉴 URL 리턴 */
	public String getFirstMyPage(HttpServletRequest request, UserVo userVo) throws SQLException{
		
		// 설정이 없으면 - 설정세팅 페이지
		Map<String, String> myMnuMap = getMyMnuSetup(request, userVo.getUserUid());
		if(myMnuMap==null) return ptSecuSvc.toAuthMenuUrl(userVo, PtConstant.URL_SET_MY_MNU, null);
		
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 포틀릿을 사용하겠다고 하면 - 포틀릿 조회용 페이지
		if("Y".equals(myMnuMap.get("usePlt"))){
			PtMnuLoutDVo ptMnuLoutDVo = ptLoutSvc.getMnuLoutByMdRid(PtConstant.MNU_GRP_REF_MY,
					userVo.getLoutCatId(), userVo.getCompId(), langTypCd);
			if(ptMnuLoutDVo != null){
				return LoutUtil.getMnuGrpUrl(ptMnuLoutDVo.getMnuGrpId(), ptMnuLoutDVo.getMnuLoutId());
			}
		}
		
		if(!"Y".equals(myMnuMap.get("useMnu"))){
			return ptSecuSvc.toAuthMenuUrl(userVo, PtConstant.URL_SET_MY_MNU, null);
		}
		
		// 외부망 권한 적용
		boolean isExAuth = ipChecker.isExAuth(userVo);
		boolean isAdmin = userVo.isAdmin();
		// 권한 체크용
		AuthCdDecider authCdDecider = !isExAuth && isAdmin ? null : ptSecuSvc.getAuthCdDecider(userVo.getCompId(),
				userVo.isInternalIp() ? PtConstant.AUTH_IP_IN : PtConstant.AUTH_IP_EX);
		
		// 나의메뉴 목록조회
		List<PtMnuLoutCombDVo> sideList = new ArrayList<PtMnuLoutCombDVo>();
		getMyPtMnuLoutCombDVoList(userVo, langTypCd, authCdDecider, null, sideList);
		
		// 나의메뉴의 첫째 유효한 URL 리턴
		String url = ptLoutSvc.getFirstValidUrl(sideList);
		if(url != null) return url;
		
		// 설정세팅 페이지
		return ptSecuSvc.toAuthMenuUrl(userVo, PtConstant.URL_SET_MY_MNU, null);
	}
	
}
