package com.innobiz.orange.web.wl.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/****** Object:  Vo - Date: 2017/02/28 15:05 ******/
/**
* 업무일지사용자목록(WL_TASK_LOG_USER_L) 테이블 VO 
*/
public class WlTaskLogUserLVo extends CommonVoImpl {
	/** serialVersionUID */
	private static final long serialVersionUID = 946706267601982793L;

	/** 일지번호 */ 
	private String logNo;

 	/** 사용자UID */ 
	private String userUid;

 	/** 열람여부 */ 
	private String readYn;
	
	/** 정렬순서 */ 
	private Integer sortOrdr;

 	/** 등록자UID */ 
	private String regrUid;

 	/** 등록일시 */ 
	private String regDt;
	
	/** 사용자명 */ 
	private String userNm;

 	public void setLogNo(String logNo) { 
		this.logNo = logNo;
	}
	/** 일지번호 */ 
	public String getLogNo() { 
		return logNo;
	}

	public void setUserUid(String userUid) { 
		this.userUid = userUid;
	}
	/** 사용자UID */ 
	public String getUserUid() { 
		return userUid;
	}

	public void setReadYn(String readYn) { 
		this.readYn = readYn;
	}
	/** 열람여부 */ 
	public String getReadYn() { 
		return readYn;
	}
	
	public void setSortOrdr(Integer sortOrdr) { 
		this.sortOrdr = sortOrdr;
	}
	/** 정렬순서 */ 
	public Integer getSortOrdr() { 
		return sortOrdr;
	}
	
	public void setRegrUid(String regrUid) { 
		this.regrUid = regrUid;
	}
	/** 등록자UID */ 
	public String getRegrUid() { 
		return regrUid;
	}

	public void setRegDt(String regDt) { 
		this.regDt = regDt;
	}
	/** 등록일시 */ 
	public String getRegDt() { 
		return regDt;
	}
	
	/** 사용자명 */ 
	public String getUserNm() {
		return userNm;
	}
	public void setUserNm(String userNm) {
		this.userNm = userNm;
	}
	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.wl.dao.WlTaskLogUserLDao.selectWlTaskLogUserL";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.wl.dao.WlTaskLogUserLDao.insertWlTaskLogUserL";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.wl.dao.WlTaskLogUserLDao.updateWlTaskLogUserL";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.wl.dao.WlTaskLogUserLDao.deleteWlTaskLogUserL";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.wl.dao.WlTaskLogUserLDao.countWlTaskLogUserL";
		}
		return null;
	}

	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":업무일지사용자목록]");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab) { 
		if(logNo!=null) { if(tab!=null) builder.append(tab); builder.append("logNo(일지번호):").append(logNo).append('\n'); }
		if(userUid!=null) { if(tab!=null) builder.append(tab); builder.append("userUid(사용자UID):").append(userUid).append('\n'); }
		if(readYn!=null) { if(tab!=null) builder.append(tab); builder.append("readYn(열람여부):").append(readYn).append('\n'); }
		if(sortOrdr!=null) { if(tab!=null) builder.append(tab); builder.append("sortOrdr(정렬순서):").append(sortOrdr).append('\n'); }
		if(regrUid!=null) { if(tab!=null) builder.append(tab); builder.append("regrUid(등록자UID):").append(regrUid).append('\n'); }
		if(regDt!=null) { if(tab!=null) builder.append(tab); builder.append("regDt(등록일시):").append(regDt).append('\n'); }
		super.toString(builder, tab);
	}

}
