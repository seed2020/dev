package com.innobiz.orange.web.ap.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

import java.util.List;

/**
 * 양식항목상세(AP_FORM_ITEM_D) 테이블 VO
 */
public class ApFormItemDVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 3876414064962456851L;

	/** 양식ID - KEY */
	private String formId;

	/** 양식구성일련번호 - KEY */
	private String formCombSeq;

	/** 줄수 */
	private String rowCnt;

	/** 열수 */
	private String colCnt;


	// 추가컬럼
	/** 자식(양식항목내역) VO 목록 */
	private List<ApFormItemLVo> childList;

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

	/** 줄수 */
	public String getRowCnt() {
		return rowCnt;
	}

	/** 줄수 */
	public void setRowCnt(String rowCnt) {
		this.rowCnt = rowCnt;
	}

	/** 열수 */
	public String getColCnt() {
		return colCnt;
	}

	/** 열수 */
	public void setColCnt(String colCnt) {
		this.colCnt = colCnt;
	}

	/** 자식(양식항목내역) VO 목록 */
	public List<ApFormItemLVo> getChildList() {
		return childList;
	}

	/** 자식(양식항목내역) VO 목록 */
	public void setChildList(List<ApFormItemLVo> childList) {
		this.childList = childList;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApFormItemDDao.selectApFormItemD";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApFormItemDDao.insertApFormItemD";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.ap.dao.ApFormItemDDao.updateApFormItemD";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.ap.dao.ApFormItemDDao.deleteApFormItemD";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApFormItemDDao.countApFormItemD";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":양식항목상세]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(formId!=null) { if(tab!=null) builder.append(tab); builder.append("formId(양식ID-PK):").append(formId).append('\n'); }
		if(formCombSeq!=null) { if(tab!=null) builder.append(tab); builder.append("formCombSeq(양식구성일련번호-PK):").append(formCombSeq).append('\n'); }
		if(rowCnt!=null) { if(tab!=null) builder.append(tab); builder.append("rowCnt(줄수):").append(rowCnt).append('\n'); }
		if(colCnt!=null) { if(tab!=null) builder.append(tab); builder.append("colCnt(열수):").append(colCnt).append('\n'); }
		if(childList!=null) { if(tab!=null) builder.append(tab); builder.append("childList(자식(양식항목내역) VO 목록):"); appendVoListTo(builder, childList, tab); builder.append('\n'); }
		super.toString(builder, tab);
	}
}
