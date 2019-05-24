package com.innobiz.orange.web.wf.svc;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.innobiz.orange.web.cm.config.ServerConfig;
import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.utils.ArrayUtil;
import com.innobiz.orange.web.cm.utils.StringUtil;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.CommonFileVo;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.em.svc.EmCmSvc;
import com.innobiz.orange.web.em.svc.EmFileUploadSvc;
import com.innobiz.orange.web.em.vo.EmTmpFileTVo;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtSysSvc;
import com.innobiz.orange.web.wf.utils.FormUtil;
import com.innobiz.orange.web.wf.utils.WfConstant;
import com.innobiz.orange.web.wf.vo.WfFormBVo;
import com.innobiz.orange.web.wf.vo.WfFormColmLVo;
import com.innobiz.orange.web.wf.vo.WfFormLstDVo;
import com.innobiz.orange.web.wf.vo.WfFormRegDVo;
import com.innobiz.orange.web.wf.vo.WfWorksCodeLVo;
import com.innobiz.orange.web.wf.vo.WfWorksImgDVo;
import com.innobiz.orange.web.wf.vo.WfWorksLVo;
import com.innobiz.orange.web.wf.vo.WfWorksReadHVo;

@Service
public class WfFormSvc {
	
	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(WfFormSvc.class);
	
	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "emCmSvc")
	private EmCmSvc emCmSvc;
	
	/** 관리 서비스 */
	@Resource(name = "wfAdmSvc")
	private WfAdmSvc wfAdmSvc;
	
	/** 공통 서비스 */
	@Resource(name = "wfCmSvc")
	private WfCmSvc wfCmSvc;
	
	/** 파일업로드 서비스 */
	@Resource(name = "emFileUploadSvc")
	private EmFileUploadSvc emFileUploadSvc;
	
	/** 파일 서비스 */
	@Resource(name = "wfFileSvc")
	private WfFileSvc wfFileSvc;
	
	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
	/** 코드 서비스 */
	@Resource(name = "wfCdSvc")
	private WfCdSvc wfCdSvc;
	
	/** 양식 기본 조회 */
	public WfFormBVo getWfFormBVo(UserVo userVo, String formNo, String useYn) throws SQLException{
		if(userVo!=null)
			return getWfFormBVo(userVo.getCompId(), userVo.getLangTypCd(), formNo, useYn);
		return getWfFormBVo(null, null, formNo, useYn);
		
	}
	
	/** 양식 기본 조회 */
	public WfFormBVo getWfFormBVo(String compId, String langTypCd, String formNo, String useYn) throws SQLException{
		WfFormBVo wfFormBVo = new WfFormBVo();
		if(compId!=null) wfFormBVo.setCompId(compId); 
		if(langTypCd!=null) wfFormBVo.setQueryLang(langTypCd);
		if(useYn!=null) wfFormBVo.setUseYn(useYn);
		wfFormBVo.setFormNo(formNo);
		
		return (WfFormBVo)commonDao.queryVo(wfFormBVo);
	}
	
	/** 양식 목록 조회 */
	@SuppressWarnings("unchecked")
	public List<WfFormBVo> getWfFormBVoList(String compId, String langTypCd, String useYn, String grpId, boolean isGen) throws SQLException{
		WfFormBVo wfFormBVo = new WfFormBVo();
		if(compId!=null) wfFormBVo.setCompId(compId); 
		if(langTypCd!=null) wfFormBVo.setQueryLang(langTypCd);
		if(useYn!=null) wfFormBVo.setUseYn(useYn);
		if(grpId!=null) wfFormBVo.setGrpId(grpId);
		
		if(isGen){
			wfFormBVo.setWhereSqllet(" AND GEN_ID IS NOT NULL");
		}
		
		return (List<WfFormBVo>)commonDao.queryList(wfFormBVo);
	}
	
	/** 양식 삭제 */
	public void deleteForm(HttpServletRequest request, QueryQueue queryQueue, UserVo userVo, String formNo, List<String> formNoList) throws SQLException{
		
		// 양식[기본정보]
		WfFormBVo wfFormBVo = new WfFormBVo();
		//wfFormBVo.setCompId(userVo.getCompId());
		wfFormBVo.setFormNo(formNo);
		
		WfFormBVo formVo = (WfFormBVo)commonDao.queryVo(wfFormBVo);
		// 배포된 양식일 경우 배포ID 조회후 배열 리턴
		if(formVo!=null && formVo.getGenId()!=null){
			formNoList.add(formNo);
		}
				
		// 양식컬럼 목록
		WfFormColmLVo wfFormColmLVo = new WfFormColmLVo();
		wfFormColmLVo.setFormNo(formNo);
		queryQueue.delete(wfFormColmLVo);
		
		// 양식컬럼 이력
		wfFormColmLVo = new WfFormColmLVo();
		wfFormColmLVo.setFormNo(formNo);
		wfFormColmLVo.setHst(true);
		queryQueue.delete(wfFormColmLVo);
		
		// 양식목록 상세
		WfFormLstDVo wfFormLstDVo = new WfFormLstDVo();
		wfFormLstDVo.setFormNo(formNo);
		queryQueue.delete(wfFormLstDVo);
		
		// 양식목록 이력
		wfFormLstDVo = new WfFormLstDVo();
		wfFormLstDVo.setFormNo(formNo);
		wfFormLstDVo.setHst(true);
		queryQueue.delete(wfFormLstDVo);
		
		// 등록화면 - 해당 양식을 사용하고 있는 데이터가 있을경우 체크필요?
		WfFormRegDVo wfFormRegD = new WfFormRegDVo();
		wfFormRegD.setFormNo(formNo);
		queryQueue.delete(wfFormRegD);
		
		// 모바일
		wfFormRegD = new WfFormRegDVo();
		wfFormRegD.setFormNo(formNo);
		wfFormRegD.setMob(true);
		queryQueue.delete(wfFormRegD);
		
		// 등록화면 이력
		wfFormRegD = new WfFormRegDVo();
		wfFormRegD.setFormNo(formNo);
		wfFormRegD.setHst(true);
		queryQueue.delete(wfFormRegD);
		
		// 모바일 이력
		wfFormRegD = new WfFormRegDVo();
		wfFormRegD.setFormNo(formNo);
		wfFormRegD.setHst(true);
		wfFormRegD.setMob(true);
		queryQueue.delete(wfFormRegD);
		
		// 양식[기본정보] 삭제
		queryQueue.delete(wfFormBVo);
	}
	
	/** 사용중인(데이터가 등록된) 양식 조회 - [양식번호|양식명] */
	public void getUseFormList(String langTypCd, String formNo, List<Map<String, String>> useFormList) throws SQLException{
		// 양식 조회
		WfFormBVo wfFormBVo = getWfFormBVo(null, langTypCd, formNo, null);
		
		// 생성ID
		String genId = wfFormBVo.getGenId();
		if(genId==null || genId.isEmpty()) return; // 배포되지 않은 양식은 제외
		
		// 테이블관리 기본(WF_0000_WORKS_L) 테이블 - BIND
		WfWorksLVo wfWorksLVo = newWfWorksLVo(null, formNo, false);
		if(commonDao.count(wfWorksLVo)==0) return; // 데이터가 없으면 리턴
		
		// 데이터가 1건 이상일 경우 양식번호, 양식명을 맵에 담음
		Map<String, String> formMap=new HashMap<String, String>();
		formMap.put("formNo", wfFormBVo.getFormNo());
		formMap.put("formNm", wfFormBVo.getFormNm());
		
		//String[] useForms=new String[]{wfFormBVo.getFormNo(), wfFormBVo.getFormNm()};
		useFormList.add(formMap);
	}
	
	/** 사용중인(데이터가 등록된) 양식 조회 - [건수기준]*/
	public boolean isUseForm(String langTypCd, String formNo) throws SQLException{
		// 양식 조회
		WfFormBVo wfFormBVo = getWfFormBVo(null, langTypCd, formNo, null);
		
		// 생성ID
		String genId = wfFormBVo.getGenId();
		if(genId==null || genId.isEmpty()) return false; // 배포되지 않은 양식은 제외
		
		// 테이블관리 기본(WF_0000_WORKS_L) 테이블 - BIND
		WfWorksLVo wfWorksLVo = newWfWorksLVo(null, formNo, false);
		return commonDao.count(wfWorksLVo)>0;
	}
	
	/** 양식 그룹 이동 */
	public void moveFormGrp(HttpServletRequest request, QueryQueue queryQueue, UserVo userVo, String formNo, String grpId){
		
		// 양식[기본정보] 삭제
		WfFormBVo wfFormBVo = new WfFormBVo();
		wfFormBVo.setCompId(userVo.getCompId());
		wfFormBVo.setFormNo(formNo);
		wfFormBVo.setGrpId(grpId);
		
		queryQueue.update(wfFormBVo);
	}
	
	/** 분할해야 할 컬럼이 있으면 분할갯수 리턴*/
	public Integer getDivisionColmList(String colm){
		Integer divisionCnt=null;
		for(String[] division : WfConstant.DIVISION_COLM_LIST){
			if(colm.startsWith(division[0])){
				divisionCnt=Integer.parseInt(division[1]);
				break;
			}
		}
		return divisionCnt;
	}
	
	/** 컬럼명 목록 세팅 */
	public List<String[]> setColmNmList(List<WfFormColmLVo> wfFormColmLVoList, String genId, String formNo, WfWorksLVo wfWorksLVo, String lstYn) throws SQLException{
		// 기본으로 설정된 목록 컬럼 조회
		if(wfFormColmLVoList==null)
			wfFormColmLVoList = wfAdmSvc.getCurrColmVoList(genId, formNo, null);
		
		// 컬럼명 목록
		List<String[]> colmList = new ArrayList<String[]>();
		
		String[] colms = null;
		String colmNm;
		Integer division=null;
		int i;
		for(WfFormColmLVo storedWfFormColmLVo : wfFormColmLVoList){
			// select 제외할 컬럼 체크
			if(ArrayUtil.isStartsWithArray(WfConstant.EXCLUDE_SELECT, storedWfFormColmLVo.getColmNm()) || 
					ArrayUtil.isStartsWithArray(WfConstant.CODE_COLM_LIST, storedWfFormColmLVo.getColmNm()))
				continue;
			division=getDivisionColmList(storedWfFormColmLVo.getColmNm());
			colmNm=storedWfFormColmLVo.getColmNm();
			if(division!=null){
				for(i=1;i<=division.intValue();i++){
					//colmNm=StringUtil.toCamelNotation(storedWfFormColmLVo.getColmNm(), false);
					colms=new String[]{colmNm.toUpperCase()+"_"+i, colmNm+"_"+i, wfCmSvc.toXmlDataType(colmNm)};
					colmList.add(colms);
				}
			}else{
				//colmNm=StringUtil.toCamelNotation(storedWfFormColmLVo.getColmNm(), false);
				colms=new String[]{colmNm.toUpperCase(), colmNm, wfCmSvc.toXmlDataType(colmNm)};
				colmList.add(colms);
			}
			
		}
		if(wfWorksLVo!=null && colmList.size()>0)
			wfWorksLVo.setColmList(colmList);
		
		return colmList;
	}
	
	/** 컬럼명 목록 리턴 */
	public List<String[]> getColmNmList(List<WfFormColmLVo> wfFormColmLVoList, String genId, String formNo) throws SQLException{
		// 기본으로 설정된 목록 컬럼 조회
		if(wfFormColmLVoList==null)
			wfFormColmLVoList = wfAdmSvc.getCurrColmVoList(genId, formNo, null);
		
		// 컬럼명 목록
		List<String[]> colmList = new ArrayList<String[]>();
		
		String[] colms = null;
		String colmNm;
		Integer division=null;
		int i;
		for(WfFormColmLVo storedWfFormColmLVo : wfFormColmLVoList){
			// select 제외할 컬럼 체크
			if(ArrayUtil.isStartsWithArray(WfConstant.EXCLUDE_SELECT, storedWfFormColmLVo.getColmNm()) || 
					ArrayUtil.isStartsWithArray(WfConstant.CODE_COLM_LIST, storedWfFormColmLVo.getColmNm()))
				continue;
			division=getDivisionColmList(storedWfFormColmLVo.getColmNm());
			
			if(division!=null){
				for(i=1;i<=division.intValue();i++){
					colmNm=StringUtil.toCamelNotation(storedWfFormColmLVo.getColmNm(), false);
					colms=new String[]{colmNm.toUpperCase()+"_"+i, colmNm+"_"+i, wfCmSvc.toXmlDataType(colmNm)};
					colmList.add(colms);
				}
			}else{
				colmNm=StringUtil.toCamelNotation(storedWfFormColmLVo.getColmNm(), false);
				colms=new String[]{colmNm.toUpperCase(), colmNm, wfCmSvc.toXmlDataType(colmNm)};
				colmList.add(colms);
			}
			
		}
		
		return colmList;
	}
	
	/** 생성 테이블 초기화 */
	public WfWorksLVo newWfWorksLVo(String genId, String formNo, Boolean isDetail) throws SQLException{
		WfWorksLVo wfWorksLVo = new WfWorksLVo(formNo);
		if(isDetail!=null){
			boolean detail=isDetail.booleanValue();
			wfWorksLVo.setDetail(detail);
			if(detail){
				setColmNmList(null, genId, formNo, wfWorksLVo, null);
			}
		}
		return wfWorksLVo;
	}
	
	/** 정렬순서 적용 */
	public void setLstSortOrder(List<WfFormLstDVo> wfFormLstDVoList, WfWorksLVo wfWorksLVo){
		if(wfWorksLVo.getOrderBy() != null && !wfWorksLVo.getOrderBy().isEmpty()) return;
		
		WfFormLstDVo wfFormLstDVo=null;
		// 정렬순서용 설정 찾기
		for(WfFormLstDVo storedWfFormLstDVo: wfFormLstDVoList){
			if(storedWfFormLstDVo.getDataSortVa() != null && !storedWfFormLstDVo.getDataSortVa().isEmpty()){
				wfFormLstDVo = storedWfFormLstDVo;
				break;
			}
		}
		// 정렬순서 세팅
		if(wfFormLstDVo!=null){
			String colmTypCd=wfFormLstDVo.getColmTypCd();
			if(ArrayUtil.isInArray(WfConstant.EXCLUDE_SELECT, colmTypCd)) return;
			String dataSortVa = wfFormLstDVo.getDataSortVa();//데이터정렬값 - asc:올림차순, desc:내림차순
			dataSortVa = dataSortVa==null||dataSortVa.isEmpty() ? "ASC" : dataSortVa.toUpperCase();
			String colmNm=wfFormLstDVo.getColmNm();
			String dbColmNm = colmNm.toUpperCase();			
			
			Map<String, String> dispOrdrMap = new HashMap<String, String>();
			if(ArrayUtil.isInArray(WfConstant.SRCH_EQ_CODE, colmTypCd) || (ArrayUtil.isInArray(WfConstant.SRCH_LIKE_CODE, colmTypCd) && wfFormLstDVo.isCdColm())){
				dispOrdrMap.put("type",  ArrayUtil.isInArray(WfConstant.SRCH_LIKE_CODE, colmTypCd) ? "code" : colmTypCd);
				dispOrdrMap.put("colmNm", wfFormLstDVo.getColmNm());
				wfWorksLVo.setDispOrdrMap(dispOrdrMap);
				wfWorksLVo.setOrderBy("DISP_ORDR "+dataSortVa);
			}else if(ArrayUtil.isInArray(WfConstant.DIVISION_COLMS, colmTypCd)){
				Integer division=getDivisionColmList(wfFormLstDVo.getColmNm());
				String orderBy="";
				for(int i=1;i<=division.intValue();i++){
					colmNm=StringUtil.toCamelNotation(wfFormLstDVo.getColmNm(), false);
					if(!"".equals(orderBy)) orderBy+=", ";
					orderBy+=colmNm.toUpperCase()+"_"+i+" "+(dataSortVa==null||dataSortVa.isEmpty() ? "ASC" : dataSortVa.toUpperCase());
				}
				if(!"".equals(orderBy)) wfWorksLVo.setOrderBy(orderBy);
			}else if(ArrayUtil.isInArray(WfConstant.SRCH_LIKE_USER, colmTypCd)){
				wfWorksLVo.setOrderBy(colmTypCd+" "+(dataSortVa==null||dataSortVa.isEmpty() ? "ASC" : dataSortVa.toUpperCase()));
			}else{
				wfWorksLVo.setOrderBy(dbColmNm+" "+(dataSortVa==null||dataSortVa.isEmpty() ? "ASC" : dataSortVa.toUpperCase()));
			}
		}
	}
	
	/** 날짜 조회 조건 세팅 */
	public void setLstFromTo(HttpServletRequest request, List<WfFormLstDVo> srchColmVoList, WfWorksLVo wfWorksLVo, boolean isDtlSrch){
		
	}
	
	/** 조회조건 세팅 */
	public void setQueryUrlOptions(HttpServletRequest request, ModelMap model, WfWorksLVo wfWorksLVo, 
			List<WfFormLstDVo> wfFormLstDVoList, List<WfFormLstDVo> srchColmVoList,
			UserVo userVo, String langTypCd, boolean isAdmin, Map<String, String> configMap) throws SQLException, CmException{
		
		if(wfFormLstDVoList==null || srchColmVoList==null) return;
		
		// 정렬순서 세팅
		setLstSortOrder(wfFormLstDVoList, wfWorksLVo);
		
		// 상세조회 여부
		String srchDetl = request.getParameter("srchDetl");
		boolean isDtlSrch=srchDetl!=null && "Y".equals(srchDetl);
		
		// 날짜 조회조건 세팅
		//setLstFromTo(request, srchColmVoList, wfWorksLVo, isDtlSrch);
		
		// 검색 목록 맵
		Map<String, List<Map<String, Object>>> schListMap=new HashMap<String, List<Map<String, Object>>>();
		Map<String, Object> colmMap=null;
		String schCat=request.getParameter("schCat");
		String schWord=request.getParameter("schWord");
		String durCat=request.getParameter("durCat");
		String durStrtDt=request.getParameter("durStrtDt");
		String durEndDt=request.getParameter("durEndDt");
		
		String strtDtSuffix="StrtDt";
		String endDtSuffix="EndDt";
		String colmTypCd, colmNm, dbColmNm, key, paramNm;
		String[] params=null;
		List<Map<String, Object>> colmList=null;
		Integer division=null;
		
		//List<Map<String, Object>> codeParamList=null;
		
		// 코드 파라미터 목록
		List<String> codeParamList=null;
		
		// 코드 파라미터맵
		Map<String, String[]> codeParamMap = new HashMap<String, String[]>();
		
		for(WfFormLstDVo storedWfFormLstDVo : srchColmVoList){
			colmTypCd=storedWfFormLstDVo.getColmTypCd();
			colmNm=storedWfFormLstDVo.getColmNm();
			dbColmNm=ArrayUtil.isInArray(WfConstant.EXCLUDE_COLM_LIST, colmNm) ? StringUtil.fromCamelNotation(colmNm): colmNm.toUpperCase();
			division=getDivisionColmList(colmNm);
			if(isDtlSrch){
				// 텍스트 LIKE OR EQ 컬럼 - [컬럼명, 데이터타입, 파라미터값]
				if((ArrayUtil.isInArray(WfConstant.SRCH_LIKE_TXT, colmTypCd) || ArrayUtil.isInArray(WfConstant.SRCH_EQ_TXT, colmTypCd)) 
						&& request.getParameter(colmNm)!=null && !request.getParameter(colmNm).isEmpty()){
					key=ArrayUtil.isInArray(WfConstant.SRCH_EQ_TXT, colmTypCd) ? "eqText" : "likeText";
					
					if(!schListMap.containsKey(key)){
						colmList=new ArrayList<Map<String, Object>>();
						schListMap.put(key, colmList);
					}
					colmList=schListMap.get(key);
					colmMap=getSchColmMap(dbColmNm, wfCmSvc.toXmlDataType(colmNm), request.getParameter(colmNm));
					colmList.add(colmMap);
					schListMap.put(key, colmList);
				}
				
				// 분할된 컬럼 ==> 텍스트 LIKE OR 컬럼 - [컬럼명, 데이터타입, 파라미터값]
				if(division!=null && ArrayUtil.isInArray(WfConstant.SRCH_OR_LIKE, colmTypCd) && request.getParameter(colmNm)!=null && !request.getParameter(colmNm).isEmpty()){
					key="orLike";
					
					if(!schListMap.containsKey(key)){
						colmList=new ArrayList<Map<String, Object>>();
						schListMap.put(key, colmList);
					}
					colmList=schListMap.get(key);
					
					for(int i=1;i<=division.intValue();i++){
						colmMap=getSchColmMap(dbColmNm+"_"+i, wfCmSvc.toXmlDataType(colmNm), request.getParameter(colmNm));
						colmList.add(colmMap);
					}
					if(colmList.size()>0) schListMap.put(key, colmList);
				}
				
				// 날짜 FROM ~ TO 컬럼 - [컬럼명, 데이터타입, 파라미터값]
				if(ArrayUtil.isInArray(WfConstant.SRCH_FROM_DATE, colmTypCd)){
					
					paramNm=colmNm+strtDtSuffix;
					durStrtDt=request.getParameter(paramNm);
					if(durStrtDt!=null && !durStrtDt.isEmpty()){
						key="strtDtList";
						if(!schListMap.containsKey(key)){
							colmList=new ArrayList<Map<String, Object>>();
							schListMap.put(key, colmList);
						}
						colmList=schListMap.get(key);
						colmMap=getSchColmMap(division!=null ? dbColmNm+"_2" : dbColmNm, wfCmSvc.toXmlDataType(colmNm), durStrtDt);
						colmList.add(colmMap);
						schListMap.put(key, colmList);
					}
					
					paramNm=colmNm+endDtSuffix;
					durEndDt=request.getParameter(paramNm);
					if(durEndDt!=null && !durEndDt.isEmpty()){
						key="endDtList";
						if(!schListMap.containsKey(key)){
							colmList=new ArrayList<Map<String, Object>>();
							schListMap.put(key, colmList);
						}
						colmList=schListMap.get(key);
						colmMap=getSchColmMap(dbColmNm=division!=null ? dbColmNm+"_1" : dbColmNm, wfCmSvc.toXmlDataType(colmNm), durEndDt);
						colmList.add(colmMap);
						schListMap.put(key, colmList);
					}
				}
				// 코드 컬럼 - [컬럼명, 데이터타입, 파라미터값]
				if(ArrayUtil.isInArray(WfConstant.SRCH_EQ_CODE, colmTypCd) || (ArrayUtil.isInArray(WfConstant.SRCH_LIKE_CODE, colmTypCd) && storedWfFormLstDVo.isCdColm())
						|| (ArrayUtil.isInArray(WfConstant.SRCH_LIKE_CODE, colmTypCd) && !storedWfFormLstDVo.isCdColm())){
					params=request.getParameterValues(colmNm);
					if(params!=null && params.length>0){
						codeParamList=ArrayUtil.toList(params, true);
						if(codeParamList==null || codeParamList.size()==0) continue;
						key=ArrayUtil.isInArray(WfConstant.SRCH_LIKE_CODE, colmTypCd) && !storedWfFormLstDVo.isCdColm() ? "likeCode": "eqCode";
						if(!schListMap.containsKey(key)){
							colmList=new ArrayList<Map<String, Object>>();
							schListMap.put(key, colmList);
						}						
						
						colmList=schListMap.get(key);
						colmMap=getSchColmMap(colmNm, wfCmSvc.toXmlDataType(colmNm), codeParamList);
						colmList.add(colmMap);
						schListMap.put(key, colmList);
						
						// 코드파라미터맵 세팅
						codeParamMap.put(colmNm, params);
					}		
				}
				
			}else{
				// 텍스트 [컬럼명, 데이터타입, 파라미터값]
				if(ArrayUtil.isInArray(WfConstant.SRCH_LIKE_TXT, colmTypCd) && schCat!=null && !schCat.isEmpty() && schCat.equals(colmNm) && schWord!=null && !schWord.isEmpty()){
					colmMap=getSchColmMap(dbColmNm, wfCmSvc.toXmlDataType(colmNm), schWord);
					colmList=new ArrayList<Map<String, Object>>();
					colmList.add(colmMap);
					schListMap.put("likeText", colmList);
				}
				// 날짜 [컬럼명, 데이터타입, 파라미터값]
				if(ArrayUtil.isInArray(WfConstant.SRCH_FROM_DATE, colmTypCd) && durCat!=null && !durCat.isEmpty() 
						&& durCat.equals(colmNm) && ((durStrtDt!=null && !durStrtDt.isEmpty()) || (durEndDt!=null && !durEndDt.isEmpty()))){
					
					if(durStrtDt!=null && !durStrtDt.isEmpty()){
						colmMap=getSchColmMap(division!=null ? dbColmNm+"_2" : dbColmNm, wfCmSvc.toXmlDataType(colmNm), durStrtDt);
						colmList=new ArrayList<Map<String, Object>>();
						colmList.add(colmMap);
						schListMap.put("strtDtList", colmList);
					}
					if(durEndDt!=null && !durEndDt.isEmpty()){
						colmMap=getSchColmMap(dbColmNm=division!=null ? dbColmNm+"_1" : dbColmNm, wfCmSvc.toXmlDataType(colmNm), durEndDt);
						colmList=new ArrayList<Map<String, Object>>();
						colmList.add(colmMap);
						schListMap.put("endDtList", colmList);
					}
				}
				
			}
		}
		
		// 검색 목록 맵을 VO에 세팅
		if(schListMap!=null && schListMap.size()>0)
			wfWorksLVo.setSchListMap(schListMap);
		
		// 코드파라미터맵 모델에 세팅
		if(codeParamMap.size()>0)
			model.put("codeParamMap", codeParamMap);
		
	}
	
	/** 검색 컬럼맵 세팅 */
	public Map<String, Object> getSchColmMap(String dbColmNm, String dataTyp, Object value){
		Map<String, Object> colmMap = new HashMap<String, Object>();
		colmMap.put("dbColmNm", dbColmNm);
		colmMap.put("dataTyp", dataTyp);
		colmMap.put("value", value);
		return colmMap;
	}
	
	/** 조회컬럼 세팅 */
	public void setLstColmList(ModelMap model, String genId, String formNo,
			UserVo userVo, String langTypCd, List<WfFormColmLVo> wfFormColmLVoList, List<WfFormLstDVo> wfFormLstDVoList) throws SQLException, CmException{
		if(wfFormColmLVoList==null || wfFormColmLVoList.size()==0 || wfFormLstDVoList==null || wfFormLstDVoList.size()==0) return;
		
		// 양식등록상세 조회
		WfFormRegDVo wfFormRegDVo = wfAdmSvc.getWfFormRegDVo(genId, formNo, true, null);
		JSONObject jsonVa=null;
		if(wfFormRegDVo!=null && wfFormRegDVo.getAttrVa()!=null){
			model.put("jsonAttrVa", wfFormRegDVo.getAttrVa());
			// JSON 객체로 변환
			jsonVa = (JSONObject) JSONValue.parse(wfFormRegDVo.getAttrVa());
		}
				
		// 컬럼 데이터 맵[0:컬럼ID, 1:컬럼명(컬럼타입), 2:항목명]
		Map<String, String[]> colmDataMap=new HashMap<String, String[]>();
		String[] arrs=null;
		String itemNm=null;
		String emptyLangId=null;
		String langTitleId=null;
		JSONObject jsonMap=null; 
		String colmNm=null;
		for(WfFormColmLVo storedWfFormColmLVo : wfFormColmLVoList){
			colmNm=storedWfFormColmLVo.getColmNm();
			if(!ArrayUtil.isInArray(WfConstant.EXCLUDE_COLM_LIST, colmNm) && jsonVa!=null && jsonVa.containsKey(colmNm)){
				jsonMap=(JSONObject)jsonVa.get(colmNm);
				emptyLangId="label".equals(storedWfFormColmLVo.getColmTypCd()) ? "label" : "name";
				langTitleId=emptyLangId+"RescVa_"+langTypCd;
				itemNm= jsonMap.get(langTitleId)!=null ? (String)jsonMap.get(langTitleId) : storedWfFormColmLVo.getItemNm();
			}else{
				itemNm=storedWfFormColmLVo.getItemNm();
			}
			arrs=new String[]{storedWfFormColmLVo.getColmId(), colmNm, itemNm, storedWfFormColmLVo.getColmTypCd()};
			colmDataMap.put(storedWfFormColmLVo.getColmId(), arrs);
		}
		
		// 검색 제외 컬럼
		String[] exclude = WfConstant.EXCLUDE_SRCH;
		String[] schCats = WfConstant.SRCH_TXT;
		String[] durCats = WfConstant.SRCH_DATE;
		// 검색컬럼 - 텍스트
		List<String[]> schCatList = new ArrayList<String[]>();
		
		// 검색컬럼 - 날짜
		List<String[]> durCatList = new ArrayList<String[]>();
		
		// 검색컬럼 - 코드
		//List<String[]> cdCatList = new ArrayList<String[]>();
		
		// 상세검색 컬럼 목록
		List<String[]> dtlSrchList = new ArrayList<String[]>();
		
		// 목록컬럼
		List<WfFormLstDVo> lstDispList = new ArrayList<WfFormLstDVo>();
				
		String colmId=null, colmTyp=null;
		String[] nms=null;
		
		for(WfFormLstDVo storedWfFormLstDVo : wfFormLstDVoList){
			colmId=storedWfFormLstDVo.getColmId();
			if(colmDataMap!=null && colmDataMap.containsKey(colmId)){
				arrs=colmDataMap.get(colmId);
				colmNm=storedWfFormLstDVo.getColmNm();
				storedWfFormLstDVo.setColmNm(arrs[1]);
				storedWfFormLstDVo.setItemNm(arrs[2]);
				colmTyp=storedWfFormLstDVo.getColmTypCd();
				storedWfFormLstDVo.setColmTyp(colmTyp);
				lstDispList.add(storedWfFormLstDVo);
				//colmDbNm=StringUtil.toCamelNotation(storedWfFormLstDVo.getColmNm(), false);
				
				// 검색여부가 'Y' and 목록컬럼에 속하는 경우에 조회컬럼 배열에 담는다.
				if(storedWfFormLstDVo.getSrchYn()!=null && "Y".equals(storedWfFormLstDVo.getSrchYn()) && !ArrayUtil.isStartsWithArray(exclude, colmNm)){					
					nms=new String[]{colmNm, storedWfFormLstDVo.getItemNm(), colmTyp};
					dtlSrchList.add(nms);
					if(ArrayUtil.isStartsWithArray(schCats, colmNm))
						schCatList.add(nms);
					else if(ArrayUtil.isStartsWithArray(durCats, colmNm))
						durCatList.add(nms);					
				}
			}
		}
		
		//if(srchDispList.size()>0) model.put("srchDispList", srchDispList);
		model.put("schCatList", schCatList);
		model.put("durCatList", durCatList);
		model.put("dtlSrchList", dtlSrchList);
		model.put("lstDispList", lstDispList);
		
	}
	
	/** 컬럼 목록에 맞게 파라미터맵으로 변환*/
	public Map<String,Object> getVoMap(HttpServletRequest request, List<String[]> colmList) throws SQLException {
		Map<String,Object> returnMap = new HashMap<String, Object>();
		// 컬럼 목록 조회
		String value=null;
		for(String[] colms : colmList){
			value=request.getParameter(colms[1]);
			if(value==null) value="NULL";
			else if(value.isEmpty()) value="''";
			returnMap.put(colms[1], value);
		}
		return returnMap;
	}
	
	/** 저장 */
	public String saveWorks(HttpServletRequest request, QueryQueue queryQueue, String genId, String formNo, String workNo, String statCd, Map<String,Object> paramMap, boolean isDetail) throws CmException, SQLException, IOException {
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		//String formId=IdUtil.createId(Long.parseLong(formNo), WfConstant.TBLNM_LEN);
		
		// 테이블관리 기본(W) 테이블 - BIND
		WfWorksLVo wfWorksLVo = newWfWorksLVo(genId, formNo, false);
		if(paramMap!=null) VoUtil.fromMap(wfWorksLVo, paramMap);
		else VoUtil.bind(request, wfWorksLVo);
		
		wfWorksLVo.setQueryLang(langTypCd);
		wfWorksLVo.setCompId(userVo.getCompId()); // 회사ID
		
		boolean isNew=workNo==null || workNo.isEmpty(); // 신규 여부
		
		if(isNew){
			String tblNm="WFG_"+wfWorksLVo.getFormId()+"_WORKS_L";
			workNo=wfCmSvc.createNo(tblNm).toString();
			wfWorksLVo.setWorkNo(workNo);
			wfWorksLVo.setRegrUid(userVo.getUserUid());
			wfWorksLVo.setRegDt("sysdate");
			setMobData(genId, formNo, wfWorksLVo, null);
			
			if(wfWorksLVo.getStatCd()==null || wfWorksLVo.getStatCd().isEmpty()){
				if(statCd==null || statCd.isEmpty()) statCd="C"; // 등록완료
				wfWorksLVo.setStatCd(statCd);
			}
			
			queryQueue.insert(wfWorksLVo);
			
		}else{
			wfWorksLVo.setModrUid(userVo.getUserUid());
			wfWorksLVo.setModDt("sysdate");
			setMobData(genId, formNo, wfWorksLVo, workNo);
			queryQueue.update(wfWorksLVo);
		}
		
		// 목록 조회형 컬럼 테이블 저장
		if(isDetail){
			WfWorksLVo wfWorksDVo = newWfWorksLVo(genId, formNo, true);
			
			BeanUtils.copyProperties(wfWorksLVo, wfWorksDVo, new String[]{"jsonVa", "colmList", "detail"});
			
			// 컬럼목록 별로 저장될 데이터 수집
			if(wfWorksDVo.getColmList()!=null && wfWorksDVo.getColmList().size()>0){
				List<String> attributeList = new ArrayList<String>();
				
				/*for(String[] colms : wfWorksDVo.getColmList()){
					if(ArrayUtil.isStartsWithArray(WfConstant.CODE_COLM_LIST, colms[1]) || 
							(ArrayUtil.isStartsWithArray(WfConstant.CD_TO_TBL_COLM_LIST, colms[1]) && isCdToTblSave(request, colms[1]))){
						continue;
					}
					System.out.println("colms[1] : "+colms[1]);
					attributeList.add(colms[1]);
				}*/
				
				for(String[] colms : wfWorksDVo.getColmList()){
					if(ArrayUtil.isStartsWithArray(WfConstant.CODE_COLM_LIST, colms[1])){
						continue;
					}
					attributeList.add(colms[1]);
				}
				if(attributeList.size()>0){
					Map<String, Object> voMap = new HashMap<String, Object>();
					FormUtil.bind(request, voMap, attributeList, null);
					
					if(voMap.size()>0){
						/*for(String attr : attributeList){
							if(!voMap.containsKey(attr)) voMap.put(attr, "NULL");
							else if(voMap.containsKey(attr) && "".equals(voMap.get(attr))) voMap.put(attr, "''");
						}*/
						wfWorksDVo.setVoMap(voMap);
					}
				}
				
				/*Map<String, Object> voMap = getVoMap(request, wfWorksDVo.getColmList());
				if(voMap!=null) wfWorksDVo.setVoMap(voMap);*/
				if(isNew) queryQueue.insert(wfWorksDVo);
				else queryQueue.update(wfWorksDVo);
			}
		}
		
		// 코드 목록 파라미터 세팅
		List<String[]> codeList = getParamToCodeList(request);
		
		// 코드 목록 저장
		if(codeList.size()>0){
			if(!isNew){ // 수정이면 기존 코드목록 데이터 삭제
				WfWorksCodeLVo wfWorksCodeLVo = new WfWorksCodeLVo(formNo);
				wfWorksCodeLVo.setWorkNo(workNo);
				queryQueue.delete(wfWorksCodeLVo);
			}
			
			// request 에서 코드컬럼 목록 바인딩
			List<WfWorksCodeLVo> wfWorksCodeLVoList = bindWfWorksCodeLVoList(request, formNo, codeList);
			if(wfWorksCodeLVoList.size()>0){
				// 코드 목록 테이블에 저장
				for(WfWorksCodeLVo storedWfWorksCodeLVo : wfWorksCodeLVoList){
					storedWfWorksCodeLVo.setWorkNo(workNo);
					queryQueue.insert(storedWfWorksCodeLVo);
				}
			}
		}
		
		// 신규 이미지 파일목록
		List<String[]> tmpImgIdList=new ArrayList<String[]>();
		
		// 수정 이미지 파일데이터 목록
		List<String[]> updateImgDataList=new ArrayList<String[]>();
		
		setParamToImageList(request, tmpImgIdList, updateImgDataList);
		
		// 이미지 목록 저장
		saveImgFile(request, queryQueue, formNo, workNo, userVo.getUserUid(), tmpImgIdList, updateImgDataList);
		
		return workNo;
	}
	
	/** 모바일 데이터 세팅 */
	public void setMobData(String genId, String formNo, WfWorksLVo wfWorksLVo, String workNo) throws SQLException{
		// 모바일 은 등록 양식 추가 저장
		if(ServerConfig.IS_MOBILE){
			if(workNo!=null){
				WfWorksLVo viewVo = newWfWorksLVo(null, formNo, null);
				viewVo.setWorkNo(workNo);
				Map<String, Object> wfWorksLVoMap = commonDao.queryMap(viewVo);
				if(wfWorksLVoMap==null) return;
				//if(wfWorksLVoMap.get("mobLoutVa")!=null) return;
			}
			
			WfFormRegDVo wfFormMobDVo = wfAdmSvc.getWfFormRegDVo(genId, formNo, true, Boolean.TRUE);
			if(wfFormMobDVo!=null){
				wfWorksLVo.setMobLoutVa(wfFormMobDVo.getLoutVa());
				wfWorksLVo.setMobTabVa(wfFormMobDVo.getTabVa());
			}
		}
	}
	
	/** "select", "radio", "checkbox" 의 코드테이블 저장 여부 */
	public boolean isCdToTblSave(HttpServletRequest request, String colmNm){
		String cdYn=request.getParameter(colmNm+"_cdYn");
		if(cdYn==null || cdYn.isEmpty()) return false;
		
		return true;
	}

	
	/** 양식 삭제 */
	public void deleteWorks(QueryQueue queryQueue, String genId, String formNo, String workNo, List<CommonFileVo> deletedFileList, List<String> deleteFilePathList, boolean isDetail) throws SQLException, CmException{
		
		// 코드 목록 삭제
		WfWorksCodeLVo wfWorksCodeLVo=new WfWorksCodeLVo(formNo);
		wfWorksCodeLVo.setWorkNo(workNo);
		queryQueue.delete(wfWorksCodeLVo);
		
		if(deletedFileList!=null){
			// 파일 삭제
			List<CommonFileVo> fileList=wfFileSvc.deleteFile(formNo, workNo, queryQueue);
			if(fileList!=null && fileList.size()>0){
				deletedFileList.addAll(fileList);
			}
		}
		
		if(deleteFilePathList!=null){
			// 이미지 삭제
			deleteImgFile(queryQueue, formNo, workNo, deleteFilePathList);
		}
		
		if(isDetail){
			// 상세 데이터 삭제
			WfWorksLVo wfWorksDVo = newWfWorksLVo(genId, formNo, true);
			wfWorksDVo.setWorkNo(workNo);		
			queryQueue.delete(wfWorksDVo);
			
			// 조회수 삭제
			WfWorksReadHVo wfWorksReadHVo = new WfWorksReadHVo(formNo);
			wfWorksReadHVo.setWorkNo(workNo);
			queryQueue.delete(wfWorksReadHVo);
			
		}
		
		// 기본 데이터 삭제
		WfWorksLVo wfWorksLVo = newWfWorksLVo(genId, formNo, false);
		wfWorksLVo.setWorkNo(workNo);		
		queryQueue.delete(wfWorksLVo);
		
	}
	
	
	
	/** 파라미터맵에서 이미지컬럼 수집 [key,value]*/
	public void setParamToImageList(HttpServletRequest request, List<String[]> tmpImgIdList, List<String[]> updateImgDataList){
		
		Map<String,String[]> map = request.getParameterMap();
		String[] params=null;
		for (String key : map.keySet()){
			if(key.startsWith("image") && request.getParameter(key)!=null && !request.getParameter(key).isEmpty()){
				tmpImgIdList.add(new String[]{key, request.getParameter(key)});
			}
			if("imgData".equals(key) && request.getParameterValues(key)!=null){
				params=request.getParameterValues(key);
				if(params.length>0){
					JSONObject jsonData;
					for(String data : params){
						if(data==null || data.isEmpty()) continue;
						jsonData = (JSONObject) JSONValue.parse(data);
						if(!jsonData.containsKey("colmNm")) continue;
						updateImgDataList.add(new String[]{(String)jsonData.get("imgNo"), (String)jsonData.get("colmNm")});
					}
				}
			}
		}
	}
	
	/** 이미지 저장 */
	public void saveImgFile(HttpServletRequest request, QueryQueue queryQueue, String formNo, String workNo, String regrUid, List<String[]> tmpImgIdList, List<String[]> updateImgDataList) throws SQLException, IOException, CmException{
		
		WfWorksImgDVo wfWorksImgDVo=null;
		
		// 신규 이미지 파일 저장
		if(tmpImgIdList!=null && tmpImgIdList.size()>0){
			EmTmpFileTVo emTmpFileTVo = null;
			
			String path = "images/upload/wf/img/"+formNo;
			String filePath = null, newSavePath;
			for(String[] tmpImgIds : tmpImgIdList){
				emTmpFileTVo = emFileUploadSvc.getEmTmpFileVo(Integer.parseInt(tmpImgIds[1]));
				if(emTmpFileTVo==null) continue;
				filePath = emTmpFileTVo.getSavePath();
				// 파일 새이름으로 복사 후 파일 배포
				newSavePath = wfFileSvc.copyAndWebDisk(request, path, filePath);
				wfWorksImgDVo = new WfWorksImgDVo(formNo);
				wfWorksImgDVo.setImgNo(wfCmSvc.createNo("WFG_WORKS_IMG_D").toString());
				wfWorksImgDVo.setWorkNo(workNo);
				wfWorksImgDVo.setColmNm(tmpImgIds[0]);
				wfWorksImgDVo.setImgPath(newSavePath);
				BufferedImage bimg = ImageIO.read(new File(filePath));
				wfWorksImgDVo.setImgWdth(Integer.parseInt(Integer.toString(bimg.getWidth())));
				wfWorksImgDVo.setImgHght(Integer.parseInt(Integer.toString(bimg.getHeight())));
				wfWorksImgDVo.setModrUid(regrUid);
				wfWorksImgDVo.setModDt("sysdate");
				queryQueue.insert(wfWorksImgDVo);
			}
		}
		
		// 기존 이미지 컬럼명 변경
		if(updateImgDataList!=null && updateImgDataList.size()>0){
			for(String[] imgData : updateImgDataList){
				wfWorksImgDVo = new WfWorksImgDVo(formNo);
				wfWorksImgDVo.setImgNo(imgData[0]);
				wfWorksImgDVo.setColmNm(imgData[1]);
				queryQueue.update(wfWorksImgDVo);
			}
		}
	}
	
	/** 이미지 삭제[업무번호 기준] */
	public void deleteImgFile(QueryQueue queryQueue, String formNo, String workNo, List<String> deleteFilePathList) throws SQLException, CmException{
		
		WfWorksImgDVo wfWorksImgDVo = new WfWorksImgDVo(formNo);
		wfWorksImgDVo.setWorkNo(workNo);
		
		@SuppressWarnings("unchecked")
		List<WfWorksImgDVo> imgList = (List<WfWorksImgDVo>)commonDao.queryList(wfWorksImgDVo);
		if(imgList!=null && imgList.size()>0){
			// 이미지 삭제
			queryQueue.delete(wfWorksImgDVo);
			
			for(WfWorksImgDVo storedWfWorksImgDVo : imgList){
				// 삭제할 이미지 경로 목록 저장
				deleteFilePathList.add(storedWfWorksImgDVo.getImgPath());
			}
		}
	}
	
	/** 이미지 삭제[번호기준] */
	public List<String> deleteImgList(QueryQueue queryQueue, String formNo, String workNo, List<String> imgNoList) throws SQLException, IOException, CmException{
		if(imgNoList==null || imgNoList.size()==0) return null;
		
		WfWorksImgDVo wfWorksImgDVo=null;
		
		WfWorksImgDVo deleteVo=null;
		
		// 삭제할 파일 경로 목록
		List<String> deleteFilePathList = new ArrayList<String>();
		
		for(String imgNo : imgNoList){
			wfWorksImgDVo = new WfWorksImgDVo(formNo);
			wfWorksImgDVo.setWorkNo(workNo);
			wfWorksImgDVo.setImgNo(imgNo);
			
			deleteVo=(WfWorksImgDVo)commonDao.queryVo(wfWorksImgDVo);
			if(deleteVo==null) continue;
			
			// 이미지 삭제
			queryQueue.delete(wfWorksImgDVo);
			
			// 삭제할 이미지 경로 목록 저장
			deleteFilePathList.add(deleteVo.getImgPath());
		}
		
		return deleteFilePathList;
	}
	
	/** 권한 세팅 - 목록 조회시 */
	public void setAuthVoMap(UserVo userVo, Map<String, Object> map, boolean isAdmin) throws SQLException{
		if(isAdmin) return;
		if(!map.get("regrUid").equals(userVo.getUserUid())) map.put("isChk", "N");
	}
	
	/** 권한 세팅 - 목록 조회시 */
	public void setAuthListMap(UserVo userVo, List<Map<String, Object>> mapList, boolean isAdmin) throws SQLException{
		if(isAdmin || mapList==null || mapList.size()==0) return;
		
		for(Map<String, Object> map : mapList){
			setAuthVoMap(userVo, map, isAdmin);
		}
	}
	
	/** 파라미터맵에서 코드컬럼(사용자,부서,코드) 수집*/
	public List<String[]> getParamToCodeList(HttpServletRequest request){
		List<String[]> returnList=new ArrayList<String[]>();
		
		Map<String,String[]> map = request.getParameterMap();
		for (String key : map.keySet()){
			if(ArrayUtil.isStartsWithArray(new String[]{"radioSingle", "checkboxSingle"}, key)) continue;
			if((ArrayUtil.isStartsWithArray(WfConstant.CODE_COLM_LIST, key) || (ArrayUtil.isStartsWithArray(WfConstant.CD_TO_TBL_COLM_LIST, key) && isCdToTblSave(request, key))) 
					&& request.getParameter(key)!=null && !request.getParameter(key).isEmpty()){
				returnList.add(new String[]{key, key.replaceAll("_?[0-9]", "").toUpperCase()});
			}
            
		}
		
		return returnList;
	}
	
	/** 코드 컬럼 목록 세팅 */
	public List<WfWorksCodeLVo> bindWfWorksCodeLVoList(HttpServletRequest request, String formNo, List<String[]> attributeList){
		
		List<WfWorksCodeLVo> wfWorksCodeLVoList = new ArrayList<WfWorksCodeLVo>();
		
		String attribute;
		
		WfWorksCodeLVo wfWorksCodeLVo=null;
		String[] params=null;
		Integer sortOrdr;
		for(String[] arrs : attributeList){
			attribute = arrs[0];
			params=request.getParameterValues(attribute);
			
			if(params!=null){
				if(params.length==1 && params[0].indexOf(",")>-1){
					params=params[0].split(",");
				}
				sortOrdr=0;
				for(String param : params){
					if(param==null || param.isEmpty()) continue;
					wfWorksCodeLVo=new WfWorksCodeLVo(formNo);
					//wfWorksCodeLVo.setWorkNo(workNo);
					wfWorksCodeLVo.setTypCd(arrs[1]);
					wfWorksCodeLVo.setColmNm(attribute);
					wfWorksCodeLVo.setCdVa(param.trim());
					wfWorksCodeLVo.setSortOrdr(++sortOrdr);
					wfWorksCodeLVoList.add(wfWorksCodeLVo);
				}
			}
			
		}
		return wfWorksCodeLVoList;
	}
	
	/** 저장된 코드 목록 VoMap에 세팅 - 목록 조회시 */
	public void setCodeListMap(String langTypCd, String formNo, List<WfFormColmLVo> wfFormColmLVoList, List<Map<String, Object>> mapList) throws SQLException{
		if(mapList==null || mapList.size()==0 || wfFormColmLVoList==null || wfFormColmLVoList.size()==0) return;
		boolean checked = false;
		for(WfFormColmLVo storedWfFormColmLVo : wfFormColmLVoList){
			if(ArrayUtil.isStartsWithArray(WfConstant.CODE_COLM_LIST, storedWfFormColmLVo.getColmNm()) || 
					ArrayUtil.isStartsWithArray(WfConstant.CD_TO_TBL_COLM_LIST, storedWfFormColmLVo.getColmNm())){
				checked=true;
				break;
			}
		}
		if(!checked) return;
		Map<String, List<WfWorksCodeLVo>> cdListMap=null;
		for(Map<String, Object> map : mapList){
			cdListMap=getCodeVoListMap(formNo,String.valueOf(map.get("workNo")), langTypCd);
			if(cdListMap!=null) map.put("cdListMap", cdListMap);
		}
	}
	
	/** 저장된 이미지 목록 VoMap에 세팅 - 목록 조회시 */
	public void setImgListMap(String langTypCd, String formNo, List<WfFormColmLVo> wfFormColmLVoList, List<Map<String, Object>> mapList) throws SQLException{
		if(mapList==null || mapList.size()==0 || wfFormColmLVoList==null || wfFormColmLVoList.size()==0) return;
		boolean checked = false;
		for(WfFormColmLVo storedWfFormColmLVo : wfFormColmLVoList){
			if(storedWfFormColmLVo.getColmNm().startsWith("image")){
				checked=true;
				break;
			}
		}
		if(!checked) return;
		Map<String, WfWorksImgDVo> imgListMap=null;
		for(Map<String, Object> map : mapList){
			imgListMap=getImgVoMap(formNo,String.valueOf(map.get("workNo")), langTypCd);
			if(imgListMap!=null) map.put("imgListMap", imgListMap);
		}
	}
	
	/** 저장된 코드 목록 VoMap에 세팅 - 목록 조회시 */
	public void setCodeVoMap(String langTypCd, String formNo, List<WfFormColmLVo> wfFormColmLVoList, Map<String, Object> map) throws SQLException{
		//if(map==null || wfFormColmLVoList==null || wfFormColmLVoList.size()==0) return;
		if(map==null) return;
		/*boolean checked = false;
		for(WfFormColmLVo storedWfFormColmLVo : wfFormColmLVoList){
			if(ArrayUtil.isStartsWithArray(WfConstant.CODE_COLM_LIST, storedWfFormColmLVo.getColmNm())){
				checked=true;
				break;
			}
		}
		if(!checked) return;*/
		Map<String, List<WfWorksCodeLVo>> cdListMap=getCodeVoListMap(formNo,String.valueOf(map.get("workNo")), langTypCd);
		if(cdListMap!=null) map.put("cdListMap", cdListMap);
	}
	
	/** 구분자로 저장된 싱글 데이터(라디오,체크박스) 목록 VoMap에 세팅 - 목록 조회시 */
	public void setSingleDataMap(List<WfFormColmLVo> wfFormColmLVoList, Map<String, Object> map) throws SQLException{
		if(map==null || wfFormColmLVoList==null || wfFormColmLVoList.size()==0) return;
		List<String> attributes=new ArrayList<String>();
		for(WfFormColmLVo storedWfFormColmLVo : wfFormColmLVoList){
			if(ArrayUtil.isStartsWithArray(WfConstant.SINGLE_TO_TBL_COLM_LIST, storedWfFormColmLVo.getColmNm())){
				attributes.add(storedWfFormColmLVo.getColmNm());
			}
		}
		if(attributes.size()==0) return;
		Map<String,Map<String,String>> singleListMap=new HashMap<String,Map<String,String>>();
		
		Map<String,String> dataMap;
		String[] datas;
		String tmpValue;
		for(String key : attributes){
			if(map.get(key)!=null){
				tmpValue=(String)map.get(key);
				if(!tmpValue.isEmpty()){
					datas=tmpValue.split(",");
					if(datas!=null && datas.length>0){
						dataMap=new HashMap<String,String>();
						for(String va : datas){
							dataMap.put(va.trim(), "Y");
						}
						singleListMap.put(key, dataMap);
					}
				}
			}
		}
		
		if(singleListMap!=null) map.put("singleListMap", singleListMap);
	}
	
	/** 저장된 이미지 목록 VoMap에 세팅 - 목록 조회시 */
	public void setImgVoMap(String langTypCd, String formNo, List<WfFormColmLVo> wfFormColmLVoList, Map<String, Object> map) throws SQLException{
		//if(map==null || wfFormColmLVoList==null || wfFormColmLVoList.size()==0) return;
		if(map==null) return;
		/*boolean checked = false;
		for(WfFormColmLVo storedWfFormColmLVo : wfFormColmLVoList){
			if(ArrayUtil.isStartsWithArray(WfConstant.CODE_COLM_LIST, storedWfFormColmLVo.getColmNm())){
				checked=true;
				break;
			}
		}
		if(!checked) return;*/
		Map<String, WfWorksImgDVo> imgListMap=getImgVoMap(formNo, String.valueOf(map.get("workNo")), langTypCd);
		if(imgListMap!=null) map.put("imgListMap", imgListMap);
	}
	
	/** 파일 건수 세팅 - 목록 조회시 */
	public void setListFileCnt(String formNo, List<WfFormColmLVo> wfFormColmLVoList, List<Map<String, Object>> mapList) throws SQLException{
		if(mapList==null || mapList.size()==0 || wfFormColmLVoList==null || wfFormColmLVoList.size()==0) return;
		boolean checked = false;
		for(WfFormColmLVo storedWfFormColmLVo : wfFormColmLVoList){
			if(storedWfFormColmLVo.getColmNm().startsWith("file")){
				checked=true;
				break;
			}
		}
		if(!checked) return;
		
		// 파일건수 세팅
		for(Map<String, Object> map : mapList){
			map.put("fileCnt", wfFileSvc.getFileVoListCnt(formNo, String.valueOf(map.get("workNo"))));
		}
		
	}
	
	/** 저장된 코드 목록 맵에 세팅 - 상세 조회시*/
	public Map<String, String[]> getCodeListMap(String formNo, String workNo, String langTypCd) throws SQLException{
		
		Map<String, String[]> returnMap = null;
		
		WfWorksCodeLVo wfWorksCodeLVo=new WfWorksCodeLVo(formNo);
		wfWorksCodeLVo.setWorkNo(workNo);
		wfWorksCodeLVo.setQueryLang(langTypCd);
		
		@SuppressWarnings("unchecked")
		List<WfWorksCodeLVo> wfWorksCodeLVoList = (List<WfWorksCodeLVo>)commonDao.queryList(wfWorksCodeLVo);
		
		if(wfWorksCodeLVoList.size()>0){
			returnMap = new HashMap<String, String[]>();
			String[] arrs=null;
			boolean first;
			for(WfWorksCodeLVo storedWfWorksCodeLVo : wfWorksCodeLVoList){				
				arrs=returnMap.get(storedWfWorksCodeLVo.getColmNm());
				if(arrs==null){
					arrs=new String[2];
					first=true;
				}else first=false;
				arrs[0]=first ? storedWfWorksCodeLVo.getCdVa() : arrs[0]+","+storedWfWorksCodeLVo.getCdVa();
				arrs[1]=first ? storedWfWorksCodeLVo.getCdNm() : arrs[1]+","+storedWfWorksCodeLVo.getCdNm();
				returnMap.put(storedWfWorksCodeLVo.getColmNm(), arrs);
			}
			
		}
		return returnMap;
	}
	
	/** 저장된 코드 목록Vo 맵에 세팅 - 상세 조회시*/
	public Map<String, List<WfWorksCodeLVo>> getCodeVoListMap(String formNo, String workNo, String langTypCd) throws SQLException{
		
		Map<String, List<WfWorksCodeLVo>> returnMap = null;
		
		WfWorksCodeLVo wfWorksCodeLVo=new WfWorksCodeLVo(formNo);
		wfWorksCodeLVo.setWorkNo(workNo);
		wfWorksCodeLVo.setQueryLang(langTypCd);
		
		@SuppressWarnings("unchecked")
		List<WfWorksCodeLVo> wfWorksCodeLVoList = (List<WfWorksCodeLVo>)commonDao.queryList(wfWorksCodeLVo);
		
		if(wfWorksCodeLVoList.size()>0){
			returnMap = new HashMap<String, List<WfWorksCodeLVo>>();
			List<WfWorksCodeLVo> codeVoList=null;
			
			for(WfWorksCodeLVo vo : wfWorksCodeLVoList){
				if(!returnMap.containsKey(vo.getColmNm())) returnMap.put(vo.getColmNm(), new ArrayList<WfWorksCodeLVo>());
				codeVoList=returnMap.get(vo.getColmNm());
				codeVoList.add(vo);
			}
			
		}
		return returnMap;
	}
	
	/** 저장된 이미지 목록Vo 맵에 세팅 - 상세 조회시 */
	public Map<String, WfWorksImgDVo> getImgVoMap(String formNo, String workNo, String langTypCd) throws SQLException{
		
		Map<String, WfWorksImgDVo> returnMap = null;
		
		WfWorksImgDVo wfWorksImgDVo=new WfWorksImgDVo(formNo);
		wfWorksImgDVo.setWorkNo(workNo);
		
		@SuppressWarnings("unchecked")
		List<WfWorksImgDVo> wfWorksImgDVoList = (List<WfWorksImgDVo>)commonDao.queryList(wfWorksImgDVo);
		
		if(wfWorksImgDVoList.size()>0){
			returnMap = new HashMap<String, WfWorksImgDVo>();
			
			for(WfWorksImgDVo vo : wfWorksImgDVoList){
				returnMap.put(vo.getColmNm(), vo);
			}
			
		}
		return returnMap;
	}
	
	/** 조회수 저장 */
	public void setWorksReadCnt(String formNo, String workNo, String userUid, WfWorksLVo wfWorksLVo, boolean isAdmin) throws SQLException{
		if("U0000001".equals(userUid) || isAdmin) return;
		WfWorksReadHVo wfWorksReadHVo = new WfWorksReadHVo(formNo);
		wfWorksReadHVo.setWorkNo(workNo);
		if(commonDao.count(wfWorksReadHVo)==0){
			QueryQueue queryQueue = new QueryQueue();
			
			wfWorksReadHVo.setUserUid(userUid);
			wfWorksReadHVo.setRegDt("sysdate");
			queryQueue.insert(wfWorksReadHVo);
			
			if(wfWorksLVo==null){
				wfWorksLVo=newWfWorksLVo(null, formNo, null);
				wfWorksLVo.setWorkNo(workNo);
			}
			wfWorksLVo.setDetail(true);
			wfWorksLVo.setReadCnt("add"); // 조회수 증가
			queryQueue.update(wfWorksLVo);
			
			commonDao.execute(queryQueue);
		}
	}
	
	/** 탭목록 맵으로 변환 */
	@SuppressWarnings("unchecked")
	public void setLangTabListMap(ModelMap model, WfFormRegDVo wfFormRegDVo){
		if(wfFormRegDVo==null || wfFormRegDVo.getTabVa()==null || wfFormRegDVo.getTabVa().isEmpty()) return;
		// 탭 목록을 map으로 변환
		try{
			JSONParser parser = new JSONParser();
			JSONArray jsonArray = (JSONArray)parser.parse(wfFormRegDVo.getTabVa());
			if(jsonArray!=null && !jsonArray.isEmpty()){
				JSONObject json;
				int i, size = jsonArray==null ? 0 : jsonArray.size();
				
				Map<String, JSONObject> tabLangListMap=new HashMap<String, JSONObject>();
				String key;
				JSONObject langJson=null;
				// 양식 컬럼 목록 저장
				for(i=0;i<size;i++){
					json=(JSONObject)jsonArray.get(i);
					Iterator<?> iter=json.keySet().iterator();
					langJson=new JSONObject();
					while (iter.hasNext()){
						key = (String)iter.next();
						langJson.put(key, json.get(key));
					}
					tabLangListMap.put((String)json.get("loutId"), langJson);
				}
				
				model.put("tabLangListMap", tabLangListMap);
			}
		}catch(ParseException pe){
			LOGGER.error(pe.getMessage());
		}
			
	}
	
	/** 파일 건수 세팅 */
	public void setFileCnt(String formNo, List<WfFormColmLVo> wfFormColmLVoList, List<Map<String, Object>> mapList) throws SQLException{
		if(mapList==null || mapList.size()==0 || wfFormColmLVoList==null || wfFormColmLVoList.size()==0) return;
		boolean checked = false;
		for(WfFormColmLVo storedWfFormColmLVo : wfFormColmLVoList){
			if(storedWfFormColmLVo.getColmNm().startsWith("file")){
				checked=true;
				break;
			}
		}
		if(!checked) return;
		Integer fileCnt=null;
		for(Map<String, Object> map : mapList){
			fileCnt=wfFileSvc.getFileVoListCnt(formNo, String.valueOf(map.get("workNo"))); // 파일 건수 조회
			map.put("fileCnt", fileCnt);
		}
	}
	
	/** 최대 본문 사이즈 model에 추가 */
	public void putBodySizeToModel(HttpServletRequest request, ModelMap model, String mdTypCd) throws SQLException {
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		UserVo userVo = LoginSession.getUser(request);
		if(mdTypCd==null || mdTypCd.isEmpty() || "no".equals(mdTypCd)) mdTypCd="wf";
		// 시스템 설정 조회 - 본문 사이즈
		Integer bodySize = ptSysSvc.getBodySizeMap(langTypCd, userVo.getCompId()).get(mdTypCd);
		if(bodySize!=null)		
			bodySize = bodySize * 1024;
		model.put("bodySize", bodySize);
	}
	
	/** json에 담긴 사용자, 조직 데이터를 목록으로 변환*/
	public void setUserOrDept(Map<String, Object> wfWorksLVoMap){
		
	}
	
}
