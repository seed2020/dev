package com.innobiz.orange.web.pt.secu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.innobiz.orange.web.cm.utils.Hash;
import com.innobiz.orange.web.pt.vo.PtAuthGrpMnuPltRVo;

/** 호출된 URL에 따른 메뉴가 결정된 후 해당 메뉴에 어떤 권한이 있는지 결정하는 객체 <br/>
 *      - 호출된 URL에 따른 메뉴가 결정 : AuthUrlMapper 가 담당함 <br/>
 *  - (메뉴 또는 포틀릿, 메뉴그룹, 레이아웃)별 권한그룹ID 와 해당 그룹의 권한(S:슈퍼, A:관리, M:수정, W:쓰기, R:읽기)을 가지고 있음 <br/>
 *  - PtSecuSvc 에 의해 로드되며 캐쉬 되어 사용됨 <br/>
 *  - (메뉴 또는 포틀릿, 메뉴그룹, 레이아웃)ID 별 권한이 있는지[hasAuth()] 및 최고 권한이 무었인지[getAuth()]를 구현함 <br/>
 *  */
public class AuthCdDecider {
	
	/** (메뉴/포틀릿/메뉴그룹/레이아웃) 별 권한코드 맵 */
	private Map<Integer, AuthCdVo[]> usrAuthMap;// Web:사용자권한그룹의 권한, Mobile:모바일권한그룹의 권한
	
	/** (메뉴/포틀릿/메뉴그룹/레이아웃) 별 권한코드 맵 */
	private Map<Integer, AuthCdVo[]> admAuthMap;
	
	/** (메뉴/포틀릿/메뉴그룹/레이아웃) 별 권한코드 설정 */
	public void setList(List<PtAuthGrpMnuPltRVo> ptAuthGrpMnuPltRVoList){
		
		// 권한그룹구분코드 - G:사용자그룹, U:사용자권한그룹, A:관리자권한그룹, M:모바일권한그룹
		String authGrpTypCd = null;
		String mnuPltId=null, authCd=null, authGrpId=null;
		
		Map<Integer, List<AuthCdVo>> listMap = null;
		Map<Integer, List<AuthCdVo>> usrListMap = new HashMap<Integer, List<AuthCdVo>>();
		Map<Integer, List<AuthCdVo>> admListMap = new HashMap<Integer, List<AuthCdVo>>();
		List<AuthCdVo> authList = null;
		
		if(ptAuthGrpMnuPltRVoList!=null){
			for(PtAuthGrpMnuPltRVo ptAuthGrpMnuPltRVo : ptAuthGrpMnuPltRVoList){
				
				// 권한그룹구분코드 - G:사용자그룹, U:사용자권한그룹, A:관리자권한그룹, M:모바일권한그룹
				if(authGrpTypCd==null || !authGrpTypCd.equals(ptAuthGrpMnuPltRVo.getAuthGrpTypCd())){
					authGrpTypCd = ptAuthGrpMnuPltRVo.getAuthGrpTypCd();
					listMap = "A".equals(authGrpTypCd) ? admListMap : usrListMap;
					mnuPltId = null;
				}
				
				// 메뉴포틀릿ID
				if(mnuPltId==null || !mnuPltId.equals(ptAuthGrpMnuPltRVo.getMnuPltId())){
					mnuPltId = ptAuthGrpMnuPltRVo.getMnuPltId();
					authList = new ArrayList<AuthCdVo>();
					listMap.put(Hash.hashId(mnuPltId), authList);
				}
				
				authGrpId = ptAuthGrpMnuPltRVo.getAuthGrpId();
				authCd = ptAuthGrpMnuPltRVo.getAuthCd();
				authList.add(new AuthCdVo(authGrpId, authCd));
			}
		}

		usrAuthMap = toArrayMap(usrListMap);
		admAuthMap = toArrayMap(admListMap);
	}
	
	/** List<AuthCdVo> 맵을 AuthCdVo[] 맵으로 변환 */
	private Map<Integer, AuthCdVo[]> toArrayMap(Map<Integer, List<AuthCdVo>> listMap){
		Map<Integer, AuthCdVo[]> returnMap = new HashMap<Integer, AuthCdVo[]>();
		Iterator<Entry<Integer, List<AuthCdVo>>> iterator = listMap.entrySet().iterator();
		Entry<Integer, List<AuthCdVo>> entry;
		AuthCdVo[] authCdVos;
		while(iterator.hasNext()){
			entry = iterator.next();
			authCdVos = toArray(entry.getValue());
			if(authCdVos!=null){
				returnMap.put(entry.getKey(), authCdVos);
			}
		}
		return returnMap;
	}
	
	
	/** 배열로 변환  */
	private AuthCdVo[] toArray(List<AuthCdVo> authList){
		if(authList!=null){
			int index = 0;
			AuthCdVo[] arr = new AuthCdVo[authList.size()];
			for(AuthCdVo authCdVo : authList){
				arr[index++] = authCdVo;
			}
			return arr;
		}
		return null;
	}
	
	/** 관리자 권한이 있는지 검사 */
	public boolean hasAdmAuth(String mnuPltId, String[] authGrpIds){
		return hasAuth(mnuPltId, authGrpIds, admAuthMap);
	}
	
	/** 사용자 권한이 있는지 검사 */
	public boolean hasUsrAuth(String mnuPltId, String[] authGrpIds){
		return hasAuth(mnuPltId, authGrpIds, usrAuthMap);
	}
	
	/** 사용자 권한이 있는지 검사 */
	public boolean hasUsrAuthOf(String mnuPltId, String[] authGrpIds, String authCd){
		return hasAuthOf(mnuPltId, authGrpIds, usrAuthMap, authCd);
	}
	
	/** 사용자(또는 관리자) 권한이 있는지 검사 */
	private boolean hasAuth(String mnuPltId, String[] authGrpIds, Map<Integer, AuthCdVo[]> authCdMap){
		AuthCdVo[] authCdVos = authCdMap.get(Hash.hashId(mnuPltId));
		if(authCdVos==null || authCdVos.length==0) return false;
		int i, size = authGrpIds.length, j, jsize = authCdVos.length;
		for(i=0;i<size;i++){
			for(j=0;j<jsize;j++){
				if(authGrpIds[i].equals(authCdVos[j].authGrpId)){
					return true;
				}
			}
		}
		return false;
	}
	
	/** 사용자(또는 관리자) 권한이 있는지 검사 */
	private boolean hasAuthOf(String mnuPltId, String[] authGrpIds, Map<Integer, AuthCdVo[]> authCdMap, String authCd){
		AuthCdVo[] authCdVos = authCdMap.get(Hash.hashId(mnuPltId));
		if(authCdVos==null || authCdVos.length==0) return false;
		int i, size = authGrpIds.length, j, jsize = authCdVos.length;
		for(i=0;i<size;i++){
			for(j=0;j<jsize;j++){
				if(authGrpIds[i].equals(authCdVos[j].authGrpId)){
					if(authCd.equals(authCdVos[j].authCd)) return true;
					if(authCdVos[j].higherThan(authCd)) return true;
				}
			}
		}
		return false;
	}
	
	
	
	/** 관리자 메뉴(포틀릿)에 대한 권한코드 리턴 */
	public String getAdmAuthCd(String mnuPltId, String[] authGrpIds){
		return getAuthCd(mnuPltId, authGrpIds, admAuthMap);
	}
	
	/** 사용자 메뉴(포틀릿)에 대한 권한코드 리턴 */
	public String getUsrAuthCd(String mnuPltId, String[] authGrpIds){
		return getAuthCd(mnuPltId, authGrpIds, usrAuthMap);
	}
	
	/** 관리자/사용자 메뉴(포틀릿)에 대한 권한코드 리턴 */
	private String getAuthCd(String mnuPltId, String[] authGrpIds, Map<Integer, AuthCdVo[]> authCdMap){
		AuthCdVo[] authCdVos = authCdMap.get(Hash.hashId(mnuPltId));
		if(authCdVos==null || authCdVos.length==0) return null;
		
		String authCd = null;
		int i, size = authGrpIds.length, j, jsize = authCdVos.length;
		for(i=0;i<size;i++){
			for(j=0;j<jsize;j++){
				// 권한 순으로 정렬 되어 있어 authGrpId가 같은게 있으면 다음 부터는 같거나 낮음 >> break
				if(authGrpIds[i].equals(authCdVos[j].authGrpId)){
					if(authCdVos[j].higherThan(authCd)){
						authCd = authCdVos[j].authCd;
					}
					break;
				}
			}
		}
		return authCd;
	}
}
