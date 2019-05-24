package com.innobiz.orange.web.wb.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/** 명함연락처상세 */
@SuppressWarnings("serial")
public class WbBcCntcDVo extends CommonVoImpl {
	/** 명함ID */ 
	private String bcId;

 	/** 명함연락처상세ID */ 
	private Integer bcCntcSeq;

 	/** 연락처구분코드 */ 
	private String cntcTypCd;

 	/** 연락처내용 */ 
	private String cntcCont;
	
	/** 기본연락처 여부 */ 
	private String dftCntcYn;
	
	/** 연락처분류코드 */
	private String cntcClsCd;
	
	/** 연락처구분별 일련번호 */
	private String cntcTypNo;
	
	/** 연락처구분별 건수*/
	private int cntcTypCnt;

 	public void setBcId(String bcId) { 
		this.bcId = bcId;
	}
	/** 명함ID */ 
	public String getBcId() { 
		return bcId;
	}

	public Integer getBcCntcSeq() {
		return bcCntcSeq;
	}
	public void setBcCntcSeq(Integer bcCntcSeq) {
		this.bcCntcSeq = bcCntcSeq;
	}
	public void setCntcTypCd(String cntcTypCd) { 
		this.cntcTypCd = cntcTypCd;
	}
	/** 연락처구분코드 */ 
	public String getCntcTypCd() { 
		return cntcTypCd;
	}

	public void setCntcCont(String cntcCont) { 
		this.cntcCont = cntcCont;
	}
	/** 연락처내용 */ 
	public String getCntcCont() { 
		return cntcCont;
	}
	public String getDftCntcYn() {
		return dftCntcYn;
	}
	public void setDftCntcYn(String dftCntcYn) {
		this.dftCntcYn = dftCntcYn;
	}
	
	public int getCntcTypCnt() {
		return cntcTypCnt;
	}
	public void setCntcTypCnt(int cntcTypCnt) {
		this.cntcTypCnt = cntcTypCnt;
	}
	
	public String getCntcTypNo() {
		return cntcTypNo;
	}
	public void setCntcTypNo(String cntcTypNo) {
		this.cntcTypNo = cntcTypNo;
	}
	
	public String getCntcClsCd() {
		return cntcClsCd;
	}
	public void setCntcClsCd(String cntcClsCd) {
		this.cntcClsCd = cntcClsCd;
	}
	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.wb.dao.WbBcCntcDDao.selectWbBcCntcD";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.wb.dao.WbBcCntcDDao.insertWbBcCntcD";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.wb.dao.WbBcCntcDDao.updateWbBcCntcD";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.wb.dao.WbBcCntcDDao.deleteWbBcCntcD";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.wb.dao.WbBcCntcDDao.countWbBcCntcD";
		}
		return null;
	}
	
	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":명함연락처상세]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab){
		if(bcId!=null) { if(tab!=null) builder.append(tab); builder.append("bcId(명함ID):").append(bcId).append('\n'); }
		if(bcCntcSeq!=null) { if(tab!=null) builder.append(tab); builder.append("bcCntcSeq(명함연락처상세ID):").append(bcCntcSeq).append('\n'); }
		if(cntcTypCd!=null) { if(tab!=null) builder.append(tab); builder.append("cntcTypCd(연락처구분코드):").append(cntcTypCd).append('\n'); }
		if(cntcCont!=null) { if(tab!=null) builder.append(tab); builder.append("cntcCont(연락처내용):").append(cntcCont).append('\n'); }
		if(dftCntcYn!=null) { if(tab!=null) builder.append(tab); builder.append("dftCntcYn(기본연락처 여부):").append(dftCntcYn).append('\n'); }
		if(cntcClsCd!=null) { if(tab!=null) builder.append(tab); builder.append("cntcClsCd(연락처분류코드):").append(cntcClsCd).append('\n'); }
		if(cntcTypNo!=null) { if(tab!=null) builder.append(tab); builder.append("cntcTypNo(연락처구분별 일련번호):").append(cntcTypNo).append('\n'); }
		if(cntcTypCnt!=0) { if(tab!=null) builder.append(tab); builder.append("cntcTypCnt(연락처구분별 건수):").append(cntcTypCnt).append('\n'); }
		super.toString(builder, tab);
	}

}