package com.innobiz.orange.web.ap.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 리소스기본(AP_RESC_B) 테이블 VO
 */
public class ApRescBVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 7837541144695051413L;

	/** 리소스ID - KEY */
	private String rescId;

	/** 언어구분코드 - KEY - ko:한글, en:영문, ja:일문, zh:중문 */
	private String langTypCd;

	/** 리소스값 */
	private String rescVa;


	// 추가컬럼
	/** 리소스명 */
	private String rescNm;

	/** 언어구분명 */
	private String langTypNm;

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

	/** 리소스명 */
	public String getRescNm() {
		return rescNm;
	}

	/** 리소스명 */
	public void setRescNm(String rescNm) {
		this.rescNm = rescNm;
	}

	/** 언어구분명 */
	public String getLangTypNm() {
		return langTypNm;
	}

	/** 언어구분명 */
	public void setLangTypNm(String langTypNm) {
		this.langTypNm = langTypNm;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApRescBDao.selectApRescB";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApRescBDao.insertApRescB";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.ap.dao.ApRescBDao.updateApRescB";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.ap.dao.ApRescBDao.deleteApRescB";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApRescBDao.countApRescB";
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
		if(rescNm!=null) { if(tab!=null) builder.append(tab); builder.append("rescNm(리소스명):").append(rescNm).append('\n'); }
		if(langTypNm!=null) { if(tab!=null) builder.append(tab); builder.append("langTypNm(언어구분명):").append(langTypNm).append('\n'); }
		super.toString(builder, tab);
	}
}
