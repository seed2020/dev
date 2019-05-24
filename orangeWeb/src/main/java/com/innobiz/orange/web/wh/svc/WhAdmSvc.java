package com.innobiz.orange.web.wh.svc;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.innobiz.orange.web.cm.config.CacheConfig;
import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.utils.Hash;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.CommonVo;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.or.vo.OrOrgBVo;
import com.innobiz.orange.web.or.vo.OrUserBVo;
import com.innobiz.orange.web.pt.svc.PtCacheSvc;
import com.innobiz.orange.web.pt.svc.PtCmSvc;
import com.innobiz.orange.web.pt.svc.PtSysSvc;
import com.innobiz.orange.web.pt.vo.PtCompBVo;
import com.innobiz.orange.web.wh.utils.WhConstant;
import com.innobiz.orange.web.wh.vo.WhCatGrpBVo;
import com.innobiz.orange.web.wh.vo.WhCatGrpLVo;
import com.innobiz.orange.web.wh.vo.WhMdBVo;
import com.innobiz.orange.web.wh.vo.WhMdPichLVo;
import com.innobiz.orange.web.wh.vo.WhPichGrpBVo;
import com.innobiz.orange.web.wh.vo.WhPichGrpLVo;
import com.innobiz.orange.web.wh.vo.WhReqEvalDVo;
import com.innobiz.orange.web.wh.vo.WhReqOngdDVo;
import com.innobiz.orange.web.wh.vo.WhRescBVo;

@Service
public class WhAdmSvc {
	/** Logger */
	//private static final Logger LOGGER = Logger.getLogger(WhAdmSvc.class);
	
	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;
	
//	/** 메세지 */
//	@Autowired
//    private MessageProperties messageProperties;
	
	/** 포탈 공통 서비스 */
	@Autowired
	private PtCmSvc ptCmSvc;
	
	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
	/** 캐쉬 서비스 */
	@Autowired
	private PtCacheSvc ptCacheSvc;
	
	/** 모듈 트리 목록 조회 */
	public List<WhMdBVo> getTreeMd(){
		List<WhMdBVo> returnList = null;
		
		return returnList;
	}
	
	/** 모듈 목록 조회 */
	@SuppressWarnings("unchecked")
	public List<WhMdBVo> getMdTreeList(String compId, String useYn, String langTypCd) throws SQLException{
		WhMdBVo whMdBVo = new WhMdBVo();
		whMdBVo.setQueryLang(langTypCd);
		// 회사 ID 조회조건 추가[계열사 설정 확인]
		if(compId!=null) setCompAffiliateIdList(compId, langTypCd, whMdBVo, false);
		if(useYn!=null) whMdBVo.setUseYn(useYn);
		whMdBVo.setOrderBy("MD_PID, SORT_ORDR");
		return (List<WhMdBVo>)commonDao.queryList(whMdBVo);
	}
	
	/** 모듈 정보 조회 */
	public WhMdBVo getWhMdBVo(String compId, String mdId, String langTypCd) throws SQLException{
		List<String> compIdList=getCompAffiliateIdList(compId, langTypCd, false);
		if(compIdList!=null && compIdList.size()>0)
			return getWhMdBVo(compIdList, mdId, langTypCd);
		Map<Integer, WhMdBVo> mdMap = getMdMap(langTypCd, compId);
		int hashId = Hash.hashId(mdId);
		return mdMap.get(hashId);
	}
	
	/** 모듈 정보 조회 - 계열사 및 전사 포함*/
	public WhMdBVo getWhMdBVo(List<String> compIdList, String mdId, String langTypCd) throws SQLException{
		Map<Integer, WhMdBVo> mdMap = null;
		if(compIdList.size()==1) 
			mdMap = getMdMap(langTypCd, compIdList.get(0));
		else{
			mdMap=new HashMap<Integer, WhMdBVo>();
			Map<Integer, WhMdBVo> tmpMap=null;
			for(String id : compIdList){
				tmpMap = getMdMap(langTypCd, id);
				if(tmpMap==null) continue;
				mdMap.putAll(tmpMap);
			}
		}
		int hashId = Hash.hashId(mdId);
		return mdMap.get(hashId);
	}
	
	/** 모듈 정보 조회 - DB */
	public WhMdBVo getWhMdBVo(String mdId, String langTypCd) throws SQLException{
		WhMdBVo whMdBVo = new WhMdBVo();
		whMdBVo.setQueryLang(langTypCd);
		whMdBVo.setMdId(mdId);
		return (WhMdBVo)commonDao.queryVo(whMdBVo);
	}
	
	/** 하위 모듈 정보 조회 */
	public List<String> getSubIdList(String compId, String mdId, String langTypCd) throws SQLException{
		Map<Integer, List<String>> mdMap = getMdSubListMap(langTypCd, compId);
		int hashId = Hash.hashId(mdId);
		return mdMap.get(hashId);
	}
	
	/** 하위 모듈ID 전체목록 조회 */
	public List<String> getSubIdAllList(List<String> mdIdList, String compId, String mdId, String langTypCd) throws SQLException{
		if(langTypCd==null) langTypCd="ko";
		if(mdIdList==null) mdIdList=new ArrayList<String>();		
		List<String> returnList=getSubIdList(compId, mdId, langTypCd);
		if(returnList!=null && returnList.size()>0){
			mdIdList.addAll(returnList);
			for(String subId : returnList){
				return getSubIdAllList(mdIdList, compId, subId, langTypCd);
			}
		}
		return mdIdList;
	}
	
	/** 상위 모듈ID 전체목록 조회 */
	public void setTopIdList(String mdId, List<String> returnList, Map<Integer, WhMdBVo> mdMap, boolean first) throws SQLException{
		int hashId = Hash.hashId(mdId);
		WhMdBVo whMdBVo = mdMap.get(hashId);
		if(whMdBVo!=null){
			if(first) returnList.add(mdId);
			setTopIdList(whMdBVo.getMdPid(), returnList, mdMap, true);
		}
	}
	
	/** 상위 모듈ID 전체목록 조회 */
	public List<String> getTopIdAllList(List<String> returnList, String compId, String mdId, String langTypCd, boolean first) throws SQLException{
		if(langTypCd==null) langTypCd="ko";
		if(returnList==null) returnList=new ArrayList<String>();
		
		Map<Integer, WhMdBVo> mdMap = getMdMap(langTypCd, compId);
		setTopIdList(mdId, returnList, mdMap, first);
		
		return returnList;
	}
	
	
	/** 최상위 모듈 정보 조회 */
	public WhMdBVo getTopTreeVo(String compId, String mdId, String langTypCd) throws SQLException{
		Map<Integer, WhMdBVo> mdMap = getMdMap(langTypCd, compId);
		WhMdBVo whMdBVo = getTopMdVo(mdId, new WhMdBVo(), mdMap);
		return whMdBVo;
	}
	
	/** 최상위 모듈 정보를 리턴 */
	private WhMdBVo getTopMdVo(String mdPid, WhMdBVo whMdBVo, 
			Map<Integer, WhMdBVo> mdMap){
		int hashId = Hash.hashId(mdPid);
		if(mdMap.get(hashId)!=null){
			whMdBVo = mdMap.get(hashId); 
			return getTopMdVo(whMdBVo.getMdPid(), whMdBVo, mdMap);
		}
		return whMdBVo;
	}
	
	/** 상위 모듈 트리 조회 */
	public List<WhMdBVo> getTopTreeList(String compId, String mdId, String langTypCd, boolean first) throws SQLException{
		Map<Integer, WhMdBVo> mdMap = getMdMap(langTypCd, compId);
		//Map<Integer, List<String>> mdSubListMap = getMdSubListMap(langTypCd, compId);
		List<WhMdBVo> returnList = new ArrayList<WhMdBVo>();
		setTopTreeList(mdId, returnList, mdMap, first);		
		return returnList;
	}
	
	/** 상위 모듈 정보를 returnList 에 담음 */
	private void setTopTreeList(String mdId, List<WhMdBVo> returnList, 
			Map<Integer, WhMdBVo> mdMap, boolean first){
		int hashId = Hash.hashId(mdId);
		WhMdBVo whMdBVo = mdMap.get(hashId);
		if(whMdBVo!=null){
			if(first) returnList.add(whMdBVo);
			setTopTreeList(whMdBVo.getMdPid(), returnList, mdMap, true);
		}
	}
	
	/** 하위 모듈 트리 조회 */
	public List<WhMdBVo> getDownTreeList(String compId, String mdId, String langTypCd, boolean first, String useYn) throws SQLException{
		Map<Integer, WhMdBVo> mdMap = getMdMap(langTypCd, compId);
		Map<Integer, List<String>> mdSubListMap = getMdSubListMap(langTypCd, compId);
		List<WhMdBVo> returnList = new ArrayList<WhMdBVo>();
		setDownTreeList(mdId, returnList, mdSubListMap, mdMap, first, useYn);
		return returnList;
	}
	
	/** 하위 모듈 정보를 returnList 에 담음 */
	private void setDownTreeList(String mdId, List<WhMdBVo> returnList, 
			Map<Integer, List<String>> mdSubListMap, Map<Integer, WhMdBVo> mdMap, boolean first, String useYn){
		int hashId = Hash.hashId(mdId);
		WhMdBVo whMdBVo = mdMap.get(hashId);
		if(whMdBVo!=null){
			if(first && ((useYn == null || (useYn != null && useYn.equals(whMdBVo.getUseYn()))))) returnList.add(whMdBVo);
			List<String> mdSubList = mdSubListMap.get(hashId);
			if(mdSubList!=null){
				for(String subMdId : mdSubList){
					setDownTreeList(subMdId, returnList, mdSubListMap, mdMap, true, useYn);
				}
			}
		}
	}
	
	/** 모듈 맵 리턴(캐쉬) */
	private Map<Integer, WhMdBVo> getMdMap(String langTypCd, String compId) throws SQLException{
		@SuppressWarnings("unchecked")
		Map<Integer, WhMdBVo> mdMap = (Map<Integer, WhMdBVo>)
				ptCacheSvc.getCache(WhConstant.MD, langTypCd, "MAP"+compId, 10);
		if(mdMap!=null) return mdMap;
		
		mdMap = new HashMap<Integer, WhMdBVo>();
		Map<Integer, List<String>> subIdListMap = new HashMap<Integer, List<String>>();
		loadMdMap(langTypCd, mdMap, subIdListMap);
		ptCacheSvc.setCache(WhConstant.MD, langTypCd, "MAP"+compId,  mdMap);
		ptCacheSvc.setCache(WhConstant.MD, langTypCd, "SUB_ID_LIST_MAP"+compId,  subIdListMap);
		return mdMap;
	}
	
	/** 서브 모듈 목록 맵 리턴(캐쉬) */
	private Map<Integer, List<String>> getMdSubListMap(String langTypCd, String compId) throws SQLException{
		@SuppressWarnings("unchecked")
		Map<Integer, List<String>> subIdListMap = (Map<Integer, List<String>>)
				ptCacheSvc.getCache(WhConstant.MD, langTypCd, "SUB_ID_LIST_MAP"+compId, 10);
		if(subIdListMap!=null) return subIdListMap;
		Map<Integer, WhMdBVo> mdMap = new HashMap<Integer, WhMdBVo>();
		subIdListMap = new HashMap<Integer, List<String>>();
		loadMdMap(langTypCd, mdMap, subIdListMap);
		ptCacheSvc.setCache(WhConstant.MD, langTypCd, "MAP"+compId,  mdMap);
		ptCacheSvc.setCache(WhConstant.MD, langTypCd, "SUB_ID_LIST_MAP"+compId,  subIdListMap);
		return subIdListMap;
	}
	
	/** 모듈맵, 서브 모듈 목록 맵 조회 */
	private void loadMdMap(String langTypCd, 
			Map<Integer, WhMdBVo> mdMap, Map<Integer, List<String>> subIdListMap) throws SQLException{
		
		// 목록 조회
		WhMdBVo whMdBVo = new WhMdBVo();
		whMdBVo.setQueryLang(langTypCd);
		whMdBVo.setOrderBy("MD_PID, SORT_ORDR");
		@SuppressWarnings("unchecked")
		List<WhMdBVo> whMdBVoList = (List<WhMdBVo>)commonDao.queryList(whMdBVo);
		
		WhMdBVo mdVo;
		String mdPid=null, mdId;
		List<String> idList = null;
		int i, size = whMdBVoList==null ? 0 : whMdBVoList.size();
		for(i=0;i<size;i++){
			mdVo = whMdBVoList.get(i);
			mdId = mdVo.getMdId();
			mdPid = mdVo.getMdPid();
			if(!subIdListMap.containsKey(Hash.hashId(mdPid)))
				subIdListMap.put(Hash.hashId(mdPid), new ArrayList<String>());
			idList = subIdListMap.get(Hash.hashId(mdPid));
			idList.add(mdVo.getMdId());
			if(!mdVo.getMdId().equals(mdPid))
				mdMap.put(Hash.hashId(mdId), mdVo);
		}
	}
	
	/** 모듈 사용여부 조회 */
	public boolean isMdUse(String mdId, List<String> mdIdList) throws SQLException{
		WhReqOngdDVo whReqOngdDVo = new WhReqOngdDVo();
		if(mdId!=null) whReqOngdDVo.setMdId(mdId);
		if(mdIdList!=null) whReqOngdDVo.setMdIdList(mdIdList);
		return commonDao.count(whReqOngdDVo)>0;
	}
	
	/** 처리유형 사용여부 조회 */
	public boolean isCatUseList(String catGrpId, String catNo, List<String> catNoList) throws SQLException{
		WhReqOngdDVo whReqOngdDVo = new WhReqOngdDVo();
		if(catGrpId!=null){
			List<WhCatGrpLVo> list = getCatGrpDtlList(null, catGrpId);
			catNoList=new ArrayList<String>();
			for(WhCatGrpLVo storedWhCatGrpLVo : list){
				catNoList.add(storedWhCatGrpLVo.getCatNo());
			}
			if(catNoList.size()==0)
				return false;
		}
		
		if(catNo!=null) whReqOngdDVo.setCatNo(catNo);
		if(catNoList!=null) whReqOngdDVo.setCatNoList(catNoList);
		return commonDao.count(whReqOngdDVo)>0;
	}
	
	/** 처리유형 사용여부 검사 */
	public void chkInUseCat(HttpServletRequest request, String catGrpId, String catNo, List<String> catNoList) throws SQLException, CmException{
		if(isCatUseList(catGrpId, catNo, catNoList)){
			// wh.msg.not.del.typ.inUse=사용중인 처리유형은 삭제 할 수 없습니다.
			throw new CmException("wh.msg.not.del.typ.inUse", request);
		}
	}
	
	/** 결과평가 사용여부 조회 */
	public boolean isEvalUseList(String evalNo) throws SQLException{
		WhReqEvalDVo whReqEvalDVo = new WhReqEvalDVo();
		whReqEvalDVo.setEvalNo(evalNo);
		return commonDao.count(whReqEvalDVo)>0;
	}
	
	/** 결과평가 사용여부 검사 */
	public void chkInUseCat(HttpServletRequest request, String evalNo) throws SQLException, CmException{
		if(isEvalUseList(evalNo)){
			// wh.msg.not.del.eval.inUse=사용중인 결과평가는 삭제 할 수 없습니다.
			throw new CmException("wh.msg.not.del.eval.inUse", request);
		}
	}
	
	/** 모듈 담당자 삭제 */
	public void deleteMdPichList(QueryQueue queryQueue, String mdId, List<String> mdIdList){
		WhMdPichLVo whMdPichLVo = new WhMdPichLVo();
		if(mdId!=null) whMdPichLVo.setMdId(mdId);
		if(mdIdList!=null) whMdPichLVo.setMdIdList(mdIdList);
		queryQueue.delete(whMdPichLVo);
	}
	
	/** 사용하는 모듈ID 조회 */
	public List<String> getMdIdUseList(String compId, List<String> mdIdList) throws SQLException{
		WhReqOngdDVo whReqOngdDVo = new WhReqOngdDVo();
		if(mdIdList!=null) whReqOngdDVo.setMdIdList(mdIdList);
		
		List<String> returnList=new ArrayList<String>();
		
		@SuppressWarnings("unchecked")
		List<WhReqOngdDVo> list=(List<WhReqOngdDVo>)commonDao.queryList(whReqOngdDVo);
		if(list!=null && list.size()>0){
			for(WhReqOngdDVo storedWhReqOngdDVo : list){
				returnList.addAll(getTopIdAllList(returnList, compId, storedWhReqOngdDVo.getMdId(), null, true));
			}
			// 중복제거
			if(returnList.size()>0)
				returnList=new ArrayList<String>(new HashSet<String>(returnList));				
		}
		return returnList;
	}
	
	/** 모듈 삭제 */
	public void deleteMdList(QueryQueue queryQueue, String compId, String mdId) throws SQLException, CmException{
		WhMdBVo whMdBVo = new WhMdBVo();
		whMdBVo.setMdId(mdId);
		whMdBVo=(WhMdBVo)commonDao.queryVo(whMdBVo);
		if(whMdBVo==null) return;
		
		// 하위모듈ID
		List<String> subIdList = getSubIdAllList(null, compId, mdId, null);
		
		if(subIdList!=null && subIdList.size()>0){
			subIdList.add(mdId);
			List<String> updateList=null;
			if(isMdUse(null, subIdList)){
				updateList=getMdIdUseList(compId, subIdList);
				subIdList.removeAll(updateList);
			}
			
			if(updateList!=null && updateList.size()>0){
				// 하위 모듈 일괄 수정
				whMdBVo = new WhMdBVo();
				whMdBVo.setMdIdList(updateList);
				whMdBVo.setUseYn("N");
				queryQueue.update(whMdBVo);
			}
			if(subIdList!=null && subIdList.size()>0){
				
				for(String subId : subIdList){
					whMdBVo=getWhMdBVo(compId, subId, null);
					if(whMdBVo!=null){
						// 리소스 삭제
						WhRescBVo whRescBVo = new WhRescBVo();
						whRescBVo.setRescId(whMdBVo.getRescId());
						queryQueue.delete(whRescBVo);
					}
				}
				
				// 담당자 삭제
				deleteMdPichList(queryQueue, null, subIdList);
				
				// 하위 모듈 일괄 삭제
				whMdBVo = new WhMdBVo();
				whMdBVo.setMdIdList(subIdList);
				queryQueue.delete(whMdBVo);
			}
		}else{
			if(isMdUse(mdId, null)){ // 사용여부만 변경
				whMdBVo.setUseYn("N");
				queryQueue.update(whMdBVo);
			}else{
				// 리소스 삭제
				WhRescBVo whRescBVo = new WhRescBVo();
				whRescBVo.setRescId(whMdBVo.getRescId());
				queryQueue.delete(whRescBVo);
				
				// 담당자 삭제 단건
				deleteMdPichList(queryQueue, mdId, null);
				// 모듈 삭제			
				queryQueue.delete(whMdBVo);
			}
		}
	}
	
	/** 유형그룹 목록 조회 */
	@SuppressWarnings("unchecked")
	public List<WhCatGrpBVo> getCatGrpList(String compId, String langTypCd, String useYn) throws SQLException{
		WhCatGrpBVo whCatGrpBVo = new WhCatGrpBVo();
		whCatGrpBVo.setQueryLang(langTypCd);
		if(compId!=null) whCatGrpBVo.setCompId(compId);
		if(useYn!=null) whCatGrpBVo.setUseYn(useYn);		
		return (List<WhCatGrpBVo>) commonDao.queryList(whCatGrpBVo);
	}
	
	/** 유형그룹 상세 조회 */
	public WhCatGrpBVo getCatGrpDtl(String compId, String langTypCd, String catGrpId, String useYn, String dftYn) throws SQLException{
		WhCatGrpBVo whCatGrpBVo = new WhCatGrpBVo();
		whCatGrpBVo.setQueryLang(langTypCd);
		if(compId!=null) whCatGrpBVo.setCompId(compId);
		if(catGrpId!=null) whCatGrpBVo.setCatGrpId(catGrpId);		
		if(useYn!=null) whCatGrpBVo.setUseYn(useYn);		
		if(dftYn!=null) whCatGrpBVo.setDftYn(dftYn);
		return (WhCatGrpBVo) commonDao.queryVo(whCatGrpBVo);
	}
	
	/** 유형그룹 상세 목록 조회 */
	@SuppressWarnings("unchecked")
	public List<WhCatGrpLVo> getCatGrpDtlList(String langTypCd, String catGrpId) throws SQLException{
		if(catGrpId==null) return null;
		if(langTypCd==null) langTypCd="ko";
		WhCatGrpLVo whCatGrpLVo = new WhCatGrpLVo();
		whCatGrpLVo.setQueryLang(langTypCd);
		whCatGrpLVo.setCatGrpId(catGrpId);
		return (List<WhCatGrpLVo>) commonDao.queryList(whCatGrpLVo);
	}
	
	/** 담당자그룹 상세 조회 */
	public WhPichGrpBVo getWhPichGrpBVo(String compId, String langTypCd, String useYn, String dftYn) throws SQLException{
		WhPichGrpBVo whPichGrpBVo = new WhPichGrpBVo();
		whPichGrpBVo.setQueryLang(langTypCd);
		if(compId!=null) whPichGrpBVo.setCompId(compId);
		if(useYn!=null) whPichGrpBVo.setUseYn(useYn);		
		if(dftYn!=null) whPichGrpBVo.setDftYn(dftYn);
		return (WhPichGrpBVo) commonDao.queryVo(whPichGrpBVo);
	}
	
	/** 담당자그룹 목록 조회 */
	@SuppressWarnings("unchecked")
	public List<WhPichGrpBVo> getPichGrpList(String compId, String langTypCd, String useYn) throws SQLException{
		WhPichGrpBVo whPichGrpBVo = new WhPichGrpBVo();
		whPichGrpBVo.setQueryLang(langTypCd);
		if(compId!=null) whPichGrpBVo.setCompId(compId);
		if(useYn!=null) whPichGrpBVo.setUseYn(useYn);		
		return (List<WhPichGrpBVo>) commonDao.queryList(whPichGrpBVo);
	}
	
	/** 담당자그룹 상세 조회 */
	public WhPichGrpBVo getPichGrpDtl(String compId, String langTypCd, String pichGrpId) throws SQLException{
		WhPichGrpBVo whPichGrpBVo = new WhPichGrpBVo();
		whPichGrpBVo.setQueryLang(langTypCd);
		if(compId!=null) whPichGrpBVo.setCompId(compId);
		whPichGrpBVo.setPichGrpId(pichGrpId);
		return (WhPichGrpBVo) commonDao.queryVo(whPichGrpBVo);
	}
	
	/** 담당자그룹 상세목록 조회 */
	@SuppressWarnings("unchecked")
	public List<WhPichGrpLVo> getPichGrpDtlList(String langTypCd, String pichGrpId) throws SQLException{
		WhPichGrpLVo whPichGrpLVo = new WhPichGrpLVo();
		whPichGrpLVo.setQueryLang(langTypCd);
		whPichGrpLVo.setPichGrpId(pichGrpId);
		return (List<WhPichGrpLVo>) commonDao.queryList(whPichGrpLVo);
	}
	
	/** 사용자 정보 조회(회사ID,조직ID) */
	public OrUserBVo getOrUserBVo(String userUid, String langTypCd) throws SQLException{
		// 사용자기본(OR_USER_B) 테이블
		OrUserBVo orUserBVo = new OrUserBVo();
		if(userUid!=null) orUserBVo.setUserUid(userUid);
		orUserBVo.setQueryLang(langTypCd);
		orUserBVo = (OrUserBVo)commonDao.queryVo(orUserBVo);
		return orUserBVo;
	}
	
	/** 조직정보 조회 */
	public OrOrgBVo getOrgInfo(String compId , String langTypCd , String orgId , String orgPid) throws SQLException{
		OrOrgBVo orOrgBVo = new OrOrgBVo();
		orOrgBVo.setCompId(compId);
		orOrgBVo.setUseYn("Y");
		if(orgPid != null && !"".equals(orgPid)) orOrgBVo.setOrgPid(orgPid);
		if(orgId != null && !"".equals(orgId)) orOrgBVo.setOrgId(orgId);
		if(orgPid == null && orgId == null ) return null;
		orOrgBVo.setQueryLang(langTypCd);
		return (OrOrgBVo)commonDao.queryVo(orOrgBVo);
	}
	
	/** 환경 설정 로드 */
	public Map<String, String> getEnvConfigMap(ModelMap model, String compId) throws SQLException {
		Map<String, String> envConfigMap = ptSysSvc.getSysSetupMap(WhConstant.SYS_CONFIG+compId, true);
		if(envConfigMap == null || envConfigMap.isEmpty()){
			
			envConfigMap = new HashMap<String, String>();
			envConfigMap.put("dtlRecvYn", "Y"); // 요청자 상세정보 - 접수사항
			envConfigMap.put("dtlHdlYn", "Y"); // 요청자 상세정보 - 처리사항
			envConfigMap.put("reqEditorYn", "Y"); // 에디터 사용 - 요청사항
			envConfigMap.put("cmplEditorYn", "Y"); // 에디터 사용 - 완료사항
			envConfigMap.put("docNoOpt1", "YYYY"); // 문서번호 조건1
			envConfigMap.put("docNoOpt2", "notUse"); // 문서번호 조건2
			envConfigMap.put("docNoOpt3", "notUse"); // 문서번호 조건3
			envConfigMap.put("docNoSeqLen", "4"); // 문서 ID 자리수
			envConfigMap.put("docNoFxLen", "Y"); // 문서 ID 고정여부
			envConfigMap.put("docNoDftYear", "Y"); // 문서채번(연도) 기준
			envConfigMap.put("docNoDftOrg", "Y"); // 문서채번(조직) 기준
			envConfigMap.put("recoMt", "1"); // 회계 기준일[월]
			envConfigMap.put("recoDt", "1"); // 회계 기준일[일]
			
			envConfigMap.put("cmplHdlDisp", "popup"); // 완료처리 화면 - 팝업
			
			envConfigMap.put("dtlRecvYn", "Y"); // 상세정보(요청자) - 접수사항
			envConfigMap.put("dtlHdlYn", "Y"); // 상세정보(요청자) - 처리사항
			
			envConfigMap.put("reqEditorYn", "Y"); // 에디터 사용 - 요청사항
			envConfigMap.put("cmplEditorYn", "Y"); // 에디터 사용 - 완료사항
			envConfigMap.put("mailSendTgt", "none"); // 메일발송 대상
			
			envConfigMap.put("hdlLstOpt", "pich"); // 목록조회 기준 - 처리사항[담당자]
			
			envConfigMap.put("dueDt", "14"); // 처리예정일 - 14일 후
			envConfigMap.put("resEvalUseYn", "N"); // 결과평가 사용여부 'N'
			envConfigMap.put("fileYn", "Y"); // 첨부파일 사용여부 'Y'
			
			ptCacheSvc.setCache(CacheConfig.SYS_SETUP, "ko", WhConstant.SYS_CONFIG+compId, envConfigMap);
		}
			
		if(model!=null) model.put("envConfigMap", envConfigMap);
		
		return envConfigMap;
	}
	
	/** 조회조건 추가 [회사 및 계열사]*/
	public void setCompAffiliateIdList(String compId, String langTypCd, CommonVo commonVo, boolean isAdmin) throws SQLException{
		// 시스템 정책 조회
		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
		if(sysPlocMap.get("helpdeskAllCompEnable")==null || !"Y".equals(sysPlocMap.get("helpdeskAllCompEnable"))){ // 전사여부가 'N' 이면 내 회사만 조회
			VoUtil.setValue(commonVo, "compId", compId);
			return;
		}
		if(sysPlocMap.get("affiliatesEnable")==null || !"Y".equals(sysPlocMap.get("affiliatesEnable"))){ // 전사여부가 'Y' 이면서 계열사여부가 'N' 이면 전체 조회
			VoUtil.setValue(commonVo, "compId", null);
			return;
		}
		// 전사여부 'Y' and 계열사 여부 'Y' 일 경우 계열사 데이터 조회(계열사 없으면 내 회사)
		PtCompBVo ptCompBVo = ptCmSvc.getPtCompBVo(compId, langTypCd);
		if(ptCompBVo!=null && ptCompBVo.getAffiliateIds()!=null){
			List<String> affiliateIds=ptCompBVo.getAffiliateIds();
			affiliateIds.add(compId);
			// HashSet 으로 중복ID 제거
			Set<String> hs = new HashSet<String>(affiliateIds);
			affiliateIds = new ArrayList<String>(hs);
			VoUtil.setValue(commonVo, "compId", null);
			VoUtil.setValue(commonVo, "compIdList", affiliateIds);
		}else{
			VoUtil.setValue(commonVo, "compId", compId);
		}
	}
	
	/** 회사 및 계열사 조회 */
	public List<String> getCompAffiliateIdList(String compId, String langTypCd, boolean isAdmin) throws SQLException{
		// 시스템 정책 조회
		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
		if(sysPlocMap.get("helpdeskAllCompEnable")==null || !"Y".equals(sysPlocMap.get("helpdeskAllCompEnable"))){ // 전사여부가 'N' 이면 내 회사만 조회
			return null;
		}
		List<String> compIdList = null;
		
		List<PtCompBVo> allCompList=ptCmSvc.getFilteredCompList(null, "Y", langTypCd);
		
		if(sysPlocMap.get("affiliatesEnable")==null || !"Y".equals(sysPlocMap.get("affiliatesEnable"))){ // 전사여부가 'Y' 이면서 계열사여부가 'N' 이면 전체 조회
			compIdList=new ArrayList<String>();
			for(PtCompBVo storedPtCompBVo : allCompList){
				if(storedPtCompBVo.getUseYn()==null || !"Y".equals(storedPtCompBVo.getUseYn())) continue;
				compIdList.add(storedPtCompBVo.getCompId());
			}
		}else{
			PtCompBVo ptCompBVo = ptCmSvc.getPtCompBVo(compId, langTypCd);
			if(ptCompBVo!=null && ptCompBVo.getAffiliateIds()!=null){
				compIdList=new ArrayList<String>();
				List<String> affiliateIds=ptCompBVo.getAffiliateIds();
				affiliateIds.add(compId);
				// HashSet 으로 중복ID 제거
				Set<String> hs = new HashSet<String>(affiliateIds);
				affiliateIds = new ArrayList<String>(hs);
				for(PtCompBVo storedPtCompBVo : allCompList){
					if(!affiliateIds.contains(storedPtCompBVo.getCompId())) continue;
					compIdList.add(storedPtCompBVo.getCompId());
				}
			}
		}
		return compIdList;
	}
	
	/** 회사 목록 추가 [회사 및 계열사]*/
	public void setCompAffiliateVoList(ModelMap model, String compId, String langTypCd) throws SQLException{
		// 시스템 정책 조회
		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
		if(sysPlocMap.get("helpdeskAllCompEnable")==null || !"Y".equals(sysPlocMap.get("helpdeskAllCompEnable"))) // 전사여부가 'N'이면 return
			return;
		
		List<PtCompBVo> allCompList=ptCmSvc.getFilteredCompList(null, "Y", langTypCd);
		
		if(sysPlocMap.get("affiliatesEnable")==null || !"Y".equals(sysPlocMap.get("affiliatesEnable"))){ // 전사여부가 'Y' 이면서 계열사여부가 'N' 이면 전사 조회
			model.put("ptCompBVoList", allCompList);
		}else{ // 전사여부 'Y' and 계열사 여부 'Y' 일 경우 계열사 데이터 조회
			PtCompBVo ptCompBVo = ptCmSvc.getPtCompBVo(compId, langTypCd);
			if(ptCompBVo!=null && ptCompBVo.getAffiliateIds()!=null){
				List<String> affiliateIds=ptCompBVo.getAffiliateIds();
				affiliateIds.add(compId);
				// HashSet 으로 중복ID 제거
				Set<String> hs = new HashSet<String>(affiliateIds);
				affiliateIds = new ArrayList<String>(hs);
				
				List<PtCompBVo> ptCompBVoList=new ArrayList<PtCompBVo>();
				
				for(PtCompBVo storedPtCompBVo : allCompList){
					if(!affiliateIds.contains(storedPtCompBVo.getCompId())) continue;
					ptCompBVoList.add(storedPtCompBVo);
				}
				
				model.put("ptCompBVoList", ptCompBVoList);
			}
		}
	}
	
}
