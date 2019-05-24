package com.innobiz.orange.web.wb.svc;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.utils.Hash;
import com.innobiz.orange.web.pt.svc.PtCacheSvc;
import com.innobiz.orange.web.wb.vo.WbBcFldBVo;

@Service
public class WbBcFldSvc {
	
	/** Logger */
	//private static final Logger LOGGER = Logger.getLogger(WbBcFldSvc.class);
	
	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;
	
	/** 캐쉬 서비스 */
	@Autowired
	private PtCacheSvc ptCacheSvc;
	
	/** 하위 폴더 트리 조회 */
	public List<WbBcFldBVo> getDownTreeList(String compId, String fldId, String langTypCd, boolean first, boolean isPub, String useYn) throws SQLException{
		Map<Integer, WbBcFldBVo> fldMap = getFldMap(langTypCd, compId, isPub);
		Map<Integer, List<String>> fldSubListMap = getFldSubListMap(langTypCd, compId, isPub);
		List<WbBcFldBVo> returnList = new ArrayList<WbBcFldBVo>();
		setDownTreeList(fldId, returnList, fldSubListMap, fldMap, first, useYn);
		
		/*System.out.println("returnList.size() : "+returnList.size());
		for(WbBcFldBVo storedWbBcFldBVo : returnList){
			System.out.println("storedWbBcFldBVo.getFldNm() : "+storedWbBcFldBVo.getFldNm()+"\t fldId : "+storedWbBcFldBVo.getBcFldId());
		}*/
		
		return returnList;
	}
	
	/** 하위 폴더 정보를 returnList 에 담음 */
	private void setDownTreeList(String fldId, List<WbBcFldBVo> returnList, 
			Map<Integer, List<String>> fldSubListMap, Map<Integer, WbBcFldBVo> fldMap, boolean first, String useYn){
		int hashId = Hash.hashId(fldId);
		WbBcFldBVo wbBcFldBVo = fldMap.get(hashId);
		if(wbBcFldBVo!=null){
			if(first && ((useYn == null || (useYn != null && useYn.equals(wbBcFldBVo.getUseYn()))))) returnList.add(wbBcFldBVo);
			List<String> mdSubList = fldSubListMap.get(hashId);
			if(mdSubList!=null){
				for(String subBcFldId : mdSubList){
					setDownTreeList(subBcFldId, returnList, fldSubListMap, fldMap, true, useYn);
				}
			}
		}
	}
	
	/** 폴더 맵 리턴(캐쉬) */
	private Map<Integer, WbBcFldBVo> getFldMap(String langTypCd, String compId, boolean isPub) throws SQLException{
		String cacheKey=isPub ? WbConstant.PUB : WbConstant.FLD;
		@SuppressWarnings("unchecked")
		Map<Integer, WbBcFldBVo> fldMap = (Map<Integer, WbBcFldBVo>)
				ptCacheSvc.getCache(cacheKey, langTypCd, "MAP"+compId, 10);
		if(fldMap!=null) return fldMap;
		
		fldMap = new HashMap<Integer, WbBcFldBVo>();
		Map<Integer, List<String>> subIdListMap = new HashMap<Integer, List<String>>();
		loadFldMap(langTypCd, compId, fldMap, subIdListMap, isPub);
		ptCacheSvc.setCache(cacheKey, langTypCd, "MAP"+compId,  fldMap);
		ptCacheSvc.setCache(cacheKey, langTypCd, "SUB_ID_LIST_MAP"+compId,  subIdListMap);
		return fldMap;
	}
	
	/** 서브 폴더 목록 맵 리턴(캐쉬) */
	private Map<Integer, List<String>> getFldSubListMap(String langTypCd, String compId, boolean isPub) throws SQLException{
		String cacheKey=isPub ? WbConstant.PUB : WbConstant.FLD;
		@SuppressWarnings("unchecked")
		Map<Integer, List<String>> subIdListMap = (Map<Integer, List<String>>)
				ptCacheSvc.getCache(cacheKey, langTypCd, "SUB_ID_LIST_MAP"+compId, 10);
		if(subIdListMap!=null) return subIdListMap;
		Map<Integer, WbBcFldBVo> fldMap = new HashMap<Integer, WbBcFldBVo>();
		subIdListMap = new HashMap<Integer, List<String>>();
		loadFldMap(langTypCd, compId, fldMap, subIdListMap, isPub);
		ptCacheSvc.setCache(cacheKey, langTypCd, "MAP"+compId,  fldMap);
		ptCacheSvc.setCache(cacheKey, langTypCd, "SUB_ID_LIST_MAP"+compId,  subIdListMap);
		return subIdListMap;
	}
	
	/** 폴더맵, 서브 폴더 목록 맵 조회 */
	private void loadFldMap(String langTypCd, String compId,
			Map<Integer, WbBcFldBVo> fldMap, Map<Integer, List<String>> subIdListMap, boolean isPub) throws SQLException{
		
		// 목록 조회
		WbBcFldBVo wbBcFldBVo = new WbBcFldBVo();
		wbBcFldBVo.setQueryLang(langTypCd);
		wbBcFldBVo.setCompId(compId);
		wbBcFldBVo.setPub(isPub); // 공유명함여부
		if(isPub) wbBcFldBVo.setOrderBy("FLD_PID, SORT_ORDR");
		else wbBcFldBVo.setOrderBy("ABV_FLD_ID, SORT_ORDR");
		@SuppressWarnings("unchecked")
		List<WbBcFldBVo> wbBcFldBVoList = (List<WbBcFldBVo>)commonDao.queryList(wbBcFldBVo);
		
		WbBcFldBVo fldVo;
		String abvFldId=null, fldId;
		List<String> idList = null;
		int i, size = wbBcFldBVoList==null ? 0 : wbBcFldBVoList.size();
		for(i=0;i<size;i++){
			fldVo = wbBcFldBVoList.get(i);
			fldId = fldVo.getBcFldId();
			abvFldId = fldVo.getAbvFldId();
			if(!subIdListMap.containsKey(Hash.hashId(abvFldId)))
				subIdListMap.put(Hash.hashId(abvFldId), new ArrayList<String>());
			idList = subIdListMap.get(Hash.hashId(abvFldId));
			idList.add(fldVo.getBcFldId());
			if(!fldVo.getBcFldId().equals(abvFldId))
				fldMap.put(Hash.hashId(fldId), fldVo);
		}
	}
	
}
