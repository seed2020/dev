package com.innobiz.orange.web.wl.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/****** Object:  Vo - Date: 2017/03/07 11:10 ******/
/**
* 업무일지취합관계(WL_TASK_CONSOL_R) 테이블 VO 
*/
public class WlTaskConsolRVo extends CommonVoImpl {
	
	/** serialVersionUID */
	private static final long serialVersionUID = -1272130699683782097L;

	/** 일지번호 */ 
	private String logNo;

 	/** 목록번호 */ 
	private String lstNo;
	
	/** 정렬순서 */ 
	private Integer sortOrdr;

 	public void setLogNo(String logNo) { 
		this.logNo = logNo;
	}
	/** 일지번호 */ 
	public String getLogNo() { 
		return logNo;
	}

	public void setLstNo(String lstNo) { 
		this.lstNo = lstNo;
	}
	/** 목록번호 */ 
	public String getLstNo() { 
		return lstNo;
	}
	
	public void setSortOrdr(Integer sortOrdr) { 
		this.sortOrdr = sortOrdr;
	}
	/** 정렬순서 */ 
	public Integer getSortOrdr() { 
		return sortOrdr;
	}
	
	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.wl.dao.WlTaskConsolRDao.selectWlTaskConsolR";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.wl.dao.WlTaskConsolRDao.insertWlTaskConsolR";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.wl.dao.WlTaskConsolRDao.updateWlTaskConsolR";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.wl.dao.WlTaskConsolRDao.deleteWlTaskConsolR";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.wl.dao.WlTaskConsolRDao.countWlTaskConsolR";
		}
		return null;
	}

	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":업무일지취합관계]");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab) { 
		if(logNo!=null) { if(tab!=null) builder.append(tab); builder.append("logNo(일지번호):").append(logNo).append('\n'); }
		if(lstNo!=null) { if(tab!=null) builder.append(tab); builder.append("lstNo(목록번호):").append(lstNo).append('\n'); }
		if(sortOrdr!=null) { if(tab!=null) builder.append(tab); builder.append("sortOrdr(정렬순서):").append(sortOrdr).append('\n'); }
		super.toString(builder, tab);
	}

}
