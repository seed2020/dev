package com.innobiz.orange.web.wl.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/****** Object:  Vo - Date: 2017/03/07 11:10 ******/
/**
* 업무일지관계(WL_TASK_LOG_R) 테이블 VO 
*/
public class WlTaskLogRVo extends CommonVoImpl {
	/** serialVersionUID */
	private static final long serialVersionUID = -3068520221475010848L;

	/** 일지번호 */ 
	private String logNo;

 	/** 등록자UID */ 
	private String regrUid;

 	public void setLogNo(String logNo) { 
		this.logNo = logNo;
	}
	/** 일지번호 */ 
	public String getLogNo() { 
		return logNo;
	}

	public void setRegrUid(String regrUid) { 
		this.regrUid = regrUid;
	}
	/** 등록자UID */ 
	public String getRegrUid() { 
		return regrUid;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.wl.dao.WlTaskLogRDao.selectWlTaskLogR";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.wl.dao.WlTaskLogRDao.insertWlTaskLogR";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.wl.dao.WlTaskLogRDao.updateWlTaskLogR";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.wl.dao.WlTaskLogRDao.deleteWlTaskLogR";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.wl.dao.WlTaskLogRDao.countWlTaskLogR";
		}
		return null;
	}

	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":업무일지관계]");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab) { 
		if(logNo!=null) { if(tab!=null) builder.append(tab); builder.append("logNo(일지번호):").append(logNo).append('\n'); }
		if(regrUid!=null) { if(tab!=null) builder.append(tab); builder.append("regrUid(등록자UID):").append(regrUid).append('\n'); }
		super.toString(builder, tab);
	}

}
