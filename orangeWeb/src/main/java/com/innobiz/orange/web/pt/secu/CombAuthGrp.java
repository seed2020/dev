package com.innobiz.orange.web.pt.secu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.innobiz.orange.web.cm.utils.ArrayUtil;
import com.innobiz.orange.web.cm.utils.Hash;
import com.innobiz.orange.web.or.vo.OrUserBVo;

/**<pre>
 *  권한 조합 정보 - 권한의 조합을 위한 정보를 담는 객체(캐쉬용)
 *  - 권한그룹권한상세(PT_AUTH_GRP_AUTH_D) 데이터 중
 *    - AUTH_GRP_TYP_CD(권한그룹구분코드:U:사용자권한그룹, M:모바일권한그룹, A:관리자권한그룹, U:사용자그룹) 별 맵을 만들고
 *    - 맵에 AUTH_GRP_ID(권한그룹ID)를 키로(해쉬 처리 해서) 나머지 데이터 저장
 *      - 나머지 : 제외대상여부:EXCLI_YN, 일련번호:SEQ, 그룹종류코드:GRP_KND_CD, 그룹ID:GRP_ID
 * 	- 세부 항목(상세 데이터 : CombAuthGrpDetl) : (포함대상, 제외대상)에 따른 사용자그룹, 부서, 직위, 직급, 직책, 직무, 역할, 보안등급
 *  - 시스템에 캐쉬되어 사용됨
 *  - 사용자가 속한 권한정보(UserAuthGrp)를 가지고 getAdminAuthGrpIds() 함수를 이용하여 사용자가 속한 권한그룹의 목록을 String-Array 로 반환함
 * </pre>
 * */
public class CombAuthGrp {

	/** 권한조합 상세 데이터 맵 : 권한그룹구분코드- U:사용자권한그룹, M:모바일권한그룹 - KEY:권한그룹ID(authGrpId) */
	private Map<Integer, CombAuthGrpDetl> userAuthMap = null;
	
	/** 권한조합 상세 데이터 맵 : 권한그룹구분코드 - A:관리자권한그룹 - KEY:권한그룹ID(authGrpId) */
	private Map<Integer, CombAuthGrpDetl> admAuthMap = null;
	
	/** 권한조합 상세 데이터 맵 : 권한그룹구분코드 - G:사용자그룹 - KEY:권한그룹ID(authGrpId) */
	private Map<Integer, CombAuthGrpDetl> userGrpMap = null;
	
	/** 관리자 권한그룹 목록 리턴 */
	public String[] getAdminAuthGrpIds(UserAuthGrp userAuthGrp, OrUserBVo orUserBVo, String[] roleCds, String[] orgPids){
		List<String> adminAuthList = new ArrayList<String>();
		
		// 사용자권한 데이터의 사용자 그룹목록을 더함
		String[] inAuthGrpIds=null, exAuthGrpIds=null;
		String[] inUserGrpIds=null;
		if(userAuthGrp!=null){
			
			// 포함대상/제외대상 관리자권한그룹ID 목록
			inAuthGrpIds = userAuthGrp.getAuthGrps("A", false);
			exAuthGrpIds = userAuthGrp.getAuthGrps("A", true);
			
			// 포함대상/제외대상 사용자그룹ID 목록
			inUserGrpIds = userAuthGrp.getAuthGrps("G", false);
			
			// 관리자권한그룹ID 목록 에 있는것은 제외대상을 제외하고 포함 시킴
			if(inAuthGrpIds!=null){
				for(String grpId : inAuthGrpIds){
					if(exAuthGrpIds==null || !ArrayUtil.isInArray(exAuthGrpIds, grpId)){
						adminAuthList.add(grpId);
					}
				}
			}
		}
		
		// 조건조합 프로세싱
		addAuthList(adminAuthList, exAuthGrpIds, admAuthMap, orUserBVo, roleCds, orgPids, inUserGrpIds);
		return ArrayUtil.toArray(adminAuthList);
	}
	
	/** 사용자 권한그룹 목록 리턴 */
	public String[] getUserAuthGrpIds(UserAuthGrp userAuthGrp, OrUserBVo orUserBVo, String[] roleCds, String[] orgPids){
		List<String> userAuthList = new ArrayList<String>();
		
		// 사용자권한 데이터의 사용자 그룹목록을 더함
		String[] inAuthGrpIds=null, exAuthGrpIds=null;
		String[] inUserGrpIds=null;
		if(userAuthGrp!=null){
			
			// 포함대상/제외대상 사용자권한그룹ID 목록
			inAuthGrpIds = userAuthGrp.getAuthGrps("U", false);
			exAuthGrpIds = userAuthGrp.getAuthGrps("U", true);
			
			// 포함대상/제외대상 사용자그룹ID 목록
			inUserGrpIds = userAuthGrp.getAuthGrps("G", false);
			
			// 사용자권한그룹ID 목록 에 있는것은 제외대상을 제외하고 포함 시킴
			if(inAuthGrpIds!=null){
				for(String grpId : inAuthGrpIds){
					if(exAuthGrpIds==null || !ArrayUtil.isInArray(exAuthGrpIds, grpId)){
						userAuthList.add(grpId);
					}
				}
			}
		}
		// 조건조합 프로세싱
		addAuthList(userAuthList, exAuthGrpIds, userAuthMap, orUserBVo, roleCds, orgPids, inUserGrpIds);
		return ArrayUtil.toArray(userAuthList);
	}
	
	/** 조건조합을 이용해 포함사는 사용자 그룹을 계산함 */
	private void addAuthList(List<String> userAuthList, String[] exAuthGrpIds,
			Map<Integer, CombAuthGrpDetl> sysAuthMap,
			OrUserBVo orUserBVo, String[] roleCds, String[] orgPids, String[] inUserGrpIds) {
		
		if(sysAuthMap!=null){
			String authGrpId;
			CombAuthGrpDetl combAuthGrpDetl;
			Iterator<Entry<Integer, CombAuthGrpDetl>> entrys = sysAuthMap.entrySet().iterator();
			while(entrys.hasNext()){
				combAuthGrpDetl = entrys.next().getValue();
				authGrpId = combAuthGrpDetl.getAuthGrpId();
				if(exAuthGrpIds!=null && ArrayUtil.isInArray(exAuthGrpIds, authGrpId)) continue;
				if(combAuthGrpDetl.hasAuth(orUserBVo, roleCds, orgPids, inUserGrpIds)){
					userAuthList.add(authGrpId);
				}
			}
		}
	}

	/** 시스템 권한관리 상세 데이터 리턴 */
	private CombAuthGrpDetl getCombAuthGrpDetl(String authGrpTypCd, String authGrpId){
		Map<Integer, CombAuthGrpDetl> map = null;
		if("U".equals(authGrpTypCd) || "M".equals(authGrpTypCd)){
			if(userAuthMap==null) userAuthMap = new HashMap<Integer, CombAuthGrpDetl>();
			map = userAuthMap;
		} else if("A".equals(authGrpTypCd)){
			if(admAuthMap==null) admAuthMap = new HashMap<Integer, CombAuthGrpDetl>();
			map = admAuthMap;
		} else if("G".equals(authGrpTypCd)){
			if(userGrpMap==null) userGrpMap = new HashMap<Integer, CombAuthGrpDetl>();
			map = userGrpMap;
		}
		CombAuthGrpDetl combAuthGrpDetl = map.get(Hash.hashId(authGrpId));
		if(combAuthGrpDetl==null){
			combAuthGrpDetl = new CombAuthGrpDetl(authGrpId);
			map.put(Hash.hashId(authGrpId), combAuthGrpDetl);
		}
		return combAuthGrpDetl;
	}

	/** 시스템 권한관리 상세 데이터를 더함 */
	public void add(String authGrpTypCd, String authGrpId, String excliYn,
			String seq, String grpKndCd, List<CombAuthGrpSubData> subList) {
		getCombAuthGrpDetl(authGrpTypCd, authGrpId).add("Y".equals(excliYn), 
				Integer.parseInt(seq)-1, grpKndCd, toArray(subList));
	}
	
	/** 배열로 변환 */
	private CombAuthGrpSubData[] toArray(List<CombAuthGrpSubData> list){
		int i, size = list==null ? 0 : list.size();
		CombAuthGrpSubData[] arr = new CombAuthGrpSubData[size];
		for(i=0;i<size;i++){
			arr[i] = list.get(i);
		}
		return arr;
	}
	
}
