package com.innobiz.orange.web.or.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 조직이력관계(OR_ORG_HST_R) 테이블 VO
 */
public class OrOrgHstRVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 3410522219815371680L;

	/** 조직ID - KEY */
	private String orgId;

	/** 이력조직ID - KEY */
	private String hstOrgId;

	/** 이력리소스ID */
	private String hstRescId;

	/** 정렬순서 */
	private String sortOrdr;


	// 추가컬럼
	/** 이력리소스명 */
	private String hstRescNm;

	/** 조직ID - KEY */
	public String getOrgId() {
		return orgId;
	}

	/** 조직ID - KEY */
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	/** 이력조직ID - KEY */
	public String getHstOrgId() {
		return hstOrgId;
	}

	/** 이력조직ID - KEY */
	public void setHstOrgId(String hstOrgId) {
		this.hstOrgId = hstOrgId;
	}

	/** 이력리소스ID */
	public String getHstRescId() {
		return hstRescId;
	}

	/** 이력리소스ID */
	public void setHstRescId(String hstRescId) {
		this.hstRescId = hstRescId;
	}

	/** 정렬순서 */
	public String getSortOrdr() {
		return sortOrdr;
	}

	/** 정렬순서 */
	public void setSortOrdr(String sortOrdr) {
		this.sortOrdr = sortOrdr;
	}

	/** 이력리소스명 */
	public String getHstRescNm() {
		return hstRescNm;
	}

	/** 이력리소스명 */
	public void setHstRescNm(String hstRescNm) {
		this.hstRescNm = hstRescNm;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.or.dao.OrOrgHstRDao.selectOrOrgHstR";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.or.dao.OrOrgHstRDao.insertOrOrgHstR";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.or.dao.OrOrgHstRDao.updateOrOrgHstR";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.or.dao.OrOrgHstRDao.deleteOrOrgHstR";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.or.dao.OrOrgHstRDao.countOrOrgHstR";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":조직이력관계]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(orgId!=null) { if(tab!=null) builder.append(tab); builder.append("orgId(조직ID-PK):").append(orgId).append('\n'); }
		if(hstOrgId!=null) { if(tab!=null) builder.append(tab); builder.append("hstOrgId(이력조직ID-PK):").append(hstOrgId).append('\n'); }
		if(hstRescId!=null) { if(tab!=null) builder.append(tab); builder.append("hstRescId(이력리소스ID):").append(hstRescId).append('\n'); }
		if(sortOrdr!=null) { if(tab!=null) builder.append(tab); builder.append("sortOrdr(정렬순서):").append(sortOrdr).append('\n'); }
		if(hstRescNm!=null) { if(tab!=null) builder.append(tab); builder.append("hstRescNm(이력리소스명):").append(hstRescNm).append('\n'); }
		super.toString(builder, tab);
	}
}
