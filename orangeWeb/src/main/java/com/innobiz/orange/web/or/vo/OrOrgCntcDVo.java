package com.innobiz.orange.web.or.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 조직연락처상세(OR_ORG_CNTC_D) 테이블 VO
 */
public class OrOrgCntcDVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 2264374130189855639L;

	/** 조직ID - KEY */
	private String orgId;

	/** 대표이메일 */
	private String repEmail;

	/** 전화번호 */
	private String phon;

	/** 팩스번호 */
	private String fno;

	/** 우편번호 */
	private String zipNo;

	/** 주소 */
	private String adr;

	/** 상세주소 */
	private String detlAdr;

	/** 대표홈페이지URL */
	private String repHpageUrl;


	// 추가컬럼
	/** 회사ID */
	private String compId;

	/** 조직ID - KEY */
	public String getOrgId() {
		return orgId;
	}

	/** 조직ID - KEY */
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	/** 대표이메일 */
	public String getRepEmail() {
		return repEmail;
	}

	/** 대표이메일 */
	public void setRepEmail(String repEmail) {
		this.repEmail = repEmail;
	}

	/** 전화번호 */
	public String getPhon() {
		return phon;
	}

	/** 전화번호 */
	public void setPhon(String phon) {
		this.phon = phon;
	}

	/** 팩스번호 */
	public String getFno() {
		return fno;
	}

	/** 팩스번호 */
	public void setFno(String fno) {
		this.fno = fno;
	}

	/** 우편번호 */
	public String getZipNo() {
		return zipNo;
	}

	/** 우편번호 */
	public void setZipNo(String zipNo) {
		this.zipNo = zipNo;
	}

	/** 주소 */
	public String getAdr() {
		return adr;
	}

	/** 주소 */
	public void setAdr(String adr) {
		this.adr = adr;
	}

	/** 상세주소 */
	public String getDetlAdr() {
		return detlAdr;
	}

	/** 상세주소 */
	public void setDetlAdr(String detlAdr) {
		this.detlAdr = detlAdr;
	}

	/** 대표홈페이지URL */
	public String getRepHpageUrl() {
		return repHpageUrl;
	}

	/** 대표홈페이지URL */
	public void setRepHpageUrl(String repHpageUrl) {
		this.repHpageUrl = repHpageUrl;
	}

	/** 회사ID */
	public String getCompId() {
		return compId;
	}

	/** 회사ID */
	public void setCompId(String compId) {
		this.compId = compId;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.or.dao.OrOrgCntcDDao.selectOrOrgCntcD";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.or.dao.OrOrgCntcDDao.insertOrOrgCntcD";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.or.dao.OrOrgCntcDDao.updateOrOrgCntcD";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.or.dao.OrOrgCntcDDao.deleteOrOrgCntcD";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.or.dao.OrOrgCntcDDao.countOrOrgCntcD";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":조직연락처상세]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(orgId!=null) { if(tab!=null) builder.append(tab); builder.append("orgId(조직ID-PK):").append(orgId).append('\n'); }
		if(repEmail!=null) { if(tab!=null) builder.append(tab); builder.append("repEmail(대표이메일):").append(repEmail).append('\n'); }
		if(phon!=null) { if(tab!=null) builder.append(tab); builder.append("phon(전화번호):").append(phon).append('\n'); }
		if(fno!=null) { if(tab!=null) builder.append(tab); builder.append("fno(팩스번호):").append(fno).append('\n'); }
		if(zipNo!=null) { if(tab!=null) builder.append(tab); builder.append("zipNo(우편번호):").append(zipNo).append('\n'); }
		if(adr!=null) { if(tab!=null) builder.append(tab); builder.append("adr(주소):").append(adr).append('\n'); }
		if(detlAdr!=null) { if(tab!=null) builder.append(tab); builder.append("detlAdr(상세주소):").append(detlAdr).append('\n'); }
		if(repHpageUrl!=null) { if(tab!=null) builder.append(tab); builder.append("repHpageUrl(대표홈페이지URL):").append(repHpageUrl).append('\n'); }
		if(compId!=null) { if(tab!=null) builder.append(tab); builder.append("compId(회사ID):").append(compId).append('\n'); }
		super.toString(builder, tab);
	}
}
