package com.innobiz.orange.web.dm.vo;

import java.util.List;
import java.util.Map;

import com.innobiz.orange.web.cm.vo.QueryType;

/****** Object:  Vo - Date: 2015/08/31 13:59 ******/
/**
* 작업이력(DM_TASK_H) 테이블 VO 
*/
@SuppressWarnings("serial")
public class DmTaskHVo extends DmCmmBVo {
 	/** 문서그룹ID */ 
	private String docGrpId;
	
	/** 작업일시 */ 
	private String taskDt;

 	/** 사용자UID */ 
	private String userUid;

 	/** 작업코드 */ 
	private String taskCd;

 	/** 비고 */ 
	private String note;
	
	/** 사용자명 */ 
	private String userNm;
	
	/** 작업상세 목록 */
	private List<Map<String,String>> detlMapList;
	
	/** 조회조건 */
	
	/** 작업코드 목록 */ 
	private String[] taskCdList;

	public void setDocGrpId(String docGrpId) { 
		this.docGrpId = docGrpId;
	}
	/** 문서그룹ID */ 
	public String getDocGrpId() { 
		return docGrpId;
	}
	
	public void setTaskDt(String taskDt) { 
		this.taskDt = taskDt;
	}
	/** 작업일시 */ 
	public String getTaskDt() { 
		return taskDt;
	}
	
	public void setUserUid(String userUid) { 
		this.userUid = userUid;
	}
	/** 사용자UID */ 
	public String getUserUid() { 
		return userUid;
	}

	public void setTaskCd(String taskCd) { 
		this.taskCd = taskCd;
	}
	/** 코드 */ 
	public String getTaskCd() { 
		return taskCd;
	}

	public void setNote(String note) { 
		this.note = note;
	}
	/** 비고 */ 
	public String getNote() { 
		return note;
	}
	
	/** 사용자명 */ 
	public String getUserNm() {
		return userNm;
	}
	public void setUserNm(String userNm) {
		this.userNm = userNm;
	}
	
	/** 코드 목록 */ 
	public String[] getTaskCdList() {
		return taskCdList;
	}
	public void setTaskCdList(String[] taskCdList) {
		this.taskCdList = taskCdList;
	}
	
	/** 작업상세 목록 */
	public List<Map<String, String>> getDetlMapList() {
		return detlMapList;
	}
	public void setDetlMapList(List<Map<String, String>> detlMapList) {
		this.detlMapList = detlMapList;
	}
	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.dm.dao.DmTaskHDao.selectDmTaskH";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.dm.dao.DmTaskHDao.insertDmTaskH";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.dm.dao.DmTaskHDao.updateDmTaskH";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.dm.dao.DmTaskHDao.deleteDmTaskH";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.dm.dao.DmTaskHDao.countDmTaskH";
		}
		return null;
	}

	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":작업이력]");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab) { 
		if(docGrpId!=null) { if(tab!=null) builder.append(tab); builder.append("docGrpId(문서그룹ID):").append(docGrpId).append('\n'); }
		if(taskDt!=null) { if(tab!=null) builder.append(tab); builder.append("taskDt(작업일시):").append(taskDt).append('\n'); }
		if(userUid!=null) { if(tab!=null) builder.append(tab); builder.append("userUid(사용자UID):").append(userUid).append('\n'); }
		if(taskCd!=null) { if(tab!=null) builder.append(tab); builder.append("taskCd(작업코드):").append(taskCd).append('\n'); }
		if(note!=null) { if(tab!=null) builder.append(tab); builder.append("note(비고):").append(note).append('\n'); }
		super.toString(builder, tab);
	}

}
