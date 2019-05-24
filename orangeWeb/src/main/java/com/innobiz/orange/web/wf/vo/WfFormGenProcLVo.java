package com.innobiz.orange.web.wf.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/****** Object:  Vo - Date: 2018/03/16 17:04 ******/
/**
* 폼생성진행내역(WF_FORM_GEN_PROC_L) 테이블 VO 
*/
public class WfFormGenProcLVo extends CommonVoImpl {
	
	/** serialVersionUID */
	private static final long serialVersionUID = -5854275162525401206L;

	/** 생성ID */ 
	private String genId;

 	/** 양식번호 */ 
	private String formNo;

 	/** 순번 */ 
	private String seq;
	
	/** 진행내용 */ 
	private String procCont;
	
	/** 전체건수 */ 
	private String allCnt;

 	/** 진행건수 */ 
	private String procCnt;

 	/** 시작일시 */ 
	private String strtDt;

 	/** 종료일시 */ 
	private String endDt;

 	/** 오류내용 */ 
	private String errCont;

 	/** 오류여부 */ 
	private String errYn;
	
	/** 추가 */
	/** 시작번호 */
	private String strtSeq;

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

	public void setSeq(String seq) { 
		this.seq = seq;
	}
	/** 순번 */ 
	public String getSeq() { 
		return seq;
	}
	
	public void setProcCont(String procCont) { 
		this.procCont = procCont;
	}
	/** 진행내용 */ 
	public String getProcCont() { 
		return procCont;
	}
	
	public void setAllCnt(String allCnt) { 
		this.allCnt = allCnt;
	}
	/** 전체건수 */ 
	public String getAllCnt() { 
		return allCnt;
	}

	public void setProcCnt(String procCnt) { 
		this.procCnt = procCnt;
	}
	/** 진행건수 */ 
	public String getProcCnt() { 
		return procCnt;
	}
	
	public void setStrtDt(String strtDt) { 
		this.strtDt = strtDt;
	}
	/** 시작일시 */ 
	public String getStrtDt() { 
		return strtDt;
	}

	public void setEndDt(String endDt) { 
		this.endDt = endDt;
	}
	/** 종료일시 */ 
	public String getEndDt() { 
		return endDt;
	}

	public void setErrCont(String errCont) { 
		this.errCont = errCont;
	}
	/** 오류내용 */ 
	public String getErrCont() { 
		return errCont;
	}

	public void setErrYn(String errYn) { 
		this.errYn = errYn;
	}
	/** 오류여부 */ 
	public String getErrYn() { 
		return errYn;
	}
	
	/** 시작번호 */
	public String getStrtSeq() {
		return strtSeq;
	}
	public void setStrtSeq(String strtSeq) {
		this.strtSeq = strtSeq;
	}
	
	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.wf.dao.WfFormGenProcLDao.selectWfFormGenProcL";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.wf.dao.WfFormGenProcLDao.insertWfFormGenProcL";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.wf.dao.WfFormGenProcLDao.updateWfFormGenProcL";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.wf.dao.WfFormGenProcLDao.deleteWfFormGenProcL";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.wf.dao.WfFormGenProcLDao.countWfFormGenProcL";
		}
		return null;
	}

	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":폼생성진행내역]");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab) { 
		if(genId!=null) { if(tab!=null) builder.append(tab); builder.append("genId(생성ID):").append(genId).append('\n'); }
		if(formNo!=null) { if(tab!=null) builder.append(tab); builder.append("formNo(양식번호):").append(formNo).append('\n'); }
		if(seq!=null) { if(tab!=null) builder.append(tab); builder.append("seq(순번):").append(seq).append('\n'); }
		if(procCont!=null) { if(tab!=null) builder.append(tab); builder.append("procCont(진행내용):").append(procCont).append('\n'); }
		if(allCnt!=null) { if(tab!=null) builder.append(tab); builder.append("allCnt(전체건수):").append(allCnt).append('\n'); }
		if(procCnt!=null) { if(tab!=null) builder.append(tab); builder.append("procCnt(진행건수):").append(procCnt).append('\n'); }		
		if(strtDt!=null) { if(tab!=null) builder.append(tab); builder.append("strtDt(시작일시):").append(strtDt).append('\n'); }
		if(endDt!=null) { if(tab!=null) builder.append(tab); builder.append("endDt(종료일시):").append(endDt).append('\n'); }
		if(errCont!=null) { if(tab!=null) builder.append(tab); builder.append("errCont(오류내용):").append(errCont).append('\n'); }
		if(errYn!=null) { if(tab!=null) builder.append(tab); builder.append("errYn(오류여부):").append(errYn).append('\n'); }
		super.toString(builder, tab);
	}

}
