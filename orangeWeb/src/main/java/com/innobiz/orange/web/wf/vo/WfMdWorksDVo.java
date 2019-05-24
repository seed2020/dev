package com.innobiz.orange.web.wf.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/****** Object:  Vo - Date: 2018/07/23 14:23 ******/
/**
* 모듈업무상세(WF_MD_WORKS_D) 테이블 VO 
*/
public class WfMdWorksDVo extends CommonVoImpl {
	
	/** serialVersionUID */
	private static final long serialVersionUID = -2287148875585377374L;

	/** 모듈코드 */ 
	private String mdCd;

 	/** 모듈참조ID */ 
	private String mdRefId;

 	/** 모듈번호 */ 
	private String mdNo;

 	/** 양식번호 */ 
	private String formNo;

 	/** 생성ID */ 
	private String genId;

 	/** 업무번호 */ 
	private String workNo;

	public void setMdCd(String mdCd) { 
		this.mdCd = mdCd;
	}
	/** 모듈코드 */ 
	public String getMdCd() { 
		return mdCd;
	}

	public void setMdRefId(String mdRefId) { 
		this.mdRefId = mdRefId;
	}
	/** 모듈참조ID */ 
	public String getMdRefId() { 
		return mdRefId;
	}

	public void setMdNo(String mdNo) { 
		this.mdNo = mdNo;
	}
	/** 모듈번호 */ 
	public String getMdNo() { 
		return mdNo;
	}

	public void setFormNo(String formNo) { 
		this.formNo = formNo;
	}
	/** 양식번호 */ 
	public String getFormNo() { 
		return formNo;
	}

	public void setGenId(String genId) { 
		this.genId = genId;
	}
	/** 생성ID */ 
	public String getGenId() { 
		return genId;
	}

	public void setWorkNo(String workNo) { 
		this.workNo = workNo;
	}
	/** 업무번호 */ 
	public String getWorkNo() { 
		return workNo;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.wf.dao.WfMdWorksDDao.selectWfMdWorksD";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.wf.dao.WfMdWorksDDao.insertWfMdWorksD";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.wf.dao.WfMdWorksDDao.updateWfMdWorksD";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.wf.dao.WfMdWorksDDao.deleteWfMdWorksD";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.wf.dao.WfMdWorksDDao.countWfMdWorksD";
		}
		return null;
	}

	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":모듈업무상세]");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab) { 
		if(mdCd!=null) { if(tab!=null) builder.append(tab); builder.append("mdCd(모듈코드):").append(mdCd).append('\n'); }
		if(mdRefId!=null) { if(tab!=null) builder.append(tab); builder.append("mdRefId(모듈참조ID):").append(mdRefId).append('\n'); }
		if(mdNo!=null) { if(tab!=null) builder.append(tab); builder.append("mdNo(모듈번호):").append(mdNo).append('\n'); }
		if(formNo!=null) { if(tab!=null) builder.append(tab); builder.append("formNo(양식번호):").append(formNo).append('\n'); }
		if(genId!=null) { if(tab!=null) builder.append(tab); builder.append("genId(생성ID):").append(genId).append('\n'); }
		if(workNo!=null) { if(tab!=null) builder.append(tab); builder.append("workNo(업무번호):").append(workNo).append('\n'); }
		super.toString(builder, tab);
	}

}
