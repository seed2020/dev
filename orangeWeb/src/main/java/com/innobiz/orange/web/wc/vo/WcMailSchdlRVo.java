package com.innobiz.orange.web.wc.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/****** Object:  Vo - Date: 2016/05/20 11:41 ******/
/**
* 메일일정(WC_MAIL_SCHDL_R) 테이블 VO 
*/
@SuppressWarnings("serial")
public class WcMailSchdlRVo extends CommonVoImpl {	
	/** 일정ID */ 
	private String schdlId;

 	/** 사용자UID */ 
	private String userUid;

 	/** 수락여부 */ 
	private String acptYn;
	
	/** 등록자UID */ 
	private String regrUid;
	
 	/** 등록일시 */ 
	private String regDt;

 	/** 완료일시 */ 
	private String cmplDt;
	
	/** 메일ID */ 
	private String mailId;
	
	/** 복사 일정ID */ 
	private String copySchdlId;
	
	/** 추가 */
	/** 등록자명 */ 
	private String regrNm;

 	public void setSchdlId(String schdlId) { 
		this.schdlId = schdlId;
	}
	/** 일정ID */ 
	public String getSchdlId() { 
		return schdlId;
	}

	public void setUserUid(String userUid) { 
		this.userUid = userUid;
	}
	/** 사용자UID */ 
	public String getUserUid() { 
		return userUid;
	}

	public void setAcptYn(String acptYn) { 
		this.acptYn = acptYn;
	}
	/** 수락여부 */ 
	public String getAcptYn() { 
		return acptYn;
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

	public void setCmplDt(String cmplDt) { 
		this.cmplDt = cmplDt;
	}
	/** 완료일시 */ 
	public String getCmplDt() { 
		return cmplDt;
	}	
	
	/** 메일ID */ 
	public String getMailId() {
		return mailId;
	}
	public void setMailId(String mailId) {
		this.mailId = mailId;
	}
	
	/** 복사 일정ID */ 
	public String getCopySchdlId() {
		return copySchdlId;
	}
	public void setCopySchdlId(String copySchdlId) {
		this.copySchdlId = copySchdlId;
	}
	
	/** 등록자명 */ 
	public String getRegrNm() {
		return regrNm;
	}
	public void setRegrNm(String regrNm) {
		this.regrNm = regrNm;
	}
	
	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.wc.dao.WcMailSchdlRDao.selectWcMailSchdlR";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.wc.dao.WcMailSchdlRDao.insertWcMailSchdlR";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.wc.dao.WcMailSchdlRDao.updateWcMailSchdlR";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.wc.dao.WcMailSchdlRDao.deleteWcMailSchdlR";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.wc.dao.WcMailSchdlRDao.countWcMailSchdlR";
		}
		return null;
	}

	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":메일일정]");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab) { 
		if(schdlId!=null) { if(tab!=null) builder.append(tab); builder.append("schdlId(일정ID):").append(schdlId).append('\n'); }
		if(userUid!=null) { if(tab!=null) builder.append(tab); builder.append("userUid(사용자UID):").append(userUid).append('\n'); }
		if(acptYn!=null) { if(tab!=null) builder.append(tab); builder.append("acptYn(수락여부):").append(acptYn).append('\n'); }
		if(regrUid!=null) { if(tab!=null) builder.append(tab); builder.append("regrUid(등록자UID):").append(regrUid).append('\n'); }
		if(regDt!=null) { if(tab!=null) builder.append(tab); builder.append("regDt(등록일시):").append(regDt).append('\n'); }
		if(cmplDt!=null) { if(tab!=null) builder.append(tab); builder.append("cmplDt(완료일시):").append(cmplDt).append('\n'); }
		if(mailId!=null) { if(tab!=null) builder.append(tab); builder.append("mailId(메일ID):").append(mailId).append('\n'); }
		if(copySchdlId!=null) { if(tab!=null) builder.append(tab); builder.append("copySchdlId(복사일정ID):").append(copySchdlId).append('\n'); }
		super.toString(builder, tab);
	}

}
