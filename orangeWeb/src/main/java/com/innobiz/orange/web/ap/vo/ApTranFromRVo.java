package com.innobiz.orange.web.ap.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 이관양식관계(AP_TRAN_FROM_R) 테이블 VO
 */
public class ApTranFromRVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 1765142674619325695L;

	/** 이관ID - KEY */
	private String tranId;

	/** 양식ID - KEY */
	private String formId;

	/** 이관ID - KEY */
	public String getTranId() {
		return tranId;
	}

	/** 이관ID - KEY */
	public void setTranId(String tranId) {
		this.tranId = tranId;
	}

	/** 양식ID - KEY */
	public String getFormId() {
		return formId;
	}

	/** 양식ID - KEY */
	public void setFormId(String formId) {
		this.formId = formId;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApTranFromRDao.selectApTranFromR";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApTranFromRDao.insertApTranFromR";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.ap.dao.ApTranFromRDao.updateApTranFromR";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.ap.dao.ApTranFromRDao.deleteApTranFromR";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApTranFromRDao.countApTranFromR";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":이관양식관계]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(tranId!=null) { if(tab!=null) builder.append(tab); builder.append("tranId(이관ID-PK):").append(tranId).append('\n'); }
		if(formId!=null) { if(tab!=null) builder.append(tab); builder.append("formId(양식ID-PK):").append(formId).append('\n'); }
		super.toString(builder, tab);
	}
}
