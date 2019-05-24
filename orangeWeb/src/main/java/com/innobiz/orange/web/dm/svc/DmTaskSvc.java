package com.innobiz.orange.web.dm.svc;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.utils.ArrayUtil;
import com.innobiz.orange.web.cm.utils.JsonUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.StringUtil;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.dm.utils.DmConstant;
import com.innobiz.orange.web.dm.vo.DmDocLVo;
import com.innobiz.orange.web.dm.vo.DmTaskHVo;
import com.innobiz.orange.web.or.vo.OrUserBVo;

@Service
public class DmTaskSvc {
	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(DmTaskSvc.class);
	
	/** 메세지 */
	@Autowired
    private MessageProperties messageProperties;
	
	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;
	
	/** 저장소 서비스 */
	@Resource(name = "dmStorSvc")
	private DmStorSvc dmStorSvc;
	
	/** 관리 서비스 */
	@Autowired
	private DmAdmSvc dmAdmSvc;
	
	/** 작업구분 목록 */
	public List<Map<String,String>> getDmTaskCdList(HttpServletRequest request) throws SQLException {
		List<Map<String,String>> returnList = new ArrayList<Map<String,String>>();
		Map<String,String> map = null;
		for(String va : DmConstant.TASK_CDS){
			map = new HashMap<String,String>();
			map.put("va", va);
			map.put("msg", messageProperties.getMessage("dm.cols.task."+va, request));
			returnList.add(map);
		}
		
		return returnList;
	}
	
	/** 관리자 설정에서 해당 작업구분을 체크한다. */
	public boolean getTaskCdChk(Map<String, String> envConfigMap, String compId, String cd) throws SQLException {
		if(envConfigMap == null) envConfigMap = dmAdmSvc.getEnvConfigMap(null, compId);
		// 관리자 설정 작업코드 배열
		String taskCds = envConfigMap.get("taskCds");
		if(taskCds != null && !taskCds.isEmpty()) return ArrayUtil.isInArray(taskCds.split(","), cd);
		return false;
	}
	
	/** 작업상세 맵 생성 */
	public Map<String,Map<String,String>> getTaskMap(String[][] rescVals){
		if(rescVals == null ) return null;
		Map<String,Map<String,String>> detlMap = new HashMap<String,Map<String,String>>();
		Map<String,String> itemMap = null;
		String attId = null;
		for(String[] rescVal : rescVals){
			itemMap = new HashMap<String,String>();
			itemMap.put("msgId", rescVal[0]);
			itemMap.put("value", rescVal[1]);
			attId = rescVal[0].substring(rescVal[0].lastIndexOf('.')+1);
			detlMap.put(attId, itemMap);
		}
		return detlMap;
	}
	
	/** 작업상세 맵 목록 생성 */
	public List<Map<String,String>> getTaskMapList(String[][] rescVals){
		if(rescVals == null ) return null;
		List<Map<String,String>> detlList = new ArrayList<Map<String,String>>();
		Map<String,String> itemMap = null;
		//String attId = null;
		for(String[] rescVal : rescVals){
			itemMap = new HashMap<String,String>();
			itemMap.put("msgId", rescVal[0]);
			itemMap.put("value", rescVal[1]);
			//attId = rescVal[0].substring(rescVal[0].lastIndexOf('.')+1);
			detlList.add(itemMap);
			//detlMap.put(attId, itemMap);
		}
		return detlList;
	}
	
	/** 작업상세 맵 목록 생성 - 목록(이력상세가 추가될 경우 해당 메소드 사용) */
	public List<Map<String,String>> getTaskMapList(DmDocLVo dmDocLVo, String... keys){
		if(keys == null || keys.length==0) return null;
		List<Map<String,String>> detlList = new ArrayList<Map<String,String>>();
		Map<String,String> itemMap = null;
		String value = null;
		for(String attr : keys){
			value = (String)VoUtil.getValue(dmDocLVo, attr); 
			if(value==null || value.isEmpty()) continue;
			itemMap = new HashMap<String,String>();
			itemMap.put("msgId", "dm.cols."+attr); // messagekey 생성
			itemMap.put("value", value); // value
			detlList.add(itemMap);
		}
		return detlList;
	}
	
	/** 작업별 저장할 상세 목록 */
	public String[][] getTaskMapList(DmDocLVo originVo, String actKey){
		String[][] rescVals = null;
		if("send".equals(actKey)){
			//rescVals = new String[][]{{"dm.cols.fld",originVo.getFldNm()}};
		}else if("receive".equals(actKey)){
			
		}else if("recovery".equals(actKey)){
			
		}else if("recycle".equals(actKey)){
			
		}else if("email".equals(actKey)){
			
		}
		return rescVals;
	}
	
	
	/** 작업이력 중복조회 */
	public boolean isTaskChk(String tableName, String docGrpId, String userUid, String taskCd, String taskDt) throws SQLException{
		// 작업이력VO
		DmTaskHVo dmTaskHVo = new DmTaskHVo();
		dmTaskHVo.setTableName(tableName);
		if(taskDt != null) dmTaskHVo.setTaskDt(taskDt);
		dmTaskHVo.setDocGrpId(docGrpId);
		if(userUid != null) dmTaskHVo.setUserUid(userUid);
		if(taskCd != null) dmTaskHVo.setTaskCd(taskCd);
		if(commonDao.count(dmTaskHVo)>0) return true;
		return false;
	}
	
	/** 조회이력 옵션 체크 및 이력 저장 */
	public boolean isChkReadHst(Map<String, String> envConfigMap, String langTypCd, String tableName, String docGrpId, String userUid) throws SQLException, CmException{
		String taskCd = "view";
		
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		HttpSession session = request.getSession();
		String toDate = commonDao.querySysdate(new CommonVoImpl("YYYY-MM-DD HH24:MI:SS"));
		String mergeKey = docGrpId+":"+userUid+":"+taskCd;
		
		if(session.getAttribute(mergeKey)==null) session.setAttribute(mergeKey, toDate);
		else{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String taskDt = (String)session.getAttribute(mergeKey);
			try{
				Date date = sdf.parse(taskDt);
				Calendar c = Calendar.getInstance();
				c.setTime(date);
				c.add(Calendar.SECOND, 2);
				if(Timestamp.valueOf(toDate).before(Timestamp.valueOf(sdf.format(c.getTime())))){
					return true;
				}
				session.setAttribute(mergeKey, toDate);
			}catch(ParseException pe){
				pe.printStackTrace();
				return true;
			}
		}
		
		// 관리자 환경설정에서 조회이력옵션 조회
		String readCntRule = envConfigMap.get("readCntRule");
		// 사용자 조회여부
		boolean returnChk = isTaskChk(tableName, docGrpId, userUid, taskCd, null);
		if(readCntRule != null){
			// '1일 1회' 설정시 해당일 이력 전체 삭제
			if("daily".equals(readCntRule) && isTaskChk(tableName, docGrpId, userUid, taskCd, StringUtil.getCurrYmd()))
				updateDmTask(tableName, docGrpId, userUid, taskCd, StringUtil.getCurrYmd(), true);
			// '한번만' 설정시 사용자 이력 전체 삭제
			else if("user".equals(readCntRule) && returnChk)
				updateDmTask(tableName, docGrpId, userUid, taskCd, null, true);
		}
		try{
			//이력 저장
			saveDmTask(null, tableName, langTypCd, docGrpId, userUid, taskCd, null);
		}catch(SQLException ignore){
			try { Thread.sleep(20); } catch(Exception e){}
		}
		
		return returnChk;
		
	}
	
	/** 작업 전후 목록 세팅 */
	public void setDmTaskList(List<DmTaskHVo> dmTaskHVoList, String docGrpId, List<Map<String,String>> detlMapList ){
		// 작업이력VO
		DmTaskHVo dmTaskHVo = new DmTaskHVo();
		dmTaskHVo.setTaskDt("sysdate");
		dmTaskHVo.setDocGrpId(docGrpId);
		if(detlMapList != null && detlMapList.size()>0) dmTaskHVo.setDetlMapList(detlMapList);
		dmTaskHVoList.add(dmTaskHVo);
	}
	
	/** 이전문서 현재 문서 비교 */
	public String[][] getDocCompareRescVals(DmDocLVo originVo, DmDocLVo currentVo){
		String[][] rescVals = null;
		return rescVals;
	}
	
	/** 작업 이력 저장 */
	public void saveDmTask(QueryQueue queryQueue, String tableName, String langTypCd, String docGrpId, 
			String userUid, String taskCd, List<Map<String,String>> detlMapList) throws SQLException, CmException{
		//if(!isTaskChk(docGrpId+":"+userUid+":"+taskCd)) return;
		// 작업이력VO
		DmTaskHVo dmTaskHVo = new DmTaskHVo();
		dmTaskHVo.setTableName(tableName);
		dmTaskHVo.setTaskDt("sysdate");
		dmTaskHVo.setDocGrpId(docGrpId);
		dmTaskHVo.setUserUid(userUid);
		dmTaskHVo.setTaskCd(taskCd);
		// 사용자 정보
		OrUserBVo orUserBVo = dmAdmSvc.getOrUserBVo(userUid, langTypCd);
		// 작업이력 전체 맵
		Map<String,Object> jsonMap = new HashMap<String,Object>();
		
		// 사용자 정보 목록
		List<String[]> userInfoList = new ArrayList<String[]>();
		userInfoList.add(new String[]{"cols.userNm", orUserBVo.getUserNm()});// 사용자명
		userInfoList.add(new String[]{"cols.deptNm", orUserBVo.getDeptRescNm()});// 부서명
		userInfoList.add(new String[]{"cols.posit", orUserBVo.getPositNm()});// 직위
		userInfoList.add(new String[]{"cols.grade", orUserBVo.getGradeNm()});// 직급
		// 사용자 정보 맵
		List<Map<String,String>> userInfoMapList = getTaskMapList(ArrayUtil.to2Array(userInfoList));
		
		// 사용자정보 맵에 저장
		jsonMap.put("userInfo", userInfoMapList);
		
		// 상세목록 맵에 저장
		if(detlMapList != null && detlMapList.size()>0) jsonMap.put("taskDetl", detlMapList);
		
		dmTaskHVo.setNote(JsonUtil.toJson(jsonMap));
		try{
			if(queryQueue == null) commonDao.insert(dmTaskHVo);
			else queryQueue.insert(dmTaskHVo);
		}catch(SQLException sqle){
			LOGGER.error(sqle.getMessage());
		}catch(Exception e){
			LOGGER.error(e.getMessage());
		}
		
	}
	
	/** 작업 이력 삭제 OR 건수 조회 */
	public Integer updateDmTask(String tableName, String docGrpId, 
			String userUid, String taskCd, String taskDt, boolean isDel) throws SQLException, CmException{
		// 작업이력VO
		DmTaskHVo dmTaskHVo = new DmTaskHVo();
		dmTaskHVo.setTableName(tableName);
		if(taskDt != null) dmTaskHVo.setTaskDt(taskDt);
		dmTaskHVo.setDocGrpId(docGrpId);
		if(userUid != null) dmTaskHVo.setUserUid(userUid);
		if(taskCd != null) dmTaskHVo.setTaskCd(taskCd);
		return isDel ? commonDao.delete(dmTaskHVo) : commonDao.count(dmTaskHVo);
	}
	
	/** 작업 이력 삭제 */
	public void deleteDmTask(QueryQueue queryQueue, String tableName, 
			String docGrpId, String taskCd) throws SQLException, CmException{
		// 작업이력VO
		DmTaskHVo dmTaskHVo = new DmTaskHVo();
		dmTaskHVo.setTableName(tableName);
		dmTaskHVo.setDocGrpId(docGrpId);
		if(taskCd != null) dmTaskHVo.setTaskCd(taskCd);
		if(queryQueue != null) queryQueue.delete(dmTaskHVo);
		else commonDao.delete(dmTaskHVo);
	}
	
	/** 작업이력 중복체크 [2초 간격]*/
	public boolean isTaskChk(String mergeKey) throws SQLException{
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		HttpSession session = request.getSession();
		String toDate = commonDao.querySysdate(new CommonVoImpl("YYYY-MM-DD HH24:MI:SS"));
		//String mergeKey = docGrpId+":"+userUid+":"+taskCd;
		
		if(session.getAttribute(mergeKey)==null) session.setAttribute(mergeKey, toDate);
		else{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String taskDt = (String)session.getAttribute(mergeKey);
			try{
				Date date = sdf.parse(taskDt);
				Calendar c = Calendar.getInstance();
				c.setTime(date);
				c.add(Calendar.SECOND, 2);
				if(Timestamp.valueOf(toDate).before(Timestamp.valueOf(sdf.format(c.getTime())))){
					return true;
				}
				session.setAttribute(mergeKey, toDate);
			}catch(ParseException pe){
				pe.printStackTrace();
				return true;
			}
		}
		return false;
	}
}
