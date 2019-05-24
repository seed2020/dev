package com.innobiz.orange.web.pt.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 리소스기본(PT_RESC_B) 테이블 VO
 */
public class PtRescBVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 1629566159276146178L;

	/** 리소스ID - KEY */
	private String rescId;

	/** 언어구분코드 - KEY - ko:한글, en:영문, ja:일문, zh:중문 */
	private String langTypCd;

	/** 리소스값 */
	private String rescVa;


	// 추가컬럼
	/** 메뉴부모ID - 메뉴목록 리소스 일괄 조회용 */
	private String mnuPid;

	/** 분류코드 - 코드 리소스 삭제용 */
	private String clsCd;

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

	/** 메뉴부모ID - 메뉴목록 리소스 일괄 조회용 */
	public String getMnuPid() {
		return mnuPid;
	}

	/** 메뉴부모ID - 메뉴목록 리소스 일괄 조회용 */
	public void setMnuPid(String mnuPid) {
		this.mnuPid = mnuPid;
	}

	/** 분류코드 - 코드 리소스 삭제용 */
	public String getClsCd() {
		return clsCd;
	}

	/** 분류코드 - 코드 리소스 삭제용 */
	public void setClsCd(String clsCd) {
		this.clsCd = clsCd;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.pt.dao.PtRescBDao.selectPtRescB";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.pt.dao.PtRescBDao.insertPtRescB";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.pt.dao.PtRescBDao.updatePtRescB";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.pt.dao.PtRescBDao.deletePtRescB";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.pt.dao.PtRescBDao.countPtRescB";
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
		if(rescId!=null) { if(tab!=null) builder.append(tab); builder.append("rescId(리소스ID-PK):").append(rescId).append('\n'); }
		if(langTypCd!=null) { if(tab!=null) builder.append(tab); builder.append("langTypCd(언어구분코드-PK):").append(langTypCd).append('\n'); }
		if(rescVa!=null) { if(tab!=null) builder.append(tab); builder.append("rescVa(리소스값):").append(rescVa).append('\n'); }
		if(mnuPid!=null) { if(tab!=null) builder.append(tab); builder.append("mnuPid(메뉴부모ID - 메뉴목록 리소스 일괄 조회용):").append(mnuPid).append('\n'); }
		if(clsCd!=null) { if(tab!=null) builder.append(tab); builder.append("clsCd(분류코드 - 코드 리소스 삭제용):").append(clsCd).append('\n'); }
		super.toString(builder, tab);
	}
}
