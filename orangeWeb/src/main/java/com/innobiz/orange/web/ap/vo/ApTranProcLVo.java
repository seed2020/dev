package com.innobiz.orange.web.ap.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 이관진행내역(AP_TRAN_PROC_L) 테이블 VO
 */
public class ApTranProcLVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 4224241412146772710L;

	/** 이관ID - KEY */
	private String tranId;

	/** 순번 - KEY */
	private String seq;

	/** 이관내용 */
	private String tranCont;

	/** 전체건수 */
	private String allCnt;

	/** 진행건수 */
	private String procCnt;

	/** 시작일시 */
	private String strtDt;

	/** 종류일시 */
	private String endDt;

	/** 오류내용 */
	private String errCont;

	/** 오류여부 */
	private String errYn;


	// 추가컬럼
	/** 시작번호 */
	private String strtSeq;

	/** 이관ID - KEY */
	public String getTranId() {
		return tranId;
	}

	/** 이관ID - KEY */
	public void setTranId(String tranId) {
		this.tranId = tranId;
	}

	/** 순번 - KEY */
	public String getSeq() {
		return seq;
	}

	/** 순번 - KEY */
	public void setSeq(String seq) {
		this.seq = seq;
	}

	/** 이관내용 */
	public String getTranCont() {
		return tranCont;
	}

	/** 이관내용 */
	public void setTranCont(String tranCont) {
		this.tranCont = tranCont;
	}

	/** 전체건수 */
	public String getAllCnt() {
		return allCnt;
	}

	/** 전체건수 */
	public void setAllCnt(String allCnt) {
		this.allCnt = allCnt;
	}

	/** 진행건수 */
	public String getProcCnt() {
		return procCnt;
	}

	/** 진행건수 */
	public void setProcCnt(String procCnt) {
		this.procCnt = procCnt;
	}

	/** 시작일시 */
	public String getStrtDt() {
		return strtDt;
	}

	/** 시작일시 */
	public void setStrtDt(String strtDt) {
		this.strtDt = strtDt;
	}

	/** 종류일시 */
	public String getEndDt() {
		return endDt;
	}

	/** 종류일시 */
	public void setEndDt(String endDt) {
		this.endDt = endDt;
	}

	/** 오류내용 */
	public String getErrCont() {
		return errCont;
	}

	/** 오류내용 */
	public void setErrCont(String errCont) {
		this.errCont = errCont;
	}

	/** 오류여부 */
	public String getErrYn() {
		return errYn;
	}

	/** 오류여부 */
	public void setErrYn(String errYn) {
		this.errYn = errYn;
	}

	/** 시작번호 */
	public String getStrtSeq() {
		return strtSeq;
	}

	/** 시작번호 */
	public void setStrtSeq(String strtSeq) {
		this.strtSeq = strtSeq;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApTranProcLDao.selectApTranProcL";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApTranProcLDao.insertApTranProcL";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.ap.dao.ApTranProcLDao.updateApTranProcL";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.ap.dao.ApTranProcLDao.deleteApTranProcL";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApTranProcLDao.countApTranProcL";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":이관진행내역]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(tranId!=null) { if(tab!=null) builder.append(tab); builder.append("tranId(이관ID-PK):").append(tranId).append('\n'); }
		if(seq!=null) { if(tab!=null) builder.append(tab); builder.append("seq(순번-PK):").append(seq).append('\n'); }
		if(tranCont!=null) { if(tab!=null) builder.append(tab); builder.append("tranCont(이관내용):").append(tranCont).append('\n'); }
		if(allCnt!=null) { if(tab!=null) builder.append(tab); builder.append("allCnt(전체건수):").append(allCnt).append('\n'); }
		if(procCnt!=null) { if(tab!=null) builder.append(tab); builder.append("procCnt(진행건수):").append(procCnt).append('\n'); }
		if(strtDt!=null) { if(tab!=null) builder.append(tab); builder.append("strtDt(시작일시):").append(strtDt).append('\n'); }
		if(endDt!=null) { if(tab!=null) builder.append(tab); builder.append("endDt(종류일시):").append(endDt).append('\n'); }
		if(errCont!=null) { if(tab!=null) builder.append(tab); builder.append("errCont(오류내용):").append(errCont).append('\n'); }
		if(errYn!=null) { if(tab!=null) builder.append(tab); builder.append("errYn(오류여부):").append(errYn).append('\n'); }
		if(strtSeq!=null) { if(tab!=null) builder.append(tab); builder.append("strtSeq(시작번호):").append(strtSeq).append('\n'); }
		super.toString(builder, tab);
	}
}
