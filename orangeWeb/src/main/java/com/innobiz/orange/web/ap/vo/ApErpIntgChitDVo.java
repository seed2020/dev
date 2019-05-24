package com.innobiz.orange.web.ap.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * ERP연계전표상세(AP_ERP_INTG_CHIT_D) 테이블 VO
 */
public class ApErpIntgChitDVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 3520298561461384384L;

	/** 연계번호 - KEY */
	private String intgNo;

	/** 전표양식번호 */
	private String chitFormId;

	/** 양식명 */
	private String formNm;

	/** 전표본문HTML */
	private String chitBodyHtml;

	/** 연계번호 - KEY */
	public String getIntgNo() {
		return intgNo;
	}

	/** 연계번호 - KEY */
	public void setIntgNo(String intgNo) {
		this.intgNo = intgNo;
	}

	/** 전표양식번호 */
	public String getChitFormId() {
		return chitFormId;
	}

	/** 전표양식번호 */
	public void setChitFormId(String chitFormId) {
		this.chitFormId = chitFormId;
	}

	/** 양식명 */
	public String getFormNm() {
		return formNm;
	}

	/** 양식명 */
	public void setFormNm(String formNm) {
		this.formNm = formNm;
	}

	/** 전표본문HTML */
	public String getChitBodyHtml() {
		return chitBodyHtml;
	}

	/** 전표본문HTML */
	public void setChitBodyHtml(String chitBodyHtml) {
		this.chitBodyHtml = chitBodyHtml;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApErpIntgChitDDao.selectApErpIntgChitD";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApErpIntgChitDDao.insertApErpIntgChitD";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.ap.dao.ApErpIntgChitDDao.updateApErpIntgChitD";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.ap.dao.ApErpIntgChitDDao.deleteApErpIntgChitD";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApErpIntgChitDDao.countApErpIntgChitD";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":ERP연계전표상세]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(intgNo!=null) { if(tab!=null) builder.append(tab); builder.append("intgNo(연계번호-PK):").append(intgNo).append('\n'); }
		if(chitFormId!=null) { if(tab!=null) builder.append(tab); builder.append("chitFormId(전표양식번호):").append(chitFormId).append('\n'); }
		if(formNm!=null) { if(tab!=null) builder.append(tab); builder.append("formNm(양식명):").append(formNm).append('\n'); }
		if(chitBodyHtml!=null) { if(tab!=null) builder.append(tab); builder.append("chitBodyHtml(전표본문HTML):").append(chitBodyHtml).append('\n'); }
		super.toString(builder, tab);
	}
}
