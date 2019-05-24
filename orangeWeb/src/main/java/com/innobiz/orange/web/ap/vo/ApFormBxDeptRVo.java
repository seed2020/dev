package com.innobiz.orange.web.ap.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 양식함부서관계(AP_FORM_BX_DEPT_R) 테이블 VO
 */
public class ApFormBxDeptRVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 3621269494474842847L;

	/** 양식함ID - KEY */
	private String formBxId;

	/** 양식부서ID - KEY */
	private String formBxDeptId;

	/** 정렬순서 */
	private String sortOrdr;

	/** 양식함ID - KEY */
	public String getFormBxId() {
		return formBxId;
	}

	/** 양식함ID - KEY */
	public void setFormBxId(String formBxId) {
		this.formBxId = formBxId;
	}

	/** 양식부서ID - KEY */
	public String getFormBxDeptId() {
		return formBxDeptId;
	}

	/** 양식부서ID - KEY */
	public void setFormBxDeptId(String formBxDeptId) {
		this.formBxDeptId = formBxDeptId;
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
			return "com.innobiz.orange.web.ap.dao.ApFormBxDeptRDao.selectApFormBxDeptR";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApFormBxDeptRDao.insertApFormBxDeptR";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.ap.dao.ApFormBxDeptRDao.updateApFormBxDeptR";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.ap.dao.ApFormBxDeptRDao.deleteApFormBxDeptR";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApFormBxDeptRDao.countApFormBxDeptR";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":양식함부서관계]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(formBxId!=null) { if(tab!=null) builder.append(tab); builder.append("formBxId(양식함ID-PK):").append(formBxId).append('\n'); }
		if(formBxDeptId!=null) { if(tab!=null) builder.append(tab); builder.append("formBxDeptId(양식부서ID-PK):").append(formBxDeptId).append('\n'); }
		if(sortOrdr!=null) { if(tab!=null) builder.append(tab); builder.append("sortOrdr(정렬순서):").append(sortOrdr).append('\n'); }
		super.toString(builder, tab);
	}
}
