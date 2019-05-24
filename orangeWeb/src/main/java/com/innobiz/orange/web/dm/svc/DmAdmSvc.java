package com.innobiz.orange.web.dm.svc;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.innobiz.orange.web.cm.config.CacheConfig;
import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.utils.ArrayUtil;
import com.innobiz.orange.web.cm.utils.Hash;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.StringUtil;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.dm.utils.DmConstant;
import com.innobiz.orange.web.dm.vo.DmCatBVo;
import com.innobiz.orange.web.dm.vo.DmCatDispDVo;
import com.innobiz.orange.web.dm.vo.DmCdDVo;
import com.innobiz.orange.web.dm.vo.DmClsBVo;
import com.innobiz.orange.web.dm.vo.DmFldBVo;
import com.innobiz.orange.web.dm.vo.DmItemBVo;
import com.innobiz.orange.web.dm.vo.DmItemDispDVo;
import com.innobiz.orange.web.dm.vo.DmRescBVo;
import com.innobiz.orange.web.dm.vo.DmStorBVo;
import com.innobiz.orange.web.or.svc.OrCmSvc;
import com.innobiz.orange.web.or.vo.OrOrgBVo;
import com.innobiz.orange.web.or.vo.OrUserBVo;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtCacheSvc;
import com.innobiz.orange.web.pt.svc.PtCmSvc;
import com.innobiz.orange.web.pt.svc.PtSysSvc;
import com.innobiz.orange.web.pt.vo.PtCdBVo;
import com.innobiz.orange.web.pt.vo.PtCompBVo;

@Service
public class DmAdmSvc {
	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(DmAdmSvc.class);
	
	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;
	
	/** 메세지 */
	@Autowired
    private MessageProperties messageProperties;
	
	/** 포탈 공통 서비스 */
	@Autowired
	private PtCmSvc ptCmSvc;
	
	/** 조직 공통 서비스 */
	@Autowired
	private OrCmSvc orCmSvc;
	
	/** 저장소 서비스 */
	@Resource(name = "dmStorSvc")
	private DmStorSvc dmStorSvc;
	
	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
	/** 캐쉬 서비스 */
	@Autowired
	private PtCacheSvc ptCacheSvc;
	
	/** 유형 삭제 */
	public void deleteCat(HttpServletRequest request, DmCatBVo dmCatBVo, QueryQueue queryQueue)
			throws SQLException {
		
		// 유형 데이터 조회
		DmCatBVo storedDmCatBVo = (DmCatBVo) commonDao.queryVo(dmCatBVo);

		// 리소스기본(DM_RESC_B) 테이블 - DELETE
		if (storedDmCatBVo.getRescId() != null && !storedDmCatBVo.getRescId().isEmpty()) {
			DmRescBVo dmRescBVo = new DmRescBVo(dmCatBVo.getStorId());
			dmRescBVo.setRescId(storedDmCatBVo.getRescId());
			queryQueue.delete(dmRescBVo);
		}
		
		// 유형관리(DM_TBL_B) 테이블 - DELETE
		queryQueue.delete(storedDmCatBVo);
		
		// 항목표시여부(DM_CAT_DISP_D) 테이블 - SELECT
		DmCatDispDVo dispVo = new DmCatDispDVo();
		dispVo.setStorId(dmCatBVo.getStorId());
		dispVo.setCatId(dmCatBVo.getCatId());
		queryQueue.delete(dispVo);
	}
	
	/** 공통코드 목록 저장 */
	@SuppressWarnings("unchecked")
	public void setDmCdList(String storId, String cdGrpTyp, String cdGrpId, DmItemBVo colmVo, String langTypCd){
		try{
			List<DmCdDVo> cdList = new ArrayList<DmCdDVo>();
			DmCdDVo dmCdDVo = null;
			//코드상세 조회
			if("CODE".equals(cdGrpTyp) && cdGrpId != null && !cdGrpId.isEmpty()){
				dmCdDVo = new DmCdDVo();
				dmCdDVo.setQueryLang(langTypCd);
				dmCdDVo.setStorId(storId);
				dmCdDVo.setCdGrpId(cdGrpId);
				cdList = (List<DmCdDVo>)commonDao.queryList(dmCdDVo);
			}else if("CD".equals(cdGrpTyp) && cdGrpId != null && !cdGrpId.isEmpty()){
				// 포탈 공통코드 조회
				List<PtCdBVo> ptCdList = ptCmSvc.getCdList(cdGrpId, langTypCd, "Y");
				for(PtCdBVo storedPtCdBVo : ptCdList){
					dmCdDVo = new DmCdDVo();
					dmCdDVo.setStorId(storId);
					dmCdDVo.setCdId(storedPtCdBVo.getCd());
					dmCdDVo.setRescNm(storedPtCdBVo.getRescNm());
					cdList.add(dmCdDVo);
				}
			}
			if(cdList != null && cdList.size() > 0){
				colmVo.setCdList(cdList);
			}
		}catch(SQLException sqle){
			LOGGER.error("[ERROR] setDmCdList - cdGrpTyp:"+cdGrpTyp+"\tcdGrpId:"+cdGrpId);
		}
	
	}
	
	/**
	 * 유형표시여부 리스트 리턴
	 */
	@SuppressWarnings("unchecked")
	public List<DmCatDispDVo> getDmCatDispDVoList(HttpServletRequest request, String storId, String catId, boolean ordered, 
			String useYn, String dispCol, String addItemYn) throws SQLException {
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);

		// 유형표시여부(DM_CAT_DISP_D) 테이블 - BIND
		DmCatDispDVo dmCatDispDVo = new DmCatDispDVo();
		dmCatDispDVo.setQueryLang(langTypCd);
		dmCatDispDVo.setStorId(storId);
		dmCatDispDVo.setCatId(catId);
		if (useYn != null) dmCatDispDVo.setUseYn(useYn);
		if (addItemYn != null) dmCatDispDVo.setAddItemYn(addItemYn);
		if ("reg".equals(dispCol)) dmCatDispDVo.setRegDispYn("Y");
		if ("mod".equals(dispCol)) dmCatDispDVo.setModDispYn("Y");
		if ("read".equals(dispCol)) dmCatDispDVo.setReadDispYn("Y");
		if ("list".equals(dispCol)) dmCatDispDVo.setListDispYn("Y");
		if (ordered) dmCatDispDVo.setOrderBy("T.LIST_DISP_ORDR ASC");

		// 유형표시여부(DM_CAT_DISP_D) 테이블 - SELECT
		List<DmCatDispDVo> dmCatDispDVoList = (List<DmCatDispDVo>) commonDao.queryList(dmCatDispDVo);
		
		if (dmCatDispDVoList != null) {
			// 기본항목 조회
			Map<String,DmItemBVo> map = dmStorSvc.getDftColmVoMap(request, null);
			// 유형(DM_ITEM_B) 테이블 - SELECT
			DmItemBVo colmVo = null;
			for (DmCatDispDVo dispVo : dmCatDispDVoList) {
				if("N".equals(dispVo.getAddItemYn()) && map.containsKey(dispVo.getAtrbId())){
					colmVo = map.get(dispVo.getAtrbId());
				}else{
					// 유형(DM_ITEM_B) 테이블 - SELECT
					colmVo = new DmItemBVo();
					colmVo.setQueryLang(langTypCd);
					colmVo.setStorId(storId);
					colmVo.setItemId(dispVo.getItemId());
					colmVo = (DmItemBVo) commonDao.queryVo(colmVo);
				}
				
				// 항목중에 코드일경우 해당 코드상세, 포탈공통코드를 조회한다.
				setDmCdList(storId, colmVo.getItemTyp(), colmVo.getItemTypVa(), colmVo, langTypCd);
				dispVo.setColmVo(colmVo);
			}
		}
		return dmCatDispDVoList;
	}
	
	/*public List<DmItemDispDVo> getDftDispVoList(HttpServletRequest request, String[] atrbIds) throws SQLException {
		List<DmItemDispDVo> dmItemDispDVoList = null;
		// 기본항목 VO List 생성
		List<DmItemBVo> colmVoList = dmStorSvc.getDftColmList(request, new ArrayList<DmItemBVo>(), null, null);
		String atrbId;
		for (DmItemBVo colmVo : colmVoList){
			atrbId = StringUtil.toCamelNotation(colmVo.getItemNm(), false);
			if(ArrayUtil.isInArray(atrbIds, atrbId)){
				
			}
		}
		DmItemDispDVo dispVo = new DmItemDispDVo();
		
	}*/
	
	/** 항목 목록을 세팅 */
	public void setDispVoList(List<DmItemBVo> colmVoList, String langTypCd, List<DmItemDispDVo> dmItemDispDVoList, String useYn, String dispCol, String[] atrbIds){
		// 기본 정렬 항목[LEFT]
		String[] alnVas = {"fldNm","clsGrpNm","docNo","subj","cont"};
				
		String atrbId;
		if (colmVoList != null) {
			DmItemDispDVo dispVo = null;
			// 기본항목 중에서 조회 조건에 맞는 항목만 추출
			for (DmItemBVo colmVo : colmVoList) {
				if(useYn != null && !"Y".equals(colmVo.getUseYn())) continue;
				if(dispCol != null){
					//if ("reg".equals(dispCol) && !"Y".equals(colmVo.getRegDispYn())) continue;
					//if ("mod".equals(dispCol) && !"Y".equals(colmVo.getModDispYn())) continue;
					if ("read".equals(dispCol) && !"Y".equals(colmVo.getReadDispYn())) continue;
					if ("list".equals(dispCol) && !"Y".equals(colmVo.getListDispYn())) continue;
				}
				atrbId = StringUtil.toCamelNotation(colmVo.getItemNm(), false);
				// 선택속성ID가 있으면 해당하는 속성목록만 구성
				if(atrbIds != null && !ArrayUtil.isInArray(atrbIds, atrbId)) continue;
				// 유형표시(DM_CAT_DISP_D) 생성 - SELECT
				dispVo = new DmItemDispDVo();
				// 속성ID
				dmStorSvc.setColmNmChn(colmVo);
				//atrbId = StringUtil.toCamelNotation(colmVo.getItemNm(), false);				
				// 항목중에 코드일경우 해당 코드상세, 포탈공통코드를 조회한다.
				setDmCdList(null, colmVo.getItemTyp(), colmVo.getItemTypVa(), colmVo, langTypCd);
				dispVo.setAtrbId(StringUtil.toCamelNotation(colmVo.getItemNm(), false));
				dispVo.setColmVo(colmVo);
				dispVo.setUseYn("Y");//사용여부 'Y'
				dispVo.setListDispYn(colmVo.getListDispYn());
				dispVo.setReadDispYn(colmVo.getReadDispYn());
				dispVo.setAlnVa(ArrayUtil.isInArray(alnVas, dispVo.getAtrbId()) ? "left" : "center");
				if("subj".equals(dispVo.getAtrbId())) dispVo.setWdthPerc("25");//제목 가로길이 설정
				
				if("CD".equals(colmVo.getItemTyp())) dispVo.setSortOptVa("cd");
				else if("CODE".equals(colmVo.getItemTyp())) dispVo.setSortOptVa("code");
				else dispVo.setSortOptVa("value");
				
				if("regDt".equals(dispVo.getAtrbId())) {// 기본 정렬 등록일시 설정
					dispVo.setDataSortVa("desc");
				}
				dmItemDispDVoList.add(dispVo);
			}
		}
	}
	
	/** 항목표시여부 리스트 조회[기본] */
	public void setDftDispVoList(HttpServletRequest request, String langTypCd, List<DmItemDispDVo> dmItemDispDVoList, 
			String useYn, String dispCol, String[] atrbIds) throws SQLException {
		// 기본항목 VO List 생성
		List<DmItemBVo> colmVoList = dmStorSvc.getDftColmList(request, new ArrayList<DmItemBVo>(), null, null);
		if(dmItemDispDVoList == null) dmItemDispDVoList = new ArrayList<DmItemDispDVo>();
		setDispVoList(colmVoList, langTypCd, dmItemDispDVoList, useYn, dispCol, atrbIds);
	}
	
	/** 항목표시여부 리스트 조회[추가] - 인수인계,이관 */
	public void setAddDispVoList(HttpServletRequest request, String langTypCd, List<DmItemDispDVo> dmItemDispDVoList, 
			String useYn, String dispCol, String[] atrbIds) throws SQLException {
		// 기본항목 VO List 생성
		List<DmItemBVo> colmVoList = dmStorSvc.getAddColmList(request, new ArrayList<DmItemBVo>(), null, null);
		if(dmItemDispDVoList == null) dmItemDispDVoList = new ArrayList<DmItemDispDVo>();
		setDispVoList(colmVoList, langTypCd, dmItemDispDVoList, useYn, dispCol, atrbIds);
	}
	
	/** 항목표시여부 리스트 조회[목록,폴더,분류] */
	public List<DmItemDispDVo> getItemDispList(HttpServletRequest request, boolean ordered, 
			String useYn, String dispCol, String itemTypCd, String compId) throws SQLException {
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
				
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		DmItemDispDVo dmItemDispDVo = new DmItemDispDVo();
		dmItemDispDVo.setQueryLang(langTypCd);
		dmItemDispDVo.setItemTypCd(itemTypCd);
		if(useYn != null) dmItemDispDVo.setUseYn(useYn);
		if(compId != null) dmItemDispDVo.setCompId(compId);
		else dmItemDispDVo.setCompId(userVo.getCompId());
		if(dispCol != null){
			if ("read".equals(dispCol)) dmItemDispDVo.setReadDispYn("Y");
			if ("list".equals(dispCol)) dmItemDispDVo.setListDispYn("Y");
		}
		if (ordered) dmItemDispDVo.setOrderBy("T.LIST_DISP_ORDR ASC");
		@SuppressWarnings("unchecked")
		List<DmItemDispDVo> dmItemDispDVoList = (List<DmItemDispDVo>)commonDao.queryList(dmItemDispDVo);
		// 기본 항목이 설정되어 있을경우
		if(dmItemDispDVoList != null && dmItemDispDVoList.size() > 0){
			Map<String,DmItemBVo> map = dmStorSvc.getDftColmVoMap(request, null);
			for(DmItemDispDVo dispVo : dmItemDispDVoList){
				if(map.containsKey(dispVo.getAtrbId())){
					dispVo.setColmVo(map.get(dispVo.getAtrbId()));
					// 항목중에 코드일경우 해당 코드상세, 포탈공통코드를 조회한다.
					setDmCdList(null, dispVo.getColmVo().getItemTyp(), dispVo.getColmVo().getItemTypVa(), dispVo.getColmVo(), langTypCd);
				}
			}
		}else{
			// 기본 항목 조회
			setDftDispVoList(request, langTypCd, dmItemDispDVoList, useYn, dispCol, null);
		}
		
		return dmItemDispDVoList;
	}
	
	
	/** 권한대상 목록 */
	public List<Map<String,String>> getDmAuthCdList(HttpServletRequest request) throws SQLException {
		List<Map<String,String>> returnList = new ArrayList<Map<String,String>>();
		Map<String,String> map = null;
		for(String va : DmConstant.AUTH_CDS){
			map = new HashMap<String,String>();
			map.put("va", va);
			map.put("msg", messageProperties.getMessage("dm.cols.auth."+va, request));
			returnList.add(map);
		}
		
		return returnList;
	}
	
	/** 기본 폴더 생성 */
	public DmFldBVo makeDmFldVo(String storId, String fldId, String fldGrpId, String fldNm, String fldPid, String fldTypCd, Integer sortOrdr, String useYn){
		DmFldBVo dmFldBVo = new DmFldBVo(storId);
		dmFldBVo.setStorId(storId);
		dmFldBVo.setFldId(fldId);
		dmFldBVo.setFldGrpId(fldGrpId);
		dmFldBVo.setCatId("none");
		dmFldBVo.setFldNm(fldNm);//회사명
		dmFldBVo.setRescId("none");
		dmFldBVo.setFldPid(fldPid);
		dmFldBVo.setFldTypCd(fldTypCd);
		dmFldBVo.setSortOrdr(sortOrdr);
		dmFldBVo.setUseYn(useYn);
		return dmFldBVo;
	}
	
	/** 폴더목록을 리턴, allChk 이 true 면 전체를 false 면 내가 속한 회사,조직 폴더 목록을 리턴 */
	public List<DmFldBVo> getDmFldBVoList(String storId, String langTypCd, String compId, String orgId, boolean allChk, boolean noneChk) throws SQLException{
		List<DmFldBVo> fldList = new ArrayList<DmFldBVo>();
		if(noneChk){//미분류 조회 여부
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
			fldList.add(makeDmFldVo(storId, DmConstant.FLD_NONE, DmConstant.FLD_NONE, messageProperties.getMessage("dm.cols.emptyCls", request), "ROOT", "F", 1, "Y"));
		}
		if(allChk) fldList.addAll(getFldAllTreeList(storId, langTypCd, null));
			//return getFldAllTreeList(storId, langTypCd);
		else fldList.addAll(getFldTreeList(storId, langTypCd, compId, orgId, "F", "Y"));
			//return getFldTreeList(storId, langTypCd, compId, orgId, "F", "Y");
		return fldList;
	}
	
	/** 폴더목록 회사, 부서 별 조회 */
	public List<DmFldBVo> getFldTreeList(String storId, String langTypCd, String compId, String orgId, String fldTypCd, String useYn) throws SQLException{
		List<DmFldBVo> returnList = new ArrayList<DmFldBVo>();
		if(compId != null) returnList.addAll(getDownTreeList(storId, compId, langTypCd, false, fldTypCd, useYn));
		if(orgId != null) returnList.addAll(getDownTreeList(storId, orgId, langTypCd, false, fldTypCd, useYn));
		setTopFldList(returnList, storId, compId, orgId);
		return returnList;
	}
	
	/** 폴더목록 전체 조회 */
	public List<DmFldBVo> getFldAllTreeList(String storId, String langTypCd, String useYn) throws SQLException{
		List<DmFldBVo> returnList = getDownTreeList(storId, DmConstant.FLD_COMP, langTypCd, true, null, useYn);
		if(returnList != null)
			returnList.addAll(getDownTreeList(storId, DmConstant.FLD_DEPT, langTypCd, true, null, useYn));
		return returnList;
	}
	
	/** 최상위 폴더 세팅 */
	public void setTopFldList(List<DmFldBVo> dmFldBVoList, String storId, String compId, String orgId){
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		//if(compId == null) compId = DmConstant.FLD_COMP;
		//if(orgId == null) orgId = DmConstant.FLD_DEPT;
		// 회사 폴더[기본]
		if(compId != null) dmFldBVoList.add(makeDmFldVo(storId, compId, DmConstant.FLD_COMP, messageProperties.getMessage("cols.comp", request), "ROOT", "F", 2, "Y"));
		// 부서 폴더[기본]
		dmFldBVoList.add(makeDmFldVo(storId, orgId, DmConstant.FLD_DEPT, messageProperties.getMessage("cols.dept", request), "ROOT", "F", 3, "Y"));
		// 미분류[기본]
		/*if(compId != null && orgId != null)
			dmFldBVoList.add(makeDmFldVo(storId, DmConstant.FLD_NONE, DmConstant.FLD_NONE, messageProperties.getMessage("dm.cols.emptyCls", request), "ROOT", "F", 2, "Y"));*/
	}
	
	/** 전체 폴더 조회 */
	@SuppressWarnings("unchecked")
	public List<DmFldBVo> getDmFldAllList(String langTypCd, String storId, String useYn) throws SQLException{
		// 폴더 목록 조회[저장소별]
		DmFldBVo dmFldBVo = new DmFldBVo();
		dmFldBVo.setStorId(storId);
		dmFldBVo.setQueryLang(langTypCd);
		return (List<DmFldBVo>)commonDao.queryList(dmFldBVo);
	}
	
	/** 하위 폴더 ID 목록 리턴 */
	public List<String> getFldSubIdList(String storId, String fldId, String langTypCd) throws SQLException{
		Map<Integer, List<String>> subIdListMap = getFldSubListMap(langTypCd, storId);
		List<String> returnList = new ArrayList<String>();
		collectFldSubIds(fldId, subIdListMap, returnList);
		return returnList;
	}
	
	/** 서브 폴더 ID 모음 */
	private void collectFldSubIds(String fldId, Map<Integer, List<String>> subIdListMap, List<String> returnList){
		List<String> subIdList = subIdListMap.get(Hash.hashId(fldId));
		if(subIdList!=null){
			for(String id : subIdList){
				returnList.add(id);
				collectFldSubIds(id, subIdListMap, returnList);
			}
		}
	}
	
	/** 폴더 맵 리턴(캐쉬) */
	private Map<Integer, DmFldBVo> getFldMap(String langTypCd, String storId) throws SQLException{
		// 전체 폴더맵
		Map<Integer, DmFldBVo> returnFldMap = new HashMap<Integer, DmFldBVo>();
		// 전체 폴더ID맵
		Map<Integer, List<String>> returnSubIdListMap = new HashMap<Integer, List<String>>();
		// 조직정보를 맵에 담는다.
		loadOrgMap(langTypCd, returnFldMap, returnSubIdListMap, storId);
		
		@SuppressWarnings("unchecked")
		Map<Integer, DmFldBVo> fldMap = (Map<Integer, DmFldBVo>)
				ptCacheSvc.getCache(DmConstant.FLD, langTypCd, "MAP"+storId, 10);
		if(fldMap==null) {
			fldMap = new HashMap<Integer, DmFldBVo>();
			Map<Integer, List<String>> subIdListMap = new HashMap<Integer, List<String>>();
			loadFldMap(langTypCd, fldMap, subIdListMap, storId);
			ptCacheSvc.setCache(DmConstant.FLD, langTypCd, "MAP"+storId,  fldMap);
			ptCacheSvc.setCache(DmConstant.FLD, langTypCd, "SUB_ID_LIST_MAP"+storId,  subIdListMap);
		}
		if(fldMap!=null) returnFldMap.putAll(fldMap);
		return returnFldMap;
	}
	
	/** 서브 폴더 목록 맵 리턴(캐쉬) */
	private Map<Integer, List<String>> getFldSubListMap(String langTypCd, String storId) throws SQLException{
		// 전체 폴더맵
		Map<Integer, DmFldBVo> returnFldMap = new HashMap<Integer, DmFldBVo>();
		// 전체 폴더ID맵
		Map<Integer, List<String>> returnSubIdListMap = new HashMap<Integer, List<String>>();
		// 조직정보를 맵에 담는다.
		loadOrgMap(langTypCd, returnFldMap, returnSubIdListMap, storId);
				
		@SuppressWarnings("unchecked")
		Map<Integer, List<String>> subIdListMap = (Map<Integer, List<String>>)
				ptCacheSvc.getCache(DmConstant.FLD, langTypCd, "SUB_ID_LIST_MAP"+storId, 10);
		if(subIdListMap==null){
			Map<Integer, DmFldBVo> fldMap = new HashMap<Integer, DmFldBVo>();
			subIdListMap = new HashMap<Integer, List<String>>();
			loadFldMap(langTypCd, fldMap, subIdListMap, storId);
			ptCacheSvc.setCache(DmConstant.FLD, langTypCd, "MAP"+storId,  fldMap);
			ptCacheSvc.setCache(DmConstant.FLD, langTypCd, "SUB_ID_LIST_MAP"+storId,  subIdListMap);
		}
		if(subIdListMap!=null) setMergeSubIdList(returnSubIdListMap, subIdListMap);//returnSubIdListMap.putAll(subIdListMap);
		return returnSubIdListMap;
	}
	
	/** 조직도 폴더와 서브폴더 목록을 담는다. */
	private void setMergeSubIdList(Map<Integer, List<String>> returnSubIdListMap, Map<Integer, List<String>> subIdListMap){
		if(subIdListMap==null || subIdListMap.size()==0) return;
		Iterator<Entry<Integer, List<String>>> iterator = subIdListMap.entrySet().iterator();
		Entry<Integer, List<String>> entry;
		while(iterator.hasNext()){
			entry = iterator.next();
			if(!returnSubIdListMap.containsKey(entry.getKey()))
				returnSubIdListMap.put(entry.getKey(), new ArrayList<String>());			
			returnSubIdListMap.get(entry.getKey()).addAll(subIdListMap.get(entry.getKey()));
		}		
	}
	
	/** 조직도목록을 폴더 맵, 서브 폴더 목록 맵에 담는다 */
	private void loadOrgMap(String langTypCd, 
			Map<Integer, DmFldBVo> fldMap, Map<Integer, List<String>> subIdListMap, String storId) throws SQLException{
		List<DmFldBVo> dmFldBVoList = getOrgFldList(langTypCd, storId, "Y");
		if(dmFldBVoList!=null && dmFldBVoList.size()>0){
			setFldMap(fldMap, subIdListMap, dmFldBVoList);
		}
	}
	
	/** 폴더 맵, 서브 폴더 목록 맵 조회 */
	private void loadFldMap(String langTypCd, 
			Map<Integer, DmFldBVo> fldMap, Map<Integer, List<String>> subIdListMap, String storId) throws SQLException{
		List<DmFldBVo> dmFldBVoList = getDmFldAllList(langTypCd, storId, "Y");
		if(dmFldBVoList!=null && dmFldBVoList.size()>0){
			setFldMap(fldMap, subIdListMap, dmFldBVoList);
		}
	}
	
	/** 폴더목록을 맵으로 변환 */
	public void setFldMap(Map<Integer, DmFldBVo> fldMap, 
			Map<Integer, List<String>> subIdListMap, List<DmFldBVo> dmFldBVoList){
		DmFldBVo dmFldBVo;
		String fldPid=null, fldId;
		List<String> idList = null;
		int i, size = dmFldBVoList==null ? 0 : dmFldBVoList.size();
		for(i=0;i<size;i++){
			dmFldBVo = dmFldBVoList.get(i);
			//저장소ID
			fldId = dmFldBVo.getFldId();
			fldPid = dmFldBVo.getFldPid();
			if(!subIdListMap.containsKey(Hash.hashId(fldPid)))
				subIdListMap.put(Hash.hashId(fldPid), new ArrayList<String>());
			idList = subIdListMap.get(Hash.hashId(fldPid));
			idList.add(dmFldBVo.getFldId());
			if(!dmFldBVo.getFldId().equals(fldPid))
				fldMap.put(Hash.hashId(fldId), dmFldBVo);
		}
	}
	
	/** 폴더 정보 조회 */
	public DmFldBVo getFldBVo(String storId, String fldId, String langTypCd) throws SQLException{
		Map<Integer, DmFldBVo> fldMap = getFldMap(langTypCd, storId);
		int hashId = Hash.hashId(fldId);
		return fldMap.get(hashId);
	}
	
	/** 최상위 폴더 정보 조회 */
	public DmFldBVo getTopTreeVo(String storId, String fldId, String langTypCd) throws SQLException{
		Map<Integer, DmFldBVo> fldMap = getFldMap(langTypCd, storId);
		DmFldBVo dmFldBVo = getTopFldVo(fldId, new DmFldBVo(), fldMap);
		return dmFldBVo;
	}
	
	/** 최상위 폴더 정보를 리턴 */
	private DmFldBVo getTopFldVo(String fldPid, DmFldBVo dmFldBVo, 
			Map<Integer, DmFldBVo> fldMap){
		int hashId = Hash.hashId(fldPid);
		if(fldMap.get(hashId)!=null){
			dmFldBVo = fldMap.get(hashId); 
			return getTopFldVo(dmFldBVo.getFldPid(), dmFldBVo, fldMap);
		}
		return dmFldBVo;
	}
	
	/** 상위 폴더 트리 조회 */
	public List<DmFldBVo> getTopTreeList(String storId, String fldId, String langTypCd, boolean first, String fldTypCd) throws SQLException{
		Map<Integer, DmFldBVo> fldMap = getFldMap(langTypCd, storId);
		Map<Integer, List<String>> fldSubListMap = getFldSubListMap(langTypCd, storId);
		List<DmFldBVo> returnList = new ArrayList<DmFldBVo>();
		setTopTreeList(fldId, returnList, fldSubListMap, fldMap, first, fldTypCd);		
		return returnList;
	}
	
	/** 상위 폴더 정보를 returnList 에 담음 */
	private void setTopTreeList(String fldId, List<DmFldBVo> returnList, 
			Map<Integer, List<String>> fldSubListMap, Map<Integer, DmFldBVo> fldMap, boolean first, String fldTypCd){
		int hashId = Hash.hashId(fldId);
		DmFldBVo dmFldBVo = fldMap.get(hashId);
		if(dmFldBVo!=null){
			if(first && (fldTypCd == null || dmFldBVo.getFldTypCd().equals(fldTypCd))) returnList.add(dmFldBVo);
			setTopTreeList(dmFldBVo.getFldPid(), returnList, fldSubListMap, fldMap, true, fldTypCd);
		}
	}
	
	/** 하위 폴더 트리 조회 */
	public List<DmFldBVo> getDownTreeList(String storId, String fldId, String langTypCd, boolean first, String fldTypCd, String useYn) throws SQLException{
		Map<Integer, DmFldBVo> fldMap = getFldMap(langTypCd, storId);
		Map<Integer, List<String>> fldSubListMap = getFldSubListMap(langTypCd, storId);
		List<DmFldBVo> returnList = new ArrayList<DmFldBVo>();
		setDownTreeList(fldId, returnList, fldSubListMap, fldMap, first, fldTypCd, useYn);
		return returnList;
	}
	
	/** 하위 폴더 정보를 returnList 에 담음 */
	private void setDownTreeList(String fldId, List<DmFldBVo> returnList, 
			Map<Integer, List<String>> fldSubListMap, Map<Integer, DmFldBVo> fldMap, boolean first, String fldTypCd, String useYn){
		int hashId = Hash.hashId(fldId);
		DmFldBVo dmFldBVo = fldMap.get(hashId);
		if(dmFldBVo!=null){
			if(first && ((useYn == null || (useYn != null && useYn.equals(dmFldBVo.getUseYn()))) && (fldTypCd == null || dmFldBVo.getFldTypCd().equals(fldTypCd)))) returnList.add(dmFldBVo);
			List<String> fldSubList = fldSubListMap.get(hashId);
			if(fldSubList!=null){
				for(String subFldId : fldSubList){
					if(fldTypCd != null && !isFldTypChk(fldMap, subFldId, fldTypCd)) continue;
					setDownTreeList(subFldId, returnList, fldSubListMap, fldMap, true, fldTypCd, useYn);
				}
			}
		}
	}
	
	/** 폴더여부 확인 */
	private boolean isFldTypChk(Map<Integer, DmFldBVo> fldMap, String fldId, String fldTypCd){
		int hashId = Hash.hashId(fldId);
		return fldMap.get(hashId) != null && fldTypCd.equals(fldMap.get(hashId).getFldTypCd()) ? true : false;
	}
	
	/** 커스텀 코드 조회 *//*
	private List<String[]> getCustomCodes(String codeGrpId){
		List<String[]> returnList = new ArrayList<String[]>();
		if(codeGrpId.equals(DmConstant.VIEW_TGT_CD)){
			
		}else if(codeGrpId.equals(DmConstant.VIEW_TGT_CD)){
			
		}
		
		return null;
	}*/
	
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
		Map<String, String> envConfigMap = ptSysSvc.getSysSetupMap(DmConstant.SYS_CONFIG+compId, true);
		if(envConfigMap == null || envConfigMap.isEmpty()){
			
			envConfigMap = new HashMap<String, String>();
			envConfigMap.put("verDft", "1.0");// 버전(시작)
			envConfigMap.put("verFrt", "1.0");// 버전업 앞자리
			envConfigMap.put("verRear", "0.1");// 버전업 뒷자리
			envConfigMap.put("verMnalYn", "N");// 수동입력여부
			//envConfigMap.put("verCondn", "L");// 버전조건
			envConfigMap.put("newDocPrd", "5");// 신규문서기간[일] - 작성,승인,반려일자
			envConfigMap.put("docNoMnalYn", "Y");// 문서번호 수동입력여부
			envConfigMap.put("docNoOpt1", "YYYY");// 문서번호 조건1
			envConfigMap.put("docNoOpt2", "notUse");// 문서번호 조건2
			envConfigMap.put("docNoOpt3", "notUse");// 문서번호 조건3
			envConfigMap.put("docNoSeqLen", "4");// 문서 ID 자리수
			envConfigMap.put("docNoFxLen", "Y");// 문서 ID 고정여부
			envConfigMap.put("recoMt", "");// 회계 기준일[월]
			envConfigMap.put("recoDt", "");// 회계 기준일[일]
			envConfigMap.put("lstTyp", "F");// 문서보기 구분[폴더:F,분류체계:C,목록:L]
			envConfigMap.put("newDdln", "7D");// 최신문서 기한
			envConfigMap.put("keepDdln", "10D");// 보존연한 기한[도래문서 기준]
			envConfigMap.put("taskCds", StringUtils.join(DmConstant.TASK_CDS, ','));// 문서이력구분코드
			envConfigMap.put("readCntRule", "daily");// 조회수 기준[1일 1회-마지막조회]
			envConfigMap.put("listOpt", "A");// 조회옵션[A:전체조회,B:권한적용]
			envConfigMap.put("moveWithSubYn", "N");// 하위문서포함[Y:포함, N:미포함] - 문서이동
			envConfigMap.put("copyWithSubYn", "N");// 하위문서포함[Y:포함, N:미포함] - 문서복사
			envConfigMap.put("takovrWithSubYn", "N");// 하위문서포함[Y:포함, N:미포함] - 문서인계
			envConfigMap.put("docNoDftYear", "Y");// 문서채번(연도) 기준
			envConfigMap.put("docNoDftOrg", "Y");// 문서채번(조직) 기준
			envConfigMap.put("dtlViewOpt", "N");// Y:열람요청허용, N:열람요청허용안함
			
			ptCacheSvc.setCache(CacheConfig.SYS_SETUP, "ko", DmConstant.SYS_CONFIG+compId, envConfigMap);
		}
		if(model!=null) model.put("envConfigMap", envConfigMap);
		
		return envConfigMap;
	}
	
	/** 환경설정 값 조회
	 *  key : listOpt - 조회옵션[A:전체조회,B:권한적용] 
	 * */
	public String getEnvConfigVal(String compId, String key) throws SQLException {
		if(compId == null || key == null) return null;
		Map<String,String>	envConfigMap = getEnvConfigMap(null, compId);
		if(envConfigMap == null) return null;
		return envConfigMap.get(key);
	}
	
	
	/** 분류목록을 리턴 */
	public List<DmClsBVo> getDmClsBVoList(String storId, String langTypCd) throws SQLException{
		return getDownClsList(storId, "ROOT", langTypCd, false, null);
	}

	/** 하위 분류 트리 조회 */
	public List<DmClsBVo> getDownClsList(String storId, String clsId, String langTypCd, boolean first, String useYn) throws SQLException{
		Map<Integer, DmClsBVo> clsMap = getClsMap(langTypCd, storId);
		Map<Integer, List<String>> clsSubListMap = getClsSubListMap(langTypCd, storId);
		List<DmClsBVo> returnList = new ArrayList<DmClsBVo>();
		setDownClsList(clsId, returnList, clsSubListMap, clsMap, first, useYn);
		return returnList;
	}

	/** 하위 분류 정보를 returnList 에 담음 */
	private void setDownClsList(String clsId, List<DmClsBVo> returnList, 
			Map<Integer, List<String>> clsSubListMap, Map<Integer, DmClsBVo> clsMap, boolean first, String useYn){
		int hashId = Hash.hashId(clsId);
		DmClsBVo dmClsBVo = clsMap.get(hashId);
		if(dmClsBVo!=null){
			if(first) returnList.add(dmClsBVo);
			List<String> clsSubList = clsSubListMap.get(hashId);
			if(clsSubList!=null){
				for(String subClsId : clsSubList){
					setDownClsList(subClsId, returnList, clsSubListMap, clsMap, true, useYn);
				}
			}
		}
	}

	/** 분류 맵 리턴(캐쉬) */
	private Map<Integer, DmClsBVo> getClsMap(String langTypCd, String storId) throws SQLException{
		@SuppressWarnings("unchecked")
		Map<Integer, DmClsBVo> clsMap = (Map<Integer, DmClsBVo>)
				ptCacheSvc.getCache(DmConstant.CLS, langTypCd, "MAP"+storId, 10);
		if(clsMap!=null) return clsMap;
		
		clsMap = new HashMap<Integer, DmClsBVo>();
		Map<Integer, List<String>> subIdListMap = new HashMap<Integer, List<String>>();
		loadClsMap(langTypCd, clsMap, subIdListMap, storId);
		ptCacheSvc.setCache(DmConstant.CLS, langTypCd, "MAP"+storId,  clsMap);
		ptCacheSvc.setCache(DmConstant.CLS, langTypCd, "SUB_ID_LIST_MAP"+storId,  subIdListMap);
		return clsMap;
	}
	
	/** 서브 분류 목록 맵 리턴(캐쉬) */
	private Map<Integer, List<String>> getClsSubListMap(String langTypCd, String storId) throws SQLException{
		@SuppressWarnings("unchecked")
		Map<Integer, List<String>> subIdListMap = (Map<Integer, List<String>>)
				ptCacheSvc.getCache(DmConstant.CLS, langTypCd, "SUB_ID_LIST_MAP"+storId, 10);
		if(subIdListMap!=null) return subIdListMap;
		
		Map<Integer, DmClsBVo> clsMap = new HashMap<Integer, DmClsBVo>();
		subIdListMap = new HashMap<Integer, List<String>>();
		loadClsMap(langTypCd, clsMap, subIdListMap, storId);
		ptCacheSvc.setCache(DmConstant.CLS, langTypCd, "MAP"+storId,  clsMap);
		ptCacheSvc.setCache(DmConstant.CLS, langTypCd, "SUB_ID_LIST_MAP"+storId,  subIdListMap);
		return subIdListMap;
	}

	/** 분류 맵, 서브 분류 목록 맵 조회 */
	private void loadClsMap(String langTypCd, 
			Map<Integer, DmClsBVo> clsMap, Map<Integer, List<String>> subIdListMap, String storId) throws SQLException{
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		
		List<DmClsBVo> dmClsBVoList = new ArrayList<DmClsBVo>();
		// 최상위 분류 삽입
		dmClsBVoList.add(makeDmClsVo(storId, "ROOT", messageProperties.getMessage("dm.cols.cls", request), DmConstant.CLS, "F", 1, "Y"));
		List<DmClsBVo> allList = getDmClsAllList(langTypCd, storId, "Y");
		// 등록된 전체 분류체계
		if(allList != null && allList.size()>0) dmClsBVoList.addAll(allList);
				
		DmClsBVo dmClsBVo;
		String clsPid=null, clsId;
		List<String> idList = null;
		int i, size = dmClsBVoList==null ? 0 : dmClsBVoList.size();
		for(i=0;i<size;i++){
			dmClsBVo = dmClsBVoList.get(i);
			//분류ID
			clsId = dmClsBVo.getClsId();
			// 분류상위ID
			clsPid = dmClsBVo.getClsPid();
			if(!subIdListMap.containsKey(Hash.hashId(clsPid)))
				subIdListMap.put(Hash.hashId(clsPid), new ArrayList<String>());
			idList = subIdListMap.get(Hash.hashId(clsPid));
			idList.add(dmClsBVo.getClsId());
			if(!dmClsBVo.getClsId().equals(clsPid))
				clsMap.put(Hash.hashId(clsId), dmClsBVo);
		}
	}

	/** 전체 분류 조회 */
	@SuppressWarnings("unchecked")
	public List<DmClsBVo> getDmClsAllList(String langTypCd, String storId, String useYn) throws SQLException{
		// 분류 목록 조회[저장소별]
		DmClsBVo dmClsBVo = new DmClsBVo();
		dmClsBVo.setStorId(storId);
		dmClsBVo.setQueryLang(langTypCd);
		if(useYn != null && !useYn.isEmpty()) dmClsBVo.setUseYn(useYn);
		
		return (List<DmClsBVo>)commonDao.queryList(dmClsBVo);
	}
	
	/** 최상위 분류 생성 */
	public DmClsBVo makeDmClsVo(String storId, String clsId, String clsNm, String clsPid, String fldTypCd, Integer sortOrdr, String useYn){
		DmClsBVo dmClsBVo = new DmClsBVo();
		dmClsBVo.setStorId(storId);
		dmClsBVo.setClsId(clsId);
		dmClsBVo.setClsNm(clsNm);//분류명
		dmClsBVo.setRescId("none");
		dmClsBVo.setClsPid(clsPid);
		dmClsBVo.setSortOrdr(sortOrdr);
		dmClsBVo.setUseYn(useYn);
		return dmClsBVo;
	}
	
	/** 조직도를 폴더로 변환 */
	public List<DmFldBVo> getOrgFldList(String langTypCd, String storId, String useYn) throws SQLException{
		// 전체 트리 
		List<DmFldBVo> dmFldBVoList = new ArrayList<DmFldBVo>();
		
		//기본 폴더 생성
		setTopFldList(dmFldBVoList, storId, DmConstant.FLD_COMP, DmConstant.FLD_DEPT);
		DmStorBVo dmStorBVo = new DmStorBVo();
		dmStorBVo.setQueryLang(langTypCd);
		dmStorBVo.setStorId(storId);
		if(useYn != null) dmStorBVo.setUseYn(useYn);
		
		dmStorBVo = (DmStorBVo)commonDao.queryVo(dmStorBVo);
		
		// 회사ID
		String compId = dmStorBVo.getCompId();
		
		String fldPid, orgTypCd;
		PtCompBVo ptCompBVo = ptCmSvc.getPtCompBVo(compId, langTypCd);
		dmFldBVoList.add(makeDmFldVo(storId, compId, DmConstant.FLD_COMP, ptCompBVo.getRescNm(), DmConstant.FLD_COMP, "C", 1, "Y"));
		// 조직도 트리 조회
		List<OrOrgBVo> orOrgBVoList = orCmSvc.getOrgTreeList(compId, null, langTypCd);
		for(OrOrgBVo storedOrOrgBVo : orOrgBVoList){
			fldPid = "ROOT".equals(storedOrOrgBVo.getOrgPid()) ? DmConstant.FLD_DEPT : storedOrOrgBVo.getOrgPid();
			orgTypCd = storedOrOrgBVo.getOrgTypCd();
			dmFldBVoList.add(makeDmFldVo(storId, storedOrOrgBVo.getOrgId(), DmConstant.FLD_DEPT, storedOrOrgBVo.getRescNm(), fldPid, orgTypCd, Integer.parseInt(storedOrOrgBVo.getSortOrdr()), storedOrOrgBVo.getUseYn()));
		}
			
		return dmFldBVoList;
	}
	
}
