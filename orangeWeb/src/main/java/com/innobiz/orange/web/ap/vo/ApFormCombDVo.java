package com.innobiz.orange.web.ap.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 양식구성상세(AP_FORM_COMB_D) 테이블 VO
 */
public class ApFormCombDVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 7777907397357481531L;

	/** 양식ID - KEY */
	private String formId;

	/** 양식구성ID - KEY - header:머리말, apvLine:결재라인, bodyHtml:양식본문, dispItems:항목지정, sender:발신명의, footer:바닥글, printer:인쇄설정 */
	private String formCombId;

	/** 양식구성일련번호 - KEY */
	private String formCombSeq;

	/** 정렬순서 */
	private String sortOrdr;

	/** 양식ID - KEY */
	public String getFormId() {
		return formId;
	}

	/** 양식ID - KEY */
	public void setFormId(String formId) {
		this.formId = formId;
	}

	/** 양식구성ID - KEY - header:머리말, apvLine:결재라인, bodyHtml:양식본문, dispItems:항목지정, sender:발신명의, footer:바닥글, printer:인쇄설정 */
	public String getFormCombId() {
		return formCombId;
	}

	/** 양식구성ID - KEY - header:머리말, apvLine:결재라인, bodyHtml:양식본문, dispItems:항목지정, sender:발신명의, footer:바닥글, printer:인쇄설정 */
	public void setFormCombId(String formCombId) {
		this.formCombId = formCombId;
	}

	/** 양식구성일련번호 - KEY */
	public String getFormCombSeq() {
		return formCombSeq;
	}

	/** 양식구성일련번호 - KEY */
	public void setFormCombSeq(String formCombSeq) {
		this.formCombSeq = formCombSeq;
	}

	/** 정렬순서 */
	public String getSortOrdr() {
		return sortOrdr;
	}

	/** 정렬순서 */
	public void setSortOrdr(String sortOrdr) {
		this.sortOrdr = sortOrdr;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApFormCombDDao.selectApFormCombD";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApFormCombDDao.insertApFormCombD";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.ap.dao.ApFormCombDDao.updateApFormCombD";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.ap.dao.ApFormCombDDao.deleteApFormCombD";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApFormCombDDao.countApFormCombD";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":양식구성상세]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(formId!=null) { if(tab!=null) builder.append(tab); builder.append("formId(양식ID-PK):").append(formId).append('\n'); }
		if(formCombId!=null) { if(tab!=null) builder.append(tab); builder.append("formCombId(양식구성ID-PK):").append(formCombId).append('\n'); }
		if(formCombSeq!=null) { if(tab!=null) builder.append(tab); builder.append("formCombSeq(양식구성일련번호-PK):").append(formCombSeq).append('\n'); }
		if(sortOrdr!=null) { if(tab!=null) builder.append(tab); builder.append("sortOrdr(정렬순서):").append(sortOrdr).append('\n'); }
		super.toString(builder, tab);
	}
}
