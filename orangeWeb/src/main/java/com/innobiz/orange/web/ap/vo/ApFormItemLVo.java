package com.innobiz.orange.web.ap.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 양식항목내역(AP_FORM_ITEM_L) 테이블 VO
 */
public class ApFormItemLVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 4538138391632503068L;

	/** 양식ID - KEY */
	private String formId;

	/** 양식구성일련번호 - KEY */
	private String formCombSeq;

	/** 줄번호 - KEY */
	private String rowNo;

	/** 열번호 - KEY */
	private String colNo;

	/** 콜스판값 */
	private String cspnVa;

	/** 항목ID - makrNm:기안자, makDeptNm:기안부서, docNo:문서번호, docSubj:문서제목, makDt:기안일시, docKeepPrdNm:보존기간, docTypNm:문서구분, seculNm:보안등급, refDocNm:참조문서, opin:의견, relDoc:관련문서, attFile:첨부파일, enfcScopNm:시행범위, enfcDt:시행일시, enfcDocKeepPrdNm:시행보존기간, recvDeptRefNm:수신처참조, recvDeptNm:수신처 */
	private String itemId;

	/** 양식ID - KEY */
	public String getFormId() {
		return formId;
	}

	/** 양식ID - KEY */
	public void setFormId(String formId) {
		this.formId = formId;
	}

	/** 양식구성일련번호 - KEY */
	public String getFormCombSeq() {
		return formCombSeq;
	}

	/** 양식구성일련번호 - KEY */
	public void setFormCombSeq(String formCombSeq) {
		this.formCombSeq = formCombSeq;
	}

	/** 줄번호 - KEY */
	public String getRowNo() {
		return rowNo;
	}

	/** 줄번호 - KEY */
	public void setRowNo(String rowNo) {
		this.rowNo = rowNo;
	}

	/** 열번호 - KEY */
	public String getColNo() {
		return colNo;
	}

	/** 열번호 - KEY */
	public void setColNo(String colNo) {
		this.colNo = colNo;
	}

	/** 콜스판값 */
	public String getCspnVa() {
		return cspnVa;
	}

	/** 콜스판값 */
	public void setCspnVa(String cspnVa) {
		this.cspnVa = cspnVa;
	}

	/** 항목ID - makrNm:기안자, makDeptNm:기안부서, docNo:문서번호, docSubj:문서제목, makDt:기안일시, docKeepPrdNm:보존기간, docTypNm:문서구분, seculNm:보안등급, refDocNm:참조문서, opin:의견, relDoc:관련문서, attFile:첨부파일, enfcScopNm:시행범위, enfcDt:시행일시, enfcDocKeepPrdNm:시행보존기간, recvDeptRefNm:수신처참조, recvDeptNm:수신처 */
	public String getItemId() {
		return itemId;
	}

	/** 항목ID - makrNm:기안자, makDeptNm:기안부서, docNo:문서번호, docSubj:문서제목, makDt:기안일시, docKeepPrdNm:보존기간, docTypNm:문서구분, seculNm:보안등급, refDocNm:참조문서, opin:의견, relDoc:관련문서, attFile:첨부파일, enfcScopNm:시행범위, enfcDt:시행일시, enfcDocKeepPrdNm:시행보존기간, recvDeptRefNm:수신처참조, recvDeptNm:수신처 */
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApFormItemLDao.selectApFormItemL";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApFormItemLDao.insertApFormItemL";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.ap.dao.ApFormItemLDao.updateApFormItemL";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.ap.dao.ApFormItemLDao.deleteApFormItemL";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApFormItemLDao.countApFormItemL";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":양식항목내역]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(formId!=null) { if(tab!=null) builder.append(tab); builder.append("formId(양식ID-PK):").append(formId).append('\n'); }
		if(formCombSeq!=null) { if(tab!=null) builder.append(tab); builder.append("formCombSeq(양식구성일련번호-PK):").append(formCombSeq).append('\n'); }
		if(rowNo!=null) { if(tab!=null) builder.append(tab); builder.append("rowNo(줄번호-PK):").append(rowNo).append('\n'); }
		if(colNo!=null) { if(tab!=null) builder.append(tab); builder.append("colNo(열번호-PK):").append(colNo).append('\n'); }
		if(cspnVa!=null) { if(tab!=null) builder.append(tab); builder.append("cspnVa(콜스판값):").append(cspnVa).append('\n'); }
		if(itemId!=null) { if(tab!=null) builder.append(tab); builder.append("itemId(항목ID):").append(itemId).append('\n'); }
		super.toString(builder, tab);
	}
}
