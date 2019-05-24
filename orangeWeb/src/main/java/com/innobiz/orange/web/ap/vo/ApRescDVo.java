package com.innobiz.orange.web.ap.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 리소스상세(AP_RESC_D) 테이블 VO
 */
public class ApRescDVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 8709989427956091672L;

	/** 회사ID - KEY */
	private String compId;

	/** 리소스ID - KEY */
	private String rescId;

	/** 언어구분코드 - KEY - ko:한글, en:영문, ja:일문, zh:중문 */
	private String langTypCd;

	/** 리소스값 */
	private String rescVa;

	/** 회사ID - KEY */
	public String getCompId() {
		return compId;
	}

	/** 회사ID - KEY */
	public void setCompId(String compId) {
		this.compId = compId;
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

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApRescDDao.selectApRescD";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApRescDDao.insertApRescD";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.ap.dao.ApRescDDao.updateApRescD";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.ap.dao.ApRescDDao.deleteApRescD";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApRescDDao.countApRescD";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":리소스상세]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(compId!=null) { if(tab!=null) builder.append(tab); builder.append("compId(회사ID-PK):").append(compId).append('\n'); }
		if(rescId!=null) { if(tab!=null) builder.append(tab); builder.append("rescId(리소스ID-PK):").append(rescId).append('\n'); }
		if(langTypCd!=null) { if(tab!=null) builder.append(tab); builder.append("langTypCd(언어구분코드-PK):").append(langTypCd).append('\n'); }
		if(rescVa!=null) { if(tab!=null) builder.append(tab); builder.append("rescVa(리소스값):").append(rescVa).append('\n'); }
		super.toString(builder, tab);
	}
}
