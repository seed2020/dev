package com.innobiz.orange.web.cu.svc;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.utils.ArrayUtil;
import com.innobiz.orange.web.or.svc.OrCmSvc;
import com.innobiz.orange.web.or.vo.OrOrgBVo;
import com.innobiz.orange.web.or.vo.OrUserBVo;
import com.innobiz.orange.web.pt.secu.UserVo;

@Service
public class CuDyneCstmMgmSvc {
	/** Logger */
	//private static final Logger LOGGER = Logger.getLogger(CuDyneCstmMgmSvc.class);
	
	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;
	
	/** 조직 공통 서비스 */
	@Autowired
	private OrCmSvc orCmSvc;
	
	
	/** 조직도에 사용자 추가 */
	public void addOrgAddUserList(List<OrOrgBVo> orgList, List<OrUserBVo> orUserBVoList, List<String> exclUserUidList) {
		if(orUserBVoList==null || orUserBVoList.size()==0) return;
		
		String[] exclUserUids = ArrayUtil.toArray(exclUserUidList);
		OrOrgBVo newOrOrgBVo;
		for(OrUserBVo storedOrUserBVo : orUserBVoList) {
			if(exclUserUids!=null && ArrayUtil.isInArray(exclUserUids, storedOrUserBVo.getUserUid())) continue;
			newOrOrgBVo=new OrOrgBVo();
			newOrOrgBVo.setOrgId(storedOrUserBVo.getUserUid());
			newOrOrgBVo.setOrgPid(storedOrUserBVo.getOrgId());
			newOrOrgBVo.setRescNm(storedOrUserBVo.getRescNm());
			newOrOrgBVo.setOrgTypCd("F");
			newOrOrgBVo.setSortOrdr("user"+storedOrUserBVo.getSortOrdr());
			newOrOrgBVo.setRescId(storedOrUserBVo.getRescId());
			newOrOrgBVo.setUseYn(ArrayUtil.isInArray(new String[] {"01", "02", "03", "04"}, storedOrUserBVo.getUserStatCd()) ? "Y" : "N");
			orgList.add(newOrOrgBVo);
		}
	}
	
	/** [트리] - 조직도에 사용자 추가 */
	@SuppressWarnings("unchecked")
	public List<OrOrgBVo> getOrgAddUserList(String compId, String langTypCd, UserVo userVo, boolean isUserAdd, boolean isAdmin) throws SQLException{
		// 조직 조회
		List<OrOrgBVo> orgList = null;
		
		OrUserBVo orUserBVo;
		
		if(isAdmin) {
			orgList = orCmSvc.getOrgTreeList(compId, null, langTypCd);
		}else {
			orgList = orCmSvc.getDownTreeList(userVo.getOrgId(), langTypCd);
		}
		
		if(orgList==null || orgList.size()==0 || !isUserAdd) return orgList;
		
		// 사용자 조회
		orUserBVo = new OrUserBVo();
		orUserBVo.setQueryLang(langTypCd);
		orUserBVo.setCompId(compId);
		// 사용자상태코드 - 01:사용신청, 02:근무중, 03:휴직, 04:정직, 05:퇴직, 11:해제, 99:삭제
		// orUserBVo.setUserStatCdList(ArrayUtil.toList(new String[]{"05","11","99"}, false));
		
		List<OrUserBVo> orUserBVoList;
		if(isAdmin) {
			// 목록 조회
			orUserBVoList = (List<OrUserBVo>)commonDao.queryList(orUserBVo);
			addOrgAddUserList(orgList, orUserBVoList, null);
		}else {
			// 제외할 사용자UID
			List<String> exclUserUidList=null;
			// 조직ID 목록
			List<String> orgIdList = new ArrayList<String>();
			for(OrOrgBVo storedOrOrgBVo : orgList) {
				orgIdList.add(storedOrOrgBVo.getOrgId());
			}
			
			for(String paramOrgId : orgIdList) {
				orUserBVo.setOrgId(paramOrgId);
				// 목록 조회
				orUserBVoList = (List<OrUserBVo>)commonDao.queryList(orUserBVo);
				// 자신이 속한 조직에서는 다른사람을 제외한다.
				exclUserUidList=getExclUserUidList(orUserBVoList, userVo.getUserUid(), userVo.getOrgId());
				addOrgAddUserList(orgList, orUserBVoList, exclUserUidList);
			}
		}
		return orgList;
	}
	
	/** 제외할 사용자UID 추출 */
	public List<String> getExclUserUidList(List<OrUserBVo> orUserBVoList, String userUId, String orgId){
		List<String> exclUserUidList=new ArrayList<String>();
		for(OrUserBVo storedOrUserBVo : orUserBVoList) {
			if(orgId.equals(storedOrUserBVo.getOrgId()) && !userUId.equals(storedOrUserBVo.getUserUid())) exclUserUidList.add(storedOrUserBVo.getUserUid());
		}
		return exclUserUidList;
	}
	
}
