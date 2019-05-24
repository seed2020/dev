package com.innobiz.orange.web.wb.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 명함미팅참석자관계(WB_BC_METNG_ATND_R) 테이블 VO
 */
@SuppressWarnings("serial")
public class WbBcMetngAtndRVo extends CommonVoImpl {
	/** 회사ID */ 
	private String compId;

 	/** 등록자UID */ 
	private String regrUid;

 	/** 명함미팅상세ID */ 
	private String bcMetngDetlId;

 	/** 명함미팅참석자상세ID */ 
	private String bcMetngAtndDetlId;

 	/** 임직원구분코드 */ 
	private String emplTypCd;

 	/** 임직원성명 */ 
	private String emplNm;

 	/** 임직원전화번호 */ 
	private String emplPhon;
	
	/** 임직원ID */ 
	private String emplId;
	
	/** 회사명 */ 
	private String compNm;
	
	/** 회사전화번호 */ 
	private String compPhon;
	
	/** 이메일 */ 
	private String email;

	
/** 추가 */
	/** 삭제 목록 */
	private String[] deleteAtndIds;
	
 	public void setCompId(String compId) { 
		this.compId = compId;
	}
	/** 회사ID */ 
	public String getCompId() { 
		return compId;
	}

	public void setRegrUid(String regrUid) { 
		this.regrUid = regrUid;
	}
	/** 등록자UID */ 
	public String getRegrUid() { 
		return regrUid;
	}

	public void setBcMetngDetlId(String bcMetngDetlId) { 
		this.bcMetngDetlId = bcMetngDetlId;
	}
	/** 명함미팅상세ID */ 
	public String getBcMetngDetlId() { 
		return bcMetngDetlId;
	}

	public void setBcMetngAtndDetlId(String bcMetngAtndDetlId) { 
		this.bcMetngAtndDetlId = bcMetngAtndDetlId;
	}
	/** 명함미팅참석자상세ID */ 
	public String getBcMetngAtndDetlId() { 
		return bcMetngAtndDetlId;
	}

	public void setEmplTypCd(String emplTypCd) { 
		this.emplTypCd = emplTypCd;
	}
	/** 임직원구분코드 */ 
	public String getEmplTypCd() { 
		return emplTypCd;
	}

	public void setEmplNm(String emplNm) { 
		this.emplNm = emplNm;
	}
	/** 임직원성명 */ 
	public String getEmplNm() { 
		return emplNm;
	}

	public void setEmplPhon(String emplPhon) { 
		this.emplPhon = emplPhon;
	}
	/** 임직원전화번호 */ 
	public String getEmplPhon() { 
		return emplPhon;
	}
	
	public String[] getDeleteAtndIds() {
		return deleteAtndIds;
	}
	public void setDeleteAtndIds(String[] deleteAtndIds) {
		this.deleteAtndIds = deleteAtndIds;
	}
	
	public String getEmplId() {
		return emplId;
	}
	public void setEmplId(String emplId) {
		this.emplId = emplId;
	}
	
	
	public String getCompNm() {
		return compNm;
	}
	public void setCompNm(String compNm) {
		this.compNm = compNm;
	}
	public String getCompPhon() {
		return compPhon;
	}
	public void setCompPhon(String compPhon) {
		this.compPhon = compPhon;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.wb.dao.WbBcMetngAtndRDao.selectWbBcMetngAtndR";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.wb.dao.WbBcMetngAtndRDao.insertWbBcMetngAtndR";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.wb.dao.WbBcMetngAtndRDao.updateWbBcMetngAtndR";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.wb.dao.WbBcMetngAtndRDao.deleteWbBcMetngAtndR";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.wb.dao.WbBcMetngAtndRDao.countWbBcMetngAtndR";
		}
		return null;
	}
	
	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":명함미팅참석자관계]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab){
		if(compId!=null) { if(tab!=null) builder.append(tab); builder.append("compId(회사ID):").append(compId).append('\n'); }
		if(regrUid!=null) { if(tab!=null) builder.append(tab); builder.append("regrUid(등록자UID):").append(regrUid).append('\n'); }
		if(bcMetngDetlId!=null) { if(tab!=null) builder.append(tab); builder.append("bcMetngDetlId(명함미팅상세ID):").append(bcMetngDetlId).append('\n'); }
		if(bcMetngAtndDetlId!=null) { if(tab!=null) builder.append(tab); builder.append("bcMetngAtndDetlId(명함미팅참석자상세ID):").append(bcMetngAtndDetlId).append('\n'); }
		if(emplTypCd!=null) { if(tab!=null) builder.append(tab); builder.append("emplTypCd(임직원구분코드):").append(emplTypCd).append('\n'); }
		if(emplNm!=null) { if(tab!=null) builder.append(tab); builder.append("emplNm(임직원성명):").append(emplNm).append('\n'); }
		if(emplPhon!=null) { if(tab!=null) builder.append(tab); builder.append("emplPhon(임직원전화번호):").append(emplPhon).append('\n'); }
		if(emplId!=null) { if(tab!=null) builder.append(tab); builder.append("emplId(임직원ID):").append(emplId).append('\n'); }
		if(compNm!=null) { if(tab!=null) builder.append(tab); builder.append("compNm(회사명):").append(compNm).append('\n'); }
		if(compPhon!=null) { if(tab!=null) builder.append(tab); builder.append("compPhon(회사전화번호):").append(compPhon).append('\n'); }
		if(email!=null) { if(tab!=null) builder.append(tab); builder.append("email(이메일):").append(email).append('\n'); }
		if(deleteAtndIds!=null) { if(tab!=null) builder.append(tab); builder.append("deleteAtndIds(삭제 목록):"); appendArrayTo(builder, deleteAtndIds); builder.append('\n'); }
		super.toString(builder, tab);
	}
	
}