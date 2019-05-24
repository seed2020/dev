package com.innobiz.orange.web.or.vo;

import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 사용자로그인이력(OR_USER_LGIN_H) 테이블 VO
 */
@SuppressWarnings("serial")
public class OrUserLginHVo extends OrUserBVo {
	/** 사용자UID */ 
	private String userUid;

 	/** 세션ID */ 
	private String sessionId;
	
	/** 회사ID */ 
	private String compId;
	
 	/** 로그인일시 */ 
	private String lginDt;

 	/** 로그아웃일시 */ 
	private String lgotDt;

 	/** 접속IP */ 
	private String accsIp;
	
	/** 회사ID */
	private String compNm;
	
	/** 장치명 */
	private String deviNm;
	
/** 검색 조건 */
	
	/** 사용자ID*/
	private String schUserUid;
	
 	public void setUserUid(String userUid) { 
		this.userUid = userUid;
	}
	/** 사용자UID */ 
	public String getUserUid() { 
		return userUid;
	}

	public void setSessionId(String sessionId) { 
		this.sessionId = sessionId;
	}
	/** 세션ID */ 
	public String getSessionId() { 
		return sessionId;
	}

	public String getCompId() {
		return compId;
	}
	public void setCompId(String compId) {
		this.compId = compId;
	}
	public void setLginDt(String lginDt) { 
		this.lginDt = lginDt;
	}
	/** 로그인일시 */ 
	public String getLginDt() { 
		return lginDt;
	}

	public void setLgotDt(String lgotDt) { 
		this.lgotDt = lgotDt;
	}
	/** 로그아웃일시 */ 
	public String getLgotDt() { 
		return lgotDt;
	}

	public void setAccsIp(String accsIp) { 
		this.accsIp = accsIp;
	}
	/** 접속IP */ 
	public String getAccsIp() { 
		return accsIp;
	}
	
	public String getCompNm() {
		return compNm;
	}
	public void setCompNm(String compNm) {
		this.compNm = compNm;
	}
	
	public String getDeviNm() {
		return deviNm;
	}
	public void setDeviNm(String deviNm) {
		this.deviNm = deviNm;
	}
	
	
	public String getSchUserUid() {
		return schUserUid;
	}
	public void setSchUserUid(String schUserUid) {
		this.schUserUid = schUserUid;
	}
	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.or.dao.OrUserLginHDao.selectOrUserLginH";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.or.dao.OrUserLginHDao.insertOrUserLginH";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.or.dao.OrUserLginHDao.updateOrUserLginH";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.or.dao.OrUserLginHDao.deleteOrUserLginH";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.or.dao.OrUserLginHDao.countOrUserLginH";
		}
		return null;
	}
	
	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":사용자로그인이력]");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab) { 
		if(userUid!=null) { if(tab!=null) builder.append(tab); builder.append("userUid(사용자UID):").append(userUid).append('\n'); }
		if(sessionId!=null) { if(tab!=null) builder.append(tab); builder.append("sessionId(세션ID):").append(sessionId).append('\n'); }
		if(compId!=null) { if(tab!=null) builder.append(tab); builder.append("compId(회사ID):").append(compId).append('\n'); }
		if(lginDt!=null) { if(tab!=null) builder.append(tab); builder.append("lginDt(로그인일시):").append(lginDt).append('\n'); }
		if(lgotDt!=null) { if(tab!=null) builder.append(tab); builder.append("lgotDt(로그아웃일시):").append(lgotDt).append('\n'); }
		if(accsIp!=null) { if(tab!=null) builder.append(tab); builder.append("accsIp(접속IP):").append(accsIp).append('\n'); }
		super.toString(builder, tab);
	}

}
