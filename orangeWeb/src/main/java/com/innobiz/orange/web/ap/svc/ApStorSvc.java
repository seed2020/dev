package com.innobiz.orange.web.ap.svc;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.innobiz.orange.web.ap.utils.ApConstant;
import com.innobiz.orange.web.ap.vo.ApStorCompRVo;
import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.pt.svc.PtCacheSvc;

/** 저장소 서비스 */
@Service
public class ApStorSvc {
	
	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;

	/** 캐쉬 서비스 */
	@Autowired
	private PtCacheSvc ptCacheSvc;
	
	/** 저장소 목록 - 캐쉬 */
	public List<ApStorCompRVo> getStorageListByComp(String compId, String langTypCd) throws SQLException{
		
		@SuppressWarnings("unchecked")
		List<ApStorCompRVo> list = (List<ApStorCompRVo>)ptCacheSvc.getCache(ApConstant.AP_STORAGE, langTypCd, compId, 200);
		if(list!=null){
			 if(!list.isEmpty()) return list;
			 else return null;
		}
		
		ApStorCompRVo apStorCompRVo = new ApStorCompRVo();
		apStorCompRVo.setCompId(compId);
		apStorCompRVo.setUseYn("Y");
		apStorCompRVo.setQueryLang(langTypCd);
		@SuppressWarnings("unchecked")
		List<ApStorCompRVo> apStorCompRVoList = (List<ApStorCompRVo>)commonDao.queryList(apStorCompRVo);
		if(apStorCompRVoList == null){
			ptCacheSvc.setCache(ApConstant.AP_STORAGE, langTypCd, compId, new ArrayList<ApStorCompRVo>());
			return null;
		}
		
		ptCacheSvc.setCache(ApConstant.AP_STORAGE, langTypCd, compId, apStorCompRVoList);
		return apStorCompRVoList;
		
		// 캐쉬 삭제
//		String dbTime = ptCacheExpireSvc.getDbTime();
//		ptCacheExpireSvc.expireAll(queryQueue, dbTime, ApConstant.AP_STORAGE);
//		commonSvc.execute(queryQueue);
//		ptCacheExpireSvc.checkNow(ApConstant.AP_STORAGE);
	}

}
