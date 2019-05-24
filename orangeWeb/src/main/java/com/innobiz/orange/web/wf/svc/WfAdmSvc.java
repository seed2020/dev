package com.innobiz.orange.web.wf.svc;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.utils.ArrayUtil;
import com.innobiz.orange.web.cm.utils.Hash;
import com.innobiz.orange.web.cm.utils.IdUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.ParamUtil;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.CommonVo;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtCacheSvc;
import com.innobiz.orange.web.pt.svc.PtCmSvc;
import com.innobiz.orange.web.pt.svc.PtSysSvc;
import com.innobiz.orange.web.pt.vo.PtCompBVo;
import com.innobiz.orange.web.wf.utils.WfConstant;
import com.innobiz.orange.web.wf.vo.WfFormBVo;
import com.innobiz.orange.web.wf.vo.WfFormColmLVo;
import com.innobiz.orange.web.wf.vo.WfFormGrpBVo;
import com.innobiz.orange.web.wf.vo.WfFormLstDVo;
import com.innobiz.orange.web.wf.vo.WfFormRegDVo;
import com.innobiz.orange.web.wf.vo.WfRescBVo;

@Service
public class WfAdmSvc {
	/** Logger */
	//private static final Logger LOGGER = Logger.getLogger(WfAdmSvc.class);
	
	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;
	
	/** 메세지 */
	@Autowired
    private MessageProperties messageProperties;
	
	/** 포탈 공통 서비스 */
	@Autowired
	private PtCmSvc ptCmSvc;
	
	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
	/** 캐쉬 서비스 */
	@Autowired
	private PtCacheSvc ptCacheSvc;
	
	/** 공통 서비스 */
	@Autowired
	private WfCmSvc wfCmSvc;
	
	/** 리소스 조회 저장용 서비스 */
	@Resource(name = "wfRescSvc")
	private WfRescSvc wfRescSvc;
	
	/** 그룹 목록 트리 조회 */
	@SuppressWarnings("unchecked")
	public List<WfFormGrpBVo> getGrpTreeList(String compId, String useYn, String langTypCd) throws SQLException{
		WfFormGrpBVo wfFormGrpBVo = new WfFormGrpBVo();
		wfFormGrpBVo.setQueryLang(langTypCd);
		wfFormGrpBVo.setCompId(compId);
		if(useYn!=null) wfFormGrpBVo.setUseYn(useYn);
		// 회사 ID 조회조건 추가[계열사 설정 확인]
		//if(compId!=null) setCompAffiliateIdList(compId, langTypCd, wfFormGrpBVo);
		if(useYn!=null) wfFormGrpBVo.setUseYn(useYn);
		wfFormGrpBVo.setOrderBy("GRP_PID, SORT_ORDR");
		return (List<WfFormGrpBVo>)commonDao.queryList(wfFormGrpBVo);
	}
	
	/** 그룹 목록 + 양식 목록 트리 조회 */
	public List<WfFormGrpBVo> getGrpAndFormTreeList(String compId, String langTypCd, String formTyp, String mdTypCd, String allCompYn) throws SQLException{
		
		// 트리 목록 조회
		List<WfFormGrpBVo> wfFormGrpBVoList = getGrpTreeList(compId, "Y", langTypCd);
		
		// 테이블관리 기본(WF_FORM_B) 테이블 - BIND
		WfFormBVo wfFormBVo = new WfFormBVo();
		wfFormBVo.setQueryLang(langTypCd);
		wfFormBVo.setCompId(compId);
		wfFormBVo.setUseYn("Y"); // 사용여부
		if(formTyp!=null )wfFormBVo.setFormTyp(formTyp);
		if(mdTypCd!=null )wfFormBVo.setMdTypCd(mdTypCd);
		if(allCompYn!=null )wfFormBVo.setAllCompYn(allCompYn);
		
		@SuppressWarnings("unchecked")
		List<WfFormBVo> wfFormBVoList = (List<WfFormBVo>) commonDao.queryList(wfFormBVo);
		if(wfFormBVoList!=null){
			if(wfFormGrpBVoList==null) wfFormGrpBVoList=new ArrayList<WfFormGrpBVo>();
			WfFormGrpBVo wfFormGrpBVo;
			for(WfFormBVo storedWfFormBVo : wfFormBVoList){
				if(storedWfFormBVo.getGenId()==null) continue; // 배포되지 않은 양식은 제외
				wfFormGrpBVo = new WfFormGrpBVo();
				wfFormGrpBVo.setGrpId(storedWfFormBVo.getFormNo());
				wfFormGrpBVo.setGrpNm(storedWfFormBVo.getFormNm());
				wfFormGrpBVo.setGrpPid(storedWfFormBVo.getGrpId());
				wfFormGrpBVo.setGrpTyp("A");
				wfFormGrpBVoList.add(wfFormGrpBVo);
			}
		}
		
		return wfFormGrpBVoList;
	}
	
	
	/** 그룹 목록 조회 */
	@SuppressWarnings("unchecked")
	public List<WfFormGrpBVo> getGrpList(WfFormGrpBVo wfFormGrpBVo, String compId, String langTypCd, String grpPid, String useYn) throws SQLException{
		boolean isNotParam=wfFormGrpBVo==null;
		if(isNotParam)
			wfFormGrpBVo = new WfFormGrpBVo();
		// 회사 ID 조회조건 추가[계열사 설정 확인]
		wfFormGrpBVo.setCompId(compId);
		//if(compId!=null) setCompAffiliateIdList(compId, langTypCd, wfFormGrpBVo);
		wfFormGrpBVo.setQueryLang(langTypCd);
		if(isNotParam && grpPid!=null)
			wfFormGrpBVo.setGrpPid(grpPid);
		if(isNotParam && useYn!=null) wfFormGrpBVo.setUseYn(useYn);
		wfFormGrpBVo.setOrderBy("SORT_ORDR ASC");
		
		return (List<WfFormGrpBVo>)commonDao.queryList(wfFormGrpBVo);
	}
	
	/** 그룹 정보 조회 */
	public WfFormGrpBVo getWfFormGrpBVo(String compId, String grpId, String langTypCd) throws SQLException{
		//List<String> compIdList=getCompAffiliateIdList(compId, langTypCd, false);
		//if(compIdList!=null && compIdList.size()>0)
		//	return getWfFormGrpBVo(compIdList, grpId, langTypCd);
		Map<Integer, WfFormGrpBVo> grpMap = getGrpMap(langTypCd, compId);
		int hashId = Hash.hashId(grpId);
		return grpMap.get(hashId);
	}
	
	/** 그룹 정보 조회 - 계열사 및 전사 포함*/
	public WfFormGrpBVo getWfFormGrpBVo(List<String> compIdList, String grpId, String langTypCd) throws SQLException{
		Map<Integer, WfFormGrpBVo> grpMap = null;
		if(compIdList.size()==1) 
			grpMap = getGrpMap(langTypCd, compIdList.get(0));
		else{
			grpMap=new HashMap<Integer, WfFormGrpBVo>();
			Map<Integer, WfFormGrpBVo> tmpMap=null;
			for(String id : compIdList){
				tmpMap = getGrpMap(langTypCd, id);
				if(tmpMap==null) continue;
				grpMap.putAll(tmpMap);
			}
		}
		int hashId = Hash.hashId(grpId);
		return grpMap.get(hashId);
	}
	
	/** 그룹 정보 조회 - DB */
	public WfFormGrpBVo getWfFormGrpBVo(String grpId, String langTypCd) throws SQLException{
		WfFormGrpBVo wfFormGrpBVo = new WfFormGrpBVo();
		wfFormGrpBVo.setQueryLang(langTypCd);
		wfFormGrpBVo.setGrpId(grpId);
		return (WfFormGrpBVo)commonDao.queryVo(wfFormGrpBVo);
	}
	
	/** 하위 그룹 정보 조회 */
	public List<String> getSubIdList(String compId, String grpId, String langTypCd) throws SQLException{
		Map<Integer, List<String>> grpMap = getGrpSubListMap(langTypCd, compId);
		int hashId = Hash.hashId(grpId);
		return grpMap.get(hashId);
	}
	
	/** 하위 그룹ID 전체목록 조회 */
	public List<String> getSubIdAllList(List<String> grpIdList, String compId, String grpId, String langTypCd) throws SQLException{
		if(langTypCd==null) langTypCd="ko";
		if(grpIdList==null) grpIdList=new ArrayList<String>();		
		List<String> returnList=getSubIdList(compId, grpId, langTypCd);
		if(returnList!=null && returnList.size()>0){
			grpIdList.addAll(returnList);
			for(String subId : returnList){
				return getSubIdAllList(grpIdList, compId, subId, langTypCd);
			}
		}
		return grpIdList;
	}
	
	/** 상위 그룹ID 전체목록 조회 */
	public void setTopIdList(String grpId, List<String> returnList, Map<Integer, WfFormGrpBVo> grpMap, boolean first) throws SQLException{
		int hashId = Hash.hashId(grpId);
		WfFormGrpBVo wfFormGrpBVo = grpMap.get(hashId);
		if(wfFormGrpBVo!=null){
			if(first) returnList.add(grpId);
			setTopIdList(wfFormGrpBVo.getGrpPid(), returnList, grpMap, true);
		}
	}
	
	/** 상위 그룹ID 전체목록 조회 */
	public List<String> getTopIdAllList(List<String> returnList, String compId, String grpId, String langTypCd, boolean first) throws SQLException{
		if(langTypCd==null) langTypCd="ko";
		if(returnList==null) returnList=new ArrayList<String>();
		
		Map<Integer, WfFormGrpBVo> grpMap = getGrpMap(langTypCd, compId);
		setTopIdList(grpId, returnList, grpMap, first);
		
		return returnList;
	}
	
	
	/** 최상위 그룹 정보 조회 */
	public WfFormGrpBVo getTopTreeVo(String compId, String grpId, String langTypCd) throws SQLException{
		Map<Integer, WfFormGrpBVo> grpMap = getGrpMap(langTypCd, compId);
		WfFormGrpBVo wfFormGrpBVo = getTopGrpVo(grpId, new WfFormGrpBVo(), grpMap);
		return wfFormGrpBVo;
	}
	
	/** 최상위 그룹 정보를 리턴 */
	private WfFormGrpBVo getTopGrpVo(String grpPid, WfFormGrpBVo wfFormGrpBVo, 
			Map<Integer, WfFormGrpBVo> grpMap){
		int hashId = Hash.hashId(grpPid);
		if(grpMap.get(hashId)!=null){
			wfFormGrpBVo = grpMap.get(hashId); 
			return getTopGrpVo(wfFormGrpBVo.getGrpPid(), wfFormGrpBVo, grpMap);
		}
		return wfFormGrpBVo;
	}
	
	/** 상위 그룹 트리 조회 */
	public List<WfFormGrpBVo> getTopTreeList(String compId, String grpId, String langTypCd, boolean first) throws SQLException{
		Map<Integer, WfFormGrpBVo> grpMap = getGrpMap(langTypCd, compId);
		//Map<Integer, List<String>> mdSubListMap = getGrpSubListMap(langTypCd, compId);
		List<WfFormGrpBVo> returnList = new ArrayList<WfFormGrpBVo>();
		setTopTreeList(grpId, returnList, grpMap, first);		
		return returnList;
	}
	
	/** 상위 그룹 정보를 returnList 에 담음 */
	private void setTopTreeList(String grpId, List<WfFormGrpBVo> returnList, 
			Map<Integer, WfFormGrpBVo> grpMap, boolean first){
		int hashId = Hash.hashId(grpId);
		WfFormGrpBVo wfFormGrpBVo = grpMap.get(hashId);
		if(wfFormGrpBVo!=null){
			if(first) returnList.add(wfFormGrpBVo);
			setTopTreeList(wfFormGrpBVo.getGrpPid(), returnList, grpMap, true);
		}
	}
	
	/** 하위 그룹 트리 조회 */
	public List<WfFormGrpBVo> getDownTreeList(String compId, String grpId, String langTypCd, boolean first, String useYn) throws SQLException{
		Map<Integer, WfFormGrpBVo> grpMap = getGrpMap(langTypCd, compId);
		Map<Integer, List<String>> mdSubListMap = getGrpSubListMap(langTypCd, compId);
		List<WfFormGrpBVo> returnList = new ArrayList<WfFormGrpBVo>();
		setDownTreeList(grpId, returnList, mdSubListMap, grpMap, first, useYn);
		return returnList;
	}
	
	/** 하위 그룹 정보를 returnList 에 담음 */
	private void setDownTreeList(String grpId, List<WfFormGrpBVo> returnList, 
			Map<Integer, List<String>> mdSubListMap, Map<Integer, WfFormGrpBVo> grpMap, boolean first, String useYn){
		int hashId = Hash.hashId(grpId);
		WfFormGrpBVo wfFormGrpBVo = grpMap.get(hashId);
		if(wfFormGrpBVo!=null){
			if(first && ((useYn == null || (useYn != null && useYn.equals(wfFormGrpBVo.getUseYn()))))) returnList.add(wfFormGrpBVo);
			List<String> mdSubList = mdSubListMap.get(hashId);
			if(mdSubList!=null){
				for(String subGrpId : mdSubList){
					setDownTreeList(subGrpId, returnList, mdSubListMap, grpMap, true, useYn);
				}
			}
		}
	}
	
	/** 그룹 맵 리턴(캐쉬) */
	private Map<Integer, WfFormGrpBVo> getGrpMap(String langTypCd, String compId) throws SQLException{
		@SuppressWarnings("unchecked")
		Map<Integer, WfFormGrpBVo> grpMap = (Map<Integer, WfFormGrpBVo>)
				ptCacheSvc.getCache(WfConstant.GRP, langTypCd, "MAP"+compId, 10);
		if(grpMap!=null) return grpMap;
		
		grpMap = new HashMap<Integer, WfFormGrpBVo>();
		Map<Integer, List<String>> subIdListMap = new HashMap<Integer, List<String>>();
		loadGrpMap(langTypCd, grpMap, subIdListMap);
		ptCacheSvc.setCache(WfConstant.GRP, langTypCd, "MAP"+compId,  grpMap);
		ptCacheSvc.setCache(WfConstant.GRP, langTypCd, "SUB_ID_LIST_MAP"+compId,  subIdListMap);
		return grpMap;
	}
	
	/** 서브 그룹 목록 맵 리턴(캐쉬) */
	private Map<Integer, List<String>> getGrpSubListMap(String langTypCd, String compId) throws SQLException{
		@SuppressWarnings("unchecked")
		Map<Integer, List<String>> subIdListMap = (Map<Integer, List<String>>)
				ptCacheSvc.getCache(WfConstant.GRP, langTypCd, "SUB_ID_LIST_MAP"+compId, 10);
		if(subIdListMap!=null) return subIdListMap;
		Map<Integer, WfFormGrpBVo> grpMap = new HashMap<Integer, WfFormGrpBVo>();
		subIdListMap = new HashMap<Integer, List<String>>();
		loadGrpMap(langTypCd, grpMap, subIdListMap);
		ptCacheSvc.setCache(WfConstant.GRP, langTypCd, "MAP"+compId,  grpMap);
		ptCacheSvc.setCache(WfConstant.GRP, langTypCd, "SUB_ID_LIST_MAP"+compId,  subIdListMap);
		return subIdListMap;
	}
	
	/** 그룹맵, 서브 그룹 목록 맵 조회 */
	private void loadGrpMap(String langTypCd, 
			Map<Integer, WfFormGrpBVo> grpMap, Map<Integer, List<String>> subIdListMap) throws SQLException{
		
		// 목록 조회
		WfFormGrpBVo wfFormGrpBVo = new WfFormGrpBVo();
		wfFormGrpBVo.setQueryLang(langTypCd);
		wfFormGrpBVo.setOrderBy("GRP_PID, SORT_ORDR");
		@SuppressWarnings("unchecked")
		List<WfFormGrpBVo> wfFormGrpBVoList = (List<WfFormGrpBVo>)commonDao.queryList(wfFormGrpBVo);
		
		WfFormGrpBVo mdVo;
		String grpPid=null, grpId;
		List<String> idList = null;
		int i, size = wfFormGrpBVoList==null ? 0 : wfFormGrpBVoList.size();
		for(i=0;i<size;i++){
			mdVo = wfFormGrpBVoList.get(i);
			grpId = mdVo.getGrpId();
			grpPid = mdVo.getGrpPid();
			if(!subIdListMap.containsKey(Hash.hashId(grpPid)))
				subIdListMap.put(Hash.hashId(grpPid), new ArrayList<String>());
			idList = subIdListMap.get(Hash.hashId(grpPid));
			idList.add(mdVo.getGrpId());
			if(!mdVo.getGrpId().equals(grpPid))
				grpMap.put(Hash.hashId(grpId), mdVo);
		}
	}
	
	/** 그룹 사용여부 조회 */
	public boolean isGrpUse(String grpId, List<String> grpIdList) throws SQLException{
		WfFormBVo wfFormBVo = new WfFormBVo();
		if(grpId!=null) wfFormBVo.setGrpId(grpId);
		if(grpIdList!=null) wfFormBVo.setGrpIdList(grpIdList);
		return commonDao.count(wfFormBVo)>0;
	}
	
	/** 사용하는 그룹ID 조회 */
	public List<String> getGrpIdUseList(String compId, List<String> grpIdList) throws SQLException{
		WfFormBVo wfFormBVo = new WfFormBVo();
		if(grpIdList!=null) wfFormBVo.setGrpIdList(grpIdList);
		
		List<String> returnList=new ArrayList<String>();
		
		@SuppressWarnings("unchecked")
		List<WfFormBVo> list=(List<WfFormBVo>)commonDao.queryList(wfFormBVo);
		if(list!=null && list.size()>0){
			for(WfFormBVo storedWfFormBVo : list){
				returnList.addAll(getTopIdAllList(returnList, compId, storedWfFormBVo.getGrpId(), null, true));
			}
			// 중복제거
			if(returnList.size()>0)
				returnList=new ArrayList<String>(new HashSet<String>(returnList));				
		}
		return returnList;
	}
	
	/** 그룹 삭제 */
	public void deleteGrpList(QueryQueue queryQueue, String compId, String grpId) throws SQLException, CmException{
		WfFormGrpBVo wfFormGrpBVo = new WfFormGrpBVo();
		wfFormGrpBVo.setGrpId(grpId);
		wfFormGrpBVo=(WfFormGrpBVo)commonDao.queryVo(wfFormGrpBVo);
		if(wfFormGrpBVo==null) return;
		
		// 하위그룹ID
		List<String> subIdList = getSubIdAllList(null, compId, grpId, null);
		
		if(subIdList!=null && subIdList.size()>0){
			subIdList.add(grpId);
			List<String> updateList=null;
			if(isGrpUse(null, subIdList)){
				updateList=getGrpIdUseList(compId, subIdList);
				subIdList.removeAll(updateList);
			}
			
			if(updateList!=null && updateList.size()>0){
				// 하위 그룹 일괄 수정
				wfFormGrpBVo = new WfFormGrpBVo();
				wfFormGrpBVo.setGrpIdList(updateList);
				wfFormGrpBVo.setUseYn("N");
				queryQueue.update(wfFormGrpBVo);
			}
			if(subIdList!=null && subIdList.size()>0){
				
				for(String subId : subIdList){
					wfFormGrpBVo=getWfFormGrpBVo(compId, subId, null);
					if(wfFormGrpBVo!=null){
						// 리소스 삭제
						WfRescBVo wfRescBVo = new WfRescBVo();
						wfRescBVo.setRescId(wfFormGrpBVo.getRescId());
						queryQueue.delete(wfRescBVo);
					}
				}
				
				// 하위 그룹 일괄 삭제
				wfFormGrpBVo = new WfFormGrpBVo();
				wfFormGrpBVo.setGrpIdList(subIdList);
				queryQueue.delete(wfFormGrpBVo);
			}
		}else{
			if(isGrpUse(grpId, null)){ // 사용여부만 변경
				wfFormGrpBVo.setUseYn("N");
				queryQueue.update(wfFormGrpBVo);
			}else{
				// 리소스 삭제
				WfRescBVo wfRescBVo = new WfRescBVo();
				wfRescBVo.setRescId(wfFormGrpBVo.getRescId());
				queryQueue.delete(wfRescBVo);
				
				// 그룹 삭제			
				queryQueue.delete(wfFormGrpBVo);
			}
		}
	}
	
	/** 양식 기본 저장 */
	public String saveForm(HttpServletRequest request, QueryQueue queryQueue, WfFormBVo wfFormBVo, WfRescBVo whRescBVo, String formNo) throws SQLException, CmException{
		
		// 리소스 VO 가 없으면 request 에서 매핑 처리
		if(whRescBVo==null){
			whRescBVo = wfRescSvc.collectWfRescBVo(request, "", queryQueue);
			
			if (whRescBVo == null) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
		}
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		if(wfFormBVo==null){
			wfFormBVo = new WfFormBVo();
			VoUtil.bind(request, wfFormBVo);
		}
		
		wfFormBVo.setCompId(userVo.getCompId());
		// 리소스 조회 후 리소스ID와 리소스명 세팅
		wfFormBVo.setRescId(whRescBVo.getRescId());
		wfFormBVo.setFormNm(whRescBVo.getRescVa());
		
		if (formNo == null || formNo.isEmpty()) {
			formNo=wfCmSvc.createNo("WF_FORM_B").toString();
			wfFormBVo.setFormNo(formNo);
			wfFormBVo.setFormId(getTblId(formNo));
			wfFormBVo.setRegrUid(userVo.getUserUid());
			wfFormBVo.setRegDt("sysdate");
			wfFormBVo.setStatCd("R"); // 상태코드[등록]
			queryQueue.insert(wfFormBVo);
		}else{
			wfFormBVo.setModrUid(userVo.getUserUid());
			wfFormBVo.setModDt("sysdate");
			queryQueue.update(wfFormBVo);
		}
		return formNo;
	}
	
	/** 조회조건 추가 [회사 및 계열사]*/
	public void setCompAffiliateIdList(String compId, String langTypCd, CommonVo commonVo, boolean isAllComp) throws SQLException{
		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
		if(!isAllComp){ // 전사여부가 'N' 이면 내 회사 조회
			VoUtil.setValue(commonVo, "compId", compId);
			return;
		}
		
		if(sysPlocMap.get("affiliatesEnable")==null || !"Y".equals(sysPlocMap.get("affiliatesEnable"))){ // 계열사여부가 'N' 이면 전체 회사 조회
			VoUtil.setValue(commonVo, "compId", null);
			return;
		}
		// 전사여부가 'Y' 면서 계열사 여부 'Y' 일 경우 계열사 데이터 조회(계열사 없으면 내 회사)
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
	
	/** 회사 및 계열사 조회 *//*
	public List<String> getCompAffiliateIdList(String compId, String langTypCd, boolean isAllComp) throws SQLException{
		if(!isAllComp){ // 전사여부가 'N' 이면 내 회사만 조회
			return null;
		}
		
		// 시스템 정책 조회
		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
		List<String> compIdList = null;
		
		if(sysPlocMap.get("affiliatesEnable")==null || !"Y".equals(sysPlocMap.get("affiliatesEnable"))){ // 계열사여부가 'N' 이면 내 회사만 조회
			return compIdList;
		}
		
		List<PtCompBVo> allCompList=ptCmSvc.getFilteredCompList(null, "Y", langTypCd);
		
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
		return compIdList;
	}*/
	
	/** 회사 목록 추가 [회사 및 계열사]*/
	public List<PtCompBVo> setCompAffiliateVoList(ModelMap model, String compId, String langTypCd, boolean isAllComp) throws SQLException{
		if(!isAllComp){ // 전사여부가 'N' 이면 리턴
			return null;
		}
		
		// 시스템 정책 조회
		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
			
		List<PtCompBVo> allCompList=ptCmSvc.getFilteredCompList(null, "Y", langTypCd);
		
		if(sysPlocMap.get("affiliatesEnable")==null || !"Y".equals(sysPlocMap.get("affiliatesEnable"))){ // 전사여부가 'Y' 면서 계열사여부가 'N' 이면 전체회사 리턴
			return allCompList;
		}
		
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
			return ptCompBVoList;
		}
		return null;
	}
	
	/** 양식 데이터 수정 */
	public void updateWfFormBVo(String formNo, String genId, String deployDt, String useYn, String statCd) throws SQLException{
		// 테이블 생성됨 저장
		WfFormBVo wfFormBVo = new WfFormBVo();
		wfFormBVo.setFormNo(formNo);
		if(genId!=null) wfFormBVo.setGenId(genId);
		if(deployDt!=null) wfFormBVo.setDeployDt(deployDt);
		if(useYn!=null) wfFormBVo.setUseYn(useYn);
		if(statCd!=null) wfFormBVo.setStatCd(statCd);
		
		commonDao.update(wfFormBVo);
	}
	
	/** 양식 컬럼 목록 조회*/
	public List<WfFormColmLVo> getColmVoList(String genId, String formNo, String lstYn, boolean isHst) throws SQLException{
		WfFormColmLVo wfFormColmLVo= new WfFormColmLVo();
		if(genId!=null) wfFormColmLVo.setGenId(genId);
		if(formNo!=null) wfFormColmLVo.setFormNo(formNo);
		wfFormColmLVo.setHst(isHst);
		if(lstYn!=null) wfFormColmLVo.setLstYn(lstYn);
		
		List<WfFormColmLVo> returnVoList = new ArrayList<WfFormColmLVo>();
		
		@SuppressWarnings("unchecked")
		List<WfFormColmLVo> wfFormColmLVoList=(List<WfFormColmLVo>)commonDao.queryList(wfFormColmLVo);
		if(wfFormColmLVoList!=null && wfFormColmLVoList.size()>0){
			for(WfFormColmLVo storedWfFormColmLVo : wfFormColmLVoList){
				returnVoList.add(storedWfFormColmLVo);
			}
		}
		
		return returnVoList;
	}
	
	/** 적용할 양식 컬럼 목록 조회*/
	public List<WfFormColmLVo> getColmVoList(String formNo, String lstYn) throws SQLException{
		return getColmVoList(null, formNo, lstYn, false);
	}
	
	/** 현재 사용하고 있는 양식 컬럼 목록 조회*/
	public List<WfFormColmLVo> getCurrColmVoList(String genId, String formNo, String lstYn) throws SQLException{
		return getColmVoList(genId, formNo, lstYn, true);
	}
	
	/** 배포된 테이블의 컬럼ID 목록 리턴 */
	public List<String> getCurrColmIdList(List<WfFormColmLVo> wfFormColmLVoList){
		List<String> returnList=new ArrayList<String>();
		for(WfFormColmLVo storedWfFormColmLVo : wfFormColmLVoList){
			returnList.add(storedWfFormColmLVo.getColmId());
		}
		return returnList;
	}
	
	/** 배포된 컬럼을 기준으로 목록컬럼, 검색컬럼 을 세팅 */
	public List<WfFormColmLVo> getLstToColmVoList(List<WfFormColmLVo> wfFormColmLVoList , List<WfFormLstDVo> wfFormLstDVoList) throws SQLException{
		if(wfFormLstDVoList==null || wfFormLstDVoList.size()==0) return null;
		
		// 배포된 테이블의 컬럼ID 목록
		List<String> colmIdList =getCurrColmIdList(wfFormColmLVoList);
				
		List<WfFormColmLVo> returnList=new ArrayList<WfFormColmLVo>();
		WfFormColmLVo newWfFormColmLVo;
		
		List<WfFormLstDVo> removeLstList=new ArrayList<WfFormLstDVo>();
		
		for(WfFormLstDVo storedWfFormLstDVo : wfFormLstDVoList){
			if(!ArrayUtil.isInArray(WfConstant.EXCLUDE_COLM_LIST, storedWfFormLstDVo.getColmId()) && !colmIdList.contains(storedWfFormLstDVo.getColmId())){
				removeLstList.add(storedWfFormLstDVo);
				continue;
			}
			newWfFormColmLVo=new WfFormColmLVo();
			newWfFormColmLVo.setColmId(storedWfFormLstDVo.getColmId());
			newWfFormColmLVo.setColmNm(storedWfFormLstDVo.getColmNm());
			newWfFormColmLVo.setItemNm(storedWfFormLstDVo.getItemNm());
			newWfFormColmLVo.setColmTypCd(storedWfFormLstDVo.getColmTypCd());
			returnList.add(newWfFormColmLVo);
		}
		
		if(removeLstList.size()>0){
			for(WfFormLstDVo storedWfFormLstDVo : removeLstList){
				wfFormLstDVoList.remove(storedWfFormLstDVo);
			}
		}
		
		return returnList;
	}
	
	/** 검색 컬럼 조회 */
	public List<WfFormLstDVo> getLstToSrchVoList(List<WfFormLstDVo> wfFormLstDVoList, WfFormRegDVo wfFormRegDVo) throws SQLException{
		if(wfFormLstDVoList==null || wfFormLstDVoList.size()==0) return null;
		
		// 양식 속성값을 JSON 객체로 변환
		JSONObject attrVa = (JSONObject) JSONValue.parse(wfFormRegDVo.getAttrVa());
		
		// 양식 속성의 상세 데이터
		JSONObject attrDtl=null;
				
		String colmNm;
		List<WfFormLstDVo> returnList=new ArrayList<WfFormLstDVo>();
		for(WfFormLstDVo storedWfFormLstDVo : wfFormLstDVoList){
			if(storedWfFormLstDVo.getSrchYn()==null || !"Y".equals(storedWfFormLstDVo.getSrchYn())) continue;
			colmNm=storedWfFormLstDVo.getColmNm();
			if(ArrayUtil.isInArray(WfConstant.SRCH_LIKE_CODE, storedWfFormLstDVo.getColmTypCd()) && attrVa.containsKey(colmNm)){
				attrDtl=(JSONObject)attrVa.get(colmNm);
				if(attrDtl.containsKey("chkTypCd") && !"".equals(attrDtl.get("chkTypCd"))){
					storedWfFormLstDVo.setCdColm(true);
				}
			}
			returnList.add(storedWfFormLstDVo);
		}
		
		return returnList;
	}
	
	/** 양식 등록화면 이력 조회 */
	@SuppressWarnings("unchecked")
	public List<WfFormRegDVo> getWfFormRegDVoList(String formNo) throws SQLException{
		WfFormRegDVo wfFormRegDVo = new WfFormRegDVo();
		wfFormRegDVo.setFormNo(formNo);
		wfFormRegDVo.setHst(true);
		wfFormRegDVo.setOrderBy("G.REG_DT DESC");
		return (List<WfFormRegDVo>)commonDao.queryList(wfFormRegDVo);
	}
	
	/** 양식 등록화면 데이터 조회 */
	public WfFormRegDVo getWfFormRegDVo(String genId, String formNo, boolean isHst, Boolean isMob) throws SQLException{
		WfFormRegDVo wfFormRegDVo = new WfFormRegDVo();
		if(genId!=null) wfFormRegDVo.setGenId(genId);
		if(formNo!=null) wfFormRegDVo.setFormNo(formNo);
		wfFormRegDVo.setHst(isHst);
		if(isMob!=null) wfFormRegDVo.setMob(isMob.booleanValue());
		return (WfFormRegDVo)commonDao.queryVo(wfFormRegDVo);
	}
	
	/** 양식 목록 데이터 조회 */
	@SuppressWarnings("unchecked")
	public List<WfFormLstDVo> getWfFormLstDVoList(HttpServletRequest request, String genId, String formNo, String srchYn, String mdCd, boolean isHst) throws SQLException{
		WfFormLstDVo wfFormLstDVo = new WfFormLstDVo();
		if(genId!=null) wfFormLstDVo.setGenId(genId);
		if(formNo!=null) wfFormLstDVo.setFormNo(formNo);
		if(mdCd!=null) wfFormLstDVo.setMdCd(mdCd);
		wfFormLstDVo.setHst(isHst);
		if(srchYn!=null) wfFormLstDVo.setSrchYn(srchYn);
		
		List<WfFormLstDVo> wfFormLstDVoList = (List<WfFormLstDVo>)commonDao.queryList(wfFormLstDVo);
		if(request!=null && wfFormLstDVoList!=null && wfFormLstDVoList.size()>0){
			Map<String, WfFormColmLVo> defaultColmMap = getDefaultColmMap(request, formNo);
			for(WfFormLstDVo storedWfFormLstDVo : wfFormLstDVoList){
				if(ArrayUtil.isInArray(WfConstant.EXCLUDE_COLM_LIST, storedWfFormLstDVo.getColmId())){
					storedWfFormLstDVo.setColmNm(defaultColmMap.get(storedWfFormLstDVo.getColmId()).getColmNm());
					storedWfFormLstDVo.setItemNm(defaultColmMap.get(storedWfFormLstDVo.getColmId()).getItemNm());
					storedWfFormLstDVo.setColmTypCd(defaultColmMap.get(storedWfFormLstDVo.getColmId()).getColmTypCd());
				}
			}
		}
		
		return wfFormLstDVoList;
	}
	
	/** 목록화면 구성 컬럼 조회 */
	public List<WfFormColmLVo> getWfFormColmLVoList(HttpServletRequest request, String formNo, String mdCd) throws SQLException{
		List<WfFormColmLVo> returnList=null;
		if("M".equals(mdCd) || "P".equals(mdCd)){
			// 기본목록 조회
			List<WfFormLstDVo> wfFormLstDVoList=getWfFormLstDVoList(request, null, formNo, null, "U", false);
			if(wfFormLstDVoList!=null && wfFormLstDVoList.size()>0){
				returnList=new ArrayList<WfFormColmLVo>();
				WfFormColmLVo wfFormColmLVo=null;
				for(WfFormLstDVo storedWfFormLstDVo : wfFormLstDVoList){
					wfFormColmLVo=new WfFormColmLVo();
					wfFormColmLVo.setColmId(storedWfFormLstDVo.getColmId());
					wfFormColmLVo.setColmNm(storedWfFormLstDVo.getColmNm());
					wfFormColmLVo.setItemNm(storedWfFormLstDVo.getItemNm());
					returnList.add(wfFormColmLVo);
				}
			}
		}else{
			// 양식 컬럼 목록 조회
			//WfFormColmLVo wfFormColmLVo= new WfFormColmLVo();
			//wfFormColmLVo.setFormNo(formNo);
			
			//returnList=(List<WfFormColmLVo>)commonDao.queryList(wfFormColmLVo);
			returnList=getColmVoList(formNo, null);
			
			if(request!=null){
				if(returnList!=null){
					// 제외할 컬럼VO 목록
					List<WfFormColmLVo> removeList=new ArrayList<WfFormColmLVo>();
					for(WfFormColmLVo storedWfFormColmLVo : returnList){
						if(ArrayUtil.isInArray(WfConstant.SINGLE_TO_TBL_COLM_LIST, storedWfFormColmLVo.getColmTypCd()))
							removeList.add(storedWfFormColmLVo);
					}
					
					if(removeList.size()>0){
						for(WfFormColmLVo storedWfFormColmLVo : removeList){
							returnList.remove(storedWfFormColmLVo);
						}
					}
				}
				if(returnList==null) returnList=new ArrayList<WfFormColmLVo>();
				// 기본컬럼 추가
				returnList.addAll(getDefaultColmList(request, formNo));
			}		
			
		}
		
		return returnList;
	}
	
	
	/** 기본컬럼 목록 맵 조회[조회수, 등록자UID, 등록일, 수정자, 수정일] */
	public Map<String, WfFormColmLVo> getDefaultColmMap(HttpServletRequest request, String formNo){
		List<WfFormColmLVo> returnList=getDefaultColmList(request, formNo);
		
		Map<String, WfFormColmLVo> returnMap=new HashMap<String, WfFormColmLVo>();
		for(WfFormColmLVo storedWfFormColmLVo : returnList){
			returnMap.put(storedWfFormColmLVo.getColmId(), storedWfFormColmLVo);
		}
		
		return returnMap;
	}
	
	
	/** 기본컬럼 목록 조회[조회수, 등록부서, 등록자UID, 등록일, 수정자, 수정일] */
	public List<WfFormColmLVo> getDefaultColmList(HttpServletRequest request, String formNo){
		List<WfFormColmLVo> returnList=new ArrayList<WfFormColmLVo>();
		
		WfFormColmLVo wfFormColmLVo=null;
		for(String[] colms : WfConstant.DFT_COLM_LIST){
			wfFormColmLVo=new WfFormColmLVo();
			wfFormColmLVo.setColmId(colms[0]); // 컬럼ID
			wfFormColmLVo.setFormNo(formNo);
			wfFormColmLVo.setColmTypCd(colms[1]);
			wfFormColmLVo.setColmNm(colms[0]);
			wfFormColmLVo.setItemNm(messageProperties.getMessage(colms[2], request));
			wfFormColmLVo.setUseYn("Y");
			wfFormColmLVo.setLstYn("Y");
			returnList.add(wfFormColmLVo);
		}
		
		return returnList;
	}
	
	/** 테이블ID 조회 */
	public String getTblId(String formNo){
		return IdUtil.createId(Long.parseLong(formNo), WfConstant.TBLNM_LEN);
	}
	
	/** 그룹 세팅 */
	public void setParamGrpList(HttpServletRequest request, ModelMap model, String compId, String langTypCd, String grpId, String useYn) throws SQLException, CmException{
		List<List<WfFormGrpBVo>> paramGrpList=new ArrayList<List<WfFormGrpBVo>>();
		List<WfFormGrpBVo> wfFormGrpBVoList=null;
		
		if(grpId==null)
			grpId = ParamUtil.getRequestParam(request, "grpId", false);
		if(grpId!=null && !grpId.isEmpty()){
			WfFormGrpBVo wfFormGrpBVo = getWfFormGrpBVo(compId, grpId, langTypCd);
			if(!"ROOT".equals(wfFormGrpBVo.getGrpPid())){ // 1단계 그룹이 아닐경우 상위 그룹 목록 조회
				List<String> grpPidList=new ArrayList<String>();
				List<String> grpIdList=new ArrayList<String>();
				List<WfFormGrpBVo> topTreeList = getTopTreeList(wfFormGrpBVo.getCompId(), grpId, langTypCd, true);
				if(topTreeList!=null && topTreeList.size()>0){
					for(WfFormGrpBVo storedWfFormGrpBVo : topTreeList){
						grpIdList.add(storedWfFormGrpBVo.getGrpId());
						grpPidList.add(storedWfFormGrpBVo.getGrpPid());					
					}
				}
				
				if(grpPidList.size()>0){
					Collections.reverse(grpIdList); // 순서를 변경
					Collections.reverse(grpPidList); // 순서를 변경
					for(String pid : grpPidList){
						wfFormGrpBVoList=getGrpList(null, compId, langTypCd, pid, useYn);
						if(wfFormGrpBVoList!=null && wfFormGrpBVoList.size()>0)
							paramGrpList.add(wfFormGrpBVoList);
					}
					
					model.put("paramGrpIds", grpIdList); // 콤보박스를 선택하기 위한 그룹ID 배열
				}
				
			}
		}
		// 목록이 없을경우 상위 그룹 목록 조회
		if(paramGrpList.size()==0){
			wfFormGrpBVoList=getGrpList(null, compId, langTypCd, "ROOT", useYn);
			if(wfFormGrpBVoList!=null)
				paramGrpList.add(wfFormGrpBVoList);
		}
		// 선택된 그룹ID의 하위 그룹 조회
		if(grpId!=null && !grpId.isEmpty()){
			wfFormGrpBVoList=getGrpList(null, compId, langTypCd, grpId, useYn);
			if(wfFormGrpBVoList!=null && wfFormGrpBVoList.size()>0)
				paramGrpList.add(wfFormGrpBVoList);
		}
		
		if(paramGrpList.size()>0)
			model.put("paramGrpList", paramGrpList);
	}
	
	
}
