package com.innobiz.orange.web.ap.svc;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.innobiz.orange.web.ap.vo.ApApvLnGrpDVo;
import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.utils.ArrayUtil;
import com.innobiz.orange.web.cm.utils.Hash;
import com.innobiz.orange.web.or.svc.OrCmSvc;
import com.innobiz.orange.web.or.vo.OrOrgBVo;
import com.innobiz.orange.web.or.vo.OrUserBVo;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtCmSvc;
import com.innobiz.orange.web.pt.vo.PtCdBVo;

/** 결재선 서비스 */
@Service
public class ApApvLnSvc {
	
	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;

	/** 포털 공통 서비스 */
	@Autowired
	private PtCmSvc ptCmSvc;

	/** 조직 공통 서비스 */
	@Autowired
	private OrCmSvc orCmSvc;
	
	/** 자동결재선 설정에 의한 사용자 목록 */
	public List<OrUserBVo> getAutoApvLnUserList(UserVo userVo, String autoApvLnCd, List<ApApvLnGrpDVo> apApvLnGrpDVoList) throws SQLException {
		
		// 자동결재선코드 목록
		String[] cds = getAutoApvLnCds(userVo.getAutoApvLnCd(), autoApvLnCd, userVo.getLangTypCd());
		if(cds == null) return null;
		
		String[] dupChkUids = getApvLnGrpUserUids(apApvLnGrpDVoList);
		
		// 현부서, 상위부서 ID
		String[] upOrgIds = getUptreeOrgIds(userVo);
		
		// 현부서, 상위부서의 - 자동결재선코드 사용자 조회
		OrUserBVo orUserBVo = new OrUserBVo();
		orUserBVo.setOrderBy("SORT_ORDR");
		
		Integer hashId;
		Map<Integer, OrUserBVo> orUserBVoMap = new HashMap<Integer, OrUserBVo>();
		String storedAutoApvLnCd;
		
		// 하위부서부터 루프 돌면서, 자동결재선코드 에 해당하는 사용자를 맵에 담음 - 중복 방지용
		for(String orgId : upOrgIds){
			orUserBVo.setOrgId(orgId);
			orUserBVo.setQueryLang(userVo.getLangTypCd());
			
			@SuppressWarnings("unchecked")
			List<OrUserBVo> orUserBVoList = (List<OrUserBVo>)commonDao.queryList(orUserBVo);
			if(orUserBVoList != null){
				for(OrUserBVo storedOrUserBVo : orUserBVoList){
					
					// 자동결재선코드 가 - 해당 하면
					storedAutoApvLnCd = storedOrUserBVo.getAutoApvLnCd();
					if(storedAutoApvLnCd!=null && !storedAutoApvLnCd.isEmpty()
							&& ArrayUtil.isInArray(cds, storedAutoApvLnCd)){
						
						// 이미 결재선에 있는 경우(통보 제외하고)
						if(dupChkUids!=null && ArrayUtil.isInArray(dupChkUids, storedOrUserBVo.getUserUid())){
							continue;
						}
						
						hashId = Hash.hashId(storedAutoApvLnCd);
						
						if(orUserBVoMap.get(hashId) == null){
							orUserBVoMap.put(hashId, storedOrUserBVo);
						}
					}
				}
			}
		}
		
		if(!orUserBVoMap.isEmpty()){
			List<OrUserBVo> orUserBVoList = new ArrayList<OrUserBVo>();
			for(String cd : cds){
				orUserBVo = orUserBVoMap.get(Hash.hashId(cd));
				if(orUserBVo!=null){
					orUserBVoList.add(orUserBVo);
				}
			}
			
			if(!orUserBVoList.isEmpty()){
				return orUserBVoList;
			}
		}
		return null;
	}
	
	/** 파라미터 결재경로그룹상세 에서 부서가 아니고 개인통보가 아닌 사용자UID 배열을 리턴 */
	private String[] getApvLnGrpUserUids(List<ApApvLnGrpDVo> apApvLnGrpDVoList){
		if(apApvLnGrpDVoList==null || apApvLnGrpDVoList.isEmpty()) return null;
		
		List<String> list = new ArrayList<String>();
		for(ApApvLnGrpDVo apApvLnGrpDVo : apApvLnGrpDVoList){
			if(!"Y".equals(apApvLnGrpDVo.getApvrDeptYn())
					&& !"psnInfm".equals(apApvLnGrpDVo.getApvrRoleCd())){
				
				list.add(apApvLnGrpDVo.getUserUid());
			}
		}
		
		if(list.isEmpty()) return null;
		String[] arr = new String[list.size()];
		for(int i=0; i<arr.length; i++){
			arr[i] = list.get(i);
		}
		return arr;
	}
	
	/** 자동결재선코드 의 내뒤 부터 지정된 코드 목록 리턴 */
	private String[] getAutoApvLnCds(String myAutoApvLnCd, String endAutoApvLnCd, String langTypCd) throws SQLException{
		
		// 자동결재선코드
		List<PtCdBVo> autoApvLnCdList = ptCmSvc.getCdList("AUTO_APV_LN_CD", langTypCd, "Y");
		if(autoApvLnCdList == null) return null;
		
		boolean emptyMyAutoApvLnCd = myAutoApvLnCd==null || myAutoApvLnCd.isEmpty();
		
		boolean matchEnd = false;
		List<String> cdList = new ArrayList<String>();
		if(emptyMyAutoApvLnCd){
			for(PtCdBVo ptCdBVo : autoApvLnCdList){
				cdList.add(ptCdBVo.getCd());
				if(endAutoApvLnCd.equals(ptCdBVo.getCd())){
					matchEnd = true;
					break;
				}
			}
		} else {
			for(PtCdBVo ptCdBVo : autoApvLnCdList){
				if(myAutoApvLnCd.equals(ptCdBVo.getCd())){
					// 초기화 시킴
					cdList = new ArrayList<String>();
				} else {
					cdList.add(ptCdBVo.getCd());
					if(endAutoApvLnCd.equals(ptCdBVo.getCd())){
						matchEnd = true;
						break;
					}
				}
			}
		}
		
		if(!matchEnd || cdList.isEmpty()) return null;
		
		String[] arr = new String[cdList.size()];
		for(int i=0;i<arr.length;i++){
			arr[i] = cdList.get(i);
		}
		return arr;
	}
	
	/** 상위 부서 조직ID - 하위 부터 자신 포함하여 */
	private String[] getUptreeOrgIds(UserVo userVo) throws SQLException{
		
		List<OrOrgBVo> upTreeList = orCmSvc.getUpTreeList(userVo.getOrgId(), userVo.getLangTypCd());
		if(upTreeList==null || upTreeList.isEmpty()){
			return new String[]{ userVo.getOrgId() };
		}
		
		String[] arr = new String[1+upTreeList.size()];
		// 자신 포함
		arr[0] = userVo.getOrgId();
		
		// 역순으로
		int j = 1;
		for(int i=upTreeList.size()-1;i>=0;i--){
			arr[j++] = upTreeList.get(i).getOrgId();
		}
		
		return arr;
	}
	
}
