package com.innobiz.orange.web.wf.vo;

import com.innobiz.orange.web.cm.utils.IdUtil;
import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.wf.utils.WfConstant;

/****** Object:  Vo - Date: 2018/02/26 16:00 ******/
/**
* 양식기본(WF_FORM_B) 테이블 VO 
*/
public class WfCmWorksVo extends CommonVoImpl {

	/** serialVersionUID */
	private static final long serialVersionUID = -3178717527019808087L;

	/** 업무번호 */ 
	private String workNo;
	
	/** 양식번호 */ 
	private String formNo;
	
	/** 양식ID - 테이블명 */
	private String formId;
	
	/** 생성자 */
 	public WfCmWorksVo() {
	}
 	
	/** 생성자 */
 	public WfCmWorksVo(String formNo) {
		this.formId = IdUtil.createId(Long.parseLong(formNo), WfConstant.TBLNM_LEN);
		this.formNo = formNo;
	}
 	
 	/** 생성자 */
 	public WfCmWorksVo(String formNo, String formId) {
		this.formId = formId;
		this.formNo = formNo;
	}
 	
	public void setWorkNo(String workNo) { 
		this.workNo = workNo;
	}
	/** 업무번호 */ 
	public String getWorkNo() { 
		return workNo;
	}
	
	public void setFormNo(String formNo) { 
		this.formNo = formNo;
	}
	/** 양식번호 */ 
	public String getFormNo() { 
		return formNo;
	}
	
	/** 양식ID - 테이블명 */ 
	public String getFormId() {
		return formId;
	}
	public void setFormId(String formId) {
		this.formId = formId;
	}

	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":생성테이블공통]");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab) { 
		if(workNo!=null) { if(tab!=null) builder.append(tab); builder.append("workNo(업무번호):").append(workNo).append('\n'); }
		if(formNo!=null) { if(tab!=null) builder.append(tab); builder.append("formNo(양식번호):").append(formNo).append('\n'); }
		if(formId!=null) { if(tab!=null) builder.append(tab); builder.append("formId(양식ID):").append(formId).append('\n'); }
		super.toString(builder, tab);
	}

}
