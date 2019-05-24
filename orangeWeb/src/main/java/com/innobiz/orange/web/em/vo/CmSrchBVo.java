package com.innobiz.orange.web.em.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 검색기본(CM_SRCH_B) 테이블 VO
 */
public class CmSrchBVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 1073022271339467080L;

	/** 검색번호 - KEY */
	private String srchNo;

	/** 회사ID */
	private String compId;

	/** 모듈참조ID */
	private String mdRid;

	/** 모듈함ID */
	private String mdBxId;

	/** 모듈ID */
	private String mdId;

	/** 모듈함리소스ID */
	private String mdBxRescId;

	/** 테이블ID */
	private String tblId;

	/** 액션ID */
	private String actId;

	/** URL */
	private String url;

	/** 등록일시 */
	private String regDt;

	/** 검색번호 - KEY */
	public String getSrchNo() {
		return srchNo;
	}

	/** 검색번호 - KEY */
	public void setSrchNo(String srchNo) {
		this.srchNo = srchNo;
	}

	/** 회사ID */
	public String getCompId() {
		return compId;
	}

	/** 회사ID */
	public void setCompId(String compId) {
		this.compId = compId;
	}

	/** 모듈참조ID */
	public String getMdRid() {
		return mdRid;
	}

	/** 모듈참조ID */
	public void setMdRid(String mdRid) {
		this.mdRid = mdRid;
	}

	/** 모듈함ID */
	public String getMdBxId() {
		return mdBxId;
	}

	/** 모듈함ID */
	public void setMdBxId(String mdBxId) {
		this.mdBxId = mdBxId;
	}

	/** 모듈ID */
	public String getMdId() {
		return mdId;
	}

	/** 모듈ID */
	public void setMdId(String mdId) {
		this.mdId = mdId;
	}

	/** 모듈함리소스ID */
	public String getMdBxRescId() {
		return mdBxRescId;
	}

	/** 모듈함리소스ID */
	public void setMdBxRescId(String mdBxRescId) {
		this.mdBxRescId = mdBxRescId;
	}

	/** 테이블ID */
	public String getTblId() {
		return tblId;
	}

	/** 테이블ID */
	public void setTblId(String tblId) {
		this.tblId = tblId;
	}

	/** 액션ID */
	public String getActId() {
		return actId;
	}

	/** 액션ID */
	public void setActId(String actId) {
		this.actId = actId;
	}

	/** URL */
	public String getUrl() {
		return url;
	}

	/** URL */
	public void setUrl(String url) {
		this.url = url;
	}

	/** 등록일시 */
	public String getRegDt() {
		return regDt;
	}

	/** 등록일시 */
	public void setRegDt(String regDt) {
		this.regDt = regDt;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.em.dao.CmSrchBDao.selectCmSrchB";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.em.dao.CmSrchBDao.insertCmSrchB";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.em.dao.CmSrchBDao.updateCmSrchB";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.em.dao.CmSrchBDao.deleteCmSrchB";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.em.dao.CmSrchBDao.countCmSrchB";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":검색기본]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(srchNo!=null) { if(tab!=null) builder.append(tab); builder.append("srchNo(검색번호-PK):").append(srchNo).append('\n'); }
		if(compId!=null) { if(tab!=null) builder.append(tab); builder.append("compId(회사ID):").append(compId).append('\n'); }
		if(mdRid!=null) { if(tab!=null) builder.append(tab); builder.append("mdRid(모듈참조ID):").append(mdRid).append('\n'); }
		if(mdBxId!=null) { if(tab!=null) builder.append(tab); builder.append("mdBxId(모듈함ID):").append(mdBxId).append('\n'); }
		if(mdId!=null) { if(tab!=null) builder.append(tab); builder.append("mdId(모듈ID):").append(mdId).append('\n'); }
		if(mdBxRescId!=null) { if(tab!=null) builder.append(tab); builder.append("mdBxRescId(모듈함리소스ID):").append(mdBxRescId).append('\n'); }
		if(tblId!=null) { if(tab!=null) builder.append(tab); builder.append("tblId(테이블ID):").append(tblId).append('\n'); }
		if(actId!=null) { if(tab!=null) builder.append(tab); builder.append("actId(액션ID):").append(actId).append('\n'); }
		if(url!=null) { if(tab!=null) builder.append(tab); builder.append("url(URL):").append(url).append('\n'); }
		if(regDt!=null) { if(tab!=null) builder.append(tab); builder.append("regDt(등록일시):").append(regDt).append('\n'); }
		super.toString(builder, tab);
	}
}
