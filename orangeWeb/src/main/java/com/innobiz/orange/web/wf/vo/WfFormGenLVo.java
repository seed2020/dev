package com.innobiz.orange.web.wf.vo;

import java.util.List;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/****** Object:  Vo - Date: 2018/03/16 16:02 ******/
/**
* 폼생성목록(WF_FORM_GEN_L) 테이블 VO 
*/
public class WfFormGenLVo extends CommonVoImpl {
	
	/** serialVersionUID */
	private static final long serialVersionUID = 4156877420845415692L;

	/** 생성ID */ 
	private String genId;

 	/** 양식번호 */ 
	private String formNo;

 	/** 회사ID */ 
	private String compId;

 	/** 양식리소스ID */ 
	private String formRescId;

	/** 진행상태코드 */ 
	private String procStatCd;
	
 	/** 등록일시 */ 
	private String regDt;

	/** 추가 */
	/** 양식리소스명 */ 
	private String formRescNm;
	
	/** 양식ID - 테이블명*/
	private String formId;
	
	/** SQL일련번호 */
	private Integer sqlSeq;
	
	/** 컬럼목록 */
	private List<WfFormColmLVo> colmVoList;
	
	/** 컬럼명 목록 */
	//private List<String> colmNmList;
	
	/** 컬럼명 조합목록[insert select] */
	//private List<String> mergeColmNmList;
	
	/** 컬럼조합목록[insert select] */
	private List<WfFormColmLVo> mergeColmVoList;
	
	/** 양식 생성 진행내역*/
	private List<WfFormGenProcLVo> wfFormGenProcLVoList;
	
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

	public void setCompId(String compId) { 
		this.compId = compId;
	}
	/** 회사ID */ 
	public String getCompId() { 
		return compId;
	}

	public void setFormRescId(String formRescId) { 
		this.formRescId = formRescId;
	}
	/** 양식리소스ID */ 
	public String getFormRescId() { 
		return formRescId;
	}
	
	public void setProcStatCd(String procStatCd) { 
		this.procStatCd = procStatCd;
	}
	/** 진행상태코드 */ 
	public String getProcStatCd() { 
		return procStatCd;
	}
	
	public void setRegDt(String regDt) { 
		this.regDt = regDt;
	}
	/** 등록일시 */ 
	public String getRegDt() { 
		return regDt;
	}
	
	/** 양식리소스명 */ 
	public String getFormRescNm() {
		return formRescNm;
	}
	public void setFormRescNm(String formRescNm) {
		this.formRescNm = formRescNm;
	}
	
	/** 양식ID - 테이블명*/
	public String getFormId() {
		return formId;
	}
	public void setFormId(String formId) {
		this.formId = formId;
	}
	
	/** SQL일련번호 */
	public Integer getSqlSeq() {
		return sqlSeq;
	}
	public void setSqlSeq(Integer sqlSeq) {
		this.sqlSeq = sqlSeq;
	}
	
	/** 컬럼목록 */
	public List<WfFormColmLVo> getColmVoList() {
		return colmVoList;
	}
	public void setColmVoList(List<WfFormColmLVo> colmVoList) {
		this.colmVoList = colmVoList;
	}
	
	/** 컬럼조합목록[insert select] */
	public List<WfFormColmLVo> getMergeColmVoList() {
		return mergeColmVoList;
	}
	public void setMergeColmVoList(List<WfFormColmLVo> mergeColmVoList) {
		this.mergeColmVoList = mergeColmVoList;
	}
	
	/** 양식 생성 진행내역*/
	public List<WfFormGenProcLVo> getWfFormGenProcLVoList() {
		return wfFormGenProcLVoList;
	}
	public void setWfFormGenProcLVoList(List<WfFormGenProcLVo> wfFormGenProcLVoList) {
		this.wfFormGenProcLVoList = wfFormGenProcLVoList;
	}
	
	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.wf.dao.WfFormGenLDao.selectWfFormGenL";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.wf.dao.WfFormGenLDao.insertWfFormGenL";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.wf.dao.WfFormGenLDao.updateWfFormGenL";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.wf.dao.WfFormGenLDao.deleteWfFormGenL";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.wf.dao.WfFormGenLDao.countWfFormGenL";
		}
		return null;
	}

	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":폼생성목록]");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab) { 
		if(genId!=null) { if(tab!=null) builder.append(tab); builder.append("genId(생성ID):").append(genId).append('\n'); }
		if(formNo!=null) { if(tab!=null) builder.append(tab); builder.append("formNo(양식번호):").append(formNo).append('\n'); }
		if(compId!=null) { if(tab!=null) builder.append(tab); builder.append("compId(회사ID):").append(compId).append('\n'); }
		if(formRescId!=null) { if(tab!=null) builder.append(tab); builder.append("formRescId(양식리소스ID):").append(formRescId).append('\n'); }
		if(procStatCd!=null) { if(tab!=null) builder.append(tab); builder.append("procStatCd(진행상태코드):").append(procStatCd).append('\n'); }
		if(regDt!=null) { if(tab!=null) builder.append(tab); builder.append("regDt(등록일시):").append(regDt).append('\n'); }
		super.toString(builder, tab);
	}

}
