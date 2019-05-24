package com.innobiz.orange.web.ap.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * ERP연계기본(AP_ERP_INTG_B) 테이블 VO
 */
public class ApErpIntgBVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 2749992927777118514L;

	/** 연계번호 - KEY */
	private String intgNo;

	/** 결재번호 */
	private String apvNo;

	/** 양식ID */
	private String formId;

	/** 참조ID */
	private String rid;

	/** 연계상태코드 - 신청:req, 결재중:ongo ,반려:rejt, 승인:apvd, 취소:cncl */
	private String intgStatCd;

	/** 문서제목 */
	private String docSubj;

	/** 등록일시 */
	private String regDt;

	/** 연계URL */
	private String intgUrl;

	/** 연계구분코드 */
	private String intgTypCd;

	/** 양식명 */
	private String formNm;

	/** 참조값 */
	private String refVa;

	/** 본문HTML */
	private String bodyHtml;

	/** 연계번호 - KEY */
	public String getIntgNo() {
		return intgNo;
	}

	/** 연계번호 - KEY */
	public void setIntgNo(String intgNo) {
		this.intgNo = intgNo;
	}

	/** 결재번호 */
	public String getApvNo() {
		return apvNo;
	}

	/** 결재번호 */
	public void setApvNo(String apvNo) {
		this.apvNo = apvNo;
	}

	/** 양식ID */
	public String getFormId() {
		return formId;
	}

	/** 양식ID */
	public void setFormId(String formId) {
		this.formId = formId;
	}

	/** 참조ID */
	public String getRid() {
		return rid;
	}

	/** 참조ID */
	public void setRid(String rid) {
		this.rid = rid;
	}

	/** 연계상태코드 - 신청:req, 결재중:ongo ,반려:rejt, 승인:apvd, 취소:cncl */
	public String getIntgStatCd() {
		return intgStatCd;
	}

	/** 연계상태코드 - 신청:req, 결재중:ongo ,반려:rejt, 승인:apvd, 취소:cncl */
	public void setIntgStatCd(String intgStatCd) {
		this.intgStatCd = intgStatCd;
	}

	/** 문서제목 */
	public String getDocSubj() {
		return docSubj;
	}

	/** 문서제목 */
	public void setDocSubj(String docSubj) {
		this.docSubj = docSubj;
	}

	/** 등록일시 */
	public String getRegDt() {
		return regDt;
	}

	/** 등록일시 */
	public void setRegDt(String regDt) {
		this.regDt = regDt;
	}

	/** 연계URL */
	public String getIntgUrl() {
		return intgUrl;
	}

	/** 연계URL */
	public void setIntgUrl(String intgUrl) {
		this.intgUrl = intgUrl;
	}

	/** 연계구분코드 */
	public String getIntgTypCd() {
		return intgTypCd;
	}

	/** 연계구분코드 */
	public void setIntgTypCd(String intgTypCd) {
		this.intgTypCd = intgTypCd;
	}

	/** 양식명 */
	public String getFormNm() {
		return formNm;
	}

	/** 양식명 */
	public void setFormNm(String formNm) {
		this.formNm = formNm;
	}

	/** 참조값 */
	public String getRefVa() {
		return refVa;
	}

	/** 참조값 */
	public void setRefVa(String refVa) {
		this.refVa = refVa;
	}

	/** 본문HTML */
	public String getBodyHtml() {
		return bodyHtml;
	}

	/** 본문HTML */
	public void setBodyHtml(String bodyHtml) {
		this.bodyHtml = bodyHtml;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApErpIntgBDao.selectApErpIntgB";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApErpIntgBDao.insertApErpIntgB";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.ap.dao.ApErpIntgBDao.updateApErpIntgB";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.ap.dao.ApErpIntgBDao.deleteApErpIntgB";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApErpIntgBDao.countApErpIntgB";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":ERP연계기본]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(intgNo!=null) { if(tab!=null) builder.append(tab); builder.append("intgNo(연계번호-PK):").append(intgNo).append('\n'); }
		if(apvNo!=null) { if(tab!=null) builder.append(tab); builder.append("apvNo(결재번호):").append(apvNo).append('\n'); }
		if(formId!=null) { if(tab!=null) builder.append(tab); builder.append("formId(양식ID):").append(formId).append('\n'); }
		if(rid!=null) { if(tab!=null) builder.append(tab); builder.append("rid(참조ID):").append(rid).append('\n'); }
		if(intgStatCd!=null) { if(tab!=null) builder.append(tab); builder.append("intgStatCd(연계상태코드):").append(intgStatCd).append('\n'); }
		if(docSubj!=null) { if(tab!=null) builder.append(tab); builder.append("docSubj(문서제목):").append(docSubj).append('\n'); }
		if(regDt!=null) { if(tab!=null) builder.append(tab); builder.append("regDt(등록일시):").append(regDt).append('\n'); }
		if(intgUrl!=null) { if(tab!=null) builder.append(tab); builder.append("intgUrl(연계URL):").append(intgUrl).append('\n'); }
		if(intgTypCd!=null) { if(tab!=null) builder.append(tab); builder.append("intgTypCd(연계구분코드):").append(intgTypCd).append('\n'); }
		if(formNm!=null) { if(tab!=null) builder.append(tab); builder.append("formNm(양식명):").append(formNm).append('\n'); }
		if(refVa!=null) { if(tab!=null) builder.append(tab); builder.append("refVa(참조값):").append(refVa).append('\n'); }
		if(bodyHtml!=null) { if(tab!=null) builder.append(tab); builder.append("bodyHtml(본문HTML):").append(bodyHtml).append('\n'); }
		super.toString(builder, tab);
	}
}
