package com.innobiz.orange.web.pt.svc;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.utils.Hash;
import com.innobiz.orange.web.or.vo.OrOrgBVo;

/** 조직도 서비스 */
@Service
public class PtOrgSvc {
	
	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;

	/** 하위조직을 맵 형태로 리턴 */
	public Map<Integer, OrOrgBVo> querySubOrgMap(String orgPid) throws SQLException{
		OrOrgBVo orOrgBVo = new OrOrgBVo();
		orOrgBVo.setOrgPid(orgPid);
		@SuppressWarnings("unchecked")
		List<OrOrgBVo> orOrgBVoList = (List<OrOrgBVo>)commonDao.queryList(orOrgBVo);
		if(orOrgBVoList==null || orOrgBVoList.isEmpty()) return null;
		
		Map<Integer, OrOrgBVo> returnMap = new HashMap<Integer, OrOrgBVo>();
		for(OrOrgBVo storedOrOrgBVo : orOrgBVoList){
			returnMap.put(Hash.hashUid(storedOrOrgBVo.getOrgId()), storedOrOrgBVo);
		}
		return returnMap;
	}
	
}
