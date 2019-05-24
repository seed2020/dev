package com.innobiz.orange.web.dm.svc;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.innobiz.orange.web.bb.svc.BbBrdSvc;
import com.innobiz.orange.web.bb.svc.BbBullSvc;
import com.innobiz.orange.web.bb.svc.BbCmSvc;
import com.innobiz.orange.web.bb.vo.BaBrdBVo;
import com.innobiz.orange.web.bb.vo.BbBullLVo;
import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.utils.ArrayUtil;
import com.innobiz.orange.web.cm.utils.Hash;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.ParamUtil;
import com.innobiz.orange.web.cm.utils.SessionUtil;
import com.innobiz.orange.web.cm.utils.StringUtil;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.CommonFileVo;
import com.innobiz.orange.web.cm.vo.CommonVo;
import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.dm.utils.DmConstant;
import com.innobiz.orange.web.dm.vo.DmAuthDVo;
import com.innobiz.orange.web.dm.vo.DmBumkBVo;
import com.innobiz.orange.web.dm.vo.DmBumkLVo;
import com.innobiz.orange.web.dm.vo.DmCatBVo;
import com.innobiz.orange.web.dm.vo.DmCatDispDVo;
import com.innobiz.orange.web.dm.vo.DmClsBVo;
import com.innobiz.orange.web.dm.vo.DmClsRVo;
import com.innobiz.orange.web.dm.vo.DmCommFileDVo;
import com.innobiz.orange.web.dm.vo.DmDocDVo;
import com.innobiz.orange.web.dm.vo.DmDocLVo;
import com.innobiz.orange.web.dm.vo.DmDocVerLVo;
import com.innobiz.orange.web.dm.vo.DmFileDVo;
import com.innobiz.orange.web.dm.vo.DmFldBVo;
import com.innobiz.orange.web.dm.vo.DmItemBVo;
import com.innobiz.orange.web.dm.vo.DmItemDispDVo;
import com.innobiz.orange.web.dm.vo.DmKwdLVo;
import com.innobiz.orange.web.dm.vo.DmPubDocTgtDVo;
import com.innobiz.orange.web.dm.vo.DmStorBVo;
import com.innobiz.orange.web.dm.vo.DmSubmLVo;
import com.innobiz.orange.web.dm.vo.DmTakovrBVo;
import com.innobiz.orange.web.dm.vo.DmTmpSaveLVo;
import com.innobiz.orange.web.em.svc.EmSrchSvc;
import com.innobiz.orange.web.em.svc.EmailSvc;
import com.innobiz.orange.web.em.vo.CmSrchBVo;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtCmSvc;
import com.innobiz.orange.web.pt.svc.PtSecuSvc;
import com.innobiz.orange.web.pt.svc.PtSysSvc;
import com.innobiz.orange.web.pt.vo.PtCdBVo;
import com.innobiz.orange.web.pt.vo.PtCompBVo;
import com.innobiz.orange.web.pt.vo.PtMnuDVo;

@Service
public class DmDocSvc {
	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(DmDocSvc.class);
	
	/** 메세지 */
	@Autowired
    private MessageProperties messageProperties;
	
	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;
	
	/** 저장소 서비스 */
	@Resource(name = "dmStorSvc")
	private DmStorSvc dmStorSvc;
	
	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
	/** 포털 보안 서비스 */
	@Autowired
	private PtSecuSvc ptSecuSvc;
	
	/** 검색 서비스 */
	@Autowired
	private EmSrchSvc emSrchSvc;
	
	/** 공통 서비스 */
	@Autowired
	private DmCmSvc dmCmSvc;
	
	/** 관리 서비스 */
	@Resource(name = "dmAdmSvc")
	private DmAdmSvc dmAdmSvc;
	
	/** 문서번호 서비스 */
	@Resource(name = "dmDocNoSvc")
	private DmDocNoSvc dmDocNoSvc;
	
	/** 파일 서비스 */
	@Resource(name = "dmFileSvc")
	private DmFileSvc dmFileSvc;
	
	/** 게시판관리 서비스 */
	@Resource(name = "bbBrdSvc")
	private BbBrdSvc bbBrdSvc;
	
	/** 게시물 서비스 */
	@Resource(name = "bbBullSvc")
	private BbBullSvc bbBullSvc;
	
	/** 게시판 공통 서비스 */
	@Autowired
	private BbCmSvc bbCmSvc;
	
	/** 포탈 공통 서비스 */
	@Autowired
	private PtCmSvc ptCmSvc;
	
	/** 작업이력 서비스 */
	@Resource(name = "dmTaskSvc")
	private DmTaskSvc dmTaskSvc;
	
	/** 이메일 서비스 */
	@Resource(name = "emEmailSvc")
	private EmailSvc emailSvc;
	
	/** 전체 테이블명 얻기 */
	public String getFullTblNm(String tblNm) {
		return DmConstant.TBl_NM_PREFIX + tblNm + DmConstant.TBl_NM_SUFFIX;
	}
	
	/** 테이블명 얻기[회사ID+테이블명] */
	public String getTblNm(String tblNm, String compId) {
		return compId +"_"+tblNm;
	}
	
	/** 테이블 VO 생성 */
	/*public DmDocLVo newDmTblLVo(DmStorBVo dmStorBVo) throws SQLException {
		// 문서(DM_X000X_L) 테이블 - SELECT
		DmDocLVo dmDocLVo = new DmDocLVo();
		dmDocLVo.setTableName(getFullTblNm(dmStorBVo.getTblNm(),dmStorBVo.getCompId()));
		dmDocLVo.setStorId(dmStorBVo.getStorId());
		return dmDocLVo;
	}*/
	
	/** 문서 VO 생성[저장소ID] */
	public DmDocLVo newDmDocLVo(String storId) throws SQLException {
		if(storId == null) return newDmDocLVo();
		DmStorBVo dmStorBVo = new DmStorBVo();
		dmStorBVo.setStorId(storId);
		dmStorBVo = (DmStorBVo)commonDao.queryVo(dmStorBVo);
		return newDmDocLVo(dmStorBVo);
	}
	
	/** 문서 VO 생성[공통] */
	public DmDocLVo newDmDocLVo() throws SQLException {
		// 문서(DM_X000X_L) 테이블 - SELECT
		DmDocLVo dmDocLVo = new DmDocLVo();
		//추가항목		
		setDmAddItem(dmDocLVo);
		dmDocLVo.setWithSubYn("N");
		dmDocLVo.setOrderSubYn("N");
		return dmDocLVo;
	}
	
	/** 추가항목 목록 조회 */
	public List<String> getDmAddItemList() throws SQLException {
		//기본 추가항목 조회
		List<DmItemBVo> addItemList = dmStorSvc.getAddItemList(null, new ArrayList<DmItemBVo>());
		List<String> addItemNmList = new ArrayList<String>();
		for(DmItemBVo storedDmItemBVo : addItemList){
			addItemNmList.add(storedDmItemBVo.getItemNm());
		}
		return addItemNmList;
	}
	
	/** 추가항목 세팅 */
	public void setDmAddItem(DmDocLVo dmDocLVo) throws SQLException {
		//기본 추가항목 조회
		List<DmItemBVo> addItemList = dmStorSvc.getAddItemList(null, new ArrayList<DmItemBVo>());
		List<String> addItemNmList = new ArrayList<String>();
		for(DmItemBVo storedDmItemBVo : addItemList){
			addItemNmList.add(storedDmItemBVo.getItemNm());
		}
		dmDocLVo.setAddItemNmList(addItemNmList);
	}
	
	/** 문서 VO 생성 */
	public DmDocLVo newDmDocLVo(DmStorBVo dmStorBVo) throws SQLException {
		// 문서(DM_X000X_L) 테이블 - SELECT
		DmDocLVo dmDocLVo = new DmDocLVo();
		// 테이블명 설정[저장소가 없을경우 개인문서테이블명[NULL] 세팅]
		// dmDocLVo.setTableName(DmConstant.TBl_NM_PREFIX + DmConstant.PSN_TBl_NM_SUFFIX + DmConstant.TBl_NM_SUFFIX);
		if(dmStorBVo != null) {
			dmDocLVo.setTableName(dmStorBVo.getTblNm());
			// 저장소ID 설정
			dmDocLVo.setStorId(dmStorBVo.getStorId());
		}
		
		//추가항목
		setDmAddItem(dmDocLVo);
		
		// lob 데이터 조회 여부
		//if(dmStorBVo.isWithLob()) dmDocLVo.setWithLob(true);
		
		return dmDocLVo;
	}
	
	/** 문서 VO 조회 */
	/*public DmDocLVo getDmDocLVo(String langTypCd, String storId, String docId) throws SQLException {
		DmStorBVo dmStorBVo = new DmStorBVo();
		dmStorBVo.setStorId(storId);
		dmStorBVo = (DmStorBVo)commonDao.queryVo(dmStorBVo);
		return getDmDocLVo(langTypCd, dmStorBVo, docId);
	}*/
	
	/** 문서 VO 조회 [내용없음] */
	public DmDocLVo getDmDocLVo(String langTypCd, DmStorBVo dmStorBVo, String docId, String docGrpId) throws SQLException {
		return getDmDocLVo(langTypCd, dmStorBVo, docId, docGrpId, false);
	}
	
	/** 문서 VO 조회 [내용조회] */
	public DmDocLVo getDmDocLVo(String langTypCd, DmStorBVo dmStorBVo, String docId, String docGrpId, boolean withLob) throws SQLException {
		// 문서(DM_X000X_L) 테이블 - SELECT
		DmDocLVo dmDocLVo = newDmDocLVo(dmStorBVo);
		//VoUtil.bind(request, dmDocLVo);
		dmDocLVo.setQueryLang(langTypCd);
		if((docId == null || docId.isEmpty()) && (docGrpId == null || docGrpId.isEmpty())) return null;
		if(docId != null && !docId.isEmpty() && docGrpId != null && !docGrpId.isEmpty()) docGrpId = null;
		if(docId != null && !docId.isEmpty()) dmDocLVo.setDocId(docId);
		if(docGrpId != null && !docGrpId.isEmpty()) {
			dmDocLVo.setDocGrpId(docGrpId);
			dmDocLVo.setDftYn("Y");
		}
		if(withLob) dmDocLVo.setWithLob(withLob);
		dmDocLVo = (DmDocLVo) commonDao.queryVo(dmDocLVo);
		if(dmDocLVo == null) return null;
		//if(storId != null && !storId.isEmpty()) dmDocLVo.setStorId(storId);
		return dmDocLVo;
	}
	
	/** 문서 VO 조회 [내용조회] */
	public DmDocLVo getDmDocLVo(DmDocLVo dmDocLVo, String langTypCd, String docId, String docGrpId, boolean withLob) throws SQLException {
		dmDocLVo.setQueryLang(langTypCd);
		if((docId == null || docId.isEmpty()) && (docGrpId == null || docGrpId.isEmpty())) return null;
		if(docId != null && !docId.isEmpty() && docGrpId != null && !docGrpId.isEmpty()) docGrpId = null;
		if(docId != null && !docId.isEmpty()) dmDocLVo.setDocId(docId);
		if(docGrpId != null && !docGrpId.isEmpty()) {
			dmDocLVo.setDocGrpId(docGrpId);
			dmDocLVo.setDftYn("Y");
		}
		if(withLob) dmDocLVo.setWithLob(withLob);
		if(commonDao.count(dmDocLVo)>1) {
			LOGGER.error("[ERROR] getDmDocLVo is many count!!");
			return null;
		}
		dmDocLVo = (DmDocLVo) commonDao.queryVo(dmDocLVo);
		if(dmDocLVo == null) return null;
		//if(storId != null && !storId.isEmpty()) dmDocLVo.setStorId(storId);
		return dmDocLVo;
	}
	
	/** 문서상세 VO 조회 */
	public DmDocDVo getDmDocDVo(String langTypCd, String tableName, String docGrpId) throws SQLException {
		DmDocDVo dmDocDVo = new DmDocDVo();
		dmDocDVo.setTableName(tableName);
		dmDocDVo.setDocGrpId(docGrpId);
		dmDocDVo = (DmDocDVo) commonDao.queryVo(dmDocDVo);
		return dmDocDVo;
	}
	
	/** 문서 VO 조회 [내용없음] *//*
	public DmDocLVo getDmDocLVo(String langTypCd, DmStorBVo dmStorBVo, String docId) throws SQLException {
		return getDmDocLVo(langTypCd, dmStorBVo, docId, false);
	}*/
	
	/** 문서 VO 조회 [내용조회] */
	/*public DmDocLVo getDmDocLVo(String langTypCd, DmStorBVo dmStorBVo, String docId, boolean withLob) throws SQLException {
		// 문서(DM_X000X_L) 테이블 - SELECT
		DmDocLVo dmDocLVo = newDmDocLVo(dmStorBVo);
		//VoUtil.bind(request, dmDocLVo);
		dmDocLVo.setQueryLang(langTypCd);
		dmDocLVo.setDocId(docId);
		if(withLob) dmDocLVo.setWithLob(withLob);
		dmDocLVo = (DmDocLVo) commonDao.queryVo(dmDocLVo);
		if(dmDocLVo == null) return null;
		return dmDocLVo;
	}*/
	
	/** 보존연한도래여부 세팅 */
	public void setDocKeepYn(DmDocLVo storedDmDocLVo, String keepDdlnYmd, String tableName) throws SQLException{
		DmDocDVo dmDocDVo = new DmDocDVo();
		//if(storId != null) dmDocDVo.setStorId(storId);
		if(tableName != null) dmDocDVo.setTableName(tableName);
		dmDocDVo.setDocGrpId(storedDmDocLVo.getDocGrpId());
		dmDocDVo = (DmDocDVo)commonDao.queryVo(dmDocDVo);
		if(dmDocDVo == null || dmDocDVo.getKeepPrdDt() == null || keepDdlnYmd == null) return;
		String keepPrdDt = StringUtil.toShortDate(dmDocDVo.getKeepPrdDt()).replaceAll("[-: ]", "");
		if(Integer.parseInt(keepPrdDt) < Integer.parseInt(keepDdlnYmd.replaceAll("[-: ]", "")))
			storedDmDocLVo.setKeepDdlnYn("Y");
	}
	
	/** 하위문서 목록을 맵으로 변환 */
	public void setSubDocMapList(List<DmDocLVo> subDmDocLVoList, List<Map<String, Object>> mapList){
		for(DmDocLVo subDmDocLVo : subDmDocLVoList){
			mapList.add(VoUtil.toMap(subDmDocLVo, null));
		}
	}
	
	/** 목록을 맵으로 변환 */
	//@SuppressWarnings("unchecked")
	public void setDocMapList(HttpServletRequest request, List<DmDocLVo> dmDocLVoList, String storId, 
			List<Map<String, Object>> mapList, UserVo userVo, String tableName, String suffix) throws SQLException, CmException{
		String toDate = commonDao.querySysdate(new CommonVoImpl("YYYY-MM-DD"));
		// 환경설정
		Map<String,String>	envConfigMap = dmAdmSvc.getEnvConfigMap(null, userVo.getCompId());
		// 보존기한
		String keepDdln = envConfigMap.get("keepDdln");
		String keepDdlnYmd = getCmYmdCd(toDate, keepDdln, "p");
		
		// 하위포함 여부
		//String withSubYn = ParamUtil.getRequestParam(request, "withSubYn", false);
				
		/*DmDocLVo subDmDocLVo = null;
		List<Map<String, Object>> subMapList = null;*/
		
		
		/*if(withSubYn != null && !withSubYn.isEmpty() && "Y".equals(withSubYn)){
			subDmDocLVo = newDmDocLVo(storId);
			subDmDocLVo.setDftYn("Y");// 기본여부
			subDmDocLVo.setSubYn("Y");// 하위여부
			subDmDocLVo.setOrderBy(" D.SUB_DOC_GRP_ID DESC, D.SORT_ORDR ASC");
			subMapList = new ArrayList<Map<String, Object>>();
		}*/
		
		DmDocLVo subDmDocLVo = new DmDocLVo();
		subDmDocLVo.setTableName(tableName);
		// 관련문서 건수 조회여부
		boolean isSubCnt = suffix.startsWith("Doc") || suffix.startsWith("TransferDoc");
		
		Map<String,String> authMap=null;
		// 관리자가 아닐 경우 문서권한 체크
		boolean isAdmin=request.getRequestURI().startsWith("/dm/adm/");
		for(DmDocLVo storedDmDocLVo : dmDocLVoList){
			// 폴더가 미분류일 경우에 '미분류'로 세팅한다.
			if(storedDmDocLVo.getFldId() != null && DmConstant.EMPTY_CLS.equals(storedDmDocLVo.getFldId()))
				storedDmDocLVo.setFldNm(messageProperties.getMessage("dm.cols.emptyCls", request));
			if(isAdmin){
				// 첨부파일(DM_FILE_D) 테이블 - SELECT COUNT
				storedDmDocLVo.setFileCnt(dmFileSvc.getFileVoListCnt(storedDmDocLVo.getDocId(), tableName).intValue());
			}else{
				try{
					authMap = getAuthMap(null, isAdmin, storedDmDocLVo, userVo, storId, "ko", "/viewDoc", false);
					if(authMap.containsKey("download")){
						// 첨부파일(DM_FILE_D) 테이블 - SELECT COUNT
						storedDmDocLVo.setFileCnt(dmFileSvc.getFileVoListCnt(storedDmDocLVo.getDocId(), tableName).intValue());
					}else{
						storedDmDocLVo.setFileCnt(0);
					}
				}catch(Exception e){
					storedDmDocLVo.setFileCnt(0);
				}
			}
			
			// 분류체계 조회
			setDmClsBVoList(request, null, storId, new ArrayList<DmClsBVo>(), storedDmDocLVo, storedDmDocLVo.getDocGrpId(), "clsNm", tableName, userVo.getLangTypCd());
			
			// 보존기한이 '영구'이면 패스
			if(storedDmDocLVo.getDocKeepPrdCd() != null && !"endless".equals(storedDmDocLVo.getDocKeepPrdCd()))
				setDocKeepYn(storedDmDocLVo, keepDdlnYmd, tableName); // 보존연한도래여부 세팅
			
			// 관련문서 건수
			if(isSubCnt && storedDmDocLVo.getStatCd() != null && "C".equals(storedDmDocLVo.getStatCd())) storedDmDocLVo.setSubDocCnt(getSubDocCnt(storedDmDocLVo, subDmDocLVo));
			
			// 조회여부
			storedDmDocLVo.setReadYn(dmTaskSvc.isTaskChk(tableName, storedDmDocLVo.getDocGrpId(), userVo.getUserUid(), "view", null)?"Y":"N");
			// 하위문서 조회
			/*if(subDmDocLVo != null){
				subDmDocLVo.setSubDocGrpId(storedDmDocLVo.getDocGrpId());
				if(commonDao.count(subDmDocLVo)>0)
					setSubDocMapList((List<DmDocLVo>)commonDao.queryList(subDmDocLVo), subMapList);
			}
			// 하위문서맵 세팅
			if(subMapList != null) {
				storedDmDocLVo.setSubDocMapList(subMapList);
				subMapList = new ArrayList<Map<String, Object>>();
			}*/
			mapList.add(VoUtil.toMap(storedDmDocLVo, null));
		}
	}
	
	/** 추가항목[기본] 정렬 - TEXTAREA를 우선으로 정렬*/
	public void setItemDispSort(List<DmItemDispDVo> itemDispList){
		// 순서변경
		Comparator<DmItemDispDVo> comp = new Comparator<DmItemDispDVo>(){
			String itemTyp = "TEXTAREA";
			@Override
			public int compare(DmItemDispDVo o1, DmItemDispDVo o2) {
				return (itemTyp.equals(o1.getColmVo().getItemTyp()) ? 1 : 0) > (itemTyp.equals(o2.getColmVo().getItemTyp()) ? 1 : 0) ? -1 : 0;
			}
		};
		// TEXTAREA를 우선순위로 정렬
		Collections.sort(itemDispList , comp);
	}
	
	/** 항목 표시여부 목록[조건에 맞는 항목 정보는 추출한다.] */
	public List<DmItemDispDVo> getDmItemDispDList(List<DmItemDispDVo> dispList,String addItemYn) {
		List<DmItemDispDVo> returnList = new ArrayList<DmItemDispDVo>();
		for (DmItemDispDVo storedDmItemDispDVo : dispList) {
			if(addItemYn != null && addItemYn.equals(storedDmItemDispDVo.getColmVo().getAddItemYn())){
				returnList.add(storedDmItemDispDVo);
			}
		}
		//정렬
		if(addItemYn != null && "Y".equals(addItemYn)){
			setItemDispSort(returnList);
		}
		return returnList;
	}
	
	/** 항목 표시여부 맵 */
	public Map<String,DmItemDispDVo> getDmItemDispDMap(List<DmItemDispDVo> dispList) {
		Map<String, DmItemDispDVo> returnMap = new HashMap<String, DmItemDispDVo>();
		for (DmItemDispDVo storedDmItemDispDVo : dispList) {
			String atrbId = storedDmItemDispDVo.getAtrbId();
			if (!returnMap.containsKey(atrbId)) returnMap.put(atrbId, storedDmItemDispDVo);
		}
		return returnMap;
	}
	
	/** 폴더유형 항목표시 목록 조회 */
	public List<DmItemDispDVo> getDmCatItemDispList(HttpServletRequest request, DmItemDispDVo dmItemDispDVo, String storId, String catId, String dispCol, String[] exDftList, String[] exAddList) throws SQLException{
		List<DmCatDispDVo> catDispList = dmAdmSvc.getDmCatDispDVoList(request, storId, catId, true, "Y", dispCol, null);
		if(catDispList== null || catDispList.size() == 0) return null;
		List<DmItemDispDVo> dispList = new ArrayList<DmItemDispDVo>();
		for(DmCatDispDVo storedDmCatDispDVo : catDispList){
			// 기본항목 비교
			if(exDftList != null && exDftList.length>0 )
				if("N".equals(storedDmCatDispDVo.getAddItemYn()) && !ArrayUtil.isInArray(exDftList, storedDmCatDispDVo.getAtrbId())) continue;
			if(dispCol == null && "Y".equals(storedDmCatDispDVo.getAddItemYn())) continue;
			// 추가항목 비교
			if(exAddList != null && exAddList.length>0 )
				if("Y".equals(storedDmCatDispDVo.getAddItemYn()) && !ArrayUtil.isInArray(exAddList, storedDmCatDispDVo.getAtrbId())) continue;
			dmItemDispDVo = new DmItemDispDVo();
			BeanUtils.copyProperties(storedDmCatDispDVo, dmItemDispDVo);
			dispList.add(dmItemDispDVo);
		}
		return dispList;
	}
	
	/** 항목 표시여부 목록 [회사,부서] */
	public List<DmItemDispDVo> getDmItemDispDList(HttpServletRequest request, String storId, String fldId, 
			String lstTyp, String compId, String dispCol, boolean isPsn) throws SQLException, CmException{
		List<DmItemDispDVo> dispList = null;
		DmItemDispDVo dmItemDispDVo = null;
		boolean fldChk = lstTyp == null || "F".equals(lstTyp) ? true : false;
		// 항목표시 조회
		if(fldChk && fldId != null && !fldId.isEmpty()){//폴더ID가 조회조건에 있을경우
			//유형ID
			String catId = null;
			// 폴더가 미분류 이면 기본유형을 조회한다.
			if(DmConstant.EMPTY_CLS.equals(fldId)){
				//기본유형 조회
				DmCatBVo dmCatBVo = getDmFldCatBVo("ko", storId, null, "Y");
				if(dmCatBVo != null) catId = dmCatBVo.getCatId();
			}else{
				DmFldBVo dmFldBVo = new DmFldBVo(isPsn ? null : storId);
				dmFldBVo.setFldId(fldId);
				dmFldBVo = (DmFldBVo)commonDao.queryVo(dmFldBVo);
				if(dmFldBVo != null ) catId = dmFldBVo.getCatId();
			}
			
			// 해당 폴더의 유형을 조회하고 그에 속한 항목 정보를 조회한다.
			if(catId != null) {
				dispList = getDmCatItemDispList(request, dmItemDispDVo, storId, catId, dispCol, null, null);
				dmItemDispDVo = null;
			}
		}
		//폴더 정보가 없을 경우 유형을 알 수 없으므로 기본항목을 조회하여 목록에 보여준다.
		if(dispList == null || dispList.size() == 0){
			if("B".equals(lstTyp) || "F".equals(lstTyp)) lstTyp = "L";
			// 기본 항목 조회
			dispList = dmAdmSvc.getItemDispList(request, true, "Y", dispCol, lstTyp, compId);
		}
		
		return dispList;
	}
	
	/** 정렬순서 적용 */
	public void setDmSortWhere(DmItemDispDVo dmItemDispDVo, List<DmItemDispDVo> dispList, DmDocLVo dmDocLVo ){
		if(dmDocLVo.getOrderBy() != null && !dmDocLVo.getOrderBy().isEmpty()) return;
		
		/** 하위정렬여부가 'Y' 일 경우에는 기본정렬 조건을 무시하고 계층형으로 정렬한다. */
		if(dmDocLVo.getOrderSubYn() != null && "Y".equals(dmDocLVo.getOrderSubYn())){
			dmDocLVo.setOrderBy(" SUB_DOC_GRP_ID DESC, SORT_ORDR ASC");
			return;
		}
		// 정렬순서용 설정 찾기
		for(DmItemDispDVo storedDmItemDispDVo: dispList){
			if(storedDmItemDispDVo.getDataSortVa() != null && !storedDmItemDispDVo.getDataSortVa().isEmpty()){
				dmItemDispDVo = storedDmItemDispDVo;
				break;
			}
		}
		// 정렬순서 세팅
		if(dmItemDispDVo!=null){
			String atrbId = dmItemDispDVo.getAtrbId();//속성ID
			// 항목구분[폴더,분류,텍스트,코드,에디터,달력UID]
			//String itemTyp = dmItemDispDVo.getColmVo().getItemTyp();
			String itemTypVa = dmItemDispDVo.getColmVo().getItemTypVa();
			String sortOptVa = dmItemDispDVo.getSortOptVa();//정렬옵션값 - code:코드 테이블의 정렬순서, name:텍스트순
			String dataSortVa = dmItemDispDVo.getDataSortVa();//데이터정렬값 - asc:내림차순, desc:올림차순
			dataSortVa = dataSortVa==null||dataSortVa.isEmpty() ? "ASC" : dataSortVa.toUpperCase();
			String column = StringUtil.fromCamelNotation(atrbId);
			if("code".equals(sortOptVa)){
				dmDocLVo.setDispOrdr("(SELECT SORT_ORDR FROM DM_CD_D WHERE CD_GRP_ID ='"+itemTypVa+"' AND CD=T."+column+")");
				dmDocLVo.setOrderBy("DISP_ORDR "+dataSortVa);
			}else if("cd".equals(sortOptVa)){
				dmDocLVo.setDispOrdr("(SELECT SORT_ORDR FROM PT_CD_B WHERE CLS_CD ='"+itemTypVa+"' AND CD=T."+column+")");
				dmDocLVo.setOrderBy("DISP_ORDR "+dataSortVa);
			}else {
				dmDocLVo.setOrderBy(column+" "+(dataSortVa==null||dataSortVa.isEmpty() ? "ASC" : dataSortVa.toUpperCase()));
			}
		}
	}
	
	/** 정렬순서 적용 - 백업*/
	/*public void setDmSortWhere(DmItemDispDVo dmItemDispDVo, List<DmItemDispDVo> dispList, DmDocLVo dmDocLVo ){
		// 정렬순서용 설정 찾기
		for(DmItemDispDVo storedDmItemDispDVo: dispList){
			if(storedDmItemDispDVo.getDataSortVa() != null && !storedDmItemDispDVo.getDataSortVa().isEmpty()){
				dmItemDispDVo = storedDmItemDispDVo;
				break;
			}
		}
		// 정렬순서 세팅
		if(dmItemDispDVo!=null){
			String atrbId = dmItemDispDVo.getAtrbId();//속성ID
			String itemTypVa = dmItemDispDVo.getColmVo().getItemTypVa();
			String sortOptVa = dmItemDispDVo.getSortOptVa();//정렬옵션값 - code:코드 테이블의 정렬순서, name:텍스트순
			String dataSortVa = dmItemDispDVo.getDataSortVa();//데이터정렬값 - asc:내림차순, desc:올림차순
			dataSortVa = dataSortVa==null||dataSortVa.isEmpty() ? "ASC" : dataSortVa.toUpperCase();
			if("code".equals(sortOptVa)){
				String column = StringUtil.fromCamelNotation(atrbId);
				dmDocLVo.setDispOrdr("(SELECT SORT_ORDR FROM DM_CD_D WHERE CD_GRP_ID ='"+itemTypVa+"' AND CD=T."+column+")");
				dmDocLVo.setOrderBy("DISP_ORDR "+dataSortVa);
			}else if("cd".equals(sortOptVa)){
				String column = StringUtil.fromCamelNotation(atrbId);
				dmDocLVo.setDispOrdr("(SELECT SORT_ORDR FROM PT_CD_B WHERE CLS_CD ='"+itemTypVa+"' AND CD=T."+column+")");
				dmDocLVo.setOrderBy("DISP_ORDR "+dataSortVa);
			}else {
				String column = StringUtil.fromCamelNotation(atrbId);
				dmDocLVo.setOrderBy(column+" "+(dataSortVa==null||dataSortVa.isEmpty() ? "ASC" : dataSortVa.toUpperCase()));
			}
		}
	}*/
	
	/** 제외할 컬럼을 제외한 목록 세팅 */
	public void setExDispList(List<DmItemDispDVo> dispList, List<String> exDispList){
		List<DmItemDispDVo> exVoList = new ArrayList<DmItemDispDVo>();
		String[] exAtrbList = exDispList.toArray(new String[exDispList.size()]);
		for(DmItemDispDVo dispVo : dispList){
			if(ArrayUtil.isInArray(exAtrbList, dispVo.getAtrbId())){
				exVoList.add(dispVo);
			}
		}
		if(exVoList.size()>0){
			for(DmItemDispDVo exVo : exVoList){
				dispList.remove(exVo);
			}
		}
	}
	
	/** 리스트 환경 설정 - 에 따른 정렬순서 세팅 [회사,부서]*/
	public void setListQueryOptions(HttpServletRequest request, String langTypCd, DmDocLVo dmDocLVo, 
			String lstTyp, String compId, String orgId, ModelMap model, List<String> exDispList, List<String> inDispList, boolean isAdmin) throws SQLException, CmException{
		DmItemDispDVo dmItemDispDVo = null;
		// 조회 조건 Parameter
		// 폴더ID
		String fldId = dmDocLVo.getFldId();
		
		//String fldId = ParamUtil.getRequestParam(request, "fldId", false);
		
		// 테이블명
		String tableName = dmDocLVo.getTableName();
		
		//저장소ID
		String storId = dmDocLVo.getStorId();
		
		// 항목 정보
		List<DmItemDispDVo> dispList = getDmItemDispDList(request, storId, fldId, lstTyp, compId, "list", false);
		
		//정렬순서 세팅
		setDmSortWhere(dmItemDispDVo, dispList, dmDocLVo);
		
		// 폴더조회여부 - 소유문서에서는 false
		boolean isFldSrch = dmDocLVo.isFldSrch();
		
		// 폴더 조건 검색
		List<DmFldBVo> fldList = null;
		if("F".equals(lstTyp) && fldId != null && !fldId.isEmpty()){
			// 미분류가 아닐 경우에만 하위폴더 조회
			if(!DmConstant.EMPTY_CLS.equals(fldId.toUpperCase())){
				fldList = dmAdmSvc.getDownTreeList(storId, fldId, langTypCd, true, "F","Y");
			}
			if(DmConstant.EMPTY_CLS.equals(fldId.toUpperCase()) || fldList==null || fldList.size()==0){
				isFldSrch = false;
				fldList = null;
			}else{
				dmDocLVo.setFldId(null);
			}
		}
		// 폴더조회여부가 true일 경우에만 사용자의 폴더 조회
		// 폴더 목록이 없을 경우 사용자의 폴더 전체를 조회한다.
		if(isFldSrch && (fldList == null || fldList.isEmpty())){
			String path = request.getRequestURI();
			path = path.substring(path.lastIndexOf("/")+1);
			path = path.substring(0,path.lastIndexOf("."));
			String suffix = dmCmSvc.getPathSuffix(path);
			String fldGrpId = request.getRequestURI().startsWith("/dm/doc/") && (suffix.startsWith("TransTgt") || suffix.startsWith("TransWait") || suffix.startsWith("Takovr")) ? DmConstant.FLD_DEPT : null;
			// 전체 조회 여부
			boolean allChk = request.getRequestURI().startsWith("/dm/adm/") && (fldGrpId == null || fldGrpId.isEmpty());
			if(!allChk && fldGrpId != null && !fldGrpId.isEmpty() ){
				if(DmConstant.FLD_DEPT.equals(fldGrpId)) compId = null;
				if(DmConstant.FLD_COMP.equals(fldGrpId)) orgId = null;
			}
			fldList = dmAdmSvc.getDmFldBVoList(storId, langTypCd, compId, orgId, allChk, fldGrpId == null || fldGrpId.isEmpty());
		}
		// 폴더 목록 조회 조건에 추가
		if(fldList != null) dmDocLVo.setDmFldBVoList(fldList);
		
		// 분류체계 목록 생성
		List<DmClsBVo> dmClsBVoList = new ArrayList<DmClsBVo>();
		// 분류체계 ID 가 있을 경우 조회조건에 추가한다.
		setDmClsBVoList(request, model, storId, dmClsBVoList, null, null, null, tableName, langTypCd);
		if(dmClsBVoList.size()>0){
			setSrchDmClsBVoList(storId, dmClsBVoList, dmDocLVo);
		}
		
		// 키워드 가 있을 경우 조회조건에 추가한다.
		setDmKwdLVoList(request, null, dmDocLVo, null, tableName);
		
		// 제외할 컬럼 목록[심의,임시]
		if(exDispList != null && exDispList.size()>0){
			setExDispList(dispList, exDispList);
		}
		// 전체 항목 목록 조회
		List<DmItemDispDVo> srchDispList = dmAdmSvc.getItemDispList(request, true, "Y", null, "L", compId);
		// 추가할 컬럼 목록[인수인계,이관]
		if(inDispList != null && inDispList.size()>0){
			String[] inAtrbList = inDispList.toArray(new String[inDispList.size()]);
			dmAdmSvc.setAddDispVoList(request, langTypCd, dispList, "Y", "list", inAtrbList);
			dmAdmSvc.setAddDispVoList(request, langTypCd, srchDispList, "Y", "list", inAtrbList);
			//setInDispList(dispList, inDispList);
		}
		if(model != null) model.put("itemDispList", dispList);
		// 검색항목 재정의
		if(model != null) model.put("srchDispList", getSrchDispDList(dispList, lstTyp, null, isAdmin));
		
		// 항목 맵 조회
		Map<String,DmItemDispDVo> dispListMap = getDmItemDispDMap(srchDispList);
		// 날짜 조회조건 세팅
		setDmSrchYmd(request, dispListMap, dmDocLVo, model);
	}
	
	/** 개인폴더 유형 목록 조회 */
	public List<DmItemDispDVo> getPsnItemDispList(HttpServletRequest request, String langTypCd, 
			String dispCol, boolean isRoot, String storId, String fldId, String[] dispAtrbList) throws SQLException, CmException{
		String catId = null;
		// 루트폴더이거나 해당 유형이 없으면 기본유형을 조회한다.
		DmCatBVo dmCatBVo = null;
		if(isRoot){
			//기본유형 조회
			dmCatBVo = getDmFldCatBVo(langTypCd, storId, null, "Y");
			if(dmCatBVo != null)
				catId = dmCatBVo.getCatId();
		}else{
			// 개인폴더에 등록되어 있는 유형ID를 조회
			DmFldBVo dmFldBVo = getDmFldBVo(null, fldId, langTypCd);
			if(dmFldBVo != null) {
				// 폴더유형이 없을경우 기본유형을 조회한다.
				dmCatBVo = getDmFldCatBVo(langTypCd, storId, dmFldBVo.getCatId(), null);
				if(dmCatBVo == null){
					dmCatBVo = getDmFldCatBVo(langTypCd, storId, null, "Y");
					if(dmCatBVo != null)
						catId = dmCatBVo.getCatId();
				}else
					catId = dmFldBVo.getCatId();
			}
		}
		
		// 유형ID가 없을경우 오류 처리
		if(catId == null || catId.isEmpty()){
			LOGGER.error("[ERROR] catId is null!!");
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}
		
		// 개인문서 기본항목 추출한 결과 목록 Vo List
		//List<DmItemDispDVo> dispList = getDmCatItemDispList(request, dmItemDispDVo, storId, catId, "list", DmConstant.PSN_ITEM_DISP_ATRBS, null);
		return getDmCatItemDispList(request, new DmItemDispDVo(), storId, catId, dispCol, dispAtrbList != null ? dispAtrbList : DmConstant.PSN_ITEM_DISP_ATRBS, null);
	}
	
	/** 리스트 환경 설정 - 에 따른 정렬순서 세팅 [개인]*/
	public void setPsnListQueryOptions(HttpServletRequest request, String langTypCd, 
			String storId, DmDocLVo dmDocLVo, String lstTyp, String userUid, String compId, ModelMap model, boolean isAdmin) throws SQLException, CmException{
		DmItemDispDVo dmItemDispDVo = null;
		// 폴더ID
		String fldId = ParamUtil.getRequestParam(request, "fldId", false);
		
		if(fldId == null || fldId.isEmpty()) fldId = "ROOT";
		
		boolean isRoot = fldId != null && !fldId.isEmpty() && "ROOT".equals(fldId);
		
		List<DmItemDispDVo> dispList = getPsnItemDispList(request, langTypCd, null, isRoot, storId, fldId, null);
		
		// 관리자 일 경우 등록자 조회조건을 추가한다.
		/*if(isAdmin){
			List<DmItemDispDVo> dmItemDispDVoList = new ArrayList<DmItemDispDVo>();
			dmAdmSvc.setDftDispVoList(request, langTypCd, dmItemDispDVoList, null, null, new String[]{"regrUid"});
			if(dmItemDispDVoList.size()>0) dispList.addAll(dmItemDispDVoList);
		}*/
				
		if(model != null) model.put("itemDispList", dispList);
		
		// 검색항목 재정의
		if(model != null) model.put("srchDispList", getSrchDispDList(dispList, "F", new String[]{"regrUid","modrUid"}, isAdmin));
		
		//정렬순서 세팅
		setDmSortWhere(dmItemDispDVo, dispList, dmDocLVo);
		
		// 폴더 조건 검색
		List<DmFldBVo> fldList = null;
		if(isRoot){
			fldList = getDmFldBVoList(langTypCd, null, null, userUid, null);
		}
		if(fldList != null && fldList.size()>0){
			dmDocLVo.setFldId(null);
			// 폴더 목록 조회 조건에 추가
			dmDocLVo.setDmFldBVoList(fldList);
		}
		
		// 항목 맵 조회
		Map<String,DmItemDispDVo> dispListMap = getDmItemDispDMap(dispList);
		// 날짜 조회조건 세팅
		setDmSrchYmd(request, dispListMap, dmDocLVo, model);
		
		// 회사ID
		dmDocLVo.setCompId(compId);
				
	}
	
	/** 문서 메뉴별 조회조건 세팅 */
	public void setQueryUrlOptions(HttpServletRequest request, ModelMap model, DmDocLVo dmDocLVo, String suffix, 
			UserVo userVo, List<String> exDispList, List<String> inDispList, boolean isAdmin) throws SQLException, CmException{
		// 보존기한
		//String keepDdln = envConfigMap.get("keepDdln");
		//dmDocLVo.setKeepDdlnDt(getCmYmdCd(startDay, keepDdln, "m"));
		
		//String suffix = dmCmSvc.getPathSuffix(path);
		if(suffix.startsWith("Psn")){// 개인문서
			dmDocLVo.setRegrUid(userVo.getUserUid());
		}else{ // 공유문서
			// 조회 초기조건 맵
			Map<String,String> initSrchMap = new HashMap<String,String>();
			
			// 환경설정
			Map<String,String>	envConfigMap = dmAdmSvc.getEnvConfigMap(null, userVo.getCompId());
			
			String startDay = commonDao.querySysdate(new CommonVoImpl("YYYY-MM-DD"));
			
			// 최신문서기한
			String newDdlnDt = getCmYmdCd(startDay, envConfigMap.get("newDdln"), "m");
			dmDocLVo.setNewDdlnDt(newDdlnDt);
			
			// 최신문서
			if(suffix.startsWith("New")){
				dmDocLVo.setNewYn("Y");
				dmDocLVo.setStatCd("C");
			}else if(suffix.startsWith("Own")){// 소유문서
				dmDocLVo.setOwnrUid(userVo.getUserUid());
				dmDocLVo.setStatCd("C");
				if(exDispList != null) Collections.addAll(exDispList, new String[]{"ownrNm"});
				dmDocLVo.setFldSrch(false); // 폴더조회 하지 않고 소유자의 문서만 조회
			}else if(suffix.startsWith("Kprd")){// 보존연한문서
				dmDocLVo.setKeepDdlnYn("Y");
				dmDocLVo.setStatCd("C");
			}else if(suffix.startsWith("Recycle")){// 휴지통
				dmDocLVo.setStatCd("D");
			}else if(suffix.startsWith("Tmp")){// 임시저장
				dmDocLVo.setStatCd("T");
				//dmDocLVo.setDmFldBVoList(null); // 폴더목록 초기화
				dmDocLVo.setRegrUid(userVo.getUserUid());
				if(exDispList != null) Collections.addAll(exDispList, new String[]{"docNo","verVa","cmplDt","modrNm","modDt","regrNm"});
				dmDocLVo.setFldSrch(false); // 폴더조회 하지않음
			}else if(suffix.startsWith("Disc")){// 문서심의
				dmDocLVo.setDiscYn("Y");
				dmDocLVo.setDiscrUid(userVo.getUserUid());
				dmDocLVo.setCompId(userVo.getCompId());
				if(exDispList != null) Collections.addAll(exDispList, new String[]{"docNo","verVa","cmplDt","modrNm","modDt"});
				if(inDispList != null) Collections.addAll(inDispList, new String[]{"discStatCd"});
				dmDocLVo.setFldSrch(false); // 폴더조회 하지않음
				// 초기 대기상태로 조회
				if(dmDocLVo.getDiscStatCd() == null) dmDocLVo.setDiscStatCd("S");
				initSrchMap.put("discStatCd", dmDocLVo.getDiscStatCd());
			}else if(suffix.startsWith("Subm")){// 문서상신
				dmDocLVo.setDiscYn("Y");
				dmDocLVo.setSubmrUid(userVo.getUserUid());
				dmDocLVo.setCompId(userVo.getCompId());
				if(exDispList != null) Collections.addAll(exDispList, new String[]{"docNo","verVa","cmplDt","modrNm","modDt","regrNm"});
				if(inDispList != null) Collections.addAll(inDispList, new String[]{"discStatCd"}); 
				dmDocLVo.setFldSrch(false); // 폴더조회 하지않음
				// 초기 대기상태로 조회
				//if(dmDocLVo.getDiscStatCd() == null) dmDocLVo.setDiscStatCd("S");
				initSrchMap.put("discStatCd", dmDocLVo.getDiscStatCd());
			}else if(suffix.startsWith("Bumk")){// 즐겨찾기
				DmBumkBVo dmBumkBVo = new DmBumkBVo();
				dmBumkBVo.setBumkCat("P");
				dmBumkBVo.setRegrUid(userVo.getUserUid());
				dmDocLVo.setDmBumkBVo(dmBumkBVo);
				dmDocLVo.setStatCd("C");
			}else if(suffix.startsWith("TransTgt")){// 인계대기문서, 인수대기
				if(isAdmin) model.put("global", "Y"); // 전체회사 조회여부
				dmDocLVo.setStatCd("C");
			}else if(suffix.startsWith("TransWait") || suffix.startsWith("Takovr")){// 인계대기문서, 인수대기
				DmTakovrBVo dmTakovrBVo = new DmTakovrBVo();
				dmDocLVo.setStatCd("O"); // 인계,인수 대기상태
				if(suffix.startsWith("TransWait")) {// 인계대기문서
					if(isAdmin){
						dmTakovrBVo.setTakOrgId(userVo.getCompId());
						model.put("global", "Y"); // 전체회사 조회여부
					}else dmTakovrBVo.setTakOrgId(userVo.getOrgId());
					if(inDispList != null) Collections.addAll(inDispList, new String[]{"tgtOrgId","takStatCd","takRegDt"});
				}
				if(suffix.startsWith("Takovr")) {// 인수대기
					dmDocLVo.setFldSrch(false); // 폴더 조회 여부
					if(inDispList != null) Collections.addAll(inDispList, new String[]{"takOrgId","takRegDt"});
					
					if(isAdmin){
						dmTakovrBVo.setTgtOrgId(userVo.getCompId());
					}else{
						dmTakovrBVo.setTgtOrgId(userVo.getOrgId());
					}
				}
				dmDocLVo.setDmTakovrBVo(dmTakovrBVo);
			}/*else if(suffix.startsWith("TransferDtl")){// 이관문서상세
				// 이관ID
				String tranId = ParamUtil.getRequestParam(request, "tranId", true);
				// 문서이관목록
				DmDocTranHVo dmDocTranHVo = new DmDocTranHVo();
				dmDocTranHVo.setTranId(tranId);
				dmDocTranHVo = (DmDocTranHVo)commonDao.queryVo(dmDocTranHVo);
				if(dmDocTranHVo == null){
					LOGGER.error("[ERROR] dmDocTranHVo == null");
					// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
					throw new CmException("pt.msg.nodata.passed", request);
				}
				dmDocLVo.setStatCd("M"); // 이관대기상태
				dmDocLVo.setTranId(tranId);
				dmDocLVo.setTgtStorId(dmDocTranHVo.getTgtStorId());
				if(inDispList != null) Collections.addAll(inDispList, new String[]{"errCont"}); 
				// get 파라미터를 post 파라미터로 전달하기 위해
				try{
					model.put("paramTransferEntryList", ParamUtil.getEntryMapList(request));
				}catch(UnsupportedEncodingException uee){
					uee.printStackTrace();
				}
			}*/else if(suffix.startsWith("OpenReq") || suffix.startsWith("OpenApv")){// 문서열람요청,승인함
				dmDocLVo.setFldSrch(false); // 폴더 조회 여부
				// 열람요청상태코드[S:요청,A:승인,R:반려]
				String viewReqStatCd = ParamUtil.getRequestParam(request, "viewReqStatCd", false);
				// 문서열람대상
				DmPubDocTgtDVo dmPubDocTgtDVo = new DmPubDocTgtDVo();
				if(suffix.startsWith("OpenReq")){
					dmPubDocTgtDVo.setSrchUserUid(userVo.getUserUid());
					dmPubDocTgtDVo.setSrchOrgId(userVo.getOrgId());
					dmPubDocTgtDVo.setRegrUid(userVo.getUserUid());
					if(inDispList != null) Collections.addAll(inDispList, new String[]{"viewReqStatCd","readDt","rjtOpin"});
					if(exDispList != null) Collections.addAll(exDispList, new String[]{"docKeepPrdNm","cmplDt","modrNm","modDt","verVa","fldNm","readCnt"});
				}else{
					if(viewReqStatCd == null || viewReqStatCd.isEmpty()) viewReqStatCd = "S"; // 승인
					if(inDispList != null) Collections.addAll(inDispList, new String[]{"tgtId","tgtTypCd","viewReqStatCd","readDt","reqUserUid","rjtOpin"});
					if(exDispList != null) Collections.addAll(exDispList, new String[]{"docKeepPrdNm","cmplDt","regrNm","regDt","modrNm","modDt","verVa","fldNm","clsNm","seculNm","readCnt"});
				}
				if(viewReqStatCd != null && !viewReqStatCd.isEmpty()) dmPubDocTgtDVo.setStatCd(viewReqStatCd);
				initSrchMap.put("viewReqStatCd", dmPubDocTgtDVo.getStatCd());
				dmDocLVo.setStatCd("C");
				dmDocLVo.setDmPubDocTgtDVo(dmPubDocTgtDVo);
			}else{// 문서조회
				dmDocLVo.setStatCd("C");
			}
			
			if(model != null && initSrchMap != null && initSrchMap.size()>0) model.put("initSrchMap", initSrchMap);
			// 관리자가 아닌 경우 권한적용 여부를 체크하여 목록조회조건에 적용한다.
			if(!isAdmin){
				// 조회옵션
				String listOpt = envConfigMap.get("listOpt");
				// 조회옵션을 설정하지 않았거나 '권한적용' 으로 설정한 경우 - 권한을 적용할 url의 suffix 를 체크한다.
				if(listOpt != null && !listOpt.isEmpty() && "B".equals(listOpt) && ArrayUtil.isStartsWithArray(DmConstant.AUTH_URL_SUFFIX , suffix)){
					// 사용자의 보안등급코드
					String seculCd = userVo.getSeculCd();
					if(seculCd == null || seculCd.isEmpty()) {
						dmDocLVo.setSeculCdList(null);
						dmDocLVo.setSeculCd(DmConstant.EMPTY_CLS.toLowerCase());
					}else{
						// 포탈 보안등급코드 조회
						List<PtCdBVo> ptCdBVoList = getPtSeculCdList(userVo.getCompId(), DmConstant.SECUL_CD);
						
						if(ptCdBVoList == null || ptCdBVoList.size() == 0){
							dmDocLVo.setSeculCdList(null);
							dmDocLVo.setSeculCd(seculCd);
						}else{
							// 조회조건에 담을 보안등급 목록
							List<String> seculCdList = getSeculCdDownList(ptCdBVoList, seculCd);
							if(seculCdList != null && seculCdList.size()>0) {
								seculCdList.add(DmConstant.EMPTY_CLS.toLowerCase());
								dmDocLVo.setSeculCdList(seculCdList);
							}else{
								dmDocLVo.setSeculCdList(null);
								dmDocLVo.setSeculCd(seculCd);
							}
						}			
					}
							
					// 권한적용여부
					dmDocLVo.setListSrchAuth(true);
					// 조회할 사용자UID
					dmDocLVo.setOwnrUid(userVo.getUserUid());
					dmDocLVo.setSrchUserUid(userVo.getUserUid());
				}
			}
			
			// 이관문서조회
			/*if(suffix.startsWith("TransferDoc")){
				if(model != null) model.put("storList", getStorList(userVo.getLangTypCd(), userVo.getCompId(), true));
			}*/
			
		}
		// 문서 상태 설정
		/*if(dmDocLVo.getStatCd() == null || dmDocLVo.getStatCd().isEmpty()){
			dmDocLVo.setStatCd("C");
		}*/
		
		//기본여부
		if(dmDocLVo.getDftYn() == null || dmDocLVo.getDftYn().isEmpty()){
			dmDocLVo.setDftYn("Y");
		}
		
		// 하위포함 여부
		if(dmDocLVo.getWithSubYn() != null && "N".equals(dmDocLVo.getWithSubYn()))
			dmDocLVo.setSubYn("N");
		
		
		//if(dmDocLVo.getWithSubYn() == null || dmDocLVo.getWithSubYn().isEmpty())
			//dmDocLVo.setSubYn("N");
		//하위여부
		/*if(dmDocLVo.getSubYn() == null || dmDocLVo.getSubYn().isEmpty()){
			dmDocLVo.setSubYn("N");
		}*/
			
	}
	
	/** 문서 메뉴별 상세정보 권한 검증 - 비정상 접근을 막기 위함 */
	public boolean viewChkUrlOptions(HttpServletRequest request, ModelMap model, String docGrpId, UserVo userVo, String path) throws SQLException, Exception{
		String suffix = dmCmSvc.getPathSuffix(path);
		if(suffix.startsWith("OpenReq")){// 열람조회요청
			String today = commonDao.querySysdate(new CommonVoImpl("YYYY-MM-DD"));
			// 현재날짜 기준 열람조회요청건이 있는지 체크
			DmPubDocTgtDVo dmPubDocTgtDVo = new DmPubDocTgtDVo();
			dmPubDocTgtDVo.setDocGrpId(docGrpId);
			dmPubDocTgtDVo.setDurCat("readDt");
			dmPubDocTgtDVo.setDurStrtDt(today);
			dmPubDocTgtDVo.setDurEndDt(today);
			dmPubDocTgtDVo.setStatCd("A"); // 승인
			// 사용자 조회
			dmPubDocTgtDVo.setTgtTypCd("U");
			dmPubDocTgtDVo.setTgtId(userVo.getUserUid());
			int cnt = commonDao.count(dmPubDocTgtDVo); 
			
			if(cnt==0) {
				// 부서 조회
				dmPubDocTgtDVo.setTgtTypCd("D");
				dmPubDocTgtDVo.setTgtId(userVo.getOrgId());
				cnt = commonDao.count(dmPubDocTgtDVo);
			}
			
			if(cnt==0){
				// dm.msg.dtlView.not.view=문서를 열람할 수 없습니다.
				//throw new CmException("dm.msg.dtlView.not.view", request);
				if(model.containsKey("listPage")) model.put("togo", "./"+model.get("listPage")+".do?"+ParamUtil.getQueryString(request, "docId","pageNo","docGrpId","tgtId"));
				else model.put("todo", "history.go(-1);");
				
				model.put("message", messageProperties.getMessage("dm.msg.dtlView.not.view", request));
				return false;
				
			}
		}
		return true;
	}
	
	/** 포탈 보안등급코드 조회 */
	public List<PtCdBVo> getPtSeculCdList(String compId, String clsId) throws SQLException{
		List<PtCdBVo> ptCdBVoList = null;
		// 시스템 정책 조회
		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
		if(DmConstant.SECUL_CD.equals(clsId)){
			// 회사별 보안등급코드 사용
			if("Y".equals(sysPlocMap.get("seculByCompEnable"))){
				// 회사목록과 compId 가 같은 것 조회
				ptCdBVoList = ptCmSvc.getCdListEqCompId(DmConstant.SECUL_CD, "ko", compId, "Y");
			} else {
				// 이 경우 - 이곳을 호출 안함
				// 회사목록에 compId 가 포함된 것 조회
				ptCdBVoList = ptCmSvc.getCdListByCompId(DmConstant.SECUL_CD, "ko", compId, "Y");
			}
		}
		
		return ptCdBVoList;
	}
	
	/** 사용자의 보안등급포함 하위코드 조회 */
	public List<String> getSeculCdDownList(List<PtCdBVo> ptCdBVoList, String chkCd){
		// 조회조건에 담을 보안등급 목록
		List<String> seculCdList = null;
		for(PtCdBVo storedPtCdBVo : ptCdBVoList){
			if(storedPtCdBVo.getCd() == null || storedPtCdBVo.getCd().isEmpty()) continue;
			// 사용자의 보안등급 코드와 같으면 ArrayList 객체 생성
			if(storedPtCdBVo.getCd().equals(chkCd)) seculCdList = new ArrayList<String>();
			// ArrayList 가 생성되고 난 이후(사용자의 보안등급가 같은 코드) 목록에 코드를 담는다.
			if(seculCdList != null) seculCdList.add(storedPtCdBVo.getCd());
		}
		return seculCdList;
	}
	
	/** 사용자의 보안등급포함 상위코드 조회 */
	public List<String> getSeculCdUpList(List<PtCdBVo> ptCdBVoList, String chkCd){
		// 조회조건에 담을 보안등급 목록
		List<String> seculCdList = new ArrayList<String>();
		for(PtCdBVo storedPtCdBVo : ptCdBVoList){
			seculCdList.add(storedPtCdBVo.getCd());
			// 사용자의 보안등급 코드와 같으면 break
			if(storedPtCdBVo.getCd().equals(chkCd)) break;
		}
		return seculCdList;
	}
	
	/** 문서 메뉴별 추가 저장 옵션 */
	public void transUrlOptions(QueryQueue queryQueue, DmDocLVo dmDocLVo, String path, UserVo userVo) throws SQLException{
		String suffix = dmCmSvc.getPathSuffix(path);
		// 최신문서
		if(suffix.startsWith("New")){
		}else if(suffix.startsWith("Own")){// 소유문서
		}else if(suffix.startsWith("Kprd")){// 보존연한문서
		}else if(suffix.startsWith("Recycle")){// 휴지통
		}else if(suffix.startsWith("Tmp")){// 임시저장 목록 삭제
			//if(dmDocLVo.getStatCd() != null && "C".equals(dmDocLVo.getStatCd())) saveTmpSave(queryQueue, userVo.getCompId(), dmDocLVo.getDocId(), userVo.getUserUid(), "delete");
		}else if(suffix.startsWith("Disc")){// 문서심의
		}else if(suffix.startsWith("Subm")){// 문서상신
		}else{// 문서조회
			// 필수 속성 복사
			//copyDmDocLVos(queryQueue, dmDocLVo.getStorId(), dmDocLVo.getDocId(), DmConstant.SUB_COPY_ATTRS);
		}
	}
	
	/** 문서 메뉴별 상세보기 옵션 */
	public void viewUrlOptions(ModelMap model, String storId, DmDocLVo dmDocLVo, String path, UserVo userVo, boolean isAdmin) throws SQLException{
		String suffix = dmCmSvc.getPathSuffix(path);
		if(suffix.startsWith("Doc") || suffix.startsWith("TransferDoc")) model.put("subDocListYn",Boolean.TRUE);// 관련문서 여부
		else model.put("subDocListYn",Boolean.FALSE);// 관련문서 여부
		
		if(suffix.startsWith("Subm")){// 문서상신
			if("R".equals(dmDocLVo.getStatCd())){
				// 반려의견 세팅
				DmSubmLVo dmSubmLVo = new DmSubmLVo();
				dmSubmLVo.setCompId(userVo.getCompId());
				dmSubmLVo.setDocGrpId(dmDocLVo.getDocGrpId());
				model.put("dmSubmLVo", (DmSubmLVo)commonDao.queryVo(dmSubmLVo));				
				model.put("subDocListYn",Boolean.FALSE);// 관련문서 여부
			}
			model.put("naviYn",Boolean.FALSE);// 이전다음 여부
		}else if(suffix.startsWith("OpenReq") || suffix.startsWith("OpenApv") ){// 열람요청,열람승인
			model.put("naviYn",Boolean.FALSE);// 이전다음 여부
		}else{
			model.put("naviYn",Boolean.TRUE);// 이전다음 여부
		}
	}
	
	/** 이전다음, 관련문서 사용여부*/
	public boolean isUrlAddOptions(String suffix, String[] arr){
		if(ArrayUtil.isStartsWithArray(arr, suffix)) return false;
		return true;
	}
	
	/** 문서 메뉴별 탭 목록 세팅 */
	public String setTabList(ModelMap model, String path, String lstTyp) throws SQLException{
		String suffix = dmCmSvc.getPathSuffix(path);
		if(suffix.startsWith("Tmp") || suffix.startsWith("Recycle") || suffix.startsWith("Takovr") || suffix.startsWith("OpenApv")){// 임시, 휴지통, 인수문서, 열람요청승인함
			model.put("tabList", new String[][]{DmConstant.TAB_LIST[0]});
			return "L";
		}else if(suffix.startsWith("Bumk")){
			model.put("tabList", new String[][]{{"B","bumk"}});
			return "B";
		}else if(suffix.startsWith("Psn")){//개인문서[목록,폴더] 
			model.put("tabList", new String[][]{DmConstant.TAB_LIST[1],DmConstant.TAB_LIST[0]});
			return lstTyp;
		}else if(suffix.startsWith("Own") || suffix.startsWith("Subm") || suffix.startsWith("Disc") || suffix.startsWith("OpenReq")){
			model.put("tabList", new String[][]{DmConstant.TAB_LIST[0],DmConstant.TAB_LIST[2]});
			if(lstTyp == null || lstTyp.isEmpty() || (!"L".equals(lstTyp) && !"C".equals(lstTyp))) lstTyp = "L";
			return lstTyp;
		}else{// [목록,폴더,분류]
			model.put("tabList", DmConstant.TAB_LIST);
			return lstTyp;
		}
	}
	
	/** 임시저장 테이블 - 저장 또는 삭제*/
	public void saveTmpSave(QueryQueue queryQueue, String compId, String docId, String userUid, String actKey) throws SQLException{
		DmTmpSaveLVo dmTmpSaveLVo = new DmTmpSaveLVo();
		dmTmpSaveLVo.setCompId(compId);
		dmTmpSaveLVo.setDocId(docId);
		if(userUid != null) dmTmpSaveLVo.setRegrUid(userUid);
		if("delete".equals(actKey)) queryQueue.delete(dmTmpSaveLVo);
		else queryQueue.store(dmTmpSaveLVo);
		
	}
	
	/** 하위 문서 목록 맵 조회 */
	private void loadSubDocIdMap(Map<Integer, DmDocLVo> subDocMap, Map<Integer, List<String>> subIdListMap, List<DmDocLVo> dmDocLVoList) throws SQLException{
		DmDocLVo dmDocLVo;
		String docPid=null, docId;
		List<String> idList = null;
		int i, size = dmDocLVoList==null ? 0 : dmDocLVoList.size();
		for(i=0;i<size;i++){
			dmDocLVo = dmDocLVoList.get(i);
			//저장소ID
			docId = dmDocLVo.getDocId();
			docPid = dmDocLVo.getDocPid();
			if(!subIdListMap.containsKey(Hash.hashId(docPid)))
				subIdListMap.put(Hash.hashId(docPid), new ArrayList<String>());
			idList = subIdListMap.get(Hash.hashId(docPid));
			idList.add(docId);
			if(!docId.equals(docPid))
				subDocMap.put(Hash.hashId(docId), dmDocLVo);
		}
	}
	
	/** 하위 문서 정보를 returnList 에 담음 */
	private void setDownDocList(String docPid, List<String> returnList, 
			Map<Integer, List<String>> subListMap, boolean first){
		int hashId = Hash.hashId(docPid);
		if(first ) returnList.add(docPid);
		if(subListMap.containsKey(hashId)){
			List<String>subIdList = subListMap.get(hashId);
			if(subIdList!=null){
				for(String id : subIdList){
					setDownDocList(id, returnList, subListMap, true);
				}
			}
		}
	}
	
	/** 관련문서 건수 세팅 - 사용안함*/
	public Integer getSubDocCnt(DmDocLVo subDmDocLVo, String subDocGrpId, String sortOrdr, Integer sortDpth) throws SQLException{
		subDmDocLVo.setInstanceQueryId("com.innobiz.orange.web.dm.dao.DmDocLDao.countSubDmDocL");
		subDmDocLVo.setSubDocGrpId(subDocGrpId);
		subDmDocLVo.setStatCd("C");
		if(sortOrdr != null )subDmDocLVo.setSortOrdr(sortOrdr);
		if(sortDpth != null )subDmDocLVo.setSortDpth(sortDpth.intValue());
		return commonDao.queryInt(subDmDocLVo);
	}
	
	/** 관련문서 건수 세팅*/
	public Integer getSubDocCnt(DmDocLVo dmDocLVo, DmDocLVo parentVo) throws SQLException{
		setSubDocWhere(dmDocLVo, parentVo, null, dmDocLVo.getSubDocGrpId());
		return commonDao.count(parentVo);
	}
	
	/** 관련문서 조회조건 세팅 */
	public void setSubDocWhere(DmDocLVo dmDocLVo, DmDocLVo parentVo, List<DmDocLVo> subList, String grpId) throws SQLException{
		boolean isTop = isParentTop(dmDocLVo);
		if(subList != null){
			if(isTop) {
				subList.add(dmDocLVo);
				grpId = dmDocLVo.getSubDocGrpId();
			}else {
				parentVo.setDftYn("Y"); // 기본여부[버전]
				parentVo.setSubYn("N");
				parentVo.setFldId(dmDocLVo.getFldId());// 폴더ID
				parentVo.setSubDocGrpId(dmDocLVo.getSubDocGrpId());
				DmDocLVo topVo = (DmDocLVo)commonDao.queryVo(parentVo);
				if(topVo != null) subList.add(topVo);
				grpId = dmDocLVo.getSubDocGrpId();
			}
		}		
		parentVo.setFldId(dmDocLVo.getFldId());// 폴더ID
		parentVo.setSubYn("Y");// 하위여부[하위문서]
		parentVo.setDftYn("Y"); // 기본여부[버전]
		parentVo.setDocGrpId(null);
		parentVo.setSubDocGrpId(grpId);// 하위문서그룹ID
		parentVo.setStatCd(dmDocLVo.getStatCd());// 상태코드
	}
	
	/** 관련문서 조회 */
	@SuppressWarnings("unchecked")
	public List<DmDocLVo> getSubDmDocLVoList(DmDocLVo dmDocLVo, DmDocLVo parentVo) throws SQLException{
		List<DmDocLVo> subList = new ArrayList<DmDocLVo>();
		// 조회조건 세팅
		setSubDocWhere(dmDocLVo, parentVo, subList, null);
		parentVo.setOrderBy(" D.SUB_DOC_GRP_ID DESC, D.SORT_ORDR ASC");
		List<DmDocLVo> tmpList = (List<DmDocLVo>)commonDao.queryList(parentVo);
		if(tmpList != null && tmpList.size()>0)
			subList.addAll(tmpList);
		/*if(!isTop){
			Map<Integer, DmDocLVo> subDocMap = new HashMap<Integer, DmDocLVo>();
			Map<Integer, List<String>> subIdListMap = new HashMap<Integer, List<String>>();
			// 목록을 맵으로 변환
			loadSubDocIdMap(subDocMap, subIdListMap, subList);
			List<DmDocLVo> subDocList = new ArrayList<DmDocLVo>();
			
			List<String> returnList = new ArrayList<String>();
			setDownDocList(dmDocLVo.getDocId(), returnList, subIdListMap, false);
			if(returnList.size()>0){
				for(String id : returnList)
					subDocList.add(subDocMap.get(Hash.hashId(id)));
			}
			return subDocList;
		}*/
		return subList;
	}
	
	/** 관련문서 건수 조회 */
	@SuppressWarnings("unchecked")
	public Integer getSubDmDocLVoListCnt(DmDocLVo dmDocLVo, DmDocLVo parentVo) throws SQLException{
		boolean isTop = isParentTop(dmDocLVo);
		parentVo.setSubYn("Y");// 하위여부[하위문서]
		parentVo.setDftYn("Y"); // 기본여부[버전]
		parentVo.setSubDocGrpId(dmDocLVo.getSubDocGrpId());
		parentVo.setOrderBy(" D.SUB_DOC_GRP_ID DESC, D.SORT_ORDR ASC");
		List<DmDocLVo> subList = (ArrayList<DmDocLVo>)commonDao.queryList(parentVo);
		
		if(!isTop){
			Map<Integer, DmDocLVo> subDocMap = new HashMap<Integer, DmDocLVo>();
			Map<Integer, List<String>> subIdListMap = new HashMap<Integer, List<String>>();
			// 목록을 맵으로 변환
			loadSubDocIdMap(subDocMap, subIdListMap, subList);
			List<String> returnList = new ArrayList<String>();
			setDownDocList(dmDocLVo.getDocId(), returnList, subIdListMap, false);
			return returnList.size();
		}
		return subList.size();
	}
	
	/** 관련문서 정렬 및 최상위 문서 지정 [완료 및 삭제상태]*/
	public void setSubGrpList(Map<String,String> topDocMap, List<DmDocDVo> subGrpList, Map<String,DmDocDVo> subGrpListMap,
			DmDocDVo dmDocDVo, String comKey, List<DmDocDVo> addList, DmDocDVo parentVo, Integer strtSortOrdr) throws SQLException{
		
		// 문서상세 목록
		List<DmDocDVo> dmDocDVoList = new ArrayList<DmDocDVo>();
		// 복원은 대상문서 + 기존문서 이기에 db에서 관련문서를 조회한다. 
		if(dmDocDVo != null){
			// 하위문서그룹ID + '|' + 폴더ID 조합키
			String[] keys = comKey.split(DmConstant.SPRIT);
			String subDocGrpId = keys[0]; // 하위문서그룹ID
			String fldId = keys[1]; // 폴더ID
			dmDocDVo.setSubDocGrpId(subDocGrpId);
			dmDocDVo.setFldId(fldId);
			// db에서 관련문서 조회
			@SuppressWarnings("unchecked")
			List<DmDocDVo> tmpList = (List<DmDocDVo>)commonDao.queryList(dmDocDVo);
			if(tmpList != null && tmpList.size()>0) dmDocDVoList.addAll(tmpList);
		}
		
		if(addList != null && addList.size()>0) dmDocDVoList.addAll(addList);
		
		Integer sortOrdr = 0;
		//정렬 오름차순(ASC)
		Comparator<DmDocDVo> comp = new Comparator<DmDocDVo>(){
			String collectId1,collectId2;
			@Override
			public int compare(DmDocDVo o1, DmDocDVo o2) {
				collectId1 = o1.getSubDocGrpId()+""+o1.getSortOrdr();
				collectId2 = o2.getSubDocGrpId()+""+o2.getSortOrdr();
				return collectId1.compareTo(collectId2);
			}
		};
		Collections.sort(dmDocDVoList , comp);
		
		// 수정된 문서상세를 담을 맵
		Map<String,DmDocDVo> listMap = new HashMap<String,DmDocDVo>();
		int cnt = 0;
		
		//기준 하위문서그룹ID[부모문서 기준]
		String topSubDocGrpId = null;
		for(DmDocDVo storedDmDocDVo : dmDocDVoList){
			// 중복문서 제외
			if(listMap.containsKey(storedDmDocDVo.getDocGrpId())) continue;
			if(cnt == 0){
				// 부모문서가 있을경우
				if(parentVo != null){
					topSubDocGrpId = parentVo.getSubDocGrpId();
					storedDmDocDVo.setSubYn("Y"); // 하위문서로 변경
					storedDmDocDVo.setDocPid(parentVo.getDocGrpId());
					storedDmDocDVo.setSortDpth(parentVo.getSortDpth()+1); // 부모문서의 단계+1
					//sortOrdr = (Integer.parseInt(parentVo.getSortOrdr())+1)+maxSortOrdr; // 부모문서의 바로 밑으로 순서변경
					sortOrdr = strtSortOrdr; // 부모문서의 바로 밑으로 순서변경
					storedDmDocDVo.setSortOrdr(sortOrdr+"");
					storedDmDocDVo.setSubDocGrpId(topSubDocGrpId);
					topDocMap.put(topSubDocGrpId, parentVo.getSubDocGrpId());
					// 부모문서상세를 맵에 추가
					listMap.put(topSubDocGrpId, parentVo);
				}else{
					topSubDocGrpId = storedDmDocDVo.getDocGrpId();
					sortOrdr = 0;
					// 하위문서를 부모문서로 세팅
					storedDmDocDVo.setSubYn("N"); // 부모문서로 변경
					storedDmDocDVo.setDocPid("");
					storedDmDocDVo.setSortDpth(0);
					storedDmDocDVo.setSortOrdr(sortOrdr+"");
					storedDmDocDVo.setSubDocGrpId(topSubDocGrpId);
					// 최상위
					topDocMap.put(topSubDocGrpId, storedDmDocDVo.getDocGrpId());
				}
			}else{
				// 하위문서
				if(listMap.containsKey(storedDmDocDVo.getDocPid()))
					parentVo = listMap.get(storedDmDocDVo.getDocPid());
				else
					parentVo = listMap.get(topDocMap.get(topSubDocGrpId));
				storedDmDocDVo.setSortDpth(parentVo.getSortDpth()+1);
				storedDmDocDVo.setSubDocGrpId(topSubDocGrpId);
				storedDmDocDVo.setDocPid(parentVo.getDocGrpId());
				//storedDmDocDVo.setSubDocGrpId(parentVo.getSubDocGrpId());
			}
			
			storedDmDocDVo.setSortOrdr(String.valueOf(sortOrdr));
			listMap.put(storedDmDocDVo.getDocGrpId(), storedDmDocDVo);
			
			sortOrdr++;
			cnt++;
			subGrpList.add(storedDmDocDVo);
			if(subGrpListMap != null) subGrpListMap.put(storedDmDocDVo.getDocGrpId(), storedDmDocDVo);
		}
		//subGrpList.addAll(addList);
	}
	
	/** 기존문서의 정렬순서를 삽입된 문서들의 다음으로 일괄 변경 */
	public void updateMaxSortOrdr(QueryQueue queryQueue, DmDocDVo dmDocDVo) throws SQLException {
		dmDocDVo.setInstanceQueryId("com.innobiz.orange.web.dm.dao.DmDocDDao.updateMaxSortOrdr");
		queryQueue.update(dmDocDVo);
	}
	
	/** 기존문서의 정렬순서를 제거된 문서만큼 일괄 변경 - 문서이동시에만 적용 */
	public void updateMinSortOrdr(QueryQueue queryQueue, String tableName, 
			Map<String,List<DmDocDVo>> addDocVoMap) throws SQLException {
		if(addDocVoMap == null || addDocVoMap.size()==0) return;
		Iterator<Entry<String, List<DmDocDVo>>> iterator = addDocVoMap.entrySet().iterator();
		Entry<String, List<DmDocDVo>> entry;
		DmDocDVo dmDocDVo = null;
		List<DmDocDVo> list;
		String[] keys;
		while(iterator.hasNext()){
			entry = iterator.next();
			keys = entry.getKey().split(DmConstant.SPRIT);
			list = entry.getValue(); 
			for(DmDocDVo storedDmDocDVo : list){
				dmDocDVo = new DmDocDVo();
				dmDocDVo.setTableName(tableName);
				dmDocDVo.setSubDocGrpId(keys[0]); // 하위문서그룹ID
				dmDocDVo.setFldId(keys[1]); // 폴더ID
				dmDocDVo.setInstanceQueryId("com.innobiz.orange.web.dm.dao.DmDocDDao.updateMinSortOrdr");
				dmDocDVo.setSortOrdr(storedDmDocDVo.getSortOrdr());
				queryQueue.update(dmDocDVo);
			}
		}
	}
	
	/** 문서그룹의 최대 정렬순서 */
	public Integer getStrtSortOrdr(String tableName, DmDocDVo parentVo) throws SQLException {
		// 부모 문서의 하위 문서 순서
		DmDocDVo dmDocDVo = new DmDocDVo();
		dmDocDVo.setTableName(tableName);
		dmDocDVo.setSubDocGrpId(parentVo.getSubDocGrpId());
		//dmDocLVo.setSubYn("Y");
		dmDocDVo.setSortOrdr(parentVo.getSortOrdr());
		dmDocDVo.setSortDpth(parentVo.getSortDpth());
		dmDocDVo.setInstanceQueryId("com.innobiz.orange.web.dm.dao.DmDocDDao.selectMinSortOrdr");
		Integer minSortOrdr = commonDao.queryInt(dmDocDVo);
		// 부모 문서의 첫번째로 순서 변경
		if (minSortOrdr != null) return minSortOrdr;
		else {
			dmDocDVo.setInstanceQueryId("com.innobiz.orange.web.dm.dao.DmDocDDao.selectMaxSortOrdr");
			Integer maxSortOrdr = commonDao.queryInt(dmDocDVo);
			if(maxSortOrdr == null) maxSortOrdr = 0;
			return maxSortOrdr+1;
		}
	} 
	
	/** 관련문서 순서 변경 */
	public Integer setSubDocGrpSortOrdrs(DmDocDVo dmDocDVo, Map<String,List<DmDocDVo>> addDocVoMap, 
			Map<String,String> topDocMap, List<DmDocDVo> subGrpList, Map<String,DmDocDVo> subGrpListMap,
			DmDocDVo parentVo, Integer strtSortOrdr) throws SQLException{
		// 관련문서 전체 목록
		//List<DmDocDVo> subGrpList = new ArrayList<DmDocDVo>();
		Iterator<Entry<String, List<DmDocDVo>>> iterator = addDocVoMap.entrySet().iterator();
		Entry<String, List<DmDocDVo>> entry;
		int maxCnt = strtSortOrdr;
		int totalCnt = 0;
		while(iterator.hasNext()){
			entry = iterator.next();
			setSubGrpList(topDocMap, subGrpList, subGrpListMap, dmDocDVo, entry.getKey(), entry.getValue(), parentVo, maxCnt);
			// 부모문서가 있으면 시작 정렬순서를 지정한다.
			if(parentVo != null) {
				maxCnt+=(entry.getValue().size()+1);// 그룹별 정렬순서 병합
				totalCnt+=entry.getValue().size();
			}
		}
		
		// 중복제거(추후 삭제 가능)
		/*if(subGrpList.size()>0){
			// 목록을 맵에 담음
			for(DmDocDVo storedDmDocDVo : subGrpList){
				if(subGrpListMap.containsKey(storedDmDocDVo.getDocGrpId())) continue;
				subGrpListMap.put(storedDmDocDVo.getDocGrpId(), storedDmDocDVo);
			}
		}*/
		
		return totalCnt;
	}
	
	/** 순서변경 [개별] - 그룹ID*/
	public void updateSortOrdr(QueryQueue queryQueue, Map<String,String> topDocMap, String docGrpId, 
			Map<String,DmDocDVo> subGrpListMap, DmDocDVo dmDocDVo, String tableName){
		// 맵에서 문서상세 정보 조회
		DmDocDVo dmDocDVoMap = subGrpListMap.get(docGrpId);
		dmDocDVo.setDocGrpId(docGrpId);
		dmDocDVo.setSortOrdr(dmDocDVoMap.getSortOrdr());
		dmDocDVo.setSubYn(dmDocDVoMap.getSubYn());
		if("N".equals(dmDocDVoMap.getSubYn())){
			dmDocDVo.setSortDpth(dmDocDVoMap.getSortDpth());
			dmDocDVo.setDocPid(dmDocDVoMap.getDocPid());
		}else{
			if(subGrpListMap.containsKey(dmDocDVoMap.getDocPid())){
				dmDocDVo.setSortDpth(subGrpListMap.get(dmDocDVoMap.getDocPid()).getSortDpth()+1);
			}else {
				// 조합키
				String comKey = dmDocDVoMap.getSubDocGrpId()+DmConstant.SPRIT+dmDocDVoMap.getFldId();
				// 상위문서 정보가 없을 경우 최상위 문서의 하위 문서로 지정
				dmDocDVo.setDocPid(topDocMap.get(comKey));
				dmDocDVo.setSortDpth(1);
			}
		}
		// 수정
		if(queryQueue != null) queryQueue.update(dmDocDVo);
	}
	
	/** 순서변경 [다수] - 그룹ID*/
	public void updateSortOrdrs(QueryQueue queryQueue, List<DmDocDVo> subGrpList, String statCd, String tableName){
		DmDocDVo dmDocDVo = null;
		for(DmDocDVo storedDmDocDVo : subGrpList){
			//if(!subGrpListMap.containsKey(storedDmDocDVo.getDocGrpId())) continue;
			dmDocDVo = new DmDocDVo();
			dmDocDVo.setTableName(tableName);
			dmDocDVo.setDocGrpId(storedDmDocDVo.getDocGrpId());
			dmDocDVo.setSortOrdr(storedDmDocDVo.getSortOrdr());
			dmDocDVo.setSubYn(storedDmDocDVo.getSubYn());
			dmDocDVo.setSortDpth(storedDmDocDVo.getSortDpth());
			dmDocDVo.setDocPid(storedDmDocDVo.getDocPid());
			dmDocDVo.setSubDocGrpId(storedDmDocDVo.getSubDocGrpId());
			if(statCd != null) dmDocDVo.setStatCd(statCd);
			queryQueue.update(dmDocDVo);
			//updateSortOrdr(queryQueue, topDocMap, storedDmDocDVo.getDocGrpId(), subGrpListMap, dmDocDVo, tableName);
		}
	}
	
	/** 문서인수인계 - 저장*/
	public void updateTakovrDoc(HttpServletRequest request, QueryQueue queryQueue, String storId, DmDocLVo originVo, String langTypCd, 
			UserVo userVo, String tableName) throws SQLException, IOException, CmException{
		
		// 부모문서ID
		String docPid = ParamUtil.getRequestParam(request, "docPid", false);
		// 하위문서여부
		boolean isSub = docPid != null && !docPid.isEmpty() ? true : false;
					
		// 등록자UID[보내기옵션]
		String regrUid = ParamUtil.getRequestParam(request, "regrUid", false);
		if(regrUid != null && !regrUid.isEmpty()) originVo.setRegrUid(regrUid);
		
		queryQueue.update(originVo);
		
		// 보안등급
		String seculCd = ParamUtil.getRequestParam(request, "seculCd", false);
		if(seculCd != null && !seculCd.isEmpty()) originVo.setSeculCd(seculCd);
		// 보존연한
		String docKeepPrdCd = ParamUtil.getRequestParam(request, "docKeepPrdCd", false);
		if(docKeepPrdCd != null && !docKeepPrdCd.isEmpty()) originVo.setDocKeepPrdCd(docKeepPrdCd);
		// 소유자UID
		String ownrUid = ParamUtil.getRequestParam(request, "ownrUid", false);
		if(ownrUid != null && !ownrUid.isEmpty()) originVo.setOwnrUid(ownrUid);
					
		// 분류체계 조회
		setDmClsBVoList(request, null, storId, null, originVo, isSub ? docPid : null, null, tableName, langTypCd);
		if(originVo.getDmClsRVoList() == null || originVo.getDmClsRVoList().size() ==0) setDmClsBVoList(request, null, storId, null, originVo, originVo.getDocGrpId(), null, tableName, langTypCd);
		List<DmClsRVo> dmClsRVoList = originVo.getDmClsRVoList();
		// 키워드 조회
		String[] kwdList = getDmKwdLVoList(request, originVo, isSub ? docPid : null, tableName);
					
		if(dmClsRVoList != null && dmClsRVoList.size()>0) saveDmClsRVoList(queryQueue, dmClsRVoList, originVo.getDocGrpId(), tableName, true);
		if(kwdList != null && kwdList.length>0) saveDmKwdLVoList(queryQueue, kwdList, originVo.getDocGrpId(), tableName, true);
		
		originVo.setRegDt("sysdate");
		
		// 문서상세 저장[보존기한등]
		originVo.setCmplDt("sysdate");
		
		if(regrUid == null) regrUid = userVo.getUserUid();
		
		//문서번호 세팅 - 현재 사용안함. 인수시에 문서번호 재 할당 일 경우 주석 제거
		//dmDocNoSvc.setDocNo(originVo, storId, langTypCd, "C", userVo.getCompId(), originVo.getFldId(), regrUid);
		
		// 문서상세 저장
		saveDmDocD(queryQueue, originVo, isSub ? originVo.getSubDocGrpId() : null);
		// 폴더유형의 심의여부가 'Y'일 경우 상신함에 저장한다.
		//if("S".equals(originVo.getStatCd()))	 saveDisc(queryQueue, storId, originVo.getDocId(), "S", null, originVo.getDiscrUid(), userVo.getUserUid(), "insert");
					
	}
	
	/** 전체 하위문서그룹ID 조회*/
	public void setSubDocGrpMap(String tableName, Map<String,List<DmDocDVo>> subDocVoMap, 
			String[] docGrpIds, String[] docIds, String[] statCds, boolean isWithSubYn) throws SQLException{
		DmDocDVo dmDocDVo = null,tmpDmDocDVo;
		// 하위문서그룹ID + '|' + 폴더ID 조합키
		String comKey = null;
		if(docIds != null){
			List<String> docGrpList = new ArrayList<String>();
			DmDocLVo dmDocLVo = null;
			//전체 하위문서그룹ID 조회
			for(String docId : docIds){
				dmDocLVo = new DmDocLVo();
				dmDocLVo.setTableName(tableName);
				dmDocLVo.setDocId(docId);
				dmDocLVo = (DmDocLVo)commonDao.queryVo(dmDocLVo);
				if(dmDocLVo != null) {
					docGrpList.add(dmDocLVo.getDocGrpId());
				}
			}
			if(docGrpList.size()>0){
				docGrpIds = ArrayUtil.toArray(docGrpList);
			}
		}
		// 임시
		isWithSubYn = false;
		// 하위문서 포함여부
		if(isWithSubYn){
			DmDocDVo srchDmDocDVo = new DmDocDVo();
			srchDmDocDVo.setTableName(tableName);
			
			// 문서이동,복사,인수등 - 하위문서포함여부가 'Y' 면 관련문서그룹ID를 배열에 병합한다.
			// 하위문서그룹ID를 조회한다.
			docGrpIds = getSubDocIdList(srchDmDocDVo, docGrpIds, statCds);
		}
		
		//전체 하위문서그룹ID 조회
		for(String grpId : docGrpIds){
			dmDocDVo = new DmDocDVo();
			dmDocDVo.setTableName(tableName);
			dmDocDVo.setDocGrpId(grpId);
			tmpDmDocDVo = (DmDocDVo)commonDao.queryVo(dmDocDVo);
			if(tmpDmDocDVo != null) {
				comKey = tmpDmDocDVo.getSubDocGrpId() +DmConstant.SPRIT + tmpDmDocDVo.getFldId();
				if(!subDocVoMap.containsKey(comKey)) subDocVoMap.put(comKey, new ArrayList<DmDocDVo>());
				//if(subDocGrpId != null && !subDocGrpId.isEmpty()) tmpDmDocDVo.setSubDocGrpId(subDocGrpId);
				subDocVoMap.get(comKey).add(tmpDmDocDVo);
			}
		}
	}
	
	/** 분류체계 조회조건 세팅 */
	public void setSrchDmClsBVoList(String storId, List<DmClsBVo> dmClsBVoList, DmDocLVo dmDocLVo) throws SQLException{
		// 검색 시작
		//long start = System.currentTimeMillis(); 
		List<DmClsBVo> clsList = new ArrayList<DmClsBVo>(),tmpList = null;
		for(DmClsBVo storedDmClsBVo : dmClsBVoList){
			tmpList = dmAdmSvc.getDownClsList(storId, storedDmClsBVo.getClsId(), "ko", true, "Y");
			if(tmpList != null && tmpList.size()>0) clsList.addAll(tmpList);
		}
		if(clsList.size()>0){
			// 분류ID 목록
			List<String> clsIdList = new ArrayList<String>();
			for(DmClsBVo storedDmClsBVo : clsList){
				clsIdList.add(storedDmClsBVo.getClsId());
			}
			if(clsIdList.size()>0){
				// HashSet 으로 중복ID 제거
				Set<String> hs = new HashSet<String>(clsIdList);
				// String[] 문서그룹ID 배열 재정의
				String[] clsIds = ArrayUtil.toArray(new ArrayList<String>(hs));
				// 분류체계 관계 목록
				List<DmClsRVo> dmClsRVoList = new ArrayList<DmClsRVo>();
				DmClsRVo dmClsRVo = null;
				for(String clsId : clsIds){
					dmClsRVo = new DmClsRVo();
					dmClsRVo.setClsId(clsId);
					dmClsRVoList.add(dmClsRVo);
				}
				// 문서 기본에 분류체계 목록 세팅
				dmDocLVo.setDmClsRVoList(dmClsRVoList);
			}
		}
		 // 검색 종료
        //long end = System.currentTimeMillis(); 
        //System.out.println("time : "+((end-start)));
	}
	
	/** 분류체계 정보 조회[Parameter] */
	@SuppressWarnings("unchecked")
	public void setDmClsBVoList(HttpServletRequest request, ModelMap model, String storId, 
			List<DmClsBVo> dmClsBVoList, DmDocLVo dmDocLVo, String docGrpId, String setName, String tableName, String langTypCd) throws SQLException{
		// 분류체계 관계 목록
		List<DmClsRVo> dmClsRVoList = null;
		if(docGrpId != null) {
			//분류체계관계 조회
			DmClsRVo dmClsRVo = new DmClsRVo();
			if(tableName != null) dmClsRVo.setTableName(tableName);
			//dmClsRVo.setStorId(storId);
			dmClsRVo.setDocGrpId(docGrpId);
			dmClsRVoList = (List<DmClsRVo>)commonDao.queryList(dmClsRVo);
		}else {
			if(request.getRequestURI().startsWith("/cm/doc/setSendOptPop") && 
					request.getParameter("clsId")!=null && request.getParameter("clsId") instanceof String){
				String[] clsIds=request.getParameter("clsId").split(",");
				if(clsIds!=null && clsIds.length>0){
					dmClsRVoList=new ArrayList<DmClsRVo>();
					DmClsRVo dmClsRVo = null;
					for(String clsId : clsIds){
						dmClsRVo=new DmClsRVo();
						dmClsRVo.setClsId(clsId.trim());
						dmClsRVoList.add(dmClsRVo);
					}
				}
			}else{
				dmClsRVoList = 	(List<DmClsRVo>) VoUtil.bindList(request, DmClsRVo.class, new String[]{"clsId"});
			}
		}
		
		if(dmClsRVoList != null && !dmClsRVoList.isEmpty()){
			// 문서 기본에 분류체계 목록 세팅
			if(dmDocLVo != null) dmDocLVo.setDmClsRVoList(dmClsRVoList);
			
			// 분류체계 기본 목록이 있을경우 에 배열을 생성한다.
			if(dmClsBVoList != null){
				DmClsBVo dmClsBVo;
				String setNames="";
				for(DmClsRVo storedDmClsRVo : dmClsRVoList){
					//분류체계 조회
					dmClsBVo = new DmClsBVo();
					dmClsBVo.setQueryLang(langTypCd);
					dmClsBVo.setStorId(storId);
					dmClsBVo.setClsId(storedDmClsRVo.getClsId());
					dmClsBVo = (DmClsBVo)commonDao.queryVo(dmClsBVo);
					if(dmClsBVo != null){
						if(setName != null){
							setNames+= "".equals(setNames) ? dmClsBVo.getClsNm() : ", "+dmClsBVo.getClsNm();
						}else{
							dmClsBVoList.add(dmClsBVo);
						}
					}
				}
				if(dmDocLVo != null && !"".equals(setNames)){
					VoUtil.setValue(dmDocLVo, setName, setNames);
				}
				// model 객체가 null이 아니면 model 객체에 분류체계기본 목록을 세팅한다.
				if(model != null) model.put("dmClsBVoList", dmClsBVoList);
			}
		}
		
	}
	
	/** 키워드 정보 세팅[Parameter] */
	@SuppressWarnings("unchecked")
	public void setDmKwdLVoList(HttpServletRequest request, ModelMap model,
			DmDocLVo dmDocLVo, String docGrpId, String tableName) throws SQLException, CmException{
		//키워드조회
		DmKwdLVo dmKwdLVo = new DmKwdLVo();
		dmKwdLVo.setTableName(tableName);
		if(docGrpId != null) {
			dmKwdLVo.setDocGrpId(docGrpId);
			// 키워드 목록 조회
			List<DmKwdLVo> dmKwdLVoList = (List<DmKwdLVo>)commonDao.queryList(dmKwdLVo);
			if(model != null) model.put("dmKwdLVoList", dmKwdLVoList);
		}else{
			String schCat = ParamUtil.getRequestParam(request, "schCat", false);
			if(schCat != null && "kwdNm".equals(schCat)){
				if(dmDocLVo != null){
					String schWord = ParamUtil.getRequestParam(request, "schWord", false);
					if(schWord != null && !schWord.isEmpty()){
						dmDocLVo.setKwdNm(schWord);
					}
				}
				
			}
		}
	}
	
	/** 키워드 정보 조회[Db - Parameter] */
	@SuppressWarnings("unchecked")
	public String[] getDmKwdLVoList(HttpServletRequest request, DmDocLVo dmDocLVo, String docGrpId, String tableName) throws SQLException, CmException{
		//키워드조회
		DmKwdLVo dmKwdLVo = new DmKwdLVo();
		dmKwdLVo.setTableName(tableName);
		if(docGrpId != null) {
			dmKwdLVo.setDocGrpId(docGrpId);
			// 키워드 목록 조회
			List<DmKwdLVo> dmKwdLVoList = (List<DmKwdLVo>)commonDao.queryList(dmKwdLVo);
			if(dmKwdLVoList == null || dmKwdLVoList.size() == 0) return null;
			List<String> kwdList = new ArrayList<String>();
			for(DmKwdLVo storedDmKwdLVo : dmKwdLVoList){
				kwdList.add(storedDmKwdLVo.getKwdNm());
			}
			return ArrayUtil.toArray(kwdList);
		}else{
			String kwdNm = ParamUtil.getRequestParam(request, "kwdNm", false);
			if(kwdNm == null || kwdNm.isEmpty()) return null;
			return kwdNm.split(",");
		}
	}
	
	/** 날짜 조회 조건 세팅 */
	public void setDmSrchYmd(HttpServletRequest request, Map<String,DmItemDispDVo> dispListMap, DmDocLVo dmDocLVo, ModelMap model ){
		// 기본 조회조건 매핑
		DmItemDispDVo tmpDispVo;
		DmItemBVo tmpItemBVo;
		// 날짜 조회조건 - BIND
		@SuppressWarnings("unchecked")
		List<DmItemDispDVo> boundVoList = (List<DmItemDispDVo>) VoUtil.bindList(request, DmItemDispDVo.class, new String[]{"durCat"});
		
		Map<String,Map<String,String>> srchYmdMap = new HashMap<String,Map<String,String>>();
		Map<String,String> srchVoMap;
		String durStrtDt,durEndDt,addWhere="",colNm;
		boolean isDisc = dmDocLVo.getDiscYn() !=null && "Y".equals(dmDocLVo.getDiscYn()); // 상신여부
		boolean isBumk = dmDocLVo.getDmBumkBVo()!=null; // 즐겨찾기 여부
		
		for(CommonVo storedCommonVo : boundVoList){
			tmpDispVo = dispListMap.get(storedCommonVo.getDurCat());
			tmpItemBVo = tmpDispVo.getColmVo();
			if("CALENDAR".equals(tmpItemBVo.getItemTyp())){
				srchVoMap = new HashMap<String,String>();
				durStrtDt = storedCommonVo.getDurStrtDt();
				durEndDt = storedCommonVo.getDurEndDt();
				colNm = tmpItemBVo.getItemNm();
				if(isDisc && ArrayUtil.isInArray(new String[]{"REG_DT", "MOD_DT"}, colNm)) colNm="S."+colNm; // 상신문서 일경우
				else if(isBumk && ArrayUtil.isInArray(new String[]{"REG_DT", "MOD_DT"}, colNm)) colNm="T."+colNm; // 즐겨찾기 일경우
				
				if((durStrtDt == null || durStrtDt.isEmpty()) && (durEndDt == null || durEndDt.isEmpty())) continue;
				// 시작일자
				if(durStrtDt != null && !durStrtDt.isEmpty()){
					addWhere += " AND "+getQueryYmdString(colNm)+" >= '"+durStrtDt+"'";
					srchVoMap.put("durStrtDt", durStrtDt);
				}
				// 종료일자
				if(durEndDt != null && !durEndDt.isEmpty()){
					addWhere += " AND "+getQueryYmdString(colNm)+" <= '"+durEndDt+"'";
					srchVoMap.put("durEndDt", durEndDt);
				}
				// 날짜 맵에 담음
				srchYmdMap.put(tmpDispVo.getAtrbId(), srchVoMap);
			}
		}
		
		if(!"".equals(addWhere)){
			if(dmDocLVo.getWhereSqllet() != null && !"".equals(dmDocLVo.getWhereSqllet())) dmDocLVo.setWhereSqllet(dmDocLVo.getWhereSqllet()+addWhere);
			else dmDocLVo.setWhereSqllet(addWhere);
		}
		if(model != null) model.put("srchYmdMap", srchYmdMap);
	}
	
	/** 검색시 필요한 항목만 추출한다. */
	public List<DmItemDispDVo> getSrchDispDList(List<DmItemDispDVo> dispList, String lstTyp, 
			String[] excludeList, boolean isAdmin) throws SQLException, CmException{
		List<DmItemDispDVo> returnList = new ArrayList<DmItemDispDVo>();
		Map<String,String[]> excludeMap = new HashMap<String,String[]>();
		String prefix = "exclude";
		excludeMap.put(prefix+"L", new String[]{"FILE","EDITOR"});// 목록형
		excludeMap.put(prefix+"F", new String[]{"FILE","EDITOR","FLD"});// 폴더형
		excludeMap.put(prefix+"C", new String[]{"FILE","EDITOR","CLS"});// 분류형
		excludeMap.put(prefix+"B", new String[]{"FILE","EDITOR"});// 즐겨찾기
		//공통 제외 항목
		String[] cmArray = isAdmin ? new String[]{"verVa","readCnt","rjtOpin"} : new String[]{"verVa","ownrNm","readCnt","rjtOpin"}; 
		DmItemDispDVo dmItemDispDVo = null;
		String[] exArray;
		DmItemBVo colmVo; 
		for(DmItemDispDVo dispVo : dispList){
			if(excludeMap.containsKey(prefix+lstTyp)){
				if(excludeList != null && ArrayUtil.isInArray(excludeList, dispVo.getAtrbId())) continue;
				exArray = excludeMap.get(prefix+lstTyp);
				colmVo = dispVo.getColmVo();
				// 제외할 항목구분이 없으면 해당 VO 객체를 배열에 담는다.
				if(colmVo != null && !ArrayUtil.isInArray(exArray, colmVo.getItemTyp()) && !ArrayUtil.isInArray(cmArray, dispVo.getAtrbId())){
					dmItemDispDVo = new DmItemDispDVo();
					// VO 속성 복사
					BeanUtils.copyProperties(dispVo, dmItemDispDVo);
					returnList.add(dmItemDispDVo);
				}
			}
		}
		return returnList;
	}
	
	/** dbms 에 따라 날짜 타입 컬럼 변환 */
	public String getQueryYmdString(String colNm){
		if(dmStorSvc.isOracle()){
			return "TO_CHAR("+colNm+", 'YYYY-MM-DD')";
		} else if(dmStorSvc.isMysql()){
			return "DATE_FORMAT("+colNm+", '%Y-%m-%d')";
		}
		return "CONVERT(VARCHAR(10), "+colNm+", 120)";
	}
	
	/** 최대 본문 사이즈 model에 추가 */
	public void putBodySizeToModel(HttpServletRequest request, ModelMap model) throws SQLException {
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		UserVo userVo = LoginSession.getUser(request);
		
		Integer size = ptSysSvc.getBodySizeMap(langTypCd, userVo.getCompId()).get("dm");
		if(size==null) size=300;
		
		// 시스템 설정 조회 - 본문 사이즈
		Integer bodySize = size * 1024;
		model.put("bodySize", bodySize);
	}
	
	/** 폴더ID로 유형정보 조회 */
	public DmCatBVo getDmCatBVo(String storId, String fldId) throws SQLException{
		DmFldBVo dmFldBVo = new DmFldBVo(storId);
		dmFldBVo.setStorId(storId);
		dmFldBVo.setFldId(fldId);
		dmFldBVo = (DmFldBVo)commonDao.queryVo(dmFldBVo);
		if(dmFldBVo != null && dmFldBVo.getCatId() != null && !dmFldBVo.getCatId().isEmpty()){
			DmCatBVo dmCatBVo = new DmCatBVo();
			dmCatBVo.setStorId(storId);
			dmCatBVo.setCatId(dmFldBVo.getCatId());
			dmCatBVo = (DmCatBVo)commonDao.queryVo(dmCatBVo);
			return dmCatBVo;
		}
		return null;
	}
	
	/** 문서 버전 목록 추가, 수정 저장 */
	public void saveDocVer(QueryQueue queryQueue, String docGrpId, String docId, String tableName, String verVa, String actKey){
		// 버전 목록 수정 저장
		DmDocVerLVo dmDocVerLVo = new DmDocVerLVo();
		//dmDocVerLVo.setStorId(storId);
		dmDocVerLVo.setTableName(tableName);
		if(docGrpId != null) dmDocVerLVo.setDocGrpId(docGrpId);
		if(docId != null) dmDocVerLVo.setDocId(docId);
		if(verVa != null) dmDocVerLVo.setVerVa(verVa);
		if("delete".equals(actKey)) queryQueue.delete(dmDocVerLVo);
		else queryQueue.store(dmDocVerLVo);
	}
	
	/** 폴더유형중에 심의여부가 'Y' 이면 'S'[심의요청] 을 아니면 'C'[완료] 로 세팅한다.*/
	public void setDmStatCd(DmDocLVo dmDocLVo, String storId, String fldId) throws SQLException{
		// 등록 [심의가 없는 경우에 한해 상태코드를 변경한다.] 
		if(dmDocLVo.getFldId() != null && !dmDocLVo.getFldId().isEmpty()){
			// 폴더ID가 'none'(미분류) 이면 상태코드를 'C' 로 변경한다.
			if(DmConstant.EMPTY_CLS.equals(dmDocLVo.getFldId())){
				dmDocLVo.setStatCd("C");
				return;
			}
			DmCatBVo dmCatBVo = getDmCatBVo(storId, fldId);
			// 유형을 조회해서 
			if(dmCatBVo != null && dmCatBVo.getDiscYn() != null && !dmCatBVo.getDiscYn().isEmpty()){
				if("Y".equals(dmCatBVo.getDiscYn())){//심의여부가 'Y'면 상태코드 변경
					dmDocLVo.setStatCd("S");
					dmDocLVo.setDiscrUid(dmCatBVo.getDiscrUid());
					dmDocLVo.setSortOrdr("0");
					dmDocLVo.setSortDpth(0);
				}
			}
			// 상태코드가 없을 경우 완료['C'] 로 설정
			if(dmDocLVo.getStatCd() == null || dmDocLVo.getStatCd().isEmpty())
				dmDocLVo.setStatCd("C");
		}
	}
	
	/** 문서번호 세팅 */
	/*public void setDocNo(DmDocLVo dmDocLVo, UserVo userVo, String orgId) throws SQLException, CmException{
		dmDocLVo.setCmplDt("sysdate");// 문서번호 생성시 완료일시도 저장
		if(dmDocLVo.getDocNo() == null){
			// 문서번호 생성
			dmDocNoSvc.setDocNo(dmDocLVo, envConfigMap, userVo.getLangTypCd(), dmDocLVo.getStatCd(), orgId);
		}
	}*/
	
	/** 버전 목록 조회 */
	public List<DmDocVerLVo> getDocVerLVoList(String docGrpId, String docId, String orderBy, String tableName) throws SQLException{
		//버전목록 조회
		DmDocVerLVo dmDocVerLVo = new DmDocVerLVo();
		dmDocVerLVo.setTableName(tableName);
		if(docGrpId != null) dmDocVerLVo.setDocGrpId(docGrpId);
		if(docId != null) dmDocVerLVo.setDocId(docId);
		if(orderBy != null) dmDocVerLVo.setOrderBy(orderBy);
		@SuppressWarnings("unchecked")
		List<DmDocVerLVo> dmDocVerLVoList = (List<DmDocVerLVo>)commonDao.queryList(dmDocVerLVo);
		return dmDocVerLVoList;
	}
	
	/** 문서 상세 삭제 */
	public void delDocDtl(QueryQueue queryQueue, String docGrpId, String tableName) throws SQLException{
		if(docGrpId == null) return;
		DmDocDVo dmDocDVo = new DmDocDVo();
		dmDocDVo.setTableName(tableName);
		dmDocDVo.setDocGrpId(docGrpId);
		queryQueue.delete(dmDocDVo);
	}
	
	/** 문서 부분 수정 [삭제,복원,이관,인수인계등]*//*
	public void updateDoc(HttpServletRequest request, QueryQueue queryQueue, DmDocLVo dmDocLVo, 
			String storId, String docGrpId, String docId, String userUid) throws SQLException{
		VoUtil.bind(request, dmDocLVo);
		queryQueue.update(dmDocLVo);
	}
	*/
	
	/** 하위문서그룹ID 조회 */
	@SuppressWarnings("unchecked")
	public List<String> getSubDocId(DmDocDVo srchDmDocDVo, List<String> docGrpList, 
			DmDocDVo storedDmDocDVo, String grpId, String[] statCds) throws SQLException{
		// 하위문서 목록 VO LIST
		List<DmDocLVo> docList = null;
		// 정렬단계
		Integer sortDpth = null;
		String fldId = null;
		// 하위문서그룹ID
		String subDocGrpId = null;
		docGrpList.add(grpId); // 그룹ID를 목록에 담는다.
		srchDmDocDVo.setDocGrpId(grpId); // 하위문서그룹ID
		storedDmDocDVo = (DmDocDVo)commonDao.queryVo(srchDmDocDVo); // 삭제대상 문서정보 조회
		if(storedDmDocDVo == null) return null;
		sortDpth = storedDmDocDVo.getSortDpth(); // 정렬단계 세팅
		subDocGrpId = storedDmDocDVo.getSubDocGrpId(); // 하위문서그룹ID 세팅
		fldId = storedDmDocDVo.getFldId();// 폴더ID
		if(sortDpth == null || subDocGrpId == null || fldId == null) return null;
		storedDmDocDVo = new DmDocLVo();
		storedDmDocDVo.setTableName(srchDmDocDVo.getTableName());
		storedDmDocDVo.setSubDocGrpId(subDocGrpId);
		storedDmDocDVo.setFldId(fldId);
		
		// 하위 마지막 순서
		DmDocDVo subDmDocDVo = new DmDocDVo();
		subDmDocDVo.setTableName(srchDmDocDVo.getTableName());
		subDmDocDVo.setSubDocGrpId(subDocGrpId);
		subDmDocDVo.setFldId(fldId);
		subDmDocDVo.setSortOrdr(storedDmDocDVo.getSortOrdr());
		subDmDocDVo.setSortDpth(storedDmDocDVo.getSortDpth());
		subDmDocDVo.setInstanceQueryId("com.innobiz.orange.web.dm.dao.DmDocLDao.selectMinSortOrdr");
		Integer minSortOrdr = commonDao.queryInt(subDmDocDVo);
		String whereSqllet = "";
		if(minSortOrdr != null){
			whereSqllet = " AND SORT_ORDR >= "+storedDmDocDVo.getSortOrdr()+" AND SORT_ORDR < "+minSortOrdr+" AND SORT_DPTH > "+sortDpth.intValue();
			//storedDmDocDVo.setWhereSqllet(" AND SORT_ORDR >= "+storedDmDocDVo.getSortOrdr()+" AND SORT_ORDR < "+minSortOrdr+" AND SORT_DPTH > "+sortDpth.intValue()+" AND D.STAT_CD IN('C','R')");
		}else{
			whereSqllet = " AND SORT_ORDR <= "+storedDmDocDVo.getSortOrdr()+" AND SORT_DPTH > "+sortDpth.intValue();
			//storedDmDocDVo.setWhereSqllet(" AND SORT_ORDR <= "+storedDmDocDVo.getSortOrdr()+" AND SORT_DPTH > "+sortDpth.intValue()+" AND D.STAT_CD IN('C','R')");
		}
		
		if(statCds != null){
			String andStatCd = " AND D.STAT_CD ";
			if(statCds.length==1) andStatCd+="= ";
			else andStatCd+="IN(";
			for(int i=0;i<statCds.length;i++){
				if(i>0) andStatCd+=",";
				andStatCd+="'"+statCds[i]+"'";
			}
			if(statCds.length>1) andStatCd+=")";
			whereSqllet+=andStatCd;
		}
		storedDmDocDVo.setWhereSqllet(whereSqllet);
		
		// 대상 하위문서 조회
		docList = (List<DmDocLVo>)commonDao.queryList(storedDmDocDVo);
		if(docList==null || docList.size()==0) return null;
		// List<String> 배열에 문서그룹ID를 담는다.
		for(DmDocLVo vo : docList){
			docGrpList.add(vo.getDocGrpId());
		}
		return docGrpList;
	}
	
	/** 하위문서그룹ID 조회(다수) */
	public String[] getSubDocIdList(DmDocDVo srchDmDocDVo, String[] docGrpIds, String[] statCds) throws SQLException{
		// 대상그룹ID를 담을 목록 VO LIST
		List<String> docGrpList = new ArrayList<String>(),tmpList;
		for(String grpId : docGrpIds){						
			tmpList = getSubDocId(srchDmDocDVo, docGrpList, new DmDocDVo(), grpId, statCds);
			if(tmpList == null) continue;
			docGrpList.addAll(tmpList);
		}
		
		if(docGrpList.size()>0){
			// HashSet 으로 중복ID 제거
			Set<String> hs = new HashSet<String>(docGrpList);
			// String[] 문서그룹ID 배열 재정의
			docGrpIds = ArrayUtil.toArray(new ArrayList<String>(hs));
		}
		
		return docGrpIds;
	}
	
	/** 문서 삭제 */
	public void delDoc(HttpServletRequest request, QueryQueue queryQueue, String langTypCd, DmDocLVo dmDocLVo, DmStorBVo dmStorBVo, 
			String docGrpId, String docId, String userUid, List<String> delDocIdList , List<CommonFileVo> deletedFileList, boolean isAdmin) throws SQLException, CmException{
		// 테이블명
		String tableName = dmDocLVo.getTableName();
		
		// 저장소ID
		String storId = dmStorBVo.getStorId();
		
		// 전체삭제 여부
		if(docId != null && !docId.isEmpty()){
			dmDocLVo.setDocId(docId);
			DmDocLVo storedDmDocLVo = (DmDocLVo)commonDao.queryVo(dmDocLVo);
			if("Y".equals(storedDmDocLVo.getDftYn())){//삭제하려는 문서가 기본으로 설정되어 있을경우
				// 버전 목록 수정 저장
				DmDocVerLVo dmDocVerLVo = new DmDocVerLVo();
				dmDocVerLVo.setTableName(tableName);
				//dmDocVerLVo.setStorId(storId);
				dmDocVerLVo.setDocGrpId(storedDmDocLVo.getDocGrpId());
				if(commonDao.count(dmDocVerLVo) > 1){// 하위 버전 문서가 있을경우
					String updateSeq = "";// 기본여부를 수정할 문서ID
					dmDocVerLVo.setOrderBy("VER_VA DESC");//최신버전으로 정렬
					@SuppressWarnings("unchecked")
					List<DmDocVerLVo> dmDocVerLVoList = (List<DmDocVerLVo>)commonDao.queryList(dmDocVerLVo);
					String lastVer = null; // 마지막 버전
					for(DmDocVerLVo storedDmDocVerLVo : dmDocVerLVoList){
						if(!docId.equals(storedDmDocVerLVo.getDocId())){
							updateSeq = storedDmDocVerLVo.getDocId();
							lastVer = storedDmDocVerLVo.getVerVa();
							break;
						}
					}
					// 수정할 문서ID가 있을경우
					if(!"".equals(updateSeq)){
						DmDocLVo updateVo = new DmDocLVo();
						updateVo.setTableName(tableName);
						updateVo.setDocId(updateSeq);
						updateVo.setDftYn("Y");//하위버전을 기본여부 'Y'로 변경
						queryQueue.update(updateVo);
					}
					// 관리자여부
					boolean admUriChk = request.getRequestURI().startsWith("/dm/adm/");
					
					// 작업 이력저장[사용자]
					if(!admUriChk){
						// 작업상세 목록을 담을 배열[null or 상세배열]
						String[][] rescVals = new String[][]{{"dm.cols.verNo",lastVer}};
						dmTaskSvc.saveDmTask(queryQueue, tableName, langTypCd, storedDmDocLVo.getDocGrpId(), userUid, "verDel", dmTaskSvc.getTaskMapList(rescVals));
					}
				}else{
					docGrpId = storedDmDocLVo.getDocGrpId();
				}
			}
			delDocIdList.add(docId);
		}
		
		// 문서[버전목록포함] 삭제할 파일 목록 조회
		if(delDocIdList != null && delDocIdList.size()>0){
			for(String seq : delDocIdList){
				// 첨부파일 삭제
				deletedFileList.addAll(dmFileSvc.deleteDmFile(seq, queryQueue, tableName));
			}
		}
		// 부가정보 삭제
		if(docGrpId != null && !docGrpId.isEmpty()){
			if(!isAdmin){// 관리자 페이지가 아닐경우 하위문서를 조회한다.
				DmDocDVo dmDocDVo = new DmDocDVo();
				dmDocDVo.setTableName(tableName);
				dmDocDVo.setDocPid(docGrpId);
				dmDocDVo.setStatCd("C");
				// 하위문서가 있으면 삭제 중지
				if(commonDao.count(dmDocDVo)>0){
					// dm.msg.deleteDoc.hasSub=하위문서가 있는 문서는 삭제할 수 없습니다.
					throw new CmException("dm.msg.deleteDoc.hasSub", request);
				}
			}
			
			// 문서 상세 삭제
			delDocDtl(queryQueue, docGrpId, tableName);
			
			// 분류체계 삭제
			saveDmClsRVoList(queryQueue, null, docGrpId, tableName, true);
			
			// 키워드목록 삭제
			saveDmKwdLVoList(queryQueue, null, docGrpId, tableName, true);
			
			// 즐겨찾기 삭제
			DmBumkLVo dmBumkLVo = new DmBumkLVo();
			dmBumkLVo.setStorId(storId);
			dmBumkLVo.setRegCat("D");
			dmBumkLVo.setCatVa(docGrpId);
			queryQueue.delete(dmBumkLVo);
			
			// 작업이력 삭제
			dmTaskSvc.deleteDmTask(queryQueue, tableName, docGrpId, null);
			
			// 저장소문서관계 삭제
			dmStorSvc.saveStorId(queryQueue, storId, docGrpId, true);
			
		}
		// 버전목록 삭제
		saveDocVer(queryQueue, docGrpId, docId, tableName, null, "delete");
		
		// 임시저장 목록 삭제
		//saveTmpSave(queryQueue, dmStorBVo.getCompId(), docId, userUid, "delete");
					
		// 문서 삭제
		queryQueue.delete(dmDocLVo);
	}
	
	/** [결재] - 연계 문서 삭제 */
	public void delDocByAp(HttpServletRequest request, QueryQueue queryQueue, UserVo userVo, String apNo) throws SQLException, CmException{
		
		// 회사ID
		String compId=userVo.getCompId();
		
		// 언어코드
		String langTypCd=userVo.getLangTypCd();
		
		// 저장소 조회
		DmStorBVo dmStorBVo = dmStorSvc.getDmStorBVo(compId, langTypCd, null);
		if(dmStorBVo == null) return;
		
		// 테이블명
		String tableName = dmStorBVo.getTblNm();
		
		// 목록으로 조회해서 삭제 필요
		// 문서상세VO
		DmDocDVo dmDocDVo = new DmDocDVo();
		dmDocDVo.setTableName(tableName);
		dmDocDVo.setRefTyp("ap"); // 참조구분 : 결재
		dmDocDVo.setRefId(apNo); // 참조ID : 결재번호
		
		// 문서상세 목록 조회[결재에서 문서보내기를 여러번 할수 있으므로 목록으로 조회]
		@SuppressWarnings("unchecked")
		List<DmDocDVo> dmDocDVoList = (List<DmDocDVo>)commonDao.queryList(dmDocDVo);
		if(dmDocDVoList == null || dmDocDVoList.size()==0) return;
		
		// 문서그룹ID 목록
		List<String> docGrpIdList = new ArrayList<String>();
		
		// 중복제거를 위해 문서그룹ID 목록 세팅
		for(DmDocDVo storedDmDocDVo : dmDocDVoList) {
			docGrpIdList.add(storedDmDocDVo.getDocGrpId());
		}
		
		// HashSet 으로 중복ID 제거
		Set<String> hs = new HashSet<String>(docGrpIdList);
		docGrpIdList = new ArrayList<String>(hs);
		
		List<CommonFileVo> deletedFileList = new ArrayList<CommonFileVo>();
		// 문서 그룹ID
		for(String docGrpId : docGrpIdList) {
			// 문서 조회[기본]
			DmDocLVo dmDocLVo = getDmDocLVo(langTypCd, dmStorBVo, null, docGrpId);
			if(dmDocLVo == null) return;
			
			// 삭제할 문서ID 배열
			List<String> delDocIdList = new ArrayList<String>();
						
			// 버전 목록 조회
			List<DmDocVerLVo> dmDocVerLVoList = getDocVerLVoList(docGrpId, null, "VER_VA DESC", tableName);
			for(DmDocVerLVo storedDmDocVerLVo : dmDocVerLVoList){
				delDocIdList.add(storedDmDocVerLVo.getDocId());
			}
			
			// 문서 삭제
			delDoc(request, queryQueue, langTypCd, dmDocLVo, dmStorBVo, docGrpId, null, userVo.getUserUid(), delDocIdList, deletedFileList, false);
			/** 검색 색인 데이터를 삭제 */
			addSrchIndex(docGrpId, userVo, queryQueue, "D");
		}
		
		
		// 파일 삭제
		//if(deletedFileList.size()>0)				
		//	dmFileSvc.deleteDiskFiles(deletedFileList);
		
	}
	
	/** 코드 날짜 리턴*/
	public String getCmYmdCd(String startDay, String cd, String pmVal){
		Integer noPrd = null;
		String keepPrdDt = null;
		if(cd.endsWith("Y")){//연
			noPrd = Integer.valueOf(cd.substring(0, cd.indexOf("Y")));
			keepPrdDt = getDateOfDay(startDay, "year", pmVal, null, noPrd.intValue());
		}else if(cd.endsWith("M")){//월
			noPrd = Integer.valueOf(cd.substring(0, cd.indexOf("M")));
			keepPrdDt = getDateOfDay(startDay, "month", pmVal, null, noPrd.intValue());
		}else if(cd.endsWith("D")){//일
			noPrd = Integer.valueOf(cd.substring(0, cd.indexOf("D")));
			keepPrdDt = getDateOfDay(startDay, "day", pmVal, null, noPrd.intValue());
		}else{
			
		}
		return keepPrdDt;
	}
	
	/** 문서상세저장 */
	public void saveDmDocD(QueryQueue queryQueue, DmDocLVo dmDocLVo, String parentDocGrpId) throws SQLException{
		//if(!"C".equals(dmDocLVo.getStatCd())) return;
		DmDocDVo dmDocDVo = new DmDocDVo();
		// 하위문서 일 경우
		if(parentDocGrpId != null && !parentDocGrpId.isEmpty()){
			BeanUtils.copyProperties(dmDocLVo, dmDocDVo, new String[]{"seculCd"});
			
			DmDocDVo parentDmDocDVo = new DmDocDVo();
			parentDmDocDVo.setTableName(dmDocLVo.getTableName());
			parentDmDocDVo.setDocGrpId(parentDocGrpId);
			//parentDmDocDVo.setSubYn("N");
			parentDmDocDVo = (DmDocDVo)commonDao.queryVo(parentDmDocDVo);
			if(parentDmDocDVo != null){
				dmDocDVo.setTableName(dmDocLVo.getTableName());
				//dmDocDVo.setFldId(parentDmDocDVo.getFldId());
				//dmDocDVo.setOwnrUid(parentDmDocDVo.getOwnrUid());
				//dmDocDVo.setDocKeepPrdCd(parentDmDocDVo.getDocKeepPrdCd());
				//dmDocDVo.setKeepPrdDt(parentDmDocDVo.getKeepPrdDt());
				String seculCd = parentDmDocDVo.getSeculCd() == null || parentDmDocDVo.getSeculCd().isEmpty() ? DmConstant.EMPTY_CLS.toLowerCase() : parentDmDocDVo.getSeculCd();
				dmDocDVo.setSeculCd(seculCd);
				//BeanUtils.copyProperties(parentDmDocDVo, dmDocDVo, new String[]{"subYn","subDocGrpId","docPid","sortOrdr","sortDpth"});
			}
			// 완료일자
			if(dmDocLVo.getCmplDt() != null && !dmDocLVo.getCmplDt().isEmpty()) dmDocDVo.setCmplDt(dmDocLVo.getCmplDt());
			
		}else{
			BeanUtils.copyProperties(dmDocLVo, dmDocDVo);
		}
		String docKeepPrdCd = dmDocLVo.getDocKeepPrdCd();
		//if("C".equals(dmDocLVo.getStatCd()) && docKeepPrdCd != null){
		if(docKeepPrdCd != null && !docKeepPrdCd.isEmpty()){
			// 보존기한이 '영구'
			if("endless".equals(docKeepPrdCd)){
				dmDocDVo.setKeepPrdDt("");
			}else{
				String startDay = StringUtil.getCurrYmd();//시작일자
				if(dmDocLVo.getRegDt() != null && !"sysdate".equals(dmDocLVo.getRegDt()))// 등록일을 기준으로 일자를 계산
					startDay = StringUtil.toShortDate(dmDocLVo.getRegDt());
				String keepPrdDt = getCmYmdCd(startDay, docKeepPrdCd, "p");
				if(keepPrdDt != null){
					dmDocDVo.setKeepPrdDt(keepPrdDt);
				}
			}
		}
		queryQueue.store(dmDocDVo);
	}
	
	/** 조회수 증가 */
	public void addReadCnt(String tableName, String docGrpId) throws SQLException {
		DmDocLVo updateDmDocLVo = new DmDocLVo();
		updateDmDocLVo.setTableName(tableName);
		updateDmDocLVo.setDocGrpId(docGrpId);
		updateDmDocLVo.setInstanceQueryId("com.innobiz.orange.web.dm.dao.DmDocDDao.updateReadCnt");
		commonDao.update(updateDmDocLVo);
	}
	
	/** 조회수 초기화 */
	public void initReadCnt(String tableName, String docGrpId, Integer readCnt) throws SQLException {
		DmDocLVo updateDmDocLVo = new DmDocLVo();
		updateDmDocLVo.setTableName(tableName);
		updateDmDocLVo.setDocGrpId(docGrpId);
		if(readCnt != null) updateDmDocLVo.setReadCnt(readCnt);
		updateDmDocLVo.setInstanceQueryId("com.innobiz.orange.web.dm.dao.DmDocDDao.updateReadCntInit");
		commonDao.update(updateDmDocLVo);
	}
	
	/** 상신 저장[상신,승인,반려] */
	public void saveDisc(QueryQueue queryQueue, String compId, String docGrpId, String statCd, String rjtOpin, String discrUid, String userUid, String actKey){
		if(compId == null || docGrpId == null) return;
		// 상신목록 저장
		DmSubmLVo dmSubmLVo = new DmSubmLVo();
		dmSubmLVo.setCompId(compId);
		dmSubmLVo.setDocGrpId(docGrpId);
		if(statCd != null){
			dmSubmLVo.setStatCd(statCd);
			if(!"S".equals(statCd)) dmSubmLVo.setDiscDt("sysdate");
		}
		if(discrUid != null) dmSubmLVo.setDiscrUid(discrUid);
		if("insert".equals(actKey)) {
			// 등록자, 등록일시
			dmSubmLVo.setRegrUid(userUid);
			dmSubmLVo.setRegDt("sysdate");
			queryQueue.insert(dmSubmLVo);
		}
		else if("delete".equals(actKey)) queryQueue.delete(dmSubmLVo);
		else {
			if(rjtOpin != null) dmSubmLVo.setRjtOpin(rjtOpin);
			// 수정자, 수정일시
			dmSubmLVo.setModrUid(userUid);
			dmSubmLVo.setModDt("sysdate");
			queryQueue.update(dmSubmLVo);
		}
	}
	
	/** 이메일 발송 - 심의문서, 열람요청*/
	public void sendDiscDocEmail(HttpServletRequest request, String langTypCd, String docNm, 
			String statCd, String toUid, UserVo userVo, String regDt, String rjtOpin, String emailTyp) throws SQLException, IOException, CmException{
		// 시스템 정책 조회
		/*Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
		if(sysPlocMap==null || "N".equals(sysPlocMap.get("mailEnable"))) return;*/
		
		String subjPrefix = messageProperties.getMessage("dm.msg.email."+emailTyp+".subj.prefix", request);
		String subjSuffix = "dm.msg.email.disc.subj.suffix"+statCd;
		//이메일 발송
		String subj = messageProperties.getMessage(subjSuffix, new String[]{subjPrefix}, request);
		if(toUid == null) return;
		String[] toUids = new String[1]; toUids[0] = toUid;
		
		String p1 = "<p>";
		String span1 = "<span style='font-weight: bold;'>";
		String span2 = "</span>";
		String p2 = "</p>";
		
		String toDate = commonDao.querySysdate(new CommonVoImpl("YYYY-MM-DD HH24:MI:SS"));
		
		if(regDt == null || regDt.isEmpty() || "sysdate".equals(regDt)) regDt = toDate; 
		else regDt = StringUtil.toLongDate(regDt);
		
		// 이메일 내용 생성
		StringBuilder sb = new StringBuilder();
		sb.append(p1).append(span1).append(messageProperties.getMessage("dm.cols.docNm", request)).append(span2).append(" : ").append(docNm).append(p2);//문서명
		sb.append(p1).append(span1).append(messageProperties.getMessage("cols.regDt", request)).append(span2).append(" : ").append(regDt).append(p2);//등록일시
		if("A".equals(statCd) || "R".equals(statCd)){
			sb.append(p1).append(span1).append(messageProperties.getMessage("dm.cols.hdlr", request)).append(span2).append(" : ").append(userVo.getUserNm()).append(p2);//처리자
			sb.append(p1).append(span1).append(messageProperties.getMessage("dm.cols.hdlDt", request)).append(span2).append(" : ").append(toDate).append(p2);//처리일시
			if("R".equals(statCd) && rjtOpin != null){
				sb.append(p1).append(span1).append(messageProperties.getMessage("cols.rjtOpin", request)).append(span2).append(" : ").append(rjtOpin).append(p2);//반려의견
			}
		}
		
		emailSvc.sendMailSvc(userVo.getUserUid(), toUids, subj, sb.toString(), null, true,true, langTypCd);
	}
	
	/** 순서 구하기 */
	public int getSortOrdr(DmStorBVo dmStorBVo, DmDocLVo parentVo, QueryQueue queryQueue)
			throws SQLException {
		// 부모 문서의 하위 문서 순서
		DmDocLVo dmDocLVo = newDmDocLVo(dmStorBVo);
		
		dmDocLVo.setSubDocGrpId(isParentTop(parentVo) ? parentVo.getDocGrpId() : parentVo.getSubDocGrpId());
		dmDocLVo.setSubYn("Y");
		dmDocLVo.setSortOrdr(parentVo.getSortOrdr());
		dmDocLVo.setSortDpth(parentVo.getSortDpth());
		dmDocLVo.setInstanceQueryId("com.innobiz.orange.web.dm.dao.DmDocLDao.selectMinSortOrdr");
		Integer minSortOrdr = commonDao.queryInt(dmDocLVo);
		// 부모 문서의 첫번째로 순서 변경
		if (minSortOrdr != null) {
			// 테이블 - UPDATE
			DmDocLVo updateDmDocLVo = newDmDocLVo(dmStorBVo);
			updateDmDocLVo.setSubYn("Y");
			updateDmDocLVo.setSubDocGrpId(parentVo.getSubDocGrpId());
			updateDmDocLVo.setSortOrdr(minSortOrdr.toString());
			updateDmDocLVo.setInstanceQueryId("com.innobiz.orange.web.dm.dao.DmDocLDao.updateSortOrdr");
			queryQueue.update(updateDmDocLVo);
			
			return minSortOrdr;
		} else {
			dmDocLVo.setSubYn("N");
			dmDocLVo.setInstanceQueryId("com.innobiz.orange.web.dm.dao.DmDocLDao.selectMaxSortOrdr");
			Integer maxSortOrdr = commonDao.queryInt(dmDocLVo);
			if(maxSortOrdr == null) maxSortOrdr = 0;
			return maxSortOrdr+1;
		}
	}
	
	/** 부모문서의 속성을 복사한다. - 사용안함 */
	public void copyDmDocLVos(QueryQueue queryQueue, DmStorBVo dmStorBVo, String docId, String[] voAttrs) throws SQLException{
		DmDocLVo parentVo = getDmDocLVo("ko", dmStorBVo, docId, null);
		
		if(parentVo == null || !isParentTop(parentVo)) return;
		
		DmDocLVo dmDocLVo = newDmDocLVo(dmStorBVo);
		dmDocLVo.setSubYn("Y"); // 하위여부
		dmDocLVo.setSubDocGrpId(parentVo.getDocGrpId()); // 하위문서그룹ID
		
		// 하위 문서
		if(commonDao.count(dmDocLVo)>0){
			@SuppressWarnings("unchecked")
			List<DmDocLVo> subList = (List<DmDocLVo>)commonDao.queryList(dmDocLVo);
			boolean isUpdate = false;
			for(DmDocLVo storedDmDocLVo : subList){
				for(String voAttr : voAttrs){
					if(!VoUtil.getValue(parentVo, voAttr).equals(VoUtil.getValue(storedDmDocLVo, voAttr))){
						isUpdate = true;
						break;
					}
				}
				if(isUpdate) break;
				//dmDocLVo.setDocId(storedDmDocLVo.getDocId());
				//copyDmDocLVo(queryQueue, parentVo, dmDocLVo, voAttrs, true);
			}
			if(isUpdate) copyDmDocLVo(queryQueue, parentVo, dmDocLVo, voAttrs, true);
		}
	}
	
	/** 문서 최상위 여부 */
	public boolean isParentTop(DmDocLVo parentVo){
		return "Y".equals(parentVo.getDftYn()) && "N".equals(parentVo.getSubYn());
	}
	
	/** 부모문서의 속성을 복사한다. */
	public void copyDmDocLVo(QueryQueue queryQueue, DmDocLVo parentVo, DmDocLVo dmDocLVo, String[] voAttrs, boolean isQuery) throws SQLException{
		for(String voAttr : voAttrs){
			VoUtil.setValue(dmDocLVo, voAttr, VoUtil.getValue(parentVo, voAttr));			
		}
		if(isQuery) queryQueue.update(dmDocLVo);
	}
	
	/** 문서 저장 [기본정보] */
	public DmDocLVo saveDoc(HttpServletRequest request, QueryQueue queryQueue , DmStorBVo dmStorBVo, 
			DmDocLVo dmDocLVo, UserVo userVo, String docId, boolean isSub, List<DmCommFileDVo> copyFileList) throws SQLException, IOException, CmException{
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 문서 VO 생성
		if(dmDocLVo == null){
			dmDocLVo = newDmDocLVo(dmStorBVo);
			VoUtil.bind(request, dmDocLVo);
		}
		
		// 테이블명
		String tableName = dmDocLVo.getTableName();
		
		boolean isFirst = docId == null || docId.isEmpty();
		// 하위여부
					
		if(isFirst) {
			// 문서ID 생성
			docId = dmCmSvc.createNo("DM_DOC_L").toString();
			// 기본버전여부
			dmDocLVo.setDftYn("Y");
			// 문서ID
			dmDocLVo.setDocId(docId);
			// 문서그룹ID
			dmDocLVo.setDocGrpId(docId);
						
			// 하위문서
			if(isSub){
				String docPid = dmDocLVo.getDocPid();
				DmDocLVo parentVo = getDmDocLVo(langTypCd, dmStorBVo, null, docPid);
				//상위문서가 삭제되었을 경우 미분류 폴더로 저장한다.
				if(parentVo == null){
					isSub = false;
					dmDocLVo.setFldId(DmConstant.EMPTY_CLS);
				}else{
					// 최상위 문서 일경우 그룹ID를 하위문서일 경우 하위그룹ID를 세팅한다.
					//String subDocGrpId = isParentTop(parentVo) ? parentVo.getDocGrpId() : parentVo.getSubDocGrpId();
					String subDocGrpId = parentVo.getSubDocGrpId();
					dmDocLVo.setSubYn("Y");
					dmDocLVo.setSubDocGrpId(subDocGrpId);
					dmDocLVo.setDocPid(parentVo.getDocGrpId());
					dmDocLVo.setSortOrdr(getSortOrdr(dmStorBVo, parentVo, queryQueue)+"");
					dmDocLVo.setSortDpth(parentVo.getSortDpth()+1);
					//부모문서의 폴더ID 세팅
					dmDocLVo.setFldId(parentVo.getFldId());
				}
			}
			
			if(!isSub){
				dmDocLVo.setSubYn("N");
				dmDocLVo.setSubDocGrpId(docId);
				dmDocLVo.setDocPid(null);
				dmDocLVo.setSortOrdr("0");
				dmDocLVo.setSortDpth(0);
			}
			dmDocLVo.setOrgnYn("Y");
			// 조회수
			dmDocLVo.setReadCnt(0);
			// 등록자, 등록일시
			if(dmDocLVo.getRegrUid() == null || dmDocLVo.getRegrUid().isEmpty()) dmDocLVo.setRegrUid(userVo.getUserUid());
			if(dmDocLVo.getRegDt() == null || dmDocLVo.getRegDt().isEmpty()) dmDocLVo.setRegDt("sysdate");
			// 문서(DM_DOC_L) 테이블 - INSERT
			queryQueue.insert(dmDocLVo);
			
			// 저장소문서관계 저장
			dmStorSvc.saveStorId(queryQueue, dmStorBVo.getStorId(), dmDocLVo.getDocGrpId(), false);
			
		}else{
			// 수정시 버전선택값
			String verVaChk = ParamUtil.getRequestParam(request, "verVaChk", false);
			// 버전 정보 저장
			if(verVaChk != null && !verVaChk.isEmpty()){
				saveDmDocVerLVo(request, queryQueue, dmDocLVo, langTypCd, dmStorBVo, verVaChk, tableName, docId, copyFileList);
			}
			
			if(dmDocLVo.getRegDt()==null){
				// 수정자, 수정일시
				dmDocLVo.setModrUid(userVo.getUserUid());
				dmDocLVo.setModDt("sysdate");
			}
			
			// 문서(DM_DOC_L) 테이블 - UPDATE
			queryQueue.update(dmDocLVo);
		}
					
		// 버전 목록 저장
		if(dmDocLVo.getVerVa() != null)	{
			//String docGrpId = dmDocLVo.getDocGrpId();
			saveDocVer(queryQueue, dmDocLVo.getDocGrpId(), docId, tableName, dmDocLVo.getVerVa(), "store");
		}
		
		return dmDocLVo;
	}
	
	/** 버전 정보 저장 */
	public void saveDmDocVerLVo(HttpServletRequest request, QueryQueue queryQueue, DmDocLVo dmDocLVo, 
			String langTypCd, DmStorBVo dmStorBVo, String verVaChk, String tableName, String docId, List<DmCommFileDVo> copyFileList) throws SQLException, IOException, CmException{
		// 버전추가여부
		boolean isVer = true;
		// 버전 정보 조회
		DmDocVerLVo dmDocVerLVo = new DmDocVerLVo();
		//dmDocVerLVo.setStorId(dmDocLVo.getStorId());
		dmDocVerLVo.setTableName(tableName);
		dmDocVerLVo.setDocId(docId);
		dmDocVerLVo = (DmDocVerLVo)commonDao.queryVo(dmDocVerLVo);
		
		if(dmDocVerLVo != null)
			dmDocLVo.setDocGrpId(dmDocVerLVo.getDocGrpId());
		
		if(!"curr".equals(verVaChk)){// 현재버전이 아니면
			//수동입력한 버전이 현재 버전과 같은지 체크
			if("mnal".equals(verVaChk)){
				// 현재버전
				double originVerVa = Double.parseDouble(dmDocVerLVo.getVerVa());
				// 수정한 버전
				double modVerVa = Double.parseDouble(dmDocLVo.getVerVa());
				
				// 기존 버전 중에 입력한 버전보다 큰게 있으면 현재버전으로 저장한다.					
				List<DmDocVerLVo> dmDocVerLVoList = getDocVerLVoList(dmDocVerLVo.getDocGrpId(), null, "VER_VA DESC", tableName);
				if(dmDocVerLVoList != null && dmDocVerLVoList.size()>0){
					//현재 버전이 아니면서 최신버전
					double topVerVa = originVerVa;
					for(DmDocVerLVo storedDmDocVerLVo : dmDocVerLVoList){
						if(!dmDocVerLVo.getDocId().equals(storedDmDocVerLVo.getDocId())){
							topVerVa = Double.parseDouble(storedDmDocVerLVo.getVerVa());
							break;
						}
					}
					// 수동입력한 버전이 현재 버전목록보다 작거나 같으면 현재버전으로 변경 저장한다.
					if(modVerVa <= topVerVa){
						dmDocLVo.setVerVa(dmDocVerLVo.getVerVa());
						isVer = false;
						// dm.msg.not.transDoc.ver=기존버전보다 낮은 버전으로 저장할 수 없습니다.
						//String message = messageProperties.getMessage("dm.msg.not.transDoc.ver", request);
						//LOGGER.error("[ERROR] Document Version save fail!! - originVerVa : "+originVerVa+"\tmodVerVa : "+modVerVa+"\ttopVerVa:"+topVerVa);
						//throw new CmException(message);
					}
				}
				// 수동입력한 버전이 기존 버전보다 작거나 같으면 [현재버전 수정], 그게 아니면(크면) [신규버전 추가 저장]
				if(isVer && originVerVa >= modVerVa) isVer = false;
			}
			if(isVer){
				// 신규 생성 문서[원본과 대상테이블이 같음]
				//dmStorBVo.setWithLob(true);// 내용 조회
				DmDocLVo newDmDocLVo = getDmDocLVo(langTypCd, dmStorBVo, docId, null, true);
				String newDocId = dmCmSvc.createNo("DM_DOC_L").toString();
				newDmDocLVo.setTableName(tableName);
				newDmDocLVo.setDocId(newDocId);
				newDmDocLVo.setDftYn("N");// 기본여부를 'N'로 변경
				newDmDocLVo.setRegDt(newDmDocLVo.getRegDt());
				newDmDocLVo.setModDt(newDmDocLVo.getModDt());
				
				queryQueue.insert(newDmDocLVo);
				
				// 버전 목록 추가 저장
				saveDocVer(queryQueue, dmDocVerLVo.getDocGrpId(), newDocId, tableName, dmDocVerLVo.getVerVa(), "store");
				
				//파일복사
				dmFileSvc.copyDmFile(request, dmDocLVo.getDocId(), newDocId, queryQueue, tableName, tableName, copyFileList);
				
				// 신규버전문서의 등록일자, 수정자, 수정일자를 초기화 
				dmDocLVo.setRegDt("sysdate");
				dmDocLVo.setModrUid("");
				dmDocLVo.setModDt("");
			}
		}
		//if(dmDocVerLVo.getDocGrpId() != null) dmDocLVo.setDocGrpId(dmDocVerLVo.getDocGrpId());
			
	}
	/** 두개의 ID를 비교 */
	public boolean eqChkId(String srcId, String selId){
		return srcId.equals(selId);
	}
	
	/** 문서 복사[공용=>개인, 공용=>게시판, 개인=>공용, 개인=>게시판] */
	public void copyDoc(HttpServletRequest request, QueryQueue queryQueue, String langTypCd, DmStorBVo dmStorBVo, UserVo userVo, String storId, String tableName,
			String mode, String selId, List<DmCommFileDVo> copyFileList, Map<String,DmDocLVo> originVoListMap, List<String> docGrpIdList, String paramStorId) throws SQLException, IOException, CmException{
		
		// 관리자 여부
		boolean admUriChk = request.getRequestURI().startsWith("/dm/adm/");
		
		// 대상 테이블명[이관문서]
		String tgtTableName = null;
		
		// 이관여부
		boolean isTransfer = false;
		String tgtStorId = null;
		String tgtCompId = null;
		if(paramStorId != null && !paramStorId.isEmpty()){
			// 기본 저장소 조회
			DmStorBVo tgtStorVo = dmStorSvc.getDmStorBVo(userVo.getCompId(), langTypCd, null);
			if(tgtStorVo != null) {
				tgtTableName = tgtStorVo.getTblNm();
				isTransfer = true;
				tgtStorId = tgtStorVo.getStorId();
				tgtCompId = tgtStorVo.getCompId();
			}
		}
		
		if(tgtTableName == null) tgtTableName = tableName;
		if(tgtStorId == null) tgtStorId = storId;
		if(tgtCompId == null) tgtCompId = dmStorBVo.getCompId();
		// 관련문서그룹 맵
		Map<String,List<DmDocDVo>> subDocVoMap = new HashMap<String,List<DmDocDVo>>();
		
		// 부모문서ID
		String docPid = ParamUtil.getRequestParam(request, "docPid", false);
				
		DmDocDVo parentVo = null;
		if(docPid != null && !docPid.isEmpty()) parentVo = getDmDocDVo(langTypCd, tgtTableName, docPid);
		
		String[] docGrpIds = ArrayUtil.toArray(docGrpIdList);
		
		// 하위문서로 보내기[보낼 문서중에 상위문서가 있는지 체크]
		if(parentVo != null){
			for(String chkId : docGrpIds){
				if(eqChkId(docPid, chkId)){
					// dm.msg.not.save.duplParent=동일한 상위문서로 저장할 수 없습니다.
					throw new CmException("dm.msg.not.save.duplParent", request);
				}		
			}
		}
		
		String[] statCds = null;
		boolean isWithSubYn = false;
		// 관리자 환경설정에서 조회
		/*Map<String, String> envConfigMap = dmAdmSvc.getEnvConfigMap(null, userVo.getCompId());
		if(envConfigMap!=null){
			// 하위문서포함여부
			String withSubYn = envConfigMap.get(mode+"WithSubYn");
			if(withSubYn != null && !withSubYn.isEmpty() && "Y".equals(withSubYn)) {
				isWithSubYn = true;
				statCds = new String[]{"C"};
			}
		}*/
		// 문서그룹 맵 세팅[그룹별] 
		setSubDocGrpMap(tableName, subDocVoMap, docGrpIds, null, statCds, isWithSubYn);
		
		// 최상위 문서그룹ID 맵
		Map<String,String> topDocMap = new HashMap<String,String>();
		// 관련문서 전체 목록
		List<DmDocDVo> subGrpList = new ArrayList<DmDocDVo>();
		// 관련문서 전체 목록맵
		Map<String,DmDocDVo> subGrpListMap = new HashMap<String,DmDocDVo>();
		
		// 시작 정렬순서
		Integer strtSortOrdr = parentVo != null ? getStrtSortOrdr(tgtTableName, parentVo) : 0;
		
		// 최종 정렬
		Integer totalCnt = setSubDocGrpSortOrdrs(null, subDocVoMap, topDocMap, subGrpList, subGrpListMap, parentVo, strtSortOrdr);
		
		// 부모문서가 있으면 삽입된 문서 다음으로 정렬순서를 변경
		if(parentVo != null) {
			DmDocDVo updateDmDocDVo = new DmDocDVo();
			updateDmDocDVo.setTableName(tgtTableName);
			updateDmDocDVo.setSubDocGrpId(parentVo.getSubDocGrpId());
			updateDmDocDVo.setFldId(parentVo.getFldId());
			updateDmDocDVo.setSortOrdr(strtSortOrdr+"");
			updateDmDocDVo.setMaxSortOrdr(totalCnt+"");
			updateDmDocDVo.setInstanceQueryId("com.innobiz.orange.web.dm.dao.DmDocDDao.updateMaxSortOrdr");
			queryQueue.update(updateDmDocDVo);
			
			selId = parentVo.getFldId();
		}
		
		if(subGrpList.size()>0){
			//상위문서가 삭제되었을 경우 미분류 폴더로 저장한다.
			if(docPid != null && !docPid.isEmpty() && parentVo == null) selId = DmConstant.EMPTY_CLS;
			
			// 폴더ID[보내기옵션]
			String fldId = ParamUtil.getRequestParam(request, "fldId", false);
			if(fldId != null && !fldId.isEmpty()) selId = fldId;
			
			// 등록자UID[보내기옵션]
			String regrUid = ParamUtil.getRequestParam(request, "regrUid", false);
			
			DmDocLVo originVo = null;
			DmDocDVo subGrpMapVo = null;
			for(DmDocDVo subGrpVo : subGrpList){
				originVo = originVoListMap.get(subGrpVo.getDocGrpId());
				if(originVo == null) continue;
				// 보낼 문서와 상위문서가 같은지 비교
				if(originVo.getDocPid() != null && "Y".equals(originVo.getSubYn()) && eqChkId(originVo.getDocPid(), docPid)){
					// dm.msg.not.save.duplParent=동일한 상위문서로 저장할 수 없습니다.
					throw new CmException("dm.msg.not.save.duplParent", request);
				}
				if(!isTransfer){
					if((("move".equals(mode) && parentVo == null) || "copy".equals(mode)) && !DmConstant.EMPTY_CLS.equals(originVo.getFldId()) && eqChkId(originVo.getFldId(), selId)){
						// dm.msg.not.save.duplFld=동일한 폴더로 저장할수 없습니다.
						throw new CmException("dm.msg.not.save.duplFld", request);
					}
				}
				
				// 작업 이력저장[원본]
				if(!admUriChk && dmTaskSvc.getTaskCdChk(null, userVo.getCompId(), mode)){
					
					dmTaskSvc.saveDmTask(queryQueue, tgtTableName, langTypCd, originVo.getDocGrpId(), userVo.getUserUid(), mode, null);
				}
				
				originVo.setFldId(selId);
				// 문서그룹ID
				String originDocGrpId = originVo.getDocGrpId();	
				
				// 정렬된 문서상세 정보를 vo에 세팅
				originVo.setSubYn(subGrpVo.getSubYn());
				originVo.setDocPid(subGrpVo.getDocPid());
				originVo.setSubDocGrpId(subGrpVo.getSubDocGrpId());
				originVo.setSortOrdr(subGrpVo.getSortOrdr());// 순서
				originVo.setSortDpth(subGrpVo.getSortDpth());// 단계
				originVo.setDocGrpId(subGrpVo.getDocGrpId());
				
				// 등록자UID가 있을경우 세팅한다.
				if(regrUid != null && !regrUid.isEmpty()) originVo.setRegrUid(regrUid);
				
				// 폴더유형중에 심의여부가 'Y' 이면 'S'[심의요청] 을 아니면 'C'[완료] 로 세팅한다.
				setDmStatCd(originVo, tgtStorId, selId);
				
				// 테이블명
				originVo.setTableName(tgtTableName);
				
				// 폴더 조회[문서번호 채번 조건중 폴더명이 있을 경우에 대하여 폴더명을 조회해서 삽입한다.]
				DmFldBVo dmFldBVo = getDmFldBVo(tgtStorId, selId, langTypCd);
				if(dmFldBVo != null) originVo.setFldNm(dmFldBVo.getFldNm());
				
				if("move".equals(mode)){
					// 심의문서 중복 체크
					if("S".equals(originVo.getStatCd()))	{
						DmSubmLVo dmSubmLVo = new DmSubmLVo();
						dmSubmLVo.setCompId(userVo.getCompId());
						dmSubmLVo.setDocGrpId(originVo.getDocGrpId());
						// 심의문서로 등록되어 있으면 기존심의정보 삭제 
						if(commonDao.count(dmSubmLVo)>0){
							queryQueue.delete(dmSubmLVo);						
						}
						//보존연한
						originVo.setKeepPrdDt("");
						originVo.setCmplDt("");
						originVo.setDocNo("");
					}
					
					// 이동할 문서의 원본여부가 'Y'
					if(originVo.getOrgnYn() != null && "Y".equals(originVo.getOrgnYn())){
						// 폴더ID가 'none'(미분류)이면 문서번호를 생성한다.
						if(DmConstant.EMPTY_CLS.equals(originVo.getFldId()) && originVo.getStatCd() != null && "C".equals(originVo.getStatCd())){
							//문서번호 세팅[등록:심의여부'N',수정:상태코드가'C']
							dmDocNoSvc.setDocNo(originVo, tgtStorId, langTypCd, "C", tgtCompId, originVo.getFldId(), originVo.getRegrUid());
						}
						// 참조구분이 결재(ap)일 경우 변경내역을 알려주기 위해 결재 서비스를 호출한다.
						if(originVo.getRefTyp() != null && "ap".equals(originVo.getRefTyp())){
							
						}
					}
					// 등록자가 변경될경우 수정한다.
					if(regrUid != null && !regrUid.isEmpty()) queryQueue.update(originVo);
				}else{// 문서복사
					// 원본문서ID
					String originDocId = originVo.getDocId();
					// 원본버전
					String originVerVa = originVo.getVerVa();
					// 신규 문서ID 생성					
					String newDocId = dmCmSvc.createNo("DM_DOC_L").toString();
					
					// 기본버전여부
					originVo.setDftYn("Y");
					// 문서ID
					originVo.setDocId(newDocId);
					// 문서그룹ID
					originVo.setDocGrpId(newDocId);
					
					// 하위여부가 'N'일 경우
					if("N".equals(subGrpVo.getSubYn())){
						// 하위문서맵에서 문서VO 로드
						subGrpMapVo = subGrpListMap.get(subGrpVo.getDocGrpId());
						// 최상위 문서의 그룹ID를 신규ID로 변경한다.
						topDocMap.put(subGrpVo.getSubDocGrpId(), originVo.getDocGrpId());
						// 하위문서그룹ID를 신규생성된 문서그룹ID로 변경한다.
						originVo.setSubDocGrpId(originVo.getDocGrpId());
						// 관련문서 맵VO에 변경된 문서그룹ID를 세팅한다.
						subGrpMapVo.setDocGrpId(originVo.getDocGrpId());
					}else{
						// 부모문서가 관련문서맵에 있으면 부모문서의 ID를 상위부모ID로 변경한다 
						if(subGrpListMap.containsKey(subGrpVo.getDocPid())){
							subGrpMapVo = subGrpListMap.get(subGrpVo.getDocPid());
							originVo.setDocPid(subGrpMapVo.getDocGrpId());
						}else{
							originVo.setDocPid(subGrpVo.getDocPid());
						}
						// 하위문서그룹ID를 최상위문서맵의 ID정보로 변경한다.
						originVo.setSubDocGrpId(topDocMap.get(subGrpVo.getSubDocGrpId()));
						// 관련문서 맵에 변경된 문서그룹ID를 세팅한다.
						subGrpListMap.get(originDocGrpId).setDocGrpId(originVo.getDocGrpId());
					}
					
					// 조회수 초기화
					originVo.setReadCnt(0);
					
					// 원본여부
					originVo.setOrgnYn("N");
					
					// 등록자, 등록일시
					if(originVo.getRegrUid() == null || originVo.getRegrUid().isEmpty()) originVo.setRegrUid(userVo.getUserUid());
					originVo.setRegDt("sysdate");
					
					if(originVo.getStatCd() != null && "C".equals(originVo.getStatCd())){
						//문서번호 세팅[등록:심의여부'N',수정:상태코드가'C']
						dmDocNoSvc.setDocNo(originVo, tgtStorId, langTypCd, "C", tgtCompId, originVo.getFldId(), originVo.getRegrUid());
					}
					
					// 완료일시
					if(originVo.getStatCd() != null && "C".equals(originVo.getStatCd()))
						originVo.setCmplDt("sysdate");
					//버전목록 저장
					saveDocVer(queryQueue, originVo.getDocGrpId(), originVo.getDocId(), tgtTableName, originVerVa, "store");
					//파일복사[DB]
					dmFileSvc.copyDmFile(request, originDocId, originVo.getDocId(), queryQueue, tableName, tgtTableName, copyFileList);
					
					if(originVo.getDocPid() == null || originVo.getDocPid().isEmpty()) originVo.setDocPid(null);
					//저장
					// 문서(DM_DOC_L) 테이블 - INSERT
					queryQueue.insert(originVo);
					
					// 작업 이력저장[복사본] - 복사일 경우에만 저장(등록)
					if(!admUriChk && "copy".equals(mode) && dmTaskSvc.getTaskCdChk(null, userVo.getCompId(), "insert")){
						dmTaskSvc.saveDmTask(queryQueue, tgtTableName, langTypCd, originVo.getDocGrpId(), userVo.getUserUid(), "insert", null);
					}
					// 저장소문서관계 저장
					dmStorSvc.saveStorId(queryQueue, tgtStorId, originVo.getDocGrpId(), false);
					
					/** 검색 색인 데이터를 더함 */
					addSrchIndex(originVo.getDocGrpId(), userVo, queryQueue, "I");
				}
				// 보안등급
				String seculCd = ParamUtil.getRequestParam(request, "seculCd", false);
				if(seculCd != null && !seculCd.isEmpty()) originVo.setSeculCd(seculCd);
				// 보존연한
				String docKeepPrdCd = ParamUtil.getRequestParam(request, "docKeepPrdCd", false);
				if(docKeepPrdCd != null && !docKeepPrdCd.isEmpty()) originVo.setDocKeepPrdCd(docKeepPrdCd);
				// 소유자UID
				String ownrUid = ParamUtil.getRequestParam(request, "ownrUid", false);
				if(ownrUid != null && !ownrUid.isEmpty()) originVo.setOwnrUid(ownrUid);
							
				// 분류체계 조회			 
				setDmClsBVoList(request, null, tgtStorId, null, originVo, parentVo != null ? parentVo.getDocGrpId() : null, null, tableName, langTypCd);
				if(originVo.getDmClsRVoList() == null || originVo.getDmClsRVoList().size() ==0) setDmClsBVoList(request, null, tgtStorId, null, originVo, originDocGrpId, null, tableName, langTypCd);
				List<DmClsRVo> dmClsRVoList = originVo.getDmClsRVoList();
				// 키워드 조회
				String[] kwdList = getDmKwdLVoList(request, originVo, parentVo != null ? parentVo.getDocGrpId() : null, tableName);
				
				// 하위문서 일 경우
				if("move".equals(mode)){ //이동
					if(dmClsRVoList != null && dmClsRVoList.size()>0) saveDmClsRVoList(queryQueue, dmClsRVoList, originVo.getDocGrpId(), tgtTableName, true);
					if(kwdList != null && kwdList.length>0) saveDmKwdLVoList(queryQueue, kwdList, originVo.getDocGrpId(), tgtTableName, true);
				}else{
					if(dmClsRVoList != null && dmClsRVoList.size()>0) saveDmClsRVoList(queryQueue, dmClsRVoList, originVo.getDocGrpId(), tgtTableName, false);
					if(kwdList != null && kwdList.length>0) saveDmKwdLVoList(queryQueue, kwdList, originVo.getDocGrpId(), tgtTableName, false);
				}
				
				// 문서상세 저장[보존기한등]
				if(originVo.getStatCd() != null && "C".equals(originVo.getStatCd()))
					originVo.setCmplDt("sysdate");
				// 문서상세 저장
				saveDmDocD(queryQueue, originVo, parentVo != null ? parentVo.getDocGrpId() : null);
				// 폴더유형의 심의여부가 'Y'일 경우 상신함에 저장한다.
				if("S".equals(originVo.getStatCd()))	 saveDisc(queryQueue, userVo.getCompId(), originVo.getDocGrpId(), "S", null, originVo.getDiscrUid(), userVo.getUserUid(), "insert");
			}
			//if("move".equals(mode)) // 기존 문서의 정렬순서를 변경한다.
			//	updateMinSortOrdr(queryQueue, tableName, subDocVoMap);
		}
		
	}
	
	/** 문서 복사[공용=>개인, 공용=>게시판, 개인=>공용, 개인=>게시판] */
	public void copyDoc(HttpServletRequest request, QueryQueue queryQueue, String langTypCd, DmStorBVo dmStorBVo, UserVo userVo, String storId, String tableName,
			DmDocLVo originVo, String mode, String tabId, String docTyp, String selId, boolean isPsn, List<DmCommFileDVo> copyFileList, Integer sortOrder) throws SQLException, IOException, CmException{
		if("brd".equals(tabId)){
			// 게시판관리(BA_BRD_B) 테이블 - SELECT
			BaBrdBVo baBrdBVo = bbBrdSvc.getBaBrdBVo(langTypCd, selId);
			// 게시물(BB_X000X_L) 테이블 - BIND
			BbBullLVo bbBullLVo = bbBullSvc.newBbBullLVo(baBrdBVo);
			bbBullLVo.setBrdId(baBrdBVo.getBrdId());
			// 부서게시판이면
 			if ("Y".equals(baBrdBVo.getDeptBrdYn())) {
				bbBullLVo.setDeptId(userVo.getDeptId());
			}
			
			// 회사ID
			bbBullLVo.setCompId(userVo.getCompId());
			// 수정자
			bbBullLVo.setModrUid(userVo.getUserUid());
			// 수정일시
			bbBullLVo.setModDt("sysdate");
			
			// 게시물상태코드-T(임시저장),R(예약저장),S(상신),J(반려),B(게시)
			if("Y".equals(baBrdBVo.getDiscYn()))
				bbBullLVo.setBullStatCd("S");
			else
				bbBullLVo.setBullStatCd("B");
			
			// 게시물ID 생성
			bbBullLVo.setBullId(bbCmSvc.createBullId());
			
			// 기본값 세팅
			bbBullLVo.setReplyGrpId(bbBullLVo.getBullId());
			bbBullLVo.setReplyOrdr(0);
			bbBullLVo.setReplyDpth(0);
			bbBullLVo.setReadCnt(0);
			bbBullLVo.setProsCnt(0);
			bbBullLVo.setConsCnt(0);
			bbBullLVo.setRecmdCnt(0);
			bbBullLVo.setScre(0);
			
			// 복사할 항목 세팅
			bbBullLVo.setSubj(originVo.getSubj());
			bbBullLVo.setCont(originVo.getCont());
			
			// 날짜 초기화[유효기간 설정]
			bbBullSvc.initBullExprDt(baBrdBVo, bbBullLVo);
			
			// 게시물(BB_X000X_L) 테이블 - INSERT
			queryQueue.insert(bbBullLVo);
			
			if ("S".equals(bbBullLVo.getBullStatCd())) {
				// 게시상신함 저장
				bbBullSvc.saveBaBullSubmLVo(request, bbBullLVo.getBullId(), baBrdBVo.getDiscrUid(), queryQueue);
			}
			
			//파일복사[DB]
			dmFileSvc.copyBullFile(request, originVo.getDocId(), String.valueOf(bbBullLVo.getBullId()), queryQueue, isPsn ? null : tableName, copyFileList, docTyp);
				
		}else if("doc".equals(tabId)){
			//미분류로의 복사 이동은 안됨
			if(DmConstant.EMPTY_CLS.equals(selId)){
				// dm.msg.not.save.emptyCls='미분류' 로 저장할 수 없습니다.
				throw new CmException("dm.msg.not.save.emptyCls", request);
			}
			// 부모문서ID
			String docPid = ParamUtil.getRequestParam(request, "docPid", false);
			// 하위문서여부
			boolean isSub = docPid != null && !docPid.isEmpty() ? true : false;
			
			// 등록일시
			//String regDt = originVo.getRegDt();
			// 문서그룹ID
			String docGrpId = originVo.getDocGrpId();
			// 하위문서
			if(isSub){
				DmDocLVo parentVo = getDmDocLVo(langTypCd, dmStorBVo, null, docPid);
				
				//상위문서가 삭제되었을 경우 미분류 폴더로 저장한다.
				if(parentVo == null){
					isSub = false;
					originVo.setFldId(DmConstant.EMPTY_CLS);
					selId = DmConstant.EMPTY_CLS;
				}else{
					// 보낼 문서와 상위문서가 같은지 비교
					if("doc".equals(docTyp) && originVo.getDocPid() != null && "Y".equals(originVo.getSubYn()) && eqChkId(originVo.getDocPid(), docPid)){
						// dm.msg.not.save.duplParent=동일한 상위문서로 저장할 수 없습니다.
						throw new CmException("dm.msg.not.save.duplParent", request);
					}
					
					// 하위문서그룹ID
					String subDocGrpId = parentVo.getSubDocGrpId();
					originVo.setSubYn("Y");
					originVo.setSubDocGrpId(subDocGrpId);
					originVo.setDocPid(parentVo.getDocGrpId());
					if(sortOrder == null) originVo.setSortOrdr(getSortOrdr(dmStorBVo, parentVo, queryQueue)+"");
					else originVo.setSortOrdr(String.valueOf(sortOrder.intValue()));
					if(originVo.getSortDpth() == null) originVo.setSortDpth(parentVo.getSortDpth()+1);
					//부모문서의 폴더ID 세팅
					originVo.setFldId(parentVo.getFldId());
					selId = parentVo.getFldId();
				}
			}
			
			if(!isSub){
				originVo.setSubYn("N");
				if(originVo.getDocPid() == null) originVo.setDocPid("move".equals(mode) ? "" : null);
				if(originVo.getSortOrdr() == null) originVo.setSortOrdr("0");
				if(originVo.getSortDpth() == null) originVo.setSortDpth(0);
			}
			
			// 폴더ID[보내기옵션]
			String fldId = ParamUtil.getRequestParam(request, "fldId", false);
			if(fldId != null && !fldId.isEmpty()) selId = fldId;
			if("doc".equals(docTyp) && !isSub && !DmConstant.EMPTY_CLS.equals(originVo.getFldId()) && eqChkId(originVo.getFldId(), selId)){
				// dm.msg.not.save.duplFld=동일한 폴더로 저장할수 없습니다.
				throw new CmException("dm.msg.not.save.duplFld", request);
			}
			// 등록자UID[보내기옵션]
			String regrUid = ParamUtil.getRequestParam(request, "regrUid", false);
			if(regrUid != null && !regrUid.isEmpty()) originVo.setRegrUid(regrUid);
						
			// 인수완료여부
			boolean isTransCmpl = false;
			// 후처리[문서인수]
			String afterActKey = ParamUtil.getRequestParam(request, "afterActKey", false);
			if(afterActKey != null && !afterActKey.isEmpty() && "doc".equals(tabId) && "doc".equals(docTyp) && "transCmplSave".equals(afterActKey)){
				isTransCmpl = true;
				originVo.setRegDt("sysdate");
			}
			
			if(isTransCmpl) {
				originVo.setStatCd(null);
				// 인수인계기본 삭제
				DmTakovrBVo dmTakovrBVo = new DmTakovrBVo();
				dmTakovrBVo.setStorId(storId);
				dmTakovrBVo.setDocGrpId(docGrpId);
				queryQueue.delete(dmTakovrBVo);
			}
			
			// 폴더유형중에 심의여부가 'Y' 이면 'S'[심의요청] 을 아니면 'C'[완료] 로 세팅한다.
			setDmStatCd(originVo, storId, selId);
			
			originVo.setTableName(tableName);
			
			// 폴더 조회[문서번호 채번 조건중 폴더명이 있을 경우에 대하여 폴더명을 조회해서 삽입한다.]
			DmFldBVo dmFldBVo = getDmFldBVo(storId, selId, langTypCd);
			if(dmFldBVo != null) originVo.setFldNm(dmFldBVo.getFldNm());
						
			if("move".equals(mode)){ //이동 - 문서번호 채번 안함
				/*if(originVo.getStatCd() != null && "C".equals(originVo.getStatCd())){
					DmFldBVo topFldVo = dmAdmSvc.getTopTreeVo(storId, selId, langTypCd);
					boolean isComp = DmConstant.FLD_COMP.equals(topFldVo.getFldGrpId());
					
					//문서번호 세팅[등록:심의여부'N',수정:상태코드가'C']
					dmDocNoSvc.setDocNo(originVo, storId, langTypCd, "C", userVo.getCompId(), userVo.getOrgId(), isComp);
				}*/
				//DmDocLVo newDocVo = newDmDocLVo(dmStorBVo);
				//newDocVo.setDocId(originVo.getDocId());
				// 하위문서가 아닐 경우 하위문서그룹ID를 문서ID로 변경
				if(!isSub) {
					originVo.setSubDocGrpId(originVo.getDocId());
				}
				/*newDocVo.setSubDocGrpId(originVo.getSubDocGrpId());
				newDocVo.setSubYn(originVo.getSubYn());
				newDocVo.setDocPid(originVo.getDocPid());
				newDocVo.setSortOrdr(originVo.getSortOrdr());
				newDocVo.setSortDpth(originVo.getSortDpth());*/
				
				originVo.setFldId(selId);
				// 심의문서 중복 체크
				if("S".equals(originVo.getStatCd()))	{
					DmSubmLVo dmSubmLVo = new DmSubmLVo();
					dmSubmLVo.setCompId(userVo.getCompId());
					dmSubmLVo.setDocGrpId(originVo.getDocGrpId());
					// 심의문서로 등록되어 있으면 기존심의정보 삭제 
					if(commonDao.count(dmSubmLVo)>0){
						queryQueue.delete(dmSubmLVo);						
						// dm.msg.not.save.duplSubm=이미 한번 이동했던 문서입니다.
						//throw new CmException("dm.msg.not.save.duplSubm", request);
					}
					//보존연한
					originVo.setKeepPrdDt("");
					originVo.setCmplDt("");
					originVo.setDocNo("");
				}
				queryQueue.update(originVo);
				
				// 이동할 문서의 원본여부가 'Y'
				if(originVo.getOrgnYn() != null && "Y".equals(originVo.getOrgnYn())){
					// 폴더ID가 'none'(미분류)이면 문서번호를 생성한다.
					if(DmConstant.EMPTY_CLS.equals(originVo.getFldId()) && originVo.getStatCd() != null && "C".equals(originVo.getStatCd())){
						//문서번호 세팅[등록:심의여부'N',수정:상태코드가'C']
						dmDocNoSvc.setDocNo(originVo, storId, langTypCd, "C", dmStorBVo.getCompId(), originVo.getFldId(), originVo.getRegrUid());
					}
					// 참조구분이 결재(ap)일 경우 변경내역을 알려주기 위해 결재 서비스를 호출한다.
					if(originVo.getRefTyp() != null && "ap".equals(originVo.getRefTyp())){
						
					}
				}
				
			}else{
				String originDocId = originVo.getDocId();
				//String originDocGrpId = originVo.getSubDocGrpId();
				
				if(isPsn){// 개인문서일 경우 기본버전을 저장한다.
					// 관리자 환경설정 조회
					Map<String, String> envConfigMap = dmAdmSvc.getEnvConfigMap(null, userVo.getCompId());
					// 기본버전 저장
					originVo.setVerVa(envConfigMap.get("verDft"));
				}
				String originVerVa = originVo.getVerVa();
				
				String docId = dmCmSvc.createNo("DM_DOC_L").toString();
				// 기본버전여부
				originVo.setDftYn("Y");
				// 문서ID
				originVo.setDocId(docId);
				// 문서그룹ID
				originVo.setDocGrpId(docId);
				// 조회수
				originVo.setReadCnt(0);
				
				// 하위문서가 아닐 경우 하위문서그룹ID를 문서ID로 변경
				if(!isSub && originVo.getSubDocGrpId() == null) originVo.setSubDocGrpId(docId);
				
				// 원본여부 - 공용문서내에서의 복사는 사본이고 개인문서에서의 복사는 원본 으로 세팅
				if(docTyp != null && "doc".equals(docTyp)) originVo.setOrgnYn("N");
				else originVo.setOrgnYn("Y");
				
				// 등록자, 등록일시
				if(originVo.getRegrUid() == null || originVo.getRegrUid().isEmpty()) originVo.setRegrUid(userVo.getUserUid());
					originVo.setRegDt("sysdate");
				
				originVo.setFldId(selId);
					
				if(originVo.getStatCd() != null && "C".equals(originVo.getStatCd())){
					//문서번호 세팅[등록:심의여부'N',수정:상태코드가'C']
					dmDocNoSvc.setDocNo(originVo, storId, langTypCd, "C", dmStorBVo.getCompId(), originVo.getFldId(), originVo.getRegrUid());
				}
				
				// 완료일시
				if(originVo.getStatCd() != null && "C".equals(originVo.getStatCd()))
					originVo.setCmplDt("sysdate");
				//버전목록 저장
				saveDocVer(queryQueue, originVo.getDocGrpId(), originVo.getDocId(), tableName, originVerVa, "store");
				//파일복사[DB]
				dmFileSvc.copyDmFile(request, originDocId, originVo.getDocId(), queryQueue, isPsn ? null : tableName, tableName, copyFileList);
				//저장
				// 문서(DM_DOC_L) 테이블 - INSERT
				queryQueue.insert(originVo);
			}
			
			// 보안등급
			String seculCd = ParamUtil.getRequestParam(request, "seculCd", false);
			if(seculCd != null && !seculCd.isEmpty()) originVo.setSeculCd(seculCd);
			// 보존연한
			String docKeepPrdCd = ParamUtil.getRequestParam(request, "docKeepPrdCd", false);
			if(docKeepPrdCd != null && !docKeepPrdCd.isEmpty()) originVo.setDocKeepPrdCd(docKeepPrdCd);
			// 소유자UID
			String ownrUid = ParamUtil.getRequestParam(request, "ownrUid", false);
			if(ownrUid != null && !ownrUid.isEmpty()) originVo.setOwnrUid(ownrUid);
						
			// 분류체계 조회			 
			setDmClsBVoList(request, null, storId, null, originVo, isSub ? docPid : null, null, tableName, langTypCd);
			if(originVo.getDmClsRVoList() == null || originVo.getDmClsRVoList().size() ==0) setDmClsBVoList(request, null, storId, null, originVo, docGrpId, null, tableName, langTypCd);
			List<DmClsRVo> dmClsRVoList = originVo.getDmClsRVoList();
			// 키워드 조회
			String[] kwdList = getDmKwdLVoList(request, originVo, isSub ? docPid : null, tableName);
						
			// 하위문서 일 경우
			if("move".equals(mode)){ //이동
				if(dmClsRVoList != null && dmClsRVoList.size()>0) saveDmClsRVoList(queryQueue, dmClsRVoList, originVo.getDocGrpId(), tableName, true);
				if(kwdList != null && kwdList.length>0) saveDmKwdLVoList(queryQueue, kwdList, originVo.getDocGrpId(), tableName, true);
			}else{
				if(dmClsRVoList != null && dmClsRVoList.size()>0) saveDmClsRVoList(queryQueue, dmClsRVoList, originVo.getDocGrpId(), tableName, false);
				if(kwdList != null && kwdList.length>0) saveDmKwdLVoList(queryQueue, kwdList, originVo.getDocGrpId(), tableName, false);
			}
			
			/*if(!isSub && "doc".equals(docTyp)){
				originVo.setRegDt(regDt);
			}*/
			// 문서상세 저장[보존기한등]
			if(isTransCmpl && originVo.getStatCd() != null && "C".equals(originVo.getStatCd()))
				originVo.setCmplDt("sysdate");
			// 문서상세 저장
			saveDmDocD(queryQueue, originVo, isSub ? originVo.getSubDocGrpId() : null);
			// 폴더유형의 심의여부가 'Y'일 경우 상신함에 저장한다.
			if("S".equals(originVo.getStatCd()))	 saveDisc(queryQueue, userVo.getCompId(), originVo.getDocGrpId(), "S", null, originVo.getDiscrUid(), userVo.getUserUid(), "insert");
		}else if("psn".equals(tabId)){// 개인폴더
			//개인문서VO 생성
			DmDocLVo newDmDocLVo = newDmDocLVo();
			
			if("move".equals(mode)){ //이동
				if(eqChkId(originVo.getFldId(), selId)){
					// dm.msg.not.save.duplFld=동일한 폴더로 저장할수 없습니다.
					throw new CmException("dm.msg.not.save.duplFld", request);
				}
				newDmDocLVo.setDocId(originVo.getDocId());
				newDmDocLVo.setFldId(selId);
				// 등록자, 등록일시
				newDmDocLVo.setModrUid(userVo.getUserUid());
				newDmDocLVo.setModDt("sysdate");
				queryQueue.update(newDmDocLVo);
			}else{
				// 추가항목 목록 조회
				List<String> addItemList = getDmAddItemList();
				addItemList.add("subj");
				addItemList.add("cont");
				
				String[] voAttrs = addItemList.toArray(new String[addItemList.size()]);
				// 문서 복사
				copyDmDocLVo(queryQueue, originVo, newDmDocLVo, voAttrs, false);
				newDmDocLVo.setCompId(userVo.getCompId());
				newDmDocLVo.setFldId(selId);
				
				//문서ID생성
				newDmDocLVo.setDocId(dmCmSvc.createNo("DM_PSN_DOC_L").toString());
				// 등록자, 등록일시
				newDmDocLVo.setRegrUid(userVo.getUserUid());
				newDmDocLVo.setRegDt("sysdate");
				queryQueue.insert(newDmDocLVo);
				
				//파일복사[DB]
				dmFileSvc.copyDmFile(request, originVo.getDocId(), newDmDocLVo.getDocId(), queryQueue, isPsn ? null : tableName, null, copyFileList);
			}
			
		}
	}
	
	/** 분류체계 목록 저장 */
	public void saveDmClsRVoList(QueryQueue queryQueue, List<DmClsRVo> boundDmClsRVoList, String docGrpId, String tableName, boolean isDel) throws SQLException{
		DmClsRVo dmClsRVo = null;
		if(isDel){//기존 삭제여부
			dmClsRVo = new DmClsRVo();
			dmClsRVo.setTableName(tableName);
			dmClsRVo.setDocGrpId(docGrpId);
			queryQueue.delete(dmClsRVo);
		}
		if(boundDmClsRVoList != null){
			for(DmClsRVo storedDmClsRVo : boundDmClsRVoList){
				dmClsRVo = new DmClsRVo();			
				dmClsRVo.setTableName(tableName);
				dmClsRVo.setDocGrpId(docGrpId);
				dmClsRVo.setClsId(storedDmClsRVo.getClsId().trim());
				queryQueue.insert(dmClsRVo);
			}
		}
	}
	
	/** 키워드 목록 저장 */
	public void saveDmKwdLVoList(QueryQueue queryQueue, String[] kwdList, String docGrpId, String tableName, boolean isDel) throws SQLException{
		DmKwdLVo dmKwdLVo = null;
		if(isDel){//기존 삭제여부
			dmKwdLVo = new DmKwdLVo();
			dmKwdLVo.setTableName(tableName);
			dmKwdLVo.setDocGrpId(docGrpId);
			queryQueue.delete(dmKwdLVo);
		}
		if(kwdList != null){
			for(String kwdNm : kwdList){
				dmKwdLVo = new DmKwdLVo();
				dmKwdLVo.setTableName(tableName);
				dmKwdLVo.setDocGrpId(docGrpId);
				dmKwdLVo.setKwdNm(kwdNm.trim());
				queryQueue.insert(dmKwdLVo);
			}
		}
	}
	
	/** 문서 저장 - 보내기[결재,게시판]
	 *   paramMap : fldId[폴더ID],clsId[분류체계ID-콤마구분],seculCd[보안등급코드],docKeepPrdCd[보존연한코드],
	 *   				   subj[제목],fromTyp[구분:게시판(bb),결재(ap)],regrUid[등록자UID],regDt[등록일자],
	 *   				   refId[pk],refUrl[링크URL],docPid[문서부모ID-하위문서]
	 *   regrUid 와 regDt 는 paramMap에 없으면 regrUid(userVo) ,  regDt('sysdate')
	 *   호출방법
	 *   게시판 : sendDoc(request, queryQueue, paramMap, fileVoList);
	 *   결재 : sendDoc(request, queryQueue, paramMap, null);
	 * */
	@SuppressWarnings("unchecked")
	public Map<String,String> sendDoc(HttpServletRequest request, QueryQueue queryQueue, 
			Map<String,String> paramMap, List<CommonFileVo> fileVoList) throws SQLException, IOException, CmException{
		// 결과값을 map에 저장한다. 
		Map<String,String> returnMap = new HashMap<String,String>();
		// 코드값 체크 업무구분[결재]
		String[] validFromTyps = new String[]{"ap"};
				
		// queryQueue Null여부
		boolean nullQueryQueue = queryQueue == null;
		// queryQueue가 Null일 경우 생성
		if(nullQueryQueue) queryQueue = new QueryQueue();
		
		// 보낸대상업무구분
		String fromTyp = paramMap != null && paramMap.containsKey("fromTyp") ? paramMap.get("fromTyp") : null;
		// request bind 사용 여부
		boolean isReqBind = fromTyp != null && "bb".equals(fromTyp);
		
		// 기본저장소 조회
		DmStorBVo dmStorBVo = dmStorSvc.getDmStorBVo(request, null, null, null, null, null);
		
		if(dmStorBVo == null){
			String message = messageProperties.getMessage("dm.msg.nodata.stor.user", request);
			LOGGER.error(message);
			throw new CmException("dm.msg.nodata.stor.user", request);
		}
		String tableName = dmStorBVo.getTblNm();
		String storId = dmStorBVo.getStorId();
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		// 세션의 언어코드
		String langTypCd = paramMap.containsKey("langTypCd") ? paramMap.get("langTypCd") : LoginSession.getLangTypCd(request);
		
		// 파일복사 목록
		List<DmCommFileDVo> copyFileList = new ArrayList<DmCommFileDVo>();
		
		// 결재,게시판 매핑VO
		DmDocLVo newDmDocLVo = newDmDocLVo(dmStorBVo);
		if(isReqBind) VoUtil.bind(request, newDmDocLVo);
		// set paramMap - Map to Vo 세팅
		Entry<String, String> entry;
		if(paramMap!=null){
			Iterator<Entry<String, String>> iterator = paramMap.entrySet().iterator();
			while(iterator.hasNext()){
				entry = iterator.next();
				VoUtil.setValue(newDmDocLVo, entry.getKey(), entry.getValue());
			}
		}
		
		// 부모문서ID
		String docPid = paramMap.containsKey("docPid") ? paramMap.get("docPid") : ParamUtil.getRequestParam(request, "docPid", false);
		// 하위문서여부
		boolean isSub = docPid != null && !docPid.isEmpty() ? true : false;
				
		// 관리자 환경설정 조회
		Map<String, String> envConfigMap = dmAdmSvc.getEnvConfigMap(null, userVo.getCompId());
		// 기본버전 저장
		newDmDocLVo.setVerVa(envConfigMap.get("verDft"));
		
		// 폴더ID는 필수 입력 체크
		if(newDmDocLVo.getFldId() == null || newDmDocLVo.getFldId().isEmpty()){
			LOGGER.error("[ERROR] sendDoc - fldId is Null!! ");
			//dm.msg.not.send.fldEmpty=폴더가 입력되지 않아 문서를 저장할 수 없습니다.
			throw new CmException("dm.msg.not.send.fldEmpty", request);
		}
		
		// 내용 Null 세팅
		newDmDocLVo.setCont("");
		
		// 저장[기본정보]
		DmDocLVo dmDocLVo = saveDoc(request, queryQueue, dmStorBVo, newDmDocLVo, userVo, null, isSub, copyFileList);
		
		if(dmDocLVo.getFldId() != null && !dmDocLVo.getFldId().isEmpty() && ( dmDocLVo.getStatCd() == null || dmDocLVo.getStatCd().isEmpty())){
			// 결재
			if(ArrayUtil.isInArray(validFromTyps, fromTyp)){
				// 폴더정보 조회
				DmFldBVo dmFldBVo = getDmFldBVo(storId, dmDocLVo.getFldId(), langTypCd);
				//폴더ID가 없으면 'NONE' 미분류로 변경
				if(dmFldBVo == null){
					dmDocLVo.setFldId(DmConstant.EMPTY_CLS);
					dmDocLVo.setFldNm(messageProperties.getMessage("dm.cols.emptyCls", SessionUtil.toLocale(langTypCd)));// 미분류로 세팅
					returnMap.put("fldId", DmConstant.EMPTY_CLS);// 폴더ID(미분류)
					returnMap.put("fldNm", messageProperties.getMessage("dm.cols.emptyCls", SessionUtil.toLocale(langTypCd)));// dm.cols.emptyCls=미분류
				}else{
					returnMap.put("fldId", dmFldBVo.getFldId());// 폴더ID(미분류)
					// 해당 언어의 폴더명이 없으면 세션의 언어로 폴더를 조회한다.
					if(dmFldBVo.getFldNm() == null || dmFldBVo.getFldNm().isEmpty()) dmFldBVo = getDmFldBVo(storId, dmDocLVo.getFldId(), LoginSession.getLangTypCd(request));
					returnMap.put("fldNm", dmFldBVo.getFldNm());
				}
			}
			// 폴더유형중에 심의여부가 'Y' 이면 'S'[심의요청] 을 아니면 'C'[완료] 로 세팅한다.
			setDmStatCd(dmDocLVo, storId, dmDocLVo.getFldId());
		}
		
		// 상신목록 저장
		if("S".equals(dmDocLVo.getStatCd()))	saveDisc(queryQueue, userVo.getCompId(), dmDocLVo.getDocGrpId(), "S", null, dmDocLVo.getDiscrUid(), userVo.getUserUid(), "insert");
					
		// 문서가 '완료' 상태이면서 문서번호가 없으면 문서번호를 부여한다.
		if(dmDocLVo.getStatCd() != null && "C".equals(dmDocLVo.getStatCd()) && ( dmDocLVo.getDocNo() == null || dmDocLVo.getDocNo().isEmpty())){
			//문서번호 세팅[등록:심의여부'N',수정:상태코드가'C'] - 폴더가 미분류이면 일련번호를 채번하지 않는다.
			dmDocNoSvc.setDocNo(dmDocLVo, storId, langTypCd, DmConstant.EMPTY_CLS.equals(dmDocLVo.getFldId()) ? null : "C", dmStorBVo.getCompId(), dmDocLVo.getFldId(), dmDocLVo.getRegrUid());
		}
		
		// 완료상태 문서일 경우 검색 색인 데이터 추가
		if("C".equals(dmDocLVo.getStatCd())){
			/** 검색 색인 데이터를 더함 */
			addSrchIndex(dmDocLVo.getDocGrpId(), userVo, queryQueue, "I");
		}
					
		// 문서가 완료 상태이면 완료일자를 세팅한다.
		if(dmDocLVo.getStatCd() != null && "C".equals(dmDocLVo.getStatCd()))
			dmDocLVo.setCmplDt("sysdate");
		
		// 등록시점의 차이로 인해 코드성 데이터가 변경되거나 삭제될수 있으므로 해당 정보를 체크한다.
		if(ArrayUtil.isInArray(validFromTyps, fromTyp)){
			PtCdBVo ptCdBVo = null;
			// 보안등급 체크 - 포탈에서 삭제가 되어 등록이 안될경우 없음('none') 으로 변경해준다.
			String seculCd = dmDocLVo.getSeculCd();
			if(seculCd == null || seculCd.isEmpty()) dmDocLVo.setSeculCd("none");
			else{
				ptCdBVo = ptCmSvc.getCd(DmConstant.SECUL_CD, langTypCd, seculCd);
				if(ptCdBVo == null) dmDocLVo.setSeculCd("none");
			}			
			
			// 보존연한 체크 - 포탈에서 삭제가 되어 등록이 안될경우 영구('endless') 으로 변경해준다.
			String docKeepPrdCd = dmDocLVo.getDocKeepPrdCd();
			if(docKeepPrdCd == null || docKeepPrdCd.isEmpty()) dmDocLVo.setDocKeepPrdCd("endless");// 영구
			else{
				ptCdBVo = ptCmSvc.getCd(DmConstant.DOC_KEEP_PRD_CD, langTypCd, docKeepPrdCd);
				if(ptCdBVo == null) dmDocLVo.setDocKeepPrdCd("endless");// 영구
			}
		}
		// 참조구분
		dmDocLVo.setRefTyp(fromTyp);
		// 원본여부
		dmDocLVo.setOrgnYn("Y");
		
		// 문서상세 저장[보존기한등]
		saveDmDocD(queryQueue, dmDocLVo, isSub ? dmDocLVo.getSubDocGrpId() : null);
		
		// 저장[부가정보] - 시작
		// 분류관계 저장
		List<DmClsRVo> boundDmClsRVoList = (List<DmClsRVo>) VoUtil.bindList(request, DmClsRVo.class, new String[]{"clsId"});
		if(boundDmClsRVoList == null || boundDmClsRVoList.size() == 0){
			if(paramMap.containsKey("clsId")){
				String[] clsIds = paramMap.get("clsId").trim().split(",");
				if(clsIds.length>0){
					DmClsRVo dmClsRVo = null;
					boundDmClsRVoList = new ArrayList<DmClsRVo>();
					for(String id : clsIds){
						dmClsRVo = new DmClsRVo();
						dmClsRVo.setClsId(id.trim());
						boundDmClsRVoList.add(dmClsRVo);
					}
				}
			}else{
				LOGGER.error("[ERROR] sendDoc - clsId is Null!! ");
				//dm.msg.not.send.clsEmpty=분류체계가 입력되지 않아 문서를 저장할 수 없습니다.
				throw new CmException("dm.msg.not.send.clsEmpty", request);
			}
		}
		// 분류체계 기본VO 
		DmClsBVo dmClsBVo = null;
		boolean emptyCls = false;
		for(DmClsRVo storedDmClsRVo : boundDmClsRVoList){
			if("ap".equals(fromTyp)){
				dmClsBVo = new DmClsBVo();
				dmClsBVo.setStorId(storId);
				dmClsBVo.setClsId(storedDmClsRVo.getClsId());
				dmClsBVo = (DmClsBVo)commonDao.queryVo(dmClsBVo);
				if(dmClsBVo == null){
					if(emptyCls) continue;
					// 미분류로 저장
					storedDmClsRVo.setClsId(DmConstant.EMPTY_CLS);
					emptyCls = true;
				}
			}
			storedDmClsRVo.setTableName(tableName);
			storedDmClsRVo.setDocGrpId(dmDocLVo.getDocGrpId());
			queryQueue.insert(storedDmClsRVo);
		}
		
		// 원본 업무[brd:게시판, apv:결재]
		String docTyp = "brd";
		
		// 대상 업무ID[공용,개인]			
		String tabId = paramMap == null || !paramMap.containsKey("tabId") || "psn".equals(paramMap.get("tabId")) ? "psn" : "doc";
		
		// 게시판만 파일을 복사하므로 docTyp를 초기 세팅해준다.[이후 연계 업무 추가되면 변경] 
		// 파일복사
		if(fileVoList != null && fileVoList.size()>0 && "brd".equals(docTyp)) dmFileSvc.copySendFile(request, dmDocLVo.getDocId(), queryQueue, fileVoList, tableName, tabId, docTyp, copyFileList, dmDocLVo.getRegrUid());
		
		// queryQueue가 문서서비스 내에서 생성되었을 경우에는 execute 해준다.
		if(nullQueryQueue) {
			commonDao.execute(queryQueue);
			//이메일 발송
			if("S".equals(dmDocLVo.getStatCd())) sendDiscDocEmail(request, langTypCd, dmDocLVo.getSubj(), "S", dmDocLVo.getDiscrUid(), userVo, null, null, "disc");
		}

		// 파일 복사
		if("brd".equals(docTyp) && copyFileList.size()>0){
			dmFileSvc.copyFileList(request, "doc", copyFileList);
		}
		
		return returnMap;
				
	}
	
	/** 권한체크 제외 */
	public boolean chkNotAuth(String path){
		String suffix = dmCmSvc.getPathSuffix(path);
		if(ArrayUtil.isStartsWithArray(DmConstant.NOT_AUTH_SUFFIX, suffix))
			return true;
		return false;
	}
	
	/** 상태코드체크 */
	public void chkDocStatCd(HttpServletRequest request, String actKey, String statCd) throws CmException{
		if(statCd == null || statCd.isEmpty()) return;
		if("update".equals(actKey)){
			if(ArrayUtil.isInArray(DmConstant.NOT_MOD_STAT_CD, statCd)){
				LOGGER.error("[ERROR] check statCd - actKey : "+actKey+"\tstatCd : "+statCd);
				//dm.msg.transDoc.not.mod01=진행상태가 '{0}'인 문서는 수정할 수 없습니다.
				throw new CmException("dm.msg.transDoc.not.mod01", new String[]{"#dm.cols.docStat"+statCd}, request);
			}
		}
	}
	
	/** 권한체크 */
	public boolean chkDocAuth(Map<String,String> authMap, String chkAuth, String path) {
		//if(!chkNotAuth(path) && !authMap.containsKey(chkAuth))
		if(!authMap.containsKey(chkAuth))
			return false;
		return true;
	}
	
	/** 권한체크[배열] */
	public boolean chkDocAuths(Map<String,String> authMap, String path, String... chkAuths) {
		for(String auth : chkAuths){
			if(!chkDocAuth(authMap, auth, path)) {
				return false;
			}
		}
		return true;
	}
	
	/** 문서의 보안등급 체크 */
	public boolean chkDocSeculCd(UserVo userVo, DmDocLVo dmDocLVo, boolean isDocAuth) throws SQLException{
		// 권한체크
		if(!isDocAuth) return false;
		// 사용자의 보안등급
		String seculCd = userVo.getSeculCd();
		// 문서의 보안등급
		String docSeculCd = dmDocLVo.getSeculCd();
		// 문서의 보안등급이 없으면 true
		if(docSeculCd == null || docSeculCd.isEmpty() || DmConstant.EMPTY_CLS.toLowerCase().equals(docSeculCd)) return true;
		// 소유자 비교
		if(userVo.getUserUid().equals(dmDocLVo.getOwnrUid())) return true;
		// 작성자 비교
		if(userVo.getUserUid().equals(dmDocLVo.getRegrUid())) return true;
		// 사용자의 보안등급이 없으면 false
		//if(seculCd == null || seculCd.isEmpty()) return false;
		// 포탈 보안등급코드 조회
		List<PtCdBVo> ptCdBVoList = getPtSeculCdList(userVo.getCompId(), DmConstant.SECUL_CD);
		if(ptCdBVoList == null) return false;
		// 사용자의 보안등급코드 포함 하위 목록 조회
		List<String> seculCdList = getSeculCdDownList(ptCdBVoList, seculCd);
		if(seculCdList != null && seculCdList.size()>0) {
			boolean isChk = false;
			// 사용자의 보안등급 하위 목록중에 문서보안등급이 있으면 true
			for(String cd : seculCdList){
				if(cd.equals(docSeculCd)){
					isChk = true;
					break;
				}
			}
			return isChk;
		}
		return false;
	}
	
	/** 권한 VO 생성*/
	public void makeDmAuthDVo(List<DmAuthDVo> dmAuthDVoList, String clsCd, String authVa){
		DmAuthDVo dmAuthDVo = new DmAuthDVo();
		if(clsCd == null) clsCd = "DEFAULT";
		dmAuthDVo.setClsCd(clsCd);
		dmAuthDVo.setAuthVa(authVa);
		dmAuthDVo.setUseYn("Y");
		dmAuthDVoList.add(dmAuthDVo);
	}
	
	/** 권한 VO 배열 생성*/
	public void makeDmAuthDVos(List<DmAuthDVo> dmAuthDVoList, String... authVas ){
		for(String authVa : authVas){
			makeDmAuthDVo(dmAuthDVoList, null, authVa);
		}
	}
	
	/** 권한 생성*/
	public void makeString(List<String> list, String authVa){
		list.add(authVa);
	}
	
	/** 권한 배열 생성*/
	public void makeListStrings(List<String> list, String... authVas ){
		for(String authVa : authVas){
			makeString(list, authVa);
		}
	}
	
	/** 메뉴별 기본 기능(버튼) 적용 및 권한 적용
	 *  includeList : 포함할 권한목록
	 *  excludeList : 제외할 권한목록
	 *  chkList : 체크할 권한목록 
	 * */
	public boolean setUserAuth(String path, List<String> includeList, List<String> excludeList, List<String> chkList, String statCd, boolean isAdmin){
		String suffix = dmCmSvc.getPathSuffix(path);
		if(ArrayUtil.isStartsWithArray(DmConstant.NOT_AUTH_SUFFIX, suffix)){
			if(path.startsWith("list")){
				if(suffix.startsWith("Psn"))
					makeListStrings(includeList,"setDoc"); // 등록
				else if(suffix.startsWith("Recycle") && isAdmin)
					makeListStrings(includeList,"recovery","disuse","move"); // 복원, 완전삭제, 이동
				else if(suffix.startsWith("TransferTgt")){
					if(isAdmin)	makeListStrings(includeList,"transferHst","transferAll","transferSelect"); // 이관이력, 전체이관, 선택이관
					else makeListStrings(includeList,"tranHst","tranSelect"); // 이관이력, 선택이관
				}else if(suffix.startsWith("TransferDoc")){// 이관문서
					
				}else if(suffix.startsWith("TransTgt")){// 인계[문서조회]
					makeListStrings(includeList,"transTgtSave"); // 선택인계
				}else if(suffix.startsWith("TransWait")){// 인계대기문서
					makeListStrings(includeList,"transTgtUpdate","transReSave","transTgtCancel"); // 인계대상변경, 재인계, 인계취소
				}else if(suffix.startsWith("Takovr")){// 인수대기문서
					makeListStrings(includeList,"transTakovrApvd","transTakovrRjt"); // 승인,거부
				}else if(suffix.startsWith("OpenApv")){//문서열람승인
					makeListStrings(includeList,"openApv"); // 열람요청승인
				}
			}else if(path.startsWith("set")){
				if(suffix.startsWith("Tmp"))
					makeListStrings(includeList,"delete","save","tmpSave","fld","cls","keepDdln","seculCd","owner","psn"); // 삭제, 저장, 임시저장
				else if(suffix.startsWith("Sub"))
					makeListStrings(includeList,"save","tmpSave","psn"); // 삭제, 저장, 임시저장
				else if(suffix.startsWith("Psn"))
					makeListStrings(includeList,"save"); // 저장
			}else if(path.startsWith("view")){
				if(suffix.startsWith("Disc")){
					if("S".equals(statCd)) makeListStrings(includeList,"saveDisc"); // 승인,반려
				}else if(suffix.startsWith("Subm")){
					if("S".equals(statCd) || "R".equals(statCd)) makeListStrings(includeList,"disuse"); // 삭제
				}else if(suffix.startsWith("Recycle")){
					makeListStrings(chkList,"recovery","disuse"); // 복원, 완전삭제
					return true;
				}else if(suffix.startsWith("Psn")){
					makeListStrings(includeList,"setDoc"); // 수정
				}else if(suffix.startsWith("TransferDoc")){// 이관문서
					
				}else if(suffix.startsWith("OpenReq")){//문서열람요청
					makeListStrings(includeList,"view","print"); // 인쇄
				}else if(suffix.startsWith("OpenApv")){//문서열람승인
					makeListStrings(includeList,"view","openApv"); // 열람요청승인
				}
			}
			return false;
		}else{
			if(path.startsWith("list")){
				if(suffix.startsWith("Transfer")){// 이관문서
					if(isAdmin)	{
						if(suffix.startsWith("TransferDtl")) makeListStrings(includeList,"transferDel"); // 전체,선택삭제
						else makeListStrings(includeList,"disuse","send","setUpdate"); // 완전삭제, 보내기, 수정
					}
				}else{
					if(isAdmin)	makeListStrings(includeList,"setDoc","disuse","delete","send","move"); // 등록, 완전삭제, 휴지통, 보내기, 이동
					else makeListStrings(includeList,"setDoc"); // 등록
				}
				return false;
			}else if(path.startsWith("set")){
				if(isAdmin) {
					makeListStrings(includeList,"update","owner","fld","cls","keepDdln","seculCd","docNoMod"); // 수정, 소유자, 폴더, 분류, 보존연한, 보안등급, 문서번호변경
					return false;
				}else makeListStrings(chkList,"update","owner","fld","cls","keepDdln","seculCd","docNoMod"); // 수정, 소유자, 폴더, 분류, 보존연한, 보안등급, 문서번호변경
			}else if(path.startsWith("view")){
				if(suffix.startsWith("Transfer")){// 이관문서
					if(isAdmin){ // 관리자 일 경우 전체권한을 세팅한다.
						if(suffix.startsWith("TransferDtl")) makeListStrings(includeList,"view","download","print","email","disuse"); // 조회,다운로드,인쇄,이메일,완전삭제
						else makeListStrings(includeList,"view","download","docHst","print","send","email","disuse","setUpdate"); // 조회,다운로드,문서이력,인쇄,보내기,이메일,완전삭제, 기본정보수정
						return false;
					}else{
						makeListStrings(chkList,"view","download","docHst","print"); // 조회,다운로드,문서이력,인쇄
						return true;
					}
				}else{
					if(isAdmin){ // 관리자 일 경우 전체권한을 세팅한다.
						makeListStrings(includeList,DmConstant.AUTH_CDS); // 전체 권한추가
						makeListStrings(excludeList,"recovery"); // 복원
						return false;
					}else{
						makeListStrings(excludeList,"recovery"); // 복원
						makeListStrings(includeList,"setSubDoc"); // 하위문서
						if(!suffix.startsWith("Bumk")) makeListStrings(includeList,"bumk"); // 즐겨찾기
					}
				}
				
			}
		}
		return true;
	}
	
	/** 권한 목록 조회 [보안등급, 역할, 소유자, 등록자] */
	@SuppressWarnings("unchecked")
	public List<DmAuthDVo> getDmAuthDVoList(String compId, String clsCd, String cd, String authVa) throws SQLException {
		DmAuthDVo dmAuthDVo = new DmAuthDVo();
		dmAuthDVo.setCompId(compId);
		dmAuthDVo.setClsCd(clsCd);
		if(cd!=null) dmAuthDVo.setCd(cd);
		if(authVa!=null) dmAuthDVo.setAuthVa(authVa);
		dmAuthDVo.setUseYn("Y");
		return (List<DmAuthDVo>)commonDao.queryList(dmAuthDVo);
	}
	
	/** 보안등급 권한 목록 조회 [조회권한] */
	@SuppressWarnings("unchecked")
	public List<String> getDmSeculCdList() throws SQLException {
		DmAuthDVo dmAuthDVo = new DmAuthDVo();
		dmAuthDVo.setClsCd(DmConstant.SECUL_CD);
		dmAuthDVo.setAuthVa("seculCd");
		dmAuthDVo.setUseYn("Y");
		List<DmAuthDVo> list = (List<DmAuthDVo>)commonDao.queryList(dmAuthDVo);
		List<String> returnList = new ArrayList<String>();
		for(DmAuthDVo storedDmAuthDVo : list){
			returnList.add(storedDmAuthDVo.getCd());
		}
		return returnList;
	}
	
	/** 권한 목록을 맵으로 변환 */
	public void setAuthMap(ModelMap model, Map<String,String> authMap, String mapNm, List<String> includeList, String[] excludeList, String[] chkList){
		// 권한 목록을 맵으로 변환한다.
		boolean isChk = chkList.length>0;
		for(String authVa : includeList){
			if(isChk && !ArrayUtil.isInArray(chkList, authVa)) continue;
			if(excludeList.length>0 && ArrayUtil.isInArray(excludeList, authVa)) continue;
			if(authMap.containsKey(authVa)) continue;
			authMap.put(authVa, authVa);
		}
		if(model != null) {
			model.put(mapNm == null ? "authMap" : mapNm, authMap);
			//model.put(mapNm == null ? "authJson" : mapNm+"Json", JsonUtil.toJson(authMap));
		}
	}
	
	/** 문서권한 목록 조회 */
	public void setDmAuthDVoList(boolean isMobile, String storId, UserVo userVo, DmDocLVo dmDocLVo, List<DmAuthDVo> dmAuthDVoList) throws SQLException{
		// 임시 배열
		List<DmAuthDVo> tmpList = null;
		
		// 사용자 보안등급코드['none'(없음)이면 스킵]
		String seculCd = userVo.getSeculCd();
		if(seculCd != null && !seculCd.isEmpty()){
			tmpList = getDmAuthDVoList(userVo.getCompId(), DmConstant.SECUL_CD, seculCd, null);
			if(tmpList != null && !tmpList.isEmpty()) dmAuthDVoList.addAll(tmpList);
		}
		
		// 사용자 권한그룹코드(웹,모바일)
		//String authGrpCd = isMobile ? DmConstant.AUTH_GRP_M : DmConstant.AUTH_GRP_U;
		
		/*if(isMobile){
			// 시스템 정책 조회
			Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();

			// 모바일 사용 여부
			if(!sysPlocMap.containsKey("mobileEnable") || "N".equals(sysPlocMap.get("mobileEnable"))){
				authGrpCd = null;
			}
		}
		if(authGrpCd != null){
			//사용자 권한그룹을 체크한다.
			String[] userAuthGrpIds = userVo.getUserAuthGrpIds();
			if(userAuthGrpIds != null){
				for(String userAuthGrpId : userAuthGrpIds){
					tmpList = getDmAuthDVoList(userVo.getCompId(), authGrpCd, userAuthGrpId, null);
					if(tmpList == null || tmpList.isEmpty()) continue;
					dmAuthDVoList.addAll(tmpList);
				}
			}
		}*/
		
		// 소유자, 등록자 조회
		if(dmDocLVo != null){
			boolean isOwnr = dmDocLVo.getOwnrUid().equals(userVo.getUserUid());
			boolean isRegr = dmDocLVo.getRegrUid().equals(userVo.getUserUid());
			// 소유자
			if(isOwnr){
				tmpList = getDmAuthDVoList(userVo.getCompId(), DmConstant.AUTH_CLS_CD, "OWNR", null);
				if(tmpList != null && !tmpList.isEmpty()) dmAuthDVoList.addAll(tmpList);
			}
			// 등록자
			if(isRegr){
				tmpList = getDmAuthDVoList(userVo.getCompId(), DmConstant.AUTH_CLS_CD, "REGR", null);
				if(tmpList != null && !tmpList.isEmpty()) dmAuthDVoList.addAll(tmpList);
			}
		}
	}
	
	/** 권한조회 맵 [보안등급, 역할, 소유자, 등록자] */
	public Map<String,String> getAuthMap(ModelMap model, boolean isAdmin, DmDocLVo dmDocLVo, 
			UserVo userVo, String storId, String langTypCd, String path, boolean isMobile) throws Exception {
		// 권한을 담을 맵
		Map<String,String> authMap = new HashMap<String,String>();
		// 권한 목록 배열		
		List<DmAuthDVo> dmAuthDVoList = new ArrayList<DmAuthDVo>();
		
		// 메뉴별 기본 권한 적용
		/*boolean isAuth = isDftAuth(path, dmAuthDVoList);
		if(dmAuthDVoList.size()>0){
			setAuthMap(model, new HashMap<String,DmAuthDVo>(), dmAuthDVoList, "authDftMap");
			dmAuthDVoList = new ArrayList<DmAuthDVo>();
		}*/
		// 추가로 포함할 권한
		List<String> includeList = new ArrayList<String>();
		
		// 삭제할 권한
		List<String> excludeList = new ArrayList<String>();
		
		// 메뉴 별 한정된 권한(휴지통-완전삭제,복원)을 체크한다.
		List<String> chkList = new ArrayList<String>();
		
		// 상태코드
		String statCd = dmDocLVo != null && dmDocLVo.getStatCd() != null && !dmDocLVo.getStatCd().isEmpty() ? dmDocLVo.getStatCd() : null; 
		// 기본 권한 목록
		if(setUserAuth(path, includeList, excludeList, chkList, statCd, isAdmin)){
			// 문서권한 목록에 추가
			setDmAuthDVoList(isMobile, storId, userVo, dmDocLVo, dmAuthDVoList);
			//setAuthMap(model, authMap, dmAuthDVoList, null);
		}
		if(dmAuthDVoList.size()>0){
			for(DmAuthDVo storedDmAuthDVo : dmAuthDVoList){
				includeList.add(storedDmAuthDVo.getAuthVa());
			}
		}
		if(includeList.size()>0)
			setAuthMap(model, authMap, null, includeList, ArrayUtil.toArray(excludeList), ArrayUtil.toArray(chkList));
		return authMap;
	}
	
	/** 즐겨찾기 저장 */
	public void saveBumkDoc(HttpServletRequest request, QueryQueue queryQueue, String storId, String bumkId, String regCat, String catVa, boolean isDel) throws SQLException, CmException {
		// 즐겨찾기[개인] 조회
		DmBumkBVo dmBumkBVo = new DmBumkBVo();
		dmBumkBVo.setBumkId(bumkId);
		dmBumkBVo = (DmBumkBVo)commonDao.queryVo(dmBumkBVo);
		if(dmBumkBVo == null) {
			// dm.msg.empty.bumkList=즐겨찾기가 없습니다.
			throw new CmException("dm.msg.empty.bumkList", request);
		}
		
		DmBumkLVo dmBumkLVo = new DmBumkLVo();
		dmBumkLVo.setStorId(storId);
		dmBumkLVo.setBumkId(bumkId);
		if(regCat != null) dmBumkLVo.setRegCat(regCat);
		if(catVa != null) dmBumkLVo.setCatVa(catVa);
		
		if(isDel){
			queryQueue.delete(dmBumkLVo);
			return;
		}
		if(commonDao.count(dmBumkLVo)>0)
			// dm.msg.not.transBumkDoc=즐겨찾기에 등록되어 있습니다.
			throw new CmException("dm.msg.not.transBumkDoc", request);
		
		dmBumkLVo.setSortOrdr(0);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		dmBumkLVo.setRegrUid(userVo.getUserUid());
		dmBumkLVo.setRegDt("sysdate");
		queryQueue.insert(dmBumkLVo);
	}
	
	/** 즐겨찾기 조회 */
	@SuppressWarnings("unchecked")
	public List<DmBumkBVo> getDmBumkBVoList(String langTypCd, String bumkCat, UserVo userVo, String orderBy ) throws SQLException {
		// 즐겨찾기[개인] 조회
		DmBumkBVo dmBumkBVo = new DmBumkBVo();
		dmBumkBVo.setQueryLang(langTypCd);
		if(bumkCat != null){
			dmBumkBVo.setBumkCat(bumkCat);//개인|회사|부서
			if("C".equals(bumkCat))
				dmBumkBVo.setRegrUid(userVo.getCompId());
			else if("D".equals(bumkCat))
				dmBumkBVo.setRegrUid(userVo.getOrgId());
			else
				dmBumkBVo.setRegrUid(userVo.getUserUid());
		}
		
		if(orderBy == null) dmBumkBVo.setOrderBy("SORT_ORDR");
		return (List<DmBumkBVo>)commonDao.queryList(dmBumkBVo);
	}
	
	/** 즐겨찾기 등록 건수 조회 */
	public Integer getDmBumkBVoListCnt(String storId, String bumkId, String regCat, String catVa) throws SQLException {
		DmBumkLVo dmBumkLVo = new DmBumkLVo();
		dmBumkLVo.setStorId(storId);
		dmBumkLVo.setBumkId(bumkId);
		dmBumkLVo.setRegCat(regCat);
		dmBumkLVo.setCatVa(catVa);
		
		return commonDao.count(dmBumkLVo);
	}
	
	/** 개인문서 전체 삭제[즐겨찾기,폴더,문서] */
	public void delPsnDocAll(HttpServletRequest request, QueryQueue queryQueue, String storId, String regrUid, List<CommonFileVo> deletedFileList) throws SQLException, CmException {
		// 즐겨찾기VO
		DmBumkBVo dmBumkBVo = new DmBumkBVo();
		dmBumkBVo.setRegrUid(regrUid);
		
		// 즐겨찾기기본(DM_BUMK_B) 테이블
		@SuppressWarnings("unchecked")
		List<DmBumkBVo> dmBumkBVoList = (List<DmBumkBVo>)commonDao.queryList(dmBumkBVo);
		for(DmBumkBVo storedDmBumkBVo : dmBumkBVoList){
			// 즐겨찾기목록 삭제
			saveBumkDoc(request, queryQueue, storId, storedDmBumkBVo.getBumkId(), null, null, true);
		}
		
		// 즐겨찾기기본 삭제
		queryQueue.delete(dmBumkBVo);
		
		// 폴더 삭제
		DmFldBVo newDmFldBVo = new DmFldBVo(null);
		newDmFldBVo.setRegrUid(regrUid);
		queryQueue.delete(newDmFldBVo);
		
		// DmDocLVo 초기화
		DmDocLVo dmDocLVo = newDmDocLVo();
		dmDocLVo.setRegrUid(regrUid);
		@SuppressWarnings("unchecked")
		List<DmDocLVo> dmDocLVoList = (List<DmDocLVo>)commonDao.queryList(dmDocLVo);
		// 삭제할 문서ID 목록
		List<String> docIdList = new ArrayList<String>();
		for(DmDocLVo storedDmDocLVo : dmDocLVoList){
			docIdList.add(storedDmDocLVo.getDocId());
		}
		// 삭제할 파일 목록			
		String[] docIds = ArrayUtil.toArray(docIdList);
		for(String id : docIds){
			dmDocLVo = newDmDocLVo();
			dmDocLVo.setDocId(id);
			dmDocLVo.setRegrUid(regrUid);
			delPsnDoc(request, queryQueue, dmDocLVo, id, regrUid, deletedFileList);
		}
	}
	
	/** 개인폴더 조회 */
	@SuppressWarnings("unchecked")
	public List<DmFldBVo> getDmFldBVoList(String langTypCd, String storId, String fldId, String userUid, String orderBy ) throws SQLException {
		// 개인폴더 조회
		DmFldBVo dmFldBVo = new DmFldBVo(storId);
		dmFldBVo.setQueryLang(langTypCd);
		dmFldBVo.setRegrUid(userUid);
		if(storId != null) dmFldBVo.setStorId(storId);
		if(fldId != null) dmFldBVo.setFldId(fldId);
		
		if(orderBy == null) dmFldBVo.setOrderBy("SORT_ORDR");
		return (List<DmFldBVo>)commonDao.queryList(dmFldBVo);
	}
	
	/** 문서유형 상세조회 */
	public DmCatBVo getDmFldCatBVo(String langTypCd, String storId, String catId, String dftYn) throws SQLException {
		DmCatBVo dmCatBVo = new DmCatBVo();
		dmCatBVo.setQueryLang(langTypCd);
		dmCatBVo.setStorId(storId);
		if(catId != null) dmCatBVo.setCatId(catId);
		if(dftYn != null) dmCatBVo.setDftYn(dftYn);
		return (DmCatBVo)commonDao.queryVo(dmCatBVo);
	}
	
	/** 개인폴더 상세 조회 */
	/*public DmFldBVo getDmPsnFldBVo(String commFldId, String userUid) throws SQLException {
		DmFldBVo dmFldBVo = new DmFldBVo(null);
		dmFldBVo.setCommFldId(commFldId);
		if(userUid != null && !userUid.isEmpty()) dmFldBVo.setRegrUid(userUid);
		return (DmFldBVo)commonDao.queryVo(dmFldBVo);
	}*/
	
	/** 폴더 조회 */
	public DmFldBVo getDmFldBVo(String storId, String fldId, String langTypCd) throws SQLException {
		// 저장소ID가 있을경우 캐쉬맵에서 조회한다.
		if(storId != null && !storId.isEmpty()) return dmAdmSvc.getFldBVo(storId, fldId, langTypCd);
		DmFldBVo dmFldBVo = new DmFldBVo(storId);
		dmFldBVo.setQueryLang(langTypCd);
		dmFldBVo.setFldId(fldId);
		return (DmFldBVo)commonDao.queryVo(dmFldBVo);
	}
	
	/** 개인 폴더 세팅 */
	public void setPsnFldList(List<DmFldBVo> dmFldBVoList, String storId, String langTypCd, String userUid) throws SQLException {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		
		//개인 최상위 폴더 세팅
		dmFldBVoList.add(dmAdmSvc.makeDmFldVo(storId, DmConstant.FLD_PSN, DmConstant.FLD_PSN, messageProperties.getMessage("dm.cols.psnFld", request), "ROOT", "F", 3, "Y"));
		//개인 폴더 조회
		List<DmFldBVo> psnFldList = getDmFldBVoList(langTypCd, null, null, userUid, null); 
		if(psnFldList.size()>0){
			dmFldBVoList.addAll(psnFldList);
		}
	}
	
	/** 문서 삭제[개인] */
	public void delPsnDoc(HttpServletRequest request, QueryQueue queryQueue, DmDocLVo dmDocLVo, String docId, String userUid, List<CommonFileVo> deletedFileList) throws SQLException, CmException{
		// 첨부파일 삭제
		if(deletedFileList != null) deletedFileList.addAll(dmFileSvc.deleteDmFile(docId, queryQueue, null));
		// 문서 삭제
		queryQueue.delete(dmDocLVo);
	}
	
	/** 목록을 맵으로 변환 */
	//@SuppressWarnings("unchecked")
	public void setPsnDocMapList(HttpServletRequest request, List<DmDocLVo> dmDocLVoList, List<Map<String, Object>> mapList, UserVo userVo) throws SQLException, CmException{
		for(DmDocLVo storedDmDocLVo : dmDocLVoList){
			// 첨부파일(DM_FILE_D) 테이블 - SELECT COUNT
			DmFileDVo dmFileDVo = new DmFileDVo(null);
			dmFileDVo.setRefId(storedDmDocLVo.getDocId());
			storedDmDocLVo.setFileCnt(commonDao.count(dmFileDVo));
			mapList.add(VoUtil.toMap(storedDmDocLVo, null));
		}
	}
	
	/** 열람요청 여부 체크 */
	public boolean isViewReqChk(DmPubDocTgtDVo dmPubDocTgtDVo, UserVo userVo) throws SQLException{
		int cnt = 0;
		// 현재날짜 기준 열람조회요청건이 있는지 체크
		dmPubDocTgtDVo.setStatCd("A"); // 승인
		
		// 사용자 조회
		dmPubDocTgtDVo.setTgtTypCd("U");
		dmPubDocTgtDVo.setTgtId(userVo.getUserUid());
		
		cnt = commonDao.count(dmPubDocTgtDVo);
		if(cnt==0){
			dmPubDocTgtDVo.setStatCd("S"); // 요청
			cnt = commonDao.count(dmPubDocTgtDVo);
		}
		
		if(cnt==0){
			// 부서 조회
			dmPubDocTgtDVo.setTgtTypCd("D");
			dmPubDocTgtDVo.setTgtId(userVo.getOrgId());
			cnt = commonDao.count(dmPubDocTgtDVo);
		}
		if(cnt==0){
			dmPubDocTgtDVo.setStatCd("A"); // 승인
			cnt = commonDao.count(dmPubDocTgtDVo);
		}
		
		return cnt>0 ? true : false;
	}
	
	/** 조회옵션 적용여부[true:권한적용,false:미적용]*/
	public boolean isDocListOpt(String compId) throws SQLException{
		// 환경설정
		Map<String,String>	envConfigMap = dmAdmSvc.getEnvConfigMap(null, compId);
		if(envConfigMap == null) return false;
		String listOpt = envConfigMap.get("listOpt");
		return listOpt != null && !listOpt.isEmpty() && "B".equals(listOpt) ? true : false;
	}
	
	/** 문서이관 - 저장*/
	@SuppressWarnings("unchecked")
	public void saveTransferDoc(HttpServletRequest request, QueryQueue queryQueue, String fromTableName, String toTableName, 
			String docGrpId, UserVo userVo, String actKey, String fromStorId, String toStorId, List<CommonFileVo> deletedFileList) throws Exception{
		//패키지 경로[추후 클래스를 통한 패키지 자동 매핑]
		String pakage = DmConstant.pakage;
		if(actKey == null || actKey.isEmpty()) actKey = "";
		Class<? extends CommonVo> clazz = null;
		//이관할 VO 명 배열
		CommonVo commonVo,subCommonVo;
		String tmpId = null;
		List<CommonVo> commonVoList = null, subVoList = null;
			String voNm;
			String[] subVos = {"DmFileD"};
			for(String nm : DmConstant.CREATE_TBLS){
				if("DmFileD".equals(nm)) continue;
				voNm = pakage + "."+nm;
				voNm+="Vo";
				clazz = (Class<? extends CommonVo>)Class.forName(voNm);
				commonVo = clazz.newInstance();
				// 생성테이블에 테이블명 세팅
				VoUtil.setValue(commonVo, "tableName", fromTableName);
				VoUtil.setValue(commonVo, "docGrpId", docGrpId);
				
				if("insert".equals(actKey)){
					// 문서VO에 내용조회여부 true로 변경
					if(nm.startsWith("DmDocL")) {
						VoUtil.setValue(commonVo, "withLob", true);
						VoUtil.setValue(commonVo, "addItemNmList", getDmAddItemList());
					}
					commonVoList = (List<CommonVo>)commonDao.queryList(commonVo);
					for(CommonVo storedCommonVo : commonVoList){
						// 생성테이블에 테이블명 세팅
						VoUtil.setValue(storedCommonVo, "tableName", toTableName);
						if(nm.startsWith("DmDocD")) {
							VoUtil.setValue(storedCommonVo, "statCd", "C");
							tmpId = ((DmDocDVo)storedCommonVo).getFldId();
							// 폴더조회 - 이관할 저장소에 폴더가 없으면 미분류로 변경한다.
							if(!DmConstant.EMPTY_CLS.equals(tmpId) && getDmFldBVo(toStorId, tmpId, "ko") == null){
								VoUtil.setValue(storedCommonVo, "fldId", DmConstant.EMPTY_CLS);
							}
							
							/** 검색 색인 데이터를 더함 */
							//addSrchIndex(docGrpId, userVo, queryQueue, "I");
						}
						queryQueue.insert(storedCommonVo);
						
						// 문서ID 단위 테이블 이관
						if(nm.startsWith("DmDocL")) {
							for(String subNm : subVos){
								voNm = pakage + "."+subNm;
								voNm+="Vo";
								clazz = (Class<? extends CommonVo>)Class.forName(voNm);
								commonVo = clazz.newInstance();
								VoUtil.setValue(commonVo, "tableName", fromTableName);
								VoUtil.setValue(commonVo, "refId", ((DmDocLVo)storedCommonVo).getDocId());
								subVoList = (List<CommonVo>)commonDao.queryList(commonVo);
								for(CommonVo subVo : subVoList){
									// 생성테이블에 테이블명 세팅
									VoUtil.setValue(subVo, "tableName", toTableName);
									queryQueue.insert(subVo);
								}
							}
						}
					}
				}else if("delete".equals(actKey)){
					if(nm.startsWith("DmDocL")) {
						commonVoList = (List<CommonVo>)commonDao.queryList(commonVo);
						for(CommonVo storedCommonVo : commonVoList){
							for(String subNm : subVos){
								voNm = pakage + "."+subNm;
								voNm+="Vo";
								clazz = (Class<? extends CommonVo>)Class.forName(voNm);
								subCommonVo = clazz.newInstance();
								VoUtil.setValue(subCommonVo, "tableName", fromTableName);
								if(subNm.startsWith("DmFileD")) {
									VoUtil.setValue(subCommonVo, "refId", ((DmDocLVo)storedCommonVo).getDocId());
									// 첨부파일 삭제
									if(deletedFileList != null) deletedFileList.addAll(dmFileSvc.deleteDmFile(((DmDocLVo)storedCommonVo).getDocId(), queryQueue, fromTableName));
								}
								else VoUtil.setValue(subCommonVo, "docId", ((DmDocLVo)storedCommonVo).getDocId());
								queryQueue.delete(subCommonVo);
							}
						}
					}else if(nm.startsWith("DmDocD")) {//문서상세
						// 저장소문서관계 삭제
						//dmStorSvc.saveStorId(queryQueue, fromStorId, docGrpId, true);
						/** 검색 색인 데이터를 삭제 - 삭제할 필요없음 */
						//addSrchIndex(docGrpId, userVo, queryQueue, "D");
					}
					queryQueue.delete(commonVo);
				}
				
			}
			// 저장 일 경우에만 트랜잭션 개별 처리
			if("insert".equals(actKey)) commonDao.execute(queryQueue);
			//return "success";		
	}
	
	/** 이전 다음 일정 세팅 */
    public String getDateOfDay(  String startDay , String tabType , String pmVal , String clicknps , Integer stepValue){
 		if(startDay != null && ( pmVal == null || "".equals(pmVal) )){
 			return startDay;
 		}
 		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
 		GregorianCalendar cal = new GregorianCalendar ( );
 		String daysValue = "";
 		int step = stepValue == null ? 1 : stepValue.intValue();
 		if(startDay != null && !"".equals(startDay)){
 			cal.set(Integer.parseInt(startDay.split("-")[0]) , Integer.parseInt(startDay.split("-")[1])-1 , Integer.parseInt(startDay.split("-")[2]));
 	 		
 	 		if("month".equals(tabType)){
 	 			if("p".equals(pmVal))
 	 				cal.add(Calendar.MONTH, step);
 	 			else
 	 				cal.add(Calendar.MONTH, -step);
 	 		}else if("week".equals(tabType) || "weeks".equals(tabType)){
 	 			if("p".equals(pmVal))
 	 				cal.add(Calendar.DATE, (7*step));
 	 			else
 	 				cal.add(Calendar.DATE, -(7*step));
 	 		}else if("day".equals(tabType) ){
 	 			if("p".equals(pmVal))
 	 				cal.add(Calendar.DATE, step);
 	 			else{
 	 				cal.add(Calendar.DATE, -step);
 	 			}
 	 		}else if( "year".equals(tabType) ){
 	 			if("p".equals(pmVal))
 	 				cal.add(Calendar.YEAR, step);
 	 			else
 	 				cal.add(Calendar.YEAR, -step);
 	 		}else if("all".equals(tabType)){
 	 			if("p".equals(pmVal))
	 	  			cal.add(Calendar.DATE, step);
	 	  		else
	 	  			cal.add(Calendar.DATE, -step);
 	 		}
 	 		Date days = cal.getTime();
 	 		daysValue = sdf.format(days);
 		}else{
 			daysValue = sdf.format((Date)(cal.getTime()));
 		}
        
 		return daysValue;
 	}
    
	/** 검색 색인 데이터를 더함 */
	public void addSrchIndex(String docGrpId, UserVo userVo, QueryQueue queryQueue, String actId) throws SQLException{
		// 시스템 정책 조회
		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
		if(sysPlocMap.get("integratedSearchEnable")==null || !"Y".equals(sysPlocMap.get("integratedSearchEnable")))//통합검색 사용여부
			return;
		if(docGrpId == null || docGrpId.isEmpty()) return;
		try{
			UserVo adminVo = ptSecuSvc.createEmptyAdmin(userVo.getCompId(), userVo.getLoutCatId(), userVo.getLangTypCd());
			String docLstTypCd = "docLst";
			// 검색엔진 인덱싱 처리
			CmSrchBVo cmSrchBVo = emSrchSvc.createVo();
			cmSrchBVo.setCompId(userVo.getCompId());
			cmSrchBVo.setMdRid("DM");
			cmSrchBVo.setMdBxId(docLstTypCd);
			cmSrchBVo.setMdId(docGrpId);
			// 리소스ID세팅
			ArrayList<PtMnuDVo> mnuList = ptSecuSvc.getAuthedMnuVoListByMdRid(adminVo, "DM");
			
			if(mnuList!=null){
				for(PtMnuDVo ptMnuDVo : mnuList){
					if(ptMnuDVo.getMdId().equals(docLstTypCd)){
						cmSrchBVo.setMdBxRescId(ptMnuDVo.getRescId());
						break;
					}
				}
			}
			// 모듈함리소스ID 가 없을 경우(메뉴에 등록되지 않았거나 해당 메뉴에 모듈ID가 지정되어 않은경우) 테이블에 저장하지 않는다.
			if(cmSrchBVo.getMdBxRescId() == null || cmSrchBVo.getMdBxRescId().isEmpty()) {
				throw new NullPointerException("[addSrchIndex insert("+actId+") fail!!] - mdBxRescId is null\n docId : "+docGrpId);
			}
			//TODO - I:INSERT, U:UPDATE, D:DELETE, C:CATEGORY UPDATE
			if(actId == null ) actId = "I";
			cmSrchBVo.setActId(actId);
			String url = "/cm/dm/redirect.do?docGrpId="+docGrpId;
			//if(!"docLst".equals(docLstTypCd)) url+="&paramStorId="+dmDocLVo.getStorId();
			cmSrchBVo.setUrl(url);
			cmSrchBVo.setRegDt("sysdate");
			queryQueue.insert(cmSrchBVo);
		}catch(SQLException se){
			LOGGER.error("[addSrchIndex insert("+actId+") fail!!] - docId : "+docGrpId);
		}catch(NullPointerException npe){
			LOGGER.error(npe.getMessage());
		}
	}
	
	/** 문서 재인덱싱 */
	public boolean reindexDm(UserVo userVo, String compId) throws SQLException {
		
		DmDocLVo dmDocLVo;
		
		// 리소스ID세팅
		String mxRescId = null;
		UserVo adminVo = ptSecuSvc.createEmptyAdmin(compId, userVo.getLoutCatId(), userVo.getLangTypCd());
		ArrayList<PtMnuDVo> mnuList = ptSecuSvc.getAuthedMnuVoListByMdRid(adminVo, "DM");
		
		String docLstTypCd = "docLst", url;
		// 리소스ID세팅
		if(mnuList!=null){
			for(PtMnuDVo ptMnuDVo : mnuList){
				if(ptMnuDVo.getMdId().equals(docLstTypCd)){
					mxRescId = ptMnuDVo.getRescId();
					break;
				}
			}
		}
		if(mxRescId==null) return false;
		
		boolean hasReindexData = false;
		Integer pageNo=0, pageRowCnt = 200;
		QueryQueue queryQueue;
		CmSrchBVo cmSrchBVo;
		
		// 회사별 저장소 목록 조회
		List<DmStorBVo> storList = dmStorSvc.getStorList(userVo.getLangTypCd(), compId, false, "Y");
		
		if(storList==null || storList.size()==0) return false;
		
		for(DmStorBVo dmStorBVo : storList){
			pageNo=0;
			pageRowCnt = 200;
			
			while(true){
				// 조회조건 매핑
				dmDocLVo = newDmDocLVo(dmStorBVo);
				
				dmDocLVo.setQueryLang(userVo.getLangTypCd());
				dmDocLVo.setPageNo(++pageNo);
				dmDocLVo.setPageRowCnt(pageRowCnt);
				dmDocLVo.setStatCd("C"); // 상태코드['완료']
				dmDocLVo.setDftYn("Y"); // 최종버전만 인덱싱
				@SuppressWarnings("unchecked")
				List<DmDocLVo> dmDocLVoList = (List<DmDocLVo>) commonDao.queryList(dmDocLVo);
				if(dmDocLVoList != null && !dmDocLVoList.isEmpty()){
					
					queryQueue = new QueryQueue();
					for(DmDocLVo storedDmDocLVo : dmDocLVoList){
						
						// 검색 인덱싱 처리
						cmSrchBVo = emSrchSvc.createVo();
						cmSrchBVo.setCompId(userVo.getCompId());
						cmSrchBVo.setMdRid("DM");
						cmSrchBVo.setMdBxId(docLstTypCd);
						cmSrchBVo.setMdId(storedDmDocLVo.getDocGrpId());
						cmSrchBVo.setMdBxRescId(mxRescId);
						cmSrchBVo.setActId("I");
						url = "/cm/dm/redirect.do?docGrpId="+storedDmDocLVo.getDocGrpId();
						//if(!"docLst".equals(docLstTypCd)) url+="&paramStorId="+dmDocLVo.getStorId();
						cmSrchBVo.setUrl(url);
						cmSrchBVo.setRegDt("sysdate");
						queryQueue.insert(cmSrchBVo);
					}
					
					if(!queryQueue.isEmpty()){
						hasReindexData = true;
						commonDao.execute(queryQueue);
					}
					if(dmDocLVoList.size() < pageRowCnt){
						break;
					}
				} else {
					break;
				}
			}
		}
		return hasReindexData;
	}
	
	/** 회사ID 조회 - 인수인계 */
	public String getParamCompId(HttpServletRequest request, ModelMap model, UserVo userVo, String suffix, boolean isAdmin) throws SQLException, CmException{
		String paramCompId = ParamUtil.getRequestParam(request, "paramCompId", false);
		if(suffix == null || (suffix.startsWith("Takovr") && isAdmin)) {// 인수대기
			// 저장소가 있는 회사목록
			List<PtCompBVo> compList = getCompList(userVo, suffix == null ? true : false);
			if(compList.size()>0){
				model.put("ptCompBVoList", compList);
				if(paramCompId == null || paramCompId.isEmpty()) {
					paramCompId = compList.get(0).getCompId();
				}
			}
		}
		model.put("paramCompId", paramCompId);
		return paramCompId;
	}
	
	/** 회사 목록 조회 - 문서저장소*/
	public List<PtCompBVo> getCompList(UserVo userVo, boolean isDft) throws SQLException{
		// 전체회사목록 조회
		List<PtCompBVo> ptCompBVoList = ptCmSvc.getFilteredCompList(null, "Y", userVo.getLangTypCd());
		// 저장소가 있는 회사목록
		List<PtCompBVo> compList = new ArrayList<PtCompBVo>(); 
		List<DmStorBVo> storList = null; // 저장소 목록
		for(PtCompBVo storedPtCompBVo : ptCompBVoList){
			if(!isDft && storedPtCompBVo.getCompId().equals(userVo.getCompId())) continue;
			// 해당 회사의 저장소 목록을 조회한다
			storList = dmStorSvc.getFilteredStorList(userVo.getLangTypCd(), storedPtCompBVo.getCompId(), null);
			if(storList.size()==0) continue;
			compList.add(storedPtCompBVo);
		}
		return compList;
	}
	
	/** 문서인수(회사) - 저장*/
	@SuppressWarnings("unchecked")
	public void saveTakovrDoc(HttpServletRequest request, QueryQueue queryQueue, String fromTableName, String toTableName, 
			String docGrpId, UserVo userVo, String actKey, String fromStorId, String toStorId, List<CommonFileVo> deletedFileList) throws Exception{
		//패키지 경로[추후 클래스를 통한 패키지 자동 매핑]
		String pakage = DmConstant.pakage;
		if(actKey == null || actKey.isEmpty()) actKey = "";
		Class<? extends CommonVo> clazz = null;
		//이관할 VO 명 배열
		CommonVo commonVo;
		String tmpId = null;
		List<CommonVo> commonVoList = null, subVoList = null;
			String voNm;
			String[] subVos = {"DmFileD"};
			for(String nm : DmConstant.CREATE_TBLS){
				if("DmFileD".equals(nm)) continue;
				voNm = pakage + "."+nm;
				voNm+="Vo";
				clazz = (Class<? extends CommonVo>)Class.forName(voNm);
				commonVo = clazz.newInstance();
				// 생성테이블에 테이블명 세팅
				VoUtil.setValue(commonVo, "tableName", fromTableName);
				VoUtil.setValue(commonVo, "docGrpId", docGrpId);
				
				if("insert".equals(actKey)){
					// 문서VO에 내용조회여부 true로 변경
					if(nm.startsWith("DmDocL")) {
						VoUtil.setValue(commonVo, "withLob", true);
						VoUtil.setValue(commonVo, "addItemNmList", getDmAddItemList());
					}
					commonVoList = (List<CommonVo>)commonDao.queryList(commonVo);
					for(CommonVo storedCommonVo : commonVoList){
						// 생성테이블에 테이블명 세팅
						VoUtil.setValue(storedCommonVo, "tableName", toTableName);
						if(nm.startsWith("DmDocD")) {
							VoUtil.setValue(storedCommonVo, "statCd", "C");
							tmpId = ((DmDocDVo)storedCommonVo).getFldId();
							// 폴더조회 - 이관할 저장소에 폴더가 없으면 미분류로 변경한다.
							if(!DmConstant.EMPTY_CLS.equals(tmpId) && getDmFldBVo(toStorId, tmpId, "ko") == null){
								VoUtil.setValue(storedCommonVo, "fldId", DmConstant.EMPTY_CLS);
							}
							
							// 저장소문서관계 저장
							if(toStorId != null && !toStorId.isEmpty()) dmStorSvc.saveStorId(queryQueue, toStorId, docGrpId, false);
							/** 검색 색인 데이터를 더함 */
							//addSrchIndex(docGrpId, userVo, queryQueue, "I");
						}
						queryQueue.insert(storedCommonVo);
						
						// 문서ID 단위 테이블 이관
						if(nm.startsWith("DmDocL")) {
							for(String subNm : subVos){
								voNm = pakage + "."+subNm;
								voNm+="Vo";
								clazz = (Class<? extends CommonVo>)Class.forName(voNm);
								commonVo = clazz.newInstance();
								VoUtil.setValue(commonVo, "tableName", fromTableName);
								VoUtil.setValue(commonVo, "refId", ((DmDocLVo)storedCommonVo).getDocId());
								subVoList = (List<CommonVo>)commonDao.queryList(commonVo);
								for(CommonVo subVo : subVoList){
									// 생성테이블에 테이블명 세팅
									VoUtil.setValue(subVo, "tableName", toTableName);
									queryQueue.insert(subVo);
								}
							}
						}
					}
				}
			}
	}
	
	/** 관련문서 기본정보 동기화 */
	public void setDocDSync(QueryQueue queryQueue, DmDocLVo dmDocLVo, String tableName, String seculCd) throws SQLException{
		// 보안등급에 변화가 없으면 리턴
		if(dmDocLVo.getSeculCd().equals(seculCd)) return;
		DmDocDVo dmDocDVo = new DmDocDVo();
		dmDocDVo.setTableName(tableName);
		dmDocDVo.setFldId(dmDocLVo.getFldId());
		dmDocDVo.setSubDocGrpId(dmDocLVo.getSubDocGrpId());
		dmDocDVo.setStatCd("C");
		dmDocDVo.setSubYn("Y"); // 하위여부
		// 관련문서 건수 조회
		if(commonDao.count(dmDocDVo)>0){
			@SuppressWarnings("unchecked")
			List<DmDocDVo> dmDocDVoList = (List<DmDocDVo>)commonDao.queryList(dmDocDVo);
			for(DmDocDVo storedDmDocDVo : dmDocDVoList){
				if(dmDocLVo.getDocGrpId().equals(storedDmDocDVo.getDocGrpId())) continue;
				dmDocDVo = new DmDocDVo();
				dmDocDVo.setTableName(tableName);
				dmDocDVo.setDocGrpId(storedDmDocDVo.getDocGrpId());
				dmDocDVo.setSeculCd(seculCd);
				queryQueue.update(dmDocDVo);
			}
		}
		
	}
	
	/** 인계인수 정보 삭제 */
	public void delTakovrVo(QueryQueue queryQueue, String storId, String tgtOrgId, String docGrpId){
		if(storId == null && tgtOrgId == null) return;
		DmTakovrBVo dmTakovrBVo = new DmTakovrBVo();
		if(storId != null) dmTakovrBVo.setStorId(storId);
		if(tgtOrgId != null) dmTakovrBVo.setTgtOrgId(tgtOrgId);
		dmTakovrBVo.setDocGrpId(docGrpId);
		queryQueue.delete(dmTakovrBVo);
	}
	
	/** bxId별 함 URL 리턴 - menuId 없는 URL 리턴 */
	public String getBxUrlByBxId(String bxId){
		return "/dm/doc/"+bxId+".do";
	}
	
}
