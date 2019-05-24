package com.innobiz.orange.web.ap.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * ERP양식기본(AP_ERP_FORM_B) 테이블 VO
 */
public class ApErpFormBVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 842520023827690439L;

	/** ERP양식ID - KEY */
	private String erpFormId;

	/** 회사ID */
	private String compId;

	/** ERP양식구분코드 - xmlFromAp:결재시작XML, xmlEditFromAp:결재시작양식변경XML, xmlFromErp:ERP시작XML, wfForm:업무관리 양식 */
	private String erpFormTypCd;

	/** 리소스ID */
	private String rescId;

	/** 등록URL */
	private String regUrl;

	/** 통보URL */
	private String infmUrl;


	// 추가컬럼
	/** 리소스명 */
	private String rescNm;

	/** ERP양식ID - KEY */
	public String getErpFormId() {
		return erpFormId;
	}

	/** ERP양식ID - KEY */
	public void setErpFormId(String erpFormId) {
		this.erpFormId = erpFormId;
	}

	/** 회사ID */
	public String getCompId() {
		return compId;
	}

	/** 회사ID */
	public void setCompId(String compId) {
		this.compId = compId;
	}

	/** ERP양식구분코드 - xmlFromAp:결재시작XML, xmlEditFromAp:결재시작양식변경XML, xmlFromErp:ERP시작XML, wfForm:업무관리 양식 */
	public String getErpFormTypCd() {
		return erpFormTypCd;
	}

	/** ERP양식구분코드 - xmlFromAp:결재시작XML, xmlEditFromAp:결재시작양식변경XML, xmlFromErp:ERP시작XML, wfForm:업무관리 양식 */
	public void setErpFormTypCd(String erpFormTypCd) {
		this.erpFormTypCd = erpFormTypCd;
	}

	/** 리소스ID */
	public String getRescId() {
		return rescId;
	}

	/** 리소스ID */
	public void setRescId(String rescId) {
		this.rescId = rescId;
	}

	/** 등록URL */
	public String getRegUrl() {
		return regUrl;
	}

	/** 등록URL */
	public void setRegUrl(String regUrl) {
		this.regUrl = regUrl;
	}

	/** 통보URL */
	public String getInfmUrl() {
		return infmUrl;
	}

	/** 통보URL */
	public void setInfmUrl(String infmUrl) {
		this.infmUrl = infmUrl;
	}

	/** 리소스명 */
	public String getRescNm() {
		return rescNm;
	}

	/** 리소스명 */
	public void setRescNm(String rescNm) {
		this.rescNm = rescNm;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApErpFormBDao.selectApErpFormB";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApErpFormBDao.insertApErpFormB";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.ap.dao.ApErpFormBDao.updateApErpFormB";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.ap.dao.ApErpFormBDao.deleteApErpFormB";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApErpFormBDao.countApErpFormB";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":ERP양식기본]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(erpFormId!=null) { if(tab!=null) builder.append(tab); builder.append("erpFormId(ERP양식ID-PK):").append(erpFormId).append('\n'); }
		if(compId!=null) { if(tab!=null) builder.append(tab); builder.append("compId(회사ID):").append(compId).append('\n'); }
		if(erpFormTypCd!=null) { if(tab!=null) builder.append(tab); builder.append("erpFormTypCd(ERP양식구분코드):").append(erpFormTypCd).append('\n'); }
		if(rescId!=null) { if(tab!=null) builder.append(tab); builder.append("rescId(리소스ID):").append(rescId).append('\n'); }
		if(regUrl!=null) { if(tab!=null) builder.append(tab); builder.append("regUrl(등록URL):").append(regUrl).append('\n'); }
		if(infmUrl!=null) { if(tab!=null) builder.append(tab); builder.append("infmUrl(통보URL):").append(infmUrl).append('\n'); }
		if(rescNm!=null) { if(tab!=null) builder.append(tab); builder.append("rescNm(리소스명):").append(rescNm).append('\n'); }
		super.toString(builder, tab);
	}
}
