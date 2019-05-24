package com.innobiz.orange.web.ct.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 리소스기본(CT_RESC_B) 테이블 VO
 */
public class CtRescBVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 1629566159276146178L;

	/** 리소스ID - KEY */
	private String rescId;

	/** 언어구분코드 - KEY - ko:한글, en:영문, ja:일문, zh:중문 */
	private String langTypCd;

	/** 리소스값 */
	private String rescVa;


	// 추가컬럼
//	/** 메뉴부모ID - 메뉴목록 리소스 일괄 조회용 */
//	private String mnuPid;
	
	/** 커뮤니티 명 리소스명과 중복 체크 여부 */
	private String ctSubjNmChkYn;
	
	public String getCtSubjNmChkYn() {
		return ctSubjNmChkYn;
	}

	public void setCtSubjNmChkYn(String ctSubjNmChkYn) {
		this.ctSubjNmChkYn = ctSubjNmChkYn;
	}

	/** 리소스ID - KEY */
	public String getRescId() {
		return rescId;
	}

	/** 리소스ID - KEY */
	public void setRescId(String rescId) {
		this.rescId = rescId;
	}

	/** 언어구분코드 - KEY - ko:한글, en:영문, ja:일문, zh:중문 */
	public String getLangTypCd() {
		return langTypCd;
	}

	/** 언어구분코드 - KEY - ko:한글, en:영문, ja:일문, zh:중문 */
	public void setLangTypCd(String langTypCd) {
		this.langTypCd = langTypCd;
	}

	/** 리소스값 */
	public String getRescVa() {
		return rescVa;
	}

	/** 리소스값 */
	public void setRescVa(String rescVa) {
		this.rescVa = rescVa;
	}
//
//	/** 메뉴부모ID - 메뉴목록 리소스 일괄 조회용 */
//	public String getMnuPid() {
//		return mnuPid;
//	}
//
//	/** 메뉴부모ID - 메뉴목록 리소스 일괄 조회용 */
//	public void setMnuPid(String mnuPid) {
//		this.mnuPid = mnuPid;
//	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.ct.dao.CtRescBDao.selectCtRescB";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.ct.dao.CtRescBDao.insertCtRescB";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.ct.dao.CtRescBDao.updateCtRescB";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.ct.dao.CtRescBDao.deleteCtRescB";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.ct.dao.CtRescBDao.countCtRescB";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":리소스기본]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(rescId!=null) { if(tab!=null) builder.append(tab); builder.append("rescId(리소스ID):").append(rescId).append('\n'); }
		if(langTypCd!=null) { if(tab!=null) builder.append(tab); builder.append("langTypCd(언어구분코드):").append(langTypCd).append('\n'); }
		if(rescVa!=null) { if(tab!=null) builder.append(tab); builder.append("rescVa(리소스값):").append(rescVa).append('\n'); }
		if(ctSubjNmChkYn!=null) { if(tab!=null) builder.append(tab); builder.append("ctSubjNmChkYn(커뮤니티 명 리소스명과 중복 체크 여부):").append(ctSubjNmChkYn).append('\n'); }
		super.toString(builder, tab);
	}
}