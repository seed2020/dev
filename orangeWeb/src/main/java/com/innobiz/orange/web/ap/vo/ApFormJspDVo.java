package com.innobiz.orange.web.ap.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 양식JSP상세(AP_FORM_JSP_D) 테이블 VO
 */
public class ApFormJspDVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 6647754314635285079L;

	/** JSP ID - KEY */
	private String jspId;

	/** 양식ID - KEY */
	private String formId;

	/** JSP 경로 */
	private String jspPath;

	/** JSP ID - KEY */
	public String getJspId() {
		return jspId;
	}

	/** JSP ID - KEY */
	public void setJspId(String jspId) {
		this.jspId = jspId;
	}

	/** 양식ID - KEY */
	public String getFormId() {
		return formId;
	}

	/** 양식ID - KEY */
	public void setFormId(String formId) {
		this.formId = formId;
	}

	/** JSP 경로 */
	public String getJspPath() {
		return jspPath;
	}

	/** JSP 경로 */
	public void setJspPath(String jspPath) {
		this.jspPath = jspPath;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApFormJspDDao.selectApFormJspD";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApFormJspDDao.insertApFormJspD";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.ap.dao.ApFormJspDDao.updateApFormJspD";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.ap.dao.ApFormJspDDao.deleteApFormJspD";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApFormJspDDao.countApFormJspD";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":양식JSP상세]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(jspId!=null) { if(tab!=null) builder.append(tab); builder.append("jspId(JSP ID-PK):").append(jspId).append('\n'); }
		if(formId!=null) { if(tab!=null) builder.append(tab); builder.append("formId(양식ID-PK):").append(formId).append('\n'); }
		if(jspPath!=null) { if(tab!=null) builder.append(tab); builder.append("jspPath(JSP 경로):").append(jspPath).append('\n'); }
		super.toString(builder, tab);
	}
}
