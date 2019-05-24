package com.innobiz.orange.web.wl.svc;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.utils.ArrayUtil;
import com.innobiz.orange.web.cm.utils.ParamUtil;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.CommonFileVo;
import com.innobiz.orange.web.cm.vo.CommonVo;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtSysSvc;
import com.innobiz.orange.web.wl.utils.WlConstant;
import com.innobiz.orange.web.wl.vo.WlLogFileDVo;
import com.innobiz.orange.web.wl.vo.WlReprtGrpBVo;
import com.innobiz.orange.web.wl.vo.WlReprtGrpLVo;
import com.innobiz.orange.web.wl.vo.WlReprtUserLVo;
import com.innobiz.orange.web.wl.vo.WlTaskConsolRVo;
import com.innobiz.orange.web.wl.vo.WlTaskLogBVo;
import com.innobiz.orange.web.wl.vo.WlTaskLogRVo;
import com.innobiz.orange.web.wl.vo.WlTaskLogUserLVo;

@Service
public class WlLogSvc {
	/** Logger */
	//private static final Logger LOGGER = Logger.getLogger(WlLogSvc.class);
	
//	/** 메세지 */
//	@Autowired
//    private MessageProperties messageProperties;
	
	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;
	
	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
//	/** 포털 보안 서비스 */
//	@Autowired
//	private PtSecuSvc ptSecuSvc;
	
//	/** 검색 서비스 */
//	@Autowired
//	private EmSrchSvc emSrchSvc;
	
	/** 공통 서비스 */
	@Autowired
	private WlCmSvc wlCmSvc;
	
	/** 파일 서비스 */
	@Resource(name = "wlFileSvc")
	private WlFileSvc wlFileSvc;
	
	/** 관리 서비스 */
	@Autowired
	private WlAdmSvc wlAdmSvc;
	
//	/** 포탈 공통 서비스 */
//	@Autowired
//	private PtCmSvc ptCmSvc;
	
	/** 메뉴별 조회조건 세팅 */
	public void setQueryUrlOptions(HttpServletRequest request, ModelMap model, WlTaskLogBVo wlTaskLogBVo, String suffix, 
			UserVo userVo, String langTypCd, boolean isAdmin) throws SQLException, CmException{
		String typCd = ParamUtil.getRequestParam(request, "typCd", false);
		// 환경설정 조회
		Map<String, String> configMap=getEnvConfigAttr(null, userVo.getCompId(), null);
		
		// 사용자 환경설정 세팅
		Map<String, String> userConfigMap = wlCmSvc.getUserConfigMap(null, userVo.getUserUid());
		//model.put("userConfigMap", userConfigMap);
		
		if(!isAdmin && userConfigMap!=null && userConfigMap.containsKey("typCd") && (typCd==null || typCd.isEmpty())){
			typCd=userConfigMap.get("typCd");
			wlTaskLogBVo.setTypCd(typCd);
			model.put("typCd", typCd);
		}
			
		if(typCd!=null && !typCd.isEmpty() && "all".equals(typCd)){
			typCd=null;
			wlTaskLogBVo.setTypCd(null);
		}
		if(!isAdmin){
			suffix=suffix.toLowerCase();
			if(suffix.startsWith("recv")){ // 보고받은
				wlTaskLogBVo.setUserUid(userVo.getUserUid());
				
				// 기간 검색 조건이 없을경우
				if((wlTaskLogBVo.getDurStrtDt()==null || wlTaskLogBVo.getDurStrtDt().isEmpty()) 
						&& ( wlTaskLogBVo.getDurEndDt()==null || wlTaskLogBVo.getDurEndDt().isEmpty())){
					Map<String,String> durMap=null;
					// 전체가 아니면서 보고일자 검색 조건이 없을 경우 
					if(typCd!=null && !typCd.isEmpty() && "reprtDt".equals(wlTaskLogBVo.getDurCat())){
						String typCdKey=getConfigTypString(typCd);
						String consolVa=userConfigMap.containsKey(typCdKey) ? userConfigMap.get(typCdKey) : configMap.containsKey(typCdKey) ? configMap.get(typCdKey) : null;
						if(consolVa!=null){
							durMap=setSrchDur(typCd, consolVa); // 기간 검색조건 조회
							setVoValue(wlTaskLogBVo, durMap); // 맵 데이터 매핑
							model.put("durMap", durMap);
						}else{
							wlTaskLogBVo.setOrderBy(" REPRT_DT DESC");
						}
					}
					String isReprtDisp=userConfigMap.containsKey("isReprtDisp") ? userConfigMap.get("isReprtDisp") : configMap.containsKey("isReprtDisp") ? configMap.get("isReprtDisp") : null;
					String grpNo = ParamUtil.getRequestParam(request, "grpNo", false);
					if(suffix.startsWith("recv") && model!=null && isReprtDisp!=null &&
							"Y".equals(isReprtDisp) && grpNo!=null && !grpNo.isEmpty()){
						model.put("notReprtUserList", getNotReprtUserList(userVo, langTypCd, grpNo, typCd, durMap));
					}
				}
				
			}else if(suffix.startsWith("log")){ // 작성한
				wlTaskLogBVo.setRegrUid(userVo.getUserUid());
			}else if(suffix.startsWith("consol")){ // 취합된
				wlTaskLogBVo.setConsolYn("Y");
				wlTaskLogBVo.setConsolUid(userVo.getUserUid());
			}else if(suffix.startsWith("dept")){ // 부서
				wlTaskLogBVo.setDeptId(userVo.getDeptId());
				String deptSrchOpt=configMap.containsKey("deptSrchOpt") ? configMap.get("deptSrchOpt") : null;
				if(deptSrchOpt==null) deptSrchOpt="log";
				if("log".equals(deptSrchOpt)) wlTaskLogBVo.setConsolYn("N");
				else if("consol".equals(deptSrchOpt)) wlTaskLogBVo.setConsolYn("Y");
				//if(!configMap.containsKey("consolOpenYn") || "N".equals(configMap.get("consolOpenYn"))) wlTaskLogBVo.setConsolYn("N");
				
			}else if(suffix.startsWith("temp")){ // 임시저장
				wlTaskLogBVo.setRegrUid(userVo.getUserUid());
				wlTaskLogBVo.setStatCd("T");
			}
		}
		
		if(wlTaskLogBVo.getStatCd()==null || wlTaskLogBVo.getStatCd().isEmpty())
			wlTaskLogBVo.setStatCd("C"); // 작성완료
		
		if(typCd==null || typCd.isEmpty()){
			// 전체가 빠짐
			wlTaskLogBVo.setOrderBy(" REPRT_DT DESC");
		}
		
		// 관리자 조회 조건
		if(isAdmin){
			String schOrgId = ParamUtil.getRequestParam(request, "schOrgId", false);
			wlTaskLogBVo.setDeptId(schOrgId);
			
			String schOptCat = ParamUtil.getRequestParam(request, "schOptCat", false);
			String schUserUid = ParamUtil.getRequestParam(request, "schUserUid", false);
			if(schOptCat!=null && schUserUid!=null && !schUserUid.isEmpty()){
				if("REGRUID".equals(schOptCat))
					wlTaskLogBVo.setRegrUid(schUserUid);
				else if("REPRTUID".equals(schOptCat))
					wlTaskLogBVo.setConsolUid(schUserUid);				
			}
		}
	}
	
	/** 탭 목록 세팅 */
	public List<String> setTabList(ModelMap model, String compId, boolean isAll) throws SQLException{
		// 환경설정 조회
		Map<String, String> envConfigMap = wlAdmSvc.getEnvConfigMap(null, compId);
		// 탭 목록
		List<String> tabList = new ArrayList<String>();
		if(isAll) tabList.add("all");
		if(envConfigMap.containsKey("typCds")){
			String[] typCds=envConfigMap.get("typCds").split("/");
			for(String key : typCds){
				tabList.add(key);
			}
			if(model!=null) model.put("tabList", tabList);
		}
		return tabList;
	}
	
	/** 탭 목록 세팅 */
	public Map<String,String> getTabListMap(ModelMap model, String compId) throws SQLException{
		Map<String,String> returnMap = new HashMap<String,String>();
		// 탭 목록
		List<String> tabList = setTabList(null, compId, false);
		if(tabList.size()>0){
			for(String tab : tabList)
				returnMap.put(tab, "Y");
		}
		return returnMap;
	}
	
	/** 기준일로 일일,주간,월간,연간 기간 조회 */
	public Map<String,String> setSrchDur(String typCd, String consolVa){
		Map<String,String> returnMap = new HashMap<String, String>();
		Calendar durCal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String durStrtDt=null, durEndDt=null;
		if("day".equals(typCd)){
			durStrtDt=sdf.format(durCal.getTime());
			durEndDt=sdf.format(durCal.getTime());
		}else if("week".equals(typCd)){
			Integer dayOfWeek = durCal.get(Calendar.DAY_OF_WEEK); // 현재 요일(1~)
			int weekVa=Integer.parseInt(consolVa); // 요일(숫자)
			if(dayOfWeek<weekVa) // 현재 요일보다 기준요일이 이후 일 경우..
				durCal.add(Calendar.DATE, weekVa-dayOfWeek); // 기준요일의 날짜를 구함
			else if(dayOfWeek>weekVa) // 현재 요일이 기준요일 이후 일 경우..
				durCal.add(Calendar.DATE, 7-(dayOfWeek-weekVa)); // 기준요일(미래)의 날짜를 구함(7일 - (현재요일-기준요일))					
			durEndDt=sdf.format(durCal.getTime());
			durCal.add(Calendar.DATE, -6);// 종료일에서 6일 전(포함)
			durStrtDt=sdf.format(durCal.getTime());
		}else if("month".equals(typCd)){
			int dayOfMonth=durCal.getActualMaximum(Calendar.DATE); // 현재 월의 마지막 일
			int dayVa=Integer.parseInt(consolVa);
			if(dayOfMonth<dayVa) dayVa=dayOfMonth; // 기준일 보다 현재월의 마지막 일이 작을 경우 현재월의 마지막 일로 입력
			Calendar todayCal = Calendar.getInstance();
			todayCal.set(Calendar.HOUR_OF_DAY, 0); // 0시
			todayCal.set(Calendar.MINUTE, 0); // 0분
			durCal.set(Calendar.DAY_OF_MONTH, dayVa); // 일 세팅
			durCal.set(Calendar.HOUR_OF_DAY, 0); // 0시
			durCal.set(Calendar.MINUTE, 0); // 0분
			
			if(todayCal.before(durCal) || todayCal.equals(durCal)){ // 오늘이 기준일보다 이전 이거나 같으면
				durEndDt=sdf.format(durCal.getTime()); // 종료일을 기준일로 하고
				durCal.add(Calendar.MONTH, -1);
				durCal.add(Calendar.DATE, 1);
				durStrtDt=sdf.format(durCal.getTime());
			}else if(todayCal.after(durCal)){ // 오늘이 기준일보다 이후이면
				durStrtDt=sdf.format(durCal.getTime()); // 기준일이 시작일이 되고
				durCal.add(Calendar.MONTH, 1); // 1개월 이후
				durEndDt=sdf.format(durCal.getTime());
			}
			
		}else if("year".equals(typCd)){
			int monthVa=Integer.parseInt(consolVa.substring(0, 2)); // 기준월
			int dayVa=Integer.parseInt(consolVa.substring(2));// 기준일
			
			Calendar todayCal = Calendar.getInstance();
			todayCal.set(Calendar.HOUR_OF_DAY, 0); // 0시
			todayCal.set(Calendar.MINUTE, 0); // 0분
			durCal.set(Calendar.MONTH, monthVa-1); // 기준월
			durCal.set(Calendar.DAY_OF_MONTH, dayVa);// 기준일
			durCal.set(Calendar.HOUR_OF_DAY, 0);
			durCal.set(Calendar.MINUTE, 0);
			
			if(todayCal.before(durCal) || todayCal.equals(durCal)){ // 오늘이 기준일보다 이전 이거나 같으면
				durEndDt=sdf.format(durCal.getTime()); // 종료일을 기준일로 하고
				durCal.add(Calendar.YEAR, -1);
				durCal.add(Calendar.DATE, 1);
				durStrtDt=sdf.format(durCal.getTime());
			}else if(todayCal.after(durCal)){ // 오늘이 기준일보다 이후이면
				durCal.add(Calendar.DATE, 1); // 기준일에서 1일 추가
				durStrtDt=sdf.format(durCal.getTime()); // 기준일이 시작일이 됨
				durCal.add(Calendar.YEAR, 1);
				durCal.add(Calendar.DATE, -1);
				durEndDt=sdf.format(durCal.getTime());
			}
			
		}
		returnMap.put("durStrtDt", durStrtDt);
		returnMap.put("durEndDt", durEndDt);
		return returnMap;
		
		//wlTaskLogBVo.setOrderBy(" REPRT_DT ASC");
	}
	
	/** Vo 에 Map 데이터 매핑 */
	public void setVoValue(CommonVo commonVo, Map<String,String> paramMap){
		if(paramMap==null || paramMap.size()==0) return;
		// set header
		Entry<String, String> entry;
		Iterator<Entry<String, String>> iterator = paramMap.entrySet().iterator();
				
		while(iterator.hasNext()){
			entry = iterator.next();
			if(paramMap.get(entry.getKey())==null) continue;
			VoUtil.setValue(commonVo, entry.getKey(), paramMap.get(entry.getKey()));
		}
	}
	
	/** 컬럼 목록 조회 */
	public List<String[]> getColumnList(ModelMap model, Map<String, String> configMap, String compId, String langTypCd) throws SQLException{
		List<String[]> columnList=null;
		// 환경설정 조회
		Map<String, String> envConfigMap = getEnvConfigAttr(null, compId, null);
		if(envConfigMap==null) return null;
		String[] column=null;
		columnList=new ArrayList<String[]>();
		String chkKey;
		for(String key : WlConstant.COL_LIST){
			chkKey=key+"UseYn"; // 사용여부
			if(!envConfigMap.containsKey(chkKey) || (envConfigMap.containsKey(chkKey) && "N".equals(envConfigMap.get(chkKey)))) continue;
			column=new String[3];
			column[0]=key;
			chkKey=key+"RescId_"+langTypCd; // 리소스명
			if(envConfigMap.containsKey(chkKey) && !envConfigMap.get(chkKey).isEmpty()){
				column[1]=envConfigMap.get(chkKey);
			}
			chkKey=key+"Height"; // 높이
			if(envConfigMap.containsKey(chkKey) && !envConfigMap.get(chkKey).isEmpty()){
				column[2]=envConfigMap.get(chkKey);
			}else{
				column[2]=null;
			}
			columnList.add(column);
		}
		
		if(model!=null) model.put("columnList", columnList);
		return columnList;
	}
	
	/** 컬럼 내용 조회해서 맵으로 변환 - 단일*/
	public Map<String,String> getColumnContList(ModelMap model, List<String[]> columnList, WlTaskLogBVo wlTaskLogBVo) throws SQLException{
		if(columnList==null || columnList.size()==0 || wlTaskLogBVo==null) return null;
		Map<String,String> columnMap = new HashMap<String,String>();
		String key, va;
		for(String[] column : columnList){
			key=column[0]+"Cont";
			va=(String)VoUtil.getValue(wlTaskLogBVo, key);
			if(va==null || va.isEmpty()) continue;
			columnMap.put(key, va);
		}
		if(model!=null) model.put("columnMap", columnMap);
		return columnMap;
	}
	
	/** 컬럼 내용 조회해서 맵으로 변환 - 다수 */
	public Map<String,String> getColumnContList(ModelMap model, List<String[]> columnList, List<WlTaskLogBVo> wlTaskLogBVoList) throws SQLException{
		if(columnList==null || columnList.size()==0 || wlTaskLogBVoList==null) return null;
		Map<String,String> columnMap = new HashMap<String,String>();
		String key, va;
		for(WlTaskLogBVo vo : wlTaskLogBVoList){
			for(String[] column : columnList){
				key=column[0]+"Cont";
				va=(String)VoUtil.getValue(vo, key);
				if(va==null || va.isEmpty()) continue;
				if(columnMap.containsKey(key)) columnMap.put(key, columnMap.get(key)+va);
				else columnMap.put(key, va);
			}
		}
		if(model!=null) model.put("columnMap", columnMap);
		return columnMap;
	}
	
	/** 업무일지 저장 */
	public String saveTaskLog(HttpServletRequest request, QueryQueue queryQueue, String logNo, boolean isUserList) throws CmException, SQLException {
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
					
		// 테이블관리 기본(WL_TASK_LOG_B) 테이블 - BIND
		WlTaskLogBVo wlTaskLogBVo = new WlTaskLogBVo();
		VoUtil.bind(request, wlTaskLogBVo);
		wlTaskLogBVo.setQueryLang(langTypCd);
		wlTaskLogBVo.setCompId(userVo.getCompId());
		if(wlTaskLogBVo.getStatCd()==null || wlTaskLogBVo.getStatCd().isEmpty()) wlTaskLogBVo.setStatCd("C");
		
		// 등록자UID
		String regrUid = userVo.getUserUid();
		boolean isNew=false; // 신규 여부
		if(logNo==null || logNo.isEmpty()){
			isNew=true;
			logNo=wlCmSvc.createNo("WL_TASK_LOG_B").toString();
			wlTaskLogBVo.setLogNo(logNo);
			wlTaskLogBVo.setDeptId(userVo.getDeptId());
			if(request.getRequestURI().startsWith("/wl/task/transLog")) wlTaskLogBVo.setConsolYn("N"); // 취합여부
			else if(request.getRequestURI().startsWith("/wl/task/transRecv")) wlTaskLogBVo.setConsolYn("Y"); // 취합여부
			wlTaskLogBVo.setRegrUid(regrUid);
			wlTaskLogBVo.setRegDt("sysdate");
			queryQueue.insert(wlTaskLogBVo);
			
			// 업무일지 관계 저장
			WlTaskLogRVo wlTaskLogRVo = new WlTaskLogRVo();
			wlTaskLogRVo.setLogNo(logNo);
			wlTaskLogRVo.setRegrUid(regrUid);
			queryQueue.insert(wlTaskLogRVo);
			
			
		}else{
			wlTaskLogBVo.setModrUid(regrUid);
			wlTaskLogBVo.setModDt("sysdate");
			queryQueue.update(wlTaskLogBVo);
		}
		
		if(isUserList){ // 보고대상 수정여부
			// 수정일 경우 보고대상 전체 삭제
			if(!isNew){
				WlTaskLogUserLVo deleteVo = new WlTaskLogUserLVo();
				deleteVo.setLogNo(logNo);
				queryQueue.delete(deleteVo);
			}
		
			// 사용자 저장 | 보고대상 - BIND 
			@SuppressWarnings("unchecked")
			List<WlTaskLogUserLVo> boundVoList = (List<WlTaskLogUserLVo>) VoUtil.bindList(request, WlTaskLogUserLVo.class, new String[]{"userUid"});
			if(boundVoList!=null && boundVoList.size()>0){
				int sortOrdr=1;
				for(WlTaskLogUserLVo vo : boundVoList){
					vo.setLogNo(logNo);
					//if(!isNew && commonDao.count(vo)>0) continue; // 이미 등록된 사용자는 제외.
					vo.setReadYn("N"); // 열람여부
					vo.setSortOrdr(sortOrdr); // 정렬순서
					vo.setRegrUid(regrUid);
					vo.setRegDt("sysdate");
					queryQueue.insert(vo);
					sortOrdr++;
				}
			}
		}
		
		String lstNos = ParamUtil.getRequestParam(request, "lstNos", false);
		
		if(lstNos!=null && !lstNos.isEmpty()){
			// 취합대상 목록 저장
			WlTaskConsolRVo wlTaskConsolRVo = null;
			// 수정이면 취합목록 전체 삭제
			if(!isNew){
				wlTaskConsolRVo = new WlTaskConsolRVo();
				wlTaskConsolRVo.setLogNo(logNo);
				queryQueue.delete(wlTaskConsolRVo);
			}
			int num=0;
			// 취합목록 저장
			for(String lstNo : lstNos.split(",")){
				lstNo=lstNo.trim();
				wlTaskConsolRVo = new WlTaskConsolRVo();
				wlTaskConsolRVo.setLogNo(logNo);
				wlTaskConsolRVo.setLstNo(lstNo);
				wlTaskConsolRVo.setSortOrdr(++num);
				queryQueue.insert(wlTaskConsolRVo);
			}
		}
		
		return logNo;
	}
	
	/** 업무일지 삭제 */
	public void deleteLog(QueryQueue queryQueue, UserVo userVo, String logNo, List<CommonFileVo> deletedFileList) throws SQLException{
		if(deletedFileList!=null){
			// 파일 삭제
			List<CommonFileVo> list=wlFileSvc.deleteLogFile(logNo, queryQueue);
			if(list!=null && list.size()>0) deletedFileList.addAll(list);
		}else{
			// 파일 삭제
			wlFileSvc.deleteLogFile(logNo, queryQueue);
		}
		
		// 보고대상 삭제
		WlTaskLogUserLVo wlTaskLogUserLVo = new WlTaskLogUserLVo();
		wlTaskLogUserLVo.setLogNo(logNo);
		queryQueue.delete(wlTaskLogUserLVo);
		
		// 업무일지 관계 삭제
		WlTaskLogRVo wlTaskLogRVo = new WlTaskLogRVo();
		wlTaskLogRVo.setLogNo(logNo);
		queryQueue.delete(wlTaskLogRVo);
					
		// 업무일지 삭제
		WlTaskLogBVo wlTaskLogBVo = new WlTaskLogBVo();
		wlTaskLogBVo.setCompId(userVo.getCompId());
		wlTaskLogBVo.setLogNo(logNo);
		
		WlTaskLogBVo tmpVo = (WlTaskLogBVo)commonDao.queryVo(wlTaskLogBVo);
		if(tmpVo!=null){
			// 취합대상 목록 삭제
			WlTaskConsolRVo wlTaskConsolRVo = new WlTaskConsolRVo();
			if("Y".equals(tmpVo.getConsolYn())) wlTaskConsolRVo.setLogNo(logNo);
			else wlTaskConsolRVo.setLstNo(logNo);
			if(commonDao.count(wlTaskConsolRVo)>0)
				queryQueue.delete(wlTaskConsolRVo);
		}
		queryQueue.delete(wlTaskLogBVo);
		
	}
	
	/** 업무일지 내용 조회 - 합치기 */
	public Map<String,String> getLogCont(ModelMap model, Map<String, String> configMap, List<WlTaskLogBVo> wlTaskLogBVoList, WlTaskLogBVo wlTaskLogBVo, UserVo userVo, String langTypCd) throws SQLException{
		if(wlTaskLogBVoList==null || wlTaskLogBVoList.size()==0) return null;
		// 컬럼 목록
		List<String[]> columnList=getColumnList(model, configMap, userVo.getCompId(), langTypCd);
		Map<String,String> columnListMap=getColumnContList(model, columnList, wlTaskLogBVoList);
		if(columnListMap!=null && wlTaskLogBVo!=null)
			setVoValue(wlTaskLogBVo, columnListMap);
		return columnListMap;
	}
	
	/** 업무일지 파일 조회 - 합치기 */
	public List<CommonFileVo> getLogFileList(List<String> logNoList) throws SQLException{
		List<CommonFileVo> fileVoList = null;
		if(logNoList==null || logNoList.size()==0) return null;
		List<CommonFileVo> returnList=new ArrayList<CommonFileVo>();
		for(String logNo : logNoList){
			fileVoList = wlFileSvc.getFileVoList(logNo);
			if(fileVoList==null || fileVoList.size()==0) continue;
			returnList.addAll(fileVoList);
		}
		return returnList;
	}
	
	/** 사용자 목록 조회 - 보고대상*/
	@SuppressWarnings("unchecked")
	public List<WlTaskLogUserLVo> getLogUserList(String logNo, String regrUid) throws SQLException{
		WlTaskLogUserLVo wlTaskLogUserLVo = new WlTaskLogUserLVo();
		if(logNo!=null) wlTaskLogUserLVo.setLogNo(logNo); // 일지번호로 조회
		if(regrUid!=null) wlTaskLogUserLVo.setRegrUid(regrUid); // 등록자UID로 조회
		return (List<WlTaskLogUserLVo>)commonDao.queryList(wlTaskLogUserLVo);
	}
	
	/** 보고그룹 목록 조회 */
	@SuppressWarnings("unchecked")
	public List<WlReprtGrpBVo> getReprtGrpList(String langTypCd, UserVo userVo, boolean isRecv) throws SQLException{
		WlReprtGrpBVo wlReprtGrpBVo = new WlReprtGrpBVo();
		wlReprtGrpBVo.setQueryLang(langTypCd);
		wlReprtGrpBVo.setCompId(userVo.getCompId());
		
		if(isRecv){
			// 나를 보고자로 지정한 그룹 목록 조회
			List<WlReprtGrpLVo> list = getReprtGrpDtlList(langTypCd, null, "R", userVo.getUserUid());
			if(list==null || list.size()==0)
				return null;
			List<String> grpNoList=new ArrayList<String>();
			for(WlReprtGrpLVo vo : list){
				grpNoList.add(vo.getGrpNo());
			}
			if(grpNoList.size()==0)
				return null;
			// 중복제거
			//HashSet<String> hashSet = new HashSet<String>(grpNoList);
			//grpNoList = new ArrayList<String>(hashSet);
			wlReprtGrpBVo.setGrpNoList(grpNoList);
		}else{
			wlReprtGrpBVo.setRegrUid(userVo.getUserUid());			
		}
		
		return (List<WlReprtGrpBVo>)commonDao.queryList(wlReprtGrpBVo);
	}
	
	/** 보고그룹 상세 목록 조회 */
	@SuppressWarnings("unchecked")
	public List<WlReprtGrpLVo> getReprtGrpDtlList(String langTypCd, String grpNo, String tgtTypCd, String userUid) throws SQLException{
		WlReprtGrpLVo wlReprtGrpLVo = new WlReprtGrpLVo();
		wlReprtGrpLVo.setQueryLang(langTypCd);
		if(grpNo!=null) wlReprtGrpLVo.setGrpNo(grpNo);
		if(tgtTypCd!=null) wlReprtGrpLVo.setTgtTypCd(tgtTypCd);
		if(userUid!=null) wlReprtGrpLVo.setUserUid(userUid);
		return (List<WlReprtGrpLVo>)commonDao.queryList(wlReprtGrpLVo);
	}
	
	/** 보고자 조회 - 업무일지 등록안된 사용자*/
	public List<String[]> getNotReprtUserList(UserVo userVo, String langTypCd, String typCd, Map<String,String> durMap) throws SQLException{
		// 보고자 목록 VO
		WlReprtUserLVo wlReprtUserLVo = new WlReprtUserLVo();
		wlReprtUserLVo.setQueryLang(langTypCd);
		wlReprtUserLVo.setRegrUid(userVo.getUserUid());
		if(commonDao.count(wlReprtUserLVo)==0) return null;
		// 전체 보고자 목록 조회
		@SuppressWarnings("unchecked")
		List<WlReprtUserLVo> wlReprtUserLVoList = (List<WlReprtUserLVo>)commonDao.queryList(wlReprtUserLVo);
				
		// 환경설정 조회
		Map<String, String> configMap=getEnvConfigAttr(null, userVo.getCompId(), null);
		
		String configTypCds=configMap.containsKey("typCds") ? configMap.get("typCds") : null;
		if(configTypCds==null) return null;
		
		if(typCd==null || typCd.isEmpty()) typCd="all";
		// 업무일지를 작성하지 않은 보고자 목록
		List<String[]> returnList=new ArrayList<String[]>();
		// 보고자 정보(사용자UID, 사용자명)
		String[] notReprtUsers=null;
		// 테이블관리 기본(WL_TASK_LOG_B) 테이블 - BIND
		WlTaskLogBVo wlTaskLogBVo = null;
		
		if(typCd!=null && "all".equals(typCd)){
			typCd=configTypCds;
		}
		// 취합기준일 설정 속성명
		String typCdKey=null;
		// 취합기준일 설정값(시작일, 종료일)
		// 중복체크 맵
		Map<String,String> userMap=new HashMap<String,String>();
		// 업무일지 종류 목록
		String[] typCds=typCd.split("/");
		for(String cd : typCds){
			if(!"all".equals(typCd) && !ArrayUtil.isInArray(configTypCds.split("/"), cd)) continue;
			typCdKey=getConfigTypString(cd);
			if(!configMap.containsKey(typCdKey)) continue;
			if(durMap==null)
				durMap=setSrchDur(cd, configMap.get(typCdKey));
			for(WlReprtUserLVo reprtVo : wlReprtUserLVoList){
				if(reprtVo.getTypCd()==null || reprtVo.getTypCd().isEmpty()) continue;
				if(!ArrayUtil.isInArray(reprtVo.getTypCd().split("/"), cd)) continue;
				wlTaskLogBVo = new WlTaskLogBVo();
				wlTaskLogBVo.setTypCd(cd); // 일일,주간,월간,연간
				wlTaskLogBVo.setStatCd("C"); // 완료 상태
				wlTaskLogBVo.setRegrUid(reprtVo.getUserUid()); // 등록자UID
				setVoValue(wlTaskLogBVo, durMap); // 기간 조건
				if(commonDao.count(wlTaskLogBVo)==0){
					if(userMap.containsKey(reprtVo.getUserUid())) continue;
					notReprtUsers=new String[2];
					notReprtUsers[0]=reprtVo.getUserUid();
					notReprtUsers[1]=reprtVo.getUserNm();
					returnList.add(notReprtUsers);
					userMap.put(reprtVo.getUserUid(), "Y");
				}
			}
		}
		
		return returnList;
		
	}
	
	/** 취합대상 목록 조회 - 일지번호 */
	public String getConsolRLogNos(String logNo) throws SQLException{
		String lstNos="";
		// 취합대상 목록
		WlTaskConsolRVo wlTaskConsolRVo = new WlTaskConsolRVo();
		wlTaskConsolRVo.setLogNo(logNo);
		@SuppressWarnings("unchecked")
		List<WlTaskConsolRVo> wlTaskConsolRVoList = (List<WlTaskConsolRVo>)commonDao.queryList(wlTaskConsolRVo);
		if(wlTaskConsolRVoList!=null && wlTaskConsolRVoList.size()>0){
			for(WlTaskConsolRVo vo : wlTaskConsolRVoList){
				lstNos+="".equals(lstNos) ? vo.getLstNo() : "," + vo.getLstNo();
			}
		}
		return lstNos;
	}
	
	/** 취합대상 목록 조회 */
	@SuppressWarnings("unchecked")
	public List<WlTaskLogBVo> getWlTaskConsolRVoList(String logNo, String langTypCd, String lstNos) throws SQLException{
		if(logNo==null && lstNos==null) return null;
		List<WlTaskLogBVo> returnList=null;
		List<WlTaskConsolRVo> wlTaskConsolRVoList=null;
		// 취합대상 목록
		WlTaskConsolRVo wlTaskConsolRVo = null;
		if(logNo!=null){
			wlTaskConsolRVo = new WlTaskConsolRVo();
			wlTaskConsolRVo.setLogNo(logNo);
			wlTaskConsolRVoList = (List<WlTaskConsolRVo>)commonDao.queryList(wlTaskConsolRVo);
		}else{
			wlTaskConsolRVoList=new ArrayList<WlTaskConsolRVo>();
			String[] logNos = lstNos.split(",");
			int sortOrdr=0;
			for(String no : logNos){
				wlTaskConsolRVo = new WlTaskConsolRVo();
				wlTaskConsolRVo.setLstNo(no.trim());
				wlTaskConsolRVo.setSortOrdr(++sortOrdr);
				wlTaskConsolRVoList.add(wlTaskConsolRVo);
			}
		}
		if(wlTaskConsolRVoList!=null && wlTaskConsolRVoList.size()>0){
			List<String> logNoList = new ArrayList<String>();
			// 조회할 일지번호 목록 세팅
			for(WlTaskConsolRVo vo : wlTaskConsolRVoList){
				logNoList.add(vo.getLstNo());
			}
			if(logNoList.size()>0){
				// 업무일지 조회
				WlTaskLogBVo wlTaskLogBVo = new WlTaskLogBVo();
				wlTaskLogBVo.setQueryLang(langTypCd);
				wlTaskLogBVo.setLogNoList(logNoList);
				
				List<WlTaskLogBVo> wlTaskLogBVoList = (List<WlTaskLogBVo>)commonDao.queryList(wlTaskLogBVo);
				if(wlTaskLogBVoList!=null && wlTaskLogBVoList.size()>0){
					returnList=new ArrayList<WlTaskLogBVo>();
					// 정렬순서 맵
					Map<String, WlTaskLogBVo> listMap = new HashMap<String, WlTaskLogBVo>();
					for(WlTaskLogBVo vo : wlTaskLogBVoList){
						listMap.put(vo.getLogNo(), vo);
					}
					// 취합 순서대로 목록변경
					for(WlTaskConsolRVo vo : wlTaskConsolRVoList){
						if(!listMap.containsKey(vo.getLstNo())) continue;
						returnList.add(listMap.get(vo.getLstNo()));
					}
				}
			}
		}
		return returnList;
	}
	
	/** 설정 조회 */
	public Map<String, String> getEnvConfigAttr(ModelMap model, String compId, String[] keys) throws SQLException{
		// 환경설정 조회
		Map<String,String> envConfigMap = wlAdmSvc.getEnvConfigMap(null, compId);		
		if(keys==null) return envConfigMap;
		Map<String,String> configMap=new HashMap<String,String>();
		for(String key : keys){
			if(envConfigMap.containsKey(key)){
				configMap.put(key, envConfigMap.get(key));
			}
		}
		if(model!=null) model.put("configMap", configMap);
		return configMap;
	}
	
	/** 첨부 여부 세팅 */
	public void setFileCnt(String compId, List<WlTaskLogBVo> list) throws SQLException{
		if(list==null || list.size()==0) return;
		// 환경설정 - 첨부파일 사용여부
		Map<String, String> configMap=getEnvConfigAttr(null, compId, new String[]{"fileYn"});
		if(!configMap.containsKey("fileYn") || "N".equals(configMap.get("fileYn"))) return;
			
		// 첨부파일(BA_BULL_FILE_D) 테이블 - SELECT
		WlLogFileDVo wlLogFileDVo = new WlLogFileDVo();
		
		for(WlTaskLogBVo storedWlTaskLogBVo : list){
			wlLogFileDVo.setRefId(storedWlTaskLogBVo.getLogNo());
			storedWlTaskLogBVo.setFileCnt(commonDao.count(wlLogFileDVo));
		}
	}
	
	/** 최대 본문 사이즈 model에 추가 */
	public void putBodySizeToModel(HttpServletRequest request, ModelMap model) throws SQLException {
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		UserVo userVo = LoginSession.getUser(request);
		
		Integer bodySize = ptSysSvc.getBodySizeMap(langTypCd, userVo.getCompId()).get("wl");
		if(bodySize==null) bodySize=0;
		// 시스템 설정 조회 - 본문 사이즈
		bodySize = bodySize * 1024;
		model.put("bodySize", bodySize);
	}
	
	/** 취합기준일 설정 속성 */
	public String getConfigTypString(String typCd){
		return WlConstant.CONSOL_PREFIX+typCd.toUpperCase().charAt(0)+typCd.substring(1);
	}
	
	/** 보고자 조회 - 업무일지 등록안된 사용자 (보고그룹) */
	public List<String[]> getNotReprtUserList(UserVo userVo, String langTypCd, String grpNo, String typCd, Map<String,String> durMap) throws SQLException{
		// 보고자 그룹 VO
		WlReprtGrpBVo wlReprtGrpBVo = new WlReprtGrpBVo();
		wlReprtGrpBVo.setQueryLang(langTypCd);
		wlReprtGrpBVo.setCompId(userVo.getCompId());
		wlReprtGrpBVo.setRegrUid(userVo.getUserUid());
		if(commonDao.count(wlReprtGrpBVo)==0) return null;
		
		// 보고자 그룹 목록 조회
		List<WlReprtGrpLVo> wlReprtGrpLVoList = getReprtGrpDtlList(langTypCd, grpNo, "R", null);
		
		// 환경설정 조회
		Map<String, String> configMap=getEnvConfigAttr(null, userVo.getCompId(), null);
		
		String configTypCds=configMap.containsKey("typCds") ? configMap.get("typCds") : null;
		if(configTypCds==null) return null;
		
		if(typCd==null || typCd.isEmpty()) typCd="all";
		// 업무일지를 작성하지 않은 보고자 목록
		List<String[]> returnList=new ArrayList<String[]>();
		// 보고자 정보(사용자UID, 사용자명)
		String[] notReprtUsers=null;
		// 테이블관리 기본(WL_TASK_LOG_B) 테이블 - BIND
		WlTaskLogBVo wlTaskLogBVo = null;
		
		if(typCd!=null && "all".equals(typCd)){
			typCd=configTypCds;
		}
		// 취합기준일 설정 속성명
		String typCdKey=null;
		// 취합기준일 설정값(시작일, 종료일)
		// 중복체크 맵
		Map<String,String> userMap=new HashMap<String,String>();
		// 업무일지 종류 목록
		String[] typCds=typCd.split("/");
		for(String cd : typCds){
			if(!"all".equals(typCd) && !ArrayUtil.isInArray(configTypCds.split("/"), cd)) continue;
			typCdKey=getConfigTypString(cd);
			if(!configMap.containsKey(typCdKey)) continue;
			if(durMap==null)
				durMap=setSrchDur(cd, configMap.get(typCdKey));
			for(WlReprtGrpLVo reprtVo : wlReprtGrpLVoList){
				if(reprtVo.getReprtTypCd()==null || reprtVo.getReprtTypCd().isEmpty()) continue;
				if(!ArrayUtil.isInArray(reprtVo.getReprtTypCd().split("/"), cd)) continue;
				wlTaskLogBVo = new WlTaskLogBVo();
				wlTaskLogBVo.setTypCd(cd); // 일일,주간,월간,연간
				wlTaskLogBVo.setStatCd("C"); // 완료 상태
				wlTaskLogBVo.setRegrUid(reprtVo.getUserUid()); // 등록자UID
				wlTaskLogBVo.setGrpNo(grpNo);
				setVoValue(wlTaskLogBVo, durMap); // 기간 조건
				if(commonDao.count(wlTaskLogBVo)==0){
					if(userMap.containsKey(reprtVo.getUserUid())) continue;
					notReprtUsers=new String[2];
					notReprtUsers[0]=reprtVo.getUserUid();
					notReprtUsers[1]=reprtVo.getUserNm();
					returnList.add(notReprtUsers);
					userMap.put(reprtVo.getUserUid(), "Y");
				}
			}
		}
		
		return returnList;
		
	}
	
}
