package com.innobiz.orange.web.pt.secu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.innobiz.orange.web.or.vo.OrUserBVo;


/**<pre>
 *  권한의 조합을 위한 상세 정보를 담는 객체(캐쉬용)
 *  - 권한그룹권한상세(PT_AUTH_GRP_AUTH_D) 데이터 중
 *    - 제외대상여부:EXCLI_YN, 일련번호:SEQ, 그룹종류코드:GRP_KND_CD, 그룹ID:GRP_ID 저장
 *    - 나머지 데이터는 상위 객체인 CombAuthGrp 에서 관리
 *  - (포함대상, 제외대상)에 따른 사용자그룹, 부서, 직위, 직급, 직책, 직무, 역할, 보안등급 을 가지고 있음
 * </pre>
 * */
public class CombAuthGrpDetl {

	/** 권한종류코드 */
	public static final String[] GRP_KND_CDS =  new String[]{"userGrp","dept","posit","grade","title","duty","role","secul","aduStat"};
	
	/** 배제대상 - 시퀀스별, 권한종류코드별, 아이디목록 */
	private List<Map<String, CombAuthGrpSubData[]>> exSubArrByKndCdMapList = null;
	
	/** 포함대상 - 시퀀스별, 권한종류코드별, 아이디목록 */
	private List<Map<String, CombAuthGrpSubData[]>> inSubArrByKndCdMapList = null;
	
	/** 권한그룹ID */
	private String authGrpId;
	
	/** 생성자 */
	public CombAuthGrpDetl(String authGrpId){
		this.authGrpId = authGrpId;
	}
	
	/** 권한그룹ID 리턴 */
	public String getAuthGrpId(){
		return authGrpId;
	}
	
	/** 제외여부와 시퀀스에 따른 서브데이터 추가 */
	public void add(boolean excli, int seq, String grpKndCd, CombAuthGrpSubData[] subDatas){
		List<Map<String, CombAuthGrpSubData[]>> subArrByKndCdMapList;
		if(excli){
			if(exSubArrByKndCdMapList==null) exSubArrByKndCdMapList = new ArrayList<Map<String, CombAuthGrpSubData[]>>();
			subArrByKndCdMapList = exSubArrByKndCdMapList;
		} else {
			if(inSubArrByKndCdMapList==null) inSubArrByKndCdMapList = new ArrayList<Map<String, CombAuthGrpSubData[]>>();
			subArrByKndCdMapList = inSubArrByKndCdMapList;
		}
		for(int i=subArrByKndCdMapList.size();i<=seq;i++){
			subArrByKndCdMapList.add(new HashMap<String, CombAuthGrpSubData[]>());
		}
		subArrByKndCdMapList.get(seq).put(grpKndCd, subDatas);
	}

	/** 해당 권한이 있는지 검사 */
	public boolean hasAuth(OrUserBVo orUserBVo, String[] roleCds, String[] orgPids,
			String[] inUserGrpIds) {
		if(hasAuth(exSubArrByKndCdMapList, orUserBVo, roleCds, orgPids, inUserGrpIds)){
			return false;
		}
		if(hasAuth(inSubArrByKndCdMapList, orUserBVo, roleCds, orgPids, inUserGrpIds)){
			return true;
		}
		return false;
	}
	
	/** 해당 권한이 있는지 검사 */
	private boolean hasAuth(List<Map<String, CombAuthGrpSubData[]>> seqKndMapList,
			OrUserBVo orUserBVo, String[] roleCds, String[] orgPids,
			String[] inUserGrpIds) {
		
		boolean matched, checked;
		int i, size, j, jsize = GRP_KND_CDS.length;
		Map<String, CombAuthGrpSubData[]> seqKndMap;
		CombAuthGrpSubData[] subs;
		
		size = seqKndMapList==null ? 0 : seqKndMapList.size();
		checked = false;
		for(i=0;i<size;i++){
			seqKndMap = seqKndMapList.get(i);
			matched = true;
			// seq level
			// 시퀀스별로 사용자그룹, 부서, 직급 .. 등의 정보가 모두 맞으면 true 리턴 - 아니면 다음 시퀀스의 모든 항목 검사
			for(j=0;j<jsize;j++){
				subs = seqKndMap.get(GRP_KND_CDS[j]);
				if(subs!=null){
					checked = true;
					if(j==0){//"userGrp"
						if(!inArrays(subs, inUserGrpIds)){
							matched = false;
							break;
						}
					} else if(j==1){//"dept"
						if(!inArray(subs, orUserBVo.getOrgId(), orgPids)){
							matched = false;
							break;
						}
					} else if(j==2){//"posit"
						if(!inArray(subs, orUserBVo.getPositCd())){
							matched = false;
							break;
						}
					} else if(j==3){//"grade"
						if(!inArray(subs, orUserBVo.getGradeCd())){
							matched = false;
							break;
						}
					} else if(j==4){//"title"
						if(!inArray(subs, orUserBVo.getTitleCd())){
							matched = false;
							break;
						}
					} else if(j==5){//"duty"
						if(!inArray(subs, orUserBVo.getDutyCd())){
							matched = false;
							break;
						}
					} else if(j==6){//"role"
						if(!inArrays(subs, roleCds)){
							matched = false;
							break;
						}
					} else if(j==7){//"secul"
						if(!inArray(subs, orUserBVo.getSeculCd())){
							matched = false;
							break;
						}
					} else if(j==8){//"aduStat"
						if(!inArray(subs, orUserBVo.getUserStatCd())){
							matched = false;
							break;
						}
					}
				}
			}
			
			// 시퀀스별로 사용자그룹, 부서, 직급 .. 등의 정보가 모두 맞으면
			// 안 맞으면 다음 시퀀스 데이터를 가지고 검사함
			if(matched && checked) return true;
		}
		return false;
	}
	
	/** SysAuthSubData[] 의 grpId 에 checkId 가 있는지 검사 */
	private boolean inArray(CombAuthGrpSubData[] subs, String checkId){
		if(checkId==null) return false;
		int i, size = subs.length;
		for(i=0;i<size;i++){
			if(checkId.equals(subs[i].grpId)) return true;
		}
		return false;
	}
	
	/** SysAuthSubData[] 의 grpId 에 orgPids에 이는 ID가 있는지 검사 */
	private boolean inArray(CombAuthGrpSubData[] subs, String checkId, String[] orgPids){
		if(checkId==null) return false;
		int i, size = subs.length;
		for(i=0;i<size;i++){
			if(subs[i].subIncl){
				if(inArray(orgPids, subs[i].grpId)) return true;
			} else {
				if(checkId.equals(subs[i].grpId)) return true;
			}
		}
		return false;
	}
	
	/** SysAuthSubData[] 의 grpId 에 checkId 가 있는지 검사 */
	private boolean inArray(String[] strs, String checkId){
		if(checkId==null) return false;
		int i, size = strs.length;
		for(i=0;i<size;i++){
			if(checkId.equals(strs[i])) return true;
		}
		return false;
	}
	
	/** SysAuthSubData[] 의 grpId 에 checkIds에 이는 ID가 있는지 검사 */
	private boolean inArrays(CombAuthGrpSubData[] subs, String[] checkIds){
		if(checkIds==null) return false;
		int i, j, size = subs.length, jsize = checkIds.length;
		for(i=0;i<size;i++){
			for(j=0;j<jsize;j++){
				if(checkIds[j]!=null && checkIds[j].equals(subs[i].grpId)) return true;
			}
		}
		return false;
	}
	
	
}
