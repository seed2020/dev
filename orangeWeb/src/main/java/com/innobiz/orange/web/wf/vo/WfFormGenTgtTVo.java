package com.innobiz.orange.web.wf.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/****** Object:  Vo - Date: 2018/07/11 11:59 ******/
/**
* 양식생성대상임시(WF_FORM_GEN_TGT_T) 테이블 VO 
*/
public class WfFormGenTgtTVo extends CommonVoImpl {
	
	/** serialVersionUID */
	private static final long serialVersionUID = -6605513748774530681L;

	/** 생성ID */ 
	private String genId;

 	/** 양식번호 */ 
	private String formNo;

 	/** 일련번호 */ 
	private Integer seq;

 	/** 업무번호 */ 
	private Integer workNo;

 	public void setGenId(String genId) { 
		this.genId = genId;
	}
	/** 생성ID */ 
	public String getGenId() { 
		return genId;
	}

	public void setFormNo(String formNo) { 
		this.formNo = formNo;
	}
	/** 양식번호 */ 
	public String getFormNo() { 
		return formNo;
	}

	public void setSeq(Integer seq) { 
		this.seq = seq;
	}
	/** 일련번호 */ 
	public Integer getSeq() { 
		return seq;
	}

	public void setWorkNo(Integer workNo) { 
		this.workNo = workNo;
	}
	/** 업무번호 */ 
	public Integer getWorkNo() { 
		return workNo;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.wf.dao.WfFormGenTgtTDao.selectWfFormGenTgtT";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.wf.dao.WfFormGenTgtTDao.insertWfFormGenTgtT";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.wf.dao.WfFormGenTgtTDao.updateWfFormGenTgtT";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.wf.dao.WfFormGenTgtTDao.deleteWfFormGenTgtT";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.wf.dao.WfFormGenTgtTDao.countWfFormGenTgtT";
		}
		return null;
	}

	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":양식생성대상임시]");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab) { 
		if(genId!=null) { if(tab!=null) builder.append(tab); builder.append("genId(생성ID):").append(genId).append('\n'); }
		if(formNo!=null) { if(tab!=null) builder.append(tab); builder.append("formNo(양식번호):").append(formNo).append('\n'); }
		if(seq!=null) { if(tab!=null) builder.append(tab); builder.append("seq(일련번호):").append(seq).append('\n'); }
		if(workNo!=null) { if(tab!=null) builder.append(tab); builder.append("workNo(업무번호):").append(workNo).append('\n'); }
		super.toString(builder, tab);
	}

}
