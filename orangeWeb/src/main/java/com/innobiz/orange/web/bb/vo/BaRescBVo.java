package com.innobiz.orange.web.bb.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 리소스기본(BA_RESC_B) 테이블 VO
 */
public class BaRescBVo extends CommonVoImpl {

	private static final long serialVersionUID = 7882368700025031427L;

	/** 리소스ID - KEY */
	private String rescId;

	/** 언어구분코드 - KEY - ko:국문, en:영문 */
	private String langTypCd;

	/** 리소스값 */
	private String rescVa;

	/**
	 * 리소스ID - KEY
	 */
	public String getRescId() {
		return rescId;
	}

	/**
	 * 리소스ID - KEY
	 */
	public void setRescId(String rescId) {
		this.rescId = rescId;
	}

	/**
	 * 언어구분코드 - KEY
	 */
	public String getLangTypCd() {
		return langTypCd;
	}

	/**
	 * 언어구분코드 - KEY
	 */
	public void setLangTypCd(String langTypCd) {
		this.langTypCd = langTypCd;
	}

	/**
	 * 리소스값
	 */
	public String getRescVa() {
		return rescVa;
	}

	/**
	 * 리소스값
	 */
	public void setRescVa(String rescVa) {
		this.rescVa = rescVa;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if (getInstanceQueryId() != null) return getInstanceQueryId();
		if (QueryType.SELECT == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaRescBDao.selectBaRescB";
		} else if (QueryType.INSERT == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaRescBDao.insertBaRescB";
		} else if (QueryType.UPDATE == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaRescBDao.updateBaRescB";
		} else if (QueryType.DELETE == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaRescBDao.deleteBaRescB";
		} else if (QueryType.COUNT == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaRescBDao.countBaRescB";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString() {
		StringBuilder sb = new StringBuilder(512);
		sb.append('[').append(this.getClass().getName()).append(":리소스기본]\n");
		toString(sb, null);
		return sb.toString();
	}

	/** String으로 변환, builder에 append함 */
	public void toString(StringBuilder sb, String tab) {
		if (rescId != null) { if (tab != null) sb.append(tab); sb.append("rescId(리소스ID - KEY):").append(rescId).append('\n'); }
		if (langTypCd != null) { if (tab != null) sb.append(tab); sb.append("langTypCd(언어구분코드 - KEY - ko:국문, en:영문):").append(langTypCd).append('\n'); }
		if (rescVa != null) { if (tab != null) sb.append(tab); sb.append("rescVa(리소스값):").append(rescVa).append('\n'); }
		super.toString(sb, tab);
	}
}