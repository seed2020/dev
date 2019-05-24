package com.innobiz.orange.web.ap.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 진행양식XML상세(AP_ONGO_FORM_XML_D) 테이블 VO
 */
public class ApOngoFormXmlDVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 3408904971970563287L;

	/** 양식ID - KEY */
	private String formId;

	/** 양식XML코드 - KEY */
	private String formXmlCd;

	/** 양식XML내용 */
	private String formXmlCont;

	/** 양식ID - KEY */
	public String getFormId() {
		return formId;
	}

	/** 양식ID - KEY */
	public void setFormId(String formId) {
		this.formId = formId;
	}

	/** 양식XML코드 - KEY */
	public String getFormXmlCd() {
		return formXmlCd;
	}

	/** 양식XML코드 - KEY */
	public void setFormXmlCd(String formXmlCd) {
		this.formXmlCd = formXmlCd;
	}

	/** 양식XML내용 */
	public String getFormXmlCont() {
		return formXmlCont;
	}

	/** 양식XML내용 */
	public void setFormXmlCont(String formXmlCont) {
		this.formXmlCont = formXmlCont;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApOngoFormXmlDDao.selectApOngoFormXmlD";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApOngoFormXmlDDao.insertApOngoFormXmlD";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.ap.dao.ApOngoFormXmlDDao.updateApOngoFormXmlD";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.ap.dao.ApOngoFormXmlDDao.deleteApOngoFormXmlD";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApOngoFormXmlDDao.countApOngoFormXmlD";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":진행양식XML상세]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(formId!=null) { if(tab!=null) builder.append(tab); builder.append("formId(양식ID-PK):").append(formId).append('\n'); }
		if(formXmlCd!=null) { if(tab!=null) builder.append(tab); builder.append("formXmlCd(양식XML코드-PK):").append(formXmlCd).append('\n'); }
		if(formXmlCont!=null) { if(tab!=null) builder.append(tab); builder.append("formXmlCont(양식XML내용):").append(formXmlCont).append('\n'); }
		super.toString(builder, tab);
	}
}
